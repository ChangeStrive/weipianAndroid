package com.maya.android.vcard.dao;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.util.SparseArray;

import com.maya.android.asyncimageview.manager.AsyncImageManager;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.ContactBookEntity;
import com.maya.android.vcard.entity.ContactEntity;
import com.maya.android.vcard.entity.ContactGroupBackupEntity;
import com.maya.android.vcard.entity.ContactGroupEntity;
import com.maya.android.vcard.entity.InstantMessengerEntity;
import com.maya.android.vcard.entity.SMSSendEntity;
import com.maya.android.vcard.entity.json.ContactBookBackupJsonEntity;
import com.maya.android.vcard.entity.result.VCardUserFromMobileResultEntity;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DAO: 本机通讯录 分组 及 联系人操作
 * 
 * @author zheng_cz
 * @since 2014年3月24日 下午5:33:25
 */
public class ContactBookDao {

	private static final String TAG = ContactBookDao.class.getSimpleName();
	private static final String UNGROUP = "未分组";
	private static ContactBookDao sInstance;

	private Context mContext = ActivityHelper.getGlobalApplicationContext();
	private ContentResolver mContentResolver;
	// #region 分组变量
	private static ArrayList<ContactGroupEntity> sLocalGroupListForShow;

	private static final String SELECTION_UNDEL = Groups.DELETED + "=0";
	// 本机通讯录分组字段
	private static final String[] GROUP_PROJECTION = new String[] { Groups._ID, Groups.TITLE, Groups.DELETED };

	// #endregion

	// #region 联系人变量
	private static final String COL_SORT_KEY = "sort_key";
	private static final String[] CONTACT_PROJECTION = new String[] { Contacts._ID, Contacts.DISPLAY_NAME,
			Contacts.HAS_PHONE_NUMBER, Contacts.PHOTO_ID, COL_SORT_KEY };
	/**<组id>**/
	private HashMap<Integer, ArrayList<ContactBookEntity>> mContactListOfBook;
	/** 全部联系人数 **/
	private int mContactTotalOfBook = -1;
	// #endregion
	public static ContactBookDao getInstance(){
		if(Helper.isNull(sInstance)){
			sInstance = new ContactBookDao();
		}
		return sInstance;
	}
	private ContactBookDao() {
		mContentResolver = mContext.getContentResolver();
	}

	// #region 通讯录分组
	/**
	 * 添加分组
	 * 
	 * @param groupName
	 * @return void
	 */
	private long insertGroup(String groupName) throws Exception {

		long newId = 0;
		ContentValues values = new ContentValues();
		values.put(Groups.TITLE, groupName);
		Uri insertUri = mContext.getContentResolver().insert(Groups.CONTENT_URI, values);
		newId = ContentUris.parseId(insertUri);
		return newId;
	}

	/**
	 * 获取分组对象
	 * 
	 * @param cursor
	 * @return
	 */
	private ContactGroupEntity getGroupEntity(Cursor cursor) {
		ContactGroupEntity group = null;
		if (Helper.isNotNull(cursor)) {
			group = new ContactGroupEntity();
			String grpName = cursor.getString(cursor.getColumnIndex(Groups.TITLE));
			int grpId = cursor.getInt(cursor.getColumnIndex(Groups._ID));
			boolean delete = ResourceHelper.int2Boolean(cursor.getInt(cursor.getColumnIndex(Groups.DELETED)));
			if (grpName.contains(":")) {
				String[] arrName = grpName.split(":");
				if (Helper.isNotEmpty(arrName)) {
					grpName = arrName[arrName.length - 1];
				}
			}
			group.setName(grpName);
			group.setId(grpId);
			group.setDelete(delete);
		}
		return group;
	}
	/**
	 * 获取通讯录的分组
	 * 
	 * @return List<ContactGroupEntity>
	 * @throws Exception
	 */
	private ArrayList<ContactGroupEntity> getGroupList() {
		ArrayList<ContactGroupEntity> groupList = new ArrayList<ContactGroupEntity>();
		try {
			Cursor cursor = mContext.getContentResolver().query(Groups.CONTENT_URI, GROUP_PROJECTION, SELECTION_UNDEL, null, null);
			ContactGroupEntity group = null;
			while (cursor.moveToNext()) {
				group = getGroupEntity(cursor);
				groupList.add(group);
			}
			cursor.close();
			cursor = null;
		} catch (Exception e) {
			Log.e(TAG, "获取通讯录分组异常", e);
		}
		return groupList;
	}
	/**
	 * 获取分组下的统计联系人
	 * @return
	 */
	private HashMap<Long, Integer> getGroupCountMap(){
		HashMap<Long, Integer> groupCountMap = new HashMap<Long, Integer>();
		
		//获取已分组联系人
//		String where = Data.CONTACT_ID.concat(" in (select ").concat(Data.CONTACT_ID).concat(" from view_data where ")
//				.concat(Data.MIMETYPE).concat("='").concat(GroupMembership.CONTENT_ITEM_TYPE)
//				.concat("' group by ").concat(Data.CONTACT_ID).concat(")").concat(" AND ").concat(Data.MIMETYPE).concat("='").concat(GroupMembership.CONTENT_ITEM_TYPE).concat("'");

		String where = (Data.MIMETYPE).concat("='").concat(GroupMembership.CONTENT_ITEM_TYPE).concat("'");
		Cursor cursor = this.mContentResolver.query(Data.CONTENT_URI, new String[]{GroupMembership.GROUP_ROW_ID, Data.CONTACT_ID}, where, null, null);
		
		if(Helper.isNotNull(cursor)){
			while(cursor.moveToNext()){
				long groupId = cursor.getLong(0);
				if(groupCountMap.containsKey(groupId)){
					int count = groupCountMap.get(groupId) + 1;
					groupCountMap.put(groupId, count);
				}else{
					groupCountMap.put(groupId, 1);
				}
			}
			cursor.close();
			cursor = null;
		}
		
		// 获取未分组的联系人数
		String whereUnGroup = " _id not in ( select contact_id from view_data where " + Data.MIMETYPE + "='" + GroupMembership.CONTENT_ITEM_TYPE + "' ) ";
		Cursor unGrpCursor = this.mContentResolver.query(Contacts.CONTENT_URI, new String[]{Contacts._ID}, whereUnGroup, null, null);
		if(Helper.isNotNull(unGrpCursor)){
			int unGroupCount = unGrpCursor.getCount();
			groupCountMap.put(Long.valueOf(ContactGroupEntity.GROUP_UNGROUPED_ID), unGroupCount);
			unGrpCursor.close();
			unGrpCursor = null;
		}
		
		//获取收藏夹联系人数
		Cursor cursorStarred = this.mContentResolver.query(Contacts.CONTENT_URI, new String[]{Contacts._ID}
			, Contacts.STARRED + "=1 ", null, null);
		if(Helper.isNotNull(cursorStarred)){
			int countStarred = cursorStarred.getCount();
			groupCountMap.put(Long.valueOf(ContactGroupEntity.GROUP_FAVORITE_ID), countStarred);
			cursorStarred.close();
			cursorStarred = null;
		}
		
		// 全部联系人
		groupCountMap.put(Long.valueOf(ContactGroupEntity.GROUP_ALL_ID), getContactTotal());
		
		//最近联系人
		Cursor cursorRecent = this.mContentResolver.query(Contacts.CONTENT_URI, CONTACT_PROJECTION, Contacts.TIMES_CONTACTED + ">0", null,
				Contacts.LAST_TIME_CONTACTED + " desc limit 0,20 ");
		if(Helper.isNotNull(cursorRecent)){
			int countRecent = cursorRecent.getCount();
			groupCountMap.put(Long.valueOf(ContactGroupEntity.GROUP_RECENTCONTACT_ID), countRecent);
			cursorRecent.close();
			cursorRecent = null;
		}
		
		return groupCountMap;
	}
	/**
	 * 获取联系人总数
	 * @return
	 */
	private int getContactTotal(){
		Cursor cursor = this.mContentResolver.query(Contacts.CONTENT_URI, new String[]{Contacts._ID}, null, null, null);
		if(Helper.isNotNull(cursor)){
			mContactTotalOfBook = cursor.getCount();
			cursor.close();
			cursor = null;
		}
		return mContactTotalOfBook;
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
	 * 获取含统计的通讯录分组
	 * 
	 * @return ArrayList<ContactGroupEntity>
	 */
	public ArrayList<ContactGroupEntity> getGroupListForShow(boolean isRefresh) {
		if (Helper.isNull(sLocalGroupListForShow) || isRefresh) {
			sLocalGroupListForShow = new ArrayList<ContactGroupEntity>();
			HashMap<Long, Integer> countMap = getGroupCountMap();
			sLocalGroupListForShow = getGroupListForAll();
			for (int i = 0, len = sLocalGroupListForShow.size(); i < len; i++) {
				int grpId = sLocalGroupListForShow.get(i).getId();
				Integer count = countMap.get(Long.valueOf(grpId));
				if (Helper.isNull(count)) {
					sLocalGroupListForShow.get(i).setItemCount(0);
				} else {
					sLocalGroupListForShow.get(i).setItemCount(count);
				}
			}
			// 清空联系人集合 重新获取
			if(Helper.isNotNull(mContactListOfBook)){
				mContactListOfBook.clear();
			}
		}
		return sLocalGroupListForShow;
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
			grpList.remove(size - 1);
			grpList.remove(0);
		}
		return grpList;
	}
	/**
	 * 获取移动分组弹出框
	 * @return
	 */
	public ArrayList<ContactGroupEntity> getGroupListForDialog() {
		ArrayList<ContactGroupEntity> grpList = new ArrayList<ContactGroupEntity>(getGroupListForShow(false));
		if (Helper.isNotNull(grpList)) {
			// 去掉 最近联系人
			grpList.remove(0);
		}
		return grpList;
	}
	/**
	 * 根据分组名称获取组id
	 * 
	 * @param groupName
	 * @return
	 */
	public long getGroupId(String groupName) {
		if (ResourceHelper.isEmpty(groupName) || groupName.equals(UNGROUP)) {
			return 0;
		}
		long groupId = 0;
		String selection = "rtrim(" + Groups.TITLE + ")='" + groupName.trim() + "'";
		Cursor grpCursor = mContext.getContentResolver().query(Groups.CONTENT_URI, new String[] { Groups._ID }, selection, null, Groups.DELETED);
		// 如果存在
		if (Helper.isNotNull(grpCursor)) {
			if (grpCursor.moveToFirst()) {
				groupId = grpCursor.getLong(grpCursor.getColumnIndex(Groups._ID));
			}
			grpCursor.close();
			grpCursor = null;
		}
		return groupId;
	}

	/**
	 * 根据分组id ，获取分组名称
	 * 
	 * @param groupId
	 * @return
	 */
	public String getGroupName(int groupId) {
		String groupName = null;
		// 获取联系人的分组名称
		Cursor groupCursor = mContentResolver.query(Groups.CONTENT_URI, new String[] { Groups.TITLE }, Groups._ID + "=?",
				new String[] { String.valueOf(groupId) }, null);
		while (Helper.isNotNull(groupCursor) && groupCursor.moveToNext()) {
			groupName = groupCursor.getString(groupCursor.getColumnIndex(Groups.TITLE));
		}
		groupCursor.close();
		return groupName;
	}

	/**
	 * 根据分组id 获取分组对象
	 * 
	 * @param groupId
	 * @return
	 */
	public ContactGroupEntity getGroupEntity(int groupId) {
		ContactGroupEntity group = new ContactGroupEntity();
		String selection = Groups._ID + "=" + groupId;
		Cursor grpCursor = mContext.getContentResolver().query(Groups.CONTENT_URI, GROUP_PROJECTION, selection, null, null);
		// 如果存在
		if (Helper.isNotNull(grpCursor)) {
			if (grpCursor.moveToFirst()) {
				group = getGroupEntity(grpCursor);
			}
			grpCursor.close();
			grpCursor = null;
		}
		return group;
	}

	/**
	 * 更改分组名称
	 * 
	 * @param newName
	 *            新分组名称
	 * @param oldName
	 *            旧分组名称
	 * @return
	 */
	public boolean updateGroup(String newName, String oldName) {
		String where = SELECTION_UNDEL + " and " + Groups.TITLE + "='" + oldName.trim() + "'";
		ContentValues values = new ContentValues();
		values.put(Groups.TITLE, newName);
		int upRow = mContentResolver.update(Groups.CONTENT_URI, values, where, null);
		return upRow > 0 ? true : false;
	}

	/**
	 * 添加通讯录分组
	 * 
	 * @param groupName
	 * @return
	 */
	public long addGroup(String groupName) {

		if (ResourceHelper.isEmpty(groupName) || groupName.equals(UNGROUP)) {
			return 0;
		}
		long newId = 0;
		try {
			newId = getGroupId(groupName);
			if (newId > 0) {
				ContentValues values = new ContentValues();
				values.put(Groups.DELETED, 0);
				mContentResolver.update(Groups.CONTENT_URI, values, Groups._ID + "=" + newId, null);
			} else {
				newId = insertGroup(groupName);
			}
		} catch (Exception ex) {
			Log.e(TAG, "添加通讯录分组异常", ex);
		}
		return newId;
	}
	/**
	 * 添加默认分组到通讯录
	 */
	public void addGroupDefault(){
		ArrayList<String> defaultGroups = ResourceHelper.getListFromResArray(mContext, R.array.default_group);
		if(Helper.isNotNull(defaultGroups)){
			for (int i = 0, size = defaultGroups.size(); i < size; i++) {
				addGroup(defaultGroups.get(i));
			}
		}
	}
	/**
	 * 删除分组
	 * 
	 * @param isDelPerson
	 *            是否删除分组下的联系人
	 * @return void
	 */
	public boolean deleteGroup(String groupName, boolean isDelPerson) {
		if (Helper.isEmpty(groupName)) {
			return false;
		}
		int delRow = 0;
		try {
			Uri deluri = Groups.CONTENT_URI;
			if (isDelPerson) {
				deluri = Uri.parse(Groups.CONTENT_URI + "?" + ContactsContract.CALLER_IS_SYNCADAPTER + "=true");
			}
			String where = Groups.TITLE + "='" + groupName.trim() + "'";
			delRow = mContext.getContentResolver().delete(deluri, where, null);
		} catch (Exception ex) {
			Log.e(TAG, "删除通讯录分组失败", ex);
		}
		return delRow > 0 ? true : false;
	}

	/**
	 * 撤销分组的删除标志
	 * 
	 * @param groupName
	 * @return boolean
	 */
	public boolean undeleteGroup(String groupName) {
		String where = Groups.DELETED + "=1 and " + Groups.TITLE + "='" + groupName + "'";
		ContentValues values = new ContentValues();
		values.put(Groups.DELETED, 0);
		try {
			int upRow = mContext.getContentResolver().update(Groups.CONTENT_URI, values, where, null);

			return upRow > 0 ? true : false;
		} catch (Exception ex) {
			Log.e(TAG, "撤销通讯录分组删除标志异常", ex);
			return false;
		}
	}

	/**
	 * 恢复通讯录分组, 分组存在则改状态,否则新增加分组
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

				addGroup(recoverGrp.getName());
			}
		} catch (Exception ex) {
			Log.e(TAG, "恢复通讯录分组异常", ex);
		}
	}

	/**
	 * 获取分组id
	 * 
	 * @param contactRawId
	 * @return
	 */
	public int getGroupId(long contactRawId) {
		int groupId = 0;
		Cursor groupIdCursor = mContentResolver.query(Data.CONTENT_URI, new String[] { Data.DATA1 }, Data.MIMETYPE + "=? AND " + Data.RAW_CONTACT_ID
				+ "=?", new String[] { GroupMembership.CONTENT_ITEM_TYPE, String.valueOf(contactRawId) }, null);
		if (Helper.isNotNull(groupIdCursor)) {
			if (groupIdCursor.moveToFirst()) {
				groupId = groupIdCursor.getInt(groupIdCursor.getColumnIndex(Data.DATA1));
			}
			groupIdCursor.close();
		}
		return groupId;
	}

	// #endregion 通讯录分组

	// #region 通讯录联系人

	/**
	 * 获取分组下的联系人
	 * 
	 * @param groupId
	 * @throws Exception
	 * @return List<ContactEntity>
	 */
	private ArrayList<ContactBookEntity> getListForGroup(int groupId) throws Exception {

		ArrayList<ContactBookEntity> contList = new ArrayList<ContactBookEntity>();

		String where = " _id in ( select contact_id from view_data where " + Data.MIMETYPE + "='" + GroupMembership.CONTENT_ITEM_TYPE + "' and "
				+ GroupMembership.GROUP_ROW_ID + "=" + groupId + " ) ";
		// 分组统计
		Cursor grpMemCursor = mContentResolver.query(Contacts.CONTENT_URI, CONTACT_PROJECTION, where, null, COL_SORT_KEY);
		while (grpMemCursor.moveToNext()) {
			ContactBookEntity cont = setContactBookEntity(grpMemCursor);
			contList.add(cont);
		}
		grpMemCursor.close();
		grpMemCursor = null;
		return contList;
	}

	/**
	 * 获取收藏夹的联系人
	 * 
	 * @throws Exception
	 * @return List<ContactEntity>
	 */
	private ArrayList<ContactBookEntity> getListForFavorite() throws Exception {
		ArrayList<ContactBookEntity> contList = new ArrayList<ContactBookEntity>();
		Cursor grpCur = mContentResolver.query(Contacts.CONTENT_URI, CONTACT_PROJECTION, Contacts.STARRED + "<>0",
				null, COL_SORT_KEY);
		while (grpCur.moveToNext()) {
			ContactBookEntity contact = setContactBookEntity(grpCur);
			contList.add(contact);
		}
		grpCur.close();
		grpCur = null;
		return contList;
	}

	/**
	 * 把数据库值赋值给对象
	 * 
	 * @param cursor
	 * @return ContactEntity
	 */
	private ContactBookEntity setContactBookEntity(Cursor cursor) throws Exception {
		long contactId = cursor.getLong(cursor.getColumnIndex(Contacts._ID));
		String displayName = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
		int hasPhone = cursor.getInt(cursor.getColumnIndex(Contacts.HAS_PHONE_NUMBER));
		String sortKey = cursor.getString(cursor.getColumnIndex(COL_SORT_KEY));

		// 得到联系人头像Bitamp
		Bitmap headPhoto = null;
		// 得到联系人头像ID
		long photoid = cursor.getLong(cursor.getColumnIndexOrThrow(Contacts.PHOTO_ID));
		if (photoid > 0) {
			Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
			InputStream input = Contacts.openContactPhotoInputStream(mContentResolver, uri);
			headPhoto = BitmapFactory.decodeStream(input);
		}
		ContactBookEntity contact = new ContactBookEntity(contactId, hasPhone, displayName, sortKey, headPhoto);
		contact.setContactRawId(getContactRawId(contactId));
		return contact;
	}

	/**
	 * 获取所有的通讯录联系人
	 * 
	 * @throws Exception
	 * @return List<ContactEntity>
	 */
	private ArrayList<ContactBookEntity> getListForAll() throws Exception {
		ArrayList<ContactBookEntity> contList = new ArrayList<ContactBookEntity>();
		Cursor contactCur = mContentResolver.query(Contacts.CONTENT_URI, CONTACT_PROJECTION, null, null, COL_SORT_KEY);
		while (contactCur.moveToNext()) {
			ContactBookEntity contact = setContactBookEntity(contactCur);
			contList.add(contact);
		}
		// 获取手机联系人
		// Cursor phoneCursor =
		// mContentResolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null,
		// null, null);

		contactCur.close();
		contactCur = null;
		return contList;
	}

	/**
	 * 获取最近联系的20个人
	 * 
	 * @return SparseIntArray key=groupId,value=count
	 */
	private ArrayList<ContactBookEntity> getListForRecent() throws Exception {
		ArrayList<ContactBookEntity> reContList = new ArrayList<ContactBookEntity>();
		// 最近联系人
		Cursor reCursor = mContentResolver.query(Contacts.CONTENT_URI, CONTACT_PROJECTION, Contacts.TIMES_CONTACTED + ">0", null,
				Contacts.LAST_TIME_CONTACTED + " desc limit 0,20 ");
		while (reCursor.moveToNext()) {
			ContactBookEntity cont = setContactBookEntity(reCursor);
			reContList.add(cont);
		}
		reCursor.close();
		reCursor = null;
		return reContList;
	}

	/**
	 * 获取未分组得联系人
	 * 
	 * @return List<ContactEntity>
	 */
	private ArrayList<ContactBookEntity> getListForUnGroup() throws Exception {
		ArrayList<ContactBookEntity> contList = new ArrayList<ContactBookEntity>();

		String where = " _id not in ( select contact_id from view_data where " + Data.MIMETYPE + "='" + GroupMembership.CONTENT_ITEM_TYPE + "' ) ";

		Cursor unGrpCursor = mContentResolver.query(Contacts.CONTENT_URI, CONTACT_PROJECTION, where, null, COL_SORT_KEY);
		while (unGrpCursor.moveToNext()) {
			ContactBookEntity cont = setContactBookEntity(unGrpCursor);
			contList.add(cont);
		}
		unGrpCursor.close();
		unGrpCursor = null;
		return contList;
	}

	/**
	 * 获取未分组的contactId
	 * 
	 * @return ArrayList<Long>
	 */
	private ArrayList<Long> getContactIdListForUnGroup() throws Exception {
		ArrayList<Long> contactIdList = new ArrayList<Long>();

		String where = " _id not in ( select contact_id from view_data where " + Data.MIMETYPE + "='" + GroupMembership.CONTENT_ITEM_TYPE + "' ) ";

		Cursor unGrpCursor = mContentResolver.query(Contacts.CONTENT_URI, new String[] { "_id" }, where, null, COL_SORT_KEY);
		while (unGrpCursor.moveToNext()) {
			long contactId = unGrpCursor.getLong(unGrpCursor.getColumnIndex("_id"));
			contactIdList.add(contactId);
		}
		unGrpCursor.close();
		unGrpCursor = null;
		return contactIdList;
	}

	/**
	 * 根据分组id 获取列表
	 * 
	 * @param groupId
	 * @return
	 */
	private ArrayList<ContactBookEntity> getList(int groupId) {
		ArrayList<ContactBookEntity> contList = null;
		try {
			switch (groupId) {
			case ContactGroupEntity.GROUP_ALL_ID:
				contList = getListForAll();
				break;
			case ContactGroupEntity.GROUP_UNGROUPED_ID:
				contList = getListForUnGroup();
				break;

			case ContactGroupEntity.GROUP_RECENTCONTACT_ID:
				contList = getListForRecent();
				break;
			case ContactGroupEntity.GROUP_FAVORITE_ID:
				contList = getListForFavorite();
				break;
			case ContactGroupEntity.GROUP_BLACKLIST_ID:
				contList = new ArrayList<ContactBookEntity>();
				break;
			default:
				contList = getListForGroup(groupId);
				break;
			}
		} catch (Exception e) {
			Log.e(TAG, "分组获取联系人异常", e);
		}
		return contList;
	}

	/**
	 * 联系人数据插入到本机通讯录
	 * 
	 * @param contact
	 */
	private int insert(ContactEntity contact) {

		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = operationList.size();
		ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				 .withValue(RawContacts.ACCOUNT_TYPE,null)
				 .withValue(RawContacts.ACCOUNT_NAME,null);
		if (contact.getStarred() == 1) {
			// 将raw_contacts表里的starred标志位设置为1
			builder.withValue(RawContacts.STARRED, contact.getStarred());
		}
		operationList.add(builder.build());

		// 添加名字
		if (Helper.isNotEmpty(contact.getDisplayName())) {
			builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
			builder.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex);
			builder.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
			builder.withValue(StructuredName.DISPLAY_NAME, contact.getDisplayName());
			operationList.add(builder.build());
		}

		// 添加公司
		if (Helper.isNotEmpty(contact.getCompanyName())) {
			builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
			builder.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex);
			builder.withValue(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
			builder.withValue(Organization.TYPE, Organization.TYPE_WORK);
			builder.withValue(Organization.COMPANY, contact.getCompanyName());
			if (Helper.isNotEmpty(contact.getJob())) {
				builder.withValue(Organization.JOB_DESCRIPTION, contact.getJob());
			}
			operationList.add(builder.build());
		}

		// 添加公司地址
		if (Helper.isNotEmpty(contact.getAddress())) {
			builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
			builder.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex);
			builder.withValue(Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE);
			builder.withValue(StructuredPostal.TYPE, StructuredPostal.TYPE_WORK);
			builder.withValue(StructuredPostal.STREET, contact.getAddress());
			if (Helper.isNotEmpty(contact.getPostcode())) {
				builder.withValue(StructuredPostal.POSTCODE, contact.getPostcode());
			}
			operationList.add(builder.build());
		}

		// 添加公司英文名
		if (Helper.isNotEmpty(contact.getCompanyEnName())) {
			builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
			builder.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex);
			builder.withValue(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
			builder.withValue(Organization.TYPE, Organization.TYPE_OTHER);
			builder.withValue(Organization.COMPANY, contact.getCompanyEnName());
		}

		// 添加手机号
		if (Helper.isNotEmpty(contact.getMobile())) {
			String[] mobiles = contact.getMobile().split(",");
			for (int i = 0, length = mobiles.length; i < length; i++) {
				if (Helper.isNotEmpty(mobiles[i])) {
					builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
					builder.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex);
					builder.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
					builder.withValue(Phone.TYPE, Phone.TYPE_MOBILE);
					builder.withValue(Phone.NUMBER, mobiles[i]);
					builder.withValue(Phone.IS_PRIMARY, 1);
					operationList.add(builder.build());
				}
			}
		}
		// 添加固定电话
		if (Helper.isNotEmpty(contact.getTelephone())) {
			String[] telphones = contact.getTelephone().split(",");
			for (int i = 0, length = telphones.length; i < length; i++) {
				if (Helper.isNotEmpty(telphones[i])) {
					builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
					builder.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex);
					builder.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
					builder.withValue(Phone.TYPE, Phone.TYPE_WORK);
					builder.withValue(Phone.NUMBER, telphones[i]);
					builder.withValue(Phone.IS_PRIMARY, 1);
					operationList.add(builder.build());
				}
			}
		}

		// 添加传真
		if (Helper.isNotEmpty(contact.getFax())) {
			String[] faxArr = contact.getFax().split(",");
			for (int i = 0, length = faxArr.length; i < length; i++) {
				if (Helper.isNotEmpty(faxArr[i])) {
					builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
					builder.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex);
					builder.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
					builder.withValue(Phone.TYPE, Phone.TYPE_FAX_WORK);
					builder.withValue(Phone.NUMBER, faxArr[i]);
					builder.withValue(Phone.IS_PRIMARY, 1);
					operationList.add(builder.build());
				}
			}
		}
		// 添加邮箱地址
		if (Helper.isNotEmpty(contact.getEmail())) {
			String[] emails = contact.getEmail().split(",");
			for (int i = 0, length = emails.length; i < length; i++) {
				if (Helper.isNotEmpty(emails[i])) {
					builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
					builder.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex);
					builder.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
					builder.withValue(Email.TYPE, Email.TYPE_WORK);
					builder.withValue(Email.DATA, emails[i]);
					operationList.add(builder.build());
				}
			}
		}

		// 添加公司网站
		if (Helper.isNotEmpty(contact.getCompanyHome())) {
			builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
			builder.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex);
			builder.withValue(Data.MIMETYPE, Website.CONTENT_ITEM_TYPE);
			builder.withValue(Website.TYPE, Website.TYPE_HOMEPAGE);
			builder.withValue(Website.DATA, contact.getCompanyHome());
			operationList.add(builder.build());
		}

		// 添加及时通讯信息
		ArrayList<InstantMessengerEntity> imList = contact.getInstantMessengerList();
		if (Helper.isNotEmpty(imList)) {
			InstantMessengerEntity im = null;
			for (int i = 0, size = imList.size(); i < size; i++) {
				im = imList.get(i);
				builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
				builder.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex);
				builder.withValue(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
				builder.withValue(Im.TYPE, Im.TYPE_OTHER);
				builder.withValue(Im.DATA, im.getValue());
				builder.withValue(Im.PROTOCOL, im.getProtocol());
				builder.withValue(Im.CUSTOM_PROTOCOL, im.getName());
				operationList.add(builder.build());
			}
		}

		// 添加分组
		if (contact.getGroupId() > 0) {
			builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
			builder.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex);
			builder.withValue(Data.MIMETYPE, GroupMembership.CONTENT_ITEM_TYPE);
			builder.withValue(GroupMembership.GROUP_ROW_ID, contact.getGroupId());
			operationList.add(builder.build());
		}
		try {
			mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);

		} catch (RemoteException e) {
			Log.e(TAG, "插入联系人异常", e);
			rawContactInsertIndex = 0;
		} catch (OperationApplicationException e) {
			rawContactInsertIndex = 0;
			Log.e(TAG, "插入联系人异常", e);
		}
		return rawContactInsertIndex;
	}

	/**
	 * 更新联系人
	 * 
	 */
	private void update(ContactEntity contactNew, ContactEntity contactOld) {
		String contactRawId = String.valueOf(contactOld.getContactId());
		ContentValues values = new ContentValues();
		// 更新收藏标示
		if (contactNew.getStarred() != contactOld.getStarred()) {
			values.put(RawContacts.STARRED, contactNew.getStarred());
			mContentResolver.update(RawContacts.CONTENT_URI, values, RawContacts._ID + "=?", new String[] { contactRawId });
		}

		// 新的数据对通讯录只做添加

		// 追加手机号
		if (Helper.isNotEmpty(contactNew.getMobile())) {
			String curMobile = contactNew.getMobile();
			if (Helper.isNotEmpty(contactOld.getMobile())) {
				String[] oldMobiles = contactOld.getMobile().split(",");
				for (int i = 0; i < oldMobiles.length; i++) {
					curMobile = curMobile.replace(oldMobiles[i], "");
				}
			}
			if (Helper.isNotEmpty(curMobile)) {
				String[] newMobiles = curMobile.split(",");
				for (int i = 0; i < newMobiles.length; i++) {
					if (Helper.isNotEmpty(newMobiles[i])) {
						values.clear();
						values.put(Data.RAW_CONTACT_ID, contactRawId);
						values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
						values.put(Phone.TYPE, Phone.TYPE_MOBILE);
						values.put(Phone.NUMBER, newMobiles[i]);
						values.put(Data.IS_PRIMARY, 1);
						mContentResolver.insert(Data.CONTENT_URI, values);
					}
				}
			}
		}
		// 追加公司名称
		if (Helper.isNotEmpty(contactNew.getCompanyName())) {
			String curCompanyName = contactNew.getCompanyName();
			if (Helper.isNotEmpty(contactOld.getCompanyName())) {
				curCompanyName = curCompanyName.replace(contactOld.getCompanyName(), "");
			}
			if (Helper.isNotEmpty(curCompanyName)) {
				values.clear();
				values.put(Data.RAW_CONTACT_ID, contactRawId);
				values.put(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
				values.put(Organization.TYPE, Organization.TYPE_WORK);
				values.put(Organization.COMPANY, curCompanyName);
				values.put(Data.IS_PRIMARY, 1);
				mContentResolver.insert(Data.CONTENT_URI, values);
			}
		}

		// 更改公司地址
		if (Helper.isNotEmpty(contactNew.getAddress())) {
			String curAddress = contactNew.getAddress();
			if (Helper.isNotEmpty(contactOld.getAddress())) {
				curAddress = curAddress.replace(contactOld.getAddress(), "");
			}
			if (Helper.isNotEmpty(curAddress)) {
				values.clear();
				values.put(Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE);
				values.put(StructuredPostal.TYPE, StructuredPostal.TYPE_WORK);
				values.put(StructuredPostal.STREET, curAddress);
				if (Helper.isNotEmpty(contactNew.getPostcode())) {
					values.put(StructuredPostal.POSTCODE, contactNew.getPostcode());
				}
				values.put(Data.IS_PRIMARY, 1);
				String where = Data.RAW_CONTACT_ID + "=? and " + StructuredPostal.STREET + "=?";
				mContentResolver.update(Data.CONTENT_URI, values, where, new String[]{contactRawId, contactOld.getAddress()});
			}
		}

		// 追加固话
		if (Helper.isNotEmpty(contactNew.getTelephone())) {
			String curTel = contactNew.getTelephone();
			if (Helper.isNotEmpty(contactOld.getTelephone())) {
				String[] oldTels = contactOld.getTelephone().split(",");
				for (int i = 0; i < oldTels.length; i++) {
					curTel = curTel.replace(oldTels[i], "");
				}
			}
			if (Helper.isNotEmpty(curTel)) {
				String[] newTels = curTel.split(",");
				for (int i = 0; i < newTels.length; i++) {
					if (Helper.isNotEmpty(newTels[i])) {
						values.clear();
						values.put(Data.RAW_CONTACT_ID, contactRawId);
						values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
						values.put(Phone.TYPE, Phone.TYPE_WORK);
						values.put(Phone.NUMBER, newTels[i]);
						values.put(Data.IS_PRIMARY, 1);
						mContentResolver.insert(Data.CONTENT_URI, values);
					}
				}
			}
		}

		// 追加传真
		if (Helper.isNotEmpty(contactNew.getFax())) {
			String curFax = contactNew.getFax();
			if (Helper.isNotEmpty(contactOld.getFax())) {
				String[] oldFaxs = contactOld.getFax().split(",");
				for (int i = 0; i < oldFaxs.length; i++) {
					curFax = curFax.replace(oldFaxs[i], "");
				}
			}
			if (Helper.isNotEmpty(curFax)) {
				String[] newFaxs = curFax.split(",");
				for (int i = 0; i < newFaxs.length; i++) {
					if (Helper.isNotEmpty(newFaxs[i])) {
						values.clear();
						values.put(Data.RAW_CONTACT_ID, contactRawId);
						values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
						values.put(Phone.TYPE, Phone.TYPE_FAX_WORK);
						values.put(Phone.NUMBER, newFaxs[i]);
						values.put(Data.IS_PRIMARY, 1);
						mContentResolver.insert(Data.CONTENT_URI, values);
					}
				}
			}
		}

		// 追加邮箱
		if (Helper.isNotEmpty(contactNew.getEmail())) {
			String curEmail = contactNew.getEmail();
			if (Helper.isNotEmpty(contactOld.getEmail())) {
				String[] oldEmails = contactOld.getEmail().split(",");
				for (int i = 0; i < oldEmails.length; i++) {
					curEmail = curEmail.replace(oldEmails[i], "");
				}
			}
			if (Helper.isNotEmpty(curEmail)) {
				String[] newEmails = curEmail.split(",");
				for (int i = 0; i < newEmails.length; i++) {
					if (Helper.isNotEmpty(newEmails[i])) {
						values.clear();
						values.put(Data.RAW_CONTACT_ID, contactRawId);
						values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
						values.put(Email.TYPE, Email.TYPE_WORK);
						values.put(Email.DATA, newEmails[i]);
						values.put(Data.IS_PRIMARY, 1);
						mContentResolver.insert(Data.CONTENT_URI, values);
					}
				}
			}
		}

		// 追加网址
		if (Helper.isNotEmpty(contactNew.getCompanyHome())) {
			String curHome = contactNew.getCompanyHome();
			if (Helper.isNotEmpty(contactOld.getCompanyHome())) {
				curHome = curHome.replace(contactOld.getCompanyHome(), "");
			}
			if (Helper.isNotEmpty(curHome)) {
				values.clear();
				values.put(Data.RAW_CONTACT_ID, contactRawId);
				values.put(Data.MIMETYPE, Website.CONTENT_ITEM_TYPE);
				values.put(Website.TYPE, Website.TYPE_HOMEPAGE);
				values.put(Website.DATA, curHome);
				values.put(Data.IS_PRIMARY, 1);
				mContentResolver.insert(Data.CONTENT_URI, values);
			}
		}

		// 追加即时通讯
		if (Helper.isNotEmpty(contactNew.getInstantMessengerList())) {
			ArrayList<InstantMessengerEntity> curImList = contactNew.getInstantMessengerList();
			if (Helper.isNotEmpty(contactOld.getInstantMessengerList())) {
				HashMap<String, InstantMessengerEntity> imNewMap = new HashMap<String, InstantMessengerEntity>();

				for (int i = 0, size = curImList.size(); i < size; i++) {
					InstantMessengerEntity im = curImList.get(i);
					imNewMap.put(im.getValue(), im);
				}
				// 移出已经存在的数据
				for (int i = 0, size = contactOld.getInstantMessengerList().size(); i < size; i++) {
					InstantMessengerEntity imOld = contactOld.getInstantMessengerList().get(i);
					imNewMap.remove(imOld.getValue());
				}
				// 追加新的数据
				if (imNewMap.size() > 0) {
					for (InstantMessengerEntity imItem : imNewMap.values()) {
						values.clear();
						values.put(Data.RAW_CONTACT_ID, contactRawId);
						values.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
						values.put(Im.PROTOCOL, imItem.getProtocol());
						values.put(Im.DATA, imItem.getValue());
						values.put(Im.CUSTOM_PROTOCOL, imItem.getName());
						mContentResolver.insert(Data.CONTENT_URI, values);
					}
				}
			}
		}
	}

	/**
	 * 根据 contact_id 获取 contact_raw_id
	 * 
	 * @param contactId
	 * @return
	 */
	public long getContactRawId(long contactId) {
		long rawId = 0;
		String where = RawContacts.CONTACT_ID + " = " + contactId;
		Cursor cursor = mContentResolver.query(RawContacts.CONTENT_URI, new String[] { String.valueOf(RawContacts._ID) }, where, null, null);
		if (Helper.isNotNull(cursor)) {
			if (cursor.moveToFirst()) {
				rawId = cursor.getLong(0);
			}
			cursor.close();
			cursor = null;
		}
		return rawId;
	}

	/**
	 * 获取本机通讯录联系人实体
	 * 
	 * @param contactId
	 *            本机通讯录联系人的contact_id
	 * @return 本机通讯录联系人实体
	 */
	public ContactEntity getEntity(long contactId) {
		if (contactId < 1) {
			return null;
		}
		ContactEntity contactEntity = new ContactEntity();
		String telphone = "", mobile = "", fax = "", webSite = "", email = "";
		ArrayList<InstantMessengerEntity> imList = new ArrayList<InstantMessengerEntity>();
		// 获取联系人的收藏夹标识
		long contactRawId = 0;
		contactEntity.setId((int) contactId);

		Cursor starredCursor = mContentResolver.query(RawContacts.CONTENT_URI, new String[] { RawContacts.STARRED, RawContacts._ID },
				RawContacts.CONTACT_ID + "=?", new String[] { String.valueOf(contactId) }, null);
		if (Helper.isNotNull(starredCursor)) {
			if (starredCursor.moveToFirst()) {
				contactRawId = starredCursor.getLong(starredCursor.getColumnIndex(RawContacts._ID));
				contactEntity.setContactId(contactRawId);

				int starred = starredCursor.getInt(starredCursor.getColumnIndex(RawContacts.STARRED));
				contactEntity.setStarred(starred);
			}
			starredCursor.close();
		}

		// 获取分组
		int groupId = getGroupId(contactRawId);
		String groupName = getGroupName(groupId);
		contactEntity.setGroupName(groupName);
		contactEntity.setGroupId(groupId);

		Cursor contactCursor = mContentResolver.query(Data.CONTENT_URI, null, Data.RAW_CONTACT_ID + "=?",
				new String[] { String.valueOf(contactRawId) }, null);
		String mimeType;
		while (Helper.isNotNull(contactCursor) && contactCursor.moveToNext()) {
			mimeType = contactCursor.getString(contactCursor.getColumnIndex(Data.MIMETYPE));
			if (mimeType.equalsIgnoreCase(StructuredName.CONTENT_ITEM_TYPE)) {
				// 获取名字
				String displayName = contactCursor.getString(contactCursor.getColumnIndex(StructuredName.DISPLAY_NAME));
				contactEntity.setDisplayName(displayName);
			} else if (mimeType.equalsIgnoreCase(Organization.CONTENT_ITEM_TYPE)) {
				// 获取公司名字
				int companyType = contactCursor.getInt(contactCursor.getColumnIndex(Organization.TYPE));
				String company = contactCursor.getString(contactCursor.getColumnIndex(Organization.COMPANY));
				if (companyType == Organization.TYPE_WORK) {
					String job = contactCursor.getString(contactCursor.getColumnIndex(Organization.JOB_DESCRIPTION));
					contactEntity.setCompanyName(company);
					contactEntity.setJob(job);
				} else if (companyType == Organization.TYPE_OTHER) {
					contactEntity.setCompanyEnName(company);
				}
			} else if (mimeType.equalsIgnoreCase(Phone.CONTENT_ITEM_TYPE)) {
				String curPhone = contactCursor.getString(contactCursor.getColumnIndex(Phone.NUMBER));
				if (contactCursor.getInt(contactCursor.getColumnIndex(Phone.TYPE)) == Phone.TYPE_WORK) {
					// 获取电话号码，多个号码之间用“,”隔开
					if(!telphone.contains(curPhone)){
						telphone += curPhone + ",";
					}
				} else if (contactCursor.getInt(contactCursor.getColumnIndex(Phone.TYPE)) == Phone.TYPE_MOBILE) {
					// 获取手机号码
					String curMobile = contactCursor.getString(contactCursor.getColumnIndex(Phone.NUMBER));
					if(!mobile.contains(curMobile)){
						mobile += curMobile +",";
					}
				} else if (contactCursor.getInt(contactCursor.getColumnIndex(Phone.TYPE)) == Phone.TYPE_FAX_WORK) {
					// 获取传真号码，多个号码之间用“,”隔开
					if(!fax.contains(curPhone)){
						fax += curPhone + ",";
					}
				}
			} else if (mimeType.equalsIgnoreCase(Email.CONTENT_ITEM_TYPE)) {
				String curEmail = contactCursor.getString(contactCursor.getColumnIndex(Email.DATA));
				if (contactCursor.getInt(contactCursor.getColumnIndex(Email.TYPE)) == Email.TYPE_WORK) {
					// 获取邮箱，多个邮箱之间用“,”隔开
					if(!email.contains(curEmail)){
						email += curEmail + ",";
					}
				}
			} else if (mimeType.equalsIgnoreCase(StructuredPostal.CONTENT_ITEM_TYPE)) {
				if (contactCursor.getInt(contactCursor.getColumnIndex(StructuredPostal.TYPE)) == StructuredPostal.TYPE_WORK) {
					// 获取工作地址
					String postcode = contactCursor.getString(contactCursor.getColumnIndex(StructuredPostal.POSTCODE));
					String state = contactCursor.getString(contactCursor.getColumnIndex(StructuredPostal.REGION));
					String country = contactCursor.getString(contactCursor.getColumnIndex(StructuredPostal.COUNTRY));
					String street = contactCursor.getString(contactCursor.getColumnIndex(StructuredPostal.STREET));
					contactEntity.setCompanyCountry(country);
					contactEntity.setCompanyProvince(state);
					contactEntity.setAddress(street);
					contactEntity.setPostcode(postcode);
				}
			} else if (mimeType.equalsIgnoreCase(Website.CONTENT_ITEM_TYPE)) {
				// 获取公司首页
				webSite = contactCursor.getString(contactCursor.getColumnIndex(Website.DATA));
				contactEntity.setCompanyHome(webSite);
			} else if (mimeType.equalsIgnoreCase(Im.CONTENT_ITEM_TYPE)) {
				String value = contactCursor.getString(contactCursor.getColumnIndex(Im.DATA));
				String name = contactCursor.getString(contactCursor.getColumnIndex(Im.CUSTOM_PROTOCOL));
				int protocol = contactCursor.getInt(contactCursor.getColumnIndex(Im.PROTOCOL));
				if (protocol == Im.PROTOCOL_QQ) {
					if (Helper.isEmpty(name)) {
						name = DatabaseConstant.InstantMessengerLabel.QQ;
					}
				} else if (protocol == Im.PROTOCOL_MSN) {
					if (Helper.isEmpty(name)) {
						name = DatabaseConstant.InstantMessengerLabel.MSN;
					}
				}
				imList.add(new InstantMessengerEntity(name, value, protocol));
			}
		}
		if(Helper.isNotEmpty(mobile)){
			contactEntity.setMobile(mobile.substring(0, mobile.length() - 1));
		}
		if (Helper.isNotEmpty(telphone)) {
			contactEntity.setTelephone(telphone.substring(0, telphone.length() - 1));
		}
		if (Helper.isNotEmpty(fax)) {
			contactEntity.setFax(fax.substring(0, fax.length() - 1));
		}
		if (Helper.isNotEmpty(email)) {
			contactEntity.setEmail(email.substring(0, email.length() - 1));
		}
		if (imList.size() > 1) {
			contactEntity.setInstantMessengerList(imList);
		}
		contactCursor.close();
		return contactEntity;
	}

	/**
	 * 根据姓名 电话号码 获取对象
	 * 
	 * @param displayName
	 * @param mobile
	 * @param telphone
	 * @return
	 */
	public ContactEntity getEntity(String displayName, String mobile, String telphone) {

		ContactEntity contactEntity = null;
		String phone = "";
		if (Helper.isNotEmpty(mobile)) {
			phone = mobile;
		}
//		if (Helper.isNotEmpty(telphone)) {
//			if (Helper.isNotEmpty(phone)) {
//				phone += ",";
//			}
//			phone += telphone;
//		}
		String where = Data.MIMETYPE + " =? AND " + Phone.DISPLAY_NAME + "=? AND " + Phone.NUMBER + " in ('" + phone.trim().replace(",", "','") + "')";
		String[] whereArgs = new String[] { Phone.CONTENT_ITEM_TYPE, displayName };
		Cursor selCursor = mContentResolver.query(Data.CONTENT_URI, new String[] { Data.CONTACT_ID }, where, whereArgs, null);
		if (Helper.isNotNull(selCursor)) {
			if (selCursor.moveToFirst()) {
				long contactId = selCursor.getLong(0);
				contactEntity = getEntity(contactId);
			}
			selCursor.close();
			selCursor = null;
		}
		return contactEntity;
	}

	/**
	 * 根据条件获取联系人的备份数据
	 * 
	 * @return
	 */
	private ArrayList<ContactBookBackupJsonEntity> getListForBackup(String where) {
		ArrayList<ContactBookBackupJsonEntity> bookList = new ArrayList<ContactBookBackupJsonEntity>();
		Cursor cursor = mContentResolver.query(Contacts.CONTENT_URI, new String[] { Contacts._ID }, where, null, COL_SORT_KEY);
		if (Helper.isNotNull(cursor)) {
			while (cursor.moveToNext()) {
				long contactId = cursor.getLong(cursor.getColumnIndex(Contacts._ID));
				ContactBookBackupJsonEntity bookItem = getEntityForBackup(contactId);
				if (!bookList.contains(bookItem)) {
					bookList.add(bookItem);
				}
			}
			cursor.close();
			cursor = null;
		}
		return bookList;
	}

	/**
	 * 更新头像
	 * 
	 * @param contactRawId
	 * @param photoBytes
	 */
	private void updateHeadPhoto(long contactRawId, byte[] photoBytes) {
		if (contactRawId < 1) {
			return;
		}

		try {

			int photoRow = -1;
			String where = Data.RAW_CONTACT_ID + " = " + contactRawId + " AND " + Data.MIMETYPE + "='"
					+ Photo.CONTENT_ITEM_TYPE + "'";
			Cursor cursor = mContentResolver.query(Data.CONTENT_URI, null, where, null, null);
			int idIdx = cursor.getColumnIndexOrThrow(Data._ID);
			if (cursor.moveToFirst()) {
				photoRow = cursor.getInt(idIdx);
			}
			cursor.close();

			if (Helper.isNull(photoBytes)) {
				if (photoRow > 0) {
					mContentResolver.delete(Data.CONTENT_URI, Data._ID + " = " + photoRow, null);
				}
			} else {

				ContentValues photoContentValues = new ContentValues();
				photoContentValues.put(Photo.PHOTO, photoBytes);
				ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
				if (photoRow > 0) {// 编辑联系人时有头像,直接更新
					ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(Data.CONTENT_URI);
					builder.withSelection(Data._ID + "=?", new String[] { String.valueOf(photoRow) });
					builder.withValues(photoContentValues);
					builder.withYieldAllowed(true);
					ops.add(builder.build());
				} else {// 编辑联系人时,没有头像.因此需要使用插入操作
					ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
					builder.withValue(Data.RAW_CONTACT_ID, contactRawId);
					builder.withValue(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
					builder.withValue(Photo.IS_SUPER_PRIMARY, 1);
					builder.withValues(photoContentValues);
					builder.withYieldAllowed(true);
					ops.add(builder.build());
				}
				mContentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
			}
		} catch (Exception e) {
			Log.e(TAG, "修改通讯录头像异常");
			e.printStackTrace();
		}
	}
	/**
	 * 获取通讯录的备份数据
	 * 
	 */
	public ArrayList<ContactBookBackupJsonEntity> getListForBackup(ArrayList<ContactGroupBackupEntity> groupList) {
		ArrayList<ContactBookBackupJsonEntity> bookList = new ArrayList<ContactBookBackupJsonEntity>();
		if (Helper.isEmpty(groupList)) {
			return bookList;
		}
		boolean isContainUnGroup = false;
		StringBuilder idCombin = new StringBuilder();
		String favWhere = "";
		String where = " 1 ";
		for (int i = 0, size = groupList.size(); i < size; i++) {
			int curGrpId = groupList.get(i).getGroupId();
			if (curGrpId == ContactGroupEntity.GROUP_FAVORITE_ID) {
				favWhere = " or " + Contacts.STARRED + "=1";
			} else {
				if (curGrpId == 0) {
					isContainUnGroup = true;
					continue;
				} else {
					idCombin.append(curGrpId + ",");
				}
			}
		}

		// 如果有选择未分组，则获取全部联系人，否则按组获取联系人
		if (Helper.isNotEmpty(idCombin)) {
			where += " and (_id in ( select contact_id from view_data where " + Data.MIMETYPE + "='" + GroupMembership.CONTENT_ITEM_TYPE
					+ "' and " + GroupMembership.GROUP_ROW_ID + " in (" + idCombin.substring(0, idCombin.length() - 1) + ")) ) ";
		}

		where += favWhere;
	

		bookList = new ArrayList<ContactBookBackupJsonEntity>(getListForBackup(where));

		// 判断是否有选择未分组
		if (isContainUnGroup) {
			ArrayList<ContactBookBackupJsonEntity> unGroupBookList = new ArrayList<ContactBookBackupJsonEntity>();
			// 若有选择未分组则添加
			try {
				ArrayList<Long> contactIdList = getContactIdListForUnGroup();
				for (int i = 0, size = contactIdList.size(); i < size; i++) {
					unGroupBookList.add(getEntityForBackup(contactIdList.get(i)));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (int i = 0, size = unGroupBookList.size(); i < size; i++) {
				bookList.add(unGroupBookList.get(i));
			}
		}

		return bookList;
	}
	/**
	 * 根据分组id 获取导入导出的 通讯录联系人
	 * @param groupId
	 * @return
	 */
	public ArrayList<ContactEntity> getListForExport(long groupId){
		ArrayList<ContactEntity> contactList = new ArrayList<ContactEntity>();
		String where = "";
		Cursor cursor = null;
		if(groupId > 0){
			where = Data.MIMETYPE + "='" + GroupMembership.CONTENT_ITEM_TYPE + "' and " + GroupMembership.GROUP_ROW_ID + "=" + groupId;
			cursor = mContentResolver.query(Data.CONTENT_URI, new String[]{Data.CONTACT_ID}, where, null, null);
		}else if(groupId == ContactGroupEntity.GROUP_FAVORITE_ID){
			cursor = mContentResolver.query(Contacts.CONTENT_URI, new String[]{Contacts._ID}, Contacts.STARRED + "<>0", null, null);
		}else if(groupId == ContactGroupEntity.GROUP_UNGROUPED_ID){
			where = " _id not in ( select contact_id from view_data where " + Data.MIMETYPE + "='" + GroupMembership.CONTENT_ITEM_TYPE + "' ) ";
			cursor = mContentResolver.query(Contacts.CONTENT_URI, new String[]{Contacts._ID}, where, null, null);
		}
		if( Helper.isNotNull(cursor)){
			while(cursor.moveToNext()){
				long contactId = cursor.getLong(0);
				ContactEntity contact = getEntity(contactId);
				contactList.add(contact);
			}
			cursor.close();
			cursor = null;
		}
		return contactList;
	}
	/**
	 * 获取单个联系人的通讯录信息
	 */
	public ContactBookBackupJsonEntity getEntityForBackup(long contactId) {
		ContactBookBackupJsonEntity bookBackupEntity = new ContactBookBackupJsonEntity();
		String dataMimyType;
		Cursor dataContactCur = null;
		StringBuffer phoneBuffer = new StringBuffer();
		StringBuffer mobileBuffer = new StringBuffer();
		StringBuffer faxBuffer = new StringBuffer();
		StringBuffer emailBuffer = new StringBuffer();
		String disPlayName = "";
		String company = "";
		String workAddress = "";
		String website = "";
		String job = "";
		String postcode = "";
		int starred = 0;

		ArrayList<InstantMessengerEntity> imList = new ArrayList<InstantMessengerEntity>();
		int groupId = 0;
		try {
			// 获取收藏夹标示
			Cursor starredCursor = mContentResolver.query(RawContacts.CONTENT_URI, new String[] { RawContacts.STARRED }, RawContacts.CONTACT_ID
					+ "=?", new String[] { String.valueOf(contactId) }, null);
			if (Helper.isNotNull(starredCursor)) {
				if (starredCursor.moveToFirst()) {
					starred = starredCursor.getInt(0);
				}
				starredCursor.close();
				starredCursor = null;
			}

			dataContactCur = mContext.getContentResolver().query(Data.CONTENT_URI, null, Data.CONTACT_ID + "=?",
					new String[] { String.valueOf(contactId) }, null);

			while (Helper.isNotNull(dataContactCur) && dataContactCur.moveToNext()) {

				dataMimyType = dataContactCur.getString(dataContactCur.getColumnIndex(Data.MIMETYPE));
				if (dataMimyType.equalsIgnoreCase(StructuredName.CONTENT_ITEM_TYPE)) {
					disPlayName = dataContactCur.getString(dataContactCur.getColumnIndex(StructuredName.DISPLAY_NAME));
				} else if (dataMimyType.equalsIgnoreCase(Organization.CONTENT_ITEM_TYPE)) {
					company = dataContactCur.getString(dataContactCur.getColumnIndex(Organization.COMPANY));
					job = dataContactCur.getString(dataContactCur.getColumnIndex(Organization.JOB_DESCRIPTION));
				} else if (dataMimyType.equalsIgnoreCase(Phone.CONTENT_ITEM_TYPE)) {
					if (dataContactCur.getInt(dataContactCur.getColumnIndex(Phone.TYPE)) == Phone.TYPE_WORK) {
						String phone = dataContactCur.getString(dataContactCur.getColumnIndex(Phone.NUMBER));
						if(phoneBuffer.indexOf(phone) == -1){
							if (Helper.isNotEmpty(phoneBuffer)) {
								phoneBuffer.append(",");
							}
							phoneBuffer.append(phone);
						}

					} else if (dataContactCur.getInt(dataContactCur.getColumnIndex(Phone.TYPE)) == Phone.TYPE_MOBILE) {
						String mobilePhone = dataContactCur.getString(dataContactCur.getColumnIndex(Phone.NUMBER));
						if(mobileBuffer.indexOf(mobilePhone) == -1){
							if (Helper.isNotEmpty(mobileBuffer)) {
								mobileBuffer.append(",");
							}
							mobileBuffer.append(mobilePhone);
						}
					} else if (dataContactCur.getInt(dataContactCur.getColumnIndex(Phone.TYPE)) == Phone.TYPE_FAX_WORK) {
						String fax = dataContactCur.getString(dataContactCur.getColumnIndex(Phone.NUMBER));
						if(faxBuffer.indexOf(fax) == -1){
							if (Helper.isNotEmpty(faxBuffer)) {
								faxBuffer.append(",");
							}
							faxBuffer.append(fax);
						}
					}
				} else if (dataMimyType.equalsIgnoreCase(Email.CONTENT_ITEM_TYPE)) {
					String email = dataContactCur.getString(dataContactCur.getColumnIndex(Email.DATA));
					if(emailBuffer.indexOf(email) == -1){
						if (Helper.isNotEmpty(emailBuffer)) {
							emailBuffer.append(",");
						}
						emailBuffer.append(email);
					}
				} else if (dataMimyType.equalsIgnoreCase(StructuredPostal.CONTENT_ITEM_TYPE)) {
					if (dataContactCur.getInt(dataContactCur.getColumnIndex(StructuredPostal.TYPE)) == StructuredPostal.TYPE_WORK) {
						String fullAddress = "";
						postcode = dataContactCur.getString(dataContactCur.getColumnIndex(StructuredPostal.POSTCODE));
						String city = dataContactCur.getString(dataContactCur.getColumnIndex(StructuredPostal.CITY));
						String state = dataContactCur.getString(dataContactCur.getColumnIndex(StructuredPostal.REGION));
						String country = dataContactCur.getString(dataContactCur.getColumnIndex(StructuredPostal.COUNTRY));
						String street = dataContactCur.getString(dataContactCur.getColumnIndex(StructuredPostal.STREET));

						if (ResourceHelper.isNotEmpty(country)) {
							fullAddress += country;
						}
						if (ResourceHelper.isNotEmpty(state)) {
							fullAddress += state;
						}
						if (ResourceHelper.isNotEmpty(city)) {
							fullAddress += city;
						}
						if (ResourceHelper.isNotEmpty(street)) {
							fullAddress += street;
						}
						if (ResourceHelper.isNotEmpty(fullAddress) && !workAddress.contains(fullAddress)) {
							if (ResourceHelper.isNotEmpty(workAddress.trim())) {
								workAddress += "#";
							}
							workAddress += fullAddress;
						}
					}
				} else if (dataMimyType.equalsIgnoreCase(Website.CONTENT_ITEM_TYPE)) {
					website = dataContactCur.getString(dataContactCur.getColumnIndex(Website.DATA));
				} else if (dataMimyType.equalsIgnoreCase(Im.CONTENT_ITEM_TYPE)) {
					String value = dataContactCur.getString(dataContactCur.getColumnIndex(Im.DATA));
					String name = dataContactCur.getString(dataContactCur.getColumnIndex(Im.CUSTOM_PROTOCOL));
					int protocol = dataContactCur.getInt(dataContactCur.getColumnIndex(Im.PROTOCOL));
					if (protocol == Im.PROTOCOL_QQ) {
						if (Helper.isEmpty(name)) {
							name = DatabaseConstant.InstantMessengerLabel.QQ;
						}
					} else if (protocol == Im.PROTOCOL_MSN) {
						if (Helper.isEmpty(name)) {
							name = DatabaseConstant.InstantMessengerLabel.MSN;
						}
					}
					imList.add(new InstantMessengerEntity(name, value, protocol));

				} else if (dataMimyType.equalsIgnoreCase(GroupMembership.CONTENT_ITEM_TYPE)) {
					groupId = dataContactCur.getInt(dataContactCur.getColumnIndex(GroupMembership.GROUP_ROW_ID));
				}
			}
		} catch (IllegalStateException e) {
			Log.i(TAG, "获取通讯录联系人异常", e);
		} finally {
			if (Helper.isNotNull(dataContactCur)) {
				dataContactCur.close();
				dataContactCur = null;
			}
		}

		bookBackupEntity.setCompany(company);
		bookBackupEntity.setJob(job);
		bookBackupEntity.setEmail(emailBuffer.toString());
		bookBackupEntity.setFax(faxBuffer.toString());
		bookBackupEntity.setWebsite(website);
		bookBackupEntity.setMobile(mobileBuffer.toString());
		bookBackupEntity.setName(disPlayName);
		bookBackupEntity.setPhone(phoneBuffer.toString());
		bookBackupEntity.setWorkAddress(workAddress);
		bookBackupEntity.setPostcode(postcode);
		bookBackupEntity.setSyncId(contactId);
		bookBackupEntity.setSyncGroupId(groupId);
		bookBackupEntity.setInstantMessengerList(imList);
		bookBackupEntity.setStarred(starred);
		String groupName = getGroupName(groupId);
		bookBackupEntity.setSyncGroupName(groupName);
		return bookBackupEntity;
	}
	
	/**
	 * 根据分组id获取联系人
	 * 
	 * @param groupId
	 * @return
	 */
	public ArrayList<ContactBookEntity> getListForShow(int groupId) {
		if (Helper.isNull(mContactListOfBook)) {
			mContactListOfBook = new HashMap<Integer, ArrayList<ContactBookEntity>>();
		}
		if(!mContactListOfBook.containsKey(groupId)){
			mContactListOfBook.put(groupId, getList(groupId));
		}
		return mContactListOfBook.get(groupId);
	}
	/**
	 * 获取好友推荐里的联系人集合（不含注册过的微片用户）
	 * @return
	 */
	public ArrayList<ContactBookEntity> getListForRecommend(ArrayList<VCardUserFromMobileResultEntity> vcardUserList){
		ArrayList<ContactBookEntity> contactList = null;
		StringBuilder mobiles = new StringBuilder();
		String where = null;
		if(Helper.isNotNull(vcardUserList)){
			for (int i = 0, size = vcardUserList.size(); i < size; i++) {
				VCardUserFromMobileResultEntity vcardUser = vcardUserList.get(i);
				if(Helper.isNotNull(vcardUser) && Helper.isNotEmpty(vcardUser.getMobile())){
					mobiles.append("'").append(vcardUser.getMobile().trim()).append("',");
				}
			}
			if(Helper.isNotEmpty(mobiles)){
				where = Contacts._ID.concat( " not in (select ").concat(Data.CONTACT_ID).concat(" from ").concat("view_data")
						.concat(" where ").concat(Phone.NUMBER).concat(" in (").concat(mobiles.substring(0, mobiles.length() - 1)).concat( "))");
			}
		}
		try{
			Cursor cursor = this.mContentResolver.query(Contacts.CONTENT_URI, CONTACT_PROJECTION, where, null, COL_SORT_KEY);
			if(Helper.isNotNull(cursor)){
				contactList = new ArrayList<ContactBookEntity>();
				while (cursor.moveToNext()) {
					ContactBookEntity cont = setContactBookEntity(cursor);
					contactList.add(cont);
				}
				cursor.close();
				cursor = null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return contactList;
	}
	/**
	 * 添加联系人到本机通讯录
	 * 
	 * @param contact
	 */
	public void add(ContactEntity contact) {
		if (Helper.isNull(contact)) {
			return;
		}
		long contactRawId = 0;
		String displayName = contact.getDisplayName();
		if (Helper.isEmpty(displayName)) {
			displayName = contact.getFullName();
		}
		ContactEntity contactOld = getEntity(displayName, contact.getMobile(), contact.getTelephone());
		if (Helper.isNotNull(contactOld)) {
			contactRawId = contactOld.getContactId();
			update(contact, contactOld);
		} else {
			contactRawId = insert(contact);
		}
		//TODO 通讯录设置头像
		if(CurrentUser.getInstance().getSetting().getAddressListShowCard() == 0 ){
			//插入名片
			setContactPhoto(contactRawId, contact.getCardImgA());
		}else{
			setContactPhoto(contactRawId, ResourceHelper.getImageUrlOnIndex(contact.getHeadImg(), 0));
		}
	}

	/**
	 * 分组添加联系人
	 * 
	 * @param groupId
	 * @param contactRawId
	 *            联系人 raw_contact_id
	 * @return boolean
	 */
	public boolean joinGroup(int groupId, long contactRawId) {
		try {
			ContentValues values = new ContentValues();
			values.put(Data.RAW_CONTACT_ID, contactRawId);
			values.put(GroupMembership.GROUP_ROW_ID, groupId);
			values.put(Data.MIMETYPE, GroupMembership.CONTENT_ITEM_TYPE);
			Uri inUri = mContentResolver.insert(Data.CONTENT_URI, values);
			return null == inUri ? false : true;
		} catch (Exception ex) {
			Log.e(TAG, "移动分组异常", ex);
			return false;
		}
	}

	/**
	 * 移除分组下的联系人
	 * 
	 * @param groupId
	 * @param contactRawId
	 *            联系人 raw_contact_id
	 * @return boolean
	 */
	public boolean removeContact(int groupId, long contactRawId) {
		int delRow = mContentResolver.delete(Data.CONTENT_URI, GroupMembership.RAW_CONTACT_ID
				+ "=? and " + GroupMembership.GROUP_ROW_ID + "=? and "
				+ GroupMembership.MIMETYPE + "=?", new String[] { "" + contactRawId, "" + groupId,
				GroupMembership.CONTENT_ITEM_TYPE });
		return delRow > 0 ? true : false;
	}

	/**
	 * 移动分组
	 * 
	 * @param contactRawId
	 *            联系人 raw_contact_id
	 * @param fromGroupId
	 *            原来的 group ID
	 * @param toGroupId
	 *            新的 group id
	 */
	public void moveContact(long contactRawId, int fromGroupId, int toGroupId) {

		if (fromGroupId > 0) {
			removeContact(fromGroupId, contactRawId);
		}
		if (toGroupId > 0) {
			joinGroup(toGroupId, contactRawId);
		}
	}
	/**
	 * 批量移动联系人
	 * @param contactList
	 * @param fromGroup
	 * @param toGroup
	 */
	public void moveContact(ArrayList<ContactBookEntity> contactList, int fromGroup, int toGroup){
		if(Helper.isNotNull(contactList)){
			for (int i = 0, size = contactList.size(); i < size; i++) {
				moveContact(contactList.get(i).getContactRawId(), fromGroup, toGroup);
			}
		}
	}
	/**
	 * 移动联系人到收藏夹
	 * @param contactList
	 */
	public void moveContactToStarred(ArrayList<ContactBookEntity> contactList, int fromGroupId){
		if(Helper.isNotNull(contactList)){
			for (int i = 0, size = contactList.size(); i < size; i++) {
				moveContactToStarred(contactList.get(i), fromGroupId);
			}
		}
	}
	/**
	 * 移动联系人 到 收藏夹
	 * @param contact
	 */
	public void moveContactToStarred(ContactBookEntity contact, int fromGroupId){
		if (fromGroupId > 0) {
			removeContact(fromGroupId, contact.getContactRawId());
		}
		ContentValues values = new ContentValues();
		values.put(Contacts.STARRED, 1);
		this.mContentResolver.update(Contacts.CONTENT_URI, values, Contacts._ID + "=" + contact.getContactId(), null);
	}
	/**
	 * 删除通讯录联系人
	 * @param contactList
	 */
	public void delete(ArrayList<ContactBookEntity> contactList){
		if(Helper.isNotNull(contactList)){
			for (int i = 0, size = contactList.size(); i < size; i++) {
				delete(contactList.get(i).getContactId());
			}
		}
	}
	/**
	 * 删除单个联系人 并 返删除回结果
	 * @param contactId
	 * @return boolean
	 */
	public int delete(Long contactId){
		int delRows = mContentResolver.delete(RawContacts.CONTENT_URI, RawContacts.CONTACT_ID+" = "+contactId, null);
		return delRows;
	}
	/**
	 * 根据名片路径 获取图片数组 插入到通讯录
	 * @param rawContactId
	 * @param imgUrl
	 */
	public void setContactPhoto(long rawContactId, String imgUrl){
		if(Helper.isEmpty(imgUrl)){
			return;
		}
		// TODO 获取下载的图片 插入到通讯录头像
		Drawable drw = AsyncImageManager.fetchCacheDrawable(imgUrl);
		if(Helper.isNotNull(drw)){
//			byte[] mImgBytes = ResourceHelper.bitmap2ByteArray(((BitmapDrawable)drw).getBitmap(), true);
//			updateHeadPhoto(rawContactId, mImgBytes);
		}
	}
	/**
	 * 获取联系人下的 所有电话号码
	 * @param contactId
	 * @return
	 */
	public ArrayList<String> getPhoneList(long contactId){
		ArrayList<String> phoneList = null;
		Cursor phoneCursor = mContentResolver.query(Phone.CONTENT_URI, new String[]{Phone.NUMBER}, Phone.CONTACT_ID + "=" + contactId, null, null);  
		if (Helper.isNotNull(phoneCursor)) {
			phoneList = new ArrayList<String>();
		    while (phoneCursor.moveToNext()) {  
			    //得到手机号码  
			    String phoneNumber = phoneCursor.getString(0);
			    if(!phoneList.contains(phoneNumber)){
			    	phoneList.add(phoneNumber);
			    }
		    }
		    phoneCursor.close();
		    phoneCursor = null;
		}
		return phoneList;
	}
	/**
	 * 获取联系人的手机号 (多个手机号用 “;” 拼接)
	 * @param contactId
	 * @return
	 */
	public String getMobiles(long contactId){
		StringBuilder mobile = new StringBuilder();
		String where = Phone.CONTACT_ID + "=" + contactId + " and " + Phone.TYPE + "='" + Phone.TYPE_MOBILE + "'";
		Cursor phoneCursor = mContentResolver.query(Phone.CONTENT_URI, new String[]{Phone.NUMBER}, where, null, null);  
		if (Helper.isNotNull(phoneCursor)) {
		    while (phoneCursor.moveToNext()) {
			    mobile.append(phoneCursor.getString(0)).append(";");
		    }
		    phoneCursor.close();
		    phoneCursor = null;
		}
		if(Helper.isNotEmpty(mobile)){
			return mobile.substring(0, mobile.length() -1);
		}
		return null;
	}
	/**
	 * 仅获取查到的第一个手机号码
	 * @param contactId
	 * @return
	 */
	public String getMobile(long contactId){
		String mobile = null;
		String where = Phone.CONTACT_ID + "=" + contactId + " and " + Phone.TYPE + "='" + Phone.TYPE_MOBILE + "'";
		Cursor phoneCursor = mContentResolver.query(Phone.CONTENT_URI, new String[]{Phone.NUMBER}, where, null, null);  
		if (Helper.isNotNull(phoneCursor)) {
		    while (phoneCursor.moveToNext()) {
			    mobile = phoneCursor.getString(0);
			    break;
		    }
		    phoneCursor.close();
		    phoneCursor = null;
		}
		return mobile;
	}
	/**
	 * 获取联系人的所有手机
	 * @param contactList
	 * @return
	 */
	public ArrayList<String> getMobileList(ArrayList<ContactBookEntity> contactList){
		if(Helper.isNull(contactList)){
			return null;
		}
		StringBuilder contactIds = new StringBuilder();
		for (int i = 0, size = contactList.size(); i < size; i++) {
			long contactId = contactList.get(i).getContactId();
			contactIds.append(contactId+",");
		}
		if(Helper.isNotEmpty(contactIds)){
			ArrayList<String> mobileList = new ArrayList<String>();
			String where = Phone.CONTACT_ID + " in (" + contactIds.substring(0, contactIds.length() -1) + ") and " + Phone.TYPE + "='" + Phone.TYPE_MOBILE + "'";
			Cursor phoneCursor = mContentResolver.query(Phone.CONTENT_URI, new String[]{Phone.NUMBER}, where, null, null);  
			if (Helper.isNotNull(phoneCursor)) {
			    while (phoneCursor.moveToNext()) {
			    	mobileList.add(phoneCursor.getString(0));
			    }
			    phoneCursor.close();
			    phoneCursor = null;
			}
			return mobileList;
		}
		return null;
	}
	/**
	 * 获取通讯录的所有手机号码
	 * @return
	 */
	public ArrayList<String> getMobileList(){
		ArrayList<String> mobileList = new ArrayList<String>();
		String where = Phone.TYPE + "='" + Phone.TYPE_MOBILE + "'";
		Cursor phoneCursor = mContentResolver.query(Phone.CONTENT_URI, new String[]{Phone.NUMBER}, where, null, null);  
		if (Helper.isNotNull(phoneCursor)) {
		    while (phoneCursor.moveToNext()) {
		    	mobileList.add(phoneCursor.getString(0));
		    }
		    phoneCursor.close();
		    phoneCursor = null;
		}
		return mobileList;
	}
	/**
	 * 获取联系人总数
	 * @return
	 */
	public int getContactTotal(boolean isRefresh){
		if(mContactTotalOfBook == -1 || isRefresh){
			getContactTotal();
		}
		return mContactTotalOfBook;
	}
	/**
	 * 恢复通讯录联系人
	 */
	public void recoverContacts(ArrayList<ContactBookBackupJsonEntity> contactRecoverList){
		if(Helper.isEmpty(contactRecoverList)){
			return;
		}
		ContactBookBackupJsonEntity recoverItem = null;
		for (int i = 0, size = contactRecoverList.size(); i < size; i++) {
			recoverItem = contactRecoverList.get(i);
			
			// 获取最新分组数据
			long groupId = addGroup(recoverItem.getSyncGroupName());
			recoverItem.setSyncGroupId((int)groupId);
			// 备份的数据加入到通讯录
			add(recoverItem.getContactEntity());
		}
	}
	/**
	 * 导入覆盖联系人
	 * @param contactList
	 */
	public void replaceContacts(ArrayList<ContactEntity> contactList){
		if(Helper.isNotNull(contactList)){
			for (int i = 0, size = contactList.size(); i < size; i++) {
				ContactEntity contact = contactList.get(i);
				long groupId = addGroup(contact.getGroupName());
				contact.setGroupId((int)groupId);
				
				add(contact);
			}
		}
	}
	// #endregion 通讯录联系人

	//#region 系统短信操作
	/**
	 * 添加短信 到系统表
	 * @param smsEntity
	 */
	public void insertSMS(SMSSendEntity smsEntity){
		 ContentValues values = new ContentValues();
		 //发送时间
		 values.put(DatabaseConstant.SMSColumns.DATE, System.currentTimeMillis());
		 //阅读状态
		 values.put(DatabaseConstant.SMSColumns.READ, smsEntity.getRead());
		 //1为收 2为发
		 values.put(DatabaseConstant.SMSColumns.TYPE, smsEntity.getType());
		 //送达号码
		 values.put(DatabaseConstant.SMSColumns.ADDRESS, smsEntity.getAddress());
		 //送达内容
		 values.put(DatabaseConstant.SMSColumns.BODY, smsEntity.getBody());
		 //插入短信库
		 mContentResolver.insert(Uri.parse(SMSSendEntity.CONTENT_URI),values);
	}
	//#endregion
}
