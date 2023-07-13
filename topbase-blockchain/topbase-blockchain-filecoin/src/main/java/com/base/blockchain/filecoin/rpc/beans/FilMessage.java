package com.base.blockchain.filecoin.rpc.beans;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

/**
 * @author start 
 */
@Data
@Builder
public class FilMessage {
	
    private int version;

    private String to;

    private String from;

    private int nonce;

    private BigDecimal value;

    private BigDecimal gasLimit;

    private BigDecimal gasFeeCap;

    private BigDecimal gasPremium;

    private int method;

    private String params;

    private String hash;
}
