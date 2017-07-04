package com.maya.android.vcard.ui.widget;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.CardModeTextEntity;
import com.maya.android.vcard.entity.CardTouchPadEntity;
import com.maya.android.vcard.entity.ContactEntity;
import com.maya.android.vcard.entity.LucidEntity;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 全透明布局
 * @author hzq
 *
 * 2013-12-14 上午10:31:13
 */
@SuppressLint("ViewConstructor")
public class TouchPadLayout extends RelativeLayout {
	//#region Contants Members
	/**
	 * 控件ID
	 */
	private static final int BUTTON_COMPANY_NAME_ID = 1001;
	private static final int BUTTON_TELEPHONE_ID = 1002;
	private static final int BUTTON_MOBILE_ID = 1003;
	private static final int BUTTON_ADDRESS_ID = 1004;
	private static final int BUTTON_HOMEPAGE_ID = 1005;
	private static final int BUTTON_EMAIL_ID = 1006;
	private static final int PX_2_DP_CELL = 2;
	//#endregion Contants Members
	
	//#region Members
	/** Activity **/
	private Activity mActivity;
	/** 上下文环境  **/
	private Context mContext;
	/** 控件声明  **/
	private Button mBtnCompanyName,mBtnTelephone,mBtnMobile,mBtnAddress,mBtnHomepage,mBtnEmail;
	private float mScaleWidth, mScaleHeight;
	/** 是否放大 **/
	private boolean isZoomIn;
	private Geocoder mGeocoder;
	private CardTouchPadEntity mTouchPadEntity;
	private ContactEntity mContact;
	private boolean isAllowClick = true;
	//#endregion Members
	
	@SuppressWarnings("deprecation")
	public TouchPadLayout(Activity activity,ContactEntity contact, CardTouchPadEntity cardTouchPadEntity, 
					int width, int height, boolean isZoomIn) {
		super(activity);
		mGeocoder = new Geocoder(activity);
		this.mContext = activity;
		this.mActivity = activity;
		this.isZoomIn = isZoomIn;
		this.setLayoutParams(new LayoutParams(width, height));
		int top = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_33dp);
		this.setPadding(0, top, 0, 0);
		this.setBackgroundDrawable(null);
		if(null != cardTouchPadEntity && null != contact){
//		if(cardTouchPadEntity == null){
//			cardTouchPadEntity = getCardTouchPadEntity();
//		}
		this.mTouchPadEntity = cardTouchPadEntity;
		countScale(isZoomIn, width, height);
//		if(contact == null){
//			contact = getContactEntity();
//		}
		this.mContact = contact;
		init(contact,cardTouchPadEntity);
		}
	}
	/**
	 * 重置红框
	 */
	@SuppressWarnings("deprecation")
	public void resetBackground() {
		if (null != mBtnCompanyName) {
			mBtnCompanyName.setBackgroundDrawable(null);
		}
		if (null != mBtnTelephone) {
			mBtnTelephone.setBackgroundDrawable(null);
		}
		if (null != mBtnMobile) {
			mBtnMobile.setBackgroundDrawable(null);
		}
		if (null != mBtnAddress) {
			mBtnAddress.setBackgroundDrawable(null);
		}
		if (null != mBtnHomepage) {
			mBtnHomepage.setBackgroundDrawable(null);
		}
		if (null != mBtnEmail) {
			mBtnEmail.setBackgroundDrawable(null);
		}
	}
	//#region 数据处理
	/**
	 * 计算比例
	 * @param width
	 * @param height
	 */
	private void countScale(boolean isZoomIn, int width, int height){
		int widthTemplate = mTouchPadEntity.getWidth();
		int heightTemplate = mTouchPadEntity.getHeight();
		if(isZoomIn && !mTouchPadEntity.isVertical()){
			//需要转向
			this.mScaleWidth = width * 1.0f/heightTemplate;
			this.mScaleHeight = height * 1.0f/widthTemplate;
		}else{
			this.mScaleWidth = width * 1.0f/widthTemplate;
			this.mScaleHeight = height * 1.0f/heightTemplate;
		}
	}
	/**
	 * 初始化数据
	 * @param contact
	 * @param cardTouchPadEntity
	 */
	@SuppressWarnings("deprecation")
	private void init(ContactEntity contact, CardTouchPadEntity cardTouchPadEntity){
		
		int layoutWidth = cardTouchPadEntity.getWidth();
		int layoutHeight = cardTouchPadEntity.getHeight();
		boolean vertical = cardTouchPadEntity.isVertical();
		long templateId = cardTouchPadEntity.getTemplateId();
		HashMap<String, CardModeTextEntity> cardModeMap = null;
		if(templateId != 0){
			cardModeMap = getCardModeTextEntity(templateId);
		}
		LucidEntity[] lucidEntityList = cardTouchPadEntity.getLucidEntityList();
		for(LucidEntity lucidEntity : lucidEntityList){
			CardModeTextEntity modeTextEntity = null;
			int tagId = lucidEntity.getTagId();
			switch(tagId){
			case CardTouchPadEntity.COMPANY_NAME:
				//公司名
				mBtnCompanyName = new Button(this.mContext);
				mBtnCompanyName.setId(BUTTON_COMPANY_NAME_ID);
				mBtnCompanyName.setOnClickListener(mClickListener);
				mBtnCompanyName.setBackgroundDrawable(null);
				if(cardModeMap != null){
					modeTextEntity = cardModeMap.get(Constants.CardMode.CARD_MODE_COMPANY_NAME);
				}
				LayoutParams parmasCompanyName = getLayoutParams(modeTextEntity,
						layoutWidth, layoutHeight, vertical, lucidEntity);
				this.addView(mBtnCompanyName, parmasCompanyName);
				break;
			case CardTouchPadEntity.ADDRESS:
				//地址
				mBtnAddress = new Button(this.mContext);
				mBtnAddress.setId(BUTTON_ADDRESS_ID);
				mBtnAddress.setOnClickListener(mClickListener);
				mBtnAddress.setBackgroundDrawable(null);
				if(cardModeMap != null){
					modeTextEntity = cardModeMap.get(Constants.CardMode.CARD_MODE_ADDRESS);
				}
				LayoutParams paramsAddress = getLayoutParams(modeTextEntity,
						layoutWidth, layoutHeight, vertical, lucidEntity);
				this.addView(mBtnAddress, paramsAddress);
				break;
			case CardTouchPadEntity.MOBILE:
				//手机
				mBtnMobile = new Button(this.mContext);
				mBtnMobile.setId(BUTTON_MOBILE_ID);
				mBtnMobile.setOnClickListener(mClickListener);
				mBtnMobile.setBackgroundDrawable(null);
				if(cardModeMap != null){
					modeTextEntity = cardModeMap.get(Constants.CardMode.CARD_MODE_MOBILE);
				}
				LayoutParams paramsMobile = getLayoutParams(modeTextEntity,
						layoutWidth, layoutHeight, vertical, lucidEntity);
				this.addView(mBtnMobile, paramsMobile);
				break;
			case CardTouchPadEntity.TELEPHONE:
				//电话(座机)
				mBtnTelephone = new Button(this.mContext);
				mBtnTelephone.setId(BUTTON_TELEPHONE_ID);
				mBtnTelephone.setOnClickListener(mClickListener);
				mBtnTelephone.setBackgroundDrawable(null);
				if(cardModeMap != null){
					modeTextEntity = cardModeMap.get(Constants.CardMode.CARD_MODE_TEL);
				}
				LayoutParams paramsTelephone = getLayoutParams(modeTextEntity,
						layoutWidth, layoutHeight, vertical, lucidEntity);
				this.addView(mBtnTelephone, paramsTelephone);
				break;
			
			case CardTouchPadEntity.HOME_PAGE:
				//网址
				mBtnHomepage = new Button(this.mContext);
				mBtnHomepage.setId(BUTTON_HOMEPAGE_ID);
				mBtnHomepage.setOnClickListener(mClickListener);
				mBtnHomepage.setBackgroundDrawable(null);
				if(cardModeMap != null){
					modeTextEntity = cardModeMap.get(Constants.CardMode.CARD_MODE_HOME_PAGE);
				}
				LayoutParams paramsHomePage = getLayoutParams(modeTextEntity,
						layoutWidth, layoutHeight, vertical, lucidEntity);
				this.addView(mBtnHomepage, paramsHomePage);
				break;
			case CardTouchPadEntity.EMAIL:
				//邮箱
				mBtnEmail = new Button(this.mContext);
				mBtnEmail.setId(BUTTON_EMAIL_ID);
				mBtnEmail.setOnClickListener(mClickListener);
				mBtnEmail.setBackgroundDrawable(null);
				if(cardModeMap != null){
					modeTextEntity = cardModeMap.get(Constants.CardMode.CARD_MODE_EMAIL);
				}
				LayoutParams paramsEmail = getLayoutParams(modeTextEntity,
						layoutWidth, layoutHeight, vertical, lucidEntity);
				this.addView(mBtnEmail, paramsEmail);
				break;
			
			default:
				break;
			}
		}
	}
	/**
	 * 获取模板参数
	 * HashMap<String,CardModeTextEntity>
	 * @param templateId
	 * @return
	 */
	private HashMap<String,CardModeTextEntity> getCardModeTextEntity(long templateId) {
		HashMap<String,CardModeTextEntity> map = new HashMap<String,CardModeTextEntity>();
		//TODO
//		TemplateEntity templateEntity = CurrentUser.getInstance().getCardTemplateForId(templateId);
//		String style = templateEntity.getStyle();
//		CardModeEntity cardModeEntity = JsonHelper.fromJson(style, CardModeEntity.class);
//		CardModeTextEntity[] arrayTextEntity = cardModeEntity.getTextEntity();
//		for(CardModeTextEntity textEntity : arrayTextEntity){
//			String name = textEntity.getName();
//			map.put(name, textEntity);
//		}
		return map;
	}
	/**
	 * 获取对应布局参数
	 * @param layoutWidth
	 * @param layoutHeight
	 * @param lucidEntity
	 * @return
	 */
	private LayoutParams getLayoutParams(CardModeTextEntity modeTextEntity, int layoutWidth, int layoutHeight,
			boolean isVertical, LucidEntity lucidEntity) {
		//TODO 通过运算模板参数(字体大小,字间距等...),来得到实际宽高
		int offsetValue = (int)(lucidEntity.getHeight() * 0.25f * PX_2_DP_CELL);
		int width = (int) (lucidEntity.getWidth() * PX_2_DP_CELL);
		int height = (int) (lucidEntity.getHeight() * PX_2_DP_CELL) + offsetValue;
		float scaleValue = 1.0f;
		if(isVertical){
			scaleValue = layoutHeight * 1.0f/layoutWidth;
		}else{
			scaleValue = layoutWidth * 1.0f/layoutHeight;
		}
		int marginLeft = 0;
		if(lucidEntity.getLeft() == 0 && lucidEntity.getRight() != 0){
			marginLeft = layoutWidth - width - lucidEntity.getRight() * PX_2_DP_CELL;
		}else{
			marginLeft = lucidEntity.getLeft() * PX_2_DP_CELL;
		}
		int marginBottom = 0;
		if(lucidEntity.getBottom() == 0 && lucidEntity.getTop() != 0){
			marginBottom = layoutHeight - height - lucidEntity.getTop() * PX_2_DP_CELL;
		}else{
			marginBottom = lucidEntity.getBottom() * PX_2_DP_CELL;
		}
		marginBottom -= offsetValue;
		LayoutParams params = new LayoutParams(width,height);
		if(isZoomIn && !isVertical){
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.topMargin = (int) ((marginLeft * mScaleWidth) - getOffsetValue(scaleValue));
			params.leftMargin = (int) (marginBottom * mScaleHeight);
			params.width = (int) (height * mScaleHeight);
			params.height = (int) (width * mScaleWidth * mScaleWidthValue);
		}else{
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params.leftMargin = (int) (marginLeft * mScaleWidth);
			params.bottomMargin = (int) (marginBottom * mScaleHeight);
			params.width = (int) (width * mScaleWidth);
			params.height = (int) (height * mScaleHeight);
		}
		return params;
	}
	/** 实际比例值 **/
	private float mScaleWidthValue = 1.0f;
	/**
	 * 计算实际高度与标准理想高度的偏移值
	 * @return
	 */
	private int getOffsetValue(float scale){
		float scaleStandard = 1.0f/0.6f;
		float scaleValue = scaleStandard - scale;
		int screenWidth = ActivityHelper.getScreenWidth();
		int screenHeight = ActivityHelper.getScreenHeight();
		int value = (int)(screenWidth * (scaleStandard + scaleValue + 0.01));
		int result = Math.abs(value - screenHeight);
		mScaleWidthValue = screenHeight * 1.0f/value;
		return result;
	}
	//#endregion
	
	//#region 监听
	/** 事件监听 **/
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//重置背景
			resetBackground();
			if(!isAllowClick()){
				return;
			}
			switch(v.getId()){
			case BUTTON_COMPANY_NAME_ID:
				//TODO 展示公司简介(跳转到单位简介)
//				MobclickAgentHelper.onEvent(mContext, MobclickAgentConstant.CARDDETAILACTIVITY_COMPANY);
				Intent intentIntro = new Intent();
				String companyAbout = "";
				if(Helper.isNotNull(mContact)){
					companyAbout = mContact.getCompanyAbout();
				}
//				intentIntro.putExtra(Constants.IntentSet.INTENT_KEY_INTRO, companyAbout);
//				intentIntro.putExtra(Constants.IntentSet.INTENT_KEY_TITLE_NAME, mContext.getString(R.string.label_company_intro));
////				ActivityHelper.switchTo(mActivity, CommonIntroActivity.class, intentIntro);
//				ActivityHelper.switchTo(mActivity, NewCommonIntroEditActivity.class, intentIntro);
				mBtnCompanyName.setBackgroundResource(R.drawable.bg_lucid_layout_btn_red);
				break;
			case BUTTON_TELEPHONE_ID:
				//TODO 电话
//				MobclickAgentHelper.onEvent(mContext, MobclickAgentConstant.CARDDETAILACTIVITY_PHONE);
				String telephone = "";
				String title = "";
				if(Helper.isNotNull(mContact)){
					telephone = mContact.getTelephone();
					title = mContact.getDisplayName();
				}
//				DialogHelper.showDialogConfirm(mActivity, title, telephone, R.string.dlg_btn_tip_content_text_phone, 0, mTelephoneDialogListener, true);
				mBtnTelephone.setBackgroundResource(R.drawable.bg_lucid_layout_btn_red);
				break;
			case BUTTON_MOBILE_ID:
				//TODO 手机
//				MobclickAgentHelper.onEvent(mContext, "");
				String mobile = "";
				if(Helper.isNotNull(mContact)){
					mobile = mContact.getMobile();
				}
//				DialogHelper.showDialogConfirm(mActivity, "", mobile, R.string.dlg_btn_tip_content_text_mobile, R.string.dlg_btn_tip_content_text_msg, mMobileDialogListener, true);
				mBtnMobile.setBackgroundResource(R.drawable.bg_lucid_layout_btn_red);
				break;
			case BUTTON_ADDRESS_ID:
//				MobclickAgentHelper.onEvent(mContext, MobclickAgentConstant.CARDDETAILACTIVITY_ADDRESS);
				String address = "";
				if(Helper.isNotNull(mContact)){
					address = mContact.getAddress();
				}
				if(Helper.isNotEmpty(address)){
					double latitude = 0, longitude = 0;
					try {
						List<Address> locationNameList = mGeocoder.getFromLocationName(address, 5);
						if(null != locationNameList && locationNameList.size() > 0){
							latitude = locationNameList.get(0).getLatitude();
							longitude = locationNameList.get(0).getLongitude();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					//链接地图
					if(checkGoogleMap()){
						Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+latitude+","+longitude));
						mActivity.startActivity(it);
					}else{
						Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(
								"http://ditu.google.cn/maps?hl=zh&mrt=loc&q="+latitude+"," +longitude));
						 mActivity.startActivity(it);
					}
				}
				mBtnAddress.setBackgroundResource(R.drawable.bg_lucid_layout_btn_red);
				break;
			case BUTTON_HOMEPAGE_ID:
				//网址
//				MobclickAgentHelper.onEvent(mContext, MobclickAgentConstant.CARDDETAILACTIVITY_WEBSITE);
				String homePageUrl = "";
				if(Helper.isNotNull(mContact)){
					homePageUrl = mContact.getCompanyHome();
				}
				if(Helper.isNotEmpty(homePageUrl)){
					if(!homePageUrl.startsWith(Constants.ImagePathSign.VCARD_IMAGE_PATH_SIGN_HTTP)){
						homePageUrl = Constants.ImagePathSign.VCARD_IMAGE_PATH_SIGN_HTTP + homePageUrl;
					}
					Intent itHomePage = new Intent(Intent.ACTION_VIEW, Uri.parse(homePageUrl));
					mActivity.startActivity(itHomePage);
				}
				mBtnHomepage.setBackgroundResource(R.drawable.bg_lucid_layout_btn_red);
				break;
			case BUTTON_EMAIL_ID:
				//邮箱
//				MobclickAgentHelper.onEvent(mContext, MobclickAgentConstant.CARDDETAILACTIVITY_EMAIL);
				String email = "";
				if(Helper.isNotNull(mContact)){
					email = mContact.getEmail();
				}
				Uri uri = Uri.parse("mailto:"+ email);
				Intent itEmail = new Intent(Intent.ACTION_SENDTO,uri);
				mActivity.startActivity(itEmail);
				mBtnEmail.setBackgroundResource(R.drawable.bg_lucid_layout_btn_red);
				break;
			}
		}
	};
	/** 电话座机对话框监听事件  **/
	private DialogInterface.OnClickListener mTelephoneDialogListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			//TODO 内容补充
			Uri uri = Uri.parse("tel:0591-83707520"); 
			Intent it = new Intent(Intent.ACTION_DIAL, uri);   
			mActivity.startActivity(it);
			dialog.dismiss();
		}
	};
	/** 手机对话框**/
	private DialogInterface.OnClickListener mMobileDialogListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			//TODO 内容补充
			if(DialogInterface.BUTTON_POSITIVE == which){
				Uri uri = Uri.parse("tel:13612345678"); 
				Intent it = new Intent(Intent.ACTION_DIAL, uri);   
				mActivity.startActivity(it);
			}else if(DialogInterface.BUTTON_NEGATIVE == which){
				Uri uri = Uri.parse("smsto:13612345678");    
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);    
				it.putExtra("sms_body", "");    
				mActivity.startActivity(it); 
			}
			dialog.dismiss();
		}
	};
	
	/**
	 * 检测是否存在GoogleMap
	 * @return
	 */
	private boolean checkGoogleMap() {
		boolean isInstallGMap = false;
		List<PackageInfo> packs = mActivity.getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if (p.packageName==null || p.packageName.equals("") || p.versionName == null || p.versionName.equals("")) {
				continue;
			}
			if ("com.google.android.apps.maps".equals(p.packageName)) {
				isInstallGMap = true;
				break;
			}
		}
		return isInstallGMap;
	}
	
	/**
	 * 是否允许被点击
	 * @param isAllowClick
	 */
	public void setAllowClick(boolean isAllowClick){
		resetBackground();
		this.isAllowClick = isAllowClick;
	}
	public boolean isAllowClick(){
		return this.isAllowClick;
	}
	//#endregion
	
	/**
	 * 计算字长
	 * @param content
	 * @param size
	 * @param wordSpace 字间距
	 * @return
	 */
	private int getFontWidthScale(String content, float size, float wordSpace){
		float sizeScale = 0.5f;
		int b = 0;
		if(Helper.isEmpty(content)){
			return 0;
		}
		int count = content.length();
		for(int i = 0;i < count;i++){
			String sub = content.substring(i, i+1);
			if(!isEnglishFont(sub)){
				sizeScale = 1.0f;
			}
			b+= (int)(size * sizeScale);
		}
		b += (int)(size * (count - 1) * wordSpace);
		return b;
	}
	/**
	 * 判断是否是英文字符
	 * PS:英文字符占一个字节,中文字符占两个字节
	 * @param content
	 * @return
	 */
	private boolean isEnglishFont(String content){
		if(Helper.isEmpty(content)){
			return false;
		}
		String strPattern = "[a-zA-Z0-9]";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(content);
		return m.matches();
	}
	
	//#region test data
		private ContactEntity getContactEntity(){
			ContactEntity contact = new ContactEntity();
			contact.setBusiness(0);
			contact.setCardAForm(Constants.Card.CARD_FORM_9054);
			contact.setCardBForm(Constants.Card.CARD_FORM_9054);
			contact.setCardAOrientation(Constants.Card.CARD_ORIENTATION_LANDSCAPE);
			contact.setCardBOrientation(Constants.Card.CARD_ORIENTATION_LANDSCAPE);
			contact.setCardAType(Constants.Card.CARD_TYPE_TEMPLATE);
			contact.setCardBType(Constants.Card.CARD_TYPE_TEMPLATE);
			contact.setCardImgA("");
			contact.setAddress("福州市鼓楼区工业路611号高新科技园");
			contact.setMobile("13612345678");
			contact.setTelephone("0591-87978585");
			contact.setCompanyHome("http://www.mayasoft.com.cn");
			contact.setEmail("176068250@qq.com");
			contact.setCompanyName("福建玛雅软件科技有限公司");
			contact.setCompanyAbout("玛雅公司的简介");
			return contact;
		}
		private CardTouchPadEntity getCardTouchPadEntity() {
			CardTouchPadEntity cardTouch = new CardTouchPadEntity();
			cardTouch.setVertical(false);
			cardTouch.setWidth(1200);
			cardTouch.setHeight(720);
			LucidEntity[] lucidEntityList = new LucidEntity[6];
			//companyName
			lucidEntityList[0] = new LucidEntity();
			lucidEntityList[0].setTagId(CardTouchPadEntity.COMPANY_NAME);
			lucidEntityList[0].setWidth(290);
			lucidEntityList[0].setHeight(30);
			lucidEntityList[0].setTop(0);
			lucidEntityList[0].setLeft(146);
			lucidEntityList[0].setRight(0);
			lucidEntityList[0].setBottom(174);
			//address
			lucidEntityList[1] = new LucidEntity();
			lucidEntityList[1].setTagId(CardTouchPadEntity.ADDRESS);
			lucidEntityList[1].setWidth(348);
			lucidEntityList[1].setHeight(22);
			lucidEntityList[1].setTop(0);
			lucidEntityList[1].setLeft(146);
			lucidEntityList[1].setRight(0);
			lucidEntityList[1].setBottom(139);
			//mobile
			lucidEntityList[2] = new LucidEntity();
			lucidEntityList[2].setTagId(CardTouchPadEntity.MOBILE);
			lucidEntityList[2].setWidth(160);
			lucidEntityList[2].setHeight(22);
			lucidEntityList[2].setTop(0);
			lucidEntityList[2].setLeft(146);
			lucidEntityList[2].setRight(0);
			lucidEntityList[2].setBottom(98);
			//telephone
			lucidEntityList[3] = new LucidEntity();
			lucidEntityList[3].setTagId(CardTouchPadEntity.TELEPHONE);
			lucidEntityList[3].setWidth(180);
			lucidEntityList[3].setHeight(22);
			lucidEntityList[3].setTop(0);
			lucidEntityList[3].setLeft(146);
			lucidEntityList[3].setRight(0);
			lucidEntityList[3].setBottom(119);
			//homepage
			lucidEntityList[4] = new LucidEntity();
			lucidEntityList[4].setTagId(CardTouchPadEntity.HOME_PAGE);
			lucidEntityList[4].setWidth(243);
			lucidEntityList[4].setHeight(22);
			lucidEntityList[4].setTop(0);
			lucidEntityList[4].setLeft(146);
			lucidEntityList[4].setRight(0);
			lucidEntityList[4].setBottom(58);
			//email
			lucidEntityList[5] = new LucidEntity();
			lucidEntityList[5].setTagId(CardTouchPadEntity.EMAIL);
			lucidEntityList[5].setWidth(230);
			lucidEntityList[5].setHeight(22);
			lucidEntityList[5].setTop(0);
			lucidEntityList[5].setLeft(146);
			lucidEntityList[5].setRight(0);
			lucidEntityList[5].setBottom(79);
			cardTouch.setLucidEntityList(lucidEntityList);
			return cardTouch;
		}
		//#endregion
}