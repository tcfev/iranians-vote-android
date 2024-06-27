package org.iranUnchained.contracts;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.5.2.
 */
@SuppressWarnings("rawtypes")
public class RegistrationVoting extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC___VOTINGREGISTRY_INIT = "__VotingRegistry_init";

    public static final String FUNC_ADDPROXYPOOL = "addProxyPool";

    public static final String FUNC_BINDVOTINGTOREGISTRATION = "bindVotingToRegistration";

    public static final String FUNC_GETPOOLIMPLEMENTATION = "getPoolImplementation";

    public static final String FUNC_GETVOTINGFORREGISTRATION = "getVotingForRegistration";

    public static final String FUNC_ISPOOLEXISTBYPROPOSER = "isPoolExistByProposer";

    public static final String FUNC_ISPOOLEXISTBYPROPOSERANDTYPE = "isPoolExistByProposerAndType";

    public static final String FUNC_ISPOOLEXISTBYTYPE = "isPoolExistByType";

    public static final String FUNC_LISTPOOLSBYPROPOSER = "listPoolsByProposer";

    public static final String FUNC_LISTPOOLSBYPROPOSERANDTYPE = "listPoolsByProposerAndType";

    public static final String FUNC_LISTPOOLSBYTYPE = "listPoolsByType";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_POOLCOUNTBYPROPOSER = "poolCountByProposer";

    public static final String FUNC_POOLCOUNTBYPROPOSERANDTYPE = "poolCountByProposerAndType";

    public static final String FUNC_POOLCOUNTBYTYPE = "poolCountByType";

    public static final String FUNC_POOLFACTORY = "poolFactory";

    public static final String FUNC_PROXIABLEUUID = "proxiableUUID";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SETNEWIMPLEMENTATIONS = "setNewImplementations";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_UPGRADETO = "upgradeTo";

    public static final String FUNC_UPGRADETOANDCALL = "upgradeToAndCall";


    @Deprecated
    protected RegistrationVoting(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected RegistrationVoting(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected RegistrationVoting(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected RegistrationVoting(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> __VotingRegistry_init(String poolFactory_) {
        final Function function = new Function(
                FUNC___VOTINGREGISTRY_INIT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, poolFactory_)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addProxyPool(String poolType_, String proposer_, String pool_) {
        final Function function = new Function(
                FUNC_ADDPROXYPOOL,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(poolType_),
                        new org.web3j.abi.datatypes.Address(160, proposer_),
                        new org.web3j.abi.datatypes.Address(160, pool_)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> bindVotingToRegistration(String proposer_, String voting_, String registration_) {
        final Function function = new Function(
                FUNC_BINDVOTINGTOREGISTRATION,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, proposer_),
                        new org.web3j.abi.datatypes.Address(160, voting_),
                        new org.web3j.abi.datatypes.Address(160, registration_)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getPoolImplementation(String poolType_) {
        final Function function = new Function(FUNC_GETPOOLIMPLEMENTATION,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(poolType_)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getVotingForRegistration(String proposer_, String registration_) {
        final Function function = new Function(FUNC_GETVOTINGFORREGISTRATION,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, proposer_),
                        new org.web3j.abi.datatypes.Address(160, registration_)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isPoolExistByProposer(String proposer_, String pool_) {
        final Function function = new Function(FUNC_ISPOOLEXISTBYPROPOSER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, proposer_),
                        new org.web3j.abi.datatypes.Address(160, pool_)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isPoolExistByProposerAndType(String proposer_, String poolType_, String pool_) {
        final Function function = new Function(FUNC_ISPOOLEXISTBYPROPOSERANDTYPE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, proposer_),
                        new org.web3j.abi.datatypes.Utf8String(poolType_),
                        new org.web3j.abi.datatypes.Address(160, pool_)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isPoolExistByType(String poolType_, String pool_) {
        final Function function = new Function(FUNC_ISPOOLEXISTBYTYPE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(poolType_),
                        new org.web3j.abi.datatypes.Address(160, pool_)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<List> listPoolsByProposer(String proposer_, BigInteger offset_, BigInteger limit_) {
        final Function function = new Function(FUNC_LISTPOOLSBYPROPOSER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, proposer_),
                        new org.web3j.abi.datatypes.generated.Uint256(offset_),
                        new org.web3j.abi.datatypes.generated.Uint256(limit_)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {
                }));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> listPoolsByProposerAndType(String proposer_, String poolType_, BigInteger offset_, BigInteger limit_) {
        final Function function = new Function(FUNC_LISTPOOLSBYPROPOSERANDTYPE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, proposer_),
                        new org.web3j.abi.datatypes.Utf8String(poolType_),
                        new org.web3j.abi.datatypes.generated.Uint256(offset_),
                        new org.web3j.abi.datatypes.generated.Uint256(limit_)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {
                }));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> listPoolsByType(String poolType_, BigInteger offset_, BigInteger limit_) {
        final Function function = new Function(FUNC_LISTPOOLSBYTYPE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(poolType_),
                        new org.web3j.abi.datatypes.generated.Uint256(offset_),
                        new org.web3j.abi.datatypes.generated.Uint256(limit_)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {
                }));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> poolCountByProposer(String proposer_) {
        final Function function = new Function(FUNC_POOLCOUNTBYPROPOSER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, proposer_)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> poolCountByProposerAndType(String proposer_, String poolType_) {
        final Function function = new Function(FUNC_POOLCOUNTBYPROPOSERANDTYPE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, proposer_),
                        new org.web3j.abi.datatypes.Utf8String(poolType_)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> poolCountByType(String poolType_) {
        final Function function = new Function(FUNC_POOLCOUNTBYTYPE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(poolType_)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> poolFactory() {
        final Function function = new Function(FUNC_POOLFACTORY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<byte[]> proxiableUUID() {
        final Function function = new Function(FUNC_PROXIABLEUUID,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
                FUNC_RENOUNCEOWNERSHIP,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setNewImplementations(List<String> poolTypes_, List<String> newImplementations_) {
        final Function function = new Function(
                FUNC_SETNEWIMPLEMENTATIONS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                                org.web3j.abi.datatypes.Utf8String.class,
                                org.web3j.abi.Utils.typeMap(poolTypes_, org.web3j.abi.datatypes.Utf8String.class)),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                                org.web3j.abi.datatypes.Address.class,
                                org.web3j.abi.Utils.typeMap(newImplementations_, org.web3j.abi.datatypes.Address.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> upgradeTo(String newImplementation) {
        final Function function = new Function(
                FUNC_UPGRADETO,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newImplementation)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> upgradeToAndCall(String newImplementation, byte[] data, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_UPGRADETOANDCALL,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newImplementation),
                        new org.web3j.abi.datatypes.DynamicBytes(data)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    @Deprecated
    public static RegistrationVoting load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new RegistrationVoting(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static RegistrationVoting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new RegistrationVoting(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static RegistrationVoting load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new RegistrationVoting(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static RegistrationVoting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new RegistrationVoting(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class AdminChangedEventResponse extends BaseEventResponse {
        public String previousAdmin;

        public String newAdmin;
    }

    public static class BeaconUpgradedEventResponse extends BaseEventResponse {
        public String beacon;
    }

    public static class InitializedEventResponse extends BaseEventResponse {
        public BigInteger version;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class UpgradedEventResponse extends BaseEventResponse {
        public String implementation;
    }
}
