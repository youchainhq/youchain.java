package cc.youchain.console;

import cc.youchain.crypto.Credentials;
import cc.youchain.crypto.RawTransaction;
import cc.youchain.crypto.TransactionEncoder;
import cc.youchain.crypto.WalletUtils;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.methods.response.YOUChainClientVersion;
import cc.youchain.protocol.core.methods.response.YOUSendTransaction;
import cc.youchain.protocol.http.HttpService;
import cc.youchain.tx.Transfer;
import cc.youchain.utils.Convert;
import cc.youchain.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import static cc.youchain.codegen.Console.exitError;

/**
 * Simple class for sending rawTransaction.
 */
public class WalletSendRaw extends WalletManager {

    private static final String USAGE = "sendRaw <rawTransaction>";

    public static void main(String[] args) {
        if (args.length != 1) {
            exitError(USAGE);
        } else {
            new WalletSendRaw().run(args[0]);
        }
    }

    private void run(String rawTransaction) {
        YOUChain youChain = getYOUChainClient();
        YOUSendTransaction youSendRawTransaction = null;
        try {
            youSendRawTransaction = youChain.youSendRawTransaction(rawTransaction).send();
        } catch (IOException e) {
            exitError("sendRawTransaction error: " + e.getMessage());
        }
        if (youSendRawTransaction.hasError()) {
            exitError("sendRawTransaction error: " + youSendRawTransaction.getError().getCode() +  ": " + youSendRawTransaction.getError().getMessage());
            return;
        }
        String transactionHash = youSendRawTransaction.getTransactionHash();
        console.printf("the rawTransaction has been successfully sent %n" +
                "the hash is %s%n", transactionHash);
        return;
    }

}
