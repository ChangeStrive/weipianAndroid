package com.maya.android.vcard.entity;

/**
 * 选择对话框 带Icon图标信息实体类
 * Created by Administrator on 2015/9/6.
 */
public class CustomLsvIntegerEntity implements Cloneable{
    private int contentId;

    public CustomLsvIntegerEntity(){

    }

    public CustomLsvIntegerEntity(int contentId){
        this.contentId = contentId;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
