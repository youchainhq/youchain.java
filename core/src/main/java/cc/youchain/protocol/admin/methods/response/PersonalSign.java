package cc.youchain.protocol.admin.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * personal_sign
 */
public class PersonalSign extends Response<String> {
    public String getSignedMessage() {
        return getResult();
    }
}
