package cc.youchain.tx;

import cc.youchain.crypto.*;
import cc.youchain.protocol.YOUChain;
import cc.youchain.utils.TxHashVerifier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ManagedTransactionTester extends SuperMockTester {

    public YOUChain youChain = mock(YOUChain.class);
    protected TxHashVerifier txHashVerifier = mock(TxHashVerifier.class);

    @Before
    public void setUp() throws Exception {
        when(txHashVerifier.verify(any(), any())).thenReturn(true);
    }

    @Test
    public void testRequestCurrentGasPrice() throws Exception {
        mockGasPriceRequest(youChain);
        RawTransactionManager rawTransactionManager = new RawTransactionManager(youChain, TestKeys.CREDENTIALS);
        Transfer transfer = new Transfer(youChain, rawTransactionManager);
        BigInteger gasPrice = transfer.requestCurrentGasPrice();
        Assert.assertTrue(gasPrice.compareTo(BigInteger.ZERO) > 0);
    }

    public TransactionManager getVerifiedTransactionManager(Credentials credentials) {
        RawTransactionManager transactionManager = new RawTransactionManager(youChain, credentials);
        transactionManager.setTxHashVerifier(txHashVerifier);
        return transactionManager;
    }

    public TransactionManager getVerifiedTransactionManager(
            Credentials credentials, int attempts, int sleepDuration) {
        RawTransactionManager transactionManager =
                new RawTransactionManager(youChain, credentials, attempts, sleepDuration);
        transactionManager.setTxHashVerifier(txHashVerifier);
        return transactionManager;
    }
}
