package com.common;

import java.math.BigInteger;

import org.bitcoinj.core.Sha256Hash;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import com.base.blockchain.ethereum.EthRawTransaction;

public class SignTest {

	/**
	 * 加签验签
	 * 
	 * @throws Exception
	 */
	@Test
	public void verifySign() throws Exception {
		byte[] data = "start".getBytes();
		byte[] shadata = Sha256Hash.hash(data);

		BigInteger priKey = Numeric.toBigInt("8b3a350cf5c34c9194ca85829a2df0ec3153be0318b5e2d3348e872092edffba");
		Credentials credentials = Credentials.create(ECKeyPair.create(priKey));

		EthRawTransaction raw = new EthRawTransaction();
		Sign.SignatureData signMessage = raw.signMessage(credentials, shadata);
		String sign = raw.signMessageHash(credentials, shadata);
		System.out.println("sign:" + sign);

		System.out.println("v:" + Numeric.toBigInt(signMessage.getV()));
		System.out.println("r:" + Numeric.toBigInt(signMessage.getR()));
		System.out.println("s:" + Numeric.toBigInt(signMessage.getS()));

		System.out.println("web3j验证结果:" + raw.verify(credentials.getAddress(), shadata, sign));
		System.out.println("web3j验证结果:" + raw.verify(credentials.getAddress(), shadata, signMessage));
	}

	@Test
	public void verifyHash() {
		String hash = "0x244fc79ecb29704bdbe1af562605a3c5a842a91c19e60a033081c14a271cfd3a";
		Long v=28L;
		String r="0xfccaed29dcda2e640725299539e8e58e73a41a90ed9f1a073fb2147163e0faae";
		String s="0x1d2457d1d3298554595d4df08e433dbff5ef54194df3a3875e6a03098be0f16";
		RawTransaction rawTransaction = RawTransaction.createTransaction(
				new BigInteger("23"),
				new BigInteger("34032"), 
				new BigInteger("210000"), 
				"0xf9b3e765b51ba104d4b42fc2384cfe3ec243a637", 
				new BigInteger("100000000000000000"),
				"0x");
		EthRawTransaction raw = new EthRawTransaction();
		System.out.println("验证结果:" + hash.equals(raw.getTransactionHash(v,r,s,rawTransaction)));
	}

}
