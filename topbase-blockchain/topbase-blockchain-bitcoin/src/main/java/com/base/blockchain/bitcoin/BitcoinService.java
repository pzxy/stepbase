package com.base.blockchain.bitcoin;

import java.math.BigInteger;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;

import com.base.blockchain.bitcoin.serivce.BitpayInsight;
import com.base.blockchain.wallet.core.BaseService;
import com.base.blockchain.wallet.core.ChainEnum;

/**
 * @author start 
 */
public class BitcoinService extends BaseService {

	private BitcoinApi bitcoinApi;
	private NetworkParameters param;

	public BitcoinService(BigInteger privateKey) {
		super(privateKey);
		this.param=MainNetParams.get();
		this.bitcoinApi=new BitpayInsight();
	}
	
	@Override
	public String getCoinName() {
		return ChainEnum.BTC.name();
	}

	@Override
	public String getAddress() {
		ECKey ecKey = ECKey.fromPrivate(getPrivateKey());
		//3开头
		LegacyAddress address = LegacyAddress.fromScriptHash(this.param, ecKey.getPubKeyHash());
		return address.toBase58();
	}

	@Override
	public BigInteger getBalance(String address) {
		return BigInteger.valueOf(bitcoinApi.getBalance(address));
	}

	public NetworkParameters getParam() {
		return param;
	}
	
}
