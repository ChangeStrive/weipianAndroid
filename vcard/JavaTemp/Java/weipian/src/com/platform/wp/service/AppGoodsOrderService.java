package com.platform.wp.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.pingplusplus.model.Refund;
import com.platform.base.BaseService;
import com.platform.sys.model.SysUser;
import com.platform.util.DateUtils;
import com.platform.util.FlowNumRuleUtils;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppGoodsOrder;
import com.platform.wp.model.AppGoodsOrderItem;
import com.platform.wp.model.AppGoodsOrderRefunder;
import com.platform.wp.model.AppRecord;
import com.platform.wp.util.WpUtil;

import freemarker.template.TemplateException;

@Component
public class AppGoodsOrderService extends BaseService {
	
	
	/**
	 * 把订单明细转成json
	 * @param list
	 * @return
	 */
	public JSONObject getOrderDetailObject(List<AppGoodsOrderItem> list){
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppGoodsOrderItem item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdGoodsId", item.getFdGoodsId());
				o.put("fdTitle", item.getFdGoodsName());
				o.put("fdPicUrl", item.getFdPicUrl());
				o.put("fdGoodsCode", item.getFdGoodsNo());
				o.put("fdTagPrice", item.getFdTagPrice());
				o.put("fdPrice", item.getFdPrice());
				o.put("fdAmount", item.getFdAmount());
				o.put("fdQuantity", item.getFdQuantity());
				o.put("fdSeqNo", i);
				array.add(o);
			}
		}
		result.put("totalSize", list.size());
		result.put("list", array);
		return result;
	}
	
	public String searchWhere(Map<String,String> map,AppGoodsOrder model,String hql){
		if(StringUtil.isNotNull(model.getFdUserCode())){
			hql+=" and fdUserCode like '%"+model.getFdUserCode()+"%'";
		}
		
		if(StringUtil.isNotNull(model.getFdUserName())){
			hql+=" and fdUserName like '%"+model.getFdUserName()+"%'";
		}
		
		if(StringUtil.isNotNull(model.getFdOrderNo())){
			hql+=" and fdOrderNo like '%"+model.getFdOrderNo()+"%'";
		}
		
		String fdStatus=map.get("fdStatus");
		if(StringUtil.isNotNull(fdStatus)){
			hql+=" and fdStatus in("+StringUtil.formatOfSqlParams(fdStatus)+")";
		}
		
		String fdStartDate=map.get("fdStartDate");
		if(StringUtil.isNotNull(fdStartDate)){
			hql+=" and (fdCreateTime>'"+fdStartDate+"' or fdCreateTime like'%"+fdStartDate+"%')";
		}
		
		String fdEndDate=map.get("fdEndDate");
		if(StringUtil.isNotNull(fdEndDate)){
			hql+=" and (fdCreateTime<'"+fdEndDate+"' or fdCreateTime like'%"+fdEndDate+"%')";
		}
		return hql;
	}
	

	public List<AppGoodsOrder> list(Map<String,String> map,AppGoodsOrder model,Integer start,Integer limit){
		String hql=" from AppGoodsOrder where 1=1 ";
		hql=searchWhere(map,model,hql);
		String orderBy=map.get("orderBy");
		if(StringUtil.isNotNull(orderBy)){
			hql+=" order by "+orderBy;
		}else{
			hql+=" order by fdCreateTime desc";
		}
		List<AppGoodsOrder> list=dbAccessor.find(hql,start,limit);
		return list;
	}
	
	public int getCount(Map<String,String> map,AppGoodsOrder model){
		String hql="select count(*) from AppGoodsOrder where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(AppGoodsOrder model) {
		dbAccessor.save(model);
	}
	
	public void update(AppGoodsOrder model) throws IllegalArgumentException, IllegalAccessException {
		AppGoodsOrder item = dbAccessor.get(AppGoodsOrder.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
		String hql=" from AppGoodsOrderItem where appGoodsOrder.fdId='"+item.getFdId()+"'";
		List<AppGoodsOrderItem> list=dbAccessor.find(hql);
		JSONObject detail=getOrderDetailObject(list);
		item.setFdOrderDetail(detail.toString());
	}

	public void delete(HttpServletRequest request,String fdId) throws IOException, TemplateException {
		String hql = "delete from AppGoodsOrder where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public AppGoodsOrder get(String fdId) {
		AppGoodsOrder item = dbAccessor.get(AppGoodsOrder.class, fdId);
		return item;
	}

	public JSONObject agreeRefund(String fdOrderId, SysUser user) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppGoodsOrder item=dbAccessor.get(AppGoodsOrder.class, fdOrderId);
		if(item==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单不存在");
			return result;
		}
		
		if(user==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","请登录");
			return result;
		}
		
		if(!item.getFdStatus().equals(WpUtil.ORDER_STATUS_CANCEL_APPLY)){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单状态不对！");
			return result;
		}
		
		try{
			String hql=" from AppGoodsOrderRefunder where appGoodsOrder.fdId='"+fdOrderId+"' and fdStatus='"+WpUtil.REFUNDER_STATUS_APPLY+"' order by fdApplyRefundTime desc";
			List<AppGoodsOrderRefunder> rlist=dbAccessor.find(hql,0,1);
			AppGoodsOrderRefunder r=null;
			if(rlist!=null&&rlist.size()>0){
				r=rlist.get(0);
			}else{
				result.put("status", WpUtil.RESULT_STATUS_NO);
				result.put("msg","订单状态不对！");
				return result;
			}
			
			hql=" from AppGoodsOrderItem where appGoodsOrder.fdId='"+fdOrderId+"'";
			List<AppGoodsOrderItem> list=dbAccessor.find(hql);
		
			Refund refund=WpUtil.refund(item,r.getFdApplyReason());
			if(refund!=null){
				
				
				item.setFdStatus(WpUtil.ORDER_STATUS_CANCEL_PAY);
				item.setFdRefundId(refund.getId());
				
				
				r.setFdRefunderId(user.getFdId());
				r.setFdRefunderName(user.getFdName());
				r.setFdRefundTime(DateUtils.getNow());
				
				dbAccessor.update(item);
				
				AppRecord record=new AppRecord();
				record.setFdAmount(item.getFdAmount().multiply(new BigDecimal(-1)));
				record.setFdCoupon(item.getFdCoupon().multiply(new BigDecimal(-1)));
				record.setFdGoodsAmount(item.getFdGoodsAmount().multiply(new BigDecimal(-1)));
				record.setFdCreateTime(DateUtils.getNow());
				record.setFdOrderNo(item.getFdOrderNo());
				record.setFdQuantity(-item.getFdQuantity());
				record.setFdUserId(item.getFdUserId());
				record.setFdUserName(item.getFdUserName());
				record.setFdUserCode(item.getFdUserCode());
				record.setFdRecordNo(FlowNumRuleUtils.getFlowNum("AppRecord"));
				record.setFdType("1");
				record.setFdStatus("1");
				record.setFdToken(refund.getId());
				dbAccessor.save(record);
				
				dbAccessor.update(r);
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						AppGoodsOrderItem orderItem=list.get(i);
						
						hql="update AppGoods set fdSaleQuantity=fdSaleQuantity-"+orderItem.getFdQuantity()+",fdQuantity=fdQuantity+"+orderItem.getFdQuantity()+" where fdId='"+orderItem.getFdGoodsId()+"'";
						dbAccessor.bulkUpdate(hql);//销售量增加 减库存
					}
				}
				result.put("status",WpUtil.RESULT_STATUS_YES);
				result.put("msg","操作成功");
			}else{
				result.put("status",WpUtil.RESULT_STATUS_ERROR);
				result.put("msg",WpUtil.RESULT_STATUS_ERROR_MSG);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("status",WpUtil.RESULT_STATUS_ERROR);
			result.put("msg",WpUtil.RESULT_STATUS_ERROR_MSG);
		}
		return result;
	
	}

	public JSONObject noAgreeRefund(String fdOrderId, SysUser user,
			String fdBackReason) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppGoodsOrder item=dbAccessor.get(AppGoodsOrder.class, fdOrderId);
		if(item==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单不存在");
			return result;
		}
		
		if(user==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","请登录");
			return result;
		}
		
		if(!item.getFdStatus().equals(WpUtil.ORDER_STATUS_CANCEL_APPLY)){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单状态不对,请重新刷新查看");
			return result;
		}
		
		String hql=" from AppGoodsOrderRefunder where appGoodsOrder.fdId='"+fdOrderId+"' and fdStatus='"+WpUtil.REFUNDER_STATUS_APPLY+"' order by fdApplyRefundTime desc";
		List<AppGoodsOrderRefunder> rlist=dbAccessor.find(hql,0,1);
		AppGoodsOrderRefunder r=null;
		if(rlist!=null&&rlist.size()>0){
			r=rlist.get(0);
			r.setFdStatus(WpUtil.REFUNDER_STATUS_BACK);
			r.setFdBackTime(DateUtils.getNow());
			r.setFdRefunderId(user.getFdId());
			r.setFdRefunderName(user.getFdName());
			r.setFdBackReason(fdBackReason);
			dbAccessor.update(r);
			
			item.setFdStatus(WpUtil.ORDER_STATUS_CANCEL_BACK_APPLY);
			dbAccessor.update(item);
		}else{
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单状态不对,请重新刷新查看");
			return result;
		}
		result.put("status",WpUtil.RESULT_STATUS_YES);
		result.put("msg","操作成功");
		return result;
	}

	public JSONObject deliver(String fdOrderId, SysUser user,
			String fdExpressName, String fdExpressNo) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppGoodsOrder item=dbAccessor.get(AppGoodsOrder.class, fdOrderId);
		if(item==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单不存在");
			return result;
		}
		
		if(user==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","请登录");
			return result;
		}
		
		if(!(StringUtil.isNotNull(fdExpressNo)&&StringUtil.isNotNull(fdExpressName))){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","快递名称和快递单号不能为空");
			return result;
		}
		
		boolean flag=item.getFdStatus().equals(WpUtil.ORDER_STATUS_PAY_YES);
		boolean flag1=item.getFdStatus().equals(WpUtil.ORDER_STATUS_CANCEL_BACK_APPLY);
		
		if(!(flag||flag1)){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单状态不对,请重新刷新查看");
			return result;
		}
		
		item.setFdExpressNo(fdExpressNo);
		item.setFdExpressName(fdExpressName);
		item.setFdStatus(WpUtil.ORDER_STATUS_SEND_GOODS);
		
		dbAccessor.update(item);
		result.put("status",WpUtil.RESULT_STATUS_YES);
		result.put("msg","操作成功");
		return result;
	}

	public JSONObject sendByShop(String fdOrderId, SysUser user) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppGoodsOrder item=dbAccessor.get(AppGoodsOrder.class, fdOrderId);
		if(item==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单不存在");
			return result;
		}
		
		if(user==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","请登录");
			return result;
		}
		
		
		boolean flag=item.getFdStatus().equals(WpUtil.ORDER_STATUS_PAY_YES);
		boolean flag1=item.getFdStatus().equals(WpUtil.ORDER_STATUS_CANCEL_BACK_APPLY);
		if(!(flag||flag1)){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单状态不对,请重新刷新查看");
			return result;
		}
		
		item.setFdExpressNo("送货上门");
		item.setFdExpressName("送货上门");
		item.setFdStatus(WpUtil.ORDER_STATUS_SEND_GOODS);
		item.setFdDeliverTime(DateUtils.getNow());
		
		dbAccessor.update(item);
		result.put("status",WpUtil.RESULT_STATUS_YES);
		result.put("msg","操作成功");
		return result;
	}

	public JSONObject sureReceiveGoods(String fdOrderId) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppGoodsOrder item=dbAccessor.get(AppGoodsOrder.class, fdOrderId);
		if(item==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单不存在");
			return result;
		}
		
		
		boolean flag=item.getFdStatus().equals(WpUtil.ORDER_STATUS_SEND_GOODS);
		boolean flag1=item.getFdStatus().equals(WpUtil.ORDER_STATUS_CANCEL_BACK_APPLY)&&StringUtil.isNotNull(item.getFdExpressName());
		boolean flag2=item.getFdStatus().equals(WpUtil.ORDER_STATUS_CANCEL_APPLY)&&StringUtil.isNotNull(item.getFdExpressName());
		if(!(flag||flag1||flag2)){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单状态不对,请重新刷新查看");
			return result;
		}
		
		item.setFdStatus(WpUtil.ORDER_STATUS_REPLY);
		item.setFdReceiveTime(DateUtils.getNow());
		
		dbAccessor.update(item);
		
		String hql=" from AppRecord where fdType='3' and fdStatus='0' and  fdOrderNo='"+item.getFdOrderNo()+"'";
		List<AppRecord> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppRecord record=list.get(i);
				record.setFdStatus("1");
				dbAccessor.update(record);
				BigDecimal fdAmount=record.getFdAmount().multiply(new BigDecimal(-1));
				hql="update AppUser set fdAmount=fdAmount+"+fdAmount+" where fdId='"+record.getFdUserId()+"'";
				dbAccessor.bulkUpdate(hql);
			}
		}
		
		result.put("status",WpUtil.RESULT_STATUS_YES);
		result.put("msg","操作成功");
		return result;
	}


	public List<AppGoodsOrder> payOrderList() {
		// TODO Auto-generated method stub
		String sql="from AppGoodsOrder where fdStatus='"+WpUtil.ORDER_STATUS_PAY_YES+"'";
		List<AppGoodsOrder> list=dbAccessor.find(sql);
		return list;
	}

	public void updateStatus(String fdId, String fdStatus) {
		// TODO Auto-generated method stub
		String hql="update AppGoodsOrder set fdStatus='"+fdStatus+"' where fdId='"+fdId+"'";
		dbAccessor.bulkUpdate(hql);
	}

	
	public JSONObject cancelOrderOfPay(String fdOrderId, String fdCancelReason,
			SysUser user) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppGoodsOrder item=dbAccessor.get(AppGoodsOrder.class, fdOrderId);
		if(item==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单不存在");
			return result;
		}
		
		if(user==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","请登录");
			return result;
		}
		
		if(!item.getFdStatus().equals(WpUtil.ORDER_STATUS_PAY_YES)){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单状态不对！");
			return result;
		}
		
		try{
			AppGoodsOrderRefunder ref=new AppGoodsOrderRefunder();
			String fdUniqueCode=item.getFdUserId()+DateUtils.getToday("yyyyMMddHH:mm:ss");
			ref.setFdUniqueCode(fdUniqueCode);
			ref.setAppGoodsOrder(item);
			ref.setFdApplyReason(fdCancelReason);
			ref.setFdApplyRefundTime(DateUtils.getNow());
			ref.setFdUserId(item.getFdUserId());
			ref.setFdUserName(item.getFdUserName());
			ref.setFdUserCode(item.getFdUserCode());
			ref.setFdStatus(WpUtil.REFUNDER_STATUS_APPLY);
			ref.setFdAmount(item.getFdAmount());
			ref.setFdQuantity(item.getFdQuantity());
			
			String hql=" from AppGoodsOrderItem where appGoodsOrder.fdId='"+fdOrderId+"'";
			List<AppGoodsOrderItem> list=dbAccessor.find(hql);
		
			
			
			Refund refund=WpUtil.refund(item,fdCancelReason);
			if(refund!=null){
				item.setFdStatus(WpUtil.ORDER_STATUS_CANCEL_AGREE_APPLY);
				item.setFdRefundId(refund.getId());
				
				
				ref.setFdRefunderId(user.getFdId());
				ref.setFdRefunderName(user.getFdName());
				ref.setFdRefundTime(DateUtils.getNow());
				dbAccessor.save(ref);
				
				dbAccessor.update(item);
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						AppGoodsOrderItem orderItem=list.get(i);
						
						hql="update AppGoods set fdSaleQuantity=fdSaleQuantity-"+orderItem.getFdQuantity()+",fdQuantity=fdQuantity+"+orderItem.getFdQuantity()+" where fdId='"+orderItem.getFdGoodsId()+"'";
						dbAccessor.bulkUpdate(hql);//销售量增加 减库存
					}
				}
				result.put("status",WpUtil.RESULT_STATUS_YES);
				result.put("msg","操作成功");
			}else{
				result.put("status",WpUtil.RESULT_STATUS_ERROR);
				result.put("msg",WpUtil.RESULT_STATUS_ERROR_MSG);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("status",WpUtil.RESULT_STATUS_ERROR);
			result.put("msg",WpUtil.RESULT_STATUS_ERROR_MSG);
		}
		return result;
	
	}
	
	public String getSearchWhere(Map<String,String> map){
		String searchWhere="";
		
		String fdOrderNo=map.get("fdOrderNo");
		if(StringUtil.isNotNull(fdOrderNo)){
			searchWhere+=" and fdOrderNo like '%"+fdOrderNo+"%'";
		}
		
		String fdStartDate=map.get("fdStartDate");
		if(StringUtil.isNotNull(fdStartDate)){
			searchWhere+=" and date(fdCreateTime)>='"+fdStartDate+"'";
		}
		
		String fdEndDate=map.get("fdEndDate");
		if(StringUtil.isNotNull(fdEndDate)){
			searchWhere+=" and date(fdCreateTime)<='"+fdEndDate+"'";
		}
		return searchWhere;
	}
	public JSONObject getOrderReport(Map<String,String> map){
		JSONObject result=new JSONObject();
		String searchWhere=getSearchWhere(map);
		
		StringBuffer sb=new StringBuffer();
		sb.append("select ");
		sb.append(" sum(fdSaleAmount) as fdSaleAmount,sum(fdSaleCount) as fdSaleCount");
		sb.append(",sum(fdRefundAmount) as fdRefundAmount,sum(fdRefundCount) as fdRefundCount");
		sb.append(",sum(fdReturnAmount) as fdReturnAmount,sum(fdReturnCount) as fdReturnCount ");
		sb.append(",sum(fdCoupon) as fdCoupon,sum(fdCouponCount) as fdCouponCount,sum(fdExpressAmount) as fdExpressAmount,sum(fdGoodsAmount) as fdGoodsAmount");
		sb.append("from(");
		sb.append(" select ");
		sb.append(" sum(fdAmount) as fdSaleAmount,sum(fdQuantity) as fdSaleCount");
		sb.append(" ,0.00 as fdRefundAmount,0.00 as fdRefundCount");
		sb.append(" ,0.00 as fdReturnAmount,0.00 as fdReturnCount");
		sb.append(" ,sum(fdCoupon) as fdCoupon,sum(fdCouponCount) as fdCouponCount,sum(fdExpressAmount) as fdExpressAmount,sum(fdGoodsAmount) as fdGoodsAmount");
		sb.append(" from mq_record");
		sb.append(" where fdType='0' ");
		sb.append(searchWhere);
		sb.append(" union");
		sb.append(" select 0,0,sum(fdAmount),sum(fdQuantity),0,0,sum(fdCoupon),sum(fdCouponCount),sum(fdExpressAmount),sum(fdGoodsAmount)");
		sb.append(" from mq_record ");
		sb.append(" where fdType='1' ");
		sb.append(searchWhere);
		sb.append("	union");
		sb.append(" select 0,0,0,0,sum(fdAmount),sum(fdQuantity),sum(fdCoupon),sum(fdCouponCount),sum(fdExpressAmount),sum(fdGoodsAmount)");
		sb.append(" from mq_record");
		sb.append(" where fdType='2' ");
		sb.append(searchWhere);
		sb.append(" ) a");
		
		JSONArray array=new JSONArray();
		List list=dbAccessor.findBySQL(sb.toString());
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object[] item=(Object[]) list.get(i);
				JSONObject o=new JSONObject();
				BigDecimal fdSaleAmount=(BigDecimal) item[0];
				Integer fdSaleCount=(Integer) item[1];
				BigDecimal fdRefundAmount=(BigDecimal) item[2];
				Integer fdRefundCount=(Integer) item[3];
				BigDecimal fdReturnAmount=(BigDecimal) item[4];
				Integer fdReturnCount=(Integer) item[5];
				
				BigDecimal fdCoupon=(BigDecimal) item[6];
				Integer fdCouponCount=(Integer) item[7];
				BigDecimal fdExpressAmount=(BigDecimal) item[8];
				BigDecimal fdGoodsAmount=(BigDecimal) item[9];
				
				BigDecimal fdTotalAmount=fdSaleAmount.add(fdRefundAmount).add(fdReturnAmount);
				int fdTotalQuantity=fdSaleCount+fdRefundCount+fdReturnCount;
				
				o.put("fdSaleAmount", fdSaleAmount);
				o.put("fdSaleCount", fdSaleCount);
				o.put("fdRefundAmount", fdRefundAmount);
				o.put("fdRefundCount", fdRefundCount);
				o.put("fdReturnAmount", fdReturnAmount);
				o.put("fdReturnCount", fdReturnCount);
				o.put("fdCoupon", fdCoupon);
				o.put("fdCouponCount", fdCouponCount);
				o.put("fdExpressAmount", fdExpressAmount);
				o.put("fdGoodsAmount", fdGoodsAmount);
				o.put("fdTotalAmount", fdTotalAmount);
				o.put("fdTotalQuantity", fdTotalQuantity);
				array.add(o);
			}
		}
		result.put("list", array);
		result.put("status", 0);
		return result;
	}
	
	public JSONObject getOrderReportByDays(Map<String,String> map){
		JSONObject result=new JSONObject();
		String searchWhere=getSearchWhere(map);
		
		
		StringBuffer sb=new StringBuffer();
		sb.append("select ");
		sb.append(" sum(fdSaleAmount) as fdSaleAmount,sum(fdSaleCount) as fdSaleCount");
		sb.append(",sum(fdRefundAmount) as fdRefundAmount,sum(fdRefundCount) as fdRefundCount");
		sb.append(",sum(fdReturnAmount) as fdReturnAmount,sum(fdReturnCount) as fdReturnCount ");
		sb.append(",sum(fdCoupon) as fdCoupon,sum(fdCouponCount) as fdCouponCount,sum(fdExpressAmount) as fdExpressAmount,sum(fdGoodsAmount) as fdGoodsAmount");
		sb.append(",fdCreateTime");
		sb.append("from(");
		sb.append(" select ");
		sb.append(" sum(fdAmount) as fdSaleAmount,sum(fdQuantity) as fdSaleCount");
		sb.append(" ,0.00 as fdRefundAmount,0.00 as fdRefundCount");
		sb.append(" ,0.00 as fdReturnAmount,0.00 as fdReturnCount");
		sb.append(" ,sum(fdCoupon) as fdCoupon,sum(fdCouponCount) as fdCouponCount,sum(fdExpressAmount) as fdExpressAmount,sum(fdGoodsAmount) as fdGoodsAmount");
		sb.append(" ,date(fdCreateTime) as fdCreateTime");
		sb.append(" from mq_record");
		sb.append(" where fdType='0' ");
		sb.append(searchWhere);
		sb.append(" group by date(fdCreateTime)");
		sb.append(" union");
		sb.append(" select 0,0,sum(fdAmount),sum(fdQuantity),0,0,sum(fdCoupon),sum(fdCouponCount),sum(fdExpressAmount),sum(fdGoodsAmount),date(fdCreateTime)");
		sb.append(" from mq_record ");
		sb.append(" where fdType='1' ");
		sb.append(searchWhere);
		sb.append(" group by date(fdCreateTime)");
		sb.append("	union");
		sb.append(" select 0,0,0,0,sum(fdAmount),sum(fdQuantity),sum(fdCoupon),sum(fdCouponCount),sum(fdExpressAmount),sum(fdGoodsAmount),date(fdCreateTime)");
		sb.append(" from mq_record");
		sb.append(" where fdType='2' ");
		sb.append(searchWhere);
		sb.append(" group by date(fdCreateTime)");
		sb.append(" ) a");
		sb.append("group by a.fdCreateTime");
		JSONArray array=new JSONArray();
		List list=dbAccessor.findBySQL(sb.toString());
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object[] item=(Object[]) list.get(i);
				JSONObject o=new JSONObject();
				BigDecimal fdSaleAmount=(BigDecimal) item[0];
				Integer fdSaleCount=(Integer) item[1];
				BigDecimal fdRefundAmount=(BigDecimal) item[2];
				Integer fdRefundCount=(Integer) item[3];
				BigDecimal fdReturnAmount=(BigDecimal) item[4];
				Integer fdReturnCount=(Integer) item[5];
				
				BigDecimal fdCoupon=(BigDecimal) item[6];
				Integer fdCouponCount=(Integer) item[7];
				BigDecimal fdExpressAmount=(BigDecimal) item[8];
				BigDecimal fdGoodsAmount=(BigDecimal) item[9];
				
				BigDecimal fdTotalAmount=fdSaleAmount.add(fdRefundAmount).add(fdReturnAmount);
				int fdTotalQuantity=fdSaleCount+fdRefundCount+fdReturnCount;
				
				o.put("fdSaleAmount", fdSaleAmount);
				o.put("fdSaleCount", fdSaleCount);
				o.put("fdRefundAmount", fdRefundAmount);
				o.put("fdRefundCount", fdRefundCount);
				o.put("fdReturnAmount", fdReturnAmount);
				o.put("fdReturnCount", fdReturnCount);
				o.put("fdCoupon", fdCoupon);
				o.put("fdCouponCount", fdCouponCount);
				o.put("fdExpressAmount", fdExpressAmount);
				o.put("fdGoodsAmount", fdGoodsAmount);
				o.put("fdTotalAmount", fdTotalAmount);
				o.put("fdTotalQuantity", fdTotalQuantity);
				array.add(o);
			}
		}
		result.put("list", array);
		result.put("status", 0);
		return result;
	}

	/**
	 * 查找未付款订单
	 * @return
	 */
	public List noPayOrderOfOneDay() {
		// TODO Auto-generated method stub
		Integer fdCancelOrderMins=30;
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); 
		c.add(Calendar.MINUTE, -fdCancelOrderMins);
		String time=DateUtils.formatDate(c, "yyyy-MM-dd HH:mm:ss");
		String sql="SELECT fdId from app_goods_order where fdCreateTime<'"+time+"' and fdStatus='"+WpUtil.ORDER_STATUS_PAY_NO+"'";
		List list=dbAccessor.findBySQL(sql);
		return list;
	}
	
}
