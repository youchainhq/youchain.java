package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

import java.util.List;

public class YOUValidators extends Response<List<Validator>> {

    public List<Validator> getValidators() {
        return this.getResult();
    }

}