package com.platform.sys.service;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtil;
import com.platform.util.StringUtil;

@Component
public class SelectUtilService extends BaseService{

	
	/**
	 * 选择当前分公司下商家用户
	 * @param map
	 * @return
	 */
	public JSONObject selectSeller(Map<String, String> map) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		String hql="select fdId,fdName,fdTel from ZvSeller where 1=1 ";
		
		String leftWhere="";
		String valueId=map.get("valueId");
		if(StringUtil.isNotNull(valueId)){
			leftWhere+=" and fdId not in ("+StringUtil.formatOfSqlParams(valueId)+")";
		}
		
		String fdName=map.get("fdName");
		if(StringUtil.isNotNull(fdName)){
			leftWhere+=" and fdName like '%"+fdName+"%'";
		}
		
		String fdTel=map.get("fdTel");
		if(StringUtil.isNotNull(fdTel)){
			leftWhere+=" and fdName like '%"+fdTel+"%'";
		}
		
		String fdCompanyId=map.get("fdCompanyId");
		if(StringUtil.isNotNull(fdCompanyId)){
			leftWhere+=" and zvCompany.fdId='"+fdCompanyId+"'";
		}
		
		Integer limit=Integer.parseInt(map.get("limit"));
		List list=dbAccessor.find(hql+leftWhere,0,limit);
		JSONArray leftArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object[] item=(Object[]) list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item[0]);
				o.put("fdName", item[1]);
				o.put("fdTel", item[2]);
				leftArray.add(o);
			}
		}
		
		JSONArray rightArray=new JSONArray();
		String rightWhere="";
		if(StringUtil.isNotNull(valueId)){
			rightWhere+=" and fdId in ("+StringUtil.formatOfSqlParams(valueId)+")";
			list=dbAccessor.find(hql+rightWhere,0,limit);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Object[] item=(Object[]) list.get(i);
					JSONObject o=new JSONObject();
					o.put("fdId", item[0]);
					o.put("fdName", item[1]);
					o.put("fdTel", item[2]);
					rightArray.add(o);
				}
			}
		}
		result.put("success", true);
		result.put("leftList", leftArray);
		result.put("rightList", rightArray);
		return result;
	}
	

	/**
	 * 选择当前分公司下众筹股东
	 * @param map
	 * @return
	 */
	public JSONObject selectSalesman(Map<String, String> map) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		String hql="select fdId,fdName,fdTel,fdReferralCode from ZvSalesman where 1=1 ";
		
		String leftWhere="";
		String valueId=map.get("valueId");
		if(StringUtil.isNotNull(valueId)){
			leftWhere+=" and fdId not in ("+StringUtil.formatOfSqlParams(valueId)+")";
		}
		
		String fdName=map.get("fdName");
		if(StringUtil.isNotNull(fdName)){
			leftWhere+=" and fdName like '%"+fdName+"%'";
		}
		
		String fdTel=map.get("fdTel");
		if(StringUtil.isNotNull(fdTel)){
			leftWhere+=" and fdName like '%"+fdTel+"%'";
		}
		
		String fdCompanyId=map.get("fdCompanyId");
		if(StringUtil.isNotNull(fdCompanyId)){
			leftWhere+=" and zvCompany.fdId='"+fdCompanyId+"'";
		}
		
		String fdReferralCode=map.get("fdReferralCode");
		if(StringUtil.isNotNull(fdReferralCode)){
			leftWhere+=" and fdReferralCode='"+fdReferralCode+"'";
		}
		
		Integer limit=Integer.parseInt(map.get("limit"));
		List list=dbAccessor.find(hql+leftWhere,0,limit);
		JSONArray leftArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object[] item=(Object[]) list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item[0]);
				o.put("fdName", item[1]);
				o.put("fdTel", item[2]);
				o.put("fdReferralCode", item[3]);
				leftArray.add(o);
			}
		}
		
		JSONArray rightArray=new JSONArray();
		String rightWhere="";
		if(StringUtil.isNotNull(valueId)){
			rightWhere+=" and fdId in ("+StringUtil.formatOfSqlParams(valueId)+")";
			list=dbAccessor.find(hql+rightWhere,0,limit);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Object[] item=(Object[]) list.get(i);
					JSONObject o=new JSONObject();
					o.put("fdId", item[0]);
					o.put("fdName", item[1]);
					o.put("fdTel", item[2]);
					o.put("fdReferralCode", item[3]);
					rightArray.add(o);
				}
			}
		}
		result.put("success", true);
		result.put("leftList", leftArray);
		result.put("rightList", rightArray);
		return result;
	}
	

	public JSONObject commonCommob(String key) {
		// TODO Auto-generated method stub
		String hql=PropertiesUtil.getPropertiesValue("hqlConfig.properties", key);
		List list=dbAccessor.find(hql);
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object[] item=(Object[]) list.get(i);
				JSONObject o=new JSONObject();
				o.put("value", item[0]);
				o.put("text", item[1]);
				array.add(o);
			}
		}
		result.put("success", true);
		result.put("list", array);
		return result;
	}


	/**
	 * 选择当前分公司下的未使用的会员卡
	 * @param map
	 * @return
	 */
	public JSONObject selectNoZvCard(Map<String, String> map) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		String hql="select fdId,fdNo,fdReferenceNo,fdSalesmanName,fdSalesmanId from ZvCard where fdStatus='0' ";
		
		String leftWhere="";
		String valueId=map.get("valueId");
		if(StringUtil.isNotNull(valueId)){
			leftWhere+=" and fdId not in ("+StringUtil.formatOfSqlParams(valueId)+")";
		}
		
		String fdReferenceNo=map.get("fdReferenceNo");
		if(StringUtil.isNotNull(fdReferenceNo)){
			leftWhere+=" and fdReferenceNo like '%"+fdReferenceNo+"%'";
		}
		
		String fdNo=map.get("fdNo");
		if(StringUtil.isNotNull(fdNo)){
			leftWhere+=" and fdNo like '%"+fdNo+"%'";
		}
		
		String fdCompanyId=map.get("fdCompanyId");
		if(StringUtil.isNotNull(fdCompanyId)){
			leftWhere+=" and fdCompanyId='"+fdCompanyId+"'";
		}
		
		String fdSalesmanName=map.get("fdSalesmanName");
		if(StringUtil.isNotNull(fdSalesmanName)){
			leftWhere+=" and fdSalesmanName='"+fdSalesmanName+"'";
		}
		String fdSalesmanId=map.get("fdSalesmanId");
		if(StringUtil.isNotNull(fdSalesmanId)){
			leftWhere+=" and fdSalesmanId='"+fdSalesmanId+"'";
		}
		
		Integer limit=Integer.parseInt(map.get("limit"));
		List list=dbAccessor.find(hql+leftWhere,0,limit);
		JSONArray leftArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object[] item=(Object[]) list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item[0]);
				o.put("fdNo", item[1]);
				o.put("fdReferenceNo", item[2]);
				o.put("fdSalesmanName", item[3]);
				o.put("fdSalesmanId", item[4]);
				leftArray.add(o);
			}
		}
		
		JSONArray rightArray=new JSONArray();
		String rightWhere="";
		if(StringUtil.isNotNull(valueId)){
			rightWhere+=" and fdId in ("+StringUtil.formatOfSqlParams(valueId)+")";
			list=dbAccessor.find(hql+rightWhere,0,limit);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Object[] item=(Object[]) list.get(i);
					JSONObject o=new JSONObject();
					o.put("fdId", item[0]);
					o.put("fdNo", item[1]);
					o.put("fdReferenceNo", item[2]);
					o.put("fdSalesmanName", item[3]);
					o.put("fdSalesmanId", item[4]);
					rightArray.add(o);
				}
			}
		}
		result.put("success", true);
		result.put("leftList", leftArray);
		result.put("rightList", rightArray);
		return result;
	}


	public JSONObject selectCompany(Map<String, String> map) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		String hql="select fdId,fdName,fdAreaName from ZvCompany where 1=1 ";
		
		String leftWhere="";
		String valueId=map.get("valueId");
		if(StringUtil.isNotNull(valueId)){
			leftWhere+=" and fdId not in ("+StringUtil.formatOfSqlParams(valueId)+")";
		}
		
		String fdName=map.get("fdName");
		if(StringUtil.isNotNull(fdName)){
			leftWhere+=" and fdName like '%"+fdName+"%'";
		}
		
		String fdAreaName=map.get("fdAreaName");
		if(StringUtil.isNotNull(fdAreaName)){
			leftWhere+=" and fdAreaName like '%"+fdAreaName+"%'";
		}
		
		Integer limit=Integer.parseInt(map.get("limit"));
		List list=dbAccessor.find(hql+leftWhere,0,limit);
		JSONArray leftArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object[] item=(Object[]) list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item[0]);
				o.put("fdName", item[1]);
				o.put("fdAreaName", item[2]);
				leftArray.add(o);
			}
		}
		
		JSONArray rightArray=new JSONArray();
		String rightWhere="";
		if(StringUtil.isNotNull(valueId)){
			rightWhere+=" and fdId in ("+StringUtil.formatOfSqlParams(valueId)+")";
			list=dbAccessor.find(hql+rightWhere,0,limit);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Object[] item=(Object[]) list.get(i);
					JSONObject o=new JSONObject();
					o.put("fdId", item[0]);
					o.put("fdName", item[1]);
					o.put("fdAreaName", item[2]);
					rightArray.add(o);
				}
			}
		}
		result.put("success", true);
		result.put("leftList", leftArray);
		result.put("rightList", rightArray);
		return result;
	}
	

}
