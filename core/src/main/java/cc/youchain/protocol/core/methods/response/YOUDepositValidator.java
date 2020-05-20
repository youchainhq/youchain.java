package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

public class YOUDepositValidator extends Response<String> {

    public String getDepositValidatorData() {
        return getResult();
    }
}
