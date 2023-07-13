package com.base.component.alibaba.dingtalk.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.base.core.context.annotation.Cache;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMediaUploadRequest;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.request.OapiRobotSendRequest.At;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.gitee.magic.core.exception.ApplicationException;
import com.taobao.api.ApiException;
import com.taobao.api.FileItem;

/**
 * <pre>
 * 登录钉钉开发者平台
 * 创建企业内部应用可获取appkey、appsecret
 * 在开发管理中配置服务器出口IP
 * chatId获取方法
 * 1、打开 https://open-dev.dingtalk.com/apiExplorer?spm=ding_open_doc.document.0.0.afb839b7W85NCP#/jsapi?api=biz.chat.chooseConversationByCorpId 选择所属企业后扫码
 * 2、搜索 biz.chat.chooseConversationByCorpId根据corpId选择会话(2.6新增)
 * 3、corpId可登录钉钉开发者后台查看、filterNotOwnerGroup可以设置为false表示只看自己创建的群
 * 4、点击执行在APP上选择对应的群就可以看到chatId
 * </pre>
 * 
 * @author start
 *
 */
@Service
public class DingTalkService {
	
	@Value("${dingtalk.appkey}")
	private String appkey;
	@Value("${dingtalk.appsecret}")
	private String appsecret;

	/**
     * 获取Token有效期 7200m
     *
     * @return
     * @throws Exception
     */
	@Cache(key="DingTalk_getAccessToken",expire = 7000)
    public String getAccessToken() {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appkey);
        request.setAppsecret(appsecret);
        request.setHttpMethod("GET");
        OapiGettokenResponse response;
        try {
            response = client.execute(request);
        } catch (ApiException e) {
            throw new ApplicationException(e);
        }
        return response.getAccessToken();
    }

    public String uploadFile(String accessToken, String fileName,InputStream inputStream) {
        FileItem fileItem=new FileItem(fileName, inputStream);
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/media/upload");
        OapiMediaUploadRequest request = new OapiMediaUploadRequest();
        request.setType("file");
        request.setMedia(fileItem);
        OapiMediaUploadResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            throw new ApplicationException(e);
        }
        return response.getMediaId();
    }

    public void sendFile(String accessToken, String fileName, String content,String chatId) {
    	InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        String mediaId=uploadFile(accessToken, fileName + System.currentTimeMillis() + ".txt",inputStream);
        sendFile(accessToken,mediaId,chatId);
    }
    
    public void sendFile(String accessToken,String mediaId,String chatId) {
    	OapiChatSendRequest.Msg msg = new OapiChatSendRequest.Msg();
        msg.setMsgtype("file");
        OapiChatSendRequest.File file = new OapiChatSendRequest.File();
        file.setMediaId(mediaId);
        msg.setFile(file);
        send(accessToken,chatId,msg);
    }
    
    public void sendText(String accessToken, String content,String chatId) {
    	 OapiChatSendRequest.Msg msg = new OapiChatSendRequest.Msg();
         msg.setMsgtype("text");
         OapiChatSendRequest.Text text = new OapiChatSendRequest.Text();
         text.setContent(content);
         msg.setText(text);
         send(accessToken,chatId,msg);
    }
    
    public void send(String accessToken,String chatId,OapiChatSendRequest.Msg msg) {
    	DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/send");
        OapiChatSendRequest request = new OapiChatSendRequest();
        request.setChatid(chatId);
        request.setMsg(msg);
        try {
            client.execute(request, accessToken);
        } catch (ApiException e) {
            throw new ApplicationException(e);
        }
    }
    
    public void sendRobot(String accessToken,String tag, String content,List<String> atMobiles) {
		DingTalkClient client = new DefaultDingTalkClient(
				"https://oapi.dingtalk.com/robot/send?access_token=" + accessToken);
		StringBuilder message = new StringBuilder();
		message.append("【" + tag + "】");
		message.append(content);
		OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
		text.setContent(message.toString());
		OapiRobotSendRequest request = new OapiRobotSendRequest();
		request.setMsgtype("text");
		if(!CollectionUtils.isEmpty(atMobiles)) {
			At at=new At();
			at.setAtMobiles(atMobiles);
			request.setAt(at);
		}
		request.setText(text);
		try {
			client.execute(request);
		} catch (ApiException e) {
            throw new ApplicationException(e);
		}
	}
    
    public static void main(String[] args) throws Exception {
        DingTalkService dt = new DingTalkService();
    	dt.appkey = "dinggftj0gieanb240sx";
    	dt.appsecret = "b4Jc7mU8JyX7qQM5yM5yKc_Diit4KrPmuJUv6esm26m70Yar2LImyDYBoFXLppn_";
        String accessToken = dt.getAccessToken();
        String defaultChatId="chat01ffd4ce45dfc8d7d2066bb683ab807a";
        dt.sendText(accessToken, "这是测试内容",defaultChatId);
//        dt.sendFile(accessToken, "Bridge", "这是测试内容",defaultChatId);
    }
	
}
