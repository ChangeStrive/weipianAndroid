package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.util.ResourceHelper;
import com.umeng.fb.model.Reply;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 意见反馈
 * Created by Administrator on 2015/8/20.
 */
public class FeedBackAdapter extends BaseAdapter{
    /**布局控件**/
    private static final int VIEW_TYPE_COUNT = 2;
    /**开发者回复Item布局 **/
    private static final int VIEW_TYPE_DEV = 0;
    /**用户反馈、回复Item布局 **/
    private static final int VIEW_TYPE_USER =1;
    private Context mContext;
    private List<Reply> mReplys = new ArrayList<Reply>();

    public FeedBackAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mReplys.size();
    }

    @Override
    public Reply getItem(int position) {
        Reply mReply = mReplys.get(position);
        if(Helper.isNotNull(mReply)){
            return mReply;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        // 获取单条回复
        Reply reply = getItem(position);
        if (Reply.TYPE_DEV_REPLY.equals(reply.type)) {
            // 开发者回复Item布局
            return VIEW_TYPE_DEV;
        }
            // 用户反馈、回复Item布局
        return VIEW_TYPE_USER;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderLeft mHolderLeft = null;
        ViewHolderRight mHolderRight = null;
        Reply reply = getItem(position);
        int type = getItemViewType(position);
        if(ResourceHelper.isNull(convertView)){
            switch (type){
                case VIEW_TYPE_DEV:
                    //左边布局（开发者）
                    convertView = LayoutInflater.from(this.mContext).inflate( R.layout.item_feedback_left, null);
                    mHolderLeft = new ViewHolderLeft();
                    mHolderLeft.mTxvShowDateTime = (TextView) convertView.findViewById(R.id.txv_datetime);
                    mHolderLeft.mTxvShowDeveloperFeedback = (TextView) convertView.findViewById(R.id.txv_developer_feedback);
                    mHolderLeft.mImvDeveloperImgHead = (AsyncImageView) convertView.findViewById(R.id.imv_developer_img_head);
                    convertView.setTag(mHolderLeft);
                    break;
                case VIEW_TYPE_USER:
                    //右边布局（用户反馈）
                    convertView = LayoutInflater.from(this.mContext).inflate( R.layout.item_feedback_right, null);
                    mHolderRight = new ViewHolderRight();
                    mHolderRight.mTxvShowDateTime = (TextView) convertView.findViewById(R.id.txv_datetime);
                    mHolderRight.mTxvShowUserFeedback = (TextView) convertView.findViewById(R.id.txv_user_feedback);
                    mHolderRight.mImvUserImgHead = (AsyncImageView) convertView.findViewById(R.id.imv_user_img_head);
                    convertView.setTag(mHolderRight);
                    break;
            }
         }else{
            switch (type){
                case VIEW_TYPE_DEV:
                    //左边布局（开发者）
                    mHolderLeft = (ViewHolderLeft) convertView.getTag();
                    break;
                case VIEW_TYPE_USER:
                    //右边布局（用户反馈）
                    mHolderRight = (ViewHolderRight) convertView.getTag();
                    break;
            }
        }
        //设置数据
        switch (type){
            case VIEW_TYPE_DEV://开发者
                mHolderLeft.mTxvShowDeveloperFeedback.setText(reply.content);
                mHolderLeft.mImvDeveloperImgHead.setImageResource(R.mipmap.ic_launcher);
                showDatetime(mHolderLeft.mTxvShowDateTime, reply, position);
                break;
            case VIEW_TYPE_USER://用户反馈
                mHolderRight.mTxvShowUserFeedback.setText(reply.content);
                mHolderRight.mImvUserImgHead.setImageResource(R.mipmap.ic_launcher);
                showDatetime(mHolderRight.mTxvShowDateTime, reply, position);

                break;
        }
        return convertView;
    }

    /**
     * 添加数据源
     * @param mReplys
     */
    public void addItems(List<Reply> mReplys){
        this.mReplys.clear();
        if(Helper.isNotNull(mReplys)){
            this.mReplys.addAll(mReplys);
        }
        notifyDataSetChanged();
    }

    /**
     * 显示发送时间(两条Reply之间相差100000ms则展示时间)
     * @param mTxvShowDateTime
     * @param reply
     * @param position
     */
    private void showDatetime(TextView mTxvShowDateTime, Reply reply, int position){
        RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) mTxvShowDateTime.getLayoutParams();
        if(position == 0){
            mTxvShowDateTime.setText(getDatetime(reply));
            mTxvShowDateTime.setVisibility(View.VISIBLE);
            mParams.topMargin = (int)ActivityHelper.getGlobalApplicationContext().getResources().getDimension(R.dimen.dimen_10dp);
        }else{
            mParams.topMargin = (int)ActivityHelper.getGlobalApplicationContext().getResources().getDimension(R.dimen.dimen_0dp);
            Reply lastReply = getItem(position - 1);
            if (reply.created_at - lastReply.created_at > 100000) {
                mTxvShowDateTime.setText(getDatetime(reply));
                mTxvShowDateTime.setVisibility(View.VISIBLE);
            }else{
                mTxvShowDateTime.setVisibility(View.GONE);
            }
         }
        mTxvShowDateTime.setLayoutParams(mParams);
    }

    private String getDatetime(Reply reply){
        Date replyTime = new Date(reply.created_at);
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        return sdf.format(replyTime);
    }

    /**
     * 左边Item布局
     */
    private class ViewHolderLeft{
        private TextView mTxvShowDateTime;
        private AsyncImageView mImvDeveloperImgHead;
        private TextView mTxvShowDeveloperFeedback;
    }

    /**
     * 右边Item布局
     */
    private class ViewHolderRight{
        private TextView mTxvShowDateTime;
        private AsyncImageView mImvUserImgHead;
        private TextView mTxvShowUserFeedback;
    }


}
