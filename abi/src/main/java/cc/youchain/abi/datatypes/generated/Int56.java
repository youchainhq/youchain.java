package cc.youchain.abi.datatypes.generated;

import cc.youchain.abi.datatypes.Int;
import java.math.BigInteger;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use cc.youchain.codegen.AbiTypesGenerator to update.
 */
public class Int56 extends Int {
    public static final Int56 DEFAULT = new Int56(BigInteger.ZERO);

    public Int56(BigInteger value) {
        super(56, value);
    }

    public Int56(long value) {
        this(BigInteger.valueOf(value));
    }
}
