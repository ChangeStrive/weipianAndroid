package com.maya.android.vcard.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.maya.android.vcard.R;

/**
 * 滑动开关自定义控件
 * 
 * @author zheng_cz
 * 
 */
public class SlipButton extends View implements OnTouchListener {
	public interface OnSlipChangedListener {
		abstract void OnChanged(View v, String strName, boolean checkState);
	}

	private String mStrName;
	private boolean isEnabled = true;
	// 设置初始化状态
	public boolean isFlag = false;
	// 记录当前按钮是否打开,true为打开,flase为关闭
	public boolean isNowChoose = false;
	// 记录用户是否在滑动的变量
	private boolean isOnSlip = false;
	// 按下时的x,当前的x,NowX>100时为ON背景,反之为OFF背景
	public float mDownX = 0f, mNowX = 0f;
	// 打开和关闭状态下,游标的Rect
	private Rect mBtn_On, mBtn_Off;

	private boolean isChgLsnOn = false;
	private OnSlipChangedListener mChgLsn;
	private Bitmap mbg_on, mbg_off, mslip_btn_off, mslip_btn_on;

	public SlipButton(Context context) {
		super(context);
		init();
	}

	public SlipButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 设置状态
	 * 
	 * @param fl
	 * @return void
	 */
	public void setChecked(boolean fl) {
		if (fl) {
			this.isFlag = true;
			this.isNowChoose = true;
			this.mNowX = 80;
		} else {
			this.isFlag = false;
			this.isNowChoose = false;
			this.mNowX = 0;
		}
	}

	/**
	 * 设置可编辑状态
	 */
	public void setEnabled(boolean b) {
		if (b) {
			this.isEnabled = true;
		} else {
			this.isEnabled = false;
		}
	}

	/**
	 * 初始化
	 * 
	 * @return void
	 */
	private void init() {
		// 载入图片资源
//		mbg_on = BitmapFactory.decodeResource(getResources(), R.drawable.img_slip_on);
//		mbg_off = BitmapFactory.decodeResource(getResources(), R.drawable.img_slip_off);
//		mslip_btn_off = BitmapFactory.decodeResource(getResources(), R.drawable.img_slip_off_btn);
//		mslip_btn_on = BitmapFactory.decodeResource(getResources(), R.drawable.img_slip_on_btn);
		this.mbg_on = BitmapFactory.decodeResource(getResources(), R.mipmap.img_general_open);
		this.mbg_off = BitmapFactory.decodeResource(getResources(), R.mipmap.img_general_isc);
		// 获得需要的Rect数据
//		mBtn_On = new Rect(0, 0, mslip_btn_off.getWidth(), mslip_btn_off.getHeight());
//		mBtn_Off = new Rect(mbg_off.getWidth() - mslip_btn_off.getWidth(), 0, mbg_off.getWidth(),
//				mslip_btn_off.getHeight());
		// 设置监听器,也可以直接复写OnTouchEvent
		setOnTouchListener(this);
	}

	/**
	 * 绘图函数
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		
		float x;
//
//		if (isFlag) {
//			mNowX = 80;
//			isFlag = false;
//		}// bg_on.getWidth()=71
//			// 滑动到前半段与后半段的背景不同,在此做判断
//		if (mNowX < (mbg_on.getWidth() / 2)) {
//			// 画出关闭时的背景
//			canvas.drawBitmap(mbg_off, matrix, paint);
//		} else {
//			// 画出打开时的背景
//			canvas.drawBitmap(mbg_on, matrix, paint);
//		}
//		// 是否是在滑动状态, true=滑动 false=非滑动
//		if (isOnSlip) {
//			// 是否划出指定范围,不能让游标跑到外头,必须做这个判断
//			if (mNowX >= mbg_on.getWidth()) {
//				// 减去游标1/2的长度...
//				x = mbg_on.getWidth() - mslip_btn_off.getWidth() / 2;
//			} else {
//				x = mNowX - mslip_btn_off.getWidth() / 2;
//			}
//		} else {
//			if (isNowChoose) {
//				// 根据现在的开关状态设置画游标的位置
//				x = mBtn_Off.left;
//			} else {
//				x = mBtn_On.left;
//			}
//		}
//		// 对游标位置进行异常判断...
//		if (x < 0) {
//			x = 0;
//		} else if (x > mbg_on.getWidth() - mslip_btn_off.getWidth()) {
//			x = mbg_on.getWidth() - mslip_btn_off.getWidth();
//		}
		if (this.isNowChoose) {
			// 画出打开时的背景
//			canvas.drawBitmap(mbg_on, matrix, paint);
//			canvas.drawBitmap(mslip_btn_on, x, 0, paint);
			canvas.drawBitmap(this.mbg_on, 0, 0, paint);
		} else {
			// 画出关闭时的背景
//			canvas.drawBitmap(mbg_off, matrix, paint);
			// 画出游标.
//			canvas.drawBitmap(mslip_btn_off, x, 0, paint);
			canvas.drawBitmap(this.mbg_off, 0, 0, paint);
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//将图片宽高设置为控件宽高
		setMeasuredDimension(this.mbg_on.getWidth(), this.mbg_on.getHeight());
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		if (!this.isEnabled) {
			return false;
		}
		// 根据动作来执行代码
		switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				this.mNowX = event.getX();
				break;
			case MotionEvent.ACTION_DOWN:
				if (event.getX() > this.mbg_on.getWidth() || event.getY() > this.mbg_on.getHeight()){
					return false;
				}
				this.isOnSlip = true;
				this.mDownX = event.getX();
				this.mNowX = this.mDownX;
				break;
			case MotionEvent.ACTION_UP:// 松开
				this.isOnSlip = false;
				boolean LastChoose = this.isNowChoose;

				this.isNowChoose = !LastChoose;
//				if (event.getX() >= (mbg_on.getWidth() / 2)){
//					isNowChoose = true;
//				}else{
//					isNowChoose = false;
//				}
				// 如果设置了监听器,就调用其方法..
				if (this.isChgLsnOn && (LastChoose != this.isNowChoose)){
					this.mChgLsn.OnChanged(v, this.mStrName, this.isNowChoose);
				}
				break;
			default:

		}
		invalidate();// 重画控件
		return true;
	}

	/**
	 * 设置监听器,当状态修改的时候
	 * 
	 * @param name
	 * @param l
	 * @return void
	 */
	public void setOnChangedListener(String name, OnSlipChangedListener l) {
		this.mStrName = name;
		this.isChgLsnOn = true;
		this.mChgLsn = l;
	}
}
