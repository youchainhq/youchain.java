package cc.youchain.abi.datatypes.generated;

import cc.youchain.abi.datatypes.StaticArray;
import cc.youchain.abi.datatypes.Type;
import java.util.List;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use cc.youchain.codegen.AbiTypesGenerator to update.
 */
public class StaticArray20<T extends Type> extends StaticArray<T> {
    public StaticArray20(List<T> values) {
        super(20, values);
    }

    @SafeVarargs
    public StaticArray20(T... values) {
        super(20, values);
    }
}
