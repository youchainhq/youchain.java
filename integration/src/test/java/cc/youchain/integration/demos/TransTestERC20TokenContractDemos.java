package cc.youchain.integration.demos;

import cc.youchain.abi.FunctionEncoder;
import cc.youchain.abi.TypeReference;
import cc.youchain.abi.datatypes.*;
import cc.youchain.abi.datatypes.generated.Uint256;
import cc.youchain.crypto.Credentials;
import cc.youchain.crypto.RawTransaction;
import cc.youchain.crypto.TransactionEncoder;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.DefaultBlockParameterName;
import cc.youchain.protocol.core.methods.request.Transaction;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;
import cc.youchain.protocol.core.methods.response.YOUCall;
import cc.youchain.protocol.core.methods.response.YOUGetTransactionReceipt;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;
import cc.youchain.protocol.http.HttpService;
import cc.youchain.tx.NetworkId;
import cc.youchain.tx.Contract;
import cc.youchain.tx.gas.StaticGasProvider;
import cc.youchain.utils.Numeric;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.fail;

public class TransTestERC20TokenContractDemos {

    private static final Logger log = LoggerFactory.getLogger(TransERC20ContractDemos.class);
    private static String nodeUrl = "http://test-node.iyouchain.com:80/";
    private static HttpService httpService = new HttpService(nodeUrl);
    private static YOUChain youChain = YOUChain.build(httpService);

    private static final String contractBinary = "60806040526040518060400160405280601081526020017f5465737420455243323020546f6b656e0000000000000000000000000000000081526020015060006000509080519060200190610055929190610177565b506040518060400160405280600581526020017f5445524354000000000000000000000000000000000000000000000000000000815260200150600160005090805190602001906100a7929190610177565b506012600260006101000a81548160ff021916908360ff160217905550620f424060056000509090553480156100dd5760006000fd5b505b600560005054600360005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005081909090555033600660006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b610227565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106101b857805160ff19168380011785556101eb565b828001600101855582156101eb579182015b828111156101ea57825182600050909055916020019190600101906101ca565b5b5090506101f891906101fc565b5090565b6102249190610206565b808211156102205760008181506000905550600101610206565b5090565b90565b610df980620002376000396000f3fe60806040523480156100115760006000fd5b50600436106100ae5760003560e01c806370a082311161007257806370a08231146102685780638da5cb5b146102c157806395d89b411461030b578063a9059cbb1461038f578063dd62ed3e146103f6578063f85faf771461046f576100ae565b806306fdde03146100b4578063095ea7b31461013857806318160ddd1461019f57806323b872dd146101bd578063313ce56714610244576100ae565b60006000fd5b6100bc6104d2565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100fd5780820151818401525b6020810190506100e1565b50505050905090810190601f16801561012a5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101856004803603604081101561014f5760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610573565b604051808215151515815260200191505060405180910390f35b6101a7610675565b6040518082815260200191505060405180910390f35b61022a600480360360608110156101d45760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061068c565b604051808215151515815260200191505060405180910390f35b61024c61093b565b604051808260ff1660ff16815260200191505060405180910390f35b6102ab6004803603602081101561027f5760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061094e565b6040518082815260200191505060405180910390f35b6102c96109a2565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6103136109c8565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156103545780820151818401525b602081019050610338565b50505050905090810190601f1680156103815780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6103dc600480360360408110156103a65760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610a69565b604051808215151515815260200191505060405180910390f35b6104596004803603604081101561040d5760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610c86565b6040518082815260200191505060405180910390f35b6104bc600480360360408110156104865760006000fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610d1b565b6040518082815260200191505060405180910390f35b60006000508054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561056b5780601f106105405761010080835404028352916020019161056b565b820191906000526020600020905b81548152906001019060200180831161054e57829003601f168201915b505050505081565b600081600460005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000508190909055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925846040518082815260200191505060405180910390a36001905061066f565b92915050565b600060056000505490508050809050610689565b90565b600081600360005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505410158015610768575081600460005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505410155b80156107745750600082115b801561080b5750600360005060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505482600360005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505401115b1561092a5781600360005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082828250540392505081909090555081600360005060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282825054019250508190909055508273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a36001905061093456610933565b60009050610934565b5b9392505050565b600260009054906101000a900460ff1681565b6000600360005060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005054905061099d565b919050565b600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60016000508054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a615780601f10610a3657610100808354040283529160200191610a61565b820191906000526020600020905b815481529060010190602001808311610a4457829003601f168201915b505050505081565b600081600360005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505410158015610ac05750600082115b8015610b575750600360005060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505482600360005060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000505401115b15610c765781600360005060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082828250540392505081909090555081600360005060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282825054019250508190909055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a360019050610c8056610c7f565b60009050610c80565b5b92915050565b6000600460005060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060005060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600050549050610d15565b92915050565b600081600360005060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828282505401925050819090905550600360005060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600050549050610dbe565b9291505056fea265627a7a723158204ebf92f3c5dc35e8bbd549edccdeea6379870d866b67d332c08ffb2d7be91d0764736f6c634300050b0032";
    private static final String contractAddress = "0x7db79a2f1e9a2a25c5b13bceae1447438e40ccbd";

    /**
     * 创建ERC20合约
     */
    @Test
    public void createTestERC20ContractDemo() throws Exception {
        String encodedConstructor = FunctionEncoder.encodeConstructor(new ArrayList<>());
        log.info("encodedConstructor={}", encodedConstructor);
        String fromUserPrivateKey = "PrivateKey";
        Credentials credential = Credentials.create(fromUserPrivateKey);
        String from = credential.getAddress();
        log.info("from_address={}", from);
        BigInteger nonce = youChain.youGetTransactionCount(from, DefaultBlockParameterName.PENDING).send().getTransactionCount();
        BigInteger gasPrice = youChain.youGasPrice().send().getGasPrice();

//        String encodedConstructor = FunctionEncoder.encodeConstructor(new ArrayList<>());
        log.info("encodedConstructor={}", encodedConstructor);
        RawTransaction rawTransaction = RawTransaction.createContractTransaction(nonce, gasPrice,
                Contract.GAS_LIMIT, BigInteger.ZERO, "0x" + contractBinary + encodedConstructor);
        // 签名交易数据
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credential);
        String signedTransactionData = Numeric.toHexString(signMessage);
        YOUSendTransaction youSendTransaction = youChain.youSendRawTransaction(signedTransactionData).send();
        String transactionHash = youSendTransaction.getTransactionHash();
        log.info("transaction hash is {}", transactionHash);
        TransactionReceipt transactionReceipt = this.waitTransactionReceipt(transactionHash);
        Assert.assertTrue(transactionHash.equalsIgnoreCase(transactionReceipt.getTransactionHash()));
        log.info("contract address is {}", transactionReceipt.getContractAddress());
    }

    @Test
    public void testIsValid() throws IOException {
        String fromUserPrivateKey = "PrivateKey";
        Credentials credential = Credentials.create(fromUserPrivateKey);
        StaticGasProvider gasProvider = new StaticGasProvider(BigInteger.TEN, BigInteger.ONE);

        TestDemoContract testDemoContract = new TestDemoContract(contractBinary, contractAddress, youChain, credential, gasProvider);
        log.info(testDemoContract.getContractAddress());
        log.info(testDemoContract.getContractBinary());
        Assert.assertTrue(testDemoContract.isValid());
    }

    @Test
    public void testTotalSupply() throws ExecutionException, InterruptedException {
        Function function = new Function("totalSupply",  // function we're calling
                new ArrayList<>(),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        String fromAddress = "0xb563b544a88e71109ee3cf8f867285beaf31bdc5";
        Transaction transaction = Transaction.createYOUCallTransaction(fromAddress, contractAddress, encodedFunction);
        YOUCall youCall = youChain.youCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger bigInteger = Numeric.toBigInt(youCall.getValue());
        log.info("bigInteger={}", bigInteger);
    }

    @Test
    public void testBalanceOf() throws ExecutionException, InterruptedException {
        String fromUserPrivateKey = "PrivateKey";
        Credentials credential = Credentials.create(fromUserPrivateKey);
        String from = credential.getAddress();
        log.info("from_address={}", from);
        String fromAddress = "0x8fb88eb4cce3b86ca522d5880b554fcdd3caea87";
        BigInteger balance = this.balanceOf(fromAddress);
        log.info("balance={}", balance);
    }

    private BigInteger balanceOf(String address) throws ExecutionException, InterruptedException {
        Function function = new Function("balanceOf",  // function we're calling
                Arrays.asList(new Address(address)),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createYOUCallTransaction(address, contractAddress, encodedFunction);
        YOUCall youCall = youChain.youCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger bigInteger = Numeric.toBigInt(youCall.getValue());
        return bigInteger;
    }

    @Test
    public void testInitBalance() throws ExecutionException, InterruptedException {
        String fromAddress = "0xb563b544a88e71109ee3cf8f867285beaf31bdc5";
//        String toAddress = "0x88189a9d3b0ccbc370e67b91211abf7f33fcf6d5";
        Function function = new Function("initBalance",  // function we're calling
                Arrays.asList(new Address(fromAddress), new Uint256(BigInteger.valueOf(1000))),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createYOUCallTransaction(fromAddress, contractAddress, encodedFunction);
        YOUCall youCall = youChain.youCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        log.info("youCall.getValue(={}", youCall.getValue());
        BigInteger bigInteger = Numeric.toBigInt(youCall.getValue());
        log.info("result={}", bigInteger.toString());
        BigInteger fromBalance = this.balanceOf(fromAddress);
        log.info("balance={}", fromBalance.toString());
    }

    @Test
    public void testApprove() throws ExecutionException, InterruptedException {
        String performerAddress = "0x495635da6c34f6e5dc1318b7eb66de23b710c4b7"; // 执行者
        String fromAddress = "0x8fb88eb4cce3b86ca522d5880b554fcdd3caea87";
        Function function = new Function("approve",  // function we're calling
                Arrays.asList(new Address(performerAddress), new Uint256(BigInteger.valueOf(1000))),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Bool>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createYOUCallTransaction(fromAddress, contractAddress, encodedFunction);
        YOUCall youCall = youChain.youCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        log.info("youCall.getValue(={}", youCall.getValue());
        BigInteger bigInteger = Numeric.toBigInt(youCall.getValue());
        log.info("result={}", bigInteger.toString());
//        BigInteger fromBalance = this.balanceOf(fromAddress);
//        log.info("balance={}", fromBalance.toString());
    }

    @Test
    public void testTransfer() throws Exception {
        String fromAddress = "0x3b6160f85c11f20178d5100d1c60146c1a20b771";
        String toAddress = "0xbbc95c67288bc7613ef9ded879bdfeba776f3b9d";
//        BigInteger fromBalance = this.balanceOf(fromAddress);
//        BigInteger toBalance = this.balanceOf(toAddress);
//        log.info("fromBalance={}, toBalance={}", fromBalance.toString(), toBalance.toString());
        Function function = new Function("transfer",  // function we're calling
                Arrays.asList(new Address(toAddress), new Uint256(BigInteger.valueOf(10000000))),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Bool>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        log.info("encodedFunction={}", encodedFunction);
        BigInteger nonce = youChain.youGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        log.info("nonce={}", nonce.toString());
        BigInteger gasPrice = youChain.youGasPrice().send().getGasPrice();
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce.add(new BigInteger("100")), gasPrice, Contract.GAS_LIMIT, toAddress, encodedFunction);
        String fromUserPrivateKey = "PrivateKey";
        Credentials credential = Credentials.create(fromUserPrivateKey);
        // 签名交易数据
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, NetworkId.TESTNET, credential);
        String signedTransactionData = Numeric.toHexString(signMessage);
        String transactionHash = youChain.youSendRawTransaction(signedTransactionData).send().getTransactionHash();
        log.info("发送交易数据 {}", transactionHash);

        TransactionReceipt transactionReceipt = this.waitTransactionReceipt(transactionHash);
        Assert.assertTrue(transactionHash.equalsIgnoreCase(transactionReceipt.getTransactionHash()));

//        fromBalance = this.balanceOf(fromAddress);
//        toBalance = this.balanceOf(toAddress);
//        log.info("fromBalance={}, toBalance={}", fromBalance.toString(), toBalance.toString());
    }

    @Test
    public void testTransferFrom() throws Exception {
        String performerAddress = "0x495635da6c34f6e5dc1318b7eb66de23b710c4b7"; // 执行者
        String fromAddress = "0x8fb88eb4cce3b86ca522d5880b554fcdd3caea87"; // 支付者
        String toAddress = "0x88189a9d3b0ccbc370e67b91211abf7f33fcf6d5"; // 接收者
        BigInteger fromBalance = this.balanceOf(fromAddress);
        BigInteger toBalance = this.balanceOf(toAddress);
        log.info("fromBalance={}, toBalance={}", fromBalance.toString(), toBalance.toString());

        // 先给执行者授权
        Function approveFunction = new Function("approve",  // function we're calling
                Arrays.asList(new Address(performerAddress), new Uint256(BigInteger.valueOf(1000))),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Bool>() {
                }));
        String encodedApproveFunction = FunctionEncoder.encode(approveFunction);
        BigInteger nonce = youChain.youGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        BigInteger gasPrice = youChain.youGasPrice().send().getGasPrice();
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, Contract.GAS_LIMIT, contractAddress, encodedApproveFunction);
        String fromUserPrivateKey = "PrivateKey";
        Credentials credential = Credentials.create(fromUserPrivateKey);
        // 签名交易数据
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credential);
        String signedTransactionData = Numeric.toHexString(signMessage);
        String transactionHash = youChain.youSendRawTransaction(signedTransactionData).send().getTransactionHash();
        log.info("发送交易数据 {}", transactionHash);
        TransactionReceipt transactionReceipt = this.waitTransactionReceipt(transactionHash);
        Assert.assertTrue(transactionHash.equalsIgnoreCase(transactionReceipt.getTransactionHash()));

        // 执行者调用transferFrom转账从A-->B
        Function function = new Function("transferFrom",  // function we're calling
                Arrays.asList(new Address(fromAddress), new Address(toAddress), new Uint256(BigInteger.valueOf(200))),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Bool>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        BigInteger nonce2 = youChain.youGetTransactionCount(performerAddress, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        RawTransaction rawTransaction2 = RawTransaction.createTransaction(nonce2, gasPrice, Contract.GAS_LIMIT, contractAddress, encodedFunction);
        // 签名交易数据
        String performerPrivateKey = "PrivateKey";
        Credentials performerCredential = Credentials.create(performerPrivateKey);
        byte[] signMessage2 = TransactionEncoder.signMessage(rawTransaction2, performerCredential);
        String signedTransactionData2 = Numeric.toHexString(signMessage2);
        String transactionHash2 = youChain.youSendRawTransaction(signedTransactionData2).send().getTransactionHash();
        log.info("发送交易数据2 {}", transactionHash2);
        TransactionReceipt transactionReceipt2 = this.waitTransactionReceipt(transactionHash2);
        Assert.assertTrue(transactionHash2.equalsIgnoreCase(transactionReceipt2.getTransactionHash()));

        fromBalance = this.balanceOf(fromAddress);
        toBalance = this.balanceOf(toAddress);
        log.info("fromBalance={}, toBalance={}", fromBalance.toString(), toBalance.toString());
    }

    @Test
    public void testAllowance() throws IOException, ExecutionException, InterruptedException {
        String performerAddress = "0x495635da6c34f6e5dc1318b7eb66de23b710c4b7"; // 执行者
        String fromAddress = "0x8fb88eb4cce3b86ca522d5880b554fcdd3caea87"; // 支付者
        // 执行者调用transferFrom转账从A-->B
        Function function = new Function("allowance",  // function we're calling
                Arrays.asList(new Address(fromAddress), new Address(performerAddress)),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createYOUCallTransaction(fromAddress, contractAddress, encodedFunction);
        YOUCall youCall = youChain.youCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        log.info("youCall.getValue(={}", youCall.getValue());
        BigInteger bigInteger = Numeric.toBigInt(youCall.getValue());
        log.info("result={}", bigInteger.toString());
    }

    @Test
    public void testGetSymbol() throws IOException, ExecutionException, InterruptedException {
        Function function = new Function("symbol",  // function we're calling
                Arrays.asList(),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Utf8String>() {}));
        String encodedFunction = FunctionEncoder.encode(function);
        BigInteger gasPrice = youChain.youGasPrice().send().getGasPrice();
        BigInteger gasLimit = Contract.GAS_LIMIT;
        String fromAddress = "0x8fb88eb4cce3b86ca522d5880b554fcdd3caea87"; // 支付者
        BigInteger nonce = youChain.youGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send().getTransactionCount();
        Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, gasPrice, gasLimit, contractAddress, encodedFunction);
        YOUCall youCall = youChain.youCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        String s = youCall.getValue();
        log.info("symbol={}", new String(Numeric.hexStringToByteArray(s)));

//        BigInteger bigInteger = Numeric.toBigInt(youCall.getValue());
//        log.info("youcall result={}", bigInteger.toString());
    }

    @Test
    public void testGetName() throws Exception {
        Function function = new Function("name",  // function we're calling
                Arrays.asList(),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Utf8String>() {}));
        String encodedFunction = FunctionEncoder.encode(function);
        BigInteger gasPrice = BigInteger.TEN;
        BigInteger gasLimit = Contract.GAS_LIMIT;
        String fromAddress = "0x8fb88eb4cce3b86ca522d5880b554fcdd3caea87"; // 支付者
//        BigInteger nonce = youChain.youGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send().getTransactionCount();
        Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, BigInteger.ZERO, gasPrice, gasLimit, contractAddress, encodedFunction);
        YOUCall youCall = youChain.youCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        String s = youCall.getValue();
        s = fromHexString(Numeric.cleanHexPrefix(s));
        log.info("symbol={}", s);
        char[] chars = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char cha : chars) {
            log.info("cha:{}", (byte) cha);
            if (cha != 0) {
                sb.append(cha);
            }
        }
        log.info("sb={}", sb.toString());

//        BigInteger bigInteger = Numeric.toBigInt(youCall.getValue());
//        log.info("youcall result={}", bigInteger.toString());
    }

    @Test
    public void testGetTotalSupply() throws Exception {
        Function function = new Function("totalSupply",  // function we're calling
                Arrays.asList(),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Uint256>() {}));
        String encodedFunction = FunctionEncoder.encode(function);
        BigInteger gasPrice = BigInteger.TEN;
        BigInteger gasLimit = Contract.GAS_LIMIT;
        String fromAddress = "0x8fb88eb4cce3b86ca522d5880b554fcdd3caea87"; // 支付者
//        BigInteger nonce = youChain.youGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send().getTransactionCount();
        Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, BigInteger.ZERO, gasPrice, gasLimit, contractAddress, encodedFunction);
        YOUCall youCall = youChain.youCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        String s = youCall.getValue();
        log.info("result={}", (new BigInteger(Numeric.cleanHexPrefix(s), 16)).toString());
    }

    /**
     * @Title:hexString2String
     * @Description:16进制字符串转字符串
     * @param src
     * 16进制字符串
     * @return 字节数组
     * @throws
     */
    public static String hexString2String(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            temp = temp
                    + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2),
                    16).byteValue();
        }
        return temp;
    }

    public static String fromHexString(String hexString) throws Exception {
        // 用于接收转换结果
        String result = "";
        // 转大写
        hexString = hexString.toUpperCase();
        // 16进制字符
        String hexDigital = "0123456789ABCDEF";
        // 将16进制字符串转换成char数组
        char[] hexs = hexString.toCharArray();
        // 能被16整除，肯定可以被2整除
        byte[] bytes = new byte[hexString.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = hexDigital.indexOf(hexs[2 * i]) * 16 + hexDigital.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        // byte[]--&gt;String
        result = new String(bytes, "UTF-8");
        return result;
    }

    public static void main(String[] args) {
        String s = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";

        log.info("symbol={}", hexString2String(Numeric.cleanHexPrefix(s)));
    }


    private TransactionReceipt waitTransactionReceipt(String transactionHash) throws Exception {
        Optional<TransactionReceipt> transactionReceiptOptional = this.getTransactionReceipt(transactionHash, 3000, 50);
        if (!transactionReceiptOptional.isPresent()) {
            fail("Transaction receipt not generated after 50 attempts");
        }
        return transactionReceiptOptional.get();
    }
    private Optional<TransactionReceipt> getTransactionReceipt(String transactionHash, int sleepDuration, int attempts) throws Exception {
        Optional<TransactionReceipt> receiptOptional = sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (!receiptOptional.isPresent()) {
                Thread.sleep(sleepDuration);
                receiptOptional = sendTransactionReceiptRequest(transactionHash);
            } else {
                break;
            }
        }
        return receiptOptional;
    }
    private Optional<TransactionReceipt> sendTransactionReceiptRequest(String transactionHash) throws Exception {
        YOUGetTransactionReceipt transactionReceipt = youChain.youGetTransactionReceipt(transactionHash).sendAsync().get();
        return transactionReceipt.getTransactionReceipt();
    }
}
