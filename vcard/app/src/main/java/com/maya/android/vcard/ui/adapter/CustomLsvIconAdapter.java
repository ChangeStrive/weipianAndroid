package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.CustomLsvIconEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 对话框带Icon 图片适配器
 * Created by Administrator on 2015/9/6.
 */
public class CustomLsvIconAdapter extends BaseAdapter{
    private Context mContext;
    private List<CustomLsvIconEntity> customIcons = new ArrayList<CustomLsvIconEntity>();

    public CustomLsvIconAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return customIcons.size();
    }

    @Override
    public CustomLsvIconEntity getItem(int position) {
        CustomLsvIconEntity  customIcon = this.customIcons.get(position);
        if(ResourceHelper.isNotNull(customIcon)){
            return customIcon;
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_fragment_custom_lsv_icon_item, null);
            mHolder = new ViewHolder();
            mHolder.mImvIcon = (ImageView) convertView.findViewById(R.id.imv_custom_lsv_icon);
            mHolder.mTxvIconName = (TextView) convertView.findViewById(R.id.txv_custom_lsv_icon_name);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        CustomLsvIconEntity  customIcon = getItem(position);
        mHolder.mImvIcon.setImageResource(customIcon.getIconId());
        mHolder.mTxvIconName.setText(customIcon.getBusinessName());
        return convertView;
    }

     /**
     * 添加数据源
     * @param customIcons
     */
    public void addItems(List<CustomLsvIconEntity> customIcons){
        this.customIcons.clear();
        if(ResourceHelper.isNotNull(customIcons)){
            this.customIcons.addAll(customIcons);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder{
        private ImageView mImvIcon;
        private TextView mTxvIconName;
    }
}
