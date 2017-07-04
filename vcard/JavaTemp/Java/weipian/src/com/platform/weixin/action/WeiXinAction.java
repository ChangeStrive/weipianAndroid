package com.platform.weixin.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.base.BaseAction;
import com.platform.sys.context.AppContextImpl;
import com.platform.util.StringUtil;
import com.platform.weixin.model.WeiXinKeywordReply;
import com.platform.weixin.model.WeiXinUser;
import com.platform.weixin.model.WeiXinWelcomes;
import com.platform.weixin.service.WeiXinKeywordReplyService;
import com.platform.weixin.service.WeiXinUserService;
import com.platform.weixin.service.WeiXinWelcomesService;
import com.platform.weixin.util.WeiXinMessage;
import com.platform.weixin.util.WeiXinUtils;

@Controller
@RequestMapping("/WeiXinAction")
public class WeiXinAction  extends BaseAction{

	final String TOKEN = "weixin";
	
	/**
	 * 获得OpendId入口
	 * @param request
	 * @param response
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("/getOpenId")
	public String getOpenId(HttpServletRequest request,HttpServletResponse response) throws ClientProtocolException, IOException{
		String  backUrl=request.getParameter("backUrl");
		String url=WeiXinUtils.getOpenId(request, backUrl);
		return "redirect:"+url;
	}
	
	/**
	 * 获得OpendId入口
	 * @param request
	 * @param response
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("/sendMessage")
	public void sendMessage(HttpServletRequest request,HttpServletResponse response) throws ClientProtocolException, IOException{
		String  fdOpenId=request.getParameter("fdOpenId");
		WeiXinUtils.sendMessage(fdOpenId, "微信推送测试");
	}
	
	/**
	 * 把openId传回去
	 * @param request
	 * @param response
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("/toBackUrl")
	public String toBackUrl(HttpServletRequest request,HttpServletResponse response) throws ClientProtocolException, IOException{
		String  code=request.getParameter("code");
		String  state=request.getParameter("state");
		JSONObject result=WeiXinUtils.getAccessTokenByCode(code);
		String fdOppenId=result.getString("openid");
		saveOpenId(request, fdOppenId);//保存在session中
		String url=WeiXinUtils.getWeiXinUrl(state);
		if(result.containsKey("access_token")){
			String accessToken=result.getString("access_token");
			JSONObject p=WeiXinUtils.getUserMessage(accessToken,fdOppenId);
			String nickname=p.getString("nickname");
			String headimgurl=p.getString("headimgurl");
			String sex=p.getString("sex");//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
			System.out.println("获取个人信息："+nickname);
			saveWxUserName(request, nickname);
			saveWxUserHeader(request, headimgurl);
			saveWxUserSex(request, sex);
		}
		return "redirect:"+url;
	}
	
	/**
	 * 链接多客服
	 * @param request
	 * @param response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("/connectChat")
	public void connectChat(HttpServletRequest request,HttpServletResponse response) throws ClientProtocolException, IOException{
		String fdServieId=request.getParameter("fdServieId");
		String fdOpenId=getOpenId(request);
		String content="很高兴为您服务！请问有什么需要帮助的";
		WeiXinUtils.sendMessage(fdOpenId, content);
		WeiXinUtils.connectChat(fdServieId, fdOpenId, "有客户接入");
	}
	@RequestMapping("/valid")
	public void valid(HttpServletRequest request,HttpServletResponse response) throws ClientProtocolException, IllegalStateException, IOException {
		String echostr = request.getParameter("echostr");
		if (null == echostr || echostr.isEmpty()) {
			responseMsg(request,response);
		} else {
			if (this.checkSignature(request)) {
				this.print(response,echostr);
			} else {
				this.print(response,"error");
			}
		}
	}
	
	// 自动回复内容
	public void responseMsg(HttpServletRequest request,HttpServletResponse response) throws ClientProtocolException, IllegalStateException, IOException {
		String postStr = null;
		try {
			postStr = this.readStreamParameter(request.getInputStream());
			System.out.println(postStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != postStr && !postStr.isEmpty()) {
			Document document = null;
			try {
				document = DocumentHelper.parseText(postStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null == document) {
				this.print(response,"");
				return;
			}
			Element root = document.getRootElement();
			String fromUsername = root.elementText("FromUserName");
			String toUsername = root.elementText("ToUserName");
			String Event=root.elementText("Event");
			String EventKey=root.elementText("EventKey");
			String EventContent = root.elementText("Content");
			Element ScanCodeInfo = root.element("ScanCodeInfo");
			String ScanResult="";
			if(ScanCodeInfo!=null){
				ScanResult = ScanCodeInfo.elementText("ScanResult");
			}
			String time = new Date().getTime() + "";
			
			if(Event!=null&&Event.equals("subscribe")){
				System.out.println("首次关注回复");
				// 保存关注用户
				saveWeiXinUser(fromUsername);
				// 首次关注回复
				sendAttentionMessage(request,response,fromUsername,toUsername,time);
			}else if(Event!=null&&(Event.equals("scancode_push")||Event.equals("scancode_waitmsg"))){
				//扫码推事件的事件推送
				if(EventKey.equals("saomiao")){}
			}else{
				// 关键词自动应答
				if(StringUtil.isNotNull(EventKey)){
					EventContent=EventKey;
				}
				if(StringUtil.isNotNull(EventContent)){
					if(EventContent.indexOf("kefu#")!=-1){
						String contents="很高兴为您服务！请问有什么需要帮助的";
						WeiXinUtils.sendMessage(fromUsername, contents);
						sendToService(response,fromUsername,toUsername,time);
						WeiXinUtils.connectChat(EventContent.split("kefu#")[1], fromUsername, "有客户进入");
						System.out.println("客服接入");
					}else{
						System.out.println("关键词自动应答");
						sendKeywordReplyMessage(request,response,fromUsername,toUsername,time,EventContent);
					}
				}else{
					this.print(response,"");
				}
				
			}
			
			if(Event!=null&&Event.equals("unsubscribe")) {
				WeiXinUserService userSrv = AppContextImpl.getInstance().getBean(WeiXinUserService.class);
				userSrv.deleteFormUserOpenId(fromUsername);
			}
		} else {
			this.print(response,"");
		}
	}
	
	/**
	 * 保存关注用户
	 * @param fromUsername 接收方帐号
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws ClientProtocolException 
	 */
	public static void saveWeiXinUser(String fromUsername) throws ClientProtocolException, IllegalStateException, IOException {
		JSONObject access_token = WeiXinUtils.getAccessToken();
		if(access_token.containsKey("access_token")){
			String accessToken=access_token.getString("access_token");
			JSONObject userInfo = WeiXinUtils.getPersonMessage(accessToken,fromUsername);
			if(userInfo.containsKey("openid")) {
				String subscribe = userInfo.getString("subscribe");
				/*
				 * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
				 */
				if(subscribe.equals("1")) {
					WeiXinUserService userSrv = AppContextImpl.getInstance().getBean(WeiXinUserService.class);
					String fdOpenId = userInfo.getString("openid");
					boolean falg = userSrv.getCheckByOpenId(fdOpenId);
					if(falg){
						String fdNickName = userInfo.getString("nickname");
						String fdSex = userInfo.getString("sex");
						String fdCity = userInfo.getString("city");
						String fdCountry = userInfo.getString("country");
						String fdProvince = userInfo.getString("province");
						String fdHeadImgUrl = userInfo.getString("headimgurl");
						String fdSubscribeTime = userInfo.getString("subscribe_time");
						WeiXinUser user = new WeiXinUser();
						user.setFdOpenId(fdOpenId);
						user.setFdNickName(fdNickName);
						user.setFdSex(fdSex);
						user.setFdCity(fdCity);
						user.setFdCountry(fdCountry);
						user.setFdProvince(fdProvince);
						user.setFdHeadImgUrl(fdHeadImgUrl);
						user.setFdSubscribeTime(fdSubscribeTime);
						userSrv.save(user);
					}
				}
			}
		}
	}

	/**
	 * 关键词自动应答
	 * @param fromUsername 开发者微信号
	 * @param toUsername 接收方帐号
	 * @param time 消息创建时间戳
	 * @param eventContent 回复的消息内容
	 */
	private void sendKeywordReplyMessage(HttpServletRequest request,HttpServletResponse response,String fromUsername, String toUsername, String time, String eventContent) {
		WeiXinKeywordReplyService keywordSrv = AppContextImpl.getInstance().getBean(WeiXinKeywordReplyService.class);
		List<WeiXinKeywordReply> list = keywordSrv.getByKeyword(eventContent);
		List<WeiXinMessage> s=new ArrayList();
		if(list != null&&list.size()>0){
			String message = "";
			for (int i = 0; i < list.size(); i++) {
				WeiXinKeywordReply item = list.get(i);
				if(item.getFdType().equals("0")){
					sendText(response,fromUsername,toUsername,time,item.getFdTitle());
					return;
				}else{
					String fdPicUrl = StringUtil.getCurrentUrl(request, "SysFileAction/downFile?path="+item.getFdPicUrl());
					String fdTitle = item.getFdTitle();
					message+=getTextAndImg(fdTitle,item.getFdDesc(),fdPicUrl,item.getFdUrl());
					
					WeiXinMessage r=new WeiXinMessage();
					r.setTitle(fdTitle);
					r.setPicurl(fdPicUrl);
					r.setDescription(item.getFdDesc());
					r.setUrl(item.getFdUrl());
					s.add(r);
				}
			}
			
			/*try {
				//主动推送图文测试
				if(s.size()>0){
					WeiXinUtils.sendMessage(fromUsername, s);
					return ;
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			sendTextAndImg(response,fromUsername,toUsername,time,list.size(),message);
		}
	}
	
	/**
	 * 发送关注信息
	 * @param fromUsername 开发者微信号
	 * @param toUsername 接收方帐号
	 * @param time 消息创建时间戳
	 */
	public void sendAttentionMessage(HttpServletRequest request,HttpServletResponse response,String fromUsername,String toUsername,String time){
		WeiXinWelcomesService service = AppContextImpl.getInstance().getBean(WeiXinWelcomesService.class);
		List<WeiXinWelcomes> list = service.getListByDefault();
		if(list!=null&&list.size()>0){
			String message="";
			String localAddr = request.getLocalAddr();
			int locatPort = request.getServerPort();
			String urls = request.getScheme()+"://"+ localAddr;
			if(locatPort != 80){
				urls+=":"+locatPort;
			}
			WeiXinWelcomes welcomes = list.get(0);
			if(welcomes.getFdType().equals("0")){
				sendText(response,fromUsername,toUsername,time,welcomes.getFdTitle());
			} else {
				for (int i = 0; i < list.size(); i++) {
					WeiXinWelcomes item = list.get(i);
					String fdPicUrl = urls+"/SysFileAction!downImg.action?path="+item.getFdPicUrl();
					message+=getTextAndImg(item.getFdTitle(),"",fdPicUrl,item.getFdUrl());
				}
				sendTextAndImg(response,fromUsername,toUsername,time,list.size(),message);
			}
			
		}
	}

	// 微信接口验证
	public boolean checkSignature(HttpServletRequest request) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String token = TOKEN;
		String[] tmpArr = { token, timestamp, nonce };
		Arrays.sort(tmpArr);
		String tmpStr = this.ArrayToString(tmpArr);
		tmpStr = this.SHA1Encode(tmpStr);
		if (tmpStr.equalsIgnoreCase(signature)) {
			return true;
		} else {
			return false;
		}
	}

	// 向请求端发送返回数据
	public void print(HttpServletResponse response,String content) {
		try {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().print(content);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {

		}
	}

	// 数组转字符串
	public String ArrayToString(String[] arr) {
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			bf.append(arr[i]);
		}
		return bf.toString();
	}

	// sha1加密
	public String SHA1Encode(String sourceString) {
		String resultString = null;
		try {
			resultString = new String(sourceString);
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			resultString = byte2hexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}

	public final String byte2hexString(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString().toUpperCase();
	}

	// 从输入流读取post参数
	public String readStreamParameter(ServletInputStream in) {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer.toString();
	}
	
	public String getTextAndImg(String title,String desc,String picUrl,String url){
		String textTpl = "<item>"
			+"<Title><![CDATA[%1$s]]></Title>" 
			+"<Description><![CDATA[%2$s]]></Description>"
			+"<PicUrl><![CDATA[%3$s]]></PicUrl>"
			+"<Url><![CDATA[%4$s]]></Url>"
			+"</item>" ;
		String resultStr = textTpl.format(textTpl,title,desc,picUrl,url);
		return resultStr;
		
	}
	
	public String getTextAndImg(String desc,String picUrl,String url){
		String textTpl = "<item>"
			+"<Description><![CDATA[%1$s]]></Description>"
			+"<PicUrl><![CDATA[%2$s]]></PicUrl>"
			+"<Url><![CDATA[%3$s]]></Url>"
			+"</item>" ;
		String resultStr = textTpl.format(textTpl,desc,picUrl,url);
		return resultStr;
		
	}
	
	/**
	 * 发送图文信息
	 */
	public void sendTextAndImg(HttpServletResponse response,String fromUsername,String toUsername,String time,int count,String fdContent){
		String textTpl = "<xml>"
		+"<ToUserName><![CDATA[%1$s]]></ToUserName>"
		+"<FromUserName><![CDATA[%2$s]]></FromUserName>"
		+"<CreateTime>%3$s</CreateTime>"
		+"<MsgType><![CDATA[news]]></MsgType>"
		+"<ArticleCount>%4$s</ArticleCount>"
		+"<Articles>"
		+"%5$s"
		+"</Articles>"
		+"</xml>" ;
		String resultStr = textTpl.format(textTpl, fromUsername,toUsername, time,count,fdContent);
		System.out.println(resultStr);
		this.print(response,resultStr);
	}
	/**
	 * 发送文本信息
	 */
	public void sendText(HttpServletResponse response,String fromUsername,String toUsername,String time,String contentStr){
		String textTpl = "<xml>"
			+ "<ToUserName><![CDATA[%1$s]]></ToUserName>"
			+ "<FromUserName><![CDATA[%2$s]]></FromUserName>"
			+ "<CreateTime>%3$s</CreateTime>"
			+ "<MsgType><![CDATA[%4$s]]></MsgType>"
			+ "<Content><![CDATA[%5$s]]></Content>"
			+ "<FuncFlag>0</FuncFlag>" + "</xml>";

		String msgType = "text";
		String resultStr = textTpl.format(textTpl, fromUsername,
				toUsername, time, msgType, contentStr);
		this.print(response,resultStr);
	}
	
	/**
	 * 发送给客服
	 */
	public void sendToService(HttpServletResponse response,String fromUsername,String toUsername,String time){
		String textTpl = "<xml>"
		+"<ToUserName><![CDATA[%1$s]]></ToUserName>"
		+"<FromUserName><![CDATA[%2$s]]></FromUserName>"
		+"<CreateTime>%3$s</CreateTime>"
		+"<MsgType><![CDATA[transfer_customer_service]]></MsgType>"
		+"</xml>";
		String msgType = "text";
		String resultStr = textTpl.format(textTpl, fromUsername,
				toUsername, time);
		this.print(response,resultStr);
	}
	
	/**
	 * 发送给客服
	 */
	public void sendToService(HttpServletResponse response,String fromUsername,String toUsername,String time,String KfAccount){
		String textTpl = "<xml>"
		+"<ToUserName><![CDATA[%1$s]]></ToUserName>"
		+"<FromUserName><![CDATA[%2$s]]></FromUserName>"
		+"<CreateTime>%3$s</CreateTime>"
		+"<MsgType><![CDATA[transfer_customer_service]]></MsgType>"
		+"<TransInfo>"
		+"<KfAccount>%4$s</KfAccount>"
		+"</TransInfo>"
		+"</xml>";
		String msgType = "text";
		String resultStr = textTpl.format(textTpl, fromUsername,
				toUsername, time,KfAccount);
		this.print(response,resultStr);
	}
}
