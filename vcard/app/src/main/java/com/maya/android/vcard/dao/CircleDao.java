package com.maya.android.vcard.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.VCardSQLiteDatabase;
import com.maya.android.vcard.entity.ContactListItemEntity;
import com.maya.android.vcard.entity.json.CircleGroupJsonEntity;
import com.maya.android.vcard.entity.json.CircleGroupMemberJsonEntity;
import com.maya.android.vcard.entity.result.CircleGroupMemberResultEntity;
import com.maya.android.vcard.entity.result.CircleGroupResultEntity;
import com.maya.android.vcard.entity.result.UnitMemberResultEntity;
import com.maya.android.vcard.entity.result.UnitResultEntity;
import com.maya.android.vcard.entity.result.MessageResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 群聊组,及成员管理类
 * @author zheng_cz
 * @since 2014-3-31 上午11:08:49
 */
public class CircleDao {

	private static final String TAG = CircleDao.class.getSimpleName();
	/**
	 * 群聊查询字段
	 */
	private static final String[] GROUP_PROJECTION = new String[]{DatabaseConstant.CircleGroupColumns.GROUP_ID
							,DatabaseConstant.CircleGroupColumns.GROUP_NAME,DatabaseConstant.CircleGroupColumns.HEAD_IMG
							,DatabaseConstant.CircleGroupColumns.MEMBER_COUNT,DatabaseConstant.CircleGroupColumns.CREATE_TIME
							,DatabaseConstant.CircleGroupColumns.ACCOUNT_ID,DatabaseConstant.CircleGroupColumns.TYPE
							,DatabaseConstant.CircleGroupColumns.REMARK, DatabaseConstant.CircleGroupColumns.CARD_ID
							,DatabaseConstant.CircleGroupColumns.HAD_REQUEST_MEMBER, DatabaseConstant.CircleGroupColumns.DISPLAY_NAME};
	
	/**
	 * 群组成员查询字段
	 */
	private static final String[] MEMBER_PROJECTION = new String[]{DatabaseConstant.CircleGroupMemberColumns.MEMBER_ID
							,DatabaseConstant.CircleGroupMemberColumns.ACCOUNT_ID,DatabaseConstant.CircleGroupMemberColumns.CARD_ID
							,DatabaseConstant.CircleGroupMemberColumns.NICK_NAME,DatabaseConstant.CircleGroupMemberColumns.ROLE
							,DatabaseConstant.CircleGroupMemberColumns.GROUP_ID,DatabaseConstant.CircleGroupMemberColumns.DISPLAY_NAME
							,DatabaseConstant.CircleGroupMemberColumns.HEAD_IMG};

	private Context mContext = ActivityHelper.getGlobalApplicationContext();

	//region 单例
	private static CircleDao sInstance;
	public static CircleDao getInstance(){
		if(Helper.isNull(sInstance)){
			sInstance = new CircleDao();
		}
		return sInstance;
	}
	private CircleDao(){
	}
	//endregion 单例
		
	//#region 圈子,组 对象
	/**
	 * 是否已经存在该分组信息
	 * @param groupId
	 * @return
	 */
	public boolean isExist(long groupId){
		if(groupId < 1){
			return false;
		}
		boolean result = false;
		String selSql = " select " + DatabaseConstant.CircleGroupColumns.GROUP_NAME + " from " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS 
				+ " where " + DatabaseConstant.CircleGroupColumns.GROUP_ID + "=" + groupId;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		if(Helper.isNotNull(cursor)){
			if(cursor.getCount()>0){
				result = true;
			}
			cursor.close();
			cursor = null;
		}
		return result;
	}

	/**
	 * 插入圈子 组对象
	 * @param groupEntity
	 * @return
	 */
	private boolean insert(CircleGroupResultEntity groupEntity){
		if(Helper.isNull(groupEntity)){
			return false;
		}
		try{
			String inSql = new StringBuilder().append(" insert into ").append( DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS).append(" (")
					.append(DatabaseConstant.CircleGroupColumns.GROUP_ID).append(",")
					.append(DatabaseConstant.CircleGroupColumns.GROUP_NAME).append(",") 
					.append(DatabaseConstant.CircleGroupColumns.MEMBER_COUNT).append(",")
					.append(DatabaseConstant.CircleGroupColumns.CREATE_TIME).append(",") 
					.append(DatabaseConstant.CircleGroupColumns.ACCOUNT_ID).append(",")
					.append(DatabaseConstant.CircleGroupColumns.CARD_ID).append(",")
					.append(DatabaseConstant.CircleGroupColumns.HEAD_IMG).append(",")
					.append(DatabaseConstant.CircleGroupColumns.TYPE).append(",")
					.append(DatabaseConstant.CircleGroupColumns.REMARK).append(",")
					.append(DatabaseConstant.CircleGroupColumns.DISPLAY_NAME).append(",")
					.append(DatabaseConstant.CircleGroupColumns.HAD_REQUEST_MEMBER)
					.append(") values(?,?,?,?,?,?,?,?,?,?,?);")
					.toString();
			
			Object[] bindArgs = new Object[]{
						groupEntity.getGroupId(), groupEntity.getGroupName(), groupEntity.getMemberCount()
						,groupEntity.getCreateTime(), groupEntity.getAccountId(), groupEntity.getCardId()
						,groupEntity.getHeadImg(), groupEntity.getType(), groupEntity.getRemark()
						,groupEntity.getDisplayName(),groupEntity.getHadRequestMember()
					};
			this.getSQLiteDatabase().execSQL(inSql, bindArgs);
			return true;
		}catch(Exception ex){
			LogHelper.e(TAG, "插入群聊组异常");
			return false;
		}
	}

	/**
	 * 修改群组信息
	 * @param groupEntity
	 */
	private void update(CircleGroupResultEntity groupEntity){
		String upSql = " update " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS + " set " +DatabaseConstant.CircleGroupColumns.MEMBER_COUNT + "=" + groupEntity.getMemberCount()
					+ "," + DatabaseConstant.CircleGroupColumns.GROUP_NAME + "='" + groupEntity.getGroupName() 
					+ "', " + DatabaseConstant.CircleGroupColumns.TYPE + "=" + groupEntity.getType()
					+ " where " + DatabaseConstant.CircleGroupColumns.GROUP_ID + "=" + groupEntity.getGroupId();
		this.getSQLiteDatabase().execSQL(upSql);
	}
	/**
	 * 添加群组
	 * @param groupEntity
	 */
	public void add(CircleGroupResultEntity groupEntity){
		if(Helper.isNotNull(groupEntity)){
			boolean isExist = isExist(groupEntity.getGroupId());
			if(isExist){
				update(groupEntity);
			}else{
				insert(groupEntity);
			}
		}
	}
	/**
	 * 添加聊天组集合
	 * @param groupList
	 */
	public void add(ArrayList<CircleGroupResultEntity> groupList){
		if(Helper.isNotNull(groupList)){
			for (int i = 0, size = groupList.size(); i < size; i++) {
				add(groupList.get(i));
			}
		}
	}
	/**
	 * 根据发起的群聊信息创建群组
	 * @param groupId
	 * @param groupJsonEntity
	 */
	public void add(long groupId, CircleGroupJsonEntity groupJsonEntity){
		boolean isExist = isExist(groupId);
		if(!isExist){
			UserInfoResultEntity myInfo = CurrentUser.getInstance().getUserInfoEntity();
			CircleGroupResultEntity groupEntity = new CircleGroupResultEntity();
			groupEntity.setGroupId(groupId);
			groupEntity.setCardId(groupJsonEntity.getCardId());
			groupEntity.setCreateTime(ResourceHelper.getNowTime());
			groupEntity.setAccountId(myInfo.getId());
			groupEntity.setDisplayName(myInfo.getDisplayName());
			groupEntity.setHadRequestMember(1);
			if(groupJsonEntity.getEnterpriseId() > 0){
				// 单位群聊
				groupEntity.setType(DatabaseConstant.CircleGroupType.CIRCLE_COMPANY);
				UnitResultEntity company = UnitDao.getInstance().getEntity(groupJsonEntity.getEnterpriseId());
				groupEntity.setGroupName(company.getEnterpriseName());
				groupEntity.setMemberCount(company.getMemberCount());
			}else{
				//讨论组
				groupEntity.setType(DatabaseConstant.CircleGroupType.GROUP);
				groupEntity.setGroupName(mContext.getString(R.string.group_chat));
				groupEntity.setMemberCount(groupJsonEntity.getMemberList().size());
			}
			
			boolean isSuccess = insert(groupEntity);
			if(isSuccess){
				// 组创建成功 则添加成员列表
				if(groupJsonEntity.getEnterpriseId() > 0){
					addMember(groupId, groupJsonEntity.getCardId(), groupJsonEntity.getEnterpriseId());
				}else{
					addMember(groupId, groupJsonEntity.getCardId(), groupJsonEntity.getMemberList());
				}
				
			}
			
		}
	}
	/**
	 * 根据 通知添加讨论组
	 */
	public void add(MessageResultEntity message){
		CircleGroupResultEntity groupEntity = new CircleGroupResultEntity();
		groupEntity.setGroupId(message.getTagId());
		groupEntity.setCardId(message.getFromCardId());
		groupEntity.setCreateTime(message.getCreateTime());
		groupEntity.setAccountId(message.getFromAccountId());
		groupEntity.setMemberCount(message.getResult());
		groupEntity.setRemark(message.getBody());
		groupEntity.setDisplayName(message.getFromName());
		
		add(groupEntity);
	}
	/**
	 * 获取组显示名称
	 * @param groupId
	 * @param isSessionTitle 是否会话标题 帯人数 例： 群聊 (20人)
	 * @return
	 */
	public String getShowName(long groupId, boolean isSessionTitle){
		String showName = null;
		String where = DatabaseConstant.CircleGroupColumns.GROUP_ID + "=" + groupId;
	
		Cursor cursor = this.getSQLiteDatabase().query(DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS
					, new String[]{DatabaseConstant.CircleGroupColumns.GROUP_NAME, DatabaseConstant.CircleGroupColumns.MEMBER_COUNT}
					, where, null, null, null, null);
		if(Helper.isNotNull(cursor)){
			if(cursor.moveToFirst()){
				String name = cursor.getString(cursor.getColumnIndex(DatabaseConstant.CircleGroupColumns.GROUP_NAME));
				
				if(isSessionTitle){
					int memberCount = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.CircleGroupColumns.MEMBER_COUNT));
					showName = mContext.getString(R.string.group_title, name, memberCount);
				}else{
					showName = name;
				}
			}
			cursor.close();
			cursor = null;
		}
		return showName;
	}
	
	/**
	 * 获取讨论组类型
	 * @param groupId
	 * @return
	 */
	public int getGroupType(long groupId){
		int groupType = DatabaseConstant.CircleGroupType.GROUP;
		String where = DatabaseConstant.CircleGroupColumns.GROUP_ID + "=" + groupId;
	
		Cursor cursor = this.getSQLiteDatabase().query(DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS, new String[]{DatabaseConstant.CircleGroupColumns.TYPE}, where, null, null, null, null);
		if(Helper.isNotNull(cursor)){
			if(cursor.moveToFirst()){
				groupType = cursor.getInt(0);
			}
			cursor.close();
			cursor = null;
		}
		return groupType;
	}
	/**
	 * 更新群名称
	 * @param groupId
	 * @param groupName
	 */
	public void updateName(long groupId,String groupName){
		String upSql = " update " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS + " set " +DatabaseConstant.CircleGroupColumns.GROUP_NAME + " = '" + groupName + "'";
		String where = " where " + DatabaseConstant.CircleGroupColumns.GROUP_ID + "=" + groupId;
		try{
			this.getSQLiteDatabase().execSQL(upSql + where);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * 更改备注
	 * @param groupId
	 * @param remark
	 */
	public void updateRemark(long groupId, String remark){
		String upSql = " update " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS + " set " + 
				DatabaseConstant.CircleGroupColumns.REMARK + " = '" + remark + "'";
		String where = " where " + DatabaseConstant.CircleGroupColumns.GROUP_ID + "=" + groupId;
		try{
			this.getSQLiteDatabase().execSQL(upSql + where);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * 删除群聊组
	 * @param groupId
	 * @return
	 */
	public boolean delete(long groupId){
		boolean del = false;
		if(groupId < 1){
			return del;
		}
		int delRow = this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS, DatabaseConstant.CircleGroupColumns.GROUP_ID + "=" + groupId, null);
		if(delRow > 0){
			// 删除该组下的成员
			deleteMember(groupId);
			del = true;
		}
		return del;
	}
	
	/**
	 * 获取群聊组对象
	 * @param groupId
	 * @return
	 */
	public CircleGroupResultEntity getEntity(long groupId){
		CircleGroupResultEntity groupItem = null;
		String where = DatabaseConstant.CircleGroupColumns.GROUP_ID + "=" +groupId;
		try{
			Cursor cursor = this.getSQLiteDatabase().query(DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS, GROUP_PROJECTION, where, null, null, null, null);
			if(Helper.isNotNull(cursor)){
				if(cursor.moveToFirst()){
					String grpName = cursor.getString(cursor.getColumnIndex(DatabaseConstant.CircleGroupColumns.GROUP_NAME));
					String headImg = cursor.getString(cursor.getColumnIndex(DatabaseConstant.CircleGroupColumns.HEAD_IMG));
					int memberCount = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.CircleGroupColumns.MEMBER_COUNT));
					String createTime = cursor.getString(cursor.getColumnIndex(DatabaseConstant.CircleGroupColumns.CREATE_TIME));
					long createCardId = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.CircleGroupColumns.CARD_ID));
					int groupType = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.CircleGroupColumns.TYPE));
					long accountId = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.CircleGroupColumns.ACCOUNT_ID));
					String remark = cursor.getString(cursor.getColumnIndex(DatabaseConstant.CircleGroupColumns.REMARK));
					String displayName = cursor.getString(cursor.getColumnIndex(DatabaseConstant.CircleGroupColumns.DISPLAY_NAME));
					int hadReqMember = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.CircleGroupColumns.HAD_REQUEST_MEMBER));
					groupItem = new CircleGroupResultEntity();
					groupItem.setGroupId(groupId);
					groupItem.setGroupName(grpName);
					groupItem.setCardId(createCardId);
					groupItem.setMemberCount(memberCount);
					groupItem.setCreateTime(createTime);
					groupItem.setType(groupType);
					groupItem.setAccountId(accountId);
					groupItem.setHeadImg(headImg);
					groupItem.setRemark(remark);
					groupItem.setDisplayName(displayName);
					groupItem.setHadRequestMember(hadReqMember);
				}
				cursor.close();
				cursor = null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return groupItem;
	}

	/**
	 * 更新成员数量
	 * @param groupId
	 * @param memberCount
	 */
	public void updateMemberCount(long groupId, int memberCount){
		String upSql = " update " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS + " set " +DatabaseConstant.CircleGroupColumns.MEMBER_COUNT + " = " + memberCount;
		String where = " where " + DatabaseConstant.CircleGroupColumns.GROUP_ID + "=" + groupId;
		try{
			this.getSQLiteDatabase().execSQL(upSql + where);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * 获取成员数量
	 * @param groupId
	 * @return
	 */
	public int getMemberCount(long groupId){
		int count = 0;
		String selectSql = " select " + DatabaseConstant.CircleGroupColumns.MEMBER_COUNT + " from " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS
					+ " where " + DatabaseConstant.CircleGroupColumns.GROUP_ID + "=" +groupId;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selectSql, null);
		if(Helper.isNotNull(cursor)){
			if(cursor.moveToFirst()){
				count = cursor.getInt(0);
			}
			cursor.close();
			cursor = null;
		}
		return count;
	}
	//#endregion
	
	//#region 群组成员对象
	/**
	 * 判断组成员是否已经存在
	 * @param groupId
	 * @param accountId 可为0 则判断该组是否有成员
	 * @return
	 */
	public boolean isExistMember(long groupId, long accountId){
		boolean exist = false;
		if(groupId < 1 ){
			return exist;
		}
		String selSql = "select _id from " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUP_MEMBERS + " where " + DatabaseConstant.CircleGroupMemberColumns.GROUP_ID + " =" + groupId;
		if(accountId > 0){
			selSql += " and " + DatabaseConstant.CircleGroupMemberColumns.ACCOUNT_ID + " = " + accountId;
		}
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selSql, null);
		if(Helper.isNotNull(cursor)){
			if(cursor.getCount() > 0){
				exist = true;
			}
			cursor.close();
			cursor = null;
		}
		return exist;
	}
	/**
	 * 获取成员对象
	 * @param cursor
	 * @return
	 */
	private CircleGroupMemberResultEntity getMemberEntity(Cursor cursor){
		CircleGroupMemberResultEntity memberItem = null;
		if(Helper.isNotNull(cursor)){
			long memberId = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.CircleGroupMemberColumns.MEMBER_ID));
			long accountId = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.CircleGroupMemberColumns.ACCOUNT_ID));
			int role = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.CircleGroupMemberColumns.ROLE));
			long cardId = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.CircleGroupMemberColumns.CARD_ID));
			String displayName = cursor.getString(cursor.getColumnIndex(DatabaseConstant.CircleGroupMemberColumns.DISPLAY_NAME));
			long groupId = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.CircleGroupMemberColumns.GROUP_ID));
			String headImg = cursor.getString(cursor.getColumnIndex(DatabaseConstant.CircleGroupMemberColumns.HEAD_IMG));
			String nickName = cursor.getString(cursor.getColumnIndex(DatabaseConstant.CircleGroupMemberColumns.NICK_NAME));
			
			memberItem = new CircleGroupMemberResultEntity();
			memberItem.setAccountId(accountId);
			memberItem.setCardId(cardId);
			memberItem.setDisplayName(displayName);
			memberItem.setGroupId(groupId);
			memberItem.setHeadImg(headImg);
			memberItem.setRole(role);
			memberItem.setNickName(nickName);
			memberItem.setMemberId(memberId);
		}
		return memberItem;
	}
	/**
	 * 插入成员到表
	 * @param memberEntity
	 * @return
	 */
	private boolean insertMember(CircleGroupMemberResultEntity memberEntity){

		if(Helper.isNull(memberEntity)){
			return false;
		}
		String inSql = "insert into " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUP_MEMBERS + "(" + DatabaseConstant.CircleGroupMemberColumns.GROUP_ID + "," 
				+ DatabaseConstant.CircleGroupMemberColumns.CARD_ID + "," + DatabaseConstant.CircleGroupMemberColumns.ACCOUNT_ID + "," 
				+ DatabaseConstant.CircleGroupMemberColumns.DISPLAY_NAME + "," + DatabaseConstant.CircleGroupMemberColumns.MEMBER_ID + ","
				+ DatabaseConstant.CircleGroupMemberColumns.HEAD_IMG + "," + DatabaseConstant.CircleGroupMemberColumns.ROLE 
				 + ") values (?,?,?,?,?,?,?)";

		
		Object[] bindArgs = new Object[]{
			memberEntity.getGroupId(),memberEntity.getCardId(),
			memberEntity.getAccountId(),memberEntity.getDisplayName(), memberEntity.getMemberId(),
			memberEntity.getHeadImg(),memberEntity.getRole()
		};
		this.getSQLiteDatabase().execSQL(inSql, bindArgs);
		return true;
	}
	/**
	 * 更改本地成员表信息
	 * @param memberEntity
	 */
	private void updateMember(CircleGroupMemberResultEntity memberEntity){
		if(Helper.isNotNull(memberEntity)){

			String upSql = " update " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUP_MEMBERS + " set " + DatabaseConstant.CircleGroupMemberColumns.DISPLAY_NAME + "='" + memberEntity.getDisplayName() +"'," 
					+ DatabaseConstant.CircleGroupMemberColumns.HEAD_IMG + "='" + memberEntity.getHeadImg() +"'," + DatabaseConstant.CircleGroupMemberColumns.ROLE + "=" + memberEntity.getRole()
					+ ", " + DatabaseConstant.CircleGroupMemberColumns.NICK_NAME + "='" + memberEntity.getNickName() + "'"
					+ " where " + DatabaseConstant.CircleGroupMemberColumns.CARD_ID + "=" + memberEntity.getCardId()
					+ " and " + DatabaseConstant.CircleGroupMemberColumns.GROUP_ID + "= " + memberEntity.getGroupId();
			this.getSQLiteDatabase().execSQL(upSql);
		}
	}
	/**
	 * 添加成员
	 * @param memberEntity
	 */
	public void addMember(CircleGroupMemberResultEntity memberEntity){
		if(Helper.isNull(memberEntity)){
			return ;
		}
		
		if(Helper.isEmpty(memberEntity.getDisplayName()) && Helper.isNotEmpty(memberEntity.getNickName())){
			memberEntity.setDisplayName(memberEntity.getNickName());		
		}
		
		boolean exist = isExistMember(memberEntity.getGroupId(), memberEntity.getAccountId());
		if(exist){
			updateMember(memberEntity);
		}else{
			insertMember(memberEntity);			
		}
	}
	/**
	 * 添加成员列表
	 * @param memList
	 */
	public void addMember(ArrayList<CircleGroupMemberResultEntity> memList){
		if(Helper.isNotEmpty(memList)){
		
			this.getSQLiteDatabase().beginTransaction();  
			try{  
				int size = memList.size();
			    for (int i = 0; i < size; i++) {
			    	addMember(memList.get(i));
				}
				//设置事务标志为成功，当结束事务时就会提交事务  
				this.getSQLiteDatabase().setTransactionSuccessful();  
			}  
			catch(Exception e){
				LogHelper.e(TAG, "添加成员出错了");
			}
		    //结束事务  
			this.getSQLiteDatabase().endTransaction();
		}
	}
	/**
	 * 添加 单位成员到 群组
	 * @param groupId
	 * @param createCardId
	 * @param enterpriseId
	 * @return
	 */
	public void addMember(long groupId, long createCardId, long enterpriseId){
		try{
			ArrayList<UnitMemberResultEntity> companyMemberList = UnitDao.getInstance().getMemberList(enterpriseId, true);
			if(Helper.isNotNull(companyMemberList)){
				int size = companyMemberList.size();
				CircleGroupMemberResultEntity groupMember = null;
				for (int i = 0; i < size; i++) {
					UnitMemberResultEntity companyMember = companyMemberList.get(i);
					groupMember = new CircleGroupMemberResultEntity();
					groupMember.setGroupId(groupId);
					groupMember.setAccountId(companyMember.getAccountId());
					groupMember.setCardId(companyMember.getCardId());
					groupMember.setDisplayName(companyMember.getDisplayName());
					groupMember.setHeadImg(companyMember.getHeadImg());
					groupMember.setRole(companyMember.getRole());
					groupMember.setNickName(companyMember.getNickName());
					insertMember(groupMember);
				}
			}
		}catch(Exception ex){
			LogHelper.e(TAG, "插入单位成员异常");
		}
	}
	/**
	 * 创建讨论组时 添加成员
	 * @param groupId
	 * @param createCardId
	 * @param groupChatMemberList
	 */
	public void addMember(long groupId, long createCardId, ArrayList<CircleGroupMemberJsonEntity> groupChatMemberList){
		if(Helper.isNotEmpty(groupChatMemberList)){
			StringBuilder groupRemark = new StringBuilder();
			// 先插入创建者到成员列表
			UserInfoResultEntity myUser = CurrentUser.getInstance().getUserInfoEntity();
			
			// 插入成员
			CircleGroupMemberResultEntity memberEntity = null;
			for (int i = 0, size = groupChatMemberList.size(); i < size; i++) {
				CircleGroupMemberJsonEntity groupChatMember = groupChatMemberList.get(i);
				
				memberEntity = new CircleGroupMemberResultEntity();
				memberEntity.setGroupId(groupId);
				if(groupChatMember.getAccountId() == myUser.getId()){
					// 创建者
					memberEntity.setAccountId(myUser.getId());
					memberEntity.setCardId(groupChatMember.getCardId());
					memberEntity.setDisplayName(myUser.getDisplayName());
					memberEntity.setHeadImg(myUser.getHeadImg());
					memberEntity.setRole(DatabaseConstant.Role.CREATER);
					memberEntity.setNickName(myUser.getNickName());
				}else{
					// 普通成员
					ContactListItemEntity contactItem = ContactVCardDao.getInstance().getEntityForListItem(groupChatMember.getCardId());
					String headImg = contactItem.getHeadImg();
					memberEntity.setAccountId(contactItem.getAccountId());
					memberEntity.setCardId(groupChatMember.getCardId());
					memberEntity.setRole(DatabaseConstant.Role.MEMBER);
					memberEntity.setDisplayName(contactItem.getDisplayName());
					memberEntity.setHeadImg(headImg);
	//				memberEntity.setNickName(contactItem.getVcardNo());
					groupRemark.append(contactItem.getDisplayName()).append("、");
				}
				addMember(memberEntity);
				
			}
			// 更改群聊备注为成员名称
			if(Helper.isNotEmpty(groupRemark)){
				String showRemark = groupRemark.substring(0, groupRemark.length()-1);
				updateRemark(groupId, showRemark);

				// 插入一条组会话到本地数据库
				String body = ActivityHelper.getGlobalApplicationContext().getString(R.string.you_invite_who_join_group_chat, showRemark);
				MessageSessionDao.getInstance().add(groupId, Constants.MessageContentType.SESSION_GROUP_TEXT, body, 1);
				
			}
		}
	}
	/**
	 * 添加单个成员通知处理
	 * @param msgParam
	 */
	public void addMember(MessageResultEntity msgParam){
		if(Helper.isNotNull(msgParam)){
			CircleGroupMemberResultEntity member = new CircleGroupMemberResultEntity();
			member.setGroupId(msgParam.getTagId());
			member.setDisplayName(msgParam.getFromName());
			member.setHeadImg(msgParam.getFromHeadImg());
			member.setAccountId(msgParam.getFromAccountId());
			member.setCardId(msgParam.getFromCardId());				
			member.setRole(DatabaseConstant.Role.MEMBER);
			
			if(!isExistMember(msgParam.getTagId(), msgParam.getFromAccountId())){
				boolean isSuccess = insertMember(member);
				if(isSuccess){
					//  成员数量加 1
					this.getSQLiteDatabase().execSQL(" update " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS + " set " + DatabaseConstant.CircleGroupColumns.MEMBER_COUNT + "=" 
							+ DatabaseConstant.CircleGroupColumns.MEMBER_COUNT + " + 1 where " + DatabaseConstant.CircleGroupColumns.GROUP_ID + "=" + msgParam.getTagId());
				}
			}
		}
	}
	/**
	 * 选择名片夹联系人添加到讨论组
	 * @param groupId
	 */
	public void addMember(long groupId, ArrayList<ContactListItemEntity> contactList){
		if(Helper.isNull(contactList) || groupId == 0){
			return;
		}
		CircleGroupMemberResultEntity memberEntity = null;
		int addMember = 0;
		for (int i = 0, size = contactList.size(); i < size; i++) {
			ContactListItemEntity contact = contactList.get(i);
			memberEntity = new CircleGroupMemberResultEntity();
			memberEntity.setGroupId(groupId);
			memberEntity.setAccountId(contact.getAccountId());						
			memberEntity.setRole(DatabaseConstant.Role.MEMBER);
			memberEntity.setCardId(contact.getCardId());
			memberEntity.setDisplayName(contact.getDisplayName());
			memberEntity.setHeadImg(contact.getHeadImg());
			boolean isSuccess = insertMember(memberEntity);
			if(isSuccess){
				addMember +=1;
			}
		}
		
	}
	/**
	 * 更改群聊成员下的身份权限
	 * @param groupId
	 * @param accountId
	 * @param role
	 */
	public void updateMemberAuth(long groupId, long accountId, int role){
		String where = DatabaseConstant.CircleGroupMemberColumns.GROUP_ID + "=" + groupId + " and " + DatabaseConstant.CircleGroupMemberColumns.ACCOUNT_ID + "=" + accountId;
		String updateSql = " update " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUP_MEMBERS + " set " + DatabaseConstant.CircleGroupMemberColumns.ROLE + "=" + role;
		this.getSQLiteDatabase().execSQL(updateSql + " where " + where);
	}
	/**
	 * 删除群组下的所有成员
	 * @param groupId
	 * @return
	 */
	public boolean deleteMember(long groupId){
		boolean isDel = deleteMember(groupId, 0);
//		if(isDel){
//			// 删除群组成员 更新群组成员数量
//			this.getSQLiteDatabase().execSQL(" update " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS + " set " + DatabaseConstant.CircleGroupColumns.MEMBER_COUNT + "=0" 
//					+ " where " + DatabaseConstant.CircleGroupColumns.GROUP_ID + "=" + groupId);
//		}
		return isDel;
	}
	/**
	 * 删除群组下的某个成员
	 * @param groupId
	 * @param accountId
	 * @return
	 */
	public boolean deleteMember(long groupId, long accountId){
		boolean del = false;
		if(groupId < 1){
			return del;
		}
		String where = DatabaseConstant.CircleGroupMemberColumns.GROUP_ID + "=" + groupId;
		if(accountId > 0){
			where += " and " + DatabaseConstant.CircleGroupMemberColumns.ACCOUNT_ID + "=" + accountId;
		}
		int delRow = this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUP_MEMBERS, where, null);
		if(delRow > 0 ){
			del = true;
			if(accountId > 0){
				// 删除群组成员 更新群组成员数量
				this.getSQLiteDatabase().execSQL(" update " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS + " set " + DatabaseConstant.CircleGroupColumns.MEMBER_COUNT + "=" 
						+ DatabaseConstant.CircleGroupColumns.MEMBER_COUNT + " - 1 where " + DatabaseConstant.CircleGroupColumns.GROUP_ID + "=" + groupId);
			}
		}
		return del;
	}
	/**
	 * 获取讨论组的创建者
	 * @param groupId
	 * @return
	 */
	public CircleGroupMemberResultEntity getMemberCreater(long groupId){
		CircleGroupMemberResultEntity member = null;
		String selectSql = " select * from " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUP_MEMBERS 
				+ " where " + DatabaseConstant.CircleGroupMemberColumns.GROUP_ID + "=" + groupId
				+ " and " + DatabaseConstant.CircleGroupMemberColumns.ROLE + "=" + DatabaseConstant.Role.CREATER;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selectSql, null);
		if(Helper.isNotNull(cursor)){
			if(cursor.moveToFirst()){
				member = getMemberEntity(cursor);
			}
			cursor.close();
			cursor = null;
		}
		return member;
	}
	/**
	 * 根据帐户id获取成员对象
	 * @param groupId
	 * @param accountId
	 * @return
	 */
	public CircleGroupMemberResultEntity getMemberEntity(long groupId, long accountId){
		CircleGroupMemberResultEntity member = null;
		String selectSql = " select * from " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUP_MEMBERS 
				+ " where " + DatabaseConstant.CircleGroupMemberColumns.GROUP_ID + "=" + groupId
				+ " and " + DatabaseConstant.CircleGroupMemberColumns.ACCOUNT_ID + "=" + accountId;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selectSql, null);
		if(Helper.isNotNull(cursor)){
			if(cursor.moveToFirst()){
				member = getMemberEntity(cursor);
			}
			cursor.close();
			cursor = null;
		}
		return member;
	}
	/**
	 * 获取群聊组成员对象
	 * @param groupId
	 * @return
	 */
	public ArrayList<CircleGroupMemberResultEntity> getMemberList(long groupId){
		ArrayList<CircleGroupMemberResultEntity> groupMemberList = getMemberList(groupId, -1);
		return groupMemberList;
	}
	/**
	 * 获取符合身份的成员列表
	 * @param groupId
	 * @param memberRole -1获取全部
	 * @return
	 */
	public ArrayList<CircleGroupMemberResultEntity> getMemberList(long groupId, int memberRole){

		ArrayList<CircleGroupMemberResultEntity> groupMemberList = null;
		String where = DatabaseConstant.CircleGroupMemberColumns.GROUP_ID + "=" +groupId;
		if(memberRole != -1){
			where += " and " + DatabaseConstant.CircleGroupMemberColumns.ROLE + "=" + memberRole;
		}
		try{
			Cursor cursor = this.getSQLiteDatabase().query(DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUP_MEMBERS, MEMBER_PROJECTION, where, null, null, null, null);
			if(Helper.isNotNull(cursor)){
				groupMemberList = new ArrayList<CircleGroupMemberResultEntity>();
				while(cursor.moveToNext()){
					CircleGroupMemberResultEntity memberItem = getMemberEntity(cursor);
	                groupMemberList.add(memberItem);			
				}
				cursor.close();
				cursor = null;
			}
		}catch(Exception ex){
			LogHelper.e(TAG, "获取群聊成员信息异常");
		}
		return groupMemberList;
	}
	/**
	 * 单位群聊成员数据刷新
	 * @param enterpriseId
	 */
	public void refreshMemberByEnterprise(long enterpriseId){
		String sql = " delete from " + DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUP_MEMBERS + " where " + DatabaseConstant.CircleGroupMemberColumns.ACCOUNT_ID
				+ " not in ( selec " + DatabaseConstant.EnterpriseMemberColumns.ACCOUNT_ID + " from " + DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS
				+ " where " + DatabaseConstant.EnterpriseMemberColumns.ENTERPRISE_ID + "=" + enterpriseId + ")";
		this.getSQLiteDatabase().execSQL(sql);
	}
	/**
	 * 是否含有成员
	 * @param groupId
	 * @return
	 */
	public boolean isHasMember(long groupId){
		boolean hasMember = false;
		String sql = " select ".concat(DatabaseConstant.CircleGroupMemberColumns.CARD_ID).concat(" from ").concat(DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUP_MEMBERS)
				.concat(" where ").concat(DatabaseConstant.CircleGroupMemberColumns.GROUP_ID).concat("=").concat(groupId+"");
		Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
		if(Helper.isNotNull(cursor)){
			if(cursor.getCount() > 1){
				hasMember = true;
			}
			cursor.close();
			cursor = null;
		}
		return hasMember;
	}
	/**
	 * 获取当前账户的身份权限
	 */
	public int getMyRole(long groupId){
		int role = DatabaseConstant.Role.MEMBER;
		long myAccountId = CurrentUser.getInstance().getUserInfoEntity().getId();
		String sql = " select ".concat(DatabaseConstant.CircleGroupMemberColumns.ROLE).concat(" from ").concat(DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUP_MEMBERS)
				.concat(" where ").concat(DatabaseConstant.CircleGroupMemberColumns.GROUP_ID).concat("=").concat(groupId + "")
				.concat(" and ").concat(DatabaseConstant.CircleGroupMemberColumns.ACCOUNT_ID).concat("=").concat(myAccountId+"");
		Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
		if(Helper.isNotNull(cursor)){
			if(cursor.moveToFirst()){
				role = cursor.getInt(0);
			}
			cursor.close();
			cursor = null;
		}
		return role;
	}
	//#endregion
	
	private SQLiteDatabase getSQLiteDatabase(){
		return VCardSQLiteDatabase.getInstance().getSQLiteDatabase();
	}
}
