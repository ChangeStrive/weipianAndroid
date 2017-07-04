package com.maya.android.vcard.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.maya.android.vcard.R;

/**
 * 短信推荐好友添加更多应用(手机号码)输入框
 * Created by Administrator on 2015/10/12.
 */
public class AddPhoneNumberView extends RelativeLayout{
    private EditText mEdtPhoneNumber;
    private CheckBox mChbChecked;
    private ImageView mImvBook;
    private View mViewDivider;
    /** 记录当前View 的位置 **/
    private int curPosition;

    public AddPhoneNumberView(Context context) {
        super(context);
        init(context);
    }

    public AddPhoneNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context mContext){
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_lsv_user_sms_recommend_friend, this);
        this.mImvBook = (ImageView) view.findViewById(R.id.imv_user_recommend_book);
        this.mEdtPhoneNumber = (EditText) view.findViewById(R.id.edt_user_recommend_phone);
        this.mChbChecked = (CheckBox) view.findViewById(R.id.chb_user_recommend_select);
        this.mViewDivider = (View) view.findViewById(R.id.view_divider);
    }

    /**
     * 获取电话薄控件
     * @return
     */
    public ImageView getImvBook(){
        return this.mImvBook;
    }

    /**
     * 输入框赋值
     * @param text
     */
    public void setEdtContent(String text){
        this.mEdtPhoneNumber.setText(text);
    }

    /**
     * 设置勾选状态
     * @param isChecked
     */
    public void setChecked(boolean isChecked){
        this.mChbChecked.setChecked(isChecked);
    }

    /**
     * 获取CheckBox选中事件
     * @return
     */
    public boolean getChecked(){
        return this.mChbChecked.isChecked();
    }

    /**
     * 获取编辑框中的内容
     * @return
     */
    public String getEdtContent(){
        return this.mEdtPhoneNumber.getText().toString();
    }

    /**
     * 设置当前View 的位置
     * @param position
     */
    public void setCurPosition(int position){
        this.curPosition = position;
        if(position == 0){
            this.mViewDivider.setVisibility(View.GONE);
        }
    }

    /**
     * 获取当前view的位置
     * @return
     */
    public int getCurPosition(){
        return this.curPosition;
    }


}
