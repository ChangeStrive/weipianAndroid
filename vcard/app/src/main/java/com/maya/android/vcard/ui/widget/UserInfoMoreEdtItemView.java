package com.maya.android.vcard.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
/**
 * 我的资料添加更多输入框
 * @author Administrator
 *
 */
public class UserInfoMoreEdtItemView extends RelativeLayout {

	private EditText mEdtContent;
	private ImageButton mImbDelete;
	private View mRelCardEditItemView,mViewLine;
	public UserInfoMoreEdtItemView(Context context) {
		super(context);
		init(context);
	}

	public UserInfoMoreEdtItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context mContext) {
		this.mRelCardEditItemView = LayoutInflater.from(mContext).inflate(R.layout.item_userinfo_more_edt, this);
		this.mEdtContent = (EditText) this.mRelCardEditItemView.findViewById(R.id.edt_educational_information);
		this.mImbDelete = (ImageButton) this.mRelCardEditItemView.findViewById(R.id.imb_educational_information_del);
		this.mViewLine = (View) this.mRelCardEditItemView.findViewById(R.id.view_divider);
	}
	/**
	 * 获取删除控件
	 * @return
	 */
	public ImageButton getBtnDelete(){
		return this.mImbDelete;
	}
	/**
	 * 获取Edt中的内容
	 * @return
	 */
	public String getEdtContent(){
		String edtContent = this.mEdtContent.getText().toString();
		return edtContent;
		
	}
	
	/**
	 * 赋值
	 * @param text
	 */
	public void setEdtContent(String text){
		if(Helper.isNotNull(text)){
			this.mEdtContent.setText(text);
		}
		
	}
}
