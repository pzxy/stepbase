package com.base.blockchain.filecoin.rpc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.base.blockchain.filecoin.Transaction;
import com.base.blockchain.filecoin.rpc.beans.FilCid;
import com.base.blockchain.filecoin.rpc.beans.FilMessage;
import com.base.blockchain.filecoin.rpc.beans.GasEstimateMessageGasVO;
import com.gitee.magic.context.ConverterEditorUtils;
import com.gitee.magic.core.json.JsonArray;
import com.gitee.magic.core.json.JsonObject;

/**
 * @author start 
 */
public class FilService {

    private static final String RESULT_STR="result";
//    private static final String EXITCODE_STR="exitCode";
	
	private String url;
	private Map<String,String> header;
	
	public FilService(String url,String authorization) {
		this.url=url;
		header=new HashMap<>();
		if(authorization!=null) {
			header.put("Authorization", authorization);
		}
	}
	
    /**
     * 查询指定高度的TipSet
     * @param blockNumber
     * @return
     */
    public List<FilCid> getChainGetTipSetByHeight(Integer blockNumber) {
        List<FilCid> filCids = new ArrayList<>();
        List<Object> info = new ArrayList<>();
        info.add(blockNumber);
        info.add(new ArrayList<>());
        Optional<String> response = FilHttpUtils.post(this.url,header,info, RpcMethodConfig.FIL_METHOD_CHAIN_GET_TIP_SETBYHEIGHT);
        if (response.isPresent()) {
            String body = response.get();
            JsonObject jsonBody = new JsonObject(body);
            if(jsonBody.has(RESULT_STR)) {
            	JsonObject result = jsonBody.getJsonObject(RESULT_STR);
            	Integer height = result.getInt("Height");
                if (Integer.compare(height, blockNumber) == 0) {
                    JsonArray jsonArray =result.getJsonArray("Cids");
                    if (jsonArray.length()>0) {
                        for (Object o : jsonArray) {
                            JsonObject cidObject = new JsonObject(o.toString());
                            FilCid cid = FilCid.builder().cid(cidObject.get("/").toString()).build();
                            filCids.add(cid);
                        }
                    }
                }
            }
        }
        return filCids;
    }

    /**
     * 查询区块内的消息
     * @param cid
     * @return
     */
    public List<FilMessage> getChainGetBlockMessages(String cid) {
        List<FilMessage> filMessages = new ArrayList<>();
        JsonObject cidObj = new JsonObject();
        cidObj.put("/", cid);
        List<Object> info = new ArrayList<>();
        info.add(cidObj);
        Optional<String> response = FilHttpUtils.post(this.url,header,info, RpcMethodConfig.FIL_METHOD_CHAIN_GGET_BLOCK_MESSAGES);
        if (response.isPresent()) {
            String body = response.get();
            JsonObject jsonBody = new JsonObject(body);
            if(jsonBody.has(RESULT_STR)) {
                JsonObject result = jsonBody.getJsonObject(RESULT_STR);
                JsonArray jsonArray =result.getJsonArray("SecpkMessages");
                for(int i=0;i<jsonArray.length();i++) {
                	JsonObject secpkMessagesJson=jsonArray.getJsonObject(i);
                	if(secpkMessagesJson.has("Message")) {
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
                        
                        FilMessage filMessage = FilMessage.builder()
                        		.to(to).from(from).value(value).hash(hash)
                        		.gasFeeCap(gasFeeCap).gasPremium(gasPremium).gasLimit(gasLimit)
                        		.build();
                        filMessages.add(filMessage);
                	}
                }
            }
        }
        return filMessages;
    }
    
    public boolean isTransactionConfirm(String cid) {
        JsonObject cidObj = new JsonObject();
        cidObj.put("/", cid);
        List<Object> info = new ArrayList<>();
        info.add(cidObj);
        Optional<String> response = FilHttpUtils.post(this.url,header,info, RpcMethodConfig.FIL_METHOD_STATE_SEARCH_MSG);
        if (response.isPresent()) {
            String body = response.get();
            JsonObject jsonBody = new JsonObject(body);
            if(jsonBody.has(RESULT_STR)) {
                JsonObject result = jsonBody.getJsonObject(RESULT_STR);
                JsonObject receipt = result.getJsonObject("Receipt");
                int exitCode=receipt.getInt("ExitCode") ;
                if (exitCode == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查询当前链头的区块高度 
     * @return
     */
    public Integer getBlockHeight() {
        Optional<String> response = FilHttpUtils.post(this.url,header,new ArrayList<>(), RpcMethodConfig.FIL_METHOD_CHAINHEAD);
        if (response.isPresent()) {
            String body = response.get();
            JsonObject jsonBody = new JsonObject(body);
            if(jsonBody.has(RESULT_STR)) {
            	JsonObject object = jsonBody.getJsonObject(RESULT_STR);
            	return object.getInt("Height");
            }
        }
        return 0;
    }
    
    /**
     * 获取余额
     * @param address
     * @return
     */
    public BigInteger getBalance(String address) {
    	List<Object> info = new ArrayList<>();
		info.add(address);
		Optional<String> response = FilHttpUtils.post(this.url,header,info, RpcMethodConfig.FIL_METHOD_WALLET_BALANCE);
		JsonObject result=FilHttpUtils.result(response);
		return result.getBigInteger(RESULT_STR);
    }
    
    public Long getNonce(String address) {
    	List<Object> info = new ArrayList<>();
		info.add(address);
		Optional<String> response = FilHttpUtils.post(this.url,header,info, RpcMethodConfig.FIL_METHOD_MPOOL_GET_NONCE);
		JsonObject result=FilHttpUtils.result(response);
		return result.getLong(RESULT_STR);
    }
    
    public GasEstimateMessageGasVO gasEstimateMessageGas(
    		String from,String to,Long nonce,BigInteger value,Long method) {
    	
    	JsonObject message=new JsonObject();
    	message.put("To", to);
    	message.put("From", from);
    	message.put("Nonce", nonce);
    	message.put("Value", value+"");
    	message.put("Method", method);
    	message.put("Params", "");
    	
    	JsonObject message1=new JsonObject();
    	
    	List<Object> info=new ArrayList<>();
    	info.add(message);
    	info.add(message1);
    	info.add(null);
    	
    	Optional<String> response = FilHttpUtils.post(this.url,header,info, RpcMethodConfig.FIL_METHOD_GAS_ESTIMATE_MESSAGE_GAS);
    	JsonObject result=FilHttpUtils.result(response);
    	return ConverterEditorUtils.restoreObject(GasEstimateMessageGasVO.class, result.getJsonObject(RESULT_STR));
    }
    
    public String mpoolPush(Transaction unmsg, String sign) {
    	
    	JsonObject message=new JsonObject();
    	message.put("Version", 0);
    	message.put("To", unmsg.getTo());
    	message.put("From", unmsg.getFrom());
    	message.put("Value", unmsg.getValue()+"");
    	message.put("Nonce", unmsg.getNonce());
    	message.put("GasLimit", unmsg.getGasLimit());
    	message.put("GasFeeCap", unmsg.getGasFeeCap()+"");
    	message.put("GasPremium", unmsg.getGasPremium()+"");
    	message.put("Method", unmsg.getMethod());
    	
    	JsonObject signature=new JsonObject();
    	signature.put("Type", 1);
    	signature.put("Data", sign);
    	
    	JsonObject params=new JsonObject();
    	params.put("Message", message);
    	params.put("Signature", signature);
    	
    	List<Object> info=new ArrayList<>();
    	info.add(params);
    	
    	Optional<String> response = FilHttpUtils.post(this.url,header,info, RpcMethodConfig.FIL_METHOD_MPOOL_PUSH);
    	JsonObject result=FilHttpUtils.result(response);
    	JsonObject cid= result.getJsonObject(RESULT_STR);
    	return cid.getString("/");
    }
    
    ////////////////////////////////

    /**
     * 根据交易hash进行交易确认
     * @param hash
     * @return
     */
//    public int transactionConfirm(String hash) {
//        Optional<String> str = FilHttpUtils.get(FilApiConfig.FILFOX_API_V1_MESSAGE + hash);
//        if (str.isPresent()) {
//            String urlinfo = str.get();
//            JsonObject message =new JsonObject(urlinfo);
//            JsonObject receipt = message.getJsonObject("receipt");
//            if (receipt.getInt(EXITCODE_STR) != 0) {
//                return 0;
//            }
//            Integer height = message.getInt("height");
//            if (height == 0) {
//                return 0;
//            }
////            Integer head = getBlockHeight();
////            if (head - height > 10) {
////                return 1;
////            }
//            return 1;
//        }
//        return 0;
//    }
    
    /**
     * 根据交易hash进行交易确认
     * @param hash
     * @return
     */
//    public int transactionConfirmExt(String hash) {
//    	FilDetail fil=getDetail(hash);
//        if (fil==null) {
//        	return 0;
//        }
//        if (fil.getHeight() == 0) {
//            return 0;
//        }
//        ReceiptVO receipt=fil.getReceipt();
//    	if(receipt==null) {
//    		return 0;
//    	}
//        if (receipt.getExitCode() != 0) {
//            return 0;
//        }
//        return 1;
//    }
    
//    public FilDetail getDetail(String cid) {
//    	Optional<String> response=FilHttpUtils.get(FilApiConfig.FILFOX_API_V1_MESSAGE + cid);
//    	if (response.isPresent()) {
//            String body = response.get();
//            return ConverterEditorUtils.restoreObject(FilDetail.class, new JsonObject(body));
//        }
//    	return null;
//    }
//    
//    public BigDecimal getTxGasFee(String cid) {
//    	FilDetail rs=getDetail(cid);
//    	long gasfee = 0;
//		if(!ObjectUtils.isEmpty(rs)) {
//			if(!ObjectUtils.isEmpty(rs.getFee())) {
//				gasfee = Long.valueOf(rs.getFee().getBaseFeeBurn()) + Long.valueOf(rs.getFee().getOverEstimationBurn()) + Long.valueOf(rs.getFee().getMinerTip());
//			}else {				
//				gasfee = rs.getReceipt().getGasUsed() * (Long.valueOf(rs.getBaseFee()) +  Math.min(Long.valueOf(rs.getGasPremium()), (new BigDecimal(rs.getGasFeeCap()).subtract(new BigDecimal(rs.getBaseFee()))).longValue()));
//			}
//		}
//		return new BigDecimal(gasfee).divide(BigDecimal.TEN.pow(18), 18, RoundingMode.HALF_DOWN).stripTrailingZeros();
//    }
    
}
