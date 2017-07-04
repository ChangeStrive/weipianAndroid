package com.platform.wx.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.wp.model.AppCollection;

@Component
public class WxCollectionService extends BaseService {

	/**
	 * 判断是否收藏  false表示否  true表示是
	 * @param fdUserId
	 * @param fdShopStoreId
	 * @return
	 */
	public boolean hasCollection(String fdUserId,String fdGoodsId){
		String hql="select count(*) from AppCollection where fdUserId='"+fdUserId+"' and fdGoodsId='"+fdGoodsId+"'";
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		if(num.intValue()>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 收藏
	 * @param fdUserId
	 * @param fdShopStoreId
	 * @return
	 */
	public JSONObject collection(String fdUserId,String fdGoodsId){
		JSONObject result=new JSONObject();
		if(!hasCollection(fdUserId,fdGoodsId)){
			AppCollection item=new AppCollection();
			item.setFdUserId(fdUserId);
			item.setFdGoodsId(fdGoodsId);
			item.setFdCreateTime(DateUtils.getNow());
			dbAccessor.save(item);
		}
		result.put("status", 0);
		result.put("msg", "操作成功!");
		return result;
	}
	
	/**
	 * 取消收藏
	 * @param fdUserId
	 * @param fdShopStoreId
	 * @return
	 */
	public JSONObject cancelCollection(String fdUserId,String fdGoodsId){
		JSONObject result=new JSONObject();
		String hql="delete from AppCollection where fdUserId='"+fdUserId+"' and fdGoodsId='"+fdGoodsId+"'";
		dbAccessor.bulkUpdate(hql);
		result.put("status", 0);
		result.put("msg", "操作成功!");
		return result;
	}
	
	/**
	 * 商品类别
	 * @param fdShopNo
	 * @param start
	 * @param limit
	 * @return
	 */
	public JSONObject goodsList(String fdUserId,int start, int limit) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		
		String sql="select b.fdId,b.fdGoodsNo,b.fdGoodsName,b.fdPicUrl,b.fdPrice,b.fdTagPrice ";
		sql+=" from app_collection a";
		sql+=" inner join app_goods b on b.fdId=a.fdGoodsId and b.fdStatus='1' and b.fdQuantity>0 ";
		sql+=" where a.fdUserId='"+fdUserId+"'";
		sql+=" order by  a.fdCreateTime desc";
		List list=dbAccessor.findBySQL(sql,start,limit);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object[] goods=(Object[])list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdGoodsId", goods[0]);
				o.put("fdGoodsNo", goods[1]);
				o.put("fdGoodsName", goods[2]);
				o.put("fdPicUrl", goods[3]);
				o.put("fdPrice", goods[4]);
				o.put("fdTagPrice", goods[5]);
				array.add(o);
			}
		}
		result.put("status", 0);
		result.put("list", array);
		return result;
	}
}
