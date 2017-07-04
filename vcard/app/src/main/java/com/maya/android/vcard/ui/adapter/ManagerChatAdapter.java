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
import com.maya.android.vcard.entity.ManagerChatEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 聊天记录管理适配器
 * Created by Administrator on 2015/11/11.
 */
public class ManagerChatAdapter extends BaseAdapter{
    private Context mContxt;
    private ArrayList<ManagerChatEntity> managerChats = new ArrayList<ManagerChatEntity>();
    private SparseArray<ManagerChatEntity> chbManagerChats = new SparseArray<ManagerChatEntity>();
    private ManagerChatListener mListener;

    public ManagerChatAdapter(Context mContxt){
        this.mContxt = mContxt;
    }

    @Override
    public int getCount() {
        return this.managerChats.size();
    }

    @Override
    public ManagerChatEntity getItem(int position) {
        ManagerChatEntity chat = this.managerChats.get(position);
        if(ResourceHelper.isNotNull(chat)){
            return chat;
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
            convertView = LayoutInflater.from(this.mContxt).inflate(R.layout.item_lsv_manager_chat, parent, false);
            mHolder = new ViewHolder();
            mHolder.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_head);
            mHolder.mTxvSize = (TextView) convertView.findViewById(R.id.txv_size);
            mHolder.mTxvName = (TextView) convertView.findViewById(R.id.txv_name);
            mHolder.mChbSelected = (CheckBox) convertView.findViewById(R.id.chb_selected);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        ManagerChatEntity chat = getItem(position);
        if(ResourceHelper.isNotNull(chat)){
            mHolder.mImvHead.setDefaultImageResId(R.mipmap.img_default_upload_head);
            ResourceHelper.asyncImageViewFillPath(mHolder.mImvHead, ResourceHelper.getImageUrlOnIndex(chat.getHeadImg(), 0));
            mHolder.mTxvName.setText(chat.getDisplayName());
            mHolder.mTxvSize.setText(chat.getSize());
            boolean isChecked = ResourceHelper.isNotNull(this.chbManagerChats.get(position, null));
            mHolder.mChbSelected.setChecked(isChecked);
            final int curPosition = position;
            mHolder.mChbSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeSelected(curPosition);
                }
            });
        }
        return convertView;
    }

    /**
     * 添加数据源
     */
    public void addItems(ArrayList<ManagerChatEntity> managerChats){
        this.managerChats.clear();
        this.chbManagerChats.clear();
        if(ResourceHelper.isNotNull(managerChats)){
            this.managerChats.addAll(managerChats);
        }
        notifyDataSetChanged();
    }

    /**
     * 取消所有选中项
     */
    public void cancelSelectedAll(){
        this.chbManagerChats.clear();
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.toSelectedNum(this.chbManagerChats.size());
        }
        notifyDataSetChanged();
    }

    /**
     * 全部选中
     */
    public void selectedAll(){
        for(int i= 0, size = getCount(); i < size; i++){
            this.chbManagerChats.put(i,getItem(i));
        }
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.toSelectedNum(this.chbManagerChats.size());
        }
        notifyDataSetChanged();
    }

    /**
     * 单个选中状态发生改变
     */
    public void changeSelected(int position){
        boolean isChecked = ResourceHelper.isNull(this.chbManagerChats.get(position, null));
        if(isChecked){
            this.chbManagerChats.put(position, getItem(position));
        }else{
            this.chbManagerChats.remove(position);
        }
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.toSelectedNum(this.chbManagerChats.size());
        }
        notifyDataSetChanged();
    }

    /**
     * 获取选中的项
     */
    public SparseArray<ManagerChatEntity> getSelected(){
        return this.chbManagerChats;
    }

    /**
     * 删除选中的项
     */
    public void deleteSelected(){
        for(int i = 0, size = this.chbManagerChats.size(); i < size; i++){
            int key = this.chbManagerChats.keyAt(i);
            ManagerChatEntity item = this.chbManagerChats.get(key, null);
            if(ResourceHelper.isNotNull(item)){
                this.managerChats.remove(item);
            }
        }
        this.chbManagerChats.clear();
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.toSelectedNum(this.chbManagerChats.size());
        }
        notifyDataSetChanged();
    }

    /**
     * 选中状态监听
     * @param mListener
     */
    public void setManagerChatListener(ManagerChatListener mListener){
        this.mListener = mListener;
    }

    public interface ManagerChatListener{
        void toSelectedNum(int count);
    }

    private class ViewHolder{
        private AsyncImageView mImvHead;
        private TextView mTxvSize, mTxvName;
        private CheckBox mChbSelected;
    }
}
