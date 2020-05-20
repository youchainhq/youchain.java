package cc.youchain.protocol.admin.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * personal_lockValKey.
 */
public class PersonalLockValKey extends Response<String> {
    public String getLockValKey() {
        return getResult();
    }
}