package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.base.BaseFragment;

/**
 * 单位：微官网
 */
public class UnitWebsiteFragment extends BaseFragment {
    private TextView mTxvWeOfficalUrl, mTxvWeOfficalHome;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txv_we_official_add_url:
                    //添加原有微官网链接
                    mFragmentInteractionImpl.onFragmentInteraction(UnitWebsiteJoinFragment.class.getName(), null);
                    break;
                case R.id.txv_we_official_establish_home:
                    //创建微官网
                    mFragmentInteractionImpl.onFragmentInteraction(UnitWebsiteCreateFragment.class.getName(), null);
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unit_website, container, false);
        this.mTxvWeOfficalUrl = (TextView) view.findViewById(R.id.txv_we_official_add_url);
        this.mTxvWeOfficalHome = (TextView) view.findViewById(R.id.txv_we_official_establish_home);
        this.mTxvWeOfficalUrl.setOnClickListener(this.mOnClickListener);
        this.mTxvWeOfficalHome.setOnClickListener(this.mOnClickListener);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.getActivityTopRightTxv().setVisibility(View.GONE);
        super.mTitleAction.setActivityTitle(R.string.official_website, false);

    }
}
