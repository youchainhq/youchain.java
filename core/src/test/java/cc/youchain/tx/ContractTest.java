package cc.youchain.tx;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import cc.youchain.abi.EventEncoder;
import cc.youchain.abi.EventValues;
import cc.youchain.abi.FunctionEncoder;
import cc.youchain.abi.TypeReference;
import cc.youchain.abi.datatypes.*;
import cc.youchain.abi.datatypes.generated.Uint256;
import cc.youchain.crypto.Credentials;
import cc.youchain.crypto.TestKeys;
import cc.youchain.protocol.YOUChain;
import cc.youchain.protocol.core.DefaultBlockParameterName;
import cc.youchain.protocol.core.RemoteCall;
import cc.youchain.protocol.core.Request;
import cc.youchain.protocol.core.Response;
import cc.youchain.protocol.core.methods.request.Transaction;
import cc.youchain.protocol.core.methods.response.*;
import cc.youchain.protocol.exceptions.TransactionException;
import cc.youchain.tx.gas.ContractGasProvider;
import cc.youchain.tx.gas.DefaultGasProvider;
import cc.youchain.tx.gas.StaticGasProvider;
import cc.youchain.utils.Async;
import cc.youchain.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ContractTest extends ManagedTransactionTester {

    private static final String TEST_CONTRACT_BINARY = "12345";

    static final String ADDRESS = "0x3d6cb163f7c72d20b0fcd6baae5889329d138a4a";
    static final String TRANSACTION_HASH = "0xHASH";
    private TestContract contract;

    @Rule public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        super.setUp();

        contract =
                new TestContract(
                        ADDRESS,
                        youChain,
                        getVerifiedTransactionManager(TestKeys.CREDENTIALS),
                        new DefaultGasProvider());
    }

    @Test
    public void testGetContractAddress() {
        assertThat(contract.getContractAddress(), is(ADDRESS));
    }

    @Test
    public void testGetContractTransactionReceipt() {
        assertFalse(contract.getTransactionReceipt().isPresent());
    }

    @Test
    public void testDeploy() throws Exception {
        TransactionReceipt transactionReceipt = createTransactionReceipt();
        Contract deployedContract = deployContract(transactionReceipt);

        assertThat(deployedContract.getContractAddress(), is(ADDRESS));
        assertTrue(deployedContract.getTransactionReceipt().isPresent());
        assertThat(deployedContract.getTransactionReceipt().get(), equalTo(transactionReceipt));
    }

    @Test
    public void testContractDeployFails() throws Exception {
        thrown.expect(TransactionException.class);
        thrown.expectMessage(
                "Transaction has failed with status: 0x0. Gas used: 1. (not-enough gas?)");
        TransactionReceipt transactionReceipt = createFailedTransactionReceipt();
        deployContract(transactionReceipt);
    }

    @Test
    public void testContractDeployWithNullStatusSucceeds() throws Exception {
        TransactionReceipt transactionReceipt = createTransactionReceiptWithStatus(null);
        Contract deployedContract = deployContract(transactionReceipt);

        assertThat(deployedContract.getContractAddress(), is(ADDRESS));
        assertTrue(deployedContract.getTransactionReceipt().isPresent());
        assertThat(deployedContract.getTransactionReceipt().get(), equalTo(transactionReceipt));
    }

    @Test
    public void testIsValid() throws Exception {
        prepareYOUGetCode(TEST_CONTRACT_BINARY);

        Contract contract = deployContract(createTransactionReceipt());
        assertTrue(contract.isValid());
    }

    @Test
    public void testIsValidSkipMetadata() throws Exception {
        prepareYOUGetCode(
                TEST_CONTRACT_BINARY
                        + "a165627a7a72305820"
                        + "a9bc86938894dc250f6ea25dd823d4472fad6087edcda429a3504e3713a9fc880029");

        Contract contract = deployContract(createTransactionReceipt());
//        assertTrue(contract.isValid());
    }

    @Test
    public void testIsValidDifferentCode() throws Exception {
        prepareYOUGetCode(TEST_CONTRACT_BINARY + "0");

        Contract contract = deployContract(createTransactionReceipt());
        assertFalse(contract.isValid());
    }

    @Test
    public void testIsValidEmptyCode() throws Exception {
        prepareYOUGetCode("");

        Contract contract = deployContract(createTransactionReceipt());
        assertFalse(contract.isValid());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsValidNoBinThrows() throws Exception {
        TransactionManager txManager = mock(TransactionManager.class);
        TestContract contract =
                new TestContract(
                        Contract.BIN_NOT_PROVIDED,
                        ADDRESS,
                        youChain,
                        txManager,
                        new DefaultGasProvider());
        contract.isValid();
    }

    @Test(expected = RuntimeException.class)
    public void testDeployInvalidContractAddress() throws Throwable {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);

        prepareTransaction(transactionReceipt);

        String encodedConstructor =
                FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(BigInteger.TEN)));

        try {
            TestContract.deployRemoteCall(
                            TestContract.class,
                    youChain,
                            TestKeys.CREDENTIALS,
                            ManagedTransaction.GAS_PRICE,
                            Contract.GAS_LIMIT,
                            "0xcafed00d",
                            encodedConstructor,
                            BigInteger.ZERO)
                    .send();
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testCallSingleValue() throws Exception {
        // Example taken from FunctionReturnDecoderTest

        YOUCall youCall = new YOUCall();
        youCall.setResult(
                "0x0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000000");
        prepareCall(youCall);

        assertThat(contract.callSingleValue().send(), equalTo(new Utf8String("")));
    }

    @Test
    public void testCallSingleValueEmpty() throws Exception {
        // Example taken from FunctionReturnDecoderTest

        YOUCall youCall = new YOUCall();
        youCall.setResult("0x");
        prepareCall(youCall);

        assertNull(contract.callSingleValue().send());
    }

    @Test
    public void testCallMultipleValue() throws Exception {
        YOUCall youCall = new YOUCall();
        youCall.setResult(
                "0x0000000000000000000000000000000000000000000000000000000000000037"
                        + "0000000000000000000000000000000000000000000000000000000000000007");
        prepareCall(youCall);

        assertThat(
                contract.callMultipleValue().send(),
                equalTo(
                        Arrays.asList(
                                new Uint256(BigInteger.valueOf(55)),
                                new Uint256(BigInteger.valueOf(7)))));
    }

    @Test
    public void testCallMultipleValueEmpty() throws Exception {
        YOUCall youCall = new YOUCall();
        youCall.setResult("0x");
        prepareCall(youCall);

        assertThat(contract.callMultipleValue().send(), equalTo(emptyList()));
    }

    @SuppressWarnings("unchecked")
    private void prepareCall(YOUCall youCall) throws IOException {
        Request<?, YOUCall> request = mock(Request.class);
        when(request.send()).thenReturn(youCall);

        when(youChain.youCall(any(Transaction.class), eq(DefaultBlockParameterName.LATEST)))
                .thenReturn((Request) request);
    }

    @Test
    public void testTransaction() throws Exception {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);
        transactionReceipt.setStatus("0x1");

        prepareTransaction(transactionReceipt);

        assertThat(
                contract.performTransaction(
                                new Address(BigInteger.TEN), new Uint256(BigInteger.ONE))
                        .send(),
                is(transactionReceipt));
    }

    @Test
    public void testTransactionFailed() throws Exception {
        thrown.expect(TransactionException.class);
        thrown.expectMessage(
                "Transaction has failed with status: 0x0. Gas used: 1. (not-enough gas?)");

        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);
        transactionReceipt.setStatus("0x0");
        transactionReceipt.setGasUsed("0x1");

        prepareTransaction(transactionReceipt);
        contract.performTransaction(new Address(BigInteger.TEN), new Uint256(BigInteger.ONE))
                .send();
    }

    @Test
    public void testProcessEvent() {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        Log log = new Log();
        log.setTopics(
                Arrays.asList(
                        // encoded function
                        "0xfceb437c298f40d64702ac26411b2316e79f3c28ffa60edfc891ad4fc8ab82ca",
                        // indexed value
                        "0000000000000000000000003d6cb163f7c72d20b0fcd6baae5889329d138a4a"));
        // non-indexed value
        log.setData("0000000000000000000000000000000000000000000000000000000000000001");

        transactionReceipt.setLogs(Arrays.asList(log));

        EventValues eventValues = contract.processEvent(transactionReceipt).get(0);

        assertThat(
                eventValues.getIndexedValues(),
                equalTo(singletonList(new Address("0x3d6cb163f7c72d20b0fcd6baae5889329d138a4a"))));
        assertThat(
                eventValues.getNonIndexedValues(),
                equalTo(singletonList(new Uint256(BigInteger.ONE))));
    }

    @Test
    public void testProcessEventForLogWithoutTopics() {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        final Log log = new Log();
        log.setTopics(Collections.emptyList());
        // non-indexed value
        log.setData("0000000000000000000000000000000000000000000000000000000000000001");
        transactionReceipt.setLogs(Arrays.asList(log));

        final List<EventValues> eventValues = contract.processEvent(transactionReceipt);
        assertTrue("No events expected", eventValues.isEmpty());
    }

    @Test(expected = TransactionException.class)
    public void testTimeout() throws Throwable {
        prepareTransaction(null);

        TransactionManager transactionManager =
                getVerifiedTransactionManager(TestKeys.CREDENTIALS, 1, 1);

        contract = new TestContract(ADDRESS, youChain, transactionManager, new DefaultGasProvider());

        testErrorScenario();
    }

    @Test(expected = RuntimeException.class)
    @SuppressWarnings("unchecked")
    public void testInvalidTransactionResponse() throws Throwable {
        prepareNonceRequest();

        YOUSendTransaction youSendTransaction = new YOUSendTransaction();
        youSendTransaction.setError(new Response.Error(1, "Invalid transaction"));

        Request<?, YOUSendTransaction> rawTransactionRequest = mock(Request.class);
        when(rawTransactionRequest.sendAsync()).thenReturn(Async.run(() -> youSendTransaction));
        when(youChain.youSendRawTransaction(any(String.class)))
                .thenReturn((Request) rawTransactionRequest);

        testErrorScenario();
    }

    @Test
    public void testSetGetAddresses() throws Exception {
        assertNull(contract.getDeployedAddress("1"));
        contract.setDeployedAddress("1", "0x000000000000add0e00000000000");
        assertNotNull(contract.getDeployedAddress("1"));
        contract.setDeployedAddress("2", "0x000000000000add0e00000000000");
        assertNotNull(contract.getDeployedAddress("2"));
    }

    @Test
    public void testSetGetGasPrice() {
        assertEquals(ManagedTransaction.GAS_PRICE, contract.getGasPrice());
        BigInteger newPrice = ManagedTransaction.GAS_PRICE.multiply(BigInteger.valueOf(2));
        contract.setGasPrice(newPrice);
        assertEquals(newPrice, contract.getGasPrice());
    }

    @Test
    public void testStaticGasProvider() throws IOException, TransactionException {
        StaticGasProvider gasProvider = new StaticGasProvider(BigInteger.TEN, BigInteger.ONE);
        TransactionManager txManager = mock(TransactionManager.class);

        when(txManager.executeTransaction(
                        any(BigInteger.class),
                        any(BigInteger.class),
                        anyString(),
                        anyString(),
                        any(BigInteger.class)))
                .thenReturn(new TransactionReceipt());

        contract = new TestContract(ADDRESS, youChain, txManager, gasProvider);

        Function func =
                new Function(
                        "test", Arrays.<Type>asList(), Collections.<TypeReference<?>>emptyList());
        contract.executeTransaction(func);

        verify(txManager)
                .executeTransaction(
                        eq(BigInteger.TEN),
                        eq(BigInteger.ONE),
                        anyString(),
                        anyString(),
                        any(BigInteger.class));
    }

    @Test(expected = RuntimeException.class)
    @SuppressWarnings("unchecked")
    public void testInvalidTransactionReceipt() throws Throwable {
        prepareNonceRequest();
        prepareTransactionRequest();

        YOUGetTransactionReceipt youGetTransactionReceipt = new YOUGetTransactionReceipt();
        youGetTransactionReceipt.setError(new Response.Error(1, "Invalid transaction receipt"));

        Request<?, YOUGetTransactionReceipt> getTransactionReceiptRequest = mock(Request.class);
        when(getTransactionReceiptRequest.sendAsync())
                .thenReturn(Async.run(() -> youGetTransactionReceipt));
        when(youChain.youGetTransactionReceipt(TRANSACTION_HASH))
                .thenReturn((Request) getTransactionReceiptRequest);

        testErrorScenario();
    }

    @Test
    public void testExtractEventParametersWithLogGivenATransactionReceipt() {

        final java.util.function.Function<String, Event> eventFactory =
                name -> new Event(name, emptyList());

        final BiFunction<Integer, Event, Log> logFactory =
                (logIndex, event) ->
                        new Log(
                                false,
                                "" + logIndex,
                                "0",
                                "0x0",
                                "0x0",
                                "0",
                                "0x" + logIndex,
                                "",
                                "",
                                singletonList(EventEncoder.encode(event)));

        final Event testEvent1 = eventFactory.apply("TestEvent1");
        final Event testEvent2 = eventFactory.apply("TestEvent2");

        final List<Log> logs =
                Arrays.asList(logFactory.apply(0, testEvent1), logFactory.apply(1, testEvent2));

        final TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setLogs(logs);

        final List<Contract.EventValuesWithLog> eventValuesWithLogs1 =
                contract.extractEventParametersWithLog(testEvent1, transactionReceipt);

        assertEquals(eventValuesWithLogs1.size(), 1);
        assertEquals(eventValuesWithLogs1.get(0).getLog(), logs.get(0));

        final List<Contract.EventValuesWithLog> eventValuesWithLogs2 =
                contract.extractEventParametersWithLog(testEvent2, transactionReceipt);

        assertEquals(eventValuesWithLogs2.size(), 1);
        assertEquals(eventValuesWithLogs2.get(0).getLog(), logs.get(1));
    }

    void testErrorScenario() throws Throwable {
        try {
            contract.performTransaction(new Address(BigInteger.TEN), new Uint256(BigInteger.ONE))
                    .send();
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    private TransactionReceipt createTransactionReceipt() {
        return createTransactionReceiptWithStatus("0x1");
    }

    private TransactionReceipt createFailedTransactionReceipt() {
        return createTransactionReceiptWithStatus("0x0");
    }

    private TransactionReceipt createTransactionReceiptWithStatus(String status) {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);
        transactionReceipt.setContractAddress(ADDRESS);
        transactionReceipt.setStatus(status);
        transactionReceipt.setGasUsed("0x1");
        return transactionReceipt;
    }

    private Contract deployContract(TransactionReceipt transactionReceipt) throws Exception {

        prepareTransaction(transactionReceipt);

        String encodedConstructor =
                FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(BigInteger.TEN)));

        return TestContract.deployRemoteCall(
                        TestContract.class,
                youChain,
                        getVerifiedTransactionManager(TestKeys.CREDENTIALS),
                        ManagedTransaction.GAS_PRICE,
                        Contract.GAS_LIMIT,
                        "0xcafed00d",
                        encodedConstructor,
                        BigInteger.ZERO)
                .send();
    }

    @SuppressWarnings("unchecked")
    private void prepareYOUGetCode(String binary) throws IOException {
        YOUGetCode youGetCode = new YOUGetCode();
        youGetCode.setResult(Numeric.prependHexPrefix(binary));

        Request<?, YOUGetCode> youGetCodeRequest = mock(Request.class);
        when(youGetCodeRequest.send()).thenReturn(youGetCode);
        when(youChain.youGetCode(ADDRESS, DefaultBlockParameterName.LATEST))
                .thenReturn((Request) youGetCodeRequest);
    }

    private static class TestContract extends Contract {
        public TestContract(
                String contractAddress,
                YOUChain youChain,
                Credentials credentials,
                BigInteger gasPrice,
                BigInteger gasLimit) {
            super(TEST_CONTRACT_BINARY, contractAddress, youChain, credentials, gasPrice, gasLimit);
        }

        public TestContract(
                String contractAddress,
                YOUChain youChain,
                TransactionManager transactionManager,
                ContractGasProvider gasProvider) {
            this(TEST_CONTRACT_BINARY, contractAddress, youChain, transactionManager, gasProvider);
        }

        public TestContract(
                String binary,
                String contractAddress,
                YOUChain youChain,
                TransactionManager transactionManager,
                ContractGasProvider gasProvider) {
            super(binary, contractAddress, youChain, transactionManager, gasProvider);
        }

        public RemoteCall<Utf8String> callSingleValue() {
            Function function =
                    new Function(
                            "call",
                            Arrays.<Type>asList(),
                            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
            return executeRemoteCallSingleValueReturn(function);
        }

        public RemoteCall<List<Type>> callMultipleValue()
                throws ExecutionException, InterruptedException {
            Function function =
                    new Function(
                            "call",
                            Arrays.<Type>asList(),
                            Arrays.<TypeReference<?>>asList(
                                    new TypeReference<Uint256>() {},
                                    new TypeReference<Uint256>() {}));
            return executeRemoteCallMultipleValueReturn(function);
        }

        public RemoteCall<TransactionReceipt> performTransaction(Address address, Uint256 amount) {
            Function function =
                    new Function(
                            "approve",
                            Arrays.<Type>asList(address, amount),
                            Collections.<TypeReference<?>>emptyList());
            return executeRemoteCallTransaction(function);
        }

        public List<EventValues> processEvent(TransactionReceipt transactionReceipt) {
            Event event =
                    new Event(
                            "Event",
                            Arrays.<TypeReference<?>>asList(
                                    new TypeReference<Address>(true) {},
                                    new TypeReference<Uint256>() {}));
            return extractEventParameters(event, transactionReceipt);
        }
    }

    void prepareTransaction(TransactionReceipt transactionReceipt) throws IOException {
        prepareNonceRequest();
        prepareTransactionRequest();
        prepareTransactionReceipt(transactionReceipt);
    }

    @SuppressWarnings("unchecked")
    void prepareNonceRequest() throws IOException {
        YOUGetTransactionCount youGetTransactionCount = new YOUGetTransactionCount();
        youGetTransactionCount.setResult("0x1");

        Request<?, YOUGetTransactionCount> transactionCountRequest = mock(Request.class);
        when(transactionCountRequest.send()).thenReturn(youGetTransactionCount);
        when(youChain.youGetTransactionCount(TestKeys.ADDRESS, DefaultBlockParameterName.PENDING))
                .thenReturn((Request) transactionCountRequest);
    }

    @SuppressWarnings("unchecked")
    void prepareTransactionRequest() throws IOException {
        YOUSendTransaction youSendTransaction = new YOUSendTransaction();
        youSendTransaction.setResult(TRANSACTION_HASH);

        Request<?, YOUSendTransaction> rawTransactionRequest = mock(Request.class);
        when(rawTransactionRequest.send()).thenReturn(youSendTransaction);
        when(youChain.youSendRawTransaction(any(String.class)))
                .thenReturn((Request) rawTransactionRequest);
    }

    @SuppressWarnings("unchecked")
    void prepareTransactionReceipt(TransactionReceipt transactionReceipt) throws IOException {
        YOUGetTransactionReceipt youGetTransactionReceipt = new YOUGetTransactionReceipt();
        youGetTransactionReceipt.setResult(transactionReceipt);

        Request<?, YOUGetTransactionReceipt> getTransactionReceiptRequest = mock(Request.class);
        when(getTransactionReceiptRequest.send()).thenReturn(youGetTransactionReceipt);
        when(youChain.youGetTransactionReceipt(TRANSACTION_HASH))
                .thenReturn((Request) getTransactionReceiptRequest);
    }

}
