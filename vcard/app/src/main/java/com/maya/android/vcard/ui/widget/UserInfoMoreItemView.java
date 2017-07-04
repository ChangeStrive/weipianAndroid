package com.maya.android.vcard.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;

public class UserInfoMoreItemView extends RelativeLayout {

	private TextView mTxvContent;
	private View mViewLine;
	
	public UserInfoMoreItemView(Context context) {
		super(context);
		init(context);
	}

	public UserInfoMoreItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context mContext) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_userinfo_more, this);
		this.mTxvContent = (TextView)view.findViewById(R.id.txv_content);
		this.mViewLine = (View)view.findViewById(R.id.view_divider);
	}

	/**
	 * 赋值
	 * @param text
	 */
	public void setTxvContent(String text){
		if(Helper.isNotNull(text)){
			this.mTxvContent.setText(text);
		}
	}
	/**
	 * 赋值
	 * @param resId
	 */
	public void setTxtContent(int resId){
		if(Helper.isNotNull(resId)){
			this.mTxvContent.setText(resId);
		}
	}
	
	/**
	 * 分割线
	 * @param visibility
	 */
	public void setmViewline(int visibility){
		this.mViewLine.setVisibility(visibility);
	}
}
