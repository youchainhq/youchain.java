package cc.youchain.protocol.core;

import java.math.BigInteger;

import cc.youchain.protocol.core.enums.YOUValidatorRole;
import cc.youchain.protocol.core.methods.response.*;
import cc.youchain.protocol.core.methods.response.YOUFilter;

/**
 * Core YOUChain JSON-RPC API.
 */
public interface YOUChainCore {

    // 新增字段
    Request<?, YOUNetworkId> youNetworkId();

    Request<?, YOUChainClientVersion> youChainClientVersion();

    Request<?, YOUChainSha3> youChainSha3(String data);

    Request<?, NetVersion> netVersion();

    Request<?, NetListening> netListening();

    Request<?, NetPeerCount> netPeerCount();

    Request<?, YOUProtocolVersion> youProtocolVersion();

    Request<?, YOUCoinbase> youCoinbase();

    Request<?, YOUSyncing> youSyncing();

    Request<?, YOUMining> youMining();

    Request<?, YOUGasPrice> youGasPrice();

    Request<?, YOUAccounts> youAccounts();

    Request<?, YOUBlockNumber> youBlockNumber();

    Request<?, YOUGetBalance> youGetBalance(
            String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, YOUGetStorageAt> youGetStorageAt(
            String address, BigInteger position,
            DefaultBlockParameter defaultBlockParameter);

    Request<?, YOUGetTransactionCount> youGetTransactionCount(
            String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, YOUGetBlockTransactionCountByHash> youGetBlockTransactionCountByHash(
            String blockHash);

    Request<?, YOUGetBlockTransactionCountByNumber> youGetBlockTransactionCountByNumber(
            DefaultBlockParameter defaultBlockParameter);

    Request<?, YOUGetCode> youGetCode(String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, YOUSign> youSign(String address, String sha3HashOfDataToSign);

    Request<?, YOUSendTransaction> youSendTransaction(
            cc.youchain.protocol.core.methods.request.Transaction transaction);

    Request<?, YOUSendTransaction> youSendRawTransaction(
            String signedTransactionData);

    Request<?, YOUGetProof> youGetProof(
            String address, String[] storageKeys, DefaultBlockParameter defaultBlockParameter);

    Request<?, YOUGetPoolNonce> youGetPoolNonce(
            String address);

    Request<?, YOUGetPoolTransaction> youGetPoolTransaction(
            String transactionHash);

    Request<?, YOUGetWithdrawRecords> youGetWithdrawRecords(
            DefaultBlockParameter defaultBlockParameter);

    Request<?, YOUCall> youCall(
            cc.youchain.protocol.core.methods.request.Transaction transaction,
            DefaultBlockParameter defaultBlockParameter);

    Request<?, YOUEstimateGas> youEstimateGas(
            cc.youchain.protocol.core.methods.request.Transaction transaction);

    Request<?, YOUBlock> youGetBlockByHash(String blockHash, boolean returnFullTransactionObjects);

    Request<?, YOUBlock> youGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter,
            boolean returnFullTransactionObjects);

    Request<?, YOUTransaction> youGetTransactionByHash(String transactionHash);

    Request<?, YOUTransaction> youGetTransactionByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex);

    Request<?, YOUTransaction> youGetTransactionByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex);

    Request<?, YOUGetTransactionReceipt> youGetTransactionReceipt(String transactionHash);

    Request<?, YOUFilter> youNewFilter(cc.youchain.protocol.core.methods.request.YOUFilter youFilter);

    Request<?, YOUFilter> youNewBlockFilter();

    Request<?, YOUFilter> youNewPendingTransactionFilter();

    Request<?, YOUUninstallFilter> youUninstallFilter(BigInteger filterId);

    Request<?, YOULog> youGetFilterChanges(BigInteger filterId);

    Request<?, YOULog> youGetFilterLogs(BigInteger filterId);

    Request<?, YOULog> youGetLogs(cc.youchain.protocol.core.methods.request.YOUFilter youFilter);

    Request<?, YOUValidator> youValidatorByMainAddress(DefaultBlockParameter defaultBlockParameter, String mainAddress);

    Request<?, YOUValidatorsStat> youValidatorsStat(DefaultBlockParameter defaultBlockParameter);

    /**
     * @param role                  1-chancellor(议长), 2-senator(议员), 3-house(众议)
     * @param page                  第一页请传0
     * @param pageSize              每页条数 最大值200
     * @param defaultBlockParameter 区块高度
     * @return 验证者信息列表
     */
    Request<?, YOUValidators> youValidators(YOUValidatorRole role, Integer page, Integer pageSize, DefaultBlockParameter defaultBlockParameter);

    Request<?, YOUCreateValidator> youCreateValidator(BigInteger nonce, String name, String operator, String coinbase,
                                                      String mainPubKey, String blsPubKey, String value, YOUValidatorRole role,
                                                      BigInteger acceptDelegation, BigInteger commissionRate, BigInteger riskObligation);

    Request<?, YOUUpdateValidator> youUpdateValidator(BigInteger nonce, String name, String mainAddress, String operator, String coinbase,
                                                      BigInteger acceptDelegation, BigInteger commissionRate, BigInteger riskObligation);

    Request<?, YOUDepositValidator> youDepositValidator(BigInteger nonce, String mainAddress, String value);

    Request<?, YOUWithdrawValidator> youWithdrawValidator(BigInteger nonce, String mainAddress, String withdraw, String value);

    Request<?, YOUSettleValidator> youSettleValidator(String mainAddress);

    Request<?, YOUChangeStatusValidator> youChangeStatusValidator(BigInteger nonce, String mainAddress, BigInteger status);

}
