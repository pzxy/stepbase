package com.base.blockchain.ethereum;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;

import com.base.blockchain.ethereum.utils.Web3jUtils;

/**
 * 
 * @author start
 *
 */
public class Erc20{

	private Web3j web3j;
	private BigInteger privateKey;
	private String contractAddress;
	
	public Web3j getWeb3j() {
		return web3j;
	}

	public void setWeb3j(Web3j web3j) {
		this.web3j = web3j;
	}

	public BigInteger getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(BigInteger privateKey) {
		this.privateKey = privateKey;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	public Erc20(Web3j web3j,String contractAddress) {
		this(web3j,null,contractAddress);
	}
	
	public Erc20(Web3j web3j,BigInteger privateKey,String contractAddress) {
		this.web3j=web3j;
		this.privateKey=privateKey;
		this.contractAddress=contractAddress;
	}
	
	@SuppressWarnings("rawtypes")
	public BigInteger getBalance(String address) {
        Function function = new Function("balanceOf",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(address)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        String data=FunctionEncoder.encode(function);
        
        String value=Web3jUtils.call(getWeb3j(), address, getContractAddress(), data);
        return new BigInteger(value.substring(2), 16);
    }
	
	public String transfer(String to,BigInteger money) {
		ContractGasProvider gasProvider=Web3jUtils.getGasProvider(web3j, new BigInteger("60000"));
		return transfer(to, money, gasProvider, false);
	}
	
	public String transfer1159(String to,BigInteger money) {
		ContractGasProvider gasProvider=Web3jUtils.get1159GasProvider(web3j, new BigInteger("60000"));
		return transfer(to, money, gasProvider, false);
	}

    @SuppressWarnings("rawtypes")
	public String transfer(
			String to,
			BigInteger money,
			ContractGasProvider gasProvider,Boolean isSign) {
    	String contractFunc="transfer";
    	Function function = new Function(
    			contractFunc,
                Arrays.<Type>asList(
                		new org.web3j.abi.datatypes.Address(to),
                        new org.web3j.abi.datatypes.generated.Uint256(money)),
                Collections.<TypeReference<?>>emptyList());
        String data = FunctionEncoder.encode(function);
        
        Long chainId=Web3jUtils.getChainId(getWeb3j());
        Credentials credentials = Credentials.create(ECKeyPair.create(privateKey));
        EthRawTransaction raw=new EthRawTransaction();
        return raw.signOrSend(
        		getWeb3j(), 
        		chainId, 
        		credentials,
        		getContractAddress(), 
        		BigInteger.ZERO,
        		data, 
        		gasProvider,
        		contractFunc, isSign);
    }
    
}
