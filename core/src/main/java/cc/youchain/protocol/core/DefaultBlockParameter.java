package cc.youchain.protocol.core;

import java.math.BigInteger;

/**
 * Wrapper for parameter that takes either a block number or block name as input.
 */
public interface DefaultBlockParameter {
    static DefaultBlockParameter valueOf(BigInteger blockNumber) {
        if (BigInteger.ZERO.compareTo(blockNumber) >= 0) {
            blockNumber = BigInteger.ZERO;
        }
        return new DefaultBlockParameterNumber(blockNumber);
    }

    static DefaultBlockParameter valueOf(String blockName) {
        return DefaultBlockParameterName.fromString(blockName);
    }

    String getValue();
}
