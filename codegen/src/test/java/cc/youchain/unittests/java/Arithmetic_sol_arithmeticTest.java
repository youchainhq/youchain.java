package cc.youchain.unittests.java;

import cc.youchain.crypto.Credentials;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.http.HttpService;
import cc.youchain.tx.Contract;
import cc.youchain.tx.gas.ContractGasProvider;
import cc.youchain.tx.gas.DefaultGasProvider;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class Arithmetic_sol_arithmeticTest {

    private static final Logger log = LoggerFactory.getLogger(Arithmetic_sol_arithmeticTest.class);
    private static String nodeUrl = "http://localhost:8283/";
    private static HttpService httpService = new HttpService(nodeUrl);
    private static YOUChain youChain = YOUChain.build(httpService);

    private static final String CONTRACT_ADDRESS = "0x253bb3042d5cc9a375cf59e18ecfbd1f2166f21c";

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
        Arithmetic_sol_arithmetic contract = Arithmetic_sol_arithmetic.deploy(youChain, credential, contractGasProvider).send();
        log.info("contract_address={}", contract.getContractAddress());
    }

    @Test
    public void loadAndVerifyContract() throws Exception {
        String fromUserPrivateKey = "PrivateKey";
        Credentials credential = Credentials.create(fromUserPrivateKey);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        Arithmetic_sol_arithmetic contract = Arithmetic_sol_arithmetic.load(CONTRACT_ADDRESS, youChain, credential, contractGasProvider);
        Assert.assertTrue(contract.isValid());
        BigInteger inputA = BigInteger.TEN;
        BigInteger inputB = BigInteger.valueOf(323);
        BigInteger result = contract.arithmetic_add(inputA, inputB).send();
        Assert.assertEquals(result, BigInteger.valueOf(333));
    }
}
