package cc.youchain.tx;

import cc.youchain.crypto.TestKeys;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.http.HttpService;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;

import static org.mockito.Mockito.mock;

public class ReadonlyTransactionManagerTest {

    HttpService httpService = mock(HttpService.class);
    YOUChain youChain = YOUChain.build(httpService);

    @Test(expected = UnsupportedOperationException.class)
    public void testSendTransaction() throws IOException {
        ReadonlyTransactionManager readonlyTransactionManager = new ReadonlyTransactionManager(youChain, TestKeys.ADDRESS);
        readonlyTransactionManager.sendTransaction(BigInteger.ZERO, BigInteger.ZERO, TestKeys.TO_ADDRESS, "", BigInteger.ZERO);
    }
}
