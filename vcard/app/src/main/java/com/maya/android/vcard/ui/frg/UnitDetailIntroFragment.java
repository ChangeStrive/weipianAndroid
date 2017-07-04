package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.UnitDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.json.UnitDetailIntroJsonEntity;
import com.maya.android.vcard.entity.result.UnitResultEntity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

/**
 *单位：详细页：简介
 */
public class UnitDetailIntroFragment extends BaseFragment {
    /** 单位Id Key **/
    public static final String KEY_DETAIL_INTRO_UNIT_ID = "key_detail_intro_unit_id";
    /** 单位简介 Key **/
    public static final String KEY_DETAIL_INTRO_UNIT_CONTENT = "KEY_DETAIL_INTRO_UNIT_CONTENT";
    /** 是否编辑 **/
    private boolean isEdit = true;
    /** 单位Id **/
    private long unitId = 0;
    /** 简介信息 **/
    private String introContent;
    private UnitResultEntity unit;
    private EditText mEdtUnitDetailIntro;
    private Button mBtnSubmit;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_submit:
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
                    publishUnitIntro();
                    break;
                case R.id.txv_act_top_right:
                    //编辑
                    if(isEdit){
                        mTitleAction.setActivityTopRightTxv(R.string.frg_text_cancel, mOnClickListener);
                        mEdtUnitDetailIntro.setEnabled(true);
                        mEdtUnitDetailIntro.setFocusableInTouchMode(true);
                        mEdtUnitDetailIntro.setFocusable(true);
                        mEdtUnitDetailIntro.setSelection(mEdtUnitDetailIntro.getText().toString().length());
                        mBtnSubmit.setVisibility(View.VISIBLE);
                    }else{
                        mTitleAction.setActivityTopRightTxv(R.string.edit, mOnClickListener);
                        mEdtUnitDetailIntro.setFocusable(false);
                        mBtnSubmit.setVisibility(View.GONE);
                    }
                    isEdit = !isEdit;
                    break;
            }
        }
    };

    @Override
    protected void onResponseFail(int tag, int responseCode, String msgInfo) {
        super.onResponseFail(tag, responseCode, msgInfo);
        ActivityHelper.closeProgressDialog();
        mEdtUnitDetailIntro.setEnabled(true);
        mEdtUnitDetailIntro.setFocusable(true);
    }

    @Override
    protected void onResponseSuccess(int tag, String msgInfo) {
        super.onResponseSuccess(tag, msgInfo);
        if(tag == Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_EDIT){
            ActivityHelper.closeProgressDialog();
            isEdit = ! isEdit;
            UnitDao.getInstance().updateAbout(this.unitId, this.introContent);
            mTitleAction.setActivityTopRightTxv(R.string.edit, mOnClickListener);
            mEdtUnitDetailIntro.setFocusable(false);
            mBtnSubmit.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unit_detail_intro, container, false);
        this.mEdtUnitDetailIntro = (EditText) view.findViewById(R.id.edt_unit_detail_intro);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.setActivityTitle(R.string.unit_detail, false);
        super.mTitleAction.setActivityTopRightTxv(R.string.edit, this.mOnClickListener);
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
            unitId = bundle.getLong(KEY_DETAIL_INTRO_UNIT_ID, 0);
            this.introContent = bundle.getString(KEY_DETAIL_INTRO_UNIT_CONTENT, "");
        }
        if(ResourceHelper.isEmpty(this.introContent)){
            this.unit = UnitDao.getInstance().getEntity(unitId);
            if(ResourceHelper.isNotNull(this.unit)){
                this.introContent = this.unit.getEnterpriseAbout();
            }
        }
        this.mEdtUnitDetailIntro.setText(this.introContent);
        boolean isAdmin =  UnitDao.getInstance().isAdminForMe(unitId);
        if(isAdmin){
            super.mTitleAction.setActivityTopRightTxvVisibility(View.VISIBLE);
        }else{
            super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        }
        this.mBtnSubmit.setVisibility(View.GONE);
        this.mEdtUnitDetailIntro.setEnabled(false);
    }

    /**
     * 发布单位简介
     */
    private void publishUnitIntro(){
        ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
        this.mEdtUnitDetailIntro.setEnabled(false);
        this.mEdtUnitDetailIntro.setFocusable(false);
        this.introContent = this.mEdtUnitDetailIntro.getText().toString();
        String token = CurrentUser.getInstance().getToken();
        String requestUrl = ProjectHelper.fillRequestURL(CurrentUser.getInstance().getURLEntity().getEnterPriseModify());
        UnitDetailIntroJsonEntity detailIntro = new UnitDetailIntroJsonEntity();
        detailIntro.setEnterpriseAbout(this.introContent);
        detailIntro.setEnterpriseId(this.unitId);
        String json = JsonHelper.toJson(detailIntro, UnitDetailIntroJsonEntity.class);
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_EDIT, requestUrl, token, json, this);
    }
}
