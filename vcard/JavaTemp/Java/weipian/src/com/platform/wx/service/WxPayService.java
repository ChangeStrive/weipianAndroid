package com.platform.wx.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Refund;
import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.util.FlowNumRuleUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppCharge;
import com.platform.wp.model.AppGoodsOrder;
import com.platform.wp.model.AppGoodsOrderItem;
import com.platform.wp.model.AppGoodsOrderRefunder;
import com.platform.wp.model.AppRecord;
import com.platform.wp.model.AppUser;
import com.platform.wp.util.WpUtil;

/**
 * 订单支付验证
 * @author Administrator
 *
 */
@Component
public class WxPayService extends BaseService {

	private static Logger logger=LoggerFactory.getLogger(WxPayService.class); 
	
	
	/**
	 * 分销抽成
	 * @param item
	 * @param user
	 */
	public void distribution(AppGoodsOrder item,AppUser user){
		String[] fdUpUser=user.getFdUpUser().split(";");
		for(int i=0;i<fdUpUser.length;i++){
			if(i<3){
				
				String[] u=fdUpUser[i].split(".");
				String index=u[0];
				String userId=u[1];
				String rank=WpUtil.getConfigValue("rank"+index);
				if(StringUtil.isNotNull(rank)){
					AppUser up=dbAccessor.get(AppUser.class,userId);
					
					AppRecord record=new AppRecord();
					record.setFdAmount(item.getFdAmount().multiply(new BigDecimal(-1).multiply(new BigDecimal(rank))));
					
					record.setFdCreateTime(DateUtils.getNow());
					record.setFdOrderNo(item.getFdOrderNo());
					
					record.setFdUserId(up.getFdId());
					record.setFdUserName(up.getFdName());
					record.setFdUserCode(up.getFdCode());
					record.setFdRecordNo(FlowNumRuleUtils.getFlowNum("AppRecord"));
					
					record.setFdType("3");
					record.setFdStatus("0");
					dbAccessor.save(record);
				}
			}
		}
	}
	
	/**
	 * 更新会员数
	 * @param fdUpUsers
	 */
	public void updateRankCount(String fdUpUsers){
		String[] fdUpUser=fdUpUsers.split(";");
		for(int i=0;i<fdUpUser.length;i++){
			if(i<3){
				String[] u=fdUpUser[i].split(".");
				String index=u[0];
				String userId=u[1];
				String hql="update AppUser ";
				if(index.equals("1")){
					hql+=" set fdFirstCount=fdFirstCount+1  ";
				}else if(index.equals("2")){
					hql+=" set fdSecondCount=fdSecondCount+1  ";
				}else if(index.equals("3")){
					hql+=" set fdThreeCount=fdThreeCount+1  ";
				}
				hql+=" where fdId='"+userId+"'";
				dbAccessor.bulkUpdate(hql);
			}
		}
	}
	/**
	 * 支付确认
	 * @param fdChargeId
	 * @throws ChannelException 
	 * @throws APIException 
	 * @throws APIConnectionException 
	 * @throws InvalidRequestException 
	 * @throws AuthenticationException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws NoSuchAlgorithmException 
	 */
	public JSONObject pay(String fdChargeId) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, ClientProtocolException, IOException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		Charge charge=WpUtil.selectPayOfWeiXinPublic(fdChargeId);
		if(charge==null){
			result.put("status",WpUtil.RESULT_STATUS_NO);
			result.put("msg","该订单还没有支付");
			return result;
		}
		String orderNo=charge.getOrderNo();
		if(charge!=null&&charge.getPaid()){

			orderNo=orderNo.split("T")[0];
			String hql=" from AppGoodsOrder where fdOrderNo=?";
			List<AppGoodsOrder> list=dbAccessor.find(hql,orderNo);
			if(list!=null&&list.size()>0){
				AppGoodsOrder item=list.get(0);
				if(item.getFdStatus().equals(WpUtil.ORDER_STATUS_PAY_YES)){//已经支付过了
					result.put("status",WpUtil.RESULT_STATUS_YES);
					result.put("msg","支付成功");
					return result;
				}
				int fdAmount=item.getFdAmount().multiply(new BigDecimal(100)).intValue();
				if(fdAmount==charge.getAmount()){//订单金额相等
					if(item.getFdStatus().equals(WpUtil.ORDER_STATUS_PAY_NO)){
						item.setFdStatus(WpUtil.ORDER_STATUS_PAY_YES);
						item.setFdTransactionNo(charge.getTransactionNo());
						item.setFdChargeId(fdChargeId);
						item.setFdPayTime(DateUtils.getNow());
						
						
						hql=" from AppGoodsOrderItem where appGoodsOrder.fdId='"+item.getFdId()+"'";
						List<AppGoodsOrderItem> lists=dbAccessor.find(hql);
						
						
						
						AppRecord record=new AppRecord();
						record.setFdAmount(item.getFdAmount());
						record.setFdCoupon(item.getFdCoupon());
						record.setFdGoodsAmount(item.getFdGoodsAmount());
						record.setFdCreateTime(DateUtils.getNow());
						record.setFdOrderNo(item.getFdOrderNo());
						record.setFdQuantity(item.getFdQuantity());
						record.setFdUserId(item.getFdUserId());
						record.setFdUserName(item.getFdUserName());
						record.setFdUserCode(item.getFdUserCode());
						record.setFdRecordNo(FlowNumRuleUtils.getFlowNum("AppRecord"));
						record.setFdType("0");
						record.setFdStatus("1");
						record.setFdToken(fdChargeId);
						dbAccessor.save(record);
						AppUser user=dbAccessor.get(AppUser.class, item.getFdUserId());
						user.setFdStatus("1");
						user.setFdBuyCount(user.getFdBuyCount()+1);
						if(!item.getFdShopId().equals(item.getFdUserId())){//不是在自己的店购买的商品
							//判断有没有上级商家
							if(!StringUtil.isNotNull(user.getFdUpUser())){
								//没有上级商家，添加上级商家
								String fdUpUser=WpUtil.getUpUser(item.getFdShopId());
								user.setFdUpUser(fdUpUser);
								updateRankCount(fdUpUser);//更新会员等级数量
							}
							if(StringUtil.isNotNull(user.getFdUpUser())){
								distribution(item,user);//分销抽成
							}
						}
						dbAccessor.update(user);
						dbAccessor.update(item);
						if(list!=null&&lists.size()>0){
							for(int i=0;i<lists.size();i++){
								AppGoodsOrderItem orderItem=lists.get(i);
								
								hql="update AppGoods set fdSaleQuantity=fdSaleQuantity+"+orderItem.getFdQuantity()+",fdQuantity=fdQuantity-"+orderItem.getFdQuantity()+" where fdId='"+orderItem.getFdGoodsId()+"'";
								dbAccessor.bulkUpdate(hql);//销售量增加 减库存
								
							}
						}
						
					}
					result.put("status",WpUtil.RESULT_STATUS_YES);
					result.put("msg","支付成功");
					return result;
				}else{
					result.put("status",WpUtil.RESULT_STATUS_NO);
					result.put("msg","异常订单支付!");
					logger.error("异常支付订单，订单编号:"+orderNo+",交易编号："+fdChargeId+",:微信交易编号："+charge.getTransactionNo()+",微信金额："+charge.getAmount()+"分");
				}
			}else{
				result.put("status",WpUtil.RESULT_STATUS_NO);
				result.put("msg","该订单不存在!");
			}
		
		}else{
			result.put("status",WpUtil.RESULT_STATUS_NO);
			result.put("msg","该订单还没有支付");
		}
		return result;
	}

	public JSONObject startCharge(String fdOrderId, String fdOpenId,String fdIp) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppGoodsOrder item=dbAccessor.get(AppGoodsOrder.class, fdOrderId);
		if(item==null){
			result.put("status", WpUtil.RESULT_STATUS_NO);
			result.put("msg","订单不存在");
			return result;
		}
		
		if(!item.getFdStatus().equals(WpUtil.ORDER_STATUS_PAY_NO)){
			result.put("status", WpUtil.RESULT_STATUS_ORDER_STATUS_ERROR);
			result.put("msg","订单状态改变!请重新查看订单");
			return result;
		}
		Charge charge=null;
		try{
			if(StringUtil.isNotNull(fdOpenId)){
				item.setFdOpenId(fdOpenId);
				dbAccessor.update(item);
			}
			if(StringUtil.isNotNull(fdIp)){
				item.setFdIp(fdIp);
				dbAccessor.update(item);
			}
			
			charge=WpUtil.startChargeOfWeiXinPublic(item);
			item.setFdChargeId(charge.getId());
			dbAccessor.update(item);
			
			AppCharge mqCharge=new AppCharge();
			mqCharge.setFdChargeId(charge.getId());
			mqCharge.setFdOrderId(item.getFdId());
			mqCharge.setFdOrderNo(item.getFdOrderNo());
			mqCharge.setFdCreateTime(item.getFdCreateTime());
			dbAccessor.save(mqCharge);
			
			JSONObject chargeObject=JSONObject.fromObject(charge.toString());
			result.put("status",WpUtil.RESULT_STATUS_YES);
			result.put("chargeDetail",chargeObject);
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			result.put("status", WpUtil.RESULT_STATUS_ERROR);
			result.put("msg",WpUtil.RESULT_STATUS_ERROR_MSG);
		}
		return result;
	}


	public AppGoodsOrder getMqGoodsOrder(String fdOrderId) {
		// TODO Auto-generated method stub
		AppGoodsOrder order=dbAccessor.get(AppGoodsOrder.class,fdOrderId);
		return order;
	}

	/**
	 * 订单退款
	 * @param fdRefundId
	 * @return
	 * @throws ChannelException 
	 * @throws APIException 
	 * @throws APIConnectionException 
	 * @throws InvalidRequestException 
	 * @throws AuthenticationException 
	 */
	public JSONObject refund(String fdRefundId) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		String hql=" from AppGoodsOrder where fdRefundId=?";
		List<AppGoodsOrder> list=dbAccessor.find(hql,fdRefundId);
		if(list!=null&&list.size()>0){
			AppGoodsOrder item=list.get(0);
			if(item.getFdStatus().equals(WpUtil.ORDER_STATUS_CANCEL_PAY)){
				result.put("status",WpUtil.RESULT_STATUS_YES);
				result.put("msg","退款成功");
				return result;
			}
			Charge charge=WpUtil.selectPayOfWeiXinPublic(item.getFdChargeId());
			Refund refund=WpUtil.selectRefund(fdRefundId, charge);
			if(refund!=null){
				if(refund.getSucceed()){
					if(item.getFdStatus().equals(WpUtil.ORDER_STATUS_CANCEL_AGREE_APPLY)){
						item.setFdStatus(WpUtil.ORDER_STATUS_CANCEL_PAY);
						dbAccessor.update(item);
						
						hql=" from AppGoodsOrderRefunder where appGoodsOrder.fdId='"+item.getFdId()+"' and fdRefundId='"+fdRefundId+"' and fdStatus='"+WpUtil.REFUNDER_STATUS_PASS+"' order by fdApplyRefundTime desc";
						List<AppGoodsOrderRefunder> rlist=dbAccessor.find(hql,0,1);
						AppGoodsOrderRefunder r=null;
						if(rlist!=null&&rlist.size()>0){
							r=rlist.get(0);
							r.setFdStatus(WpUtil.REFUNDER_STATUS_SUCCESS);
							r.setFdFinishTime(DateUtils.getNow());
							dbAccessor.update(r);
						}
					}
					result.put("status",WpUtil.RESULT_STATUS_YES);
					result.put("msg","退款成功");
					return result;
				}
			}else{
				result.put("status",WpUtil.RESULT_STATUS_NO);
				result.put("msg","退款单不存在！");
			}
		}else{
			result.put("status",WpUtil.RESULT_STATUS_NO);
			result.put("msg","退款单不存在！");
		}
		return result;
	
	}


}
