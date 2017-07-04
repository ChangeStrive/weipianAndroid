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
import com.maya.android.vcard.entity.result.UserNearbyPeopleEntity;
import com.maya.android.vcard.util.ResourceHelper;
import java.util.ArrayList;

/**
 * 附近的人脉适配器
 * Created by Administrator on 2015/9/23.
 */
public class UserNearbyPeopleAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<UserNearbyPeopleEntity> nearbyPeoples = new ArrayList<UserNearbyPeopleEntity>();
    private ArrayList<UserNearbyPeopleEntity> peoples;
    /**记录女性朋友 **/
    private ArrayList<UserNearbyPeopleEntity> womanPeoples;
    /**记录男性朋友 **/
    private ArrayList<UserNearbyPeopleEntity> manPeoples;
    private StatusListener mListener;

    public UserNearbyPeopleAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.nearbyPeoples.size();
    }

    @Override
    public UserNearbyPeopleEntity getItem(int position) {
        UserNearbyPeopleEntity nearbyPeople = this.nearbyPeoples.get(position);
        if(ResourceHelper.isNotNull(nearbyPeople)){
            return nearbyPeople;
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_user_nearby_people, parent, false);
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
            mHolder = (ViewHolder)convertView.getTag();
        }
        UserNearbyPeopleEntity nearbyPeople = getItem(position);
        mHolder.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
        ResourceHelper.asyncImageViewFillUrl(mHolder.mImvHead, ResourceHelper.getImageUrlOnIndex(nearbyPeople.getHeadImg(), 0));
        if(0 == nearbyPeople.getBusiness()){
            mHolder.mImvBus.setVisibility(View.GONE);
        } else {
            mHolder.mImvBus.setVisibility(View.VISIBLE);
            mHolder.mImvBus.setImageBitmap(ResourceHelper.getBusinessIconByCode(this.mContext, nearbyPeople.getBusiness()));
        }
        String job = nearbyPeople.getJob();
        if(ResourceHelper.isEmpty(job)){
            job = this.mContext.getString(R.string.not_input_job);
        }
        mHolder.mTxvJob.setText(ResourceHelper.getImageUrlOnIndex(job, 0));
        mHolder.mTxvName.setText(nearbyPeople.getDisplayName());
        String company = nearbyPeople.getCompanyName();
        if(ResourceHelper.isEmpty(company)){
            company = this.mContext.getString(R.string.not_input_company);
        }
        mHolder.mTxvCompany.setText(ResourceHelper.getImageUrlOnIndex(company, 0));
        int status = nearbyPeople.getAttentionStatus();
        if(2 == status){
            //相互关注
            setTextStatus(mHolder.mTxvState, 0 , R.color.color_787878, R.string.mutual_attention);
        }else if(1 == status) {
            //已关注
            setTextStatus(mHolder.mTxvState, 0, R.color.color_787878, R.string.store_up_already);
        }else{
            //关注他
            final int currentPosition = position;//记录当前的position
            final String vCardNo = nearbyPeople.getVcardNo();//记录当前微片号
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
     * 添加数据源
     * @param nearbyPeoples
     */
    public void addItems(ArrayList<UserNearbyPeopleEntity> nearbyPeoples){
        this.nearbyPeoples.clear();
        if(ResourceHelper.isNotNull(nearbyPeoples)){
            this.nearbyPeoples.addAll(nearbyPeoples);
            this.peoples = nearbyPeoples;
        }
        notifyDataSetChanged();
    }

    /**
     * 显示男性朋友
     */
    public void showMale(){
        this.nearbyPeoples.clear();
        if(ResourceHelper.isNull(this.manPeoples)){
            this.manPeoples = new ArrayList<UserNearbyPeopleEntity>();
            if(ResourceHelper.isNotNull(this.peoples)){
                for(int i = 0, size = this.peoples.size(); i < size; i++){
                    UserNearbyPeopleEntity people = this.peoples.get(i);
                    if(people.getSex() == 1){
                        this.manPeoples.add(people);
                    }
                }
            }
        }
        this.nearbyPeoples.addAll(this.manPeoples);
        notifyDataSetChanged();
    }

    /**
     * 显示女性朋友
     */
    public void showFemale(){
        this.nearbyPeoples.clear();
        if(ResourceHelper.isNull(this.womanPeoples)){
            this.womanPeoples = new ArrayList<UserNearbyPeopleEntity>();
            if(ResourceHelper.isNotNull(this.peoples)){
                for(int i = 0, size = this.peoples.size(); i < size; i++){
                    UserNearbyPeopleEntity people = this.peoples.get(i);
                    if(people.getSex() != 1){
                        this.womanPeoples.add(people);
                    }
                }
            }
        }
        this.nearbyPeoples.addAll(this.womanPeoples);
        notifyDataSetChanged();
    }

    /**
     * 显示全部朋友
     */
    public void showAllPeoples(){
        this.nearbyPeoples.clear();
        if(ResourceHelper.isNotNull(this.peoples)){
            this.nearbyPeoples.addAll(this.peoples);
        }
        notifyDataSetChanged();
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
     * 修改关注状态
     * @param position
     */
    public void setStatusChange(int position){
        getItem(position).setAttentionStatus(1);
        notifyDataSetChanged();
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
