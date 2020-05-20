package cc.youchain.integration.demos;

import cc.youchain.crypto.Hash;
import cc.youchain.protocol.admin.Admin;
import cc.youchain.protocol.admin.methods.response.*;
import cc.youchain.protocol.core.DefaultBlockParameterName;
import cc.youchain.protocol.core.methods.request.Transaction;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;
import cc.youchain.protocol.http.HttpService;
import cc.youchain.tx.Transfer;
import cc.youchain.utils.Convert;
import cc.youchain.utils.Numeric;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class RpcPersonalAdminDemos {

    private static final Logger logger = LoggerFactory.getLogger(RpcPersonalAdminDemos.class);
    private static String nodeUrl = "http://localhost:8283/";
    private static HttpService httpService = new HttpService(nodeUrl);
    private static Admin admin = Admin.build(httpService);
    private static final String PASSWORD = "password";

    @Test
    public void test_personalListAccounts() throws IOException {
        PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
        Assert.assertNotNull(personalListAccounts);
        personalListAccounts.getAccountIds().forEach(address -> {
            logger.info("address={}", address);
        });
    }

    @Test
    public void test_personalNewAccount() throws IOException {
        NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(PASSWORD).send();
        Assert.assertNotNull(newAccountIdentifier);
        logger.info("address={}", newAccountIdentifier.getAccountId());
    }

    @Test
    public void test_personalUnlockAccount() throws IOException {
        String address = "0xcee89b240f8a08c59d5fa20c195f2ad38feccb88";
        PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(address, PASSWORD).send();
        Assert.assertTrue(personalUnlockAccount.accountUnlocked());
    }

    @Test
    public void test_personalLockAccount() throws IOException {
        String address = "0xcee89b240f8a08c59d5fa20c195f2ad38feccb88";
        PersonalLockAccount personalLockAccount = admin.personalLockAccount(address).send();
        Assert.assertTrue(personalLockAccount.accountLocked());
    }

    @Test
    public void test_personalImportRawKey() throws IOException {
        String privateKey = "PrivateKey";
        privateKey = Numeric.cleanHexPrefix(privateKey);
        String keyPassword = "password";
        PersonalImportRawKey personalImportRawKey = admin.personalImportRawKey(privateKey, keyPassword).send();
        Assert.assertNotNull(personalImportRawKey);
        logger.info("address={}", personalImportRawKey.getAccountId());
    }

    @Test
    public void test_personalSign_ecRecover() throws IOException {
//        String data = "0xdeadbeaf";
        String data = Hash.sha3("hello");
        logger.info("data={}", data);
        String address = "0xac0e59bb17be504b79230ff746f029c779800e2e";
        PersonalSign personalSign = admin.personalSign(data, address, PASSWORD).send();
        String signedMessage = personalSign.getSignedMessage();
        Assert.assertNotNull(signedMessage);

        PersonalEcRecover personalEcRecover = admin.personalEcRecover(data, signedMessage).send();
        String addressRecover = personalEcRecover.getRecoverAccountId();
        Assert.assertTrue(address.equalsIgnoreCase(addressRecover));
    }

    @Test
    public void test_personalSendTransaction() throws IOException {
        String password = "Test_Password_123456";
        String fromAddress = "0x0b42eB76113d9EDe60C68B9e6832f65Ff8a78bbF";
        String toAddress = "0xAD07755BCd95c78a733169A4cFCc6F36e3020295";
        BigInteger nonce = admin.youGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        BigInteger gasPrice = admin.youGasPrice().send().getGasPrice();
        BigInteger gasLimit = Transfer.GAS_LIMIT;
        BigInteger amountLu = Convert.toLu(BigDecimal.TEN, Convert.Unit.YOU).toBigInteger();
        Transaction transaction = Transaction.createYOUTransaction(fromAddress, nonce, gasPrice, gasLimit, toAddress, amountLu);
        YOUSendTransaction youSendTransaction = admin.personalSendTransaction(transaction, password).send();
        Assert.assertNotNull(youSendTransaction.getTransactionHash());
    }

    @Test
    public void test_personalSignTransaction() throws IOException {
        String password = "Test_Password_123456";
        String fromAddress =  "0xcee89b240f8a08c59d5fa20c195f2ad38feccb88";
        String toAddress = "0xAD07755BCd95c78a733169A4cFCc6F36e3020295";
        BigInteger nonce = admin.youGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        BigInteger gasPrice = admin.youGasPrice().send().getGasPrice();
        BigInteger gasLimit = Transfer.GAS_LIMIT;
        BigInteger amountLu = Convert.toLu(BigDecimal.TEN, Convert.Unit.YOU).toBigInteger();
        Transaction transaction = Transaction.createYOUTransaction(fromAddress, nonce, gasPrice, gasLimit, toAddress, amountLu);
        PersonalSignTransaction personalSignTransaction = admin.personalSignTransaction(transaction, password).send();
        Assert.assertNotNull(personalSignTransaction);
        Assert.assertNotNull(personalSignTransaction.getResp().getRaw());
        Assert.assertNotNull(personalSignTransaction.getResp().getTx());
    }


}
