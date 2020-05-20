package cc.youchain.crypto;

import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ContractUtilsTest {

    @Test
    public void testCreateContractAddress() {
        assertThat(
                ContractUtils.generateContractAddress(TestKeys.ADDRESS, BigInteger.valueOf(209)),
                is("0x874bfdedd53d7c4b776128621abb670433f54abc"));

        assertThat(
                ContractUtils.generateContractAddress(TestKeys.ADDRESS, BigInteger.valueOf(257)),
                is("0x17402ba8a271d7415fa24f689fb7ce87f2dcfdb8"));
    }
}
