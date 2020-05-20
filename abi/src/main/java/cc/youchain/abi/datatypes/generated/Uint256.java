package cc.youchain.abi.datatypes.generated;

import cc.youchain.abi.datatypes.Uint;
import java.math.BigInteger;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use cc.youchain.codegen.AbiTypesGenerator to update.
 */
public class Uint256 extends Uint {
    public static final Uint256 DEFAULT = new Uint256(BigInteger.ZERO);

    public Uint256(BigInteger value) {
        super(256, value);
    }

    public Uint256(long value) {
        this(BigInteger.valueOf(value));
    }
}
