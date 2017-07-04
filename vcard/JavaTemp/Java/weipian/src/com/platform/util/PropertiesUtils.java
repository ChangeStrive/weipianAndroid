package com.platform.util;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.BeanUtils;
/**
 * @action 用于简单的属性值互相拷贝
 * @author YWX
 *
 */
public class PropertiesUtils {
	/**
	 * @action  1.如果属性值为“”则把值存入到要拷贝到的对象相应的属性值中
	 * 			2.如果属性值为null则要拷贝到的对象值不变
	 * 			3.如果属性值为存在不为空的值则把值存入到要拷给到的对象相应的属性中
	 * 			4.如果是set集合要把关系去掉，则要在这方法后面多个setXX(null)的方法
	 * @param toobj
	 * @param fromobj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void copyProperties(Object toobj,Object fromobj) throws IllegalArgumentException, IllegalAccessException{
		//取得属性值数组
		Field[] formField = fromobj.getClass().getDeclaredFields();
		Field[] toField = toobj.getClass().getDeclaredFields();
		//不同类对应的类实例的情况下
		if(toobj.getClass()!=fromobj.getClass()){
			for (int i = 0; i < formField.length; i++) {
				if(Modifier.isStatic(formField[i].getModifiers())) continue;
				for (int j = 0; j < toField.length; j++) {
					if(formField[i].getName().equals(toField[j].getName())){
						//获取访问权限
						boolean a = formField[i].isAccessible();
						boolean b = toField[j].isAccessible();
						//设置访问权限
						formField[i].setAccessible(true);
						toField[j].setAccessible(true);
						//3种情况的判断
						if("".equals(formField[i].get(fromobj))){
							toField[j].set(toobj, formField[i].get(fromobj));
						}else if(formField[i].get(fromobj)==null){
							toField[j].set(toobj, toField[j].get(toobj));
						}else{
							if(formField[i].getType().isInterface()){
								if(formField[i].get(fromobj).toString().length()==2){
									toField[j].set(toobj, toField[j].get(toobj));
								}else{
									toField[j].set(toobj, formField[i].get(fromobj));
								}	
							}else{
								toField[j].set(toobj, formField[i].get(fromobj));
							}
							
						}
						//还原访问权限
						formField[i].setAccessible(a);
						toField[j].setAccessible(b);
					}
				}
			}
//			throw new IllegalArgumentException("2个对象不属于同一个类");
		//同一个类对应的类实例
		}else{
			for (int i = 0; i < formField.length; i++) {
				if(Modifier.isStatic(formField[i].getModifiers())) continue;
				boolean a = formField[i].isAccessible();
				formField[i].setAccessible(true);
				if("".equals(formField[i].get(fromobj))){
					formField[i].set(toobj, formField[i].get(fromobj));
				}else if(formField[i].get(fromobj)==null){
					formField[i].set(toobj, formField[i].get(toobj));
				}else{
					if(formField[i].getType().isInterface()){
						if(formField[i].get(fromobj).toString().length()==2){
							formField[i].set(toobj, formField[i].get(toobj));
						}else{
							formField[i].set(toobj, formField[i].get(fromobj));
						}	
					}else{
						formField[i].set(toobj, formField[i].get(fromobj));
					}
				}
				formField[i].setAccessible(a);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Set getPropertyValues(Collection objs,String propertyName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if(objs==null||objs.size()==0) return Collections.EMPTY_SET;
		Set resultSet=new HashSet();
		Iterator itor=objs.iterator();
		Object obj1=itor.next();
		PropertyDescriptor pd=BeanUtils.getPropertyDescriptor(obj1.getClass(),propertyName);
		if(pd==null){
			throw new RuntimeException(obj1.getClass().getName()+" 没有 "+propertyName+"属性");
		}
		Method rm=pd.getReadMethod();
		if(rm==null){
			throw new RuntimeException(obj1.getClass().getName()+" 属性 "+propertyName+"不可访问");
		}
		Object val=rm.invoke(obj1);
		if(val!=null&&!"".equals(val))
				resultSet.add(val);
		while(itor.hasNext()){
			val=rm.invoke(itor.next());
			if(val!=null&&!"".equals(val))
				resultSet.add(val);
		}
		return resultSet;
	}
	
	public static void main(String[] args){
	//	Formatter f=new Formatter();
		//System.out.println(String.format(Config.getProperty("sms.jianbi"), "asa",DateUtils.getToday()));
	}
}
