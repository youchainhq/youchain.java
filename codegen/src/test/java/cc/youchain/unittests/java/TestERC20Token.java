package cc.youchain.unittests.java;

import cc.youchain.abi.EventEncoder;
import cc.youchain.abi.TypeReference;
import cc.youchain.abi.datatypes.Address;
import cc.youchain.abi.datatypes.Event;
import cc.youchain.abi.datatypes.Function;
import cc.youchain.abi.datatypes.Type;
import cc.youchain.abi.datatypes.Utf8String;
import cc.youchain.abi.datatypes.generated.Uint256;
import cc.youchain.abi.datatypes.generated.Uint8;
import cc.youchain.crypto.Credentials;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.DefaultBlockParameter;
import cc.youchain.protocol.core.RemoteCall;
import cc.youchain.protocol.core.methods.request.YOUFilter;
import cc.youchain.protocol.core.methods.response.Log;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;
import cc.youchain.tx.Contract;
import cc.youchain.tx.TransactionManager;
import cc.youchain.tx.gas.ContractGasProvider;
import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 *
 * <p>Generated with YOUChain version none.
 */
public class TestERC20Token extends Contract {
    private static final String BINARY = "60806040526040518060400160405280601081526020017f5465737420455243323020546f6b656e0000000000000000000000000000000081526020015060006000509080519060200190610055929190610177565b506040518060400160405280600581526020017f5445524354000000000000000000000000000000000000000000000000000000815260200150600160005090805190602001906100a7929190610177565b506012600260006101000a81548160ff021916908360ff160217905550620f424060056000509090553480156100dd5760006000fd5b505b600560005054600360005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005081909090555033600660006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b610227565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106101b857805160ff19168380011785556101eb565b828001600101855582156101eb579182015b828111156101ea57825182600050909055916020019190600101906101ca565b5b5090506101f891906101fc565b5090565b6102249190610206565b808211156102205760008181506000905550600101610206565b5090565b90565b610df980620002376000396000f3fe60806040523480156100115760006000fd5b50600436106100ae5760003560e01c806370a082311161007257806370a08231146102685780638da5cb5b146102c157806395d89b411461030b578063a9059cbb1461038f578063dd62ed3e146103f6578063f85faf771461046f576100ae565b806306fdde03146100b4578063095ea7b31461013857806318160ddd1461019f57806323b872dd146101bd578063313ce56714610244576100ae565b60006000fd5b6100bc6104d2565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100fd5780820151818401525b6020810190506100e1565b50505050905090810190601f16801561012a5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101856004803603604081101561014f5760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610573565b604051808215151515815260200191505060405180910390f35b6101a7610675565b6040518082815260200191505060405180910390f35b61022a600480360360608110156101d45760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061068c565b604051808215151515815260200191505060405180910390f35b61024c61093b565b604051808260ff1660ff16815260200191505060405180910390f35b6102ab6004803603602081101561027f5760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061094e565b6040518082815260200191505060405180910390f35b6102c96109a2565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6103136109c8565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156103545780820151818401525b602081019050610338565b50505050905090810190601f1680156103815780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6103dc600480360360408110156103a65760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610a69565b604051808215151515815260200191505060405180910390f35b6104596004803603604081101561040d5760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610c86565b6040518082815260200191505060405180910390f35b6104bc600480360360408110156104865760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610d1b565b6040518082815260200191505060405180910390f35b60006000508054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561056b5780601f106105405761010080835404028352916020019161056b565b820191906000526020600020905b81548152906001019060200180831161054e57829003601f168201915b505050505081565b600081600460005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000508190909055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925846040518082815260200191505060405180910390a36001905061066f565b92915050565b600060056000505490508050809050610689565b90565b600081600360005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505410158015610768575081600460005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505410155b80156107745750600082115b801561080b5750600360005060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505482600360005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505401115b1561092a5781600360005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082828250540392505081909090555081600360005060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282825054019250508190909055508273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a36001905061093456610933565b60009050610934565b5b9392505050565b600260009054906101000a900460ff1681565b6000600360005060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005054905061099d565b919050565b600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60016000508054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a615780601f10610a3657610100808354040283529160200191610a61565b820191906000526020600020905b815481529060010190602001808311610a4457829003601f168201915b505050505081565b600081600360005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505410158015610ac05750600082115b8015610b575750600360005060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505482600360005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505401115b15610c765781600360005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082828250540392505081909090555081600360005060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282825054019250508190909055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a360019050610c8056610c7f565b60009050610c80565b5b92915050565b6000600460005060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600050549050610d15565b92915050565b600081600360005060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828282505401925050819090905550600360005060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600050549050610dbe565b9291505056fea265627a7a723158204ebf92f3c5dc35e8bbd549edccdeea6379870d866b67d332c08ffb2d7be91d0764736f6c634300050b0032";
//    private static final String BINARY = "60806040523480156100115760006000fd5b50600436106100ae5760003560e01c806370a082311161007257806370a08231146102685780638da5cb5b146102c157806395d89b411461030b578063a9059cbb1461038f578063dd62ed3e146103f6578063f85faf771461046f576100ae565b806306fdde03146100b4578063095ea7b31461013857806318160ddd1461019f57806323b872dd146101bd578063313ce56714610244576100ae565b60006000fd5b6100bc6104d2565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100fd5780820151818401525b6020810190506100e1565b50505050905090810190601f16801561012a5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101856004803603604081101561014f5760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610573565b604051808215151515815260200191505060405180910390f35b6101a7610675565b6040518082815260200191505060405180910390f35b61022a600480360360608110156101d45760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061068c565b604051808215151515815260200191505060405180910390f35b61024c61093b565b604051808260ff1660ff16815260200191505060405180910390f35b6102ab6004803603602081101561027f5760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061094e565b6040518082815260200191505060405180910390f35b6102c96109a2565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6103136109c8565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156103545780820151818401525b602081019050610338565b50505050905090810190601f1680156103815780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6103dc600480360360408110156103a65760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610a69565b604051808215151515815260200191505060405180910390f35b6104596004803603604081101561040d5760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610c86565b6040518082815260200191505060405180910390f35b6104bc600480360360408110156104865760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610d1b565b6040518082815260200191505060405180910390f35b60006000508054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561056b5780601f106105405761010080835404028352916020019161056b565b820191906000526020600020905b81548152906001019060200180831161054e57829003601f168201915b505050505081565b600081600460005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000508190909055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925846040518082815260200191505060405180910390a36001905061066f565b92915050565b600060056000505490508050809050610689565b90565b600081600360005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505410158015610768575081600460005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505410155b80156107745750600082115b801561080b5750600360005060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505482600360005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505401115b1561092a5781600360005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082828250540392505081909090555081600360005060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282825054019250508190909055508273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a36001905061093456610933565b60009050610934565b5b9392505050565b600260009054906101000a900460ff1681565b6000600360005060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005054905061099d565b919050565b600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60016000508054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a615780601f10610a3657610100808354040283529160200191610a61565b820191906000526020600020905b815481529060010190602001808311610a4457829003601f168201915b505050505081565b600081600360005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505410158015610ac05750600082115b8015610b575750600360005060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505482600360005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505401115b15610c765781600360005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082828250540392505081909090555081600360005060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282825054019250508190909055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a360019050610c8056610c7f565b60009050610c80565b5b92915050565b6000600460005060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600050549050610d15565b92915050565b600081600360005060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828282505401925050819090905550600360005060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600050549050610dbe565b9291505056fea265627a7a723158204ebf92f3c5dc35e8bbd549edccdeea6379870d866b67d332c08ffb2d7be91d0764736f6c634300050b0032

    public static final String FUNC_NAME = "name";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_INITBALANCE = "initBalance";

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected TestERC20Token(String contractAddress, YOUChain youChain, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, youChain, credentials, gasPrice, gasLimit);
    }

    protected TestERC20Token(String contractAddress, YOUChain youChain, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, youChain, credentials, contractGasProvider);
    }

    @Deprecated
    protected TestERC20Token(String contractAddress, YOUChain youChain, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, youChain, transactionManager, gasPrice, gasLimit);
    }

    protected TestERC20Token(String contractAddress, YOUChain youChain, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, youChain, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _amount) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new Address(_spender),
                new Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _amount) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new Address(_from),
                new Address(_to),
                new Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new Address(_owner)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _amount) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new Address(_to),
                new Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new Address(_owner),
                new Address(_spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> initBalance(String _to, BigInteger _amount) {
        final Function function = new Function(
                FUNC_INITBALANCE, 
                Arrays.<Type>asList(new Address(_to),
                new Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(YOUFilter filter) {
        return youChain.youLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        YOUFilter filter = new YOUFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(YOUFilter filter) {
        return youChain.youLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        YOUFilter filter = new YOUFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    @Deprecated
    public static TestERC20Token load(String contractAddress, YOUChain youChain, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TestERC20Token(contractAddress, youChain, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TestERC20Token load(String contractAddress, YOUChain youChain, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TestERC20Token(contractAddress, youChain, transactionManager, gasPrice, gasLimit);
    }

    public static TestERC20Token load(String contractAddress, YOUChain youChain, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TestERC20Token(contractAddress, youChain, credentials, contractGasProvider);
    }

    public static TestERC20Token load(String contractAddress, YOUChain youChain, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TestERC20Token(contractAddress, youChain, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TestERC20Token> deploy(YOUChain youChain, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TestERC20Token.class, youChain, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<TestERC20Token> deploy(YOUChain youChain, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TestERC20Token.class, youChain, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TestERC20Token> deploy(YOUChain youChain, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TestERC20Token.class, youChain, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TestERC20Token> deploy(YOUChain youChain, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TestERC20Token.class, youChain, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String _owner;

        public String _spender;

        public BigInteger _value;
    }

    public static class TransferEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public BigInteger _value;
    }
}
