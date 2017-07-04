package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.entity.SettingEntity;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * JsonEntity：保存设置
 * Created by YongJi on 2015/9/14.
 */
public class SaveSettingJsonEntity {
    @SerializedName("setting")
    private SettingEntity setting;

    public SettingEntity getSetting() {
        if(ResourceHelper.isNull(this.setting)){
            this.setting = new SettingEntity();
        }
        return setting;
    }

    public void setSetting(SettingEntity setting) {
        this.setting = setting;
    }

    public SaveSettingJsonEntity(){}

    public SaveSettingJsonEntity(SettingEntity setting){
        this.setting = setting;
    }
}
