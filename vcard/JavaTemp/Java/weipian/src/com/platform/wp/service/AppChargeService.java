package com.platform.wp.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.wp.model.AppCharge;

@Component
public class AppChargeService extends BaseService {


	public AppCharge getAppCharge(String fdChargeId) {
		// TODO Auto-generated method stub
		String hql=" from MqCharge where fdChargeId='"+fdChargeId+"'";
		List<AppCharge> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
}
