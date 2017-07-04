package com.maya.android.vcard.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.vcard.R;
/**
 * 名片信息编辑中添加更多控件的组合控件
 * @author Administrator
 *
 */
public class VCardInfoMoreEdtItemView extends RelativeLayout{
	private Context mContext;
	private View mViewLine;
	private TextView mTxvLable;
	private EditText mEditContent;
	private ImageButton mImbDelete;
	
	public VCardInfoMoreEdtItemView(Context context) {
		super(context);
		this.mContext = context;
		init(context);
	}

	public VCardInfoMoreEdtItemView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
		init(context);
	}
	
	private void init(Context mContext){
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_vcardinfo_more_edt, this);
		this.mTxvLable = (TextView) view.findViewById(R.id.txv_lable);
		this.mEditContent =(EditText) view.findViewById(R.id.edt_content);
		this.mImbDelete = (ImageButton) view.findViewById(R.id.imb_del);
		this.mViewLine = (View) view.findViewById(R.id.view_divider);
	}
	
	public void setLineVisiable(boolean isVisiable){
		if(isVisiable){
			this.mViewLine.setVisibility(View.VISIBLE);
		} else {
			this.mViewLine.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 给控件赋值
	 * @param labelResId 名称
	 * @param contentResId 内容	 
	 */
	public void setViewHint(int labelResId,int contentResId){
		this.mTxvLable.setText(labelResId);
		this.mEditContent.setHint(contentResId);
	}
	
	/**
	 * 给控件赋值
	 * @param strLabelRes 名称
	 * @param strContentRes 内容
	 */
	public void setViewHint(String strLabelRes,String strContentRes){
		this.mTxvLable.setText(strLabelRes);
		this.mEditContent.setHint(strContentRes);
	}
	/**
	 * 给编辑框的hint赋值
	 * @param hintResId
	 */
	public void setViewHint(int hintResId){
		this.mEditContent.setHint(hintResId);
	}
	/**
	 * 给控件赋值
	 * @param labelResId 名称
	 * @param contentResId 内容	 
	 */
	public void setViewValue(int labelResId,int contentResId){
		this.mTxvLable.setText(labelResId);
		this.mEditContent.setText(contentResId);
	}
	
	/**
	 * 给控件赋值
	 * @param strLabelRes 名称
	 * @param strContentRes 内容
	 */
	public void setViewValue(String strLabelRes,String strContentRes){
		this.mTxvLable.setText(strLabelRes);
		this.mEditContent.setText(strContentRes);
	}
	/**
	 * 设置标签内容
	 * @param labelResId
	 */
	public void setViewLabel(int labelResId){
		if(labelResId != -1){
			this.mTxvLable.setText(labelResId);
		}

	}
	/**
	 * 设置标签内容
	 * @param text
	 */
	public void setViewLabel(CharSequence text){
		this.mTxvLable.setText(text);
	}
    /**
     * 给编辑框赋值
     * @param text
     */
	public void setViewValue(CharSequence text){
		this.mEditContent.setText(text);
	}
	/**
	 * 获取按钮
	 */
	public ImageButton getImbDelete(){
		return this.mImbDelete;
	}
    
	/**
	 * 获取编辑内容
	 * @return
	 */
	public String getContent(){
		return mEditContent.getText().toString();
	}
	
	/**
	 * 获取标签内容
	 * @return
	 */
	public String getLabel(){
		return this.mTxvLable.getText().toString();
	}
	

	/**
	 * 设置控件内容
	 * @param content
	 */
	public void setContent(String content){
		this.mEditContent.setText(content);
	}
	
	/**
	 * 设置标签字体颜色
	 * @param colorId
	 */
	public void setLableViewColor(int colorId){
		if(R.color.color_00000000 == colorId){
			this.mViewLine.setVisibility(View.VISIBLE);
		}else{
			this.mViewLine.setVisibility(View.GONE);
		}
		this.mTxvLable.setTextColor(this.mContext.getResources().getColor(colorId));
	}
}
