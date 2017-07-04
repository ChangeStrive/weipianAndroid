package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.base.BaseFragment;

/**
 * 单位：微官网：加入微官网
 */
public class UnitWebsiteJoinFragment extends BaseFragment {

    private TextView mTxvBinding, mEdtUrl;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txv_we_official_join_Binding:
                    ActivityHelper.showToast(R.string.no_operation_permissions);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unit_website_join, container, false);
        this.mTxvBinding = (TextView) view.findViewById(R.id.txv_we_official_join_Binding);
        this.mEdtUrl = (EditText) view.findViewById(R.id.edt_we_official_join_url);
        this.mTxvBinding.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.getActivityTopRightTxv().setVisibility(View.GONE);
        super.mTitleAction.setActivityTitle(R.string.join_official_website, false);
    }
}
