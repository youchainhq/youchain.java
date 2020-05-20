package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;

import java.math.BigInteger;
import java.util.List;

public class YOUWithdrawRecords extends Response<List<YOUWithdrawRecords.WithdrawRecords>> {

    public List<YOUWithdrawRecords.WithdrawRecords> getWithdrawRecords() {
        return getResult();
    }

    public static class WithdrawRecords {
        private String operator;
        private String delegator;
        private String validator;
        private String recipient;
        private BigInteger nonce;
        private BigInteger creationHeight;
        private BigInteger completionHeight;
        private String initialBalance;
        private String finalBalance;
        private BigInteger finished;

        public WithdrawRecords() {
        }

        public WithdrawRecords(String operator, String delegator, String validator, String recipient, BigInteger nonce, BigInteger creationHeight, BigInteger completionHeight, String initialBalance, String finalBalance, BigInteger finished) {
            this.operator = operator;
            this.delegator = delegator;
            this.validator = validator;
            this.recipient = recipient;
            this.nonce = nonce;
            this.creationHeight = creationHeight;
            this.completionHeight = completionHeight;
            this.initialBalance = initialBalance;
            this.finalBalance = finalBalance;
            this.finished = finished;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getDelegator() {
            return delegator;
        }

        public void setDelegator(String delegator) {
            this.delegator = delegator;
        }

        public String getValidator() {
            return validator;
        }

        public void setValidator(String validator) {
            this.validator = validator;
        }

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public BigInteger getNonce() {
            return nonce;
        }

        public void setNonce(BigInteger nonce) {
            this.nonce = nonce;
        }

        public BigInteger getCreationHeight() {
            return creationHeight;
        }

        public void setCreationHeight(BigInteger creationHeight) {
            this.creationHeight = creationHeight;
        }

        public BigInteger getCompletionHeight() {
            return completionHeight;
        }

        public void setCompletionHeight(BigInteger completionHeight) {
            this.completionHeight = completionHeight;
        }

        public String getInitialBalance() {
            return initialBalance;
        }

        public void setInitialBalance(String initialBalance) {
            this.initialBalance = initialBalance;
        }

        public String getFinalBalance() {
            return finalBalance;
        }

        public void setFinalBalance(String finalBalance) {
            this.finalBalance = finalBalance;
        }

        public BigInteger getFinished() {
            return finished;
        }

        public void setFinished(BigInteger finished) {
            this.finished = finished;
        }
    }
}
