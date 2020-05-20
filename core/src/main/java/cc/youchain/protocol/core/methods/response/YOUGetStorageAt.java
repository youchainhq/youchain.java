package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * you_getStorageAt.
 */
public class YOUGetStorageAt extends Response<String> {
    public String getData() {
        return getResult();
    }
}
