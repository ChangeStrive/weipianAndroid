package com.maya.android.vcard.entity;

import com.maya.android.vcard.entity.result.UnitResultEntity;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 名片夹主页名片实体类
 * Created by Administrator on 2015/9/15.
 */
public class CardcaseVCardEntity implements Cloneable{
    /**布局类型**/
    private int sendType;
    private ContactListItemEntity vcardEntity;
    private UnitResultEntity unitDetailEntity;
    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public ContactListItemEntity getVcardEntity() {
        if(ResourceHelper.isNull(this.vcardEntity)){
            this.vcardEntity = new ContactListItemEntity();
        }
        return vcardEntity;
    }

    public void setVcardEntity(ContactListItemEntity vcardEntity) {
        this.vcardEntity = vcardEntity;
    }

    public UnitResultEntity getUnitDetailEntity() {
        if(ResourceHelper.isNull(this.unitDetailEntity)){
            this.unitDetailEntity = new UnitResultEntity();
        }
        return unitDetailEntity;
    }

    public void setUnitDetailEntity(UnitResultEntity unitDetailEntity) {
        this.unitDetailEntity = unitDetailEntity;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        CardcaseVCardEntity entity = (CardcaseVCardEntity) super.clone();
        entity.vcardEntity = (ContactListItemEntity) getVcardEntity().clone();
        entity.unitDetailEntity = (UnitResultEntity) getUnitDetailEntity().clone();
        return entity;
    }
}
