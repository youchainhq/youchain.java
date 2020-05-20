package cc.youchain.protocol.admin.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * personal_delValKey.
 */
public class PersonalDelValKey extends Response<String> {
    public String getDelValKey() {
        return getResult();
    }
}