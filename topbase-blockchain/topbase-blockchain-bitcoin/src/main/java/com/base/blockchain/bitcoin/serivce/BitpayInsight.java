package com.base.blockchain.bitcoin.serivce;

import java.util.ArrayList;
import java.util.List;

import com.base.blockchain.bitcoin.BitcoinApi;
import com.base.blockchain.bitcoin.UnSpentUtxo;
import com.gitee.magic.core.json.JsonArray;
import com.gitee.magic.core.json.JsonObject;

/**
 * https://www.blockchain.com/zh-cn/api/blockchain_api
 * @author start
 *
 */
public class BitpayInsight implements BitcoinApi {

	/**
	 * 获取余额 
	 * https://blockchain.info/balance?active=$address
	 * @param address
	 * @return
	 */
	@Override
	public long getBalance(String address) {
		String content=HttpClient.requestGet("https://blockchain.info/balance?active="+address);
		JsonObject json=new JsonObject(content);
		JsonObject value=json.getJsonObject(address);
		return value.getLong("final_balance");
	}

	/**
	 * 未被花费的交易
	 * https://blockchain.info/unspent?active=$address
	 * @param address
	 * @return
	 */
	@Override
	public List<UnSpentUtxo> getUnspent(String address) {
        String url = "https://insight.bitpay.com/api/addr/"+address+"/utxo";
		String content=HttpClient.requestGet(url);
		JsonArray array=new JsonArray(content);
		List<UnSpentUtxo> listUtxo=new ArrayList<>();
		for(int i=0;i<array.length();i++) {
			JsonObject obj=array.getJsonObject(i);
			UnSpentUtxo utxo=new UnSpentUtxo();
			utxo.setAddress(obj.getString("address"));
			utxo.setHash(obj.getString("txid"));
			utxo.setIndex(obj.getLong("vout"));
			utxo.setScript(obj.getString("scriptPubKey"));
			utxo.setValue(obj.getLong("amount"));
			utxo.setHeight(obj.getInt("height"));
			listUtxo.add(utxo);
		}
		return listUtxo;
	}

}
