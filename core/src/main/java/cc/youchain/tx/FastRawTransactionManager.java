package cc.youchain.tx;

import java.io.IOException;
import java.math.BigInteger;

import cc.youchain.protocol.YOUChain;
import cc.youchain.tx.response.TransactionReceiptProcessor;
import cc.youchain.crypto.Credentials;

/**
 * Simple RawTransactionManager derivative that manages nonces to facilitate multiple transactions per block.
 */
public class FastRawTransactionManager extends RawTransactionManager {

    private volatile BigInteger nonce = BigInteger.valueOf(-1);

    public FastRawTransactionManager(YOUChain youChain, Credentials credentials, byte networkId) {
        super(youChain, credentials, networkId);
    }

    public FastRawTransactionManager(YOUChain youChain, Credentials credentials) {
        super(youChain, credentials);
    }

    public FastRawTransactionManager(
            YOUChain youChain, Credentials credentials,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(youChain, credentials, NetworkId.NONE, transactionReceiptProcessor);
    }

    public FastRawTransactionManager(
            YOUChain youChain, Credentials credentials, byte networkId,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(youChain, credentials, networkId, transactionReceiptProcessor);
    }

    @Override
    protected synchronized BigInteger getNonce() throws IOException {
        if (nonce.signum() == -1) {
            // obtain lock
            nonce = super.getNonce();
        } else {
            nonce = nonce.add(BigInteger.ONE);
        }
        return nonce;
    }

    public BigInteger getCurrentNonce() {
        return nonce;
    }

    public synchronized void resetNonce() throws IOException {
        nonce = super.getNonce();
    }

    public synchronized void setNonce(BigInteger value) {
        nonce = value;
    }
}
