package cc.youchain.tx.response;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;

/**
 * Return an {@link EmptyTransactionReceipt} receipt back to callers containing only the transaction hash.
 */
public class NoOpProcessor extends TransactionReceiptProcessor {

    public NoOpProcessor(YOUChain youChain) {
        super(youChain);
    }

    @Override
    public TransactionReceipt waitForTransactionReceipt(String transactionHash) {
        return new EmptyTransactionReceipt(transactionHash);
    }
}
