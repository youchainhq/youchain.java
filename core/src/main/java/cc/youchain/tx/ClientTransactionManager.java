package cc.youchain.tx;

import java.io.IOException;
import java.math.BigInteger;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.methods.request.Transaction;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;
import cc.youchain.tx.response.TransactionReceiptProcessor;

/**
 * TransactionManager implementation for using an YOUChain node to transact.
 *
 * <p><b>Note</b>: accounts must be unlocked on the node for transactions to be successful.
 */
public class ClientTransactionManager extends TransactionManager {

    private final YOUChain youChain;

    public ClientTransactionManager(
            YOUChain youChain, String fromAddress) {
        super(youChain, fromAddress);
        this.youChain = youChain;
    }

    public ClientTransactionManager(
            YOUChain youChain, String fromAddress, int attempts, int sleepDuration) {
        super(youChain, attempts, sleepDuration, fromAddress);
        this.youChain = youChain;
    }

    public ClientTransactionManager(
            YOUChain youChain, String fromAddress,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, fromAddress);
        this.youChain = youChain;
    }

    @Override
    public YOUSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws IOException {

        Transaction transaction = new Transaction(
                getFromAddress(), null, gasPrice, gasLimit, to, value, data);

        return youChain.youSendTransaction(transaction)
                .send();
    }
}
