package cc.youchain.protocol.admin.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * personal_lockAccount.
 */
public class PersonalLockAccount extends Response<Boolean> {
    public Boolean accountLocked() {
        return getResult();
    }
}