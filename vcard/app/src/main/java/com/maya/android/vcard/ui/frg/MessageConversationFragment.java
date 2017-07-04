package com.maya.android.vcard.ui.frg;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.dao.CircleDao;
import com.maya.android.vcard.dao.ContactVCardDao;
import com.maya.android.vcard.dao.MessageSessionDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.ContactListItemEntity;
import com.maya.android.vcard.entity.MessageSessionEntity;
import com.maya.android.vcard.entity.TerminalAgentEntity;
import com.maya.android.vcard.entity.json.CircleGroupIdJsonEntity;
import com.maya.android.vcard.entity.result.CircleGroupResultEntity;
import com.maya.android.vcard.entity.result.MessageResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.act.CardcaseMainActivity;
import com.maya.android.vcard.ui.adapter.CustomLsvAdapter;
import com.maya.android.vcard.ui.adapter.CustomLsvIntegerAdapter;
import com.maya.android.vcard.ui.adapter.MsgConversationAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CartoonFaceView;
import com.maya.android.vcard.ui.widget.ChatAddItemView;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.UserHeadMaxDialogFragment;
import com.maya.android.vcard.util.AudioRecordHelper;
import com.maya.android.vcard.util.CustomLsvContentHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ExpressionHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * 消息：会话
 */
public class MessageConversationFragment extends BaseFragment {
    // 录制最小时间 1秒
    private static final long RECORD_MIN_TIME = 1 * 1000;
    private ImageButton mImbFace, mImbMore, mImbMoreToo, mImbKeyboard, mImbVoice, mImbSend;
    private EditText mEdtContent;
    private TextView mTxvSpeek;
    private ListView mLsvMsgConversation;
    private TextView mTxvEmpty;
    private MsgConversationAdapter msgConversationAdapter;
    private RelativeLayout mRelVoice, mRelKeyboard;
    private LinearLayout mLilEnter;
    private ArrayList<MessageSessionEntity> mSessionList;
    private MessageSessionDao mSessionDao = MessageSessionDao.getInstance();
    /** 消息类型 Key **/
    public static final String KEY_MESS_CONVERSATION_TYPE = "key_mess_conversation_type";
    /** 会话类型类型 **/
    private int mIntentCode;
    /** 群聊组类 **/
    private CircleGroupResultEntity mCircleGroupEntity;
    /** 推送消息中的tagId **/
    private long mTagId;
    /** 账户Id **/
    private long  mAccountId;
    /** 转发消息Id **/
    private long mDefaultSessionId;
    /** 群组类型 **/
    private int mCircleGroupType = DatabaseConstant.CircleGroupType.GROUP;
    /** 标题 **/
    private String mTitle;
    /** 当前选中的item项 **/
    private int curPosition;
    /** 当前是否处于无网络状态 **/
    private boolean isNotNetWork = false;

    /** 日期时间对象 **/
    private Calendar mCalendar;
    // 时间计时
    private Handler mStepHandler = null;
    private Runnable mTicker = null;
    // 当前时长
    private long mCurTime = 0;
    private String mTimeFormat = "mm:ss";
    private long mStartTime = 0;
    private View mVwPopVoice,mVwRoot;
    // 语音弹出框 对象
    private PopupWindow mPopVoice;
    private TextView mTxvPopVoiceTime,mTxvPopVoicePrompt;
    private ImageView mImvPopVoiceMic,mImvPopVoiceDel;
    private String mVoicePath;
   /**表情**/
    private CartoonFaceView mFaceView;
    /***插入项**/
    private ChatAddItemView mChatAddView;
    /** 弹窗 **/
    private CustomDialogFragment mDialogFragmentQuickNews, mDialogFragmentBlessPhrase, mDialogFragmentCallPhone, mDialogFragmentItemLong;
    /** 放大窗口 **/
    private UserHeadMaxDialogFragment mDialogFragmentHeadMax;
    /** 拨号适配器 **/
    private CustomLsvAdapter mLsvCanllAdapter;
    /** 内容长按弹窗适配器 **/
    private CustomLsvIntegerAdapter mLsvLongAdapter;
    /** 广播接收 **/
    private MessageConversationReceiver messageConversationReceiver;
    private URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
    private CardEntity curCard =  CurrentUser.getInstance().getCurrentVCardEntity();
    private UserInfoResultEntity userInfo = CurrentUser.getInstance().getUserInfoEntity();

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imb_face:
                    //表情
                    ResourceHelper.hideInputMethod(getActivity());
                    mChatAddView.removeChatAdd(mLilEnter);
                    mFaceView.showFaceView(mLilEnter);
                    break;
                case R.id.imb_more:
                    //输入框 - 更多
                    ResourceHelper.hideInputMethod(getActivity());
                    mFaceView.removeFaceView(mLilEnter);
                    mChatAddView.showChatAdd(mLilEnter);
                    break;
                case R.id.imb_more_too:
                    // 按住说话 - 更多
                    ResourceHelper.hideInputMethod(getActivity());
                    mFaceView.removeFaceView(mLilEnter);
                    mChatAddView.showChatAdd(mLilEnter);
                    break;
                case R.id.imb_keyboard:
                    //键盘
                    mChatAddView.removeChatAdd(mLilEnter);
                    mRelKeyboard.setVisibility(View.VISIBLE);
                    mRelVoice.setVisibility(View.GONE);
                    break;
                case R.id.imb_voice:
                    //语音
                    ResourceHelper.hideInputMethod(getActivity());
                    mFaceView.removeFaceView(mLilEnter);
                    mChatAddView.removeChatAdd(mLilEnter);
                    mRelKeyboard.setVisibility(View.GONE);
                    mRelVoice.setVisibility(View.VISIBLE);
                    break;
                case R.id.imb_send:
                    String content = mEdtContent.getText().toString();
                    boolean isFile = false;
                    if(mIntentCode == Constants.IntentSet.INTENT_CODE_MESSAGE_CHAT_TRANSMIT){
                        //判断是否是img audio video 打头
                        if(content.startsWith("img") || content.startsWith("audio") || content.startsWith("video")){
                            isFile = true;
                        }
                    }
                    if(!CurrentUser.getInstance().isLogin()){
                        ActivityHelper.showToast(R.string.login_please);
                        return;
                    }
                    if(!NetworkHelper.isNetworkAvailable(getActivity())){
                        // 网络不给力
                        ActivityHelper.showToast(R.string.no_network);
                        return;
                    }

                    int contentType = Constants.MessageContentType.SESSION_TEXT;
                    if(mTagId > 0){
                        if(isFile){
                            contentType = Constants.MessageContentType.SESSION_GROUP_FILE;
                        }else{
                            contentType = Constants.MessageContentType.SESSION_GROUP_TEXT;
                        }
                    }else{
                        if(isFile){
                            contentType = Constants.MessageContentType.SESSION_FILE;
                        }
                    }
                    // 发送消息
                    requestSendMess(content, contentType, Constants.MessageResultType.SUCCESS);
                    break;
                case R.id.edt_enter_msg:
                    //输入信息
                    mFaceView.removeFaceView(mLilEnter);
                    mChatAddView.removeChatAdd(mLilEnter);
                    break;
                case R.id.imv_act_top_right:
                    if(mTagId > 0){//群聊
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constants.IntentSet.INTENT_KEY_MESSAGE_TAG_ID, mTagId);
                        if(ResourceHelper.isNotNull(mCircleGroupEntity)){
                            bundle.putInt(Constants.IntentSet.INTENT_KEY_CIRCLE_GROUP_TYPE, mCircleGroupEntity.getType());
                        }
                        mFragmentInteractionImpl.onFragmentInteraction(MessageGroupSettingFragment.class.getName(), bundle);
                    }else{//拨号
                        ArrayList<String> phoneList = ContactVCardDao.getInstance().getPhoneList(mAccountId);
                        if(Helper.isNotNull(phoneList)){
                            int size = phoneList.size();
                            if(size == 1){
                                ResourceHelper.callPhone(getActivity(), phoneList.get(0));
                            }else if(size > 1){
                                showDialogFragmentCallPhone(phoneList);
                            }
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 会话监听
     */
    private MsgConversationAdapter.ChatContentClickListener mChatContentClickListener = new MsgConversationAdapter.ChatContentClickListener() {
        @Override
        public void onLongClick(int position) {
            //内容长按操作
            showDialogFragmentItemLong(position);
        }

        @Override
        public void onClickZoomImg(String url) {
            //单击图片放大
            showDialogFragmentHeadMax(url);
        }

        @Override
        public void onClickPlayAudio(String url, final AnimationDrawable animDrawable) {
            //单击播放语音
            String fullUrl = ResourceHelper.getHttpImageFullUrl(url);
            AudioRecordHelper.setMediaPlayListener(new AudioRecordHelper.MediaPlayListener() {

                @Override
                public void onCompletion() {
                    animDrawable.stop();
                    animDrawable.selectDrawable(0);
                }
            });
            AudioRecordHelper.startMediaPlay(fullUrl);
        }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_SELECT:
                    if(Helper.isNotEmpty(commandResult)){
                        this.mCircleGroupEntity = JsonHelper.fromJson(commandResult, CircleGroupResultEntity.class);
                        this.mCircleGroupEntity.setType(this.mCircleGroupType);
                        CircleDao.getInstance().add(this.mCircleGroupEntity);
                        if(this.mCircleGroupType == DatabaseConstant.CircleGroupType.CIRCLE_COMPANY){
                            MessageSessionDao.getInstance().add(mTagId, Constants.MessageContentType.SESSION_GROUP_TEXT, this.mCircleGroupEntity.getGroupName(), 1);
                        }
                        refreshData();
                    }
                    break;
                case Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_MESSAGE_SESSION_AUDIO:
                case Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_MESSAGE_SESSION_FILE:
                case Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_MESSAGE_SESSION_IMAGE:
                case Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_MESSAGE_SESSION_VCARD:
                case Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_MESSAGE_SESSION_VIDEO:
                    try {
                        JSONArray urlArr = commandResult.optJSONArray("fileUrl");
                        int retCount = urlArr.length();
                        if(retCount > 0){
                            // 获取服务端返回的 路径
                            sendSessionFile(tag, urlArr.getString(0));
                        }
                    }catch (JSONException e) {
                        ActivityHelper.showToast(R.string.file_overflow);
                        e.printStackTrace();
                    }
                    ActivityHelper.closeProgressDialog();
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            String path = "";
            int tag = 0 ;
            String lastDir = "";
            if(ResourceHelper.isNotNull(data)){
                switch (requestCode){
                    case Constants.RequestCode.REQUEST_CODE_ADD_IMAGE:
                        //图片
                        path = String.valueOf(data.getData()).substring(7);
                        lastDir = Constants.MessageFiles.FOLDER_OTHER;
                        tag = Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_MESSAGE_SESSION_FILE;
                        break;
                    case Constants.RequestCode.REQUEST_CODE_ADD_VIDEO:
                        //视频
                        lastDir = Constants.MessageFiles.FOLDER_IMAGE;
                        tag = Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_MESSAGE_SESSION_IMAGE;
                        path = ResourceHelper.getChoosePhotoPath(data, getActivity());
                        break;
                    case Constants.RequestCode.REQUEST_CODE_ADD_FILE:
                        //文件
                        lastDir = Constants.MessageFiles.FOLDER_VIDEO;
                        tag = Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_MESSAGE_SESSION_VIDEO;
                        try {
                            Cursor cursorVideo = getActivity().getContentResolver().query(data.getData(),
                                    new String[]{MediaStore.Video.Media.DATA},                 // Which columns to return
                                    null,       // WHERE clause; which rows to return (all rows)
                                    null,       // WHERE clause selection arguments (none)
                                    null);                 // Order-by clause (ascending by name)

                            cursorVideo.moveToFirst();
                            path = cursorVideo.getString(0);
                            cursorVideo.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                uploadFile(tag, path, lastDir);
            }

        }
    }

    @Override
    protected void onBackPressed() {
        super.onBackPressed();
        sendBroadMessageMain();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_conversation, container, false);
        this.mImbFace = (ImageButton) view.findViewById(R.id.imb_face);
        this.mImbMore = (ImageButton) view.findViewById(R.id.imb_more);
        this.mImbMoreToo = (ImageButton) view.findViewById(R.id.imb_more_too);
        this.mImbKeyboard = (ImageButton) view.findViewById(R.id.imb_keyboard);
        this.mImbVoice = (ImageButton) view.findViewById(R.id.imb_voice);
        this.mImbSend = (ImageButton) view.findViewById(R.id.imb_send);
        this.mEdtContent = (EditText) view.findViewById(R.id.edt_enter_msg);
        this.mTxvSpeek = (TextView) view.findViewById(R.id.txv_press_speak);
        this.mRelVoice = (RelativeLayout) view.findViewById(R.id.rel_voice);
        this.mRelKeyboard = (RelativeLayout) view.findViewById(R.id.rel_keyboard);
        this.mLsvMsgConversation = (ListView) view.findViewById(R.id.lsv_chat);
        this.mTxvEmpty = (TextView) view.findViewById(R.id.txv_empty);
        this.mLilEnter = (LinearLayout) view.findViewById(R.id.lil_bottom);
        this.mLsvMsgConversation.setEmptyView(this.mTxvEmpty);
        this.mImbFace.setOnClickListener(this.mOnClickListener);
        this.mImbMore.setOnClickListener(this.mOnClickListener);
        this.mImbMoreToo.setOnClickListener(this.mOnClickListener);
        this.mImbKeyboard.setOnClickListener(this.mOnClickListener);
        this.mEdtContent.setOnClickListener(this.mOnClickListener);
        this.mImbSend.setOnClickListener(this.mOnClickListener);
        this.mImbVoice.setOnClickListener(this.mOnClickListener);
        this.mEdtContent.addTextChangedListener(this.mTextWatcher);
        this.mTxvSpeek.setLongClickable(true);
        this.mTxvSpeek.setOnTouchListener(this.mOnTouchListener);
        this.mEdtContent.setOnFocusChangeListener(this.mOnFocusChangeListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.mVwRoot = view;
        this.msgConversationAdapter = new MsgConversationAdapter(getActivity());
        this.mLsvMsgConversation.setAdapter(this.msgConversationAdapter);
        this.msgConversationAdapter.setChatContentClickListener(this.mChatContentClickListener);
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
            this.mIntentCode = bundle.getInt(KEY_MESS_CONVERSATION_TYPE, 0);
            this.mAccountId = bundle.getLong(Constants.IntentSet.INTENT_KEY_ACCOUNT_ID, 0);
            this.mTagId = bundle.getLong(Constants.IntentSet.INTENT_KEY_MESSAGE_TAG_ID, 0);
            this.mCircleGroupType = bundle.getInt(Constants.IntentSet.INTENT_KEY_CIRCLE_GROUP_TYPE, DatabaseConstant.CircleGroupType.GROUP);
            this.mDefaultSessionId = bundle.getLong(Constants.IntentSet.INTENT_KEY_MESSAGE_SESSION_ID, 0L);
            this.mTitle = bundle.getString(Constants.IntentSet.INTENT_KEY_TITLE_NAME);
            //当前网络状态
            this.isNotNetWork = bundle.getBoolean(Constants.IntentSet.INTENT_KEY_IS_FROM_NETWORK_FAIL, false);
        }
        refreshData();
        if(ResourceHelper.isNotNull(this.mSessionList)){
            this.msgConversationAdapter.addItems(this.mSessionList);
        }
        this.mHandler.sendMessage(Message.obtain());
        this.mFaceView = new CartoonFaceView(getActivity());
        this.mFaceView.setOnItemCartoonFaceViewListener(this.mOnItemCartoonFaceViewListener);
        this.mChatAddView = new ChatAddItemView(getActivity());
        this.mChatAddView.setOnItemChatAddItemViewListener(this.mOnItemChatAddItemViewListener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerChatReceiver();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AudioRecordHelper.releaseMediaRecorder();
        AudioRecordHelper.releaseMediaPlay(true);
        if(ResourceHelper.isNotNull(this.messageConversationReceiver)){
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
            broadcastManager.unregisterReceiver(this.messageConversationReceiver);
        }
    }

    /**
     * 信息输入框焦点监听
     */
    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                mFaceView.removeFaceView(mLilEnter);
                mChatAddView.removeChatAdd(mLilEnter);
            }
        }
    };

    /**
     * 刷新数据源
     */
    private void refreshData(){
        int mImgRightResId = 0;
        if(this.mTagId > 0 ){
            mImgRightResId = R.mipmap.img_top_more;
            this.mTitle = CircleDao.getInstance().getShowName(this.mTagId, true);
            this.mCircleGroupEntity = CircleDao.getInstance().getEntity(this.mTagId);
            if(ResourceHelper.isNull(this.mCircleGroupEntity)){
                requestSelectCircleGroup(mTagId);
            }
            this.mSessionList = this.mSessionDao.getListByGroup(this.mTagId);
        }else{
            mImgRightResId = R.mipmap.img_msg_conversation_telephone;
            if(ResourceHelper.isNull(this.mTitle)) {
                this.mTitle = MessageSessionDao.getInstance().getSessionShowTitle(this.mAccountId);
            }
            this.mSessionList = this.mSessionDao.getListByAccount(this.mAccountId);
        }
        // 转发消息
        if(this.mDefaultSessionId > 0){
            MessageSessionEntity defSession = MessageSessionDao.getInstance().getEntity(this.mDefaultSessionId);
            if(ResourceHelper.isNotNull(defSession)){
                requestSendMess(defSession.getBody(), defSession.getContentType(), Constants.MessageResultType.SUCCESS);
            }
        }
        if(ResourceHelper.isNotNull(this.mTitle) && this.mTitle.contains("null")){
            this.mTitle = getString(R.string.group_chat);
        }
        super.mTitleAction.setActivityTitle(this.mTitle, false);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImv(mImgRightResId, this.mOnClickListener);
    }

    /**
     *  请求查询单个组消息
     * @param groupId
     */
    private void requestSelectCircleGroup(long groupId){
        String questUrl = ProjectHelper.fillRequestURL(this.urlEntity.getGroupInfo());
        String mToken = CurrentUser.getInstance().getToken();
        CircleGroupIdJsonEntity circleGroupId = new CircleGroupIdJsonEntity();
        circleGroupId.setGroupId(groupId);
        String json = JsonHelper.toJson(circleGroupId, CircleGroupIdJsonEntity.class);
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_SELECT, questUrl, mToken, json, this);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mLsvMsgConversation.setSelection(msgConversationAdapter.getCount() -1);//刷新到底部
        }
    };

    /**
     * 按住说话
     */
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(!CurrentUser.getInstance().isLogin()){
                ActivityHelper.showToast(R.string.login_please);
                return true;
            }else{
                if(!NetworkHelper.isNetworkAvailable(getActivity())){
                    ActivityHelper.showToast(R.string.send_voice_without_network);
                    return true;
                }
            }
            float x = 0, y = 0;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    showPopVoice();
                    mStartTime = System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_UP:
                    ActivityHelper.showProgressDialog(getActivity(), R.string.common_loading_data);
                    x = event.getRawX();
                    y = event.getRawY();
                    dismissVoicePop();
                    if(isTouchVoiceSign(x, y) || (System.currentTimeMillis() - mStartTime) < RECORD_MIN_TIME){
                        //删除语音文件
                        File file = new File(mVoicePath);
                        if(Helper.isNotNull(file)){
                            file.deleteOnExit();
                        }
                        //复位图标
                        switchVoiceDel(false);
                        ActivityHelper.closeProgressDialog();
                        ActivityHelper.showToast(R.string.audio_time_short);
                    }else{
                        //停止录音
                        AudioRecordHelper.releaseMediaRecorder();
                        uploadFile(Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_MESSAGE_SESSION_AUDIO, mVoicePath,Constants.MessageFiles.FOLDER_AUDIO);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    x = event.getRawX();
                    y = event.getRawY();
                    boolean isDel = isTouchVoiceSign(x, y);
                    switchVoiceDel(isDel);
                    break;
            }
            return false;
        }
    };

    /**
     * 显示录音过程
     */
    private void showPopVoice(){
        // 语音栏按下效果
        Drawable txvDrw = getResources().getDrawable(R.mipmap.img_audio_white);
        setViewPress(this.mTxvSpeek, true);
        if (ResourceHelper.isNull(this.mPopVoice)) {
            int width = ResourceHelper.getDp2PxFromResouce( R.dimen.dimen_148dp);
            this.mVwPopVoice = LayoutInflater.from(getActivity()).inflate(R.layout.item_chat_voice, null);
            this.mPopVoice = new PopupWindow(this.mVwPopVoice, width, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.mTxvPopVoiceTime = (TextView) this.mVwPopVoice.findViewById(R.id.txv_pop_voice_time);
            this.mTxvPopVoicePrompt = (TextView) this.mVwPopVoice.findViewById(R.id.txv_pop_voice_prompt);
            this.mImvPopVoiceMic = (ImageView) this.mVwPopVoice.findViewById(R.id.imv_pop_voice_mic);
            this.mImvPopVoiceDel = (ImageView) this.mVwPopVoice.findViewById(R.id.imv_pop_voice_del);
            this.mPopVoice.setBackgroundDrawable(new BitmapDrawable(getResources()));
            this. mPopVoice.setOutsideTouchable(true);
        }
        //设置 语音文件路径
        this.mVoicePath = ResourceHelper.getMessageLocalFilePath(Constants.MessageFiles.FOLDER_AUDIO, System.currentTimeMillis() + Constants.MessageFiles.FILE_EXT_AUDIO);
        Log.d("录音路径", mVoicePath);
        if(ResourceHelper.isNotEmpty(this.mVoicePath)){
            this.mPopVoice.showAtLocation(this.mVwRoot, Gravity.CENTER, 0, 0);
            // 开始录音
            setKeepTime(true);
        }
    }

    /**
     * 关闭语音说话弹出框
     */
    private void dismissVoicePop(){
        if(Helper.isNotNull(this.mPopVoice) && this.mPopVoice.isShowing()){
            this.mPopVoice.dismiss();
            setViewPress(this.mTxvSpeek, false);
        }
    }

    /**
     * 删除 语音
     * @param isDel
     */
    private void switchVoiceDel(boolean isDel){
        if(isDel){
            this.mTxvPopVoicePrompt.setText(R.string.pop_voice_delete_send);
            this.mImvPopVoiceDel.setVisibility(View.VISIBLE);
            this.mImvPopVoiceMic.setVisibility(View.GONE);
            this.mTxvPopVoiceTime.setVisibility(View.GONE);
        }else{
            this.mTxvPopVoicePrompt.setText(R.string.pop_voice_start_speak);
            this.mImvPopVoiceDel.setVisibility(View.GONE);
            this.mImvPopVoiceMic.setVisibility(View.VISIBLE);
            this.mTxvPopVoiceTime.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 检测录音标志区域
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchVoiceSign(float x,float y){
        int top = (ActivityHelper.getScreenHeight() - this.mVwPopVoice.getHeight())/2;
        int bottom = top + this.mVwPopVoice.getHeight();
        int left = (ActivityHelper.getScreenWidth() - this.mVwPopVoice.getWidth())/2;
        int right = left + this.mVwPopVoice.getWidth();
        return (x >= left && x <= right)&&(y >= top && y <= bottom);
    }

    /**
     * 语音栏 或 摄像栏的 按压效果
     * @param txv
     * @param isPress
     *            是否 按下
     * @return void
     */
    private void setViewPress(TextView txv, boolean isPress) {
        if (isPress) {
            txv.setTextColor(getActivity().getResources().getColor(R.color.color_b7babc));
        } else {
            txv.setTextColor(getActivity().getResources().getColor(R.color.color_292929));
        }
    }

    /**
     * 获取时间的分秒格式 00:00
     * @return
     */
    private void setKeepTime(boolean isStart){
        if(isStart){

            if (this.mCalendar == null) {
                this.mCalendar = Calendar.getInstance();
                TimeZone tz = TimeZone.getTimeZone("GMT");//GMT+8
                this.mCalendar.setTimeZone(tz);
                this.mCalendar.get(Calendar.HOUR_OF_DAY);//24小时制
            }
            this.mStepHandler = new Handler();
            //System.uptimeMillis()　　　　　　　　 //记录从机器启动后到现在的毫秒数，当系统进入深度睡眠时，此计时器将会停止
            //System.currentTimeMillis()   //返回自1970年1月1日到现在的毫秒数，通常用来设置日期和时间
            //System.elapsedRealtime() 　　//返回从机器启动后到现在的毫秒数，包括系统深度睡眠的时间，api里没有这个方法
            //直接取得的是当地时区时间，当地时间跟时区有关,设置GMT后始终多12小时
            final long startTime = System.currentTimeMillis();//12*3600000  - 36*3600000减掉或者加上12小时都不行
            this.mTicker = new Runnable() {
                public void run() {
                    //这个减出来的日期是1970年的  时间格式不能出现00:00:00 12:00:00
                    long showTime = System.currentTimeMillis() - startTime;
//	                Log.d(TAG, "录音时间:" + showTime +" | 最长时间:" + VOICE_MAX_TIME);
//	                if(showTime > VOICE_MAX_TIME){
//		            	   // 超过最长录制时间则自动上传
//		            	   dismissVoicePop();
//		            	   mAudioHelper.stopRecord();
//		            	   ActivityHelper.showToast(R.string.toast_record_time_end);
//							// 上传语音文件到服务器
//		            	   uploadFile(Constant.CmdRequest.CMD_UPLOAD_AUDIO, mVoicePath,JsonLabelConstant.FileCategory.AUDIO);
//
//	                }else{
                    mCalendar.setTimeInMillis(showTime + 13*3600000 + 1000);
                    String content = (String) DateFormat.format(mTimeFormat, mCalendar);
                    mTxvPopVoiceTime.setText(content);

                    long now = SystemClock.uptimeMillis();
                    mCurTime = now + (1000 - now % 1000);
                    mStepHandler.postAtTime(mTicker, mCurTime);
                }
            };
            //启动计时线程，定时更新
            this.mTicker.run();
            // 录音
            AudioRecordHelper.startMediaRecordAudio(mVoicePath);
        }else{
            this.mTxvPopVoiceTime.setText("00:00");
            //停止计时 Remove any pending posts of Runnable r that are in the message queue.
            this.mStepHandler.removeCallbacks(mTicker);
        }
    }

    /**
     * 输入框监听
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
            if(s.length() > 0){
                mImbSend.setVisibility(View.VISIBLE);
                mImbMore.setVisibility(View.GONE);
            }else{
                mImbSend.setVisibility(View.GONE);
                mImbMore.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 表情输入监听
     */
    private CartoonFaceView.OnItemCartoonFaceViewListener mOnItemCartoonFaceViewListener = new CartoonFaceView.OnItemCartoonFaceViewListener() {
        @Override
        public void onGetFace(SpannableString ss) {
            mEdtContent.append(ss);
        }

        @Override
        public void onDelFace() {
            mEdtContent.setText(mFaceView.removeLastStr(mEdtContent.toString()));
        }
    };

    /**
     * 插入项监听
     */
    private ChatAddItemView.OnItemChatAddItemViewListener mOnItemChatAddItemViewListener = new ChatAddItemView.OnItemChatAddItemViewListener() {

        @Override
        public void onSelected(int tag) {
            switch (tag){
                case ChatAddItemView.BLESSE_PHRASE:
                    //祝福短语
                    showDialogFragmentBlessPhrase();
                    break;
                case ChatAddItemView.QUICK_NEWS:
                    //快捷短信
                    showDialogFragmentQuickNews();
                    break;
            }

        }

        @Override
        public void onIntent(Intent intent, int requestCode) {
            startActivityForResult(intent,requestCode);
        }
    };

    /**
     * 快捷短信对话框
     */
    private void showDialogFragmentQuickNews(){
        if(ResourceHelper.isNull(this.mDialogFragmentQuickNews)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SpannableString spannableString = ExpressionHelper.getExpressionString(getActivity(), mLsvAdapter.getItem(position));
                    mEdtContent.setText(spannableString);
                    mDialogFragmentQuickNews.getDialog().dismiss();
                }
            });

            mLsvItem.setAdapter(mLsvAdapter);
            mLsvAdapter.setShowRadio(false);
            mLsvAdapter.addItems(DialogFragmentHelper.getListFromResArray(getActivity(), R.array.sms_quick));
            this.mDialogFragmentQuickNews = DialogFragmentHelper.showCustomDialogFragment(R.string.pop_chat_quick_message, mLsvItem);
        }
        this.mDialogFragmentQuickNews.show(getFragmentManager(), "mDialogFragmentQuickNews");
    }

    /**
     * 祝福短信
     */
    private void showDialogFragmentBlessPhrase(){
        if(ResourceHelper.isNull(this.mDialogFragmentBlessPhrase)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SpannableString spannableString = ExpressionHelper.getExpressionString(getActivity(), mLsvAdapter.getItem(position));
                    mEdtContent.setText(spannableString);
                    mDialogFragmentBlessPhrase.getDialog().dismiss();
                }
            });
            mLsvItem.setAdapter(mLsvAdapter);
            mLsvAdapter.setShowRadio(false);
            mLsvAdapter.addItems(DialogFragmentHelper.getListFromResArray(getActivity(), R.array.sms_blessing));
            this.mDialogFragmentBlessPhrase = DialogFragmentHelper.showCustomDialogFragment(R.string.pop_chat_bless_message, mLsvItem);
        }
        this.mDialogFragmentBlessPhrase.show(getFragmentManager(), "mDialogFragmentBlessPhrase");
    }

     /**
     * 选择号码拨号弹窗
     */
    private void showDialogFragmentCallPhone(ArrayList<String> phoneList){
        if(ResourceHelper.isNull(this.mDialogFragmentCallPhone)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            this.mLsvCanllAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(this.mLsvCanllAdapter);
            this.mLsvCanllAdapter.setShowRadio(false);
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String phone = mLsvCanllAdapter.getItem(position);
                    mDialogFragmentCallPhone.getDialog().dismiss();
                    ResourceHelper.callPhone(getActivity(), phone);
                }
            });
            this.mDialogFragmentCallPhone = DialogFragmentHelper.showCustomDialogFragment(this.mTitle, mLsvItem);
        }
        this.mDialogFragmentCallPhone.setTitle(this.mTitle);
        this.mLsvCanllAdapter.addItems(phoneList);
        this.mDialogFragmentCallPhone.show(getFragmentManager(), "mDialogFragmentCallPhone");
    }

    /**
     * 信息长按弹窗
     * @param position
     */
    private void showDialogFragmentItemLong(int position){
        this.curPosition = position;
        if(ResourceHelper.isNull(this.mDialogFragmentItemLong)){
            ListView mLisItem = DialogFragmentHelper.getCustomContentView(getActivity());
            this.mLsvLongAdapter = new CustomLsvIntegerAdapter(getActivity());
            mLisItem.setAdapter(this.mLsvLongAdapter);
            this.mLsvLongAdapter.setShowRadio(false);
            mLisItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MessageSessionEntity mSelectSessionItem = msgConversationAdapter.getItem(curPosition);
                    if (ResourceHelper.isNotNull(mSelectSessionItem)) {
                        switch (mLsvLongAdapter.getItem(position).getContentId()) {
                            case R.string.repeat:
                                //重发
                                requestSendMess(mSelectSessionItem.getBody(), mSelectSessionItem.getContentType(), mSelectSessionItem.getResult());
                                break;
                            case R.string.forward:
                                // 转发
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, CardcaseChooseFragment.class.getName());
                                bundle.putInt(CardcaseChooseFragment.KEY_CARDCASE_CHOOSE, CardcaseChooseFragment.CARDCASE_CHOOSE_SELECT_SESSION_FORWARDING);
                                bundle.putLong(Constants.IntentSet.INTENT_KEY_MESSAGE_SESSION_ID, mSelectSessionItem.getId());
                                intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                                mActivitySwitchTo.switchTo(CardcaseMainActivity.class, intent);
                                break;
                            case R.string.delete:
                                //删除
                                msgConversationAdapter.deleteItem(mSelectSessionItem);
                                break;
                            case R.string.copy_message_text:
                                //复制文本信息
                                copyMessage(mSelectSessionItem);
                                break;
                            case R.string.share_message:
                                //分享信息
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, mSelectSessionItem.getBody());
                                getActivity().startActivity(Intent.createChooser(shareIntent, getString(R.string.mess_share)));
                                break;
                        }
                    }
                   mDialogFragmentItemLong.getDialog().dismiss();
                }
            });
            this.mDialogFragmentItemLong = DialogFragmentHelper.showCustomDialogFragment(R.string.info_choose, mLisItem);
        }
        this.mLsvLongAdapter.addItems(CustomLsvContentHelper.getMsgChatInfo(this.msgConversationAdapter.getItem(this.curPosition).getSendType()));
        this.mDialogFragmentItemLong.show(getFragmentManager(), "this.mDialogFragmentItemLong");
    }

    /**
     * 显示大图
     */
    private void showDialogFragmentHeadMax(String headImg){
        if(ResourceHelper.isNull(this.mDialogFragmentHeadMax)){
            this.mDialogFragmentHeadMax = DialogFragmentHelper.showUserHeadMaxDialogFragment(headImg);
            this.mDialogFragmentHeadMax.setTargetFragment(this, Constants.TakePicture.REQUEST_CODE_CHOOSE_HEAD);
        }
        this.mDialogFragmentHeadMax.setHeadImg(headImg);
        this.mDialogFragmentHeadMax.show(getFragmentManager(),"mDialogFragmentHeadMax");
    }

    /**
     * 上传文件到服务器
     */
    private void uploadFile(int tag, final String filePath, final String lastDir){
        if(ResourceHelper.isEmpty(filePath)) {
            return;
        }
        if(ResourceHelper.isNotNull(this.urlEntity)){
            String urlUpload = this.urlEntity.getImgBaseService().concat(this.urlEntity.getCommonUpload());
            urlUpload += "?" + "category" + "=push-file/" + lastDir;
            String newFileName = "";
            JSONObject json = new JSONObject();
            try {
                String terminalAgent = JsonHelper.toJson(ResourceHelper.getTerminalAgentEntity(), TerminalAgentEntity.class);
                ReqAndRespCenter.getInstance().upload(tag, urlUpload, filePath, newFileName, json, null, this, terminalAgent);
            }catch (Exception e){
                e.printStackTrace();
            }
         }
    }

    /**
     * 发送 文件路径
     * @param tag
     * @param url
     */
    private void sendSessionFile(int tag, String url){
        String fileDir = "";
        switch (tag) {
            case Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_MESSAGE_SESSION_AUDIO:
                fileDir = Constants.MessageFiles.FOLDER_AUDIO;
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_MESSAGE_SESSION_VIDEO:
                fileDir = Constants.MessageFiles.FOLDER_VIDEO;
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_MESSAGE_SESSION_IMAGE:
                fileDir = Constants.MessageFiles.FOLDER_IMAGE;
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_MESSAGE_SESSION_VCARD:
                fileDir = Constants.MessageFiles.FOLDER_VCARD;
                break;
        }

        String content = "";
        if(Helper.isNotEmpty(fileDir)){
            content = fileDir + Constants.Other.CONTENT_ARRAY_SPLIT + url;
        }else{
            content = url;
        }

        int contentType = Constants.MessageContentType.SESSION_FILE;

        if(mTagId > 0){
            contentType = Constants.MessageContentType.SESSION_GROUP_FILE;
        }
        requestSendMess(content, contentType, Constants.MessageResultType.SUCCESS);
    }

    /**
     * 追加消息到数据库
     * @param body
     * @param contentType
     */
    private void addSession(final String body, final int contentType){
        new AsyncTask<Void, Void, MessageSessionEntity>(){
            @Override
            protected MessageSessionEntity doInBackground(Void... params) {

                MessageSessionEntity msg = new MessageSessionEntity();
                // 数据插入到 本地
                long otherCardId = 0;
                String otherHead = null;
                String otherName = null;
                if(mAccountId > 0 && mTagId == 0){
                    ContactListItemEntity curContact = ContactVCardDao.getInstance().getEntityForListItemByAccount(mAccountId);
                    if(Helper.isNotNull(curContact)){
                        otherCardId = curContact.getCardId();
                        otherHead = curContact.getHeadImg();
                        otherName = curContact.getDisplayName();
                    }else{
                        MessageSessionEntity messageSessionEntity = fromOtherInformation(mAccountId);
                        if(Helper.isNotNull(messageSessionEntity)){
                            otherCardId = messageSessionEntity.getFromCardId();
                            otherHead = messageSessionEntity.getFromHeadImg();
                            otherName = messageSessionEntity.getFromName();
                        }
                    }
                }
                //获取我的当前名片id
                long curMyCardId = 0;
                if(ResourceHelper.isNotNull(curCard)){
                    curMyCardId = curCard.getId();
                }
                msg.setMyCardId(curMyCardId);
                msg.setFromAccountId(mAccountId);
                msg.setFromCardId(otherCardId);
                msg.setFromName(otherName);
                msg.setFromHeadImg(otherHead);
                msg.setContentType(contentType);
                msg.setCreateTime(ResourceHelper.getNowTime());
                msg.setBody(body);
                msg.setTagId(mTagId);
                msg.setSendType(DatabaseConstant.MessageSendType.SEND);
                msg.setRead(DatabaseConstant.MessageReadFlag.READED);
                mSessionDao.insert(msg);
                return msg;
            }

            protected void onPostExecute(MessageSessionEntity result) {
                msgConversationAdapter.addItem(result);
                mHandler.sendMessage(Message.obtain());
                mEdtContent.setText("");
            }

        }.execute();
    }

    private MessageSessionEntity fromOtherInformation(long mAccountId){
        if(Helper.isNotNull(this.mSessionList) && this.mSessionList.size() > 0){
            int sessionListSize = this.mSessionList.size();
            for (int i = 0; i < sessionListSize; i++) {
                if(this.mSessionList.get(i).getFromAccountId() == mAccountId){
                    return this.mSessionList.get(i);
                }
            }
        }
        return null;
    }

    /**
     * 推送消息请求
     */
    private void requestSendMess(String body, int contentType, int result){
        addSession(body, contentType);
        MessageResultEntity msgEntity = new MessageResultEntity();
        msgEntity.setBody(body);
        msgEntity.setToAccountId(this.mAccountId);
        msgEntity.setContentType(contentType);
        msgEntity.setResult(result);
        msgEntity.setCreateTime(ResourceHelper.getNowTime());
        msgEntity.setLoseTime(0);
        msgEntity.setTagId(this.mTagId);
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
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_MESSAGE_SESSION_SEND, msgUrl, mAccessToken, toJson, this);
    }

    /**
     * 复制文本信息
     * @param msgEntity
     */
    private void copyMessage(MessageSessionEntity msgEntity){
        if(ResourceHelper.isNotNull(msgEntity)){
            String body = msgEntity.getBody();
            String text = "";
            if(Helper.isNotNull(body)){
                ResourceHelper.copyText(body);
                text = getString(R.string.copy) + getString(R.string.operate_ok);
            }else{
                text = getString(R.string.copy) + getString(R.string.operate_fail);
            }
            if(ResourceHelper.isNotEmpty(text)){
                ActivityHelper.showToast(text);
            }
        }
    }

    /**
     * 通知广播刷新
     */
    private void sendBroadMessageMain(){
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        Intent msgMainIntent = new Intent();
        msgMainIntent.setAction(Constants.ActionIntent.ACTION_INTENT_MESSAGE_MAIN);
        broadcastManager.sendBroadcast(msgMainIntent);
    }

    /**
     * 广播接收
     */
    private void registerChatReceiver(){
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        this.messageConversationReceiver = new MessageConversationReceiver();
        IntentFilter filterSession = new IntentFilter();
        filterSession.addAction(Constants.ActionIntent.ACTION_INTENT_SESSION_CHAT);
        broadcastManager.registerReceiver(this.messageConversationReceiver, filterSession);
    }

    /**
     * 聊天会话广播
     */
    private class MessageConversationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isUpdateGroupName = intent.getBooleanExtra(Constants.IntentSet.INTENT_KEY_IS_UPDATE_CIRCLE_GROUP_NAME, false);
            if(!isUpdateGroupName){
                mTitle = CircleDao.getInstance().getShowName(mTagId, true);
                mTitleAction.setActivityTitle(mTitle, false);
                mHandler.sendMessage(Message.obtain());
            }else{
                mSessionList = mSessionDao.getList(mAccountId, mTagId);
                msgConversationAdapter.addItems(mSessionList);
                //滚动到底部
                mHandler.sendMessage(Message.obtain());
            }
        }
    }
}
