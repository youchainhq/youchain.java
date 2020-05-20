package cc.youchain.protocol.core.methods.response;

import java.math.BigInteger;

import cc.youchain.utils.Numeric;
import cc.youchain.protocol.core.Response;

/**
 * you_estimateGas.
 */
public class YOUEstimateGas extends Response<String> {
    public BigInteger getAmountUsed() {
        return Numeric.decodeQuantity(getResult());
    }
}
