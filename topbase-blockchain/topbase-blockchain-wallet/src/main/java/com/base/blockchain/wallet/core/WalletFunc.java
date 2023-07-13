package com.base.blockchain.wallet.core;

import java.math.BigInteger;

/**
 * @author start 
 */
public interface WalletFunc {
	
	/**
	 * 币名称
	 * @return
	 */
	String getCoinName();
	
	/**
	 * 地址
	 * @return
	 */
	String getAddress();

	/**
	 * 余额
	 * @param address
	 * @return
	 */
	BigInteger getBalance(String address);
	
}
