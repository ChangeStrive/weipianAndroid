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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.CardcaseVCardEntity;
import com.maya.android.vcard.entity.ContactListItemEntity;
import com.maya.android.vcard.entity.result.UnitResultEntity;
import com.maya.android.vcard.util.ConverChineseCharToEnHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 *  名片夹主页名片适配器
 * Created by Administrator on 2015/9/15.
 */
public class CardcaseVCardAdapter extends BaseAdapter implements Filterable{
    private static final String TAG = CardcaseVCardAdapter.class.getName();
     /**布局控件**/
    private static final int VIEW_TYPE_COUNT = 2;
    /**单位Item布局 **/
    public static final int VIEW_TYPE_UNIT = 0;
    /**名片 Item布局 **/
    public static final int VIEW_TYPE_VCARD =1;
    private Context mContext;
    private ArrayList<CardcaseVCardEntity> mItems = new ArrayList<CardcaseVCardEntity>();
    /** 单位列表 **/
    private ArrayList<CardcaseVCardEntity> mUnitValues;
    /** 名片夹列表 过滤列表 **/
    private ArrayList<CardcaseVCardEntity> mOriginalValues = new ArrayList<CardcaseVCardEntity>();
    /** 联系人过滤 **/
    private ContactFilter mContactFilter;
     /** 是否显示字母标题**/
    private SparseBooleanArray mTitleItems = new SparseBooleanArray();
    /**记录选中项**/
    private SparseArray<CardcaseVCardEntity> mSelectedCardCaseVCard = new SparseArray<CardcaseVCardEntity>();
    /**z左侧列表是否隐藏**/
    private boolean isLeftHide = false;//默认隐藏
    /**是否隐藏选择项CheckBox**/
    private boolean isSelectHide = true;//默认隐藏
    /** 是否显示单位 **/
    private boolean isShowUnit = true;//默认显示
    /** 选项事件监听 **/
    private CardcaseVCardListener mListener;
    private Object mLock = new Object();

    public CardcaseVCardAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.mItems.size();
    }

    @Override
    public CardcaseVCardEntity getItem(int position) {
        CardcaseVCardEntity card = this.mItems.get(position);
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
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        // 获取单条回复
        int senType = VIEW_TYPE_VCARD;
        CardcaseVCardEntity entity = getItem(position);
        if(ResourceHelper.isNotNull(entity)){
            switch (entity.getSendType()){
                case VIEW_TYPE_UNIT:
                    //单位Item布局
                    senType = VIEW_TYPE_UNIT;
                    break;
                case VIEW_TYPE_VCARD:
                    //名片 Item布局
                    senType = VIEW_TYPE_VCARD;
                    break;
            }
        }
        return senType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderUnit  mHolderUnit = null;
        ViewHolderVCard mHolderVCard = null;
        CardcaseVCardEntity card = getItem(position);
        int senType = card.getSendType();
        if(ResourceHelper.isNull(convertView)){
            switch (senType){
                case VIEW_TYPE_UNIT:
                    //单位Item布局
                    convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_card_case_unit, parent, false);
                    mHolderUnit = new ViewHolderUnit();
                    mHolderUnit.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_head);
                    mHolderUnit.mTxvCompanyName = (TextView) convertView.findViewById(R.id.txv_company_name);
                    mHolderUnit.mTxvMemberNum = (TextView) convertView.findViewById(R.id.txv_member_num);
                    mHolderUnit.mTxvNotice = (TextView) convertView.findViewById(R.id.txv_notice);
                    mHolderUnit.mTxvChar = (TextView) convertView.findViewById(R.id.txv_char);
                    mHolderUnit.mRelMember = (RelativeLayout) convertView.findViewById(R.id.rel_member);
                    mHolderUnit.mViewDivider = convertView.findViewById(R.id.view_card_case_divider);
                    convertView.setTag(mHolderUnit);
                    break;
                case VIEW_TYPE_VCARD:
                    //名片 Item布局
                    convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_card_case_member, parent, false);
                    mHolderVCard = new ViewHolderVCard();
                    mHolderVCard.mImvHead = (AsyncImageView) convertView.findViewById(R.id.imv_head);
                    mHolderVCard.mTxvName = (TextView) convertView.findViewById(R.id.txv_name);
                    mHolderVCard.mTxvCompany = (TextView) convertView.findViewById(R.id.txv_company);
                    mHolderVCard.mTxvJob = (TextView) convertView.findViewById(R.id.txv_job);
                    mHolderVCard.mTxvChar = (TextView) convertView.findViewById(R.id.txv_char);
                    mHolderVCard.mImvBus = (ImageView) convertView.findViewById(R.id.imv_bus);
                    mHolderVCard.mChbSelect = (CheckBox) convertView.findViewById(R.id.chb_selected);
                    mHolderVCard.mRelMember = (RelativeLayout) convertView.findViewById(R.id.rel_member);
                    mHolderVCard.mViewDivider = convertView.findViewById(R.id.view_card_case_divider);
                    convertView.setTag(mHolderVCard);
                    break;
            }
        }else{
            switch (senType){
                case VIEW_TYPE_UNIT:
                    //单位Item布局
                    mHolderUnit = (ViewHolderUnit) convertView.getTag();
                    break;
                case VIEW_TYPE_VCARD:
                    //名片 Item布局
                    mHolderVCard = (ViewHolderVCard) convertView.getTag();
                    break;
            }
        }
        //获取左间距
        int leftPadding = 0;
        int rightPadding = (int)this.mContext.getResources().getDimension(R.dimen.dimen_15dp);
        if(this.isLeftHide){
            leftPadding = (int)this.mContext.getResources().getDimension(R.dimen.dimen_15dp);
        }else{
            leftPadding = (int)this.mContext.getResources().getDimension(R.dimen.dimen_10dp);
        }

        switch (senType){
            case VIEW_TYPE_UNIT:
                //单位Item布局
                UnitResultEntity unitDetail = card.getUnitDetailEntity();
                //标题栏
                if(position == 0){
                    mHolderUnit.mTxvChar.setVisibility(View.VISIBLE);
                    mHolderUnit.mTxvChar.setText(R.string.my_unit);
                    mHolderUnit.mViewDivider.setVisibility(View.GONE);
                }else{
                    mHolderUnit.mTxvChar.setVisibility(View.GONE);
                    mHolderUnit.mViewDivider.setVisibility(View.VISIBLE);
                }
                if(position == getCount() -1 || getItem(position + 1).getSendType() == VIEW_TYPE_VCARD){
                    mHolderUnit.mViewDivider.setVisibility(View.GONE);
                }
                mHolderUnit.mTxvChar.setPadding(leftPadding, 0, rightPadding , 0);
                mHolderUnit.mRelMember.setPadding(leftPadding, 0, rightPadding, 0);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mHolderUnit.mViewDivider.getLayoutParams();
                params.leftMargin = leftPadding;
                mHolderUnit.mViewDivider.setLayoutParams(params);
                //内容填充
                mHolderUnit.mImvHead.setDefaultImageResId(R.mipmap.img_unit_detail);
                ResourceHelper.asyncImageViewFillUrl(mHolderUnit.mImvHead, ResourceHelper.getImageUrlOnIndex(unitDetail.getHeadImg(), 0));
                mHolderUnit.mTxvCompanyName.setText(unitDetail.getEnterpriseName());
                mHolderUnit.mTxvMemberNum.setText(""+unitDetail.getMemberCount());
                mHolderUnit.mTxvNotice.setText(unitDetail.getAnnouncement());
                break;
            case VIEW_TYPE_VCARD:
                //名片 Item布局
                ContactListItemEntity vcardEntity = card.getVcardEntity();
                String curAlpha = ConverChineseCharToEnHelper.getFirstLetter(vcardEntity.getDisplayName()).toUpperCase();
                if( position == getCount() -1 || this.mTitleItems.get(position + 1, false)){
                    mHolderVCard.mViewDivider.setVisibility(View.GONE);
                }else{
                    mHolderVCard.mViewDivider.setVisibility(View.VISIBLE);
                }
                //标题栏
                if(this.mTitleItems.get(position, false)){
                    mHolderVCard.mTxvChar.setVisibility(View.VISIBLE);
                    mHolderVCard.mTxvChar.setText(curAlpha);
                }else{
                    mHolderVCard.mTxvChar.setVisibility(View.GONE);
                }
                mHolderVCard.mTxvChar.setPadding(leftPadding, 0, rightPadding , 0);
                mHolderVCard.mRelMember.setPadding(leftPadding, 0, rightPadding , 0);
                RelativeLayout.LayoutParams vcardParams = (RelativeLayout.LayoutParams) mHolderVCard.mViewDivider.getLayoutParams();
                vcardParams.leftMargin = leftPadding;
                mHolderVCard.mViewDivider.setLayoutParams(vcardParams);
                //内容填充
                if(Constants.DefaultUser.DEFAULT_USER_VCARD_NO.equals(vcardEntity.getVcardNo())){
                    mHolderVCard.mImvHead.setDefaultImageResId(R.mipmap.img_user_head_mytip);
                    mHolderVCard.mImvHead.setLoadFailImage(R.mipmap.img_user_head_mytip);
                }else {
                    mHolderVCard.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
                    ResourceHelper.asyncImageViewFillUrl(mHolderVCard.mImvHead, ResourceHelper.getImageUrlOnIndex(vcardEntity.getHeadImg(), 0));
                }
                mHolderVCard.mImvHead.setLoadFailImage(R.mipmap.img_upload_head);
                mHolderVCard.mTxvName.setText(vcardEntity.getDisplayName());
                mHolderVCard.mTxvCompany.setText(vcardEntity.getCompanyName());
                mHolderVCard.mTxvJob.setText(vcardEntity.getJob());
                if(0 == vcardEntity.getBusiness()){
                    mHolderVCard.mImvBus.setVisibility(View.GONE);
                } else {
                    mHolderVCard.mImvBus.setVisibility(View.VISIBLE);
                    mHolderVCard.mImvBus.setImageBitmap(ResourceHelper.getBusinessIconByCode(mContext, vcardEntity.getBusiness()));
                }
                //CheckBox管理
                if(this.isSelectHide){
                    mHolderVCard.mChbSelect.setVisibility(View.GONE);
                }else{
                     mHolderVCard.mChbSelect.setChecked(ResourceHelper.isNotNull(this.mSelectedCardCaseVCard.get(position, null)));
                     mHolderVCard.mChbSelect.setVisibility(View.VISIBLE);
                     final int mCurPosition = position;
                     mHolderVCard.mChbSelect.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             changeSelected(mCurPosition);
                         }
                     });
                }
                break;
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
     * 设置显示单位
     * @param isShowUnit
     */
    public void setShowUnit(boolean isShowUnit){
        this.isShowUnit = isShowUnit;
        this.notifyDataSetChanged();
    }

    /**
     * 获取当前单位状态
     * @return
     */
    public boolean getIsShowUnit(){
        return this.isShowUnit;
    }

    /**
     * 获取当前单位总数
     * @return
     */
    public int getUnitCount(){
        int result = 0;
        if(ResourceHelper.isNotNull(this.mUnitValues)){
            result = this.mUnitValues.size();
        }
        return result;
    }

    /**
     * 添加单位数据源
     * @param units
     */
    public void addUnitItems(ArrayList<UnitResultEntity> units){
        LogHelper.d(TAG, "有进入");
        if(ResourceHelper.isNotNull(units)){
            LogHelper.d(TAG, "单位数量" + units.size());
            ArrayList<CardcaseVCardEntity> items = new ArrayList<>();
            CardcaseVCardEntity entity = new CardcaseVCardEntity();
            for(UnitResultEntity unit : units){
                try {
                    entity = (CardcaseVCardEntity) entity.clone();
                    entity.setSendType(VIEW_TYPE_UNIT);
                    entity.setUnitDetailEntity(unit);
                    items.add(entity);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

            }
            this.mUnitValues = items;
        }
        this.notifyDataSetChanged();
    }

    /**
     * 添加联系人名片数据源
     * @param cards
     */
    public void addVCardItems(ArrayList<ContactListItemEntity> cards){
        this.mItems.clear();
        this.mSelectedCardCaseVCard.clear();
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.selectNum(this.mSelectedCardCaseVCard.size(), false);
        }
        if(ResourceHelper.isNotNull(cards)){
            ArrayList<CardcaseVCardEntity> items = new ArrayList<>();
            CardcaseVCardEntity entity = new CardcaseVCardEntity();
            for(ContactListItemEntity card : cards){
                try {
                    entity = (CardcaseVCardEntity) entity.clone();
                    entity.setSendType(VIEW_TYPE_VCARD);
                    entity.setVcardEntity(card);
                    items.add(entity);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
             }
//            Collections.sort(items, new EnterpriseMemberComparator());
            this.mItems.addAll(this.mItems.size(), items);
            this.mOriginalValues = items;
         }
        if(this.isShowUnit && ResourceHelper.isNotNull(this.mUnitValues)) {
            LogHelper.d(TAG, "有插入单位");
            this.mItems.addAll(0, this.mUnitValues);
        }
        isShowTitleAndDivider(this.mItems);
        notifyDataSetChanged();
    }

    /**
     * 移除所有选中项
     */
    public void removeVCardAllSelected(){
        int key;
        for(int i = 0, size = this.mSelectedCardCaseVCard.size(); i < size; i++){
            key = this.mSelectedCardCaseVCard.keyAt(i);
            CardcaseVCardEntity item = this.mSelectedCardCaseVCard.get(key, null);
            if(ResourceHelper.isNotNull(item)){
                this.mItems.remove(item);
            }
        }
        this.mSelectedCardCaseVCard.clear();
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.selectNum(this.mSelectedCardCaseVCard.size(), false);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除指定联系人
     */
    public void removeVCardItem(CardcaseVCardEntity entity){
        if(ResourceHelper.isNotNull(entity)){
            this.mItems.remove(entity);
            isShowTitleAndDivider(this.mItems);
        }
        notifyDataSetChanged();
    }

    /**
     * 全部选中
     */
    public void setSelectedAll(){
        boolean isCanSendMess = true;
        boolean isGoto = true;
        for(int i = 0, size = getCount(); i < size; i++){
            if(isGoto){
                CardcaseVCardEntity vcard = getItem(i);
                if(ResourceHelper.isNotNull(vcard)){
                    ContactListItemEntity card = vcard.getVcardEntity();
                    if(ResourceHelper.isNotNull(card) && ResourceHelper.isEmpty(card.getVcardNo())){
                        isCanSendMess = false;
                        isGoto = false;
                    }
                }
            }
            this.mSelectedCardCaseVCard.put(i, getItem(i));
        }
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.selectNum(this.mSelectedCardCaseVCard.size(), isCanSendMess);
        }
        notifyDataSetChanged();
    }

    /**
     * 取消所有选中项
     */
    public void setCancelSelectAll(){
        this.mSelectedCardCaseVCard.clear();
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.selectNum(this.mSelectedCardCaseVCard.size(), false);
        }
        notifyDataSetChanged();
    }

    /**
     * 是否隐藏选择项
     * @param isSelectHide
     */
    public void setHideSelect(boolean isSelectHide){
        this.isSelectHide = isSelectHide;
        if(this.isSelectHide){
            this.mSelectedCardCaseVCard.clear();
            if(ResourceHelper.isNotNull(this.mListener)){
                this.mListener.selectNum(this.mSelectedCardCaseVCard.size(), false);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 获取所有勾选的item
     * @return
     */
    public ArrayList<ContactListItemEntity> getSelecedeALl(){
        ArrayList<ContactListItemEntity> items = new ArrayList<ContactListItemEntity>();
        int key;
        for(int i = 0, size = this.mSelectedCardCaseVCard.size(); i < size; i++){
            key = this.mSelectedCardCaseVCard.keyAt(i);
            CardcaseVCardEntity item = this.mSelectedCardCaseVCard.get(key, null);
            if(ResourceHelper.isNotNull(item)){
                ContactListItemEntity vCard = item.getVcardEntity();
                if(ResourceHelper.isNotNull(vCard)){
                    items.add(vCard);
                }
            }
        }
        return items;
    }

    /**
     * 根据 字母的值找到存储的汉字首字母拼音的索引，listView中根据这个索引定位显示 * @param alpha
     * @return
     */
    public int getPosition(String alpha) {
        int position = -1;
        String firstStr;
        for (int i = 0, size = getCount(); i < size; i++) {
            CardcaseVCardEntity entity = getItem(i);
            if(ResourceHelper.isNotNull(entity)){
                firstStr = getFirstAlpha(entity.getVcardEntity());
                if (Helper.isNotEmpty(firstStr) && firstStr.startsWith(alpha)) {
                    position = i;
                    break;
                }
            }
        }
        return position;
    }

    /**
     * 右侧列表是否隐藏
     * @param isLeftHide
     */
    public void setLeftHide(boolean isLeftHide){
        this.isLeftHide = isLeftHide;
        notifyDataSetChanged();
    }

    /**
     * 选中事件监听
     * @param mListener
     */
    public void setCardcaseVCardListener(CardcaseVCardListener mListener){
        this.mListener = mListener;
    }

    /**
     * 选中项事件
     * @param position
     */
    private void changeSelected(int position){
        boolean isSelected = ResourceHelper.isNull(this.mSelectedCardCaseVCard.get(position, null));
        if(isSelected){
            this.mSelectedCardCaseVCard.put(position, getItem(position));
        }else{
            this.mSelectedCardCaseVCard.remove(position);
        }
        boolean isCanSendMess = true;
        int size = this.mSelectedCardCaseVCard.size();
        for(int i = 0; i < size; i++){
            int key = this.mSelectedCardCaseVCard.keyAt(i);
            CardcaseVCardEntity vcard = this.mSelectedCardCaseVCard.get(key, null);
            if(ResourceHelper.isNotNull(vcard)){
                ContactListItemEntity card = vcard.getVcardEntity();
                if(ResourceHelper.isNotNull(card) && ResourceHelper.isEmpty(card.getVcardNo())){
                    isCanSendMess = false;
                    break;
                }
            }
        }
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.selectNum(size, isCanSendMess);
        }
        notifyDataSetChanged();
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
    private void isShowTitleAndDivider(ArrayList<CardcaseVCardEntity> items){
        int idx = 0;
        String curAlpha = "";
        String preAlpha = "";
        this.mTitleItems.clear();
        CardcaseVCardEntity curCard;//当前item项
        CardcaseVCardEntity preCard;//上一个item项
        for(int i = 0, size = items.size(); i < size; i++){
            curCard = items.get(i);
            if(i == 0){
                this.mTitleItems.put(i, true);
            }else if(curCard.getSendType() == VIEW_TYPE_UNIT){
                continue;
            }
            idx = i - 1;
            if(idx >= 0){
                preCard = getItem(idx);
                if(preCard.getSendType() == VIEW_TYPE_UNIT){
                    this.mTitleItems.put(i, true);
                }else{
                    curAlpha = ConverChineseCharToEnHelper.getFirstLetter(curCard.getVcardEntity().getDisplayName()).toUpperCase();
                    preAlpha = ConverChineseCharToEnHelper.getFirstLetter(preCard.getVcardEntity().getDisplayName()).toUpperCase();
                    if(!curAlpha.equals(preAlpha.toUpperCase())){
                        this.mTitleItems.put(i, true);
                    }
                }
            }


        }
    }

   /**
     * 添加过滤数据源
     * @param items
     */
    private void filterAddItems(ArrayList<CardcaseVCardEntity> items){
        this.mItems.clear();
        this.mSelectedCardCaseVCard.clear();
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.selectNum(this.mSelectedCardCaseVCard.size(), false);
        }
        if(this.isShowUnit && ResourceHelper.isNotNull(this.mUnitValues)){
            this.mItems.addAll(this.mUnitValues);
        }
        if(ResourceHelper.isNotNull(items)){
            this.mItems.addAll(getCount(), items);
            isShowTitleAndDivider(this.mItems);
        }
        notifyDataSetChanged();
    }

     /**
     * 行业切换搜索
     * @param busCode
     */
    public void switchBusiness(int busCode){
        if(busCode < 1){
            this.mItems.clear();
            if(this.isShowUnit && ResourceHelper.isNotNull(this.mUnitValues)){
                this.mItems.addAll(this.mUnitValues);
            }
             this.mItems.addAll(getCount(), this.mOriginalValues);
            isShowTitleAndDivider(this.mItems);
            notifyDataSetChanged();
        }else{
            ArrayList<CardcaseVCardEntity> newList = new ArrayList<CardcaseVCardEntity>();
            for(int i = 0, size = this.mOriginalValues.size(); i < size; i++){
                CardcaseVCardEntity curItem = this.mOriginalValues.get(i);
                ContactListItemEntity card = curItem.getVcardEntity();
                if(busCode == card.getBusiness() && !newList.contains(curItem)){
                    newList.add(curItem);
                }
                filterAddItems(newList);
            }
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
                final ArrayList<CardcaseVCardEntity> values = mOriginalValues;
                final int count = values.size();
                // 新的集合对象
                ArrayList<CardcaseVCardEntity> newValues = new ArrayList<CardcaseVCardEntity>(count);
                for (int i = 0; i < count; i++) {
                    // 如果姓名的前缀相符或者电话相符或职位相符或公司名称前缀相符 就添加到新的集合
                    CardcaseVCardEntity value = values.get(i);
                    ContactListItemEntity mValue = value.getVcardEntity();
                    String nameSpell = mValue.getOrderCode().toLowerCase();
                    String allFirstName = ConverChineseCharToEnHelper.converToAllFirstSpellsUppercase(mValue.getDisplayName());
                    String nameToNum = ConverChineseCharToEnHelper.converChinesToNumber(allFirstName);
                    String displayName = ResourceHelper.isEmpty(mValue.getDisplayName()) ? "" : mValue.getDisplayName();
                    String job = ResourceHelper.isNull(mValue.getJob()) ? "" : mValue.getJob();

                    // 姓名  || 姓名拼音 || 姓名拼音首字母  || 姓名首字母拼音对应的数字 || 职位 || 公司名称
                    boolean isMatch = displayName.contains(prefixString)
                            || nameSpell.contains(prefixString.toLowerCase())
                            || allFirstName.contains(prefixString)
                            || nameToNum.startsWith(prefixString)
                            || job.contains(prefixString)
                            || mValue.getCompanyName().contains(prefixString) ;
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
            filterAddItems((ArrayList<CardcaseVCardEntity>) results.values);
        }
    }

    private class ViewHolderUnit{
        private AsyncImageView mImvHead;
        private TextView mTxvCompanyName, mTxvNotice, mTxvMemberNum, mTxvChar;
        private RelativeLayout mRelMember;
        private View mViewDivider;
    }

    private class ViewHolderVCard{
        private AsyncImageView mImvHead;
        private TextView mTxvName, mTxvJob, mTxvCompany, mTxvChar;
        private ImageView mImvBus;
        private CheckBox mChbSelect;
        private RelativeLayout mRelMember;
        private View mViewDivider;
    }

    public interface CardcaseVCardListener{
        /**
         *
         * @param num 选中的个数
         * @param isCanSendMess 是否可以发送消息
         */
        void selectNum(int num, boolean isCanSendMess);
    }

}
