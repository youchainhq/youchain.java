package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

public class YOUSettleValidator extends Response<String> {

    public String getSettleValidatorData() {
        return getResult();
    }
}
