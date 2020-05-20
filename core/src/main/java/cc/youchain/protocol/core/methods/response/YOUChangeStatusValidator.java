package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

public class YOUChangeStatusValidator extends Response<String> {

    public String getChangeStatusValidatorData() {
        return getResult();
    }
}
