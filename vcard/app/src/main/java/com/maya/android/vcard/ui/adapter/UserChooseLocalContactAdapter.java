package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.ContactBookEntity;
import com.maya.android.vcard.util.ConverChineseCharToEnHelper;
import com.maya.android.vcard.util.ResourceHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *  选择本地联系人适配器(通讯录)
 * Created by Administrator on 2015/9/28.
 */
public class UserChooseLocalContactAdapter extends BaseAdapter implements Filterable{
    private ArrayList<ContactBookEntity> mLocalContacts = new ArrayList<ContactBookEntity>();
    private Context mContext;
    /** 记录是否显示字母标题 **/
    private SparseBooleanArray mABCBooleanArray = new SparseBooleanArray();
    /** 记录是否被勾选 **/
    private SparseArray<ContactBookEntity> mSelectArray = new SparseArray<ContactBookEntity>();
    /** 是否隐藏勾选项 **/
    private boolean isHideSelected = false;//默认false
    /** 是否单选 **/
    private boolean isSingleChoose = true;//默认true
    private final Object mLock = new Object();
    /** 联系人过滤 **/
    private ContactFilter mContactFilter;
    /** 记录原Items 用于收索遍历**/
    private ArrayList<ContactBookEntity> mOriginalValues = new ArrayList<ContactBookEntity>();

    public UserChooseLocalContactAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.mLocalContacts.size();
    }

    @Override
    public ContactBookEntity getItem(int position) {
        ContactBookEntity mLocalContact = this.mLocalContacts.get(position);
        if(ResourceHelper.isNotNull(mLocalContact)){
            return mLocalContact;
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_user_choose_local_contact, parent, false);
            mHolder = new ViewHolder();
            mHolder.mImvHead = (ImageView) convertView.findViewById(R.id.imv_head);
            mHolder.mChbSelected = (CheckBox) convertView.findViewById(R.id.chb_is_agree);
            mHolder.mTxvName = (TextView) convertView.findViewById(R.id.txv_name);
            mHolder.mTxvChar = (TextView) convertView.findViewById(R.id.txv_char);
            mHolder.mViewDivider = (View) convertView.findViewById(R.id.view_divider);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        ContactBookEntity mLocalContact = getItem(position);
        mHolder.mTxvName.setText(mLocalContact.getDisplayName());
        if(ResourceHelper.isNotNull(mLocalContact.getHeadPhoto())){
            mHolder.mImvHead.setImageBitmap(mLocalContact.getHeadPhoto());
        }else{
            mHolder.mImvHead.setImageResource(R.mipmap.img_upload_head);
        }
        //CheckBox事件
        if(!this.isHideSelected){
            boolean isSelected = ResourceHelper.isNotNull(this.mSelectArray.get(position, null));
            mHolder.mChbSelected.setChecked(isSelected);
            if(isSelected){
                mHolder.mChbSelected.setVisibility(View.VISIBLE);
            }else{
                if(this.isSingleChoose){
                    mHolder.mChbSelected.setVisibility(View.GONE);
                }else{
                    mHolder.mChbSelected.setVisibility(View.VISIBLE);
                }
            }
        }else{
            mHolder.mChbSelected.setVisibility(View.GONE);
        }
        //判断是否显示标题栏
        boolean isShowChar = this.mABCBooleanArray.get(position, false);
        if(isShowChar){
            mHolder.mTxvChar.setVisibility(View.VISIBLE);
            String curAlpha = ConverChineseCharToEnHelper.getFirstLetter(mLocalContact.getDisplayName()).toUpperCase();
            mHolder.mTxvChar.setText(curAlpha);
        }else{
            mHolder.mTxvChar.setVisibility(View.GONE);
        }
        //判断是否显示下划线
        boolean isShowDivider = this.mABCBooleanArray.get(position + 1, false);
        if(position == getCount() - 1 ||isShowDivider){
            mHolder.mViewDivider.setVisibility(View.GONE);
        }else{
            mHolder.mViewDivider.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (Helper.isNull(this.mContactFilter)){
            this.mContactFilter = new ContactFilter();
        }
        return this.mContactFilter;
    }

    /**
     * 默认设置选择项
     * @param position
     */
    public void setSelected(int position){
        setSelected(position, this.isSingleChoose);
    }

    /**
     * 设置CheckBox选项
     * @param isSingleChoose 是否单选
     */
    public void setIsSingleChoose(boolean isSingleChoose){
        this.isSingleChoose = isSingleChoose;
        notifyDataSetChanged();
    }

    /**
     * 取消所有选择项
     */
    public void cancelSelectedAll(){
        this.mSelectArray.clear();
        notifyDataSetChanged();
    }

    /**
     * 设置CheckBox选项
     * @param isHideSelected 是否隐藏
     */
    public void setIsHideSelected(boolean isHideSelected){
        this.isHideSelected = isHideSelected;
        notifyDataSetChanged();
    }

    /**
     * 设置选中项
     * @param position
     * @param isSingleChoose 是否单选
     */
    public void setSelected(int position, boolean isSingleChoose){
        if(isSingleChoose){
            this.mSelectArray.clear();
        }
        boolean isSelected = ResourceHelper.isNull(this.mSelectArray.get(position, null));
        if(isSelected){
            this.mSelectArray.put(position, getItem(position));
        }else{
            this.mSelectArray.remove(position);
        }
         notifyDataSetChanged();
    }

    /**
     * 获取选中个数
     * @return
     */
    public int getSelectedCount(){
        return this.mSelectArray.size();
    }

    /**
     * 获取选中项第一个有效手机号码
     * @return
     */
    public String getMobile(){
       String mobile = "";
       int key;
       for(int i = 0, size = this.mSelectArray.size(); i < size; i++){
           key = this.mSelectArray.keyAt(i);
           ContactBookEntity mLocalContact = this.mSelectArray.get(key, null);
           if(ResourceHelper.isNotNull(mLocalContact)){
               mobile = mLocalContact.getMobile();
               break;
           }
       }
        return mobile;
    }

    /**
     * 添加数据源
     */
    public void addItems(ArrayList<ContactBookEntity> mLocalContacts){
        this.mLocalContacts.clear();
        if(ResourceHelper.isNotNull(mLocalContacts)){
            Collections.sort(mLocalContacts, new EnterpriseMemberComparator());
            this.mLocalContacts.addAll(mLocalContacts);
            isShowTitleAndDivider(this.mLocalContacts);
            this.mOriginalValues = mLocalContacts;
        }
        notifyDataSetChanged();
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
            if (ResourceHelper.isNotEmpty(firstStr) && firstStr.startsWith(alpha)) {
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * 记录需显示标题和下划线的地方
     * @param mLocalContacts
     */
    private void isShowTitleAndDivider(ArrayList<ContactBookEntity> mLocalContacts){
        int idx;
        String curAlpha = "";
        String preAlpha = "";
        this.mABCBooleanArray.clear();
        for(int i = 0, size = mLocalContacts.size(); i < size; i++){
            if(i == 0){
                this.mABCBooleanArray.put(i, true);
            }else{
                ContactBookEntity mLocalContact = mLocalContacts.get(i);
                curAlpha = ConverChineseCharToEnHelper.getFirstLetter(mLocalContact.getDisplayName()).toUpperCase();
                idx = i - 1;
                preAlpha = idx >= 0 ? ConverChineseCharToEnHelper.getFirstLetter(mLocalContacts.get(idx).getDisplayName()) : "";
                if(!curAlpha.equals(preAlpha.toUpperCase())){
                    this.mABCBooleanArray.put(i, true);
                }
            }
         }
    }

   /**
     * 获取名称的拼音
     * @param person
     * @return String
     */
    private String getDisplayNameSpell(ContactBookEntity person){
        String displayName = ConverChineseCharToEnHelper.converToAllFirstSpellsLowercase(person.getDisplayName());
        return displayName;
    }

    /**
     * 获取首字母
     * @param person
     * @return String
     */
    private String getFirstAlpha(ContactBookEntity person){
        if(ResourceHelper.isNotNull(person)){
            return ConverChineseCharToEnHelper.getFirstLetter(person.getDisplayName()).toUpperCase();
        }
        return "";
    }

    /**
     * 名字排序
     * @author zheng_cz
     * @since 2014年4月16日 下午7:55:24
     */
    class EnterpriseMemberComparator implements Comparator<ContactBookEntity> {
        @Override
        public int compare(ContactBookEntity o1, ContactBookEntity o2) {
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
                String letter1 = getDisplayNameSpell(o1);
                String letter2 = getDisplayNameSpell(o2);
                if (letter2.equals(Constants.Other.CONTENT_ARRAY_SPLIT)) {
                    return -1;
                } else if (letter1.equals(Constants.Other.CONTENT_ARRAY_SPLIT)) {
                    return 1;
                } else {
                    return letter1.compareTo(letter2);
                }
         }
    }

    /**
     * 展示搜索的列表
     */
    private void searchItems(ArrayList<ContactBookEntity> searchList){
        this.mLocalContacts.clear();
        //清空选中项,防止应为搜索引发的选中项错乱
        this.mSelectArray.clear();
        if(Helper.isNotNull(searchList)){
            Collections.sort(searchList, new EnterpriseMemberComparator());
            this.mLocalContacts.addAll(searchList);
            isShowTitleAndDivider(this.mLocalContacts);
        }
        notifyDataSetChanged();
    }

    /**
     * 联系人过滤
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
                final ArrayList<ContactBookEntity> values = mOriginalValues;
                final int count = values.size();
                 // 新的集合对象
                ArrayList<ContactBookEntity> newValues = new ArrayList<ContactBookEntity>(count);
                for (int i = 0; i < count; i++) {
                    // 如果姓名的前缀相符或者电话相符或职位相符或公司名称前缀相符 就添加到新的集合
                    ContactBookEntity value = (ContactBookEntity) values.get(i);
                    String allFirstName = ConverChineseCharToEnHelper.converToAllFirstSpellsUppercase(value.getDisplayName());
                    String nameToNum = ConverChineseCharToEnHelper.converChinesToNumber(allFirstName);
                    String displayName = Helper.isEmpty(value.getDisplayName()) ? "" : value.getDisplayName();
                    // 姓名  || 姓名拼音 || 姓名拼音首字母  || 姓名首字母拼音对应的数字
                    boolean isMatch = displayName.contains(prefixString)
//							|| getDisplayNameSpell(value).contains(prefixString)
                            || allFirstName.contains(prefixString)
                            || nameToNum.startsWith(prefixString);
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
            searchItems((ArrayList<ContactBookEntity>) results.values);
        }
    }

    private class ViewHolder{
        private ImageView mImvHead;
        private TextView mTxvName, mTxvChar;
        private CheckBox mChbSelected;
        private View mViewDivider;
    }
}
