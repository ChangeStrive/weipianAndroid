package com.platform.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.platform.sys.context.AppContextImpl;
import com.platform.sys.model.SysFlowNum;
import com.platform.sys.model.SysFlowNumRule;
import com.platform.sys.service.SysFlowNumService;

public class FlowNumRuleUtils {
	
	public static String formatString(String str,Integer start){
		Pattern p=Pattern.compile("\\{[^\\{]+\\}"); 
		Matcher m=p.matcher(str);  
		while(m.find()) {
			String oldItem=m.group(0).trim();
            String item=oldItem.replace("{", "").replace("}", "");
            if(item.matches("\\d+")){
            	Integer n=Integer.parseInt(item);
            	String startStr=start.toString();
            	str=str.replace(oldItem, formatNumber(startStr,n));
            }else{
            	str=str.replace(oldItem, DateUtils.getToday(item));
            }
        }    
		return str;
	}
	
	public static String formatNumber(String str,int n){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i <n-str.length(); i++) { 
			sb.append("0");
		}
		sb.append(str);
		str = sb.toString();
		return str;
	}
	
	public static synchronized String getFlowNum(String className){
		SysFlowNumService flowNumSvc =(SysFlowNumService) AppContextImpl.getInstance().getBean(SysFlowNumService.class);
		int num=flowNumSvc.getMaxFlowNumByClassName(className);
		SysFlowNumRule rule=flowNumSvc.getRuleByClassName(className);
		Integer value=0;
		if(num!=-1){
			value=num+1;
		}else{
			value=rule.getFdStartValue();
		}
		String flowNum=formatString(rule.getFdRule(),value);
		SysFlowNum item=new SysFlowNum();
		item.setFdFlowNum(flowNum);
		item.setFdNum(value);
		item.setSysFlowNumRule(rule);
		flowNumSvc.save(item);
		return flowNum;
	}
}
