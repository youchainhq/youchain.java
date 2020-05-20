package cc.youchain.protocol.admin.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * personal_useValKey.
 */
public class PersonalUseValKey extends Response<String> {
    public String getUseValKey() {
        return getResult();
    }
}