package cc.youchain.protocol.core.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.methods.response.YOUFilter;
import cc.youchain.protocol.core.methods.response.YOULog;
import cc.youchain.protocol.core.Request;

/**
 * Handler for working with block filter requests.
 */
public class BlockFilter extends Filter<String> {

    public BlockFilter(YOUChain youChain, Callback<String> callback) {
        super(youChain, callback);
    }

    @Override
    YOUFilter sendRequest() throws IOException {
        return youChain.youNewBlockFilter().send();
    }

    @Override
    void process(List<YOULog.LogResult> logResults) {
        for (YOULog.LogResult logResult : logResults) {
            if (logResult instanceof YOULog.Hash) {
                String blockHash = ((YOULog.Hash) logResult).get();
                callback.onEvent(blockHash);
            } else {
                throw new FilterException(
                        "Unexpected result type: " + logResult.get() + ", required Hash");
            }
        }
    }

    /**
     * Since the block filter does not support historic filters, the filterId is ignored
     * and an empty optional is returned.
     * @param filterId
     * Id of the filter for which the historic log should be retrieved
     * @return
     * Optional.empty()
     */
    @Override
    protected Optional<Request<?, YOULog>> getFilterLogs(BigInteger filterId) {
        return Optional.empty();
    }
}

