package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.AddVCardTemplateViewPager;

import java.util.ArrayList;

/**
 * 我的：微集市 */
public class UserMarketFragment extends BaseFragment {
    private RelativeLayout mRelLocalTemplate, mRelOnLineTemplate;
    private TextView mTxvLocalTemplate, mTxvOnLineTemplate;
    private View mViewLocalTemplate, mViewOnLineTemplate;
    private AddVCardTemplateViewPager mVwpTemplate;
    private ArrayList<View> mVwpList = new ArrayList<View>();
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rel_local_template:
                    //本地模板
                    break;
                case R.id.rel_on_line_template:
                    //在线模板
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_market, container, false);
        this.mRelLocalTemplate = (RelativeLayout) view.findViewById(R.id.rel_local_template);
        this.mRelOnLineTemplate = (RelativeLayout) view.findViewById(R.id.rel_on_line_template);
        this.mTxvLocalTemplate = (TextView) view.findViewById(R.id.txv_local_template);
        this.mTxvOnLineTemplate = (TextView) view.findViewById(R.id.txv_on_line_template);
        this.mViewLocalTemplate = (View) view.findViewById(R.id.view_selected_left);
        this.mViewOnLineTemplate = (View) view.findViewById(R.id.view_selected_right);
        this.mVwpTemplate = (AddVCardTemplateViewPager) view.findViewById(R.id.vwp_card_template);
        this.mRelLocalTemplate.setOnClickListener(this.mOnClickListener);
        this.mRelOnLineTemplate.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 切换选项
     * @param curIndex
     */
    private void switchTitle(int curIndex){
        int defaultColor = R.color.color_787878;
        int focusColor =R.color.color_d9690e;
        if(curIndex == Constants.Card.CARD_LOCAL_TEMPLATE){
            this.mTxvLocalTemplate.setTextColor(getActivity().getResources().getColor(focusColor));
            this.mTxvOnLineTemplate.setTextColor(getActivity().getResources().getColor(defaultColor));
            this.mViewLocalTemplate.setVisibility(View.VISIBLE);
            this.mViewOnLineTemplate.setVisibility(View.GONE);
        }else if(curIndex == Constants.Card.CARD_ONLINE_TEMPLATE){
            this.mTxvLocalTemplate.setTextColor(getActivity().getResources().getColor(defaultColor));
            this.mTxvOnLineTemplate.setTextColor(getActivity().getResources().getColor(focusColor));
            this.mViewLocalTemplate.setVisibility(View.GONE);
            this.mViewOnLineTemplate.setVisibility(View.VISIBLE);
        }
    }



}
