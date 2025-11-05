package com.example.Hospital.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class BlockchainService {

    @Value("${blockchain.ganache.url:http://localhost:7545}")
    private String ganacheUrl;

    @Value("${blockchain.contract.address:0x1d326F032144C18199DD5F2391d84985cD194E08}")
    private String contractAddress;

    @Value("${blockchain.contract.abi}")
    private String contractAbi;

    @Value("${blockchain.wallet.privateKey:0x...}")
    private String privateKey;

    private Web3j web3j;
    private Credentials credentials;

    private static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(6_721_975L);

    @PostConstruct
    public void init() {
        this.web3j = Web3j.build(new HttpService(ganacheUrl));
        if (privateKey == null || privateKey.equals("0x...")) {
            System.err.println("警告：未配置区块链钱包私钥");
        } else {
            System.out.println("DEBUG: Loaded private key for credentials (first 10 chars): " + privateKey.substring(0, Math.min(privateKey.length(), 10)) + "...");
            this.credentials = Credentials.create(privateKey);
            System.out.println("Web3j 连接到: " + ganacheUrl);
            System.out.println("以太坊账户地址: " + credentials.getAddress());
        }
    }

    public TransactionReceipt setWorkHash(String pid) throws IOException, ExecutionException, InterruptedException, TransactionException {
        System.out.println("DEBUG: setWorkHash for PID: " + pid + ", Step 1: Preparing initial transaction.");
        Function function = new Function(
                "setRecordTxHash",
                Arrays.asList(new Utf8String(pid), new Utf8String("0x0")),
                Collections.emptyList());

        String encodedFunction = FunctionEncoder.encode(function);

        BigInteger nonce = web3j.ethGetTransactionCount(
                credentials.getAddress(), org.web3j.protocol.core.DefaultBlockParameterName.LATEST).send()
                .getTransactionCount();
        System.out.println("DEBUG: setWorkHash for PID: " + pid + ", Step 2: Nonce obtained: " + nonce);

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                contractAddress,
                BigInteger.ZERO,
                encodedFunction);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        System.out.println("DEBUG: setWorkHash for PID: " + pid + ", Step 3: Sending initial transaction.");
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();

        if (ethSendTransaction.hasError()) {
            System.err.println("ERROR: Initial transaction failed for PID " + pid + ": " + ethSendTransaction.getError().getMessage());
            throw new TransactionException("区块链交易发送失败: " + ethSendTransaction.getError().getMessage(),
                                          ethSendTransaction.getTransactionHash());
        }

        String transactionHash = ethSendTransaction.getTransactionHash();
        if (transactionHash == null || transactionHash.isEmpty()) {
            System.err.println("ERROR: Initial transaction hash is null/empty for PID " + pid);
            throw new TransactionException("区块链交易哈希为空，可能交易未被接受。", (String) null);
        }
        System.out.println("DEBUG: setWorkHash for PID: " + pid + ", Step 4: Initial transaction hash: " + transactionHash);

        System.out.println("DEBUG: setWorkHash for PID: " + pid + ", Step 5: Waiting for initial transaction receipt.");
        TransactionReceipt transactionReceipt =
            new PollingTransactionReceiptProcessor(
                web3j,
                TransactionManager.DEFAULT_POLLING_FREQUENCY,
                TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH)
            .waitForTransactionReceipt(transactionHash);

        if (transactionReceipt != null) {
            if (!transactionReceipt.isStatusOK()) {
                System.err.println("ERROR: Initial transaction failed on EVM for PID " + pid + ", hash: " + transactionHash + ", status: " + transactionReceipt.getStatus() + ", revert reason: " + transactionReceipt.getRevertReason());
                throw new TransactionException("初始区块链交易在EVM上执行失败。", transactionHash);
            }
            System.out.println("DEBUG: setWorkHash for PID: " + pid + ", Step 6: Initial transaction receipt obtained. Block Hash: " + transactionReceipt.getBlockHash());
            System.out.println("DEBUG: setWorkHash for PID: " + pid + ", Step 7: Preparing update transaction with initial transaction hash: " + transactionReceipt.getTransactionHash());

            Function updateFunction = new Function(
                    "setRecordTxHash",
                    Arrays.asList(new Utf8String(pid), new Utf8String(transactionReceipt.getTransactionHash())),
                    Collections.emptyList());
            String encodedUpdateFunction = FunctionEncoder.encode(updateFunction);

            BigInteger updateNonce = web3j.ethGetTransactionCount(
                    credentials.getAddress(), org.web3j.protocol.core.DefaultBlockParameterName.LATEST).send()
                    .getTransactionCount();
            System.out.println("DEBUG: setWorkHash for PID: " + pid + ", Step 8: Update Nonce obtained: " + updateNonce);

            RawTransaction updateRawTransaction = RawTransaction.createTransaction(
                    updateNonce,
                    GAS_PRICE,
                    GAS_LIMIT,
                    contractAddress,
                    BigInteger.ZERO,
                    encodedUpdateFunction);

            byte[] signedUpdateMessage = TransactionEncoder.signMessage(updateRawTransaction, credentials);
            String hexUpdateValue = Numeric.toHexString(signedUpdateMessage);

            System.out.println("DEBUG: setWorkHash for PID: " + pid + ", Step 9: Sending update transaction.");
            EthSendTransaction ethSendUpdateTransaction = web3j.ethSendRawTransaction(hexUpdateValue).sendAsync().get();

            if (ethSendUpdateTransaction.hasError()) {
                System.err.println("ERROR: Update transaction failed for PID " + pid + ": " + ethSendUpdateTransaction.getError().getMessage());
                throw new TransactionException("区块链交易更新失败: " + ethSendUpdateTransaction.getError().getMessage(),
                                              ethSendUpdateTransaction.getTransactionHash());
            }
            String updateTransactionHash = ethSendUpdateTransaction.getTransactionHash();
            if (updateTransactionHash == null || updateTransactionHash.isEmpty()) {
                System.err.println("ERROR: Update transaction hash is null/empty for PID " + pid);
                throw new TransactionException("区块链更新交易哈希为空，可能交易未被接受。", (String) null);
            }
            System.out.println("DEBUG: setWorkHash for PID: " + pid + ", Step 10: Update transaction hash: " + updateTransactionHash);
            return transactionReceipt;
        } else {
            System.err.println("ERROR: Initial transaction receipt not found for PID " + pid + ", hash: " + transactionHash);
            throw new TransactionException("无法获取交易收据，交易可能未被挖矿。", transactionHash);
        }
    }

    public String getWorkHash(String pid) throws IOException, ExecutionException, InterruptedException {
        Function function = new Function(
                "getRecordTxHash",
                Collections.singletonList(new Utf8String(pid)),
                Collections.singletonList(new TypeReference<Utf8String>() {}));

        String encodedFunction = FunctionEncoder.encode(function);

        System.out.println("DEBUG: getWorkHash for PID: " + pid + ", Contract Address: " + contractAddress);
        System.out.println("DEBUG: getWorkHash for PID: " + pid + ", Encoded Function: " + encodedFunction);

        org.web3j.protocol.core.methods.response.EthCall ethCall = web3j.ethCall(
                org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(
                        null,
                        contractAddress, encodedFunction),
                org.web3j.protocol.core.DefaultBlockParameterName.LATEST)
                .sendAsync().get();

        System.out.println("DEBUG: Raw ethCall value from blockchain (before processing): " + ethCall.getValue());

        String rawRpcValue = ethCall.getValue();

        if (rawRpcValue == null || rawRpcValue.isEmpty() || rawRpcValue.equals("0x") ||
            (rawRpcValue.matches("\\d+") && !rawRpcValue.startsWith("0x"))) {
            System.err.println("警告：尝试获取未上链记录时，Web3j返回非标准值，视为未上链。原始值：" + rawRpcValue);
            return null;
        }

        if (ethCall.hasError()) {
            System.err.println("ERROR: getWorkHash for PID " + pid + ", Blockchain call error (after value check): " + ethCall.getError().getMessage());
            throw new RuntimeException("Blockchain call error (after value check): " + ethCall.getError().getMessage());
        }

        System.out.println("DEBUG: Decoding raw RPC value: " + rawRpcValue);
        java.util.List<Type> decodedOutputs = org.web3j.abi.FunctionReturnDecoder.decode(
                rawRpcValue,
                function.getOutputParameters()
        );
        
        if (!decodedOutputs.isEmpty()) {
            System.out.println("DEBUG: Decoded output: " + decodedOutputs.get(0).getValue());
            return (String) decodedOutputs.get(0).getValue();
        } 
        System.out.println("DEBUG: Decoded outputs list is empty.");
        return null;
    }
}
