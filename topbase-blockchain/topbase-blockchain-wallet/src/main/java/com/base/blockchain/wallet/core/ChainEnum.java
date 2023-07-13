package com.base.blockchain.wallet.core;

/**
 * @author start 
 */
public enum ChainEnum {
	
	//See:https://github.com/satoshilabs/slips/blob/master/slip-0044.md
	
	BTC(0),
	ETH(60),
	BCH(145),
//	TRX(195),
	DOT(354),
	FIL(461);
	
	private final int coinType;

    ChainEnum(int coinType) {
        this.coinType = coinType;
    }

    public int getCoinType() {
        return coinType;
    }
	
}
