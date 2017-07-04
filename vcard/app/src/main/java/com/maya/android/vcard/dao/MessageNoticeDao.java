package com.maya.android.vcard.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.data.VCardSQLiteDatabase;
import com.maya.android.vcard.entity.MessageNoticeEntity;
import com.maya.android.vcard.entity.result.MessageResultEntity;
/**
 * 通知消息dao
 * 
 */
public class MessageNoticeDao {
	private static final String TAG = MessageNoticeDao.class.getSimpleName();
	private Context mContext = ActivityHelper.getGlobalApplicationContext();

	//region 单例
	private static MessageNoticeDao sInstance;
	public static MessageNoticeDao getInstance(){
		if(Helper.isNull(sInstance)){
			sInstance = new MessageNoticeDao();
		}
		return sInstance;
	}
	private MessageNoticeDao() {}
	//endregion 单例

	//region public
	/**
	 * 根据处理类型 获取通知列表
	 *
	 * @param dealedStatus
	 *            -1 获取所有
	 * @return
	 */
	public ArrayList<MessageNoticeEntity> getList(int dealedStatus) {
		ArrayList<MessageNoticeEntity> list = new ArrayList<MessageNoticeEntity>();
		String where = "";
		if (dealedStatus > -1) {
			where = " where " + DatabaseConstant.NoticeColumns.DEALED + "=" + dealedStatus;
		}
		String selectSql = " select * from " + DatabaseConstant.DBTableName.TABLE_NOTICES + where + " order by "
				+ DatabaseConstant.NoticeColumns.CREATE_TIME + " desc ";
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selectSql, null);
		if (Helper.isNotNull(cursor)) {
			while (cursor.moveToNext()) {
				MessageNoticeEntity notice = getEntity(cursor);
				if (Helper.isNotNull(notice)) {
					list.add(notice);
				}
			}
			cursor.close();
			cursor = null;
		}
		return list;
	}

	/**
	 * 查询所有
	 *
	 * @return
	 */
	public ArrayList<MessageNoticeEntity> getList() {
		return getList(-1);
	}
	/**
	 * 获取 未读取的最新 的一条通知
	 * @return
	 */
	public String getNewestContent(){
		String content = ActivityHelper.getGlobalApplicationContext().getString(R.string.no_get_system_notice);
		String selectSql = " select " + DatabaseConstant.NoticeColumns.TITLE + " from " + DatabaseConstant.DBTableName.TABLE_NOTICES + " where " + DatabaseConstant.NoticeColumns.READ + "=0" + " order by "
				+ DatabaseConstant.NoticeColumns.CREATE_TIME + " desc ";
		try{
			Cursor cursor = this.getSQLiteDatabase().rawQuery(selectSql, null);
			if (Helper.isNotNull(cursor)) {
				if (cursor.moveToFirst()) {
					content = cursor.getString(0);
				}
				cursor.close();
			}
		}catch(NullPointerException e){
			LogHelper.e(TAG, "数据库空指针异常");
		}catch(Exception e){
			LogHelper.e(TAG, "数据库操作异常");
		}
		return content;
	}
	/**
	 * 收到通知保存到本地
	 * @param notice
	 */
	public int add(MessageNoticeEntity notice) {
		boolean exist = isExist(notice.getBody(), notice.getFromCardId(), notice.getDealStatus());
		if (exist) {
			return update(notice);
		} else {
			return insert(notice);
		}
	}

	/**
	 * 接收到的透传消息添加到本地通知表
	 *
	 * @param msgParam
	 *            处理状态
	 */
	public void add(MessageResultEntity msgParam) {

		MessageNoticeEntity notice = new MessageNoticeEntity();
		notice.setCreateTime(msgParam.getCreateTime());
		notice.setContentType(msgParam.getContentType());
		notice.setLoseTime(msgParam.getLoseTime());
		notice.setFromCardId(msgParam.getFromCardId());
		notice.setFromAccountId(msgParam.getFromAccountId());
		notice.setFromHeadImg(msgParam.getFromHeadImg());
		notice.setFromName(msgParam.getFromName());
		notice.setToAccountId(msgParam.getToAccountId());
		notice.setTagId(msgParam.getTagId());
		notice.setBody(msgParam.getBody());

		switch (msgParam.getContentType()) {
			case Constants.MessageContentType.NOTICE_CARD_SWAP_REQUEST:
			case Constants.MessageContentType.NOTICE_SMS_RECOMMEND_SWAP_REQUEST:
			case Constants.MessageContentType.NOTICE_TRANSMIT_SWAP_REQUEST:
				notice.setDealStatus(DatabaseConstant.NoticeDealStatus.UNDEAL);
				notice.setTitle(mContext.getString(R.string.who_hope_to_swap_card, msgParam.getFromName()));
				break;
			case Constants.MessageContentType.NOTICE_TRANSMIT_ALLOW_REQUEST:
				notice.setDealStatus(DatabaseConstant.NoticeDealStatus.UNDEAL);
				notice.setTitle(msgParam.getBody());
				break;
			case Constants.MessageContentType.NOTICE_ENTERPRISE_ADD_REQUEST:
				notice.setDealStatus(DatabaseConstant.NoticeDealStatus.UNDEAL);
				notice.setTitle(msgParam.getBody());
				break;
			case Constants.MessageContentType.NOTICE_CIRCLE_GROUP_CREATE:
				String title = mContext.getString(R.string.who_invite_who_join_group_chat, msgParam.getFromName(), msgParam.getBody());
				notice.setTitle(title);
				break;
			default:
				notice.setTitle(msgParam.getBody());
				break;
		}

		add(notice);
	}

	/**
	 * 删除通知
	 * @param keyId
	 * @return
	 */
	public int delete(int keyId) {

		String where = "_id=" + keyId;
		return this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_NOTICES, where, null);
	}

	/**
	 * 批量删除通知
	 *
	 * @param ids
	 *            "," 拼接的集合
	 * @return
	 */
	public int delete(String ids) {
		String where = " _id in (" + ids + ")";
		return this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_NOTICES, where, null);
	}

	/**
	 * 更改处理 状态
	 *
	 * @param keyId
	 * @param dealed
	 */
	public void updateDealed(int keyId, int dealed) {
		String sql = "update " + DatabaseConstant.DBTableName.TABLE_NOTICES + " set " + DatabaseConstant.NoticeColumns.DEALED + "=" + dealed
				+ " where _id=" + keyId;
		try {
			this.getSQLiteDatabase().execSQL(sql);
		} catch (Exception e) {
			LogHelper.e(TAG, "更改处理状态异常");
		}
	}

	/**
	 * 更新已读状态
	 * @return
	 */
	public void updateReaded() {
		String sql = "update " + DatabaseConstant.DBTableName.TABLE_NOTICES + " set " + DatabaseConstant.NoticeColumns.READ + "=1 where "
				+ DatabaseConstant.NoticeColumns.READ + "=0";
		try {
			this.getSQLiteDatabase().execSQL(sql);
		} catch (Exception e) {
			LogHelper.e(TAG, "更改阅读状态异常");
		}
	}

	/**
	 * 获得未处理的通知条数
	 *
	 * @return
	 */
	public int getUnDealCount() {
		String sql = "select count(1) from " + DatabaseConstant.DBTableName.TABLE_NOTICES + " where " + DatabaseConstant.NoticeColumns.DEALED + " =0";
		Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
		int isDealed = 0;
		if (Helper.isNotNull(cursor)) {
			if (cursor.moveToFirst()) {
				isDealed = cursor.getInt(0);
			}
			cursor.close();
		}
		return isDealed;
	}

	/**
	 * 获得未读通知条数
	 *
	 * @return
	 */
	public int getUnReadCount() {
		int isRead = 0;
		String sql = "select count(1) from " + DatabaseConstant.DBTableName.TABLE_NOTICES + " where " + DatabaseConstant.NoticeColumns.READ + " =0";
		try{
			Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
			if (Helper.isNotNull(cursor)) {
				if (cursor.moveToFirst()) {
					isRead = cursor.getInt(0);
				}
				cursor.close();
			}
		}catch(NullPointerException e){
			LogHelper.e(TAG, "数据库空指针异常");
		}catch(Exception e){
			LogHelper.e(TAG, "数据库操作异常");
		}
		return isRead;
	}
	//endregion public

	//region private
	/**
	 * 给通知 对象赋值
	 * 
	 * @param cursor
	 * @return
	 */
	private MessageNoticeEntity getEntity(Cursor cursor) {
		MessageNoticeEntity notice = null;
		if (Helper.isNotNull(cursor)) {
			notice = new MessageNoticeEntity();

			Integer id = cursor.getInt(cursor.getColumnIndex(DatabaseConstant._ID));
			String title = cursor.getString(cursor.getColumnIndex(DatabaseConstant.NoticeColumns.TITLE));
			long cardId = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.NoticeColumns.FROM_CARD_ID));
			int type = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.NoticeColumns.CONTENT_TYPE));
			String createTime = cursor.getString(cursor.getColumnIndex(DatabaseConstant.NoticeColumns.CREATE_TIME));
			long loseTime = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.NoticeColumns.LOST_TIME));
			int isDealed = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.NoticeColumns.DEALED));
			String headImg = cursor.getString(cursor.getColumnIndex(DatabaseConstant.NoticeColumns.FROM_HEAD_IMG));
			String name = cursor.getString(cursor.getColumnIndex(DatabaseConstant.NoticeColumns.FROM_NAME));
			long otherId = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.NoticeColumns.FROM_ACCOUNT_ID));
			String msg = cursor.getString(cursor.getColumnIndex(DatabaseConstant.NoticeColumns.BODY));
			long tagId = cursor.getLong(cursor.getColumnIndex(DatabaseConstant.NoticeColumns.TAG_ID));
			int read = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.NoticeColumns.READ));
			
			notice.setTitle(title);
			notice.setCreateTime(createTime);
			notice.setContentType(type);
			notice.setLoseTime(loseTime);
			notice.setDealStatus(isDealed);
			notice.setId(id);
			notice.setFromCardId(cardId);
			notice.setBody(msg);
			notice.setFromHeadImg(headImg);
			notice.setFromName(name);
			notice.setFromAccountId(otherId);
			notice.setTagId(tagId);
			notice.setRead(read);
		}
		return notice;
	}

	/**
	 * 给notice 的 数据库对象赋值
	 * 
	 * @param notice
	 * @return
	 */
	private ContentValues getContentValues(MessageNoticeEntity notice) {
		ContentValues values = new ContentValues();
		if (Helper.isNotNull(notice)) {
			values.put(DatabaseConstant.NoticeColumns.TITLE, notice.getTitle());
			values.put(DatabaseConstant.NoticeColumns.FROM_CARD_ID, notice.getFromCardId());
			values.put(DatabaseConstant.NoticeColumns.CONTENT_TYPE, notice.getContentType());
			values.put(DatabaseConstant.NoticeColumns.LOST_TIME, notice.getLoseTime());
			values.put(DatabaseConstant.NoticeColumns.CREATE_TIME, notice.getCreateTime());
			values.put(DatabaseConstant.NoticeColumns.DEALED, notice.getDealStatus());
			values.put(DatabaseConstant.NoticeColumns.BODY, notice.getBody());
			values.put(DatabaseConstant.NoticeColumns.FROM_HEAD_IMG, notice.getFromHeadImg());
			values.put(DatabaseConstant.NoticeColumns.FROM_ACCOUNT_ID, notice.getFromAccountId());
			values.put(DatabaseConstant.NoticeColumns.FROM_NAME, notice.getFromName());
			values.put(DatabaseConstant.NoticeColumns.TAG_ID, notice.getTagId());
			values.put(DatabaseConstant.NoticeColumns.READ, notice.getRead());

		}
		return values;
	}

	/**
	 * 判断同一请求内容已经存在
	 * 
	 * @param messageContent
	 * @param fromCardId
	 * @return
	 */
	private boolean isExist(String messageContent, long fromCardId, int deal) {
		boolean exist = false;
		String selectSql = "select _id from " + DatabaseConstant.DBTableName.TABLE_NOTICES + " where " + DatabaseConstant.NoticeColumns.BODY + "='"
				+ messageContent + "'" + " and " + DatabaseConstant.NoticeColumns.FROM_CARD_ID + "=" + fromCardId + " and "
				+ DatabaseConstant.NoticeColumns.DEALED + "=" + deal;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(selectSql, null);
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
	 * 增
	 * 
	 * @param notice
	 * @return
	 */
	private int insert(MessageNoticeEntity notice) {
		ContentValues values = getContentValues(notice);
		return (int) this.getSQLiteDatabase().insert(DatabaseConstant.DBTableName.TABLE_NOTICES, "", values);
	}

	/**
	 * 重复发来的通知则更改为当前信息
	 * 
	 * @param notice
	 * @return
	 */
	private int update(MessageNoticeEntity notice) {
		ContentValues values = getContentValues(notice);
		String where = DatabaseConstant.NoticeColumns.BODY + "='" + notice.getBody() + "'" + " and " + DatabaseConstant.NoticeColumns.FROM_CARD_ID
				+ "=" + notice.getFromCardId() + " and " + DatabaseConstant.NoticeColumns.DEALED + "=" + notice.getDealStatus();
		return this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_NOTICES, values, where, null);
	}

	private SQLiteDatabase getSQLiteDatabase(){
		return VCardSQLiteDatabase.getInstance().getSQLiteDatabase();
	}
	//endregion private

}
