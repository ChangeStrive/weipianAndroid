package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 我的：财富中心
 */
public class UserWealthCenterFragment extends BaseFragment {
    private TextView mTxvUserName, mTxvMyselfIntegral;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_wealth_center, container, false);
        this.mTxvUserName = (TextView) view.findViewById(R.id.txv_user_name);
        this.mTxvMyselfIntegral = (TextView) view.findViewById(R.id.txv_myself_integral);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleAction.setActivityTitle(R.string.user_money, true);
        UserInfoResultEntity userEntity = CurrentUser.getInstance().getUserInfoEntity();
        if(ResourceHelper.isNotNull(userEntity)){
            this.mTxvUserName.setText(userEntity.getVcardNo());
            this.mTxvMyselfIntegral.setText("" + userEntity.getIntegral());
        }
    }
}
