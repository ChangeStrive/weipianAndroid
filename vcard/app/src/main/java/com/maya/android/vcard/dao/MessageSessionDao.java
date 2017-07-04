package com.maya.android.vcard.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.VCardSQLiteDatabase;
import com.maya.android.vcard.entity.ContactListItemEntity;
import com.maya.android.vcard.entity.MessageSessionEntity;
import com.maya.android.vcard.entity.result.MessageResultEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 消息中心数据
 * @author zcz
 *
 * 2014-2-26 下午5:24:09
 */
public class MessageSessionDao {

	private static final String TAG = MessageSessionDao.class.getSimpleName();

	//region 单例
	private static MessageSessionDao sInstance;
	public static MessageSessionDao getInstance(){
		if(Helper.isNull(sInstance)){
			sInstance = new MessageSessionDao();
		}
		return sInstance;
	}
	private MessageSessionDao(){}
	//endregion 单例

	/**
	 * 获取对象
	 * @param cursor
	 * @return MessageEntity
	 */
	private MessageSessionEntity getEntity(Cursor cursor){
		MessageSessionEntity message = null;
		if(Helper.isNotNull(cursor)){
			message = new MessageSessionEntity();
			long id = cursor.getLong(cursor.getColumnIndex(DatabaseConstant._ID));
			long myCardId = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.MessageColumns.CARD_ID));
			long acctId = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.MessageColumns.OTHER_ACCOUNT_ID));
			long cardId = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.MessageColumns.OTHER_CARD_ID));
			String displayName = cursor.getString(cursor.getColumnIndex(DatabaseConstant.MessageColumns.OTHER_DISPLAY_NAME));
			int sendType = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.MessageColumns.SEND_TYPE));
			String body = cursor.getString(cursor.getColumnIndex(DatabaseConstant.MessageColumns.BODY));
			int read = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.MessageColumns.READ));
			int contentType = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.MessageColumns.CONTENT_TYPE));
			boolean isSendFail = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseConstant.MessageColumns.IS_SEND_FAIL)));
			String createTime = cursor.getString(cursor.getColumnIndex(DatabaseConstant.MessageColumns.CREATE_TIME));
			long loseTime = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.MessageColumns.LOST_TIME));
			String filePath = cursor.getString(cursor.getColumnIndex(DatabaseConstant.MessageColumns.FILE_PATH));
			boolean isBlack = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseConstant.MessageColumns.IS_BLACK)));
			long tagId = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.MessageColumns.TAG_ID));
			String headImg = cursor.getString(cursor.getColumnIndex(DatabaseConstant.MessageColumns.OTHER_HEAD_IMG));
			int enable = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.MessageColumns.ENABLE));
			/**
			 * 填充值
			 */
			message.setId(id);
			message.setBody(body);
			message.setMyCardId(myCardId);
			message.setFromCardId(cardId);
			message.setFromAccountId(acctId);
			message.setFromName(displayName);
			message.setFromHeadImg(headImg);
			message.setContentType(contentType);
			message.setCreateTime(createTime);
			message.setRead(read);
			message.setSendFail(isSendFail);
			message.setSendType(sendType);
			message.setLoseTime(loseTime);
			message.setBlack(isBlack);
			message.setFilePath(filePath);
			message.setTagId(tagId);
			message.setEnable(enable);
		}
		return message;
	}
	/**
	 * 获取对象
	 * @param cursor
	 * @return MessageEntity
	 */
	private ArrayList<MessageSessionEntity> getList(Cursor cursor){
		ArrayList<MessageSessionEntity> list = new ArrayList<MessageSessionEntity>();
		if(Helper.isNotNull(cursor)){
			while(cursor.moveToNext()){
				MessageSessionEntity message = getEntity(cursor);
				list.add(message);
			}
			cursor.close();
			cursor = null;
		}
		return list;
	}
	/**
	 * 根据sql 语句查询列表
	 * @param sql
	 * @param isCount 是否含 count(*) 值
	 * @return
	 */
	private ArrayList<MessageSessionEntity> getList(String sql, boolean isCount){
		ArrayList<MessageSessionEntity> messageList = new ArrayList<MessageSessionEntity>();
		Cursor cursor = getSQLiteDatabase().rawQuery(sql, null);
		if(Helper.isNotNull(cursor)){
			while (cursor.moveToNext()) {
				try {
					MessageSessionEntity msg = getEntity(cursor);				
					if(Helper.isNotNull(msg) && isCount){
						// 获取 count(1) 字段值
//						msg.setMessageCount(cursor.getInt(cursor.getColumnIndex(DatabaseConstant._COUNT)));
						
						// 统计未读得消息
						int unreadCount = getUnreadCount(msg.getFromAccountId(), msg.getTagId());
						msg.setUnreadCount(unreadCount);
					}
					messageList.add(msg);
				} catch (Exception e) {
					Log.i("MessageSessionDao", "方法getList(String, boolean)出现空指针错误");
				}
			}

			cursor.close();
			cursor = null;
		}
		
		return messageList;
	}
	/**
	 * 判断 单位名称消息是否已经存在
	 * @param groupId
	 * @return
	 */
	public boolean isExistGroupMessage(long groupId){
		boolean exist = false;
		String sql = " select ".concat(DatabaseConstant._ID).concat(" from ").concat(DatabaseConstant.DBTableName.TABLE_MESSAGES).concat(" where ")
				.concat(DatabaseConstant.MessageColumns.TAG_ID).concat("=" + groupId);
		try{
			Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
			if(Helper.isNotNull(cursor)){
				if(cursor.getCount() > 0){
					exist = true;
				}
				cursor.close();
				cursor = null;
			}
		}catch(NullPointerException e){
			Log.e(TAG, "数据库对象未创建");
		}
		return exist;
	}
	/**
	 * 判断本地记录里是否已有该账号聊天记录
	 * @param accountId
	 * @return
	 */
	public boolean isExistAccountMessage(long accountId){
		boolean exist = false;
		String sql = " select ".concat(DatabaseConstant._ID).concat(" from ").concat(DatabaseConstant.DBTableName.TABLE_MESSAGES).concat(" where ")
				.concat(DatabaseConstant.MessageColumns.OTHER_ACCOUNT_ID).concat("=" + accountId).concat(" and ").concat(DatabaseConstant.MessageColumns.TAG_ID).concat("=0");
		try{
			Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
			if(Helper.isNotNull(cursor)){
				if(cursor.getCount() > 0){
					exist = true;
				}
				cursor.close();
				cursor = null;
			}
		}catch(NullPointerException e){
			Log.e(TAG, "数据库对象未创建");
		}
		return exist;
	}
	/**
	 * 根据消息id获取消息对象
	 * @param sessionId
	 * @return
	 */
	public MessageSessionEntity getEntity(long sessionId){
		MessageSessionEntity message = null;
		String sql = " select * from ".concat(DatabaseConstant.DBTableName.TABLE_MESSAGES)
				.concat(" where ").concat(DatabaseConstant._ID).concat("=" + sessionId);
		Cursor cursor = null;
		try {
			cursor = this.getSQLiteDatabase().rawQuery(sql, null);
		} catch (Exception e) {
			cursor = null;
		}
		if(Helper.isNotNull(cursor)){
			if(cursor.moveToFirst()){
				message = getEntity(cursor);
			}
			cursor.close();
			cursor = null;
		}
		return message;
	}
	/**
	 * 获取所有人的信息列表 按账户分组
	 * @param isBlack 是否取黑名单列表
	 * @return
	 */
	public ArrayList<MessageSessionEntity> getListForAll(boolean isBlack){
		int iBlack = 0;
		if(isBlack){
			iBlack = 1;
		}
		String where = " where " + DatabaseConstant.MessageColumns.TAG_ID + "<1 and (" + DatabaseConstant.MessageColumns.IS_BLACK + " = '" + isBlack +"' or  " +
				 DatabaseConstant.MessageColumns.IS_BLACK + " = '" + iBlack +"')";
		String groupWhere = " where " + DatabaseConstant.MessageColumns.TAG_ID + ">0";
		String sql = "select * from ( select count(1) as " + DatabaseConstant._COUNT +",* from " + DatabaseConstant.DBTableName.TABLE_MESSAGES + where + " group by " + DatabaseConstant.MessageColumns.OTHER_ACCOUNT_ID 
				+ " union select count(1) as " + DatabaseConstant._COUNT +",* from " + DatabaseConstant.DBTableName.TABLE_MESSAGES + groupWhere + " group by " + DatabaseConstant.MessageColumns.TAG_ID 
				+ ") as tmpTable order by " + DatabaseConstant.MessageColumns.CREATE_TIME + " desc ";
		return getList(sql, true);
	}
	/**
	 * 获取账户或群组下的 会话列表 
	 * @param accountId
	 * @param groupId
	 * @return
	 */
	public ArrayList<MessageSessionEntity> getList(long accountId, long groupId){
		if(groupId > 0){
			return getListByGroup(groupId);
		}else{
			return getListByAccount(accountId);
		}
		
	}
	/**
	 * 获取和某个联系人的消息列表
	 * @param accountId 联系人账户id
	 * @return
	 */
	public ArrayList<MessageSessionEntity> getListByAccount(long accountId){
		String where = " where " + DatabaseConstant.MessageColumns.TAG_ID + "<1 and " + DatabaseConstant.MessageColumns.OTHER_ACCOUNT_ID + "="+accountId;
		String sql = "select * from " + DatabaseConstant.DBTableName.TABLE_MESSAGES + where + " order by _id";
		return getList(sql, false);
	}
	/**
	 * 获取群聊信息
	 * @param groupId
	 * @return
	 */
	public ArrayList<MessageSessionEntity> getListByGroup(long groupId){
		String where = " where " + DatabaseConstant.MessageColumns.TAG_ID + "="+groupId;
		String sql = "select * from " + DatabaseConstant.DBTableName.TABLE_MESSAGES + where + " order by _id";
		return getList(sql, false);
	}
	/**
	 * 获取会话聊天的对方姓名
	 * @param accountId
	 * @return
	 */
	public String getSessionShowTitle(long accountId){
		String sessionTitle = ActivityHelper.getGlobalApplicationContext().getString(R.string.stranger);
		
		// 先从我的联系人中获取姓名
		ContactListItemEntity contact = ContactVCardDao.getInstance().getEntityForListItemByAccount(accountId);
		if(Helper.isNotNull(contact) && Helper.isNotEmpty(contact.getDisplayName())){
			sessionTitle = contact.getDisplayName();
		}else{
			String where = " where " + DatabaseConstant.MessageColumns.TAG_ID + "<1 and " + DatabaseConstant.MessageColumns.OTHER_ACCOUNT_ID + "="+accountId;
			String sql = "select " + DatabaseConstant.MessageColumns.OTHER_DISPLAY_NAME + " from " + DatabaseConstant.DBTableName.TABLE_MESSAGES + where + " order by _id";
			Cursor cursor = getSQLiteDatabase().rawQuery(sql, null);
			if(Helper.isNotNull(cursor)){
				if(cursor.moveToFirst()){
					sessionTitle = cursor.getString(0);
				}
				cursor.close();
				cursor = null;
			}
		}
		return sessionTitle;
	}
	/**
	 * 插入语句
	 * @param message
	 * @return
	 */
	public int insert(MessageSessionEntity message){
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.MessageColumns.CARD_ID, message.getMyCardId());
		values.put(DatabaseConstant.MessageColumns.OTHER_ACCOUNT_ID, message.getFromAccountId());
		values.put(DatabaseConstant.MessageColumns.OTHER_CARD_ID, message.getFromCardId());
		values.put(DatabaseConstant.MessageColumns.OTHER_DISPLAY_NAME, message.getFromName());
		values.put(DatabaseConstant.MessageColumns.OTHER_HEAD_IMG, message.getFromHeadImg());
		values.put(DatabaseConstant.MessageColumns.TAG_ID, message.getTagId());
		values.put(DatabaseConstant.MessageColumns.BODY, message.getBody());
		values.put(DatabaseConstant.MessageColumns.CREATE_TIME, message.getCreateTime());
		values.put(DatabaseConstant.MessageColumns.CONTENT_TYPE, message.getContentType());
		values.put(DatabaseConstant.MessageColumns.LOST_TIME, message.getLoseTime());
		values.put(DatabaseConstant.MessageColumns.READ, message.getRead());
		values.put(DatabaseConstant.MessageColumns.SEND_TYPE, message.getSendType());
		values.put(DatabaseConstant.MessageColumns.FILE_PATH, message.getFilePath());
		values.put(DatabaseConstant.MessageColumns.IS_SEND_FAIL, message.isSendFail());
		values.put(DatabaseConstant.MessageColumns.IS_BLACK, message.isBlack());
		values.put(DatabaseConstant.MessageColumns.ENABLE, message.getEnable());
		int ret = (int)this.getSQLiteDatabase().insert(DatabaseConstant.DBTableName.TABLE_MESSAGES, null, values);		
		return ret;
	}
	/**
	 * 根据接收到的消息 添加会话
	 * @param reciveMsg 接收到的透传消息
	 * @param localFilePath 文件的本地路径
	 */
	public int add( MessageResultEntity reciveMsg, String localFilePath) {
		if(Helper.isNull(reciveMsg)){
			return 0;
		}
		// 获取我的当前名片id
		long curMyCardId = CurrentUser.getInstance().getCurrentVCardEntity().getId();
		MessageSessionEntity msg = new MessageSessionEntity();
		msg.setMyCardId(curMyCardId);
		msg.setFromAccountId(reciveMsg.getFromAccountId());
		msg.setFromCardId(reciveMsg.getFromCardId());
		msg.setFromName(reciveMsg.getFromName());
		msg.setFromHeadImg(reciveMsg.getFromHeadImg());
		msg.setContentType(reciveMsg.getContentType());
		msg.setCreateTime(reciveMsg.getCreateTime());
		msg.setBody(reciveMsg.getBody());
		msg.setLoseTime(reciveMsg.getLoseTime());
		msg.setTagId(reciveMsg.getTagId());
		msg.setSendType(DatabaseConstant.MessageSendType.RECIVER);		
		
		if(Helper.isNotNull(localFilePath)){
			msg.setFilePath(localFilePath);
		}
		return insert(msg);
	}
	/**
	 * 插入一条会话 
	 * @param otherAccountId
	 * @param otherCardId
	 * @param otherHeadImg
	 * @param otherName
	 * @param tagId
	 * @param contentType
	 * @param body
	 */
	public void add(long otherAccountId, long otherCardId, String otherHeadImg, String otherName, long tagId, int contentType, String body, int sendType){

		MessageSessionEntity msg = new MessageSessionEntity();
		msg.setMyCardId(CurrentUser.getInstance().getCurrentVCardEntity().getId());
		msg.setFromAccountId(otherAccountId);
		msg.setFromCardId(otherCardId);
		msg.setFromName(otherName);
		msg.setFromHeadImg(otherHeadImg);
		msg.setContentType(contentType);
		msg.setCreateTime(ResourceHelper.getNowTime());
		msg.setBody(body);
		msg.setTagId(tagId);
		msg.setSendType(sendType);		
		
		if(sendType == DatabaseConstant.MessageSendType.SEND){
			msg.setRead(DatabaseConstant.MessageReadFlag.READED);
		}
		insert(msg);
	}
	/**
	 * 插入群聊里的非对话消息
	 * @param tagId
	 * @param contentType
	 * @param body
	 * @param enable
	 * @param createTime 创建时间
	 */
	public void add(long tagId, int contentType, String body, int enable, String createTime){
		if(Helper.isEmpty(createTime)){
			createTime = ResourceHelper.getNowTime();
		}
		MessageSessionEntity msg = new MessageSessionEntity();
		msg.setMyCardId(CurrentUser.getInstance().getCurrentVCardEntity().getId());
		msg.setTagId(tagId);
		msg.setEnable(enable);
		msg.setContentType(contentType);
		msg.setBody(body);
		msg.setSendType(DatabaseConstant.MessageSendType.OTHER);
		msg.setCreateTime(createTime);
		insert(msg);
	}
	/**
	 * 插入群聊里的非对话消息
	 * @param tagId
	 * @param contentType
	 * @param body
	 * @param enable
	 */
	public void add(long tagId, int contentType, String body, int enable){
		add(tagId, contentType, body, enable, null);
	}
	/**
	 * 删除
	 * @param keyId
	 * @return
	 */
	public int delete(long keyId){
		String where = DatabaseConstant._ID + "=" + keyId;
		return this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_MESSAGES, where,null);
	}

	/**
	 * 根据联系人账户id 删除消息
	 * @param accountId
	 * @return
	 */
	public int deleteByAccount(long accountId){
		String where = DatabaseConstant.MessageColumns.OTHER_ACCOUNT_ID + "=" + accountId 
			     	+ " and " + DatabaseConstant.MessageColumns.TAG_ID + "=0";
		return this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_MESSAGES, where,null);
	}
	/**
	 * 删除群组会话 (保留enbale =1 的消息)
	 * @param groupId
	 * @return
	 */
	public int deleteByGroup(long groupId){
		return deleteByGroup(groupId, false);
	}
	/**
	 * 删除群组会话
	 * @param groupId
	 * @param isDelAll
	 * @return
	 */	
	public int deleteByGroup(long groupId, boolean isDelAll){
		String where = DatabaseConstant.MessageColumns.TAG_ID.concat("=").concat(groupId + "");
		if(!isDelAll){				
			where = where.concat(" and ").concat(DatabaseConstant.MessageColumns.ENABLE).concat("=0") ;
		}
		return this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_MESSAGES, where,null);
	}
	/**
	 * 删除会话 
	 * @param accountId
	 * @param tagId
	 */
	public void delete(long accountId, long tagId){
		if(accountId > 0){
			deleteByAccount(accountId);
		}
		if(tagId > 0){
			deleteByGroup(tagId);
		}
	}
	/**
	 * 清空所有聊天记录
	 */
	public void deleteAll(){
		String where = DatabaseConstant._ID.concat(">2");
				//.concat(" and ").concat(DatabaseConstant.MessageColumns.ENABLE).concat("=0");
		this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_MESSAGES, where, null);
	}
	/**
	 * 设置 信息已读标志
	 * @param accountId 帐户id
	 * @param tagId 群聊组id
	 */
	public int updateReaded(long accountId, long tagId){
		if(accountId == 0 && tagId == 0){
			return 0;
		}
		String where = DatabaseConstant.MessageColumns.READ + "=0 ";
		if(tagId > 0){
			where += " and " + DatabaseConstant.MessageColumns.TAG_ID + "=" + tagId;
		}
		if(accountId > 0){
			where +=" and " + DatabaseConstant.MessageColumns.OTHER_ACCOUNT_ID + "=" + accountId;
		}

		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.MessageColumns.READ, 1);
		int updateCount = this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_MESSAGES, values, where, null);
		return updateCount;
	}
	/**
	 *  获取未读消息的条数（某个人/某群组）
	 * @param accountId
	 * @param tagId
	 * @return
	 */
	public int getUnreadCount(long accountId, long tagId){
		int count = 0;
		String sql = " select count(1) from " + DatabaseConstant.DBTableName.TABLE_MESSAGES + " where " + 
					DatabaseConstant.MessageColumns.SEND_TYPE + "=" + DatabaseConstant.MessageSendType.RECIVER + 
					" and " + DatabaseConstant.MessageColumns.READ + "= " + DatabaseConstant.MessageReadFlag.UNREAD;
		if(accountId > 0){
			sql += " and " + DatabaseConstant.MessageColumns.OTHER_ACCOUNT_ID + "=" + accountId ;
		}
		if(tagId > 0){
			sql += " and " + DatabaseConstant.MessageColumns.TAG_ID + "=" + tagId;
		}
		Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
		if(Helper.isNotNull(cursor)){
			if(cursor.moveToFirst()){
				count = cursor.getInt(0);
			}
			cursor.close();
			cursor = null;
		}
		return count;
	}

	/**
	 * 获取所有未读消息的条数
	 * @return
	 */
	public int getUnreadCount(){
		int count = 0;
		String sql = " select count(1) from " + DatabaseConstant.DBTableName.TABLE_MESSAGES + " where " + 
					DatabaseConstant.MessageColumns.SEND_TYPE + "=" + DatabaseConstant.MessageSendType.RECIVER + 
					" and " + DatabaseConstant.MessageColumns.READ + "= " + DatabaseConstant.MessageReadFlag.UNREAD ;
		try{
			Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
			if(Helper.isNotNull(cursor)){
				if(cursor.moveToFirst()){
					count = cursor.getInt(0);
				}
				cursor.close();
				cursor = null;
			}
		}catch(NullPointerException e){
			Log.e(TAG, "数据库对象未创建");
		}
		return count;
	}

	/**
	 * 模糊查找
	 * @param content
	 * @return
	 * 			ArrayList<MessageEntity>
	 */
	public ArrayList<MessageSessionEntity> getListForSearch(String content){
		String sql = new StringBuilder().append("select * from ")
				.append("(select t1.* from ")
				.append(DatabaseConstant.DBTableName.TABLE_MESSAGES)
				.append(" as t1 left join ")
				.append(DatabaseConstant.DBTableName.TABLE_CONTACTS).append(" as t2 ")
				.append(" on t1.").append(DatabaseConstant.MessageColumns.OTHER_CARD_ID)
				.append(" = t2.").append(DatabaseConstant.ContactColumns.CARD_ID)
				.append(" where t2.").append(DatabaseConstant.ContactColumns.DISPLAY_NAME).append(" like '%").append(content).append("%'")
				.append(" or t2.").append(DatabaseConstant.ContactColumns.COMPANY_NAME).append(" like '%").append(content).append("%'")
				.append(" or t2.").append(DatabaseConstant.ContactColumns.MOBILE).append(" like '").append(content).append("%'")
				.append(" or t1.").append(DatabaseConstant.MessageColumns.BODY).append(" like '%").append(content).append("%'")
				.append(") group by ").append(DatabaseConstant.MessageColumns.TAG_ID).append(",").append(DatabaseConstant.MessageColumns.OTHER_ACCOUNT_ID)
				.append(" order by ").append(DatabaseConstant.MessageColumns.CREATE_TIME).append(" desc ")
				.toString();
		
		return getList(sql,false);
	}
	/**
	 * 获取当前消息组的id集合
	 * @return
	 */
	public ArrayList<Long> getMessageGroupIdList(){
		ArrayList<Long> groupIdList = null;
		String sql = "select ".concat(DatabaseConstant.MessageColumns.TAG_ID).concat(" from ").concat(DatabaseConstant.DBTableName.TABLE_MESSAGES)
					.concat(" where ").concat(DatabaseConstant.MessageColumns.TAG_ID).concat(">0").concat(" group by ").concat(DatabaseConstant.MessageColumns.TAG_ID);
		Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
		if(Helper.isNotNull(cursor)){
			groupIdList = new ArrayList<Long>();
			while(cursor.moveToNext()){
				groupIdList.add(cursor.getLong(0));
			}
			cursor.close();
		}
		return groupIdList;
	}
	private SQLiteDatabase getSQLiteDatabase(){
		return VCardSQLiteDatabase.getInstance().getSQLiteDatabase();
	}
}
