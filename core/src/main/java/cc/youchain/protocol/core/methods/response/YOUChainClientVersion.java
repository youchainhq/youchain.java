package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * youchain_clientVersion.
 */
public class YOUChainClientVersion extends Response<String> {

    public String getYOUChainClientVersion() {
        return getResult();
    }
}
