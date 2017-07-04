package com.platform.sys.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.sys.model.SysDict;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;

@Component
public class SysDictService extends BaseService {
	
	
	/**
	 * 根据父Id查找字典的内容
	 * @param fdFunId
	 * @return
	 */
	public List<SysDict>  getListByParentId(String fdFunId){
		String hql = "from SysDict where fdFunId=? order  by fdSeqNo asc";
		List<SysDict> list=dbAccessor.find(hql,fdFunId);
		return list;
	}
	
	/**
	 * 根据fdValue查找字典的内容
	 * @param fdValue
	 * @return
	 */
	public List<SysDict> getListByValue(String fdValue) {
		String hql=" from SysDict dict where EXISTS(";
		hql+=" select sysDict.fdId from SysDict sysDict where fdValue='"+fdValue+"' and sysDict.fdId=dict.fdFunId";
		hql+=")";
		List<SysDict> list=dbAccessor.find(hql);
		return list;
	}
	
	/**
	 * 保存
	 * @param model
	 */
	public void save(SysDict model) {
		String hql = "select max(fdSeqNo) from SysDict where fdFunId=?";
		Number max=(Number)dbAccessor.uniqueResultByHQL(hql, model.getFdFunId());
		if(max==null)
			model.setFdSeqNo(0);
		else
			model.setFdSeqNo(max.intValue()+1);
		dbAccessor.save(model);
	}
	
	/**
	 * 删除
	 * @param fdId
	 */
	public void delete(String fdId) {
		if(StringUtil.isNotNull(fdId)){
			SysDict item=dbAccessor.get(SysDict.class, fdId);
			dbAccessor.bulkUpdate("update SysDict set fdSeqNo=fdSeqNo-1 where fdSeqNo>?", item.getFdSeqNo());
			delSysDictByFunId(fdId);
		}
	}
	
	/**
	 * 联级删除
	 * @param fdId
	 */
	public void delSysDictByFunId(String fdId){
		List list=dbAccessor.find("select fdId from SysDict where fdFunId='"+fdId+"'");
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String id=(String) list.get(i);
				delSysDictByFunId(id);
			}
		}
		dbAccessor.bulkUpdate("delete SysDict where fdId=?",fdId);
	}
	
	/**
	 * 删除
	 * @param dict
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void update(SysDict model) throws IllegalArgumentException, IllegalAccessException{
		SysDict item=dbAccessor.get(SysDict.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}
	
	/**
	 * 查询字典表记录总条数
	 * @return int
	 */
	public int getCount() {
		DetachedCriteria decria = DetachedCriteria.forClass(SysDict.class);
		return dbAccessor.getRowCount(decria);
	}
	
	/**
	 * 统计字典数
	 * @param 字段对象
	 * @return 数据条数
	 */
	public int selectCount(SysDict dict) {
		int count=0;
		if(dict.getFdId()!=null&&!"".equals(dict.getFdId())){
			count=((Long)dbAccessor.uniqueResultByHQL("select count(*) from SysDict where fdFunId=?",dict.getFdId())).intValue();
		}
		return count;
	}
	
	/**
	 * 统计字典数
	 * @param 字段对象
	 * @return 数据条数
	 */
	public int selectCount(String fdId) {
		int count=0;
		if(StringUtil.isNotNull(fdId)){
			count=((Long)dbAccessor.uniqueResultByHQL("select count(*) from SysDict where fdFunId=?",fdId)).intValue();
		}
		return count;
	}
	
	/**
	 * 排序
	 * @param model
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void updateOrder(SysDict model) throws IllegalArgumentException, IllegalAccessException {
		SysDict dict = dbAccessor.get(SysDict.class,model.getFdId());
		dbAccessor.bulkUpdate("update SysDict set fdSeqNo=fdSeqNo-1 where fdSeqNo>? and fdFunId=?", dict.getFdSeqNo(),dict.getFdFunId());
		dbAccessor.bulkUpdate("update SysDict set fdSeqNo=fdSeqNo+1 where (fdSeqNo>=? or fdSeqNo=?) and fdFunId=?", model.getFdSeqNo(),model.getFdSeqNo(),model.getFdFunId());
		dict.setFdFunId(model.getFdFunId());
		dict.setFdSeqNo(model.getFdSeqNo());
		dbAccessor.update(dict);	
	}
	
	/**
	 * 根据主键Id查找一条数据
	 * @param fdId
	 * @return
	 */
	public SysDict get(String fdId) {
		return dbAccessor.get(SysDict.class, fdId);
	}
}
