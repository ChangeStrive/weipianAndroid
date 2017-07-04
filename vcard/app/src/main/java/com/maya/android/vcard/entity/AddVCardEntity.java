package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Entity：名片上传
 * Created by ZuoZiJi on 2015/9/24.
 */
public class AddVCardEntity {
    @SerializedName("vcard")
    private CardEntity vcard;
    @SerializedName("fromClass")
    private Class fromClass;
    @SerializedName("vcardARotate")
    private int vcardARotate;
    @SerializedName("vcardBRotate")
    private int vcardBRotate;

    public CardEntity getVcard() {
        return vcard;
    }

    public void setVcard(CardEntity vcard) {
        this.vcard = vcard;
    }

    public Class getFromClass() {
        return fromClass;
    }

    public void setFromClass(Class fromClass) {
        this.fromClass = fromClass;
    }

    public int getVcardARotate() {
        return vcardARotate;
    }

    public void setVcardARotate(int vcardARotate) {
        this.vcardARotate = vcardARotate;
    }

    public int getVcardBRotate() {
        return vcardBRotate;
    }

    public void setVcardBRotate(int vcardBRotate) {
        this.vcardBRotate = vcardBRotate;
    }
}
