package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 添加名片：选择上传名片
 */
public class AddVCardChooseUploadFormFragment extends BaseFragment {
    private TextView mTxvStandard, mTxvHeteromorphism, mTxvFold;
    private ImageView mImvDivider;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle arg = new Bundle();
            int addUploadVCardForm = Constants.Card.CARD_FORM_9054;
            switch (v.getId()){
                case R.id.txv_standard_template:
                    //标准名片模板
                    addUploadVCardForm = Constants.Card.CARD_FORM_9054;
                    break;
                case R.id.txv_heteromorphism_template:
                    //异形名片模板
                    addUploadVCardForm = Constants.Card.CARD_FORM_9045;
                    break;
                case R.id.txv_fold_template:
                    //折叠名片模板
                    addUploadVCardForm = Constants.Card.CARD_FORM_9094;
                    break;
            }
            arg.putInt(AddVCardUploadFragment.ADD_UPLOAD_VCARD_FORM, addUploadVCardForm);
            mFragmentInteractionImpl.onFragmentInteraction(AddVCardUploadFragment.class.getName(), arg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_vcard_choose_upload_form, container, false);
        this.mTxvStandard = (TextView) view.findViewById(R.id.txv_standard_template);
        this.mTxvHeteromorphism = (TextView) view.findViewById(R.id.txv_heteromorphism_template);
        this.mTxvFold = (TextView) view.findViewById(R.id.txv_fold_template);
        this.mImvDivider = (ImageView) view.findViewById(R.id.imv_fold_template_divier);
        this.mTxvStandard.setOnClickListener(this.mOnClickListener);
        this.mTxvHeteromorphism.setOnClickListener(this.mOnClickListener);
        this.mTxvFold.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTitle(R.string.please_choose_add_upload_vcard_form, true);
        int allWidth = (int) (ActivityHelper.getScreenWidth() * Constants.ScaleButton.ADD_VCARD_CHOOSE_UPLOAD_WIDTH_BUTTON);
        int standardHeight = (int)( ActivityHelper.getScreenHeight() * Constants.ScaleButton.ADD_VCARD_CHOOSE_UPLOAD_STANDARD_HEIGHT_BUTTON);
        int heteromorphismHeight = (int)( ActivityHelper.getScreenHeight() * Constants.ScaleButton.ADD_VCARD_CHOOSE_UPLOAD_HETEROMORPHISM_HEIGHT_BUTTON);
        int flodHeight = (int)( ActivityHelper.getScreenHeight() *  Constants.ScaleButton.ADD_VCARD_CHOOSE_UPLOAD_FOLD_HEIGHT_BUTTON);
        setLayoutParams(this.mTxvStandard, allWidth, standardHeight);
        setLayoutParams(this.mTxvHeteromorphism, allWidth, heteromorphismHeight);
        setLayoutParams(this.mTxvFold, allWidth, flodHeight);
        setStoreTextView(mTxvStandard, getString(R.string.standard_template), getString(R.string.standard_template_specification));
        setStoreTextView(mTxvHeteromorphism, getString(R.string.heteromorphism_template), getString(R.string.heteromorphism_template_specification));
        setStoreTextView(mTxvFold, getString(R.string.fold_template), getString(R.string.fold_template_specification));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mImvDivider.getLayoutParams();
        params.width = allWidth;
        this.mImvDivider.setLayoutParams(params);
    }

     /**
     * 设置按钮宽高
     * @param mView
     * @param width
     * @param height
     */
    private void setLayoutParams(TextView mView, int width, int height){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mView.getLayoutParams();
        params.width = width;
        params.height = height;
        mView.setLayoutParams(params);
    }

    /**
     * 设置按钮内容及其字体大小
     * @param mTextView
     * @param topText
     * @param bottomText
     */
    private void setStoreTextView(TextView mTextView, String topText, String bottomText){
        ResourceHelper.setStoreTextSize(getActivity(),mTextView, topText, R.dimen.dimen_16sp, bottomText, R.dimen.dimen_14sp);
    }

}
