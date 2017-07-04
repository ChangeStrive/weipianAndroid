package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.base.BaseFragment;

/**
 * 交换结果：扫一扫（单个）
 */
public class VCardSwapResultSingleFragment extends BaseFragment {
    private AsyncImageView mImvHead;
    private TextView mTxvName, mTxvJob, mTxvCompany;
    private RelativeLayout mRelCardDeatilCard;
    private Button mBtnSubmit;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           switch (v.getId()){
               case R.id.btn_submit:
                   //TODO 名片交换
                   break;
           }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vcard_swap_result_single, container, false);
        this.mImvHead = (AsyncImageView) view.findViewById(R.id.imv_head);
        this.mTxvName = (TextView) view.findViewById(R.id.txv_name);
        this.mTxvJob = (TextView) view.findViewById(R.id.txv_job);
        this.mTxvCompany = (TextView) view.findViewById(R.id.txv_company);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mRelCardDeatilCard = (RelativeLayout) view.findViewById(R.id.rel_card_details_card);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
