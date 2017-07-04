package com.maya.android.vcard.entity;

/**
 * 消息会话实体类
 * Created by Administrator on 2015/8/21.
 */
public class NewsConversation implements Cloneable{
    private String imgHead;//头像
    private String title;//会话标题
    private String content;//会话正文
    private String datetime;//创建时间
    private int type;//类型
    private int unreadCount;//未读消息数量
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

  public String getImgHead() {
        return imgHead;
    }

    public void setImgHead(String imgHead) {
        this.imgHead = imgHead;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
