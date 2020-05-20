package cc.youchain.tx;

import java.io.IOException;
import java.math.BigInteger;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.methods.response.YOUGasPrice;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;
import cc.youchain.protocol.exceptions.TransactionException;
import cc.youchain.tx.gas.DefaultGasProvider;


/**
 * Generic transaction manager.
 */
public abstract class ManagedTransaction {

    /**
     * @deprecated use ContractGasProvider
     * @see DefaultGasProvider
     */
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);

    protected YOUChain youChain;

    protected TransactionManager transactionManager;

    protected ManagedTransaction(YOUChain youChain, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.youChain = youChain;
    }

    /**
     * Return the current gas price from the YOUChain node.
     * <p>
     *     Note: this method was previously called {@code getGasPrice} but was renamed to
     *     distinguish it when a bean accessor method on {@link Contract} was added with that name.
     *     If you have a Contract subclass that is calling this method (unlikely since those
     *     classes are usually generated and until very recently those generated subclasses were
     *     marked {@code final}), then you will need to change your code to call this method
     *     instead, if you want the dynamic behavior.
     * </p>
     * @return the current gas price, determined dynamically at invocation
     * @throws IOException if there's a problem communicating with the YOUChain node
     */
    public BigInteger requestCurrentGasPrice() throws IOException {
        YOUGasPrice youGasPrice = youChain.youGasPrice().send();

        return youGasPrice.getGasPrice();
    }

    protected TransactionReceipt send(
            String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit)
            throws IOException, TransactionException {

        return transactionManager.executeTransaction(
                gasPrice, gasLimit, to, data, value);
    }
}
