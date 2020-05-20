package cc.youchain.integration.demos;

import cc.youchain.abi.EventEncoder;
import cc.youchain.abi.TypeReference;
import cc.youchain.abi.datatypes.Address;
import cc.youchain.abi.datatypes.Event;
import cc.youchain.abi.datatypes.generated.Uint256;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.DefaultBlockParameter;
import cc.youchain.protocol.core.DefaultBlockParameterName;
import cc.youchain.protocol.core.methods.request.YOUFilter;
import cc.youchain.protocol.core.methods.response.*;
import cc.youchain.protocol.http.HttpService;
import cc.youchain.utils.Numeric;
import io.reactivex.Flowable;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class EventFilterDemos {

    private static final String CONTRACT_ADDRESS = "0x7db79a2f1e9a2a25c5b13bceae1447438e40ccbd";
    private static final Logger logger = LoggerFactory.getLogger(TransERC20ContractDemos.class);
    private static String nodeUrl = "http://localhost:8283/";
    private static HttpService httpService = new HttpService(nodeUrl);
    private static YOUChain youChain = YOUChain.build(httpService);

    @Test
    public void testTransferEventFilter() throws Exception {
        Event event = new Event("Transfer", Arrays.asList(
                new TypeReference<Address>() {
                },
                new TypeReference<Address>() {
                },
                new TypeReference<Uint256>() {
                }));
        String encodedEventSignature = EventEncoder.encode(event);
        BigInteger startBlock = BigInteger.valueOf(24850);
        BigInteger pageSize = BigInteger.valueOf(200); // 每页200条
        BigInteger endBlock = this.getBlockNum();
        int num = Integer.parseInt(String.valueOf(endBlock.subtract(startBlock).divide(pageSize)));

        // 分页（200个区块每页）拉取旧区块的上数据 防止数量太多
        for (int i = 0; i < num; i++) {
            BigInteger sectionEndBlock = startBlock.add(pageSize);
            YOUFilter youFilter = new YOUFilter(
                    DefaultBlockParameter.valueOf(startBlock),
                    DefaultBlockParameter.valueOf(sectionEndBlock),
                    CONTRACT_ADDRESS);
            youFilter.addSingleTopic(encodedEventSignature);
            startBlock = sectionEndBlock;
            Flowable<Log> flowable = youChain.youLogFlowable(youFilter);
            flowable.subscribe(log -> {
                this.handleTransferEvents(log);
            });
        }

        // 获取最新的数据以及监听新触发的数据
        YOUFilter youFilter = new YOUFilter(
                DefaultBlockParameter.valueOf(startBlock),
                DefaultBlockParameterName.LATEST,
                CONTRACT_ADDRESS);
        youFilter.addSingleTopic(encodedEventSignature);
        Flowable<Log> flowable = youChain.youLogFlowable(youFilter);
        flowable.subscribe(log -> {
            this.handleTransferEvents(log);
        });

        Thread.sleep(10000000);
    }

    private BigInteger getBlockNum() throws Exception {
        YOUBlockNumber youBlockNumber = youChain.youBlockNumber().send();
        return youBlockNumber.getBlockNumber();
    }

    private void handleTransferEvents(Log log) {
        logger.info("logs={}", log.getTopics().toString());
        logger.info("subscribe handled");
        BigInteger blockNumber = log.getBlockNumber();
        List<String> topics = log.getTopics();
        String fromAddress = topics.get(1);
        String toAddress = topics.get(2);
        String value = log.getData();
        BigDecimal amount = new BigDecimal(new BigInteger(value.substring(2), 16)).divide(BigDecimal.valueOf(1000000000000000000.0), 18, BigDecimal.ROUND_HALF_EVEN);
        String timestamp = "";
        try {
            YOUBlock youBlock = youChain.youGetBlockByNumber(DefaultBlockParameter.valueOf(log.getBlockNumber()), false).send();
            timestamp = String.valueOf(youBlock.getBlock().getTimestamp());
        } catch (IOException e) {
            logger.warn("Block timestamp get failure, block number is {}", log.getBlockNumber());
            logger.error("Block timestamp get failure,{}", e);
        }
        logger.info("blockNumber={}", blockNumber);
        logger.info("fromAddress={}", fromAddress);
        logger.info("toAddress={}", toAddress);
        logger.info("amount={}", amount);
        logger.info("timestamp={}", timestamp);
    }

    @Test
    public void testBlockFlowable() throws Exception {
        Flowable<YOUBlock> flowable = youChain.blockFlowable(false);
        flowable.subscribe(youBlock -> {
            logger.info("blockNumber={}", youBlock.getBlock().getNumber().toString());
        });
        Thread.sleep(10000000);
    }

    @Test
    public void testTransactionFlowable() throws Exception {
        Flowable<Transaction> flowable = youChain.transactionFlowable();
        flowable.subscribe(tx -> {
            logger.info("blockNumber={} txHash={}", tx.getBlockNumber().toString(), tx.getHash());
        });
        Thread.sleep(10000000);
    }

    @Test
    public void testPendingTransactionFlowable() throws Exception {
        Flowable<Transaction> flowable = youChain.pendingTransactionFlowable();
        flowable.subscribe(tx -> {
            logger.info("blockNumber={} txHash={}", tx.getBlockNumber().toString(), tx.getHash());
        });
        Thread.sleep(10000000);
    }

    @Test
    public void testYouGetLogs() throws Exception {
        String userAddress = "0x7db79a2f1e9a2a25c5b13bceae1447438e40ccbd";
        YOUFilter youFilter = new YOUFilter(
                DefaultBlockParameter.valueOf(BigInteger.ONE),
                DefaultBlockParameterName.LATEST,
                userAddress);
        YOULog youLog = youChain.youGetLogs(youFilter).send();
        Assert.assertNotNull(youLog);
        youLog.getResult().forEach(logResult -> {
            logger.info("logResult={}", logResult);
        });
    }

    @Test
    public void testNewPendingTransactionFilter() throws Exception {
        cc.youchain.protocol.core.methods.response.YOUFilter youFilter = youChain.youNewPendingTransactionFilter().send();
        BigInteger filterId = youFilter.getFilterId();
        String filterIdStr = Numeric.toHexStringWithPrefix(filterId);
        logger.info("filterId = {}", filterIdStr);
//        Thread.sleep(3000);
        // 轮询
        for (int i = 0; i < 10000; i++) {
            YOULog youLog = youChain.youGetFilterChanges(filterId).send();
            logger.info("youlog={}", youLog.getResult().size());
            youLog.getLogs().forEach(youLg -> {
                logger.info("youLg");
            });
            Thread.sleep(2000);
        }
    }

    @Test
    public void testNewFilter() throws Exception {
        String address = "0x88189a9d3b0ccbc370e67b91211abf7f33fcf6d5";
        YOUFilter youFilter = new YOUFilter(
                DefaultBlockParameter.valueOf(BigInteger.valueOf(30000)),
                DefaultBlockParameterName.LATEST,
                address);
        cc.youchain.protocol.core.methods.response.YOUFilter youFilterResp = youChain.youNewFilter(youFilter).send();
        BigInteger filterId = youFilterResp.getFilterId();
        String filterIdStr = Numeric.toHexStringWithPrefix(filterId);
        logger.info("filterId = {}", filterIdStr);
//        Thread.sleep(3000);
        for (int i = 0; i < 10000; i++) {
            YOULog youLog = youChain.youGetFilterLogs(filterId).send();
            logger.info("youlog={}", youLog.getResult().size());
            youLog.getLogs().forEach(youLg -> {
                logger.info("youLg");
            });
            Thread.sleep(2000);
        }
    }

    @Test
    public void testReplayBlockFilter() {
        BigInteger startBlock = BigInteger.valueOf(24850);
        DefaultBlockParameter start = DefaultBlockParameter.valueOf(startBlock);
        Flowable<YOUBlock> flowable = youChain.replayPastBlocksFlowable(start, DefaultBlockParameterName.LATEST, false);
        flowable.subscribe(block -> {
            logger.info("blockNumber={}", block.getBlock().getNumber().toString());
        });
    }

    @Test
    public void testReplayPastAndFutureBlockFilter() {
        BigInteger startBlock = BigInteger.valueOf(24850);
        DefaultBlockParameter start = DefaultBlockParameter.valueOf(startBlock);
        Flowable<YOUBlock> flowable = youChain.replayPastAndFutureBlocksFlowable(start, false);
        flowable.subscribe(block -> {
            logger.info("blockNumber={}", block.getBlock().getNumber().toString());
        });
    }

    @Test
    public void testReplayTransactionFilter() {
        BigInteger startBlock = BigInteger.valueOf(24850);
        DefaultBlockParameter start = DefaultBlockParameter.valueOf(startBlock);
        Flowable<Transaction> flowable = youChain.replayPastTransactionsFlowable(start, DefaultBlockParameterName.LATEST);
        flowable.subscribe(tx -> {
            logger.info("blockNumber={} txHash={}", tx.getBlockNumber().toString(), tx.getHash());
        });
    }

    @Test
    public void testReplayPastAndFutureTransactionBlockFilter() {
        BigInteger startBlock = BigInteger.valueOf(24850);
        DefaultBlockParameter start = DefaultBlockParameter.valueOf(startBlock);
        Flowable<Transaction> flowable = youChain.replayPastAndFutureTransactionsFlowable(start);
        flowable.subscribe(tx -> {
            logger.info("blockNumber={} txHash={}", tx.getBlockNumber().toString(), tx.getHash());
        });
    }
}
