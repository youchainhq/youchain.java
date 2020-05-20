package cc.youchain.protocol.admin.methods.response;

import cc.youchain.protocol.core.Response;

/**
 * personal_importValKey.
 */
public class PersonalImportValKey extends Response<PersonalImportValKey.PersonalImportValKeyResp> {

    public PersonalImportValKeyResp getResp() {
        return this.getResult();
    }

    public static class PersonalImportValKeyResp {

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
