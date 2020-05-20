package cc.youchain.protocol.core.methods.response;

import java.math.BigInteger;

import cc.youchain.utils.Numeric;
import cc.youchain.protocol.core.Response;

/**
 * net_peerCount.
 */
public class NetPeerCount extends Response<String> {

    public BigInteger getQuantity() {
        return Numeric.decodeQuantity(getResult());
    }
}
