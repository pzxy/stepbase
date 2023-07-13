package com.base.blockchain.bitcoin;

import java.math.BigInteger;

import com.base.blockchain.wallet.core.ChainEnum;

/**
 * @author start 
 */
public class BitcoinCashService extends BitcoinService {

	public BitcoinCashService(BigInteger privateKey) {
		super(privateKey);
	}
	
	@Override
	public String getCoinName() {
		return ChainEnum.BCH.name();
	}

	@Override
	public BigInteger getBalance(String address) {
		return null;
	}

}
