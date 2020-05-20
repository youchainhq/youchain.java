package cc.youchain.tx;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.DefaultBlockParameter;
import cc.youchain.protocol.core.methods.response.YOUCall;
import cc.youchain.protocol.http.HttpService;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionManagerTest {

    HttpService httpService = mock(HttpService.class);
    YOUChain youChain = YOUChain.build(httpService);
    DefaultBlockParameter defaultBlockParameter = mock(DefaultBlockParameter.class);
    YOUCall youCall = mock(YOUCall.class);

    @Test
    public void sendCallTest() throws IOException {
        when(youCall.getValue()).thenReturn("test");
        when(httpService.send(any(), any())).thenReturn(youCall);
        ReadonlyTransactionManager readonlyTransactionManager =
                new ReadonlyTransactionManager(youChain, "");
        String value = readonlyTransactionManager.sendCall("", "", defaultBlockParameter);
        assertThat(value, is("test"));
    }
}
