package com.base.core.mvc.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.gitee.magic.framework.head.utils.IoUtils;

/**
 * @author  start
 */
public class BodyStringReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {
	
	private final byte[] body;

	public BodyStringReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		body=IoUtils.inputStreamConvertBytes(request.getInputStream(), request.getContentLength());
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		final ByteArrayInputStream bais = new ByteArrayInputStream(body);

		return new ServletInputStream() {

			@Override
			public int read() throws IOException {
				return bais.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}
			
		};
	}
}
