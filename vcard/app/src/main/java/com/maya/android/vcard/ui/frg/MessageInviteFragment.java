package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.ContactBookDao;
import com.maya.android.vcard.dao.ContactVCardDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.ContactBookEntity;
import com.maya.android.vcard.entity.VCardAndBookEntity;
import com.maya.android.vcard.entity.json.MessInviteJsonEntity;
import com.maya.android.vcard.entity.json.ValidVCardUserListJsonEntity;
import com.maya.android.vcard.entity.result.MessInviteListResultEntity;
import com.maya.android.vcard.entity.result.MessInviteResultEntity;
import com.maya.android.vcard.entity.result.MessageResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.entity.result.VCardAndBookListResultEntity;
import com.maya.android.vcard.entity.result.VCardUserFromMobileResultEntity;
import com.maya.android.vcard.entity.result.ValidVCardUserListResultEntity;
import com.maya.android.vcard.ui.adapter.MessageInviteAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.PinnedSectionListView;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import net.tsz.afinal.core.AsyncTask;

import org.json.JSONObject;
import java.util.ArrayList;

/**
 * 消息：好友推荐
 */
public class MessageInviteFragment extends BaseFragment {
    private PinnedSectionListView mPsLsvInvite;
    private TextView mTxvEmPty;
    private MessageInviteAdapter mInviteAdapter;
    private static final String TAG_USER_FROM_VCARD = "vcard";
    private static final String TAG_USER_FROM_BOOK = "book";
    private URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
    private CardEntity curCard =  CurrentUser.getInstance().getCurrentVCardEntity();
    private UserInfoResultEntity userInfo = CurrentUser.getInstance().getUserInfoEntity();

    private DataTask dataTask, dataTaskNew;

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_VCARD_USERS_FROM_MOBILE:
                    //请求手机关联的微片用户回调
                    String strVCardAndBook = PreferencesHelper.getInstance().getString(Constants.Preferences.KEY_RECOMMEND_FRIEND_VCARD_USER_LIST, "");
                    String refreshVCardAndBook = commandResult.toString();
                    if(! strVCardAndBook.equals(refreshVCardAndBook)){
                        this.dataTaskNew = new DataTask();
                        this.dataTaskNew.execute(strVCardAndBook);
                        PreferencesHelper.getInstance().putString(Constants.Preferences.KEY_RECOMMEND_FRIEND_VCARD_USER_LIST, refreshVCardAndBook);
                    }
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
                    ActivityHelper.showToast(R.string.had_recommend_sms_send);
                    break;
                case Constants.CommandRequestTag.CMD_REQUEST_SWAP_CARD:
                    //TODO 请求云端交换回调
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onResponseFail(int tag, int responseCode, String msgInfo) {
        super.onResponseFail(tag, responseCode, msgInfo);
        switch (tag){
            case Constants.CommandRequestTag.CMD_REQUEST_VCARD_USERS_FROM_MOBILE:
                super.mFragmentInteractionImpl.onActivityBackPressed();
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_RECOMMOND_SMS_URL:
                //TODO 短信推荐请求失败
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_SWAP_CARD:
                //TODO 云端交换请求失败
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_invite, container, false);
        this.mPsLsvInvite = (PinnedSectionListView) view.findViewById(R.id.lsv_msg_invite_list);
        this.mTxvEmPty = (TextView) view.findViewById(R.id.txv_lsv_empty);
        this.mPsLsvInvite.setEmptyView(this.mTxvEmPty);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.setActivityTitle(R.string.friend_recommend, false);
        this.mInviteAdapter = new MessageInviteAdapter(getActivity());
        this.mPsLsvInvite.setAdapter(this.mInviteAdapter);
        this.mInviteAdapter.setMsgInviteItemOperateListener(this.mItemOperateListener);
        String strVCardAndBook = PreferencesHelper.getInstance().getString(Constants.Preferences.KEY_RECOMMEND_FRIEND_VCARD_USER_LIST, "");
        if(ResourceHelper.isNotEmpty(strVCardAndBook)){
            this.dataTask = new DataTask();
            this.dataTask.execute(strVCardAndBook);
        }
        requestValidVcardUsers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(ResourceHelper.isNotNull(this.dataTask) && this.dataTask.getStatus() == AsyncTask.Status.RUNNING){
            this.dataTask.cancel(true);
        }

        if(ResourceHelper.isNotNull(this.dataTaskNew) && this.dataTaskNew.getStatus() == AsyncTask.Status.RUNNING){
            this.dataTaskNew.cancel(true);
        }

    }

    private MessageInviteAdapter.MsgInviteItemOperateListener mItemOperateListener = new MessageInviteAdapter.MsgInviteItemOperateListener() {
        @Override
        public void onRecommend(int position) {
            VCardAndBookEntity vCardAndBook = mInviteAdapter.getItem(position);
            // 发送短信推荐请求
            if(ResourceHelper.isNotNull(vCardAndBook)){
                String mobile = ContactBookDao.getInstance().getMobile(vCardAndBook.getContactId());
                if(ResourceHelper.isEmpty(mobile)){
                    return;
                }
                requestMessInvite(mobile);
                PreferencesHelper.getInstance().putInt(Constants.Preferences.KEY_RECOMMEND_FRIEND_BOOK_CONTACT_ID + vCardAndBook.getContactId() + CurrentUser.getInstance().getVCardNo(), 0);
                mInviteAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onVCardSwap(int position) {
            if(CurrentUser.getInstance().isLogin()){
                // 0-发送请求 等待状态
                VCardAndBookEntity vCardAndBook = mInviteAdapter.getItem(position);
                // 发送短信推荐请求
                if(ResourceHelper.isNotNull(vCardAndBook)){
                    PreferencesHelper.getInstance().putInt(Constants.Preferences.KEY_RECOMMEND_FRIEND_VCARD_USER_ID + vCardAndBook.getAccountId() + CurrentUser.getInstance().getVCardNo(), 0);
                    requestExchangeVcard(vCardAndBook.getAccountId());
                    mInviteAdapter.notifyDataSetChanged();
                }
            }else{
                ActivityHelper.showToast(R.string.please_login_first);
            }
        }
    };

    /**
     * 获取本地数据
     * @return
     */
    private ArrayList<VCardAndBookEntity> getVCardAndBooks(String strVCardAndBook){
        return JsonHelper.fromJson(strVCardAndBook, VCardAndBookListResultEntity.class).getvCardAndBook();
//        return JsonHelper.fromJson(strVCardAndBook, new TypeToken<ArrayList<VCardAndBookEntity>>(){}.getType());
    }

    /**
     * 这个主要是将2种不同格式的ArrayList存放在同一个ArrayList中
     */
    private ArrayList<VCardAndBookEntity> loadVCardAndBook(String commandResult) {
        ArrayList<VCardAndBookEntity> mLoadingData = new ArrayList<VCardAndBookEntity>();
        ValidVCardUserListResultEntity validVCardUserListResultEntity = JsonHelper.fromJson(commandResult, ValidVCardUserListResultEntity.class);
        if(ResourceHelper.isNotNull(validVCardUserListResultEntity)){
            ArrayList<VCardUserFromMobileResultEntity> mValidVCardUserList = validVCardUserListResultEntity.getValidVcardUserList();
            if(ResourceHelper.isNotNull(mValidVCardUserList)){
                //微片用户转化VCardUsers数据
                int mcard = mValidVCardUserList.size();
                if(mcard > 0){
                    ArrayList<VCardAndBookEntity> mListUserFromVcardTotal = new ArrayList<VCardAndBookEntity>();
                    for (int i = 0; i < mcard; i++) {
                        long curAccountId = 0;
                        VCardUserFromMobileResultEntity vcardUser = mValidVCardUserList.get(i);
                        if(ResourceHelper.isNotNull(vcardUser)){
                            curAccountId = vcardUser.getAccountId();
                        }
                        long curUserAccountId = 0;
                        if(ResourceHelper.isNotNull(this.userInfo)){
                            curUserAccountId = this.userInfo.getId();
                        }

                        // 排除自己 和 已交换过的微片用户
                        if(ContactVCardDao.getInstance().isExistByAccount(curAccountId) || curAccountId == curUserAccountId){
                            continue;
                        }else{
                            VCardAndBookEntity item = new VCardAndBookEntity(VCardAndBookEntity.ITEM, TAG_USER_FROM_VCARD);
                            item.accountId = curAccountId;
                            item.auth = vcardUser.getAuth();
                            item.displayName = vcardUser.getDisplayName();
                            item.bindWay = vcardUser.getBindWay();
                            item.headImg = vcardUser.getHeadImg();
                            item.mobile = vcardUser.getMobile();
                            item.VcardNo = vcardUser.getVcardNo();
                            item.Flag = TAG_USER_FROM_VCARD;
                            mListUserFromVcardTotal.add(item);
                        }
                    }
                    if(mListUserFromVcardTotal.size() > 1){
                        ArrayList<VCardAndBookEntity> mListUserFromVcard = new ArrayList<VCardAndBookEntity>();
                        VCardAndBookEntity sb = new VCardAndBookEntity(VCardAndBookEntity.SECTION, getString(R.string.friend_recommend_label_num, mListUserFromVcardTotal.size()));
                        mListUserFromVcard.add(sb);
                        mListUserFromVcard.addAll(mListUserFromVcardTotal);
                        mLoadingData.addAll(mListUserFromVcard);
                    }

                }

                ArrayList<ContactBookEntity> contactBooks = ContactBookDao.getInstance().getListForRecommend(mValidVCardUserList);
                if(ResourceHelper.isNotNull(contactBooks)){
                    //通讯录转化VCardInBook数据
                    int book = contactBooks.size();
                    if(book > 0){
                        ArrayList<VCardAndBookEntity> mListUserFromBookTotal = new ArrayList<VCardAndBookEntity>();
                        for (int i = 0; i < book; i++) {
                            ContactBookEntity curBook = contactBooks.get(i);
                            VCardAndBookEntity item = new VCardAndBookEntity(VCardAndBookEntity.ITEM, TAG_USER_FROM_BOOK);
                            item.contactId = curBook.getContactId();
                            item.contactRawId = curBook.getContactRawId();
                            item.displayName = curBook.getDisplayName();
                            item.hasPhone = curBook.getHasPhone();
                            item.headData = curBook.getHeadData();
                            item.headPhoto = curBook.getHeadPhoto();
                            item.mobile = curBook.getMobile();
                            item.nameSpell = curBook.getNameSpell();
                            item.sortKey = curBook.getSortKey();
                            item.isVcardUser = curBook.isVcardUser();
                            item.Flag = TAG_USER_FROM_BOOK;
                            mListUserFromBookTotal.add(item);
                        }
                        if(mListUserFromBookTotal.size() > 1){
                            ArrayList<VCardAndBookEntity> mListUserFromBook = new ArrayList<VCardAndBookEntity>();
                            VCardAndBookEntity vb = new VCardAndBookEntity(VCardAndBookEntity.SECTION, getResources().getString(R.string.friend_recommend_label_request));
                            mListUserFromBook.add(vb);
                            mListUserFromBook.addAll(mListUserFromBookTotal);
                            mLoadingData.addAll(mListUserFromBook);
                        }
                    }
                }
            }
        }
        return mLoadingData;
    }

    class DataTask extends AsyncTask<String, Void , ArrayList<VCardAndBookEntity>>{

        @Override
        protected ArrayList<VCardAndBookEntity> doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
            return loadVCardAndBook(params[0]);
        }

         @Override
        protected void onPostExecute(ArrayList<VCardAndBookEntity> vCardAndBookEntities) {
            super.onPostExecute(vCardAndBookEntities);
            mInviteAdapter.addItems(vCardAndBookEntities);
        }

    }

    /**
     * 请求手机关联的微片用户
     */
    private void requestValidVcardUsers(){
        if(Helper.isNotNull(this.urlEntity)){
            ArrayList<String> mobileList = ContactBookDao.getInstance().getMobileList();
            if(ResourceHelper.isNotNull(mobileList)){
                ValidVCardUserListJsonEntity jsonEntity = new ValidVCardUserListJsonEntity(mobileList);
                String json = JsonHelper.toJson(jsonEntity, ValidVCardUserListJsonEntity.class);
                String url = ProjectHelper.fillRequestURL(this.urlEntity.getContactsValid());
                String token = CurrentUser.getInstance().getToken();
                ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_VCARD_USERS_FROM_MOBILE, url, token, json, this);
            }
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

    /**
     * 请求云端交换名片
     */
    private void requestExchangeVcard(long toAccountId){
        if(ResourceHelper.isNotNull(this.urlEntity)){
            String body = getString(R.string.notice_from_friend_invite);
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
}
