package com.maya.android.vcard.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.VCardSQLiteDatabase;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.result.UnitMemberResultEntity;
import com.maya.android.vcard.entity.result.UnitResultEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 企业信息 及 成员
 * 
 * @author zheng_cz
 * @since 2014-2-25
 */
public class UnitDao {
	private static final String TAG = UnitDao.class.getSimpleName();

	//region 单例
	private static UnitDao sInstance;
	public static UnitDao getInstance() {
		if (Helper.isNull(sInstance)) {
			sInstance = new UnitDao();
		}
		return sInstance;
	}
	private UnitDao() {
	}
	//endregion 单例

	// region 企业信息
	/**
	 * 传入当前游标,获取对象
	 * 
	 * @param cur
	 * @return
	 */
	private UnitResultEntity getEntity(Cursor cur) {
		UnitResultEntity enterprise = null;
		if (Helper.isNotNull(cur)) {
			long enterpriseId = cur.getLong(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID));
			String headImg = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.HEAD_IMG));
			String address = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.ADDRESS));
			int grade = cur.getInt(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.GRADE));
			String announcement = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.ANNOUNCEMENT));
			int classLabel = cur.getInt(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.CLASS_LABEL));
			String enterpriseName = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.ENTERPRISE_NAME));
			String enterpriseAbout = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.ENTERPRISE_ABOUNT));
			String createTime = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.CREATE_TIME));
			String creater = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.CREATER));
			int memberCount = cur.getInt(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.MEMBER_COUNT));
			long messageGroupId = cur.getLong(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.MESSAGE_GROUP_ID));
			boolean isOpen = ResourceHelper.int2Boolean(cur.getInt(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.IS_OPEN)));
			int approval = cur.getInt(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.APPROVAL));
			int hadMembers = cur.getInt(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.HAD_REQUEST_MEMBER));
			String web = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseColumns.ENTERPRISE_WEB));
			enterprise = new UnitResultEntity();
			enterprise.setAddress(address);
			enterprise.setAnnouncement(announcement);
			enterprise.setApproval(approval);
			enterprise.setClassLabel(classLabel);
			enterprise.setCreater(creater);
			enterprise.setCreateTime(createTime);
			enterprise.setEnterpriseAbout(enterpriseAbout);
			enterprise.setEnterpriseId(enterpriseId);
			enterprise.setEnterpriseName(enterpriseName);
			enterprise.setGrade(grade);
			enterprise.setHeadImg(headImg);
			enterprise.setMemberCount(memberCount);
			enterprise.setMessageGroupId(messageGroupId);
			enterprise.setOpen(isOpen);
			enterprise.setHadRequestMember(hadMembers);
			enterprise.setEnterpriseWeb(web);
		}
		return enterprise;
	}

	/**
	 * 获取contentValues
	 * 
	 * @param enterprise
	 * @return
	 */

	private ContentValues getContentValues(UnitResultEntity enterprise) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID, enterprise.getEnterpriseId());
		values.put(DatabaseConstant.EnterpriseColumns.HEAD_IMG, enterprise.getHeadImg());
		values.put(DatabaseConstant.EnterpriseColumns.ADDRESS, enterprise.getAddress());
		values.put(DatabaseConstant.EnterpriseColumns.GRADE, enterprise.getGrade());
		values.put(DatabaseConstant.EnterpriseColumns.ANNOUNCEMENT, enterprise.getAnnouncement());
		values.put(DatabaseConstant.EnterpriseColumns.CLASS_LABEL, enterprise.getClassLabel());
		values.put(DatabaseConstant.EnterpriseColumns.ENTERPRISE_NAME, enterprise.getEnterpriseName());
		values.put(DatabaseConstant.EnterpriseColumns.ENTERPRISE_ABOUNT, enterprise.getEnterpriseAbout());
		values.put(DatabaseConstant.EnterpriseColumns.CREATE_TIME, enterprise.getCreateTime());
		values.put(DatabaseConstant.EnterpriseColumns.MEMBER_COUNT, enterprise.getMemberCount());
		values.put(DatabaseConstant.EnterpriseColumns.CREATER, enterprise.getCreater());
		values.put(DatabaseConstant.EnterpriseColumns.APPROVAL, enterprise.getApproval());
		values.put(DatabaseConstant.EnterpriseColumns.IS_OPEN, enterprise.isOpen());
		values.put(DatabaseConstant.EnterpriseColumns.MESSAGE_GROUP_ID, enterprise.getMessageGroupId());
		values.put(DatabaseConstant.EnterpriseColumns.HAD_REQUEST_MEMBER, enterprise.getHadRequestMember());
		values.put(DatabaseConstant.EnterpriseColumns.ENTERPRISE_WEB, enterprise.getEnterpriseWeb());
		return values;
	}

	/**
	 * 判断指定的公司是否存在
	 * @param unitId
	 * @return
	 */

	private boolean isExist(long unitId) {
		boolean isExist = false;
		String selSql = "select _id from " + DatabaseConstant.DBTableName.TABLE_ENTERPRISES + " where " + DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + unitId;
		try{
			Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
			if (Helper.isNotNull(cursor)) {
				if (cursor.getCount() > 0) {
					isExist = true;
				}
				cursor.close();
			}
		}catch(NullPointerException e){
			LogHelper.e(TAG, "数据库对象未创建");
		}
		return isExist;
	}

	/**
	 * 添加企业信息
	 * 
	 * @param enterprise
	 * @return
	 */

	private int insert(UnitResultEntity enterprise) {
		ContentValues values = getContentValues(enterprise);
		try{
			return (int) this.getSQLiteDatabase().insert(DatabaseConstant.DBTableName.TABLE_ENTERPRISES, null, values);
		}catch(NullPointerException e){
			LogHelper.e(TAG, "数据库对象未创建");
			return 0;
		}
	}
	/**
	 * 添加单位列表
	 * @param enterpriseList
	 */
	public void add(ArrayList<UnitResultEntity> enterpriseList){
		if(Helper.isNull(enterpriseList)){
			return;
		}
		for (int i = 0, size = enterpriseList.size(); i < size; i++) {
			add(enterpriseList.get(i));
		}
	}
	/**
	 * 添加单位信息（若存在则修改）
	 * 
	 * @param enterprise
	 * @return
	 */

	public int add(UnitResultEntity enterprise) {
		if(Helper.isNull(enterprise)){
			return -1;
		}
		boolean isExist = isExist(enterprise.getEnterpriseId());
		if (isExist) {
			return update(enterprise);
		} else {
			return insert(enterprise);
		}
	}

	/**
	 * 改
	 * 
	 * @param enterprise
	 * @return
	 */

	public int update(UnitResultEntity enterprise) {
		ContentValues values = getContentValues(enterprise);
		String where = DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterprise.getEnterpriseId();
		try{
			return this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_ENTERPRISES, values, where, null);
		}catch(NullPointerException e){
			LogHelper.e(TAG, "数据库对象未创建");
			return 0;
		}
		
	}

	/**
	 * 删除
	 * 
	 * @param enterpriseId
	 * @return
	 */

	public int delete(long enterpriseId) {
		String where = DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId;
		return this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_ENTERPRISES, where, null);
	}
	/**
	 * 删除所有
	 * @return
	 */
	public int deleteAll(){
		try {
			return this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_ENTERPRISES, null, null);
		} catch (Exception e) {
			return 0;
		}
		
	}

	/**
	 * 根据企业id 获取对象
	 * @param enterpriseId
	 * @return
	 */

	public UnitResultEntity getEntity(long enterpriseId) {
		Cursor cursor;
		UnitResultEntity enterprise = null;
		String selSql = "select * from " + DatabaseConstant.DBTableName.TABLE_ENTERPRISES + " where "
				+ DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId;
		try {
			cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		} catch (Exception e) {
			cursor = null;
		}
		if (Helper.isNotNull(cursor)) {
			if (cursor.moveToFirst()) {
				enterprise = getEntity(cursor);
			}
			cursor.close();
			cursor = null;
		}
		return enterprise;
	}

	/**
	 * 根据企业名称 获取对象
	 * 
	 * @param enterpriseName
	 * @return
	 */

	public UnitResultEntity getEntity(String enterpriseName) {
		UnitResultEntity enterprise = null;
		if(ResourceHelper.isEmpty(enterpriseName)){
			return enterprise;
		}
		String selSql = "select * from " + DatabaseConstant.DBTableName.TABLE_ENTERPRISES + " where "
				+ DatabaseConstant.EnterpriseColumns.ENTERPRISE_NAME + "='" + enterpriseName + "'";
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		if (Helper.isNotNull(cursor)) {
			if (cursor.moveToFirst()) {
				enterprise = getEntity(cursor);
			}
			cursor.close();
			cursor = null;
		}
		return enterprise;
	}

	/**
	 * 根据群聊组id 获取单位信息
	 * @param messageGroupId
	 * @return
	 */
	public UnitResultEntity getEntityByMsgGroupId(long messageGroupId) {

		UnitResultEntity companyInfoEntity = null;
		String selSql = "select * from " + DatabaseConstant.DBTableName.TABLE_ENTERPRISES + " where "
				+ DatabaseConstant.EnterpriseColumns.MESSAGE_GROUP_ID + "=" + messageGroupId;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		if (Helper.isNotNull(cursor)) {
			if (cursor.moveToFirst()) {
				companyInfoEntity = getEntity(cursor);
			}
			cursor.close();
			cursor = null;
		}
		return companyInfoEntity;
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */

	public ArrayList<UnitResultEntity> getAllUnit() {
		ArrayList<UnitResultEntity> enterpriseList = null;
		String sql = "select * from " + DatabaseConstant.DBTableName.TABLE_ENTERPRISES;
		Cursor cur = this.getSQLiteDatabase().rawQuery(sql, null);
		if(Helper.isNotNull(cur)){
			enterpriseList = new ArrayList<UnitResultEntity>();
			UnitResultEntity enterprise = null;
			while(cur.moveToNext()){
				enterprise = getEntity(cur);
				enterpriseList.add(enterprise);
			}
			cur.close();
		}
		return enterpriseList;
	}

	/**
	 * 通过单位id获取认证状态
	 * 
	 * @param enterpriseId
	 * @return
	 */

	public int getApproval(long enterpriseId) {
		String selSql = "select " + DatabaseConstant.EnterpriseColumns.APPROVAL + " from " + DatabaseConstant.DBTableName.TABLE_ENTERPRISES
				+ " where " + DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId;
		int approval = -1;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		if (Helper.isNotNull(cursor)) {
			if (cursor.moveToNext()) {
				approval = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.EnterpriseColumns.APPROVAL));
			}
			cursor.close();
			cursor = null;
		}
		return approval;
	}
	/**
	 * 根据企业id 获取讨论组id
	 * @param enterpriseId
	 * @return
	 */
	public long getMessageGroupId(long enterpriseId){
		long groupId = 0;
		String selSql = "select " + DatabaseConstant.EnterpriseColumns.MESSAGE_GROUP_ID + " from " + DatabaseConstant.DBTableName.TABLE_ENTERPRISES
				+ " where " + DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		if (Helper.isNotNull(cursor)) {
			if (cursor.moveToFirst()) {
				groupId = cursor.getLong(0);
			}
			cursor.close();
			cursor = null;
		}
		return groupId;
	}
	/**
	 * 获取 已请求成员次数
	 * @param enterpriseId
	 * @return
	 */
	public int getHadRequestMember(long enterpriseId){
		int hadCount = 0;
		String selSql = "select " + DatabaseConstant.EnterpriseColumns.HAD_REQUEST_MEMBER + " from " + DatabaseConstant.DBTableName.TABLE_ENTERPRISES
				+ " where " + DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId ;
		try{
			Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
			if (Helper.isNotNull(cursor)) {
				if (cursor.moveToNext()) {
					hadCount = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.EnterpriseColumns.APPROVAL));
				}
				cursor.close();
				cursor = null;
			}
		}catch(NullPointerException e){
			LogHelper.e(TAG, "数据库对象未创建");
		}
		return hadCount;
	}
	/**
	 * 更新成员请求次数
	 * @return
	 */
	public void updateHadRequestMember(long enterpriseId){
		StringBuilder upSb = new StringBuilder(" update ");
		upSb.append(DatabaseConstant.DBTableName.TABLE_ENTERPRISES)
			.append(" set ")
			.append(DatabaseConstant.EnterpriseColumns.HAD_REQUEST_MEMBER)
			.append(" = ")
			.append(DatabaseConstant.EnterpriseColumns.HAD_REQUEST_MEMBER + " +1 ")
			.append(" where ")
			.append( DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID).append("=")
			.append(enterpriseId);
		this.getSQLiteDatabase().execSQL(upSb.toString());
	}
	/**
	 * 编辑公告
	 * 
	 * @param enterpriseId
	 * @param announce
	 * @return
	 */

	public int updateAnnounce(long enterpriseId, String announce) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.EnterpriseColumns.ANNOUNCEMENT, announce);
		String where = DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId;
		return this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_ENTERPRISES, values, where, null);
	}
	/**
	 * 编辑单位地址
	 * @param enterpriseId
	 * @param address
	 * @return
	 */

	public int updateAddress(long enterpriseId, String address) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.EnterpriseColumns.ADDRESS, address);
		String where = DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId;
		return this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_ENTERPRISES, values, where, null);
	}
	/**
	 * 编辑单位网址
	 * 
	 * @param enterpriseId
	 * @param weburl
	 * @return
	 */

	public int updateEnterpriseWeb(long enterpriseId, String weburl) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.EnterpriseColumns.ENTERPRISE_WEB, weburl);
		String where = DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId;
		return this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_ENTERPRISES, values, where, null);
	}
	/**
	 * 编辑认证状态
	 * @param enterpriseId
	 * @param approval
	 * @return
	 */

	public int updateApproval(long enterpriseId, int approval) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.EnterpriseColumns.APPROVAL, approval);
		String where = DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId;
		return this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_ENTERPRISES, values, where, null);
	}

	/**
	 * 编辑单位简介
	 * 
	 * @param enterpriseId
	 * @param introduction
	 * @return
	 */

	public int updateAbout(long enterpriseId, String introduction) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.EnterpriseColumns.ENTERPRISE_ABOUNT, introduction);
		String where = DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId;
		return this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_ENTERPRISES, values, where, null);
	}

	/**
	 * 编辑单位群聊id
	 * 
	 * @param enterpriseId
	 * @param messageGroupId
	 * @return
	 */

	public int updateMessageGroupId(long enterpriseId, long messageGroupId) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.EnterpriseColumns.MESSAGE_GROUP_ID, messageGroupId);
		String where = DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId;
		return this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_ENTERPRISES, values, where, null);
	}

	// endregion

	// region 企业成员信息
	/**
	 * 传入当前游标,获取对象
	 * 
	 * @param cur
	 * @return
	 */

	private UnitMemberResultEntity getMemberEntity(Cursor cur) {
		UnitMemberResultEntity member = null;
		if (Helper.isNotNull(cur)) {
			long enterpriseId = cur.getLong(cur.getColumnIndex(DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID));
			long memberId = cur.getLong(cur.getColumnIndex(DatabaseConstant.EnterpriseMemberColumns.MEMBER_ID));
			int role = cur.getInt(cur.getColumnIndex(DatabaseConstant.EnterpriseMemberColumns.ROLE));
			long accountId = cur.getLong(cur.getColumnIndex(DatabaseConstant.EnterpriseMemberColumns.ACCOUNT_ID));
			String displayName = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseMemberColumns.DISPLAY_NAME));
			String nickName = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseMemberColumns.NICK_NAME));
			long cardId = cur.getLong( cur.getColumnIndex(DatabaseConstant.EnterpriseMemberColumns.CARD_ID));
			String description = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseMemberColumns.DESPRITION));
			String headImg = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseMemberColumns.HEAD_IMG));
			String job = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseMemberColumns.JOB));
			String vcardNo = cur.getString(cur.getColumnIndex(DatabaseConstant.EnterpriseMemberColumns.VCARD_NO));
			
			member = new UnitMemberResultEntity();
			member.setAccountId(accountId);
			member.setCardId(cardId);
			member.setDescription(description);
//			member.setDisplayName(displayName);
			member.setEnterpriseId(enterpriseId);
			member.setNickName(nickName);
			member.setHeadImg(headImg);
			member.setJob(job);
//			member.setVcardNo(vcardNo);
//			member.setMemberId(memberId);
			member.setRole(role);
			
		}
		return member;
	}

	/**
	 * 获取contentValues
	 * @param member
	 * @return
	 */

	private ContentValues getMemberValues(UnitMemberResultEntity member) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID, member.getEnterpriseId());
//		values.put(DatabaseConstant.EnterpriseMemberColumns.MEMBER_ID, member.getMemberId());
		values.put(DatabaseConstant.EnterpriseMemberColumns.ROLE, member.getRole());
		values.put(DatabaseConstant.EnterpriseMemberColumns.ACCOUNT_ID, member.getAccountId());
		values.put(DatabaseConstant.EnterpriseMemberColumns.DISPLAY_NAME, member.getDisplayName());
		values.put(DatabaseConstant.EnterpriseMemberColumns.NICK_NAME, member.getNickName());
		values.put(DatabaseConstant.EnterpriseMemberColumns.CARD_ID, member.getCardId());
		values.put(DatabaseConstant.EnterpriseMemberColumns.DESPRITION, member.getDescription());
		values.put(DatabaseConstant.EnterpriseMemberColumns.HEAD_IMG, member.getHeadImg());
		values.put(DatabaseConstant.EnterpriseMemberColumns.JOB, member.getJob());
//		values.put(DatabaseConstant.EnterpriseMemberColumns.VCARD_NO, member.getVcardNo());
		return values;
	}

	/**
	 * 该企业下是否已经存在成员
	 * 
	 * @param enterpriseId
	 * @param cardId
	 * @return
	 */

	private boolean isExistMember(long enterpriseId, long cardId) {
		boolean isExist = false;
		String selSql = "select member_id from " + DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS + " where enterprise_id=" + enterpriseId
				+ " and " + DatabaseConstant.EnterpriseMemberColumns.CARD_ID + "=" + cardId;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		if (Helper.isNotNull(cursor)) {
			if (cursor.getCount() > 0) {
				isExist = true;
			}
			cursor.close();
			cursor = null;
		}
		return isExist;
	}

	/**
	 * 插入成员
	 * 
	 * @param member
	 * @return
	 */

	private int insertMember(UnitMemberResultEntity member) {
		ContentValues values = getMemberValues(member);
		return (int) this.getSQLiteDatabase().insert(DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS, null, values);
	}

	/**
	 * 根据权限获取企业下的成员列表
	 * 
	 * @param enterpriseId
	 * @param role  -1 获取全部, -2 获取非普通成员
	 * @param isOrderRole 是否按权限排序
	 * @return
	 */

	private ArrayList<UnitMemberResultEntity> getMemberList(long enterpriseId, int role, boolean isOrderRole) {
		ArrayList<UnitMemberResultEntity> memberList = null;
		String selSql = "select * from " + DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS + " where "
				+ DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID + "=" + enterpriseId;
		if (role > -1) {
			selSql += " and " + DatabaseConstant.EnterpriseMemberColumns.ROLE + "=" + role;
		}else if(role == -2){
			selSql += " and " + DatabaseConstant.EnterpriseMemberColumns.ROLE + ">" + DatabaseConstant.Role.MEMBER;
		}
		if(isOrderRole){
			selSql.concat(" order by ").concat(DatabaseConstant.EnterpriseMemberColumns.ROLE).concat(" desc "); 
		}
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		if (Helper.isNotNull(cursor)) {
			memberList = new ArrayList<UnitMemberResultEntity>();
			UnitMemberResultEntity member = null;
			while (cursor.moveToNext()) {
				member = getMemberEntity(cursor);
				memberList.add(member);
			}
			cursor.close();
			cursor = null;
		}
		return memberList;
	}

	/**
	 * 添加企业成员 （若存在则修改）
	 * 
	 * @param member
	 * @return
	 */

	public int addMember(UnitMemberResultEntity member) {
		if(Helper.isNull(member)){
			return -1;
		}
		boolean isExist = isExistMember(member.getEnterpriseId(), member.getCardId());
		if (isExist) {
			return updateMember(member);
		} else {
			return insertMember(member);
		}
	}

	/**
	 * 添加成员列表
	 * 
	 * @param memberList
	 */

	public void addMember(long enterpriseId, ArrayList<UnitMemberResultEntity> memberList) {
		if (Helper.isEmpty(memberList)) {
			return;
		}
		StringBuilder  cardIds = new StringBuilder();
		for (int i = 0, size = memberList.size(); i < size; i++) {
			UnitMemberResultEntity member = memberList.get(i);
			addMember(member);
			cardIds.append(member.getCardId()).append(",");
		}
		if(Helper.isNotEmpty(cardIds)){
			deleteMember(enterpriseId, cardIds.substring(0, cardIds.length()-1));
		}
	}

	/**
	 * 更改成员信息
	 * @param member
	 * @return
	 */

	public int updateMember(UnitMemberResultEntity member) {
		ContentValues values = getMemberValues(member);
		String where = DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID + "=" + member.getEnterpriseId() + " and "
				+ DatabaseConstant.EnterpriseMemberColumns.CARD_ID + "=" + member.getCardId();
		return this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS, values, where, null);
	}

	/**
	 * 更改企业下的成语权限
	 * 
	 * @param enterpriseId
	 * @param memberId
	 * @param role
	 * @return
	 */

	public int updateMemberRole(long enterpriseId, long memberId, int role) {
		String where = DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID + " =" + enterpriseId + " and "
				+ DatabaseConstant.EnterpriseMemberColumns.MEMBER_ID + "=" + memberId;
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.EnterpriseMemberColumns.ROLE, role);
		return this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS, values, where, null);
	}

	/**
	 * 根据单位id 获取t
	 * 
	 * @param enterpriseId
	 * @param isOrderRole 是否按权限排序
	 * @return
	 */

	public ArrayList<UnitMemberResultEntity> getMemberList(long enterpriseId, boolean isOrderRole) {
		ArrayList<UnitMemberResultEntity> companyMemberEntityList = getMemberList(enterpriseId, -1, isOrderRole);
		return companyMemberEntityList;
	}
	/**
	 * 按权限获取列表
	 * @param enterpriseId
	 * @param role role  -1 获取全部, -2 获取非普通成员
	 * @return
	 */
	public ArrayList<UnitMemberResultEntity> getMemberList(long enterpriseId, int role){
		ArrayList<UnitMemberResultEntity> companyMemberEntityList = getMemberList(enterpriseId, role, false);
		return companyMemberEntityList;
	}

	/**
	 * 排除传入的名片的成员列表
	 * 
	 * @param enterpriseId
	 * @param cardId 排除的成员名片id
	 * @return
	 */

	public ArrayList<UnitMemberResultEntity> getMemberList(long enterpriseId, long cardId) {
		ArrayList<UnitMemberResultEntity> memberList = null;
		String where = " where ".concat(DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID).concat("=" + enterpriseId);
		if(cardId > 0){
			where.concat(" and ").concat(DatabaseConstant.EnterpriseMemberColumns.CARD_ID).concat(" <>" + cardId);
		}
		String selSql = "select * from ".concat(DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS).concat(where);
		
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		if (Helper.isNotNull(cursor)) {
			memberList = new ArrayList<UnitMemberResultEntity>();
			UnitMemberResultEntity member = null;
			while (cursor.moveToNext()) {
				member = getMemberEntity(cursor);
				memberList.add(member);
			}
			cursor.close();
			cursor = null;
		}
		return memberList;
	}

	/**
	 * 根据名片id 获取对象
	 * 
	 * @param enterpriseId
	 * @param cardId
	 * @return
	 */

	public UnitMemberResultEntity getMemberEntity(long enterpriseId, long cardId) {
		UnitMemberResultEntity companyMemberEntity = null;
		String selSql = "select * from " + DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS + " where "
				+ DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID + "=" + enterpriseId + " and "
				+ DatabaseConstant.EnterpriseMemberColumns.CARD_ID + "=" + cardId;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		if (Helper.isNotNull(cursor)) {
			if (cursor.moveToFirst()) {
				companyMemberEntity = getMemberEntity(cursor);
			}
			cursor.close();
			cursor = null;
		}
		return companyMemberEntity;
	}

	/**
	 * 删除企业所有成员 或企业的某个成员
	 * 
	 * @param enterpriseId
	 * @param cardId
	 *            为0则删除企业的所有成员
	 */

	public void deleteMember(long enterpriseId, long cardId) {
		if (enterpriseId == 0) {
			return;
		}

		String where = DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID + "=" + enterpriseId;
		if (cardId > 0) {
			where += " and " + DatabaseConstant.EnterpriseMemberColumns.CARD_ID + "=" + cardId;
		}
		int delRow = this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS, where, null);
		if (delRow > 0) {
			this.getSQLiteDatabase().execSQL("update " + DatabaseConstant.DBTableName.TABLE_ENTERPRISES + " set "
					+ DatabaseConstant.EnterpriseColumns.MEMBER_COUNT + "=" + DatabaseConstant.EnterpriseColumns.MEMBER_COUNT + "-" + delRow
					+ " where " + DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId);
		}
	} 
	/**
	 * 批量删除成员
	 * @param enterpriseId
	 * @param memberIdList
	 */
	public void deleteMember(long enterpriseId, ArrayList<Long> memberIdList){
		if (enterpriseId == 0) {
			return;
		}

		String where = DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID + "=" + enterpriseId;
		if (Helper.isNotNull(memberIdList)) {
			StringBuilder memberIds = new StringBuilder();
			for (int i = 0, size = memberIdList.size(); i < size; i++) {
				memberIds.append(memberIdList.get(i)+",");
			}
			if(Helper.isNotEmpty(memberIds)){
				where += " and " + DatabaseConstant.EnterpriseMemberColumns.MEMBER_ID + " in (" + memberIds.substring(0, memberIds.length()-1) + ")";
			}
		}
		int delRow = this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS, where, null);
		if (delRow > 0) {
			this.getSQLiteDatabase().execSQL("update " + DatabaseConstant.DBTableName.TABLE_ENTERPRISES + " set "
					+ DatabaseConstant.EnterpriseColumns.MEMBER_COUNT + "=" + DatabaseConstant.EnterpriseColumns.MEMBER_COUNT + "-" + delRow
					+ " where " + DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId);
		}
	}
	/**
	 * 删除名片 (排除传入的名片)
	 * @param cardIds “,”分割的 id串
	 */
	public void deleteMember( long enterpriseId, String cardIds){
		if(Helper.isNotEmpty(cardIds)){
			String where = DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID + "=" + enterpriseId + " and " + DatabaseConstant.EnterpriseMemberColumns.CARD_ID + " not in( " + cardIds + ")";
			int delRow = this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS, where, null);
			if (delRow > 0) {
				this.getSQLiteDatabase().execSQL("update " + DatabaseConstant.DBTableName.TABLE_ENTERPRISES + " set "
						+ DatabaseConstant.EnterpriseColumns.MEMBER_COUNT + "=" + DatabaseConstant.EnterpriseColumns.MEMBER_COUNT + "-" + delRow
						+ " where " + DatabaseConstant.EnterpriseColumns.ENTERPRISE_ID + "=" + enterpriseId);
			}
		}
	}
	/**
	 * 获取管理员的名字
	 * @param concatSign 名字之间的拼接符号
	 * @return
	 */
	public String getAdminNames(long enterpriseId, String concatSign){
		StringBuilder nameBuild = new StringBuilder();
		String selection = DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID + "=" + enterpriseId + " and " + DatabaseConstant.EnterpriseMemberColumns.ROLE + "=" + DatabaseConstant.Role.ADMIN;
		Cursor cursor = this.getSQLiteDatabase().query(DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS, new String[]{DatabaseConstant.EnterpriseMemberColumns.DISPLAY_NAME}
			, selection, null, null, null, null);
		if(Helper.isNotNull(cursor)){
			while(cursor.moveToNext()){
				nameBuild.append(cursor.getString(0)).append(concatSign);
			}
			cursor.close();
			cursor = null;
		}
		if(Helper.isNotEmpty(nameBuild)){
			return nameBuild.substring(0, nameBuild.length()-1);
		}
		return "";
	}
	/**
	 * 名片是否为企业管理员
	 * @param cardId
	 * @return
	 */
	public boolean isAdmin(long enterpriseId, long cardId){
		boolean admin = false;
		String selection = DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID.concat("=").concat(enterpriseId + " and ").concat( DatabaseConstant.EnterpriseMemberColumns.ROLE)
				.concat("=" + DatabaseConstant.Role.ADMIN).concat(" and ").concat( DatabaseConstant.EnterpriseMemberColumns.CARD_ID).concat( "=" + cardId);
		String sql = " select * from " + DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS + " where " + selection;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
		if(Helper.isNotNull(cursor)){
			if(cursor.getCount() > 0){
				admin = true;
			}
			cursor.close();
			cursor = null;
		}
		return admin;
	}
	/**
	 * 判断我的名片是否是该企业的管理员
	 * @return
	 */
	public boolean isAdminForMe(long enterpriseId){
		boolean admin = false;
		ArrayList<CardEntity> myCardList = CurrentUser.getInstance().getVCardEntityList();
		if(Helper.isNotNull(myCardList)){
			String cardIds = "";
			for (int i = 0, size = myCardList.size(); i < size; i++) {
				CardEntity cardItem = myCardList.get(i);
				cardIds += cardItem.getId() + ",";
			}
			if(Helper.isNotEmpty(cardIds)){

				String selection = DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID.concat("=").concat(enterpriseId + " and ").concat( DatabaseConstant.EnterpriseMemberColumns.ROLE)
						.concat("=" + DatabaseConstant.Role.ADMIN).concat(" and ").concat( DatabaseConstant.EnterpriseMemberColumns.CARD_ID)
						.concat( " in (").concat(cardIds.substring(0, cardIds.length()-1)).concat(")");
				String sql = " select * from ".concat(DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS).concat(" where ").concat(selection);
				Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
				if(Helper.isNotNull(cursor)){
					if(cursor.getCount() > 0){
						admin = true;
					}
					cursor.close();
				}
			}
		}
		return admin;
	}
	// endregion

	private SQLiteDatabase getSQLiteDatabase(){
		return VCardSQLiteDatabase.getInstance().getSQLiteDatabase();
	}
}
