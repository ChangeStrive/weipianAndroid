package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.base.BaseFragment;

/**
 *会员等级：如何获得名片和积分
 *  */
public class UserLevelGetPointFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_level_get_point, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleAction.setActivityTitle(R.string.how_to_get_a_card_and_integral, false);
    }
}
