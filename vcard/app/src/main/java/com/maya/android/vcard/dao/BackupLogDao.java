package com.maya.android.vcard.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.data.VCardSQLiteDatabase;
import com.maya.android.vcard.entity.BackupLogEntity;
import com.maya.android.vcard.entity.ContactGroupBackupEntity;
import com.maya.android.vcard.entity.ContactGroupEntity;
import com.maya.android.vcard.entity.json.BackupJsonEntity;
import com.maya.android.vcard.entity.result.RecoverResultEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
/**
 * 备份恢复日志
 * @Version: 1.0
 * @author: zheng_cz 
 * @since: 2013-8-22 上午9:49:10 
 */
public class BackupLogDao {

	private static final String TAG = BackupLogDao.class.getSimpleName();
	
	
	private static final String[] PROJECTION_LIST = new String[]{DatabaseConstant.BackupLogColumns.NAME,DatabaseConstant.BackupLogColumns.NUMBER
													,DatabaseConstant.BackupLogColumns.SIZE,DatabaseConstant.BackupLogColumns.FLAG
													,DatabaseConstant.BackupLogColumns.LAST_TIME,DatabaseConstant.BackupLogColumns.LEVEL
													,DatabaseConstant.BackupLogColumns.TYPE};
	private static BackupLogDao sInstance;
	private Context mContext;
	
	public static BackupLogDao getInstance(){
		if(Helper.isNull(sInstance)){
			sInstance = new BackupLogDao();
		}
		return sInstance;
	}
	private BackupLogDao(){
		this.mContext = ActivityHelper.getGlobalApplicationContext(); 
	}
	/**
	 * 自动备份
	 */
	public void autoBuckupData(){
		// TODO 自动备份
		
	}
	/**
	 * 添加备份恢复日志
	 * @param logEntity 
	 * @return boolean
	 */
	public boolean insert(BackupLogEntity logEntity){
		String insertSql = " insert into " + DatabaseConstant.DBTableName.TABLE_BACKUP_LOGS +"("+ DatabaseConstant.BackupLogColumns.NAME + "," + DatabaseConstant.BackupLogColumns.NUMBER + "," 
						+ DatabaseConstant.BackupLogColumns.FLAG +"," + DatabaseConstant.BackupLogColumns.LAST_TIME + "," + DatabaseConstant.BackupLogColumns.LEVEL + ","
						+ DatabaseConstant.BackupLogColumns.SIZE + "," + DatabaseConstant.BackupLogColumns.TYPE +") values (?,?,?,?,?,?,?)";
		Object[] bindArgs = new Object[]{logEntity.getName(),logEntity.getNum(),logEntity.getFlag(),logEntity.getTime(),logEntity.getLevel(),logEntity.getSize(),logEntity.getType()};
		try {
//			if(logEntity.getLevel() == BackupLogEntity.LEVEL_SECOND){
				delete(logEntity);
//			}
			getSQLiteDatabase().execSQL(insertSql, bindArgs);
			return true;
			
		} catch (Exception e) {
			Log.e(TAG, "日志记录异常", e);
			return false;
		}
	}
	/**
	 * 更新日志
	 * @param logEntity
	 * @return
	 */
	public boolean delete(BackupLogEntity logEntity){
		String deleteWhere = DatabaseConstant.BackupLogColumns.NAME + "=? and " + DatabaseConstant.BackupLogColumns.FLAG +"=?"
					+ " and " + DatabaseConstant.BackupLogColumns.TYPE + "=? ";
		String[] bindArgs = new String[]{logEntity.getName(),logEntity.getFlag()+"",logEntity.getType()+""};
		try {
			getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_BACKUP_LOGS, deleteWhere, bindArgs);
			return true;
			
		} catch (Exception e) {
			Log.e(TAG, "日志记录异常", e);
			return false;
		}
	}
	/**
	 * 分组数据 备份日志
	 * @param type 组类型 1-名片夹分组 2-通讯录分组
	 */
	private void add(ArrayList<ContactGroupBackupEntity> groupBackupList, int type, int flag){
		if(Helper.isNull(groupBackupList)){
			return;
		}
		for (int i = 0, size = groupBackupList.size(); i < size; i++) {

			ContactGroupBackupEntity groupEntity = groupBackupList.get(i);
			BackupLogEntity backLog = new BackupLogEntity();
			backLog.setFlag(flag);
			backLog.setLevel(BackupLogEntity.LEVEL_SECOND);
			backLog.setType(type);
			backLog.setName(groupEntity.getName());
			backLog.setNum(groupEntity.getContactCount());
			backLog.setTime(ResourceHelper.getNowTime());
			
			insert(backLog);
		}
	}
	/**
	 * 备份 /恢复 父级 
	 * @param type 组类型 1-名片夹分组 2-通讯录分组 3-私密
	 * @param flag 备份 /恢复标示
	 * @param num 数量
	 * @param size 大小(0.6k)
	 */
	public void add(int type,int flag, int num, String size){
		String name = mContext.getString(R.string.my_card);
		if(BackupLogEntity.TYPE_LOCAL == type){
			name = mContext.getString(R.string.contact);
		}else if(BackupLogEntity.TYPE_OTHER == type){
			name = mContext.getString(R.string.contact);
		}
		BackupLogEntity logEntity = new BackupLogEntity(name, type, ResourceHelper.getNowTime(), num, size, flag, BackupLogEntity.LEVEL_FIRST);
		insert(logEntity);
	}
	/**
	 * 记录备份日志
	 * @param paramsEntity
	 */
	public void add(BackupJsonEntity paramsEntity){
		add(paramsEntity.getCardGroupList(), BackupLogEntity.TYPE_VCARD, BackupLogEntity.FLAG_BACKUP);
		add(paramsEntity.getBookGroupList(), BackupLogEntity.TYPE_LOCAL, BackupLogEntity.FLAG_BACKUP);
		add(BackupLogEntity.TYPE_VCARD, BackupLogEntity.FLAG_BACKUP, paramsEntity.getCardContactTotal(), "");
		add(BackupLogEntity.TYPE_LOCAL, BackupLogEntity.FLAG_BACKUP, paramsEntity.getBookContactTotal(), "");
	}
	/**
	 * 备份恢复名片日志记录
	 * @param logList   
	 * @return void
	 */
	public void addCardBackupLog(ArrayList<BackupLogEntity> logList){
		if(Helper.isNull(logList)){
			return;
		}
		int size = logList.size();
		for (int i = 0; i < size; i++) {
			BackupLogEntity logItem = logList.get(i);
			logItem.setTime(ResourceHelper.getNowTime());
			insert(logItem);
		}
	}
	/**
	 * 添加日志记录
	 * @param logList
	 */
	public void add(ArrayList<BackupLogEntity> logList){
		if(Helper.isNull(logList)){
			return;
		}
		for (int i = 0, size = logList.size(); i < size; i++) {
			BackupLogEntity logItem = logList.get(i);
			insert(logItem);
		}
	}
	/**
	 * 获取所有的备份恢复日志列表
	 * @return
	 */
	public ArrayList<BackupLogEntity> getList(){
		ArrayList<BackupLogEntity> logList = new ArrayList<BackupLogEntity>();
		String orderBy = " last_time desc";

		try {
			Cursor cursor = getSQLiteDatabase().query(DatabaseConstant.DBTableName.TABLE_BACKUP_LOGS, PROJECTION_LIST, null, null, null, null, orderBy,null);
			if(Helper.isNotNull(cursor)){
				while (cursor.moveToNext()) {
					BackupLogEntity logEntity = getEntity(cursor);
					logList.add(logEntity);					
				}
				cursor.close();
				cursor = null;
			}
		} catch (Exception e) {
			Log.e(TAG,"获取备份恢复日志信息异常", e);
		}
		return logList;
	}
	/**
	 * 获取名片 私密等一级备份恢复对象的日志
	 * @param name 名称
	 * @param flag 备份恢复标志
	 * @param topRows  展示的记录数 
	 * @return List<BackupLogEntity>
	 */
	public ArrayList<BackupLogEntity> getList(String name,int flag,int topRows){
		ArrayList<BackupLogEntity> logList = new ArrayList<BackupLogEntity>();
		String orderBy = " last_time desc";
		String where = " level=" + BackupLogEntity.LEVEL_FIRST ;
		String limit = null;
		if(topRows > 0){
			limit = "0," + topRows;
		}
		if(Helper.isNotEmpty(name)){
			where += " and  "+ DatabaseConstant.BackupLogColumns.NAME +"='"+name+"' ";
		}
		if(flag > 0){
			where +=  " and flag=" + flag;
		}
		
		try {
			Cursor cursor = getSQLiteDatabase().query(DatabaseConstant.DBTableName.TABLE_BACKUP_LOGS, PROJECTION_LIST, where, null, null, null, orderBy,limit);
			if(Helper.isNotNull(cursor)){
				while (cursor.moveToNext()) {
					BackupLogEntity logEntity = getEntity(cursor);
					logList.add(logEntity);					
				}
				cursor.close();
				cursor = null;
			}
		} catch (Exception e) {
			Log.e(TAG,"获取备份恢复日志信息异常", e);
		}
		return logList;
	}
	/**
	 * 赋值到对象
	 * @param cursor
	 * @return
	 */
	private BackupLogEntity getEntity(Cursor cursor){
		BackupLogEntity logEntity = null;
		if(Helper.isNotNull(cursor)){
			String curName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstant.BackupLogColumns.NAME));
			int curFlag = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstant.BackupLogColumns.FLAG));
			String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstant.BackupLogColumns.LAST_TIME));
			String size = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstant.BackupLogColumns.SIZE));
			int num = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstant.BackupLogColumns.NUMBER));
			int level = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstant.BackupLogColumns.LEVEL));
			int type = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstant.BackupLogColumns.TYPE));
			logEntity = new BackupLogEntity(curName,type, time, num, size, curFlag, level);							
		}
		return logEntity;
	}
	/**
	 * 获取某类型的 备份/恢复项的最后操作时间
	 * @param name
	 * @return String
	 */
	public String getLastTime(String name,int flag,Integer type){
		String time = null;
		String selSql = " select max(last_time) from " + DatabaseConstant.DBTableName.TABLE_BACKUP_LOGS +" where flag="+flag ;
		if(Helper.isNotNull(type)){
			selSql += " and type=" + type;
		}
		if(Helper.isNotEmpty(name)){
			selSql += " and  "+ DatabaseConstant.BackupLogColumns.NAME +"='" + name + "'";
		}
		try{
			Cursor cursor = getSQLiteDatabase().rawQuery(selSql, null);
			if(cursor.moveToFirst()){
				time = cursor.getString(0);
			}
			cursor.close();
		}catch(SQLiteException e) {
			Log.e(TAG, e.getMessage());
		}
		if(Helper.isNull(time)){
			time = "";
		}
		return time;
	}
	
	/**
	 * 获取某类型的 恢复项的最后操作的个数
	 * @param name
	 * @return String
	 */
	public int getLastRecoverCount(String name,int flag,Integer type){
		int count = 0;
		String selSql = " select number from " + DatabaseConstant.DBTableName.TABLE_BACKUP_LOGS +" where flag=" + flag ;
		if(Helper.isNotNull(type)){
			selSql += " and type=" + type;
		}
		if(Helper.isNotEmpty(name)){
			selSql += " and  "+ DatabaseConstant.BackupLogColumns.NAME +"='" + name + "'" +" order by last_time desc";
		}
		try{
			Cursor cursor = getSQLiteDatabase().rawQuery(selSql, null);
			if(Helper.isNotNull(cursor) && cursor.moveToFirst()){
				count = cursor.getInt(0);
			}
			cursor.close();
		}catch(SQLiteException e) {
			Log.e(TAG,"获取恢复日条数异常", e);
		}
		
		return count;
	}
	/**
	 * 获取最后一次备份/恢复时间
	 * @param name
	 * @param flag
	 * @return
	 */
	public String getLastTime(String name,int flag){
		return getLastTime(name,flag,null);
	}
	/**
	 * 获取 备份项的最后一次数量
	 * @param name
	 * @param flag
	 * @return
	 */
	public int getLastNum(String name,int flag,int type){
		int num = 0;
		String where = " flag="+flag +" and type=" +type;
		if(Helper.isNotEmpty(name)){
			where += " and "+ DatabaseConstant.BackupLogColumns.NAME +"='" + name + "'";
		}
		try{
			Cursor cursor = getSQLiteDatabase().query(DatabaseConstant.DBTableName.TABLE_BACKUP_LOGS, new String[]{DatabaseConstant.BackupLogColumns.NUMBER}, where, null, DatabaseConstant.BackupLogColumns.NAME, null, DatabaseConstant.BackupLogColumns.LAST_TIME + " desc");
			if(cursor.moveToFirst()){
				num = cursor.getInt(0);
			}
			cursor.close();
		}catch(SQLiteException e) {
			Log.e(TAG,"获取最后备份数量异常", e);
		}
		return num;
	}
	/**
	 * 获取日志的组信息
	 * @return
	 */
	public ArrayList<BackupLogEntity> getDefaultGroupList(){
		ArrayList<BackupLogEntity> logGroupList = new ArrayList<BackupLogEntity>();
		// 我的名片
		logGroupList.add(new BackupLogEntity(mContext.getString(R.string.my_card),BackupLogEntity.TYPE_VCARD, 0, R.mipmap.img_myinfo_card,BackupLogEntity.FLAG_BACKUP));
		//联系人
		logGroupList.add(new BackupLogEntity(mContext.getString(R.string.contact),BackupLogEntity.TYPE_LOCAL,  0, R.mipmap.img_myinfo_address_book,BackupLogEntity.FLAG_BACKUP));
		//私密空间
		logGroupList.add(new BackupLogEntity(mContext.getString(R.string.contact),BackupLogEntity.TYPE_OTHER, 0, R.mipmap.img_secret_space_gray,BackupLogEntity.FLAG_BACKUP));
		return logGroupList;
	}
	/**
	 * 获取备份/恢复的父级分组 
	 * @param flag 备份/恢复
	 * @return
	 */
	public ArrayList<ContactGroupEntity> getDefaultList(int flag){
		ArrayList<ContactGroupEntity> groupList = new ArrayList<ContactGroupEntity>();
		// (排除微片小秘书)
		int cardTotal = ContactVCardDao.getInstance().getContactTotal()-1;
		int bookTotal = ContactBookDao.getInstance().getContactTotal(true);
		int secretTotal = 0;
		if(flag == BackupLogEntity.FLAG_RECOVER){
			cardTotal = getLastRecoverCount(mContext.getString(R.string.my_card), BackupLogEntity.FLAG_BACKUP, BackupLogEntity.TYPE_VCARD);
			bookTotal = getLastRecoverCount(mContext.getString(R.string.contact), BackupLogEntity.FLAG_BACKUP, BackupLogEntity.TYPE_LOCAL);
		}
		
		// 我的名片
		groupList.add(new ContactGroupEntity(mContext.getString(R.string.my_card), cardTotal,"", R.mipmap.img_myinfo_card));
		//联系人
		groupList.add(new ContactGroupEntity(mContext.getString(R.string.contact), bookTotal, "", R.mipmap.img_myinfo_address_book));
		//私密空间
		groupList.add(new ContactGroupEntity(mContext.getString(R.string.contact), secretTotal, "", R.mipmap.img_secret_space_gray));
		return groupList;
	}
	/**
	 * 获取名片夹下的分组
	 * @param flag
	 * @param isRefresh
	 * @return
	 */
	public ArrayList<ContactGroupEntity> getCardChildList(int flag, boolean isRefresh){
		// 获取名片分组集合
		ArrayList<ContactGroupEntity> groupList = new ArrayList<ContactGroupEntity>(ContactVCardDao.getInstance().getGroupListForExport(isRefresh));
		int type = BackupLogEntity.TYPE_VCARD;
		if(Helper.isNotNull(groupList)){
			for (int i = 0,size = groupList.size(); i < size; i++) {
				ContactGroupEntity group = groupList.get(i);
				String lastTime = getLastTime(group.getName(), flag,type);
				group.setLastUpdateTime(lastTime);
				
				if(BackupLogEntity.FLAG_RECOVER == flag){
					int count = getLastRecoverCount(group.getName(), BackupLogEntity.FLAG_BACKUP, type);
					group.setItemCount(count);
				}else{
					// 排除微片小秘书
					if(group.getId() == ContactGroupEntity.GROUP_UNGROUPED_ID){
						group.setItemCount(group.getItemCount()-1);
					}
				}
			}
		}
		return groupList;
	}
	/**
	 * 我的联系人 分组
	 * @param flag
	 * @param isRefresh
	 * @return
	 */
	public ArrayList<ContactGroupEntity> getLocalChildList(int flag, boolean isRefresh){
		// 获取名片分组集合
		ArrayList<ContactGroupEntity> groupList = ContactBookDao.getInstance().getGroupListForExport(isRefresh);
		int type = BackupLogEntity.TYPE_LOCAL;
		if(Helper.isNotNull(groupList)){
			for (int i = 0,size = groupList.size(); i < size; i++) {
				ContactGroupEntity group = groupList.get(i);
				String lastTime = getLastTime(group.getName(), flag,type);
				group.setLastUpdateTime(lastTime);
				
				if(BackupLogEntity.FLAG_RECOVER == flag){
					int count = getLastRecoverCount(group.getName(), BackupLogEntity.FLAG_BACKUP, type);
					group.setItemCount(count);
				}
			}
		}
		return groupList;
	}
	/**
	 * 私密空间
	 * @param flag
	 * @return
	 */
	public ArrayList<ContactGroupEntity> getPrivateChildList(int flag){
		ArrayList<ContactGroupEntity> childList = new ArrayList<ContactGroupEntity>();
		String cardName = mContext.getString(R.string.contact);
		String msgName = mContext.getString(R.string.information);
		String callName = mContext.getString(R.string.call_records);
		
		int type = BackupLogEntity.TYPE_OTHER;
		childList.add(new ContactGroupEntity(cardName,type,getLastTime(cardName, flag,type), 0));
		childList.add(new ContactGroupEntity(msgName,type, getLastTime(msgName, flag,type), 0));
		childList.add(new ContactGroupEntity(callName,type, getLastTime(callName, flag,type), 0));
		
		return childList;
	}
	/**
	 * 返回 主要设置 子项列表
	 * @param flag
	 * @return List<BackupLogEntity>
	 */
	public ArrayList<BackupLogEntity> getSettingChildList(int flag){
		ArrayList<BackupLogEntity> childList = new ArrayList<BackupLogEntity>();
		return childList;
	}
	
	// #region 与服务端交互的操作
	/**
	 * 云端恢复
	 * @param recoverEntity
	 */
	public void recoverContacts(RecoverResultEntity recoverEntity){
		if(Helper.isNull(recoverEntity)){
			return;
		}
		// 名片夹恢复
		ContactVCardDao.getInstance().recoverGroup(recoverEntity.getCardGroupList());
		ContactVCardDao.getInstance().recoverContacts(recoverEntity.getCardPersonList());
		
		//通讯录恢复
		ContactBookDao.getInstance().recoverGroup(recoverEntity.getBookGroupList());
		ContactBookDao.getInstance().recoverContacts(recoverEntity.getBookPersonList());

		//记录日志
		add(BackupLogEntity.TYPE_VCARD, BackupLogEntity.FLAG_RECOVER, recoverEntity.getCardContactTotal(), "");
		add(BackupLogEntity.TYPE_LOCAL, BackupLogEntity.FLAG_RECOVER, recoverEntity.getBookContactTotal(), "");
		add(recoverEntity.getCardGroupList(), BackupLogEntity.TYPE_VCARD, BackupLogEntity.FLAG_RECOVER);
		add(recoverEntity.getBookGroupList(), BackupLogEntity.TYPE_LOCAL, BackupLogEntity.FLAG_RECOVER);
	}
	
	/**
	 * 获取备份时间
	 * @param backupTime
	 * @return
	 */
	private long getBackupTime(int backupTime){
		long longBackupTime = 0L;
		long hour = 3600 * 1000;
		switch(backupTime){
		case 0:
			longBackupTime = hour;
			break;
		case 1:
			longBackupTime = 24 * hour;
			break;
		case 2:
			longBackupTime = 7 * 24 * hour;
			break;
		case 3:
			longBackupTime = 30 * 24 * hour;
			break;
		}
		return longBackupTime;
	}
	// #endregion

	private SQLiteDatabase getSQLiteDatabase(){
		return VCardSQLiteDatabase.getInstance().getSQLiteDatabase();
	}
}
