package cc.youchain.tx.response;

import cc.youchain.crypto.TestKeys;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;
import cc.youchain.tx.NetworkId;
import cc.youchain.tx.RawTransactionManager;
import cc.youchain.tx.SuperMockTester;
import cc.youchain.tx.Transfer;
import cc.youchain.utils.Convert;
import cc.youchain.utils.TxHashVerifier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueuingTransactionReceiptProcessorTest extends SuperMockTester {

    private YOUChain youChain = mock(YOUChain.class);

    @Before
    public void setUp() throws Exception {
        mockGasPriceRequest(youChain);
        mockNonceRequest(youChain);
        mockSendRawTransactionRequest(youChain);
    }

    @Test
    public void testWaitForTransactionReceipt() throws Exception {
        TransactionReceipt transactionReceiptToMock = new TransactionReceipt();
        mockTransactionReceipt(youChain, transactionReceiptToMock);
        LinkedBlockingQueue<TransactionReceipt> transactionReceipts = new LinkedBlockingQueue<>();
        QueuingTransactionReceiptProcessor queuingTransactionReceiptProcessor = new QueuingTransactionReceiptProcessor(
                youChain, new Callback() {
            @Override
            public void accept(TransactionReceipt transactionReceipt) {
                transactionReceipts.add(transactionReceipt);
            }

            @Override
            public void exception(Exception exception) {
            }
        }, 10, 1000);
        RawTransactionManager transactionManager = new RawTransactionManager(
                youChain,
                TestKeys.CREDENTIALS,
                NetworkId.TESTNET,
                queuingTransactionReceiptProcessor);
        TxHashVerifier txHashVerifier = mock(TxHashVerifier.class);
        when(txHashVerifier.verify(anyString(), anyString())).thenReturn(Boolean.TRUE);
        transactionManager.setTxHashVerifier(txHashVerifier);

        Transfer transfer = new Transfer(youChain, transactionManager);
        BigInteger gasPrice = transfer.requestCurrentGasPrice();
        Map<String, Object> pendingTransactions = new ConcurrentHashMap<>();
        TransactionReceipt transactionReceipt = transfer.sendFunds(
                TestKeys.TO_ADDRESS,
                BigDecimal.valueOf(1.0),
                Convert.Unit.LU,
                gasPrice,
                Transfer.GAS_LIMIT).send();
        pendingTransactions.put(transactionReceipt.getTransactionHash(), new Object());
        TransactionReceipt transactionReceipt2Result = transactionReceipts.take();
        Assert.assertEquals(transactionReceipt2Result, transactionReceiptToMock);
    }
}
