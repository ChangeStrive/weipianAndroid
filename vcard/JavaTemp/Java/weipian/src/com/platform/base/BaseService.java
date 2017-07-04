package com.platform.base;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseService {
	
	@Autowired
	protected DbAccessor dbAccessor=null;
	
	public void setDbAccessor(DbAccessor dbAccessor){
		this.dbAccessor=dbAccessor;
	}
}
