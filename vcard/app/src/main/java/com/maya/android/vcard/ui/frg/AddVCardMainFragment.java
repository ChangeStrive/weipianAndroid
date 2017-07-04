package com.maya.android.vcard.ui.frg;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.ui.act.AddVCardScanActivity;
import com.maya.android.vcard.ui.act.RegisterActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.wxapi.WXEntryActivity;

/**
 * 添加名片：主页
 */
public class AddVCardMainFragment extends BaseFragment {

    private boolean isNeedShowLogin = false;
    private boolean isNeedShowBack = false;
    private RelativeLayout mRelVCardScan, mRelVCardCustomCard, mRelVCardLocalup;
    private LinearLayout mLilAddVCardBottom, mLlAddVCard;
    private CurrentUser mCurrentUser = CurrentUser.getInstance();
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        //TODO 点击事件
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rel_vcard_scan:
                    //名片扫描
                    if (mCurrentUser.isLogin()) {
                        mActivitySwitchTo.switchTo(AddVCardScanActivity.class, null);
                    } else {
                        ActivityHelper.showToast(R.string.please_login_first);
                    }
                    break;
                case R.id.rel_vcard_custom_card:
                    //名片模板
                    if (mCurrentUser.isLogin()) {

                    } else {
                        ActivityHelper.showToast(R.string.please_login_first);
                    }
                    break;
                case R.id.rel_vcard_localup:
                    //名片上传
                    if (mCurrentUser.isLogin()) {
                        mFragmentInteractionImpl.onFragmentInteraction(AddVCardChooseUploadFormFragment.class.getName(), null);
                    } else {
                        ActivityHelper.showToast(R.string.please_login_first);
                    }
                    break;
                case R.id.lil_add_vcard_learn_now:
                    //立即体验
                    mActivitySwitchTo.switchTo(RegisterActivity.class, null);
                    break;
                case R.id.lil_add_vcard_login_now:
                    //登录
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.IntentSet.KEY_LOGIN_FRG_IS_SHOW_BACK, true);
                    intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                    mActivitySwitchTo.switchTo(WXEntryActivity.class, intent);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_vcard_main, container, false);
        this.mRelVCardScan = (RelativeLayout) view.findViewById(R.id.rel_vcard_scan);
        this.mRelVCardCustomCard = (RelativeLayout) view.findViewById(R.id.rel_vcard_custom_card);
        this.mRelVCardLocalup = (RelativeLayout) view.findViewById(R.id.rel_vcard_localup);
        LinearLayout mLilVCardLearnNow = (LinearLayout) view.findViewById(R.id.lil_add_vcard_learn_now);
        LinearLayout mLilVCardLoginNow = (LinearLayout) view.findViewById(R.id.lil_add_vcard_login_now);
        this.mLilAddVCardBottom = (LinearLayout) view.findViewById(R.id.lil_add_vcard_bottom);
        this.mLlAddVCard = (LinearLayout) view.findViewById(R.id.ll_add_vcard);//添加模板、名片
        this.mRelVCardScan.setOnClickListener(this.mOnClickListener);
        this.mRelVCardCustomCard.setOnClickListener(this.mOnClickListener);
        this.mRelVCardLocalup.setOnClickListener(this.mOnClickListener);
        mLilVCardLearnNow.setOnClickListener(this.mOnClickListener);
        mLilVCardLoginNow.setOnClickListener(this.mOnClickListener);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arg = getArguments();
        if (Helper.isNotNull(arg)) {
            this.isNeedShowLogin = arg.getBoolean(Constants.IntentSet.KEY_ADD_VCARD_FRG_IS_SHOW_LOGIN, false);
            this.isNeedShowBack = arg.getBoolean(Constants.IntentSet.KEY_ADD_VCARD_FRG_IS_SHOW_BACK, false);
        }
        if (this.isNeedShowLogin) {
            //需要显示登录
            this.mLilAddVCardBottom.setVisibility(View.VISIBLE);
            this.mLlAddVCard.setVisibility(View.GONE);//登录的时候不显示
        } else {
            //不需要显示登录
            this.mLilAddVCardBottom.setVisibility(View.GONE);
        }
        if (this.isNeedShowBack) {
            this.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
            this.mTitleAction.setActivityTitle(R.string.add_my_vcard, false);

        } else {
            this.mTitleAction.setActivityTopLeftVisibility(View.GONE);
            this.mTitleAction.setActivityTitle(R.string.add_my_vcard, true);

        }

        int screenWidth = ActivityHelper.getScreenWidth();
        int screenHeight = ActivityHelper.getScreenHeight();
        int btnWidth = (int) (screenWidth * Constants.ScaleButton.ADD_VCARD_SCALE_WIDTH_BUTTON);
        int btnHeight = (int) (screenHeight * Constants.ScaleButton.ADD_VCARD_SCALE_HEIGHT_BUTTON);

        LinearLayout.LayoutParams lpScan = (LinearLayout.LayoutParams) this.mRelVCardScan.getLayoutParams();
        lpScan.width = btnWidth;
        lpScan.height = btnHeight;
        lpScan.topMargin = (int) (screenHeight * ResourceHelper.getDimenNumFromResouce(getActivity(), R.dimen.dimen_0dot052dp));
        this.mRelVCardScan.setLayoutParams(lpScan);

        LinearLayout.LayoutParams lpCust = (LinearLayout.LayoutParams) this.mRelVCardCustomCard.getLayoutParams();
        lpCust.width = btnWidth;
        lpCust.height = btnHeight;
        this.mRelVCardCustomCard.setLayoutParams(lpCust);

        LinearLayout.LayoutParams lpUpLoad = (LinearLayout.LayoutParams) this.mRelVCardLocalup.getLayoutParams();
        lpUpLoad.width = btnWidth;
        lpUpLoad.height = btnHeight;
        this.mRelVCardLocalup.setLayoutParams(lpUpLoad);

        if (this.isNeedShowLogin) {
            //需要显示登录
            this.mLilAddVCardBottom.setVisibility(View.VISIBLE);
        } else {
            //不需要显示登录
            this.mLilAddVCardBottom.setVisibility(View.GONE);
        }
        if (this.isNeedShowBack) {
            this.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
//            this.mTitleAction.setActivityTitle(R.string.add_my_vcard, false);
            this.mTitleAction.setActivityTitle(R.string.add_my_vcard, true);
        } else {
            this.mTitleAction.setActivityTopLeftVisibility(View.GONE);
//            this.mTitleAction.setActivityTitle(R.string.add_my_vcard, true);
            this.mTitleAction.setActivityTitle(R.string.wec_my_vcard, true);
        }

    }

   /* @Override
    public void onResume() {
        super.onResume();
        if(this.isFromLoadingFragment){
            this.mLilAddVCardBottom.setVisibility(View.VISIBLE);
        }else{
            this.mLilAddVCardBottom.setVisibility(View.GONE);
        }
    }*/


}
