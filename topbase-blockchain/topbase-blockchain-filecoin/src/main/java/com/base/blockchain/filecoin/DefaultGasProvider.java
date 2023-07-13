package com.base.blockchain.filecoin;

import java.math.BigInteger;

/**
 * 
 * @author start
 *
 */
public class DefaultGasProvider implements GasProvider{

	@Override
	public BigInteger getGasLimit() {
		return new BigInteger("608335");
	}

	@Override
	public BigInteger getGasFeeCap() {
		return new BigInteger("1830029194");
	}

	@Override
	public BigInteger getGasPremium() {
		return new BigInteger("100837");
	}
    
}
