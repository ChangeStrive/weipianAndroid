package com.maya.android.vcard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.data.VCardSQLiteDatabase;
import com.maya.android.vcard.entity.ContactEntity;
import com.maya.android.vcard.entity.ContactGroupBackupEntity;
import com.maya.android.vcard.entity.ContactGroupEntity;
import com.maya.android.vcard.entity.ContactListItemEntity;
import com.maya.android.vcard.entity.json.ContactVCardBackupJsonEntity;
import com.maya.android.vcard.util.ConverChineseCharToEnHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO: 名片夹 联系人
 */
public class ContactVCardDao {

	private static final String TAG = ContactVCardDao.class.getSimpleName();
	private static ContactVCardDao sInstance;
	private Context mContext = ActivityHelper.getGlobalApplicationContext();
	private final String TABLE_CONTACTS = DatabaseConstant.DBTableName.TABLE_CONTACTS;

	private int mContactTotal = 0;
	private HashMap<Integer, Integer> mCountMap;
	// #region 分组变量
	private static ArrayList<ContactGroupEntity> sGroupListForShow;
	/**
	 * 名片夹分组字段
	 */
	private static final String[] GROUP_PROJECTION = new String[] { DatabaseConstant._ID, DatabaseConstant.ContactGroupColumns.NAME,
			DatabaseConstant.ContactGroupColumns.ICON, DatabaseConstant.ContactGroupColumns.ORDER_CODE,
			DatabaseConstant.ContactGroupColumns.IS_ENABLE, DatabaseConstant.ContactGroupColumns.LAST_UPDATE_TIME,
			DatabaseConstant.ContactGroupColumns.DESCRIPTION };
	// #endregion 分组变量

	// #region 名片夹联系人变量

	/**
	 * 联系人列表显示字段
	 */
	private static final String[] CONTACT_PROJECTION_LIST = new String[] { DatabaseConstant._ID, DatabaseConstant.ContactColumns.GROUP_ID,
			DatabaseConstant.ContactColumns.GROUP_NAME, DatabaseConstant.ContactColumns.DISPLAY_NAME, DatabaseConstant.ContactColumns.COMPANY_NAME,
			DatabaseConstant.ContactColumns.JOB, DatabaseConstant.ContactColumns.HEAD_IMG, DatabaseConstant.ContactColumns.BUSINESS,
			DatabaseConstant.ContactColumns.PARTNER_ID, DatabaseConstant.ContactColumns.ACCOUNT_GRADE, DatabaseConstant.ContactColumns.AUTH,
			DatabaseConstant.ContactColumns.ORDER_CODE, DatabaseConstant.ContactColumns.CARD_ID, DatabaseConstant.ContactColumns.ACCOUNT_ID,
			DatabaseConstant.ContactColumns.CONTACT_ID, DatabaseConstant.ContactColumns.CREATE_TIME, DatabaseConstant.ContactColumns.BIND_WAY,
			DatabaseConstant.ContactColumns.VCARD_NO };

	/**
	 * 联系人详细字段
	 */
	private static final String[] CONTACT_PROJECTION_DETAILS = new String[] { DatabaseConstant._ID, DatabaseConstant.ContactColumns.CONTACT_ID,
			DatabaseConstant.ContactColumns.GROUP_ID, DatabaseConstant.ContactColumns.GROUP_NAME, DatabaseConstant.ContactColumns.DISPLAY_NAME,
			DatabaseConstant.ContactColumns.COMPANY_NAME, DatabaseConstant.ContactColumns.JOB, DatabaseConstant.ContactColumns.HEAD_IMG,
			DatabaseConstant.ContactColumns.BUSINESS, DatabaseConstant.ContactColumns.PARTNER_ID, DatabaseConstant.ContactColumns.ACCOUNT_GRADE,
			DatabaseConstant.ContactColumns.AUTH, DatabaseConstant.ContactColumns.ORDER_CODE, DatabaseConstant.ContactColumns.ACCOUNT_NAME,
			DatabaseConstant.ContactColumns.EN_NAME, DatabaseConstant.ContactColumns.SEX, DatabaseConstant.ContactColumns.BIRTHDAY,
			DatabaseConstant.ContactColumns.DEGREE, DatabaseConstant.ContactColumns.SELF_SIGN, DatabaseConstant.ContactColumns.REGISTER_TIME,
			DatabaseConstant.ContactColumns.COUNTRY, DatabaseConstant.ContactColumns.PROVINCE, DatabaseConstant.ContactColumns.CITY,
			DatabaseConstant.ContactColumns.NATIVEPLACE, DatabaseConstant.ContactColumns.CARD_ID, DatabaseConstant.ContactColumns.CARD_NAME,
			DatabaseConstant.ContactColumns.CARD_IMG_A, DatabaseConstant.ContactColumns.CARD_IMG_B, DatabaseConstant.ContactColumns.MOBILE,
			DatabaseConstant.ContactColumns.TELEPHONE, DatabaseConstant.ContactColumns.FAX, DatabaseConstant.ContactColumns.EMAIL,
			DatabaseConstant.ContactColumns.ADDRESS, DatabaseConstant.ContactColumns.POSTCODE, DatabaseConstant.ContactColumns.COMPANY_COUNTRY,
			DatabaseConstant.ContactColumns.COMPANY_PROVINCE, DatabaseConstant.ContactColumns.COMPANY_HOME,
			DatabaseConstant.ContactColumns.COMPANY_ABOUT, DatabaseConstant.ContactColumns.CARD_REMARK, DatabaseConstant.ContactColumns.REMARK,
			DatabaseConstant.ContactColumns.VCARD_NO, DatabaseConstant.ContactColumns.COMPANY_APPROVAL, DatabaseConstant.ContactColumns.ACCOUNT_ID,
			DatabaseConstant.ContactColumns.BIND_WAY, DatabaseConstant.ContactColumns.SCHOOL, DatabaseConstant.ContactColumns.INTRODUCTION,
			DatabaseConstant.ContactColumns.CREATE_TIME, DatabaseConstant.ContactColumns.CARD_A_TYPE, DatabaseConstant.ContactColumns.CARD_B_TYPE,
			DatabaseConstant.ContactColumns.CARD_A_FORM, DatabaseConstant.ContactColumns.CARD_B_FORM,
			DatabaseConstant.ContactColumns.CARD_A_ORIENTATION, DatabaseConstant.ContactColumns.CARD_B_ORIENTATION,
			DatabaseConstant.ContactColumns.TENCENT_BLOG_URL, DatabaseConstant.ContactColumns.SINA_BLOG_URL,
			DatabaseConstant.ContactColumns.TENCENT_QQ_URL, DatabaseConstant.ContactColumns.INSTANT_MESSENGER,
			DatabaseConstant.ContactColumns.COMPANY_EN_NAME, DatabaseConstant.ContactColumns.COMPANY_NAME_LIST,
			DatabaseConstant.ContactColumns.LAST_CONTACTED_TIME, DatabaseConstant.ContactColumns.TIMES_CONTACTED };

	// 备份数据字段
	private static final String BACKUP_COLUMNS = new StringBuilder().append(DatabaseConstant._ID).append(",")
			.append(DatabaseConstant.ContactColumns.CONTACT_ID).append(",").append(DatabaseConstant.ContactColumns.VCARD_NO).append(",")
			.append(DatabaseConstant.ContactColumns.HEAD_IMG).append(",").append(DatabaseConstant.ContactColumns.GROUP_ID).append(",")
			.append(DatabaseConstant.ContactColumns.GROUP_NAME).append(",").append(DatabaseConstant.ContactColumns.DISPLAY_NAME).append(",")
			.append(DatabaseConstant.ContactColumns.COMPANY_NAME).append(",").append(DatabaseConstant.ContactColumns.JOB).append(",")
			.append(DatabaseConstant.ContactColumns.BUSINESS).append(",").append(DatabaseConstant.ContactColumns.EN_NAME).append(",")
			.append(DatabaseConstant.ContactColumns.REMARK).append(",").append(DatabaseConstant.ContactColumns.CARD_ID).append(",")
			.append(DatabaseConstant.ContactColumns.CARD_NAME).append(",").append(DatabaseConstant.ContactColumns.CARD_IMG_A).append(",")
			.append(DatabaseConstant.ContactColumns.CARD_IMG_B).append(",").append(DatabaseConstant.ContactColumns.MOBILE).append(",")
			.append(DatabaseConstant.ContactColumns.TELEPHONE).append(",").append(DatabaseConstant.ContactColumns.FAX).append(",")
			.append(DatabaseConstant.ContactColumns.EMAIL).append(",").append(DatabaseConstant.ContactColumns.ADDRESS).append(",")
			.append(DatabaseConstant.ContactColumns.POSTCODE).append(",").append(DatabaseConstant.ContactColumns.COMPANY_HOME).append(",")
			.append(DatabaseConstant.ContactColumns.COMPANY_ABOUT).append(",").append(DatabaseConstant.ContactColumns.CARD_REMARK).append(",")
			.append(DatabaseConstant.ContactColumns.CARD_A_TYPE).append(",").append(DatabaseConstant.ContactColumns.CARD_B_TYPE).append(",")
			.append(DatabaseConstant.ContactColumns.CARD_A_FORM).append(",").append(DatabaseConstant.ContactColumns.CARD_B_FORM).append(",")
			.append(DatabaseConstant.ContactColumns.CARD_A_ORIENTATION).append(",").append(DatabaseConstant.ContactColumns.CARD_B_ORIENTATION)
			.append(",").append(DatabaseConstant.ContactColumns.INSTANT_MESSENGER).append(",")
			.append(DatabaseConstant.ContactColumns.COMPANY_EN_NAME).append(",").append(DatabaseConstant.ContactColumns.COMPANY_NAME_LIST).toString();

	// #endregion

	private ContactVCardDao() {
	}

	public static ContactVCardDao getInstance(){
		if(Helper.isNull(sInstance)){
			sInstance = new ContactVCardDao();
		}
		return sInstance;
	}
	// #region 名片夹分组

	//#region private 方法
	/**
	 * 插入数据 返回键值id
	 * 
	 * @param group
	 * @throws Exception
	 */
	private int insertGroup(ContactGroupEntity group) throws Exception {
		int newId = 0;
		if (group == null) {
			return newId;
		}
		ContentValues values = getGroupContentValues(group);
		this.getSQLiteDatabase().insert(DatabaseConstant.DBTableName.TABLE_CONTACT_GROUPS, null, values);
		newId = (int) VCardSQLiteDatabase.getInstance().getCurSequence(DatabaseConstant.DBTableName.TABLE_CONTACT_GROUPS);
		return newId;
	}

	private ContentValues getGroupContentValues(ContactGroupEntity group) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.ContactGroupColumns.NAME, group.getName());
		values.put(DatabaseConstant.ContactGroupColumns.DESCRIPTION, group.getDescription());
		values.put(DatabaseConstant.ContactGroupColumns.ICON, group.getIconName());
		values.put(DatabaseConstant.ContactGroupColumns.IS_ENABLE, group.isEnable());
		values.put(DatabaseConstant.ContactGroupColumns.LAST_UPDATE_TIME, group.getLastUpdateTime());
		values.put(DatabaseConstant.ContactGroupColumns.ORDER_CODE, group.getOrderCode());
		return values;
	}

	/**
	 * 将数据库字段转化为对象属性
	 * 
	 * @param cursor
	 * @return ContactGroupEntity
	 */
	private ContactGroupEntity getGroupEntity(Cursor cursor) {
		ContactGroupEntity groupM = new ContactGroupEntity();
		if (Helper.isNull(cursor))
			return null;
		try {
			groupM.setId(cursor.getInt(cursor.getColumnIndex(DatabaseConstant._ID)));
			groupM.setName(cursor.getString(cursor.getColumnIndex(DatabaseConstant.ContactGroupColumns.NAME)));
			groupM.setIconName(cursor.getString(cursor.getColumnIndex(DatabaseConstant.ContactGroupColumns.ICON)));
			groupM.setOrderCode(cursor.getInt(cursor.getColumnIndex(DatabaseConstant.ContactGroupColumns.ORDER_CODE)));
			int enable = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.ContactGroupColumns.IS_ENABLE));
			groupM.setEnable(ResourceHelper.int2Boolean(enable));
			groupM.setLastUpdateTime(cursor.getString(cursor.getColumnIndex(DatabaseConstant.ContactGroupColumns.LAST_UPDATE_TIME)));
			// groupM.setDescription(cursor.getString(6));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupM;
	}

	/**
	 * 获取不含统计数量的 所有分组信息
	 * @return
	 */
	private ArrayList<ContactGroupEntity> getGroupListForAll(){
		ArrayList<ContactGroupEntity> grpList = getGroupList();
		SparseArray<ContactGroupEntity> grpMap = ContactGroupEntity.getFixedGroup();
		if (Helper.isNull(grpList)) {
		    grpList = new ArrayList<ContactGroupEntity>();
		}
		// 添加 最近联系人/收藏夹/未分组/黑名单 固定分组
		grpList.add(0, grpMap.get(ContactGroupEntity.GROUP_RECENTCONTACT_ID));
		grpList.add(1, grpMap.get(ContactGroupEntity.GROUP_FAVORITE_ID));
		grpList.add(grpMap.get(ContactGroupEntity.GROUP_UNGROUPED_ID));
		grpList.add(grpMap.get(ContactGroupEntity.GROUP_BLACKLIST_ID));

		return grpList;
	}

	/**
	 * 更该分组名称
	 * 
	 * @param groupId
	 * @param groupNewName
	 * @return boolean
	 * @throws Exception
	 */
	private boolean updateGroup(int groupId, String groupNewName) {
		ContentValues values = new ContentValues();
		try {
			values.put(DatabaseConstant.ContactGroupColumns.NAME, groupNewName);
			int upRow = this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_CONTACT_GROUPS, values, DatabaseConstant._ID + " = " + groupId,
					null);
			if( upRow > 0 ){			
				// 关联更改 联系人下的分组名称
				StringBuilder upSb = new StringBuilder("update ").append(TABLE_CONTACTS).append(" set ")
						.append(DatabaseConstant.ContactColumns.GROUP_NAME).append("='").append(groupNewName).append("'")
						.append(" where " + DatabaseConstant.ContactColumns.GROUP_ID).append("=").append(groupId);
				this.getSQLiteDatabase().execSQL(upSb.toString());
				return true;
			}
			return false;
			
		} catch (Exception ex) {
			Log.e(TAG, "更改分组名称异常", ex);
			return false;
		}
	}

	//#endregion 方法
	/**
	 * 判断分组是否存在
	 * 
	 * @param groupName
	 * @return
	 */
	public boolean isExistGroup(String groupName) {

		boolean exist = false;
		StringBuilder sb = new StringBuilder();
		sb.append(" select * from ").append(DatabaseConstant.DBTableName.TABLE_CONTACT_GROUPS).append(" where ")
				.append(DatabaseConstant.ContactGroupColumns.NAME).append(" = '").append(groupName).append("'");
		Cursor cursor = this.getSQLiteDatabase().rawQuery(sb.toString(), null);
		if (Helper.isNotNull(cursor)) {
			if (cursor.getCount() > 0) {
				exist = true;
			}
			cursor.close();
			cursor = null;
		}
		return exist;
	}
	/**
	 * 获取名片分组名称
	 * @param groupId
	 * @return
	 */
	public String getGroupName(int groupId){
		String groupName = null;
		switch (groupId) {
		case ContactGroupEntity.GROUP_ALL_ID:
			groupName = mContext.getString(R.string.all);
			break;
		case ContactGroupEntity.GROUP_UNGROUPED_ID:
			groupName = mContext.getString(R.string.ungrouped);
			break;
		case ContactGroupEntity.GROUP_BLACKLIST_ID:
			groupName = mContext.getString(R.string.blacklist);
			break;
		case ContactGroupEntity.GROUP_FAVORITE_ID:
			groupName = mContext.getString(R.string.favorite);
		case ContactGroupEntity.GROUP_RECENTCONTACT_ID:
			groupName = mContext.getString(R.string.recent_contact);
			break;
		default:
			ContactGroupEntity groupEntity = ContactVCardDao.getInstance().getGroupEntity(groupId);
			if(Helper.isNotNull(groupEntity) && Helper.isNotEmpty(groupEntity.getName())){
				groupName = groupEntity.getName();
			}
			break;
		}
		return groupName;
	}
	/**
	 * 获取 分组对象
	 * 
	 * @param groupId
	 * @return
	 */
	public ContactGroupEntity getGroupEntity(int groupId) {
		ContactGroupEntity group = null;
		String where = DatabaseConstant._ID + "=" + groupId;
		Cursor cursor = null;
		try {
			cursor = this.getSQLiteDatabase().query(DatabaseConstant.DBTableName.TABLE_CONTACT_GROUPS, GROUP_PROJECTION, where, null, null, null,
					DatabaseConstant.ContactGroupColumns.ORDER_CODE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Helper.isNotNull(cursor) && cursor.moveToFirst()) {
			group = getGroupEntity(cursor);
			cursor.close();
			cursor = null;
		}
		return group;
	}

	/**
	 * 根据分组名称获取分组对象
	 * 
	 * @param groupName
	 * @return
	 */
	public ContactGroupEntity getGroupEntity(String groupName) {
		ContactGroupEntity group = null;
		if(Helper.isEmpty(groupName)){
			return group;
		}
		String where = DatabaseConstant.ContactGroupColumns.NAME + "='" + groupName + "'";
		Cursor cursor = null;
		try {
			cursor = this.getSQLiteDatabase().query(DatabaseConstant.DBTableName.TABLE_CONTACT_GROUPS, GROUP_PROJECTION, where, null, null, null,
					DatabaseConstant.ContactGroupColumns.ORDER_CODE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Helper.isNotNull(cursor)) {
			group = getGroupEntity(cursor);
			cursor.close();
			cursor = null;
		}
		return group;
	}

	/**
	 * 获取分组列表
	 * 
	 * @return List<ContactGroupEntity>
	 */
	public ArrayList<ContactGroupEntity> getGroupList() {
		ArrayList<ContactGroupEntity> groupList = new ArrayList<ContactGroupEntity>();
		Cursor groupCursor = null;
		try {
			groupCursor = this.getSQLiteDatabase().query(DatabaseConstant.DBTableName.TABLE_CONTACT_GROUPS, GROUP_PROJECTION, null, null, null, null,
					DatabaseConstant.ContactGroupColumns.ORDER_CODE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(Helper.isNotNull(groupCursor)){
			while (groupCursor.moveToNext()) {
				ContactGroupEntity model = getGroupEntity(groupCursor);
				if (Helper.isNotNull(model) && !groupList.contains(model))
					groupList.add(model);
			}
			groupCursor.close();
			groupCursor = null;
		}
		return groupList;
	}
	/**
	 * 获取用户的自定义分组列表
	 * 
	 * @return
	 */
	public ArrayList<ContactGroupEntity> getGroupListForCustom() {

		ArrayList<ContactGroupEntity> groupList = new ArrayList<ContactGroupEntity>();
		String where = DatabaseConstant.ContactGroupColumns.IS_ENABLE + ">0";
		Cursor groupCursor = this.getSQLiteDatabase().query(DatabaseConstant.DBTableName.TABLE_CONTACT_GROUPS, GROUP_PROJECTION, where, null, null, null,
				DatabaseConstant.ContactGroupColumns.ORDER_CODE);
		if (Helper.isNotNull(groupCursor)) {
			while (groupCursor.moveToNext()) {
				ContactGroupEntity model = getGroupEntity(groupCursor);
				if (Helper.isNotNull(model) && !groupList.contains(model))
					groupList.add(model);
			}
			groupCursor.close();
			groupCursor = null;
		}
		return groupList;
	}
	/**
	 * 获取含统计的分组
	 * 
	 * @param isRefresh
	 * @return List<ContactGroupEntity>
	 */
	public ArrayList<ContactGroupEntity> getGroupListForShow(boolean isRefresh) {
		if (Helper.isNull(sGroupListForShow) || isRefresh) {
			sGroupListForShow = getGroupListForAll();
			Map<Integer, Integer> countMap = getContactCount();
			mContactTotal = countMap.get(ContactGroupEntity.GROUP_ALL_ID);
			for (int i = 0, len = sGroupListForShow.size(); i < len; i++) {
				int grpId = sGroupListForShow.get(i).getId();
				Integer count = countMap.get(grpId);
				if (Helper.isNull(count)) {
					sGroupListForShow.get(i).setItemCount(0);
				} else {
					sGroupListForShow.get(i).setItemCount(count);
				}
			}
		}
		return sGroupListForShow;
	}

	/**
	 * 导入导出 分组信息
	 * 
	 * @param isRefresh
	 * @return ArrayList<ContactGroupEntity>
	 */
	public ArrayList<ContactGroupEntity> getGroupListForExport(boolean isRefresh) {
		ArrayList<ContactGroupEntity> grpList = new ArrayList<ContactGroupEntity>(getGroupListForShow(isRefresh));
		if (Helper.isNotNull(grpList)) {
			int size = grpList.size();
			// 去掉 最近联系人(\黑名单 分组)
			if(size > 1){
				grpList.remove(size - 1);
				grpList.remove(0);
			}
		}
		return grpList;
	}
	/**
	 * 移动分组的弹出框 分组列表
	 * @return
	 */
	public ArrayList<ContactGroupEntity> getGroupListForDialog(){
		ArrayList<ContactGroupEntity> groupList = getGroupListForAll();
		if(Helper.isNotEmpty(groupList)){
			groupList.remove(0);
		}
		return groupList;
	}
	/**
	 * 获取 备份恢复显示的分组列表
	 * 
	 * @return List<ContactGroupBackupEntity>
	 */
	public ArrayList<ContactGroupBackupEntity> getGroupListForShowBackup() {
		ArrayList<ContactGroupEntity> groupList = getGroupListForExport(true);
		ArrayList<ContactGroupBackupEntity> groupBackupList = new ArrayList<ContactGroupBackupEntity>();
		if (Helper.isNotNull(groupList) && groupList.size() > 0) {
			for (int i = 0, size = groupList.size(); i < size; i++) {
				groupBackupList.add(groupList.get(i).getGroupBackupEntity());
			}
		}
		return groupBackupList;
	}
	/**
	 * 获取 备份恢复的自定义分组列表
	 * 
	 * @return ArrayList<ContactGroupBackupEntity>
	 */
	public ArrayList<ContactGroupBackupEntity> getGroupListForBackup() {
		ArrayList<ContactGroupBackupEntity> grpBackupList = new ArrayList<ContactGroupBackupEntity>();
		String selSql = " select " + DatabaseConstant._ID + "," + DatabaseConstant.ContactGroupColumns.NAME + ","
				+ DatabaseConstant.ContactGroupColumns.ORDER_CODE + " from " + DatabaseConstant.DBTableName.TABLE_CONTACT_GROUPS + " where "
				+ DatabaseConstant.ContactGroupColumns.IS_ENABLE + ">0";

		try {
			Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
			if (Helper.isNotNull(cursor)) {
				while (cursor.moveToNext()) {
					ContactGroupBackupEntity group = new ContactGroupBackupEntity();
					group.setGroupId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstant._ID)));
					group.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstant.ContactGroupColumns.NAME)));
					group.setOrderCode(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConstant.ContactGroupColumns.ORDER_CODE)));
					grpBackupList.add(group);

				}
				cursor.close();
				cursor = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return grpBackupList;
	}

	
	/**
	 * 添加分组
	 * 
	 * @param groupName
	 * @param orderCode
	 * @return
	 */
	public ContactGroupEntity addGroup(String groupName, int orderCode) {
		if(Helper.isEmpty(groupName)){
			return null;
		}
		try {
			boolean exist = isExistGroup(groupName);
			if (!exist) {
				if (orderCode < 1) {
					orderCode = getGroupMaxOrderCode() + 1;
				}
				ContactGroupEntity group = new ContactGroupEntity();
				group.setName(groupName);
				group.setOrderCode(orderCode);
				group.setIconName("img_del");
				group.setEnable(true);
				group.setLastUpdateTime(ResourceHelper.getNowTime());
				int newId = insertGroup(group);
				if(newId > 0){
					group.setId(newId);
					//TODO  判断设置条件是否插入到 本机通讯录
//					 if (SettingData.getSettingEntity().isCardInfo2AddressList()) {
//						 ContactBookDao.getInstance().addGroup(groupName);
//					 }
					 return group;
				}
			}
			return null;
		} catch (Exception ex) {
			Log.e(TAG, "添加分组数据错误", ex);
			return null;
		}
	}

	/**
	 * 更改分组名称
	 * 
	 * @param groupOld
	 * @param groupNewName
	 */
	public boolean updateGroup(ContactGroupEntity groupOld, String groupNewName) {
		if (Helper.isNull(groupOld) || Helper.isEmpty(groupNewName)) {
			return false;
		}
		boolean upSuccess = updateGroup(groupOld.getId(), groupNewName);
		//TODO 判断设置条件是否插入到 本机通讯录
//		 if (SettingData.getSettingEntity().isCardInfo2AddressList()) {
//			 ContactBookDao.getInstance().updateGroup(groupNewName, groupOld.getName());
//		 }
		return upSuccess;
	}

	/**
	 * 删除单个分组
	 * 
	 * @param groupId
	 * @return boolean
	 * @throws Exception
	 */
	public boolean deleteGroup(int groupId) {
		if (groupId < 1) {
			return false;
		}
		String delWhere = DatabaseConstant._ID + " = " + groupId;
		int delRow = this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_CONTACT_GROUPS, delWhere, null);
		return delRow > 0 ? true : false;
	}

	/**
	 * 删除分组,同时删除通讯录的
	 *
	 * @param group
	 * @param isDelContact
	 *            是否删除联系人
	 * @return
	 */
	public boolean deleteGroup(ContactGroupEntity group, boolean isDelContact) {
		if (Helper.isEmpty(group)) {
			return false;
		}
		boolean del = deleteGroup(group.getId());
		if (del) {
			deleteGroupContact(group.getId(), isDelContact);
			//  删除通讯录对应的分组 (不删除联系人)
//			ContactBookDao.getInstance().deleteGroup(groupName, false);

		}
		return del;
	}
	/**
	 * 分组排序
	 * 
	 * @param startGroup
	 * @param endGroup
	 * @return
	 */
	public boolean sortGroup(ContactGroupEntity startGroup, ContactGroupEntity endGroup) {
		boolean upbl = false;
		try {
			String upSql = " update contact_groups set order_code = order_code-1 where order_code>" + startGroup.getOrderCode() + " and order_code<="
					+ endGroup.getOrderCode();
			String upStart = " update contact_groups set order_code = " + endGroup.getOrderCode() + " where " + DatabaseConstant._ID + " = "
					+ startGroup.getId();

			this.getSQLiteDatabase().execSQL(upSql);
			this.getSQLiteDatabase().execSQL(upStart);
			upbl = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return upbl;
	}

	/**
	 * 根据分组名称获取分组id
	 * 
	 * @param groupName
	 * @param isInsert
	 *            是否插入
	 * @return
	 */
	public int getGroupId(String groupName, boolean isInsert) {
		int groupId = 0;
		if(Helper.isNotEmpty(groupName)){
			ContactGroupEntity group = getGroupEntity(groupName);
			if (Helper.isNull(group)) {
				if (isInsert) {
					groupId = addGroup(groupName, 0).getId();
				}
			} else {
				groupId = group.getId();
			}
		}
		return groupId;
	}

	/**
	 * 获取排序值得最大值
	 * 
	 * @return int
	 */
	public int getGroupMaxOrderCode() {
		int max = 0;
		Cursor cursor = this.getSQLiteDatabase().rawQuery("select max(order_code) from " + DatabaseConstant.DBTableName.TABLE_CONTACT_GROUPS, null);
		if (Helper.isNotNull(cursor)) {
			if (cursor.moveToFirst()) {
				max = cursor.getInt(0);
			}
			cursor.close();
			cursor = null;
		}
		return max;
	}

	/**
	 * 恢复分组
	 * 
	 * @param groupList
	 */
	public void recoverGroup(ArrayList<ContactGroupBackupEntity> groupList) {
		if (Helper.isEmpty(groupList)) {
			return;
		}
		ContactGroupBackupEntity recoverGrp = null;
		try {
			for (int i = 0, size = groupList.size(); i < size; i++) {
				recoverGrp = groupList.get(i);

				// 未分组或收藏夹 则不添加到分组表
				boolean unEditGroup = recoverGrp.getGroupId() == ContactGroupEntity.GROUP_UNGROUPED_ID
						|| recoverGrp.getGroupId() == ContactGroupEntity.GROUP_FAVORITE_ID
						|| recoverGrp.getName().equals(mContext.getString(R.string.ungrouped))
						|| recoverGrp.getName().equals(mContext.getString(R.string.favorite))
						|| ResourceHelper.isEmpty(recoverGrp.getName());
				if (unEditGroup) {
					continue;
				}

				addGroup(recoverGrp.getName(), recoverGrp.getOrderCode());
			}
		} catch (Exception ex) {
			Log.e(TAG, "恢复分组异常", ex);
		}
	}

	// #endregion

	// #region 名片夹联系人

	//#region private 方法
	/**
	 * 获取列表所需的对象
	 * 
	 * @param contactCursor
	 * @return ContactListItemEntity
	 */
	private ContactListItemEntity getEntityForListItem(Cursor contactCursor) {
		ContactListItemEntity item = new ContactListItemEntity();
		item.setId(contactCursor.getInt(0));
		item.setContactId(contactCursor.getLong(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CONTACT_ID)));
		item.setGroupId(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.GROUP_ID)));
		item.setGroupName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.GROUP_NAME)));
		item.setDisplayName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.DISPLAY_NAME)));
		item.setCompanyName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_NAME)));
		item.setJob(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.JOB)));
		item.setHeadImg(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.HEAD_IMG)));
		item.setBusiness(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.BUSINESS)));
		item.setPartnerId(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.PARTNER_ID)));
		item.setAccountGrade(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.ACCOUNT_GRADE)));
		item.setAuth(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.AUTH)));
		item.setOrderCode(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.ORDER_CODE)));
		item.setCardId(contactCursor.getLong(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_ID)));
		item.setAccountId(contactCursor.getLong(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.ACCOUNT_ID)));
		item.setCreateTime(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CREATE_TIME)));
		item.setBindway(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.BIND_WAY)));
		item.setVcardNo(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.VCARD_NO)));
		if(Helper.isEmpty(item.getOrderCode())){
			item.setOrderCode(ConverChineseCharToEnHelper.converToPingYingHeadUppercase(item.getDisplayName()));
		}
		item.setFirstLetter(ConverChineseCharToEnHelper.getFirstLetter(item.getOrderCode()));
		return item;
	}

	/**
	 * 获取联系人的详细信息
	 * 
	 * @param contactCursor
	 * @return ContactEntity
	 */
	private ContactEntity getEntity(Cursor contactCursor) {

		ContactEntity item = new ContactEntity();

		item.setId(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant._ID)));
		item.setContactId(contactCursor.getLong(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CONTACT_ID)));
		item.setGroupId(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.GROUP_ID)));
		item.setGroupName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.GROUP_NAME)));
		item.setDisplayName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.DISPLAY_NAME)));
		item.setCompanyName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_NAME)));
		item.setCompanyEnName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_EN_NAME)));
		item.setOtherCompanyNameList(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_NAME_LIST)));
		item.setJob(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.JOB)));
		item.setHeadImg(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.HEAD_IMG)));
		item.setBusiness(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.BUSINESS)));
		item.setPartnerId(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.PARTNER_ID)));
		item.setAccountGrade(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.ACCOUNT_GRADE)));
		item.setAuth(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.AUTH)));
		item.setOrderCode(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.ORDER_CODE)));
		item.setFirstLetter(ConverChineseCharToEnHelper.getFirstLetter(item.getOrderCode()));

		item.setAccountName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.ACCOUNT_NAME)));
		item.setEnName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.EN_NAME)));
		item.setSex(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.SEX)));
		item.setBirthday(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.BIRTHDAY)));
		item.setDegree(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.DEGREE)));
		item.setSelfSign(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.SELF_SIGN)));
		item.setRegisterTime(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.REGISTER_TIME)));
		item.setCountry(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COUNTRY)));
		item.setProvince(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.PROVINCE)));
		item.setCity(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CITY)));
		item.setNativeplace(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.NATIVEPLACE)));

		item.setCardId(contactCursor.getLong(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_ID)));
		item.setCardName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_NAME)));
		item.setCardImgA(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_IMG_A)));
		item.setCardImgB(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_IMG_B)));
		item.setMobile(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.MOBILE)));
		item.setTelephone(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.TELEPHONE)));
		item.setFax(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.FAX)));
		item.setEmail(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.EMAIL)));
		item.setAddress(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.ADDRESS)));
		item.setPostcode(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.POSTCODE)));
		item.setCompanyCountry(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_COUNTRY)));
		item.setCompanyProvince(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_PROVINCE)));
		item.setCompanyHome(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_HOME)));
		item.setCompanyAbout(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_ABOUT)));
		item.setCardRemark(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_REMARK)));
		item.setRemark(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.REMARK)));
		item.setVcardNo(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.VCARD_NO)));
		item.setCompanyApproval(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_APPROVAL)));
		item.setAccountId(contactCursor.getLong(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.ACCOUNT_ID)));
		item.setSchool(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.SCHOOL)));
		item.setIntroduction(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.INTRODUCTION)));
		item.setCreateTime(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CREATE_TIME)));
		item.setBindway(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.BIND_WAY)));
		item.setCardAType(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_A_TYPE)));
		item.setCardBType(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_B_TYPE)));
		item.setCardAForm(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_A_FORM)));
		item.setCardBForm(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_B_FORM)));
		item.setCardAOrientation(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_A_ORIENTATION)));
		item.setCardBOrientation(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_B_ORIENTATION)));
		item.setTencentBlogUrl(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.TENCENT_BLOG_URL)));
		item.setSinaBlogUrl(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.SINA_BLOG_URL)));
		item.setTencentQQUrl(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.TENCENT_QQ_URL)));
		item.setInstantMessenger(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.INSTANT_MESSENGER)));
		return item;
	}

	/**
	 * 获取备份对象原型
	 * 
	 * @param contactCursor
	 * @return
	 */
	private ContactVCardBackupJsonEntity getEntityForBackup(Cursor contactCursor) {
		if (Helper.isNotNull(contactCursor)) {
			ContactVCardBackupJsonEntity item = new ContactVCardBackupJsonEntity();
			item.setKeyId(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant._ID)));
			item.setContactId(contactCursor.getLong(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CONTACT_ID)));
			item.setGroupId(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.GROUP_ID)));
			item.setGroupName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.GROUP_NAME)));
			item.setDisplayName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.DISPLAY_NAME)));
			item.setCompanyName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_NAME)));
			item.setJob(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.JOB)));
			item.setBusiness(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.BUSINESS)));
			item.setEnName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.EN_NAME)));
			item.setHeadImg(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.HEAD_IMG)));

			item.setCardId(contactCursor.getLong(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_ID)));
			item.setCardName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_NAME)));
			item.setCardImgA(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_IMG_A)));
			item.setCardImgB(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_IMG_B)));
			item.setMobile(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.MOBILE)));
			item.setTelephone(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.TELEPHONE)));
			item.setFax(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.FAX)));
			item.setEmail(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.EMAIL)));
			item.setAddress(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.ADDRESS)));
			item.setPostcode(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.POSTCODE)));
			item.setCompanyHome(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_HOME)));
			item.setCompanyAbout(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_ABOUT)));
			item.setCardRemark(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_REMARK)));
			item.setRemark(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.REMARK)));
			item.setVcardCode(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.VCARD_NO)));
			item.setCardAType(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_A_TYPE)));
			item.setCardBType(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_B_TYPE)));
			item.setCardAForm(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_A_FORM)));
			item.setCardBForm(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_B_FORM)));
			item.setCardAOrientation(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_A_ORIENTATION)));
			item.setCardBOrientation(contactCursor.getInt(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.CARD_B_ORIENTATION)));

			item.setInstantMessenger(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.INSTANT_MESSENGER)));
			item.setCompanyEnName(contactCursor.getString(contactCursor.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_EN_NAME)));
			item.setOtherCompanyNameList(contactCursor.getString(contactCursor
					.getColumnIndexOrThrow(DatabaseConstant.ContactColumns.COMPANY_NAME_LIST)));
			return item;
		}
		return null;
	}

	/**
	 * 给更新对象赋值
	 * 
	 * @param contact
	 * @param isUpdateGroup 是否更改 分组
	 * @param isRecover 是否 恢复/导入覆盖
	 * @return
	 */
	private ContentValues getContentValues(ContactEntity contact, boolean isUpdateGroup, boolean isRecover) {
		ContentValues values = new ContentValues();
		long cardId = 0;
		if(Helper.isNotNull(contact.getCardId())){
			cardId = contact.getCardId();
		}
		values.put(DatabaseConstant.ContactColumns.VCARD_NO, contact.getVcardNo());
		values.put(DatabaseConstant.ContactColumns.DISPLAY_NAME, contact.getDisplayName());
		values.put(DatabaseConstant.ContactColumns.CARD_ID, cardId);
		values.put(DatabaseConstant.ContactColumns.CONTACT_ID, contact.getContactId());
		values.put(DatabaseConstant.ContactColumns.BIRTHDAY, contact.getBirthday());
		values.put(DatabaseConstant.ContactColumns.BUSINESS, contact.getBusiness());
		values.put(DatabaseConstant.ContactColumns.CARD_IMG_A, contact.getCardImgA());
		values.put(DatabaseConstant.ContactColumns.CARD_IMG_B, contact.getCardImgB());
		values.put(DatabaseConstant.ContactColumns.CARD_NAME, contact.getCardName());
		values.put(DatabaseConstant.ContactColumns.CARD_REMARK, contact.getCardRemark());
		values.put(DatabaseConstant.ContactColumns.CITY, contact.getCity());
		values.put(DatabaseConstant.ContactColumns.COMPANY_ABOUT, contact.getCompanyAbout());
		values.put(DatabaseConstant.ContactColumns.COMPANY_COUNTRY, contact.getCompanyCountry());
		values.put(DatabaseConstant.ContactColumns.COMPANY_HOME, contact.getCompanyHome());
		values.put(DatabaseConstant.ContactColumns.COMPANY_NAME, contact.getCompanyName());
		values.put(DatabaseConstant.ContactColumns.COMPANY_PROVINCE, contact.getCompanyProvince());
		values.put(DatabaseConstant.ContactColumns.COUNTRY, contact.getCountry());
		values.put(DatabaseConstant.ContactColumns.EMAIL, contact.getEmail());
		values.put(DatabaseConstant.ContactColumns.EN_NAME, contact.getEnName());
		values.put(DatabaseConstant.ContactColumns.FAX, contact.getFax());
		values.put(DatabaseConstant.ContactColumns.HEAD_IMG, contact.getHeadImg());
		values.put(DatabaseConstant.ContactColumns.JOB, contact.getJob());
		values.put(DatabaseConstant.ContactColumns.MOBILE, contact.getMobile());
		values.put(DatabaseConstant.ContactColumns.POSTCODE, contact.getPostcode());
		values.put(DatabaseConstant.ContactColumns.PROVINCE, contact.getProvince());
		values.put(DatabaseConstant.ContactColumns.REMARK, contact.getRemark());
		values.put(DatabaseConstant.ContactColumns.STARRED, contact.getStarred());
		values.put(DatabaseConstant.ContactColumns.TELEPHONE, contact.getTelephone());
		values.put(DatabaseConstant.ContactColumns.ADDRESS, contact.getAddress());
		values.put(DatabaseConstant.ContactColumns.CARD_A_TYPE, contact.getCardAType());
		values.put(DatabaseConstant.ContactColumns.CARD_B_TYPE, contact.getCardBType());
		values.put(DatabaseConstant.ContactColumns.CARD_A_FORM, contact.getCardAForm());
		values.put(DatabaseConstant.ContactColumns.CARD_B_FORM, contact.getCardBForm());
		values.put(DatabaseConstant.ContactColumns.CARD_A_ORIENTATION, contact.getCardAOrientation());
		values.put(DatabaseConstant.ContactColumns.CARD_B_ORIENTATION, contact.getCardBOrientation());
		values.put(DatabaseConstant.ContactColumns.TENCENT_BLOG_URL, contact.getTencentBlogUrl());
		values.put(DatabaseConstant.ContactColumns.SINA_BLOG_URL, contact.getSinaBlogUrl());
		values.put(DatabaseConstant.ContactColumns.TENCENT_QQ_URL, contact.getTencentQQUrl());
		values.put(DatabaseConstant.ContactColumns.INSTANT_MESSENGER, contact.getInstantMessenger());
		values.put(DatabaseConstant.ContactColumns.COMPANY_EN_NAME, contact.getCompanyEnName());
		values.put(DatabaseConstant.ContactColumns.COMPANY_NAME_LIST, contact.getOtherCompanyNameList());
		values.put(DatabaseConstant.ContactColumns.COMPANY_LOGO, contact.getCompanyLogo());
		values.put(DatabaseConstant.ContactColumns.ORDER_CODE, contact.getOrderCode());
		if (!isRecover) {
			values.put(DatabaseConstant.ContactColumns.ACCOUNT_GRADE, contact.getAccountGrade());
			values.put(DatabaseConstant.ContactColumns.AUTH, contact.getAuth());
			values.put(DatabaseConstant.ContactColumns.DEGREE, contact.getDegree());
			values.put(DatabaseConstant.ContactColumns.NATIVEPLACE, contact.getNativeplace());
			values.put(DatabaseConstant.ContactColumns.COMPANY_APPROVAL, contact.getCompanyApproval());
			values.put(DatabaseConstant.ContactColumns.SCHOOL, contact.getSchool());
			values.put(DatabaseConstant.ContactColumns.INTRODUCTION, contact.getIntroduction());
			values.put(DatabaseConstant.ContactColumns.SELF_SIGN, contact.getSelfSign());
			values.put(DatabaseConstant.ContactColumns.SEX, contact.getSex());
			values.put(DatabaseConstant.ContactColumns.BIND_WAY, contact.getBindway());
		}
		if (isUpdateGroup) {
			values.put(DatabaseConstant.ContactColumns.GROUP_ID, contact.getGroupId());
			values.put(DatabaseConstant.ContactColumns.GROUP_NAME, contact.getGroupName());
		}
		return values;
	}

	/**
	 * 统计 分组下的联系人数
	 * 
	 * @return Map<Integer,Integer> <分组id，联系人数>
	 */
	private Map<Integer, Integer> getContactCount() {
		mCountMap = new HashMap<Integer, Integer>();
		String sql = "select group_id,count(*) as contactCount from " + DatabaseConstant.DBTableName.TABLE_CONTACTS + " where "
				+ DatabaseConstant.ContactColumns.STARRED + " =0 group by group_id " + " union all select " + ContactGroupEntity.GROUP_FAVORITE_ID
				+ ",count(*) from contacts where " + DatabaseConstant.ContactColumns.STARRED + "=1" + " union all select "
				+ ContactGroupEntity.GROUP_ALL_ID + ",count(*) from contacts where group_id > " + ContactGroupEntity.GROUP_BLACKLIST_ID;

		// 统计最近联系人查询语句 begin
		String recentWhere = " where " + DatabaseConstant._ID + ">1 ";
		// 加密不显示收藏
		String encodePwd = PreferencesHelper.getInstance().getString(Constants.Preferences.KEY_CARDCASE_COLLECT_LOCKED_PWD);
		if (Helper.isNotEmpty(encodePwd)) {
			recentWhere += " and " + DatabaseConstant.ContactColumns.STARRED + " = 0 ";
		}
		sql += " union all select " + ContactGroupEntity.GROUP_RECENTCONTACT_ID + ", count(*) from (select _id from "
				+ DatabaseConstant.DBTableName.TABLE_CONTACTS + recentWhere + " order by " + DatabaseConstant.ContactColumns.CREATE_TIME
				+ " desc limit 0,20)";
		// 统计最近联系人查询语句end

		Cursor countCursor = this.getSQLiteDatabase().rawQuery(sql, null);
		if (Helper.isNotNull(countCursor)) {
			while (countCursor.moveToNext()) {
				mCountMap.put(countCursor.getInt(0), countCursor.getInt(1));
			}
			countCursor.close();
			countCursor = null;
		}
		return mCountMap;
	}

	/**
	 * 移动至分组 操作项
	 * 
	 * @param toGroupId
	 * @param toGroupName
	 * @return ContentValues
	 */
	private ContentValues getMoveContentValues(int toGroupId, String toGroupName) {

		ContentValues values = new ContentValues();
		if (toGroupId == ContactGroupEntity.GROUP_FAVORITE_ID) {
			values.put(DatabaseConstant.ContactColumns.STARRED, 1);
		} else {
			values.put(DatabaseConstant.ContactColumns.GROUP_ID, toGroupId);
			values.put(DatabaseConstant.ContactColumns.GROUP_NAME, toGroupName);
			values.put(DatabaseConstant.ContactColumns.STARRED, 0);
		}
		return values;
	}

	/**
	 * 删除单个分组 ,并更新分组下的联系人
	 * 
	 * @param groupId
	 * @return boolean
	 * @throws Exception
	 */
	private boolean deleteGroupContact(int groupId, boolean isDelContact) {
		boolean delRow = false;

		try {
			if (isDelContact) {
				delRow = deleteContactByGroup(groupId);
			} else {
				delRow = moveContact(groupId, ContactGroupEntity.GROUP_UNGROUPED_ID, mContext.getString(R.string.ungrouped));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return delRow;
	}

	/**
	 * 新增联系人 返回新增键值
	 * 
	 * @param contact
	 * @return int
	 */
	private int insertContact(ContactEntity contact) {
		if (Helper.isNull(contact)) {
			return 0;
		}
		// 微片小秘书 不导入
		String vcardNo = contact.getVcardNo();
		if(Helper.isNull(vcardNo)){
			vcardNo = "";
		}
		if (Helper.isNotEmpty(vcardNo) && vcardNo.equals(Constants.DefaultUser.DEFAULT_USER_VCARD_NO)) {
			return 1;
		}
		
		Long cardId = contact.getCardId();
		if (Helper.isNull(cardId)) {
			cardId = 0L;
		}
		String cardName = contact.getCardName();
		if (Helper.isNull(cardName)) {
			cardName = "";
		}
		String cardImgA = contact.getCardImgA();
		if (Helper.isNull(cardImgA)) {
			cardImgA = "";
		}
		String cardImgB = contact.getCardImgB();
		if (Helper.isNull(cardImgB)) {
			cardImgB = "";
		}
		String sql = new StringBuilder().append("insert into ").append(TABLE_CONTACTS).append("(").append(DatabaseConstant.ContactColumns.CONTACT_ID)
				.append(",").append(DatabaseConstant.ContactColumns.GROUP_ID).append(",").append(DatabaseConstant.ContactColumns.GROUP_NAME)
				.append(",").append(DatabaseConstant.ContactColumns.PARTNER_ID).append(",").append(DatabaseConstant.ContactColumns.DISPLAY_NAME)
				.append(",").append(DatabaseConstant.ContactColumns.ACCOUNT_NAME).append(",").append(DatabaseConstant.ContactColumns.VCARD_NO)
				.append(",").append(DatabaseConstant.ContactColumns.EN_NAME).append(",").append(DatabaseConstant.ContactColumns.ACCOUNT_GRADE)
				.append(",").append(DatabaseConstant.ContactColumns.AUTH).append(",").append(DatabaseConstant.ContactColumns.BIRTHDAY).append(",")
				.append(DatabaseConstant.ContactColumns.COUNTRY).append(",").append(DatabaseConstant.ContactColumns.PROVINCE).append(",")
				.append(DatabaseConstant.ContactColumns.CITY).append(",").append(DatabaseConstant.ContactColumns.NATIVEPLACE).append(",")
				.append(DatabaseConstant.ContactColumns.DEGREE).append(",").append(DatabaseConstant.ContactColumns.SEX).append(",")
				.append(DatabaseConstant.ContactColumns.SELF_SIGN).append(",").append(DatabaseConstant.ContactColumns.ORDER_CODE).append(",")
				.append(DatabaseConstant.ContactColumns.HEAD_IMG).append(",").append(DatabaseConstant.ContactColumns.CARD_ID).append(",")
				.append(DatabaseConstant.ContactColumns.CARD_NAME).append(",").append(DatabaseConstant.ContactColumns.CARD_IMG_A).append(",")
				.append(DatabaseConstant.ContactColumns.CARD_IMG_B).append(",").append(DatabaseConstant.ContactColumns.MOBILE).append(",")
				.append(DatabaseConstant.ContactColumns.TELEPHONE).append(",").append(DatabaseConstant.ContactColumns.FAX).append(",")
				.append(DatabaseConstant.ContactColumns.EMAIL).append(",").append(DatabaseConstant.ContactColumns.ADDRESS).append(",")
				.append(DatabaseConstant.ContactColumns.POSTCODE).append(",").append(DatabaseConstant.ContactColumns.COMPANY_NAME).append(",")
				.append(DatabaseConstant.ContactColumns.COMPANY_COUNTRY).append(",").append(DatabaseConstant.ContactColumns.COMPANY_PROVINCE)
				.append(",").append(DatabaseConstant.ContactColumns.COMPANY_HOME).append(",").append(DatabaseConstant.ContactColumns.JOB).append(",")
				.append(DatabaseConstant.ContactColumns.BUSINESS).append(",").append(DatabaseConstant.ContactColumns.COMPANY_APPROVAL).append(",")
				.append(DatabaseConstant.ContactColumns.ACCOUNT_ID).append(",").append(DatabaseConstant.ContactColumns.SCHOOL).append(",")
				.append(DatabaseConstant.ContactColumns.INTRODUCTION).append(",").append(DatabaseConstant.ContactColumns.CREATE_TIME).append(",")
				.append(DatabaseConstant.ContactColumns.BIND_WAY).append(",").append(DatabaseConstant.ContactColumns.REGISTER_TIME).append(",")
				.append(DatabaseConstant.ContactColumns.CARD_A_TYPE).append(",").append(DatabaseConstant.ContactColumns.CARD_B_TYPE).append(",")
				.append(DatabaseConstant.ContactColumns.CARD_A_FORM).append(",").append(DatabaseConstant.ContactColumns.CARD_B_FORM).append(",")
				.append(DatabaseConstant.ContactColumns.CARD_A_ORIENTATION).append(",").append(DatabaseConstant.ContactColumns.CARD_B_ORIENTATION)
				.append(",").append(DatabaseConstant.ContactColumns.TENCENT_BLOG_URL).append(",")
				.append(DatabaseConstant.ContactColumns.SINA_BLOG_URL).append(",").append(DatabaseConstant.ContactColumns.TENCENT_QQ_URL).append(",")
				.append(DatabaseConstant.ContactColumns.INSTANT_MESSENGER).append(",").append(DatabaseConstant.ContactColumns.COMPANY_EN_NAME)
				.append(",").append(DatabaseConstant.ContactColumns.COMPANY_NAME_LIST).append(",")
				.append(DatabaseConstant.ContactColumns.COMPANY_LOGO).append(",").append(DatabaseConstant.ContactColumns.REMARK).append(",")
				.append(DatabaseConstant.ContactColumns.CARD_REMARK).append(",").append(DatabaseConstant.ContactColumns.COMPANY_ABOUT)
				.append(") values(?,?,?,?,?,?,?,?,?,?").append(",?,?,?,?,?,?,?,?,?,?").append(",?,?,?,?,?,?,?,?,?,?").append(",?,?,?,?,?,?,?,?,?,?")
				.append(",?,?,?,?,?,?,?,?,?,?").append(",?,?,?,?,?,?,?,?,?);").toString();

		Object[] bindArgs = new Object[] { contact.getContactId(), contact.getGroupId(), contact.getGroupName(), contact.getPartnerId(),
				contact.getDisplayName(), contact.getAccountName(), contact.getVcardNo(), contact.getEnName(), contact.getAccountGrade(),
				contact.getAuth(), contact.getBirthday(), contact.getCountry(), contact.getProvince(), contact.getCity(), contact.getNativeplace(),
				contact.getDegree(), contact.getSex(), contact.getSelfSign(), contact.getOrderCode(), contact.getHeadImg(), cardId, cardName,
				cardImgA, cardImgB, contact.getMobile(), contact.getTelephone(), contact.getFax(), contact.getEmail(), contact.getAddress(),
				contact.getPostcode(), contact.getCompanyName(), contact.getCompanyCountry(), contact.getCompanyProvince(), contact.getCompanyHome(),
				contact.getJob(), contact.getBusiness(), contact.getCompanyApproval(), contact.getAccountId(), contact.getSchool(),
				contact.getIntroduction(), contact.getCreateTime(), contact.getBindway(), contact.getRegisterTime(), contact.getCardAType(),
				contact.getCardBType(), contact.getCardAForm(), contact.getCardBForm(), contact.getCardAOrientation(), contact.getCardBOrientation(),
				contact.getTencentBlogUrl(), contact.getSinaBlogUrl(), contact.getTencentQQUrl(), contact.getInstantMessenger(),
				contact.getCompanyEnName(), contact.getOtherCompanyNameList(), contact.getCompanyLogo(), contact.getCompanyAbout(),
				contact.getRemark(), contact.getCardRemark() };
		this.getSQLiteDatabase().execSQL(sql, bindArgs);

		int newKeyId = 0;
		try {
			newKeyId = (int) VCardSQLiteDatabase.getInstance().getCurSequence(TABLE_CONTACTS);
		} catch (Exception e) {
			Log.e(TAG, "获取新增键值异常", e);
		}
		return newKeyId;

	}
	
	/**
	 * 判断是插入数据 还是 修改数据 (返回本地对应的键值id) 当contactId 大于0时,
	 * 若cardId为0,vcardNo为空则表示本地新增名片,否则为交换名片
	 * 
	 * @param contactId
	 * @param cardId
	 * @param vcardNo
	 * @return
	 */
	private int isExist(long contactId, long cardId, String vcardNo) {
		int curKeyId = 0;
		if (contactId == 0 && cardId == 0 && Helper.isEmpty(vcardNo)) {
			return curKeyId;
		}
		String where = "";
		if(cardId > 0){
			where = DatabaseConstant.ContactColumns.CARD_ID.concat("=" + cardId);
			if(Helper.isNotEmpty(vcardNo)){
				where.concat(" and ").concat(DatabaseConstant.ContactColumns.VCARD_NO).concat( "='").concat(vcardNo).concat("'");
			}
		}else{
			where = DatabaseConstant.ContactColumns.CONTACT_ID.concat("=" + contactId);
		}
		
		String sql = " select " + DatabaseConstant._ID + " from " + TABLE_CONTACTS + " where " + where;
		try{
			Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
			if (Helper.isNotNull(cursor)) {
				if (cursor.moveToFirst()) {
					curKeyId = cursor.getInt(0);
				}
				cursor.close();
				cursor = null;
			}
		}catch(NullPointerException e){
			Log.e(TAG, "数据库对象未创建");
		}
		
		return curKeyId;

	}

	/**
	 * 根据 姓名和名片查找记录 (手机号可空)
	 * 
	 * @param name
	 * @param mobile
	 * @param imgA
	 * @return
	 */
	private int isExist(String name, String mobile, String imgA) {
		int curKeyId = 0;
		if (Helper.isEmpty(name) && Helper.isEmpty(imgA)) {
			return curKeyId;
		}
		String where = " rtrim(" + DatabaseConstant.ContactColumns.DISPLAY_NAME + ") ='" + name + "'" + " and rtrim("
				+ DatabaseConstant.ContactColumns.CARD_IMG_A + ") = '" + imgA + "'";
		if (Helper.isNotEmpty(mobile)) {
			where += " and rtrim(" + DatabaseConstant.ContactColumns.MOBILE + ") = '" + mobile + "'";
		}

		String sql = " select " + DatabaseConstant._ID + " from " + TABLE_CONTACTS + "  where " + where;
		try{
			Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
			if (Helper.isNotNull(cursor)) {
				if (cursor.moveToFirst()) {
					curKeyId = cursor.getInt(0);
				}
				cursor.close();
				cursor = null;
			}
		}catch(NullPointerException e){
			Log.e(TAG, "数据库对象未创建");
		}
		return curKeyId;
	}

	/**
	 * 判断联系人是否存在
	 * 
	 * @param contact
	 * @return _id
	 */
	private int isExist(ContactEntity contact) {
		if (Helper.isEmpty(contact)) {
			return 0;
		}
		int curKeyId = isExist(contact.getContactId(), contact.getCardId(), contact.getVcardNo());

		// 本地新增名片多加判断
		if (curKeyId == 0 && contact.getCardId() == 0) {
			curKeyId = isExist(contact.getDisplayName(), contact.getMobile(), contact.getCardImgA());
		}
		return curKeyId;
	}

	/**
	 * 移动整个分组下的联系人
	 * 
	 * @param fromGroupId
	 * @param toGroupId
	 * @param toGroupName
	 * @return boolean
	 */
	private boolean moveContact(int fromGroupId, int toGroupId, String toGroupName) {

		String where = DatabaseConstant.ContactColumns.GROUP_ID + " = " + fromGroupId;

		ContentValues values = getMoveContentValues(toGroupId, toGroupName);
		int upRow = this.getSQLiteDatabase().update(TABLE_CONTACTS, values, where, null);
		return upRow > 0 ? true : false;
	}

	/**
	 * 恢复/导入 数据更新
	 * 
	 * @param recoverContact
	 */
	private void recoverContact(ContactEntity recoverContact) {
		if (Helper.isNull(recoverContact)) {
			return;
		}
		// 获取本地分组最新数据, 若不存在分组则新增
		int groupId = getGroupId(recoverContact.getGroupName(), true);
		recoverContact.setGroupId(groupId);

		int curKeyId = isExist(recoverContact.getContactId(), recoverContact.getCardId(), recoverContact.getVcardNo());
		if (curKeyId > 0) {
			recoverContact.setId(curKeyId);
			updateContact(recoverContact, true, true);
		} else {
			// 名片没有上传到服务端情况判断
			curKeyId = isExist(recoverContact.getDisplayName(), recoverContact.getMobile(), recoverContact.getCardImgA());
			if (curKeyId > 0) {
				recoverContact.setId(curKeyId);
				updateContact(recoverContact, true, true);
			} else {
				insertContact(recoverContact);
			}
		}

	}

	/**
	 * 更改 名片联系人
	 * 
	 * @param values
	 * @param keyID
	 *            键值id
	 * @return
	 */
	private boolean updateContact(ContentValues values, int keyID) {
		boolean result = false;
		String where = DatabaseConstant._ID + "=" + keyID;

		int upRows = this.getSQLiteDatabase().update(TABLE_CONTACTS, values, where, null);
		if (upRows > 0) {
			result = true;
		}
		return result;
	}

	/**
	 * 更新联系人信息
	 * 
	 * @param contact
	 * @param isUpdateGroup
	 *            是否更改分组
	 * @return
	 */
	private boolean updateContact(ContactEntity contact, boolean isUpdateGroup, boolean isRecover) {
		boolean result = false;
		if (Helper.isNull(contact)) {
			return result;
		}
		String where = DatabaseConstant._ID + " = " + contact.getId();
		
		ContentValues values = getContentValues(contact, isUpdateGroup, isRecover);
		try{
			int upRows = this.getSQLiteDatabase().update(TABLE_CONTACTS, values, where, null);
			if (upRows > 0) {
				result = true;
			}
		}catch(NullPointerException e){
			Log.e(TAG, "数据库对象未创建");
		}
		return result;
	}

	/**
	 * 移动联系人 至 分组
	 * 
	 * @param keyIds
	 *            "," 拼接的集合
	 * @param toGroupId
	 * @param toGroupName
	 * @return boolean
	 */
	private boolean moveContact(String keyIds, int toGroupId, String toGroupName) {
		if (Helper.isEmpty(keyIds)) {
			return false;
		}
		String where = DatabaseConstant._ID + " in ( " + keyIds + " ) ";
		ContentValues values = getMoveContentValues(toGroupId, toGroupName);
		int upRow = this.getSQLiteDatabase().update(TABLE_CONTACTS, values, where, null);
		return upRow > 0 ? true : false;
	}
	/**
	 * 根据条件获取电话列表
	 * @param where
	 * @return
	 */
	private ArrayList<String> getPhoneList(String where){
		ArrayList<String> phoneList = null;
		StringBuilder sb = new StringBuilder();
		String mobile = "", telephone = "", fax = "";
		Cursor cursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, new String[]{DatabaseConstant.ContactColumns.MOBILE, DatabaseConstant.ContactColumns.TELEPHONE, DatabaseConstant.ContactColumns.FAX}
				, where, null, null, null, DatabaseConstant.ContactColumns.CREATE_TIME + " desc");
		if(Helper.isNotNull(cursor)){
			phoneList = new ArrayList<String>();
			while (cursor.moveToNext()) {
				mobile = cursor.getString(cursor.getColumnIndex(DatabaseConstant.ContactColumns.MOBILE));
				telephone = cursor.getString(cursor.getColumnIndex(DatabaseConstant.ContactColumns.TELEPHONE));
				fax = cursor.getString(cursor.getColumnIndex(DatabaseConstant.ContactColumns.FAX));
				if(Helper.isNotNull(mobile) && !"".equals(mobile)){
					sb.append(mobile).append(",");
				}
				if(Helper.isNotNull(telephone) && !"".equals(telephone)){
					sb.append(telephone).append(",");
				}
				if(Helper.isNotNull(fax) && !"".equals(fax)){
					sb.append(fax).append(",");
				}
				
			}
			if(Helper.isNotNull(sb)){
				phoneList = ResourceHelper.getListFromString(sb.toString(), ",");
			}
			cursor.close();
			cursor = null;
		}
		return phoneList;
	}

	//#endregion private 方法
	/**
	 * 根据名片id判断联系人是否存在，并返回本地 键值id
	 * @param cardId
	 * @return
	 */
	public int isExist(Long cardId){
		int curKeyId = 0;
		if(Helper.isNull(cardId) || cardId.longValue() < 1){
			return curKeyId;
		}
		String sql = " select " + DatabaseConstant._ID + " from " + TABLE_CONTACTS + " where " + DatabaseConstant.ContactColumns.CARD_ID + "=" + cardId;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
		if (Helper.isNotNull(cursor)) {
			if (cursor.moveToFirst()) {
				curKeyId = cursor.getInt(0);
			}
			cursor.close();
			cursor = null;
		}
		return curKeyId;
	}
	/**
	 * 是否交换过名片
	 * @param accountId
	 * @return
	 */
	public boolean isExistByAccount(long accountId){
		if(accountId < 1){
			return false;
		}
		boolean exist = false;
		String sql = " select " + DatabaseConstant._ID + " from " + TABLE_CONTACTS + " where " + DatabaseConstant.ContactColumns.ACCOUNT_ID + "=" + accountId;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
		if (Helper.isNotNull(cursor) ) {
			if (cursor.getCount() > 0) {
				exist = true;
			}
			cursor.close();
			cursor = null;
		}
		return exist;
	}
	/**
	 * 根据分组id 获取 显示 列表
	 * 
	 * @param groupId
	 * @return ArrayList<ContactListItemEntity>
	 */
	public ArrayList<ContactListItemEntity> getList(int groupId) {

		ArrayList<ContactListItemEntity> contactList = new ArrayList<ContactListItemEntity>();
		String limit = null;
		String orderBy = DatabaseConstant.ContactColumns.ORDER_CODE;
		String where = "";
		String selectTable = TABLE_CONTACTS;
		if (groupId == ContactGroupEntity.GROUP_FAVORITE_ID) {
			// 收藏夹
			where = DatabaseConstant.ContactColumns.STARRED + " = 1 ";
		} else if (groupId >= ContactGroupEntity.GROUP_UNGROUPED_ID) {
			// 具体分组和未分组（排除收藏夹）
			where = DatabaseConstant.ContactColumns.STARRED + " = 0 and " + DatabaseConstant.ContactColumns.GROUP_ID + " = " + groupId;
		} else if(groupId == ContactGroupEntity.GROUP_BLACKLIST_ID){
			// 黑名单
			where = DatabaseConstant.ContactColumns.GROUP_ID + " = " + groupId;
		}else {
			// 加密不显示收藏
			String encodePwd = PreferencesHelper.getInstance().getString(Constants.Preferences.KEY_CARDCASE_COLLECT_LOCKED_PWD);
			if (Helper.isNotEmpty(encodePwd)) {
				where = DatabaseConstant.ContactColumns.STARRED + " = 0 ";
			} else {
				where = " 1 ";
			}

			if (groupId == ContactGroupEntity.GROUP_ALL_ID) {
				// 全部联系人(去除黑名单)
				where += " and " + DatabaseConstant.ContactColumns.GROUP_ID + " > " + ContactGroupEntity.GROUP_BLACKLIST_ID;
			} else if (groupId == ContactGroupEntity.GROUP_RECENTCONTACT_ID) {
				// 最近联系人（排除小秘书）
				where += " and " + DatabaseConstant._ID + " > 1 " + " order by " + DatabaseConstant.ContactColumns.CREATE_TIME + " desc limit 0,20) ";

				// 取出前20条记录 再排序
				selectTable = " ( select * from " + TABLE_CONTACTS;
			}
		}

		Cursor contactCursor = this.getSQLiteDatabase().query(selectTable, CONTACT_PROJECTION_LIST, where, null, null, null, orderBy, limit);
		if (Helper.isNotNull(contactCursor)) {
			while (contactCursor.moveToNext()) {
				ContactListItemEntity item = getEntityForListItem(contactCursor);
				contactList.add(item);
			}
			contactCursor.close();
			contactCursor = null;
		}
		return contactList;
	}

	/**
	 * 查询 联系人列表
	 * 
	 * @param keyIds
	 *            用,隔开的id集合
	 * @param isContain
	 *            是含 id 还是 排除 id
	 * @return ArrayList<ContactListItemEntity>
	 */
	public ArrayList<ContactListItemEntity> getList(String keyIds, boolean isContain) {
		if (Helper.isEmpty(keyIds)) {
			return null;
		}
		String where = "";
		if (isContain) {
			where = DatabaseConstant._ID + " in (" + keyIds + ")";
		} else {
			where = DatabaseConstant._ID + " not in (" + keyIds + ")";
		}
		ArrayList<ContactListItemEntity> contactList = null;
		Cursor contactCursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, CONTACT_PROJECTION_LIST, where, null, null, null,
				DatabaseConstant.ContactColumns.ORDER_CODE);
		if (Helper.isNotNull(contactCursor)) {
			contactList = new ArrayList<ContactListItemEntity>();
			while (contactCursor.moveToNext()) {
				ContactListItemEntity item = getEntityForListItem(contactCursor);
				contactList.add(item);
			}
			contactCursor.close();
			contactCursor = null;
		}
		return contactList;
	}
	/**
	 * 获取讨论组添加成员的列表
	 * @param circleGroupId
	 * @return
	 */
	public ArrayList<ContactListItemEntity> getListForCircle(long circleGroupId){
		if(circleGroupId < 1){
			return null;
		}
		String where =  DatabaseConstant._ID.concat(">1 and ").concat(DatabaseConstant.ContactColumns.ACCOUNT_ID).concat(" not in (")
				.concat(" select ").concat(DatabaseConstant.CircleGroupMemberColumns.ACCOUNT_ID).concat(" from ").concat(DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUP_MEMBERS)
				.concat(" where ").concat(DatabaseConstant.CircleGroupMemberColumns.GROUP_ID).concat("=" + circleGroupId)
				.concat(")");

		ArrayList<ContactListItemEntity> contactList = null;
		Cursor contactCursor = null;
		try {
			contactCursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, CONTACT_PROJECTION_LIST, where, null, null, null,
					DatabaseConstant.ContactColumns.ORDER_CODE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Helper.isNotNull(contactCursor)) {
			contactList = new ArrayList<ContactListItemEntity>();
			while (contactCursor.moveToNext()) {
				ContactListItemEntity item = getEntityForListItem(contactCursor);
				contactList.add(item);
			}
			contactCursor.close();
			contactCursor = null;
		}
		return contactList;
	}
	/**
	 * 获取交换的联系人列表
	 * 
	 * @param cardId
	 *            不含某张名片 (0 获取全部)
	 * @return
	 */
	public ArrayList<ContactListItemEntity> getListForExchange(long cardId) {
		// 排除微片小秘书
		String where = DatabaseConstant.ContactColumns.CARD_ID + ">1 ";
		if (cardId > 1) {
			where += " and " + DatabaseConstant.ContactColumns.CARD_ID + "<>" + cardId;
		}
		ArrayList<ContactListItemEntity> contactList = null;
		Cursor contactCursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, CONTACT_PROJECTION_LIST, where, null, null, null,
				DatabaseConstant.ContactColumns.ORDER_CODE);
		if (Helper.isNotNull(contactCursor)) {
			contactList = new ArrayList<ContactListItemEntity>();
			while (contactCursor.moveToNext()) {
				ContactListItemEntity item = getEntityForListItem(contactCursor);
				contactList.add(item);
			}
			contactCursor.close();
			contactCursor = null;
		}
		return contactList;
	}

	/**
	 * 根据分组id 获取导出联系人的所有信息
	 * 
	 * @param groupId
	 * @return ArrayList<ContactEntity>
	 */
	public ArrayList<ContactEntity> getListForExport(int groupId) {
		ArrayList<ContactEntity> contactList = new ArrayList<ContactEntity>();

		String orderBy = DatabaseConstant.ContactColumns.ORDER_CODE;
		String where = " 1 ";
		if (groupId == ContactGroupEntity.GROUP_ALL_ID) {
			where += " and " + DatabaseConstant.ContactColumns.GROUP_ID + " <> " + ContactGroupEntity.GROUP_BLACKLIST_ID;
		} else if (groupId == ContactGroupEntity.GROUP_FAVORITE_ID) {
			where += " and " + DatabaseConstant.ContactColumns.STARRED + " = 1 ";
		} else {
			where += " and " + DatabaseConstant.ContactColumns.GROUP_ID + " = " + groupId;
		}
		Cursor contactCursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, CONTACT_PROJECTION_DETAILS, where, null, null,
				null, orderBy);
		while (contactCursor.moveToNext()) {
			ContactEntity item = getEntity(contactCursor);
			contactList.add(item);
		}
		contactCursor.close();
		contactCursor = null;
		return contactList;
	}
	/**
	 * 分组获取列表返回所有字段
	 * @param groupId
	 * @return
	 */
	public ArrayList<ContactEntity> getListForAll(int groupId){

		ArrayList<ContactEntity> contactList = new ArrayList<ContactEntity>();
		String limit = null;
		String orderBy = DatabaseConstant.ContactColumns.ORDER_CODE;
		String where = "";
		String selectTable = TABLE_CONTACTS;
		if (groupId == ContactGroupEntity.GROUP_FAVORITE_ID) {
			// 收藏夹
			where = DatabaseConstant.ContactColumns.STARRED + " = 1 ";
		} else if (groupId >= ContactGroupEntity.GROUP_UNGROUPED_ID) {
			// 具体分组和未分组（排除收藏夹）
			where = DatabaseConstant.ContactColumns.STARRED + " = 0 and " + DatabaseConstant.ContactColumns.GROUP_ID + " = " + groupId;
		} else if(groupId == ContactGroupEntity.GROUP_BLACKLIST_ID){
			// 黑名单
			where = DatabaseConstant.ContactColumns.GROUP_ID + " = " + groupId;
		}else {
			// 加密不显示收藏
			String encodePwd = PreferencesHelper.getInstance().getString(Constants.Preferences.KEY_CARDCASE_COLLECT_LOCKED_PWD);
			if (Helper.isNotEmpty(encodePwd)) {
				where = DatabaseConstant.ContactColumns.STARRED + " = 0 ";
			} else {
				where = " 1 ";
			}

			if (groupId == ContactGroupEntity.GROUP_ALL_ID) {
				// 全部联系人(去除黑名单)
				where += " and " + DatabaseConstant.ContactColumns.GROUP_ID + " > " + ContactGroupEntity.GROUP_BLACKLIST_ID;
			} else if (groupId == ContactGroupEntity.GROUP_RECENTCONTACT_ID) {
				// 最近联系人（排除小秘书）
				where += " and " + DatabaseConstant._ID + " > 1 " + " order by " + DatabaseConstant.ContactColumns.CREATE_TIME + " desc limit 0,20) ";

				// 取出前20条记录 再排序
				selectTable = " ( select * from " + TABLE_CONTACTS;
			}
		}

		Cursor contactCursor = this.getSQLiteDatabase().query(selectTable, CONTACT_PROJECTION_DETAILS, where, null, null, null, orderBy, limit);
		if (Helper.isNotNull(contactCursor)) {
			while (contactCursor.moveToNext()) {
				ContactEntity item = getEntity(contactCursor);
				contactList.add(item);
			}
			contactCursor.close();
			contactCursor = null;
		}
		return contactList;
	}
	/**
	 * 获取所有备份数据
	 * 
	 * @return ArrayList<ContactVCardBackupJsonEntity>
	 */
	public ArrayList<ContactVCardBackupJsonEntity> getListForBackup() {
		ArrayList<ContactVCardBackupJsonEntity> backupList = new ArrayList<ContactVCardBackupJsonEntity>();
		String selSql = "select " + BACKUP_COLUMNS + " from " + TABLE_CONTACTS + " where " + DatabaseConstant._ID + "> 1 ";
		try {
			Cursor contactCursor = this.getSQLiteDatabase().rawQuery(selSql, null);
			if (Helper.isNotNull(contactCursor)) {
				while (contactCursor.moveToNext()) {
					ContactVCardBackupJsonEntity item = getEntityForBackup(contactCursor);
					backupList.add(item);
				}
				contactCursor.close();
				contactCursor = null;
			}
		} catch (Exception e) {
			Log.e(TAG, "获取备份数据异常", e);
		}
		return backupList;
	}

	/**
	 * 获取备份恢复的联系人列表
	 * 
	 * @param groupList
	 * @return ArrayList<ContactVCardBackupJsonEntity>
	 */
	public ArrayList<ContactVCardBackupJsonEntity> getListForBackup(ArrayList<ContactGroupBackupEntity> groupList) {
		ArrayList<ContactVCardBackupJsonEntity> backupList = new ArrayList<ContactVCardBackupJsonEntity>();
		if (Helper.isEmpty(groupList)) {
			return backupList;
		}
		String selSql = "";
		String selsqlPre = "select " + BACKUP_COLUMNS + " from " + TABLE_CONTACTS + " where " + DatabaseConstant._ID
				+ "> 1 ";
		String favWhere = null;
		StringBuilder idCombin = new StringBuilder();

		for (int i = 0, size = groupList.size(); i < size; i++) {
			int curGrpId = groupList.get(i).getGroupId();
			if (curGrpId != ContactGroupEntity.GROUP_FAVORITE_ID) {
				idCombin.append(curGrpId + ",");
			} else {
				favWhere = " and " + DatabaseConstant.ContactColumns.STARRED + "=1";
			}
		}
			/**
			 * 非收藏的分组联系人
			 */
			if (Helper.isNotEmpty(idCombin)) {
				String selSqlGrp = selsqlPre + " and " + DatabaseConstant.ContactColumns.STARRED + "<>1" + " and group_id in (" + idCombin.substring(0, idCombin.length() - 1) + ")";
				if (Helper.isNotEmpty(selSql)) {
					selSql += "union all ";
				}
				selSql += selSqlGrp;
			}
			// 收藏夹联系人
			if (Helper.isNotEmpty(favWhere)) {
				String selSqlFav = selsqlPre + favWhere;
				if (Helper.isNotEmpty(selSql)) {
					selSql += "union all ";
				}
				selSql += selSqlFav;
			}
		
		try {
			if (Helper.isNotEmpty(selSql)) {
				Cursor contactCursor = this.getSQLiteDatabase().rawQuery(selSql, null);
				if (Helper.isNotNull(contactCursor)) {
					while (contactCursor.moveToNext()) {
						ContactVCardBackupJsonEntity item = getEntityForBackup(contactCursor);
						backupList.add(item);
					}
				}
				contactCursor.close();
				contactCursor = null;
			}
		} catch (Exception e) {
			Log.e(TAG, "获取备份数据异常", e);
		}

		return backupList;
	}

	/**
	 * 根据微片号获取列表联系人对象
	 * 
	 * @param vcardNo
	 * @return ContactListItemEntity
	 */
	public ContactListItemEntity getEntityForListItem(String vcardNo) {
		ContactListItemEntity item = null;
		String where = DatabaseConstant.ContactColumns.VCARD_NO + " = '" + vcardNo + "'";
		Cursor contactCursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, CONTACT_PROJECTION_LIST, where, null, null, null, null);
		if (Helper.isNotNull(contactCursor)) {
			if (contactCursor.moveToFirst()) {
				item = getEntityForListItem(contactCursor);
			}
			contactCursor.close();
			contactCursor = null;
		}
		return item;
	}
	
	/**
	 * 根据微片号获取列表联系人对象
	 * @param vcardNo
	 * @return
	 */
	public ContactEntity getContactEntityForListItem(String vcardNo){
		ContactEntity item = null;
		String where = DatabaseConstant.ContactColumns.VCARD_NO + " = '" + vcardNo + "'";
		Cursor contactCursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, CONTACT_PROJECTION_LIST, where, null, null, null, null);
		if (Helper.isNotNull(contactCursor)) {
			if (contactCursor.moveToFirst()) {
				item = getEntity(contactCursor);
			}
			contactCursor.close();
			contactCursor = null;
		}
		return item;
	}
	
	/**
	 * 根据账户id 获取第一条对象数据
	 * @param accountId
	 * @return
	 */
	public ContactListItemEntity getEntityForListItemByAccount(long accountId) {
		ContactListItemEntity item = null;
		String where = DatabaseConstant.ContactColumns.ACCOUNT_ID + " = " + accountId;
		Cursor contactCursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, CONTACT_PROJECTION_LIST, where, null, null, null, null);
		if (Helper.isNotNull(contactCursor)) {
			if (contactCursor.moveToFirst()) {
				item = getEntityForListItem(contactCursor);
			}
			contactCursor.close();
			contactCursor = null;
		}
		return item;
	}
	/**
	 * 根据名片id获取对象
	 * @param cardId
	 * @return
	 */
	public ContactListItemEntity getEntityForListItem(long cardId) {
		ContactListItemEntity item = null;
		String where = DatabaseConstant.ContactColumns.CARD_ID + " = " + cardId;
		Cursor contactCursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, CONTACT_PROJECTION_LIST, where, null, null, null, null);
		if (Helper.isNotNull(contactCursor)) {
			if (contactCursor.moveToFirst()) {
				item = getEntityForListItem(contactCursor);
			}
			contactCursor.close();
			contactCursor = null;
		}
		return item;
	}
	
	/**
	 * 根据键值id 获取 联系人详细
	 * 
	 * @param keyId
	 * @return ContactEntity
	 */
	public ContactEntity getEntity(int keyId) {
		ContactEntity item = new ContactEntity();

		String where = DatabaseConstant._ID + " = " + keyId;
		Cursor contactCursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, CONTACT_PROJECTION_DETAILS, where, null, null, null, null);
		if (Helper.isNotNull(contactCursor) && contactCursor.getCount() > 0) {
			if (contactCursor.moveToFirst())
				item = getEntity(contactCursor);
		}
		contactCursor.close();
		contactCursor = null;
		return item;
	}

	/**
	 * 根据名片id获取联系人详细信息
	 * 
	 * @param cardId
	 * @return ContactEntity
	 */
	public ContactEntity getEntity(long cardId) {
		ContactEntity item = null;
		if(cardId == 0){
			return item;
		}
		String where = DatabaseConstant.ContactColumns.CARD_ID + "=" + cardId;
		Cursor contactCursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, CONTACT_PROJECTION_DETAILS, where, null, null, null, null);

		if (Helper.isNotNull(contactCursor)) {
			if (contactCursor.moveToFirst()) {
				item = getEntity(contactCursor);
			}
			contactCursor.close();
			contactCursor = null;
		}
		return item;
	}

	/**
	 * 根据 contactId(服务端id) 获取联系人对象
	 * 
	 * @param contactId
	 * @return
	 */
	public ContactEntity getEntityByContactId(long contactId) {
		if (contactId < 1) {
			return null;
		}
		ContactEntity item = new ContactEntity();

		String where = DatabaseConstant.ContactColumns.CONTACT_ID + "=" + contactId;
		Cursor contactCursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, CONTACT_PROJECTION_DETAILS, where, null, null, null, null);
		if (Helper.isNotNull(contactCursor)) {
			if (contactCursor.moveToFirst()) {
				item = getEntity(contactCursor);
			}
			contactCursor.close();
			contactCursor = null;
		}
		return item;
	}

	/**
	 * 根据名片id获取联系人的键值id
	 * 
	 * @param cardId
	 * @return ContactEntity
	 */
	public int getKeyIdByCard(long cardId) {
		int keyId = 0;

		String where = DatabaseConstant.ContactColumns.CARD_ID + "=" + cardId;
		Cursor contactCursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, CONTACT_PROJECTION_DETAILS, where, null, null, null, null);
		if (Helper.isNotNull(contactCursor) && contactCursor.getCount() > 0) {
			if (contactCursor.moveToFirst()) {
				keyId = contactCursor.getInt(contactCursor.getColumnIndex(DatabaseConstant._ID));
			}
		}
		contactCursor.close();
		contactCursor = null;
		return keyId;
	}
	/**
	 * 新增联系人, 返回新增的键值
	 * 
	 * @param contact
	 * @return void
	 */
	public int add(ContactEntity contact) {
		if (Helper.isNull(contact)) {
			return 0;
		}
		if (Helper.isEmpty(contact.getDisplayName())) {
			contact.setDisplayName(contact.getFullName());
		}
		contact.setOrderCode(ConverChineseCharToEnHelper.converToPingYingHeadUppercase(contact.getDisplayName()));
		int curKeyId = isExist(contact);
		if (curKeyId > 0) {
			contact.setId(curKeyId);
			updateContact(contact, false, false);
		} else {
			curKeyId = insertContact(contact);
		}
		
		//TODO 关联通讯录设置
//		if(SettingData.getSettingEntity().isCardInfo2AddressList()){
//			ContactBookDao.getInstance().add(contact);
//		}
		return curKeyId;

	}
	/**
	 * 批量添加联系人
	 * @param contactList
	 */
	public void add(ArrayList<ContactEntity> contactList){
		if(Helper.isNotNull(contactList))
			for (int i = 0, size = contactList.size(); i < size; i++) {
				add(contactList.get(i));
			}
	}
	/**
	 * 移动指定的联系人
	 */
	public boolean moveContact(ArrayList<ContactListItemEntity> contactList, ContactGroupEntity toGroup) {
		if (Helper.isNull(contactList)) {
			return false;
		}
		StringBuilder keyIds = new StringBuilder();
		for (int i = 0, size = contactList.size(); i < size; i++) {
			keyIds.append(contactList.get(i).getId() + ",");
		}
		return moveContact(keyIds.substring(0, keyIds.length()-1), toGroup.getId(), toGroup.getName());
	}

	/**
	 * 移动整个分组下的联系人
	 * 
	 * @param fromGroupId
	 * @param toGroup
	 */
	public boolean moveContact(int fromGroupId, ContactGroupEntity toGroup) {
		if (Helper.isNull(toGroup)) {
			return false;
		}

		String where = DatabaseConstant.ContactColumns.GROUP_ID + " = " + toGroup.getId();

		ContentValues values = getMoveContentValues(toGroup.getId(), toGroup.getName());
		int upRow = this.getSQLiteDatabase().update(TABLE_CONTACTS, values, where, null);
		return upRow > 0 ? true : false;
	}

	/**
	 * 更改分组 或 备注名称
	 * 
	 * @param keyID
	 *            更改的键值id
	 * @param remark
	 *            联系人备注
	 */
	public boolean updateRemark(int keyID, String remark) {
		if (Helper.isEmpty(remark)) {
			return false;
		}
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.ContactColumns.REMARK, remark);
		
		return updateContact(values, keyID);
	}
	/**
	 * 更改服务端过来的键值id
	 * @param keyId
	 * @param contactPersonId
	 */
	public void updateContactPersonId(int keyId, long contactPersonId){
		if(keyId > 0 && contactPersonId > 0){
			ContentValues values = new ContentValues();
			values.put(DatabaseConstant.ContactColumns.CONTACT_ID, contactPersonId);
			updateContact(values, keyId);
		}
	}

	/**
	 * 更改 名片备注
	 * 
	 * @param remark
	 * @param keyID
	 */
	public boolean updateCardRemark(String remark, int keyID) {
		if (Helper.isEmpty(remark)) {
			return false;
		}
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.ContactColumns.CARD_REMARK, remark);
		return updateContact(values, keyID);
	}
	/**
	 * 更改名片图片路径
	 * @param keyId
	 * @param imgA
	 * @param imgB
	 * @return
	 */
	public boolean updateCardImg(int keyId, String imgA, String imgB){

		ContentValues values = new ContentValues();
		if(Helper.isNotEmpty(imgA)){
			values.put(DatabaseConstant.ContactColumns.CARD_IMG_A, imgA);
		}
		if(Helper.isNotEmpty(imgB)){
			values.put(DatabaseConstant.ContactColumns.CARD_IMG_B, imgB);
		}
		return updateContact(values, keyId);
	}
	/**
	 * 删除联系人 
	 * @return boolean
	 */
	public boolean deleteContact(ArrayList<ContactListItemEntity> contactList) {

		if (Helper.isNull(contactList)) {
			return false;
		}
		StringBuilder keyIds = new StringBuilder();
		for (int i = 0, size = contactList.size(); i < size; i++) {
			keyIds.append(contactList.get(i).getId() + ",");
		}
		String delSql = " delete from " + TABLE_CONTACTS + " where " + DatabaseConstant._ID + " in (" + keyIds.substring(0, keyIds.length()-1) + ")";
		this.getSQLiteDatabase().execSQL(delSql);
		return true;
	}

	/**
	 * 删除单张名片
	 * 
	 * @param keyId
	 * @return boolean
	 */
	public boolean deleteContact(int keyId) {
		if (keyId < 1) {
			return false;
		}
		String delSql = " delete from " + TABLE_CONTACTS + " where " + DatabaseConstant._ID + " = " + keyId;
		this.getSQLiteDatabase().execSQL(delSql);
		return true;
	}
	/**
	 * 删除vcardNo的所有名片
	 * @param vcardNo
	 * @return
	 */
	public boolean deleteContact(String vcardNo){
		if(Helper.isEmpty(vcardNo)){
			return false;
		}
		String delSql = " delete from " + TABLE_CONTACTS + " where " + DatabaseConstant.ContactColumns.VCARD_NO + " = " + vcardNo;
		this.getSQLiteDatabase().execSQL(delSql);
		return true;
	}

	/**
	 * 根据服务端返回的列表 更新本地数据
	 * @param contactList
	 * @return
	 */
	public void deleteContactFromService(ArrayList<ContactEntity> contactList){
		if(Helper.isNotNull(contactList)){
			String cardIds = "";
			String contactIds = "";
			for (int i = 0, size = contactList.size(); i < size; i++) {
				ContactEntity contact = contactList.get(i);
				if(contact.getCardId() > 0){
					cardIds += contact.getCardId() + ",";
				}else{
					contactIds += contact.getContactId() + ",";
				}
			}
			String where = DatabaseConstant.ContactColumns.CONTACT_ID.concat("= 0");
			if(Helper.isNotEmpty(cardIds)){
				where = where.concat(" or ").concat(DatabaseConstant.ContactColumns.CARD_ID).concat(" in (").concat(cardIds.substring(0, cardIds.length() - 1)).concat(")");
			}
			if(Helper.isNotEmpty(contactIds)){
				where = where.concat(" or ").concat(DatabaseConstant.ContactColumns.CONTACT_ID).concat(" in (").concat(contactIds.substring(0, contactIds.length() - 1)).concat(")");
			}
			
			String delSql = " delete from ".concat(DatabaseConstant.DBTableName.TABLE_CONTACTS).concat(" where ")
					.concat(DatabaseConstant._ID).concat(" not in (")
					.concat(" select ").concat(DatabaseConstant._ID).concat(" from ")
					.concat(DatabaseConstant.DBTableName.TABLE_CONTACTS).concat(" where ").concat(DatabaseConstant._ID).concat("=1")
					.concat(" or ").concat(where).concat(")");
			this.getSQLiteDatabase().execSQL(delSql);
		}
	}

	/**
	 * 删除所有联系人(小秘书除外)
	 */
	public void deleteAllContact(){
		String delSql = " delete from " + TABLE_CONTACTS + " where " + DatabaseConstant._ID + " > 1 ";
		this.getSQLiteDatabase().execSQL(delSql);
	}

	/**
	 * 获取交换的联系人数
	 * 
	 * @return
	 */
	public int getContactOnlineTotal() {
		int onlineTotal = 0;
		String selSql = " select count(*) from " + TABLE_CONTACTS + " where " + DatabaseConstant.ContactColumns.CARD_ID + ">0 ";
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		if (Helper.isNotNull(cursor)) {
			if (cursor.moveToFirst()) {
				onlineTotal = cursor.getInt(0);
			}
			cursor.close();
			cursor = null;
		}
		return onlineTotal;
	}
	/**
	 * 全部联系人数
	 * @return
	 */
	public int getContactTotal(){
		return mContactTotal;
	}

	/**
	 * 更改最近联系 次数 及时间
	 * 
	 * @param keyId
	 */
	public void updateContactTime(int keyId) {
		String upSql = " update " + TABLE_CONTACTS + " set " + DatabaseConstant.ContactColumns.LAST_CONTACTED_TIME + "='"
				+ ResourceHelper.getNowTime() + "'," + DatabaseConstant.ContactColumns.TIMES_CONTACTED + "= ("
				+ DatabaseConstant.ContactColumns.TIMES_CONTACTED + " + 1)";
		String where = " where " + DatabaseConstant._ID + "=" + keyId;

		this.getSQLiteDatabase().execSQL(upSql + where);
	}

	/**
	 * 获取名片id
	 */
	public long getCardId(int keyId) {
		Cursor cursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, new String[] { "card_id" }, "_id = ?", new String[] { String.valueOf(keyId) }, null,
				null, null);
		long card_id = 0;
		if (Helper.isNotNull(cursor)) {
			cursor.moveToNext();
			card_id = cursor.getLong(cursor.getColumnIndex("card_id"));
		}
		return card_id;
	}

	/**
	 * 通过电话号码获取绑定方式
	 * 
	 * @param mobile
	 * @return
	 */
	public int getBindwayByMobile(String mobile) {
		Cursor cursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, new String[] { "bind_way" }, "mobile = ?", new String[] { mobile }, null, null, null);
		int bindway = 0;
		if (Helper.isNotNull(cursor) && cursor.moveToNext()) {
			bindway = cursor.getInt(cursor.getColumnIndex("bind_way"));
		}
		return bindway;
	}

	/**
	 * 根据 名片id 获取 联系人所在分组 的 id
	 * 
	 * @param cardId
	 *            名片id
	 * @return int 分组id
	 */
	public Integer getGroupByCardId(long cardId) {
		Integer groupId = null;
		String selSql = " select group_id from " + TABLE_CONTACTS + " where card_id = " + cardId;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		if (Helper.isNotNull(cursor)) {
			if (cursor.moveToFirst()) {
				groupId = cursor.getInt(0);
			}
			cursor.close();
			cursor = null;
		}
		return groupId;
	}
	/**
	 * 获取账户显示的名称 
	 * @param accountId
	 * @param nameType 0-姓名（默认） 1-AccountName, 2-VcardNo
	 * @return
	 */
	public String getShowName(long accountId, int nameType){
		String showName = null;
		String selSql = new StringBuilder().append(" select ")
				.append(DatabaseConstant.ContactColumns.DISPLAY_NAME).append(",")
				.append(DatabaseConstant.ContactColumns.ACCOUNT_NAME).append(",")
				.append(DatabaseConstant.ContactColumns.VCARD_NO)
				.append(" from ")
				.append(TABLE_CONTACTS)
				.append(" where ").append(DatabaseConstant.ContactColumns.ACCOUNT_ID).append(" = ").append(accountId)
				.toString();
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		if (Helper.isNotNull(cursor)) {
			if (cursor.moveToFirst()) {
				if(nameType == Constants.ContactShowNameType.DISPLAY_NAME){
					showName = cursor.getString(cursor.getColumnIndex(DatabaseConstant.ContactColumns.DISPLAY_NAME));
				}else if(nameType == Constants.ContactShowNameType.ACCOUNT_NAME){
					showName = cursor.getString(cursor.getColumnIndex(DatabaseConstant.ContactColumns.ACCOUNT_NAME));
				}else if(nameType == Constants.ContactShowNameType.VCARD_NO){
					showName = cursor.getString(cursor.getColumnIndex(DatabaseConstant.ContactColumns.VCARD_NO));
				}
			}
			cursor.close();
			cursor = null;
		}
		if(Helper.isEmpty(showName)){
			showName = mContext.getString(R.string.stranger);
		}
		return showName;
	}
	/**
	 * 删除分组下的联系人
	 * 
	 * @param groupId
	 * @return boolean
	 */
	public boolean deleteContactByGroup(int groupId) {
		if (groupId < 0) {
			return false;
		}
		String delSql = " delete from " + TABLE_CONTACTS + " where " + DatabaseConstant.ContactColumns.GROUP_ID + " = " + groupId;
		this.getSQLiteDatabase().execSQL(delSql);
		return true;
	}

	/**
	 * 更改替换联系人表数据
	 * 
	 * @return void
	 */
	public void recoverContacts(ArrayList<ContactVCardBackupJsonEntity> contactList) {
		if (Helper.isEmpty(contactList)) {
			return;
		}
		ContactEntity recoverContact = null;

		for (int i = 0, size = contactList.size(); i < size; i++) {
			recoverContact = contactList.get(i).getContactEntity();
			recoverContact(recoverContact);
		}
	}
	/**
	 * 导入数据覆盖
	 * @param contactList
	 */
	public void replaceContacts(ArrayList<ContactEntity> contactList){
		if (Helper.isEmpty(contactList)) {
			return;
		}
		ContactEntity recoverContact = null;
		for (int i = 0, size = contactList.size(); i < size; i++) {
			recoverContact = contactList.get(i);
			recoverContact(recoverContact);
		}
	}
	/**
	 * 获取该账户下的所有电话号码
	 * @param accountId
	 * @return
	 */
	public ArrayList<String> getPhoneList(long accountId){
		if(accountId == 0){
			return null;
		}
		String where = DatabaseConstant.ContactColumns.ACCOUNT_ID + "=" + accountId;
		ArrayList<String> phoneList = getPhoneList(where);
		return phoneList;
	}
	/**
	 * 根据键值id 获取电话列表
	 * @param keyId
	 * @return
	 */
	public ArrayList<String> getPhoneList(int keyId){
		String where = DatabaseConstant._ID + "=" + keyId;
		ArrayList<String> phoneList = getPhoneList(where);
		return phoneList;
	}
	/**
	 * 根据名片id获取电话列表
	 * @param cardId
	 * @return
	 */
	public ArrayList<String> getPhoneListByCard(long cardId){
		if(cardId == 0){
			return null;
		}
		String where = DatabaseConstant.ContactColumns.CARD_ID + "=" + cardId;
		ArrayList<String> phoneList = getPhoneList(where);
		return phoneList;
	}
	/**
	 * 获取账户下的所有手机
	 * @param accountId
	 * @return
	 */
	public ArrayList<String> getMobileList(long accountId){
		ArrayList<String> phoneList = null;
		String where = DatabaseConstant.ContactColumns.ACCOUNT_ID.concat("=" + accountId);
		
		Cursor cursor = this.getSQLiteDatabase().query(TABLE_CONTACTS, new String[]{DatabaseConstant.ContactColumns.MOBILE}
				, where, null, null, null, DatabaseConstant.ContactColumns.CREATE_TIME + " desc");
		if(Helper.isNotNull(cursor)){
			phoneList = new ArrayList<String>();
			while (cursor.moveToNext()) {
				String mobile = cursor.getString(cursor.getColumnIndex(DatabaseConstant.ContactColumns.MOBILE));
				if(!phoneList.contains(mobile)){
					phoneList.add(mobile);
				}
			}
			cursor.close();
			cursor = null;
		}
		return phoneList;
	}
	/**
	 * 加入黑名单
	 * @param contactList
	 */
	public void joinBlacklist(ArrayList<ContactListItemEntity> contactList){
		if(Helper.isNull(contactList)){
			return;
		}
		StringBuilder idCombin = new StringBuilder();
		for (int i = 0, size = contactList.size(); i < size; i++) {
			int keyId = contactList.get(i).getId();
			idCombin.append(keyId +",");
		}
		if(Helper.isNotEmpty(idCombin)){
			joinBlacklist(idCombin.substring(0, idCombin.length()-1));
		}
	}
	/**
	 * 加入黑名单
	 * @param contactKeyIds 键值id集合","相隔
	 */
	public void joinBlacklist(String contactKeyIds){
		if(Helper.isNotEmpty(contactKeyIds)){
			String where = DatabaseConstant._ID + " in (" + contactKeyIds + ")";
			ContentValues updateValues = new ContentValues();
			updateValues.put(DatabaseConstant.ContactColumns.GROUP_ID, ContactGroupEntity.GROUP_BLACKLIST_ID);
			updateValues.put(DatabaseConstant.ContactColumns.GROUP_NAME, mContext.getString(R.string.blacklist));
			
			this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_CONTACTS, updateValues, where, null);
		}
	}
	/**
	 * 获取分组下的联系人数
	 * @param groupId
	 */
	public int getContactCount(int groupId){
		if(Helper.isNull(mCountMap)){
			getContactCount();
		}
		return mCountMap.get(groupId);
	}
	/**
	 * 清空联系人表,但保留默认联系人
	 * 
	 * @return
	 */
	public void truncateContact() {
		String delSql = "delete from " + TABLE_CONTACTS + " where _id <> 1";
		String upSql = " UPDATE sqlite_sequence set seq=1 where name='" + TABLE_CONTACTS + "';";
		this.getSQLiteDatabase().execSQL(delSql);
		this.getSQLiteDatabase().execSQL(upSql);
	}

	// #endregion
	
	private SQLiteDatabase getSQLiteDatabase(){
		return VCardSQLiteDatabase.getInstance().getSQLiteDatabase();
	}
}
