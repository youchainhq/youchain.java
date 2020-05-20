package cc.youchain.protocol.core.methods.response;

import java.math.BigInteger;

import cc.youchain.utils.Numeric;
import cc.youchain.protocol.core.Response;

/**
 * you_getBalance.
 */
public class YOUGetBalance extends Response<String> {
    public BigInteger getBalance() {
        return Numeric.decodeQuantity(getResult());
    }
}
