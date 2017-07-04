package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

public class MessageResultEntity {
	/**
	 * 联系人帐户ID
	 */
	@SerializedName("toAccountId")
	private long toAccountId;

	/**
	 * 来源名片id
	 */
	@SerializedName("fromCardId")
	private long fromCardId;
	/**
	 * 根据类型变动的 id
	 * 11-文本会话：tagId为空
	 * 12-文件消息：tagId为空
	 * 13-群聊文本消息：tagId为群聊组Id
	 * 14-群聊文件消息：tagId为群聊组Id

	 * 21-云端交换名片请求通知：tagId为空, data(来自附近的朋友/来自手机联系人/来自云端		查找)
	 * 22-云端交换名片请求反馈：tagId为空 （成功：系统无通知显示，会话窗显示开始聊天。 	失败：系统通知显示）
	 * 27-扫一扫交换结果通知tagId为空 （被扫描者更新到交换ui的通知）

	 * 31-单位成员加入请求：tagId为单位ID, fromCardId申请人名片ID，fromAccountId申请人帐户ID，fromHeadImg为申请人头像，fromName为申请人名字，toAccountId为空。
	 * 32-单位(加人\踢人)消息通知：tagId为单位ID，fromCardId为空，fromAccountId为空，fromHeadImg为单位头像，fromName为单位名字
	 * 33-群聊成员加入，退出的消息通知类型:fromCardId为空，tagId为群组ID，群聊成员加入，退出的情况fromAccountId，fromName、fromHeadImg为该用户相关信息（解散群为空）。
	 * 34-单位是否通过认证的通知：tagId为企业ID，fromCardId、fromAccountId、fromName、	fromHeadImg为空
	 * 35-群聊组名称更改的通知：fromCardId、fromAccountId、fromHeadImg为空，tagId为群	组ID，fromName为更改后的组名称。
	 * 36-群聊组(创建?)解散 通知： tagId 为群聊组id，data （解散时为空）

	 * 41-转发名片请求允许的通知：tagId为转发给谁（接收者）的帐户ID  （同意：发送43 给	名片接收者，拒绝：请求者接收到通知 42）
	 * 42-转发名片请求允许的反馈：tagId为空 
	 * 43-转发名片确认通知（被转发者请求与接收者交换名片）：tagId为发起转发请求的帐户	id （同意/拒绝 也都发45 给中间人）
	 * 44-转发名片确认反馈：tagId为空 （成功：系统无通知显示，会话窗显示开始聊天。 		失败：系统通知显示）
	 * 45-转发名片确认反馈给发起转发请求的中间人：

	 * 61-短信推荐云端交换好友请求: tagId为推送给谁的名片id
	 * 62-短信推荐的云端交换成功通知
	 * 63-名片数量同步：tagId为推送给谁的名片id

	 */
	@SerializedName("type")
	private int contentType;
	/**
	 * 消息体
	 */
	@SerializedName("data")
	private String body;
	/**
	 *  消息时间
	 */
	@SerializedName("sendDate")
	private String createTime;
	/**
	 *  失效时间
	 */
	@SerializedName("loseTime")
	private long loseTime;
	/**
	 * 来源帐户id
	 */
	@SerializedName("fromAccountId")
	private long fromAccountId;
	/**
	 * 名称
	 */
	@SerializedName("fromName")
	private String fromName;
	/**
	 * 头像
	 */
	@SerializedName("fromHeadImg")
	private String fromHeadImg;
	/**
	 * 根据类型变动的 id
	 * 11-文本会话：tagId为空
	 * 12-文件消息：tagId为空
	 * 13-群聊文本消息：tagId为群聊组Id
	 * 14-群聊文件消息：tagId为群聊组Id

	 * 21-云端交换名片请求通知：tagId为空, data(来自附近的朋友/来自手机联系人/来自云端		查找)
	 * 22-云端交换名片请求反馈：tagId为空 （成功：系统无通知显示，会话窗显示开始聊天。 	失败：系统通知显示）
	 * 27-扫一扫交换结果通知tagId为空 （被扫描者更新到交换ui的通知）

	 * 31-单位成员加入请求：tagId为单位ID, fromCardId申请人名片ID，fromAccountId申请人帐户ID，fromHeadImg为申请人头像，fromName为申请人名字，toAccountId为空。
	 * 32-单位(加人\踢人)消息通知：tagId为单位ID，fromCardId为空，fromAccountId为空，fromHeadImg为单位头像，fromName为单位名字
	 * 33-群聊成员加入，退出的消息通知类型:fromCardId为空，tagId为群组ID，群聊成员加入，退出的情况fromAccountId，fromName、fromHeadImg为该用户相关信息（解散群为空）。
	 * 34-单位是否通过认证的通知：tagId为企业ID，fromCardId、fromAccountId、fromName、	fromHeadImg为空
	 * 35-群聊组名称更改的通知：fromCardId、fromAccountId、fromHeadImg为空，tagId为群	组ID，fromName为更改后的组名称。
	 * 36-群聊组(创建?)解散 通知： tagId 为群聊组id，data （解散时为空）

	 * 41-转发名片请求允许的通知：tagId为转发给谁（接收者）的帐户ID  （同意：发送43 给	名片接收者，拒绝：请求者接收到通知 42）
	 * 42-转发名片请求允许的反馈：tagId为空 
	 * 43-转发名片确认通知（被转发者请求与接收者交换名片）：tagId为发起转发请求的帐户	id （同意/拒绝 也都发45 给中间人）
	 * 44-转发名片确认反馈：tagId为空 （成功：系统无通知显示，会话窗显示开始聊天。 		失败：系统通知显示）
	 * 45-转发名片确认反馈给发起转发请求的中间人：

	 * 61-短信推荐云端交换好友请求: tagId为推送给谁的名片id
	 * 62-短信推荐的云端交换成功通知
	 * 63-名片数量同步：tagId为推送给谁的名片id

	 */
	@SerializedName("tagId")
	private long tagId;
	
	/** 处理结果  0-成功 1-拒绝/忽略/不通过**/
	@SerializedName("result")
	private int result;
	
	public long getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(long acctId) {
		this.toAccountId = acctId;
	}

	public long getFromCardId() {
		return fromCardId;
	}

	public void setFromCardId(long fromCardID) {
		this.fromCardId = fromCardID;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public long getLoseTime() {
		return loseTime;
	}

	public void setLoseTime(long loseTime) {
		this.loseTime = loseTime;
	}

	public long getFromAccountId() {
		return fromAccountId;
	}

	public void setFromAccountId(long fromAccountId) {
		this.fromAccountId = fromAccountId;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromHeadImg() {
		return fromHeadImg;
	}

	public void setFromHeadImg(String fromHeadImg) {
		this.fromHeadImg = fromHeadImg;
	}

	public long getTagId() {
		return tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
	
}
