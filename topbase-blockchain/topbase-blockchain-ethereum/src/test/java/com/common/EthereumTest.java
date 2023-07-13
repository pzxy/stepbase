package com.common;

import java.math.BigInteger;

import org.junit.Test;
import org.web3j.protocol.Web3j;

import com.base.blockchain.ethereum.Erc20;
import com.base.blockchain.ethereum.EthereumService;
import com.base.blockchain.ethereum.utils.Web3jUtils;
import com.base.blockchain.wallet.core.ChainEnum;
import com.base.blockchain.wallet.utils.BaseTest;

public class EthereumTest extends BaseTest {
    
    @Test
    public void address() {
		BigInteger priKey=getPrivateKey(ChainEnum.ETH);

		String coinAddress="0x04F535663110A392A6504839BEeD34E019FdB4E0";
		
		String url="https://http-testnet.hecochain.com";
//		String url="https://http-mainnet-node.huobichain.com";
//		String url="https://mainnet.infura.io/v3/d75fbde6f9c34a2b80a613f91186b601";
		Web3j web3j=Web3jUtils.getWeb3j(url);
		EthereumService f=new EthereumService(priKey);
		f.setWeb3j(web3j);
		System.out.println(f.getAddress());
		System.out.println("余额:"+f.getBalance(f.getAddress()));
		
		Erc20 erc20=new Erc20(f.getWeb3j(),priKey,coinAddress);
		System.out.println("代币余额:"+erc20.getBalance(f.getAddress()));
		String toAddress="0xD1f9981F00E10bE7E22450f09267a8F9AD587BFd";
		BigInteger money=new BigInteger("10000000");
//		f.transfer(toAddress, money, gasPrice, gasLimit);
        String hash1=erc20.transfer(toAddress, money);
        System.out.println(hash1);
	}
	
}
