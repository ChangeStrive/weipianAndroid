package com.maya.android.vcard.ui.frg;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.ContactVCardDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.ContactEntity;
import com.maya.android.vcard.entity.json.CloudSearchMobileJsonEntity;
import com.maya.android.vcard.entity.json.MessInviteJsonEntity;
import com.maya.android.vcard.entity.result.MessInviteListResultEntity;
import com.maya.android.vcard.entity.result.MessInviteResultEntity;
import com.maya.android.vcard.entity.result.MessageResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.act.UserMainActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 云端查找
 */
public class CardcaseCloudSearchFragment extends BaseFragment {
    public static final int FOR_RESULT_MOBILE = 33333;
    /** 云端查找 **/
    private static final int DEAL_CLOUD_SEARCH = 0;
    /** 云端交换 **/
    private static final int DEAL_CLOUD_EXCHANGE = 1;
    /** 短信推荐 **/
    private static final int DEAL_RECOMMEND_SEND = 2;
    /** 按钮类型 **/
    private int btnType = DEAL_CLOUD_SEARCH;
    /** 请求对方用户Id **/
    private long toAccountId;
    private CheckBox mChbSelected;
    private LinearLayout mLilContact;
    private View rootView;
    private ImageButton mImbBook;
    private RelativeLayout mRelFaceFail;
    private Button mBtnSubmit;
    private EditText mEdtPhone;
    private TextView mTxvName, mTxvJob, mTxvCompany;
    private AsyncImageView mImvHead;
    private UserInfoResultEntity userInfo = CurrentUser.getInstance().getUserInfoEntity();
    private CardEntity curCard =  CurrentUser.getInstance().getCurrentVCardEntity();
    private URLResultEntity urlEntity =  CurrentUser.getInstance().getURLEntity();

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imb_book:
                    //选择联系人
                    Intent intent = new Intent(getActivity(), UserMainActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserChooseLocalContactFragment.class.getName());
                    bundle.putInt(UserChooseLocalContactFragment.KEY_CHOOSE_LOCAL_CONTACT, UserChooseLocalContactFragment.FROM_ACTIVITY_RESULT);
                    intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                    startActivityForResult(intent, FOR_RESULT_MOBILE);
                    break;
                case R.id.btn_submit:
                    //点击按钮
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
                    //网络验证
                    if(!NetworkHelper.isNetworkAvailable(getActivity())){
                        ActivityHelper.showToast(R.string.no_network);
                        return;
                    }
                    requestCloud();
                    break;
            }
        }
    };

    @Override
    protected void onResponseFail(int tag, int responseCode, String msgInfo) {
        super.onResponseFail(tag, responseCode, msgInfo);
        switch (tag){
            case Constants.CommandRequestTag.CMD_SEARCH_DETAIL_FOR_PHONE:
                //云端手机号码查找失败回调
                this.mRelFaceFail.setVisibility(View.VISIBLE);
                this.mBtnSubmit.setText(R.string.pop_send_msg);
                ActivityHelper.closeProgressDialog();
                this.btnType = DEAL_RECOMMEND_SEND;
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_SWAP_CARD:
                //TODO  请求名片交换失败回调
                ActivityHelper.closeProgressDialog();
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_RECOMMOND_SMS_URL:
                //TODO 短信推荐失败回调
                break;
        }
    }

    @Override
    protected void onResponseSuccess(int tag, String msgInfo) {
        super.onResponseSuccess(tag, msgInfo);
        switch (tag){
            case Constants.CommandRequestTag.CMD_REQUEST_SWAP_CARD:
                //请求名片交换发送成功
                ActivityHelper.showToast(R.string.exchange_vcard_sended);
                ActivityHelper.closeProgressDialog();
                break;
        }
    }

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_SEARCH_DETAIL_FOR_PHONE:
                    //云端手机号码查找回调
                    ContactEntity contact = JsonHelper.fromJson(commandResult, ContactEntity.class);
                    if(ResourceHelper.isNotNull(contact)){
                        this.btnType = DEAL_CLOUD_EXCHANGE;
                        this.toAccountId = contact.getAccountId();
                        showCloudContact(contact);
                        int hasFriendKeyId = 0;
                        if(ResourceHelper.isNotNull(contact.getCardId())){
                            hasFriendKeyId = ContactVCardDao.getInstance().isExist(contact.getCardId());
                        }
                        if(hasFriendKeyId > 0){
                            this.mBtnSubmit.setText(R.string.swap_card_fail_is_friend);
                            ButtonHelper.setButtonEnable(mBtnSubmit, R.color.color_787878, false);
                         }else{
                            if(contact.getSwapHis() > 0){
                                this.mBtnSubmit.setText(R.string.swap_card_fail_exchange_sended);
                                ButtonHelper.setButtonEnable(mBtnSubmit, R.color.color_787878, false);
                            }else{
                                this.mBtnSubmit.setText(R.string.swap_cloud);
                                ButtonHelper.setButtonEnable(mBtnSubmit, R.color.color_399c2f, true);
                            }
                        }
                    }
                    ActivityHelper.closeProgressDialog();
                    break;
                case Constants.CommandRequestTag.CMD_REQUEST_RECOMMOND_SMS_URL:
                    //获取短信链接回调
                    MessInviteListResultEntity messInviteList = JsonHelper.fromJson(commandResult, MessInviteListResultEntity.class);
                    if(ResourceHelper.isNotNull(messInviteList)){
                        ArrayList<MessInviteResultEntity> messInvites = messInviteList.getUrlList();
                        if(ResourceHelper.isNotNull(messInvites)){
                            MessInviteResultEntity messInvite;
                            String phoneNumber = "";
                            String disPlayName = "";
                            if(ResourceHelper.isNotNull(this.userInfo)){
                                disPlayName = this.userInfo.getDisplayName();
                            }
                            for(int i = 0, size = messInvites.size(); i < size; i++){
                                messInvite = messInvites.get(i);
                                if(ResourceHelper.isNotNull(messInvite)){
                                    phoneNumber = ResourceHelper.getImageUrlOnIndex(messInvite.getMobile(), 0);
                                    if(ResourceHelper.isValidMobile(phoneNumber)){
                                        String msg = getString(R.string.sms_content_recommend, disPlayName, messInvite.getUrl());
                                        ResourceHelper.sendSMS(phoneNumber, msg, false);
                                    }
                                }
                            }
                        }
                    }
                    this.mEdtPhone.setText("");
                    ActivityHelper.showToast(R.string.had_recommend_sms_send);
                    break;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case FOR_RESULT_MOBILE:
                     Bundle bundle = data.getBundleExtra("data");
                    if(ResourceHelper.isNotNull(bundle)){
                        String mible = bundle.getString(Constants.IntentSet.INTENT_KEY_MOBILE, "");
                        this.mEdtPhone.setText(mible);
//                        ButtonHelper.setButtonEnable(mBtnSubmit, R.color.color_399c2f, true);
                    }
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cardcase_cloud_search, container, false);
        this.mImbBook = (ImageButton) view.findViewById(R.id.imb_book);
        this.mRelFaceFail = (RelativeLayout) view.findViewById(R.id.rel_face_fail);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mEdtPhone = (EditText) view.findViewById(R.id.edt_enter_phone);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        this.mImbBook.setOnClickListener(this.mOnClickListener);
        this.mEdtPhone.addTextChangedListener(this.mTextWatcher);
        this.mLilContact = (LinearLayout) view.findViewById(R.id.lil_contact);
        this.mTxvName = (TextView) view.findViewById(R.id.txv_name);
        this.mTxvJob = (TextView) view.findViewById(R.id.txv_job);
        this.mTxvCompany = (TextView) view.findViewById(R.id.txv_company);
        this.mImvHead = (AsyncImageView) view.findViewById(R.id.imv_head);
        this.mChbSelected = (CheckBox) view.findViewById(R.id.chb_is_agree);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rootView = view;
        ButtonHelper.setButtonEnable(this.mBtnSubmit, R.color.color_787878, false);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.pop_cloud_search, false);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.getActivityTopRightTxv().setVisibility(View.GONE);
        this.mBtnSubmit.setText(R.string.pop_cloud_search);
        this.mChbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mBtnSubmit.getText().toString().equals(getString(R.string.swap_cloud)) && isChecked){
                    ButtonHelper.setButtonEnable(mBtnSubmit, R.color.color_399c2f, true);
                }else{
                    ButtonHelper.setButtonEnable(mBtnSubmit, R.color.color_787878, false);
                }

            }
        });
    }

    /**
     * 电话号码输入监听
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
           String mMobileNum = s.toString().trim();
            if(ResourceHelper.isNotEmpty(mMobileNum) && ResourceHelper.isValidMobile(mMobileNum)){
                if( mMobileNum.equals(userInfo.getMobileTel())){
                    ActivityHelper.showToast(R.string.no_to_search_owner);
                }else{
                    //隐藏软键盘
                   ResourceHelper.hideInputMethod(getActivity());
                   ButtonHelper.setButtonEnable(mBtnSubmit, R.color.color_399c2f, true);
                }
            }else{
                btnType = DEAL_CLOUD_SEARCH;
                mBtnSubmit.setText(R.string.pop_cloud_search);
                ButtonHelper.setButtonEnable(mBtnSubmit, R.color.color_787878, false);
                if(mRelFaceFail.getVisibility() == View.VISIBLE){
                    mRelFaceFail.setVisibility(View.GONE);
                }
                if(mLilContact.getVisibility() == View.VISIBLE){
                    mLilContact.setVisibility(View.GONE);
                }
            }
        }
    };

    /**
     * 显示查找到的联系人
     * @param contact
     */
    private void showCloudContact( ContactEntity contact){
        if(ResourceHelper.isNotNull(contact)){
            if(contact.getCloudFindShowType() == 1 && Helper.isNotEmpty(contact.getCompanyName())){
                String displayName ="";
                if(Helper.isNotEmpty(contact.getFullName())){
                    displayName = contact.getFullName() + ",";
                }
                mTxvName.setText(displayName );
                mTxvJob.setText(contact.getJob());
                mTxvCompany.setText(contact.getCompanyName());
            }else{
                mTxvName.setText(contact.getFullName());
                mTxvJob.setText(contact.getJob());
                mTxvCompany.setText(getString(R.string.vcard_sno_add_other, contact.getVcardNo()));
            }
            this.mImvHead.setDefaultImageResId(R.mipmap.img_default_upload_head);
            ResourceHelper.asyncImageViewFillUrl(this.mImvHead, ResourceHelper.getImageUrlOnIndex(contact.getHeadImg(), 0));
            this.mLilContact.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 发起请求
     */
    private void requestCloud(){
        //网络验证
        if(!NetworkHelper.isNetworkAvailable(getActivity())){
            ActivityHelper.showToast(R.string.no_network);
            return;
        }
        switch (btnType){
            case DEAL_CLOUD_SEARCH:
                //云端查找
                requestCloudSearch();
                break;
            case DEAL_CLOUD_EXCHANGE:
                // 云端交换
                requestExchangeVCard(this.toAccountId);
                break;
            case DEAL_RECOMMEND_SEND:
                // 短信推荐
                requestMessInvite(mEdtPhone.getText().toString());
                break;
        }
    }

    /**
     * 请求云端查找
     */
    private void requestCloudSearch(){
        if(ResourceHelper.isNotNull(this.urlEntity)){
            ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
            String jsonUrl =  ProjectHelper.fillRequestURL(urlEntity.getMobilePerson());
            String token = CurrentUser.getInstance().getToken();
            CloudSearchMobileJsonEntity jsonEntity = new CloudSearchMobileJsonEntity();
            jsonEntity.setMobileTel(this.mEdtPhone.getText().toString());
            String json = JsonHelper.toJson(jsonEntity, CloudSearchMobileJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_SEARCH_DETAIL_FOR_PHONE, jsonUrl, token, json, this);
        }
     }

    /**
     * 请求云端交换名片
     */
    private void requestExchangeVCard(long toAccountId){
        if(ResourceHelper.isNotNull(this.urlEntity)){
            ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
            String body = getString(R.string.notice_from_cloud_find);
            MessageResultEntity msgEntity = new MessageResultEntity();
            msgEntity.setBody(body);
            msgEntity.setToAccountId(toAccountId);
            msgEntity.setContentType(Constants.MessageContentType.NOTICE_CARD_SWAP_REQUEST);
            msgEntity.setResult(0);
            msgEntity.setCreateTime(ResourceHelper.getNowTime());
            msgEntity.setLoseTime(0);
            msgEntity.setTagId(0);
            long curCardId = 0;
            if(ResourceHelper.isNotNull(this.curCard)){
                curCardId = this.curCard.getId();
            }
            msgEntity.setFromCardId(curCardId);
            if(ResourceHelper.isNotNull(this.userInfo)){
                msgEntity.setFromAccountId(this.userInfo.getId());
                msgEntity.setFromHeadImg(this.userInfo.getHeadImg());
                msgEntity.setFromName(this.userInfo.getDisplayName());
            }
            String mAccessToken = CurrentUser.getInstance().getToken();
            String msgUrl = ProjectHelper.fillRequestURL(this.urlEntity.getMsgPushSingle());
            String toJson = JsonHelper.toJson(msgEntity, MessageResultEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_SWAP_CARD, msgUrl, mAccessToken, toJson, this);
        }
    }

    /**
     * 请求短信推荐
     * @param mobile
     */
    private void requestMessInvite(String mobile){
        if(ResourceHelper.isNotNull(this.urlEntity)){
            long curCardId = 0;
            if(ResourceHelper.isNotNull(this.curCard)){
                curCardId = this.curCard.getId();
            }
            if(curCardId < 1){
                ActivityHelper.showToast(R.string.you_card_add_please);
                return;
            }
            String url = ProjectHelper.fillRequestURL(this.urlEntity.getRecommonUrl());
            String accessToken = CurrentUser.getInstance().getToken();
            MessInviteJsonEntity messInvite = new MessInviteJsonEntity();
            messInvite.setCardId(curCardId);
            messInvite.setfMobileArr(new String[]{mobile});
            messInvite.setShareType(MessInviteJsonEntity.SHARE_TYPE_SMS);
            String json = JsonHelper.toJson(messInvite, MessInviteJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_RECOMMOND_SMS_URL, url, accessToken, json, this);
        }
    }
}
