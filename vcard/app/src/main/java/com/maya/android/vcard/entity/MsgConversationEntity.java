package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 消息会话实体类
 * Created by Administrator on 2015/9/9.
 */
public class MsgConversationEntity {
    private long id;
    /** 我的名片id **/
    @SerializedName("myCardId")
    private long myCardId;
    /*** 已读/未读状态 0-未读 1-已读-*/
    @SerializedName("read")
    private int read;
    /**- 发送类型  0-表示已接收 1-表示已发送-*/
    @SerializedName("sendType")
    private int sendType;
    /**-* 是否发送失败 true:表示发送失败 false:表示发送成功 **/
    @SerializedName("isSendFail")
    private boolean isSendFail;
    /** 文件的本地路径 */
    @SerializedName("filePath")
    private String filePath;
    /** 是否可操作  0-可以 1-不可以 **/
    @SerializedName("enable")
    private int enable;
    /** 是否黑名单 消息 */
    @SerializedName("isBlack")
    private boolean isBlack;
    /** 当前帐户未读消息数量 (显示用) */
    @SerializedName("unreadCount")
    private int unreadCount;
    /** 联系人帐户ID */
    @SerializedName("toAccountId")
    private long toAccountId;
    /*** 来源名片id */
    @SerializedName("fromCardId")
    private long fromCardId;
    /**= 根据类型变动的 id
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
     * 63-名片数量同步：tagId为推送给谁的名片id */
    @SerializedName("type")
    private int type;
    /**=* 消息体=*/
    @SerializedName("data")
    private String body;
    /**=*  消息时间= */
    @SerializedName("sendDate")
    private String createTime;
    /**=*  失效时间 * */
    @SerializedName("loseTime")
    private long loseTime;
    /*** 来源帐户id */
    @SerializedName("fromAccountId")
    private long fromAccountId;
    /*** 名称 */
    @SerializedName("fromName")
    private String fromName;
    /*** 头像*/
    @SerializedName("fromHeadImg")
    private String fromHeadImg;
    /** 根据类型变动的 id
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
     * 63-名片数量同步：tagId为推送给谁的名片id*/
    @SerializedName("tagId")
    private long tagId;
    /** 处理结果  0-成功 1-拒绝/忽略/不通过**/
    @SerializedName("result")
    private int result;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMyCardId() {
        return myCardId;
    }

    public void setMyCardId(long myCardId) {
        this.myCardId = myCardId;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public boolean isSendFail() {
        return isSendFail;
    }

    public void setIsSendFail(boolean isSendFail) {
        this.isSendFail = isSendFail;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public void setIsBlack(boolean isBlack) {
        this.isBlack = isBlack;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public long getFromCardId() {
        return fromCardId;
    }

    public void setFromCardId(long fromCardId) {
        this.fromCardId = fromCardId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getLoseTime() {
        return loseTime;
    }

    public void setLoseTime(long loseTime) {
        this.loseTime = loseTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
