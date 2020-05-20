package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;
import cc.youchain.utils.Numeric;

import java.math.BigInteger;

/**
 * you_getPoolNonce.
 */
public class YOUGetPoolNonce extends Response<String> {
    public BigInteger getPoolNonce() {
        return Numeric.decodeQuantity(getResult());
    }
}