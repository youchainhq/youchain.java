package cc.youchain.unittests.java;

import cc.youchain.abi.FunctionEncoder;
import cc.youchain.abi.TypeReference;
import cc.youchain.abi.datatypes.Address;
import cc.youchain.abi.datatypes.Bool;
import cc.youchain.abi.datatypes.Function;
import cc.youchain.abi.datatypes.generated.Uint256;
import cc.youchain.crypto.Credentials;
import cc.youchain.crypto.RawTransaction;
import cc.youchain.crypto.TransactionEncoder;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.DefaultBlockParameter;
import cc.youchain.protocol.core.DefaultBlockParameterName;
import cc.youchain.protocol.core.methods.request.Transaction;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;
import cc.youchain.protocol.core.methods.response.YOUCall;
import cc.youchain.protocol.http.HttpService;
import cc.youchain.tx.Contract;
import cc.youchain.tx.gas.ContractGasProvider;
import cc.youchain.tx.gas.DefaultGasProvider;
import cc.youchain.utils.Numeric;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TestERC20TokenTest {

    private static final Logger log = LoggerFactory.getLogger(TestERC20TokenTest.class);
    private static String nodeUrl = "http://localhost:8283/";
    private static HttpService httpService = new HttpService(nodeUrl);
    private static YOUChain youChain = YOUChain.build(httpService);

    private static final String CONTRACT_ADDRESS = "0x39f00dd99a5a562cd2d8de358927e8117d5734ce";

    /**
     * 创建合约
     */
    @Test
    public void createContract() throws Exception {
        String fromUserPrivateKey = "PrivateKey";
        Credentials credential = Credentials.create(fromUserPrivateKey);
        String from = credential.getAddress();
        log.info("from_address={}", from);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TestERC20Token contract = TestERC20Token.deploy(youChain, credential, contractGasProvider).send();
        log.info("contract_address={}", contract.getContractAddress());
    }

    @Test
    public void loadAndVerifyContract() throws Exception {
        TestERC20Token contract = this.getContract();
        Assert.assertTrue(contract.isValid());
    }

    private TestERC20Token getContract() {
        String fromUserPrivateKey = "PrivateKey";
        return getContractByPrivateKey(fromUserPrivateKey);
    }

    private TestERC20Token getContractByPrivateKey(String fromUserPrivateKey) {
        Credentials credential = Credentials.create(fromUserPrivateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TestERC20Token contract = TestERC20Token.load(CONTRACT_ADDRESS, youChain, credential, contractGasProvider);
        return contract;
    }

    @Test
    public void testTotalSupply() throws Exception {
        TestERC20Token contract = this.getContract();
        BigInteger bigInteger = contract.totalSupply().send();
        log.info("bigInteger={}", bigInteger.toString());
    }

    @Test
    public void testBalanceOf() throws Exception {
        String fromUserPrivateKey = "PrivateKey";
        Credentials credential = Credentials.create(fromUserPrivateKey);
        String from = credential.getAddress();
        log.info("from_address={}", from);
        String fromAddress = "0x8fb88eb4cce3b86ca522d5880b554fcdd3caea87";
        TestERC20Token contract = this.getContract();
        BigInteger balance = this.balanceOf(contract, fromAddress);
        log.info("balance={}", balance.toString());
        String address = "0x0b42eb76113d9ede60c68b9e6832f65ff8a78bbf";
        balance = this.balanceOf(contract, address);
        log.info("balance={}", balance.toString());
    }

    private BigInteger balanceOf(TestERC20Token contract, String address) throws Exception {
        BigInteger bigInteger = contract.balanceOf(address).send();
        return bigInteger;
    }

    @Test
    public void testTransfer() throws Exception {
        TestERC20Token contract = this.getContract();
        String fromAddress = "0x8fb88eb4cce3b86ca522d5880b554fcdd3caea87";
        String toAddress = "0x88189a9d3b0ccbc370e67b91211abf7f33fcf6d5";
        BigInteger fromBalance = this.balanceOf(contract, fromAddress);
        BigInteger toBalance = this.balanceOf(contract, toAddress);
        log.info("fromBalance={}, toBalance={}", fromBalance.toString(), toBalance.toString());
        BigInteger amount = BigInteger.valueOf(200);
        TransactionReceipt transactionReceipt = contract.transfer(toAddress, amount).send();
        log.info("{}", transactionReceipt.getTransactionHash());

        fromBalance = this.balanceOf(contract, fromAddress);
        toBalance = this.balanceOf(contract, toAddress);
        log.info("fromBalance={}, toBalance={}", fromBalance.toString(), toBalance.toString());
    }

    @Test
    public void testTransferFrom() throws Exception {
        TestERC20Token contract = this.getContract();
        String performerAddress = "0x495635da6c34f6e5dc1318b7eb66de23b710c4b7"; // 执行者
        String fromAddress = "0x8fb88eb4cce3b86ca522d5880b554fcdd3caea87"; // 支付者
        String toAddress = "0x88189a9d3b0ccbc370e67b91211abf7f33fcf6d5"; // 接收者
        BigInteger fromBalance = this.balanceOf(contract, fromAddress);
        BigInteger toBalance = this.balanceOf(contract, toAddress);
        log.info("fromBalance={}, toBalance={}", fromBalance.toString(), toBalance.toString());
        BigInteger approveAmount = BigInteger.valueOf(1000);
        TransactionReceipt receipt = contract.approve(performerAddress, approveAmount).send();
        log.info("approve receipt = {}", receipt.getTransactionHash());

        BigInteger transferAmount = BigInteger.valueOf(1000);
        String performerProviteKey = "0xc3f93b0be33fc97995ad87fb2d813160cfa796c10c67cedad380d69da9c92123";
        TestERC20Token contractByPerformer = this.getContractByPrivateKey(performerProviteKey);
        TransactionReceipt transactionReceipt = contractByPerformer.transferFrom(fromAddress, toAddress, transferAmount).send();
        log.info("transfer receipt = {}", transactionReceipt.getTransactionHash());

        fromBalance = this.balanceOf(contract, fromAddress);
        toBalance = this.balanceOf(contract, toAddress);
        log.info("fromBalance={}, toBalance={}", fromBalance.toString(), toBalance.toString());
    }

    @Test
    public void testTransferEventFlowable() throws Exception {
        TestERC20Token contract = this.getContract();
        String toAddress = "0x88189a9d3b0ccbc370e67b91211abf7f33fcf6d5"; // 接收者

        contract.transferEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
                .subscribe(loo -> {
                    log.info("from={}", loo._from.toString());
                    log.info("to={}", loo._to.toString());
                    log.info("value={}", loo._value.toString());
                }, error -> {
                    log.error("error");
                });

        BigInteger transferAmount = BigInteger.valueOf(10);
        contract.transfer(toAddress, transferAmount).sendAsync();


        Thread.sleep(100000);
    }

}
