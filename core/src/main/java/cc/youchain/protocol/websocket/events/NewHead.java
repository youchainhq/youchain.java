package cc.youchain.protocol.websocket.events;

public class NewHead {
    private String extraData;
    private String gasLimit;
    private String gasUsed;
    private String hash;
    private String miner;
    private String logsBloom;
    private String number;
    private String mixHash;
    private String parentHash;
    private String receiptRoot;
    private String stateRoot;
    private String timestamp;
    private String transactionRoot;
    private String gasRewards;
    private String consensus;
    private String signature;
    private String slashData;
    private String validator;
    private String valRoot;
    private String chtRoot;
    private String bltRoot;
    private String certificate;
    private String version;
    private String nextVersion;
    private String nextApprovals;
    private String nextSwitchOn;
    private String nextVoteBefore;

    public String getMiner(){
        return miner;
    }

    public String getExtraData() {
        return extraData;
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public String getHash() {
        return hash;
    }

    public String getMixHash(){
        return mixHash;
    }

    public String getLogsBloom() {
        return logsBloom;
    }

    public String getNumber() {
        return number;
    }

    public String getParentHash() {
        return parentHash;
    }

    public String getReceiptRoot() {
        return receiptRoot;
    }

    public String getStateRoot() {
        return stateRoot;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getGasRewards() {
        return gasRewards;
    }

    public String getConsensus() {
        return consensus;
    }

    public String getSignature(){
        return signature;
    }

    public String getSlashData(){
        return slashData;
    }

    public String getValidator(){
        return validator;
    }

    public String getValRoot(){
        return valRoot;
    }

    public String getChtRoot(){
        return chtRoot;
    }

    public String getBltRoot(){
        return bltRoot;
    }

    public String getCertificate(){
        return certificate;
    }

    public String getVersion(){
        return version;
    }

    public String getNextVersion(){
        return nextVersion;
    }

    public String getNextApprovals(){
        return nextApprovals;
    }

    public String nextSwitchOn(){
        return nextSwitchOn;
    }

    public String nextVoteBefore(){
        return nextVoteBefore;
    }

    public String getTransactionRoot() {
        return transactionRoot;
    }
}
