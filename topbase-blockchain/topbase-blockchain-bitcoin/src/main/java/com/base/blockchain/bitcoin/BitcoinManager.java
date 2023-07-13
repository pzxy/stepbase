package com.base.blockchain.bitcoin;

import java.util.ArrayList;
import java.util.List;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.UTXO;
import org.bitcoinj.core.Utils;
import org.bitcoinj.script.Script;

import com.base.blockchain.bitcoin.serivce.BitpayInsight;

/**
 * @author start 
 */
public class BitcoinManager {
	
	private NetworkParameters network;
	
	public BitcoinManager(NetworkParameters network) {
		this.network=network;
	}

	/**
	 * 交易签名
	 * @param privKey 私钥
	 * @param recevieAddr 收款地址
	 * @param formAddr	发送地址
	 * @param amount	金额
	 * @param fee	手续费
	 * @param unUtxos	未交易的UTXO
	 * @return
	 */
	public String signTransaction(String privKey, String recevieAddr, String formAddr, long amount, long fee,
			List<UnSpentUtxo> unUtxos) {
		DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(network, privKey);
		ECKey key = dumpedPrivateKey.getKey();
		// 构建交易
		Transaction tx = new Transaction(network);
		// 接收地址
		Address receiveAddress = Address.fromString(network, recevieAddr);
		// 转出
		tx.addOutput(Coin.valueOf(amount), receiveAddress);
		// 如果需要找零 消费列表总金额 - 已经转账的金额 - 手续费
		long value = unUtxos.stream().mapToLong(UnSpentUtxo::getValue).sum();
		long leave = value - amount - fee;
		if (leave > 0) {
			Address toAddress = Address.fromString(network, formAddr);
			tx.addOutput(Coin.valueOf(leave), toAddress);
		}
		List<UTXO> utxos = new ArrayList<>();
		//utxos is an array of inputs from my wallet
		for (UnSpentUtxo unUtxo : unUtxos) {
			utxos.add(new UTXO(Sha256Hash.wrap(unUtxo.getHash()), 
					unUtxo.getIndex(), 
					Coin.valueOf(unUtxo.getValue()),
					unUtxo.getHeight(), 
					false, 
					new Script(Utils.HEX.decode(unUtxo.getScript())),
					unUtxo.getAddress()));
		}
		for (UTXO utxo : utxos) {
			TransactionOutPoint outPoint = new TransactionOutPoint(network, utxo.getIndex(), utxo.getHash());
			//YOU HAVE TO CHANGE THIS
			tx.addSignedInput(outPoint, utxo.getScript(), key, Transaction.SigHash.ALL, true);
		}
		new Context(network);
		tx.getConfidence().setSource(TransactionConfidence.Source.NETWORK);
		tx.setPurpose(Transaction.Purpose.USER_PAYMENT);

		return Utils.HEX.encode(tx.bitcoinSerialize());
	}
	
	public static void main(String[] args) {
		BitpayInsight bc=new BitpayInsight();
		System.out.println(bc.getUnspent("36aReDFympuy5diDuNPVYjhKQFX9JayCAE"));
		
	}

}
