package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YongJi on 2015/10/26.
 */
public class UnitListResultEntity {
    @SerializedName("enterpriseList")
    private ArrayList<UnitResultEntity> unitList;

    public ArrayList<UnitResultEntity> getUnitList() {
        return unitList;
    }

    public void setUnitList(ArrayList<UnitResultEntity> unitList) {
        this.unitList = unitList;
    }
}
