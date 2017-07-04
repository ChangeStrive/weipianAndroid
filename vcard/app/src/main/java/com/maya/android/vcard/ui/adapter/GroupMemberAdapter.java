package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.entity.result.CircleGroupMemberResultEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 群聊天设置中聊天成员适配器
 * Created by Administrator on 2015/11/12.
 */
public class GroupMemberAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<CircleGroupMemberResultEntity> groupMembers = new ArrayList<CircleGroupMemberResultEntity>();
    private String addName, deleteName;

    public GroupMemberAdapter(Context mContext){
        this.mContext = mContext;
        this.addName = mContext.getResources().getString(R.string.common_add);
        this.deleteName = mContext.getResources().getString(R.string.delete);
    }

    @Override
    public int getCount() {
        return this.groupMembers.size();
    }

    @Override
    public CircleGroupMemberResultEntity getItem(int position) {
        CircleGroupMemberResultEntity member = this.groupMembers.get(position);
        if(ResourceHelper.isNotNull(member)){
            return member;
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_grid_unit_auth, parent, false);
            mHolder = new ViewHolder();
            mHolder.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_head);
            mHolder.mTxvName = (TextView) convertView.findViewById(R.id.txv_name);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        CircleGroupMemberResultEntity member = getItem(position);
        if(ResourceHelper.isNotNull(member)){
             String headImg = member.getHeadImg();
            if(ResourceHelper.isNotEmpty(headImg)){
                mHolder.mImvHead.setDefaultImageResId(R.mipmap.img_default_upload_head);
                ResourceHelper.asyncImageViewFillUrl(mHolder.mImvHead, ResourceHelper.getImageUrlOnIndex(member.getHeadImg(), 0));
             }
             String diaplayName =  member.getDisplayName();
            if(ResourceHelper.isNotEmpty(diaplayName)){
                if(member.getAccountId() == R.string.common_add && diaplayName.equals(this.addName)){
                    mHolder.mImvHead .setDefaultImageResId(R.mipmap.img_add_frame);
                }
//                else if(member.getAccountId() == R.string.delete && diaplayName.equals(this.deleteName)){
//                    mHolder.mImvHead .setDefaultImageResId(R.mipmap.img_del_frame);
//                }
                mHolder.mTxvName.setText(member.getDisplayName());
            }
            if(member.getRole() == DatabaseConstant.Role.CREATER){
                //创建者
                mHolder.mTxvName.setTextColor(this.mContext.getResources().getColor(R.color.color_fc0200));
            }else if(member.getRole() == DatabaseConstant.Role.ADMIN){
                mHolder.mTxvName.setTextColor(this.mContext.getResources().getColor(R.color.color_fe6030));
            }else{
                mHolder.mTxvName.setTextColor(this.mContext.getResources().getColor(R.color.color_292929));
            }

         }
        return convertView;
    }

    /**
     * 添加数据
     * @param groupMembers
     */
    public void addItems(ArrayList<CircleGroupMemberResultEntity> groupMembers){
        this.groupMembers.clear();
        if(ResourceHelper.isNotNull(groupMembers)){
            this.groupMembers.addAll(groupMembers);
            CircleGroupMemberResultEntity memberAdd  = new CircleGroupMemberResultEntity();
            memberAdd.setDisplayName(this.addName);
            memberAdd.setAccountId(R.string.common_add);
            this.groupMembers.add(memberAdd);
//            CircleGroupMemberResultEntity memberDelete  = new CircleGroupMemberResultEntity();
//            memberDelete.setDisplayName(this.deleteName);
//            memberDelete.setAccountId(R.string.delete);
//            this.groupMembers.add(memberDelete);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置成员权限
     * @param position
     * @param role
     */
    public void setAdministrator(int position, int role){
        CircleGroupMemberResultEntity member = getItem(position);
        if(ResourceHelper.isNotNull(member)){
            member.setRole(role);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除成员
     * @param member
     */
    public void removeItem(CircleGroupMemberResultEntity member ){
        if(ResourceHelper.isNotNull(member)){
            this.groupMembers.remove(member);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder{
        private AsyncImageView mImvHead;
        private TextView mTxvName;
    }
}
