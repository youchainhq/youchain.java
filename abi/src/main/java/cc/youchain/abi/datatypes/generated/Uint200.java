package cc.youchain.abi.datatypes.generated;

import cc.youchain.abi.datatypes.Uint;
import java.math.BigInteger;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use cc.youchain.codegen.AbiTypesGenerator to update.
 */
public class Uint200 extends Uint {
    public static final Uint200 DEFAULT = new Uint200(BigInteger.ZERO);

    public Uint200(BigInteger value) {
        super(200, value);
    }

    public Uint200(long value) {
        this(BigInteger.valueOf(value));
    }
}
