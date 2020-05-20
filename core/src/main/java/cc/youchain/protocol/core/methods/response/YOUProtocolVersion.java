package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * you_protocolVersion.
 */
public class YOUProtocolVersion extends Response<String> {
    public String getProtocolVersion() {
        return getResult();
    }
}
