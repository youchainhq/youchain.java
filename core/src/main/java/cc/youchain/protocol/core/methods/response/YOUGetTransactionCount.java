package cc.youchain.protocol.core.methods.response;

import java.math.BigInteger;

import cc.youchain.utils.Numeric;
import cc.youchain.protocol.core.Response;

/**
 * you_getTransactionCount.
 */
public class YOUGetTransactionCount extends Response<String> {
    public BigInteger getTransactionCount() {
        return Numeric.decodeQuantity(getResult());
    }
}
