package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.entity.MessageNoticeEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 系统通知适配器
 * Created by Administrator on 2015/9/8.
 */
public class MessageNotifyAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<MessageNoticeEntity> msgNotify = new ArrayList<MessageNoticeEntity>();
    /** 记录选中项 **/
    private SparseArray<MessageNoticeEntity> mChbItemArray = new SparseArray<MessageNoticeEntity>();
    /** 是否显示ChecKBox **/
    private boolean isRedio = false;
    private NoticeItemOperateListener mItemOperateListener;

    public MessageNotifyAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.msgNotify.size();
    }

    @Override
    public MessageNoticeEntity getItem(int position) {
        MessageNoticeEntity notify = this.msgNotify.get(position);
        if(ResourceHelper.isNotNull(notify)){
            return notify;
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
        if(ResourceHelper.isNull(convertView)){
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_msg_notify, parent, false);
            mHolder = new ViewHolder();
            mHolder.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_head);
            mHolder.mTxvTitle = (TextView) convertView.findViewById(R.id.txv_swap_card_want);
            mHolder.mTxvDateTime = (TextView) convertView.findViewById(R.id.txv_datetime);
            mHolder.mTxvHaveAgree = (TextView) convertView.findViewById(R.id.txv_have_agree);
            mHolder.mTxvFromWhere = (TextView) convertView.findViewById(R.id.txv_from_where);
            mHolder.mChbAgree = (CheckBox) convertView.findViewById(R.id.chb_have_agree);
            mHolder.mRelShowButton = (RelativeLayout) convertView.findViewById(R.id.rel_button);
            mHolder.mBtnAgree = (Button) convertView.findViewById(R.id.btn_agree);
            mHolder.mBtnCancel = (Button) convertView.findViewById(R.id.btn_cancel);
            mHolder.mBtnAgree.setTag(position);
            mHolder.mBtnCancel.setTag(position);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
            mHolder.mBtnAgree.setTag(position);
            mHolder.mBtnCancel.setTag(position);
        }
        MessageNoticeEntity notify = getItem(position);

        mHolder.mTxvTitle.setText(notify.getTitle());
        String dateTime = ResourceHelper.getDateTimeForSession(notify.getCreateTime());
        if(ResourceHelper.isNotEmpty(dateTime)){
            mHolder.mTxvDateTime.setText(dateTime);
        }
        if(ResourceHelper.isNotEmpty(notify.getFromHeadImg())){
            String headImg = ResourceHelper.getImageUrlOnIndex(notify.getFromHeadImg(), 0);
            ResourceHelper.asyncImageViewFillUrl(mHolder.mImvHead, headImg);
        }
        //通知状态
        int contentType = notify.getContentType();
        if(contentType == Constants.MessageContentType.NOTICE_CARD_SWAP_REQUEST
                || contentType == Constants.MessageContentType.NOTICE_SMS_RECOMMEND_SWAP_REQUEST
                || contentType == Constants.MessageContentType.NOTICE_TRANSMIT_SWAP_REQUEST){
            mHolder.mTxvFromWhere.setText(notify.getBody());
            showRequestStatus(mHolder, position);
        }else if(contentType == Constants.MessageContentType.NOTICE_TRANSMIT_ALLOW_REQUEST
                ||contentType == Constants.MessageContentType.NOTICE_ENTERPRISE_ADD_REQUEST){
            showRequestStatus(mHolder, position);
        }else{
            mHolder.mTxvHaveAgree.setVisibility(View.GONE);
            mHolder.mRelShowButton.setVisibility(View.GONE);
        }

        if(this.isRedio){
            boolean isSelected = ResourceHelper.isNotNull(this.mChbItemArray.get(position, null));
            mHolder.mChbAgree.setChecked(isSelected);
            mHolder.mChbAgree.setVisibility(View.VISIBLE);
            final int mCurPosition = position;
            mHolder.mChbAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select(mCurPosition);
                 }
            }) ;
        }else{
            mHolder.mChbAgree.setVisibility(View.GONE);
        }

        return convertView;
    }

    /**
     * 设置操作项监听
     * @param itemOperateListener
     */
    public void setNoticeItemOperateListener(NoticeItemOperateListener itemOperateListener){
        this.mItemOperateListener = itemOperateListener;
    }



    /**
     * 更新数据源(添加所有数据)
     * @param msgNotify
     */
    public void addItems(ArrayList<MessageNoticeEntity> msgNotify){
        this.msgNotify.clear();
        if(ResourceHelper.isNotNull(msgNotify)){
            this.msgNotify.addAll(msgNotify);
        }
        notifyDataSetChanged();
    }

    /**
     * 给对应位置的checkbox 赋值
     * @param position
     */
    public void select(int position) {
        boolean isSelected = ResourceHelper.isNull(this.mChbItemArray.get(position, null));
        if(isSelected){
            this.mChbItemArray.put(position, getItem(position));
        }else{
            this.mChbItemArray.remove(position);
        }
        if(ResourceHelper.isNotNull(this.mItemOperateListener)){
            this.mItemOperateListener.onSelect(this.mChbItemArray.size());
        }
        notifyDataSetChanged();
    }

    /**
     * 获取所有选中Item
     */
    public ArrayList<MessageNoticeEntity> getAllSelectedNotify(){
        ArrayList<MessageNoticeEntity> notifies = new ArrayList<MessageNoticeEntity>();
        int key;
        for(int i = 0, size = this.mChbItemArray.size(); i < size; i++){
            key = this.mChbItemArray.keyAt(i);
            MessageNoticeEntity curNotify = this.mChbItemArray.get(key, null);
            if(ResourceHelper.isNotNull(curNotify)){
                notifies.add(curNotify);
            }
        }
        return notifies;
    }

    /**
     * 移除所有选中项
     */
    public void removeALlSelected(){
        int key;
        for(int i = 0 , size = this.mChbItemArray.size(); i < size; i++){
             key = this.mChbItemArray.keyAt(i);
            MessageNoticeEntity curNotify = this.mChbItemArray.get(key, null);
            if(ResourceHelper.isNotNull(curNotify)){
                this.msgNotify.remove(curNotify);
            }
         }
        this.mChbItemArray.clear();
        if(ResourceHelper.isNotNull(this.mItemOperateListener)){
            this.mItemOperateListener.onSelect(this.mChbItemArray.size());
        }
        notifyDataSetChanged();
    }

    /**
     * 获取所有选中的项
     * @return
     */
    public ArrayList<MessageNoticeEntity> getALlSelected(){
        ArrayList<MessageNoticeEntity> notices = new ArrayList<MessageNoticeEntity>();
        int key;
        MessageNoticeEntity notice;
        for(int i = 0, size = this.mChbItemArray.size(); i < size; i++){
            key = this.mChbItemArray.keyAt(i);
            notice = this.mChbItemArray.get(key, null);
            if(ResourceHelper.isNotNull(notice)){
                notices.add(notice);
            }
        }
        return notices;
    }

    /**
     * 全选 取消 全选
     *
     * @param isChecked true 全选 false 全部取消
     */
    public void selectAllorNot(boolean isChecked) {
        if(!isChecked){//全部取消
            this.mChbItemArray.clear();
            this.isRedio = false;
        }else{//全选
            for (int i = 0, size = getCount(); i < size; i++) {
                this.mChbItemArray.put(i, getItem(i));
                this.isRedio = true;
            }
        }
        if(ResourceHelper.isNotNull(this.mItemOperateListener)){
            this.mItemOperateListener.onSelect(this.mChbItemArray.size());
        }
        notifyDataSetChanged();
    }

     /**
     *  根据通知状态给定button点击项事件
     * @param mHolder
     * @param position
     */
    private void showRequestStatus(ViewHolder mHolder, int position){
        final int mCurPosition = position;
        final MessageNoticeEntity notify = getItem(position);
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_agree:
                        //同意
                        if(v.getTag().toString().equals(mCurPosition + "")){
                            notify.setDealStatus(DatabaseConstant.NoticeDealStatus.DEAL_AGREE);
                            if(ResourceHelper.isNotNull(mItemOperateListener)){
                                mItemOperateListener.onAgree(mCurPosition);
                            }
                        }
                        notifyDataSetChanged();
                        break;
                    case R.id.btn_cancel:
                        //取消
                        if(v.getTag().toString().equals(mCurPosition + "")){
                            notify.setDealStatus(DatabaseConstant.NoticeDealStatus.DEAL_REJECT);
                            if(ResourceHelper.isNotNull(mItemOperateListener)){
                                mItemOperateListener.onRefuse(mCurPosition);
                            }
                        }
                        notifyDataSetChanged();
                        break;
                }
            }
        };
        if(notify.getDealStatus() == DatabaseConstant.NoticeDealStatus.UNDEAL){
            mHolder.mTxvHaveAgree.setVisibility(View.GONE);
            mHolder.mBtnAgree.setOnClickListener(mOnClickListener);
            mHolder.mBtnCancel.setOnClickListener(mOnClickListener);
            mHolder.mRelShowButton.setVisibility(View.VISIBLE);
        }else{
            mHolder.mRelShowButton.setVisibility(View.GONE);
            mHolder.mTxvHaveAgree.setVisibility(View.VISIBLE);
            if(notify.getDealStatus() == DatabaseConstant.NoticeDealStatus.DEAL_AGREE){
                mHolder.mTxvHaveAgree.setText(R.string.have_agree);
            }else if(notify.getDealStatus() == DatabaseConstant.NoticeDealStatus.DEAL_REJECT){
                mHolder.mTxvHaveAgree.setText(R.string.have_refuse);
            }
        }
    }

     private class ViewHolder{
        private AsyncImageView mImvHead;
        private TextView mTxvTitle, mTxvFromWhere, mTxvDateTime, mTxvHaveAgree;
        private Button mBtnAgree,mBtnCancel;
        private CheckBox mChbAgree;
        private RelativeLayout mRelShowButton;
    }


    /**
     * 通知项操作监听
     * @author zheng_cz
     * @since 2014年5月4日 下午3:20:14
     */
    public interface NoticeItemOperateListener{
        /**
         * 同意
         * @param position
         */
        void onAgree(int position);
        /**
         * 拒绝
         * @param position
         */
        void onRefuse(int position);

        /**
         * 复选框选择的项数量
         * @param selectCount
         */
         void onSelect(int selectCount);
    }
}
