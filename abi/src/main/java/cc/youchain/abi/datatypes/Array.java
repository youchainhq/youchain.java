package cc.youchain.abi.datatypes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Fixed size array.
 */
public abstract class Array<T extends Type> implements Type<List<T>> {

    private String type;
    protected final List<T> value;

    @SafeVarargs
    Array(String type, T... values) {
        checkValid(type, Arrays.asList(values));
        
        this.type = type;
        this.value = Arrays.asList(values);
    }

    Array(String type, List<T> values) {
        checkValid(type, values);

        this.type = type;
        this.value = values;
    }

    Array(String type) {
        this.type = type;
        this.value = Collections.emptyList();
    }

    @Override
    public List<T> getValue() {
        return value;
    }

    @Override
    public String getTypeAsString() {
        return type;
    }

    private void checkValid(String type, List<T> values) {
        Objects.requireNonNull(type);
   
        if (values == null || values.size() == 0) {
            throw new UnsupportedOperationException(
                "If empty list is provided, use empty array instance");
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Array<?> array = (Array<?>) o;

        if (!type.equals(array.type)) {
            return false;
        }
        return value != null ? value.equals(array.value) : array.value == null;

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
