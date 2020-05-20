package cc.youchain.integration.demos;

import cc.youchain.crypto.Credentials;
import cc.youchain.crypto.RawTransaction;
import cc.youchain.crypto.TransactionEncoder;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.DefaultBlockParameterName;
import cc.youchain.protocol.core.methods.response.*;
import cc.youchain.protocol.http.HttpService;
import cc.youchain.protocol.websocket.WebSocketService;
import cc.youchain.protocol.websocket.events.*;
import cc.youchain.protocol.websocket.events.Log;
import cc.youchain.tx.Transfer;
import cc.youchain.utils.Convert;
import cc.youchain.utils.Numeric;
import io.reactivex.Flowable;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebSocketNotificationDemos {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketNotificationDemos.class);
    private static String wsNodeUrl = "ws://localhost:8284/ws";
    private static WebSocketService webSocketService = new WebSocketService(wsNodeUrl, Boolean.TRUE);
    private static YOUChain youChainWs = YOUChain.build(webSocketService);
    String nodeUrlRpc = "http://localhost:8283/";
    HttpService httpServiceRpc = new HttpService(nodeUrlRpc);
    YOUChain youChainRpc = YOUChain.build(httpServiceRpc);

    @Before
    public void before() throws ConnectException {
        webSocketService.connect();
    }

    @Test
    public void testNewHeadsNotifications() throws Exception {
        Flowable<NewHeadsNotification> flowable = youChainWs.newHeadsNotifications();
        flowable.subscribe(notfication -> {
                    logger.info("newHeadsNotification");
                    NewHead newHead = notfication.getParams().getResult();
                    BigInteger transCount = youChainRpc.youGetBlockTransactionCountByHash(newHead.getHash()).send().getTransactionCount();
                    logger.info("transCount={}", transCount.toString());
                    if (transCount.compareTo(BigInteger.ZERO) > 0) {
                        YOUBlock youBlock = youChainRpc.youGetBlockByHash(newHead.getHash(), Boolean.TRUE).send();
                        List<YOUBlock.TransactionResult> transactionResultList = youBlock.getBlock().getTransactions();
                        transactionResultList.forEach(transactionResult -> {
                            YOUBlock.TransactionObject transactionObject = (YOUBlock.TransactionObject) transactionResult;
                            logger.info("Transaction from:{} --> to:{} value={}", transactionObject.getFrom(), transactionObject.getTo(), transactionObject.getValue().toString());
                        });
                    }
                },
                error -> {
                    logger.error("onError");
                });

        Thread.sleep(1000000);
    }

    @Test
    public void testPendingTransactionNotifications() throws Exception {
        Flowable<PendingTransactionNotification> flowable = youChainWs.newPendingTransactionsNotifications();
        flowable.subscribe(notfication -> {
                    logger.info("transaction hash={}", notfication.getParams().getResult());
                },
                error -> {
                    logger.error("onError");
                });
        this.heartBeat();
    }

    @Test
    public void testSyncingStatusNotifications() throws Exception {
        Flowable<SyncingNotfication> flowable = youChainWs.syncingStatusNotifications();
        flowable.subscribe(notfication -> {
                    YOUSyncing youSyncing = notfication.getParams().getResult();
                    logger.info("isSyncing={}", youSyncing.isSyncing());
                    YOUSyncing.Result result = youSyncing.getResult();
                    if (result instanceof YOUSyncing.Syncing) {
                        YOUSyncing.Syncing syncing = (YOUSyncing.Syncing) result;
                        logger.info("startingBlock={}", syncing.getStartingBlock());
                        logger.info("currentBlock={}", syncing.getCurrentBlock());
                        logger.info("highestBlock={}", syncing.getHighestBlock());
                    }
                },
                error -> {
                    logger.error("onError testSyncingStatusNotifications");
                });
        this.heartBeat();
    }

    // 订阅合约日志 合约交易使用TransTestERC20TokenContractDemos.testTransfer调试
    @Test
    public void testLogsNotifications() throws Exception {
        String address = "0x88189a9d3b0ccbc370e67b91211abf7f33fcf6d5";
        String contractAddress = "0x238fa6dcd36fe92851127a5b663beee40e2e3302";

        // 获取最新的数据以及监听新触发的数据
        Flowable<LogNotification> flowable = youChainWs.logsNotifications(
                Arrays.asList(address,
                        contractAddress), new ArrayList<>());

        flowable.subscribe(logs -> {
                    logger.info("LogNotification handled");
                    Log log = logs.getParams().getResult();
                    List<String> topics = log.getTopics();
                    String fromAddress = topics.get(1);
                    String toAddress = topics.get(2);
                    String addressLog = log.getAddress();
                    String transactionHash = log.getTransactionHash();
                    String blockHash = log.getBlockHash();
                    logger.info("blockHash={}", blockHash);
                    logger.info("transactionHash={}", transactionHash);
                    logger.info("addressLog={}", addressLog);
                    logger.info("fromAddress={}", fromAddress);
                    logger.info("toAddress={}", toAddress);
                },
                error -> {
                    logger.error("on Error");
                });
        this.heartBeat();
    }

    // 心跳一下防止websocket断线
    private void heartBeat() throws InterruptedException, IOException {
        for (; ; ) {
            BigInteger blockNumber = youChainWs.youBlockNumber().send().getBlockNumber();
            logger.info("blockNumber={}", blockNumber.toString());
            Thread.sleep(10000);
        }
    }

    @Test
    public void testClientVersion() throws Exception {
        YOUChainClientVersion youChainClientVersion = youChainWs.youChainClientVersion().send();
        logger.info("version={}", youChainClientVersion.getYOUChainClientVersion());
    }

    @Test
    public void testTrans() throws Exception {
        // 发送者私钥
        String fromUserKey = "0x211454223878c7e499458219809487c8ef8c79cb26dde3b4cb7b3b5cd9d87f33";
        // 接收者地址
        String toUserAddress = "0x88189a9d3b0ccbc370e67b91211abf7f33fcf6d5";
        // 发送100YOU
        BigInteger amount = BigInteger.valueOf(120);
        Credentials fromUserCredential = Credentials.create(fromUserKey);
        String addressFrom = fromUserCredential.getAddress();
        BigInteger gasPrice = youChainWs.youGasPrice().send().getGasPrice();
        BigInteger gasLimit = Transfer.GAS_LIMIT;
        BigInteger amountLu = Convert.toLu(new BigDecimal(amount), Convert.Unit.YOU).toBigInteger();
        BigInteger nonce = youChainWs.youGetTransactionCount(addressFrom, DefaultBlockParameterName.LATEST).send().getTransactionCount();

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce, gasPrice, gasLimit, toUserAddress, amountLu, "");
        // 签名交易数据
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, fromUserCredential);
        String signedTransactionData = Numeric.toHexString(signMessage);

        YOUSendTransaction youSendTransaction = youChainWs.youSendRawTransaction(signedTransactionData).send();
        String transactionHash = youSendTransaction.getTransactionHash();
        logger.info("transactionHash={}", transactionHash);
        YOUGetTransactionReceipt transactionReceipt = youChainWs.youGetTransactionReceipt(transactionHash).sendAsync().get();
        logger.info("transactionReceipt={}", transactionReceipt.getTransactionReceipt().isPresent());
    }

    // 采用轮询机制(10s)，实时性没有Notification好
    @Test
    public void testBlockHashFlowable() throws Exception {
        Flowable<String> flowable = youChainWs.youBlockHashFlowable();
        flowable.subscribe(tx -> {
            logger.info("blockHash={}", tx);
        });
        Thread.sleep(10000000);
    }

    // 采用轮询机制(10s)，实时性没有Notification好
    @Test
    public void testTransactionFlowable() throws Exception {
        Flowable<Transaction> flowable = youChainWs.transactionFlowable();
        flowable.subscribe(tx -> {
            logger.info("blockNumber={} txHash={}", tx.getBlockNumber().toString(), tx.getHash());
        });
        Thread.sleep(10000000);
    }

    // 采用轮询机制(10s)，实时性没有Notification好
    @Test
    public void testPendingTransactionHashFlowable() throws Exception {
        Flowable<String> flowable = youChainWs.youPendingTransactionHashFlowable();
        flowable.subscribe(tx -> {
                    logger.info("txHash={}", tx);
                },
                error -> {
                    logger.error("onError");
                });
        Thread.sleep(10000000);
    }
}
