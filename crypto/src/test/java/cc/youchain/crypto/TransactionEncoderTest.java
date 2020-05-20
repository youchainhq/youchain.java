package cc.youchain.crypto;

import cc.youchain.rlp.RlpString;
import cc.youchain.rlp.RlpType;
import cc.youchain.utils.Numeric;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TransactionEncoderTest {

    static RawTransaction createRawTransaction() {
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                BigInteger.ZERO,
                BigInteger.ONE,
                BigInteger.valueOf(90000),
                "0xf573c08f1d8403444d1426d868b275efcd5c6710",
                BigInteger.valueOf(100000000).toString());
        return rawTransaction;
    }

    static RawTransaction createContractTransaction() {
        return RawTransaction.createContractTransaction(
                BigInteger.ZERO,
                BigInteger.ONE,
                BigInteger.TEN,
                BigInteger.valueOf(Long.MAX_VALUE),
                "01234566789");
    }

    @Test
    public void testSignMessage() {
        byte[] signedMessage = TransactionEncoder.signMessage(createRawTransaction(), TestKeys.CREDENTIALS);
        String hexMessage = Numeric.toHexString(signedMessage);
        assertThat(hexMessage, is("0xf865800183015f9094f573c08f1d8403444d1426d868b275efcd5c671080850100000000" +
                "1ca06b58bdec8e70fd3b8920375e36a1d6b15e3291b3987f1ca53a3d8a5ba7b8b2d8a058151ab5988a935dde0a32df83f802dfbd3a110a272ad4934ca0195667b57b4e"));
    }

    @Test
    public void testSignMessageEip155() {
        byte networkId = 1;
        byte[] signedMessage = TransactionEncoder.signMessage(createRawTransaction(), networkId, TestKeys.CREDENTIALS);
        String hexMessage = Numeric.toHexString(signedMessage);
        assertThat(hexMessage, is("0xf865800183015f9094f573c08f1d8403444d1426d868b275efcd5c671080850100000000" +
                "26a00f264a92af501dd70a1863912c1c13bc0b3ccb4b64b2b57f942d33ba0db09119a05e86982aeabf146ba6412348a22e4cfba665738308720dd3ecce7775a83eed1e" +
                ""));
    }

    @Test
    public void testYOUTransactionAsRlpValues() {
        List<RlpType> rlpStrings =
                TransactionEncoder.asRlpValues(
                        createRawTransaction(),
                        new Sign.SignatureData((byte) 0, new byte[32], new byte[32]));
        assertThat(rlpStrings.size(), is(9));
        assertThat(rlpStrings.get(3), equalTo(RlpString.create(new BigInteger("f573c08f1d8403444d1426d868b275efcd5c6710", 16))));
    }

    @Test
    public void testContractAsRlpValues() {
        List<RlpType> rlpStrings =
                TransactionEncoder.asRlpValues(createContractTransaction(), null);
        assertThat(rlpStrings.size(), is(6));
        assertThat(rlpStrings.get(3), is(RlpString.create("")));
    }
}
