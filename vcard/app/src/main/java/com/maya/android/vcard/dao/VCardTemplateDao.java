package com.maya.android.vcard.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.data.VCardSQLiteDatabase;
import com.maya.android.vcard.entity.CardModeEntity;
import com.maya.android.vcard.entity.json.VcardTemplateLocalJsonEntity;
import com.maya.android.vcard.entity.result.VCardTemplateResultEntity;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * DAO：名片模板
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-3-23
 *
 */
public class VCardTemplateDao {

	//region 成员变量
	private static final String TAG = VCardTemplateDao.class.getSimpleName();
	public static final String SPLIT_STRING_TO_CHANGE = "\\\"";
	public static final String SPLIT_STRING_FROM_CHANGE = "\"";

	private String mInsertString = "insert into " + DatabaseConstant.DBTableName.TABLE_CARD_TEMPLATES
			+ "(" + DatabaseConstant.CardTemplateColumns.MODE_ID + ","
			+ DatabaseConstant.CardTemplateColumns.MODE_NAME + ","
			+ DatabaseConstant.CardTemplateColumns.MODE_TYPE + ","
			+ DatabaseConstant.CardTemplateColumns.IS_VERTICAL + ","
			+ DatabaseConstant.CardTemplateColumns.SHOW_IMG+ ","
			+ DatabaseConstant.CardTemplateColumns.SHOW_IMG_BACK + ","
			+ DatabaseConstant.CardTemplateColumns.CARD_MODE_ENTITY + ","
			+ DatabaseConstant.CardTemplateColumns.CLASS_ID + ","
			+ DatabaseConstant.CardTemplateColumns.HAS_HEAD_IMG + ","
			+ DatabaseConstant.CardTemplateColumns.HAS_LOGO + ","
			+ DatabaseConstant.CardTemplateColumns.IS_ONLINE
			+") VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	//endregion 成员变量

	//region 单例
	private static VCardTemplateDao sInstance;
	public static VCardTemplateDao getInstance(){
		if(Helper.isNull(sInstance)){
			sInstance = new VCardTemplateDao();
		}
		return sInstance;
	}
	private VCardTemplateDao(){}
	//endregion 单例


	//region public 方法
	/**
	 * 初始化名片模板
	 */
	public void initLocalVCardTemplate(){
		//本地模板表存在，进行创建表
		String localVcardTemplateStr = ResourceHelper.getStringFromRawFile(R.raw.local_card_template);
		if(Helper.isNotEmpty(localVcardTemplateStr)){
			VcardTemplateLocalJsonEntity jsonEnity = JsonHelper.fromJson(localVcardTemplateStr, VcardTemplateLocalJsonEntity.class);
			if(Helper.isNotNull(jsonEnity) && Helper.isNotNull(jsonEnity.getTempList())){
				for (VCardTemplateResultEntity entity : jsonEnity.getTempList()) {
					insert(entity);
				}
			}
		}
	}
	/**
	 * 取得本地模板
	 */
	public ArrayList<VCardTemplateResultEntity> getLocalVcardTemplate(){
		String sql = "select * from " + DatabaseConstant.DBTableName.TABLE_CARD_TEMPLATES;
		Cursor cursor = null;
		try {
			cursor = this.getSQLiteDatabase().rawQuery(sql, null);
		} catch (Exception e) {
			cursor = null;
		}
		ArrayList<VCardTemplateResultEntity> result = null;
		if(Helper.isNotNull(cursor)){
			if(cursor.moveToNext()){
				result = new ArrayList<VCardTemplateResultEntity>();
				int cardModeEntityColumn = cursor.getColumnIndex(DatabaseConstant.CardTemplateColumns.CARD_MODE_ENTITY);
				int idColumn = cursor.getColumnIndex(DatabaseConstant.CardTemplateColumns.MODE_ID);
				int modeNameColumn = cursor.getColumnIndex(DatabaseConstant.CardTemplateColumns.MODE_NAME);
				int modeTypeColumn = cursor.getColumnIndex(DatabaseConstant.CardTemplateColumns.MODE_TYPE);
				int verticalColumn = cursor.getColumnIndex(DatabaseConstant.CardTemplateColumns.IS_VERTICAL);
				int onlineColumn = cursor.getColumnIndex(DatabaseConstant.CardTemplateColumns.IS_ONLINE);
				int showImgColumn = cursor.getColumnIndex(DatabaseConstant.CardTemplateColumns.SHOW_IMG);
				int showImgBackColumn = cursor.getColumnIndex(DatabaseConstant.CardTemplateColumns.SHOW_IMG_BACK);
				int classColumn = cursor.getColumnIndex(DatabaseConstant.CardTemplateColumns.CLASS_ID);
				do{
					VCardTemplateResultEntity entity = new VCardTemplateResultEntity();
					entity.setId(cursor.getLong(idColumn));
					entity.setStyle(JsonHelper.fromJson(cursor.getString(cardModeEntityColumn), CardModeEntity.class));
					entity.setName(cursor.getString(modeNameColumn));
					entity.setType(cursor.getInt(modeTypeColumn));
					entity.setShowImage(cursor.getString(showImgColumn));
					entity.setShowImageBack(cursor.getString(showImgBackColumn));
					entity.setClassId(cursor.getInt(classColumn));
					entity.setVertical(ResourceHelper.int2Boolean(cursor.getInt(verticalColumn)));
					entity.setOnline(ResourceHelper.int2Boolean(cursor.getInt(onlineColumn)));
					result.add(entity);
				}while(cursor.moveToNext());
				return result;
			}
		}
		return result;
	}
	/**
	 * 取得模板样式数据
	 * @param templateId
	 * @return
	 */
	public CardModeEntity getTemplateCardModeEntity(long templateId){
		CardModeEntity result = null;
		String sql = "select " + DatabaseConstant.CardTemplateColumns.CARD_MODE_ENTITY + " from " + DatabaseConstant.DBTableName.TABLE_CARD_TEMPLATES + " where " + 
				DatabaseConstant.CardTemplateColumns.MODE_ID + " = " + templateId;
		Cursor cursor = this.getSQLiteDatabase().rawQuery(sql, null);
		if(Helper.isNotNull(cursor)){
			if(cursor.moveToNext()){
				String cardModeStype = cursor.getString(cursor.getColumnIndex(DatabaseConstant.CardTemplateColumns.CARD_MODE_ENTITY));
				if(Helper.isNotEmpty(cardModeStype)){
					result = JsonHelper.fromJson(cardModeStype, CardModeEntity.class);
				}
			}
		}
		return result;
	}
	/**
	 * 添加 线上 下载的模板
	 * @param entity
	 */
	public void add(VCardTemplateResultEntity entity){
		entity.setOnline(true);
		boolean exist = isExist(entity.getId());
		if(exist){
			update(entity);
		}else{
			insert(entity);
		}
	}
	/**
	 * 添加 下载模板列表
	 * @param templateList
	 */
	public void add(ArrayList<VCardTemplateResultEntity> templateList){
		if(Helper.isNotNull(templateList)){
			for (int i = 0, size = templateList.size(); i < size; i++) {
				add(templateList.get(i));
			}
		}
	}
	/**
	 * 名片模板是否已存在
	 * @param modeId
	 * @return
	 */
	public boolean isExist(long modeId){
		boolean exist = false;
		if(modeId < 1){
			return false;
		}
		String sql = "select * from ".concat(DatabaseConstant.DBTableName.TABLE_CARD_TEMPLATES).concat(" where ").concat(DatabaseConstant.CardTemplateColumns.IS_ONLINE)
				.concat("=1 and ").concat(DatabaseConstant.CardTemplateColumns.MODE_ID).concat("=" + modeId);
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
			LogHelper.e(TAG, "数据库对象未创建");
		}
		return exist;
	}
	/**
	 * 获取本地模板数量
	 * @return
	 */
	public int getTemplateCount(){
		int count = 0;
		String sql = " select count(*) from ".concat(DatabaseConstant.DBTableName.TABLE_CARD_TEMPLATES);
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
			LogHelper.e(TAG, "数据库对象未创建");
		}
		return count;
	}
	/**
	 * 删除名片模板
	 * @param modeId
	 */
	public void delete(long modeId, boolean isOnline){
		String where = DatabaseConstant.CardTemplateColumns.MODE_ID.concat("=" + modeId).concat(" and ").concat(DatabaseConstant.CardTemplateColumns.IS_ONLINE);
		if(isOnline){
			where.concat(" = 1 ");
		}else{
			where.concat("= 0");
		}
		this.getSQLiteDatabase().delete(DatabaseConstant.DBTableName.TABLE_CARD_TEMPLATES, where, null);
	}
	//endregion public 方法

	//region private 方法
	private void insert(VCardTemplateResultEntity entity){
		Object[] objectArray = new Object[]{
				entity.getId(),
				entity.getName(),
				entity.getType(),
				entity.isVertical(),
				entity.getShowImage(),
				entity.getShowImageBack(),
				JsonHelper.toJson(entity.getStyle(), CardModeEntity.class),
				entity.getClassId(),
				entity.isHasHeadImg(),
				entity.isHasLogo(),
				entity.isOnline()
		};
		try{
			this.getSQLiteDatabase().execSQL(mInsertString, objectArray);
		}catch(NullPointerException e){
			LogHelper.e(TAG, "数据库对象未创建");
		}
	}
	/**
	 * 修改下载的模板
	 * @param tempEntity
	 */
	private void update(VCardTemplateResultEntity tempEntity){
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.CardTemplateColumns.MODE_NAME, tempEntity.getName());
		values.put(DatabaseConstant.CardTemplateColumns.MODE_TYPE, tempEntity.getType());
		values.put(DatabaseConstant.CardTemplateColumns.CLASS_ID, tempEntity.getClassId());
		values.put(DatabaseConstant.CardTemplateColumns.IS_VERTICAL, tempEntity.isVertical());
		values.put(DatabaseConstant.CardTemplateColumns.IS_ONLINE, tempEntity.isOnline());
		values.put(DatabaseConstant.CardTemplateColumns.SHOW_IMG, tempEntity.getShowImage());
		values.put(DatabaseConstant.CardTemplateColumns.SHOW_IMG_BACK, tempEntity.getShowImageBack());
		values.put(DatabaseConstant.CardTemplateColumns.CARD_MODE_ENTITY, JsonHelper.toJson(tempEntity.getStyle()));
		String[] whereArgs = new String[]{
				tempEntity.getId()+""
		};
		try{
			this.getSQLiteDatabase().update(DatabaseConstant.DBTableName.TABLE_CARD_TEMPLATES, values, DatabaseConstant.CardTemplateColumns.MODE_ID.concat("=?"), whereArgs);
		}catch(NullPointerException e){
			LogHelper.e(TAG, "数据库对象未创建");
		}
	}
	
	private SQLiteDatabase getSQLiteDatabase(){
		return VCardSQLiteDatabase.getInstance().getSQLiteDatabase();
	}
	//endregion private 方法

}
