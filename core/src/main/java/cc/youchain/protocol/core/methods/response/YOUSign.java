package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * you_sign.
 */
public class YOUSign extends Response<String> {
    public String getSignature() {
        return getResult();
    }
}
