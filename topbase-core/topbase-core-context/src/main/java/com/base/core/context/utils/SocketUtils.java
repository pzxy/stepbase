package com.base.core.context.utils;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.framework.head.utils.IoUtils;

/**
 * 
 * @author start
 *
 */
public class SocketUtils {

	public static String send(String ip,int port,String payload) {
		try (Socket socket = new Socket(ip, port);
				BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));) {
			bufferedWriter.write(payload);
			bufferedWriter.flush();
			return new String(IoUtils.inputStreamConvertBytes(socket.getInputStream(), -1));
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

}
