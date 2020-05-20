package cc.youchain.integration.demos;

import cc.youchain.crypto.Credentials;
import cc.youchain.crypto.RawTransaction;
import cc.youchain.crypto.TransactionEncoder;
import cc.youchain.crypto.WalletUtils;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.DefaultBlockParameterName;
import cc.youchain.protocol.core.methods.response.TransactionReceipt;
import cc.youchain.protocol.core.methods.response.YOUGetTransactionReceipt;
import cc.youchain.protocol.core.methods.response.YOUSendRawTransaction;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;
import cc.youchain.protocol.http.HttpService;
import cc.youchain.tx.NetworkId;
import cc.youchain.tx.Transfer;
import cc.youchain.utils.Convert;
import cc.youchain.utils.Numeric;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public class OfflineSignTransactionDemos {

    private static final Logger log = LoggerFactory.getLogger(OfflineSignTransactionDemos.class);
    private static String nodeUrl = "http://test-node.iyouchain.com:80";
    private static HttpService httpService = new HttpService(nodeUrl);
    private static YOUChain youChain = YOUChain.build(httpService);

    /**
     * 离线交易签名演示
     *
     * @throws Exception
     */
    @Test
    public void offlineSignTransactionDemo() throws Exception {
        // 发送者地址
        String addressFrom = "0x942d6cc8a1b4a06b2818277978a4975b651d8038";
        // 发送者私钥
        String privateKey = "0x9fea45d8e7b2db116d19470d60d27ddb87f315b9ca1ddd63a04b884a150aa524";
        // 接收者地址
        String addressTo = "0xcb00539B4ADD42723b828a6b6ea1414fEc2f3367";
        // 发送100YOU
        BigDecimal amount = BigDecimal.valueOf(10);
        Credentials credential = Credentials.create(privateKey);
        BigInteger nonce = youChain.youGetTransactionCount(addressFrom, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        BigInteger gasPrice = youChain.youGasPrice().send().getGasPrice();
        BigInteger amountLu = Convert.toLu(amount, Convert.Unit.YOU).toBigInteger();
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, Transfer.GAS_LIMIT, addressTo, amountLu, "");
        // 签名交易数据
        byte networkId = NetworkId.TESTNET;
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, networkId, credential);
        String signedTransactionData = Numeric.toHexString(signMessage);
        log.info("签名后的交易数据:{}", signedTransactionData);

        // 发送交易数据到节点
        String transactionHash = youChain.youSendRawTransaction(signedTransactionData).send().getTransactionHash();
        log.info("发送交易数据 {}", transactionHash);

        // 查询交易凭证
        TransactionReceipt transactionReceipt = youChain.youGetTransactionReceipt(transactionHash).send().getTransactionReceipt().orElse(null);
        // 这里应该是返回null的，因为需要等交易确认之后才会有凭证
//        log.info("transactionReceipt={}", JSONUtil.toJsonStr(transactionReceipt));

        // 等10秒交易确认
//        Thread.sleep(10000);
        transactionReceipt = youChain.youGetTransactionReceipt(transactionHash).send().getTransactionReceipt().orElse(null);
        CompletableFuture<YOUGetTransactionReceipt> completableFuture = youChain.youGetTransactionReceipt(transactionHash).sendAsync();
        completableFuture.thenAccept(c -> {
            //log.info("thenAccept={}");
            TransactionReceipt receipt = c.getTransactionReceipt().get();
//            log.info("thenAccept result={}", JSONUtil.toJsonStr(receipt));
        });

        Thread.sleep(1000000);

    }


    @Test
    public void sendRawTransactionDemo() throws Exception {
        String password = "Z12345678";
        String tmpdir = "/Users/liuzuoli/Downloads/YOUChainj-1.1.2/1.json";

        // 通过keyStore文件加载证书
        Credentials credentials = WalletUtils.loadCredentials(password, tmpdir);
        String signedTransactionData = "0xf86d0d85746a52880083015f9094fe1593732af8935b89166e6b43e5689a1c2af8c5881d24b2dfac5200008027a09bb28dabbc300f7bdd911da0c7c25e54e2e3592e997ee5d1cd38dbcfe9a6ac1ba056999b36c6f3ffde270c0164cd23953fdfe94915c8bebba642300028a1cc1870";

        // 发送交易数据到节点
        YOUSendTransaction youSendRawTransaction = youChain.youSendRawTransaction(signedTransactionData).send();
        if (youSendRawTransaction.hasError()) {
            log.info("发送交易数据 {}", youSendRawTransaction.getError().getMessage());
            log.info( "sendRawTransaction error: " + youSendRawTransaction.getError().getCode() +  ":" + youSendRawTransaction.getError().getMessage());
        }
        String transactionHash = youSendRawTransaction.getTransactionHash();
        log.info("发送交易数据 {}", transactionHash);


        Thread.sleep(1000000);

    }


}
