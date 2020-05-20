package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * you_sendRawTransaction.
 */
public class YOUSendRawTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
