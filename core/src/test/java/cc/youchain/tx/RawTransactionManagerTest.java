package cc.youchain.tx;

import cc.youchain.crypto.TestKeys;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;
import cc.youchain.tx.exceptions.TxHashMismatchException;
import cc.youchain.tx.response.TransactionReceiptProcessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RawTransactionManagerTest extends SuperMockTester {

    YOUChain youChain = mock(YOUChain.class);

    @Before
    public void setUp() throws Exception {
        mockNonceRequest(youChain);
        mockSendRawTransactionRequest(youChain);
    }

    @Test(expected = TxHashMismatchException.class)
    public void testSendTransaction() throws IOException {
        RawTransactionManager rawTransactionManager = new RawTransactionManager(youChain, TestKeys.CREDENTIALS);
        rawTransactionManager = new RawTransactionManager(youChain, TestKeys.CREDENTIALS, NetworkId.TESTNET, mock(TransactionReceiptProcessor.class));
        YOUSendTransaction youSendTransaction = rawTransactionManager.sendTransaction(BigInteger.valueOf(10), BigInteger.valueOf(90000), TestKeys.TO_ADDRESS, "", BigInteger.TEN);
        Assert.assertEquals(youSendTransaction.getTransactionHash(), RESULT_HASH);
    }

    @Test(expected = TxHashMismatchException.class)
    public void testSendTransaction2() throws IOException {
        RawTransactionManager rawTransactionManager = new RawTransactionManager(youChain, TestKeys.CREDENTIALS, NetworkId.TESTNET);
        rawTransactionManager = new RawTransactionManager(youChain, TestKeys.CREDENTIALS, 10, 100);
        YOUSendTransaction youSendTransaction = rawTransactionManager.sendTransaction(BigInteger.valueOf(10), BigInteger.valueOf(90000), TestKeys.TO_ADDRESS, "", BigInteger.TEN);
        Assert.assertEquals(youSendTransaction.getTransactionHash(), RESULT_HASH);
    }

}
