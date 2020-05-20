package cc.youchain.tx;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.Request;
import cc.youchain.protocol.core.methods.response.*;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SuperMockTester {

    public static final String RESULT_HASH = "0x93aadd7b4f3aa3b30ea49e225d4deb5de1a2b78f7d54b2c50e4eecbb6c16d2a4";

    protected void mockGasPriceRequest(YOUChain youChain) throws IOException {
        YOUGasPrice youGasPrice = new YOUGasPrice();
        youGasPrice.setResult("0x1");
        Request<?, YOUGasPrice> gasPriceRequest = mock(Request.class);
        when(gasPriceRequest.send()).thenReturn(youGasPrice);
        when(youChain.youGasPrice()).thenReturn((Request) gasPriceRequest);
    }

    protected void mockNonceRequest(YOUChain youChain) throws IOException {
        YOUGetTransactionCount transactionCount = new YOUGetTransactionCount();
        transactionCount.setResult("0x1");
        Request<?, YOUGetTransactionCount> transactionCountRequest = mock(Request.class);
        when(transactionCountRequest.send()).thenReturn(transactionCount);
        when(youChain.youGetTransactionCount(anyString(), any())).thenReturn((Request) transactionCountRequest);
    }

    protected void mockSendTransactionRequest(YOUChain youChain) throws IOException {
        YOUSendTransaction youSendTransaction = new YOUSendTransaction();
        youSendTransaction.setResult(RESULT_HASH);
        Request<?, YOUSendTransaction> youSendTransactionRequest = mock(Request.class);
        when(youSendTransactionRequest.send()).thenReturn(youSendTransaction);
        when(youChain.youSendTransaction(any())).thenReturn((Request) youSendTransactionRequest);
    }

    protected void mockSendRawTransactionRequest(YOUChain youChain) throws IOException {
        YOUSendTransaction youSendTransaction = new YOUSendTransaction();
        youSendTransaction.setResult(RESULT_HASH);
        Request<?, YOUSendTransaction> youSendTransactionRequest = mock(Request.class);
        when(youSendTransactionRequest.send()).thenReturn(youSendTransaction);
        when(youChain.youSendRawTransaction(any(String.class))).thenReturn((Request) youSendTransactionRequest);
    }

    protected void mockTransactionReceipt(YOUChain youChain, TransactionReceipt transactionReceipt) throws IOException {
        YOUGetTransactionReceipt youGetTransactionReceipt0 = new YOUGetTransactionReceipt();
        youGetTransactionReceipt0.setResult(null);
        YOUGetTransactionReceipt youGetTransactionReceipt1 = new YOUGetTransactionReceipt();
        youGetTransactionReceipt1.setResult(transactionReceipt);
        Request<?, YOUGetTransactionReceipt> getTransactionReceiptRequest = mock(Request.class);
        when(getTransactionReceiptRequest.send()).thenReturn(youGetTransactionReceipt0).thenReturn(youGetTransactionReceipt1);
        when(youChain.youGetTransactionReceipt(anyString())).thenReturn((Request) getTransactionReceiptRequest);
    }
}
