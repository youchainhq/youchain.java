package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

import java.util.List;

/**
 * you_getProof
 */

public class YOUGetProof extends Response<YOUGetProof.AccountResult> {
    public AccountResult getProof() {
        return this.getResult();
    }

    public static class AccountResult {
        private String address;
        private List<String> accountProof;
        private String balance;
        private String codeHash;
        private String nonce;
        private String storageHash;
        private List<StorageProof> storageProof;

        public AccountResult() {
        }

        public AccountResult(String address, List<String> accountProof, String balance,
                             String codeHash, String nonce, String storageHash,
                             List<StorageProof> storageProof) {
            this.address = address;
            this.accountProof = accountProof;
            this.balance = balance;
            this.codeHash = codeHash;
            this.nonce = nonce;
            this.storageHash = storageHash;
            this.storageProof = storageProof;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<String> getAccountProof() {
            return accountProof;
        }

        public void setAccountProof(List<String> accountProof) {
            this.accountProof = accountProof;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getCodeHash() {
            return codeHash;
        }

        public void setCodeHash(String codeHash) {
            this.codeHash = codeHash;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getStorageHash() {
            return storageHash;
        }

        public void setStorageHash(String storageHash) {
            this.storageHash = storageHash;
        }

        public List<StorageProof> getStorageProof() {
            return storageProof;
        }

        public void setStorageProof(List<StorageProof> storageProof) {
            this.storageProof = storageProof;
        }

    }

    public static class StorageProof {
        private String key;
        private String value;
        private List<String> proof;

        public StorageProof() {
        }

        public StorageProof(String key, String value, List<String> proof) {
            this.key = key;
            this.value = value;
            this.proof = proof;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<String> getProof() {
            return proof;
        }

        public void setProof(List<String> value) {
            this.proof = proof;
        }
    }
}