package cc.youchain.protocol.admin.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * personal_newValKey.
 */
public class PersonalNewValKey extends Response<PersonalNewValKey.PersonalNewValKeyResp> {

    public PersonalNewValKeyResp getResp() {
        return this.getResult();
    }

    public static class PersonalNewValKeyResp {

        private String address;

        private String mainPubKey;

        private String blsPubKey;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMainPubKey() {
            return mainPubKey;
        }

        public void setMainPubKey(String mainPubKey) {
            this.mainPubKey = mainPubKey;
        }

        public String getBlsPubKey() {
            return blsPubKey;
        }

        public void setBlsPubKey(String blsPubKey) {
            this.blsPubKey = blsPubKey;
        }

    }
}