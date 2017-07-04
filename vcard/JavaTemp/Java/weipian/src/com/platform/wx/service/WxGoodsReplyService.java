package com.platform.wx.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.wp.model.AppGoodsOrder;
import com.platform.wp.model.AppGoodsOrderItem;
import com.platform.wp.model.AppGoodsReply;
import com.platform.wp.model.AppUser;
import com.platform.wp.util.WpUtil;

@Component
public class WxGoodsReplyService extends BaseService {

	/**
	 * 商品类别
	 * @param fdShopNo
	 * @param start
	 * @param limit
	 * @return
	 */
	public JSONObject goodsReplyList(String fdGoodsId,int start, int limit) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		
		String hql=" from AppGoodsReply";
		hql+=" where fdGoodsId='"+fdGoodsId+"'";
		hql+=" order by  fdReplyTime desc";
		List<AppGoodsReply> list=dbAccessor.find(hql,start,limit);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppGoodsReply item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdGoodsId",item.getFdGoodsId());
				o.put("fdGoodsNo", item.getFdGoodsNo());
				o.put("fdGoodsName", item.getFdGoodsName());
				o.put("fdGoodsPicUrl",item.getFdGoodsPicUrl());
				o.put("fdUserId", item.getFdUserId());
				o.put("fdUserName", item.getFdUserName());
				o.put("fdUserPicUrl", item.getFdUserPicUrl());
				o.put("fdReplyContent", item.getFdReplyContent());
				o.put("fdReplyTime", item.getFdReplyTime());
				o.put("fdOrderId", item.getFdOrderId());
				o.put("fdOrderItemId", item.getFdOrderItemId());
				array.add(o);
			}
		}
		result.put("status", 0);
		result.put("list", array);
		return result;
	}

	/**
	 * 商品类别
	 * @param fdShopNo
	 * @param start
	 * @param limit
	 * @return
	 */
	public JSONObject myReplyList(String fdUserId,int start, int limit) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		
		String hql=" from AppGoodsReply";
		hql+=" where fdUserId='"+fdUserId+"'";
		hql+=" order by  fdReplyTime desc";
		List<AppGoodsReply> list=dbAccessor.find(hql,start,limit);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppGoodsReply item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdGoodsId",item.getFdGoodsId());
				o.put("fdGoodsNo", item.getFdGoodsNo());
				o.put("fdGoodsName", item.getFdGoodsName());
				o.put("fdGoodsPicUrl",item.getFdGoodsPicUrl());
				o.put("fdUserId", item.getFdUserId());
				o.put("fdUserName", item.getFdUserName());
				o.put("fdUserPicUrl", item.getFdUserPicUrl());
				o.put("fdReplyContent", item.getFdReplyContent());
				o.put("fdReplyTime", item.getFdReplyTime());
				o.put("fdOrderId", item.getFdOrderId());
				o.put("fdOrderItemId", item.getFdOrderItemId());
				array.add(o);
			}
		}
		result.put("status", 0);
		result.put("list", array);
		return result;
	}
	
	public JSONObject reply(AppUser user, String fdOrderId, String replyDetail) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppGoodsOrder order=dbAccessor.get(AppGoodsOrder.class, fdOrderId);
		if(!order.getFdUserId().equals(user.getFdId())){
			result.put("status", -1);
			result.put("msg","没权限评论");
			return result;
		}
		if(!order.getFdStatus().equals(WpUtil.ORDER_STATUS_REPLY)){
			result.put("status", -1);
			result.put("msg","不能评价");
			return result;
		}
		JSONArray array=JSONArray.fromObject(replyDetail);
		for(int i=0;i<array.size();i++){
			JSONObject item=array.getJSONObject(i);
			String fdOrderItemId=item.getString("fdOrderItemId");
			String fdReplyContent=item.getString("fdReplyContent");
			AppGoodsReply reply=new AppGoodsReply();
			AppGoodsOrderItem oItem=dbAccessor.get(AppGoodsOrderItem.class, fdOrderItemId);
			
			reply.setFdOrderItemId(oItem.getFdId());
			reply.setFdOrderId(fdOrderId);
			
			reply.setFdGoodsId(oItem.getFdGoodsId());
			reply.setFdGoodsName(oItem.getFdGoodsName());
			reply.setFdGoodsNo(oItem.getFdGoodsNo());
			reply.setFdGoodsPicUrl(oItem.getFdPicUrl());
			
			reply.setFdReplyContent(fdReplyContent);
			reply.setFdReplyTime(DateUtils.getNow());
			
			reply.setFdUserId(user.getFdId());
			reply.setFdUserName(user.getFdName());
			reply.setFdUserCode(user.getFdCode());
			reply.setFdUserPicUrl(user.getFdPicUrl());
			
			dbAccessor.save(reply);
			
		}
		order.setFdStatus(WpUtil.ORDER_STATUS_FINISH);
		dbAccessor.update(order);
		result.put("status",0);
		result.put("msg","评价成功!");
		return result;
	}
}
