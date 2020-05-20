package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.core.Response;
import cc.youchain.utils.Numeric;

import java.math.BigInteger;

/**
 * you_networkId.
 */
public class YOUNetworkId extends Response<String> {

    public BigInteger getYOUNetworkId() {
        return Numeric.decodeQuantity(getResult());
    }
}
