package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.json.AddAttentionJsonEntity;
import com.maya.android.vcard.entity.json.UserNearbyPeopleJsonEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.entity.result.UserNearbyPeopleEntity;
import com.maya.android.vcard.entity.result.UserNearbyPeopleListResultEntity;
import com.maya.android.vcard.ui.adapter.UserNearbyPeopleAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 附近的人
 */
public class UserNearbyPeopleFragment extends BaseFragment {
    /** 当前点击Item 的项 **/
    private int curPosition;
    private ListView mLsvNearbyPeople;
    private TextView mTxvEmpty;
    private ImageButton mImbAllPerson, mImbWoman, mImbMan;
    private UserNearbyPeopleAdapter userNearbyPeopleAdapter;
    private CustomDialogFragment userNearbyWarmPromptDialog, userNearbyNotCardDialog;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            recoveryImbBackground();
            switch (msg.what) {
                case R.id.imb_nearby_all_person:
                    //所有人
                    mImbAllPerson.setBackgroundColor(getResources().getColor(R.color.transparent));
                    userNearbyPeopleAdapter.showAllPeoples();
                    break;
                case R.id.imb_nearby_woman_person:
                    //女性朋友
                    mImbWoman.setBackgroundColor(getResources().getColor(R.color.transparent));
                    userNearbyPeopleAdapter.showFemale();
                    break;
                case R.id.imb_nearby_man_person:
                    //男性朋友
                    mImbMan.setBackgroundColor(getResources().getColor(R.color.transparent));
                    userNearbyPeopleAdapter.showMale();
                    break;
            }
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            recoveryImbBackground();
//            switch (v.getId()) {
//                case R.id.imb_nearby_all_person:
//                    //所有人
//                    mImbAllPerson.setBackgroundColor(getResources().getColor(R.color.transparent));
//                    userNearbyPeopleAdapter.showAllPeoples();
//                    break;
//                case R.id.imb_nearby_woman_person:
//                    //女性朋友
//                    mImbWoman.setBackgroundColor(getResources().getColor(R.color.transparent));
//                    userNearbyPeopleAdapter.showFemale();
//                    break;
//                case R.id.imb_nearby_man_person:
//                    //男性朋友
//                    mImbMan.setBackgroundColor(getResources().getColor(R.color.transparent));
//                    userNearbyPeopleAdapter.showMale();
//                    break;
//            }
            final View view = v;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    msg.what = view.getId();
                    mHandler.sendMessage(msg);
                }
            }).start();
        }
    };

    /**
     *关注TA事件监听
     */
    private UserNearbyPeopleAdapter.StatusListener mStatusListener = new UserNearbyPeopleAdapter.StatusListener() {
        @Override
        public void onClick(int position, String vCardNo) {
            requestAddAttention(vCardNo);
            curPosition = position;
        }
    };

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle bundle = new Bundle();
            bundle.putInt(UserInfoFragment.KEY_SHOW_USER, UserInfoFragment.CODE_SHOW_OTHER_USER_INFO);
            bundle.putString(UserInfoFragment.KEY_VCARDSNO, userNearbyPeopleAdapter.getItem(position).getVcardNo());
            mFragmentInteractionImpl.onFragmentInteraction(UserInfoFragment.class.getName(), bundle);
        }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_NEARBY_PEOPLE_LIST:

                    UserNearbyPeopleListResultEntity nearbyPeopleList = JsonHelper.fromJson(commandResult, UserNearbyPeopleListResultEntity.class);
                    if(ResourceHelper.isNotNull(nearbyPeopleList)){
                        //当前附近包含自己的所有人
                        ArrayList<UserNearbyPeopleEntity>  nearAllPeople = nearbyPeopleList.getPersonList();
                        //当前附近的人
                        ArrayList<UserNearbyPeopleEntity>  nearbyPeoples = new  ArrayList<UserNearbyPeopleEntity>();
                        long curAccountId = 0;
                        UserInfoResultEntity userInfo = CurrentUser.getInstance().getUserInfoEntity();
                        if(ResourceHelper.isNotNull(userInfo)){
                            curAccountId = userInfo.getId();
                        }
                        UserNearbyPeopleEntity curContact;
                        for(int i = 0, size = nearAllPeople.size(); i < size; i++ ){
                            curContact = nearAllPeople.get(i);
                            if(curContact.getAccountId() == curAccountId){
                                continue;
                            }
                            nearbyPeoples.add(curContact);
                        }
                        this.userNearbyPeopleAdapter.addItems(nearbyPeoples);
                    }
                    this.mTxvEmpty.setText(R.string.no_contact);
                    break;
            }
        }
        return true;

    }

    @Override
    protected void onResponseSuccess(int tag, String msgInfo) {
        super.onResponseSuccess(tag, msgInfo);
        if(tag == Constants.CommandRequestTag.CMD_REQUEST_ATTENTIONS_ADD){
            this.userNearbyPeopleAdapter.setStatusChange(curPosition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_nearby_people, container, false);
        this.mLsvNearbyPeople = (ListView) view.findViewById(R.id.lsv_list);
        this.mTxvEmpty = (TextView) view.findViewById(R.id.txv_lsv_empty);
        this.mLsvNearbyPeople.setEmptyView(this.mTxvEmpty);
        this.mImbAllPerson = (ImageButton) view.findViewById(R.id.imb_nearby_all_person);
        this.mImbWoman = (ImageButton) view.findViewById(R.id.imb_nearby_woman_person);
        this.mImbMan = (ImageButton) view.findViewById(R.id.imb_nearby_man_person);
        this.mImbAllPerson.setOnClickListener(this.mOnClickListener);
        this.mImbWoman.setOnClickListener(this.mOnClickListener);
        this.mImbMan.setOnClickListener(this.mOnClickListener);
        this.mLsvNearbyPeople.setOnItemClickListener(this.mOnItemClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.nearby_contacts, false);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        this.userNearbyPeopleAdapter = new UserNearbyPeopleAdapter(getActivity());
        this.mLsvNearbyPeople.setAdapter(this.userNearbyPeopleAdapter);
        this.userNearbyPeopleAdapter.setStatusListener(this.mStatusListener);
        boolean isShowAgain = PreferencesHelper.getInstance().getBoolean(Constants.Preferences.KEY_IS_NEARBY_PERSON_PROMPT_SHOW, true);
        if(isShowAgain){
            // 弹出提示信息
            showDialogFragmentWarmPrompt();
        }
        requestNearbyContact();
        //测试
    }

    private void recoveryImbBackground(){
        this.mImbAllPerson.setBackgroundResource(R.mipmap.bg_nearby_select);
        this.mImbWoman.setBackgroundResource(R.mipmap.bg_nearby_select);
        this.mImbMan.setBackgroundResource(R.mipmap.bg_nearby_select);
    }

    /**
     * 温馨提示对话框
     */
    private void showDialogFragmentWarmPrompt(){
        if(ResourceHelper.isNull(this.userNearbyWarmPromptDialog)){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_cardcase_choose_propmt, null);
            TextView tvxMsg = (TextView) view.findViewById(R.id.txv_msg);
            tvxMsg.setText(R.string.confirm_nearby_people);
            final CheckBox chbIsAgree = (CheckBox) view.findViewById(R.id.chb_is_agree);
            CustomDialogFragment.DialogFragmentInterface dlgOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        if(chbIsAgree.isChecked()){
                            PreferencesHelper.getInstance().putBoolean(Constants.Preferences.KEY_IS_NEARBY_PERSON_PROMPT_SHOW, false);
                        }else{
                            PreferencesHelper.getInstance().putBoolean(Constants.Preferences.KEY_IS_NEARBY_PERSON_PROMPT_SHOW, true);

                        }
                    }
                }
            };
            this.userNearbyWarmPromptDialog = DialogFragmentHelper.showCustomDialogFragment(R.string.warm_prompt, view, R.string.frg_text_ok, dlgOnClick);
        }
        this.userNearbyWarmPromptDialog.show(getFragmentManager(), "userNearbyWarmPromptDialog");
    }

    /**
     * 还未添加名片提示框
     */
    private void showDialogFragmentNotCard(){
        if(ResourceHelper.isNull(this.userNearbyNotCardDialog)){
            CustomDialogFragment.DialogFragmentInterface dlgOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        //TODO 添加名片
                    }else{
                        mFragmentInteractionImpl.onActivityBackPressed();
                    }
                }
            };
            this.userNearbyNotCardDialog = DialogFragmentHelper.showCustomDialogFragment(R.string.warm_prompt, R.string.nearby_people_no_card ,R.string.frg_text_cancel, R.string.frg_text_ok, dlgOnClick);
        }
        this.userNearbyNotCardDialog.show(getFragmentManager(), "userNearbyNotCardDialog");
    }

    /**
     * 请求附近的朋友
     */
    private void requestNearbyContact(){
        CardEntity cardInfo = CurrentUser.getInstance().getCurrentVCardEntity();
        long curMyCardId = 0;
        if(ResourceHelper.isNotNull(cardInfo)){
            curMyCardId = cardInfo.getId();
        }
        if(curMyCardId > 0){
            URLResultEntity mUrlEntity = CurrentUser.getInstance().getURLEntity();
            String url = ProjectHelper.fillSwapRequestURL(mUrlEntity.getNearbyPeopleList());
            UserNearbyPeopleJsonEntity nearbyPeople = new UserNearbyPeopleJsonEntity();
            nearbyPeople.setCardId(curMyCardId);
            String json = JsonHelper.toJson(nearbyPeople, UserNearbyPeopleJsonEntity.class);
            String mToken = CurrentUser.getInstance().getToken();
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_NEARBY_PEOPLE_LIST, url, mToken, json, this);
        }else{
            showDialogFragmentNotCard();
        }
    }

    /**
     * 请求关注
     * @param vcardNo
     */
    private void requestAddAttention(String vcardNo){
        AddAttentionJsonEntity mStatusEntity = new AddAttentionJsonEntity();
        mStatusEntity.setVcardNo(vcardNo);
        String json = JsonHelper.toJson(mStatusEntity, AddAttentionJsonEntity.class);
        URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
        String url = ProjectHelper.fillRequestURL(urlEntity.getAttentionAdd());
        String token = CurrentUser.getInstance().getToken();
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_ATTENTIONS_ADD, url, token , json, this);
    }

}
