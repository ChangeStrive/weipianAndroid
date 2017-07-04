package com.maya.android.vcard.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.util.ResourceHelper;

import java.io.File;

/**
 * 本地数据库
 * 
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-3-18
 * 
 */
public class VCardSQLiteDatabase {
    // #region 常量
    private static final String TAG = VCardSQLiteDatabase.class.getSimpleName();
    /** 数据库文件 **/
    private static final String DB_NAME = "data.db";
    /** 数据库初始化版本 **/
    private static final int VERSION_DATABASE_INIT = 0;
    /** 数据库版本1 **/
    private static final int VERSION_DATABASE_ONE = 1;
    /** 数据库版本2 **/
    private static final int VERSION_DATABASE_TWO = 2;
    /** 数据库版本3 **/
    private static final int VERSION_DATABASE_THREE = 3;
    /** 数据当前版本 **/
    private static final int VERSION_DATABASE_CURRENT = VERSION_DATABASE_THREE;
    // #endregion 常量

    // #region 静态成员变量
    private static VCardSQLiteDatabase sInstance;
    // #endregion 静态成员变量

    // #region 成员变量
    private SQLiteDatabase mSQLiteDatabase;
    private String mCurrentDatabasePath = "";
    // #endregion 成员变量

    // #region Getter & Setter集
    public static VCardSQLiteDatabase getInstance() {
		if (Helper.isNull(sInstance)) {
		    sInstance = new VCardSQLiteDatabase();
		}
		return sInstance;
    }

    public SQLiteDatabase getSQLiteDatabase() {
    	if(Helper.isNull(this.mSQLiteDatabase)){
    		this.initSQLiteDatabase(this.mCurrentDatabasePath);
    	}
    	return mSQLiteDatabase;
    }

    // #endregion Getter & Setter集

    // #region 构造方法
    private VCardSQLiteDatabase() {
    }

    // #endregion 构造方法

    // #region public 方法
    /**
     * 初始化数据库
     * 
     * @param databasePath
     */
    public void initSQLiteDatabase(String databasePath) {
		if (ResourceHelper.isNotEmpty(databasePath)) {
		    if (!databasePath.contains(DB_NAME)) {
				File file = new File(databasePath);
				if (!file.exists()) {
				    file.mkdirs();
				}
				databasePath += DB_NAME;
		    }
//		    synchronized (databasePath) {
//		    	if(Helper.isNotNull(mCurrentDatabasePath)){
		    		if(!this.mCurrentDatabasePath.equals(databasePath) || Helper.isNull(this.mSQLiteDatabase)){
			    		this.mCurrentDatabasePath = databasePath;
						LogHelper.d(TAG, "数据库地址：" + this.mCurrentDatabasePath);
			    		this.mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
			    		int version = this.mSQLiteDatabase.getVersion();
			    		if (version != VERSION_DATABASE_CURRENT) {
			    			//数据库初始化(一步到位)
			    			if (version == VERSION_DATABASE_INIT) {
			    				createTable();
			    				//			    initTable();
			    				// 初始化本地自带名片模板
			    				//			    VCardTemplateDao.getInstance().initLocalVCardTemplate();
			    				version = VERSION_DATABASE_CURRENT;
			    			}
			    			//数据库 更新微片小秘书资料   版本：VERSION_DATABASE_TWO
			    			if(version < VERSION_DATABASE_CURRENT){
			    				this.updateDBVersion2();
			    				version = VERSION_DATABASE_TWO;
			    			}
			    			//联系人表 更新 微片小秘书 公司资料   版本：VERSION_DATABASE_THREE
			    			if(version < VERSION_DATABASE_CURRENT){
			    				this.updateDBVersion3();
			    				version = VERSION_DATABASE_THREE;
			    			}
			    			this.mSQLiteDatabase.setVersion(VERSION_DATABASE_CURRENT);
			    		}
//			    		this.updateDBVersion2();
//			    		this.updateDBVersion3();
			    	}
	    			//判断字段是否存在
//	    			boolean isCol = columnIsExist(DatabaseConstant.DBTableName.TABLE_ENTERPRISES, DatabaseConstant.EnterpriseColumns.ENTERPRISE_WEB);
//	    			if(tableIsExist(DatabaseConstant.DBTableName.TABLE_ENTERPRISES) && !isCol){
//	    				try {
//	    					getSQLiteDatabase().execSQL("ALTER TABLE " + DatabaseConstant.DBTableName.TABLE_ENTERPRISES + " ADD " + DatabaseConstant.EnterpriseColumns.ENTERPRISE_WEB + " TEXT");
//						}catch (Exception e) {
//							LogHelper.e(TAG, "Add enterprise_web error");
//						}
//	    			}
		    		
//		    	}
//			}
		} else {
			LogHelper.e(TAG, "the databasePath is NULL!!!");
		}
    }
    /**
     * 关闭数据库
     */
    public void closeSQLiteDatabase(){
    	if(Helper.isNotNull(this.mSQLiteDatabase)){
    		this.mCurrentDatabasePath = "";
    		if(!this.mSQLiteDatabase.isDbLockedByCurrentThread()){
	    		this.mSQLiteDatabase.close();
	    		this.mSQLiteDatabase = null;
    		}
    	}
    }
    /**
     * 获取自增键值得最新值
     * 
     * @param tableName
     * @throws Exception
     * @return long
     * @autho zheng_cz 2013-07-17
     */
    public long getCurSequence(String tableName) throws Exception {
		long seq = 0;
		String seqSql = " select seq from sqlite_sequence where name='" + tableName + "'";
		Cursor seqCursor = this.mSQLiteDatabase.rawQuery(seqSql, null);
		seqCursor.moveToFirst();
		seq = seqCursor.getLong(0);
		seqCursor.close();
		return seq;
    }
    /**
     * 判断表是否存在
     * 
     * @param tableName
     * @return
     */
    public boolean tableIsExist(String tableName) {
		boolean result = false;
		if (Helper.isEmpty(tableName)) {
		    return result;
		}
		Cursor cursor = null;
		try {
		    String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='" + tableName.trim() + "' ";
		    cursor = getSQLiteDatabase().rawQuery(sql, null);
		    if (cursor.moveToNext()) {
			int count = cursor.getInt(0);
			if (count > 0) {
			    result = true;
			}
		    }
		    cursor.close();
		    cursor = null;
	
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return result;
    }
    /**
     * 判断表的某字段是否存在
     * @param tableName
     * @param columnName
     * @return
     */
    public boolean columnIsExist(String tableName, String columnName){
    	boolean result = false ;
        Cursor cursor = null ;
        try{
            //查询一行
            cursor = getSQLiteDatabase().rawQuery( "SELECT * FROM " + tableName + " LIMIT 0", null );
            result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
        }catch (Exception e){
             LogHelper.e(TAG, "判断表字段列存在出错") ;
        }finally{
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }

        return result ;
    }
    // #endregion public 方法

    // #region private 方法
    /**
     * 创建表
     */
    private void createTable() {
		if(!tableIsExist(DatabaseConstant.DBTableName.TABLE_MY_ACCOUNT)){
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.CREATE_TABLE_MY_ACCOUNT);
		}
		if(!tableIsExist(DatabaseConstant.DBTableName.TABLE_CARD_TEMPLATES)){
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.CREATE_TABLE_CARD_TEMPLATES);
	
			    //TODO 初始化本地自带名片模板
//			    VCardTemplateDao.getInstance().initLocalVCardTemplate();
		}
		if(!tableIsExist(DatabaseConstant.DBTableName.TABLE_CONTACT_GROUPS)){
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.CREATE_TABLE_CONTACT_GROUPS);
			// 插入默认数据
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.INSERT_CONTACT_GROUP1);
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.INSERT_CONTACT_GROUP2);
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.INSERT_CONTACT_GROUP3);
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.INSERT_CONTACT_GROUP4);
		}
		if(!tableIsExist(DatabaseConstant.DBTableName.TABLE_CONTACTS)){
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.CREATE_TABLE_CONTACTS);
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.INSERT_CONTACT_MYTIP);
		}
		if(!tableIsExist(DatabaseConstant.DBTableName.TABLE_MESSAGES)){
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.CREATE_TABLE_MESSAGES);
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.INSERT_MESSAGE_MYTIP1);
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.INSERT_MESSAGE_MYTIP2);
		}
		if(!tableIsExist(DatabaseConstant.DBTableName.TABLE_ENTERPRISES)){
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.CREATE_TABLE_ENTERPRISES);
		}
	
		if(!tableIsExist(DatabaseConstant.DBTableName.TABLE_ENTERPRISE_MEMBERS)){
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.CREATE_TABLE_ENTERPRISE_MEMBERS);
		}
	
		if(!tableIsExist(DatabaseConstant.DBTableName.TABLE_NOTICES)){
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.CREATE_TABLE_NOTICES);
		}
		if(!tableIsExist(DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUPS)){
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.CREATE_TABLE_CIRCLE_GROUPS);
		}
		if(!tableIsExist(DatabaseConstant.DBTableName.TABLE_CIRCLE_GROUP_MEMBERS)){
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.CREATE_TABLE_CIRCLE_GROUP_MEMBERS);
		}
		if(!tableIsExist(DatabaseConstant.DBTableName.TABLE_BACKUP_LOGS)){
			getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.CREATE_TABLE_BACKUP_LOGS);
		}
    }
    /**
     * 更新微片小秘书资料
     */
    private void updateDBVersion2(){
    	if(tableIsExist(DatabaseConstant.DBTableName.TABLE_CONTACTS)){
			//更新微片小秘书资料
//    		getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.DELETE_CONTACT_MYTIP);
    		getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.UPDATE_CONTACT_MYTIP);
		}
    }
    /**
     * 联系人表 更新 微片小秘书 公司资料
     */
    private void updateDBVersion3(){
    	if(tableIsExist(DatabaseConstant.DBTableName.TABLE_CONTACTS)){
    		getSQLiteDatabase().execSQL(DatabaseConstant.initDatabaseSQL.UPDATE_CONTACT_MYTIP_INFO);
    	}
    }
    // #endregion private 方法
}
