package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.LoginSimpleInfoEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 账号管理适配器
 * Created by Administrator on 2015/8/12.
 */
public class AccountManageAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<LoginSimpleInfoEntity> mEntitys = new ArrayList<LoginSimpleInfoEntity>();
    /** 当前用户微片号 **/
    private String curUserVCardNo;

    public AccountManageAdapter(Context mContext){
        this.mContext = mContext;
        this.curUserVCardNo = CurrentUser.getInstance().getVCardNo();
    }
    @Override
    public int getCount() {
        return mEntitys.size();
    }

    @Override
    public LoginSimpleInfoEntity getItem(int position) {
        LoginSimpleInfoEntity mEntity = mEntitys.get(position);
        if(Helper.isNotNull(mEntity)){
            return mEntity;
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
        if(Helper.isNull(convertView)){
            convertView =  LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_account_manage, null);
            mHolder = new ViewHolder();
            mHolder.mImvHeadImg = (AsyncImageView) convertView.findViewById(R.id.imv_account_manage_headimg);
            mHolder.mImvCurrentAccount = (ImageView) convertView.findViewById(R.id.imv_account_manage_current_account);
            mHolder.mTxvUserName = (TextView) convertView.findViewById(R.id.txv_account_manage_username);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        LoginSimpleInfoEntity entity = getItem(position);
        if(ResourceHelper.isNotNull(entity) && ResourceHelper.isNotEmpty(entity.getVcardNo())){
            String showName = entity.getDisplayName();
            if(Helper.isEmpty(showName)){
                mHolder.mTxvUserName.setText(R.string.frg_account_manage_username_no_add);
            }else{
                mHolder.mTxvUserName.setText(showName);
            }
            if(ResourceHelper.isNotEmpty(this.curUserVCardNo) && entity.getVcardNo().equals(this.curUserVCardNo)){
                mHolder.mImvCurrentAccount.setVisibility(View.VISIBLE);
            }else{
                mHolder.mImvCurrentAccount.setVisibility(View.GONE);
            }
            mHolder.mImvHeadImg.setDefaultImageResId(R.mipmap.img_upload_head);
            ResourceHelper.asyncImageViewFillUrl(mHolder.mImvHeadImg, ResourceHelper.getImageUrlOnIndex(entity.getHeadImg(), 0));

        }
        return convertView;
    }

    /**
     * 加载所有成员数据
     * @param items
     * @return
     */
     public boolean addItems(ArrayList<LoginSimpleInfoEntity> items){
        boolean result = false;
         this.mEntitys.clear();
        if(Helper.isNotNull(items)){
            result = this.mEntitys.addAll(items);
        }
        this.notifyDataSetChanged();
        return result;
    }
    /**
     * 移除成员
     * @param item
     */
    public void removeItem(LoginSimpleInfoEntity item){
        this.mEntitys.remove(item);
        notifyDataSetChanged();
    }

    private class ViewHolder{
        private AsyncImageView mImvHeadImg;
        private ImageView mImvCurrentAccount;
        private TextView mTxvUserName;
    }
}
