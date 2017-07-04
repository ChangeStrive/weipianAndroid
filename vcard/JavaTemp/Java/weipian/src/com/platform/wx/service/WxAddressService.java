package com.platform.wx.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppUserAddress;

/**
 *
 * @author hyw
 *
 */
@Component
public class WxAddressService extends BaseService {

	/**
	 * 收货地址列表
	 * @return
	 */
	public JSONObject addressList(String fdUserId) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		String hql=" from AppUserAddress where fdUserId='"+fdUserId+"' order by fdStatus desc,fdCreateTime desc";
		List<AppUserAddress> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppUserAddress item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdAddress", item.getFdAddress());
				o.put("fdConsignee", item.getFdConsignee());
				o.put("fdStatus", item.getFdStatus());
				o.put("fdTel", item.getFdTel());
				o.put("fdZipCode", item.getFdZipCode());
				o.put("fdProvince", item.getFdProvince());
				o.put("fdCity", item.getFdCity());
				o.put("fdArea", item.getFdArea());
				array.add(o);
			}
		}
		result.put("status",0);
		result.put("addressSize",list.size());
		result.put("list", array);
		return result;
	}

	/**
	 * 删除收货地址
	 * @param fdUserId
	 * @param fdAddressId
	 * @return
	 */
	public JSONObject delAddressList(String fdUserId, String fdAddressId) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		String hql="delete from AppUserAddress where fdUserId='"+fdUserId+"' and fdId='"+fdAddressId+"'";
		dbAccessor.bulkUpdate(hql);
		result.put("status",0);
		result.put("msg", "操作成功");
		return result;
	}

	/**
	 * 保存收货地址
	 * @param fdUserId
	 * @param item
	 * @return
	 */
	public JSONObject saveAddress(String fdUserId, AppUserAddress item) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		if(StringUtil.isNotNull(item.getFdId())){
			item.setFdUserId(fdUserId);
			item.setFdCreateTime(DateUtils.getNow());
			dbAccessor.update(item);
		}else{
			item.setFdUserId(fdUserId);
			item.setFdCreateTime(DateUtils.getNow());
			dbAccessor.save(item);
		}
		if(item.getFdStatus().equals("1")){
			String hql="update AppUserAddress set fdStatus='0' where fdId!='"+item.getFdId()+"'";
			dbAccessor.bulkUpdate(hql);
		}
		result.put("fdAddressId",item.getFdId());
		result.put("status",0);
		result.put("msg", "操作成功");
		return result;
	}

	/**
	 * 获得默认地址
	 * @param fdUserId
	 * @return
	 */
	public JSONObject getDefaultAddress(String fdUserId,String fdAddressId) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		String hql=" from AppUserAddress where fdUserId='"+fdUserId+"' ";
		if(StringUtil.isNotNull(fdAddressId)){
			hql+=" and fdId='"+fdAddressId+"'";
		}
		hql+=" order by fdStatus desc,fdCreateTime desc";
		List<AppUserAddress> list=dbAccessor.find(hql,0,1);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppUserAddress item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdAddress", item.getFdAddress());
				o.put("fdConsignee", item.getFdConsignee());
				o.put("fdStatus", item.getFdStatus());
				o.put("fdTel", item.getFdTel());
				o.put("fdZipCode", item.getFdZipCode());
				o.put("fdProvince", item.getFdProvince());
				o.put("fdCity", item.getFdCity());
				o.put("fdArea", item.getFdArea());
				result.put("defaultAddress",o);
			}
			result.put("hasAddress", 1);
		}else{
			result.put("hasAddress", 0);
		}
		result.put("status",0);
		return result;
	}

	public AppUserAddress get(String fdId) {
		// TODO Auto-generated method stub
		AppUserAddress address=dbAccessor.get(AppUserAddress.class, fdId);
		return address;
	}

	/**
	 * 修改地址默认状态
	 * @param fdId
	 * @param fdStatus
	 * @return
	 */
	public JSONObject addressStatus(String fdId, String fdStatus) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		String hql="update AppUserAddress set fdStatus='"+fdStatus+"' where fdId='"+fdId+"'";
		dbAccessor.bulkUpdate(hql);
		if(fdStatus.equals("1")){
			hql="update AppUserAddress set fdStatus='0' where fdId!='"+fdId+"'";
			dbAccessor.bulkUpdate(hql);
		}
		result.put("status",0);
		result.put("msg", "操作成功");
		return result;
	}
	
}
