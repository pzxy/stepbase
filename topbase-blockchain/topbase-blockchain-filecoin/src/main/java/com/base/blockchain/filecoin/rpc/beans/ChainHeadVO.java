package com.base.blockchain.filecoin.rpc.beans;

import java.math.BigInteger;
import java.util.List;

import com.gitee.magic.core.annotations.Column;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author start
 */
@Getter@Setter@ToString
public class ChainHeadVO {
	
	@Column("Height")
	private Integer height;
	
	@Column("Blocks")
	private List<Block> blocks;
	
	@Getter@Setter@ToString
	public static class Block{

		@Column("ParentBaseFee")
		private BigInteger parentBaseFee;
		
	}
	
}
