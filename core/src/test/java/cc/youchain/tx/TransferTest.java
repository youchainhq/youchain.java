package cc.youchain.tx;

import cc.youchain.crypto.Credentials;
import cc.youchain.crypto.TestKeys;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.methods.response.*;
import cc.youchain.utils.Convert;
import cc.youchain.utils.TxHashVerifier;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static cc.youchain.crypto.TestKeys.TO_ADDRESS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransferTest extends SuperMockTester {

    private YOUChain youChain;

    protected TransactionReceipt transactionReceipt;

    protected TxHashVerifier txHashVerifier;

    @Before
    public void setUp() throws Exception {
        youChain = mock(YOUChain.class);
        transactionReceipt = mockTransfer();
        txHashVerifier = mock(TxHashVerifier.class);
        when(txHashVerifier.verify(any(), any())).thenReturn(true);
    }

    protected TransactionReceipt mockTransfer() throws IOException {
        mockGasPriceRequest(youChain);
        mockNonceRequest(youChain);
        mockSendRawTransactionRequest(youChain);
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash("0xTRANSACTION_HASH");
        transactionReceipt.setStatus("0x1");
        mockTransactionReceipt(youChain, transactionReceipt);
        return transactionReceipt;
    }

    @Test
    public void testSendFunds() throws Exception {
        assertThat(sendFunds(TestKeys.CREDENTIALS, TO_ADDRESS, BigDecimal.TEN, Convert.Unit.YOU),
                is(transactionReceipt));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTransferInvalidValue() throws Exception {
        sendFunds2(TestKeys.CREDENTIALS, TO_ADDRESS, new BigDecimal(0.1), Convert.Unit.LU);
    }

    @Test
    public void testTransfer() throws Exception {
        Transfer.sendFunds(youChain, TestKeys.CREDENTIALS, TO_ADDRESS, new BigDecimal(10), Convert.Unit.LU).send();
    }

    protected TransactionReceipt sendFunds(
            Credentials credentials, String toAddress, BigDecimal value, Convert.Unit unit)
            throws Exception {
        return new Transfer(youChain, getVerifiedTransactionManager(credentials))
                .sendFunds(toAddress, value, unit)
                .send();
    }

    protected TransactionReceipt sendFunds2(
            Credentials credentials, String toAddress, BigDecimal value, Convert.Unit unit)
            throws Exception {
        return new Transfer(youChain, getVerifiedTransactionManager(credentials))
                .sendFunds(toAddress, value, unit, BigInteger.valueOf(10), BigInteger.valueOf(90000l))
                .send();
    }

    public TransactionManager getVerifiedTransactionManager(Credentials credentials) {
        RawTransactionManager transactionManager = new RawTransactionManager(youChain, credentials);
        transactionManager.setTxHashVerifier(txHashVerifier);
        return transactionManager;
    }
}
