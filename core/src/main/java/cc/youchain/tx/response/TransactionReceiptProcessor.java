package cc.youchain.tx.response;

import java.io.IOException;
import java.util.Optional;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.methods.response.YOUGetTransactionReceipt;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;
import cc.youchain.protocol.exceptions.TransactionException;

/**
 * Abstraction for managing how we wait for transaction receipts to be generated on the network.
 */
public abstract class TransactionReceiptProcessor {

    private final YOUChain youChain;

    public TransactionReceiptProcessor(YOUChain youChain) {
        this.youChain = youChain;
    }

    public abstract TransactionReceipt waitForTransactionReceipt(
            String transactionHash)
            throws IOException, TransactionException;

    Optional<TransactionReceipt> sendTransactionReceiptRequest(
            String transactionHash) throws IOException, TransactionException {
        YOUGetTransactionReceipt transactionReceipt =
                youChain.youGetTransactionReceipt(transactionHash).send();
        if (transactionReceipt.hasError()) {
            throw new TransactionException("Error processing request: "
                    + transactionReceipt.getError().getMessage());
        }

        return transactionReceipt.getTransactionReceipt();
    }

    public YOUChain getYouChain() {
        return this.youChain;
    }
}
