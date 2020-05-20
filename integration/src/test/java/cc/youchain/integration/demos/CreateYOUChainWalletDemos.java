package cc.youchain.integration.demos;

import cc.youchain.crypto.Bip39Wallet;
import cc.youchain.crypto.Bip44WalletUtils;
import cc.youchain.crypto.Credentials;
import cc.youchain.crypto.WalletUtils;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.http.HttpService;
import cc.youchain.utils.Convert;
import cc.youchain.utils.Numeric;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateYOUChainWalletDemos {

    private static final Logger log = LoggerFactory.getLogger(CreateYOUChainWalletDemos.class);

    /**
     * 根据指定密码生成钱包的keyStore文件演示
     *
     * @throws Exception
     */
    @Test
    public void createWalletDemo() throws Exception {
        String password = "123456";
        String tmpdir = WalletUtils.getDefaultKeyDirectory();
        Path path = Paths.get(tmpdir);
        if (!Files.exists(path)) {
            path = Files.createDirectory(path);
        }
        File keyStoreFile = path.toFile();
        // 生成钱包文件
        String fileName = WalletUtils.generateNewWalletFile(password, keyStoreFile);

        // 通过keyStore文件加载证书
        Credentials credentials = WalletUtils.loadCredentials(password, new File(tmpdir, fileName));
        String address = credentials.getAddress();
        BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();
        BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
        log.info("keyStore文件为:{}，请妥善保管", tmpdir + "/" + fileName);
        log.info("钱包地址为:{}", address);
        log.info("钱包私钥为:{}", privateKey.toString());
        log.info("钱包公钥为:{}", publicKey.toString());
        String privateKeyHex = Numeric.toHexStringWithPrefix(privateKey);
        log.info("钱包私钥(16进制)为:{}，请妥善保管", privateKeyHex);
        log.info("钱包公钥(16进制)为:{}", Numeric.toHexStringWithPrefix(publicKey));

        // 通过privateKey加载证书
        credentials = Credentials.create(privateKeyHex);
        log.info("钱包地址为:{}", credentials.getAddress());
        log.info("钱包私钥为:{}", Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey()));
        log.info("钱包公钥为:{}", Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPublicKey()));

        // 删除keyStore文件 如果不需要keyStore文件的话，这么做的前提是需要确保privateKey不会丢失，否则钱包将无法找回
//        keyStoreFile.delete();
    }

    /**
     * 根据指定密码生成Bip39钱包的keyStore文件
     *
     * @throws Exception
     */
    @Test
    public void createBip39WalletDemo() throws Exception {
        String password = "Z12345678";
        String tmpdir = "/Users/liuzuoli/Downloads/YOUChainj-1.1.2";
        Path path = Paths.get(tmpdir);
        if (!Files.exists(path)) {
            path = Files.createDirectory(path);
        }
        File keyStoreFile = path.toFile();
        Bip39Wallet bip39Wallet = WalletUtils.generateBip39Wallet(password, keyStoreFile);
        String fileName = bip39Wallet.getFilename();
        log.info("keyStore文件为:{}，请妥善保管", tmpdir + "/" + fileName);
        String mnemonic = bip39Wallet.getMnemonic();
        log.info("助记词为:{}，请妥善保管", mnemonic);

        // 通过keyStore文件
        Credentials credentials = WalletUtils.loadCredentials(password, new File(tmpdir, fileName));
        String address = credentials.getAddress();
        BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();
        BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
        log.info("keyStore文件为:{}，请妥善保管", tmpdir + fileName);
        log.info("钱包地址为:{}", address);
        log.info("钱包私钥为:{}", privateKey.toString());
        log.info("钱包公钥为:{}", publicKey.toString());
        String privateKeyHex = Numeric.toHexStringWithPrefix(privateKey);
        log.info("钱包私钥(16进制)为:{}，请妥善保管", privateKeyHex);
        log.info("钱包公钥(16进制)为:{}", Numeric.toHexStringWithPrefix(publicKey));

        // 通过密码和助记词文件加载证书
        credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
        log.info("钱包地址为:{}", credentials.getAddress());
        log.info("钱包私钥为:{}", Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey()));
        log.info("钱包公钥为:{}", Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPublicKey()));

        // 删除keyStore文件 如果不需要keyStore文件的话，这么做的前提是需要确保privateKey不会丢失，否则钱包将无法找回
//        keyStoreFile.delete();
    }

    @Test
    public void eee() {
        BigDecimal amountToTransfer = BigDecimal.valueOf(2.213);
        BigDecimal lu = Convert.toLu(amountToTransfer, Convert.Unit.YOU);
        String luStr = lu.toPlainString();
        System.out.println(lu);
        System.out.println(lu.longValue());
        if (luStr.indexOf(".") != -1) {
            luStr = luStr.substring(0, luStr.indexOf("."));
        }
        System.out.println(luStr);
        BigInteger amountInLu = new BigInteger(luStr);
        System.out.println(amountInLu.toString());
    }

    @Test
    public void importWalletFromKeyStoreFileDemo() throws Exception {
        String password = "Z12345678";
        String tmpdir = "/Users/liuzuoli/Downloads/YOUChainj-1.1.2/2.json";

        // 通过keyStore文件加载证书
        Credentials credentials = WalletUtils.loadCredentials(password, tmpdir);
        String address = credentials.getAddress();
        BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();
        BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
        log.info("钱包地址为:{}", address);
        log.info("钱包私钥为:{}", privateKey.toString());
        log.info("钱包公钥为:{}", publicKey.toString());
        String privateKeyHex = Numeric.toHexStringWithPrefix(privateKey);
        log.info("钱包私钥(16进制)为:{}，请妥善保管", privateKeyHex);
        log.info("钱包公钥(16进制)为:{}", Numeric.toHexStringWithPrefix(publicKey));

        // 通过privateKey加载证书
        credentials = Credentials.create(privateKeyHex);
        log.info("钱包地址为:{}", credentials.getAddress());
        log.info("钱包私钥为:{}", Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey()));
        log.info("钱包公钥为:{}", Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPublicKey()));

        // 删除keyStore文件 如果不需要keyStore文件的话，这么做的前提是需要确保privateKey不会丢失，否则钱包将无法找回
//        keyStoreFile.delete();
    }

    @Test
    public void importWalletFromMnemonicDemo() throws Exception {
        String password = "Z12345678";
        String mnemonic = "supreme maze half two elbow steak strategy curious pond surround anxiety top";

        // 通过keyStore文件加载证书
        Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);

        log.info("钱包地址为:{}", credentials.getAddress());
//        log.info("钱包私钥为:{}", Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey()));
//        log.info("钱包公钥为:{}", Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPublicKey()));

        credentials = Bip44WalletUtils.loadBip44Credentials(password, mnemonic, false);
        log.info("钱包地址为:{}", credentials.getAddress());
        credentials = Bip44WalletUtils.loadBip44Credentials(password, mnemonic, true);
        log.info("钱包地址为:{}", credentials.getAddress());

        credentials = Bip44WalletUtils.loadBip39Credentials(password, mnemonic);
        log.info("钱包地址为:{}", credentials.getAddress());
        // 删除keyStore文件 如果不需要keyStore文件的话，这么做的前提是需要确保privateKey不会丢失，否则钱包将无法找回
//        keyStoreFile.delete();
    }
}
