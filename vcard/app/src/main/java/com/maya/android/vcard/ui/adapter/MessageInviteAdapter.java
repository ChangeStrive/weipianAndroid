package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.LogHelper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.VCardAndBookEntity;
import com.maya.android.vcard.ui.widget.PinnedSectionListView;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 好友推荐适配器
 * Created by Administrator on 2015/9/9.
 */
public class MessageInviteAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private static final String TAG = MessageInviteAdapter.class.getSimpleName();
    /**布局控件**/
    private static final int VIEW_TYPE_COUNT = 2;
    /**分类标签 Item布局 **/
    private static final int VIEW_TYPE_LABEL = 1;
    /**成员 Item布局 **/
    private static final int VIEW_TYPE_MEMBER = 0;

    /*定义常量*/
    private static final String TAG_USER_FROM_VCARD = "vcard";

    private Context mContext;
    private ArrayList<VCardAndBookEntity> books = new ArrayList<VCardAndBookEntity>();
    private MsgInviteItemOperateListener mListener;

    public MessageInviteAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        LogHelper.d(TAG, "count:" + this.books.size());
        return this.books.size();
    }

    @Override
    public VCardAndBookEntity getItem(int position) {
        VCardAndBookEntity book = this.books.get(position);
        if(ResourceHelper.isNotNull(book)){
            return book;
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
        VCardAndBookEntity book = getItem(position);
        if(book.getType() == VCardAndBookEntity.SECTION){
            //分类标签
            return VIEW_TYPE_LABEL;
        }
        //成员Item
        return VIEW_TYPE_MEMBER;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VCardAndBookEntity book = getItem(position);
        LogHelper.d(TAG, "position:" + position);
        if(position == 45){
            LogHelper.d(TAG, TAG);
        }
        ViewHolderLabel mHolderLabel = null;
        ViewHolderMember mHolderMember = null;
        int type = book.getType();
        if(ResourceHelper.isNull(convertView)){
            switch (type){
                case VIEW_TYPE_LABEL: //分类标签（悬浮窗）
                    convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_msg_invite_txv, parent, false);
                    mHolderLabel = new ViewHolderLabel();
                    mHolderLabel.mTxvLable = (TextView) convertView.findViewById(R.id.txv_label);
                    convertView.setTag(mHolderLabel);
                    break;
                case VIEW_TYPE_MEMBER://成员Item
                    convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_msg_invite_member, parent, false);
                    mHolderMember = new ViewHolderMember();
                    mHolderMember.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_head);
                    mHolderMember.mTxvName = (TextView) convertView.findViewById(R.id.txv_name);
                    mHolderMember.mTxvWait = (TextView) convertView.findViewById(R.id.txv_wait);
                    mHolderMember.mBtnInvite = (Button) convertView.findViewById(R.id.btn_invite);
                    mHolderMember.mImvSign = (ImageView) convertView.findViewById(R.id.imv_vcard_sign);
                    mHolderMember.mViewDivider = (View) convertView.findViewById(R.id.view_divider);
                    convertView.setTag(mHolderMember);
                    break;
            }
        }else{
            switch (type){
                case VIEW_TYPE_LABEL: //分类标签（悬浮窗）
                    mHolderLabel = (ViewHolderLabel) convertView.getTag();
                    break;
                case VIEW_TYPE_MEMBER://成员Item
                    mHolderMember = (ViewHolderMember) convertView.getTag();
                    break;
            }
        }

        switch (type){
            case VIEW_TYPE_LABEL: //分类标签（悬浮窗）
                mHolderLabel.mTxvLable.setText(book.getText());
                break;
            case VIEW_TYPE_MEMBER://成员Item
                //分割线处理
                if(position == getCount() - 1 || getItem(position + 1).type == VCardAndBookEntity.SECTION){
                    mHolderMember.mViewDivider.setVisibility(View.GONE);
                }else{
                    mHolderMember.mViewDivider.setVisibility(View.VISIBLE);
                }
                mHolderMember.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
                mHolderMember.mTxvName.setText(book.getDisplayName());
                int result;
                final int mCurPosition = position;
                if(TAG_USER_FROM_VCARD.equals(book.Flag)) {//微片通讯录
                    ResourceHelper.asyncImageViewFillUrl(mHolderMember.mImvHead, ResourceHelper.getImageUrlOnIndex(book.getHeadImg(), 0));
                    mHolderMember.mImvSign.setVisibility(View.VISIBLE);
                    result = PreferencesHelper.getInstance().getInt(Constants.Preferences.KEY_RECOMMEND_FRIEND_VCARD_USER_ID + book.getAccountId(), -1);
                    if(result == -1){
                        mHolderMember.mBtnInvite.setVisibility(View.VISIBLE);
                        mHolderMember.mBtnInvite.setText(R.string.swap_cloud);
                        mHolderMember.mTxvWait.setVisibility(View.GONE);
                        mHolderMember.mBtnInvite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ResourceHelper.isNotNull(mListener)) {
                                    mListener.onVCardSwap(mCurPosition);
                                }
                            }
                        });
                    }else{
                        mHolderMember.mBtnInvite.setVisibility(View.GONE);
                        mHolderMember.mTxvWait.setVisibility(View.VISIBLE);
                        if(result == 0){
                            mHolderMember.mTxvWait.setText(R.string.wait_verify);
                        }
                    }

                }else{//手机本地通讯录
                    if(ResourceHelper.isNotNull(book.getHeadPhoto())){
                        mHolderMember.mImvHead.setImageBitmap(book.getHeadPhoto());
                    }
                    mHolderMember.mImvSign.setVisibility(View.GONE);
                    result = PreferencesHelper.getInstance().getInt(Constants.Preferences.KEY_RECOMMEND_FRIEND_BOOK_CONTACT_ID + book.getContactId() + CurrentUser.getInstance().getVCardNo(), -1);
                    if(result == -1){
                        mHolderMember.mBtnInvite.setVisibility(View.VISIBLE);
                        mHolderMember.mBtnInvite.setText(R.string.short_message_recommend);
                        mHolderMember.mTxvWait.setVisibility(View.GONE);

                        mHolderMember.mBtnInvite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ResourceHelper.isNotNull(mListener)) {
                                    mListener.onRecommend(mCurPosition);
                                }
                            }
                        });
                    }else{
                        mHolderMember.mTxvWait.setVisibility(View.VISIBLE);
                        mHolderMember.mTxvWait.setText(R.string.wait_join);
                    }
                }
             break;
        }

        return convertView;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        //判断是否悬浮
        return viewType == VCardAndBookEntity.SECTION;
    }

    /**
     * 更新数据
     * @param books
     */
    public void addItems(ArrayList<VCardAndBookEntity> books){
        addItems(books, false);
    }

    /**
     *  更新数据源(更新或追加)
     * @param books
     * @param isAppend true = 追加数据， false = 替换数据
     */
    public void addItems(ArrayList<VCardAndBookEntity> books, boolean isAppend){
        if(isAppend){
            if(ResourceHelper.isNotNull(books)){
                this.books.addAll(books);
            }
        }else{
            this.books.clear();
            if(ResourceHelper.isNotNull(books)){
                this.books.addAll(books);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 云端交换、短信推荐监听
     * @param mListener
     */
    public void setMsgInviteItemOperateListener(MsgInviteItemOperateListener mListener){
        this.mListener = mListener;
    }

    /**
     * 分类标签
     */
    private class ViewHolderLabel{
        private TextView mTxvLable;
    }

    public interface MsgInviteItemOperateListener{
        /**
         * 短信推荐
         *  @param position
         */
        void onRecommend(int position);
        /**
         * 云端交换
         * @param position
         */
        void onVCardSwap(int position);
    }

    private class ViewHolderMember{
        private AsyncImageView mImvHead;
        private ImageView mImvSign;
        private Button mBtnInvite;
        private TextView mTxvName, mTxvWait;
        private View mViewDivider;
    }
}
