package com.maya.android.vcard.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.maya.android.vcard.R;

/**
 * 字母检索侧边栏
 * 
 * @author zheng_cz 2013-03-29
 * 
 */
public class SideBar extends TextView {
	OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	private boolean isShowBkg = false;
	private int mChoose = -1;
	private int mBarHeight;
	private Paint mPaint = new Paint();
	private int mBgColor = Color.parseColor("#40000000");
	public static String[] mAlphaArr = { "#", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };
	/**
	 * 底部导航 高度
	 */
	private int mNavBottomHeight = 60; 
	public SideBar(Context context) {
		super(context);
		this.mNavBottomHeight = context.getResources().getDimensionPixelSize(R.dimen.dimen_49dp) + 10;
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mNavBottomHeight = context.getResources().getDimensionPixelSize(R.dimen.dimen_49dp) + 10;
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mNavBottomHeight = context.getResources().getDimensionPixelSize(R.dimen.dimen_49dp) + 10;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 去除底部导航的高度
		this.mBarHeight = getHeight();
		int width = getWidth();
		int len = this.mAlphaArr.length;
		int singleHeight = mBarHeight / len;
		for (int i = 0 ; i < len; i++) {
			this.mPaint.setColor(getCurrentTextColor());
			// paint.setColor(Color.WHITE);
			this.mPaint.setTypeface(Typeface.DEFAULT_BOLD);
			this.mPaint.setAntiAlias(true);
			this.mPaint.setTextSize(getTextSize());
			if (i == this.mChoose) {
				this.mPaint.setColor(Color.parseColor("#3399ff"));
				this.mPaint.setFakeBoldText(true);
			}
			float xPos = width / 2 - mPaint.measureText(mAlphaArr[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(this.mAlphaArr[i], xPos, yPos, mPaint);
			this.mPaint.reset();
		}
		if (!this.isShowBkg) {
			canvas.drawColor(Color.TRANSPARENT); 
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = this.mChoose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / mBarHeight * mAlphaArr.length);
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			this.isShowBkg = true;
			if (oldChoose != c && listener != null) {
				if (c > 0 && c < this.mAlphaArr.length) {
					listener.onTouchingLetterChanged(this.mAlphaArr[c]);
					this.mChoose = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (oldChoose != c && listener != null) {
				if (c > 0 && c < this.mAlphaArr.length) {
					listener.onTouchingLetterChanged(this.mAlphaArr[c]);
					this.mChoose = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			this.isShowBkg = false;
			this.mChoose = -1;
			invalidate();
			break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	/**
	 * * 向外公开的方法 * * @param onTouchingLetterChangedListener
	 * */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * * 接口 * * @author coder *
	 */
	public interface OnTouchingLetterChangedListener {
		 void onTouchingLetterChanged(String s);
	}
}
