package cc.youchain.protocol.core.methods.response;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cc.youchain.utils.Numeric;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cc.youchain.protocol.ObjectMapperFactory;
import cc.youchain.protocol.core.Response;

/**
 * Block object returned by:
 * <ul>
 * <li>you_getBlockByHash</li>
 * <li>you_getBlockByNumber</li>
 * <li>you_getUncleByBlockHashAndIndex</li>
 * <li>you_getUncleByBlockNumberAndIndex</li>
 * </ul>
 */
public class YOUBlock extends Response<YOUBlock.Block> {

    @Override
    @JsonDeserialize(using = YOUBlock.ResponseDeserialiser.class)
    public void setResult(Block result) {
        super.setResult(result);
    }

    public Block getBlock() {
        return getResult();
    }

    public static class Block {

        private String number;
        private String hash;
        private String parentHash;
        private String logsBloom;
        private String transactionsRoot;
        private String stateRoot;
        private String receiptsRoot;
        private String miner;
        private String mixHash;
        private String extraData;
        private String size;
        private String gasLimit;
        private String gasUsed;
        private String timestamp;
        private String subsidy;
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
        private List<TransactionResult> transactions;

        public Block() {
        }

        public Block(String number, String hash, String parentHash, String logsBloom,
                     String transactionsRoot, String stateRoot, String receiptsRoot,
                     String miner, String extraData, String size, String timestamp,
                     String gasLimit, String gasUsed, String gasRewards, String subsidy, String mixHash,
                     String signature, String slashData, String validator, String certificate,
                     String valRoot, String chtRoot, String bltRoot, String version,
                     String nextVersion, String nextApprovals, String nextSwitchOn, String nextVoteBefore,
                     String consensus, List<TransactionResult> transactions) {

            this.number = number;
            this.hash = hash;
            this.parentHash = parentHash;
            this.logsBloom = logsBloom;
            this.transactionsRoot = transactionsRoot;
            this.stateRoot = stateRoot;
            this.receiptsRoot = receiptsRoot;
            this.miner = miner;
            this.mixHash = mixHash;
            this.extraData = extraData;
            this.size = size;
            this.gasLimit = gasLimit;
            this.gasUsed = gasUsed;
            this.gasRewards = gasRewards;
            this.subsidy = subsidy;
            this.timestamp = timestamp;
            this.consensus = consensus;
            this.certificate = certificate;
            this.chtRoot = chtRoot;
            this.bltRoot = bltRoot;
            this.signature = signature;
            this.slashData = slashData;
            this.valRoot = valRoot;
            this.validator = validator;
            this.version = version;
            this.nextVersion = nextVersion;
            this.nextApprovals = nextApprovals;
            this.nextSwitchOn = nextSwitchOn;
            this.nextVoteBefore = nextVoteBefore;
            this.transactions = transactions;
        }

        public BigInteger getNumber() {
            return Numeric.decodeQuantity(number);
        }

        public String getNumberRaw() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getParentHash() {
            return parentHash;
        }

        public void setParentHash(String parentHash) {
            this.parentHash = parentHash;
        }

        public String getLogsBloom() {
            return logsBloom;
        }

        public void setLogsBloom(String logsBloom) {
            this.logsBloom = logsBloom;
        }

        public String getTransactionsRoot() {
            return transactionsRoot;
        }

        public void setTransactionsRoot(String transactionsRoot) {
            this.transactionsRoot = transactionsRoot;
        }

        public String getStateRoot() {
            return stateRoot;
        }

        public void setStateRoot(String stateRoot) {
            this.stateRoot = stateRoot;
        }

        public String getReceiptsRoot() {
            return receiptsRoot;
        }

        public void setReceiptsRoot(String receiptsRoot) {
            this.receiptsRoot = receiptsRoot;
        }

        public String getMiner() {
            return miner;
        }

        public void setMiner(String miner) {
            this.miner = miner;
        }

        public String getMixHash() {
            return mixHash;
        }

        public void setMixHash(String mixHash) {
            this.mixHash = mixHash;
        }

        public String getExtraData() {
            return extraData;
        }

        public void setExtraData(String extraData) {
            this.extraData = extraData;
        }

        public BigInteger getSize() {
            return Numeric.decodeQuantity(size);
        }

        public String getSizeRaw() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public BigInteger getGasLimit() {
            return Numeric.decodeQuantity(gasLimit);
        }

        public String getGasLimitRaw() {
            return gasLimit;
        }

        public void setGasLimit(String gasLimit) {
            this.gasLimit = gasLimit;
        }

        public BigInteger getGasUsed() {
            return Numeric.decodeQuantity(gasUsed);
        }

        public String getGasUsedRaw() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public BigInteger getGasRewards() {
            return Numeric.decodeQuantity(gasRewards);
        }

        public String getGasRewardsRaw() {
            return gasRewards;
        }

        public BigInteger getSubsidy() {
            return Numeric.decodeQuantity(subsidy);
        }

        public void setSubsidy(String subsidy) {
            this.subsidy = subsidy;
        }

        public void setGasRewardsRaw(String gasRewards) {
            this.gasRewards = gasRewards;
        }

        public BigInteger getTimestamp() {
            return Numeric.decodeQuantity(timestamp);
        }

        public String getTimestampRaw() {
            return timestamp;
        }

        public void setChtRoot(String chtRoot) {
            this.chtRoot = chtRoot;
        }

        public String getChtRoot() {
            return chtRoot;
        }

        public void setBltRoot(String bltRoot) {
            this.bltRoot = bltRoot;
        }

        public String getBltRoot() {
            return bltRoot;
        }

        public void setSlashData(String slashData) {
            this.slashData = slashData;
        }

        public String getSlashData() {
            return slashData;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getSignature() {
            return signature;
        }

        public void setValRoot(String valRoot) {
            this.valRoot = valRoot;
        }

        public String getValRoot() {
            return valRoot;
        }

        public void setValidator(String validator) {
            this.validator = validator;
        }

        public String getValidator() {
            return validator;
        }

        public void setCertificate(String certificate) {
            this.certificate = certificate;
        }

        public String getCertificate() {
            return certificate;
        }

        public void setConsensus(String consensus) {
            this.consensus = consensus;
        }

        public String getConsensus() {
            return consensus;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getVersionRaw() {
            return version;
        }

        public BigInteger getVersion() {
            return Numeric.decodeQuantity(version);
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getNextVersionRaw() {
            return nextVersion;
        }

        public BigInteger getNextVersion() {
            return Numeric.decodeQuantity(nextVersion);
        }

        public void setNextVersion(String nextVersion) {
            this.nextVersion = nextVersion;
        }

        public String getNextApprovalsRaw() {
            return nextApprovals;
        }

        public BigInteger getNextApprovals() {
            return Numeric.decodeQuantity(nextApprovals);
        }

        public void setNextApprovals(String nextApprovals) {
            this.nextApprovals = nextApprovals;
        }

        public String getNextSwitchOnRaw() {
            return nextSwitchOn;
        }

        public BigInteger getNextSwitchOn() {
            return Numeric.decodeQuantity(nextSwitchOn);
        }

        public void setNextSwitchOn(String nextSwitchOn) {
            this.nextSwitchOn = nextSwitchOn;
        }

        public String getNextVoteBeforeRaw() {
            return nextVoteBefore;
        }

        public BigInteger getNextVoteBefore() {
            return Numeric.decodeQuantity(nextVoteBefore);
        }

        public void setNextVoteBefore(String nextVoteBefore) {
            this.nextVoteBefore = nextVoteBefore;
        }

        public List<TransactionResult> getTransactions() {
            return transactions;
        }

        @JsonDeserialize(using = ResultTransactionDeserialiser.class)
        public void setTransactions(List<TransactionResult> transactions) {
            this.transactions = transactions;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Block)) {
                return false;
            }

            Block block = (Block) o;

            if (getNumberRaw() != null
                    ? !getNumberRaw().equals(block.getNumberRaw()) : block.getNumberRaw() != null) {
                return false;
            }
            if (getHash() != null ? !getHash().equals(block.getHash()) : block.getHash() != null) {
                return false;
            }
            if (getParentHash() != null
                    ? !getParentHash().equals(block.getParentHash())
                    : block.getParentHash() != null) {
                return false;
            }
            if (getLogsBloom() != null
                    ? !getLogsBloom().equals(block.getLogsBloom())
                    : block.getLogsBloom() != null) {
                return false;
            }
            if (getTransactionsRoot() != null
                    ? !getTransactionsRoot().equals(block.getTransactionsRoot())
                    : block.getTransactionsRoot() != null) {
                return false;
            }
            if (getStateRoot() != null
                    ? !getStateRoot().equals(block.getStateRoot())
                    : block.getStateRoot() != null) {
                return false;
            }
            if (getReceiptsRoot() != null
                    ? !getReceiptsRoot().equals(block.getReceiptsRoot())
                    : block.getReceiptsRoot() != null) {
                return false;
            }
            if (getMiner() != null
                    ? !getMiner().equals(block.getMiner()) : block.getMiner() != null) {
                return false;
            }

            if (getMixHash() != null
                    ? !getMixHash().equals(block.getMixHash()) : block.getMixHash() != null) {
                return false;
            }
            if (getExtraData() != null
                    ? !getExtraData().equals(block.getExtraData())
                    : block.getExtraData() != null) {
                return false;
            }
            if (getSizeRaw() != null
                    ? !getSizeRaw().equals(block.getSizeRaw())
                    : block.getSizeRaw() != null) {
                return false;
            }
            if (getGasLimitRaw() != null
                    ? !getGasLimitRaw().equals(block.getGasLimitRaw())
                    : block.getGasLimitRaw() != null) {
                return false;
            }
            if (getGasUsedRaw() != null
                    ? !getGasUsedRaw().equals(block.getGasUsedRaw())
                    : block.getGasUsedRaw() != null) {
                return false;
            }
            if (getGasRewardsRaw() != null
                    ? !getGasRewardsRaw().equals(block.getGasRewardsRaw())
                    : block.getGasRewardsRaw() != null) {
                return false;
            }
            if (getTimestampRaw() != null
                    ? !getTimestampRaw().equals(block.getTimestampRaw())
                    : block.getTimestampRaw() != null) {
                return false;
            }

            if (getSlashData() != null
                    ? !getSlashData().equals(block.getSlashData()) : block.getSlashData() != null) {
                return false;
            }
            if (getSignature() != null
                    ? !getSignature().equals(block.getSignature()) : block.getSignature() != null) {
                return false;
            }
            if (getBltRoot() != null
                    ? !getBltRoot().equals(block.getBltRoot()) : block.getBltRoot() != null) {
                return false;
            }
            if (getCertificate() != null
                    ? !getCertificate().equals(block.getCertificate()) : block.getCertificate() != null) {
                return false;
            }
            if (getChtRoot() != null
                    ? !getChtRoot().equals(block.getChtRoot()) : block.getChtRoot() != null) {
                return false;
            }
            if (getValRoot() != null
                    ? !getValRoot().equals(block.getValRoot()) : block.getValRoot() != null) {
                return false;
            }
            if (getTransactions() != null
                    ? !getTransactions().equals(block.getTransactions())
                    : block.getTransactions() != null) {
                return false;
            }

            if (getValidator() != null
                    ? !getValidator().equals(block.getValidator())
                    : block.getValidator() != null) {
                return false;
            }

            if (getSubsidy() != null
                    ? !getSubsidy().equals(block.getSubsidy())
                    : block.getSubsidy() != null) {
                return false;
            }

            if (getVersionRaw() != null
                    ? !getVersion().equals(block.getVersion())
                    : block.getVersion() != null) {
                return false;
            }

            if (getNextVersionRaw() != null
                    ? !getNextVersion().equals(block.getNextVersion())
                    : block.getNextVersion() != null) {
                return false;
            }

            if (getNextApprovalsRaw() != null
                    ? !getNextApprovals().equals(block.getNextApprovals())
                    : block.getNextApprovals() != null) {
                return false;
            }

            if (getNextSwitchOnRaw() != null
                    ? !getNextSwitchOn().equals(block.getNextSwitchOn())
                    : block.getNextSwitchOn() != null) {
                return false;
            }

            if (getNextVoteBeforeRaw() != null
                    ? !getNextVoteBefore().equals(block.getNextVoteBefore())
                    : block.getNextVoteBefore() != null) {
                return false;
            }

            return getConsensus() != null
                    ? getConsensus().equals(block.getConsensus()) : block.getConsensus() == null;
        }

        @Override
        public int hashCode() {
            int result = getNumberRaw() != null ? getNumberRaw().hashCode() : 0;
            result = 31 * result + (getHash() != null ? getHash().hashCode() : 0);
            result = 31 * result + (getParentHash() != null ? getParentHash().hashCode() : 0);
            result = 31 * result + (getLogsBloom() != null ? getLogsBloom().hashCode() : 0);
            result = 31 * result
                    + (getTransactionsRoot() != null ? getTransactionsRoot().hashCode() : 0);
            result = 31 * result + (getStateRoot() != null ? getStateRoot().hashCode() : 0);
            result = 31 * result + (getReceiptsRoot() != null ? getReceiptsRoot().hashCode() : 0);
            result = 31 * result + (getMiner() != null ? getMiner().hashCode() : 0);
            result = 31 * result + (getMixHash() != null ? getMixHash().hashCode() : 0);
            result = 31 * result + (getExtraData() != null ? getExtraData().hashCode() : 0);
            result = 31 * result + (getSizeRaw() != null ? getSizeRaw().hashCode() : 0);
            result = 31 * result + (getGasLimitRaw() != null ? getGasLimitRaw().hashCode() : 0);
            result = 31 * result + (getGasUsedRaw() != null ? getGasUsedRaw().hashCode() : 0);
            result = 31 * result + (getGasRewardsRaw() != null ? getGasRewardsRaw().hashCode() : 0);
            result = 31 * result + (getBltRoot() != null ? getBltRoot().hashCode() : 0);
            result = 31 * result + (getSlashData() != null ? getSlashData().hashCode() : 0);
            result = 31 * result + (getSignature() != null ? getSignature().hashCode() : 0);
            result = 31 * result + (getVersionRaw() != null ? getVersion().hashCode() : 0);
            result = 31 * result + (getNextVersionRaw() != null ? getNextVersion().hashCode() : 0);
            result = 31 * result + (getNextApprovalsRaw() != null ? getNextApprovals().hashCode() : 0);
            result = 31 * result + (getNextSwitchOnRaw() != null ? getNextSwitchOn().hashCode() : 0);
            result = 31 * result + (getNextVoteBeforeRaw() != null ? getNextVoteBefore().hashCode() : 0);
            result = 31 * result + (getCertificate() != null ? getCertificate().hashCode() : 0);
            result = 31 * result + (getConsensus() != null ? getConsensus().hashCode() : 0);
            result = 31 * result + (getTimestampRaw() != null ? getTimestampRaw().hashCode() : 0);
            result = 31 * result + (getChtRoot() != null ? getChtRoot().hashCode() : 0);
            result = 31 * result + (getValRoot() != null ? getValRoot().hashCode() : 0);
            result = 31 * result + (getValidator() != null ? getValidator().hashCode() : 0);
            result = 31 * result + (getSubsidy() != null ? getSubsidy().hashCode() : 0);
            result = 31 * result + (getTransactions() != null ? getTransactions().hashCode() : 0);
            return result;
        }
    }

    public interface TransactionResult<T> {
        T get();
    }

    public static class TransactionHash implements TransactionResult<String> {
        private String value;

        public TransactionHash() {
        }

        public TransactionHash(String value) {
            this.value = value;
        }

        @Override
        public String get() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TransactionHash)) {
                return false;
            }

            TransactionHash that = (TransactionHash) o;

            return value != null ? value.equals(that.value) : that.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    public static class TransactionObject extends Transaction
            implements TransactionResult<Transaction> {
        public TransactionObject() {
        }

        public TransactionObject(String hash, String nonce, String blockHash, String blockNumber,
                                 String transactionIndex, String from, String to, String value,
                                 String gasPrice, String gas, String input, String creates,
                                 String publicKey, String raw, String r, String s, int v) {
            super(hash, nonce, blockHash, blockNumber, transactionIndex, from, to, value,
                    gasPrice, gas, input, creates, publicKey, raw, r, s, v);
        }

        @Override
        public Transaction get() {
            return this;
        }
    }

    public static class ResultTransactionDeserialiser
            extends JsonDeserializer<List<TransactionResult>> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public List<TransactionResult> deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {

            List<TransactionResult> transactionResults = new ArrayList<>();
            JsonToken nextToken = jsonParser.nextToken();

            if (nextToken == JsonToken.START_OBJECT) {
                Iterator<TransactionObject> transactionObjectIterator =
                        objectReader.readValues(jsonParser, TransactionObject.class);
                while (transactionObjectIterator.hasNext()) {
                    transactionResults.add(transactionObjectIterator.next());
                }
            } else if (nextToken == JsonToken.VALUE_STRING) {
                jsonParser.getValueAsString();

                Iterator<TransactionHash> transactionHashIterator =
                        objectReader.readValues(jsonParser, TransactionHash.class);
                while (transactionHashIterator.hasNext()) {
                    transactionResults.add(transactionHashIterator.next());
                }
            }

            return transactionResults;
        }
    }

    public static class ResponseDeserialiser extends JsonDeserializer<Block> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public Block deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, Block.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
}
