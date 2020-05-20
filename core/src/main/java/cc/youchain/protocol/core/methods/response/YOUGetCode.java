package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * you_getCode.
 */
public class YOUGetCode extends Response<String> {
    public String getCode() {
        return getResult();
    }
}
