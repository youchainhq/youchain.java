package cc.youchain.integration.demos;

import cc.youchain.crypto.Credentials;
import cc.youchain.crypto.RawTransaction;
import cc.youchain.crypto.TransactionEncoder;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.DefaultBlockParameter;
import cc.youchain.protocol.core.DefaultBlockParameterName;
import cc.youchain.protocol.core.enums.YOUValidatorRole;
import cc.youchain.protocol.core.methods.request.*;
import cc.youchain.protocol.core.methods.response.*;
import cc.youchain.protocol.core.methods.response.Transaction;
import cc.youchain.protocol.http.HttpService;
import cc.youchain.tx.NetworkId;
import cc.youchain.tx.Transfer;
import cc.youchain.utils.Convert;
import cc.youchain.utils.Numeric;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public class ValidatorDemos {

    private static final Logger logger = LoggerFactory.getLogger(ValidatorDemos.class);
    private static String nodeUrl = "http://test-node.iyouchain.com:80";
    private static HttpService httpService = new HttpService(nodeUrl);
    private static YOUChain youChain = YOUChain.build(httpService);

    @Test
    public void createTestPoolDemo() throws Exception {
        // TODO: poolNonce
        YOUGetPoolNonce youGetPoolNonce = youChain.youGetPoolNonce("0xd7bad0135F8c6bE4C77D8Bc9bE60a51F9b9Ec77B").send();
        BigInteger poolNonce = youGetPoolNonce.getPoolNonce();
        logger.info("=================================\n");
        logger.info("poolNonce: " + poolNonce.toString() + "\n");

        // TODO: poolTransaction
        YOUGetPoolTransaction youGetpoolTransaction = youChain.youGetPoolTransaction("Transaction").send();
        Optional<Transaction> poolTransaction = youGetpoolTransaction.getPoolTransaction();
        logger.info("=================================\n");
        logger.info("poolTransaction: " + poolTransaction.toString());
    }

    @Test
    public void createTestValidatorQueriesDemo() throws Exception {

        // TODO: getWithdrawsRecords
        YOUGetWithdrawRecords youGetWithdrawRecords = youChain.youGetWithdrawRecords(DefaultBlockParameter.valueOf("latest")).send();
        List<YOUGetWithdrawRecords.WithdrawRecords> withdrawRecords = youGetWithdrawRecords.getWithdrawRecords();
        logger.info("=================================\n");
        logger.info("withdrawRecords: " + withdrawRecords.toString() + "\n");

    }

    @Test
    public void createTestValidatorDemo() throws Exception {
        BigInteger nonce = BigInteger.valueOf(100);

        nonce = nonce.add(BigInteger.ONE);
        // TODO: depositValidator
        YOUDepositValidator youDepositValidator = youChain.youDepositValidator(nonce, "0x93e8cCBF6383Ce4186d8f1F25a9429Ce6Aecc954", "0x100").send();
        String depositValidator = youDepositValidator.getDepositValidatorData();
        logger.info("=================================\n");
        logger.info("depositValidator: " + depositValidator + "\n");

        nonce = nonce.add(BigInteger.ONE);
        // TODO: withdrawValidator
        YOUWithdrawValidator youWithdrawValidator = youChain.youWithdrawValidator(nonce, "abcdef", "0x59677fd68ec54e43ad4319d915f81748b5a6ff8b", "0x100").send();
        String withdrawValidator = youWithdrawValidator.getWithdrawValidatorData();
        logger.info("=================================\n");
        logger.info("withdrawValidator: " + withdrawValidator + "\n");
    }

    @Test
    public void youValidatorsTest() throws Exception {
        YOUValidators youValidators = youChain.youValidators(YOUValidatorRole.ALL, 0, 100, DefaultBlockParameterName.LATEST).send();
        logger.info("validators: " + JSONUtil.toJsonStr(youValidators.getValidators()) + "\n");
    }

    @Test
    public void youValidatorByMainAddressTest() throws Exception {
        YOUValidator youValidator = youChain.youValidatorByMainAddress(DefaultBlockParameterName.LATEST, "0xFc417779C6805A80Fe8e788335ffD9F326AA5893").send();
        logger.info("validator: " + JSONUtil.toJsonStr(youValidator.getValidator()));
    }

    @Test
    public void youValidatorStatTest() throws Exception {
        YOUValidatorsStat youValidatorStat = youChain.youValidatorsStat(DefaultBlockParameterName.LATEST).send();
        String onlineStake = youValidatorStat.getValidatorsStat().get("1").getOnlineStake();
        logger.info("onlineStake: " + onlineStake + "\n");
        String offlineStake = youValidatorStat.getValidatorsStat().get("1").getOfflineStake();
        logger.info("offlineStake: " + offlineStake + "\n");
    }

    @Test
    public void youSettleValidatorTest() throws Exception {
        YOUSettleValidator youSettleValidator = youChain.youSettleValidator("0x1071C81cC33073b1a6670722C5cF7E5388638060").send();
        String settleValidator = youSettleValidator.getSettleValidatorData();
        logger.info("0x1071C81cC33073b1a6670722C5cF7E5388638060: " + settleValidator + "\n");
    }

    @Test
    public void changeStatusValidatorTest() throws Exception {
        String mainAddress = "0x1071C81cC33073b1a6670722C5cF7E5388638060";
        BigInteger nonce = youChain.youGetTransactionCount(mainAddress, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        YOUChangeStatusValidator youChangeStatusValidator = youChain.youChangeStatusValidator(nonce.add(BigInteger.ONE), mainAddress, BigInteger.valueOf(1)).send();
        String changeStatusValidator = youChangeStatusValidator.getChangeStatusValidatorData();
        logger.info("changeStatusValidator: " + changeStatusValidator + "\n");
    }


    @Test
    public void updateValidatorTest() throws Exception {
        String privateKey = "0xd44c94b2f297f69db0d04c4d85ecff169bf87480603b26a9c411811e7ab3cb0e";
        Credentials credential = Credentials.create(privateKey);
        String mainAddress = credential.getAddress();
        BigInteger nonce = youChain.youGetTransactionCount(mainAddress, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        YOUUpdateValidator youUpdateValidator = youChain.youUpdateValidator(nonce.add(BigInteger.ONE), "abcdeftest", mainAddress, "0x59677fD68ec54e43aD4319D915f81748B5a6Ff8B", "0x93e8cCBF6383Ce4186d8f1F25a9429Ce6Aecc954", BigInteger.valueOf(1), BigInteger.valueOf(120), BigInteger.valueOf(200)).send();
        String updateValidator = youUpdateValidator.getUpdateValidatorData();
        logger.info("=================================\n");
        logger.info("updateValidator: " + updateValidator + "\n");
//        byte networkId = NetworkId.TESTNET;
//        Credentials credential = Credentials.create(privateKey);
//        BigInteger gasPrice = youChain.youGasPrice().send().getGasPrice();
//        BigInteger amountLu = Convert.toLu(amount, Convert.Unit.YOU).toBigInteger();
//        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, Transfer.GAS_LIMIT, addressTo, amountLu, "");
//        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, networkId, credential);
//        String signedTransactionData = Numeric.toHexString(signMessage);
//        logger.info("签名后的交易数据:{}", signedTransactionData);
//
//        // 发送交易数据到节点
//        String transactionHash = youChain.youSendRawTransaction(signedTransactionData).send().getTransactionHash();
//        logger.info("发送交易数据 {}", transactionHash);
    }

    @Test
    public void createValidatorTest() throws Exception {
        String privateKey = "0x78e09c3bd25bfcdd169f115a8d3e74867ec78f761d3c0b2508a55630e3288d2e";
        Credentials credential = Credentials.create(privateKey);
        String mainAddress = "0x35c9a59b4882a00428c9dfea31c1034777fe8b7a";
        BigInteger nonce = BigInteger.valueOf(100);
        String mainPubKey = "";
        String blsPubKey = "";
        YOUCreateValidator youCreateValidator = youChain.youCreateValidator(nonce, "lzltest", "0x59677fd68ec54e43ad4319d915f81748b5a6ff8b", mainAddress, mainPubKey, blsPubKey, "0x10", YOUValidatorRole.HOUSE, BigInteger.valueOf(1), BigInteger.valueOf(120), BigInteger.valueOf(200)).send();
        String createValidator = youCreateValidator.getCreateValidatorData();
        logger.info("createValidator: " + createValidator + "\n");
    }

}
