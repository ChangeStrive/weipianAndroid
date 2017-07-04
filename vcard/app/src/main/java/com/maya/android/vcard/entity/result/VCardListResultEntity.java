package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.entity.CardEntity;

import java.util.ArrayList;

/**
 * ResultEntity：我的名片列表
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-3-13
 *
 */
public class VCardListResultEntity {
	@SerializedName("cardList")
	private ArrayList<CardEntity> cardEntityList;

	public ArrayList<CardEntity> getCardEntityList() {
		return cardEntityList;
	}

	public void setCardEntityList(ArrayList<CardEntity> cardEntityList) {
		this.cardEntityList = cardEntityList;
	}
	
	
}
