package cc.youchain.protocol.core.methods.response;

import java.math.BigInteger;

import cc.youchain.utils.Numeric;
import cc.youchain.protocol.core.Response;

/**
 * you_blockNumber.
 */
public class YOUBlockNumber extends Response<String> {
    public BigInteger getBlockNumber() {
        return Numeric.decodeQuantity(getResult());
    }
}
