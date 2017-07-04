package com.platform.sys.listener;

import java.io.IOException;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

import net.sf.json.JSONObject;

import com.platform.sys.context.AppContextImpl;
import com.platform.util.StringUtil;
import com.platform.weixin.model.WeiXinConfig;
import com.platform.weixin.service.WeiXinConfigService;
import com.platform.weixin.util.WeiXinUtils;

public class WeiXinJSTimer extends TimerTask {

	
	@Override
	public void run() {
		WeiXinConfigService configSrv = AppContextImpl.getInstance().getBean(WeiXinConfigService.class);
		WeiXinConfig config = configSrv.get();
		String appId = config.getFdAppId();
		String secret = config.getFdAppSecret();
		if(StringUtil.isNotNull(appId)&&StringUtil.isNotNull(secret)){
			JSONObject result;
			try {
				result = WeiXinUtils.getJsapiTicket(appId,secret);
				System.out.println("更新微信端"+result);
				this.cancel();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.cancel();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.cancel();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.cancel();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.cancel();
			}
		}
	}

}
