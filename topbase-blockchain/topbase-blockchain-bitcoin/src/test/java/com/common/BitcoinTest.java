package com.common;

import java.math.BigInteger;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.bitcoinj.core.Sha256Hash;
import org.junit.Test;

import com.base.blockchain.bitcoin.BitcoinCashService;
import com.base.blockchain.bitcoin.BitcoinService;
import com.base.blockchain.wallet.core.ChainEnum;
import com.base.blockchain.wallet.utils.BaseTest;

public class BitcoinTest extends BaseTest {
    
    @Test
    public void address() {
		
		BitcoinService f=new BitcoinService(getPrivateKey(ChainEnum.BTC));
		System.out.println(f.getAddress());
		System.out.println("余额:"+f.getBalance("1Bf9sZvBHPFGVPX71WX2njhd1NXKv5y7v5"));
		
		BitcoinCashService fcash=new BitcoinCashService(getPrivateKey(ChainEnum.BCH));
		System.out.println(fcash.getAddress());
//		System.out.println("余额:"+fcash.getBalance("1Bf9sZvBHPFGVPX71WX2njhd1NXKv5y7v5"));
	}
    
    /**
     * bitcoin加签验签
     * @throws Exception
     */
    @Test
    public void signatureVerification() throws Exception {
    	byte[] data="start".getBytes();
    	BigInteger priKey=new BigInteger("62974329224788767027781683098475633401756052717538436960952236504350829969338");
    	ECKey eckey=ECKey.fromPrivate(priKey);
    	System.out.println("pubKey:"+eckey.getPublicKeyAsHex());
    	ECDSASignature signature=eckey.sign(Sha256Hash.of(data));
    	System.out.println("bitcoin验签结果:"+ECKey.verify(Sha256Hash.hash(data), signature,eckey.getPubKey()));
    }
	
}
