package com.maya.android.vcard.entity;

import android.content.Context;
import android.util.SparseArray;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;

/**
 * 通讯录分组对象实体
 * 
 * @Version: 1.0
 * @author: zheng_cz
 * @since: 2013-7-4 下午2:11:17
 */
public class ContactGroupEntity {

	public static final int GROUP_FAVORITE_ID = -1; // 收藏夹id
	public static final int GROUP_BLACKLIST_ID = -2; // 黑名单分组id
	public static final int GROUP_RECENTCONTACT_ID = -3; // 最近联系
	public static final int GROUP_UNGROUPED_ID = 0; // 未分组id
	public static final int GROUP_ALL_ID = -10;// 全部分组id

	private int id;
	private String name;
	/**
	 * 是否可编辑
	 */
	private boolean isEnable = false;
	/**
	 * 图标名称 或者 资源id
	 */
	private String iconName;
	/**
	 * 排序值
	 */
	private int orderCode = 1;
	private String description;
	/**
	 * 最后更新时间
	 */
	private String lastUpdateTime;
	/**
	 * 分组下的人数
	 */
	private int itemCount = 0;
	/**
	 * 是否删除
	 */
	private boolean isDelete = false;

	public ContactGroupEntity() {

	}

	public ContactGroupEntity(int id, String name, int itemCount, String iconName) {
		this.id = id;
		this.name = name;
		this.itemCount = itemCount;
		this.iconName = iconName;
	}

	public ContactGroupEntity(String name, int itemCount, String lastUpdateTime, int iconResId) {
		this.name = name;
		this.itemCount = itemCount;
		this.lastUpdateTime = lastUpdateTime;
		this.iconName = iconResId + "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public int getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * 转化为备份的分组向
	 * 
	 * @return
	 */
	public ContactGroupBackupEntity getGroupBackupEntity() {
		ContactGroupBackupEntity groupBackupEntity = new ContactGroupBackupEntity();
		groupBackupEntity.setContactCount(itemCount);
		groupBackupEntity.setGroupId(id);
		groupBackupEntity.setName(name);
		groupBackupEntity.setOrderCode(orderCode);
		return groupBackupEntity;
	}

	/**
	 * 获取默认固定分组
	 * 
	 * @return SparseArray<ContactGroupEntity>
	 */
	public static SparseArray<ContactGroupEntity> getFixedGroup() {
		Context mContext = ActivityHelper.getGlobalApplicationContext();
		SparseArray<ContactGroupEntity> defGroups = new SparseArray<ContactGroupEntity>();

		defGroups.put(ContactGroupEntity.GROUP_RECENTCONTACT_ID,
				new ContactGroupEntity(ContactGroupEntity.GROUP_RECENTCONTACT_ID, mContext.getString(R.string.recent_contact), 1, null));
		defGroups.put(ContactGroupEntity.GROUP_FAVORITE_ID,
				new ContactGroupEntity(ContactGroupEntity.GROUP_FAVORITE_ID, mContext.getString(R.string.favorite), 0, null));
		defGroups.put(ContactGroupEntity.GROUP_UNGROUPED_ID,
				new ContactGroupEntity(ContactGroupEntity.GROUP_UNGROUPED_ID, mContext.getString(R.string.ungrouped), 0, null));
		defGroups.put(ContactGroupEntity.GROUP_BLACKLIST_ID,
				new ContactGroupEntity(ContactGroupEntity.GROUP_BLACKLIST_ID, mContext.getString(R.string.blacklist), 0, null));
		return defGroups;
	}
}
