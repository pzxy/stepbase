package com.base.blockchain.ethereum.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractEIP1559GasProvider;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import com.gitee.magic.core.exception.ApplicationException;

import okhttp3.OkHttpClient;

/**
 * @author start
 */
public class Web3jUtils {

	public static Web3j getWeb3j(String url) {
		return getWeb3j(url, 300L);
	}

	public static Web3j getWeb3j(String url, Long tos) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(tos, TimeUnit.SECONDS);
		builder.readTimeout(tos, TimeUnit.SECONDS);
		builder.writeTimeout(tos, TimeUnit.SECONDS);
		return Web3j.build(new HttpService(url, builder.build()));
	}
	
	public static Credentials getCredentials(BigInteger privateKey) {
		return Credentials.create(ECKeyPair.create(privateKey));
	}

	public static Credentials getCredentials(String privateKey) {
		return Credentials.create(privateKey);
	}
	
	public static BigInteger getBalance(Web3j web3j, String address) {
		try {
			return web3j.ethGetBalance(address, DefaultBlockParameterName.PENDING).send().getBalance();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static BigInteger getNonce(Web3j web3j, String address) {
		EthGetTransactionCount ethGetTransactionCount;
		try {
			ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		return ethGetTransactionCount.getTransactionCount();
	}

	public static Long getChainId(Web3j web3j) {
		try {
			return web3j.ethChainId().send().getChainId().longValue();
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}

	public String getCode(Web3j web3j, String address) {
		EthGetCode getCode = null;
		try {
			getCode = web3j.ethGetCode(address, DefaultBlockParameterName.LATEST).send();
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		return getCode.getCode();
	}

	public static String call(Web3j web3j, String address, String contractAddress, String data) {
		try {
			return web3j.ethCall(
					org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(address,
					contractAddress, data), DefaultBlockParameterName.PENDING).send().getValue();
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}

	public Transaction getTransactionByHash(Web3j web3j, String hash) {
		EthTransaction transaction = null;
		try {
			transaction = web3j.ethGetTransactionByHash(hash).send();
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		return transaction.getResult();
	}

	public static TransactionReceipt getTransactionReceipt(Web3j web3j, String hash) {
		EthGetTransactionReceipt transactionReceipt;
		try {
			transactionReceipt = web3j.ethGetTransactionReceipt(hash).send();
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		return transactionReceipt.getResult();
	}

	public static BigInteger getGasPrice(Web3j web3j) {
		try {
			return web3j.ethGasPrice().send().getGasPrice();
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}
	
	public static ContractGasProvider getGasProvider(Web3j web3j,BigInteger gasLimit) {
		BigInteger gasPrice=Web3jUtils.getGasPrice(web3j);
		return new StaticGasProvider(gasPrice,gasLimit);
	}
	
	public static ContractEIP1559GasProvider get1159GasProvider(Web3j web3j,BigInteger gasLimit) {
		Long chainId=getChainId(web3j);
		BigInteger gasPrice=Web3jUtils.getGasPrice(web3j);
		return new ContractEIP1559GasProvider() {

			@Override
			public BigInteger getGasPrice(String contractFunc) {
				return gasPrice;
			}

			@Override
			public BigInteger getGasPrice() {
				return gasPrice;
			}

			@Override
			public BigInteger getGasLimit() {
				return gasLimit;
			}

			@Override
			public BigInteger getGasLimit(String contractFunc) {
				return gasLimit;
			}

			@Override
			public boolean isEIP1559Enabled() {
				return true;
			}

			@Override
			public long getChainId() {
				return chainId;
			}

			@Override
			public BigInteger getMaxFeePerGas(String contractFunc) {
				return gasPrice;
			}

			@Override
			public BigInteger getMaxPriorityFeePerGas(String contractFunc) {
				return gasPrice;
			}

		};
	}

}