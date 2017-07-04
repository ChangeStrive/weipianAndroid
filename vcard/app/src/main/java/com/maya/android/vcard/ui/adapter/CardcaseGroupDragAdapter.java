package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.dao.ContactVCardDao;
import com.maya.android.vcard.entity.ContactGroupEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 分组管理适配器
 * Created by Administrator on 2015/10/19.
 */
public class CardcaseGroupDragAdapter extends BaseAdapter{
    private static final String TAG = CardcaseGroupDragAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<ContactGroupEntity> mGroupList = new ArrayList<ContactGroupEntity>();
    private ContactGroupItemOperateListener listener;

    public CardcaseGroupDragAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.mGroupList.size();
    }

    @Override
    public ContactGroupEntity getItem(int position) {
        ContactGroupEntity item = this.mGroupList.get(position);
        if(ResourceHelper.isNotNull(item)){
            return item;
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_group_drag, parent, false);
            mHolder = new ViewHolder();
            mHolder.mImvIcon = (ImageView) convertView.findViewById(R.id.imv_icon);
            mHolder.mImvDrag = (ImageView) convertView.findViewById(R.id.imv_drag);
            mHolder.mTxvTitle = (TextView) convertView.findViewById(R.id.txv_title);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        ContactGroupEntity group = getItem(position);
        mHolder.mTxvTitle.setText(group.getName());
        Bitmap icon = ResourceHelper.getImgBitmap(this.mContext, group.getIconName());
        if(group.isEnable()){
            mHolder.mImvDrag.setVisibility(View.VISIBLE);
            icon = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.img_general_delete);
        }else{
            mHolder.mImvDrag.setVisibility(View.GONE);
        }
        mHolder.mImvIcon.setImageBitmap(icon);
        final int curPosition = position;
        mHolder.mImvIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ResourceHelper.isNotNull(listener)){
                    listener.onDeleteClick(curPosition);
                }
            }
        });
        return convertView;
    }

    /**
     * 更新数据源
     * @param items
     */
    public void addItems(ArrayList<ContactGroupEntity> items){
        this.mGroupList.clear();
        if(ResourceHelper.isNotNull(items)){
            this.mGroupList.addAll(items);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加分组
     * @param newName
     */
    public void addItem(String newName){
        ContactGroupEntity newGroup = ContactVCardDao.getInstance().addGroup(newName, 0);
        if(ResourceHelper.isNotNull(newGroup)){
            this.mGroupList.add(newGroup);
        }
        notifyDataSetChanged();
    }

    /**
     * 修改分组名称
     *
     * @param position
     * @param newName
     */
    public void updateItem(int position, String newName) {
        int result;
        ContactGroupEntity selectGroup = getItem(position);

        boolean resUp;
        try {
            resUp = ContactVCardDao.getInstance().updateGroup(selectGroup, newName);
            if (resUp) {
                result = R.string.groups_edit_name_success;
                selectGroup.setName(newName);
                mGroupList.set(position, selectGroup);
                notifyDataSetChanged();
            } else{
                result = R.string.groups_edit_name_failed;
            }
            ActivityHelper.showToast(result);
        } catch (Exception e) {
            Log.e(TAG, "更改分组名称异常", e);
        }
    }

    /**
     * 拖动图标 实时更新位置
     *
     * @param start
     * @param end
     */
    public void exchange(int start, int end) {
        ContactGroupEntity selectGp = getItem(start);
        ContactGroupEntity endGp = getItem(end);
        int result;
        boolean resbl = ContactVCardDao.getInstance().sortGroup(selectGp, endGp);
        if (resbl) {
            result = R.string.groups_sort_success;
            mGroupList.remove(start);
            mGroupList.add(end, selectGp);
        } else{
            result = R.string.groups_sort_failed;
        }
        ActivityHelper.showToast(result);
        this.notifyDataSetChanged();
    }

    /**
     * 删除Item
     * @param delGroup
     */
    public void removeItem(ContactGroupEntity delGroup){
        this.mGroupList.remove(delGroup);
        notifyDataSetChanged();
    }

    /**
     * 适配器监听
     * @param listener
     */
    public void setContactGroupItemOperateListener(ContactGroupItemOperateListener listener){
        this.listener = listener;
    }

    private class ViewHolder{
        private ImageView mImvIcon, mImvDrag;
        private TextView mTxvTitle;
    }

    /**
     * 分作操作回调接口
     * @author zheng_cz
     * @since 2014年4月15日 上午10:13:33
     */
    public interface ContactGroupItemOperateListener{
        /**
         * 删除操作
         * @param position
         */
        void onDeleteClick(int position);
    }
}
