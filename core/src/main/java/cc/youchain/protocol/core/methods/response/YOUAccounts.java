package cc.youchain.protocol.core.methods.response;

import java.util.List;

import cc.youchain.protocol.core.Response;

/**
 * you_accounts.
 */
public class YOUAccounts extends Response<List<String>> {
    public List<String> getAccounts() {
        return getResult();
    }
}
