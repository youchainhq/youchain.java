package cc.youchain.protocol;

import java.util.concurrent.ScheduledExecutorService;

import cc.youchain.protocol.core.YOUChainCore;
import cc.youchain.protocol.core.JsonRpc2_0YOUChain;
import cc.youchain.protocol.rx.YOUChainRx;

/**
 * JSON-RPC Request object building factory.
 */
public interface YOUChain extends YOUChainCore, YOUChainRx {

    /**
     * Construct a new YOUChain instance.
     *
     * @param youChainService YOUChain service instance - i.e. HTTP or IPC
     * @return new YOUChain instance
     */
    static YOUChain build(YOUChainService youChainService) {
        return new JsonRpc2_0YOUChain(youChainService);
    }

    /**
     * Construct a new YOUChain instance.
     *
     * @param youChainService YOUChain service instance - i.e. HTTP or IPC
     * @param pollingInterval polling interval for responses from network nodes
     * @param scheduledExecutorService executor service to use for scheduled tasks.
     *                                 <strong>You are responsible for terminating this thread
     *                                 pool</strong>
     * @return new YOUChain instance
     */
    static YOUChain build(
            YOUChainService youChainService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0YOUChain(youChainService, pollingInterval, scheduledExecutorService);
    }

    /**
     * Shutdowns a YOUChain instance and closes opened resources.
     */
    void shutdown();
}
