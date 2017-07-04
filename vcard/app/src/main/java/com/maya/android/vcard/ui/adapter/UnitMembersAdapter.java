package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.entity.result.UnitMemberResultEntity;
import com.maya.android.vcard.util.ConverChineseCharToEnHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 单位成员适配器
 * Created by Administrator on 2015/9/7.
 */
public class UnitMembersAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<UnitMemberResultEntity> members = new ArrayList<UnitMemberResultEntity>();
    //原始列表
    private ArrayList<UnitMemberResultEntity> mOriginalValues = new ArrayList<UnitMemberResultEntity>();
    /** 是否显示字母标题**/
    private SparseBooleanArray mTitleItems = new SparseBooleanArray();
    /** 是否单选 **/
    private boolean isSingleChoose = false;
    private int mCurPosition = -1;
    private final Object mLock = new Object();
    private ContactFilter mContactFilter;

    public UnitMembersAdapter(Context mContext){
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
            convertView =  LayoutInflater.from(this.mContext).inflate( R.layout.item_lsv_unit_member, parent, false);
            mHolder = new ViewHolder();
            mHolder.mTxvCarcaseChar = (TextView) convertView.findViewById(R.id.txv_unit_member_carcase_char);
            mHolder.mImvImghead = (AsyncImageView) convertView.findViewById(R.id.imv_head);
            mHolder.mTxvName = (TextView) convertView.findViewById(R.id.txv_name);
            mHolder.mTxvPosition = (TextView) convertView.findViewById(R.id.txv_position);
            mHolder.mTxvJurisdiction = (TextView) convertView.findViewById(R.id.txv_Jurisdiction);
            mHolder.mChbSelected = (CheckBox) convertView.findViewById(R.id.chb_selected);
            mHolder.mViewDivider = convertView.findViewById(R.id.view_unit_member_divider);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }

        UnitMemberResultEntity member = getItem(position);
        mHolder.mImvImghead.setImageResource(R.mipmap.img_upload_head);
        ResourceHelper.asyncImageViewFillUrl(mHolder.mImvImghead, member.getHeadImg());
        mHolder.mTxvName.setText(member.getDisplayName());
        mHolder.mTxvPosition.setText(member.getJob());
        int role = member.getRole();
        String curAlpha = ConverChineseCharToEnHelper.getFirstLetter(member.getDisplayName()).toUpperCase();
        //下划线
        if(position == getCount() -1 || this.mTitleItems.get(position +1, false)){
            mHolder.mViewDivider.setVisibility(View.GONE);
        }else{
            mHolder.mViewDivider.setVisibility(View.VISIBLE);
        }
        //提示性标题
        if(this.mTitleItems.get(position, false)){
            mHolder.mTxvCarcaseChar.setVisibility(View.VISIBLE);
            if(position == 0 && role > DatabaseConstant.Role.MEMBER){
                mHolder.mTxvCarcaseChar.setText(R.string.administrator);
            }else{
                mHolder.mTxvCarcaseChar.setText(curAlpha);
            }
        }else{
            mHolder.mTxvCarcaseChar.setVisibility(View.GONE);
        }
        //展示选中项
        if(this.isSingleChoose){
            if( this.mCurPosition == position){
                mHolder.mChbSelected.setVisibility(View.VISIBLE);
                mHolder.mTxvJurisdiction.setVisibility(View.GONE);
            } else {
                mHolder.mChbSelected.setVisibility(View.GONE);
                mHolder.mTxvJurisdiction.setVisibility(View.VISIBLE);
            }
        }else{
            if(role == DatabaseConstant.Role.ADMIN){//管理员
                mHolder.mTxvJurisdiction.setText(R.string.unit_administrator);
            }else if(role == DatabaseConstant.Role.CREATER){//超级管理员
                mHolder.mTxvJurisdiction.setText(R.string.super_administrator);
            }else{
                mHolder.mTxvJurisdiction.setText(null);
            }
        }

        return convertView;
    }

    /**
     * 单击项
     * @param position
     */
    public void setPosition(int position){
        this.mCurPosition = position;
        notifyDataSetChanged();
    }

    /**
     * 取消选中项
     */
    public void canclePosition(){
        mCurPosition = -1;
        notifyDataSetChanged();
    }

    /**
     * 设置编辑状态
     * @param isSingleChoose
     */
    public void setItemChooseType(boolean isSingleChoose){
        this.isSingleChoose = isSingleChoose;
        notifyDataSetChanged();
    }

    /**
     * 获取成员Id
     * @param position
     * @return
     */
    public long getMemberId(int position){
        if(position != -1){
            return getItem(position).getId();
        }
        return 0;
     }

    /**
     * 添加所有数据
     * @param items
     */
    public void addItems(ArrayList<UnitMemberResultEntity> items){
        this.members.clear();
        if(Helper.isNotNull(items)){
            Collections.sort(items, new EnterpriseMemberComparator());
            this.members.addAll(items);
            this.mOriginalValues = items;
            isShowTitleAndDivider(this.members);
        }
        notifyDataSetChanged();
    }


    /**
     * 添加单个数据
     * @param member
     */
    public void addItem(UnitMemberResultEntity member){
        if(Helper.isNotNull(member)){
            this.members.add(member);
            Collections.sort(this.members, new EnterpriseMemberComparator());
            this.mOriginalValues = this.members;
            isShowTitleAndDivider(this.members);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除项
     * @param position
     */
    public void removeItem(int position){
        if(position != -1){
            this.members.remove(position);
            isShowTitleAndDivider(this.members);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置管理员 角色说明：0-普通成员；1-管理员；2-超级管理员（群组创建者）
     * @param position 选中项
     * @param role 更改权限
     */
    public void setAdministrator(int position, int role){
        if(position != -1){
            getItem(position).setRole(role);
            Collections.sort(this.members, new EnterpriseMemberComparator());
            this.mOriginalValues = this.members;
            isShowTitleAndDivider(this.members);
        }
        notifyDataSetChanged();
    }

     /**
     * 过滤
     * @return
     */
    public Filter getFilter() {
        if (Helper.isNull(mContactFilter)){
            this.mContactFilter = new ContactFilter();
        }
        return this.mContactFilter;
    }

    /**
     * 根据 字母的值找到存储的汉字首字母拼音的索引，listView中根据这个索引定位显示
     *
     * @param alpha
     * @return
     */
    public int getPosition(String alpha) {
        int position = -1;
        String firstStr;
        for (int i = 0, size = getCount(); i < size; i++) {
            firstStr = getFirstAlpha(getItem(i));
            if (Helper.isNotEmpty(firstStr) && firstStr.startsWith(alpha)) {
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * 获取首字母
     * @param member
     * @return String
     */
    private String getFirstAlpha(UnitMemberResultEntity member){
        if(Helper.isNotNull(member)){
            return ConverChineseCharToEnHelper.getFirstLetter(member.getDisplayName()).toUpperCase();
        }
        return "";
    }

    /**
     * 记录需显示标题和下划线的地方
     * @param items
     */
    private void isShowTitleAndDivider(ArrayList<UnitMemberResultEntity> items){
        int idx = 0;
        String curAlpha = "";
        String preAlpha = "";
        this.mTitleItems.clear();
        for(int i = 0, size = items.size(); i < size; i++){
            if(i == 0){
                this.mTitleItems.put(i, true);
            }else{
                curAlpha = ConverChineseCharToEnHelper.getFirstLetter(items.get(i).getDisplayName()).toUpperCase();
                idx = i - 1;
                preAlpha = idx >= 0 ? ConverChineseCharToEnHelper.getFirstLetter(getItem(idx).getDisplayName()) : "";
                if(!curAlpha.equals(preAlpha.toUpperCase()) && items.get(i).getRole() ==  DatabaseConstant.Role.MEMBER){
                    this.mTitleItems.put(i, true);
                }
            }

        }
    }

     /**
     * 名字排序
     * @author zheng_cz
     * @since 2014年4月16日 下午7:55:24
     */
    class EnterpriseMemberComparator implements Comparator<UnitMemberResultEntity> {

        @Override
        public int compare(UnitMemberResultEntity o1, UnitMemberResultEntity o2) {
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
            int role1 = o1.getRole();
            int role2 = o2.getRole();
            if(role1 == role2){
                String letter1 = getDisplayNameSpell(o1);
                String letter2 = getDisplayNameSpell(o2);
                if (letter2.equals(Constants.Other.CONTENT_ARRAY_SPLIT)) {
                    return -1;
                } else if (letter1.equals(Constants.Other.CONTENT_ARRAY_SPLIT)) {
                    return 1;
                } else {
                    return letter1.compareTo(letter2);
                }
            }else{
                return String.valueOf(role2).compareTo(String.valueOf(role1));
            }
        }
    }


    /**
     * 获取名称的拼音
     * @param person
     * @return String
     */
    private String getDisplayNameSpell(UnitMemberResultEntity person){
        String displayName = ConverChineseCharToEnHelper.converToAllFirstSpellsLowercase(person.getDisplayName());
        return displayName;
    }

    /**
     * 公司成员过滤
     *
     * @author zheng_cz
     *
     */
    class ContactFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            // 持有过滤操作完成之后的数据。该数据包括过滤操作之后的数据的值以及数量。 count:数量 values包含过滤操作之后的数据的值
            FilterResults results = new FilterResults();
            if ( Helper.isEmpty(prefix)) {
                synchronized (mLock) {
                    results.values = mOriginalValues;
                    results.count = mOriginalValues.size();
                }
            } else {
                // 做正式的筛选
                String prefixString = prefix.toString().toUpperCase();

                // 声明一个临时的集合对象 将原始数据赋给这个临时变量
                final List<UnitMemberResultEntity> values = mOriginalValues;

                final int count = values.size();

                // 新的集合对象
                ArrayList<UnitMemberResultEntity> newValues = new ArrayList<UnitMemberResultEntity>(count);

                for (int i = 0; i < count; i++) {
                    // 如果姓名的前缀相符或者电话相符或职位相符或公司名称前缀相符 就添加到新的集合
                    UnitMemberResultEntity value = values.get(i);

                    String allFirstName = ConverChineseCharToEnHelper.converToAllFirstSpellsUppercase(value.getDisplayName());
                    String nameToNum = ConverChineseCharToEnHelper.converChinesToNumber(allFirstName);
                    String displayName = ResourceHelper.isEmpty(value.getDisplayName()) ? "" : value.getDisplayName();
                    String vCardNum = ResourceHelper.isNull(value.getVcardNumber()) ? "" : value.getVcardNumber();

                    // 姓名  || 姓名拼音 || 姓名拼音首字母  || 姓名首字母拼音对应的数字 ||微片号
                    boolean isMatch = displayName.contains(prefixString)
                            || getDisplayNameSpell(value).contains(prefixString)
                            || allFirstName.contains(prefixString)
                            || nameToNum.startsWith(prefixString)
                            || vCardNum.contains(prefixString) ;
                    if (isMatch) {
                        newValues.add(value);
                    }
                }
                // 然后将这个新的集合数据赋给FilterResults对象
                results.values = newValues;
                results.count = newValues.size();
            }

            return results;

        }

        @Override
        protected void publishResults(CharSequence arg0, FilterResults results) {
            // 重新将与适配器相关联的List重赋值一下
            members = (ArrayList<UnitMemberResultEntity>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    private class ViewHolder{
        private TextView mTxvCarcaseChar,mTxvName,mTxvPosition,mTxvJurisdiction;
        private AsyncImageView mImvImghead;
        private CheckBox mChbSelected;
        private View mViewDivider;
    }
}
