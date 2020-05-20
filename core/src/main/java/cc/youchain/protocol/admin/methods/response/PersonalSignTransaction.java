package cc.youchain.protocol.admin.methods.response;

import cc.youchain.protocol.core.Response;
import cc.youchain.protocol.core.methods.response.Transaction;

/**
 * personal_signTransaction.
 */
public class PersonalSignTransaction extends Response<PersonalSignTransaction.PersonalSignTransactionResp> {

    public PersonalSignTransactionResp getResp() {
        return this.getResult();
    }

    public static class PersonalSignTransactionResp {

        private String raw;

        private Transaction tx;

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public Transaction getTx() {
            return tx;
        }

        public void setTx(Transaction tx) {
            this.tx = tx;
        }
    }
}
