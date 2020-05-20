package cc.youchain.crypto;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CredentialsTest {

    @Test
    public void testCredentialsFromString() {
        Credentials credentials = Credentials.create(TestKeys.KEY_PAIR);
        verify(credentials);
    }

    @Test
    public void testCredentialsFromECKeyPair() {
        Credentials credentials = Credentials.create(TestKeys.PRIVATE_KEY_STRING, TestKeys.PUBLIC_KEY_STRING);
        verify(credentials);
    }

    @Test
    public void testCredentialsFromPrivateKey() {
        Credentials credentials = Credentials.create(TestKeys.PRIVATE_KEY_STRING);
        verify(credentials);
    }

    private void verify(Credentials credentials) {
        assertThat(credentials.getAddress(), is(TestKeys.ADDRESS));
        assertThat(credentials.getEcKeyPair(), is(TestKeys.KEY_PAIR));
    }
}
