package cc.youchain.integration.demos;

import cc.youchain.abi.FunctionEncoder;
import cc.youchain.abi.TypeReference;
import cc.youchain.abi.datatypes.Address;
import cc.youchain.abi.datatypes.Function;
import cc.youchain.abi.datatypes.generated.Uint256;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.admin.Admin;
import cc.youchain.protocol.core.DefaultBlockParameter;
import cc.youchain.protocol.core.DefaultBlockParameterName;
import cc.youchain.protocol.core.methods.request.Transaction;
import cc.youchain.protocol.core.methods.response.*;
import cc.youchain.protocol.http.HttpService;
import cn.hutool.json.JSONUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class RpcYOUChainCoreDemos {

    private static final Logger log = LoggerFactory.getLogger(RpcYOUChainCoreDemos.class);
    private static String nodeUrl = "http://39.102.70.117:8283/";
    private static HttpService httpService = new HttpService(nodeUrl);
    private static YOUChain youChain = YOUChain.build(httpService);
    private static final String PASSWORD = "password";

    @Test
    public void test_youChainClientVersion() throws IOException {
        YOUChainClientVersion youChainClientVersion = youChain.youChainClientVersion().send();
        Assert.assertTrue(youChainClientVersion.getYOUChainClientVersion() != null);
    }

    @Test
    public void test_youChainSha3() throws IOException {
        String data = "0x68656c6c6f20776f726c64";
        YOUChainSha3 youChainSha3 = youChain.youChainSha3(data).send();
        Assert.assertTrue(youChainSha3.getResult() != null);
    }

    @Test
    public void test_netVersion() throws IOException {
        NetVersion netVersion = youChain.netVersion().send();
        Assert.assertTrue(netVersion.getResult() != null);
    }

    @Test
    public void test_netListening() throws IOException {
        NetListening netListening = youChain.netListening().send();
        Assert.assertTrue(netListening.isListening());
    }

    @Test
    public void test_netPeerCount() throws IOException {
        NetPeerCount netPeerCount = youChain.netPeerCount().send();
        Assert.assertTrue(netPeerCount.getQuantity().compareTo(BigInteger.ZERO) >= 0);
    }

    @Test
    public void test_youProtocolVersion() throws IOException {
        YOUProtocolVersion youProtocolVersion = youChain.youProtocolVersion().send();
        Assert.assertNotNull(youProtocolVersion.getProtocolVersion());
    }

    @Test
    public void test_youCoinbase() throws IOException {
        YOUCoinbase youCoinbase = youChain.youCoinbase().send();
        Assert.assertNotNull(youCoinbase.getAddress());
    }

    @Test
    public void test_youSyncing() throws IOException {
        YOUSyncing youSyncing = youChain.youSyncing().send();
        Assert.assertFalse(youSyncing.isSyncing());
    }

    @Test
    public void test_youMining() throws IOException {
        YOUMining youMining = youChain.youMining().send();
        Assert.assertTrue(youMining.isMining());
    }

    @Test
    public void test_youNetworkId() throws IOException {
        YOUNetworkId youNetworkId = youChain.youNetworkId().send();
        Assert.assertNotNull(youNetworkId.getYOUNetworkId());
    }

    @Test
    public void test_youAccounts_Balance_Storage() throws IOException {

        YOUCoinbase youCoinbase = youChain.youCoinbase().send();
        String address = youCoinbase.getAddress();
        YOUAccounts youAccounts = youChain.youAccounts().send();
        Assert.assertTrue(!youAccounts.getAccounts().isEmpty());

        YOUGetBalance youGetBalance = youChain.youGetBalance(address, DefaultBlockParameterName.PENDING).send();
        Assert.assertTrue(youGetBalance.getBalance().compareTo(BigInteger.ZERO) == 0);

        YOUGetStorageAt youGetStorageAt = youChain.youGetStorageAt(address, BigInteger.ZERO, DefaultBlockParameterName.PENDING).send();
        Assert.assertNotNull(youGetStorageAt);
        Assert.assertNotNull(youGetStorageAt.getData());
    }

    @Test
    public void test_youGetBlockTransactionCountByNumber() throws IOException {
        YOUGetBlockTransactionCountByNumber youGetBlockTransactionCountByNumber = youChain.youGetBlockTransactionCountByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(20000))).send();
        Assert.assertTrue(youGetBlockTransactionCountByNumber.getTransactionCount().compareTo(BigInteger.ZERO) >= 0);
    }

    @Test
    public void test_youSign() throws IOException {
        // 发送者地址 改地址需要在客户端账户列表里面，也就是youAccounts的返回结果里面，否则会报unknown account
        String address = "0x6A8b03c7F1653374198bf3a5De97ab6609EE18c7";
        String data = "0x68656c6c6f20776f726c64";
        YOUChainSha3 youChainSha3 = youChain.youChainSha3(data).send();
        String sha3HashOfDataToSign = youChainSha3.getResult();

        // 调用youSign需要先解锁账户地址
        Admin admin = Admin.build(httpService);
        admin.personalUnlockAccount(address, PASSWORD).send();

        YOUSign youSign = youChain.youSign(address, sha3HashOfDataToSign).send();
        Assert.assertNotNull(youSign);
        Assert.assertNotNull(youSign.getSignature());
    }

    @Test
    public void test_youEstimateGas() throws IOException {
        String address = "0x8fb88eb4cce3b86ca522d5880b554fcdd3caea87";
        String contractAddress = "0x238fa6dcd36fe92851127a5b663beee40e2e3302";
        Function function = new Function("balanceOf",  // function we're calling
                Arrays.asList(new Address(address)),  // Parameters to pass as Solidity Types
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createYOUCallTransaction(address, contractAddress, encodedFunction);
        YOUEstimateGas youEstimateGas = youChain.youEstimateGas(transaction).send();
        Assert.assertTrue(youEstimateGas.getAmountUsed().compareTo(BigInteger.ZERO) > 0);
    }

    @Test
    public void test_youGetBlockByNumber() throws IOException {
        YOUBlock youBlock = youChain.youGetBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
        Assert.assertNotNull(youBlock.getBlock());
        System.out.println(JSONUtil.toJsonStr(youBlock.getBlock()));
    }

    @Test
    public void test_youGetTransactionByHash() throws IOException {
        YOUTransaction youTransaction = youChain.youGetTransactionByHash("0xa82dc96a042ca8cacd4e0ca4221e3d2c63c057e04e2a5896257d871262a8df24").send();
        Assert.assertNotNull(youTransaction);
    }

    @Test
    public void test_youGetTransactionByBlockHashAndIndex() throws IOException {
        String blockHash = "0xfe1e986e7b81e5d71cca7d95af95ab80b7d6c7fbe3293bf4806238c9ff61dc65";
        YOUTransaction youTransaction = youChain.youGetTransactionByBlockHashAndIndex(blockHash, BigInteger.ZERO).send();
        Assert.assertNotNull(youTransaction);
    }

    @Test
    public void test_youGetTransactionByBlockNumberAndIndex() throws IOException {
        YOUTransaction youTransaction = youChain.youGetTransactionByBlockNumberAndIndex(DefaultBlockParameter.valueOf(BigInteger.valueOf(0xc6f4)), BigInteger.ZERO).send();
        Assert.assertNotNull(youTransaction);
    }

    @Test
    public void test_youGetProof() throws IOException {
        String[] storageKeys = {"0x000000000000000000000000000000000000000000000001bc16d674ec800000"};
        YOUGetProof youGetProof = youChain.youGetProof("0xcF9F5007AA1A9ae7b8092DBE3E12B3B97fD5b18D", storageKeys, DefaultBlockParameterName.LATEST).send();
        Assert.assertNotNull(youGetProof);
    }

}
