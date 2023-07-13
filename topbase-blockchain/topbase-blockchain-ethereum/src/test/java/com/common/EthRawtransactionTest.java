package com.common;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Numeric;

import com.base.blockchain.ethereum.EthRawTransaction;
import com.base.blockchain.ethereum.utils.Web3jUtils;
import com.base.blockchain.wallet.utils.BaseTest;

public class EthRawtransactionTest extends BaseTest {
    
	@Test
    public void signOrSend() {
		BigInteger privateKey=Numeric.toBigInt("7a0f48198ad1cbf5c47c9b665dbbd50a3ee30de65aac026edef77958d07e20a9");
		String url="https://data-seed-prebsc-1-s1.binance.org:8545";
		String address="0x45B55B71d11df606186fB1820672762Fa5c4C8Ba";
		BigInteger amount=new BigInteger("23000000");
		BigInteger fee=new BigInteger("23000000");
		
		@SuppressWarnings("rawtypes")
		org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
				"mintToAccount", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(address), 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.generated.Uint256(fee)), 
                Collections.<TypeReference<?>>emptyList());
        String funData = FunctionEncoder.encode(function);

        BigInteger gasLimit=new BigInteger("300000");
        Web3j web3j=Web3jUtils.getWeb3j(url);
        ContractGasProvider provider=Web3jUtils.getGasProvider(web3j, gasLimit);
        
        EthRawTransaction as=new EthRawTransaction();
        String hash=as.signOrSend(url,privateKey, 
        		"0xab468D1234a640371F9781122eA2e0949028f753", BigInteger.ZERO,funData,
        		provider,"",true);
        System.out.println(hash);
        String hash1=as.signOrSend(url,privateKey, 
        		"0xab468D1234a640371F9781122eA2e0949028f753", BigInteger.ZERO,funData,
        		provider,"",false);
        System.out.println(hash1);
	}
	
    @Test
    public void signOrSend1159() {
    	BigInteger privateKey=Numeric.toBigInt("0859cad4050d0d7c453738576a7a48b3c54f205f52f134d439a253b643ce092c");
		String url="http://192.168.50.122:8080";
		String address="0xe8A36c3d3ff15D28aD4F150a0B9DB081b779DB77";
		
		List<String> addresss=new ArrayList<>();
		addresss.add("0xe8A36c3d3ff15D28aD4F150a0B9DB081b779DB77");
		addresss.add("0xe8A36c3d3ff15D28aD4F150a0B9DB081b779DB77");
		List<String> tokenUris=new ArrayList<>();
		tokenUris.add("f4.png");
		tokenUris.add("f2.png");
		@SuppressWarnings("rawtypes")
		org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function("",
				Arrays.<Type>asList(
						new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
								org.web3j.abi.datatypes.Address.class,
								org.web3j.abi.Utils.typeMap(addresss, org.web3j.abi.datatypes.Address.class)),
						new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
								org.web3j.abi.datatypes.Utf8String.class,
								org.web3j.abi.Utils.typeMap(tokenUris, org.web3j.abi.datatypes.Utf8String.class))),
				Collections.<TypeReference<?>>emptyList());
		String funData = FunctionEncoder.encode(function);

        BigInteger gasLimit=new BigInteger("3000000");
        Web3j web3j=Web3jUtils.getWeb3j(url);
        ContractGasProvider provider=Web3jUtils.get1159GasProvider(web3j, gasLimit);

        EthRawTransaction raw=new EthRawTransaction();
        String hash=raw.signOrSend(url,privateKey, address, BigInteger.ZERO,funData,
        		provider,"",true);
        System.out.println(hash);
        String hash1=raw.signOrSend(url,privateKey,address, BigInteger.ZERO,funData,
        		provider,"",false);
        System.out.println(hash1);
	}
	
}
