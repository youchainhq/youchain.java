package cc.youchain.protocol.core;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.YOUChainService;
import cc.youchain.protocol.core.methods.response.NetVersion;
import cc.youchain.protocol.core.methods.response.YOUBlockNumber;
import cc.youchain.protocol.http.HttpService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JsonRpc2_0YOUChainTest {

    private ScheduledExecutorService scheduledExecutorService =
            mock(ScheduledExecutorService.class);

    private YOUChainService service = mock(YOUChainService.class);

    private YOUChain youChain = YOUChain.build(service, 10, scheduledExecutorService);

    private JsonRpc2_0YOUChain jsonRpc2_0YOUChain;

    private HttpService httpService = new HttpService();

    @Before
    public void init() {
        jsonRpc2_0YOUChain = new JsonRpc2_0YOUChain(service, 10, scheduledExecutorService);
    }

    @Test
    public void testStopExecutorOnShutdown() throws Exception {
        youChain.shutdown();

        verify(scheduledExecutorService).shutdown();
        verify(service).close();
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionOnServiceClosure() throws Exception {
        doThrow(new IOException("Failed to close")).when(service).close();

        youChain.shutdown();
    }

    @Test
    public void testYouBlockNumber() throws IOException {
        YOUBlockNumber resp = httpService.send(jsonRpc2_0YOUChain.youBlockNumber(), YOUBlockNumber.class);
        assert resp != null;
        assert resp.getBlockNumber().compareTo(BigInteger.ZERO) > 0 ;
    }
}
