package com.maya.android.vcard.data;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.VCardFormEntity;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;
/**
 * 我的名片格式数据
 * @author ZuoZiJi-Y.J
 * @version v2.0
 * @since 2015-8-31
 *
 */
public class VCardFormData {

	//region 单例
	private static VCardFormData sInstance;
	private VCardFormData(){
		this.init();
	}
	public static VCardFormData getInstance(){
		if(Helper.isNull(sInstance)){
			sInstance = new VCardFormData();
		}
		return sInstance;
	}
	//endregion 单例

	private static final String TAG = VCardFormData.class.getSimpleName();
	private static final Pattern COMMA_PATTERN = Pattern.compile(",");
	private static final String SHOW_CARD_FORM_ADAPTIVE = "480x800, 480x854, 540x960, 640x960, 640x1136, 720x1280, 768x1024, 768x1184, 800x1280, 1080x1920";
	private static final String KEY_CURRENT_VCARD_FORM_DATA = "VCardFormData";
	private VCardFormEntity mVCardFormEntity = null;
	private PreferencesHelper mPreferences = PreferencesHelper.getInstance();
	/**
	 * 取得卡片格式数据
	 * @param cardForm {Constants.Card.CARD_FORM_9045、Constants.Card.CARD_FORM_9054、Constants.Card.CARD_FORM_9094}
	 * @return
	 */
	public VCardFormEntity.CardFormEntity getVCardFormEntity(int cardForm){
		switch(cardForm){
		case Constants.Card.CARD_FORM_9045:
			return getVCardFormEntity().getForm4590();
		case Constants.Card.CARD_FORM_9054:
			return getVCardFormEntity().getForm5490();
		case Constants.Card.CARD_FORM_9094:
			return getVCardFormEntity().getForm9490();
		}
		return null;
	}
	
	/**
	 * 初始化
	 */
	public void init(){
		//进行屏幕判断
		String vCardFormData = this.mPreferences.getString(KEY_CURRENT_VCARD_FORM_DATA, "");
		int saveVersion = this.mPreferences.getInt(Constants.Preferences.KEY_SAVE_OLD_VERSION, 0);
		int currentVersion = ActivityHelper.getCurrentVersion();
		if(!"".equals(vCardFormData)  && (saveVersion == 0 || saveVersion == currentVersion)){
			VCardFormEntity vCardFormEntity = JsonHelper.fromJson(vCardFormData, VCardFormEntity.class);
			if(Helper.isNotNull(vCardFormEntity)){
				this.setVCardFormEntity(vCardFormEntity);
			}
		}else{
			String allCardFormData = ResourceHelper.getStringFromRawFile(R.raw.vcard_form_data);
			String bestPreviewSize = findBestPreviewSizeValue();
			if(Helper.isNotNull(allCardFormData) && !"".equals(allCardFormData)){
				try {
					JSONObject allCardFormJson = new JSONObject(allCardFormData);
					JSONObject cardFormJson = allCardFormJson.optJSONObject(bestPreviewSize);
					if(Helper.isNotNull(cardFormJson)){
						this.mPreferences.putString(KEY_CURRENT_VCARD_FORM_DATA, cardFormJson.toString());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String findBestPreviewSizeValue() {
	    int bestX = 0;
	    int bestY = 0;
	    int screenWidth = ActivityHelper.getScreenWidth();
	    int screenHeight = ActivityHelper.getScreenHeight();
	    int diff = Integer.MAX_VALUE;
	    for (String previewSize : COMMA_PATTERN.split(SHOW_CARD_FORM_ADAPTIVE)) {
	
	      previewSize = previewSize.trim();
	      int dimPosition = previewSize.indexOf('x');
	      if (dimPosition < 0) {
	        LogHelper.w(TAG, "Bad preview-size: " + previewSize);
	        continue;
	      }
	
	      int newX;
	      int newY;
	      try {
	    	  newX = Integer.parseInt(previewSize.substring(0, dimPosition));
	          newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
	      } catch (NumberFormatException nfe) {
	        LogHelper.w(TAG, "Bad preview-size: " + previewSize);
	        continue;
	      }
	
	      int newDiff = Math.abs(newX - screenWidth) + Math.abs(newY - screenHeight);
	      if (newDiff == 0) {
	        bestX = newX;
	        bestY = newY;
	        break;
	      } else if (newDiff < diff) {
	        bestX = newX;
	        bestY = newY;
	        diff = newDiff;
	      }
	    }
	    
	    if (bestX > 0 && bestY > 0) {
	      return bestX + "x" + bestY;
//	      return new Point(bestX, bestY);
	    }
	    return null;
	  }
	
	private VCardFormEntity getVCardFormEntity() {
		if(Helper.isNull(this.mVCardFormEntity)){
			init();
		}
		return mVCardFormEntity;
	}

	private void setVCardFormEntity(VCardFormEntity vCardFormEntity) {
		this.mVCardFormEntity = vCardFormEntity;
	}
}
