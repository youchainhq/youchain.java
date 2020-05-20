package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * you_sendTransaction.
 */
public class YOUSendTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
