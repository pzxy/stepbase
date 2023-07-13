package com.base.blockchain.filecoin;

import java.math.BigInteger;

import com.base.blockchain.filecoin.rpc.beans.GasEstimateMessageGasVO;
import com.base.blockchain.wallet.core.BaseService;
import com.base.blockchain.wallet.core.ChainEnum;

/**
 * @author start
 */
public class FilcoinService extends BaseService {

	private Account account;
	private Filecoinj filecoinj;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Filecoinj getFilecoinj() {
		return filecoinj;
	}

	public void setFilecoinj(Filecoinj filecoinj) {
		this.filecoinj = filecoinj;
	}

	public FilcoinService(BigInteger privateKey) {
		super(privateKey);
		this.account=new Account(privateKey);
	}

	@Override
	public String getCoinName() {
		return ChainEnum.FIL.name();
	}

	@Override
	public String getAddress() {
		return account.getAddress();
	}

	@Override
	public BigInteger getBalance(String address) {
		return getFilecoinj().getRpcService().getBalance(address);
	}

	public String transfer(String to, BigInteger value) {
		String from = getAddress();
		Long nonce = getFilecoinj().getRpcService().getNonce(from);
		
		GasEstimateMessageGasVO gasEstimateMessageGas=getFilecoinj().getRpcService().gasEstimateMessageGas(
				from, 
				to, 
				nonce, 
				value, 
				0L);
		
		return transfer(to,value,nonce,new GasProvider() {
			
			@Override
			public BigInteger getGasPremium() {
				return gasEstimateMessageGas.getGasPremium();
			}
			
			@Override
			public BigInteger getGasLimit() {
				return gasEstimateMessageGas.getGasLimit();
			}
			
			@Override
			public BigInteger getGasFeeCap() {
				return gasEstimateMessageGas.getGasFeeCap();
			}
		},true);
	}

	public String transfer(String to, BigInteger value, Long nonce,GasProvider gasProvider, Boolean isSend) {

		Transaction transaction = new Transaction();
		transaction.setFrom(getAddress());
		transaction.setTo(to);
		transaction.setValue(value);

		transaction.setNonce(nonce);
		transaction.setMethod(0L);
		transaction.setGasLimit(gasProvider.getGasLimit());
		transaction.setGasFeeCap(gasProvider.getGasFeeCap());
		transaction.setGasPremium(gasProvider.getGasPremium());

		return transfer(transaction,isSend);
	}
	
	public String transfer(Transaction transaction, Boolean isSend) {
		RawMessage raw = new RawMessage();
		if (isSend) {
			String sign = raw.sign(transaction, getPrivateKey());
			return getFilecoinj().getRpcService().mpoolPush(transaction, sign);
		} else {
			return raw.cid(transaction, getPrivateKey());
		}
	}

}
