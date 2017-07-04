package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.result.SwapCardResultEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 名片交换适配器
 * Created by Administrator on 2015/9/16.
 */
public class VCardSwapAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<SwapCardResultEntity> swapCards = new ArrayList<SwapCardResultEntity>();
    /**记录选中项**/
    private SparseArray<SwapCardResultEntity> mSelectedSwapCard = new SparseArray<SwapCardResultEntity>();
    public VCardSwapAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.swapCards.size();
    }

    @Override
    public SwapCardResultEntity getItem(int position) {
        SwapCardResultEntity swapCard = this.swapCards.get(position);
        if(ResourceHelper.isNotNull(swapCard)){
            return swapCard;
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_vcard_swap_member, parent, false);
            mHolder = new ViewHolder();
            mHolder.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_head);
            mHolder.mChbSelect = (CheckBox) convertView.findViewById(R.id.chb_selected);
            mHolder.mTxvName = (TextView) convertView.findViewById(R.id.txv_name);
            mHolder.mTxvJob = (TextView) convertView.findViewById(R.id.txv_job);
            mHolder.mTxvCompany = (TextView) convertView.findViewById(R.id.txv_company);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        SwapCardResultEntity swapCard = getItem(position);
        mHolder.mTxvName.setText(swapCard.getDisplayName());
        mHolder.mTxvCompany.setText(swapCard.getCompanyName());
        mHolder.mTxvJob.setText(swapCard.getJob());
        mHolder.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
        ResourceHelper.asyncImageViewFillUrl(mHolder.mImvHead, ResourceHelper.getImageUrlOnIndex(swapCard.getHeadImg(), 0));
        //设置CheckBox勾选状态
        boolean isSelected = ResourceHelper.isNotNull(this.mSelectedSwapCard.get(position, null));
        mHolder.mChbSelect.setChecked(isSelected);
        return convertView;
    }

    /**
     * 添加数据源
     */
    public void addItems(ArrayList<SwapCardResultEntity> swapCards){
        this.swapCards.clear();
        if(ResourceHelper.isNotNull(swapCards)){
            this.swapCards.addAll(swapCards);
            setSelectedAll(swapCards);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置全部选中
     */
    public void setSelectedAll(ArrayList<SwapCardResultEntity> swapCards){
        for(int i = 0, len = swapCards.size(); i < len; i++){
            this.mSelectedSwapCard.put(i, getItem(i));
        }
        notifyDataSetChanged();
    }

    /**
     * 取消所有选中项
     */
    public void setCancelSelectAll(){
        this.mSelectedSwapCard.clear();
        notifyDataSetChanged();
    }

    /**
     * 设置勾选项
     * @param position
     */
    public void setSelect(int position){
        boolean isSelected = ResourceHelper.isNull(this.mSelectedSwapCard.get(position, null));
        if(isSelected){
            this.mSelectedSwapCard.put(position, getItem(position));
        }else{
            this.mSelectedSwapCard.remove(position);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder{
        private AsyncImageView mImvHead;
        private TextView mTxvName, mTxvJob, mTxvCompany;
        private CheckBox mChbSelect;
    }
}
