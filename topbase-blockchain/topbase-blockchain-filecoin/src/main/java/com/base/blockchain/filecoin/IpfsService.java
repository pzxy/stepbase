package com.base.blockchain.filecoin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.gitee.magic.core.exception.ApplicationException;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

/**
 * @author start
 */
public class IpfsService {

	/**
	 * API server listening on /ip4/127.0.0.1/tcp/5001
	 */
	private IPFS ipfs;
	
	public IpfsService(IPFS ipfs) {
		this.ipfs=ipfs;
	}

	public String putObject(File file) {
		NamedStreamable.FileWrapper fileWrapper = new NamedStreamable.FileWrapper(file);
		MerkleNode addResult;
		try {
			addResult = ipfs.add(fileWrapper).get(0);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		return addResult.hash.toString();
	}

	public String putObject(InputStream inputStream) {
		NamedStreamable.InputStreamWrapper fileWrapper = new NamedStreamable.InputStreamWrapper(inputStream);
		MerkleNode addResult;
		try {
			addResult = ipfs.add(fileWrapper).get(0);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		return addResult.hash.toString();
	}

	public void deleteObject(String key) {
		try {
			ipfs.pin.rm(Multihash.fromBase58(key));
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}
	
	public byte[] getObject(String key) {
		try {
			return ipfs.cat(Multihash.fromBase58(key));
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}

	public void getObject(String key, File file) {
		byte[] data = getObject(key);
		
		if (data != null && data.length > 0) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				fos.write(data);
				fos.flush();
			} catch (IOException e) {
				throw new ApplicationException(e);
			} finally {
				try {
					fos.close();
				} catch (IOException e) {
					throw new ApplicationException(e);
				}
			}
		}
	}

    
    public static void main(String[] args) {
//    	IPFS ipfs=new IPFS("/ip4/127.0.0.1/tcp/5001");
    	IPFS ipfs=new IPFS("/ip4/127.0.0.1/tcp/5001");
    	IpfsService ipf=new IpfsService(ipfs);
    	File f=new File("/Users/start/Desktop/first.txt");
    	ipf.putObject(f);
//    	ipf.putObject("/Users/start/Desktop/115_1670512101.mp4");
//    	ipf.getObject("QmW3hw52xd8HfEiMP5czL5eWSkb1esqwZmWAca6ixK41Mv", "/Users/start/Desktop/1.txt");
	}

}
