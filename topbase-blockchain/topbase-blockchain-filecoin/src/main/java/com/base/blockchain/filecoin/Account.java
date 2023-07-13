package com.base.blockchain.filecoin;

import java.math.BigInteger;

import org.bitcoinj.core.ECKey;

import com.base.blockchain.wallet.utils.NumericUtil;
import com.gitee.magic.core.utils.codec.Base32;

import ove.crypto.digest.Blake2b;

/**
 * @author start
 */
public class Account {

	private ECKey ecKey;
	private BigInteger privateKey;

	public Account(BigInteger privateKey) {
		this.privateKey = privateKey;
		this.ecKey = ECKey.fromPrivate(privateKey, false);
	}

	public ECKey getEcKey() {
		return ecKey;
	}

	public BigInteger getPrivateKey() {
		return privateKey;
	}

	public String getAddress() {
		String publicKey = ecKey.getPublicKeyAsHex();
		Blake2b.Digest blake2b1 = Blake2b.Digest.newInstance(20);
		byte[] bytes = NumericUtil.hexToBytes(publicKey);
		byte[] black2HashByte = blake2b1.digest(bytes);
		String black2HashStr = NumericUtil.bytesToHex(black2HashByte);
		String black2HashSecond = "0x01" + black2HashStr;
		Blake2b.Digest blake2b2 = Blake2b.Digest.newInstance(4);

		byte[] checksumBytes = blake2b2.digest(NumericUtil.hexToBytes(black2HashSecond));
		byte[] addressBytes = new byte[black2HashByte.length + checksumBytes.length];
		System.arraycopy(black2HashByte, 0, addressBytes, 0, black2HashByte.length);
		System.arraycopy(checksumBytes, 0, addressBytes, black2HashByte.length, checksumBytes.length);
		// f 正式 t 测试 1 钱包 2 合约
		return "f1" + Base32.encode(addressBytes);
	}

}
