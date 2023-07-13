package com.base.blockchain.wallet.core;

import java.math.BigInteger;

/**
 * @author start 
 */
public abstract class BaseService implements WalletFunc{
	
	private BigInteger privateKey;
	
	public BaseService() {}
	
	public BaseService(BigInteger privateKey) {
		this.privateKey=privateKey;
	}
	
	public BigInteger getPrivateKey() {
		return privateKey;
	}
	
	public void setPrivateKey(BigInteger privateKey) {
		this.privateKey = privateKey;
	}
	
}
