package com.platform.sys.listener;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.scheduling.annotation.Scheduled;

import com.platform.sys.context.AppContextImpl;
import com.platform.wp.service.AppGoodsOrderService;
import com.platform.wx.service.WxGoodsOrderService;

public class TimerTaskUtil {
	
	/**
	 * 自动取消未付款订单
	 * @throws ClientProtocolException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Scheduled(cron = "0 * * * * *")
	public void autoCancelOrder() throws ClientProtocolException, IllegalStateException, IOException, IllegalArgumentException, IllegalAccessException{
		AppGoodsOrderService service = AppContextImpl.getInstance().getBean(AppGoodsOrderService.class);
		WxGoodsOrderService service2 = AppContextImpl.getInstance().getBean(WxGoodsOrderService.class);
		List list=service.noPayOrderOfOneDay();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String fdOrderId=(String) list.get(i);
				service2.cancelOrderNoPay(fdOrderId);
			}
		}
	}
	
}
