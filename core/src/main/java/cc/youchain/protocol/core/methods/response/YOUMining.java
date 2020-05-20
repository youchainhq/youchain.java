package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * you_mining.
 */
public class YOUMining extends Response<Boolean> {
    public boolean isMining() {
        return getResult();
    }
}
