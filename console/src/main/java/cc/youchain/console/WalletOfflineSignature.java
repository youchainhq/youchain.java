package cc.youchain.console;

import cc.youchain.crypto.Credentials;
import cc.youchain.crypto.RawTransaction;
import cc.youchain.crypto.TransactionEncoder;
import cc.youchain.crypto.WalletUtils;
import cc.youchain.tx.Transfer;
import cc.youchain.utils.Convert;
import cc.youchain.utils.Numeric;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;

import static cc.youchain.codegen.Console.exitError;

/**
 * Simple class for signing transaction offline.
 */
public class WalletOfflineSignature extends WalletManager {

    private static final String USAGE = "sign <walletfile> <destination-address>";

    public static void main(String[] args) {
        if (args.length != 2) {
            exitError(USAGE);
        } else {
            new WalletOfflineSignature().run(args[0], args[1]);
        }
    }

    private void run(String walletFileLocation, String destinationAddress) {
        File walletFile = new File(walletFileLocation);
        Credentials credentials = getCredentials(walletFile);
        console.printf("Wallet for address " + credentials.getAddress() + " loaded\n");

        if (!WalletUtils.isValidAddress(destinationAddress)) {
            exitError("Invalid destination address specified");
        }

        BigDecimal amountInYou = getAmountToTransfer();
        BigDecimal lu = Convert.toLu(amountInYou, Convert.Unit.YOU);
        String luStr = lu.toPlainString();
        if (luStr.indexOf(".") != -1) {
            luStr = luStr.substring(0, luStr.indexOf("."));
        }
        BigInteger amountInLu = new BigInteger(luStr);
//        Convert.Unit transferUnit = getTransferUnit();
        console.printf("the amount is: %sYOU (%slu) %n", amountInYou, amountInLu);
        BigInteger nonce = getNonce();
        console.printf("the nonce is: %s %n", nonce);
        BigInteger gasPrice = getGasPrice();
        console.printf("the gasPrice is: %s %n", gasPrice);

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, Transfer.GAS_LIMIT, destinationAddress, amountInLu, "");
        // 签名交易数据
        byte networkId = getNetworkId();
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, networkId, credentials);
        String signedTransactionData = Numeric.toHexString(signMessage);

        console.printf("Funds have been successfully signed %n" +
                        "send %s YOU from %s to %s%n"
                        + "the rawTransaction is: %s%n" +
                        "you can use you_sendRawTransaction interface to send this transaction%n" +
                        "or you can use ./YOUChainj wallet sendRaw <rawTransaction> to send this transaction%n",
                amountInYou,
                credentials.getAddress(),
                destinationAddress,
                signedTransactionData);
    }

    private BigDecimal getAmountToTransfer() {
        String amount = console.readLine("How many YOU would you like to transfer (unit YOU) "
                + "(please enter a numeric value): ")
                .trim();
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            exitError("Invalid amount specified");
        }
        throw new RuntimeException("Application exit failure");
    }

    private BigInteger getNonce() {
        String nonce = console.readLine("What is the latest nonce of the sender address "
                + "(please enter a hex value from the you_getTransactionCount interface): ")
                .trim();
        try {
            return Numeric.toBigInt(nonce);
        } catch (NumberFormatException e) {
            exitError("Invalid amount specified");
        }
        throw new RuntimeException("Application exit failure");
    }

    private BigInteger getGasPrice() {
        String gasPrice = console.readLine("Please specify the gasPrice "
                + "(please enter a hex value from the you_gasPrice interface): ")
                .trim();
        try {
            return Numeric.toBigInt(gasPrice);
        } catch (NumberFormatException e) {
            exitError("Invalid amount specified");
        }
        throw new RuntimeException("Application exit failure");
    }

    private byte getNetworkId() {
        String unit = console.readLine("Please specify the Network (1 mainnet 2 testnet) "
                + "(please enter 1 or 2) : ")
                .trim();

        byte networdId = 1;
        if (!"".equals(unit)) {
            networdId = Byte.valueOf(unit);
        }
        return networdId;
    }

    private Convert.Unit getTransferUnit() {
        String unit = console.readLine("Please specify the unit (you, lu, ...) : ")
                .trim();

        Convert.Unit transferUnit;
        if (unit.equals("")) {
            transferUnit = Convert.Unit.YOU;
        } else {
            transferUnit = Convert.Unit.fromString(unit.toLowerCase());
        }

        return transferUnit;
    }

}
