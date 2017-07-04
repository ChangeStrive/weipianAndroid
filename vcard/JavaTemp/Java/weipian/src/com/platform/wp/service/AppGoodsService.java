package com.platform.wp.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppGoods;
import com.platform.wp.model.AppGoodsType;

import freemarker.template.TemplateException;

@Component
public class AppGoodsService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,AppGoods model,String hql){
		if(StringUtil.isNotNull(model.getFdGoodsName())){
			hql+=" and fdGoodsName like '%"+model.getFdGoodsName()+"%'";
		}
		
		
		if(StringUtil.isNotNull(model.getFdStatus())){
			hql+=" and fdStatus='"+model.getFdStatus()+"'";
		}
		
		if(StringUtil.isNotNull(model.getFdTypeId())&&!model.getFdTypeId().equals("#")){
			hql+=" and fdTypeId like '%"+model.getFdTypeId()+"%'";
		}
		if(StringUtil.isNotNull(model.getFdTypeName())){
			hql+=" and fdTypeName like '%"+model.getFdTypeName()+"%'";
		}
		return hql;
	}

	public List<AppGoods> list(Map<String,String> map,AppGoods model,Integer start,Integer limit){
		String hql=" from AppGoods where 1=1 ";
		hql=searchWhere(map,model,hql);
		hql+=" order by fdCreateTime asc";
		List<AppGoods> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,AppGoods model){
		String hql="select count(*) from AppGoods where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	
	public JSONObject update(AppGoods model) throws IllegalArgumentException, IllegalAccessException {
		JSONObject result=new JSONObject();
		AppGoods item = dbAccessor.get(AppGoods.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		AppGoodsType AppGoodsType = dbAccessor.get(AppGoodsType.class, model.getFdTypeId());
		if(AppGoodsType!=null){
			String fdTypeId= AppGoodsType.getFdId();
			String fdTypeName= AppGoodsType.getFdTypeName();
			if(StringUtil.isNotNull(AppGoodsType.getFdPid())){
				AppGoodsType p=dbAccessor.get(AppGoodsType.class, AppGoodsType.getFdPid());
				if(p!=null){
					fdTypeId=p.getFdId()+","+fdTypeId;
					fdTypeName=p.getFdTypeName()+","+fdTypeName;
				}
			}
			item.setFdTypeId(fdTypeId);
			item.setFdTypeName(fdTypeName);
		}
		dbAccessor.update(item);
		result.put("success", true);
		result.put("msg", "保存成功!");
		return result;
	}
	public void delete(HttpServletRequest request,String fdId) throws IOException, TemplateException {
		String hql = "delete from AppGoods where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public AppGoods get(String fdId) {
		AppGoods item = dbAccessor.get(AppGoods.class, fdId);
		return item;
	}
	
	
	
	public JSONObject  save(AppGoods model) {
		JSONObject result=new JSONObject();
		AppGoodsType AppGoodsType = dbAccessor.get(AppGoodsType.class, model.getFdTypeId());
		model.setFdTypeName(AppGoodsType.getFdTypeName());
		dbAccessor.save(model);
		result.put("success", true);
		result.put("msg", "保存成功!");
		return result;
		
	}
	
	
	public void modifStatus(String fdId, String fdStatus) {
		// TODO Auto-generated method stub
		String hql = "update AppGoods set fdStatus='"+fdStatus+"' where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}
	
	
	
	public AppGoods getGoodsByGoodsNo(String fdGoodsNo){
		String hql=" from AppGoods where fdGoodsNo='"+fdGoodsNo+"'";
		List<AppGoods> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
}
