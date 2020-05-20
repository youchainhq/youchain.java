package cc.youchain.protocol.admin.methods.response;

import java.util.List;

import cc.youchain.protocol.core.Response;

/**
 * personal_listAccounts.
 */
public class PersonalListAccounts extends Response<List<String>> {
    public List<String> getAccountIds() {
        return getResult();
    }
}
