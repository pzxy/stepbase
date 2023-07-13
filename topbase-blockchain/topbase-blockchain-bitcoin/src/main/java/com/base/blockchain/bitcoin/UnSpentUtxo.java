package com.base.blockchain.bitcoin;

/**
 * @author start 
 */
public class UnSpentUtxo {

	/**
	 * 交易哈希
	 */
	private String hash;

	/**
	 * 交易索引
	 */
	private long index;

	/**
	 * 交易金额
	 */
	private long value;

	/**
	 * 区块高度
	 */
	private int height;

	/**
	 * 目标公钥脚本
	 */
	private String script;

	/**
	 * 钱包地址
	 */
	private String address;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
