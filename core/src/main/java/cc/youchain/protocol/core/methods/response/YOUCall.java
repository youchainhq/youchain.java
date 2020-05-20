package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * you_call.
 */
public class YOUCall extends Response<String> {
    public String getValue() {
        return getResult();
    }
}
