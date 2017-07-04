package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.dao.ContactVCardDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.ContactEntity;
import com.maya.android.vcard.entity.InstantMessengerEntity;
import com.maya.android.vcard.entity.json.VCardInfoJsonEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 名片信息页
 */
public class VCardInfoFragment extends BaseFragment {
    private final String TAG = VCardInfoFragment.class.getName();
    /** 当前页面 key**/
    public static final String IS_SHOW_CURRENT_VCARD = "IS_SHOW_CURRENT_VCARD";
    /** 当前用户名片 **/
    public static final int VCARD_INO_CURUSER_CARD = 60001;
    /** 线上名片 **/
    public static final int VCARD_INO_OTHER_OLINE_CARD = 60002;
    /** 本地名片 **/
    public static final int VCARD_INO_OTHER_CONTCAT_CARD = 60003;
    /** 当前显示的名片 **/
    private int curShowCard;
    /** 是否是当前用户名片信息 **/
    private boolean isUnfoldInformation = true;
    /** 当前名片 Id**/
    private long mCurCardId;
    /**名片Key编号(联系人键值对 Id)**/
    private int mCurContactKeyId = 0;
    private TextView mTxvName, mTxvBusiness, mTxvCompany, mTxvJob, mTxvAdress, mTxvUrl;
    private TextView mTxvMobile, mTxvTelePhone, mTxvFax, mTxvEmail, mTxvQq, mTxvWeChat, mTxvMicrocobol;
    private TextView mTxvOtherInformation;
    private CardEntity mShowVCardEntity;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txv_other_information:
                    //其他信息的展示盒隐藏
                    if(isUnfoldInformation){
                        mTxvOtherInformation.setMaxLines(100);
                    }else{
                        mTxvOtherInformation.setMaxLines(3);
                    }
                    unfoldInformation();
                    break;
                case R.id.txv_act_top_right:
                    //编辑
                    Bundle bundle = new Bundle();
                    bundle.putInt(VCardInfoEditFragment.KEY_VCARD_TYPE, VCardInfoEditFragment.INTENT_CODE_VCARD_EDIT);
                    mFragmentInteractionImpl.onFragmentInteraction(VCardInfoEditFragment.class.getName(), null);
                    break;
            }
        }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_CARD_PERSON:
                    //请求联系人信息回调
                    ContactEntity mContact = JsonHelper.fromJson(commandResult, ContactEntity.class);
                    if(ResourceHelper.isNotNull(mContact)){
                        this.mShowVCardEntity = mContact.getCardEntity();
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vcard_info, container, false);
        this.mTxvName = (TextView) view.findViewById(R.id.txv_name);
        this.mTxvBusiness = (TextView) view.findViewById(R.id.txv_business);
        this.mTxvCompany = (TextView) view.findViewById(R.id.txv_company);
        this.mTxvJob = (TextView) view.findViewById(R.id.txv_job);
        this.mTxvAdress = (TextView) view.findViewById(R.id.txv_address);
        this.mTxvUrl = (TextView) view.findViewById(R.id.txv_url);
        this.mTxvMobile = (TextView) view.findViewById(R.id.txv_mobile);
        this.mTxvTelePhone = (TextView) view.findViewById(R.id.txv_telephone);
        this.mTxvFax = (TextView) view.findViewById(R.id.txv_fax);
        this.mTxvEmail = (TextView) view.findViewById(R.id.txv_email);
        this.mTxvQq = (TextView) view.findViewById(R.id.txv_qq);
        this.mTxvWeChat = (TextView) view.findViewById(R.id.txv_we_chat);
        this.mTxvMicrocobol = (TextView) view.findViewById(R.id.txv_micro_cobol);
        this.mTxvOtherInformation = (TextView) view.findViewById(R.id.txv_other_information);
        this.mTxvOtherInformation.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTitle(R.string.card_detail, false);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);


        this.showVCardData();

    }

    /**
     * 展开(收起)显示全部其他信息
     */
    private void unfoldInformation(){
        this.isUnfoldInformation = !this.isUnfoldInformation;
    }

    /**
     * 显示名片数据
     * @param
     */
    private void showVCardData(){
        Bundle arg = this.getArguments();
        this.curShowCard = VCARD_INO_CURUSER_CARD;
        if(Helper.isNotNull(arg)) {
            this.curShowCard = arg.getInt(IS_SHOW_CURRENT_VCARD, VCARD_INO_CURUSER_CARD);
        }
        String showName = "";
        switch (this.curShowCard ){
            case VCARD_INO_CURUSER_CARD:
                //当前用户名片
                super.mTitleAction.setActivityTopRightTxv(R.string.edit, this.mOnClickListener);
                super.mTitleAction.setActivityTopRightTxvVisibility(View.VISIBLE);
                this.mShowVCardEntity = CurrentUser.getInstance().getCurrentVCardEntity();
                UserInfoResultEntity currentUserInfoEntity = CurrentUser.getInstance().getUserInfoEntity();
                if(ResourceHelper.isNotNull(currentUserInfoEntity)){
                    showName = currentUserInfoEntity.getDisplayName();
                }
                break;
            case VCARD_INO_OTHER_OLINE_CARD:
                //线上用户名片
                this.mCurCardId = arg.getLong(Constants.IntentSet.INTENT_KEY_VCARD_ID, 0);
                showName = arg.getString(Constants.IntentSet.INTENT_KEY_VCARD_NAME, "");
                super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
                ContactEntity lineCard = ContactVCardDao.getInstance().getEntity(this.mCurCardId);
                this.mShowVCardEntity = lineCard.getCardEntity();
                if(ResourceHelper.isNull(this.mShowVCardEntity)){
                    requestContactVCardInfo(this.mCurCardId);
                }
                break;
            case VCARD_INO_OTHER_CONTCAT_CARD:
                //本地名片
                this.mCurContactKeyId = arg.getInt(Constants.IntentSet.INTENT_KEY_CONTACT_ID, 0);
                showName = arg.getString(Constants.IntentSet.INTENT_KEY_VCARD_NAME, "");
                super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
                ContactEntity contactCard = ContactVCardDao.getInstance().getEntity(this.mCurContactKeyId);
                this.mShowVCardEntity = contactCard.getCardEntity();
                break;
        }
        showVCardData(this.mShowVCardEntity, showName);
    }

    private void showVCardData(CardEntity entity, String showName){
        if(ResourceHelper.isNotNull(entity)){
            this.mTxvName.setText(showName);
            this.mTxvBusiness.setText(ResourceHelper.getBusinessNameByCode(getActivity(), entity.getBusiness()));
            this.mTxvCompany.setText(ResourceHelper.getImageUrlOnIndex(entity.getCompanyName(), 0));
            this.mTxvJob.setText(entity.getJob());
            this.mTxvAdress.setText(ResourceHelper.getImageUrlOnIndex(entity.getWorkAddress(), 0));
            this.mTxvUrl.setText(entity.getCompanyHome());
            this.mTxvMobile.setText(ResourceHelper.getImageUrlOnIndex(entity.getMobileTelphone(), 0));
            this.mTxvTelePhone.setText(entity.getLineTelphone());
            this.mTxvFax.setText(ResourceHelper.getImageUrlOnIndex(entity.getFax(), 0));
            this.mTxvEmail.setText(ResourceHelper.getImageUrlOnIndex(entity.getEmail(),0));
            String imJson = entity.getImJson();
            if(ResourceHelper.isNotEmpty(imJson)){
                Type typeOfIm = new TypeToken<ArrayList<InstantMessengerEntity>>(){}.getType();
                ArrayList<InstantMessengerEntity> imList = JsonHelper.fromJson(entity.getImJson(), typeOfIm);
                if(ResourceHelper.isNotNull(imList)){
                    for (int i = 0, size = imList.size(); i < size; i++) {
                        InstantMessengerEntity imItem = imList.get(i);
                        if(ResourceHelper.isNotNull(imItem)){
                            String label = imItem.getLabel();
                            if(label.contains(DatabaseConstant.InstantMessengerLabel.QQ)){
                                //腾讯QQ
                                this.mTxvQq.setText(imItem.getValue());
                            } else if(label.contains(DatabaseConstant.InstantMessengerLabel.MICROBLOG_SINA)){
                                //新浪微博
                                this.mTxvMicrocobol.setText(imItem.getValue());
                            } else if(label.contains(DatabaseConstant.InstantMessengerLabel.MICROBLOG_TX)){
                                //腾讯微博
                                this.mTxvMicrocobol.setText(imItem.getValue());
                            } else if(label.contains(DatabaseConstant.InstantMessengerLabel.WEI_XIN)){
                                //微信
                                this.mTxvWeChat.setText(ResourceHelper.getImageUrlOnIndex(imItem.getValue(), 0));
                            }
                        }
                     }
                }
            }
        }
    }

    /**
     *  请求名片联系人详细信息
     * @param cardId
     */
    public void requestContactVCardInfo(long cardId){
        URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
        if(Helper.isNotNull(urlEntity)){
            String token = CurrentUser.getInstance().getToken();
            VCardInfoJsonEntity jsonEntity = new VCardInfoJsonEntity();
            jsonEntity.setCardId(cardId);
            String json = JsonHelper.toJson(jsonEntity, VCardInfoJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CARD_PERSON, ProjectHelper.fillRequestURL(urlEntity.getCardPerson()), token, json, this);
        }
    }
}
