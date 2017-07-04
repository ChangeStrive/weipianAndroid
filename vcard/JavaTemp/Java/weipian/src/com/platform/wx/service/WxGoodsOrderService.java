package com.platform.wx.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppGoodsOrder;
import com.platform.wp.model.AppGoodsOrderItem;
import com.platform.wp.model.AppGoodsOrderRefunder;
import com.platform.wp.model.AppUser;
import com.platform.wp.util.WpUtil;

/**
 * 订单支付验证
 * @author Administrator
 *
 */
@Component
public class WxGoodsOrderService extends BaseService {

	public AppGoodsOrder getOrder(String fdOrderId){
		AppGoodsOrder item=dbAccessor.get(AppGoodsOrder.class, fdOrderId);
		return item;
	}

	/**
	 * 获得订单列表
	 * @param fdUserId
	 * @param fdStatus
	 * @param start
	 * @param limit
	 * @return
	 */
	public JSONObject getMyOrderList(String fdUserId, String fdStatus,
			int start, int limit) {
		// TODO Auto-generated method stub
		String hql=" from AppGoodsOrder where fdUserId='"+fdUserId+"' ";
		if(StringUtil.isNotNull(fdStatus)){
			hql+="  and fdStatus in("+StringUtil.formatOfSqlParams(fdStatus)+") ";
		}
		hql+=" order by fdCreateTime desc";
		List<AppGoodsOrder> list=dbAccessor.find(hql,start,limit);
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppGoodsOrder item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdOrderNo", item.getFdOrderNo());
				JSONObject orderDetail=JSONObject.fromObject(item.getFdOrderDetail());
				o.put("fdOrderDetail", orderDetail.getJSONArray("list"));
				o.put("fdStatus", item.getFdStatus());
				o.put("fdAmount", item.getFdAmount());
				o.put("fdGoodsAmount", item.getFdGoodsAmount());
				o.put("fdCoupon", item.getFdCoupon());
				o.put("fdQuantity", item.getFdQuantity());
				
				array.add(o);
			}
		}
		result.put("status", 0);
		result.put("list", array);
		return result;
	}

	public JSONObject cancelOrderNoPay(String fdOrderId) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppGoodsOrder item=dbAccessor.get(AppGoodsOrder.class, fdOrderId);
		if(item==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单不存在");
			return result;
		}
		
		if(!item.getFdStatus().equals(WpUtil.ORDER_STATUS_PAY_NO)){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单状态不对,请重新刷新查看");
			return result;
		}
		
		item.setFdStatus(WpUtil.ORDER_STATUS_CANCEL_NO_PAY);
		dbAccessor.update(item);
		result.put("status",WpUtil.RESULT_STATUS_YES);
		result.put("msg","操作成功");
		return result;
	}

	public JSONObject applyRefund(String fdOrderId, String fdUserId,
			String fdApplyReason) {
		String fdUniqueCode=fdUserId+DateUtils.getToday("yyyyMMddHH:mm:ss");
		
		JSONObject result=new JSONObject();
		if(!StringUtil.isNotNull(fdApplyReason)){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","申请退款理由不能为空！");
			return result;
		}
		
		AppGoodsOrder item=dbAccessor.get(AppGoodsOrder.class, fdOrderId);
		if(item==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单不存在");
			return result;
		}
		
		if(!item.getFdUserId().equals(fdUserId)){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单不存在");
			return result;
		}
		
		if(!item.getFdStatus().equals(WpUtil.ORDER_STATUS_PAY_YES)&&!item.getFdStatus().equals(WpUtil.ORDER_STATUS_CANCEL_BACK_APPLY)){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单状态为已付款，未发货的订单才可以申请退款！");
			return result;
		}
		
		item.setFdStatus(WpUtil.ORDER_STATUS_CANCEL_APPLY);
		item.setFdApplyRefundTime(DateUtils.getNow());
		item.setFdApplyRefundReason(fdApplyReason);
		dbAccessor.update(item);
		
		AppUser user=dbAccessor.get(AppUser.class, fdUserId);
		AppGoodsOrderRefunder r=new AppGoodsOrderRefunder();
		r.setFdUniqueCode(fdUniqueCode);
		r.setAppGoodsOrder(item);
		r.setFdApplyReason(fdApplyReason);
		r.setFdApplyRefundTime(DateUtils.getNow());
		r.setFdUserId(fdUserId);
		r.setFdUserName(user.getFdName());
		r.setFdUserCode(user.getFdCode());
		r.setFdStatus(WpUtil.REFUNDER_STATUS_APPLY);
		r.setFdAmount(item.getFdAmount());
		r.setFdQuantity(item.getFdQuantity());
		dbAccessor.save(r);
		
		result.put("status",WpUtil.RESULT_STATUS_YES);
		result.put("msg","操作成功");
		return result;
	}
	
	public JSONObject getDetail(String fdOrderId) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		JSONObject result1=new JSONObject();
		JSONArray array1=new JSONArray();
		AppGoodsOrder item=dbAccessor.get(AppGoodsOrder.class, fdOrderId);
		if(item==null){
			result1.put("status", WpUtil.RESULT_STATUS_NO);
			result1.put("msg","订单不存在");
			return result1;
		}
		
		if(!item.getFdStatus().equals(WpUtil.ORDER_STATUS_SEND_GOODS)){
			result1.put("status", WpUtil.RESULT_STATUS_NO);
			result1.put("msg","订单状态不对,请重新刷新查看");
			return result1;
		}
		
		//重新查询一下明细
		String hql1=" from AppGoodsOrderItem where 1=1 and AppGoodsOrder.fdId='"+fdOrderId+"' and (fdQuantity-fdReturnQuantity)>0";
		List<AppGoodsOrderItem> list1=dbAccessor.find(hql1);
		JSONObject o1=new JSONObject();
		if(list1!=null&&list1.size()>0){
			for (int i = 0; i < list1.size(); i++) {
				AppGoodsOrderItem AppGoodsOrderItem =list1.get(i);
				o1.put("fdId", AppGoodsOrderItem.getFdId());
				o1.put("fdGoodsId", AppGoodsOrderItem.getFdGoodsId());
				o1.put("fdGoodsNo", AppGoodsOrderItem.getFdGoodsNo());
				o1.put("fdGoodsName", AppGoodsOrderItem.getFdGoodsName());
				o1.put("fdPicUrl", AppGoodsOrderItem.getFdPicUrl());
				o1.put("fdQuantity", AppGoodsOrderItem.getFdQuantity());
				o1.put("fdAmount", AppGoodsOrderItem.getFdAmount());
				o1.put("fdTagPrice", AppGoodsOrderItem.getFdTagPrice());
				o1.put("fdPrice", AppGoodsOrderItem.getFdPrice());
				o1.put("fdSeqNo", AppGoodsOrderItem.getFdSeqNo());
				array1.add(o1);
			}
		}
		
		
		result1.put("totalSize", array1.size());
		result1.put("list", array1);
		result1.put("status", 0);
		return result1;
	}

	/**
	 * 根据状态获得订单数
	 * @param fdUserId
	 * @return
	 */
	public int getOrderCountByStatus(String fdUserId,String fdStatus) {
		// TODO Auto-generated method stub
		String hql="select count(*) from  AppGoodsOrder where fdUserId='"+fdUserId+"' and fdStatus in("+StringUtil.formatOfSqlParams(fdStatus)+")";
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		if(num!=null&&num.intValue()>0){
			return num.intValue();
		}
		return 0;
	}


}
