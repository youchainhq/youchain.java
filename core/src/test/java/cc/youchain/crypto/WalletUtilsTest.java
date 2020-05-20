package cc.youchain.crypto;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;

public class WalletUtilsTest {

    private static final Logger log = LoggerFactory.getLogger(WalletUtilsTest.class);

    public static final String PASSWORD = "Test_Password_123456";

    private File tempDir;

    @Before
    public void setUp() throws Exception {
        tempDir = Files.createTempDirectory(WalletUtilsTest.class.getSimpleName() + "-test-keys").toFile();
        log.info(tempDir.getAbsolutePath());
    }

    @After
    public void tearDown() throws Exception {
//        for (File file : tempDir.listFiles()) {
//            file.delete();
//        }
//        tempDir.delete();
    }

    @Test
    public void testGenerateFullNewWalletFile() throws Exception {
        String fileName = WalletUtils.generateFullNewWalletFile(PASSWORD, tempDir);
        testGeneratedNewWalletFile(fileName);
    }

    @Test
    public void testGenerateNewWalletFile() throws Exception {
        String fileName = WalletUtils.generateNewWalletFile(PASSWORD, tempDir);
        testGeneratedNewWalletFile(fileName);
    }

    @Test
    public void testGenerateLightNewWalletFile() throws Exception {
        String fileName = WalletUtils.generateLightNewWalletFile(PASSWORD, tempDir);
        testGeneratedNewWalletFile(fileName);
    }

    private void testGeneratedNewWalletFile(String fileName) throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(PASSWORD, new File(tempDir, fileName));
        log.info("address={}", credentials.getAddress());
        log.info("privateKey={}", credentials.getEcKeyPair().getPrivateKey());
        log.info("publicKey={}", credentials.getEcKeyPair().getPublicKey());
    }

    @Test
    public void testGetDefaultKeyDirectory() {
        String pathStr = WalletUtils.getDefaultKeyDirectory();
        Assert.assertNotEquals(pathStr, null);
        String testPathStr = WalletUtils.getTestnetKeyDirectory();
        Assert.assertNotEquals(testPathStr, null);
        String mainPathStr = WalletUtils.getMainnetKeyDirectory();
        Assert.assertNotEquals(mainPathStr, null);
    }

    @Test
    public void testGenerateBip39Wallet() throws Exception {
        Bip39Wallet bip39Wallet = WalletUtils.generateBip39Wallet(PASSWORD, tempDir);
        log.info("mnemonic={}, filename={}, bip39Wallet={}", bip39Wallet.getMnemonic(), bip39Wallet.getFilename(), bip39Wallet.toString());
        Assert.assertTrue(bip39Wallet.getMnemonic() != null);
        Credentials credentials = WalletUtils.loadCredentials(PASSWORD, new File(tempDir, bip39Wallet.getFilename()));
        log.info("address={}", credentials.getAddress());
        log.info("privateKey={}", credentials.getEcKeyPair().getPrivateKey());
        log.info("publicKey={}", credentials.getEcKeyPair().getPublicKey());
//        WalletUtils.loadBip39Credentials(PASSWORD, bip39Wallet.getMnemonic());
//
//        bip39Wallet = WalletUtils.generateBip39WalletFromMnemonic(PASSWORD, bip39Wallet.getMnemonic(), tempDir);
//        log.info("mnemonic={}, filename={}", bip39Wallet.getMnemonic(), bip39Wallet.getFilename());
//        Assert.assertTrue(bip39Wallet.getMnemonic() != null);
    }
}
