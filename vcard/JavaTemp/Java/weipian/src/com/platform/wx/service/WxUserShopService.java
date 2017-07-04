package com.platform.wx.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.wp.model.AppUserShop;

/**
 * 店铺
 * @author Administrator
 *
 */
@Component
public class WxUserShopService extends BaseService {

	
	/**
	 * 获得店铺信息
	 * @param fdUserId
	 * @return
	 */
	public AppUserShop getUserShopMessageByUserId(String fdUserId) {
		// TODO Auto-generated method stub
		String hql=" from AppUserShop where fdUserId='"+fdUserId+"' ";
		List<AppUserShop> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 获得店铺信息
	 * @param fdUserId
	 * @return
	 */
	public AppUserShop getUserShopMessageByUserCode(String fdUserCode) {
		// TODO Auto-generated method stub
		String hql=" from AppUserShop where fdUserCode='"+fdUserCode+"' ";
		List<AppUserShop> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 获得店铺信息
	 * @param fdUserId
	 * @return
	 */
	public void save(AppUserShop model) {
		// TODO Auto-generated method stub
		dbAccessor.save(model);
	}
	
	public void update(AppUserShop model) throws IllegalArgumentException, IllegalAccessException {
		AppUserShop item = dbAccessor.get(AppUserShop.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}
}
