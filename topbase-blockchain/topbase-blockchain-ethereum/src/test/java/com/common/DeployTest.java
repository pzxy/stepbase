package com.common;

import java.math.BigInteger;

import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

import com.base.blockchain.ethereum.erc20.Erc20Dai;
import com.base.blockchain.ethereum.utils.Web3jUtils;

public class DeployTest {
	
	private String url="http://192.168.50.122:8080";
	private String privateKey="0859cad4050d0d7c453738576a7a48b3c54f205f52f134d439a253b643ce092c";

	@Test
	public void deploy() throws Exception {
		Web3j web3j=Web3jUtils.getWeb3j(url);
		Credentials credentials=Web3jUtils.getCredentials(privateKey);
		ContractGasProvider gasProvider=Web3jUtils.get1159GasProvider(
				web3j, new BigInteger("30000000"));
		
		RemoteCall<Erc20Dai> deployResult = Erc20Dai.deploy(
				web3j,
				credentials, 
				gasProvider,
				new BigInteger(Web3jUtils.getChainId(web3j)+""));
		Erc20Dai result = deployResult.send();
		System.out.println("部署地址:" + result.getContractAddress());
	}
	
}
