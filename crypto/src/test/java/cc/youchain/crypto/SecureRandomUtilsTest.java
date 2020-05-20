package cc.youchain.crypto;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static cc.youchain.crypto.SecureRandomUtils.isAndroidRuntime;
import static cc.youchain.crypto.SecureRandomUtils.secureRandom;

public class SecureRandomUtilsTest {

    @Test
    public void testSecureRandom() {
        secureRandom().nextInt();
    }

    @Test
    public void testIsNotAndroidRuntime() {
        assertFalse(isAndroidRuntime());
    }
}
