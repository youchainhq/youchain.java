package cc.youchain.crypto;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TransactionUtilsTest {

    @Test
    public void testGenerateTransactionHash() {
        RawTransaction rawTransaction = TransactionEncoderTest.createRawTransaction();
        String hashEncoded = TransactionUtils.generateTransactionHashHexEncoded(rawTransaction, TestKeys.CREDENTIALS);
        System.out.println(hashEncoded);
        assertThat(hashEncoded, is("0x2a864a6fdfb0a083571b0a3fb1211fba8d3a75ebb2f37b7e857db41671e6c571"));
    }

    @Test
    public void testGenerateEip155TransactionHash() {
        RawTransaction rawTransaction = TransactionEncoderTest.createContractTransaction();
        byte networkId = 1;
        String hashEncoded = TransactionUtils.generateTransactionHashHexEncoded(rawTransaction, networkId, TestKeys.CREDENTIALS);
        assertThat(hashEncoded, is("0xafa87a6dad73004394fd4e91ca52ad0c5b8095a8c97271397b46ce3f1f23ad16"));
    }

}
