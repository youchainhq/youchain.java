package cc.youchain.console;

import cc.youchain.codegen.Console;
import cc.youchain.codegen.SolidityFunctionWrapperGenerator;
import cc.youchain.codegen.YOUBoxJsonFunctionWrapperGenerator;
import cc.youchain.utils.Version;

import static cc.youchain.codegen.SolidityFunctionWrapperGenerator.COMMAND_SOLIDITY;
import static cc.youchain.utils.Collection.tail;

/**
 * Main entry point for running command line utilities.
 */
public class Runner {

    private static String USAGE = "Usage: youchain version|wallet|solidity ...";

    private static String LOGO = "\n" // generated at http://patorjk.com/software/taag
            + " __     ______  _    _  _____ _           _       \n"
            + " \\ \\   / / __ \\| |  | |/ ____| |         (_)      \n"
            + "  \\ \\_/ / |  | | |  | | |    | |__   __ _ _ _ __  \n"
            + "   \\   /| |  | | |  | | |    | '_ \\ / _` | | '_ \\ \n"
            + "    | | | |__| | |__| | |____| | | | (_| | | | | |\n"
            + "    |_|  \\____/ \\____/ \\_____|_| |_|\\__,_|_|_| |_|\n";

    public static void main(String[] args) throws Exception {
        System.out.println(LOGO);

        if (args.length < 1) {
            Console.exitError(USAGE);
        } else {
            switch (args[0]) {
                case "wallet":
                    WalletRunner.run(tail(args));
                    break;
                case COMMAND_SOLIDITY:
                    SolidityFunctionWrapperGenerator.main(tail(args));
                    break;
                case "youbox":
                    YOUBoxJsonFunctionWrapperGenerator.run(tail(args));
                    break;
                case "version":
                    Console.exitSuccess("Version: " + Version.getVersion() + "\n"
                            + "Build timestamp: " + Version.getTimestamp());
                    break;
                default:
                    Console.exitError(USAGE);
            }
        }
    }
}
