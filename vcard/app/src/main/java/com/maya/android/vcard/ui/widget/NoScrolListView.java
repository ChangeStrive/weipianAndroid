package com.maya.android.vcard.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 禁用滚动的listview
 * @author zheng_cz
 * @since 2014年4月19日 下午4:32:16
 */
public class NoScrolListView extends ListView {

	/**
	 * @param context
	 */
	public NoScrolListView(Context context) {
		super(context);		
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public NoScrolListView(Context context, AttributeSet attrs) {
		super(context, attrs);		
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public NoScrolListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);		
	}
	
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}

}
