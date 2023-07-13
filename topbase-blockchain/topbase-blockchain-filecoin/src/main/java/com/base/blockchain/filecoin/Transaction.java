package com.base.blockchain.filecoin;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author start
 */
public class Transaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String from;
	private String to;
	private BigInteger value;

	private Long nonce;

	private BigInteger gasFeeCap;
	private BigInteger gasPremium;
	private BigInteger gasLimit;

	private Long method;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public BigInteger getValue() {
		return value;
	}

	public void setValue(BigInteger value) {
		this.value = value;
	}

	public Long getNonce() {
		return nonce;
	}

	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}

	public BigInteger getGasFeeCap() {
		return gasFeeCap;
	}

	public void setGasFeeCap(BigInteger gasFeeCap) {
		this.gasFeeCap = gasFeeCap;
	}

	public BigInteger getGasPremium() {
		return gasPremium;
	}

	public void setGasPremium(BigInteger gasPremium) {
		this.gasPremium = gasPremium;
	}

	public BigInteger getGasLimit() {
		return gasLimit;
	}

	public void setGasLimit(BigInteger gasLimit) {
		this.gasLimit = gasLimit;
	}

	public Long getMethod() {
		return method;
	}

	public void setMethod(Long method) {
		this.method = method;
	}

}
