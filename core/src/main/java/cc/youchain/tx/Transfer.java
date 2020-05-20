package cc.youchain.tx;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.RemoteCall;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;
import cc.youchain.protocol.exceptions.TransactionException;
import cc.youchain.utils.Convert;
import cc.youchain.utils.Numeric;
import cc.youchain.crypto.Credentials;

/**
 * Class for performing YOU transactions on the YOUChain blockchain.
 */
public class Transfer extends ManagedTransaction {

    // This is the cost to send YOU between parties
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(90000);

    public Transfer(YOUChain youChain, TransactionManager transactionManager) {
        super(youChain, transactionManager);
    }

    /**
     * Given the duration required to execute a transaction, asyncronous execution is strongly
     * recommended via {@link Transfer#sendFunds(String, BigDecimal, Convert.Unit)}.
     *
     * @param toAddress destination address
     * @param value     amount to send
     * @param unit      of specified send
     * @return {@link Optional} containing our transaction receipt
     * @throws ExecutionException   if the computation threw an
     *                              exception
     * @throws InterruptedException if the current thread was interrupted
     *                              while waiting
     * @throws TransactionException if the transaction was not mined while waiting
     */
    private TransactionReceipt send(String toAddress, BigDecimal value, Convert.Unit unit)
            throws IOException,
            TransactionException {

        BigInteger gasPrice = requestCurrentGasPrice();
        return send(toAddress, value, unit, gasPrice, GAS_LIMIT);
    }

    private TransactionReceipt send(
            String toAddress, BigDecimal value, Convert.Unit unit, BigInteger gasPrice,
            BigInteger gasLimit) throws IOException,
            TransactionException {

        BigDecimal luValue = Convert.toLu(value, unit);
        if (!Numeric.isIntegerValue(luValue)) {
            throw new UnsupportedOperationException(
                    "Non decimal Lu value provided: " + value + " " + unit.toString()
                            + " = " + luValue + " Lu");
        }

        return send(toAddress, "", luValue.toBigIntegerExact(), gasPrice, gasLimit);
    }

    public static RemoteCall<TransactionReceipt> sendFunds(
            YOUChain youChain, Credentials credentials,
            String toAddress, BigDecimal value, Convert.Unit unit) {

        TransactionManager transactionManager = new RawTransactionManager(youChain, credentials);

        return new RemoteCall<>(() ->
                new Transfer(youChain, transactionManager).send(toAddress, value, unit));
    }

    /**
     * Execute the provided function as a transaction asynchronously. This is intended for one-off
     * fund transfers. For multiple, create an instance.
     *
     * @param toAddress destination address
     * @param value     amount to send
     * @param unit      of specified send
     * @return {@link RemoteCall} containing executing transaction
     */
    public RemoteCall<TransactionReceipt> sendFunds(
            String toAddress, BigDecimal value, Convert.Unit unit) {
        return new RemoteCall<>(() -> send(toAddress, value, unit));
    }

    public RemoteCall<TransactionReceipt> sendFunds(
            String toAddress, BigDecimal value, Convert.Unit unit, BigInteger gasPrice,
            BigInteger gasLimit) {
        return new RemoteCall<>(() -> send(toAddress, value, unit, gasPrice, gasLimit));
    }
}
