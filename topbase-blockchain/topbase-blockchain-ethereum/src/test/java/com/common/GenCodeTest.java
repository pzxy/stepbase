package com.common;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.web3j.abi.datatypes.Address;
import org.web3j.codegen.SolidityFunctionWrapperGenerator;

/**
 * 
 * @author start
 *
 */
public class GenCodeTest {
    
	@Test
    public void generate() throws IOException {
		File binFile=new File(getClass().getResource("/solidity/Erc20.bin").getFile());
		File abiFile=new File(getClass().getResource("/solidity/Erc20.abi").getFile());
		//输出目录
		File outDir=new File("/Users/start/Desktop/generate");
		
		int addressLength = Address.DEFAULT_LENGTH / Byte.SIZE;
		try {
			new SolidityFunctionWrapperGenerator(
					binFile,
					abiFile,
					outDir,
					"Erc20","com.topnetwork.contract",
					true,true,addressLength).generate();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		};
	}
	
}
