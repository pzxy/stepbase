package com.base.blockchain.ethereum;

import java.io.IOException;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.RawTransactionManager;

import com.gitee.magic.core.exception.ApplicationException;

/**
 * 合约调用
 * @author start
 *
 */
public class ContractCallService extends ManagedTransaction {
    
	private String contractAddress;
	
    public ContractCallService(Web3j web3j,Credentials credentials,String contractAddress) {
        super(web3j, new RawTransactionManager(web3j, credentials));
		this.contractAddress = ensResolver.resolve(contractAddress);
    }

	public String call(String funData) {
		try {
			return call(contractAddress, funData, DefaultBlockParameterName.LATEST);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
    }
    
}
