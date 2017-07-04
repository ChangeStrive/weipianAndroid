package com.maya.android.vcard.ui.frg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.SettingEntity;
import com.maya.android.vcard.ui.adapter.CustomLsvAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 设置：隐私:交换模式
 */
public class SettingPrivacySwapModelFragment extends BaseFragment {
    private SettingEntity mSetting = CurrentUser.getInstance().getSetting();
    private TextView mTxvSlipVCard , mTxvScanVCard, mTxvRockVCard;//右边提示信息
    private int slipVCardPosition, scanVCardPosition, rockVCardPosition;
    private CustomDialogFragment mDialogFragmentSlipVCard, mDialogFragmentScanVCard, mDialogFragmentRockVCard;
    private View.OnClickListener mOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            //TODO 名片交换模式
            switch (v.getId()){
                case R.id.rel_privacy_swap_slip_vcard:
                    //滑一滑名片交换模式
                    showDialogFragmentSlipVCard();
                    break;
                case R.id.rel_privacy_swap_scan_vcard:
                    //扫一扫名片交换模式
                    showDialogFragmentScanVCard();
                    break;
                case R.id.rel_privacy_swap_rock_vcard:
                    //摇一摇名片交换模式
                    showDialogFragmentRockVCard();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_privacy_swap_model, container, false);
        RelativeLayout mReSlipVCard = (RelativeLayout) view.findViewById(R.id.rel_privacy_swap_slip_vcard);
        RelativeLayout mRelScanVCard = (RelativeLayout) view.findViewById(R.id.rel_privacy_swap_scan_vcard);
        RelativeLayout mRelRockVCard = (RelativeLayout) view.findViewById(R.id.rel_privacy_swap_rock_vcard);
        this.mTxvSlipVCard = (TextView) view.findViewById(R.id.txv_privacy_swap_slip_vcard);
        this.mTxvScanVCard = (TextView) view.findViewById(R.id.txv_privacy_swap_scan_vcard);
        this.mTxvRockVCard = (TextView) view.findViewById(R.id.txv_privacy_swap_rock_vcard);
        mReSlipVCard.setOnClickListener(this.mOnClickListener);
        mRelScanVCard.setOnClickListener(this.mOnClickListener);
        mRelRockVCard.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTitle(R.string.frg_setting_privacy_exchange_mode_setting, true);
        //滑一滑名片交换模式
        this.mTxvSlipVCard.setText(ResourceHelper.getResArrayChild(this.slipVCardPosition, R.array.swap_card_slip_pattern));
        //扫一扫名片交换模式
        this.mTxvScanVCard.setText(ResourceHelper.getResArrayChild(this.scanVCardPosition, R.array.swap_card_slip_pattern));
        //摇一摇名片交换模式
        this.mTxvRockVCard.setText(ResourceHelper.getResArrayChild(this.rockVCardPosition, R.array.swap_card_rock_pattern));
    }

    /**
     * 滑一滑名片交换对话框对话框
     */
    private void showDialogFragmentSlipVCard(){
        if(ResourceHelper.isNull(this.mDialogFragmentSlipVCard)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mLsvAdapter);
            ArrayList<Integer> unEnableList = new ArrayList<Integer>();
            unEnableList.add(1);
            mLsvAdapter.setUnEnableItems(unEnableList);
            mLsvAdapter.setPosition(this.slipVCardPosition);
            mLsvAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.swap_card_slip_pattern));
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    slipVCardPosition = position;
                    mLsvAdapter.setPosition(position);
                }
            });

            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        mTxvSlipVCard.setText(mLsvAdapter.getItem(slipVCardPosition));
                    }
                }
            };
            this.mDialogFragmentSlipVCard = DialogFragmentHelper.showCustomDialogFragment(R.string.slip_vcard_set, mLsvItem, R.string.frg_text_cancel, R.string.frg_text_ok, mOnClick);
        }
        this.mDialogFragmentSlipVCard.show(getFragmentManager(), "mDialogFragmentSlipVCard");
    }

    /**
     * 扫一扫名片交换模式对话框
     */
    private void showDialogFragmentScanVCard(){
        if(ResourceHelper.isNull(this.mDialogFragmentScanVCard)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mLsvAdapter);
            ArrayList<Integer> unEnableList = new ArrayList<Integer>();
            unEnableList.add(1);
            mLsvAdapter.setUnEnableItems(unEnableList);
            mLsvAdapter.setPosition(this.scanVCardPosition);
            mLsvAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.swap_card_slip_pattern));
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    scanVCardPosition = position;
                    mLsvAdapter.setPosition(position);
                }
            });

            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        mTxvScanVCard.setText(mLsvAdapter.getItem(scanVCardPosition));
                    }
                }
            };
            this.mDialogFragmentScanVCard = DialogFragmentHelper.showCustomDialogFragment(R.string.scan_vcard_set, mLsvItem, R.string.frg_text_cancel, R.string.frg_text_ok, mOnClick);
        }
        this.mDialogFragmentScanVCard.show(getFragmentManager(), "mDialogFragmentScanVCard");
    }

    /**
     * 扫一扫名片交换模式对话框
     */
    private void showDialogFragmentRockVCard(){
        if(ResourceHelper.isNull(this.mDialogFragmentRockVCard)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mLsvAdapter);
            ArrayList<Integer> unEnableList = new ArrayList<Integer>();
            unEnableList.add(1);
            unEnableList.add(2);
            unEnableList.add(3);
            mLsvAdapter.setUnEnableItems(unEnableList);
            mLsvAdapter.setPosition(this.rockVCardPosition);
            mLsvAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.swap_card_rock_pattern));
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    rockVCardPosition = position;
                    mLsvAdapter.setPosition(position);
                }
            });

            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        mTxvScanVCard.setText(mLsvAdapter.getItem(rockVCardPosition));
                    }
                }
            };
            this.mDialogFragmentRockVCard = DialogFragmentHelper.showCustomDialogFragment(R.string.rock_vcard_set, mLsvItem, R.string.frg_text_cancel, R.string.frg_text_ok, mOnClick);
        }
        this.mDialogFragmentRockVCard.show(getFragmentManager(), "mDialogFragmentScanVCard");
    }
}
