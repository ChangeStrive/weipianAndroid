package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;
/**
 * 本地备份恢复日志表实体
 * @author zheng_cz
 * @since 2014年4月15日 下午2:42:50
 */
public class BackupLogEntity implements Cloneable{

	/**
	 * 备份标志值
	 */
	public static final int FLAG_BACKUP = 1;
	/**
	 * 恢复标志值
	 */
	public static final int FLAG_RECOVER = 2;
	/**
	 * 级别 我的名片、私密文件
	 */
	public static final int LEVEL_FIRST = 1;
	/**
	 * 级别2 朋友、同学
	 */
	public static final int LEVEL_SECOND = 2;
	/**
	 * 组类型 名片夹分组
	 */
	public static final int TYPE_VCARD = 1;
	/**
	 * 组类型 通讯录分组
	 */
	public static final int TYPE_LOCAL = 2;
	/**
	 * 组其他类型
	 */
	public static final int TYPE_OTHER = 0;
	/**
	 * 最后备份恢复时间
	 */
	@SerializedName("time")
	private String time;
	/**
	 * 备份恢复数量
	 */
	@SerializedName("num")
	private int num;
	/**
	 * 大小
	 */
	@SerializedName("size")
	private String size;
	/**
	 * 备份恢复项的名称
	 */
	@SerializedName("name")
	private String name;
	/**
	 * 备份/恢复标志
	 */
	@SerializedName("flag")
	private int flag;
	/**
	 * 内容级别
	 */
	@SerializedName("level")
	private int level;

	/**
	 * 1名片夹分组  2-通讯录分组
	 */
	@SerializedName("type")
	private int type;
	/**
	 * 名片时的分组id
	 */
	@SerializedName("childKey")
	private Integer childKey;
	/**
	 * 第一层的图标资源id
	 */
	@SerializedName("iconResId")
	private int iconResId;
	
	public BackupLogEntity(String name,int type,String time, int num, String size,int flag,int level) {
		this.time = time;
		this.num = num;
		this.size = size;
		this.name = name;
		this.flag = flag;
		this.level = level;
		this.type = type;
	}

	public BackupLogEntity(String name,int type,int num,int iconResId,int flag){
		this.name = name;
		this.num = num;
		this.iconResId = iconResId;
		this.type = type;
		this.flag = flag;
	}
	public BackupLogEntity(String name,int type,int num,int flag,String time){
		this.name = name;
		this.num = num;
		this.flag = flag;
		this.time = time;
		this.type = type;
	}
	public BackupLogEntity(){
		
	}
	public String getTime() {

		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level){
		this.level = level;
	}
	public int getIconResId() {
		return iconResId;
	}

	public void setIconResId(int iconResId) {
		this.iconResId = iconResId;
	}

	public Integer getChildKey() {
		return childKey;
	}

	public void setChildKey(Integer childKey) {
		this.childKey = childKey;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
