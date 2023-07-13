package com.base.core.mvc.bigdata;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;

import com.base.core.context.utils.LoggerWrapper;
import com.base.core.mvc.business.CommonBusiness;
import com.gitee.magic.context.ConverterEditorUtils;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.json.JsonException;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.core.utils.CheckUtils;
import com.gitee.magic.core.utils.codec.Base64;
import com.gitee.magic.framework.base.rest.HttpRequest;
import com.gitee.magic.framework.base.rest.HttpWrapper;
import com.gitee.magic.framework.head.utils.IoUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @description : 向大数据提供数据
 * @author : Scott
 * @date :2022/10/19
 */
@Slf4j
@Getter
@Setter
public class TzBigData {

	private String url = "https://xc2pl.lipianzong.com/report/log/async";

	public void send(HttpServletRequest request, String projectName, String active) {
		try {
			String contentType = request.getContentType();
			log.info("send big data name : {} active : {} contentType : {}", projectName, active, contentType);
			PostToServerBean serverBean = generatePostToServerBean(request, active, projectName);
			JsonObject msgJson = ConverterEditorUtils.converter(serverBean, JsonObject.class);
			Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						if (copyOfContextMap != null) {
							MDC.setContextMap(copyOfContextMap);
						}
						HttpRequest request = new HttpRequest(url);
						request.setHeader("Content-Type", "application/json;charset=UTF-8");
						request.setBodyContent(msgJson.toString());
						HttpWrapper wrapper = new HttpWrapper();
						String content = wrapper.start(request);
						log.info("send big data result : {}", content);
					} catch (Exception e) {
						log.error("sende big data error : {}"+e.getMessage());
					}
				}
			}).start();
		} catch (JsonException e) {
			//body json解析问题不处理
			log.error("JsonException : {}"+e.getMessage());
		} catch (Exception e) {
			LoggerWrapper.print(e);
		}
	}

	public PostToServerBean generatePostToServerBean(HttpServletRequest request, String active, String projectName) {
		PostToServerBean postToServerBean = new PostToServerBean();
		PostToMsg msgBean = new PostToMsg();
		msgBean.setActiontime(System.currentTimeMillis());
		msgBean.setAppname(projectName);
		msgBean.setCategory("interface");
		msgBean.setEvent(request.getRequestURI());
		msgBean.setIp(getIpAddr(request));
		msgBean.setOstype(getOsAndBrowserInfo(request));
		ProPertiesInfo info = new ProPertiesInfo();
		info.setAccount(request.getHeader("account"));
		if (CommonBusiness.isContentType(request.getContentType(), HttpWrapper.CONTENTTYPE_JSON)) {
			if (request.getContentLength() > 0) {
				String body;
				try {
					body = new String(IoUtils.inputStreamConvertBytes(request.getInputStream(), -1));
				} catch (IOException e) {
					throw new ApplicationException(e);
				}
				//争对base64单独处理转json
				if(CheckUtils.isBase64(body)) {
					body=new String(Base64.decode(body));
				}
				info.setData(new JsonObject(body));
			}
		}
		info.setEvn(active);
		info.setHref(request.getRequestURL().toString());
		info.setVisitor_id(request.getHeader("visitorId"));
		JsonObject json = ConverterEditorUtils.converter(info, JsonObject.class);
		msgBean.setProperties(json);
		JsonObject msg = ConverterEditorUtils.converter(msgBean, JsonObject.class);
		String msgStr = msg.toString();
		postToServerBean.setTopic("tz_block");
		postToServerBean.setMsg(msgStr);
		postToServerBean.setSign(this.getSignFromMsg(msgStr));
		return postToServerBean;
	}

	public String getSignFromMsg(String msgStr) {
		return msgStr.length() > 128
				? DtMd5Utils.sha256(msgStr.substring(0, 64) + msgStr.substring(msgStr.length() - 64))
				: DtMd5Utils.sha256(msgStr);
	}

	public String getIpAddr(HttpServletRequest request) {
		String ipAddress = null;
		try {
			ipAddress = request.getHeader("x-forwarded-for");
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getRemoteAddr();
				if (ipAddress.equals("127.0.0.1")) {
					// 根据网卡取本机配置的IP
					InetAddress inet = null;
					try {
						inet = InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					ipAddress = inet.getHostAddress();
				}
			}
			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			if (ipAddress != null && ipAddress.length() > 15) {
				// = 15
				if (ipAddress.indexOf(",") > 0) {
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
				}
			}
		} catch (Exception e) {
			ipAddress = "";
		}
		// ipAddress = this.getRequest().getRemoteAddr();

		return ipAddress;
	}

	/**
	 * 获取操作系统,浏览器及浏览器版本信息
	 * 
	 * @param request
	 * @return
	 */
	public static String getOsAndBrowserInfo(HttpServletRequest request) {
		return request.getHeader("User-Agent");
//		String userAgent = browserDetails;
//		if(StringUtils.isEmpty(userAgent)) {
//			return "";
//		}
//		String user = userAgent.toLowerCase();
//
//		String os = "";
//		String browser = "";
//
//		// =================OS Info=======================
//		if (userAgent.toLowerCase().indexOf("windows") >= 0) {
//			os = "Windows";
//		} else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
//			os = "Mac";
//		} else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
//			os = "Unix";
//		} else if (userAgent.toLowerCase().indexOf("android") >= 0) {
//			os = "Android";
//		} else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
//			os = "IPhone";
//		} else {
//			os = "UnKnown, More-Info: " + userAgent;
//		}
//		// ===============Browser===========================
//		if (user.contains("edge")) {
//			browser = (userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
//		} else if (user.contains("msie")) {
//			String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
//			browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
//		} else if (user.contains("safari") && user.contains("version")) {
//			browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-"
//					+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
//		} else if (user.contains("opr") || user.contains("opera")) {
//			if (user.contains("opera")) {
//				browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-"
//						+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
//			} else if (user.contains("opr")) {
//				browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
//						.replace("OPR", "Opera");
//			}
//
//		} else if (user.contains("chrome")) {
//			browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
//		} else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)
//				|| (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1)
//				|| (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
//			browser = "Netscape-?";
//
//		} else if (user.contains("firefox")) {
//			browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
//		} else if (user.contains("rv")) {
//			String IEVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
//			browser = "IE" + IEVersion.substring(0, IEVersion.length() - 1);
//		} else {
//			browser = "UnKnown, More-Info: " + userAgent;
//		}
//
//		return os + " --- " + browser;
	}
}
