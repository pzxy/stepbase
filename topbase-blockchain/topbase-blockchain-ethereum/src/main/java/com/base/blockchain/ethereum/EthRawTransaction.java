package com.base.blockchain.ethereum;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.List;

import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpType;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractEIP1559GasProvider;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Numeric;

import com.base.blockchain.ethereum.utils.Web3jUtils;
import com.gitee.magic.core.exception.ApplicationException;

/**
 * 
 * @author start
 *
 */
public class EthRawTransaction{
	
	public String signOrSend(
			String url,
			BigInteger privateKey,
			String contractAddress,
			BigInteger value,
			String data,
			ContractGasProvider gasProvider,Boolean isSign) {
        return signOrSend(url, privateKey, contractAddress, value,data, gasProvider,"", isSign);
	}
	
	public String signOrSend(
			String url,
			BigInteger privateKey,
			String contractAddress,
			BigInteger value,
			String data,
			ContractGasProvider gasProvider,
			String contractFunc,Boolean isSign) {
		Web3j web3j=Web3jUtils.getWeb3j(url);
		Long chainId=Web3jUtils.getChainId(web3j);
        Credentials credentials = Credentials.create(ECKeyPair.create(privateKey));
        return signOrSend(web3j, chainId, credentials, contractAddress, value,data, gasProvider,contractFunc, isSign);
	}
	
	public String signOrSend(
			Web3j web3j,
			Long chainId,
			Credentials credentials,
			String contractAddress,
			BigInteger value,
			String data,
			ContractGasProvider gasProvider,Boolean isSign) {
		return signOrSend(web3j, chainId, credentials, contractAddress, value, data, gasProvider, "", isSign);
	}

	public String signOrSend(
			Web3j web3j,
			Long chainId,
			Credentials credentials,
			String contractAddress,
			BigInteger value,
			String data,
			ContractGasProvider gasProvider,
			String contractFunc,Boolean isSign) {
        BigInteger nonce =  Web3jUtils.getNonce(web3j, credentials.getAddress());
        
        RawTransaction rawTransaction=null;
        if(gasProvider instanceof ContractEIP1559GasProvider) {
        	ContractEIP1559GasProvider provider=(ContractEIP1559GasProvider)gasProvider;
        	if(provider.isEIP1559Enabled()) {
        		rawTransaction = RawTransaction.createTransaction(
        				chainId, 
        				nonce,
        				provider.getGasLimit(contractFunc), 
        				contractAddress,
        				value,
        				data,
        				provider.getMaxPriorityFeePerGas(contractFunc),
        				provider.getMaxFeePerGas(contractFunc));
        	}
        }
        if(rawTransaction==null) {
        	rawTransaction = RawTransaction.createTransaction(
            		nonce,
            		gasProvider.getGasPrice(contractFunc),
            		gasProvider.getGasLimit(contractFunc), 
            		contractAddress, 
            		value, 
            		data);
        }
		return signAndSend(web3j,chainId,credentials,rawTransaction,isSign);
	}
	
	public String signAndSend(
			Web3j web3j,
			Long chainId,
			Credentials credentials,
			RawTransaction rawTransaction,
			Boolean isSign) {
		RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials,chainId);
        if(isSign) {
			String hexValue = rawTransactionManager.sign(rawTransaction);
    	    return Hash.sha3(hexValue);
        }else {
        	try {
    			return rawTransactionManager.signAndSend(rawTransaction).getTransactionHash();
    		} catch (IOException e) {
    			throw new ApplicationException(e);
    		}
        }
    }
	
	/**
	 * 对应python实现
	 * <pre>
	 * 	from eth_account import Account
	 * 	import hashlib
	 * 	data="harry"
	 * 	account = Account.from_key("8b3a350cf5c34c9194ca85829a2df0ec3153be0318b5e2d3348e872092edffba")
	 * 	hexStr=hashlib.sha256(data.encode("UTF-8")).hexdigest();
	 * 	hash=account.signHash(hexStr)
	 * </pre>
	 * @param credentials
	 * @param data
	 * @return
	 */
	public Sign.SignatureData signMessage(Credentials credentials,byte[] data) {
		return Sign.signMessage(data, credentials.getEcKeyPair(),false);
	}
	
	public String signMessageHash(Credentials credentials,byte[] data) {
		Sign.SignatureData sign=signMessage(credentials,data);
		String r=Numeric.toHexString(sign.getR());
		String s=Numeric.toHexString(sign.getS());
		String v=Numeric.toHexString(sign.getV());
		return r+s.substring(2,s.length())+v.substring(2,v.length());
	}
	
	public Boolean verify(String address,byte[] data,String sign) {
		byte[] signHash = Numeric.hexStringToByteArray(sign);
	    byte[] r = Arrays.copyOf(signHash,32);
	    byte[] s = Arrays.copyOfRange(signHash,32,64);
	    byte[] v = Arrays.copyOfRange(signHash,64,65);
		return verify(address,data,new Sign.SignatureData(v,r,s));
	}
	
	public Boolean verify(String address,byte[] data,Sign.SignatureData signatureData) {
        BigInteger publicKey;
		try {
			publicKey = Sign.signedMessageHashToKey(data, signatureData);
		} catch (SignatureException e) {
			throw new ApplicationException(e);
		}
        String parseAddress = "0x" + Keys.getAddress(publicKey);
        return parseAddress.equals(address);
	}
	
	/**
	 * 根据交易数据获取Hash
	 * @param transaction
	 * @return
	 */
	public String getTransactionHash(
			Long v,String r,String s,RawTransaction rawTransaction) {
		//去掉0x
		String rr = r.replace("0x", "");
		String ss = s.replace("0x", "");
		//长度不足64位前面补0
		while(ss.length()<64) {
			ss="0"+ss;
		}
		Sign.SignatureData signatureData = new Sign.SignatureData(
				v.byteValue(),
				Hex.decode(rr), 
				Hex.decode(ss));
		List<RlpType> rlpTypeList = rawTransaction.getTransaction().asRlpValues(signatureData);
		RlpList rlpList = new RlpList(rlpTypeList);
		byte[] encoded = RlpEncoder.encode(rlpList);
		byte[] localHash = Hash.sha3(encoded);
		String localHashHex = Hex.toHexString(localHash);
		return "0x" + localHashHex;
	}
	
}
