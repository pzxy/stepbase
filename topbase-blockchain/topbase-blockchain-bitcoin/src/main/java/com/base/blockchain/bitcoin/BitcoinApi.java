package com.base.blockchain.bitcoin;

import java.util.List;

/**
 * @author start 
 */
public interface BitcoinApi {
	
	/**
	 * 获取余额 
	 * @param address
	 * @return
	 */
	long getBalance(String address);

	/**
	 * 未被花费的交易
	 * @param address
	 * @return
	 */
	List<UnSpentUtxo> getUnspent(String address);

}
