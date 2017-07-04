package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.result.UnitMemberResultEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 单位认证设置管理员
 * Created by Administrator on 2015/9/7.
 */
public class UnitAuthGridAdapter extends BaseAdapter{
    private Context mContext;
    private List<UnitMemberResultEntity> members = new ArrayList<UnitMemberResultEntity>();

    public UnitAuthGridAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public UnitMemberResultEntity getItem(int position) {
        UnitMemberResultEntity member = members.get(position);
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
        UnitMemberResultEntity member = getItem(position);
        String headPath = member.getHeadImg();
        if (position == getCount() - 1 && ResourceHelper.isEmpty(headPath)) {
            mHolder.mImvHead .setDefaultImageResId(R.mipmap.img_add_frame);
        } else {
            mHolder.mImvHead .setDefaultImageResId(R.mipmap.img_upload_head);
            ResourceHelper.asyncImageViewFillUrl(mHolder.mImvHead, headPath);
        }
        mHolder.mTxvName.setText(member.getDisplayName());

        return convertView;
    }

    /**
     * 更新数据源
     * @param members
     */
    public void addItems(List<UnitMemberResultEntity> members){
        this.members.clear();
        if(Helper.isNotNull(members)){
            this.members.addAll(members);
            insertAddpicture();
         }
        notifyDataSetChanged();
    }

    /**
     * 添加单个数据
     * @param member
     */
    public void addItem(UnitMemberResultEntity member){
        this.members.add(getCount() - 1, member);
        if(getCount() > Constants.UNIT.SETTING_ADMINISTRATOR_COUNT){
            this.members.remove(getCount() - 1);
        }
        notifyDataSetChanged();
    }

    /**
     * 删除选中的Item项
     * @param position
     */
    public void delItem(int position){
        if(position != -1){
            this.members.remove(position);
        }
        notifyDataSetChanged();
    }

    /**
     * 插入添加图片
     */
    private void insertAddpicture(){
        if(getCount() < Constants.UNIT.SETTING_ADMINISTRATOR_COUNT){//默认最多4个管理员
            UnitMemberResultEntity member = new UnitMemberResultEntity();
            member.setNickName(this.mContext.getString(R.string.common_add));
            member.setCardId(-1);
            this.members.add(member);
        }
    }

    private class ViewHolder{
        private AsyncImageView mImvHead;
        private TextView mTxvName;
    }

}
