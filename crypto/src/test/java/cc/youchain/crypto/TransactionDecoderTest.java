package cc.youchain.crypto;

import cc.youchain.utils.Numeric;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class TransactionDecoderTest {

    @Test
    public void testDecoding() throws Exception {
        BigInteger nonce = BigInteger.ZERO;
        BigInteger gasPrice = BigInteger.ONE;
        BigInteger gasLimit = BigInteger.TEN;
        BigInteger value = BigInteger.valueOf(10);
        RawTransaction rawTransaction = RawTransaction.createYOUTransaction(nonce, gasPrice, gasLimit, TestKeys.ADDRESS, value);
        byte[] encodedMessage = TransactionEncoder.encode(rawTransaction);
        String hexMessage = Numeric.toHexString(encodedMessage);

        RawTransaction result = TransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(TestKeys.ADDRESS, result.getTo());
        assertEquals(value, result.getValue());
        assertEquals("", result.getData());

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, TestKeys.CREDENTIALS);
        hexMessage = Numeric.toHexString(signedMessage);
        result = TransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(TestKeys.ADDRESS, result.getTo());
        assertEquals(value, result.getValue());
        assertEquals("", result.getData());
        assertTrue(result instanceof SignedRawTransaction);
        SignedRawTransaction signedResult = (SignedRawTransaction) result;
        assertNotNull(signedResult.getSignatureData());
        Sign.SignatureData signatureData = signedResult.getSignatureData();
        byte[] encodedTransaction = TransactionEncoder.encode(rawTransaction);
        BigInteger key = Sign.signedMessageToKey(encodedTransaction, signatureData);
        assertEquals(key, TestKeys.PUBLIC_KEY);
        assertEquals(TestKeys.ADDRESS, signedResult.getFrom());
        signedResult.verify(TestKeys.ADDRESS);
        assertNull(signedResult.getChainId());
    }

}
