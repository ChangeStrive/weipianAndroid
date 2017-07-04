package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.UserAppCenterEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用中心适配器
 * Created by Administrator on 2015/8/31.
 */
public class UserAppCenterAdapter extends BaseAdapter {
    private Context mContext;
    private List<UserAppCenterEntity> appCenters = new ArrayList<UserAppCenterEntity>();
    private UserAppCenterListener mListener;

    public UserAppCenterAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return appCenters.size();
    }

    @Override
    public UserAppCenterEntity getItem(int position) {
        UserAppCenterEntity appCenter = appCenters.get(position);
        if(ResourceHelper.isNotNull(appCenter)){
            return appCenter;
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_user_app_center, parent, false);
            mHolder = new ViewHolder();
            mHolder.mImvIcon = (ImageView) convertView.findViewById(R.id.imv_app_center_icon);
            mHolder.mImvOperate = (ImageView) convertView.findViewById(R.id.imv_app_center_operate);
            mHolder.mTxvName = (TextView) convertView.findViewById(R.id.txv_app_center_name);
            mHolder.mTxvDescription = (TextView) convertView.findViewById(R.id.txv_app_center_description);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        UserAppCenterEntity appCenter = getItem(position);
        if(ResourceHelper.isNotNull(appCenter)){
            if(ResourceHelper.isEmpty(appCenter.getUrl())){
                mHolder.mImvIcon.setImageResource(appCenter.getIconGayId());
            }else{
                mHolder.mImvIcon.setImageResource(appCenter.getIconId());
            }
            final int operateResId = appCenter.getOperateResId();
            final int curPosition = position;
            mHolder.mImvOperate.setImageResource(operateResId);
            mHolder.mTxvName.setText(appCenter.getName());
            mHolder.mTxvDescription.setText(appCenter.getDescription());
            mHolder.mImvOperate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ResourceHelper.isNotNull(mListener)){
                        mListener.toGetOrDelApp(curPosition, operateResId);
                    }
                }
            });
        }

        return convertView;
    }

    /**
     * 添加数据源
     * @param appCenters
     */
    public void addItems(List<UserAppCenterEntity> appCenters){
        this.appCenters.clear();
        if(ResourceHelper.isNotNull(appCenters)){
            this.appCenters.addAll(appCenters);
        }
        notifyDataSetChanged();

    }

    /**
     * 添加单个数据
     * @param appCenter
     */
    public void addItem(UserAppCenterEntity appCenter){
        if(ResourceHelper.isNotNull(appCenter)){
            this.appCenters.add(appCenter);
        }
        notifyDataSetChanged();

    }

    /**
     * 应用下载或者删除监听
     * @param mListener
     */
    public void setUserAppCenterListener(UserAppCenterListener mListener){
        this.mListener = mListener;
    }

    private class ViewHolder{
        private ImageView mImvIcon, mImvOperate;
        private TextView mTxvName, mTxvDescription;
    }

    public interface UserAppCenterListener{
        /**
         * 删除应用或下载应用
         * @param position
         * @param operateResId
         */
        void toGetOrDelApp(int position,int operateResId);
    }
}
