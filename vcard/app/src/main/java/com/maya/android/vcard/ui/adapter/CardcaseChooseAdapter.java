package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.ContactListItemEntity;
import com.maya.android.vcard.util.ConverChineseCharToEnHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *  名片夹主页 联系人选择、转发名片等 适配器
 * Created by Administrator on 2015/9/15.
 */
public class CardcaseChooseAdapter extends BaseAdapter implements Filterable{
    private Context mContext;
    private ArrayList<ContactListItemEntity> contactLsvItem = new ArrayList<ContactListItemEntity>();
    /** 是否显示字母标题**/
    private SparseBooleanArray mTitleItems = new SparseBooleanArray();
    /**记录选中项**/
    private SparseArray<ContactListItemEntity> mSelectedCardCaseVCard = new SparseArray<ContactListItemEntity>();
    /** 过滤列表 **/
    private ArrayList<ContactListItemEntity> mOriginalValues = new ArrayList<ContactListItemEntity>();
    /** 联系人过滤 **/
    private ContactFilter mContactFilter;
    /**是否隐藏选择项CheckBox**/
    private boolean isSelectHide = true;//默认隐藏
    /** true 单选  false 多选**/
    private boolean isSingleChoose = true;//默认单选
    private final Object mLock = new Object();
    /** 事件监听 **/
    private OnItemCardcaseChooseListener mListener;

    public CardcaseChooseAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.contactLsvItem.size();
    }

    @Override
    public ContactListItemEntity getItem(int position) {
        ContactListItemEntity card = this.contactLsvItem.get(position);
        if(ResourceHelper.isNotNull(card)){
            return card;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderVCard mHolderVCard = null;
        if(ResourceHelper.isNull(convertView)){
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_card_case_choose, parent, false);
            mHolderVCard = new ViewHolderVCard();
            mHolderVCard.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_head);
            mHolderVCard.mTxvName = (TextView) convertView.findViewById(R.id.txv_name);
            mHolderVCard.mTxvCompany = (TextView) convertView.findViewById(R.id.txv_company);
            mHolderVCard.mTxvJob = (TextView) convertView.findViewById(R.id.txv_job);
            mHolderVCard.mTxvChar = (TextView) convertView.findViewById(R.id.txv_char);
            mHolderVCard.mImvBus = (ImageView) convertView.findViewById(R.id.imv_bus);
            mHolderVCard.mImvSelect = (ImageView) convertView.findViewById(R.id.imv_selected);
            mHolderVCard.mRelMember = (RelativeLayout) convertView.findViewById(R.id.rel_member);
            mHolderVCard.mViewDivider = (View) convertView.findViewById(R.id.view_card_case_divider);
            convertView.setTag(mHolderVCard);
        }else{
            mHolderVCard = (ViewHolderVCard) convertView.getTag();
        }
        ContactListItemEntity vcardEntity = getItem(position);
        //分割线
        if( position == getCount() -1 || this.mTitleItems.get(position + 1, false)){
            mHolderVCard.mViewDivider.setVisibility(View.GONE);
        }else{
            mHolderVCard.mViewDivider.setVisibility(View.VISIBLE);
        }
        //标题栏
        String curAlpha = ConverChineseCharToEnHelper.getFirstLetter(vcardEntity.getDisplayName()).toUpperCase();
        if(this.mTitleItems.get(position, false)){
            mHolderVCard.mTxvChar.setVisibility(View.VISIBLE);
            mHolderVCard.mTxvChar.setText(curAlpha);
        }else{
            mHolderVCard.mTxvChar.setVisibility(View.GONE);
        }
        mHolderVCard.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
        ResourceHelper.asyncImageViewFillUrl(mHolderVCard.mImvHead, vcardEntity.getHeadImg());
        mHolderVCard.mTxvName.setText(vcardEntity.getDisplayName());
        mHolderVCard.mTxvCompany.setText(vcardEntity.getCompanyName());
        mHolderVCard.mTxvJob.setText(vcardEntity.getJob());
        if(0 == vcardEntity.getBusiness()){
            mHolderVCard.mImvBus.setVisibility(View.GONE);
        } else {
            mHolderVCard.mImvBus.setVisibility(View.VISIBLE);
            mHolderVCard.mImvBus.setImageBitmap(ResourceHelper.getBusinessIconByCode(this.mContext, vcardEntity.getBusiness()));
        }
        //CheckBox管理
        if(!this.isSelectHide){
            boolean isSelected = ResourceHelper.isNotNull(this.mSelectedCardCaseVCard.get(position, null));
            if(isSelected){
                 mHolderVCard.mImvSelect.setVisibility(View.VISIBLE);
                 mHolderVCard.mImvSelect.setImageResource(R.mipmap.img_general_is_agree);
            }else{
                mHolderVCard.mImvSelect.setImageResource(R.mipmap.img_general_no_agree);
                if(this.isSingleChoose){
                    mHolderVCard.mImvSelect.setVisibility(View.GONE);
                }else{
                    mHolderVCard.mImvSelect.setVisibility(View.VISIBLE);
                }
            }
        }else {
            mHolderVCard.mImvSelect.setVisibility(View.GONE);
        }
         return convertView;
    }


    @Override
    public Filter getFilter() {
        if(mContactFilter == null){
            mContactFilter = new ContactFilter();
        }
        return mContactFilter;
    }

    /**
     * 添加数据源
     * @param cards
     */
    public void addItems(ArrayList<ContactListItemEntity> cards){
        addItems(cards, false);
    }

    /**
     * 添加数据源
     * @param cards
     * @param isContactFilter 是否来源于过滤
     */
    public void addItems(ArrayList<ContactListItemEntity> cards, boolean isContactFilter){
        this.contactLsvItem.clear();
        this.mSelectedCardCaseVCard.clear();
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.selectCount(this.mSelectedCardCaseVCard.size(), false);
        }
         if(ResourceHelper.isNotNull(cards)){
            Collections.sort(cards, new EnterpriseMemberComparator());
            this.contactLsvItem.addAll(cards);
            if(!isContactFilter){
                this.mOriginalValues = cards;
            }
            isShowTitleAndDivider(this.contactLsvItem);
         }
        this.mSelectedCardCaseVCard.clear();
        notifyDataSetChanged();
    }

    /**
     * 设置全部选中
     */
    public void setSelectedAll() {
        setIsSelectHide(false);
        boolean isCanSend = true;
        boolean isGoto = true;
        this.mSelectedCardCaseVCard.clear();
        for(int i = 0, len = getCount(); i < len; i++){
            if(isGoto){
                ContactListItemEntity card = getItem(i);
                if(ResourceHelper.isNotNull(card) && ResourceHelper.isEmpty(card.getVcardNo())){
                    isCanSend = false;
                    isGoto = false;
                }
            }
            this.mSelectedCardCaseVCard.put(i, getItem(i));
        }
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.selectCount(this.mSelectedCardCaseVCard.size(), isCanSend);
        }
        notifyDataSetChanged();
    }

    /**
     * 取消所有选中项
     */
    public void setCancelSelectAll(){
        this.mSelectedCardCaseVCard.clear();
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.selectCount(this.mSelectedCardCaseVCard.size(), false);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置选择项(CheckBox)勾选状态
     * @param position
     */
    public void setSelect(int position){
        if(this.isSingleChoose){//单选
            this.mSelectedCardCaseVCard.clear();
        }
        boolean isSelected = ResourceHelper.isNull(this.mSelectedCardCaseVCard.get(position, null));
        if(isSelected){
            this.mSelectedCardCaseVCard.put(position, getItem(position));
         }else{
            this.mSelectedCardCaseVCard.remove(position);
        }
        int size = this.mSelectedCardCaseVCard.size();
        boolean isCanSend = true;
        if(size > 0){
            for (int i = 0; i < size; i++){
                int key =  this.mSelectedCardCaseVCard.keyAt(i);
                ContactListItemEntity card = this.mSelectedCardCaseVCard.get(key, null);
                if(ResourceHelper.isNotNull(card) && ResourceHelper.isEmpty(card.getVcardNo())){
                    isCanSend = false;
                    break;
                }
            }
        }
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.selectCount(size, isCanSend);
        }
        notifyDataSetChanged();
    }

    /**
     * 是否隐藏选择项
     * @param isSelectHide
     */
    public void setIsSelectHide(boolean isSelectHide){
        this.isSelectHide = isSelectHide;
        notifyDataSetChanged();
    }

    /**
     * 设置 true单选  false 多选
     * @param isSingleChoose
     */
    public void setIsSingleChoose(boolean isSingleChoose){
        this.isSingleChoose = isSingleChoose;
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
            if (Helper.isNotEmpty(firstStr) && firstStr.startsWith(alpha)) {
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * 勾选数量改变监听
     * @param mListener
     */
    public void setOnItemCardcaseChooseListener(OnItemCardcaseChooseListener mListener){
        this.mListener = mListener;
    }

    /**
     * 获取选中的对象
     * @return
     */
    public ArrayList<ContactListItemEntity>  getSelectedCards(){
        ArrayList<ContactListItemEntity> items = new ArrayList<ContactListItemEntity>();
        int key;
        for(int i = 0, size = this.mSelectedCardCaseVCard.size(); i < size; i++){
            key = this.mSelectedCardCaseVCard.keyAt(i);
            ContactListItemEntity item = this.mSelectedCardCaseVCard.get(key, null);
            if(ResourceHelper.isNotNull(item)){
                items.add(item);
            }
        }
        return items;
    }

    /**
     * 获取首字母
     * @param card
     * @return String
     */
    private String getFirstAlpha(ContactListItemEntity card){
        if(ResourceHelper.isNotNull(card)){
            return ConverChineseCharToEnHelper.getFirstLetter(card.getDisplayName()).toUpperCase();
        }
        return "";
    }

    /**
     * 记录需显示标题和下划线的地方
     * @param items
     */
    private void isShowTitleAndDivider(ArrayList<ContactListItemEntity> items){
        int idx = 0;
        String curAlpha = "";
        String preAlpha = "";
        this.mTitleItems.clear();
        for(int i = 0, size = items.size(); i < size; i++){
            if(i == 0){
                this.mTitleItems.put(i, true);
            }else{
                ContactListItemEntity curCard = items.get(i);
                curAlpha = ConverChineseCharToEnHelper.getFirstLetter(curCard.getDisplayName()).toUpperCase();
                idx = i - 1;
                preAlpha = idx >= 0 ? ConverChineseCharToEnHelper.getFirstLetter(getItem(idx).getDisplayName()) : "";
                if(!curAlpha.equals(preAlpha.toUpperCase())){
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
    class EnterpriseMemberComparator implements Comparator<ContactListItemEntity> {

        @Override
        public int compare(ContactListItemEntity o1, ContactListItemEntity o2) {
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
     * 获取名称的拼音
     * @param person
     * @return String
     */
    private String getDisplayNameSpell(ContactListItemEntity person){
        String displayName = ConverChineseCharToEnHelper.converToAllFirstSpellsLowercase(person.getDisplayName());
        return displayName;
    }

    /**
     * 行业切换搜索
     * @param busCode
     */
    public void switchBusiness(int busCode){
        if(busCode < 1){
            addItems(this.mOriginalValues, true);
        }else{
            ArrayList<ContactListItemEntity> newList = new ArrayList<ContactListItemEntity>();
            for(int i = 0, size = this.mOriginalValues.size(); i < size; i++){
                ContactListItemEntity curItem = mOriginalValues.get(i);
                if(busCode == curItem.getBusiness() && !newList.contains(curItem)){
                    newList.add(curItem);
                }
            }
            addItems(newList, true);
        }
    }

    /**
     * 联系人过滤 * @author zheng_cz
     * */
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
                final ArrayList<ContactListItemEntity> values = mOriginalValues;

                final int count = values.size();

                // 新的集合对象
                ArrayList<ContactListItemEntity> newValues = new ArrayList<ContactListItemEntity>(count);

                for (int i = 0; i < count; i++) {
                    // 如果姓名的前缀相符或者电话相符或职位相符或公司名称前缀相符 就添加到新的集合
                    ContactListItemEntity value = (ContactListItemEntity) values.get(i);

                    String nameSpell = value.getOrderCode().toLowerCase();
                    String allFirstName = ConverChineseCharToEnHelper.converToAllFirstSpellsUppercase(value.getDisplayName());
                    String nameToNum = ConverChineseCharToEnHelper.converChinesToNumber(allFirstName);
                    String displayName = Helper.isEmpty(value.getDisplayName()) ? "" : value.getDisplayName();
                    String job = Helper.isNull(value.getJob()) ? "" : value.getJob();

                    // 姓名  || 姓名拼音 || 姓名拼音首字母  || 姓名首字母拼音对应的数字 || 职位 || 公司名称
                    boolean isMatch = displayName.contains(prefixString)
                            || nameSpell.contains(prefixString.toLowerCase())
                            || allFirstName.contains(prefixString)
                            || nameToNum.startsWith(prefixString)
                            || job.contains(prefixString)
                            || value.getCompanyName().contains(prefixString) ;
                    // ||
                    // value.getCard().getMobileTelphone().startsWith(prefixString)
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
            addItems((ArrayList<ContactListItemEntity>) results.values, true);
        }
     }

    private class ViewHolderVCard{
        private AsyncImageView mImvHead;
        private TextView mTxvName, mTxvJob, mTxvCompany, mTxvChar;
        private ImageView mImvBus;
        private ImageView mImvSelect;
        private RelativeLayout mRelMember;
        private View mViewDivider;
    }

    public interface OnItemCardcaseChooseListener{
        /**
         * 选中项数量改变监听
         * @param count 选中项的个数
         */
        void selectCount(int count, boolean isSend);
    }
}
