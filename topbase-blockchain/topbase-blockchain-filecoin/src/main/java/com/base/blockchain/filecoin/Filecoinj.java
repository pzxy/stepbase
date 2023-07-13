package com.base.blockchain.filecoin;

import com.base.blockchain.filecoin.rpc.RpcService;

/**
 * 
 * @author start
 *
 */
public class Filecoinj {

	public static Filecoinj build(String url, String authorization) {
		RpcService rpcService=new RpcService(url,authorization);
		Filecoinj filecoinj=new Filecoinj();
		filecoinj.setRpcService(rpcService);
		return filecoinj;
    }
	
	private RpcService rpcService;

	public RpcService getRpcService() {
		return rpcService;
	}

	public void setRpcService(RpcService rpcService) {
		this.rpcService = rpcService;
	}
	
}
