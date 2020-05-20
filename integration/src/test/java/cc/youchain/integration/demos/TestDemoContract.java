package cc.youchain.integration.demos;

import cc.youchain.crypto.Credentials;
import cc.youchain.protocol.YOUChain;
import cc.youchain.tx.Contract;
import cc.youchain.tx.TransactionManager;
import cc.youchain.tx.gas.ContractGasProvider;

public class TestDemoContract extends Contract {
    protected TestDemoContract(String contractBinary, String contractAddress,
                               YOUChain youChain, TransactionManager transactionManager,
                               ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, youChain, transactionManager, gasProvider);
    }

    protected TestDemoContract(String contractBinary, String contractAddress, YOUChain youChain,
                               Credentials credentials, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, youChain, credentials, gasProvider);
    }
}