package com.base.blockchain.wallet.utils;

import java.math.BigInteger;
import java.util.List;

import org.bitcoinj.core.ECKey;

import com.base.blockchain.wallet.core.ChainEnum;
import com.base.blockchain.wallet.core.WalletManager;

/**
 * 测试基类
 * @author start
 *
 */
public class BaseTest {
	
	public List<String> getMnemonicWords() {
		WalletManager wallet = new WalletManager();
		return wallet.generateMnemonic();
	}
	
	public BigInteger getPrivateKey(ChainEnum chain) {
		WalletManager wallet = new WalletManager();
		List<String> words=wallet.generateMnemonic();
		ECKey ecKey = wallet.getKey(words, chain);
		return ecKey.getPrivKey();
	}

}
