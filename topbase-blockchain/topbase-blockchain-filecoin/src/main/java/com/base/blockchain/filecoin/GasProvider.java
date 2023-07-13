package com.base.blockchain.filecoin;

import java.math.BigInteger;

/**
 * 
 * @author start
 *
 */
public interface GasProvider {

	/**
	 * ignore
	 * @return
	 */
	BigInteger getGasLimit();

	/**
	 * ignore
	 * @return
	 */
	BigInteger getGasFeeCap();

	/**
	 * ignore
	 * @return
	 */
    BigInteger getGasPremium();
    
}
