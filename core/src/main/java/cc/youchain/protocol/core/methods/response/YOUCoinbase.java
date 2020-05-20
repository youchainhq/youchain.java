package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * you_coinbase.
 */
public class YOUCoinbase extends Response<String> {
    public String getAddress() {
        return getResult();
    }
}
