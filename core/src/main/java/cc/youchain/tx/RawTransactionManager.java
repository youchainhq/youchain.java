package cc.youchain.tx;

import java.io.IOException;
import java.math.BigInteger;

import cc.youchain.crypto.Hash;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.DefaultBlockParameterName;
import cc.youchain.protocol.core.methods.response.YOUGetTransactionCount;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;
import cc.youchain.tx.exceptions.TxHashMismatchException;
import cc.youchain.tx.response.TransactionReceiptProcessor;
import cc.youchain.utils.Numeric;
import cc.youchain.utils.TxHashVerifier;
import cc.youchain.crypto.Credentials;
import cc.youchain.crypto.RawTransaction;
import cc.youchain.crypto.TransactionEncoder;

/**
 * TransactionManager implementation using YOUChain wallet file to create and sign transactions locally.
 */
public class RawTransactionManager extends TransactionManager {

    private final YOUChain youChain;
    final Credentials credentials;

    private final byte networkId;

    protected TxHashVerifier txHashVerifier = new TxHashVerifier();

    public RawTransactionManager(YOUChain youChain, Credentials credentials, byte networkId) {
        super(youChain, credentials.getAddress());

        this.youChain = youChain;
        this.credentials = credentials;

        this.networkId = networkId;
    }

    public RawTransactionManager(
            YOUChain youChain, Credentials credentials, byte networkId,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, credentials.getAddress());

        this.youChain = youChain;
        this.credentials = credentials;

        this.networkId = networkId;
    }

    public RawTransactionManager(
            YOUChain youChain, Credentials credentials, byte networkId, int attempts, long sleepDuration) {
        super(youChain, attempts, sleepDuration, credentials.getAddress());

        this.youChain = youChain;
        this.credentials = credentials;

        this.networkId = networkId;
    }

    public RawTransactionManager(YOUChain youChain, Credentials credentials) {
        this(youChain, credentials, NetworkId.NONE);
    }

    public RawTransactionManager(
            YOUChain youChain, Credentials credentials, int attempts, int sleepDuration) {
        this(youChain, credentials, NetworkId.NONE, attempts, sleepDuration);
    }

    protected BigInteger getNonce() throws IOException {
        YOUGetTransactionCount youGetTransactionCount = youChain.youGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.PENDING).send();

        return youGetTransactionCount.getTransactionCount();
    }

    public TxHashVerifier getTxHashVerifier() {
        return txHashVerifier;
    }

    public void setTxHashVerifier(TxHashVerifier txHashVerifier) {
        this.txHashVerifier = txHashVerifier;
    }

    @Override
    public YOUSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value) throws IOException {

        BigInteger nonce = getNonce();

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                value,
                data);

        return signAndSend(rawTransaction);
    }

    /*
     * @param rawTransaction a RawTransaction istance to be signed
     * @return The transaction signed and encoded without ever broadcasting it
     */
    public String sign(RawTransaction rawTransaction) {

        byte[] signedMessage;

        if (networkId > NetworkId.NONE) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, networkId, credentials);
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }

        return Numeric.toHexString(signedMessage);
    }

    public YOUSendTransaction signAndSend(RawTransaction rawTransaction)
            throws IOException {
        String hexValue = sign(rawTransaction);
        YOUSendTransaction youSendTransaction = youChain.youSendRawTransaction(hexValue).send();

        if (youSendTransaction != null && !youSendTransaction.hasError()) {
            String txHashLocal = Hash.sha3(hexValue);
            String txHashRemote = youSendTransaction.getTransactionHash();
            if (!this.getTxHashVerifier().verify(txHashLocal, txHashRemote)) {
                throw new TxHashMismatchException(txHashLocal, txHashRemote);
            }
        }

        return youSendTransaction;
    }
}
