package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/3.
 */
public class BackupLogListResultEntity {

   @SerializedName("syncContactsLogsList")
   private ArrayList<BackupLogResultEntity> syncContactsLogsList;

    public ArrayList<BackupLogResultEntity> getSyncContactsLogsList() {
        return syncContactsLogsList;
    }

    public void setSyncContactsLogsList(ArrayList<BackupLogResultEntity> syncContactsLogsList) {
        this.syncContactsLogsList = syncContactsLogsList;
    }
}
