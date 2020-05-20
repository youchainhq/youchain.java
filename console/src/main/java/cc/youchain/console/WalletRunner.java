package cc.youchain.console;

import cc.youchain.codegen.Console;

import static cc.youchain.utils.Collection.tail;

/**
 * Class for managing our wallet command line utilities.
 */
public class WalletRunner {
    private static final String USAGE = "wallet create|update|transfer|sign|sendRaw|fromkey";

    public static void run(String[] args) {
        main(args);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            Console.exitError(USAGE);
        } else {
            switch (args[0]) {
                case "create":
                    WalletCreator.main(new String[]{});
                    break;
                case "update":
                    WalletUpdater.main(tail(args));
                    break;
                case "transfer":
                    WalletSendFunds.main(tail(args));
                    break;
                case "sign":
                    WalletOfflineSignature.main(tail(args));
                    break;
                case "sendRaw":
                    WalletSendRaw.main(tail(args));
                    break;
                case "fromkey":
                    KeyImporter.main(tail(args));
                    break;
                default:
                    Console.exitError(USAGE);
            }
        }
    }
}
