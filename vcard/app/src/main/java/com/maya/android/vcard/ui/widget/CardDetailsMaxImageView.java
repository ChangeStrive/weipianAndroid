package com.maya.android.vcard.ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.CardTouchPadEntity;
import com.maya.android.vcard.entity.ContactEntity;
import com.maya.android.vcard.util.ResourceHelper;
/**
 * 放大图片的 viewpager 子项
 * @author zheng_cz
 * @since 2013-12-22 下午4:13:54
 */
@SuppressLint("ViewConstructor")
public class CardDetailsMaxImageView extends RelativeLayout {

	//#region Getter & Setter集
	public void setImageViewLongPressListener(ImageViewLongPressListener listener){
		this.mListener = listener;
	}
	//#endregion Getter & Setter集


	
	private static final int MAX_MARGIN = 20 ;
	private static int sMaxWidth,sMaxHeight;
	private AsyncImageView mImvMax;
	public boolean isMaxFront = true;
	private Activity mActivity;
	private String mVCardNo = null;
	private TouchPadLayout mLucidLayout = null;
	private ContactEntity mDataEntity;
	private GestureDetector mGestureDetector;
	private ImageViewLongPressListener mListener;
	
	public CardDetailsMaxImageView(Activity activity) {
		super(activity);
		this.mActivity = activity;
		init(activity);
	}
	private void init(Activity activity){
		LayoutInflater.from(activity).inflate(R.layout.item_cardcase_details_big_vcard, this);
		mImvMax = (AsyncImageView) this.findViewById(R.id.imv_cardcase_details_big_vcard);
		mImvMax.setClickable(true);
		sMaxWidth = ActivityHelper.getScreenWidth() - MAX_MARGIN * 2;
		sMaxHeight = ActivityHelper.getScreenHeight() - MAX_MARGIN * 2;
		mGestureDetector = new GestureDetector(activity, new ImageViewGestureListener());
	}
	/**
	 * 展示放大的图片
	 * @param entity
	 * @param isShowFront
	 */
	public void showMax(ContactEntity entity, boolean isShowFront){

		if(Helper.isNull(entity)){
			return;
		}
		this.mDataEntity = entity;
		//TODO 图片异步修改
//		mImvMax.setRotate(0);
		this.mImvMax.setPortrait(true);
		
		int cardOrientation = entity.getCardAOrientation();
		
		int cardFormat = entity.getCardAForm();
		String vcardNo = entity.getVcardNo();
		boolean isShowDefault = false;
		String imgUrl = "";
		if(isShowFront){
			imgUrl = ResourceHelper.getHttpImageFullUrl(entity.getCardImgA());
		}else{
			 imgUrl = ResourceHelper.getHttpImageFullUrl(entity.getCardImgB());
//			 cardOrientation = curContact.getCardBOrientation();
		}
		CardTouchPadEntity cardTouchPadEntity = null;
		this.mVCardNo = vcardNo;
		if(ResourceHelper.isNotEmpty(vcardNo) && vcardNo.equals(Constants.DefaultUser.DEFAULT_USER_VCARD_NO)){
			cardFormat = Constants.Card.CARD_FORM_9054;
			isShowDefault = true;
			cardTouchPadEntity = JsonHelper.fromJson(ResourceHelper.getStringFromRawFile(R.raw.mytip_touch_pad_data), CardTouchPadEntity.class);
		}
		
		if(!isShowDefault && ResourceHelper.isEmpty(imgUrl)){
			return;
		}
		
		// 计算展示宽高		
		ViewGroup.LayoutParams imvMaxLp = mImvMax.getLayoutParams(); 
		if(Constants.Card.CARD_FORM_9094 == cardFormat){
			imvMaxLp.width = sMaxWidth;
			imvMaxLp.height = (int)(sMaxWidth / Constants.Card.CARD_RATIO_9094);
		}else if(Constants.Card.CARD_FORM_9045 == cardFormat){
			imvMaxLp.height = sMaxHeight;
			imvMaxLp.width = (int)(sMaxHeight / Constants.Card.CARD_RATIO_9045);
		}else{
			imvMaxLp.width = sMaxWidth;
			imvMaxLp.height = (int)(sMaxWidth * Constants.Card.CARD_RATIO_9054);
		}
		mImvMax.setLayoutParams(imvMaxLp);
		this.mImvMax.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mGestureDetector.onTouchEvent(event);
			}
		});
		
		// "触摸板数据引入"
		if(null == mLucidLayout){
			mLucidLayout = new TouchPadLayout(mActivity, entity, cardTouchPadEntity, imvMaxLp.width, imvMaxLp.height, true);
			this.addView(mLucidLayout, imvMaxLp);
		}
		mLucidLayout.setAllowClick(isShowFront);
		//微片小秘书
		if(isShowDefault){
			//TODO 图片异步修改
//			AsyncImageManager.clearMemoryCache();
			if(isShowFront){
				mImvMax.setDefaultImageResId(R.mipmap.img_def_card_front);
			}else{
				mImvMax.setDefaultImageResId(R.mipmap.img_def_card_back);
			}

			isMaxFront = isShowFront;
		}else{
			//TODO 图片异步修改
//			mImvMax.setDefaultImageResId(R.drawable.img_loading_card_port);
			this.mImvMax.setLoadingImage(null);
			this.mImvMax.setLoadFailImage(R.mipmap.img_loading_card_port);
//			if(cardOrientation == 0){
//				mImvMax.setRotate(90);
//			}else{
//				mImvMax.setRotate(0);
//			}
			
			mImvMax.autoFillFromUrl(imgUrl);
//			if(imgUrl.startsWith(Constant.ImagePathSign.VCARD_IMAGE_PATH_SIGN_HTTP)){
//				mImvMax.autoFillFromUrl(imgUrl);
//			}else{
//				AsyncLocalImageManager.fillLocalUrl2ImageView(mImvMax, imgUrl, imvMaxLp.width, imvMaxLp.height, true);
//			}
		
			isMaxFront = isShowFront;
		}
	}
	/**
	 * 旋转图片
	 * @param curContact
	 */
	private void startRoteAnimation(final ContactEntity curContact){
		Animation rotateAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.vcard_rote);
		rotateAnimation.setDuration(500);
		rotateAnimation.setFillAfter(false);
		rotateAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				showMax(curContact, !isMaxFront);
			}
		});
		mImvMax.startAnimation(rotateAnimation);
	}
	/**
	 * 手势监听
	 * @author ZuoZiJi-Y.J
	 * @version v1.0
	 * @since 2014-7-11
	 *
	 */
	private class ImageViewGestureListener extends SimpleOnGestureListener {
		public boolean onDoubleTap(MotionEvent e) {
			startRoteAnimation(mDataEntity);
			return true;
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			if(Helper.isNotNull(mListener)){
				mListener.imageViewLongPress();
			}
		}
	}
	
	public interface ImageViewLongPressListener {
		public void imageViewLongPress();
	}
}
