package com.base.blockchain.filecoin;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

import org.web3j.crypto.ECKeyPair;

import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.utils.codec.Base32;
import com.gitee.magic.core.utils.codec.Base64;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.UnsignedInteger;
import io.ipfs.cid.Cid;
import io.ipfs.multibase.Multibase;
import io.ipfs.multihash.Multihash;
import ove.crypto.digest.Blake2b;

/**
 * @author start
 */
public class RawMessage {

	/**
	 * 签名数据
	 * @param transaction 交易结构体
	 * @param priKey 私钥
	 * @return
	 */
	public String sign(Transaction transaction, BigInteger priKey) {
		byte[] encodedBytes = cborEncoder(transaction);
		byte[] cidHashBytes = getCidHash(encodedBytes);
		byte[] sig = sign(cidHashBytes, priKey);
		return Base64.encode(sig);
	}
	
	/**
	 * 交易cid
	 * @param transaction 交易结构体
	 * @param priKey 私钥
	 * @return
	 */
	public String cid(Transaction transaction, BigInteger priKey) {
		byte[] encodedBytes = cborEncoder(transaction);
		byte[] cidHashBytes = getCidHash(encodedBytes);
		byte[] sig = sign(cidHashBytes, priKey);
		byte[] signBytes = new byte[66];
		// 第一个是签名的type
		signBytes[0] = 1;
		// 签名
		System.arraycopy(sig, 0, signBytes, 1, sig.length);
		
		ByteArrayOutputStream signBaos = new ByteArrayOutputStream();
		try {
			new CborEncoder(signBaos).encode(new CborBuilder().add(signBytes).build());
		} catch (CborException e) {
			throw new ApplicationException(e);
		}
		byte[] signCborBytes = signBaos.toByteArray();
		byte[] cidBytes = new byte[1 + encodedBytes.length + signCborBytes.length];
		// 拼接字节流
		cidBytes[0] = (byte) 130;
		System.arraycopy(encodedBytes, 0, cidBytes, 1, encodedBytes.length);
		System.arraycopy(signCborBytes, 0, cidBytes, 1 + encodedBytes.length, signCborBytes.length);
		Blake2b.Param param = new Blake2b.Param();
		param.setDigestLength(32);
		byte[] hash = Blake2b.Digest.newInstance(param).digest(cidBytes);
		Cid cid = new Cid(1, Cid.Codec.DagCbor, Multihash.Type.blake2b_256, hash);
		return Multibase.encode(Multibase.Base.Base32, cid.toBytes());
	}

	/**
	 * 形成摘要需要拼接的字符串
	 */
	public static final byte[] CID_PREFIX = new byte[] { 0x01, 0x71, (byte) 0xa0, (byte) 0xe4, 0x02, 0x20 };

	/**
	 * @param message 交易结构体的序列化字节 通过交易结构体字节获取CidHash
	 */
	public byte[] getCidHash(byte[] message) {
		Blake2b.Param param = new Blake2b.Param();
		param.setDigestLength(32);

		// 消息体字节
		byte[] messageByte = Blake2b.Digest.newInstance(param).digest(message);

		int xlen = CID_PREFIX.length;
		int ylen = messageByte.length;

		byte[] result = new byte[xlen + ylen];

		System.arraycopy(CID_PREFIX, 0, result, 0, xlen);
		System.arraycopy(messageByte, 0, result, xlen, ylen);

		return Blake2b.Digest.newInstance(param).digest(result);

	}

	/**
	 * 对摘要进行椭圆签名椭圆签名
	 * 
	 * @param cidHash
	 * @param priKey
	 * @return
	 */
	public byte[] sign(byte[] cidHash, BigInteger priKey) {
		ECKeyPair ecKeyPair = ECKeyPair.create(priKey);
		org.web3j.crypto.Sign.SignatureData signatureData = org.web3j.crypto.Sign.signMessage(cidHash, ecKeyPair,
				false);
		byte[] sig = new byte[65];
		System.arraycopy(signatureData.getR(), 0, sig, 0, 32);
		System.arraycopy(signatureData.getS(), 0, sig, 32, 32);
		byte v = signatureData.getV()[0];
		sig[64] = (byte) ((v & 0xFF) - 27);
		return sig;
	}

	public byte[] cborEncoder(Transaction transaction) {
		co.nstant.in.cbor.model.ByteString value = null;
		if (transaction.getValue().toByteArray()[0] != 0) {
			byte[] byte1 = new byte[transaction.getValue().toByteArray().length + 1];
			byte1[0] = 0;
			System.arraycopy(transaction.getValue().toByteArray(), 0, byte1, 1,
					transaction.getValue().toByteArray().length);
			value = new co.nstant.in.cbor.model.ByteString(byte1);
		} else {
			value = new co.nstant.in.cbor.model.ByteString(
					transaction.getValue().toByteArray());
		}
		co.nstant.in.cbor.model.ByteString gasFeeCap = null;
		if (transaction.getGasFeeCap().toByteArray()[0] != 0) {
			byte[] byte2 = new byte[transaction.getGasFeeCap().toByteArray().length + 1];
			byte2[0] = 0;
			System.arraycopy(transaction.getGasFeeCap().toByteArray(), 0, byte2, 1,
					transaction.getGasFeeCap().toByteArray().length);
			gasFeeCap = new co.nstant.in.cbor.model.ByteString(byte2);
		} else {
			gasFeeCap = new co.nstant.in.cbor.model.ByteString(
					transaction.getGasFeeCap().toByteArray());
		}
		co.nstant.in.cbor.model.ByteString gasGasPremium = null;
		if (transaction.getGasPremium().toByteArray()[0] != 0) {
			byte[] byte2 = new byte[transaction.getGasPremium().toByteArray().length + 1];
			byte2[0] = 0;
			System.arraycopy(transaction.getGasPremium().toByteArray(), 0, byte2, 1,
					transaction.getGasPremium().toByteArray().length);
			gasGasPremium = new co.nstant.in.cbor.model.ByteString(byte2);
		} else {
			gasGasPremium = new co.nstant.in.cbor.model.ByteString(
					transaction.getGasPremium().toByteArray());
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			new CborEncoder(baos).encode(new CborBuilder().addArray()
					.add(new UnsignedInteger(0))
					.add(new co.nstant.in.cbor.model.ByteString(fromAddress(transaction.getTo())))
					.add(new co.nstant.in.cbor.model.ByteString(fromAddress(transaction.getFrom())))
					.add(new UnsignedInteger(transaction.getNonce()))
					.add(value)
					.add(new UnsignedInteger(transaction.getGasLimit()))
					.add(gasFeeCap)
					.add(gasGasPremium)
					.add(new UnsignedInteger(transaction.getMethod()))
					.add(new co.nstant.in.cbor.model.ByteString(new byte[] {})).end().build());
		} catch (CborException e) {
			throw new ApplicationException(e);
		}
		return baos.toByteArray();
	}

	public byte[] fromAddress(String address) {
		String str = address.substring(2);
		byte[] secp256k1 = new byte[21];
		secp256k1[0] = 1;
		System.arraycopy(Base32.decode(str), 0, secp256k1, 1, 20);
		return secp256k1;
	}
	
	/**
	 * 根据交易数据获取Hash
	 * @param transaction
	 * @return
	 */
	public String getTransactionHash(Transaction transaction) {
        byte[] encodedBytes = cborEncoder(transaction);
        Blake2b.Param param = new Blake2b.Param();
		param.setDigestLength(32);
		// 消息体字节
		byte[] messageByte = Blake2b.Digest.newInstance(param).digest(encodedBytes);
		Cid cid = new Cid(1, Cid.Codec.DagCbor, Multihash.Type.blake2b_256, messageByte);
		return Multibase.encode(Multibase.Base.Base32, cid.toBytes());
	}

}
