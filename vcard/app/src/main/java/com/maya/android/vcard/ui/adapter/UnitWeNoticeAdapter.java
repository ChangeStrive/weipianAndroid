package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.WeNoticeEntity;
import com.maya.android.vcard.util.DateTimeHepler;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 微公告适配器
 * Created by Administrator on 2015/9/2.
 */
public class UnitWeNoticeAdapter extends BaseAdapter{

    private Context mContext;
    private List<WeNoticeEntity> notices = new ArrayList<WeNoticeEntity>();

    public UnitWeNoticeAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.notices.size();
    }

    @Override
    public WeNoticeEntity getItem(int position) {
        WeNoticeEntity notice = this.notices.get(position);
        if(ResourceHelper.isNotNull(notice)){
            return notice;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if(ResourceHelper.isNull(convertView)){
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_we_notice, parent, false);
            mHolder = new ViewHolder();
            mHolder.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_head);
            mHolder.mTxvContent = (TextView) convertView.findViewById(R.id.txv_content);
            mHolder.mTxvDatetime = (TextView) convertView.findViewById(R.id.txv_datetime);
            mHolder.mLilPhotoWall = (LinearLayout) convertView.findViewById(R.id.lil_photo_wall);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        WeNoticeEntity notice = getItem(position);
        mHolder.mImvHead.setTag(notice.getHeadImg());
        mHolder.mImvHead.setImageResource(R.mipmap.img_upload_head);
        ResourceHelper.asyncImageViewFillUrl(mHolder.mImvHead, notice.getHeadImg());
        mHolder.mTxvContent.setText(notice.getContent());
        mHolder.mTxvDatetime.setText(DateTimeHepler.getDateWeekForShow(notice.getPublicTime()));
        addLilChildView(mHolder.mLilPhotoWall, notice.getImage());
        return convertView;
    }

    /**
     * 添加数据源
     * @param notices
     */
    public void addItems(List<WeNoticeEntity> notices){
        this.notices.clear();
        if(ResourceHelper.isNotNull(notices)){
            this.notices.addAll(notices);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加单个数据源
     * @param notice
     */
    public void addItem(WeNoticeEntity notice){
        if(ResourceHelper.isNotNull(notice)){
            this.notices.add(notice);
            notifyDataSetChanged();
        }
    }

    /**
     * 生成子容器及其控件
     * @param mParentLil 需要加载子容器的LinearLayout
     *  @param childGroupNum 子容器个数
     */
    private void addLilChild(LinearLayout mParentLil, int childGroupNum){
        if(ResourceHelper.isNotNull(mParentLil)){
            int childCount = mParentLil.getChildCount();//当前mParentLil中子容器个数
            if(childCount < childGroupNum){
                int addChildNum = childGroupNum - childCount;//还需要添加的子容器个数
                for(int i = 0; i < addChildNum; i++){
                    LinearLayout mChildLil = addChildLil();
                    mChildLil.addView(addChildImv(false));
                    mChildLil.addView(addChildImv(true));
                    mChildLil.addView(addChildImv(true));
                    mParentLil.addView(mChildLil);
                }
             }
        }
     }

    /**
     * 添加子view并展示图片
     * @param mParentLil
     */
    private void addLilChildView(LinearLayout mParentLil, String urls){
        addLilChild(mParentLil, 2);
        if(ResourceHelper.isNotEmpty(urls)){
            mParentLil.setVisibility(View.VISIBLE);
            String[] urlArr = urls.split(Constants.Other.CONTENT_ARRAY_SPLIT);
            int count = urlArr.length;//图片数量
            int childNum = (count - 1) / 3;//需要子容器数量
            int childLilCount = mParentLil.getChildCount();//现有子容器数量
            LinearLayout mChildLil;//当前使用的子容器
            int mImvChildCount;//当前容器中图片控件数量
            AsyncImageView mImvPicture = null;
            int curImvPosition;//当前的第几张图片
            String mImagePath; //当前图片链接
            for(int i = 0; i < childLilCount; i++){
                mChildLil = (LinearLayout) mParentLil.getChildAt(i);
                if(i <= childNum){
                    mChildLil.setVisibility(View.VISIBLE);
                    mImvChildCount = mChildLil.getChildCount();
                    for(int j = 0; j < mImvChildCount; j++){
                        mImvPicture = (AsyncImageView) mChildLil.getChildAt(j);
                        curImvPosition = (3 * i) + j;
                        mImvPicture.setImageResource(R.mipmap.img_upload_head);
                        if(curImvPosition < count){
                            mImvPicture.setVisibility(View.VISIBLE);
                            mImagePath = urlArr[curImvPosition];
                            if(ResourceHelper.isNotEmpty(mImagePath)){
                                mImvPicture.setTag(mImagePath);
                                ResourceHelper.asyncImageViewFillUrl(mImvPicture, mImagePath);
                            }
                        }else{
                            mImvPicture.setVisibility(View.GONE);
                        }
                     }
                }else{
                    mChildLil.setVisibility(View.GONE);
                }
            }
        }else{
            mParentLil.setVisibility(View.GONE);
        }
    }

    /**
     * 生成照片墙的图片控件
     * @param isLeftMargin
     * @return
     */
    private AsyncImageView addChildImv(boolean isLeftMargin){
        AsyncImageView mImvPicture = new AsyncImageView(this.mContext);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams((int)mContext.getResources().getDimension(R.dimen.dimen_70dp),  (int)mContext.getResources().getDimension(R.dimen.dimen_70dp));
        mImvPicture.setScaleType(ImageView.ScaleType.FIT_XY);
        if(isLeftMargin){
            param.leftMargin = (int)mContext.getResources().getDimension(R.dimen.dimen_14dp);
        }
        mImvPicture.setLayoutParams(param);
        mImvPicture.setVisibility(View.GONE);
        return mImvPicture;
    }

    /**
     * 生成子容器
     */
    private LinearLayout addChildLil(){
        LinearLayout mChildLil = new LinearLayout(this.mContext);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int)this.mContext.getResources().getDimension(R.dimen.dimen_70dp));
        param.topMargin = (int)this.mContext.getResources().getDimension(R.dimen.dimen_10dp);
        mChildLil.setLayoutParams(param);
        mChildLil.setOrientation(LinearLayout.HORIZONTAL);
        mChildLil.setVisibility(View.GONE);
        return mChildLil;
    }

    private class ViewHolder{
        private AsyncImageView mImvHead;
        private TextView mTxvContent;
        private TextView mTxvDatetime;
        private LinearLayout mLilPhotoWall;
    }
}
