package cc.youchain.contracts.token;

import java.math.BigInteger;
import java.util.List;

import io.reactivex.Flowable;

import cc.youchain.protocol.core.DefaultBlockParameter;
import cc.youchain.protocol.core.RemoteCall;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;

@SuppressWarnings("unused")
public interface ERC20BasicInterface<T> {

    RemoteCall<BigInteger> totalSupply();

    RemoteCall<BigInteger> balanceOf(String who);

    RemoteCall<TransactionReceipt> transfer(String to, BigInteger value);
    
    List<T> getTransferEvents(TransactionReceipt transactionReceipt);

    Flowable<T> transferEventFlowable(DefaultBlockParameter startBlock,
                                        DefaultBlockParameter endBlock);

}
