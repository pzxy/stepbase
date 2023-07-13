package com.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.RawTransactionManager;

import com.base.blockchain.ethereum.utils.DecodeUtils;

public class ContractCallTest extends ManagedTransaction {

	private String contractAddress;

	public ContractCallTest(String contractAddress, Web3j web3j, Credentials credentials) {
		super(web3j, new RawTransactionManager(web3j, credentials));
		this.contractAddress = ensResolver.resolve(contractAddress);
	}

	@SuppressWarnings("rawtypes")
	public String calculateTokenReserveTotalSupply(List<String> add1, List<String> add2) throws IOException {
		String name = "calculateTokenReserveTotalSupply";
		final Function function = new Function(
				name, 
                Arrays.<Type>asList(
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(add1, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(add2, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
		
		String encodedFunction = FunctionEncoder.encode(function);
		return call(contractAddress, encodedFunction, DefaultBlockParameterName.LATEST);
	}
	

	public static void main(String[] args) throws IOException {
		String ownerPrivateKey = "EB1AA7BDCF0B9804B1B246E6F11E40B2BEA87D0FB1C5EB265D28EF7AB0D86830";
		
		Web3j web3j = Web3j.build(new HttpService("https://data-seed-prebsc-1-s1.binance.org:8545"));
		String c = "0x7B51d55165743a06aAdc32474Af2bB8dBfeDDE55";
		ContractCallTest ser = new ContractCallTest(c, web3j, Credentials.create(ownerPrivateKey));
		
		List<String> add1=new ArrayList<>();
		add1.add("0xF89f4B86303d936cC3C1E7cb5AC425eDb9494fe7");
		add1.add("0xF89f4B86303d936cC3C1E7cb5AC425eDb9494fe7");
		List<String> add2=new ArrayList<>();
		add2.add("0x90664F36D1e2Da28EB26164D8110C29E97158849");
		add2.add("0x90664F36D1e2Da28EB26164D8110C29E97158849");
		
		String dataHex = ser.calculateTokenReserveTotalSupply(add1,add2);
		System.out.println(dataHex);
		System.out.println("=================");
		
		List<String> dataHexList=DecodeUtils.decodeArray(dataHex);
		for(String itemHex : dataHexList) {
			//封装成对应的实体对象
        	System.out.println(DecodeUtils.decodeBigInt(itemHex.substring(0,64)));
        	System.out.println(DecodeUtils.decodeAddress(itemHex.substring(64,128)));
        	System.out.println(DecodeUtils.decodeAddress(itemHex.substring(128,192)));
        }
		
	}

}
