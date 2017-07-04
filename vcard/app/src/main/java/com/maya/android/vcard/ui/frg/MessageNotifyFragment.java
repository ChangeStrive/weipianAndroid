package com.maya.android.vcard.ui.frg;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.dao.ContactVCardDao;
import com.maya.android.vcard.dao.MessageNoticeDao;
import com.maya.android.vcard.dao.MessageSessionDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.ContactEntity;
import com.maya.android.vcard.entity.MessageNoticeEntity;
import com.maya.android.vcard.entity.UserAccountCommonEntity;
import com.maya.android.vcard.entity.json.SwapCardJsonEntity;
import com.maya.android.vcard.entity.json.UnitMemberJsonEntity;
import com.maya.android.vcard.entity.result.ContactsListResultEntity;
import com.maya.android.vcard.entity.result.MessageResultEntity;
import com.maya.android.vcard.entity.result.SwapCardCommitResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.adapter.MessageNotifyAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 消息：通知
 */
public class MessageNotifyFragment extends BaseFragment {
    /** true 全选  false 全部取消 **/
    private boolean isSelectedAllorCancel = true;
    private ListView mLsvNotify;
    private Button mBtnDel;
    private TextView mTxvEmpty;
    private MessageNotifyAdapter mNotifyAdapter;
    private RelativeLayout mRelDel;
    private URLResultEntity mUrlEntity = CurrentUser.getInstance().getURLEntity();
    private CardEntity curCard =  CurrentUser.getInstance().getCurrentVCardEntity();
    private UserInfoResultEntity userInfo = CurrentUser.getInstance().getUserInfoEntity();
    private MessageNotifyReceiver mNoticeReceiver;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_del:
                    //删除
                    if(ResourceHelper.isNotNull(mNotifyAdapter)){
                        ArrayList<MessageNoticeEntity> notices = mNotifyAdapter.getALlSelected();
                        for(int i = 0, size = notices.size(); i < size; i++){
                            MessageNoticeEntity notice = notices.get(i);
                            if(ResourceHelper.isNotNull(notice)){
                                MessageNoticeDao.getInstance().delete(notice.getId());
                            }
                        }
                        mNotifyAdapter.removeALlSelected();
                        sendRefreshMessageMain(false);
                    }
                 case R.id.txv_act_top_right:

                     if(isSelectedAllorCancel){
                         //全选
                         mTitleAction.setActivityTopRightTxv(R.string.frg_text_cancel, mOnClickListener);
                         mNotifyAdapter.selectAllorNot(true);
                     }else{
                         //取消
                         mTitleAction.setActivityTopRightTxv(R.string.select_all, mOnClickListener);
                         mNotifyAdapter.selectAllorNot(false);
                     }
                     isSelectedAllorCancel = !isSelectedAllorCancel;
                    break;
            }
         }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_SWAP_CARD_COMMIT:
                    //确认云端交换名片回调
                    SwapCardCommitResultEntity swaCard = JsonHelper.fromJson(commandResult, SwapCardCommitResultEntity.class);
                    if(ResourceHelper.isNotNull(swaCard)){
                        long cardId = Long.valueOf(objects[0].toString());
                        int count = swaCard.getUserCardCount();
                        CurrentUser.getInstance().updateVCardEntityListCount(cardId, count, true);
                        if(count > 0){
                            requestMyContacts();
                            if(objects.length == 3){
                                UserAccountCommonEntity userCommon = (UserAccountCommonEntity) objects[2];
                                if(Helper.isNotNull(userCommon)){
                                    // 如果还未有交换确认会话，则 先 插入一条 好友确认会话
                                    boolean isExistAccountMsg = MessageSessionDao.getInstance().isExistAccountMessage(userCommon.getAccountId());
                                    if(!isExistAccountMsg){
                                        // 插入会话聊天
                                        int contentType = Constants.MessageContentType.SESSION_TEXT;
                                        String body = ActivityHelper.getGlobalApplicationContext().getString(R.string.notice_request_swap_success);
                                        MessageSessionDao.getInstance().add(userCommon.getAccountId(), userCommon.getCardId(), userCommon.getHeadImg(),
                                                userCommon.getDisplayName(), 0, contentType, body, DatabaseConstant.MessageSendType.RECIVER);

                                        Intent broadcastIntent = new Intent();
                                        broadcastIntent.setAction(Constants.ActionIntent.ACTION_INTENT_MESSAGE_MAIN);
                                        getActivity().sendBroadcast(broadcastIntent);
                                    }
                                    // 推送交换结果
                                }
                            }else{
                                String message = swaCard.getMessage();
                                if(ResourceHelper.isNotEmpty(message)){
                                    ActivityHelper.showToast(message);
                                }
                            }
                        }
                    }
                    break;
                case Constants.CommandRequestTag.CMD_REQUEST_MY_CONTACT:
                    //请求联系人回调
                    ContactsListResultEntity resultEntity = JsonHelper.fromJson(commandResult, ContactsListResultEntity.class);
                    if(Helper.isNotNull(resultEntity)){
                        final ArrayList<ContactEntity> contactEntityList = resultEntity.getContactEntityList();
                        if(Helper.isNotNull(contactEntityList)){
                            try{
                                //TODO 空指针，得解决（造成原因： 用户经常却换账号登录）
                                ContactVCardDao.getInstance().add(contactEntityList);
                                //删除 不存在的联系人
                                ContactVCardDao.getInstance().deleteContactFromService(contactEntityList);
                                PreferencesHelper.getInstance().putBoolean(Constants.Preferences.KEY_IS_CONTACT_HAS_REFRESH, true);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onResponseSuccess(int tag, String msgInfo) {
        super.onResponseSuccess(tag, msgInfo);
        switch (tag){
            case Constants.CommandRequestTag.CMD_REQUEST_MESSAGE_NOTICE_SEND:
                // 云端交换请求、短信推荐
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_TRANSMIT_VCARD_ALLOW_RESULT:
                //发起请求交换回调
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_ADD:
                //确认添加成员回调
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_TRANSMIT_VCARD_ALLOW_REQUEST:
                //转发明片给中间人回调
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_notify, container, false);
        this.mLsvNotify = (ListView) view.findViewById(R.id.lsv_list);
        this.mTxvEmpty = (TextView) view.findViewById(R.id.txv_lsv_empty);
        this.mBtnDel = (Button) view.findViewById(R.id.btn_del);
        this.mRelDel = (RelativeLayout) view.findViewById(R.id.rel_del);
        this.mLsvNotify.setEmptyView(this.mTxvEmpty);
        this.mBtnDel.setOnClickListener(this.mOnClickListener);
        this.mLsvNotify.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.system_notice, false);
        super.mTitleAction.setActivityTopRightTxv(R.string.select_all, this.mOnClickListener);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        this.mNotifyAdapter = new MessageNotifyAdapter(getActivity());
        this.mLsvNotify.setAdapter(this.mNotifyAdapter);
        this.mNotifyAdapter.setNoticeItemOperateListener(this.mNoticeItemOperateListener);
        this.mNotifyAdapter.addItems(getArrayListNotices());
        this.mTxvEmpty.setText(R.string.frg_common_nothing_data);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestNotifyReceiver();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(ResourceHelper.isNotNull(this.mNoticeReceiver)){
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
            broadcastManager.unregisterReceiver(this.mNoticeReceiver);
        }
    }

    /**
     * 获取数据
     * @return
     */
    private ArrayList<MessageNoticeEntity> getArrayListNotices() {
        ArrayList<MessageNoticeEntity> notices = MessageNoticeDao.getInstance().getList();
        if(ResourceHelper.isNotNull(notices) && notices.size() > 0){
            super.mTitleAction.setActivityTopRightTxvVisibility(View.VISIBLE);
        }else{
            super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        }
        return notices;
    }

    /**
     * 适配器事件监听
     */
    private MessageNotifyAdapter.NoticeItemOperateListener mNoticeItemOperateListener = new MessageNotifyAdapter.NoticeItemOperateListener() {
        @Override
        public void onAgree(int position) {
            //同意
            if(!NetworkHelper.isNetworkAvailable(getActivity())){
                ActivityHelper.showToast(R.string.no_network);
                return;
            }
            MessageNoticeEntity notice = mNotifyAdapter.getItem(position);
            if(ResourceHelper.isNotNull(notice)){
                int contentType, result = Constants.MessageResultType.SUCCESS ;
                String body;
                MessageNoticeDao.getInstance().updateDealed(notice.getRead(), DatabaseConstant.NoticeDealStatus.DEAL_AGREE);
                int type = notice.getContentType();
                int requestTag = 0;
                mNotifyAdapter.notifyDataSetChanged();
                switch (type){
                    case Constants.MessageContentType.NOTICE_TRANSMIT_ALLOW_REQUEST:
                        // 同意转发则发起 交换请求
                        contentType = Constants.MessageContentType.NOTICE_TRANSMIT_SWAP_REQUEST;
                        body = getString(R.string.notice_from_friend_transmit);
                        requestTag = Constants.CommandRequestTag.CMD_REQUEST_TRANSMIT_VCARD_ALLOW_RESULT;
                        requestSendNotice(notice.getTagId(), notice.getFromAccountId(), result, body, contentType, requestTag);
                        // 反馈结果给发起人
                        contentType = Constants.MessageContentType.NOTICE_TRANSMIT_ALLOW_RESULT;
                        body = getString(R.string.notice_allow_transmit, userInfo.getDisplayName());
                        requestSendNotice(notice.getFromAccountId(), 0, result, body, contentType, requestTag);
                        break;
                    case Constants.MessageContentType.NOTICE_ENTERPRISE_ADD_REQUEST:
                        // 单位成员请求加入
                        requestAddUnitMember(notice);
                        break;
                    case Constants.MessageContentType.NOTICE_TRANSMIT_SWAP_REQUEST:
                        // 转发交换确认
                        requestConfirmExchangeVCard(notice.getFromCardId(), Constants.SwapCard.SWAP_CARD_WAY_TO_TRANSLATE);
                        // 转发名片结果发给中间人
                        body = getString(R.string.notice_already_receive_your_card, userInfo.getDisplayName());
                        requestTag = Constants.CommandRequestTag.CMD_REQUEST_TRANSMIT_VCARD_ALLOW_REQUEST;
                        requestSendNotice(notice.getTagId(), notice.getFromAccountId(), 0, body, Constants.MessageContentType.NOTICE_TRANSMIT_SWAP_RESULT_TO_REQUESTER, requestTag);
                        break;
                    case Constants.MessageContentType.NOTICE_CARD_SWAP_REQUEST:
                        // 云端交换
                        int swapWay = Constants.SwapCard.SWAP_CARD_WAY_TO_CLOUND;
                        if(notice.getBody().equals(getString(R.string.notice_from_radar_scan))){
                            swapWay = Constants.SwapCard.SWAP_CARD_WAY_TO_RADA;
                        }
                        requestConfirmExchangeVCard(notice.getFromCardId(), swapWay);
                        break;
                }
            }

        }

        @Override
        public void onRefuse(int position) {
            //拒绝
            if(!NetworkHelper.isNetworkAvailable(getActivity())){
                ActivityHelper.showToast(R.string.no_network);
                return;
            }
            MessageNoticeEntity notice = mNotifyAdapter.getItem(position);
            if(ResourceHelper.isNotNull(notice)){
                int sendContentType = 0;
                String body = "";
                int requestTag = 0;
                int result = Constants.MessageResultType.REJUECT;
                MessageNoticeDao.getInstance().updateDealed(notice.getRead(), DatabaseConstant.NoticeDealStatus.DEAL_REJECT);
                int type = notice.getContentType();
                switch (type){
                    case Constants.MessageContentType.NOTICE_TRANSMIT_SWAP_REQUEST:
                        //把结果反馈给发起转发请求的人
                        body =  getString(R.string.notice_already_reject_your_card, userInfo.getDisplayName());
                        sendContentType = Constants.MessageContentType.NOTICE_TRANSMIT_SWAP_RESULT_TO_REQUESTER;
                        requestSendNotice(notice.getTagId(), notice.getFromAccountId(), result, body, sendContentType , requestTag);
                        break;
                    case Constants.MessageContentType.NOTICE_CARD_SWAP_REQUEST:
                        // 云端交换请求
                        sendContentType = Constants.MessageContentType.NOTICE_CARD_SWAP_RESULT;
                        body = getString(R.string.notice_reject_swap_card, notice.getFromName());
                        requestTag = Constants.CommandRequestTag.CMD_REQUEST_MESSAGE_NOTICE_SEND;
                        requestSendNotice(notice.getFromAccountId(), 0, result, body, sendContentType , requestTag);
                        break;
                    case Constants.MessageContentType.NOTICE_SMS_RECOMMEND_SWAP_REQUEST:
                        // 短信推荐交换请求
                        sendContentType = Constants.MessageContentType.NOTICE_SMS_RECOMMEND_SWAP_RESULT;
                        body = getString(R.string.notice_reject_swap_card, notice.getFromName());
                        requestTag = Constants.CommandRequestTag.CMD_REQUEST_MESSAGE_NOTICE_SEND;
                        requestSendNotice(notice.getFromAccountId(), 0, result, body, sendContentType , requestTag);
                        break;
                    case Constants.MessageContentType.NOTICE_TRANSMIT_ALLOW_REQUEST:
                        // 转发请求
                        sendContentType = Constants.MessageContentType.NOTICE_TRANSMIT_ALLOW_RESULT;
                        body = getString(R.string.notice_reject_transmit, userInfo.getDisplayName());
                        requestTag = Constants.CommandRequestTag.CMD_REQUEST_TRANSMIT_VCARD_ALLOW_RESULT;
                        requestSendNotice(notice.getFromAccountId(), 0, result, body, sendContentType , requestTag);
                        break;
                }
            }
        }

        @Override
        public void onSelect(int selectCount) {
            //复选框 选择的数量
            if(selectCount > 0){
                mRelDel.setVisibility(View.VISIBLE);
            }else{
                mRelDel.setVisibility(View.GONE);
            }
        }
    };


    /**
     * 确认添加成员请求
     * @param noticeItem
     */
    private void requestAddUnitMember(MessageNoticeEntity noticeItem){
        if(ResourceHelper.isNotNull(noticeItem) && ResourceHelper.isNotNull(this.mUrlEntity)){
            String url = ProjectHelper.fillRequestURL(this.mUrlEntity.getEnterpriseMemberSave());
            String token = CurrentUser.getInstance().getToken();
            UnitMemberJsonEntity unitMember = new UnitMemberJsonEntity();
            unitMember.setAccountId(noticeItem.getFromAccountId());
            unitMember.setCardId(noticeItem.getFromCardId());
            unitMember.setEnterpriseId(noticeItem.getTagId());
            String json = JsonHelper.toJson(unitMember, UnitMemberJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_ADD, url, token, json, this);
        }
    }

    /**
     * 推送消息
     * @param toAccountId
     * @param tagId
     * @param result
     * @param body
     * @param contentType
     * @param requestTag
     */
    private void requestSendNotice(long toAccountId, long tagId, int result, String body, int contentType, int requestTag){
        MessageResultEntity msgEntity = new MessageResultEntity();
        msgEntity.setBody(body);
        msgEntity.setToAccountId(toAccountId);
        msgEntity.setContentType(contentType);
        msgEntity.setResult(result);
        msgEntity.setCreateTime(ResourceHelper.getNowTime());
        msgEntity.setLoseTime(0);
        msgEntity.setTagId(tagId);
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
        String msgUrl = ProjectHelper.fillRequestURL(this.mUrlEntity.getMsgPushSingle());
        String toJson = JsonHelper.toJson(msgEntity, MessageResultEntity.class);
        ReqAndRespCenter.getInstance().postForResult(requestTag, msgUrl, mAccessToken, toJson, this);
    }

    /**
     * 确认云端交换名片请求
     */
    private void requestConfirmExchangeVCard(long otherCardId, int swapWay){
        if(otherCardId <= 0){
            return;
        }
        if (ResourceHelper.isNotNull(this.mUrlEntity) ){
            String url = ProjectHelper.fillRequestURL(this.mUrlEntity.getCardSwapCommit());
            String accessToken = CurrentUser.getInstance().getToken();
            ArrayList<Long> cardIdList = new ArrayList<Long>();
            cardIdList.add(otherCardId);
            long myCardId = 0;
            if(ResourceHelper.isNotNull(this.curCard)){
                myCardId = this.curCard.getId();
            }
            SwapCardJsonEntity swapCard = new SwapCardJsonEntity();
            swapCard.setCardId(otherCardId);
            swapCard.setSwapWay(swapWay);
            swapCard.setCardId(myCardId);
            String toJson = JsonHelper.toJson(swapCard, SwapCardJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_SWAP_CARD_COMMIT, url, accessToken, toJson, this);
        }
    }

    /**
     * 请求我的联系人
     */
    public void requestMyContacts(){
        if(Helper.isNotNull(this.mUrlEntity)){
            String mAccessToken = CurrentUser.getInstance().getToken();
            String myContactUrl = ProjectHelper.fillRequestURL(this.mUrlEntity.getMyContact());
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_MY_CONTACT, myContactUrl, mAccessToken, new JSONObject(), this);
        }
    }

    /**
     * 刷新消息中心页
     */
    private void sendRefreshMessageMain(boolean isNotice){
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constants.IntentSet.INTENT_KEY_IS_NOTICE, isNotice);
        broadcastIntent.setAction(Constants.ActionIntent.ACTION_INTENT_MESSAGE_MAIN);
        broadcastManager.sendBroadcast(broadcastIntent);
    }


    /**
     * 请求接收广播
     */
    private void requestNotifyReceiver(){
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        this.mNoticeReceiver = new MessageNotifyReceiver();
        IntentFilter filterSession = new IntentFilter();
        filterSession.addAction(Constants.ActionIntent.ACTION_INTENT_NOTICE_LIST);
        broadcastManager.registerReceiver(mNoticeReceiver, filterSession);
    }

    /**
     * 通知广播接收
     */
    private class MessageNotifyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<MessageNoticeEntity> notices = MessageNoticeDao.getInstance().getList();
            if(ResourceHelper.isNotNull(notices) && notices.size() > 0){
                mTitleAction.setActivityTopRightTxvVisibility(View.VISIBLE);
            }else{
                mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
            }
            mNotifyAdapter.addItems(notices);
        }
    }
}
