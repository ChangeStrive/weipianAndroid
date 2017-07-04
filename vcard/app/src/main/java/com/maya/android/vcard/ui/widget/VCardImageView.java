package com.maya.android.vcard.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Scroller;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.VCardFormData;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.VCardFormEntity.CardFormEntity;
import com.maya.android.vcard.util.AudioRecordHelper;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 用于做名片滑动的自定义控件
 * @author zheng_cz
 * @since 2013-07-10
 */
public class VCardImageView extends AsyncImageView{

	//region 常量
	/** Duration Time **/
	private static final int DURATION_SCROLL_TO_SITU = 500;
	//endregion 常量

	//region 静态成员变量

	//endregion 静态成员变量

	//region 成员变量
	private Context mContext;
	private Scroller mScroller;
	private int mScreenWidth = ActivityHelper.getScreenWidth();
	private int mScreenHeight = ActivityHelper.getScreenHeight();
	/** X轴移动最小距离 **/
	private int mMinDistanceX = mScreenWidth / 3;
	/** Y轴移动最小距离 **/
	private int mMinDistanceY = mScreenHeight / 3;
	private boolean isScrollY = false;
	private boolean isScrollX = false;
	/** CardEntity **/
	private CardEntity mCardEntity = null;
	/** CardFormEntity **/
	private CardFormEntity mCardFormEntity = null;
	/** 是否展示名片正面 **/
	private boolean isFront = false;
	/** 是否有顶部点击 **/
	private boolean isTopClick;
	/** 顶部区域 **/
	private int mTopTitleArea;
	private float mLastX;
	private float mLastY;
	/** 实际X轴移动的距离 **/
	private int mDistanceX;
	/** 实际Y轴移动的距离 **/
	private int mDistanceY;
	/** 是否为双面 **/
	private boolean isTwoSided = false;
	/** 旋转动画 **/
	private Animation mRotateAnimation;
	/** 手势 **/
	private GestureDetector mGestureDetector;
	private OnEventListener mListener;
	private ImageLoadListener mImageLoadListener = null;
	//endregion 成员变量

	//region 构造方法
	public VCardImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}
	
	public VCardImageView(Context context){
		super(context);
		this.init(context);
	}
	//endregion 构造方法

	//region overwrite 方法
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 手指位置地点
		float y = event.getY();
		float x = event.getX();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 如果屏幕的动画还没结束，你就按下了，我们就结束该动画
				if (mScroller != null) {
					if (!mScroller.isFinished()) {
						mScroller.abortAnimation();
						smoothScrollReset();
						isTopClick = false;
					}
				}
				isTopClick = y < mTopTitleArea;
				mLastY = y;
				mLastX = x;
				break;
			case MotionEvent.ACTION_UP :
				mDistanceY = (int)(mLastY - y);
				mDistanceX = (int)(mLastX - x);

				int absDisX = Math.abs(mDistanceX);
				int absDisY = Math.abs(mDistanceY);
				if(( isScrollX && absDisX < mMinDistanceX) || ( isScrollY && absDisY < mMinDistanceY)){
					smoothScrollReset();
				}else{
					if(isScrollX){
						mDistanceY = 0;
						// 左右滑动
						if(mDistanceX > 0){
							// 向右滑
							smoothAutoScrollX(getWidth());
							mListener.onScrollRightComplete();
						}else{
							smoothAutoScrollX(-getWidth());
							mListener.onScrollLeftComplete();
						}
					}else if(isScrollY){
						mDistanceX = 0;
						// 上下滑动
						if(mDistanceY > 0){
							// 向上滑动
							//播放音效
							AudioRecordHelper.setRescource2MediaPlay(R.raw.voice_flipping_card);
							AudioRecordHelper.play();
							smoothAutoScrollY( getHeight());
							mListener.onScrollUpComplete();
						}else{
							// 向下滑动
							smoothAutoScrollY( -getHeight());
							mListener.onScrollDownComplete();
						}
					}
				}

				break;
			default:
		}
		return mGestureDetector.onTouchEvent(event);
	}
	
	@Override
	public void computeScroll() {
	
		//先判断mScroller滚动是否完成
		if (mScroller.computeScrollOffset()) {
		
			//这里调用View的scrollTo()完成实际的滚动
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			
			//必须调用该方法，否则不一定能看到滚动效果
			postInvalidate();
		}
		super.computeScroll();
	}
	
	//endregion overwrite 方法

	//region public 方法
	/**
	 * 展示名片
	 * @param cardEntity
	 */
	public void showCard(CardEntity cardEntity){
		this.mImageLoadListener = this.getImageLoadListener();
		if(Helper.isNotNull(cardEntity)){
			if( Helper.isNull(this.mCardEntity) || !this.mCardEntity.equals(cardEntity)){
				
				this.mCardEntity = cardEntity;
				this.isTwoSided = Helper.isNotEmpty(this.mCardEntity.getCardImgB());
				int cardForm = this.mCardEntity.getCardAForm();
				if(cardForm == 0){
					cardForm = Constants.Card.CARD_FORM_9054;
				}
				this.mCardFormEntity = VCardFormData.getInstance().getVCardFormEntity(cardForm);
				initShowForm(cardForm, this.mCardFormEntity);
				showCard(this.mCardEntity.getCardAOrientation(), this.mCardEntity.getCardImgA());
			}
		}else{
			if(Helper.isNotNull(this.mImageLoadListener)){
				this.mImageLoadListener.imageLoadFail();
			}
		}
		this.isFront = true;
	}
	/**
	 * 设置事件监听
	 * @param listener   
	 * @return void
	 */
	public void setOnEventListener(OnEventListener listener) {
		mListener = listener;
	}
	/**
	 * 取得当前卡片格式
	 * @return
	 */
	public CardFormEntity getCardFormEntity() {
		if(Helper.isNull(this.mCardFormEntity)){
			this.mCardFormEntity = VCardFormData.getInstance().getVCardFormEntity(Constants.Card.CARD_FORM_9054);
		}
		return this.mCardFormEntity;
	}
	/**
	 * 重新加载我的名片动画
	 */
	public void reloadCardAnim(){
		smoothScrollReset();
		if(Math.abs(mDistanceY) >= mMinDistanceY){
			boolean isFromTop = mDistanceY > 0;
			startVerticalAnim(isFromTop);
		}else if(Math.abs(mDistanceX) >= mMinDistanceX){
			boolean isFromLeft = mDistanceX > 0;
			startHorizontalAnim(isFromLeft);
		}
	}
	//endregion public 方法

	//region private 方法
	private void init(Context context){
		this.mContext = context;
		this.mScroller = new Scroller(this.mContext);
		this.mTopTitleArea = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_40dp);
		this.mGestureDetector = new GestureDetector(this.mContext, new MyCardGestureListener());
	}
	/**
	 * 调用此方法设置滚动的相对偏移
	 * @param dx
	 * @param dy   
	 * @return void
	 */
	private void smoothScrollBy(int dx, int dy) {

		//设置mScroller的滚动偏移量
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
		//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
		invalidate();
	}
	/**
	 * 还原 滚动
	 */
	private void smoothScrollReset(){
		if(isScrollX){
			mScroller.startScroll(mScroller.getFinalX(), 0, -mScroller.getFinalX(), 0, DURATION_SCROLL_TO_SITU);
		}else if(isScrollY){
			mScroller.startScroll(0, mScroller.getFinalY(), 0, -mScroller.getFinalY(), DURATION_SCROLL_TO_SITU);
		}else{
			mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), -mScroller.getFinalX(), -mScroller.getFinalY(), DURATION_SCROLL_TO_SITU);
		}
		invalidate();
		isScrollX = false;
		isScrollY = false;
	}
	/**
	 * 调用此方法设置滚动的相对偏移
	 * @param dy
	 */
	private void smoothAutoScrollY(int dy) {
		//设置mScroller的滚动偏移量
		mScroller.startScroll(0, mScroller.getFinalY(), 0, dy, DURATION_SCROLL_TO_SITU);
		invalidate();
		isScrollY = false;
	}
	/**
	 * 调用此方法设置滚动的相对偏移
	 * @param dx
	 */
	private void smoothAutoScrollX(int dx) {
		//设置mScroller的滚动偏移量
		mScroller.startScroll(mScroller.getFinalX(), 0, dx, 0, DURATION_SCROLL_TO_SITU);
		invalidate();
		isScrollX = false;
	}
	/**
	 * 设置展示参数
	 * @param cardForm
	 * @param cardFormEntity
	 */
	private void initShowForm(int cardForm, CardFormEntity cardFormEntity){
		this.setPadding(cardFormEntity.getCardLeft(), cardFormEntity.getCardTop(), 
				cardFormEntity.getCardLeft(), cardFormEntity.getCardBottom());
		if(Constants.Card.CARD_FORM_9094 == cardForm){
			this.setScaleType(ScaleType.FIT_CENTER);
		}else{
			this.setScaleType(ScaleType.FIT_XY);
		}
	}
	/**
	 * 展示图片
	 * @param imgUrl
	 */
	private void showCard(int cardOrientation, String imgUrl){
		if(Helper.isNotEmpty(imgUrl)){
			String imgFullPath = imgUrl;
			imgFullPath = ResourceHelper.getHttpImageFullUrl(imgFullPath);
			this.setLoadingImage(null);
			// 图片展示
			if(Constants.Card.CARD_ORIENTATION_LANDSCAPE == cardOrientation){
				//TODO 图片异步修改
//				this.setRotate(90);
				this.setPortrait(true);
			}else{
				//TODO 图片异步修改
//				this.setRotate(0);
//				this.setLandscape(true);
			}
//			this.setPortrait(true);
			//TODO 图片异步修改
//			this.autoFillFromUrl(imgFullPath, true);
			autoFillFromUrl(imgFullPath);
//			if(Helper.isNotEmpty(imgFullPath) && imgFullPath.startsWith(Constant.ImagePathSign.VCARD_IMAGE_PATH_SIGN_HTTP)){
//				//TODO 图片异步修改
//				if(Constant.Card.CARD_ORIENTATION_LANDSCAPE == cardOrientation){
//					//TODO 图片异步修改
////					this.setRotate(90);
//					this.setPortrait(true);
//				}else{
//					//TODO 图片异步修改
////					this.setRotate(0);
////					this.setLandscape(true);
//				}
////				this.setPortrait(true);
//				//TODO 图片异步修改
////				this.autoFillFromUrl(imgFullPath, true);
//				autoFillFromUrl(imgFullPath);
//			}else{
////				this.setDefaultImageResId(R.drawable.bg_act_mycard_empty_small);
//				AsyncLocalImageManager.fillLocalUrl2ImageView(this, imgFullPath, true);
//			}
		}else{
			if(Helper.isNotNull(this.mImageLoadListener)){
				this.mImageLoadListener.imageLoadFail();
			}
		}
	}
	/**
	 * 图片垂直加载动画 
	 * @param isFromTop   true = 从上向下进入,  false = 从下往上进入
	 * @return void
	 */
	private void startVerticalAnim(boolean isFromTop){
		Animation transAnimation ;
		if(isFromTop){
			//加载动画
			transAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.translate_up_in);
		}else{
			transAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.translate_down_in);
		}
		startAnimation(transAnimation);
		mDistanceY = 0;
	}
	/**
	 * 图片水平加载动画 
	 * @param isFromLeft   true = 从左往右,  false = 从右往左
	 * @return void
	 */
	private void startHorizontalAnim(boolean isFromLeft){
		Animation transAnimation ;
		if(isFromLeft){
			//加载动画
			transAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.translate_left_in);
		}else{
			transAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.translate_right_in);
		}
		startAnimation(transAnimation);
		mDistanceX = 0;
	}
	/**
	 * 执行转动切换卡片正反面动画
	 */
	private void startRoteAnimation(){
		if(Helper.isNull(this.mRotateAnimation)){
			mRotateAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.vcard_rote);
			mRotateAnimation.setDuration(500);
			mRotateAnimation.setFillAfter(false);
			mRotateAnimation.setAnimationListener(new Animation.AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					if(isTwoSided){
						if(isFront){
						//正面
							showCard(mCardEntity.getCardAOrientation(), mCardEntity.getCardImgB());
						}else{
						//背面
							showCard(mCardEntity.getCardAOrientation(), mCardEntity.getCardImgA());
						}
						isFront = !isFront;
					}
				}
			});
		}
		this.startAnimation(this.mRotateAnimation);
	}
	
	//endregion private 方法

	//region interface && 子类
	/**
	 * 手势监听
	 * @author ZuoZiJi-Y.J
	 * @version v1.0
	 * @since 2014-3-14
	 *
	 */
	public class MyCardGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		//短按，触摸屏按下后片刻后抬起，会触发这个手势，如果迅速抬起则不会
		@Override
		public void onShowPress(MotionEvent e) {			
		}

		// 抬起，手指离开触摸屏时触发(长按、滚动、滑动时，不会触发这个手势)
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			
			if( Math.abs(distanceY) >= Math.abs(distanceX)){
				if(!isScrollX){
					//  上下滑动
					smoothScrollBy(0, (int)distanceY);
					isScrollY = true;
					isScrollX = false;
				}
			}else{
				if(!isScrollY){
					// 左右滑动
					smoothScrollBy((int)distanceX, 0);
					isScrollY = false;
					isScrollX = true;
				}
			}
			return false;
		}

		//触摸屏按下后既不抬起也不移动，过一段时间后触发
		@Override
		public void onLongPress(MotionEvent e) {	
			mListener.onLongClick();
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			return false;
		}
		/**
		 * 单击
		 */
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if(isTopClick){
				mListener.onTopTitleClick();
			}else{
				mListener.onClick();
			}
			return true;
		}
		/**
		 *  双击时产生一次
		 */
		public boolean onDoubleTap(MotionEvent e) {
			//旋转旋转图片
			mListener.onAnimRoteImage();
			startRoteAnimation();
			return true;
		}
	}
	/**
	 * 控件 单击 滑动处理接口
	 * @author ZuoZiJi-Y.J
	 * @version v1.0
	 * @since 2014-3-14
	 *
	 */
	public interface OnEventListener {
		/**
		 * 向上滑动
		 */
		void onScrollUpComplete();
		/**
		 * 向下滑动
		 */
		void onScrollDownComplete();
		/**
		 * 单击事件
		 */
		void onClick();
		/**
		 * 双击旋转事件
		 */
		void onAnimRoteImage();
		/**
		 * 顶部点击事件
		 */
		void onTopTitleClick();
		/**
		 * 长按事件
		 */
		void onLongClick();
		
		/**
		 * 向右滑动
		 */
		void onScrollRightComplete();
		/**
		 * 向左滑动
		 */
		void onScrollLeftComplete();
	}
	//endregion interface && 子类
}
