package com.maya.android.vcard.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.GridView;

/**
 * 禁用滚动的  gridview
 * @author zheng_cz
 * @since 2014年4月8日 下午1:50:50
 */
public class NoScrolGridView  extends GridView {
	
	 public NoScrolGridView(Context context, AttributeSet attrs) { 
	        super(context, attrs); 
	    } 

	    public NoScrolGridView(Context context) { 
	        super(context); 
	    } 

	    public NoScrolGridView(Context context, AttributeSet attrs, int defStyle) { 
	        super(context, attrs, defStyle); 
	    } 

	    @Override 
	    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

	        int expandSpec = MeasureSpec.makeMeasureSpec( 
	                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
	        super.onMeasure(widthMeasureSpec, expandSpec); 
	    } 

}
