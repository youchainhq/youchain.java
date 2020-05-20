package cc.youchain.protocol.rx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.DefaultBlockParameter;
import cc.youchain.protocol.core.DefaultBlockParameterName;
import cc.youchain.protocol.core.DefaultBlockParameterNumber;
import cc.youchain.protocol.core.filters.BlockFilter;
import cc.youchain.protocol.core.filters.Filter;
import cc.youchain.protocol.core.filters.LogFilter;
import cc.youchain.protocol.core.filters.PendingTransactionFilter;
import cc.youchain.protocol.core.methods.request.YOUFilter;
import cc.youchain.protocol.core.methods.response.YOUBlock;
import cc.youchain.protocol.core.methods.response.Log;
import cc.youchain.protocol.core.methods.response.Transaction;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import cc.youchain.utils.Flowables;

/**
 * YOUChain reactive API implementation.
 */
public class JsonRpc2_0Rx {

    private final YOUChain youChain;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Scheduler scheduler;

    public JsonRpc2_0Rx(YOUChain youChain, ScheduledExecutorService scheduledExecutorService) {
        this.youChain = youChain;
        this.scheduledExecutorService = scheduledExecutorService;
        this.scheduler = Schedulers.from(scheduledExecutorService);
    }

    public Flowable<String> youBlockHashFlowable(long pollingInterval) {
        return Flowable.create(subscriber -> {
            BlockFilter blockFilter = new BlockFilter(
                    youChain, subscriber::onNext);
            run(blockFilter, subscriber, pollingInterval);
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<String> youPendingTransactionHashFlowable(long pollingInterval) {
        return Flowable.create(subscriber -> {
            PendingTransactionFilter pendingTransactionFilter = new PendingTransactionFilter(
                    youChain, subscriber::onNext);

            run(pendingTransactionFilter, subscriber, pollingInterval);
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<Log> youLogFlowable(
            YOUFilter youFilter, long pollingInterval) {
        return Flowable.create(subscriber -> {
            LogFilter logFilter = new LogFilter(
                    youChain, subscriber::onNext, youFilter);

            run(logFilter, subscriber, pollingInterval);
        }, BackpressureStrategy.BUFFER);
    }

    private <T> void run(
            Filter<T> filter, FlowableEmitter<? super T> emitter,
            long pollingInterval) {

        filter.run(scheduledExecutorService, pollingInterval);
        emitter.setCancellable(filter::cancel);
    }

    public Flowable<Transaction> transactionFlowable(long pollingInterval) {
        return blockFlowable(true, pollingInterval)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Flowable<Transaction> pendingTransactionFlowable(long pollingInterval) {
        return youPendingTransactionHashFlowable(pollingInterval)
                .flatMap(transactionHash ->
                        youChain.youGetTransactionByHash(transactionHash).flowable())
                .filter(youTransaction -> youTransaction.getTransaction().isPresent())
                .map(youTransaction -> youTransaction.getTransaction().get());
    }

    public Flowable<YOUBlock> blockFlowable(
            boolean fullTransactionObjects, long pollingInterval) {
        return youBlockHashFlowable(pollingInterval)
                .flatMap(blockHash ->
                        youChain.youGetBlockByHash(blockHash, fullTransactionObjects).flowable());
    }

    public Flowable<YOUBlock> replayBlocksFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksFlowable(startBlock, endBlock, fullTransactionObjects, true);
    }

    public Flowable<YOUBlock> replayBlocksFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {
        // We use a scheduler to ensure this Flowable runs asynchronously for users to be
        // consistent with the other Flowables
        return replayBlocksFlowableSync(startBlock, endBlock, fullTransactionObjects, ascending)
                .subscribeOn(scheduler);
    }

    private Flowable<YOUBlock> replayBlocksFlowableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksFlowableSync(startBlock, endBlock, fullTransactionObjects, true);
    }

    private Flowable<YOUBlock> replayBlocksFlowableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {

        BigInteger startBlockNumber = null;
        BigInteger endBlockNumber = null;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            endBlockNumber = getBlockNumber(endBlock);
        } catch (IOException e) {
            Flowable.error(e);
        }

        if (ascending) {
            return Flowables.range(startBlockNumber, endBlockNumber)
                    .flatMap(i -> youChain.youGetBlockByNumber(
                            new DefaultBlockParameterNumber(i),
                            fullTransactionObjects).flowable());
        } else {
            return Flowables.range(startBlockNumber, endBlockNumber, false)
                    .flatMap(i -> youChain.youGetBlockByNumber(
                            new DefaultBlockParameterNumber(i),
                            fullTransactionObjects).flowable());
        }
    }

    public Flowable<Transaction> replayTransactionsFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return replayBlocksFlowable(startBlock, endBlock, true)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Flowable<YOUBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Flowable<YOUBlock> onCompleteFlowable) {
        // We use a scheduler to ensure this Flowable runs asynchronously for users to be
        // consistent with the other Flowables
        return replayPastBlocksFlowableSync(
                startBlock, fullTransactionObjects, onCompleteFlowable)
                .subscribeOn(scheduler);
    }

    public Flowable<YOUBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return replayPastBlocksFlowable(
                startBlock, fullTransactionObjects, Flowable.empty());
    }

    private Flowable<YOUBlock> replayPastBlocksFlowableSync(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Flowable<YOUBlock> onCompleteFlowable) {

        BigInteger startBlockNumber;
        BigInteger latestBlockNumber;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            latestBlockNumber = getLatestBlockNumber();
        } catch (IOException e) {
            return Flowable.error(e);
        }

        if (startBlockNumber.compareTo(latestBlockNumber) > -1) {
            return onCompleteFlowable;
        } else {
            return Flowable.concat(
                    replayBlocksFlowableSync(
                            new DefaultBlockParameterNumber(startBlockNumber),
                            new DefaultBlockParameterNumber(latestBlockNumber),
                            fullTransactionObjects),
                    Flowable.defer(() -> replayPastBlocksFlowableSync(
                            new DefaultBlockParameterNumber(latestBlockNumber.add(BigInteger.ONE)),
                            fullTransactionObjects,
                            onCompleteFlowable)));
        }
    }

    public Flowable<Transaction> replayPastTransactionsFlowable(
            DefaultBlockParameter startBlock) {
        return replayPastBlocksFlowable(
                startBlock, true, Flowable.empty())
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Flowable<YOUBlock> replayPastAndFutureBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            long pollingInterval) {

        return replayPastBlocksFlowable(
                startBlock, fullTransactionObjects,
                blockFlowable(fullTransactionObjects, pollingInterval));
    }

    public Flowable<Transaction> replayPastAndFutureTransactionsFlowable(
            DefaultBlockParameter startBlock, long pollingInterval) {
        return replayPastAndFutureBlocksFlowable(
                startBlock, true, pollingInterval)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    private BigInteger getLatestBlockNumber() throws IOException {
        return getBlockNumber(DefaultBlockParameterName.LATEST);
    }

    private BigInteger getBlockNumber(
            DefaultBlockParameter defaultBlockParameter) throws IOException {
        if (defaultBlockParameter instanceof DefaultBlockParameterNumber) {
            return ((DefaultBlockParameterNumber) defaultBlockParameter).getBlockNumber();
        } else {
            YOUBlock latestYOUBlock = youChain.youGetBlockByNumber(
                    defaultBlockParameter, false).send();
            return latestYOUBlock.getBlock().getNumber();
        }
    }

    private static List<Transaction> toTransactions(YOUBlock youBlock) {
        // If you ever see an exception thrown here, it's probably due to an incomplete chain in
        // Geth/Parity. You should resync to solve.
        return youBlock.getBlock().getTransactions().stream()
                .map(transactionResult -> (Transaction) transactionResult.get())
                .collect(Collectors.toList());
    }
}
