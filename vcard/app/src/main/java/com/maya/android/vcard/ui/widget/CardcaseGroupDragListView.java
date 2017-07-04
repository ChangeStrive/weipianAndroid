package com.maya.android.vcard.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.maya.android.vcard.R;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 分组管理 可移动 listview
 * @author zheng_cz
 * @since 2014年4月18日 下午6:24:27
 */
public class CardcaseGroupDragListView extends ListView {

	// window窗口管理类
	private WindowManager mWindowManager;
	// 控制拖拽项的显示参数
	private WindowManager.LayoutParams mLayoutParams;
	// 拖拽项的item,其实就是一个ImageView
	private ImageView mDragImageView;
	// 手指拖动项(item)原始在列表中的位置
	private int mDragSrcPosition;
	// 手指点击准备拖动的时候，当前拖动项在列表中的位置
	private int mDragPosition;
	// 在当前数据项的位置
	private int mDragPoint;
	// 以当前视图和屏幕的距离(这里使用y方向上)
	private int mDragOffset;
	// 拖动的时候，开始向上滚动的边界
	private int mUpScollBounce;
	// 拖动的时候，开箱向下滚动的边界
	private int mDownScrollBounce;
	/** 固定分组数量 默认是4 **/
	private int mDefFixGpCount = 4;
	/** 用于拖拽图片的Id **/
	private int mImvId;

	private CardcaseGroupDragListListener listener;

	public CardcaseGroupDragListView(Context context) {
		super(context);

	}

	public CardcaseGroupDragListView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public CardcaseGroupDragListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	/**
	 * event.getX()和event.getRawX()的区别 getX()是表示Widget相对于自身左上角的x坐标,
	 * 而getRawX()是表示相对于屏幕左上角的x坐标值 getX()是标示Widget相对于自身左上角x坐标(如果不懂请查看源码)
	 * getRawX()是标示相对于屏幕左上角左上角,不管activity是否又titleBar或者是否全屏幕
	 * 
	 */

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// 按下
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			// 获取按下的x坐标(相对于父容器)
			int x = (int) ev.getX();
			// 获取按下的y坐标(相对于父容器)
			int y = (int) ev.getY();
			/**
			 * AbsListView里面有pointToPosition(x, y)方法根据x,y轴坐标
			 * 返回这个坐标对应的选中项的号码，所以把这个序号给原数据源的序号dragSrcPosition， 把这个序号给当前是拖动项
			 */
			this.mDragPosition = this.mDragSrcPosition = this.pointToPosition(x, y);
			// 如果是无效位置(超出边界，分割线等位置)，返回
			if (this.mDragPosition == AdapterView.INVALID_POSITION) {
				return super.onInterceptTouchEvent(ev);
			}
			// 获取当前位置的视图(可见状态)
			/**
			 * 这里要先说明getChildAt是返回*显示*(当前显示界面)在当前ViewGroup中坐标的item
			 * viewGroup.getFirstVisiblePosition()是当前显示页面中第一个item的在数据源的position
			 * 由于当前屏幕不一定是第一个屏幕所以必须用dragPoint
			 * -this.getFirstVisiblePosition()是当前页面的第几个item 比如
			 * dragPoint是50，而this
			 * .getFirstVisiblePosition是45(当前可以看到的第一个item就是45),你选中的
			 * (50)就是当前界面的第五个item
			 * getChildAt(int position)显示display在界面的position位置的View 
			 * getFirstVisiblePosition()返回第一个display在界面的view在adapter的位置position，可能是0，也可能是4
			 */
			ViewGroup itemView = (ViewGroup) this.getChildAt(this.mDragPosition - this.getFirstVisiblePosition());
			/**
			 * view.getTop()方法返回的是view相对于父容器顶部的距离，单位是像素。 而itemView的父容器是listview
			 * dragPoint点击位置在点击View内的相对位置 
			 * dragOffset屏幕位置和当前ListView位置的偏移量，这里只用到y坐标上的值 
			 * 这两个参数用于后面拖动的开始位置和移动位置的计算
			 */
			this.mDragPoint = y - itemView.getTop();
			this.mDragOffset = (int) (ev.getRawY() - y);
			// 获取可拖拽的图标
			View dragger = itemView.findViewById(this.mImvId);
			boolean isMove = dragger != null && dragger.getVisibility() == View.VISIBLE;
			if (isMove && x > this.getWidth() * 3 / 4 ) {
				// 取得向上滚动的边界，大概为该控件的1/3
				this.mUpScollBounce = this.getHeight() / 3;
				// 取得向下滚动的边界，大概为该控件的2/3
				this.mDownScrollBounce = this.getHeight() * 2 / 3;
				
				// 开启cache 获得选中项的影像bm，
				itemView.setDrawingCacheEnabled(true);
				itemView.setDrawingCacheBackgroundColor(getContext().getResources().getColor(R.color.color_c0e2f7));
				Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
				// 初始化影像
				startDrag(bm, y);
			}
			return false;

		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//如果dragmageView为空，说明拦截事件中已经判定仅仅是点击，不是拖动，返回
		 
		//如果点击的是无效位置，返回，需要重新判断

		if (this.mDragImageView != null && this.mDragPosition != AdapterView.INVALID_POSITION) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
				int upY = (int) ev.getY();		
				onDrop(upY);
				break;
			case MotionEvent.ACTION_MOVE:
				int moveY = (int) ev.getY();
				// 带着影像移动
				onDrag(moveY);
				break;
			case MotionEvent.ACTION_DOWN:
				break;
			default:
				break;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 设置固定分组数量 默认为4个
	 * @param count
	 */
	public void setDefFixGpCount(int count){
		this.mDefFixGpCount = count;
	}

	/**
	 *
	 * @param id 用于拖拽的控件Id
	 * @param listener 拖拽成功监听
	 */
	public void setCardcaseGroupDragListListenerAndDragImvId(int id, CardcaseGroupDragListListener listener){
		this.mImvId = id;
		this.listener = listener;
	}

	private void startDrag(Bitmap bm, int y) {
		//释放影像，在准备影像的时候，防止影像没释放，每次都执行一下
		delDrag();
		// 初始化window
		this.mLayoutParams = new WindowManager.LayoutParams();
		// 设置定布局 从上到下计算y方向上的相对位置
		this.mLayoutParams.gravity = Gravity.TOP;
		this.mLayoutParams.x = 0;
		this.mLayoutParams.y = y - this.mDragPoint + this.mDragOffset;
		//下面这些参数能够帮助准确定位到选中项点击位置
		this.mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		this.mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		this.mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE // 不需要焦点
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE // 不需要接触事件
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON // 保持设备常开，并保证高度不变
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN; // 窗口占满整个屏幕，忽略周围装饰边框(例如边界框，此窗口考虑到装饰边框的内容)
		// 窗口所使用的动画设置
		this.mLayoutParams.windowAnimations = 0;
		ImageView iv = new ImageView(this.getContext());
		iv.setImageBitmap(bm);
		this.mWindowManager = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
		this.mWindowManager.addView(iv,this.mLayoutParams);
		this.mDragImageView = iv;
	}


	/**
	 * 带着影像拖动
	 * 
	 * @param y
	 */
	private void onDrag(int y) {
		// 拖拽的item的值不能小于0，如果小于0说明处于无效区域
		int drag_top = y - this.mDragPoint;
		if (this.mDragImageView != null && drag_top >= 0) {
			// 设置透明度
			this.mLayoutParams.alpha = 0.5f;
			// 移动y值//记得要加上dragOffset,windowManager是计算整个屏幕的
			this.mLayoutParams.y = y - this.mDragPoint + this.mDragOffset;
			this.mWindowManager.updateViewLayout(this.mDragImageView, this.mLayoutParams);
		}
		// 避免拖动项在分割线的时position返回-1
		int tempPosition = this.pointToPosition(0, y);
		if (tempPosition != AdapterView.INVALID_POSITION) {
			this.mDragPosition = tempPosition;
		}

		//滚动
	    int scrollHeight = 0;
	    if(y < this.mUpScollBounce){
	        scrollHeight = 10;//定义向上滚动8个像素，如果可以向上滚动的话
	    }else if(y > this.mDownScrollBounce){
	        scrollHeight = -10;//定义向下滚动8个像素，，如果可以向上滚动的话
	    } 

	    if(scrollHeight!=0){
	    	// 获取你拖拽滑动位置及显示item相对应的view上
			View dragV = this.getChildAt(this.mDragPosition - this.getFirstVisiblePosition());
	        //真正滚动的方法setSelectionFromTop()
	        setSelectionFromTop(this.mDragPosition, dragV.getTop()+scrollHeight);
	    }
	}

	private void onDrop(int y)
	{
		//获取放下位置在数据集合中position
	    //定义临时位置变量为了避免滑动到分割线的时候，返回-1的问题，如果为-1，则不修改dragPosition的值，急需执行，达到跳过无效位置的效果
	    int tempPosition = pointToPosition(0, y);
	    if(tempPosition != INVALID_POSITION){
			this.mDragPosition = tempPosition;
	    }	     

	    //超出边界处理
	    if(y < getChildAt(0).getTop()){
	        //超出上边界，设为最小值位置 非固定分组的首个位置
			this.mDragPosition = this.mDefFixGpCount;
	    }
	    else if(y > getChildAt(getChildCount() - 1).getBottom()){

	        //超出下边界，设为最大值位置，注意哦，如果大于可视界面中最大的View的底部则是越下界，所以判断中用getChildCount()方法
	        //但是最后一项在数据集合中的position是getAdapter().getCount()-1，这点要区分清除
			this.mDragPosition = getAdapter().getCount()-1;
	    }	     

	    if(this.mDragPosition != this.mDragSrcPosition && this.mDragPosition >= this.mDefFixGpCount){
	    	//数据更新
			if(ResourceHelper.isNotNull(this.listener)){
					this.listener.exchange(this.mDragSrcPosition, this.mDragPosition);
			}

	    }	
	    //释放影像
	    delDrag();
	}

	/**
	 * 删除影像
	 */
	private void delDrag() {
		if (this.mDragImageView != null) {
			this.mWindowManager.removeView(this.mDragImageView);
			this.mDragImageView = null;
			this.setDrawingCacheEnabled(false);
			this.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
		}
	}

	public interface CardcaseGroupDragListListener{
		void exchange(int mDragSrcPosition,int mDragPosition);
	}
}
