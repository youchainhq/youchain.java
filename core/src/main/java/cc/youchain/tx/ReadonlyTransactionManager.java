package cc.youchain.tx;

import java.io.IOException;
import java.math.BigInteger;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.DefaultBlockParameter;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;

/**
 * Transaction manager implementation for read-only operations on smart contracts.
 */
public class ReadonlyTransactionManager extends TransactionManager {

    public ReadonlyTransactionManager(YOUChain youChain, String fromAddress) {
        super(youChain, fromAddress);
    }

    @Override
    public YOUSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) {
        throw new UnsupportedOperationException("Only read operations are supported by this transaction manager");
    }

//    @Override
//    public String sendCall(String to, String data, DefaultBlockParameter defaultBlockParameter) {
//        throw new UnsupportedOperationException("Only read operations are supported by this transaction manager");
//    }
}
