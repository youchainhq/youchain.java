package cc.youchain.crypto;

import cc.youchain.utils.Numeric;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class WalletTest {

    public static final String PASSWORD = "Test_Password_123456";

    @Test
    public void testCreateStandard() throws Exception {
        testCreate(Wallet.createStandard(TestKeys.PASSWORD, TestKeys.KEY_PAIR));
    }

    @Test
    public void testCreateLight() throws Exception {
        testCreate(Wallet.createLight(TestKeys.PASSWORD, TestKeys.KEY_PAIR));
    }

    private void testCreate(WalletFile walletFile) throws Exception {
        assertThat(walletFile.getAddress(), is(TestKeys.ADDRESS_NO_PREFIX));
    }

    @Test
    public void testEncryptDecryptStandard() throws Exception {
        testEncryptDecrypt(Wallet.createStandard(TestKeys.PASSWORD, TestKeys.KEY_PAIR));
    }

    @Test
    public void testEncryptDecryptLight() throws Exception {
        testEncryptDecrypt(Wallet.createLight(TestKeys.PASSWORD, TestKeys.KEY_PAIR));
    }

    private void testEncryptDecrypt(WalletFile walletFile) throws Exception {
        assertThat(Wallet.decrypt(TestKeys.PASSWORD, walletFile), equalTo(TestKeys.KEY_PAIR));
    }

    @Test
    public void testDecryptAes128Ctr() throws Exception {
        WalletFile walletFile = load(TestKeys.AES_128_CTR);
        ECKeyPair ecKeyPair = Wallet.decrypt(PASSWORD, walletFile);
        assertThat(Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey()), is("80bc7e06548255c7a284d4c918331e8e10c794f4a5ed5387840caffb764475db"));
    }

    @Test
    public void testDecryptScrypt() throws Exception {
        WalletFile walletFile = load(TestKeys.SCRYPT);
        ECKeyPair ecKeyPair = Wallet.decrypt(PASSWORD, walletFile);
        assertThat(Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey()), is("f30b6058cc839b9e2ee032d9a023feb231955be950fba33888ac824530f1be9f"));
    }

    @Test
    public void testGenerateRandomBytes() {
        assertThat(Wallet.generateRandomBytes(0), is(new byte[]{}));
        assertThat(Wallet.generateRandomBytes(10).length, is(10));
    }

    private WalletFile load(String source) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(source, WalletFile.class);
    }
}
