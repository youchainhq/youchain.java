package cc.youchain.unittests.java;

import cc.youchain.abi.TypeReference;
import cc.youchain.abi.datatypes.Function;
import cc.youchain.abi.datatypes.Type;
import cc.youchain.abi.datatypes.generated.Uint256;
import cc.youchain.crypto.Credentials;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.RemoteCall;
import cc.youchain.tx.Contract;
import cc.youchain.tx.TransactionManager;
import cc.youchain.tx.gas.ContractGasProvider;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 *
 * <p>Generated with YOUChain version none.
 */
public class Arithmetic_sol_arithmetic extends Contract {
    private static final String BINARY = "60806040523480156100115760006000fd5b50610017565b610211806100266000396000f3fe60806040523480156100115760006000fd5b50600436106100515760003560e01c8063120f34c4146100575780633bc48ac3146100a45780635c242535146100f15780638bc94f2b1461013e57610051565b60006000fd5b61008e6004803603604081101561006e5760006000fd5b81019080803590602001909291908035906020019092919050505061018b565b6040518082815260200191505060405180910390f35b6100db600480360360408110156100bb5760006000fd5b8101908080359060200190929190803590602001909291905050506101a6565b6040518082815260200191505060405180910390f35b610128600480360360408110156101085760006000fd5b8101908080359060200190929190803590602001909291905050506101b8565b6040518082815260200191505060405180910390f35b610175600480360360408110156101555760006000fd5b8101908080359060200190929190803590602001909291905050506101ca565b6040518082815260200191505060405180910390f35b6000818381151561019857fe5b0490506101a0565b92915050565b600081830190506101b2565b92915050565b600081830290506101c4565b92915050565b600081830390506101d6565b9291505056fea265627a7a72315820dc8d7c872a9fccbcd852b7f8107811e1319609d67df4b2112f081e4df91eb57864736f6c634300050b0032";

    public static final String FUNC_ARITHMETIC_DIVIDE = "arithmetic_divide";

    public static final String FUNC_ARITHMETIC_ADD = "arithmetic_add";

    public static final String FUNC_ARITHMETIC_MULTIPLY = "arithmetic_multiply";

    public static final String FUNC_ARITHMETIC_SUBTRACT = "arithmetic_subtract";

    @Deprecated
    protected Arithmetic_sol_arithmetic(String contractAddress, YOUChain youChain, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, youChain, credentials, gasPrice, gasLimit);
    }

    protected Arithmetic_sol_arithmetic(String contractAddress, YOUChain youChain, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, youChain, credentials, contractGasProvider);
    }

    @Deprecated
    protected Arithmetic_sol_arithmetic(String contractAddress, YOUChain youChain, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, youChain, transactionManager, gasPrice, gasLimit);
    }

    protected Arithmetic_sol_arithmetic(String contractAddress, YOUChain youChain, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, youChain, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> arithmetic_divide(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_ARITHMETIC_DIVIDE, 
                Arrays.<Type>asList(new Uint256(a),
                new Uint256(b)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> arithmetic_add(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_ARITHMETIC_ADD, 
                Arrays.<Type>asList(new Uint256(a),
                new Uint256(b)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> arithmetic_multiply(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_ARITHMETIC_MULTIPLY, 
                Arrays.<Type>asList(new Uint256(a),
                new Uint256(b)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> arithmetic_subtract(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_ARITHMETIC_SUBTRACT, 
                Arrays.<Type>asList(new Uint256(a),
                new Uint256(b)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Arithmetic_sol_arithmetic load(String contractAddress, YOUChain youChain, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Arithmetic_sol_arithmetic(contractAddress, youChain, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Arithmetic_sol_arithmetic load(String contractAddress, YOUChain youChain, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Arithmetic_sol_arithmetic(contractAddress, youChain, transactionManager, gasPrice, gasLimit);
    }

    public static Arithmetic_sol_arithmetic load(String contractAddress, YOUChain youChain, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Arithmetic_sol_arithmetic(contractAddress, youChain, credentials, contractGasProvider);
    }

    public static Arithmetic_sol_arithmetic load(String contractAddress, YOUChain youChain, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Arithmetic_sol_arithmetic(contractAddress, youChain, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Arithmetic_sol_arithmetic> deploy(YOUChain youChain, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Arithmetic_sol_arithmetic.class, youChain, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Arithmetic_sol_arithmetic> deploy(YOUChain youChain, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Arithmetic_sol_arithmetic.class, youChain, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Arithmetic_sol_arithmetic> deploy(YOUChain youChain, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Arithmetic_sol_arithmetic.class, youChain, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Arithmetic_sol_arithmetic> deploy(YOUChain youChain, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Arithmetic_sol_arithmetic.class, youChain, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
