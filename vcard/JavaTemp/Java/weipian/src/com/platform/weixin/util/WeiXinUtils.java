package com.platform.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.platform.sys.context.AppContextImpl;
import com.platform.util.StringUtil;
import com.platform.weixin.model.WeiXinConfig;
import com.platform.weixin.model.WeiXinUrl;
import com.platform.weixin.service.WeiXinConfigService;
import com.platform.weixin.service.WeiXinUrlService;

public class WeiXinUtils {
	
	/**
	 * 获得微信OpenId
	 * @param request
	 * @param backUrl 回调网址
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String getOpenId(HttpServletRequest request,String backUrl) throws UnsupportedEncodingException{
		WeiXinConfigService configSrv = AppContextImpl.getInstance().getBean(WeiXinConfigService.class);
		WeiXinConfig config = configSrv.get();
		String appId = config.getFdAppId();
		//不需要获取用户信息
		//String baseUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state="+backUrl+"#wechat_redirect";
		//需要获取用户信息
		WeiXinUrl item=WeiXinUtils.saveUrl(backUrl);
		String baseUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state="+item.getFdId()+"#wechat_redirect";
		String url=baseUrl.replace("REDIRECT_URI",StringUtil.getFormatUrl2(request, "WeiXinAction/toBackUrl"));
		return url;
	}
	
	/**
	 * 保存跳转路径
	 * @param fdOpenId
	 * @return
	 */
	public static WeiXinUrl saveUrl(String fdUrl){
		WeiXinUrlService service = AppContextImpl.getInstance().getBean(WeiXinUrlService.class);
		return service.saveUrl(fdUrl);
	} 

	public static String convertStreamToString(InputStream is){    
		try {
	         byte[] b = new byte[1024];
	         String res = "";
	         if (is == null) {
	        	 return "";
	         }
	         
	         int bytesRead = 0;
	         while (true) {
	             bytesRead = is.read(b, 0, 1024); // return final read bytes counts
	             if (bytesRead == -1) {// end of InputStream
	                    return res;
	             }
	             res += new String(b, 0, bytesRead, "utf8"); // convert to string using bytes
	          }
	      } catch (Exception e) {
	            e.printStackTrace();
	            System.out.print("Exception: " + e);
	            return "";
	      }
    }
	
	/**
	 * 获得对应的key
	 * @param appid
	 * @param secret
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONObject getAccessToken(String appid,String secret) throws ClientProtocolException, IOException{
		HttpClient httpclient = new DefaultHttpClient();
	    // 创建Get方法实例   
		String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
        HttpGet httpgets = new HttpGet(url);  
        HttpResponse response = httpclient.execute(httpgets);  
        HttpEntity entity = response.getEntity();  
        if (entity != null) {  
            InputStream instreams = entity.getContent();
            String str = convertStreamToString(instreams);
            instreams.close();
            System.out.println(str);
            httpgets.abort(); 
            if(str==null||str.equals("")){
            	return null;
            }
            JSONObject result = JSONObject.fromObject(str);
            return result;
        }
        return null;
	}
	
	/**
	 * 获取配置信息
	 * @return
	 */
	public static WeiXinConfig getWeiXinConfig() {
		WeiXinConfigService configSrv = AppContextImpl.getInstance().getBean(WeiXinConfigService.class);
		WeiXinConfig config = configSrv.get();
		return config;
	}
	
	public static JSONObject getAccessToken() throws ClientProtocolException, IOException, IllegalStateException{
		WeiXinConfig config = getWeiXinConfig();
		String appId = config.getFdAppId();
		String secret = config.getFdAppSecret();
		HttpClient httpclient = new DefaultHttpClient();
		String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+secret;
		HttpGet httpgets = new HttpGet(url);  
		HttpResponse response = httpclient.execute(httpgets);  
        HttpEntity entity = response.getEntity();  
        if (entity != null) {  
            InputStream instreams = entity.getContent();  
            String str = convertStreamToString(instreams);
            instreams.close();
            System.out.println(str);
            httpgets.abort(); 
            JSONObject result = JSONObject.fromObject(str);
            return result;
        }
        return null;
	}

	/**
	 * 获得对应的key
	 * @param appid
	 * @param secret
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONObject getAccessTokenByCode(String code) throws ClientProtocolException, IOException{
		WeiXinConfigService configSrv = AppContextImpl.getInstance().getBean(WeiXinConfigService.class);
		WeiXinConfig config = configSrv.get();
		String appId = config.getFdAppId();
		String secret = config.getFdAppSecret();
		return getAccessTokenByCode(appId, secret,code);
	}
	
	/**
	 * 获得对应的key
	 * @param appid
	 * @param secret
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONObject getAccessTokenByCode(String appid,String secret,String code) throws ClientProtocolException, IOException{
		HttpClient httpclient = new DefaultHttpClient();
	    // 创建Get方法实例   
		String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
		HttpGet httpgets = new HttpGet(url);  
		HttpResponse response = httpclient.execute(httpgets);  
        HttpEntity entity = response.getEntity();  
        if (entity != null) {  
            InputStream instreams = entity.getContent();  
            String str = convertStreamToString(instreams);
            instreams.close();
            System.out.println(str);
            httpgets.abort(); 
            JSONObject result = JSONObject.fromObject(str);
            return result;
        }
        return null;
	}
	
	/**
	 * 获得关注列表
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static JSONObject getAttendPersonList(String access_token) throws ClientProtocolException, IOException{
		HttpClient httpclient = new DefaultHttpClient();
	    // 创建Get方法实例   
		String url="https://api.weixin.qq.com/cgi-bin/user/get?access_token="+access_token;
        HttpGet httpgets = new HttpGet(url);  
        HttpResponse response = httpclient.execute(httpgets);  
        HttpEntity entity = response.getEntity();  
        if (entity != null) {  
            InputStream instreams = entity.getContent();  
            String str = convertStreamToString(instreams);
            instreams.close();
            httpgets.abort(); 
            JSONObject result = JSONObject.fromObject(str);
            if(result.has("errcode")){
            	return null;
            }
            return result;
        }
        return null;
	}
	/**
	 * 获得个人信息
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static JSONObject getPersonMessage(String access_token,String openid) throws ClientProtocolException, IOException{
		HttpClient httpclient = new DefaultHttpClient();
	    // 创建Get方法实例   
		String url="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
        HttpGet httpgets = new HttpGet(url);  
        HttpResponse response = httpclient.execute(httpgets);  
        HttpEntity entity = response.getEntity();  
        if (entity != null) {  
            InputStream instreams = entity.getContent();  
            String str = convertStreamToString(instreams);
            System.out.println(str);
            instreams.close();
            httpgets.abort(); 
            JSONObject result = JSONObject.fromObject(str);
            if(result.has("errcode")){
            	return null;
            }
            return result;
        }
        return null;
	}
	
	/**
	 * 获得获得授权用户信息
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static JSONObject getUserMessage(String access_token,String openid) throws ClientProtocolException, IOException{
		HttpClient httpclient = new DefaultHttpClient();
	    // 创建Get方法实例   
		String url="https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
        HttpGet httpgets = new HttpGet(url);  
        HttpResponse response = httpclient.execute(httpgets);  
        HttpEntity entity = response.getEntity();  
        if (entity != null) {  
            InputStream instreams = entity.getContent();  
            String str = convertStreamToString(instreams);
            System.out.println(str);
            instreams.close();
            httpgets.abort(); 
            JSONObject result = JSONObject.fromObject(str);
            if(result.has("errcode")){
            	return null;
            }
            return result;
        }
        return null;
	}
	
	/**
	 * 获得个人信息
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static JSONObject getPersonMessage(String openid) throws ClientProtocolException, IOException{
		WeiXinConfigService configSrv = AppContextImpl.getInstance().getBean(WeiXinConfigService.class);
		WeiXinConfig config = configSrv.get();
		HttpClient httpclient = new DefaultHttpClient();
	    // 创建Get方法实例   
		String url="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+config.getFdAccessToken()+"&openid="+openid+"&lang=zh_CN";
        HttpGet httpgets = new HttpGet(url);  
        HttpResponse response = httpclient.execute(httpgets);  
        HttpEntity entity = response.getEntity();  
        if (entity != null) {  
            InputStream instreams = entity.getContent();  
            String str = convertStreamToString(instreams);
            System.out.println(str);
            instreams.close();
            httpgets.abort(); 
            JSONObject result = JSONObject.fromObject(str);
            if(result.has("errcode")){
            	return null;
            }
            return result;
        }
        return null;
	}
	
	public static String  getTicket(String fdSceneId) throws ClientProtocolException, IOException{
		WeiXinConfigService configSrv = AppContextImpl.getInstance().getBean(WeiXinConfigService.class);
		WeiXinConfig config = configSrv.get();
		String appId = config.getFdAppId();
		String secret = config.getFdAppSecret();
		JSONObject result=getAccessToken(appId, secret);
		if(result.containsKey("access_token")){
			String accessToken=result.getString("access_token");
			return getTicket(accessToken,fdSceneId);
		}
		return null;
	}
	
	
	public static String  getTicket(String appId,String secret,String fdSceneId) throws ClientProtocolException, IOException{
		JSONObject result=getAccessToken(appId,secret);
		if(result.containsKey("access_token")){
			String accessToken=result.getString("access_token");
			return getTicket(accessToken,fdSceneId);
		}
		return null;
	}
	public static String  getTicket(String access_token,String fdSceneId) throws ClientProtocolException, IOException{
		HttpClient httpclient = new DefaultHttpClient();
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+access_token;
		HttpPost httpPost=new HttpPost(url);
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		String canshu="{\"action_name\":\"QR_LIMIT_SCENE\",\"action_info\":{\"scene\": {\"scene_id\":"+fdSceneId+"}}}";
		System.out.println(canshu);
		StringEntity s = new StringEntity(canshu);  
		s.setContentEncoding("UTF-8");
		s.setContentType("application/json");  
		httpPost.setEntity(s);
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity entity = response.getEntity();  
        if (entity != null) {  
            InputStream instreams = entity.getContent();  
            String str = convertStreamToString(instreams);
            instreams.close();
            httpPost.abort(); 
            JSONObject result = JSONObject.fromObject(str);
            return result.getString("ticket");
        }
	    return null;
		
	}
	
	public static String getURL(String baseUrl,String url) throws UnsupportedEncodingException{
		return baseUrl.replace("REDIRECT_URI",URLEncoder.encode(url,"utf-8"));
	}
	
	public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("noncestr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }
	
	public static JSONObject getJsapiTicket(String appid,String secret) throws ClientProtocolException, IOException, IllegalArgumentException, IllegalAccessException{
		JSONObject accessToken=getAccessToken(appid,secret);
		if(accessToken!=null&&accessToken.has("access_token")){
			String ACCESS_TOKEN=accessToken.getString("access_token");
			HttpClient httpclient = new DefaultHttpClient();
		    // 创建Get方法实例   
			String url="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ACCESS_TOKEN+"&type=jsapi";
	        HttpGet httpgets = new HttpGet(url);  
	        HttpResponse response = httpclient.execute(httpgets);  
	        HttpEntity entity = response.getEntity();  
	        if (entity != null) {  
	            InputStream instreams = entity.getContent();
	            String str = convertStreamToString(instreams);
	            instreams.close();
	            httpgets.abort(); 
	            JSONObject result = JSONObject.fromObject(str);
	            if(result!=null&&result.has("ticket")){
	    			String jsapiTicket=result.getString("ticket");
	    			WeiXinConfigService configSrv = AppContextImpl.getInstance().getBean(WeiXinConfigService.class);
	    			WeiXinConfig config = configSrv.get();
	    			config.setFdJsapiTicket(jsapiTicket);
	    			config.setFdAccessToken(ACCESS_TOKEN);
	    			configSrv.update(config);
	    		}
	            return result;
	        }
		}
        return null;
	}
	
	public static void getWeiXinJSConfig(HttpServletRequest request){
		WeiXinConfigService configSrv = AppContextImpl.getInstance().getBean(WeiXinConfigService.class);
		WeiXinConfig config = configSrv.get();
		String appId = config.getFdAppId();
		String secret = config.getFdAppSecret();
		String access_token=config.getFdAccessToken();
		String jsapi_ticket=config.getFdJsapiTicket();
		
		Map<String, String> result=WeiXinUtils.sign(jsapi_ticket,StringUtil.getLocationUrl(request));
		String timestamp=result.get("timestamp");
		String noncestr=result.get("noncestr");
		String signature=result.get("signature");
		
		request.setAttribute("appId", appId);
		request.setAttribute("secret", secret);
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("access_token", access_token);
		request.setAttribute("noncestr", noncestr);
		request.setAttribute("signature", signature);
		
	}
	
	/**
	 * 主动推送文字测试
	 * @param openid
	 * @param content
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void  sendMessage(String touser,String content) throws ClientProtocolException, IOException{
		WeiXinConfig config = getWeiXinConfig();
		String appId = config.getFdAppId();
		String secret = config.getFdAppSecret();
		
		//JSONObject result=getAccessToken(PropertiesUtil.getPropertiesValue("appid"), PropertiesUtil.getPropertiesValue("secret"));
		HttpClient httpclient = new DefaultHttpClient();
		//String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+result.getString("access_token");
		String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+config.getFdAccessToken();
		HttpPost httpPost=new HttpPost(url);
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		String canshu="{\"touser\":\""+touser+"\",\"text\":{\"content\":\""+content+"\"},\"msgtype\":\"text\"}";
		System.out.println(canshu);
		StringEntity s = new StringEntity(canshu,"UTF-8");  
		s.setContentEncoding("UTF-8");
		s.setContentType("application/json;charset=utf-8");  
		httpPost.setEntity(s);
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity entity = response.getEntity();  
        if (entity != null) {  
            InputStream instreams = entity.getContent();  
            String str = convertStreamToString(instreams);
            instreams.close();
            httpPost.abort(); 
        }
	}
	
	/**
	 * 主动推送图文测试
	 * @param touser
	 * @param list
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void  sendMessage(String touser,List<WeiXinMessage> list) throws ClientProtocolException, IOException{
		WeiXinConfig config = getWeiXinConfig();
		String appId = config.getFdAppId();
		String secret = config.getFdAppSecret();
		
		String content="";
		JSONObject o=new JSONObject();
		o.put("touser", touser);
		o.put("msgtype", "news");
		JSONObject news=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				array.add(list.get(i).getObject());
			}
		}
		news.put("articles",array);
		o.put("news", news);
		content=o.toString();
		HttpClient httpclient = new DefaultHttpClient();
		String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+config.getFdAccessToken();
		HttpPost httpPost=new HttpPost(url);
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		System.out.println(content);
		StringEntity s = new StringEntity(content,"UTF-8");  
		s.setContentEncoding("UTF-8");
		s.setContentType("application/json;charset=utf-8");  
		httpPost.setEntity(s);
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity entity = response.getEntity();  
        if (entity != null) {  
            InputStream instreams = entity.getContent();  
            String str = convertStreamToString(instreams);
            System.out.println(str);
            instreams.close();
            httpPost.abort(); 
        }
	}
	
	public static void  connectChat(String fdServieId,String touser,String content) throws ClientProtocolException, IOException{
		WeiXinConfig config = getWeiXinConfig();
		String appId = config.getFdAppId();
		String secret = config.getFdAppSecret();
		
		//JSONObject result=getAccessToken(PropertiesUtil.getPropertiesValue("appid"), PropertiesUtil.getPropertiesValue("secret"));
		HttpClient httpclient = new DefaultHttpClient();
		//String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+result.getString("access_token");
		System.out.println();
		String url="https://api.weixin.qq.com/customservice/kfsession/create?access_token="+config.getFdAccessToken();
		HttpPost httpPost=new HttpPost(url);
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		String canshu="{\"kf_account\":\""+fdServieId+"\",\"openid\":\""+touser+"\"}";
		System.out.println(canshu);
		StringEntity s = new StringEntity(canshu,"UTF-8");  
		s.setContentEncoding("UTF-8");
		s.setContentType("application/json;charset=utf-8");  
		httpPost.setEntity(s);
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity entity = response.getEntity();  
        if (entity != null) {  
            InputStream instreams = entity.getContent();  
            String str = convertStreamToString(instreams);
            instreams.close();
            httpPost.abort(); 
        }
	}
	
	 private static String byteToHex(final byte[] hash) {
	        Formatter formatter = new Formatter();
	        for (byte b : hash)
	        {
	            formatter.format("%02x", b);
	        }
	        String result = formatter.toString();
	        formatter.close();
	        return result;
	    }

	    private static String create_nonce_str() {
	        return UUID.randomUUID().toString();
	    }

	    private static String create_timestamp() {
	        return Long.toString(System.currentTimeMillis() / 1000);
	    }

	public static String getWeiXinUrl(String fdId) {
		// TODO Auto-generated method stub
		WeiXinUrlService service = AppContextImpl.getInstance().getBean(WeiXinUrlService.class);
	    return service.get(fdId);
	}
}
