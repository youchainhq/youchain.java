package cc.youchain.protocol.admin;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import cc.youchain.protocol.admin.methods.response.*;
import cc.youchain.protocol.YOUChainService;
import cc.youchain.protocol.core.JsonRpc2_0YOUChain;
import cc.youchain.protocol.core.Request;
import cc.youchain.protocol.core.methods.request.Transaction;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;

/**
 * JSON-RPC 2.0 factory implementation for common Parity and Geth.
 */
public class JsonRpc2_0Admin extends JsonRpc2_0YOUChain implements Admin {

    public JsonRpc2_0Admin(YOUChainService youChainService) {
        super(youChainService);
    }

    public JsonRpc2_0Admin(YOUChainService youChainService, long pollingInterval,
                           ScheduledExecutorService scheduledExecutorService) {
        super(youChainService, pollingInterval, scheduledExecutorService);
    }

    @Override
    public Request<?, PersonalListAccounts> personalListAccounts() {
        return new Request<>(
                "personal_listAccounts",
                Collections.<String>emptyList(),
                youChainService,
                PersonalListAccounts.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> personalNewAccount(String password) {
        return new Request<>(
                "personal_newAccount",
                Arrays.asList(password),
                youChainService,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, PersonalUnlockAccount> personalUnlockAccount(
            String address, String password,
            BigInteger duration) {
        List<Object> attributes = new ArrayList<>(3);
        attributes.add(address);
        attributes.add(password);

        if (duration != null) {
            // Parity has a bug where it won't support a duration
            // See https://github.com/ethcore/parity/issues/1215
            attributes.add(duration.longValue());
        } else {
            // we still need to include the null value, otherwise Parity rejects request
            attributes.add(null);
        }

        return new Request<>(
                "personal_unlockAccount",
                attributes,
                youChainService,
                PersonalUnlockAccount.class);
    }

    @Override
    public Request<?, PersonalUnlockAccount> personalUnlockAccount(
            String address, String password) {

        return personalUnlockAccount(address, password, null);
    }

    @Override
    public Request<?, PersonalLockAccount> personalLockAccount(String address) {
        return new Request<>(
                "personal_lockAccount",
                Arrays.asList(address),
                youChainService,
                PersonalLockAccount.class);
    }

    @Override
    public Request<?, PersonalImportRawKey> personalImportRawKey(String rawKey, String password) {
        return new Request<>(
                "personal_importRawKey",
                Arrays.asList(rawKey, password),
                youChainService,
                PersonalImportRawKey.class);
    }

    @Override
    public Request<?, PersonalSign> personalSign(
            String data, String address, String password) {
        return new Request<>(
                "personal_sign",
                Arrays.asList(data, address, password),
                youChainService,
                PersonalSign.class);
    }

    @Override
    public Request<?, PersonalEcRecover> personalEcRecover(
            String hexMessage, String signedMessage) {
        return new Request<>(
                "personal_ecRecover",
                Arrays.asList(hexMessage, signedMessage),
                youChainService,
                PersonalEcRecover.class);
    }

    @Override
    public Request<?, YOUSendTransaction> personalSendTransaction(
            Transaction transaction, String passphrase) {
        return new Request<>(
                "personal_sendTransaction",
                Arrays.asList(transaction, passphrase),
                youChainService,
                YOUSendTransaction.class);
    }

    @Override
    public Request<?, PersonalSignTransaction> personalSignTransaction(
            Transaction transaction, String passphrase) {
        return new Request<>(
                "personal_signTransaction",
                Arrays.asList(transaction, passphrase),
                youChainService,
                PersonalSignTransaction.class);
    }

    @Override
    public Request<?, PersonalNewValKey> personalNewValKey(
            String password) {
        return new Request<>(
                "personal_newValKey",
                Arrays.asList(password),
                youChainService,
                PersonalNewValKey.class);
    }

    @Override
    public Request<?, PersonalDelValKey> personalDelValKey(
            String address, String password) {
        return new Request<>(
                "personal_delValKey",
                Arrays.asList(address, password),
                youChainService,
                PersonalDelValKey.class);
    }

    @Override
    public Request<?, PersonalImportValKey> personalImportValKey(
            String privateKey, String password, String encryptPassword) {
        return new Request<>(
                "personal_importValKey",
                Arrays.asList(privateKey, password, encryptPassword),
                youChainService,
                PersonalImportValKey.class);
    }

    @Override
    public Request<?, PersonalExportValKey> personalExportValKey(
             String address, String password, String encryptPassword) {
        return new Request<>(
                "personal_exportValKey",
                Arrays.asList(address, password, encryptPassword),
                youChainService,
                PersonalExportValKey.class);
    }

    @Override
    public Request<?, PersonalUseValKey> personalUseValKey(
             String address, String password, boolean keep) {
        return new Request<>(
                "personal_useValKey",
                Arrays.asList(address, password, keep),
                youChainService,
                PersonalUseValKey.class);
    }

    @Override
    public Request<?, PersonalLockValKey> personalLockValKey(
            ) {
        return new Request<>(
                "personal_lockValKey",
                Arrays.asList(),
                youChainService,
                PersonalLockValKey.class);
    }

}
