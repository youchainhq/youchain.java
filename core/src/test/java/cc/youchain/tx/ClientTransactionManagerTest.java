package cc.youchain.tx;

import cc.youchain.crypto.TestKeys;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;
import cc.youchain.tx.response.TransactionReceiptProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;

import static org.mockito.Mockito.mock;

public class ClientTransactionManagerTest extends SuperMockTester {

    private YOUChain youChain = mock(YOUChain.class);
    TransactionReceiptProcessor transactionReceiptProcessor = mock(TransactionReceiptProcessor.class);

    @Test
    public void testAll() throws IOException {
        mockSendTransactionRequest(youChain);
        ClientTransactionManager clientTransactionManager = new ClientTransactionManager(youChain, TestKeys.ADDRESS);
        clientTransactionManager = new ClientTransactionManager(youChain, TestKeys.ADDRESS, 10, 100);
        clientTransactionManager = new ClientTransactionManager(youChain, TestKeys.ADDRESS, transactionReceiptProcessor);
        YOUSendTransaction youSendTransaction = clientTransactionManager.sendTransaction(BigInteger.valueOf(20), BigInteger.valueOf(90000), TestKeys.TO_ADDRESS, "", BigInteger.ONE);
        Assert.assertEquals(youSendTransaction.getTransactionHash(), "0x93aadd7b4f3aa3b30ea49e225d4deb5de1a2b78f7d54b2c50e4eecbb6c16d2a4");
    }
}
