package com.platform.wp.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppGoods;
import com.platform.wp.model.AppGoodsPic;

@Component
public class AppGoodsPicService extends BaseService {
	
	

	public List<AppGoodsPic> list(String fdGoodsId){
		String hql=" from AppGoodsPic where appGoods.fdId='"+fdGoodsId+"' order by fdSeqNo asc";
		List<AppGoodsPic> list=dbAccessor.find(hql);
		return list;
	}
	
	public void save(AppGoodsPic model) {
		dbAccessor.save(model);
	}
	
	public void update(AppGoodsPic model) throws IllegalArgumentException, IllegalAccessException {
		AppGoodsPic item = dbAccessor.get(AppGoodsPic.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from AppGoodsPic where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public AppGoodsPic get(String fdId) {
		AppGoodsPic item = dbAccessor.get(AppGoodsPic.class, fdId);
		return item;
	}

	public Integer getMaxFdSeqNo(String fdGoodsId){
		String hql="select max(fdSeqNo) from AppGoodsPic where  appGoods.fdId='"+fdGoodsId+"'";
		List list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			Number num=(Number) list.get(0);
			if(num==null){
				return 0;
			}
			return num.intValue();
		}
		return 0;
	}
	public void save(String fdGoodsId, String imageUrls) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotNull(fdGoodsId)&&StringUtil.isNotNull(imageUrls)){
			AppGoods goods=new AppGoods();
			goods.setFdId(fdGoodsId);
			String[] fdPicUrls=imageUrls.split(",");
			Integer fdSeqNo=getMaxFdSeqNo(fdGoodsId);
			for(int i=0;i<fdPicUrls.length;i++){
				fdSeqNo++;
				AppGoodsPic goodsImage=new AppGoodsPic();
				goodsImage.setFdPicUrl(fdPicUrls[i]);
				goodsImage.setAppGoods(goods);
				goodsImage.setFdSeqNo(fdSeqNo);
				dbAccessor.save(goodsImage);
			}
		}
	}

	public void updateFdSeqNo(String fdId, String fdSeqNo) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotNull(fdId)&&StringUtil.isNotNull(fdSeqNo)){
			String hql="update AppGoodsPic set fdSeqNo="+fdSeqNo+" where fdId='"+fdId+"'";
			dbAccessor.bulkUpdate(hql);
		}
	}
	
	public JSONObject getAppGoodsPicList(String fdGoodsId) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		String hql=" from AppGoodsPic where appGoods.fdId='"+fdGoodsId+"' order by fdSeqNo asc";
		List<AppGoodsPic> list=dbAccessor.find(hql);
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				AppGoodsPic AppGoodsPic=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", AppGoodsPic.getFdId());
				o.put("fdPicUrl", AppGoodsPic.getFdPicUrl());
				array.add(o);
			}
		}
		result.put("len", array.size());
		result.put("list", array);
		return result;
	}
}
