package com.base.blockchain.wallet.core;

import java.security.SecureRandom;
import java.util.List;

import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDPath;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException.MnemonicLengthException;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;

import com.gitee.magic.core.exception.ApplicationException;

/**
 * @author start 
 */
public class WalletManager {
	
	//https://iancoleman.io/bip39/

	public static final String PASSPHRASE = "";

	/**
	 * 生成助记词
	 * 
	 * @return
	 */
	public List<String> generateMnemonic() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
		secureRandom.nextBytes(entropy);
		try {
			return MnemonicCode.INSTANCE.toMnemonic(entropy);
		} catch (MnemonicLengthException e) {
			throw new ApplicationException(e);
		}
	}
	
	public DeterministicKey getKey(List<String> mnemonicCode,ChainEnum coinType) {
		return getKey(mnemonicCode,coinType,0);
	}
	
	public DeterministicKey getKey(List<String> mnemonicCode,int coin) {
		return getKey(mnemonicCode,coin,0);
	}
	
	public DeterministicKey getKey(List<String> mnemonicCode,ChainEnum coinType,int account) {
		return getKey(mnemonicCode,coinType.getCoinType(),account);
	}
	
	public DeterministicKey getKey(List<String> mnemonicCode,int coin,int account) {
		DeterministicSeed deterministicSeed = new DeterministicSeed(mnemonicCode, null, PASSPHRASE, Utils.currentTimeSeconds());
	    DeterministicKeyChain deterministicKeyChain = DeterministicKeyChain.builder().seed(deterministicSeed).build();
	    return deterministicKeyChain.getKeyByPath(HDPath.parsePath("44H / "+coin+"H / "+account+"H / 0 / 0"), true);
	}
	
}
