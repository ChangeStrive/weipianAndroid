package com.maya.android.vcard.entity;

import android.util.SparseArray;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 用于存储日志的实体类
 * Created by Administrator on 2015/11/3.
 */
public class BackupLogSaveEntity {
    @SerializedName("saveEntity")
    private SparseArray<ArrayList<BackupLogEntity>> saveEntity;

    public SparseArray<ArrayList<BackupLogEntity>> getSaveEntity() {
        return saveEntity;
    }

    public void setSaveEntity(SparseArray<ArrayList<BackupLogEntity>> saveEntity) {
        this.saveEntity = saveEntity;
    }
}
