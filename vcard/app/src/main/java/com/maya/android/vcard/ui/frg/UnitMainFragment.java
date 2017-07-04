package com.maya.android.vcard.ui.frg;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.UnitDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.result.UnitResultEntity;
import com.maya.android.vcard.ui.act.UserMainActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 单位主页
 */
public class UnitMainFragment extends BaseFragment {
    private CardEntity card =  CurrentUser.getInstance().getCurrentVCardEntity();;
    private UnitResultEntity unit;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 点击事件
            Bundle bundle = new Bundle();
            switch (v.getId()){
                case R.id.txv_business_assistant:
                    //企业运营助手
                    //do nothing
                    break;
                case R.id.txv_card_information:
                    //我的名片信息
                    if(Helper.isNotNull(card)) {
                        bundle.putInt(VCardInfoFragment.IS_SHOW_CURRENT_VCARD, VCardInfoFragment.VCARD_INO_CURUSER_CARD);
                        mFragmentInteractionImpl.onFragmentInteraction(VCardInfoFragment.class.getName(), bundle);
                    }else{
                        ActivityHelper.showToast(R.string.please_add_vcard);
                    }
                    break;
                case R.id.rel_official_website:
                    //微官网
                    mFragmentInteractionImpl.onFragmentInteraction(UnitWebsiteFragment.class.getName(), null);
                    break;
                case R.id.rel_picture_album:
                    //微画册
                    mFragmentInteractionImpl.onFragmentInteraction(UnitPictureFragment.class.getName(), null);
                    break;
                case R.id.txv_qr_code_card:
                    //二维码名片
                    if(Helper.isNotNull(card)) {
                        mFragmentInteractionImpl.onFragmentInteraction(VCardQRCodeFragment.class.getName(), null);
                    }else{
                        ActivityHelper.showToast(R.string.please_add_vcard);
                    }
                    break;
                case R.id.txv_share_card:
                    //分享这张名片
                    Intent intent = new Intent();
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserShareFragment.class.getName());
                    bundle.putInt(UserShareFragment.KEY_SHARE_WAY, UserShareFragment.SHARE_WAY_VCARD);
                    intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent);
                    break;
                case R.id.txv_act_top_right:
                    //单位认证
                    if(ResourceHelper.isNotNull(getUnit())){
                        bundle.putLong(UnitAuthFragment.KEY_UNIT_AUTH_UNIT_ID, getUnit().getEnterpriseId());
                        mFragmentInteractionImpl.onFragmentInteraction(UnitAuthFragment.class.getName(), bundle);
                    }else{
                         ActivityHelper.showToast(R.string.please_add_vcard);
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unit_main, container, false);
        TextView mTxvBusinessAassistant = (TextView) view.findViewById(R.id.txv_business_assistant);
        TextView mTxvCardINformation = (TextView) view.findViewById(R.id.txv_card_information);
        RelativeLayout mRelOfficialWebsite = (RelativeLayout) view.findViewById(R.id.rel_official_website);
        RelativeLayout mRelPictureAblum = (RelativeLayout) view.findViewById(R.id.rel_picture_album);
        TextView mTxvQrCodeCard = (TextView) view.findViewById(R.id.txv_qr_code_card);
        TextView mTxvShareCard = (TextView) view.findViewById(R.id.txv_share_card);
        mTxvBusinessAassistant.setOnClickListener(this.mOnClickListener);
        mTxvCardINformation.setOnClickListener(this.mOnClickListener);
        mRelOfficialWebsite.setOnClickListener(this.mOnClickListener);
        mRelPictureAblum.setOnClickListener(this.mOnClickListener);
        mTxvQrCodeCard.setOnClickListener(this.mOnClickListener);
        mTxvShareCard.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.unit_main, false);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightTxv(R.string.auth, this.mOnClickListener);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.VISIBLE);
    }

    /**
     * 获取单位信息
     * @return
     */
    private UnitResultEntity getUnit(){
       if(ResourceHelper.isNull(this.unit) && ResourceHelper.isNotNull(this.card)){
            String companyName = ResourceHelper.getImageUrlOnIndex(this.card.getCompanyName(), 0);
            this.unit = UnitDao.getInstance().getEntity(companyName);
        }
        return unit;
    }
}
