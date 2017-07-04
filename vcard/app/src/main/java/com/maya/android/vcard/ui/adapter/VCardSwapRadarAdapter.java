package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.result.ContactRadarResultEntity;
import com.maya.android.vcard.ui.frg.VCardSwapRadarFragment;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.Random;

/**
 * 雷达搜索结果适配器
 * Created by Administrator on 2015/9/17.
 */
public class VCardSwapRadarAdapter extends BaseAdapter{
    /**设置圆角角度**/
    private int mCorner = -2;
    private Context mContext;
    private ArrayList<ContactRadarResultEntity> swapRadars = new ArrayList<ContactRadarResultEntity>();
    /** 记录随机数中存入的item **/
    private SparseArray<ContactRadarResultEntity> mRandomIntegers = new SparseArray<ContactRadarResultEntity>();

    public VCardSwapRadarAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.swapRadars.size();
    }

    @Override
    public ContactRadarResultEntity getItem(int position) {
        ContactRadarResultEntity swapRadar = this.swapRadars.get(position);
        if(ResourceHelper.isNotNull(swapRadar)){
            return swapRadar;
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_grid_swap_radar, parent, false);
            mHolder = new ViewHolder();
            mHolder.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_radar_search_head);
            mHolder.mImvHead.setRoundCorner(this.mCorner);
            mHolder.mTxvName = (TextView) convertView.findViewById(R.id.txv_radar_search_name);
            mHolder.mImvTag = (ImageView) convertView.findViewById(R.id.imv_radar_search_tag);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        ContactRadarResultEntity swapRadar = getItem(position);
        if(ResourceHelper.isNotNull(swapRadar) && swapRadar.getCardId() != -3333){
            mHolder.mImvHead.setDefaultImageResId(R.mipmap.img_head);
            String headImg = swapRadar.getHeadImg();
            if(ResourceHelper.isNotEmpty(headImg)){
                ResourceHelper.asyncImageViewFillUrl(mHolder.mImvHead, ResourceHelper.getImageUrlOnIndex(headImg, 0));
            }
            mHolder.mTxvName.setText(swapRadar.getDisplayName());
            int tag = swapRadar.getFriend();
            if(tag == VCardSwapRadarFragment.FRIEND_IS){
                mHolder.mImvTag.setImageResource(R.mipmap.img_swap_radar_is_friend);
            }else if(tag == VCardSwapRadarFragment.FRIEND_SEND){
                mHolder.mImvTag.setImageResource(R.mipmap.img_swap_radar_is_send);
            }else if(tag == VCardSwapRadarFragment.FRIEND_RECEIVE){
                mHolder.mImvTag.setImageResource(R.mipmap.img_swap_radar_is_receive);
            }else{
                mHolder.mImvTag.setImageDrawable(null);
            }
        }else{
            mHolder.mImvHead.setBackground(null);
            mHolder.mImvHead.setImageDrawable(null);
        }

        return convertView;
    }



    /**
     * 添加数据源
     * @param swapRadars 数据源
     */
    public void addItems(ArrayList<ContactRadarResultEntity> swapRadars){
        this.swapRadars.clear();
        if(ResourceHelper.isNotNull(swapRadars)){
            this.swapRadars.addAll(getRearrangeArrayList(swapRadars));
        }
        notifyDataSetChanged();
    }

    /**
     * 插入单个数据
     * @param swapRadar
     */
    public void addItem(ContactRadarResultEntity swapRadar){
        if(ResourceHelper.isNotNull(swapRadar)){
            boolean isAdd = true;
            for(int i = 0, size = this.mRandomIntegers.size(); i < size; i++){
                int key = this.mRandomIntegers.keyAt(i);
                ContactRadarResultEntity hasSwapRadar = this.mRandomIntegers.get(key, null);
                if(ResourceHelper.isNotNull(hasSwapRadar) && hasSwapRadar.getCardId() == swapRadar.getCardId()){
                    isAdd = false;
                    break;
                }
            }
            if(isAdd){
                int mRandom;
                if(this.mRandomIntegers.size() < 16){
                    mRandom = new Random().nextInt(16);
                    this.swapRadars.set(mRandom, swapRadar);
                    this.mRandomIntegers.put(mRandom, swapRadar);
                }else if(this.mRandomIntegers.size() < 32){
                    mRandom = new Random().nextInt(32);
                    this.swapRadars.set(mRandom, swapRadar);
                    this.mRandomIntegers.put(mRandom, swapRadar);
                }else {
                    this.mRandomIntegers.put(getCount(), swapRadar);
                    this.swapRadars.add(swapRadar);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     *  修改Item状态
     * @param accountId 用户名
     * @param friend 好友状态
     */
    public void setItem(long accountId, int friend){
        for(int i = 0, size = getCount(); i < size; i++){
            ContactRadarResultEntity swapRadar = getItem(i);
            if(ResourceHelper.isNotNull(swapRadar) && swapRadar.getCardId() != -3333 && swapRadar.getAccountId() == accountId){
                swapRadar.setFriend(friend);
                break;
            }
        }

        notifyDataSetChanged();
    }

    /**
     * 获取所有数据
     */
    public  ArrayList<ContactRadarResultEntity> getItems(){
        ArrayList<ContactRadarResultEntity> items = new ArrayList<ContactRadarResultEntity>();
        for(int i = 0, size = this.mRandomIntegers.size(); i < size; i++){
            int key = this.mRandomIntegers.keyAt(i);
            ContactRadarResultEntity radar = this.mRandomIntegers.get(key, null);
            if(ResourceHelper.isNotNull(radar)){
                items.add(radar);
            }
        }
        return items;
    }

    /**
     * 返回一个随机没有重复的数字
     * @param maxNum
     * @return
     */
    private int getBackRandom(int maxNum) {
        int mRandom = -1;
        if(maxNum > 0){
            Random ran = new Random();
            do{
                mRandom = ran.nextInt(maxNum);
            } while(ResourceHelper.isNotNull(this.mRandomIntegers.get(mRandom, null)));
        }
        return mRandom;
    }

    /**
     * 获取重新排列后的数据
     * @param swapRadars
     * @return
     */
    private ArrayList<ContactRadarResultEntity> getRearrangeArrayList(ArrayList<ContactRadarResultEntity> swapRadars){
        this.mRandomIntegers.clear();
        int count = swapRadars.size();
        if(count < 32){
            ArrayList<ContactRadarResultEntity> items = new ArrayList<ContactRadarResultEntity>();
            int num = 32;
            if(count < 16){
                num = 16;
            }
            ContactRadarResultEntity radar = new ContactRadarResultEntity();
            for(int i = 0; i < num; i++){
                items.add(getItem(radar));
            }
            for(int i = 0; i < count; i++){
                int position = getBackRandom(num);
                ContactRadarResultEntity swapRadar = swapRadars.get(i);
                items.set(position, swapRadar);
                this.mRandomIntegers.put(position, swapRadar);
            }
            return items;
        }
        for(int i = 0; i < count; i++){
            this.mRandomIntegers.put(i, swapRadars.get(i));
        }
        return swapRadars;
    }

    /**
     * 克隆数据实体类
     * @param radar
     * @return
     */
    private ContactRadarResultEntity getItem(ContactRadarResultEntity radar){
        ContactRadarResultEntity item = null;
        try {
            item = (ContactRadarResultEntity) radar.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if(ResourceHelper.isNull(item)){
            item = new ContactRadarResultEntity();
        }
        item.setCardId(-3333);
        return item;
    }

    private class ViewHolder{
        private AsyncImageView mImvHead;
        private TextView mTxvName;
        private ImageView mImvTag;
    }
}
