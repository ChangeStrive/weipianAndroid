package com.platform.base;

import java.util.Collection;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateOperations;

/**
 * 数据库访问器接口
 * @author
 * <br/>创建日期 2011-7-26
 * <br/>最后修改 2011-11-14
 * @version 1.1
 */
@SuppressWarnings("unchecked")
public interface DbAccessor extends HibernateOperations{
	
	/**
	 * 创建hql查询
	 * @param hql
	 * @return
	 */
	public Query createQuery(String hql);
	
	/**
	 * 执行sql查询
	 * @param sql
	 * @param params
	 * @return
	 */
	public List findBySQL(String sql, Object... params);
	
	/**
	 * 执行带分页的sql查询
	 * @param sql
	 * @param firstResult
	 * @param maxResults
	 * @param params
	 * @return
	 */
	public List findBySQL(String sql, int firstResult, int maxResults, Object... params);
	
	/**
	 * 创建Criteria
	 * @param entityClass 实体类
	 * @return
	 */
	public Criteria creaeteCriteria(Class entityClass);
	
	/**
	 * 创建Criteria
	 * @param entityClass 实体类
	 * @param alias  别名
	 * @return
	 */
	public Criteria creaeteCriteria(Class entityClass, String alias);
	
	/**
	 * 创建Sql查询
	 * @param sql
	 * @return
	 */
	public SQLQuery createSQLQuery(String sql);
	
	/**
	 * 获取行数
	 * @param criteria
	 * @return
	 */
	public int getRowCount(DetachedCriteria criteria);
	
	/**
	 * 取得实体主键名
	 * @param entityClass
	 * @return
	 */
	public String getIdName(Class entityClass);
	
	/**
	 * sql查询单个结果
	 * @param hql
	 * @param params
	 * @return
	 */
	public Object uniqueResultBySQL(String sql, Object... params);
	
	/**
	 * hql查询单个结果
	 * @param hql
	 * @param params
	 * @return
	 */
	public Object uniqueResultByHQL(String hql, Object... params);
	
	/**
	 * 创建Criteria
	 * @param entityName
	 * @return
	 */
	
	public Criteria creaeteCriteria(String entityName);
	
	/**
	 * 创建Criteria
	 * @param entityName 实体名
	 * @param alias 别名
	 * @return
	 */
	public Criteria creaeteCriteria(String entityName, String alias);
	
	/**
	 * hql分页查询
	 * @param hql
	 * @param firstResult
	 * @param maxResults
	 * @param params
	 * @return
	 */
	public List find(String hql, int firstResult, int maxResults, Object... params);

	/**
	 *  带命名参数hql查询单个结果
	 * @param hql
	 * @param paramName 参数名
	 * @param value	值
	 * @return
	 */
	Object uniqueResultByNamedParamHQL(String hql, String paramName,
			Object value);
	/**
	 * 带命名参数hql查询单个结果
	 * @param hql
	 * @param paramNames 
	 * @param values
	 * @return
	 */
	Object uniqueResultByNamedParamHQL(String hql, String[] paramNames,
			Object[] values);
	
	/**
	 * 带命名参数sql查询单个结果
	 * @param sql
	 * @param paramNames
	 * @param values
	 * @return
	 */
	Object uniqueResultByNamedParamSQL(String sql, String[] paramNames,
			Object[] values);
	
	/**
	 * 带命名参数sql查询单个结果
	 * @param sql
	 * @param paramName
	 * @param value
	 * @return
	 */
	Object uniqueResultByNamedParamSQL(String sql, String paramName,
			Object value);
	
	/**
	 *  执行带命名参数sql查询
	 * @param sql
	 * @param paramName
	 * @param value
	 * @return
	 */
	List findByNamedParamSQL(String sql, String paramName, Object value);
	/**
	 * 执行带命名参数sql查询
	 * @param sql
	 * @param paramNames
	 * @param values
	 * @return
	 */
	List findByNamedParamSQL(String sql, String[] paramNames, Object[] values);
	
	/**
	 * 执行带命名参数sql分页查询
	 * @param sql
	 * @param firstResult
	 * @param maxResults
	 * @param paramName
	 * @param value
	 * @return
	 */
	List findByNamedParamSQL(String sql, int firstResult, int maxResults,
			String paramName, Object value);
	/**
	 * 执行带命名参数sql分页查询
	 * @param sql
	 * @param firstResult
	 * @param maxResults
	 * @param paramNames
	 * @param values
	 * @return
	 */
	List findByNamedParamSQL(String sql, int firstResult, int maxResults,
			String[] paramNames, Object[] values);
	/**
	 * 执行sql
	 * @param sql
	 * @param params
	 * @return 受影响的记录数
	 */
	int executeSQL(String sql, Object... params);
	
	/**
	 * criteria查询,使用缓存
	 * @param criteria
	 * @return
	 */
	List findByCriteriaUseCache(DetachedCriteria criteria);
	
	/**
	 * criteria查询,使用缓存
	 * @param criteria
	 * @param cacheMode
	 * @param cacheRegion
	 * @return
	 */
	List findByCriteriaUseCache(DetachedCriteria criteria, CacheMode cacheMode,
			String cacheRegion);
	
	<T> List<T> findBySQL(String sql, Class<T> clazz,Object... params);
	
	<T> List<T> findBySQL(String sql, Class<T> clazz, int firstResult,
			int maxResults, Object... params);

	Object uniqueResultByCriteria(DetachedCriteria criteria);

	boolean hasProperty(Class clazz, String property);

	void addOrder(DetachedCriteria cri, String propertyName, String dir);

	List filterCollection(Collection collection, String queryString,
			Object... params);

	List filterCollection(Collection collection, String queryString,
			int firstResult, int maxResults, Object... params);
	
	<T> T executeWithNewSession(HibernateCallback<T> action);

	int bulkUpdateByNamedParams(String hql, String paramName, Object value);

	int bulkUpdateByNamedParams(String hql, String[] paramNames, Object[] values);
}
