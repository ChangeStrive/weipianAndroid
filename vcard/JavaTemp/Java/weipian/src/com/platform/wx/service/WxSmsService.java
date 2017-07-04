package com.platform.wx.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.util.IDGenerator;
import com.platform.util.SmsUtil;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppSms;

/**
 *
 * @author hyw
 *
 */
@Component
public class WxSmsService extends BaseService {

	/**
	 *  获得短信验证码
	 * @return
	 */
	public JSONObject saveSms(String fdMobile,String fdType){
		JSONObject result=new JSONObject();
		
		Calendar calendar=Calendar.getInstance();   
		calendar.setTime(new Date()); 
		calendar.add(Calendar.MINUTE, -3);
		String date=DateUtils.formatDate(calendar, "yyyy-MM-dd HH:mm:ss");
		
		String hql=" from AppSms where fdMobile='"+fdMobile+"' and fdType='"+fdType+"' and fdIsUse='0' and fdSendTime>'"+date+"' ";
		List<AppSms> list = dbAccessor.find(hql,0,1);
		if (list != null && !list.isEmpty()) {// 如果当天已经发送验证密码
			result.put("status", 0);
			result.put("msg", "验证码已经发出，请等待3分钟再来");
			return result;
		}else{
			int fdValidCode = SmsUtil.buildRandom(4);
			String content="";
			if(fdType.equals("0")){
				content = "您正在申请注册微片商城会员，验证码："
					+ fdValidCode + "，有效期3分钟。";
			}else if(fdType.equals("1")){
				content = "您正在申请修改微片商城密码，验证码："
					+ fdValidCode + "，有效期3分钟。";
			}
			if(StringUtil.isNotNull(content)){
			// 调用发短信接口
				JSONObject r=SmsUtil.sendMsg(fdMobile, content);
				if(r.getString("code").equals("03")){
					AppSms zvSms = new AppSms();
					zvSms.setFdId(IDGenerator.generateID());
					zvSms.setFdMobile(fdMobile);
					zvSms.setFdType(fdType);//注册的验证码
					zvSms.setFdValidCode(String.valueOf(fdValidCode));
					zvSms.setFdContent(content);
					zvSms.setFdSendTime(DateUtils.getNow());
					zvSms.setFdStatus(r.getString("code"));
					zvSms.setFdIsUse("0");
					dbAccessor.save(zvSms);
					result.put("status", 0);
					result.put("msg", "验证码已经发出，请等待");
				}else if(r.getString("code").equals("11")){
					result.put("status", -1);
					result.put("msg", "短信余额不足");
				}else if(r.getString("code").equals("02")){
					result.put("status", -1);
					result.put("msg", "短信错误代码：IP验证失败");
				}else {
					result.put("status", -1);
					result.put("msg","短信错误代码："+r.getString("code"));
				}
			}else{
				result.put("status", -1);
				result.put("msg", "短信类型不存在");
			}
		} 
		return result;
	}
	
	/**
	 * 验证短信验证码
	 * @return
	 */
	public boolean vaildCode(String fdMobile,String fdType,String fdCode){
		JSONObject result=new JSONObject();
		
		Calendar calendar=Calendar.getInstance();   
		calendar.setTime(new Date()); 
		calendar.add(Calendar.MINUTE, -3);
		String date=DateUtils.formatDate(calendar, "yyyy-MM-dd HH:mm:ss");
		
		String hql=" from AppSms where fdMobile='"+fdMobile+"' and fdType='"+fdType+"' and fdIsUse='0' and fdSendTime>'"+date+"' and fdValidCode='"+fdCode+"' ";
		List<AppSms> list = dbAccessor.find(hql,0,1);
		if (list != null && !list.isEmpty()) {// 如果当天已经发送验证密码
			AppSms item=list.get(0);
			item.setFdIsUse("1");
			dbAccessor.update(item);
			return true;
		}else{
			return false;
		} 
	}
}
