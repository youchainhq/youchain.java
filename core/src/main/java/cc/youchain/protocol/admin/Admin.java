package cc.youchain.protocol.admin;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;

import cc.youchain.protocol.admin.methods.response.*;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.YOUChainService;
import cc.youchain.protocol.core.Request;
import cc.youchain.protocol.core.methods.request.Transaction;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;

/**
 * JSON-RPC Request object building factory for common YOUChain client.
 */
public interface Admin extends YOUChain {

    static Admin build(YOUChainService youChainService) {
        return new JsonRpc2_0Admin(youChainService);
    }

    static Admin build(YOUChainService youChainService, long pollingInterval,
                       ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0Admin(youChainService, pollingInterval, scheduledExecutorService);
    }

    Request<?, PersonalListAccounts> personalListAccounts();

    Request<?, NewAccountIdentifier> personalNewAccount(String password);

    Request<?, PersonalUnlockAccount> personalUnlockAccount(String address, String passphrase, BigInteger duration);

    Request<?, PersonalUnlockAccount> personalUnlockAccount(String address, String passphrase);

    Request<?, PersonalLockAccount> personalLockAccount(String address);

    Request<?, PersonalImportRawKey> personalImportRawKey(String rawKey, String password);

    Request<?, PersonalSign> personalSign(String data, String address, String password);

    Request<?, PersonalEcRecover> personalEcRecover(String hexMessage, String signedMessage);

    Request<?, YOUSendTransaction> personalSendTransaction(Transaction transaction, String password);

    Request<?, PersonalSignTransaction> personalSignTransaction(Transaction transaction, String password);

    Request<?, PersonalNewValKey> personalNewValKey(String password);

    Request<?, PersonalImportValKey> personalImportValKey(String privateKey, String password, String encryptPassword);

    Request<?, PersonalExportValKey> personalExportValKey(String address, String password, String encryptPassword);

    Request<?, PersonalUseValKey> personalUseValKey(String address, String password, boolean keep);

    Request<?, PersonalLockValKey> personalLockValKey();

    Request<?, PersonalDelValKey> personalDelValKey(String address, String password);

}   
