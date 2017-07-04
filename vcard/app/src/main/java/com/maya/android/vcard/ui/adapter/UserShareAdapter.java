package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hp.hpl.sparta.Text;
import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.UserShareEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 用户分享适配器
 * Created by Administrator on 2015/9/18.
 */
public class UserShareAdapter extends BaseAdapter{
    private ArrayList<UserShareEntity> userShares = new ArrayList<UserShareEntity>();
    private Context mContext;
    public UserShareAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.userShares.size();
    }

    @Override
    public UserShareEntity getItem(int position) {
        UserShareEntity userShare = this.userShares.get(position);
        if(ResourceHelper.isNotNull(userShare)){
            return userShare;
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_user_share, parent, false);
            mHolder = new ViewHolder();
            mHolder.mImvIcon = (ImageView) convertView.findViewById(R.id.imv_icon);
            mHolder.mTxvShareName = (TextView) convertView.findViewById(R.id.txv_share_name);
            mHolder.mTxvShareContent = (TextView) convertView.findViewById(R.id.txv_share_content);
            mHolder.mv = convertView.findViewById(R.id.v);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        UserShareEntity userShare = getItem(position);
        if(position<getCount()-1){
            UserShareEntity item = getItem(position+1);
            String type1 = item.getType();//下一个item
            String type2 = userShare.getType();//本个item
            if(type1.equals(type2)){
                mHolder.mv.setVisibility(View.INVISIBLE);
            }
        }
        mHolder.mImvIcon.setImageResource(userShare.getDrawableId());
        mHolder.mTxvShareName.setText(userShare.getShareNameId());
        mHolder.mTxvShareContent.setText(userShare.getShareContent());
        return convertView;
    }

    /**
     * 添加数据源
     */
    public void addItems(ArrayList<UserShareEntity> userShares){
        this.userShares.clear();
        if(ResourceHelper.isNotNull(userShares)){
            this.userShares.addAll(userShares);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder{
        private ImageView mImvIcon;
        private TextView mTxvShareName;
        private TextView mTxvShareContent;
        private View mv;
    }
}
