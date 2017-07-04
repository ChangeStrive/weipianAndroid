package com.maya.android.vcard.ui.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;

/**
 * 自定义Ratingbar
 * @author Administrator
 *
 */
public class RatingbarView extends View{
	
	private Bitmap mBitmapGround, mBitampCover;
	private double mFloat = 0;
	
	public RatingbarView(Context context) {
		super(context);
		this.init();
	}

	public RatingbarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}
	
	public RatingbarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.init();
	}
	
	private void init(){
		if(Helper.isNull(mBitmapGround)){
			mBitmapGround =  BitmapFactory.decodeResource(getResources(), R.mipmap.bg_regular_members_gay);
		}
		if(Helper.isNull(mBitampCover)){
			mBitampCover =  BitmapFactory.decodeResource(getResources(), R.mipmap.bg_regular_members);
		}
			
	}
	
	/**
	 * 引入图片
	 * @param backgroundDrawableId 背景图片
	 * @param coverDrawableId 渐隐图片
	 */
	public void setRatingbarDrawable(int backgroundDrawableId, int coverDrawableId){
		mBitmapGround = BitmapFactory.decodeResource(getResources(), backgroundDrawableId);
		mBitampCover = BitmapFactory.decodeResource(getResources(), coverDrawableId);
	}
	
	/**
	 * 设置等级
	 * @param numRtb
	 */
	public void setRating(double numRtb){
		this.mFloat = numRtb * 0.2;
		if(this.mFloat > 1){
			this.mFloat = 1;
		}
		invalidate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(mBitmapGround.getWidth(), mBitmapGround.getHeight());
		setMeasuredDimension(mBitmapGround.getWidth(), mBitmapGround.getHeight());
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		canvas.save();
		if(Helper.isNotNull(mBitmapGround)){
			//设置画布
			canvas.drawBitmap(mBitmapGround, 0, 0, null);
		}
		if(Helper.isNotNull(mBitampCover)){
			//绘制区域
			canvas.clipRect(0, 0, getWidth() * (float) mFloat, getHeight());
			//将图片画在画布上
			canvas.drawBitmap(mBitampCover, 0, 0, paint);
		}
		canvas.restore();  
	}
}
