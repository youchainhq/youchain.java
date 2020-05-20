package cc.youchain.protocol.admin.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * personal_exportValKey.
 */
public class PersonalExportValKey extends Response<String> {
    public String getPrivateKey() {
        return getResult();
    }
}
