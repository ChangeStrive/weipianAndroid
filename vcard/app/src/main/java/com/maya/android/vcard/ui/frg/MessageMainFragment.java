package com.maya.android.vcard.ui.frg;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.MessageNoticeDao;
import com.maya.android.vcard.dao.MessageSessionDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.MessageSessionEntity;
import com.maya.android.vcard.ui.act.MessageMainActivity;
import com.maya.android.vcard.ui.adapter.ConversationAdapter;
import com.maya.android.vcard.ui.adapter.CustomLsvAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 消息主页
 */
public class MessageMainFragment extends BaseFragment {
    private static final int FRIEND_INVITE_SIGN = 3000001;
    private static final int SYSTEM_NEWS_SIGN = 3000002;
    /** 未查看系统通知数量 **/
    private int noticeCount;
    /** 小红点 **/
    private ImageView mImvFriendRecommendSign, mImvSystemNewsSign;
    private ListView mLsvNews;
    private ConversationAdapter mConversationAdapter;
    private CustomDialogFragment mDialogFragmentConversation;
    private int mCurPosition;
    private MessageMainReceiver messageReceiver;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.rel_friend_recommend:
                    //好友推荐
                    if(CurrentUser.getInstance().isLogin()){
                        intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, MessageInviteFragment.class.getName());
                        mActivitySwitchTo.switchTo(MessageMainActivity.class, intent);
                    }else{
                        ActivityHelper.showToast(R.string.login_please);
                    }
                   break;
                case R.id.rel_system_news:
                    //系统消息
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, MessageNotifyFragment.class.getName());
                    if (noticeCount > 0) {
                        MessageNoticeDao.getInstance().updateReaded();
                    }
                    mActivitySwitchTo.switchTo(MessageMainActivity.class, intent);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_main, container, false);
        this.mLsvNews = (ListView) view.findViewById(R.id.lsv_news);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mLsvNews.addHeaderView(getInformView());
        this.mLsvNews.setHeaderDividersEnabled(false);
        this.mLsvNews.setOnItemLongClickListener(this.mOnItemLongClickListener);
        this.mLsvNews.setOnItemClickListener(this.mOnItemClickListener);
        this.mConversationAdapter = new ConversationAdapter(getActivity());
        this.mLsvNews.setAdapter(this.mConversationAdapter);
        ArrayList<MessageSessionEntity> sessionEntityList = MessageSessionDao.getInstance().getListForAll(false);
        this.mConversationAdapter.addItems(sessionEntityList);
        refreshFriendUI();
        refreshSystemUI();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerMainReceiver();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(ResourceHelper.isNotNull(this.messageReceiver)){
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
            broadcastManager.unregisterReceiver(this.messageReceiver);
        }
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MessageSessionEntity sessionItem = mConversationAdapter.getItem(position - 1);
            if(ResourceHelper.isNotNull(sessionItem)){
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, MessageConversationFragment.class.getName());
                bundle.putLong(Constants.IntentSet.INTENT_KEY_ACCOUNT_ID, sessionItem.getFromAccountId());
                bundle.putLong(Constants.IntentSet.INTENT_KEY_MESSAGE_TAG_ID, sessionItem.getTagId());
                intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                if (sessionItem.getUnreadCount() > 0) {
                    // 把 消息 置 已读标识 并 通知 导航栏消息数 刷新
                    MessageSessionDao.getInstance().updateReaded(sessionItem.getFromAccountId(),sessionItem.getTagId());
                    mConversationAdapter.setUnreadCount(position - 1);
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(Constants.ActionIntent.ACTION_INTENT_NAV_BOTTOM_MESSAGE_COUNT);
                    getActivity().sendBroadcast(broadcastIntent);
                }
                mActivitySwitchTo.switchTo(MessageMainActivity.class, intent);
            }
        }
    };

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            showDialogFragmentConversation(position - 1);
            return true;
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FRIEND_INVITE_SIGN:
                    refreshFriendUI();
                    break;
                case SYSTEM_NEWS_SIGN:
                    refreshSystemUI();
                    break;
            }
        }
    };

    /**
     * 刷新好友推荐小红点显示状态
     */
    private void refreshFriendUI(){
        boolean isShowFriendREcommendSign = PreferencesHelper.getInstance().getBoolean(Constants.Preferences.KEY_IS_RECOMMEN_NEW_SIGN_SHOW, true);
        if (isShowFriendREcommendSign) {
            mImvFriendRecommendSign.setVisibility(View.VISIBLE);
        } else {
            mImvFriendRecommendSign.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新系统通知小红点显示状态
     */
    private void refreshSystemUI(){
        this.noticeCount = MessageNoticeDao.getInstance().getUnReadCount();
        if(noticeCount > 0){
            mImvSystemNewsSign.setVisibility(View.VISIBLE);
        }else{
            mImvSystemNewsSign.setVisibility(View.GONE);
        }
    }
    /**
     * 会话列表长按弹出对话框
     * @param position
     */
    private void showDialogFragmentConversation(int position){
        this.mCurPosition = position;
        MessageSessionEntity news = this.mConversationAdapter.getItem(position);
        if(ResourceHelper.isNull(this.mDialogFragmentConversation)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {//置顶会话
                        mConversationAdapter.moveTopAndgetListShowName(mCurPosition);
                    } else if (position == 1) {//删除会话
                        mConversationAdapter.deleteItem(mConversationAdapter.getItem(mCurPosition));
                    }
                    mDialogFragmentConversation.getDialog().dismiss();
                }
            });
            CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mLsvAdapter);
            mLsvAdapter.setShowRadio(false);
            mLsvAdapter.addItems(DialogFragmentHelper.getListFromResArray(getActivity(), R.array.message_main_session));
            this.mDialogFragmentConversation = DialogFragmentHelper.showCustomDialogFragment(news.getListShowName(), mLsvItem);
        }
        this.mDialogFragmentConversation.setTitle(news.getListShowName());
        this.mDialogFragmentConversation.show(getFragmentManager(), "mDialogFragmentConversation");
    }

    /**
     * 收索栏、好友推荐、系统通知
     * @return
     */
    private View getInformView(){
        View informView = LayoutInflater.from(getActivity()).inflate(R.layout.item_news_serach_secommend, null);
        EditText mEdtSearchContent = (EditText) informView.findViewById(R.id.edt_enter_search_content);
        RelativeLayout mRelFriendRecommend = (RelativeLayout) informView.findViewById(R.id.rel_friend_recommend);
        RelativeLayout mRelSystemNews = (RelativeLayout) informView.findViewById(R.id.rel_system_news);
        this.mImvFriendRecommendSign = (ImageView) informView.findViewById(R.id.imv_friend_recommend);
        this.mImvSystemNewsSign = (ImageView) informView.findViewById(R.id.imv_system_news);
        mRelFriendRecommend.setOnClickListener(this.mOnClickListener);
        mRelSystemNews.setOnClickListener(this.mOnClickListener);
        mEdtSearchContent.addTextChangedListener(this.mTextWatcher);
        return informView;
    }

    /**
     * 输入监听
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
           int len = s.length();
           if(len > 13){
               ActivityHelper.showToast(R.string.letter_over_write);
                return;
           }
           ArrayList<MessageSessionEntity> sessionEntityList;
           if(len < 1){
               sessionEntityList =  MessageSessionDao.getInstance().getListForAll(false);
           }else{
               sessionEntityList =  MessageSessionDao.getInstance().getListForSearch(s.toString());
           }
           mConversationAdapter.addItems(sessionEntityList);
       }
   };

    /**
     * 请求广播接收
     */
    private void registerMainReceiver() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        this.messageReceiver = new MessageMainReceiver();
        IntentFilter filterSession = new IntentFilter();
        filterSession.addAction(Constants.ActionIntent.ACTION_INTENT_MESSAGE_MAIN);
        broadcastManager.registerReceiver(this.messageReceiver, filterSession);
    }

    /**
     * 会话广播接收
     */
    private class MessageMainReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isNotice = intent.getBooleanExtra(Constants.IntentSet.INTENT_KEY_IS_NOTICE, false);
            if (isNotice) {
                // 刷新通知
                Message msg = Message.obtain();
                msg.what = SYSTEM_NEWS_SIGN;
                mHandler.sendMessage(msg);
            } else {
                ArrayList<MessageSessionEntity> sessionEntityList = MessageSessionDao.getInstance().getListForAll(false);
                mConversationAdapter.addItems(sessionEntityList);
            }
        }
    }

}
