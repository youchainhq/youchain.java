package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

public class YOUUpdateValidator extends Response<String> {

    public String getUpdateValidatorData() {
        return getResult();
    }
}
