package cc.youchain.abi.datatypes.generated;

import cc.youchain.abi.datatypes.Int;
import java.math.BigInteger;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use cc.youchain.codegen.AbiTypesGenerator to update.
 */
public class Int256 extends Int {
    public static final Int256 DEFAULT = new Int256(BigInteger.ZERO);

    public Int256(BigInteger value) {
        super(256, value);
    }

    public Int256(long value) {
        this(BigInteger.valueOf(value));
    }
}
