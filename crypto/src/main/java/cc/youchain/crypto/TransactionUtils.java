package cc.youchain.crypto;

import cc.youchain.utils.Numeric;

/**
 * Transaction utility functions.
 */
public class TransactionUtils {

    /**
     * Utility method to provide the transaction hash for a given transaction.
     *
     * @param rawTransaction we wish to send
     * @param credentials of the sender
     * @return encoded transaction hash
     */
    public static byte[] generateTransactionHash(
            RawTransaction rawTransaction, Credentials credentials) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Hash.sha3(signedMessage);
    }

    /**
     * Utility method to provide the transaction hash for a given transaction.
     *
     * @param rawTransaction we wish to send
     * @param networkId of the intended chain
     * @param credentials of the sender
     * @return encoded transaction hash
     */
    public static byte[] generateTransactionHash(
            RawTransaction rawTransaction, byte networkId, Credentials credentials) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, networkId, credentials);
        return Hash.sha3(signedMessage);
    }

    /**
     * Utility method to provide the transaction hash for a given transaction.
     *
     * @param rawTransaction we wish to send
     * @param credentials of the sender
     * @return transaction hash as a hex encoded string
     */
    public static String generateTransactionHashHexEncoded(
            RawTransaction rawTransaction, Credentials credentials) {
        return Numeric.toHexString(generateTransactionHash(rawTransaction, credentials));
    }

    /**
     * Utility method to provide the transaction hash for a given transaction.
     *
     * @param rawTransaction we wish to send
     * @param networkId of the intended chain
     * @param credentials of the sender
     * @return transaction hash as a hex encoded string
     */
    public static String generateTransactionHashHexEncoded(
            RawTransaction rawTransaction, byte networkId, Credentials credentials) {
        return Numeric.toHexString(generateTransactionHash(rawTransaction, networkId, credentials));
    }
}
