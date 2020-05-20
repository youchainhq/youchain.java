package cc.youchain.tx;

import cc.youchain.crypto.TestKeys;
import cc.youchain.protocol.YOUChain;
import cc.youchain.tx.response.TransactionReceiptProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;

import static org.mockito.Mockito.mock;

public class FastRawTransactionManagerTest extends SuperMockTester {

    YOUChain youChain = mock(YOUChain.class);
    TransactionReceiptProcessor transactionReceiptProcessor = mock(TransactionReceiptProcessor.class);

    @Test
    public void testAll() throws IOException {
        mockNonceRequest(youChain);
        FastRawTransactionManager fastRawTransactionManager = new FastRawTransactionManager(youChain, TestKeys.CREDENTIALS);
        BigInteger nonce = fastRawTransactionManager.getNonce();
        Assert.assertTrue(nonce.compareTo(BigInteger.ZERO) > 0);
        nonce = fastRawTransactionManager.getNonce();
        Assert.assertTrue(nonce.compareTo(BigInteger.ZERO) > 0);
        nonce = fastRawTransactionManager.getCurrentNonce();
        Assert.assertTrue(nonce.compareTo(BigInteger.ZERO) > 0);
        fastRawTransactionManager.resetNonce();
        fastRawTransactionManager.setNonce(BigInteger.TEN);
        nonce = fastRawTransactionManager.getNonce();
        Assert.assertEquals(nonce, BigInteger.TEN.add(BigInteger.ONE));
        fastRawTransactionManager = new FastRawTransactionManager(youChain, TestKeys.CREDENTIALS, NetworkId.TESTNET);
        fastRawTransactionManager = new FastRawTransactionManager(youChain, TestKeys.CREDENTIALS, transactionReceiptProcessor);
        fastRawTransactionManager = new FastRawTransactionManager(youChain, TestKeys.CREDENTIALS, NetworkId.TESTNET, transactionReceiptProcessor);
        nonce = fastRawTransactionManager.getNonce();
        Assert.assertTrue(nonce.compareTo(BigInteger.ZERO) > 0);

    }
}
