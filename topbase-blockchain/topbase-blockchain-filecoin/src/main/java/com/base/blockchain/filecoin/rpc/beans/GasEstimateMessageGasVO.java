package com.base.blockchain.filecoin.rpc.beans;

import java.math.BigInteger;

import com.gitee.magic.core.annotations.Column;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author start
 *
 */
@Getter@Setter@ToString
public class GasEstimateMessageGasVO {

	@Column("Version")
	private Long version;
	@Column("GasLimit")
	private BigInteger gasLimit;
	@Column("GasFeeCap")
	private BigInteger gasFeeCap;
	@Column("GasPremium")
	private BigInteger gasPremium;
}
