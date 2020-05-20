package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

public class YOUCreateValidator extends Response<String> {

    public String getCreateValidatorData() {
        return getResult();
    }
}
