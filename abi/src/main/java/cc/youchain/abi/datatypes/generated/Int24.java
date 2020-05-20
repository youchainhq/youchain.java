package cc.youchain.abi.datatypes.generated;

import cc.youchain.abi.datatypes.Int;
import java.math.BigInteger;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use cc.youchain.codegen.AbiTypesGenerator to update.
 */
public class Int24 extends Int {
    public static final Int24 DEFAULT = new Int24(BigInteger.ZERO);

    public Int24(BigInteger value) {
        super(24, value);
    }

    public Int24(long value) {
        this(BigInteger.valueOf(value));
    }
}
