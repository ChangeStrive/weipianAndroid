package com.maya.android.vcard.ui.frg;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.act.MultiMainActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.impl.CardcaseCardNumImpl;
import com.maya.android.vcard.ui.impl.TwoFragmentParameterImpl;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 名片夹主页批量操作弹窗
 * Created by Administrator on 2015/10/8.
 */
public class CardcaseBatchOperationFragment extends BaseFragment implements CardcaseCardNumImpl {
    public static final String KEY_BUTTON_CODE = "key_button_code";
    /** 移除分组 **/
    public static final int REMOVE_GROUP = 70000001;
    /** 发送消息 **/
    public static final int SEND_MESS = 70000002;
    /** 删除名片 **/
    public static final int DELETE_CARD = 70000003;
    /** 选择所有 **/
    public static final int SELECT_ALL = 70000004;
    /** 全部取消 **/
    public static final int CANCEL_ALL = 70000005;
    /** 显示按钮可点击但不能发送消息 **/
    public static final int COUNT_AND_IS_NOT_SEND_MESS =  70000006;
    /** 显示按钮可点击并且能发送消息 **/
    public static final int COUNT_AND_IS_CAN_SEND_MESS = 70000007;
    /** 显示按钮不可点击 **/
    public static final int COUNT_IS_NOT =  70000008;
    protected TwoFragmentParameterImpl towFragmentParameterListener;
    private Button mBtnRemove, mBtnSend, mBtnDelete, mBtnSelectAll;
    protected MultiMainActivity multiMainActivity;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            switch (v.getId()){
                case R.id.btn_remove_group://移除分组
                        bundle.putInt(KEY_BUTTON_CODE, REMOVE_GROUP);
                    break;
                case R.id.btn_send_message://发送消息
                        bundle.putInt(KEY_BUTTON_CODE, SEND_MESS);
                    break;
                case R.id.btn_delete://删除
                        bundle.putInt(KEY_BUTTON_CODE, DELETE_CARD);
                    break;
                case R.id.btn_selected_all://选择所有
                    if(mBtnSelectAll.getText().toString().equals(getString(R.string.cancel_all))){
                        bundle.putInt(KEY_BUTTON_CODE, CANCEL_ALL);
                    }else{
                        bundle.putInt(KEY_BUTTON_CODE, SELECT_ALL);
                    }
                    break;
            }
            if(ResourceHelper.isNotNull(towFragmentParameterListener)){
                towFragmentParameterListener.twoFragmentTransfer(CardcaseMainFragment.class.getName(), bundle);
            }
        }
    };

    public static CardcaseBatchOperationFragment newInstance(){
        CardcaseBatchOperationFragment dialogFragment = new CardcaseBatchOperationFragment();
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //去掉原始标题栏
        View view  = inflater.inflate(R.layout.dialog_fragment_card_case_batch_operation, container, false);
        this.mBtnRemove = (Button) view.findViewById(R.id.btn_remove_group);
        this.mBtnSend = (Button) view.findViewById(R.id.btn_send_message);
        this.mBtnDelete = (Button) view.findViewById(R.id.btn_delete);
        this.mBtnSelectAll = (Button) view.findViewById(R.id.btn_selected_all);
        this.mBtnRemove.setOnClickListener(this.mOnClickListener);
        this.mBtnSend.setOnClickListener(this.mOnClickListener);
        this.mBtnDelete.setOnClickListener(this.mOnClickListener);
        this.mBtnSelectAll.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButtonHelper.setButtonEnable(this.mBtnRemove, R.color.color_787878, false);
        ButtonHelper.setButtonEnable(this.mBtnSend, R.color.color_787878, false);
        ButtonHelper.setButtonEnable(this.mBtnDelete, R.color.color_787878, false);
        ButtonHelper.setButtonEnable(this.mBtnSelectAll, R.color.color_399c2f, true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            towFragmentParameterListener = (TwoFragmentParameterImpl) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TwoFragmentParameterImpl");
        }

        try {
            multiMainActivity = (MultiMainActivity) activity;
            multiMainActivity.setCardcaseCardNumListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MultiMainActivity");
        }

     }

    @Override
    public void refreshCardcaseBatchUI(Bundle bundle) {
        if(ResourceHelper.isNotNull(bundle)){
            int typeUI = bundle.getInt(KEY_BUTTON_CODE, SELECT_ALL);
            switch (typeUI){
                case COUNT_IS_NOT:
                    //按钮不可以点
                    ButtonHelper.setButtonEnable(mBtnRemove, R.color.color_787878, false);
                    ButtonHelper.setButtonEnable(mBtnSend, R.color.color_787878, false);
                    ButtonHelper.setButtonEnable(mBtnDelete, R.color.color_787878, false);
                    mBtnSelectAll.setText(R.string.common_select_all);
                    break;
                case COUNT_AND_IS_NOT_SEND_MESS:
                    //按钮可点击但不能发送消息
                    ButtonHelper.setButtonEnable(mBtnRemove, R.color.color_399c2f, true);
                    ButtonHelper.setButtonEnable(mBtnSend, R.color.color_787878, false);
                    ButtonHelper.setButtonEnable(mBtnDelete, R.color.color_399c2f, true);
                    mBtnSelectAll.setText(R.string.cancel_all);
                    break;
                case COUNT_AND_IS_CAN_SEND_MESS:
                    //按钮可点击且能够发送消息
                    ButtonHelper.setButtonEnable(mBtnRemove, R.color.color_399c2f, true);
                    ButtonHelper.setButtonEnable(mBtnSend, R.color.color_399c2f, true);
                    ButtonHelper.setButtonEnable(mBtnDelete, R.color.color_399c2f, true);
                    mBtnSelectAll.setText(R.string.cancel_all);
                    break;
            }
        }
    }
}
