package com.base.blockchain.filecoin.rpc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.base.blockchain.filecoin.Transaction;
import com.base.blockchain.filecoin.rpc.beans.ChainHeadVO;
import com.base.blockchain.filecoin.rpc.beans.FilCid;
import com.base.blockchain.filecoin.rpc.beans.FilMessage;
import com.base.blockchain.filecoin.rpc.beans.GasEstimateMessageGasVO;
import com.gitee.magic.context.ConverterEditorUtils;
import com.gitee.magic.core.json.JsonArray;
import com.gitee.magic.core.json.JsonObject;

/**
 * @author start
 */
public class RpcService {

	private static final String RESULT_STR = "result";

	private String url;
	private String authorization;

	public RpcService(String url, String authorization) {
		this.url = url;
		this.authorization = authorization;
	}

	/**
	 * 查询指定高度的TipSet
	 * 
	 * @param blockNumber
	 * @return
	 */
	public List<FilCid> getChainGetTipSetByHeight(Integer blockNumber) {
		List<Object> info = new ArrayList<>();
		info.add(blockNumber);
		info.add(new ArrayList<>());
		String response = HttpUtils.post(this.url, this.authorization, info,
				RpcMethodConfig.FIL_METHOD_CHAIN_GET_TIP_SETBYHEIGHT);
		
		List<FilCid> filCids = new ArrayList<>();
		JsonObject jsonBody = new JsonObject(response);
		if (jsonBody.has(RESULT_STR)) {
			JsonObject result = jsonBody.getJsonObject(RESULT_STR);
			Integer height = result.getInt("Height");
			if (Integer.compare(height, blockNumber) == 0) {
				JsonArray jsonArray = result.getJsonArray("Cids");
				if (jsonArray.length() > 0) {
					for (Object o : jsonArray) {
						JsonObject cidObject = new JsonObject(o.toString());
						FilCid cid = FilCid.builder().cid(cidObject.get("/").toString()).build();
						filCids.add(cid);
					}
				}
			}
		}
		return filCids;
	}

	/**
	 * 查询区块内的消息
	 * 
	 * @param cid
	 * @return
	 */
	public List<FilMessage> getChainGetBlockMessages(String cid) {
		JsonObject cidObj = new JsonObject();
		cidObj.put("/", cid);
		List<Object> info = new ArrayList<>();
		info.add(cidObj);
		String response = HttpUtils.post(this.url, this.authorization, info,
				RpcMethodConfig.FIL_METHOD_CHAIN_GGET_BLOCK_MESSAGES);
		
		List<FilMessage> filMessages = new ArrayList<>();
		JsonObject jsonBody = new JsonObject(response);
		if (jsonBody.has(RESULT_STR)) {
			JsonObject result = jsonBody.getJsonObject(RESULT_STR);
			JsonArray jsonArray = result.getJsonArray("SecpkMessages");
			for (int i = 0; i < jsonArray.length(); i++) {
				JsonObject secpkMessagesJson = jsonArray.getJsonObject(i);
				if (secpkMessagesJson.has("Message")) {
					JsonObject messageObject = secpkMessagesJson.getJsonObject("Message");
					JsonObject hashObject = secpkMessagesJson.getJsonObject("CID");
					String hash = "";
					if (hashObject != null) {
						hash = hashObject.getString("/");
					}
					String to = messageObject.getString("To");
					String from = messageObject.getString("From");
					BigDecimal value = messageObject.getBigDecimal("Value");
					BigDecimal gasFeeCap = messageObject.getBigDecimal("GasFeeCap");
					BigDecimal gasLimit = messageObject.getBigDecimal("GasLimit");
					BigDecimal gasPremium = messageObject.getBigDecimal("GasPremium");

					FilMessage filMessage = FilMessage.builder().to(to).from(from).value(value).hash(hash)
							.gasFeeCap(gasFeeCap).gasPremium(gasPremium).gasLimit(gasLimit).build();
					filMessages.add(filMessage);
				}
			}
		}
		return filMessages;
	}

	/**
	 * 查询当前链头的区块高度
	 * 
	 * @return
	 */
	public Integer getBlockHeight() {
		return getChainHead().getHeight();
	}
	
	public ChainHeadVO getChainHead() {
		String response = HttpUtils.post(this.url, this.authorization, new ArrayList<>(),
				RpcMethodConfig.FIL_METHOD_CHAINHEAD);
		JsonObject result = HttpUtils.result(response);
		return ConverterEditorUtils.restoreObject(ChainHeadVO.class, result.getJsonObject(RESULT_STR));
	}

	public BigInteger getBalance(String address) {
		List<Object> info = new ArrayList<>();
		info.add(address);
		String response = HttpUtils.post(this.url, this.authorization, info, RpcMethodConfig.FIL_METHOD_WALLET_BALANCE);
		JsonObject result = HttpUtils.result(response);
		return result.getBigInteger(RESULT_STR);
	}

	public Long getNonce(String address) {
		List<Object> info = new ArrayList<>();
		info.add(address);
		String response = HttpUtils.post(this.url, this.authorization, info,
				RpcMethodConfig.FIL_METHOD_MPOOL_GET_NONCE);
		JsonObject result = HttpUtils.result(response);
		return result.getLong(RESULT_STR);
	}

	public GasEstimateMessageGasVO gasEstimateMessageGas(String from, String to, Long nonce, BigInteger value,
			Long method) {

		JsonObject message = new JsonObject();
		message.put("To", to);
		message.put("From", from);
		message.put("Nonce", nonce);
		message.put("Value", value + "");
		message.put("Method", method);
		message.put("Params", "");

		JsonObject message1 = new JsonObject();

		List<Object> info = new ArrayList<>();
		info.add(message);
		info.add(message1);
		info.add(null);

		String response = HttpUtils.post(this.url, this.authorization, info,
				RpcMethodConfig.FIL_METHOD_GAS_ESTIMATE_MESSAGE_GAS);
		JsonObject result = HttpUtils.result(response);
		return ConverterEditorUtils.restoreObject(GasEstimateMessageGasVO.class, result.getJsonObject(RESULT_STR));
	}

	public String mpoolPush(Transaction unmsg, String sign) {

		JsonObject message = new JsonObject();
		message.put("Version", 0);
		message.put("To", unmsg.getTo());
		message.put("From", unmsg.getFrom());
		message.put("Value", unmsg.getValue() + "");
		message.put("Nonce", unmsg.getNonce());
		message.put("GasLimit", unmsg.getGasLimit());
		message.put("GasFeeCap", unmsg.getGasFeeCap() + "");
		message.put("GasPremium", unmsg.getGasPremium() + "");
		message.put("Method", unmsg.getMethod());

		JsonObject signature = new JsonObject();
		signature.put("Type", 1);
		signature.put("Data", sign);

		JsonObject params = new JsonObject();
		params.put("Message", message);
		params.put("Signature", signature);

		List<Object> info = new ArrayList<>();
		info.add(params);

		String response = HttpUtils.post(this.url, this.authorization, info, RpcMethodConfig.FIL_METHOD_MPOOL_PUSH);
		JsonObject result = HttpUtils.result(response);
		JsonObject cid = result.getJsonObject(RESULT_STR);
		return cid.getString("/");
	}

	public boolean isTransactionConfirm(String cid) {
		JsonObject cidObj = new JsonObject();
		cidObj.put("/", cid);
		List<Object> info = new ArrayList<>();
		info.add(cidObj);
		String response = HttpUtils.post(this.url, this.authorization, info,
				RpcMethodConfig.FIL_METHOD_STATE_SEARCH_MSG);
		JsonObject jsonBody = new JsonObject(response);
		try {
			if (jsonBody.has(RESULT_STR)) {
				JsonObject result = jsonBody.getJsonObject(RESULT_STR);
				JsonObject receipt = result.getJsonObject("Receipt");
				int exitCode = receipt.getInt("ExitCode");
				if (exitCode == 0) {
					return true;
				}
			}
		}catch(Exception e) {
			
		}
		return false;
	}

}
