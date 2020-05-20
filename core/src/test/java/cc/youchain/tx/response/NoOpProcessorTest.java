package cc.youchain.tx.response;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class NoOpProcessorTest {

    private YOUChain youChain = mock(YOUChain.class);

    @Test
    public void testWaitForTransactionReceipt() {
        NoOpProcessor noOpProcessor = new NoOpProcessor(youChain);
        TransactionReceipt transactionReceipt = noOpProcessor.waitForTransactionReceipt("0xHASH");
        Assert.assertEquals(transactionReceipt.getTransactionHash(), "0xHASH");
    }
}
