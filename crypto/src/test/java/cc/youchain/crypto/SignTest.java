package cc.youchain.crypto;

import org.bouncycastle.math.ec.ECPoint;
import org.junit.Test;
import cc.youchain.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class SignTest {

    private static final byte[] TEST_MESSAGE = "A test message".getBytes();

    @Test
    public void testSignMessage() {
        Sign.SignatureData signatureData =
                Sign.signPrefixedMessage(TEST_MESSAGE, TestKeys.KEY_PAIR);

        Sign.SignatureData expected =
                new Sign.SignatureData(
                        (byte) 28,
                        Numeric.hexStringToByteArray(
                                "0xee1633059e050d4ae6bec7b97ffe039bbb04fe06ec78519fd9654303d02fb894"),
                        Numeric.hexStringToByteArray(
                                "0x69b3af4a865763008391e69b801b5c80a10b8b3f5bfe344a2bddde01715f9ee9"));
        assertThat(signatureData, is(expected));
    }

    @Test
    public void testSignedMessageToKey() throws SignatureException {
        Sign.SignatureData signatureData =
                Sign.signPrefixedMessage(TEST_MESSAGE, TestKeys.KEY_PAIR);
        BigInteger key = Sign.signedPrefixedMessageToKey(TEST_MESSAGE, signatureData);
        assertThat(key, equalTo(TestKeys.PUBLIC_KEY));
    }

    @Test
    public void testPublicKeyFromPrivateKey() {
        assertThat(Sign.publicKeyFromPrivate(TestKeys.PRIVATE_KEY), equalTo(TestKeys.PUBLIC_KEY));
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidSignature() throws SignatureException {
        Sign.signedMessageToKey(TEST_MESSAGE, new Sign.SignatureData((byte) 27, new byte[]{1}, new byte[]{0}));
    }

    @Test
    public void testPublicKeyFromPrivatePoint() {
        ECPoint point = Sign.publicPointFromPrivate(TestKeys.PRIVATE_KEY);
        assertThat(Sign.publicFromPoint(point.getEncoded(false)), equalTo(TestKeys.PUBLIC_KEY));
    }
}
