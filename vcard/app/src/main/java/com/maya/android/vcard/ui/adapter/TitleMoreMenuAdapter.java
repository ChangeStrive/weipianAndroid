package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.TitleMoreMenuLsvIconEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * pop标题栏右侧更多适配器
 * Created by Administrator on 2015/9/14.
 */
public class TitleMoreMenuAdapter extends BaseAdapter{
    private static final String TAG = TitleMoreMenuAdapter.class.getName();
    private Context mContext;
    private List<TitleMoreMenuLsvIconEntity> menus = new ArrayList<TitleMoreMenuLsvIconEntity>();
    private ArrayList<Integer> mUnEnablePositionList = null;

    public TitleMoreMenuAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.menus.size();
    }

    @Override
    public TitleMoreMenuLsvIconEntity getItem(int position) {
        TitleMoreMenuLsvIconEntity menu = this.menus.get(position);
        if(ResourceHelper.isNotNull(menu)){
            return menu;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        if(Helper.isNotNull(this.mUnEnablePositionList)){
            if(this.mUnEnablePositionList.contains(position)){
                return false;
            }
        }
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;

        if(ResourceHelper.isNull(convertView)){
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_pop_titile_more_menu, parent, false );
            mHolder = new ViewHolder();
            mHolder.mTxvIconName = (TextView) convertView.findViewById(R.id.txv_menu_lsv_icon_name);
            mHolder.mImvIcon = (ImageView) convertView.findViewById(R.id.imv_menu_lsv_icon);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        TitleMoreMenuLsvIconEntity menu = getItem(position);

        int iconId;
        if(Helper.isNotNull(this.mUnEnablePositionList)){
            if(isEnabled(position)) {
                mHolder.mTxvIconName.setTextColor(this.mContext.getResources().getColor(R.color.color_292929));
                iconId = menu.getIconId();
            }else{
                mHolder.mTxvIconName.setTextColor(this.mContext.getResources().getColor(R.color.color_b7babc));
                iconId = menu.getIconGayId();
            }
        }else{
            iconId =  menu.getIconId();
        }
        mHolder.mTxvIconName.setText(menu.getBusinessName());
        mHolder.mImvIcon.setImageResource(iconId);
        LogHelper.d(TAG, ""+ iconId);
        if(iconId > 0){
            mHolder.mImvIcon.setVisibility(View.VISIBLE);
        }else{
            mHolder.mImvIcon.setVisibility(View.GONE);
        }
        return convertView;
    }

    /**
     * 添加数据源
     * @param menu
     */
    public void addItems(List<TitleMoreMenuLsvIconEntity> menu){
        this.menus.clear();
        if(ResourceHelper.isNotNull(menu)){
            this.menus.addAll(menu);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置不可点击 项
     * @param unEnablePositionList
     */
    public void setUnEnableItems(ArrayList<Integer> unEnablePositionList){
        if(Helper.isNotNull(this.mUnEnablePositionList)){
            this.mUnEnablePositionList.clear();
        }else{
            this.mUnEnablePositionList = new ArrayList<Integer>();
        }
        if(Helper.isNotNull(unEnablePositionList)){
            this.mUnEnablePositionList.addAll(unEnablePositionList);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder{
        private ImageView mImvIcon;
        private TextView mTxvIconName;
    }
}
