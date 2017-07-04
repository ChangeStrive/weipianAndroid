package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.CardEntity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * 我的名片：名片名称
 * Created by YongJi on 2015/9/15.
 */
public class VCardNameAdapter extends BaseAdapter {

    private static final int MAX_CARD_COUNT = 3;

    private ArrayList<NameValuePair> mItems = new ArrayList<>();
    private Context mContext;
    private Resources mResources;
    //选中项位置
    private int mSelectedPosition = -1;

    public VCardNameAdapter(Context context){
        this.mContext = context;
        this.mResources = this.mContext.getResources();
    }
    @Override
    public int getCount() {
        return MAX_CARD_COUNT;
    }

    @Override
    public NameValuePair getItem(int position) {
        return this.mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (Helper.isNull(convertView)) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_lsv_vcard_name, null);
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.chb_vcard_item_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Drawable checkBoxImageDrawable = null;

        if(position < this.mItems.size()){
            //我的名片
            NameValuePair item = getItem(position);
            String num = item.getValue();
            if( Helper.isNotEmpty(num) && Integer.valueOf(num) > 999){
                num = "999+";
            }
            viewHolder.mCheckBox.setText(item.getName() + "(" + num + ")");
            checkBoxImageDrawable = this.mResources.getDrawable(R.drawable.bg_chb_white);
            viewHolder.mCheckBox.setTextColor(this.mResources.getColor(R.color.color_ffffff));
        }else{
            //未添加名片
            viewHolder.mCheckBox.setText(this.mResources.getString(R.string.add_vcard) + "(0)");
            checkBoxImageDrawable = this.mResources.getDrawable(R.mipmap.bg_chb_false_gray);
            viewHolder.mCheckBox.setTextColor(this.mResources.getColor(R.color.color_4b5558));
        }
        if(mSelectedPosition == position){
            viewHolder.mCheckBox.setChecked(true);
        }else{
            viewHolder.mCheckBox.setChecked(false);
        }
        viewHolder.mCheckBox.setCompoundDrawablesWithIntrinsicBounds(checkBoxImageDrawable, null, null, null);
        return convertView;
    }

    public boolean addItems(ArrayList<CardEntity> vcardList){
        boolean result = false;
        if(Helper.isNotNull(vcardList)){
            this.mItems.clear();
            ArrayList<NameValuePair> items = new ArrayList<>();
            for(CardEntity entity : vcardList){
                BasicNameValuePair item = new BasicNameValuePair(this.mContext.getString(R.string.my_card), String.valueOf(entity.getCardCount()));
                items.add(item);
            }
            result = this.mItems.addAll(items);
            this.notifyDataSetChanged();
        }
        return result;
    }

    /**
     * 设置选择的项
     * @param position
     */
    public void setPosition(int position){
        this.mSelectedPosition = position;
        this.notifyDataSetChanged();
    }

    private class ViewHolder{
        CheckBox mCheckBox;
    }
}
