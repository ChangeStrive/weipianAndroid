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
import com.maya.android.vcard.entity.result.UserAttAndFansEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注/粉丝适配器
 * Created by Administrator on 2015/8/31.
 */
public class UserAttAndFansAdapter extends BaseAdapter {
    private Context mContext;
    private List<UserAttAndFansEntity> attAndFanss = new ArrayList<UserAttAndFansEntity>();
    private StatusListener mListener;

    public UserAttAndFansAdapter(Context mContext){
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return attAndFanss.size();
    }

    @Override
    public UserAttAndFansEntity getItem(int position) {
        UserAttAndFansEntity attAndFans = attAndFanss.get(position);
        if(ResourceHelper.isNotNull(attAndFans)){
            return attAndFans;
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_att_and_fans, parent, false);
            mHolder = new ViewHolder();
            mHolder.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_head);
            mHolder.mImvBus = (ImageView) convertView.findViewById(R.id.imv_bus);
            mHolder.mImvGroup = (ImageView) convertView.findViewById(R.id.imv_group);
            mHolder.mImvPartner = (ImageView) convertView.findViewById(R.id.imv_partner);
            mHolder.mTxvName = (TextView) convertView.findViewById(R.id.txv_name);
            mHolder.mTxvCompany = (TextView) convertView.findViewById(R.id.txv_company);
            mHolder.mTxvJob = (TextView) convertView.findViewById(R.id.txv_position);
            mHolder.mTxvState = (TextView) convertView.findViewById(R.id.txv_state);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }

        final UserAttAndFansEntity attAndFans = getItem(position);
        mHolder.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
        ResourceHelper.asyncImageViewFillUrl(mHolder.mImvHead, attAndFans.getHeadImg());
        if(0 == attAndFans.getBuisness()){
            mHolder.mImvBus.setVisibility(View.GONE);
        } else {
            mHolder.mImvBus.setVisibility(View.VISIBLE);
            mHolder.mImvBus.setImageBitmap(ResourceHelper.getBusinessIconByCode(this.mContext, attAndFans.getBuisness()));
        }
        String job = attAndFans.getJob();
        if(ResourceHelper.isEmpty(job)){
            job = this.mContext.getString(R.string.not_input_job);
        }
        mHolder.mTxvJob.setText(job);
        mHolder.mTxvName.setText(attAndFans.getSurnname() + attAndFans.getFirstName());
        String company = attAndFans.getCompanyName();
        if(Helper.isEmpty(company)){
            company = this.mContext.getString(R.string.not_input_company);
        }
        mHolder.mTxvCompany.setText(company);

        int status = attAndFans.getAttentionStatus();
        if(2 == status){
            //相互关注
            setTextStatus(mHolder.mTxvState, 0 , R.color.color_787878, R.string.mutual_attention);
        }else if(1 == status) {
            //已关注
            setTextStatus(mHolder.mTxvState, 0, R.color.color_787878, R.string.store_up_already);
        }else{
            //关注他
            final int currentPosition = position;//记录当前的position
            final String vCardNo = attAndFans.getVcardNo();//记录当前微片号
            setTextStatus(mHolder.mTxvState, R.drawable.bg_general_btn_green, R.color.color_399c2f, R.string.store_up_ta);
            mHolder.mTxvState.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(ResourceHelper.isNotNull(mListener)){
                        mListener.onClick(currentPosition, vCardNo);
                    }
                }
            });
        }
        return convertView;
    }

    /**
     * 关注他接口监听类
     * @param mListener
     */
    public void setStatusListener(StatusListener mListener){
        if(Helper.isNotNull(mListener)){
            this.mListener = mListener;
        }
    }

    /**
     * 修改关注状态
     * @param position
     */
    public void chanceStutas(int position){
        if(position != -1){
            this.getItem(position).setAttentionStatus(2);
        }
        this.notifyDataSetChanged();
    }

    /**
     * 关注状态
     * @param view
     * @param drawableId
     * @param colorId
     * @param stringId
     */
    private void setTextStatus(TextView view, int drawableId, int colorId, int stringId){
        if(ResourceHelper.isNotNull(view)){
            view.setTextColor(this.mContext.getResources().getColor(colorId));
            view.setBackgroundResource(drawableId);
            view.setText(stringId);
        }

    }

    /**
     * 添加数据源
     * @param attAndFanss
     * @return
     */
    public boolean addItems(List<UserAttAndFansEntity> attAndFanss){
        boolean result = false;
        this.attAndFanss.clear();
        if(ResourceHelper.isNotNull(attAndFanss)){
            result = this.attAndFanss.addAll(attAndFanss);
            this.notifyDataSetChanged();
        }
        return result;
    }

    private class ViewHolder {
        private AsyncImageView mImvHead;
        private TextView mTxvName, mTxvCompany, mTxvJob, mTxvState;
        private ImageView mImvBus, mImvGroup,mImvPartner;
    }

    /**
     * 创建interface
     */
    public interface StatusListener{
        void onClick(int position, String vCardNo);
    }
}
