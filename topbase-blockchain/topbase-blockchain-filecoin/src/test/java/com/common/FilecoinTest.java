package com.common;

import java.math.BigInteger;

import org.junit.Test;

import com.base.blockchain.filecoin.FilcoinService;
import com.base.blockchain.filecoin.Filecoinj;
import com.base.blockchain.filecoin.RawMessage;
import com.base.blockchain.filecoin.Transaction;
import com.base.blockchain.wallet.core.ChainEnum;
import com.base.blockchain.wallet.utils.BaseTest;

public class FilecoinTest extends BaseTest {
	
//	private String url="http://127.0.0.1:1234/rpc/v0";
//	private String authorization="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBbGxvdyI6WyJyZWFkIiwid3JpdGUiLCJzaWduIiwiYWRtaW4iXX0.QIjwi8fTErgAMb0B9-FTU-69mI-n3h9HHF7ewoff6QA";
	private String url="http://69.194.1.166:1670/rpc/v0";
	private String authorization="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBbGxvdyI6WyJyZWFkIiwid3JpdGUiLCJzaWduIiwiYWRtaW4iXX0.7PueMjY1sWJkhkLflQSN8Wm2AbXe45YBO82UWxPure0";
//	private String url="https://filecoin.infura.io";
//	private String authorization="Basic "+Base64.encode("1wU9jkT35xa4r5uo9SaUlqgcglf:aa61a081808fbc98f00a2e13b1423af6".getBytes());

	private BigInteger priKey=getPrivateKey(ChainEnum.FIL);
	
    @Test
    public void address() {
		Filecoinj filecoinj=Filecoinj.build(url, authorization);
		FilcoinService f=new FilcoinService(priKey);
		f.setFilecoinj(filecoinj);
		
//		System.out.println(f.getAddress());
		System.out.println("地址:"+f.getAddress()+"\t余额:"+f.getBalance(f.getAddress()));
	}
    
    @Test
    public void transfer() {
		Filecoinj filecoinj=Filecoinj.build(url, authorization);
		FilcoinService f=new FilcoinService(priKey);
		f.setFilecoinj(filecoinj);
		System.out.println("地址:"+f.getAddress()+"\n余额:"+f.getBalance(f.getAddress()));
		
//		String to="f1lwlcfnryhw5tqobsm26ry2j4fnp7stpfxlbug5q";
//		BigInteger value=BigNumberUtils.pow18(new BigDecimal("0.001"));
//    	System.out.println("转账hash:"f.transfer(to, value));
	}
    
    @Test
    public void cid() {
    	Transaction unsignedMessageAPI = new Transaction();
        unsignedMessageAPI.setFrom("f1pityiep2h3j6xn3sq6ytbu3ubzicy2r76sczyfy");
        unsignedMessageAPI.setTo("f1lwlcfnryhw5tqobsm26ry2j4fnp7stpfxlbug5q");
        unsignedMessageAPI.setNonce(2L);
        unsignedMessageAPI.setValue(new BigInteger("1000000000000000"));
        unsignedMessageAPI.setGasFeeCap(new BigInteger("1830029194"));
        unsignedMessageAPI.setGasPremium(new BigInteger("100837"));
        unsignedMessageAPI.setGasLimit(new BigInteger("608335"));
        unsignedMessageAPI.setMethod(0L);
        RawMessage raw=new RawMessage();
		System.out.println(raw.cid(unsignedMessageAPI, priKey));
	}
    
    @Test
    public void verifyHash() {
    	Transaction transaction = new Transaction();
        transaction.setFrom("f1dpkjwnlmtzzhflznk57souty2mc6xavpujqczsi");
        transaction.setTo("f13egozqdr4ckzpuyiccwddhfddychpzilzl2lm5q");
        transaction.setNonce(27L);
        transaction.setValue(new BigInteger("1000000000000000000"));
        transaction.setGasFeeCap(new BigInteger("167980890"));
        transaction.setGasPremium(new BigInteger("100075"));
        transaction.setGasLimit(new BigInteger("605085"));
        transaction.setMethod(0L);
        RawMessage raw=new RawMessage();
        String hash=raw.getTransactionHash(transaction);
        System.out.println("bafy2bzacebs5q5gtfmjyvghd22woqiyigg7i43so2xr5jluuyln677dcrtiqo".equals(hash));
    }
	
}
