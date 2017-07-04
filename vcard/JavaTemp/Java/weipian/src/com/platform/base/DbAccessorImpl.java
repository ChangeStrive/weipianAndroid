package com.platform.base;



import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * 数据库访问器实现
 * <br />创建日期 2011-7-26
 * <br />最后修改 2011-11-14
 * @version 1.1
 */
public class DbAccessorImpl extends HibernateTemplate implements DbAccessor {
	
	
	public Query createQuery(String hql){
		return getSession().createQuery(hql);
	}
	
	
	public SQLQuery createSQLQuery(String sql){
		return getSession().createSQLQuery(sql);
	}
	
	
	@SuppressWarnings("unchecked")
	public Criteria creaeteCriteria(Class entityClass){
		return getSession().createCriteria(entityClass);
	}
	
	
	@SuppressWarnings("unchecked")
	public Criteria creaeteCriteria(Class entityClass,String alias){
		return getSession().createCriteria(entityClass,alias);
	}
	
	
	public Criteria creaeteCriteria(String entityName){
		return getSession().createCriteria(entityName);
	}
	
	
	public Criteria creaeteCriteria(String entityName,String alias){
		return getSession().createCriteria(entityName,alias);
	}
	
	
	@SuppressWarnings("unchecked")
	public List findByNamedParamSQL(final String sql,final String paramName,final Object value){
		return findByNamedParamSQL(sql,new String[]{paramName},new Object[]{value});
	}
	
	
	@SuppressWarnings("unchecked")
	public List findByNamedParamSQL(final String sql,final String[] paramNames,final Object[] values){
		if (values!=null&&paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		return executeWithNativeSession(new HibernateCallback<List>() {
			
			public List doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query=session.createSQLQuery(sql);
				if(values!=null){
					for(int i=0;i<values.length;i++){
						applyNamedParameterToQuery(query, paramNames[i], values[i]);
					}
				}
				return query.list();
			}
			
		});		
	}
	
	
	public <T> List<T> findBySQL(final String sql,final Class<T> clazz,final Object... params){	
		return findBySQL(sql, clazz, -1, -1, params);
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findBySQL(final String sql,final Class<T> clazz,final int firstResult,final int maxResults,final Object... params){	
		return executeWithNativeSession(new HibernateCallback<List<T>>() {
			
			public List<T> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query=session.createSQLQuery(sql);
				query.addEntity(clazz);
				applyUnNamedParameterToQuery(query, params);
				applyPage(query, firstResult, maxResults);
				return query.list();
			}
		});
	}
	
	
	@SuppressWarnings("unchecked")
	public List findBySQL(final String sql,final Object... params){
		return findBySQL(sql,-1,-1,params);
	}
	
	
	@SuppressWarnings("unchecked")
	public List findBySQL(final String sql,final int firstResult,final int maxResults,final Object... params){
		return executeWithNativeSession(new HibernateCallback<List>() {
			
			public List doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query=session.createSQLQuery(sql);
				applyUnNamedParameterToQuery(query, params);
				applyPage(query, firstResult, maxResults);
				return query.list();
			}
		});
	}
	
	
	@SuppressWarnings("unchecked")
	public List findByNamedParamSQL(final String sql,final int firstResult,final int maxResults,final String paramName,final Object value){
		return findByNamedParamSQL(sql,firstResult,maxResults,new String[]{paramName},new Object[]{value});
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List findByNamedParamSQL(final String sql,final int firstResult,final int maxResults,final String[] paramNames,final Object[] values){
		if (values!=null&&paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		return executeWithNativeSession(new HibernateCallback<List>() {
			
			public List doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query=session.createSQLQuery(sql);
				if(values!=null){
					for(int i=0;i<values.length;i++){
						applyNamedParameterToQuery(query, paramNames[i], values[i]);
					}
				}
				applyPage(query, firstResult, maxResults);
				return query.list();
			}
			
		});		
	}
	
	@SuppressWarnings("unchecked")
	
	public List find(final String hql,final int firstResult,final int maxResults,final Object... params){
		return executeWithNativeSession(new HibernateCallback<List>() {
			
			public List doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query=session.createQuery(hql);
				applyUnNamedParameterToQuery(query, params);
				applyPage(query, firstResult, maxResults);
				return query.list();
			}
			
		});
	}
	
	
	public int getRowCount(final DetachedCriteria criteria){
		return executeWithNativeSession(new HibernateCallback<Integer>() {
			
			public Integer doInHibernate(Session session) throws HibernateException,
					SQLException {
				criteria.setProjection(Projections.rowCount());
				Number count=(Number) criteria.getExecutableCriteria(session).uniqueResult();
				if(count==null)
					return 0;
				else
					return count.intValue();
			}
		});
	}
	
	
	@SuppressWarnings("unchecked")
	public  List filterCollection(final Collection collection,final String queryString,final Object... params){
		return filterCollection(collection,queryString,-1,-1,params);
	}
	
	
	@SuppressWarnings("unchecked")
	public  List filterCollection(final Collection collection,final String queryString,final int firstResult,final int maxResults,final Object... params){
		return executeWithNativeSession(new HibernateCallback<List>() {
			
			public List doInHibernate(Session session)
					throws HibernateException, SQLException {
					Query query=session.createFilter(collection, queryString);
					applyUnNamedParameterToQuery(query, params);
					applyPage(query, firstResult, maxResults);
				return query.list();
			}
		});
	}
	

	
	@SuppressWarnings("unchecked")
	public String getIdName(Class entityClass) {
		ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
		return meta.getIdentifierPropertyName();
	}
	
	
	public Object uniqueResultByCriteria(final DetachedCriteria criteria){
		return executeWithNativeSession(new HibernateCallback<Object>() {

			
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return criteria.getExecutableCriteria(session).uniqueResult();				
			}
		});
	}
	
	
	public Object uniqueResultByHQL(final String hql,final Object... params){
		return executeWithNativeSession(new HibernateCallback<Object>() {
			
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query=session.createQuery(hql);
				applyUnNamedParameterToQuery(query, params);
				return query.uniqueResult();
			}
		});
	}
	
	
	public Object uniqueResultByNamedParamHQL(final String hql,final String paramName,final Object value){
		return uniqueResultByNamedParamHQL(hql, new String[]{paramName},new Object[]{value});
	}
	
	
	public Object uniqueResultByNamedParamHQL(final String hql,final String[] paramNames,final Object[] values){
		if (values!=null&&paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		return executeWithNativeSession(new HibernateCallback<Object>() {
			
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query=session.createQuery(hql);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						applyNamedParameterToQuery(query, paramNames[i], values[i]);
					}
				}
				return query.uniqueResult();
			}
		});
	}
	
	
	public Object uniqueResultBySQL(final String sql,final Object... params){
		return executeWithNativeSession(new HibernateCallback<Object>() {
			
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query=session.createSQLQuery(sql);
				applyUnNamedParameterToQuery(query, params);
				return query.uniqueResult();
			}
		});
	}
	
	
	public Object uniqueResultByNamedParamSQL(final String sql,final String paramName,final Object value){
		return uniqueResultByNamedParamSQL(sql,new String[]{paramName},new Object[]{value});
	}
	
	
	public Object uniqueResultByNamedParamSQL(final String sql,final String[] paramNames,final Object[] values){
		if (values!=null&&paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		return executeWithNativeSession(new HibernateCallback<Object>() {
			
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query=session.createSQLQuery(sql);
				if(values!=null){
					for(int i=0;i<values.length;i++){
						applyNamedParameterToQuery(query, paramNames[i], values[i]);
					}
				}
				return query.uniqueResult();
			}
		});
	}
	
	
	public int executeSQL(final String  sql,final Object... params){
		return executeWithNativeSession(new HibernateCallback<Integer>() {
			
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query=session.createSQLQuery(sql);
				applyUnNamedParameterToQuery(query, params);
				return query.executeUpdate();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	
	public List findByCriteriaUseCache(final DetachedCriteria criteria){
		return findByCriteriaUseCache(criteria, null, null);
	}
	
	@SuppressWarnings("unchecked")
	
	public List findByCriteriaUseCache(final DetachedCriteria criteria,final CacheMode cacheMode,final String cacheRegion){
		return executeWithNativeSession(new HibernateCallback<List>() {

			
			public List doInHibernate(Session session) throws HibernateException,
					SQLException {
				Criteria cri=criteria.getExecutableCriteria(session);
				cri.setCacheable(true);
				if(cacheMode!=null)
					cri.setCacheMode(cacheMode);
				if(cacheRegion!=null)
					cri.setCacheRegion(cacheRegion);
				return cri.list();
			}
		});
	}
		
	
	@SuppressWarnings("unchecked")
	public boolean hasProperty(Class clazz,String property){
		String cname=clazz.getName();
		ClassMetadata cm=getSessionFactory().getClassMetadata(cname);
		if(cm==null){
			throw new MappingException( "Unknown entity: " + cname);
		}
		try{
			Type type=null;
			int dp=property.indexOf('.');
			if(dp>-1){
				 type=cm.getPropertyType(property.substring(0, dp));
			}
			else type=cm.getPropertyType(property);
			if(type!=null){
				if(dp>-1){
					try{
						return hasProperty(type.getReturnedClass(),property.substring(dp+1));
					}
					catch (Exception e) {
						return false;
					}
				}
				return true;
			}
			else{
				return false;
			}
		}
		catch(HibernateException ex){
			return false;
		}
	}
	
	
	public void addOrder(DetachedCriteria cri,String propertyName,String dir){
		if("desc".equalsIgnoreCase(dir)){
			cri.addOrder(Order.desc(propertyName));
		}
		else{
			cri.addOrder(Order.asc(propertyName));
		} 
	}
	
	
	public int bulkUpdateByNamedParams(final String hql,final String paramName,final Object value){
		return bulkUpdateByNamedParams(hql,new String[]{paramName},new Object[]{value});
	}
	
	
	public int bulkUpdateByNamedParams(final String hql,final String[] paramNames,final Object[] values){
		return executeWithNativeSession(new HibernateCallback<Integer>() {

			
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if(values!=null){
					for(int i=0;i<values.length;i++){
						applyNamedParameterToQuery(query, paramNames[i], values[i]);
					}
				}
				return query.executeUpdate();
			}
			
		});
	}
	
	protected void applyPage(Query query,int firstResult,int maxResults){
		if(firstResult>=0){
			query.setFirstResult(firstResult);
		}
		if(maxResults>0){
			query.setMaxResults(maxResults);
		}
	}
	
	protected int applyUnNamedParameterToQuery(Query query,Object[] params){
		return applyUnNamedParameterToQuery(query,0,params);
	}
	
	protected int applyUnNamedParameterToQuery(Query query,int start,Object[] params){
		int i=0;
		if(params!=null){
			for(;i<params.length;i++){
				query.setParameter(i+start, params[i]);
			}
		}
		return i+start;
	}
		
}
