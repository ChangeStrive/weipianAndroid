package com.platform.wx.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.internal.LinkedTreeMap;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;
import com.platform.base.BaseAction;
import com.platform.wx.service.WxPayService;

/**
 * ping++返回的支付结果
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/wxpay")
public class WxPayAction  extends BaseAction{

	@Autowired
	private WxPayService payService = null;

	private static Logger logger=LoggerFactory.getLogger(WxPayAction.class); 
	
	
	/**
	 * 付款监听和退款监听
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/webhooks")
	public void webhooks(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF8");
        //获取头部所有信息
        Enumeration headerNames = request.getHeaderNames();
        if(headerNames!=null){
	        while (headerNames.hasMoreElements()) {
	            String key = (String) headerNames.nextElement();
	            String value = request.getHeader(key);
	            System.out.println(key+" "+value);
	        }
	        // 获得 http body 内容
	        BufferedReader reader = request.getReader();
	        StringBuffer buffer = new StringBuffer();
	        String string;
	        while ((string = reader.readLine()) != null) {
	            buffer.append(string);
	        }
	        reader.close();
	        // 解析异步通知数据
	        Event event = Webhooks.eventParse(buffer.toString());
	        if(event!=null){
	        	Map<String,Object> data=event.getData();
	        	LinkedTreeMap<String, Object> r=(LinkedTreeMap<String, Object>) data.get("object");
		        if ("charge.succeeded".equals(event.getType())) {//付款成功
		        	String fdChargeId=(String) r.get("id");
		        	try{
		        		logger.error("通知：fdChargeId:"+fdChargeId);
		        		System.out.println("通知：fdChargeId:"+fdChargeId);
			        	payService.pay(fdChargeId);
			            response.setStatus(200);
		        	}catch (Exception e) {
						// TODO: handle exception
		        		e.printStackTrace();
		        		response.setStatus(500);
					}
		        } else if ("refund.succeeded".equals(event.getType())) {//退款成功
		        	String fdRefundId=(String) r.get("id");
		        	try{
		        		payService.refund(fdRefundId);
		        		System.out.println("退款成功！退款id"+fdRefundId);
		            	response.setStatus(200);
		        	}catch (Exception e) {
						// TODO: handle exception
		        		e.printStackTrace();
		        		response.setStatus(500);
					}
		        } else {
		            response.setStatus(500);
		        }
	        }
        }
	}
	
}
