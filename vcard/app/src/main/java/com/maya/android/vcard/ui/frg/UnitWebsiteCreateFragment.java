package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ButtonHelper;

/**
 * 单位：微官网：创建微官网
 */
public class UnitWebsiteCreateFragment extends BaseFragment {
    private TextView mTxvShowHint;
    private Button mBtnNagtive, mBtnPositive;
    private View mViewline;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 点击事件
            switch (v.getId()){
                case R.id.btn_nagtive:
                    //取消
                    break;
                case R.id.btn_positive:
                    //确认登录、扫描二维码登录
                    ButtonHelper.setButtonEnableDelayed(mBtnPositive);
                    ActivityHelper.showToast(R.string.the_feature_is_not_yet_open);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment View view = inflater.inflate(R.layout.fragment_unit_website_join, container, false);
        View view = inflater.inflate(R.layout.fragment_unit_website_create, container, false);
        this.mBtnNagtive = (Button) view.findViewById(R.id.btn_nagtive);
        this.mBtnPositive = (Button) view.findViewById(R.id.btn_positive);
        this.mTxvShowHint = (TextView) view.findViewById(R.id.txv_hint);
        this.mViewline = view.findViewById(R.id.view_line);
        this.mBtnNagtive.setOnClickListener(this.mOnClickListener);
        this.mBtnPositive.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.getActivityTopRightTxv().setVisibility(View.GONE);
        super.mTitleAction.setActivityTitle(R.string.create_official_website, false);
    }

    /**
     * 确认登录
     */
    private void confirmLogin(){
        this.mViewline.setVisibility(View.VISIBLE);
        this.mBtnNagtive.setVisibility(View.VISIBLE);
        this.mBtnPositive.setText(getActivity().getString(R.string.confirm_login));
        this.mTxvShowHint.setText(R.string.will_login_form_computer_confirm_please);
    }

    /**
     * 扫描二维码登录
     */
    private void scanQrCodeLogin(){
        this.mViewline.setVisibility(View.GONE);
        this.mBtnNagtive.setVisibility(View.GONE);
        this.mBtnPositive.setText(getActivity().getString(R.string.scan_computer_login));
        this.mTxvShowHint.setText(R.string.we_official_establish_please);
    }

}
