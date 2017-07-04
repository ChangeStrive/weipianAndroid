package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.dao.MessageSessionDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.MessageSessionEntity;
import com.maya.android.vcard.util.ExpressionHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 聊天会话适配器
 * Created by Administrator on 2015/9/9.
 */
public class MsgConversationAdapter extends BaseAdapter{
    /** 布局控件 样式 **/
    private static final int VIEW_TYPE_COUNT = 3;
    /** 我发起的对话 **/
    private static final int VIEW_TYPE_ME = 0;
    /** 别人发起的对话 **/
    private static final int VIEW_TYPE_FROM = 1;
    /** 组消息 **/
    private static final int VIEW_TYPE_OTHER = 2;

    private Context mContext;
    private ArrayList<MessageSessionEntity> msgConversations = new ArrayList<MessageSessionEntity>();
    // 内容最大宽度
    private int mContentWidth, mImgHeight;
    // 语音播放动画
    private AnimationDrawable mAnimDrawable = null;
    private String mMsgGroupCreateTime = "";
    private ChatContentClickListener mContentListener;
    private Handler mLoadingImg ;
    private HandlerThread mThread ;

    public MsgConversationAdapter(Context mContext){
        this.mContext = mContext;
        this.mContentWidth = (int)(ActivityHelper.getScreenWidth() * 0.62);
        this.mImgHeight = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_100dp);
        this.mThread = new HandlerThread("li");
        this.mThread.start();
        this.mLoadingImg = new Handler(mThread.getLooper());
    }

    @Override
    public int getCount() {
        return this.msgConversations.size();
    }

    @Override
    public MessageSessionEntity getItem(int position) {
        MessageSessionEntity msgConversation = this.msgConversations.get(position);
        if(ResourceHelper.isNotNull(msgConversation)){
            return msgConversation;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        MessageSessionEntity sessionItem = getItem(position);
        int type = VIEW_TYPE_OTHER;
        if(ResourceHelper.isNotNull(sessionItem)){
            switch (sessionItem.getSendType()) {
                case DatabaseConstant.MessageSendType.RECIVER:
                    type = VIEW_TYPE_FROM;
                    break;
                case DatabaseConstant.MessageSendType.SEND:
                    type = VIEW_TYPE_ME;
                    break;
                default:
                    type = VIEW_TYPE_OTHER;
                    break;
            }
        }
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderLeft mHolderLeft = null;
        ViewHolderRight mHolderRight = null;
        ViewHolderOther mHolderOther = null;
        int type = getItemViewType(position);
        if(ResourceHelper.isNull(convertView)){
            switch (type){
                case VIEW_TYPE_ME://我 右边
                    convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_chat_right, parent, false);
                    mHolderRight = new ViewHolderRight();
                    mHolderRight.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_user_img_head);
                    mHolderRight.mTxvDateTime = (TextView) convertView.findViewById(R.id.txv_datetime);
                    mHolderRight.mTxvTime = (TextView) convertView.findViewById(R.id.txv_time);
                    mHolderRight.mTxvContent = (TextView) convertView.findViewById(R.id.txv_user_content);
                    mHolderRight.mLilContent = (LinearLayout) convertView.findViewById(R.id.lil_user_content);
                    convertView.setTag(mHolderRight);
                    break;
                case VIEW_TYPE_FROM://别人 左边
                    convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_chat_left, parent, false);
                    mHolderLeft = new ViewHolderLeft();
                    mHolderLeft.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_from_head);
                    mHolderLeft.mTxvDateTime = (TextView) convertView.findViewById(R.id.txv_datetime);
                    mHolderLeft.mTxvTime = (TextView) convertView.findViewById(R.id.txv_time);
                    mHolderLeft.mTxvContent = (TextView) convertView.findViewById(R.id.txv_from_content);
                    mHolderLeft.mLilContent = (LinearLayout) convertView.findViewById(R.id.lil_from_content);
                    convertView.setTag(mHolderLeft);
                    break;
                case VIEW_TYPE_OTHER://其他
                    convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_chat_other, parent, false);
                    mHolderOther = new ViewHolderOther();
                    mHolderOther.mTxvOtherDate = (TextView) convertView.findViewById(R.id.txv_date_other);
                    mHolderOther.mTxvOtherContent = (TextView) convertView.findViewById(R.id.txv_chat_content_other);
                    convertView.setTag(mHolderOther);
                    break;
            }
        }else{
            switch (type){
                case VIEW_TYPE_ME://我 右边
                    mHolderRight = (ViewHolderRight) convertView.getTag();
                    break;
                case VIEW_TYPE_FROM://别人 左边
                    mHolderLeft = (ViewHolderLeft) convertView.getTag();
                    break;
                case VIEW_TYPE_OTHER://其他
                    mHolderOther = (ViewHolderOther) convertView.getTag();
                break;
            }
        }
        MessageSessionEntity msgConversation = getItem(position);
        if(ResourceHelper.isNotNull(msgConversation)){
            String preDate = mMsgGroupCreateTime;
            if(position > 0){
                preDate = ResourceHelper.getDateWeekForShow(getItem(position - 1).getCreateTime());
            }
            String curDate = ResourceHelper.getDateWeekForShow(msgConversation.getCreateTime());
            String curTime = ResourceHelper.getTimeShortStr(msgConversation.getCreateTime());
            switch (type){
                case VIEW_TYPE_ME://我 右边
                    mHolderRight.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
                    String myHead = CurrentUser.getInstance().getUserInfoEntity().getHeadImg();
                    ResourceHelper.asyncImageViewFillUrl(mHolderRight.mImvHead, ResourceHelper.getImageUrlOnIndex(myHead, 0));
                    mHolderRight.mTxvTime.setText(curTime);
                    setContentText(mHolderRight.mTxvContent, mHolderRight.mLilContent, position);
                    showDatetime(mHolderRight.mTxvDateTime, curDate, preDate, position);
                    break;
                case VIEW_TYPE_FROM://别人 左边
                    String headImg = ResourceHelper.getImageUrlOnIndex(msgConversation.getFromHeadImg(), 0);
                    if(msgConversation.getFromAccountId() == Constants.DefaultUser.DEFAULT_USER_ID){
                        mHolderLeft.mImvHead.setDefaultImageResId(R.mipmap.img_user_head_mytip);
                    }else{
                        mHolderLeft.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
                        ResourceHelper.asyncImageViewFillUrl(mHolderLeft.mImvHead, headImg);
                    }
                    mHolderLeft.mTxvTime.setText(curTime);
                    setContentText(mHolderLeft.mTxvContent, mHolderLeft.mLilContent, position);
                    showDatetime(mHolderLeft.mTxvDateTime, curDate, preDate, position);
                    break;
                case VIEW_TYPE_OTHER://其他
                    mHolderOther.mTxvOtherContent.setText(msgConversation.getBody());
                    if(ResourceHelper.isNotEmpty(preDate) && preDate.equals(curDate)){
                        mHolderOther.mTxvOtherDate.setText(curTime);
                    }else{
                        mHolderOther.mTxvOtherDate.setText(curDate + " " + curTime);
                    }
                    showDatetime(mHolderOther.mTxvOtherDate, curDate, preDate, position);
                    break;
            }
        }
       return convertView;
    }

    /**
     * 删除该条会话
     * @param conversation
     */
    public void deleteItem(MessageSessionEntity conversation){
        if(ResourceHelper.isNotNull(conversation)){
            if(conversation.getFromAccountId() == Constants.DefaultUser.DEFAULT_USER_ID){
                ActivityHelper.showToast(R.string.common_cannot_delete);
                return;
            }
            MessageSessionDao.getInstance().delete(conversation.getId());
            this.msgConversations.remove(conversation);
        }
        notifyDataSetChanged();
    }

    /**
     * 追加一条会话
     * @param conversation
     */
    public void addItem(MessageSessionEntity conversation){
        if(ResourceHelper.isNotNull(conversation)){
            this.msgConversations.add(conversation);
        }
        notifyDataSetChanged();
    }

    /**
     * 用户发送文字或表情消息
     * @param content
     */
    public void sendMsg(String content){
        if(ResourceHelper.isNotEmpty(content)){
            MessageSessionEntity conversation = new MessageSessionEntity();
            conversation.setSendType(0);
            conversation.setBody(content);
            conversation.setCreateTime(ResourceHelper.getNowTime());
            addItem(conversation);
        }

    }

    /**
     * 更新数据源
     * @param msgConversations
     */
    public void addItems(ArrayList<MessageSessionEntity> msgConversations){
        this.msgConversations.clear();
        if(ResourceHelper.isNotNull(msgConversations)){
            this.msgConversations.addAll(msgConversations);
        }
        notifyDataSetChanged();
    }

    /**
     * 设定时间
     * @param groupCreateTime
     */
    public void setMsgGroupCreateType(String groupCreateTime){
        this.mMsgGroupCreateTime = groupCreateTime;
    }

    /**
     * 监听接口
     * @param contentClickListener
     */
    public void setChatContentClickListener(ChatContentClickListener contentClickListener){
        this.mContentListener = contentClickListener;
    }

    /**
     * 显示发送时间(两条Reply之间相差100000ms则展示时间)
     * @param mTxvShowDateTime
     * @param curDate
     * @param position
     */
    private void showDatetime(TextView mTxvShowDateTime, String curDate, String preDate, int position){
        RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) mTxvShowDateTime.getLayoutParams();
        if(position == 0){
            mTxvShowDateTime.setText(curDate);
            mTxvShowDateTime.setVisibility(View.VISIBLE);
            mParams.topMargin = (int) ActivityHelper.getGlobalApplicationContext().getResources().getDimension(R.dimen.dimen_10dp);
        }else{
            mParams.topMargin = (int)ActivityHelper.getGlobalApplicationContext().getResources().getDimension(R.dimen.dimen_0dp);
            if(ResourceHelper.isNotEmpty(preDate) && preDate.equals(curDate)){
                mTxvShowDateTime.setVisibility(View.GONE);
            }else{
                mTxvShowDateTime.setText(curDate);
                mTxvShowDateTime.setVisibility(View.VISIBLE);
            }
        }
        mTxvShowDateTime.setLayoutParams(mParams);
    }


    /**
     * 给内容项 赋值
     * @param mTxvContent
     * @param curPosition
     * @param lilContent
     */
    private void setContentText(TextView mTxvContent, LinearLayout lilContent, final int curPosition){
        MessageSessionEntity msg = getItem(curPosition);
        String content = msg.getBody();
        mTxvContent.setMaxWidth(this.mContentWidth);
        mTxvContent.setVisibility(View.VISIBLE);
        if(getCount() > 1){
            lilContent.removeViews(1, lilContent.getChildCount()-1);
        }
        View.OnLongClickListener longClick = new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                mContentListener.onLongClick(curPosition);
                return false;
            }
        };
        switch (msg.getContentType()) {
            case Constants.MessageContentType.SESSION_FILE:
            case Constants.MessageContentType.SESSION_GROUP_FILE:
                String[] dataArr = content.split(Constants.Other.CONTENT_ARRAY_SPLIT);
                String fPath = "";
                if(dataArr.length == 1){
                    //视频   文件
                    fPath = content;
                    mTxvContent.setText(Html.fromHtml("<img src='"+R.mipmap.img_chat_file+"'/>"
                            + "<a href='" + fPath+"'> 点击下载   </a>", this.mImageGetter, null));
                }else if( dataArr.length == 2){
                    fPath = dataArr[1];
                    //语音文件播放  、 图片
                    if(Constants.MessageFiles.FOLDER_AUDIO.equals(dataArr[0])){
                        mTxvContent.setVisibility(View.GONE);
                        ImageView imv = addVoiceImv(fPath, msg.getSendType());
                        lilContent.addView(imv, 1);
                        break;
                    }else if(Constants.MessageFiles.FOLDER_IMAGE.equals(dataArr[0])){
                        // 图片
                        mTxvContent.setVisibility(View.GONE);
                        ImageView imv = addImgImv(fPath);
                        lilContent.addView(imv, 1);
                        imv.setLongClickable(true);
                        imv.setOnLongClickListener(longClick);
                        break;
                    }else if(Constants.MessageFiles.FOLDER_VIDEO.equals(dataArr[0])){
                        mTxvContent.setText(Html.fromHtml("<img src='" + R.mipmap.img_chat_video + "'/>"
                                + "<a href='" + fPath + "'> 点击下载播放  </a>", this.mImageGetter, null));
                        break;
                    }
                }

                break;
            default:
                try {
                    SpannableString spannableString = ExpressionHelper.getExpressionString(this.mContext, content);
                    mTxvContent.setText(spannableString);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                mTxvContent.setLongClickable(true);
                mTxvContent.setOnLongClickListener(longClick);
                break;
        }
        mTxvContent.setMovementMethod(LinkMovementMethod.getInstance());

    }

    /**
     * 添加语音对象
     * @param filePath
     */
    private ImageView addVoiceImv(final String filePath, int sendType){
        final ImageView imvVoice = new ImageView(this.mContext);
        int drwResId = 0;
        int stateIntResId = 0;
        if(sendType == DatabaseConstant.MessageSendType.SEND){
            drwResId = R.drawable.msg_anim_img_voice_right;
            stateIntResId = R.mipmap.img_audio_play_r3;
        }else if(sendType == DatabaseConstant.MessageSendType.RECIVER){
            drwResId = R.drawable.msg_anim_img_voice_left;
            stateIntResId = R.mipmap.img_audio_play_l3;
        }

        final AnimationDrawable animDrw = (AnimationDrawable) mContext.getResources().getDrawable(drwResId);
        animDrw.setVisible(true, false);
        imvVoice.setImageResource(stateIntResId);
        imvVoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imvVoice.setImageDrawable(animDrw);
                stopAnim();
                mAnimDrawable = animDrw;
                animDrw.start();
                mContentListener.onClickPlayAudio(filePath, animDrw);
            }
        });
        return imvVoice;
    }

    /**
     * 关闭动画
     */
    private void stopAnim(){
        if(ResourceHelper.isNotNull(this.mAnimDrawable) && this.mAnimDrawable.isRunning()){
            this.mAnimDrawable.stop();
        }
    }

    /**
     * 添加图片展示控件
     * @param httpUrl
     * @return
     */
    private AsyncImageView addImgImv(final String httpUrl){
        final AsyncImageView mAsyncImv = new AsyncImageView(this.mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(this.mContentWidth / 2, this.mImgHeight);
        mAsyncImv.setLayoutParams(lp);
        mAsyncImv.setScaleType(ImageView.ScaleType.FIT_XY);
        mAsyncImv.setDefaultImageResId(R.mipmap.img_chat_defaut_gay);
        this.mLoadingImg.post(new Runnable() {
            @Override
            public void run() {
                ResourceHelper.asyncImageViewFillUrl(mAsyncImv, httpUrl);
            }
        });
        mAsyncImv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mContentListener.onClickZoomImg(httpUrl);
            }
        });
        return mAsyncImv;
    }

    /** 获取表情 **/
    private Html.ImageGetter mImageGetter = new Html.ImageGetter() {

        @Override
        public Drawable getDrawable(String source) {
            int id = Integer.parseInt(source);
            Drawable drawable = mContext.getResources().getDrawable(id);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            return drawable;
        }
    };

    /**
     * 内容项操作回调 接口
     * @author zheng_cz
     * @since 2014年4月3日 下午3:06:54
     */
    public interface ChatContentClickListener{
        /**
         * 内容长按操作
         * @param position
         */
         void onLongClick(int position);
        /**
         * 单击图片放大
         * @param url
         */
        void onClickZoomImg(String url);
        /**
         * 单击播放语音
         * @param url
         * @param animDrawable
         */
        void onClickPlayAudio(String url, AnimationDrawable animDrawable);
    }

    private class ViewHolderLeft{
        private AsyncImageView mImvHead;
        private TextView mTxvDateTime, mTxvTime, mTxvContent;
        private LinearLayout mLilContent;
    }

    private class ViewHolderRight{
        private AsyncImageView mImvHead;
        private TextView mTxvDateTime, mTxvTime, mTxvContent;
        private LinearLayout mLilContent;
    }

    private class ViewHolderOther{
        TextView mTxvOtherDate, mTxvOtherContent;
    }
}
