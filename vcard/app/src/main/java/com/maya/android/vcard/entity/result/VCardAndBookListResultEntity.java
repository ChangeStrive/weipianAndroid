package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.entity.VCardAndBookEntity;

import java.util.ArrayList;

/**
 * VCard和本地通讯录实体类
 * Created by Administrator on 2015/11/5.
 */
public class VCardAndBookListResultEntity {

    @SerializedName("vCardAndBook")
    private ArrayList<VCardAndBookEntity> vCardAndBook;

    public ArrayList<VCardAndBookEntity> getvCardAndBook() {
        return vCardAndBook;
    }

    public void setvCardAndBook(ArrayList<VCardAndBookEntity> vCardAndBook) {
        this.vCardAndBook = vCardAndBook;
    }
}
