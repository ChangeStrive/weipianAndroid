package com.platform.wp.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.wp.model.AppGoodsOrderRefunder;

@Component
public class AppGoodsOrderRefunderService extends BaseService {

	public List<AppGoodsOrderRefunder> list(String fdOrderId){
		String hql=" from AppGoodsOrderRefunder where 1=1 ";
		hql+=" and appGoodsOrder.fdId='"+fdOrderId+"'";
		hql+=" order by fdApplyRefundTime desc";
		List<AppGoodsOrderRefunder> list=dbAccessor.find(hql);
		return list;
	}
}
