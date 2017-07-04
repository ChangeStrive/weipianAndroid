package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/3.
 */
public class UserNearbyPeopleListResultEntity {

    @SerializedName("personList")
    private ArrayList<UserNearbyPeopleEntity> personList;

    public ArrayList<UserNearbyPeopleEntity> getPersonList() {
        return personList;
    }

    public void setPersonList(ArrayList<UserNearbyPeopleEntity> personList) {
        this.personList = personList;
    }
}
