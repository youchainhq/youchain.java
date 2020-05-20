package cc.youchain.protocol.core.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.methods.response.YOUFilter;
import cc.youchain.protocol.core.methods.response.YOULog;
import cc.youchain.protocol.core.methods.response.YOUUninstallFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.youchain.protocol.core.Request;
import cc.youchain.protocol.core.Response;
import cc.youchain.protocol.core.Response.Error;
import cc.youchain.protocol.core.RpcErrors;


/**
 * Class for creating managed filter requests with callbacks.
 */
public abstract class Filter<T> {

    private static final Logger log = LoggerFactory.getLogger(Filter.class);

    final YOUChain youChain;
    final Callback<T> callback;

    private volatile BigInteger filterId;

    private ScheduledFuture<?> schedule;
    
    private ScheduledExecutorService scheduledExecutorService;

    private long blockTime;

    public Filter(YOUChain youChain, Callback<T> callback) {
        this.youChain = youChain;
        this.callback = callback;
    }

    public void run(ScheduledExecutorService scheduledExecutorService, long blockTime) {
        try {
            YOUFilter youFilter = sendRequest();
            if (youFilter.hasError()) {
                throwException(youFilter.getError());
            }

            filterId = youFilter.getFilterId();
            this.scheduledExecutorService = scheduledExecutorService;
            this.blockTime = blockTime;
            // this runs in the caller thread as if any exceptions are encountered, we shouldn't
            // proceed with creating the scheduled task below
            getInitialFilterLogs();

            schedule = scheduledExecutorService.scheduleAtFixedRate(
                    () -> {
                        try {
                            this.pollFilter(youFilter);
                        } catch (Throwable e) {
                            // All exceptions must be caught, otherwise our job terminates without
                            // any notification
                            log.error("Error sending request", e);
                        }
                    },
                    0, blockTime, TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            throwException(e);
        }
    }

    private void getInitialFilterLogs() {
        try {
            Optional<Request<?, YOULog>> maybeRequest = this.getFilterLogs(this.filterId);
            YOULog youLog = null;
            if (maybeRequest.isPresent()) {
                youLog = maybeRequest.get().send();
            } else {
                youLog = new YOULog();
                youLog.setResult(Collections.emptyList());
            }
            process(youLog.getLogs());

        } catch (IOException e) {
            throwException(e);
        }
    }

    private void pollFilter(YOUFilter youFilter) {
        YOULog youLog = null;
        try {
            youLog = youChain.youGetFilterChanges(filterId).send();
        } catch (IOException e) {
            throwException(e);
        }
        if (youLog.hasError()) {
            Error error = youLog.getError();
            switch (error.getCode()) {
                case RpcErrors.FILTER_NOT_FOUND: reinstallFilter();
                    break;
                default: throwException(error);
                    break;
            }
        } else {
            process(youLog.getLogs());
        }
    }

    abstract YOUFilter sendRequest() throws IOException;

    abstract void process(List<YOULog.LogResult> logResults);
    
    private void reinstallFilter() {
        log.warn("The filter has not been found. Filter id: " + filterId);
        schedule.cancel(true);
        this.run(scheduledExecutorService, blockTime);
    }

    public void cancel() {
        schedule.cancel(false);

        try {
            YOUUninstallFilter youUninstallFilter = youChain.youUninstallFilter(filterId).send();
            if (youUninstallFilter.hasError()) {
                throwException(youUninstallFilter.getError());
            }

            if (!youUninstallFilter.isUninstalled()) {
                throw new FilterException("Filter with id '" + filterId + "' failed to uninstall");
            }
        } catch (IOException e) {
            throwException(e);
        }
    }

    /**
     * Retrieves historic filters for the filter with the given id.
     * Getting historic logs is not supported by all filters.
     * If not the method should return an empty YOULog object
     *
     * @param filterId Id of the filter for which the historic log should be retrieved
     * @return Historic logs, or an empty optional if the filter cannot retrieve historic logs
     */
    protected abstract Optional<Request<?, YOULog>> getFilterLogs(BigInteger filterId);

    void throwException(Response.Error error) {
        throw new FilterException("Invalid request: "
                + (error == null ? "Unknown Error" : error.getMessage()));
    }

    void throwException(Throwable cause) {
        throw new FilterException("Error sending request", cause);
    }
}
