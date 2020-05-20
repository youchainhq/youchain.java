package cc.youchain.protocol.core.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import cc.youchain.protocol.core.methods.request.YOUFilter;
import cc.youchain.protocol.core.methods.response.YOULog;
import cc.youchain.protocol.core.methods.response.Log;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.Request;

/**
 * Log filter handler.
 */
public class LogFilter extends Filter<Log> {

    private final YOUFilter youFilter;

    public LogFilter(
            YOUChain youChain, Callback<Log> callback,
            YOUFilter youFilter) {
        super(youChain, callback);
        this.youFilter = youFilter;
    }


    @Override
    cc.youchain.protocol.core.methods.response.YOUFilter sendRequest() throws IOException {
        return youChain.youNewFilter(youFilter).send();
    }

    @Override
    void process(List<YOULog.LogResult> logResults) {
        for (YOULog.LogResult logResult : logResults) {
            if (logResult instanceof YOULog.LogObject) {
                Log log = ((YOULog.LogObject) logResult).get();
                callback.onEvent(log);
            } else {
                throw new FilterException(
                        "Unexpected result type: " + logResult.get() + " required LogObject");
            }
        }
    }

    @Override
    protected Optional<Request<?, YOULog>> getFilterLogs(BigInteger filterId) {
        return Optional.of(youChain.youGetFilterLogs(filterId));
    }
}
