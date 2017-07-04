package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.maya.android.vcard.R;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 通过短信发送名片
 */
public class UserSmsShareVCardFragment extends BaseFragment {
    /** 记录分享功能 **/
    private int SHARE_CODE;
    private EditText mEdtContact, mEdtContent;
    private Button mBtnSend;
    private ImageButton mImbBook;
    /** 是否允许发送请求 **/
    private boolean isRequestUrl = false;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_send:
                    ButtonHelper.setButtonEnableDelayed(mBtnSend);
                    //TODO 发送
                    break;
                case R.id.imv_book:
                    mFragmentInteractionImpl.onFragmentInteraction(UserChooseLocalContactFragment.class.getName(), null);
                    break;
            }
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_sms_share_vcard, container, false);
        this.mEdtContact = (EditText) view.findViewById(R.id.edt_sms_share_contact);
        this.mEdtContent = (EditText) view.findViewById(R.id.edt_sms_share_content);
        this.mBtnSend = (Button) view.findViewById(R.id.btn_send);
        this.mImbBook = (ImageButton) view.findViewById(R.id.imv_book);
        this.mBtnSend.setOnClickListener(this.mOnClickListener);
        this.mEdtContact.addTextChangedListener(this.mTextWatcher);
        this.mImbBook.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.mess_send_vcard, false);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        String userName = CurrentUser.getInstance().getUserInfoEntity().getDisplayName();
        this.mEdtContent.setText(getString(R.string.sms_content_recommend, userName, getString(R.string.common_vcard_web_url)));
        SHARE_CODE = UserShareFragment.SHARE_WAY_SOFTWARE;
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
            SHARE_CODE = bundle.getInt(UserShareFragment.KEY_SHARE_WAY, UserShareFragment.SHARE_WAY_SOFTWARE);
        }

    }



    /**
     * 联系人输入监听
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(count < 11){
                isRequestUrl = false;
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String mobile = s.toString();
            if(ResourceHelper.isValidMobile(mobile)){
                mEdtContact.setFocusable(false);
                requestRecommendUrl(mobile, false);
            }
        }
    };

    /**
     * 服务端交换
     * @param mobile
     * @param isChangeEdt
     */
    private void requestRecommendUrl(String mobile, boolean isChangeEdt){
        if(isChangeEdt){
            mEdtContact.setText(mobile);
        }
        String[] mobileArr = new String[]{mobile};
        //TODO 服务端请求
//        RequestAndResponseCentreData.getInstance().requestRecommendUrl(mobileArr, RecommendUrlJsonEntity.SHARE_TYPE_SMS, this);
    }
}
