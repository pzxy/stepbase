package com.base.blockchain.filecoin.rpc;

/**
 * http://cw.hubwiz.com/card/c/filecoin-lotus-rpc/
 * @author start
 */
public interface RpcMethodConfig {

    /**
     * Chain/链相关API
     */
    /**
     * 返回当前TipSet
     */
    String FIL_METHOD_CHAINHEAD = "Filecoin.ChainHead";
    /**
     * 读取指定高度的TipSet
     */
    String FIL_METHOD_CHAIN_GET_TIP_SETBYHEIGHT = "Filecoin.ChainGetTipSetByHeight";
    /**
     * 读取区块内消息
     */
    String FIL_METHOD_CHAIN_GGET_BLOCK_MESSAGES = "Filecoin.ChainGetBlockMessages";
    /**
     * 返回指定地址的下一随机值
     */
    String FIL_METHOD_MPOOL_GET_NONCE = "Filecoin.MpoolGetNonce";
    /**
     * 将指定签名消息推入内存池
     */
    String FIL_METHOD_MPOOL_PUSH = "Filecoin.MpoolPush";
    /**
     * 估算消息Gas用量
     */
	String FIL_METHOD_GAS_ESTIMATE_MESSAGE_GAS = "Filecoin.GasEstimateMessageGas";
    /**
     * Wallet/钱包相关API,查询钱包余额
     */
    String FIL_METHOD_WALLET_BALANCE = "Filecoin.WalletBalance";
    
    /**
     * Filecoin.StateSearchMsg 搜索链上消息
     */
    String FIL_METHOD_STATE_SEARCH_MSG = "Filecoin.StateSearchMsg";
	
//	String FILFOX_API_V1_MESSAGE = "https://filfox.info/api/v1/message/";
    
}
