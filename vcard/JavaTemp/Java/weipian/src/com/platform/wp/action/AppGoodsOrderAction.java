package com.platform.wp.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.base.BaseAction;
import com.platform.sys.context.AppContextImpl;
import com.platform.sys.model.SysUser;
import com.platform.util.DateUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppGoodsOrder;
import com.platform.wp.model.AppGoodsOrderRefunder;
import com.platform.wp.service.AppGoodsOrderRefunderService;
import com.platform.wp.service.AppGoodsOrderService;
import com.platform.wp.util.WpUtil;

@Controller
@RequestMapping("/AppGoodsOrderAction")
public class AppGoodsOrderAction extends BaseAction{
	
	
	@Autowired
	private AppGoodsOrderService service = null;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response){
		return "Admin/jsp/app/AppGoodsOrder/AppGoodsOrderList.html";
	}
	
	/**
	 * 所有订单
	 * @param model
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/getList")
	public void getList(AppGoodsOrder model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String,String> map=StringUtil.getParams(request, "fdStatus,fdStartDate,fdEndDate");
		getList1(model,map,request,response);
		
	}
	public void getList1(AppGoodsOrder model,Map<String,String> map,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String strStart=request.getParameter("start");
		String strLimit=request.getParameter("limit");
		int start=0;
		int limit=pageSize;
		if(StringUtil.isNotNull(strStart)){
			start=Integer.parseInt(strStart);
		}
		
		if(StringUtil.isNotNull(strLimit)){
			limit=Integer.parseInt(strLimit);
		}
		SysUser user=getCurrentUser(request);
		List<AppGoodsOrder> list=service.list(map,model,start, limit);
		int count=service.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppGoodsOrder item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdOrderNo", item.getFdOrderNo());
				o.put("fdUserId", item.getFdUserId());
				o.put("fdUserCode", item.getFdUserCode());
				o.put("fdUserName", item.getFdUserName()); 
				o.put("fdCoupon", item.getFdCoupon()); 
				o.put("fdAmount", item.getFdAmount()); 
				
				
				o.put("fdOrderDetail", item.getFdOrderDetail());
				o.put("fdStatus", item.getFdStatus());
				o.put("fdCreateTime", item.getFdCreateTime());
				
				o.put("fdConsignee", item.getFdConsignee()); 
				o.put("fdTel", item.getFdTel()); 
				o.put("fdZipCode", item.getFdZipCode()); 
				o.put("fdAddress", item.getFdAddress()); 
				
				o.put("fdExpressName", item.getFdExpressName()); 
				o.put("fdExpressNo", item.getFdExpressNo()); 
				
				o.put("fdApplyRefundReason", item.getFdApplyRefundReason()); 
				o.put("fdApplyRefundTime", item.getFdApplyRefundTime()); 
				o.put("fdShopId", item.getFdShopId()); 
				o.put("fdShopName", item.getFdShopName()); 
				o.put("fdShopNo", item.getFdShopNo()); 
				
				o.put("fdStatusStr",  WpUtil.getOrderStatusStr(item.getFdStatus()));
				array.add(o);
			}
		}
		object.put("totalSize", count);
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}
	
	

	@RequestMapping("/save")
	public void save(AppGoodsOrder model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(model.getFdId())){
				service.update(model);
			}else{
				model.setFdCreateTime(DateUtils.getNow());
				service.save(model);
			}
			result.put("success", true);
			result.put("msg", "保存成功!");
		}catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "保存失败!");
		}
		writeJSON(response,result.toString());
	}
	
	@RequestMapping("/delete")
	public void delete(String fdId,HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(fdId)){
				service.delete(request,fdId);
			}
			result.put("success", true);
			result.put("msg", "删除成功!");
		}catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "删除失败!");
		}
		writeJSON(response,result.toString());
	}
	
	/**
	 * 查看订单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/view")
	public String view(HttpServletRequest request,HttpServletResponse response){
		String fdId=request.getParameter("fdId");
		String url="Admin/jsp/app/AppOrderDetail/AppGoodsDetails.html";
		if(StringUtil.isNotNull(fdId)){
			AppGoodsOrder item = service.get(fdId);
			request.setAttribute("item", item);
			request.setAttribute("statusStr", WpUtil.getOrderStatusStr(item.getFdStatus()));
			JSONObject orderDetail=JSONObject.fromObject(item.getFdOrderDetail());
			request.setAttribute("orderDetail", orderDetail);
			
			AppGoodsOrderRefunderService rsvc = AppContextImpl.getInstance().getBean(AppGoodsOrderRefunderService.class);
			List<AppGoodsOrderRefunder> refunerList=rsvc.list(fdId);
			request.setAttribute("refunerList", refunerList);
		}else{
			url="Admin/jsp/app/AppOrderDetail/AppGoodsDetailsError.html";
		}
		return url;
	}
	
	/**
	 *退款单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toReturnOrder")
	public String toReturnOrder(HttpServletRequest request,HttpServletResponse response){
		return "Admin/jsp/app/toReturnOrder/toReturnOrder.html";
	}
	
	/**
	 *退款订申请
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toApplyReturnOrder")
	public String toApplyReturnOrder(HttpServletRequest request,HttpServletResponse response){
		return "Admin/jsp/app/toApplyReturnOrder/toApplyReturnOrder.html";
	}
	
	/**
	 *退款订申请列表
	 * @param model
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/toApplyReturnOrderList")
	public void toApplyReturnOrderList(AppGoodsOrder model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String,String> map=StringUtil.getParams(request, "fdStartDate,fdEndDate");
		map.put("fdStatus", "4");
		map.put("orderBy", "fdApplyRefundTime desc");
		getList1(model,map,request,response);
	}
	
	/**
	 *退款订申请列表
	 * @param model
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/returnOrderList")
	public void returnOrderList(AppGoodsOrder model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String,String> map=StringUtil.getParams(request, "fdStartDate,fdEndDate");
		map.put("fdStatus", "5,-2");
		map.put("orderBy", "fdApplyRefundTime desc");
		getList1(model,map,request,response);
	}
	
	/**
	 * 同意退款
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/agreeRefund")
	public void agreeRefund(HttpServletRequest request,HttpServletResponse response) throws  IOException{
		String fdOrderId=request.getParameter("fdOrderId");//订单id
		JSONObject result=new JSONObject();
		SysUser user=getCurrentUser(request);
		try{
			result=service.agreeRefund(fdOrderId,user);
		}catch (Exception e) {
			e.printStackTrace();
			result=new JSONObject();
			result.put("status", WpUtil.RESULT_STATUS_ERROR);
			result.put("msg", WpUtil.RESULT_STATUS_ERROR_MSG);
		}
		writeJSON(response,result.toString());
	}
	
	/**
	 * 不同意退款
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/noAgreeRefund")
	public void noAgreeRefund(HttpServletRequest request,HttpServletResponse response) throws  IOException{
		String fdOrderId=request.getParameter("fdOrderId");//订单id
		String fdBackReason=request.getParameter("fdBackReason");//订单id
		JSONObject result=new JSONObject();
		SysUser user=getCurrentUser(request);
		try{
			result=service.noAgreeRefund(fdOrderId,user,fdBackReason);
		}catch (Exception e) {
			result=new JSONObject();
			result.put("status", WpUtil.RESULT_STATUS_ERROR);
			result.put("msg", WpUtil.RESULT_STATUS_ERROR_MSG);
		}
		writeJSON(response,result.toString());
	}
	
	/**
	 * 快递发货
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/deliver")
	public void deliver(HttpServletRequest request,HttpServletResponse response) throws  IOException{
		String fdOrderId=request.getParameter("fdOrderId");//订单id
		String fdExpressNo=request.getParameter("fdExpressNo");//快递编号
		String fdExpressName=request.getParameter("fdExpressName");//快递名称
		JSONObject result=new JSONObject();
		SysUser user=getCurrentUser(request);
		try{
			result=service.deliver(fdOrderId,user,fdExpressName,fdExpressNo);
		}catch (Exception e) {
			result=new JSONObject();
			result.put("status", WpUtil.RESULT_STATUS_ERROR);
			result.put("msg", WpUtil.RESULT_STATUS_ERROR_MSG);
		}
		writeJSON(response,result.toString());
	}
	
	
	/**
	 * 确定收货
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/sureReceiveGoods")
	public void sureReceiveGoods(HttpServletRequest request,HttpServletResponse response) throws  IOException{
		String fdOrderId=request.getParameter("fdOrderId");//订单id
		JSONObject result=new JSONObject();
		try{
			result=service.sureReceiveGoods(fdOrderId);
		}catch (Exception e) {
			result=new JSONObject();
			result.put("status", WpUtil.RESULT_STATUS_ERROR);
			result.put("msg", WpUtil.RESULT_STATUS_ERROR_MSG);
		}
		writeJSON(response,result.toString());
	}
	
	/**
	 * 取消订单(已付款)
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/cancelOrderOfPay")
	public void cancelOrderOfPay(HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		String fdOrderId=request.getParameter("fdOrderId");//订单id
		String fdCancelReason=request.getParameter("fdCancelReason");//订单id
		SysUser user=getCurrentUser(request);
		try{
			result=service.cancelOrderOfPay(fdOrderId,fdCancelReason,user);
		}catch (Exception e) {
			result=new JSONObject();
			result.put("status", WpUtil.RESULT_STATUS_ERROR);
			result.put("msg", WpUtil.RESULT_STATUS_ERROR_MSG);
		}
		writeJSON(response,result.toString());
	}
	
}
