package cc.youchain.tx;

import java.io.IOException;
import java.math.BigInteger;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.DefaultBlockParameter;
import cc.youchain.protocol.core.JsonRpc2_0YOUChain;
import cc.youchain.protocol.core.methods.request.Transaction;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;
import cc.youchain.protocol.exceptions.TransactionException;
import cc.youchain.tx.response.PollingTransactionReceiptProcessor;
import cc.youchain.tx.response.TransactionReceiptProcessor;

/**
 * Transaction manager abstraction for executing transactions with YOUChain client via
 * various mechanisms.
 */
public abstract class TransactionManager {

    public static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 40;
    public static final long DEFAULT_POLLING_FREQUENCY = JsonRpc2_0YOUChain.DEFAULT_BLOCK_TIME;

    private final TransactionReceiptProcessor transactionReceiptProcessor;
    private final String fromAddress;

    protected TransactionManager(
            TransactionReceiptProcessor transactionReceiptProcessor, String fromAddress) {
        this.transactionReceiptProcessor = transactionReceiptProcessor;
        this.fromAddress = fromAddress;
    }

    protected TransactionManager(YOUChain youChain, String fromAddress) {
        this(new PollingTransactionReceiptProcessor(
                        youChain, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH),
                fromAddress);
    }

    protected TransactionManager(
            YOUChain youChain, int attempts, long sleepDuration, String fromAddress) {
        this(new PollingTransactionReceiptProcessor(youChain, sleepDuration, attempts), fromAddress);
    }

    public TransactionReceipt executeTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws IOException, TransactionException {

        YOUSendTransaction youSendTransaction = sendTransaction(
                gasPrice, gasLimit, to, data, value);
        return processResponse(youSendTransaction);
    }

    public abstract YOUSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws IOException;

    public String getFromAddress() {
        return fromAddress;
    }

    private TransactionReceipt processResponse(YOUSendTransaction transactionResponse)
            throws IOException, TransactionException {
        if (transactionResponse == null || transactionResponse.hasError()) {
            throw new RuntimeException("Error processing transaction request: "
                    + transactionResponse.getError().getMessage());
        }

        String transactionHash = transactionResponse.getTransactionHash();

        return transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
    }

    public String sendCall(String to, String data, DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        return transactionReceiptProcessor.getYouChain().youCall(
                Transaction.createYOUCallTransaction(getFromAddress(), to, data),
                defaultBlockParameter)
                .send()
                .getValue();
    }

}
