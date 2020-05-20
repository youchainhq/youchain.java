package cc.youchain.abi.datatypes.generated;

import cc.youchain.abi.datatypes.Int;
import java.math.BigInteger;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use cc.youchain.codegen.AbiTypesGenerator to update.
 */
public class Int8 extends Int {
    public static final Int8 DEFAULT = new Int8(BigInteger.ZERO);

    public Int8(BigInteger value) {
        super(8, value);
    }

    public Int8(long value) {
        this(BigInteger.valueOf(value));
    }
}
