package cc.youchain.protocol.core;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.enums.YOUValidatorRole;
import cc.youchain.protocol.core.methods.response.*;
import cc.youchain.protocol.core.methods.response.YOUFilter;
import cc.youchain.protocol.websocket.events.LogNotification;
import cc.youchain.protocol.websocket.events.NewHeadsNotification;
import cc.youchain.protocol.websocket.events.PendingTransactionNotification;
import cc.youchain.protocol.websocket.events.SyncingNotfication;
import cc.youchain.utils.Numeric;
import io.reactivex.Flowable;

import cc.youchain.protocol.YOUChainService;
import cc.youchain.protocol.rx.JsonRpc2_0Rx;
import cc.youchain.utils.Async;

/**
 * JSON-RPC 2.0 factory implementation.
 */
public class JsonRpc2_0YOUChain implements YOUChain {

    public static final int DEFAULT_BLOCK_TIME = 10 * 1000;

    protected final YOUChainService youChainService;
    private final JsonRpc2_0Rx youChainRx;
    private final long blockTime;
    private final ScheduledExecutorService scheduledExecutorService;

    public JsonRpc2_0YOUChain(YOUChainService youChainService) {
        this(youChainService, DEFAULT_BLOCK_TIME, Async.defaultExecutorService());
    }

    public JsonRpc2_0YOUChain(
            YOUChainService youChainService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        this.youChainService = youChainService;
        this.youChainRx = new JsonRpc2_0Rx(this, scheduledExecutorService);
        this.blockTime = pollingInterval;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public Request<?, YOUChainClientVersion> youChainClientVersion() {
        return new Request<>(
                "youchain_clientVersion",
                Collections.<String>emptyList(),
                youChainService,
                YOUChainClientVersion.class);
    }

    @Override
    public Request<?, YOUChainSha3> youChainSha3(String data) {
        return new Request<>(
                "youchain_sha3",
                Arrays.asList(data),
                youChainService,
                YOUChainSha3.class);
    }

    @Override
    public Request<?, NetVersion> netVersion() {
        return new Request<>(
                "net_version",
                Collections.<String>emptyList(),
                youChainService,
                NetVersion.class);
    }

    @Override
    public Request<?, NetListening> netListening() {
        return new Request<>(
                "net_listening",
                Collections.<String>emptyList(),
                youChainService,
                NetListening.class);
    }

    @Override
    public Request<?, NetPeerCount> netPeerCount() {
        return new Request<>(
                "net_peerCount",
                Collections.<String>emptyList(),
                youChainService,
                NetPeerCount.class);
    }

    @Override
    public Request<?, YOUNetworkId> youNetworkId() {
        return new Request<>(
                "you_networkId",
                Collections.<String>emptyList(),
                youChainService,
                YOUNetworkId.class);
    }

    @Override
    public Request<?, YOUProtocolVersion> youProtocolVersion() {
        return new Request<>(
                "you_protocolVersion",
                Collections.<String>emptyList(),
                youChainService,
                YOUProtocolVersion.class);
    }

    @Override
    public Request<?, YOUCoinbase> youCoinbase() {
        return new Request<>(
                "you_coinbase",
                Collections.<String>emptyList(),
                youChainService,
                YOUCoinbase.class);
    }

    @Override
    public Request<?, YOUSyncing> youSyncing() {
        return new Request<>(
                "you_syncing",
                Collections.<String>emptyList(),
                youChainService,
                YOUSyncing.class);
    }

    @Override
    public Request<?, YOUMining> youMining() {
        return new Request<>(
                "you_mining",
                Collections.<String>emptyList(),
                youChainService,
                YOUMining.class);
    }

    @Override
    public Request<?, YOUGasPrice> youGasPrice() {
        return new Request<>(
                "you_gasPrice",
                Collections.<String>emptyList(),
                youChainService,
                YOUGasPrice.class);
    }

    @Override
    public Request<?, YOUAccounts> youAccounts() {
        return new Request<>(
                "you_accounts",
                Collections.<String>emptyList(),
                youChainService,
                YOUAccounts.class);
    }

    @Override
    public Request<?, YOUBlockNumber> youBlockNumber() {
        return new Request<>(
                "you_blockNumber",
                Collections.<String>emptyList(),
                youChainService,
                YOUBlockNumber.class);
    }

    @Override
    public Request<?, YOUGetBalance> youGetBalance(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "you_getBalance",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                youChainService,
                YOUGetBalance.class);
    }

    @Override
    public Request<?, YOUGetStorageAt> youGetStorageAt(
            String address, BigInteger position, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "you_getStorageAt",
                Arrays.asList(
                        address,
                        Numeric.encodeQuantity(position),
                        defaultBlockParameter.getValue()),
                youChainService,
                YOUGetStorageAt.class);
    }

    @Override
    public Request<?, YOUGetTransactionCount> youGetTransactionCount(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "you_getTransactionCount",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                youChainService,
                YOUGetTransactionCount.class);
    }

    @Override
    public Request<?, YOUGetBlockTransactionCountByHash> youGetBlockTransactionCountByHash(
            String blockHash) {
        return new Request<>(
                "you_getBlockTransactionCountByHash",
                Arrays.asList(blockHash),
                youChainService,
                YOUGetBlockTransactionCountByHash.class);
    }

    @Override
    public Request<?, YOUGetBlockTransactionCountByNumber> youGetBlockTransactionCountByNumber(
            DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "you_getBlockTransactionCountByNumber",
                Arrays.asList(defaultBlockParameter.getValue()),
                youChainService,
                YOUGetBlockTransactionCountByNumber.class);
    }

    @Override
    public Request<?, YOUGetCode> youGetCode(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "you_getCode",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                youChainService,
                YOUGetCode.class);
    }

    @Override
    public Request<?, YOUSign> youSign(String address, String sha3HashOfDataToSign) {
        return new Request<>(
                "you_sign",
                Arrays.asList(address, sha3HashOfDataToSign),
                youChainService,
                YOUSign.class);
    }

    //@Override
    public Request<?, YOUValidator> youValidatorByMainAddress(DefaultBlockParameter defaultBlockParameter, String mainAddress) {
        return new Request<>(
                "you_validatorByMainAddress",
                Arrays.asList(defaultBlockParameter.getValue(), mainAddress),
                youChainService,
                YOUValidator.class);
    }

    //@Override
    public Request<?, YOUValidators> youValidators(YOUValidatorRole role, Integer page, Integer pageSize, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "you_validators",
                Arrays.asList(role.getRole(), page, pageSize, defaultBlockParameter.getValue()),
                youChainService,
                YOUValidators.class);
    }

    //@Override
    public Request<?, YOUValidatorsStat> youValidatorsStat(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "you_validatorsStat",
                Arrays.asList(defaultBlockParameter.getValue()),
                youChainService,
                YOUValidatorsStat.class);
    }

    //@Override
    public Request<?, YOUCreateValidator> youCreateValidator(BigInteger nonce, String name, String operator, String coinbase,
                                                             String mainPubKey, String blsPubKey, String value, YOUValidatorRole role,
                                                             BigInteger acceptDelegation, BigInteger commissionRate, BigInteger riskObligation) {
        return new Request<>(
                "you_createValidator",
                Arrays.asList(nonce, name, operator, coinbase, mainPubKey, blsPubKey, value, role.getRole(), acceptDelegation, commissionRate, riskObligation),
                youChainService,
                YOUCreateValidator.class);
    }

    //@Override
    public Request<?, YOUDepositValidator> youDepositValidator(BigInteger nonce, String mainAddress, String value) {
        return new Request<>(
                "you_depositValidator",
                Arrays.asList(nonce, mainAddress, value),
                youChainService,
                YOUDepositValidator.class);
    }

    //@Override
    public Request<?, YOUWithdrawValidator> youWithdrawValidator(BigInteger nonce, String mainAddress, String withdraw, String value) {
        return new Request<>(
                "you_withdrawValidator",
                Arrays.asList(nonce, mainAddress, withdraw, value),
                youChainService,
                YOUWithdrawValidator.class);
    }

    //@Override
    public Request<?, YOUUpdateValidator> youUpdateValidator(BigInteger nonce, String name, String mainAddress, String operator, String coinbase,
                                                             BigInteger acceptDelegation, BigInteger commissionRate, BigInteger riskObligation) {
        return new Request<>(
                "you_updateValidator",
                Arrays.asList(nonce, name, mainAddress, operator, coinbase, acceptDelegation, commissionRate, riskObligation),
                youChainService,
                YOUUpdateValidator.class);
    }

    //@Override
    public Request<?, YOUSettleValidator> youSettleValidator(String mainAddress) {
        return new Request<>(
                "you_settleValidator",
                Arrays.asList(mainAddress),
                youChainService,
                YOUSettleValidator.class);
    }

    //@Override
    public Request<?, YOUChangeStatusValidator> youChangeStatusValidator(BigInteger nonce, String mainAddress, BigInteger status) {
        return new Request<>(
                "you_changeStatusValidator",
                Arrays.asList(nonce, mainAddress, status),
                youChainService,
                YOUChangeStatusValidator.class);
    }

    //@Override
    public Request<?, YOUGetWithdrawRecords> youGetWithdrawRecords(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "you_getWithdrawRecords",
                Arrays.asList(defaultBlockParameter.getValue()),
                youChainService,
                YOUGetWithdrawRecords.class);
    }

    @Override
    public Request<?, YOUSendTransaction>
    youSendTransaction(
            cc.youchain.protocol.core.methods.request.Transaction transaction) {
        return new Request<>(
                "you_sendTransaction",
                Arrays.asList(transaction),
                youChainService,
                YOUSendTransaction.class);
    }

    @Override
    public Request<?, YOUSendTransaction>
    youSendRawTransaction(
            String signedTransactionData) {
        return new Request<>(
                "you_sendRawTransaction",
                Arrays.asList(signedTransactionData),
                youChainService,
                YOUSendTransaction.class);
    }

    //@Override
    public Request<?, YOUGetProof>
    youGetProof(
            String address, String[] storageKeys, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "you_getProof",
                Arrays.asList(address, storageKeys, defaultBlockParameter),
                youChainService,
                YOUGetProof.class);
    }

    //@Override
    public Request<?, YOUGetPoolNonce>
    youGetPoolNonce(
            String address) {
        return new Request<>(
                "you_getPoolNonce",
                Arrays.asList(address),
                youChainService,
                YOUGetPoolNonce.class);
    }

    //@Override
    public Request<?, YOUGetPoolTransaction>
    youGetPoolTransaction(
            String transactionHash) {
        return new Request<>(
                "you_getPoolTransaction",
                Arrays.asList(transactionHash),
                youChainService,
                YOUGetPoolTransaction.class);
    }

    @Override
    public Request<?, YOUCall> youCall(
            cc.youchain.protocol.core.methods.request.Transaction transaction, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "you_call",
                Arrays.asList(transaction, defaultBlockParameter),
                youChainService,
                YOUCall.class);
    }

    @Override
    public Request<?, YOUEstimateGas> youEstimateGas(cc.youchain.protocol.core.methods.request.Transaction transaction) {
        return new Request<>(
                "you_estimateGas",
                Arrays.asList(transaction),
                youChainService,
                YOUEstimateGas.class);
    }

    @Override
    public Request<?, YOUBlock> youGetBlockByHash(
            String blockHash, boolean returnFullTransactionObjects) {
        return new Request<>(
                "you_getBlockByHash",
                Arrays.asList(
                        blockHash,
                        returnFullTransactionObjects),
                youChainService,
                YOUBlock.class);
    }

    @Override
    public Request<?, YOUBlock> youGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter,
            boolean returnFullTransactionObjects) {
        return new Request<>(
                "you_getBlockByNumber",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        returnFullTransactionObjects),
                youChainService,
                YOUBlock.class);
    }

    @Override
    public Request<?, YOUTransaction> youGetTransactionByHash(String transactionHash) {
        return new Request<>(
                "you_getTransactionByHash",
                Arrays.asList(transactionHash),
                youChainService,
                YOUTransaction.class);
    }

    @Override
    public Request<?, YOUTransaction> youGetTransactionByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) {
        return new Request<>(
                "you_getTransactionByBlockHashAndIndex",
                Arrays.asList(
                        blockHash,
                        Numeric.encodeQuantity(transactionIndex)),
                youChainService,
                YOUTransaction.class);
    }

    @Override
    public Request<?, YOUTransaction> youGetTransactionByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex) {
        return new Request<>(
                "you_getTransactionByBlockNumberAndIndex",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        Numeric.encodeQuantity(transactionIndex)),
                youChainService,
                YOUTransaction.class);
    }

    @Override
    public Request<?, YOUGetTransactionReceipt> youGetTransactionReceipt(String transactionHash) {
        return new Request<>(
                "you_getTransactionReceipt",
                Arrays.asList(transactionHash),
                youChainService,
                YOUGetTransactionReceipt.class);
    }

    @Override
    public Request<?, YOUFilter> youNewFilter(
            cc.youchain.protocol.core.methods.request.YOUFilter youFilter) {
        return new Request<>(
                "you_newFilter",
                Arrays.asList(youFilter),
                youChainService,
                YOUFilter.class);
    }

    @Override
    public Request<?, YOUFilter> youNewBlockFilter() {
        return new Request<>(
                "you_newBlockFilter",
                Collections.<String>emptyList(),
                youChainService,
                YOUFilter.class);
    }

    @Override
    public Request<?, YOUFilter> youNewPendingTransactionFilter() {
        return new Request<>(
                "you_newPendingTransactionFilter",
                Collections.<String>emptyList(),
                youChainService,
                YOUFilter.class);
    }

    @Override
    public Request<?, YOUUninstallFilter> youUninstallFilter(BigInteger filterId) {
        return new Request<>(
                "you_uninstallFilter",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                youChainService,
                YOUUninstallFilter.class);
    }

    @Override
    public Request<?, YOULog> youGetFilterChanges(BigInteger filterId) {
        return new Request<>(
                "you_getFilterChanges",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                youChainService,
                YOULog.class);
    }

    @Override
    public Request<?, YOULog> youGetFilterLogs(BigInteger filterId) {
        return new Request<>(
                "you_getFilterLogs",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                youChainService,
                YOULog.class);
    }

    @Override
    public Request<?, YOULog> youGetLogs(
            cc.youchain.protocol.core.methods.request.YOUFilter youFilter) {
        return new Request<>(
                "you_getLogs",
                Arrays.asList(youFilter),
                youChainService,
                YOULog.class);
    }

    @Override
    public Flowable<NewHeadsNotification> newHeadsNotifications() {
        return youChainService.subscribe(
                new Request<>(
                        "you_subscribe",
                        Collections.singletonList("newHeads"),
                        youChainService,
                        YOUSubscribe.class),
                "you_unsubscribe",
                NewHeadsNotification.class
        );
    }

    @Override
    public Flowable<PendingTransactionNotification> newPendingTransactionsNotifications() {
        return youChainService.subscribe(
                new Request<>(
                        "you_subscribe",
                        Collections.singletonList("newPendingTransactions"),
                        youChainService,
                        YOUSubscribe.class),
                "you_unsubscribe",
                PendingTransactionNotification.class
        );
    }

    @Override
    public Flowable<SyncingNotfication> syncingStatusNotifications() {
        return youChainService.subscribe(
                new Request<>(
                        "you_subscribe",
                        Arrays.asList("syncing"),
                        youChainService,
                        YOUSubscribe.class),
                "you_unsubscribe",
                SyncingNotfication.class);
    }

    @Override
    public Flowable<LogNotification> logsNotifications(
            List<String> addresses, List<String> topics) {

        Map<String, Object> params = createLogsParams(addresses, topics);

        return youChainService.subscribe(
                new Request<>(
                        "you_subscribe",
                        Arrays.asList("logs", params),
                        youChainService,
                        YOUSubscribe.class),
                "you_unsubscribe",
                LogNotification.class
        );
    }

    private Map<String, Object> createLogsParams(List<String> addresses, List<String> topics) {
        Map<String, Object> params = new HashMap<>();
        if (!addresses.isEmpty()) {
            params.put("address", addresses);
        }
        if (!topics.isEmpty()) {
            params.put("topics", topics);
        }
        return params;
    }

    @Override
    public Flowable<String> youBlockHashFlowable() {
        return youChainRx.youBlockHashFlowable(blockTime);
    }

    @Override
    public Flowable<String> youPendingTransactionHashFlowable() {
        return youChainRx.youPendingTransactionHashFlowable(blockTime);
    }

    @Override
    public Flowable<Log> youLogFlowable(
            cc.youchain.protocol.core.methods.request.YOUFilter youFilter) {
        return youChainRx.youLogFlowable(youFilter, blockTime);
    }

    @Override
    public Flowable<cc.youchain.protocol.core.methods.response.Transaction>
    transactionFlowable() {
        return youChainRx.transactionFlowable(blockTime);
    }

    @Override
    public Flowable<cc.youchain.protocol.core.methods.response.Transaction>
    pendingTransactionFlowable() {
        return youChainRx.pendingTransactionFlowable(blockTime);
    }

    @Override
    public Flowable<YOUBlock> blockFlowable(boolean fullTransactionObjects) {
        return youChainRx.blockFlowable(fullTransactionObjects, blockTime);
    }

    @Override
    public Flowable<YOUBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return youChainRx.replayBlocksFlowable(startBlock, endBlock, fullTransactionObjects);
    }

    @Override
    public Flowable<YOUBlock> replayPastBlocksFlowable(DefaultBlockParameter startBlock,
                                                       DefaultBlockParameter endBlock,
                                                       boolean fullTransactionObjects,
                                                       boolean ascending) {
        return youChainRx.replayBlocksFlowable(startBlock, endBlock,
                fullTransactionObjects, ascending);
    }

    @Override
    public Flowable<YOUBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Flowable<YOUBlock> onCompleteFlowable) {
        return youChainRx.replayPastBlocksFlowable(
                startBlock, fullTransactionObjects, onCompleteFlowable);
    }

    @Override
    public Flowable<YOUBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return youChainRx.replayPastBlocksFlowable(startBlock, fullTransactionObjects);
    }

    @Override
    public Flowable<cc.youchain.protocol.core.methods.response.Transaction>
    replayPastTransactionsFlowable(DefaultBlockParameter startBlock,
                                   DefaultBlockParameter endBlock) {
        return youChainRx.replayTransactionsFlowable(startBlock, endBlock);
    }

    @Override
    public Flowable<cc.youchain.protocol.core.methods.response.Transaction>
    replayPastTransactionsFlowable(DefaultBlockParameter startBlock) {
        return youChainRx.replayPastTransactionsFlowable(startBlock);
    }

    @Override
    public Flowable<YOUBlock> replayPastAndFutureBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return youChainRx.replayPastAndFutureBlocksFlowable(
                startBlock, fullTransactionObjects, blockTime);
    }

    @Override
    public Flowable<cc.youchain.protocol.core.methods.response.Transaction>
    replayPastAndFutureTransactionsFlowable(DefaultBlockParameter startBlock) {
        return youChainRx.replayPastAndFutureTransactionsFlowable(
                startBlock, blockTime);
    }

    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
        try {
            youChainService.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close YOUChain service", e);
        }
    }
}
