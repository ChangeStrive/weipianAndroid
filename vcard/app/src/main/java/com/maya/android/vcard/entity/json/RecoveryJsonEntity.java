package com.maya.android.vcard.entity.json;

import java.util.ArrayList;

/**
 * 备份恢复实体类
 * Created by Administrator on 2015/11/2.
 */
public class RecoveryJsonEntity {
   private ArrayList<String> cardGroupNameList;
   private ArrayList<String> bookGroupNameList;

    public ArrayList<String> getCardGroupNameList() {
        return cardGroupNameList;
    }

    public void setCardGroupNameList(ArrayList<String> cardGroupNameList) {
        this.cardGroupNameList = cardGroupNameList;
    }

    public ArrayList<String> getBookGroupNameList() {
        return bookGroupNameList;
    }

    public void setBookGroupNameList(ArrayList<String> bookGroupNameList) {
        this.bookGroupNameList = bookGroupNameList;
    }
}
