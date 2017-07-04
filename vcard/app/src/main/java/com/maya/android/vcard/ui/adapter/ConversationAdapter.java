package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.MessageSessionEntity;
import com.maya.android.vcard.util.ExpressionHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 会话列表适配器
 * Created by Administrator on 2015/8/21.
 */
public class ConversationAdapter extends BaseAdapter {
    private Context mContext;
    private List<MessageSessionEntity> mConversations = new ArrayList<MessageSessionEntity>();

    public ConversationAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mConversations.size();
    }

    @Override
    public MessageSessionEntity getItem(int position) {
        MessageSessionEntity mConversation = this.mConversations.get(position);
        if(ResourceHelper.isNotNull(mConversation)){
            return mConversation;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        MessageSessionEntity entity = getItem(position);
        if(Helper.isNull(convertView)){
            convertView =  LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_news_conversation, null);
            mHolder = new ViewHolder();
            mHolder.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_img_head);
            mHolder.mTxvChatTitle = (TextView) convertView.findViewById(R.id.txv_title);
            mHolder.mTxvChatContent = (TextView) convertView.findViewById(R.id.txv_content);
            mHolder.mTxvDataTime = (TextView) convertView.findViewById(R.id.txv_datetime);
            mHolder.mTxvRedNum = (TextView) convertView.findViewById(R.id.txv_red_hint_num);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        if(entity.getTagId() > 0){
            mHolder.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
        }else{
            mHolder.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
            if(entity.getFromAccountId() == Constants.DefaultUser.DEFAULT_USER_ID){
                mHolder.mImvHead.setDefaultImageResId(R.mipmap.img_user_head_mytip);
            }else{
                String headImg = ResourceHelper.getImageUrlOnIndex(entity.getFromHeadImg(), 0);
                ResourceHelper.asyncImageViewFillUrl(mHolder.mImvHead, headImg);
            }
        }
        mHolder.mTxvChatTitle.setText(entity.getListShowName());
//        mHolder.mTxvChatContent.setText(entity.getContent());
        mHolder.mTxvDataTime.setText(ResourceHelper.getDateTimeForSession(entity.getCreateTime()));
        int noReadNumber = entity.getUnreadCount();
        if(noReadNumber > 0){
            mHolder.mTxvRedNum.setText(""+ noReadNumber);
            mHolder.mTxvRedNum.setVisibility(View.VISIBLE);
        }else{
            mHolder.mTxvRedNum.setVisibility(View.GONE);
        }
        setContent(mHolder.mTxvChatContent, entity.getBody(), entity.getContentType());
        return convertView;
    }

    /**
     * 添加数据源
     * @param mConversations
     */
    public void addItems(ArrayList<MessageSessionEntity> mConversations){
        this.mConversations.clear();
        if(Helper.isNotNull(mConversations)){
            this.mConversations.addAll(mConversations);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加单条数据
     * @param mConversation
     */
    public void addItem(MessageSessionEntity mConversation){
        if(Helper.isNotNull(mConversation)){
            this.mConversations.add(mConversation);
        }
        notifyDataSetChanged();
    }

    /**
     * 删除指定项
     * @param entity
     */
    public void deleteItem(MessageSessionEntity entity){
        this.mConversations.remove(entity);
        notifyDataSetChanged();
    }

    /**
     * 置顶会话 并返回当前会话名
     * @param position
     */
    public String moveTopAndgetListShowName(int position){
        this.mConversations.add(0, getItem(position));
        this.mConversations.remove(position + 1);
        notifyDataSetChanged();
        return getItem(0).getListShowName();
    }

    /**
     * 未读数量重置为0
     * @param position
     */
    public void setUnreadCount(int position){
        getItem(position).setUnreadCount(0);
        notifyDataSetChanged();
    }

    /**
     * 设置内容
     * @param txvContent
     * @param body
     * @param type 内容类型
     */
    private void setContent(TextView txvContent, String body, int type){
        String showContent = null;
        switch (type) {
            case Constants.MessageContentType.SESSION_FILE:
            case Constants.MessageContentType.SESSION_GROUP_FILE:
                String[] dataArr = body.split("#");
                if(dataArr[0].equals(Constants.MessageFiles.FOLDER_IMAGE)){
                    showContent = "[一张图片]";
                }else if(dataArr[0].equals(Constants.MessageFiles.FOLDER_AUDIO)){
                    showContent = "[语音文件]";
                }else if(dataArr[0].equals(Constants.MessageFiles.FOLDER_VIDEO)){
                    showContent = "[视频文件]";
                }else if(dataArr[0].equals(Constants.MessageFiles.FOLDER_OTHER)){
                    showContent = "[普通文件]";
                }else if(dataArr[0].equals(Constants.MessageFiles.FOLDER_VCARD)){
                    showContent = "[一张名片]";
                }
                txvContent.setText(showContent);
                break;
            default:
                try {
                    SpannableString spannableString = ExpressionHelper.getExpressionString(this.mContext, body);
                    txvContent.setText(spannableString);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private class ViewHolder{
        private AsyncImageView mImvHead;
        private TextView mTxvChatTitle, mTxvChatContent, mTxvDataTime, mTxvRedNum;
    }
}
