package com.maya.android.vcard.ui.frg;


import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.UserShareEntity;
import com.maya.android.vcard.entity.json.MessInviteJsonEntity;
import com.maya.android.vcard.entity.result.MessInviteListResultEntity;
import com.maya.android.vcard.entity.result.MessInviteResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.adapter.UserShareAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户分享：1、名片 2、软件
 * <p1> 推荐好友使用 </p1>
 * <p2> 名片分享 </p2>
 */
public class UserShareFragment extends BaseFragment {
    public static final String KEY_SHARE_WAY = "SHARE_WAY";
    /** 分享我的名片(名片分享) **/
    public static final int SHARE_WAY_VCARD = 10001;
    /** 分享我的软件(推荐好友使用) **/
    public static final int SHARE_WAY_SOFTWARE = 10002;
    /** 记录当前界面 **/
    private int curShareCode;
    private static final String SMS_PACKAGE_NAME = "mms";
    private static final String EMAIL_PACKAGE_NAME = "com.google.android.gm";
    private static final String EMAIL_PACKAGE_NAME2 = "com.android.email";
    /** 我的名片分享链接前缀 **/
    private static final String SHARE_URL_PREFIX = "?s=";
    private static final String DOWNLOAD_URL = "http://m.vcard2012.com/mobile?p=32";
    /** 我的名片分享截取长度 **/
    private static final int SHARE_URL_CUT_OUT_SIZE = 6;
    /** 短信分享 **/
    private static final int KEY_MESS = 900001;
    /** 邮件分享 **/
    private static final int KEY_EMAIL = 900002;
    /** 分享到微信朋友圈 **/
    private static final int KEY_WEHAT_FRIENT_CRICLE = 900003;
    /** 分享给微信朋友 **/
    private static final int KEY_WECHAT_FRIEND = 900004;
    /** 分享给QQ好友 **/
    private static final int KEY_QQ_FRIEND = 900005;
    /** 分享到腾讯微博 **/
    private static final int KEY_TENCENT_WEIBO = 900006;
    /** 分享到新浪微博 **/
    private static final int KEY_SINA_WEIBO = 900007;
    /** 记录已收录的分享类别 **/
    private SparseArray<UserShareEntity> shares = new SparseArray<UserShareEntity>();
    /** 分享内容 **/
    private String mShareContent;
    /** 微信 api **/
    private IWXAPI mIWXAPI;
    /** 分享链接 **/
    private String mShareUrl;
    private ListView mLsvShareVCard;
    private UserShareAdapter mUserShareAdapter;

    /**
     * 点击事件
     */
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            UserShareEntity userShare = mUserShareAdapter.getItem(position);
            switch (userShare.getShareNameId()){

                case R.string.send_mess_share:
                    //发送短信分享
                    Bundle bundle = new Bundle();
                    if(curShareCode == SHARE_WAY_VCARD){
                        bundle.putInt(KEY_SHARE_WAY, SHARE_WAY_VCARD);
                    }else{
                        bundle.putInt(KEY_SHARE_WAY, SHARE_WAY_SOFTWARE);
                    }
                    mFragmentInteractionImpl.onFragmentInteraction(UserSmsShareVCardFragment.class.getName(), bundle);
                    break;
                case R.string.share_wechat_friend_circle:
                    //微信朋友圈分享
                    SendMessageToWX.Req reqCricle = null;
                    boolean isToTimeline = true;
                    if(curShareCode == SHARE_WAY_VCARD){
                        //名片分享
                        reqCricle = sendReqToWeChatVCard(true);
                        isToTimeline = true;
                    }else{
                        //软件分享
                        reqCricle = sendReqToWeChatVCard(false);
                        isToTimeline = false;
                    }
                    if (Helper.isNotNull(reqCricle)) {
                        mIWXAPI.handleIntent(getShareIntent(), mWXHandler);
                        mIWXAPI.sendReq(reqCricle);
                    }else{
                        requestRecommonSms(null, MessInviteJsonEntity.SHARE_TYPE_CARD, true,  isToTimeline);
                    }
                    break;
                case R.string.share_wechat_friend:
                    //微信朋友分享
                    SendMessageToWX.Req reqSoftware = null;
                    boolean isToTimeLine = true;
                    if(curShareCode == SHARE_WAY_VCARD){
                        //名片分享
                        reqSoftware = sendReqToWeChatSoftware(true);
                        isToTimeLine = true;
                    }else{
                        //软件分享
                        reqSoftware = sendReqToWeChatSoftware(false);
                        isToTimeLine = false;
                    }
                    if (Helper.isNotNull(reqSoftware)) {
                        mIWXAPI.handleIntent(getShareIntent(), mWXHandler);
                        mIWXAPI.sendReq(reqSoftware);
                    }else{
                        requestRecommonSms(null, MessInviteJsonEntity.SHARE_TYPE_CARD, true,  isToTimeLine);
                    }
                    break;
                case R.string.send_email_share:
                    //电子邮件分享
                case R.string.share_sina_weibo:
                    //新浪微博分享
                case R.string.share_tencent_weibo:
                    //腾讯微博分享
                    getShareIntent().setAction("android.intent.action.SEND");
                case R.string.share_qq_friend:
                    //qq朋友分享
                    sendIntent(mShareContent, userShare.getPackageName(), userShare.getActivityName());
                    break;
            }
        }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_RECOMMOND_SMS_URL:
                    //短信推荐回调
                    boolean isToWeChat = false;
                    boolean isWeChatToTimeline = false;
                    if (Helper.isNotNull(objects)) {
                        isToWeChat = (Boolean) objects[0];
                        isWeChatToTimeline = (Boolean) objects[1];
                    }
                    // 获取短信链接 并跳转到发送页
                    if(isToWeChat){
                        MessInviteListResultEntity messInviteList = JsonHelper.fromJson(commandResult, MessInviteListResultEntity.class);
                        if (Helper.isNotNull(messInviteList) ) {
                            ArrayList<MessInviteResultEntity> messInvites = messInviteList.getUrlList();
                            if(ResourceHelper.isNotNull(messInvites)){
                                MessInviteResultEntity item = messInvites.get(0);
                                Intent intent = getShareIntent();
                                SendMessageToWX.Req req;
                                if(curShareCode == SHARE_WAY_VCARD){
                                    //分享名片
                                    this.mShareUrl = item.getUrl();
                                    this.mShareContent = getString(R.string.share_content_from_myinfo, this.mShareUrl);
                                    req = sendReqToWeChatVCard(isWeChatToTimeline);
                                }else{
                                    //分享软件
                                    req = sendReqToWeChatSoftware(isWeChatToTimeline);
                                }
                                intent.putExtra("android.intent.extra.TEXT",this.mShareContent);
                                if (Helper.isNotNull(req)) {
                                    mIWXAPI.handleIntent(intent, mWXHandler);
                                    mIWXAPI.sendReq(req);
                                }
                            }
                        }
                    }
                    ActivityHelper.closeProgressDialog();
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onResponseFail(int tag, int responseCode, String msgInfo) {
        super.onResponseFail(tag, responseCode, msgInfo);
        switch (tag){
            case Constants.CommandRequestTag.CMD_REQUEST_RECOMMOND_SMS_URL:
                ActivityHelper.closeProgressDialog();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_share, container, false);
        this.mLsvShareVCard = (ListView) view.findViewById(R.id.lsv_list);
        this.mLsvShareVCard.setOnItemClickListener(this.mOnItemClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mUserShareAdapter = new UserShareAdapter(getActivity());
        this.mLsvShareVCard.setAdapter(this.mUserShareAdapter);
//        initShareIntent();
        this.mUserShareAdapter.addItems(getArrayListShareType());
        Bundle arg = getArguments();
        this.curShareCode = SHARE_WAY_SOFTWARE;
        if(ResourceHelper.isNotNull(arg)){
            this.curShareCode = arg.getInt(KEY_SHARE_WAY, SHARE_WAY_VCARD);
        }
        if(this.curShareCode == SHARE_WAY_VCARD){
            //分享我的名片
            this.mTitleAction.setActivityTitle(R.string.vcard_share, true);
        }else{
            //分享我的软件
            this.mTitleAction.setActivityTitle(R.string.recommend_friend_user, true);
            this.mShareUrl = DOWNLOAD_URL;
        }
        this.mShareContent = getString(R.string.share_content_from_set, DOWNLOAD_URL);

    }

    /** 微信接收反馈信息 **/
    private IWXAPIEventHandler mWXHandler = new IWXAPIEventHandler() {

        @Override
        public void onResp(BaseResp arg0) {
            ActivityHelper.showToast(R.string.share_success);
        }

        @Override
        public void onReq(BaseReq arg0) {
            ActivityHelper.showToast(R.string.share_success);
        }
    };

    /**
     * 名片分享微信
     * @param isToTimeline 是否是微信朋友圈
     * @return
     */
    private SendMessageToWX.Req sendReqToWeChatVCard(boolean isToTimeline){
        //TODO 需检查问题
        SendMessageToWX.Req req = null;
        if (ResourceHelper.isNotNull(this.mShareUrl)) {
            req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.scene = isToTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            WXWebpageObject webpage = new WXWebpageObject();
            int shareUrlLength = this.mShareUrl.length();
            String webPageUrl = Constants.UmengOnlineKeyAndValue.VALUE_UMENG_URL_SHARE_MY_CARD_WEIXIN;
            if (shareUrlLength > SHARE_URL_CUT_OUT_SIZE) {
                webPageUrl += (SHARE_URL_PREFIX + this.mShareUrl.substring(shareUrlLength - SHARE_URL_CUT_OUT_SIZE,shareUrlLength));
            }
            webpage.webpageUrl = webPageUrl;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            CardEntity cardEntity = CurrentUser.getInstance().getCurrentVCardEntity();
            if (Helper.isNotNull(cardEntity)) {
                msg.description = cardEntity.getCompanyName()+ "\n" + cardEntity.getJob();
            }
            UserInfoResultEntity userInfo = CurrentUser.getInstance().getUserInfoEntity();
            if (Helper.isNotNull(userInfo)&& ResourceHelper.isNotEmpty(userInfo.getDisplayName())) {
                msg.title = userInfo.getDisplayName();
            } else {
                msg.title = "微名片";
            }
            Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.img_upload_head);
            msg.thumbData = ResourceHelper.bitmap2ByteArray(thumb, false);
            req.message = msg;
        }
        return req;
    }

    /**
     * 软件分享
     * @param isToTimeline 是否是微信朋友圈
     * @return
     */
    private SendMessageToWX.Req sendReqToWeChatSoftware(boolean isToTimeline){
        //TODO 需检查问题
        SendMessageToWX.Req req = null;
        if (ResourceHelper.isNotNull(this.mShareUrl)) {
            WXMediaMessage msg = null;
            if(isToTimeline){
                //微信朋友圈
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = Constants.UmengOnlineKeyAndValue.VALUE_UMENG_URL_SHARE_TO_FRIEND_WEIXIN;
                msg = new WXMediaMessage(webpage);
                msg.title = this.mShareContent;
                msg.description = this.mShareContent;
                Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.img_upload_head);
                msg.thumbData = ResourceHelper.bitmap2ByteArray(thumb, false);
            }else{
                //微信朋友
                // 初始化一个WXTextObject对象
                WXTextObject textObj = new WXTextObject();
                String shareContent = this.mShareContent;
                int shareUrlLength = this.mShareUrl.length();
                if (this.curShareCode == SHARE_WAY_SOFTWARE && shareUrlLength > SHARE_URL_CUT_OUT_SIZE) {
                    shareContent += (SHARE_URL_PREFIX + this.mShareUrl.substring(shareUrlLength - SHARE_URL_CUT_OUT_SIZE, shareUrlLength));
                }
                textObj.text = shareContent;
                // 用WXTextObject对象初始化一个WXMediaMessage对象
                msg = new WXMediaMessage();
                msg.mediaObject = textObj;
                // 发送文本类型的消息时，title字段不起作用
                msg.description = this.mShareContent;
            }
            req.message = msg;
        }
        return req;
    }

    /**
     * 短信推荐请求
     * @param arrayMobile
     * @param shareType
     * @param isSendToWechat
     * @param isWeChatToTimeline
     */
    private void requestRecommonSms(String[] arrayMobile, int shareType, boolean isSendToWechat, boolean isWeChatToTimeline) {
        URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
        CardEntity card = CurrentUser.getInstance().getCurrentVCardEntity();
        if(ResourceHelper.isNotNull(urlEntity) && ResourceHelper.isNotNull(card)){
            ActivityHelper.showProgressDialog(getActivity(), R.string.recommend_request_url);
            MessInviteJsonEntity jsonEntity = new MessInviteJsonEntity();
            jsonEntity.setCardId(card.getId());
            jsonEntity.setShareType(shareType);
            jsonEntity.setfMobileArr(arrayMobile);
            String json = JsonHelper.toJson(jsonEntity , MessInviteJsonEntity.class);
            String url = ProjectHelper.fillRequestURL(urlEntity.getRecommonUrl());
            String token = CurrentUser.getInstance().getToken();
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_RECOMMOND_SMS_URL, url,token, json,this, isSendToWechat, isWeChatToTimeline);
        }
    }

    /**
     * 发送 * @param intent
     * @param packageName
     * @param activityName
     */
    private void sendIntent(String content, String packageName,String activityName) {
        Intent mIntent = getShareIntent();
        mIntent.putExtra("android.intent.extra.TEXT", content);
        mIntent.putExtra("sms_body", content);
        ComponentName cn = new ComponentName(packageName, activityName);
        mIntent.setComponent(cn);
        mIntent.setAction("android.intent.action.SEND");
        startActivityForResult(mIntent, Constants.RequestCode.REQUEST_CODE_SHARE);
    }


    /**
     * 初始化Intent
     */
    private Intent getShareIntent() {
        Intent mIntent = new Intent();
        mIntent.setType("image/*");
        mIntent.putExtra("android.intent.extra.SUBJECT", "分享");
        mIntent.setType("text/plain");
        return mIntent;
    }

    /**
     * 获取可分享的类别
     * @return
     */
    private ArrayList<UserShareEntity> getArrayListShareType(){

        PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(getShareIntent(), PackageManager.MATCH_DEFAULT_ONLY);
        UserShareEntity userShare = new UserShareEntity();
        for (ResolveInfo resolveInfo : list) {
            // 包名
             String packageName = resolveInfo.activityInfo.packageName;
             String activityName = resolveInfo.activityInfo.name;
            try {
                userShare = (UserShareEntity) userShare.clone();
                userShare.setActivityName(activityName);
                userShare.setPackageName(packageName);
                if (packageName.contains(SMS_PACKAGE_NAME)) {
                    // 第一个:短信
                    if(ResourceHelper.isNull(this.shares.get(KEY_MESS, null))){
                        userShare.setDrawableId(R.mipmap.img_share_wechat);
                        userShare.setShareNameId(R.string.send_mess_share);
                        userShare.setShareContent(getString(R.string.share_home_sms_content));
                        userShare.setType("1");//短信分享
                        this.shares.put(KEY_MESS, userShare);
                    }
                    continue;
                }
                if (packageName.contains("mail")|| packageName.contains(EMAIL_PACKAGE_NAME)|| packageName.contains(EMAIL_PACKAGE_NAME2)) {
                    // 第二个:邮件
                    if(ResourceHelper.isNull(this.shares.get(KEY_EMAIL, null))){
                        userShare.setDrawableId(R.mipmap.img_share_email);
                        userShare.setShareNameId(R.string.send_email_share);
                        userShare.setShareContent(getString(R.string.share_home_email_content));
                        userShare.setType("1");//短信分享
                        this.shares.put(KEY_EMAIL, userShare);
                    }

                    continue;
                }
                if (packageName.contains("com.tencent.mm.ui.tools.ShareImgUI")|| packageName.contains("com.tencent.mm")|| packageName.contains("weixin")) {
                    // 第三个:微信朋友圈
                    if(ResourceHelper.isNull(this.shares.get(KEY_WEHAT_FRIENT_CRICLE, null))){
                        userShare.setDrawableId(R.mipmap.img_share_wechat_friend_circel);
                        userShare.setShareNameId(R.string.share_wechat_friend_circle);
                        userShare.setShareContent(getString(R.string.share_wechat_friend_circle_hint));
                        userShare.setType("2");//腾讯分享
                        this.shares.put(KEY_WEHAT_FRIENT_CRICLE, userShare);
                    }

                    // 第四个:微信朋友
                    if(ResourceHelper.isNull(this.shares.get(KEY_WECHAT_FRIEND, null))){
                        userShare = (UserShareEntity) userShare.clone();
                        userShare.setActivityName(activityName);
                        userShare.setPackageName(packageName);
                        userShare.setDrawableId(R.mipmap.img_share_wechat_friend);
                        userShare.setShareNameId(R.string.share_wechat_friend);
                        userShare.setShareContent(getString(R.string.share_wechat_friend_hint));
                        userShare.setType("2");//腾讯分享

                        this.shares.put(KEY_WECHAT_FRIEND, userShare);
                    }

                    continue;
                }
                if (packageName.contains("com.tencent.mobileqq.activity.JumpActivity")|| packageName.contains("com.tencent.mobileqq")) {
                    // 第五个:QQ好友
                    if(ResourceHelper.isNull(this.shares.get(KEY_QQ_FRIEND, null))){
                        userShare.setDrawableId(R.mipmap.img_share_qq);
                        userShare.setShareNameId(R.string.share_qq_friend);
                        userShare.setShareContent(getString(R.string.share_qq_friend_hint));
                        userShare.setType("2");//腾讯分享

                        this.shares.put(KEY_QQ_FRIEND, userShare);
                    }

                    continue;
                }
                if (packageName.contains("com.tencent.WBlog.intentproxy.TencentWeiboIntent")|| packageName.contains("com.tencent.WBlog")) {
                    // 第七个:腾讯微博
                    if(ResourceHelper.isNull(this.shares.get(KEY_TENCENT_WEIBO, null))){
                        userShare.setDrawableId(R.mipmap.img_share_tencent_weibo);
                        userShare.setShareNameId(R.string.share_tencent_weibo);
                        userShare.setShareContent(getString(R.string.share_tencent_weibo_hint));
                        this.shares.put(KEY_TENCENT_WEIBO, userShare);
                        userShare.setType("2");//腾讯分享

                    }
                    continue;
                }
                if (packageName.contains("com.sina.weibo.EditActivity")|| packageName.contains("com.sina.weibo")) {
                    // 第六个:新浪微博
                    if(ResourceHelper.isNull(this.shares.get(KEY_SINA_WEIBO, null))){
                        userShare.setDrawableId(R.mipmap.img_share_sina_weibo);
                        userShare.setShareNameId(R.string.share_sina_weibo);
                        userShare.setShareContent(getString(R.string.share_sina_weibo_hint));
                        this.shares.put(KEY_SINA_WEIBO, userShare);
                        userShare.setType("3");//腾讯分享

                    }

                    continue;
                }


            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        ArrayList<UserShareEntity> userShares = new ArrayList<UserShareEntity>();
        int key;
        UserShareEntity item;
        for(int i = 0, size = this.shares.size(); i < size; i++){
            key = this.shares.keyAt(i);
            item = this.shares.get(key, null);
            if(ResourceHelper.isNotNull(item)){
                userShares.add(item);
            }
        }
        return userShares;
    }
}
