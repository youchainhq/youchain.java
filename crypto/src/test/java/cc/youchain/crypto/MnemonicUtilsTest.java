package cc.youchain.crypto;

import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class MnemonicUtilsTest {

    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();

    @Test
    public void testGenerateMnemonic() {
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        assertTrue(mnemonic != null);
    }

    @Test
    public void testToChecksumAddress() {
        assertTrue(MnemonicUtils.validateMnemonic(TestKeys.MNEMONIC));
    }

    @Test
    public void testGenerateSeed() {
        byte[] actualSeed = MnemonicUtils.generateSeed(TestKeys.MNEMONIC, "TREZOR");
        assertTrue(actualSeed.length > 0);
    }
}
