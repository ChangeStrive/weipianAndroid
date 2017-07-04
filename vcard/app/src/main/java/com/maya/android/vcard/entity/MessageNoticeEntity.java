package com.maya.android.vcard.entity;

import com.maya.android.vcard.entity.result.MessageResultEntity;
/**
 * 通知实体类
 * @author zheng_cz
 * @since 2014年3月29日 下午2:37:11
 */
public class MessageNoticeEntity extends MessageResultEntity {

	private Integer id;
	/**
	 * 处理状态   0未处理   1 已同意  2 已拒绝  3已批量处理
	 */
	private int dealStatus;
	
	/**
	 * 是否已读 0未读 1已读
	 */
	private int read;
	/** 列表显示时的标题   **/
	private String title;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(int dealStatus) {
		this.dealStatus = dealStatus;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
