package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.ContactGroupBackupEntity;
import com.maya.android.vcard.entity.ContactGroupEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 云端备份/恢复适配器
 * Created by Administrator on 2015/9/23.
 */
public class BackupElvAdapter extends BaseExpandableListAdapter {
    /** 云端备份 **/
    public static final int BACKUP = 30001;
    /** 云端恢复  **/
    public static final int RECOVERY = 30002;
    /** 记录数据类型 **/
    private int codeType;
    /** 选中监听事件 **/
    private BackupElvListener mListener;
    private Context mContext;
    /**父类列表**/
    private ArrayList<ContactGroupEntity> mGroupList = new ArrayList<ContactGroupEntity>();
    /** 父类所含有的子类列表 **/
    private SparseArray<ArrayList<ContactGroupEntity>> mChildList = new SparseArray<ArrayList<ContactGroupEntity>>();
    /** 组项 posotion的checkbox 值集合 **/
    private SparseBooleanArray mGroupChbArr = new SparseBooleanArray();
    /** 对应子项的选中项集合集合 **/
    private SparseArray<SparseArray<ContactGroupEntity>> mChildChbArr = new SparseArray<SparseArray<ContactGroupEntity>>();

    /**
     *
     * @param mContext
     * @param codeType 当前类型
     */
    public BackupElvAdapter(Context mContext, int codeType){
        this.mContext = mContext;
        this.codeType = codeType;
    }

    @Override
    public int getGroupCount() {
        return mGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = 0;
        ArrayList<ContactGroupEntity> curChildList = mChildList.get(groupPosition, null);
        if(ResourceHelper.isNotNull(curChildList)){
            count = curChildList.size();
        }
        return count;
    }

    @Override
    public ContactGroupEntity getGroup(int groupPosition) {
        ContactGroupEntity Entity = mGroupList.get(groupPosition);
        if(ResourceHelper.isNotNull(Entity)){
            return Entity;
        }
        return null;
    }

    @Override
    public ContactGroupEntity getChild(int groupPosition, int childPosition) {
        ArrayList<ContactGroupEntity> curChildList = this.mChildList.get(groupPosition, null);
        if(ResourceHelper.isNotNull(curChildList)){
            ContactGroupEntity curLog = curChildList.get(childPosition);
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderParent mHolderParent = null;
        if(ResourceHelper.isNull(convertView)){
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_elv_backup_parent, parent, false);
            mHolderParent = new ViewHolderParent();
            mHolderParent.mTxvTitle = (TextView) convertView.findViewById(R.id.txv_title);
            mHolderParent.mImvSelect = (ImageView) convertView.findViewById(R.id.chb_is_agree);
            mHolderParent.mImvIcon = (ImageView) convertView.findViewById(R.id.imv_icon);
            convertView.setTag(mHolderParent);
        }else{
            mHolderParent = (ViewHolderParent) convertView.getTag();
        }
        ContactGroupEntity backup = getGroup(groupPosition);
        if (isExpanded) {
            mHolderParent.mImvIcon.setImageResource(R.mipmap.img_backup_log_arrow_down);
        } else {
            mHolderParent.mImvIcon.setImageResource(R.mipmap.img_backup_log_arrow_right);
        }
        if(groupPosition == 2){
            String mPrivate = this.mContext.getString(R.string.frg_backup_details_private);
            String mPrivateNum = this.mContext.getString(R.string.frg_backup_details_num_secret);
            mHolderParent.mTxvTitle.setTextColor(this.mContext.getResources().getColor(R.color.color_b7babc));
            mHolderParent.mImvIcon.setImageResource(R.mipmap.img_backup_log_arrow_right_gay);
            mHolderParent.mTxvTitle.setText(mPrivate + this.mContext.getString(R.string.backup_num, mPrivateNum));
            mHolderParent.mImvSelect.setVisibility(View.GONE);
        }else{
            mHolderParent.mTxvTitle.setTextColor(this.mContext.getResources().getColor(R.color.color_292929));
            mHolderParent.mTxvTitle.setText( backup.getName() + this.mContext.getString(R.string.backup_num, backup.getItemCount()));
            mHolderParent.mImvSelect.setVisibility(View.VISIBLE);
        }

        // 设置改组的勾选状态
        boolean isSelected = this.mGroupChbArr.get(groupPosition, false);
        if(isSelected){
            SparseArray<ContactGroupEntity> childChb =  this.mChildChbArr.get(groupPosition, null);
            if(ResourceHelper.isNull(childChb)){
                childChb = new SparseArray<ContactGroupEntity>();
                this.mChildChbArr.put(groupPosition, childChb);
            }
            if(getChildrenCount(groupPosition) == childChb.size()){
                mHolderParent.mImvSelect.setImageResource(R.mipmap.img_general_is_agree);
            }else{
                mHolderParent.mImvSelect.setImageResource(R.mipmap.img_general_is_agree_gay);
            }
        }else{
            mHolderParent.mImvSelect.setImageResource(R.mipmap.img_general_no_agree);
        }

        final int curGroupPosition = groupPosition;
        mHolderParent.mImvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chanceChildAllChecked(curGroupPosition);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild mHolderChild = null;
        if(ResourceHelper.isNull(convertView)){
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_elv_backup_child, parent, false);
            mHolderChild = new ViewHolderChild();
            mHolderChild.mTxvName = (TextView) convertView.findViewById(R.id.txv_name);
            mHolderChild.mTxvDateTime = (TextView) convertView.findViewById(R.id.txv_datetime);
            mHolderChild.mChbSelect = (CheckBox) convertView.findViewById(R.id.chb_is_agree);
            convertView.setTag(mHolderChild);
        }else {
            mHolderChild = (ViewHolderChild) convertView.getTag();
        }
        ContactGroupEntity backup = getChild(groupPosition, childPosition);
        mHolderChild.mTxvName.setText(backup.getName() + this.mContext.getString(R.string.backup_num, backup.getItemCount()));
        String lastDateTime = backup.getLastUpdateTime();
        if(ResourceHelper.isNotEmpty(lastDateTime)){
            mHolderChild.mTxvDateTime.setVisibility(View.VISIBLE);
            String date = ResourceHelper.getDateTimeFormat(lastDateTime, ResourceHelper.sDateFormat_YYMMDD_HHMM);
            if(this.codeType == BACKUP){//云端备份
                mHolderChild.mTxvDateTime.setText(this.mContext.getString(R.string.backup_last_time, date) );
            }else{//云端恢复
                mHolderChild.mTxvDateTime.setText(this.mContext.getString(R.string.recovery_last_time, date) );
            }
        }else{
            mHolderChild.mTxvDateTime.setVisibility(View.GONE);
        }
        // 给该分组下的子项设置checkbox值
        SparseArray<ContactGroupEntity> childChb =  this.mChildChbArr.get(groupPosition, null);
        if(ResourceHelper.isNull(childChb)){
            childChb = new SparseArray<ContactGroupEntity>();
            this.mChildChbArr.put(groupPosition, childChb);
        }
        mHolderChild.mChbSelect.setChecked(ResourceHelper.isNotNull(childChb.get(childPosition, null)));
        final int curGroupPosition = groupPosition;
        final int curChildPosition = childPosition;
        mHolderChild.mChbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeChildChecked(curGroupPosition,curChildPosition);
            }
        });
        return convertView;
    }
    /**
     * 子项在指定位置是否可选择
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolderChild{
        private TextView mTxvName;
        private TextView mTxvDateTime;
        private CheckBox mChbSelect;
    }

    private class ViewHolderParent{
        private TextView mTxvTitle;
//        private CheckBox mChbSelect;
        private ImageView mImvIcon, mImvSelect;
    }

    /**
     * 添加组集合
     * @param groupList
     */
    public void addGroupItems(ArrayList<ContactGroupEntity> groupList){
        this.mGroupList.clear();
        if(Helper.isNotNull(groupList)){
            this.mGroupList.addAll(groupList);
        }
        notifyDataSetChanged();
    }

    /**
     * 更新子项集合
     */
    public void addChildItems(SparseArray<ArrayList<ContactGroupEntity>> childList){
        this.mChildList.clear();
        if(ResourceHelper.isNotNull(childList)){
            int key;
            for(int i = 0, size = childList.size(); i < size; i++){
                key = childList.keyAt(i);
                ArrayList<ContactGroupEntity> items = childList.get(key, null);
                if(ResourceHelper.isNotNull(items)){
                    this.mChildList.put(key, items);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 设置全部选中
     */
    public void setGroupAllChecked(){
        for(int i = 0, size = getGroupCount(); i < size; i++ ){
            setSelectedGroup(i, true);
        }
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.toChecked(getSelected());
        }
        notifyDataSetChanged();
    }

    /**
     * 设置全部取消
     */
    public void setGroupAllCancel(){
        this.mGroupChbArr.clear();
        this.mChildChbArr.clear();
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.toChecked(getSelected());
        }
        notifyDataSetChanged();
    }



    /**
     * 改变单个分组CheckBox值，及分组下的子集值全部选中或不选中
     * @param groupPosition
     * @return void
     */
    public void chanceChildAllChecked(int groupPosition){
        //得到父类CheckBox值并取相反值
        boolean isChecked = !this.mGroupChbArr.get(groupPosition, false);
        setSelectedGroup(groupPosition, isChecked);
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.toChecked(getSelected());
        }
        notifyDataSetChanged();
    }

    /**
     * 改变单个Item的CheckBox值
     * @param groupPosition
     * @param childPosition
     */
    public void changeChildChecked(int groupPosition, int childPosition){
        SparseArray<ContactGroupEntity> curChbArr = this.mChildChbArr.get(groupPosition, null);
        if(ResourceHelper.isNull(curChbArr)){
            curChbArr = new  SparseArray<ContactGroupEntity>();
            this.mChildChbArr.put(groupPosition, curChbArr);
        }
        boolean isChecked = !ResourceHelper.isNotNull(curChbArr.get(childPosition, null));
        if(isChecked){
            curChbArr.put(childPosition, getChild(groupPosition, childPosition));
        }else{
            curChbArr.remove(childPosition);
        }
        if(curChbArr.size() > 0){
            this.mGroupChbArr.put(groupPosition, true);
        }else{
            this.mGroupChbArr.put(groupPosition, false);
        }
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.toChecked(getSelected());
        }
        notifyDataSetChanged();
    }

    /**
     * 获取分组下 选中的 子项集合
     * @param groupPosition
     * @return ArrayList<ContactGroupBackupEntity>
     */
    public ArrayList<ContactGroupBackupEntity> getSelectedGroupBackupList(int groupPosition){
        ArrayList<ContactGroupBackupEntity> childBackupList = new ArrayList<ContactGroupBackupEntity>();
        SparseArray<ContactGroupEntity> curChbArr = this.mChildChbArr.get(groupPosition, null);
        if(Helper.isNotNull(curChbArr)){
            int key;
            for (int i = 0, size = curChbArr.size(); i < size; i++) {
                key = curChbArr.keyAt(i);
                ContactGroupEntity groupBackupEntity = curChbArr.get(key, null);
                if(ResourceHelper.isNotNull(groupBackupEntity)){
                    childBackupList.add(groupBackupEntity.getGroupBackupEntity());
                }
            }
        }
        return childBackupList;
    }

    /**
     * 选中监听事件
     * @param mListener
     */
    public void setBackupElvListener(BackupElvListener mListener){
        this.mListener = mListener;
    }

    /**
     * 设置分组列表选中事件
     */
    private void setSelectedGroup(int groupPosition, boolean isChecked){
        this.mGroupChbArr.put(groupPosition, isChecked);
        SparseArray<ContactGroupEntity> curChbArr = this.mChildChbArr.get(groupPosition, null);
        if(ResourceHelper.isNull(curChbArr)){
            curChbArr = new SparseArray<ContactGroupEntity>();
            this.mChildChbArr.put(groupPosition, curChbArr);
        }
        if(isChecked){
            for (int i = 0, size = getChildrenCount(groupPosition); i < size; i++) {
                curChbArr.put(i, getChild(groupPosition, i));
            }
        }else{
            curChbArr.clear();
        }
    }

    /**
     * 是否存在选中项
     */
    private boolean getSelected(){
        int key;
        for(int i = 0, size = this.mGroupChbArr.size(); i < size; i++){
            key = this.mGroupChbArr.keyAt(i);
            if(this.mGroupChbArr.get(key, false)){
                return true;
            }
        }
        return false;
    }

    public interface BackupElvListener{
        void toChecked(boolean isSelected);
    }
}
