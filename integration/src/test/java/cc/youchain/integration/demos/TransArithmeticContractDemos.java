package cc.youchain.integration.demos;

import cc.youchain.abi.FunctionEncoder;
import cc.youchain.abi.TypeReference;
import cc.youchain.abi.datatypes.Function;
import cc.youchain.abi.datatypes.Uint;
import cc.youchain.crypto.*;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.admin.Admin;
import cc.youchain.protocol.admin.methods.response.NewAccountIdentifier;
import cc.youchain.protocol.core.DefaultBlockParameterName;
import cc.youchain.protocol.core.methods.request.Transaction;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;
import cc.youchain.protocol.core.methods.response.YOUCall;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;
import cc.youchain.protocol.http.HttpService;
import cc.youchain.tx.*;
import cc.youchain.tx.gas.StaticGasProvider;
import cc.youchain.utils.Numeric;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class TransArithmeticContractDemos {

    private static final Logger log = LoggerFactory.getLogger(TransArithmeticContractDemos.class);
    private static String nodeUrl = "http://localhost:8283/";
    private static HttpService httpService = new HttpService(nodeUrl);
    private static YOUChain youChain = YOUChain.build(httpService);

    private static final String contractBinary = "60806040523480156100115760006000fd5b50610017565b610211806100266000396000f3fe6080604052348" +
            "0156100115760006000fd5b50600436106100515760003560e01c8063120f34c4146100575780633bc48ac3146100a45780635c" +
            "242535146100f15780638bc94f2b1461013e57610051565b60006000fd5b61008e6004803603604081101561006e5760006000f" +
            "d5b81019080803590602001909291908035906020019092919050505061018b565b604051808281526020019150506040518091" +
            "0390f35b6100db600480360360408110156100bb5760006000fd5b8101908080359060200190929190803590602001909291905" +
            "050506101a6565b6040518082815260200191505060405180910390f35b610128600480360360408110156101085760006000fd" +
            "5b8101908080359060200190929190803590602001909291905050506101b8565b6040518082815260200191505060405180910" +
            "390f35b610175600480360360408110156101555760006000fd5b81019080803590602001909291908035906020019092919050" +
            "50506101ca565b6040518082815260200191505060405180910390f35b6000818381151561019857fe5b0490506101a0565b929" +
            "15050565b600081830190506101b2565b92915050565b600081830290506101c4565b92915050565b600081830390506101d656" +
            "5b9291505056fea265627a7a72315820dc8d7c872a9fccbcd852b7f8107811e1319609d67df4b2112f081e4df91eb57864736f6" +
            "c634300050b0032";

    /**
     * 创建合约
     */
    @Test
    public void createContractDemo() throws Exception {
        String fromUserPrivateKey = "PrivateKey";
        Credentials credential = Credentials.create(fromUserPrivateKey);
        String from = credential.getAddress();
        log.info("from_address={}", from);
        String to = "";
        BigInteger gasPrice = youChain.youGasPrice().send().getGasPrice();
        BigInteger gasLimit = Contract.GAS_LIMIT;
        byte networkId = NetworkId.TESTNET; // 主网请使用ChainId.MAINNET
//        TransactionManager transactionManager = new FastRawTransactionManager(youChain, credential, networkId);
//        String data = "608060405234801561001057600080fd5b5060c28061001f6000396000f3fe6080604052348015600f57600080fd5b50600" +
//                "436106045576000357c010000000000000000000000000000000000000000000000000000000090048063c6888fa114604a575b60" +
//                "0080fd5b607360048036036020811015605e57600080fd5b81019080803590602001909291905050506089565b604051808281526" +
//                "0200191505060405180910390f35b600060078202905091905056fea165627a7a7230582090bc2429768e3ef419faf08aad24e6cb" +
//                "46096e14ca475031ea409d4b3497a8dc0029";
//        YOUSendTransaction youSendTransaction = transactionManager.sendTransaction(gasPrice, gasLimit, to, data, BigInteger.TEN);

        BigInteger nonce = youChain.youGetTransactionCount(from, DefaultBlockParameterName.PENDING).send().getTransactionCount();
        RawTransaction rawTransaction = RawTransaction.createContractTransaction(nonce, gasPrice, gasLimit, BigInteger.valueOf(100), "0x" + contractBinary);
        // 签名交易数据
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credential);
        String signedTransactionData = Numeric.toHexString(signMessage);
        YOUSendTransaction youSendTransaction = youChain.youSendRawTransaction(signedTransactionData).send();
        log.info("transaction hash is {}", youSendTransaction.getTransactionHash());
        Thread.sleep(10000); // sleep10秒等交易确认
        TransactionReceipt transactionReceipt = youChain.youGetTransactionReceipt(youSendTransaction.getTransactionHash()).send().getResult();
//        log.info("transaction receipt={}", JSONUtil.toJsonStr(transactionReceipt));
        log.info("contract address is {}", transactionReceipt.getContractAddress());
    }

    @Test
    public void testIsValid() throws IOException {
        String contractAddress = "0x1cd3d1f59992f5eee02ca3d5f6a540fed89d09c7";
        String fromUserPrivateKey = "PrivateKey";
        Credentials credential = Credentials.create(fromUserPrivateKey);
        StaticGasProvider gasProvider = new StaticGasProvider(BigInteger.TEN, BigInteger.ONE);
        TestDemoContract testDemoContract = new TestDemoContract(contractBinary, contractAddress, youChain, credential, gasProvider);
        Assert.assertTrue(testDemoContract.isValid());
    }

    // 20 + 10的加法运算
    @Test
    public void testArithmeticAdd() throws IOException, ExecutionException, InterruptedException {
        String contractAddress = "0x1cd3d1f59992f5eee02ca3d5f6a540fed89d09c7";
        Function function = new Function("arithmetic_add",  // function we're calling
                Arrays.asList(new Uint(BigInteger.valueOf(20)), new Uint(BigInteger.valueOf(10))),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Uint>() {}));
        String encodedFunction = FunctionEncoder.encode(function);
//        BigInteger gasPrice = BigInteger.TEN;
//        BigInteger gasLimit = Contract.GAS_LIMIT;
        String fromAddress = "0x88189a9d3b0ccbc370e67b91211abf7f33fcf6d5";
//        BigInteger nonce = youChain.youGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send().getTransactionCount();
//        Transaction transaction = Transaction.createFunctionCallTransaction(
//            fromAddress, nonce, gasPrice, gasLimit, contractAddress, encodedFunction);
        Transaction transaction = Transaction.createYOUCallTransaction(fromAddress, contractAddress, encodedFunction);
//        YOUSendTransaction transactionResponse = youChain.youSendTransaction(transaction).sendAsync().get();
//        String transactionHash = transactionResponse.getTransactionHash();
//        log.info("transactionHash={}", transactionHash);


        YOUCall youCall = youChain.youCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger bigInteger = Numeric.toBigInt(youCall.getValue());
        log.info("youcall result={}", bigInteger.toString());
    }

    @Test
    public void testArithmeticAdd2() throws IOException, ExecutionException, InterruptedException {
        String contractAddress = "0x1cd3d1f59992f5eee02ca3d5f6a540fed89d09c7";
        Function function = new Function("arithmetic_add",  // function we're calling
                Arrays.asList(new Uint(BigInteger.valueOf(20)), new Uint(BigInteger.valueOf(10))),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Uint>() {}));
        String encodedFunction = FunctionEncoder.encode(function);
        BigInteger gasPrice = youChain.youGasPrice().send().getGasPrice();
        BigInteger gasLimit = Contract.GAS_LIMIT;
        Admin admin = Admin.build(httpService);
        String password = "password";
        NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
        String fromAddress = newAccountIdentifier.getAccountId();
        BigInteger nonce = youChain.youGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send().getTransactionCount();
        Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, gasPrice, gasLimit, contractAddress, encodedFunction);
        admin.personalUnlockAccount(fromAddress, password).send();
        YOUSendTransaction transactionResponse = admin.youSendTransaction(transaction).sendAsync().get();
        String transactionHash = transactionResponse.getTransactionHash();
        log.info("transactionHash={}", transactionHash);


        YOUCall youCall = youChain.youCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
//        BigInteger bigInteger = Numeric.toBigInt(youCall.getValue());
//        log.info("youcall result={}", bigInteger.toString());
    }

}
