package com.maya.android.vcard.ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.CardTouchPadEntity;
import com.maya.android.vcard.entity.ContactEntity;
import com.maya.android.vcard.util.ResourceHelper;

public class CardDetailsImageView extends RelativeLayout {
	//#region 常量
	/** 微片小秘书 微片号 TODO: 由于某人设下的坑，7位的微片号  */
	public static final String DEFAULT_USER_VCARD_NO_OLD = "1000000";
	//#endregion 常量

	//#region 静态成员变量
	/** 图片可展示的最大 宽 高 */
	public static double sMaxHeight;
	public static double sMaxWidth ;
	//#endregion 静态成员变量

	//#region 成员变量
	public boolean isFront = true;
	
	private static int sWidthPadding = 0;
	public String mCurImgFullUrl;
	public int mTop = 0, mLeft = 0;
	public int mCardFormat, mCardOrientation;
	private int mShowWidth,mShowHeight;
	/** 控件 **/
	private AsyncImageView mImageView;
	private TouchPadLayout mLucidLayout;
	private ContactEntity mDataEntity;
	private GestureDetector mGestureDetector;
	private ImageViewLongPressListener mListener;
	//#endregion 成员变量

	//#region Getter & Setter集
	public void setImageViewLongPressListener(ImageViewLongPressListener listener){
		this.mListener = listener;
	}
	//#endregion Getter & Setter集

	//#region 构造方法
//	public CardDetailsImageView(Activity context,ContactEntity contactEntity, CardTouchPadEntity cardTouchPadEntity) {
//		super(context);
//		init(context, contactEntity, cardTouchPadEntity);
//	}

	public CardDetailsImageView(Activity activity, ContactEntity contactEntity, CardTouchPadEntity cardTouchPadEntity) {
		super(activity);
		init(activity, contactEntity, cardTouchPadEntity);
	}
	//#endregion 构造方法

	//#region public 方法
	public void show(ContactEntity entity){
//		entity.getVcardNo(), entity.getCardImgA(), entity.getCardImgB(), entity.getCardAOrientation(), entity.getCardAForm(), mImvCurCard.isFront
		if(Helper.isNotNull(entity)){
			this.mDataEntity = entity;
			this.mCardOrientation = this.mDataEntity.getCardAOrientation();
			int format = this.mDataEntity.getCardAForm();
			String vcardNo = this.mDataEntity.getVcardNo();
			String url = this.mDataEntity.getCardImgA();
			setCardSize(format, this.mCardOrientation);
			showCard(vcardNo, url, true);
			this.mImageView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return mGestureDetector.onTouchEvent(event);
				}
			});
		}
	}
	//#endregion public 方法

	//#region private 方法
	@SuppressLint("NewApi")
	private void init(Activity activity, ContactEntity contactEntity, CardTouchPadEntity cardTouchPadEntity){
		sWidthPadding = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_10dp);
		sMaxWidth = ActivityHelper.getScreenWidth() - sWidthPadding * 2;
		sMaxHeight = ActivityHelper.getScreenHeight() * 0.55;
		mGestureDetector = new GestureDetector(activity, new ImageViewGestureListener());
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mImageView = new AsyncImageView(activity);
		mImageView.setClickable(true);
		int width = (int) sMaxWidth;
		int height = (int)(sMaxWidth * 0.6);
		//TODO 触摸板,暂时关闭
		mLucidLayout = new TouchPadLayout(activity, contactEntity, cardTouchPadEntity, width, height, false);
		/**TEST END**/
		this.addView(mImageView);
		this.addView(mLucidLayout, width, height);
	}
	
	private void setCardSize(int cardFormat, int cardOrientation){
		mTop = 0;
		mLeft = 0 ;

		mCardFormat = cardFormat;
		mCardOrientation = cardOrientation;
		
		if(cardFormat == Constants.Card.CARD_FORM_9094){
			mTop = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_6dp);
			mShowWidth = (int)sMaxWidth;
			mShowHeight = (int) (mShowWidth / Constants.Card.CARD_RATIO_9094);
		}else{
			if(Constants.Card.CARD_ORIENTATION_LANDSCAPE == cardOrientation){
				mShowWidth = (int)sMaxWidth;
				if(cardFormat == Constants.Card.CARD_FORM_9045){
					mShowHeight = (int)(mShowWidth / Constants.Card.CARD_RATIO_9045);
					mTop = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_48dp);
				}else{
					mShowHeight = (int)(mShowWidth / Constants.Card.CARD_RATIO_9054);
					mTop = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_33dp);
				}
			}else{
				mShowHeight = (int) sMaxHeight;
				mTop = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_6dp);
				if(cardFormat == Constants.Card.CARD_FORM_9045){
					mShowWidth = (int)(mShowHeight / Constants.Card.CARD_RATIO_9045);
				}else{
					mShowWidth = (int)(mShowHeight / Constants.Card.CARD_RATIO_9054);
				}
				mLeft = ((int)sMaxWidth - mShowWidth) / 2 ;
			}
		}
		
		LayoutParams imvLp = (LayoutParams) mImageView.getLayoutParams();
		imvLp.width = mShowWidth;
		imvLp.height = mShowHeight;
		imvLp.addRule(RelativeLayout.CENTER_HORIZONTAL);		
		mImageView.setLayoutParams(imvLp);
		mImageView.setScaleType(ScaleType.FIT_XY);
		this.setPadding(0, mTop, 0, 0);
	}
	
	private void showCard(String vcardNo, String imgUrl, boolean isShowFront){
		//微片小秘书
		if(ResourceHelper.isNotEmpty(vcardNo) &&
				(vcardNo.equals(Constants.DefaultUser.DEFAULT_USER_VCARD_NO) || DEFAULT_USER_VCARD_NO_OLD.equals(vcardNo))){
			if(isShowFront){
				mImageView.setDefaultImageResId(R.mipmap.img_def_card_front_landscape);
			}else{
				mImageView.setDefaultImageResId(R.mipmap.img_def_card_back_landscape);
			}
		}else{
			if(Constants.Card.CARD_ORIENTATION_LANDSCAPE == mCardOrientation){
				//TODO 图片异步修改
				mImageView.setDefaultImageResId(R.mipmap.img_loading_card_land);
//				this.mImageView.setLoadFailImage(R.drawable.img_loading_card_land);
			}else{
				//TODO 图片异步修改
				mImageView.setDefaultImageResId(R.mipmap.img_loading_card_port);
//				this.mImageView.setLoadFailImage(R.drawable.img_loading_card_port);
			}
			//TODO 图片异步修改
//			this.mImageView.setLoadingImage(null);
			
			mCurImgFullUrl = ResourceHelper.getHttpImageFullUrl(imgUrl);
			ResourceHelper.asyncImageViewFillPath(mImageView, mCurImgFullUrl);
		}
		if(null != mLucidLayout){
			mLucidLayout.setAllowClick(isShowFront);
		}
	}

	/**
	 * 执行转动切换卡片正反面动画
	 * @param vcardNo
	 * @param imgUrl
	 */
	private void startRoteAnimation(String vcardNo, String imgUrl){
		Animation rotateAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.vcard_rote);
		rotateAnimation.setDuration(500);
		rotateAnimation.setFillAfter(false);
		rotateAnimation.setAnimationListener(new RotateListener(vcardNo, imgUrl));
		mImageView.startAnimation(rotateAnimation);
	}

	/**
	 * 执行转动切换卡片正反面动画
	 * @param curContact
	 */
	private void startRoteAnimation(ContactEntity curContact) {
		if(Helper.isNull(curContact)){
			return;
		}
		String vcardNo = curContact.getVcardNo();
		if(ResourceHelper.isNotEmpty(vcardNo) && vcardNo.equals(Constants.DefaultUser.DEFAULT_USER_VCARD_NO)){
			startRoteAnimation(vcardNo, null);
		}else{
			// 旋转旋转图片
			if(isFront){
				startRoteAnimation(vcardNo, curContact.getCardImgB());
			}else{
				startRoteAnimation(vcardNo, curContact.getCardImgA());
			}
		}
	}
	
	public void resetLucidLayout(){
		if(null != mLucidLayout){
			mLucidLayout.resetBackground();
		}
	}
	//#endregion private 方法

	//#region interface & 子类
	/**
	 * 旋转监听事件
	 * @author zheng_cz
	 * @since 2013-11-12 下午6:30:48
	 */
	private class RotateListener implements AnimationListener{
		private String mVcardNo;
		private String mImgUrl;
		public RotateListener(String vcardNo, String imgUrl){
			this.mVcardNo = vcardNo;
			this.mImgUrl = imgUrl;
		}

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if(ResourceHelper.isNotEmpty(mVcardNo) && Constants.DefaultUser.DEFAULT_USER_VCARD_NO.equals(mVcardNo)){
				isFront = !isFront;
				showCard(mVcardNo, null, isFront);
			}else{
				if(Helper.isNotEmpty(mImgUrl)){
					isFront = !isFront;
					showCard(mVcardNo, mImgUrl, isFront);
				}
			}
		}
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
			resetLucidLayout();
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
		void imageViewLongPress();
	}
	//#endregion interface & 子类
}
