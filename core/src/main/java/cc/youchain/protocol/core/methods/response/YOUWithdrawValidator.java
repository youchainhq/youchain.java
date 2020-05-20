package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

public class YOUWithdrawValidator extends Response<String> {

    public String getWithdrawValidatorData() {
        return getResult();
    }
}
