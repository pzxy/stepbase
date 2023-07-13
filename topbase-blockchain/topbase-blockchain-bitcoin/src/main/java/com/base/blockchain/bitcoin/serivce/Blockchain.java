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
public class Blockchain implements BitcoinApi {

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
		String content=HttpClient.requestGet("https://blockchain.info/unspent?active="+address);
		JsonObject json=new JsonObject(content);
		JsonArray array=json.getJsonArray("unspent_outputs");
		System.out.println(array);
		
		List<UnSpentUtxo> listUtxo = new ArrayList<UnSpentUtxo>();
		UnSpentUtxo u = new UnSpentUtxo();
		u.setAddress("");
		u.setHeight(0);
		u.setHash("233e6a080067ed52b5bf483056c65b5893127f3eb47388acf87b6d6792c3b846");
		u.setScript("a9143599842e027800fe692da74773e5765594e9978687");
		u.setValue(100000);
		u.setIndex(0);
		listUtxo.add(u);
		return listUtxo;
	}

}
