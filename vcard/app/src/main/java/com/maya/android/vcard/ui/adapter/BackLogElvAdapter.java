package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.BackupLogEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 备份恢复日志管理数据源适配器
 * Created by Administrator on 2015/8/11.
 */
public class BackLogElvAdapter extends BaseExpandableListAdapter{
    private Context mContext;
    private ArrayList<BackupLogEntity> mGroupList = new ArrayList<BackupLogEntity>();
    /** 对应分组的position 的子集列表 **/
    private SparseArray<ArrayList<BackupLogEntity>> mChildList = new SparseArray<ArrayList<BackupLogEntity>>();

    public BackLogElvAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return mGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = 0;
        int childKey = getGroup(groupPosition).getType();
        ArrayList<BackupLogEntity> curChildList = this.mChildList.get(childKey, null);
        if(Helper.isNotNull(curChildList)){
            count = curChildList.size();
        }
        return count;
    }

    @Override
    public BackupLogEntity getGroup(int groupPosition) {
        BackupLogEntity Entity = this.mGroupList.get(groupPosition);
        if(ResourceHelper.isNotNull(Entity)){
            return Entity;
        }
        return null;
    }

    @Override
    public BackupLogEntity getChild(int groupPosition, int childPosition) {
        int childKey = getGroup(groupPosition).getType();
        ArrayList<BackupLogEntity> curChildList = this.mChildList.get(childKey, null);
        if(ResourceHelper.isNotNull(curChildList)){
            BackupLogEntity curLog = curChildList.get(childPosition);
            return curLog;
        }
         return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return Long.valueOf(groupPosition + "" + childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderParent mParentHolder = null;
        if(Helper.isNull(convertView)){
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_elv_backup_log_manage_parent,null);
            mParentHolder = new ViewHolderParent();
            mParentHolder.mTxvParentTitle = (TextView) convertView.findViewById(R.id.txv_item_elv_log_title);
            mParentHolder.mTxvParentNum = (TextView) convertView.findViewById(R.id.txv_item_log_num);
            mParentHolder.mImvIcon = (ImageView) convertView.findViewById(R.id.imv_icon);
            convertView.setTag(mParentHolder);
        }else{
            mParentHolder = (ViewHolderParent) convertView.getTag();
        }
        // 判断分组是否展开，分别传入不同的图片资源
        if (isExpanded) {
            mParentHolder.mImvIcon.setImageResource(R.mipmap.img_backup_log_arrow_down);
        } else {
            mParentHolder.mImvIcon.setImageResource(R.mipmap.img_backup_log_arrow_right);
        }
        BackupLogEntity curGroup = this.mGroupList.get(groupPosition);
         if(groupPosition == 2){
            String mPrivate = this.mContext.getString(R.string.frg_backup_details_private);
            mParentHolder.mTxvParentNum.setText(" (" + this.mContext.getString(R.string.frg_backup_details_num_secret) + ")");
            mParentHolder.mTxvParentTitle.setTextColor(this.mContext.getResources().getColor(R.color.color_b7babc));
            mParentHolder.mTxvParentTitle.setText(mPrivate);
            mParentHolder.mTxvParentNum.setTextColor(this.mContext.getResources().getColor(R.color.color_b7babc));
            mParentHolder.mImvIcon.setImageResource(R.mipmap.img_backup_log_arrow_right_gay);
        }else{
            mParentHolder.mTxvParentTitle.setText(curGroup.getName());
            mParentHolder.mTxvParentNum.setText(" (" + curGroup.getNum() + ")");
            mParentHolder.mTxvParentNum.setTextColor(this.mContext.getResources().getColor(R.color.color_292929));
            mParentHolder.mTxvParentTitle.setTextColor(this.mContext.getResources().getColor(R.color.color_292929));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild mChildHolder = null;
        if(Helper.isNull(convertView)){
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_elv_backup_log_manage_child,null);
            mChildHolder = new ViewHolderChild();
            mChildHolder.mTxvChildDateTime = (TextView) convertView.findViewById(R.id.txv_item_elv_log_datetime);
            mChildHolder.mTxvChildVcardNum = (TextView) convertView.findViewById(R.id.txv_item_backup_log_num);
            convertView.setTag(mChildHolder);
        }else{
            mChildHolder = (ViewHolderChild) convertView.getTag();
        }
        // 当前子项
        BackupLogEntity curChild = getChild(groupPosition, childPosition);
        if(ResourceHelper.isNotNull(curChild)){
            String name =this.mContext.getResources().getString(R.string.frg_backup_log_item_child_manage_num);
            String unit = this.mContext.getResources().getString(R.string.frg_piece);
            String timeName =this.mContext.getResources().getString(R.string.frg_backup_log_item_child_manage_time);
            mChildHolder.mTxvChildVcardNum.setText(name + curChild.getNum() + unit + " (" + curChild.getSize() + ")");
            mChildHolder.mTxvChildDateTime.setText(timeName + curChild.getTime());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolderParent{
        private TextView mTxvParentTitle, mTxvParentNum;
        private ImageView mImvIcon;
    }
    private class ViewHolderChild{
        private TextView mTxvChildDateTime, mTxvChildVcardNum;
    }

    /**
     * 添加分组集合
     */
    public void addGroupItems(ArrayList<BackupLogEntity> groupList){
        this.mGroupList.clear();
        if(Helper.isNotNull(groupList)){
            this.mGroupList.addAll(groupList);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加分组下的 子集
     * @param childList
     */
    public void addChildItems(SparseArray<ArrayList<BackupLogEntity>> childList){
        this.mChildList.clear();
        if(ResourceHelper.isNotNull(childList)){
            int key;
            for(int i = 0, size = childList.size(); i < size; i++){
                key = childList.keyAt(i);
                ArrayList<BackupLogEntity> items = childList.get(key, null);
                if(ResourceHelper.isNotNull(items)){
                    this.mChildList.put(key, items);
                }
            }
        }
        notifyDataSetChanged();
    }
}
