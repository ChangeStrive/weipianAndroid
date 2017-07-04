package com.maya.android.vcard.ui.frg;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.ui.act.WebActivity;
import com.maya.android.vcard.ui.base.BaseFragment;

/**
 * 设置：关于微片
 * A simple {@link Fragment} subclass.
 */
public class SettingAboutFragment extends BaseFragment {

    private TextView mTxvVersion;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 点击事件
            Intent intent;
            switch (v.getId()){
                case R.id.txv_homepage_voice:
                    //首页语;鸣谢
                    intent = new Intent();
                    intent.putExtra(Constants.IntentSet.KEY_INTENT_CODE, Constants.IntentSet.INTENT_CODE_SET_THANKS);
                    mActivitySwitchTo.switchTo(WebActivity.class, intent);
                    break;
                case R.id.txv_function_introduction:
                    //功能介绍
                    intent = new Intent();
                    intent.putExtra(Constants.IntentSet.KEY_INTENT_CODE, Constants.IntentSet.INTENT_CODE_FUNCTION_INTRODUCE);
                    mActivitySwitchTo.switchTo(WebActivity.class, intent);
                    break;
                case R.id.txv_using_help:
                    //使用帮助
                    intent = new Intent();
                    intent.putExtra(Constants.IntentSet.KEY_INTENT_CODE, Constants.IntentSet.INTENT_CODE_SET_HELP);
                    mActivitySwitchTo.switchTo(WebActivity.class, intent);
                    break;
                case R.id.txv_feedback:
                    //意见反馈
                    mFragmentInteractionImpl.onFragmentInteraction(SettingFeedbackFragment.class.getName(), null);
                    break;
                case R.id.txv_check_for_updates:
                    //检查更新
                    break;
                case R.id.txv_terms_of_use_and_privacy_policy:
                    //使用条款
                    intent = new Intent();
                    intent.putExtra(Constants.IntentSet.KEY_INTENT_CODE, Constants.IntentSet.INTENT_CODE_STATEMENT);
                    mActivitySwitchTo.switchTo(WebActivity.class, intent);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_about, container, false);
        TextView mTxvHomePageVoic = (TextView) view.findViewById(R.id.txv_homepage_voice);
        TextView mTxvFunction = (TextView) view.findViewById(R.id.txv_function_introduction);
        TextView mTxvUsingHelper = (TextView) view.findViewById(R.id.txv_using_help);
        TextView mTxvFeedback = (TextView) view.findViewById(R.id.txv_feedback);
        TextView mTxvCheckUpdate = (TextView) view.findViewById(R.id.txv_check_for_updates);
        TextView mTxvPolicy = (TextView) view.findViewById(R.id.txv_terms_of_use_and_privacy_policy);


        this.mTxvVersion = findView(view, R.id.txv_vcard_version);
        mTxvHomePageVoic.setOnClickListener(this.mOnClickListener);
        mTxvFunction.setOnClickListener(this.mOnClickListener);
        mTxvUsingHelper.setOnClickListener(this.mOnClickListener);
        mTxvFeedback.setOnClickListener(this.mOnClickListener);
        mTxvCheckUpdate.setOnClickListener(this.mOnClickListener);
        mTxvPolicy.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTitle(R.string.frg_setting_main_about_vcard, true);
        this.mTxvVersion.setText(getString(R.string.app_name) +" V" +ActivityHelper.getCurrentVersionName());
    }


}
