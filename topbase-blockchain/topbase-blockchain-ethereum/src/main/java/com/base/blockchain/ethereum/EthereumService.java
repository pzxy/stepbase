package com.base.blockchain.ethereum;

import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;

import com.base.blockchain.ethereum.utils.Web3jUtils;
import com.base.blockchain.wallet.core.BaseService;
import com.base.blockchain.wallet.core.ChainEnum;

/**
 * 以太坊浏览器:https://cn.etherscan.com/
 * @author start
 *
 */
public class EthereumService extends BaseService {
    
    private Web3j web3j;

	public void setWeb3j(Web3j web3j) {
		this.web3j = web3j;
	}
    
    public Web3j getWeb3j() {
    	return web3j;
    }

	public EthereumService(BigInteger privateKey) {
		super(privateKey);
	}
	
	@Override
	public String getCoinName() {
		return ChainEnum.ETH.name();
	}

	@Override
	public String getAddress() {
		ECKeyPair keyPair=ECKeyPair.create(getPrivateKey());
		return "0x"+Keys.getAddress(keyPair);
	}

	@Override
	public BigInteger getBalance(String address) {
		return Web3jUtils.getBalance(getWeb3j(), getAddress());
	}
	
	/**
	 * 转账
	 * @param to
	 * @param value
	 * @param remark
	 * @return
	 */
	public String transfer(String to,BigInteger value,String remark) {
		ContractGasProvider gasProvider=Web3jUtils.getGasProvider(web3j, new BigInteger("21000"));
        return transfer(to, value, remark, gasProvider, false);
    }
	
	/**
	 * 转账
	 * @param to
	 * @param value
	 * @param remark
	 * @return
	 */
	public String transfer1159(String to,BigInteger value,String remark) {
		ContractGasProvider gasProvider=Web3jUtils.get1159GasProvider(web3j, new BigInteger("21000"));
        return transfer(to, value, remark, gasProvider, false);
    }
	
	/**
	 * 转账
	 * @param to
	 * @param value
	 * @param remark
	 * @param gasProvider
	 * @param isSign
	 * @return
	 */
    public String transfer(
    		String to,
    		BigInteger value,
    		String remark,
    		ContractGasProvider gasProvider,
    		Boolean isSign) {
        Long chainId=Web3jUtils.getChainId(getWeb3j());
        Credentials credentials = Credentials.create(ECKeyPair.create(getPrivateKey()));
        EthRawTransaction raw=new EthRawTransaction();
        return raw.signOrSend(getWeb3j(),chainId, credentials, to, value, remark, gasProvider,"", isSign);
    }
    
}
