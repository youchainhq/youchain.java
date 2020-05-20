package cc.youchain.integration.demos;

import cc.youchain.crypto.Credentials;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;
import cc.youchain.protocol.http.HttpService;
import cc.youchain.tx.*;
import cc.youchain.tx.response.Callback;
import cc.youchain.tx.response.PollingTransactionReceiptProcessor;
import cc.youchain.tx.response.QueuingTransactionReceiptProcessor;
import cc.youchain.utils.Convert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public class TransactionDemos {

    private static final Logger log = LoggerFactory.getLogger(TransactionDemos.class);
    private static String nodeUrl = "http://test-node.iyouchain.com:80";
    private static HttpService httpService = new HttpService(nodeUrl);
    private static YOUChain youChain = YOUChain.build(httpService);

    private static String address = "0xCEE89b240f8a08c59D5Fa20C195f2aD38fECCB88";
    private static String privateKey = "PrivateKey";

    /**
     * 直接发送，不查看交易确认之后的凭证
     *
     * @throws Exception
     */
    @Test
    public void onlySendTransactionDemo() throws Exception {
        // 发送者私钥
        String fromUserKey = privateKey;
        // 接收者地址
        String toUserAddress = "0x7db79a2f1e9a2a25c5b13bceae1447438e40ccbd";
        // 发送100YOU
        BigInteger amount = BigInteger.valueOf(10000);
        Credentials fromUserCredential = Credentials.create(fromUserKey);
        System.out.println(fromUserCredential.getAddress());
        BigInteger gasPrice = youChain.youGasPrice().send().getGasPrice();
        BigInteger gasLimit = Transfer.GAS_LIMIT;
        BigInteger amountLu = Convert.toLu(new BigDecimal(amount), Convert.Unit.YOU).toBigInteger();

        int attempts = 10, sleepDuration = 1000;
        byte networkId = NetworkId.TESTNET; // 主网请使用ChainId.MAINNET
        TransactionManager transactionManager = new RawTransactionManager(youChain, fromUserCredential, networkId, attempts, sleepDuration);
        YOUSendTransaction youSendTransaction = transactionManager.sendTransaction(gasPrice, gasLimit, toUserAddress, "", amountLu);
        if (youSendTransaction == null || youSendTransaction.hasError()) {
            log.error("发送交易失败");
            return;
        }
//        log.info("youSendTransaction={}", JSONUtil.toJsonStr(youSendTransaction));
        String transactionHash = youSendTransaction.getTransactionHash();
        // 查询交易凭证
        TransactionReceipt transactionReceipt = youChain.youGetTransactionReceipt(transactionHash).send().getTransactionReceipt().orElse(null);
        // 这里应该是返回null的，因为需要等交易确认之后才会有凭证
//        log.info("transactionReceipt={}", JSONUtil.toJsonStr(transactionReceipt));
    }

    /**
     * 发送交易，sleep当前线程直到交易确认成功（返回凭证）或者交易确认失败（抛出异常）
     *
     * @throws Exception
     */
    @Test
    public void sendTransactionAndWaitForResultDemo() throws Exception {
        // 发送者私钥
        String fromUserKey = privateKey;
        // 接收者地址
        String toUserAddress = "0xbbc95c67288bc7613ef9ded879bdfeba776f3b9d";
        // 发送100YOU
        BigInteger amount = BigInteger.valueOf(1000000000);
        Credentials fromUserCredential = Credentials.create(fromUserKey);
        BigInteger gasPrice = youChain.youGasPrice().send().getGasPrice();
        BigInteger gasLimit = Transfer.GAS_LIMIT;
        BigInteger amountLu = Convert.toLu(new BigDecimal(amount), Convert.Unit.YOU).toBigInteger();

        int attempts = 10, sleepDuration = 1000;
        byte networkId = NetworkId.TESTNET; // 主网请使用ChainId.MAINNET
        PollingTransactionReceiptProcessor pollingTransactionReceiptProcessor = new PollingTransactionReceiptProcessor(youChain, attempts, sleepDuration);
        TransactionManager transactionManager = new RawTransactionManager(youChain, fromUserCredential, networkId, pollingTransactionReceiptProcessor);
        // 或者使用简化版
//        RawTransactionManager rawTransactionManager = new RawTransactionManager(youChain, fromUserCredential, networkId, attempts, sleepDuration);
        TransactionReceipt transactionReceipt = transactionManager.executeTransaction(gasPrice, gasLimit, toUserAddress, "", amountLu);
//        log.info("transactionReceipt={}", JSONUtil.toJsonStr(transactionReceipt));
    }

    /**
     * 使用异步回调发送交易并获取结果
     *
     * @throws Exception
     */
    @Test
    public void sendTransactionAndUseCallbackDemo() throws Exception {
        // 发送者私钥
        String fromUserKey = privateKey;
        // 接收者地址
        String toUserAddress = "0x88189a9d3b0ccbc370e67b91211abf7f33fcf6d5";
        // 发送100YOU
        BigInteger amount = BigInteger.valueOf(100);
        Credentials fromUserCredential = Credentials.create(fromUserKey);
        BigInteger gasPrice = youChain.youGasPrice().send().getGasPrice();
        BigInteger gasLimit = Transfer.GAS_LIMIT;
        BigInteger amountLu = Convert.toLu(new BigDecimal(amount), Convert.Unit.YOU).toBigInteger();
        int pollingAttemptsPerTxHash = 50;
        long pollingFrequency = 300;
        byte networkId = NetworkId.TESTNET; // 主网请使用ChainId.MAINNET
        AsyncTransCallback asyncTransCallback = new AsyncTransCallback();
        QueuingTransactionReceiptProcessor queuingTransactionReceiptProcessor = new QueuingTransactionReceiptProcessor(youChain, asyncTransCallback, pollingAttemptsPerTxHash, pollingFrequency);
        TransactionManager transactionManager = new RawTransactionManager(youChain, fromUserCredential, networkId, queuingTransactionReceiptProcessor);
        TransactionReceipt transactionReceipt = transactionManager.executeTransaction(gasPrice, gasLimit, toUserAddress, "", amountLu);
        // 注意：此transactionReceipt是一个EmptyTransactionReceipt，只能用来获取transactionHash
        String transactionHash = transactionReceipt.getTransactionHash();
        log.info("transactionHash={}", transactionHash);
        Thread.sleep(20000);
    }

    public static class AsyncTransCallback implements Callback {

        @Override
        public void accept(TransactionReceipt transactionReceipt) {
//            log.info("transactionReceipt={}", JSONUtil.toJsonStr(transactionReceipt));
        }

        @Override
        public void exception(Exception exception) {
            log.error("交易失败", exception);
        }
    }

    /**
     * 连续发送多笔交易
     *
     * @throws Exception
     */
    @Test
    public void continuousSendTransactionDemo() throws Exception {

        // 发送者私钥
        String fromUserKey = privateKey;
        Credentials fromUserCredential = Credentials.create(fromUserKey);
        BigInteger gasPrice = youChain.youGasPrice().send().getGasPrice();
        BigInteger gasLimit = Transfer.GAS_LIMIT;
        byte networkId = NetworkId.TESTNET; // 主网请使用ChainId.MAINNET
        // 接收者地址A
        String toUserAddressA = "0x88189a9d3b0ccbc370e67b91211abf7f33fcf6d5";
        // 发送100YOU
        BigInteger amountA = BigInteger.valueOf(100);
        BigInteger amountLuA = Convert.toLu(new BigDecimal(amountA), Convert.Unit.YOU).toBigInteger();
        // 接收者地址2
        String toUserAddressB = "0x88189a9d3b0ccbc370e67b91211abf7f33fcf6d5";
        // 发送500YOU
        BigInteger amountB = BigInteger.valueOf(500);
        BigInteger amountLuB = Convert.toLu(new BigDecimal(amountB), Convert.Unit.YOU).toBigInteger();
        TransactionManager transactionManager = new FastRawTransactionManager(youChain, fromUserCredential, networkId);
        YOUSendTransaction youSendTransactionA = transactionManager.sendTransaction(gasPrice, gasLimit, toUserAddressA, "", amountLuA);
        if (youSendTransactionA == null || youSendTransactionA.hasError()) {
            log.error("发送交易to用户A失败");
            return;
        }
        YOUSendTransaction youSendTransactionB = transactionManager.sendTransaction(gasPrice, gasLimit, toUserAddressB, "", amountLuB);
        if (youSendTransactionB == null || youSendTransactionB.hasError()) {
            log.error("发送交易to用户B失败");
            return;
        }
        Thread.sleep(10000); // sleep10秒等交易确认
        TransactionReceipt transactionReceiptA = youChain.youGetTransactionReceipt(youSendTransactionA.getTransactionHash()).send().getResult();
//        log.info("transactionReceiptA={}", JSONUtil.toJsonStr(transactionReceiptA));
        TransactionReceipt transactionReceiptB = youChain.youGetTransactionReceipt(youSendTransactionB.getTransactionHash()).send().getResult();
//        log.info("transactionReceiptB={}", JSONUtil.toJsonStr(transactionReceiptB));
    }

    /**
     * 简易交易--同步
     *
     * @throws Exception
     */
    @Test
    public void transferSyncDemo() throws Exception {
        Transfer transfer = this.createTransfer();
        // 接收者地址A
        String toUserAddress = "0x391694e7e0b0cce554cb130d723a9d27458f9298";
        // 发送200.50YOU
        BigDecimal amount = BigDecimal.valueOf(2000000.50);
        TransactionReceipt transactionReceipt = transfer.sendFunds(toUserAddress, amount, Convert.Unit.YOU).send();
//        log.info("transactionReceipt={}", JSONUtil.toJsonStr(transactionReceipt));
    }

    /**
     * 简易交易--异步
     *
     * @throws Exception
     */
    @Test
    public void transferAsyncDemo() {
        Transfer transfer = this.createTransfer();
        // 接收者地址A
        String toUserAddress = "0xbbc95c67288bc7613ef9ded879bdfeba776f3b9d";
        // 发送800.88YOU
        BigDecimal amount = BigDecimal.valueOf(100000000);
        CompletableFuture<TransactionReceipt> transactionReceiptFuture = transfer.sendFunds(toUserAddress, amount, Convert.Unit.YOU).sendAsync();
        transactionReceiptFuture.whenCompleteAsync((r, e) -> {
            log.info("transactionReceipt={}", 1);
        });
    }

    private Transfer createTransfer() {
        // 发送者私钥
        String fromUserKey = privateKey;
        Credentials fromUserCredential = Credentials.create(fromUserKey);
        log.info(fromUserCredential.getAddress());
        byte networkId = NetworkId.TESTNET; // 主网请使用ChainId.MAINNET
        RawTransactionManager transactionManager = new RawTransactionManager(youChain, fromUserCredential, networkId);
        Transfer transfer = new Transfer(youChain, transactionManager);
        return transfer;
    }

}
