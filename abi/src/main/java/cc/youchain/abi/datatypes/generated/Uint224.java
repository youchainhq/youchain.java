package cc.youchain.abi.datatypes.generated;

import cc.youchain.abi.datatypes.Uint;
import java.math.BigInteger;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use cc.youchain.codegen.AbiTypesGenerator to update.
 */
public class Uint224 extends Uint {
    public static final Uint224 DEFAULT = new Uint224(BigInteger.ZERO);

    public Uint224(BigInteger value) {
        super(224, value);
    }

    public Uint224(long value) {
        this(BigInteger.valueOf(value));
    }
}
