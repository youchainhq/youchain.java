package cc.youchain.protocol.core.methods.request;

import java.util.Arrays;
import java.util.List;

import cc.youchain.protocol.core.DefaultBlockParameter;

/**
 * Filter implementation as per
 */
public class YOUFilter extends Filter<YOUFilter> {
    private DefaultBlockParameter fromBlock;  // optional, params - defaults to latest for both
    private DefaultBlockParameter toBlock;
    private List<String> address;  // spec. implies this can be single address as string or list

    public YOUFilter() {
        super();
    }

    public YOUFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock,
                     List<String> address) {
        super();
        this.fromBlock = fromBlock;
        this.toBlock = toBlock;
        this.address = address;
    }

    public YOUFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock,
                     String address) {
        this(fromBlock, toBlock, Arrays.asList(address));
    }

    public DefaultBlockParameter getFromBlock() {
        return fromBlock;
    }

    public DefaultBlockParameter getToBlock() {
        return toBlock;
    }

    public List<String> getAddress() {
        return address;
    }

    @Override
    YOUFilter getThis() {
        return this;
    }
}
