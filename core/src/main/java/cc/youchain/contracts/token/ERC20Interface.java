package cc.youchain.contracts.token;

import java.math.BigInteger;
import java.util.List;

import io.reactivex.Flowable;

import cc.youchain.protocol.core.DefaultBlockParameter;
import cc.youchain.protocol.core.RemoteCall;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;

/**
 * The YOUChain ERC-20 token standard.
 * <p>
 *     Implementations should provide the concrete <code>ApprovalEventResponse</code> and
 *     <code>TransferEventResponse</code> from their token as the generic types "R" amd "T".
 * </p>
 */
@SuppressWarnings("unused")
public interface ERC20Interface<R, T> extends ERC20BasicInterface<T> {

    RemoteCall<BigInteger> allowance(String owner, String spender);

    RemoteCall<TransactionReceipt> approve(String spender, BigInteger value);

    RemoteCall<TransactionReceipt> transferFrom(String from, String to, BigInteger value);

    List<R> getApprovalEvents(TransactionReceipt transactionReceipt);

    Flowable<R> approvalEventFlowable(DefaultBlockParameter startBlock,
                                        DefaultBlockParameter endBlock);

}
