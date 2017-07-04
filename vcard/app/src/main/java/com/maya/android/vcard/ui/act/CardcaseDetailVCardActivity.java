package com.maya.android.vcard.ui.act;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.ContactVCardDao;
import com.maya.android.vcard.entity.CardTouchPadEntity;
import com.maya.android.vcard.entity.ContactEntity;
import com.maya.android.vcard.entity.EmailSendEntity;
import com.maya.android.vcard.entity.TitleMoreMenuLsvIconEntity;
import com.maya.android.vcard.ui.adapter.CustomLsvAdapter;
import com.maya.android.vcard.ui.base.BaseActivity;
import com.maya.android.vcard.ui.frg.CardcaseChooseFragment;
import com.maya.android.vcard.ui.frg.MessageConversationFragment;
import com.maya.android.vcard.ui.frg.UserInfoFragment;
import com.maya.android.vcard.ui.frg.VCardInfoFragment;
import com.maya.android.vcard.ui.widget.CardDetailsImageView;
import com.maya.android.vcard.ui.widget.CardDetailsMaxImageView;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.TitleMoreMenuPopView;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.util.TitleMoreMenuPopHelper;

import java.util.ArrayList;

/**
 * 名片夹详情页
 */
public class CardcaseDetailVCardActivity extends BaseActivity {
    private static final String TAG = CardcaseDetailVCardActivity.class.getSimpleName();
    /** 微片小秘书 微片号 TODO: 由于某人设下的坑，7位的微片号 */
    public static final String DEFAULT_USER_VCARD_NO_OLD = "1000000";
    public static final String GROUP_ID = "GROUP_ID";
    public static final String GROUP_NAME = "GROUP_NAME";
    public static final String GROUP_POSITON = "GROUP_POSITON";
    /**名片Key编号(联系人键值对 Id)**/
    private int mCurContactKeyId = 0;
    /** 当前名片 Id**/
    private long mCurCardId;
    /**当前名片账户Id**/
    private long mCurAccountId;
    /** 当前名片微片号 **/
    private String mCurVcardNo;
    /**当前名片用户名**/
    private String mCurDisplayName;
    /** 当前名片公司网址 **/
    private String mCurEnterpriseUrl;
    /** 当前头像链接 **/
    private String mCurHeadPath;
    /**电话列表**/
    private ArrayList<String> mCurPhoneList;
    /** 当前名片手机 **/
    private String mCurMobile;
    /** 当前名片E-mial **/
    private String mCurEmail;
    /**当前名片总数**/
    private int cardCount;
    /** 右上角弹窗不可点击项 **/
    private ArrayList<Integer> mMoreMenuUnEnableList = new ArrayList<Integer>();
    /**填充内容控件**/
    private AsyncImageView mImvHead;
    private ImageView mImvVip, mImvGride, mImvSex, mImvVerify, mImvBusiness;
    private TextView mTxvName, mTxvVcardSNo, mTxvAge, mTxvConstellation, mTxvProvince, mTxvCity, mTxvCurcardPosition, mTxvCurCardBigPosition, mTxvCompany;
    private LinearLayout mLilSex;
    /**单击事件控件**/
    private Button mbtnCardDetail, mBtnWeiSite, mBtnWeiAblum, mBtnWeiNotice;
    private TextView mTxvCall, mTxvSendMess, mTxvSendEmail, mTxvMapavigation;
    private int mGroupId;
    private int mCurPosition;
    private String mTitle;
    private ArrayList<ContactEntity> mContactEntityList;
    private ViewPager mVwpVCard, mVwpBigVCard;
    private RelativeLayout mRelBigVCard, mRelHead;
    private CardcaseDetailImagePagerAdapter mCardcaseDetailImagePagerAdapter;
    private CardcaseBigVCardImagePagerAdapter mCardcaseBigVCardImagePagerAdapter;
    private TitleMoreMenuPopView mTitleMoreMenu;

    private CustomDialogFragment mDialogFragmentCallPhone, mDialogFragmentNetwork;
    /** 拨号适配器 **/
    private CustomLsvAdapter mLsvCallAdapter;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_card_detail:
                    //名片信息
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, VCardInfoFragment.class.getName());
                    if(ResourceHelper.isEmpty(mCurVcardNo)){
                        bundle.putInt(VCardInfoFragment.IS_SHOW_CURRENT_VCARD, VCardInfoFragment.VCARD_INO_OTHER_CONTCAT_CARD);
                        bundle.putInt(Constants.IntentSet.INTENT_KEY_CONTACT_ID, mCurContactKeyId);
                    }else{
                        bundle.putInt(VCardInfoFragment.IS_SHOW_CURRENT_VCARD, VCardInfoFragment.VCARD_INO_OTHER_OLINE_CARD);
                        bundle.putLong(Constants.IntentSet.INTENT_KEY_VCARD_ID, mCurCardId);
                     }
                    bundle.putString(Constants.IntentSet.INTENT_KEY_VCARD_NAME, mCurDisplayName);
                    intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                    switchTo(UnitMainActivity.class, intent);
                    break;
                case R.id.btn_we_web_site:
                    //TODO 微网站
                    break;
                case R.id.btn_picture_album:
                    //TODO 微画册
                    break;
                case R.id.btn_we_notice:
                    //TODO 微公告
                    break;
                case R.id.txv_call:
                    //拨打电话
                    callPhone(mCurPhoneList);
                    break;
                case R.id.txv_send_mess:
                    //发送短信
                    sendMess();
                    break;
                case R.id.txv_send_email:
                    //发送邮件
                    sendEmail();
                    break;
                case R.id.txv_map_navigation:
                    //TODO 地图导航
                    break;
                case R.id.rel_head:
                    //名片联系人用户资料
                    Intent toUserInfoIntent = new Intent();
                    Bundle toUserInfoBundle = new Bundle();
                    toUserInfoIntent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserInfoFragment.class.getName());
                    toUserInfoBundle.putInt(UserInfoFragment.KEY_SHOW_USER, UserInfoFragment.CODE_SHOW_CONTACT_USER_INFO);
                    toUserInfoBundle.putLong(UserInfoFragment.KEY_CARD_ID, mCurCardId);
                    toUserInfoIntent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, toUserInfoBundle);
                    switchTo(UserMainActivity.class, toUserInfoIntent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardcase_detail_vcard);
        this.initUI();
        this.initData();
    }

    @Override
    public void switchTo(Class<?> cls, Intent intent) {
        if(Helper.isNull(intent)){
            intent = new Intent();
        }
        ActivityHelper.switchTo(this, cls, intent);
    }

    private void initUI(){
        super.initTop();
        super.setTopLeftVisibility(View.VISIBLE);

        this.mImvHead = findView(R.id.imv_user_head);
        this.mImvVip = findView(R.id.imv_user_head_vip);
        this.mImvGride = findView(R.id.imv_user_head_grade);
        this.mImvSex = findView(R.id.imv_user_sex);
        this.mImvVerify = findView(R.id.imv_user_head_verify);
        this.mImvBusiness = findView(R.id.imv_user_head_bus);
        this.mTxvName = findView(R.id.txv_user_name);
        this.mTxvVcardSNo = findView(R.id.txv_user_vcardsno);
        this.mTxvAge = findView(R.id.txv_user_age);
        this.mTxvConstellation = findView(R.id.txv_user_constellation);
        this.mTxvProvince = findView(R.id.txv_user_province);
        this.mTxvCity = findView(R.id.txv_user_city);
        this.mTxvCurcardPosition = findView(R.id.txv_cur_card_positon);
        this.mbtnCardDetail = findView(R.id.btn_card_detail);
        this.mBtnWeiSite = findView(R.id.btn_we_web_site);
        this.mBtnWeiAblum = findView(R.id.btn_picture_album);
        this.mBtnWeiNotice = findView(R.id.btn_we_notice);
        this.mTxvCall = findView(R.id.txv_call);
        this.mTxvSendMess = findView(R.id.txv_send_mess);
        this.mTxvSendEmail = findView(R.id.txv_send_email);
        this.mTxvMapavigation = findView(R.id.txv_map_navigation);
        this.mLilSex =  findView(R.id.lil_sex);
        this.mRelHead = findView(R.id.rel_head);
        this.mTxvCompany = findView(R.id.txv_company);
        this.mbtnCardDetail.setOnClickListener(this.mOnClickListener);
        this.mBtnWeiSite.setOnClickListener(this.mOnClickListener);
        this.mBtnWeiAblum.setOnClickListener(this.mOnClickListener);
        this.mBtnWeiNotice.setOnClickListener(this.mOnClickListener);
        this.mTxvCall.setOnClickListener(this.mOnClickListener);
        this.mTxvSendMess.setOnClickListener(this.mOnClickListener);
        this.mTxvSendEmail.setOnClickListener(this.mOnClickListener);
        this.mTxvMapavigation.setOnClickListener(this.mOnClickListener);
        this.mTxvMapavigation.setOnClickListener(this.mOnClickListener);
        this.mRelHead.setOnClickListener(this.mOnClickListener);
        this.mRelBigVCard = findView(R.id.rel_big_vcard);
        this.mTxvCurCardBigPosition = findView(R.id.txv_cur_big_card_positon);
        this.mVwpVCard = findView(R.id.vwp_card);
        this.mVwpBigVCard = findView(R.id.vwp_big_card);
        this.mCardcaseDetailImagePagerAdapter = new CardcaseDetailImagePagerAdapter();
        this.mCardcaseBigVCardImagePagerAdapter = new CardcaseBigVCardImagePagerAdapter();
        this.mVwpVCard.setAdapter(this.mCardcaseDetailImagePagerAdapter);
        this.mVwpBigVCard.setAdapter(this.mCardcaseBigVCardImagePagerAdapter);
        this.mVwpVCard.setOnPageChangeListener(new VCardPageChangeListener());
        this.mVwpBigVCard.setOnPageChangeListener(new BigVCardPageChangeListener());
    }

    private void initData(){
        Intent arg = getIntent();
        if(Helper.isNotNull(arg)){
            this.mGroupId = arg.getIntExtra(GROUP_ID, 0);
            this.mCurPosition = arg.getIntExtra(GROUP_POSITON, 0);
        }
        LogHelper.d(TAG, "发送:groupId:" + this.mGroupId + " position:" +  this.mCurPosition);
        this.mTitle = ContactVCardDao.getInstance().getGroupName(this.mGroupId);
        if(ResourceHelper.isEmpty(this.mTitle)){
            this.mTitle = getString(R.string.all);
        }
        this.mContactEntityList = ContactVCardDao.getInstance().getListForAll(this.mGroupId);

        if(ResourceHelper.isNotNull(this.mContactEntityList)){
            this.cardCount = this.mContactEntityList.size();
        }
        super.setActivityTitle(this.mTitle + getString(R.string.card_num, cardCount), false);
        super.setActivityTopRightImvVisibility(View.VISIBLE);
        super.setActivityTopRightImv(R.mipmap.img_top_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTitleMoreMenu(v);
            }
        });

        this.mCardcaseDetailImagePagerAdapter.addItems(this.mContactEntityList);
        this.mCardcaseBigVCardImagePagerAdapter.addItems(this.mContactEntityList);
        this.mVwpVCard.setCurrentItem(this.mCurPosition);
        if(this.mCurPosition == 0){
            fillVCardData(this.mCurPosition);
        }
    }

    /**
     * 右上角弹窗
     */
    private void showTitleMoreMenu(View v){
        if(ResourceHelper.isNull(this.mTitleMoreMenu)){
            this.mTitleMoreMenu = TitleMoreMenuPopHelper.getVcardDetailPop(this);
            this.mTitleMoreMenu.setItemClickListener(new TitleMoreMenuPopView.MoreMenuItemClickListener() {
                @Override
                public void onItemClick(TitleMoreMenuLsvIconEntity menu) {
                        switch (menu.getBusinessName()) {
                            case R.string.pop_telephone:
                                //拨打电话
                                callPhone(mCurPhoneList);
                                break;
                            case R.string.pop_send_msg:
                                //发送短信
                                sendMess();
                                break;
                            case R.string.pop_send_email:
                                //发送邮件
                                sendEmail();
                                break;
                            case R.string.pop_navigation:
                                //TODO 地图导航
                                break;
                            case R.string.pop_vcard_forward:
                                //转发名片
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, CardcaseChooseFragment.class.getName());
                                bundle.putLong(Constants.IntentSet.INTENT_KEY_VCARD_ID, mCurCardId);
                                bundle.putLong(Constants.IntentSet.INTENT_KEY_ACCOUNT_ID, mCurAccountId);
                                bundle.putInt(CardcaseChooseFragment.KEY_CARDCASE_CHOOSE, CardcaseChooseFragment.CARDCASE_CHOOSE_SELECT_VCARD_FORWARD);
                                intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                                switchTo(CardcaseMainActivity.class, intent);
                                break;
                            case R.string.pop_vcard_del:
                                //TODO 删除名片
                                break;
                            case R.string.pop_report:
                                //TODO 拉黑举报
                                break;
                        }
                 }
            });
        }
        this.mMoreMenuUnEnableList.clear();
        if (this.mCurCardId == 0 && Helper.isEmpty(this.mCurVcardNo)) {
            //设置不可点击项
            this.mMoreMenuUnEnableList.add(3);
            this.mMoreMenuUnEnableList.add(4);
            this.mMoreMenuUnEnableList.add(6);
        }else if(Helper.equalString(this.mCurVcardNo, Constants.DefaultUser.DEFAULT_USER_VCARD_NO, false)){
            //设置不可点击项
            this.mMoreMenuUnEnableList.add(3);
            this.mMoreMenuUnEnableList.add(4);
            this.mMoreMenuUnEnableList.add(5);
            this.mMoreMenuUnEnableList.add(6);
        } else {
            this.mMoreMenuUnEnableList.add(6);
        }
        this.mTitleMoreMenu.setItemUnEnable(this.mMoreMenuUnEnableList);
        this.mTitleMoreMenu.showAtLocation(v, Gravity.FILL, 0, 0);
    }

    private void showOrHideMax(){
        if (Helper.isNotNull(this.mRelBigVCard)){
            if(this.mRelBigVCard.getVisibility() == View.GONE){
                this.mRelBigVCard.setVisibility(View.VISIBLE);
                this.mVwpBigVCard.setCurrentItem(this.mCurPosition);
            }else{
                this.mRelBigVCard.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 填充数据(赋值)
     * @param position
     */
    private void fillVCardData(int position){
        this.mCurPosition = position;
        ContactEntity cardEntity = this.mContactEntityList.get(this.mCurPosition);
        if(Helper.isNotNull(cardEntity)){
            this.mCurContactKeyId = cardEntity.getId();
            this.mCurCardId = cardEntity.getCardId();
            this.mCurVcardNo = cardEntity.getVcardNo();
            this.mCurAccountId = cardEntity.getAccountId();
            this.mCurDisplayName = cardEntity.getDisplayName();
            this.mCurEnterpriseUrl = cardEntity.getCompanyHome();
            // 排除小秘书被更新
            if (this.mCurCardId != 1l) {
                // TODO 更新联系人
            }
            this.mCurHeadPath = ResourceHelper.getHttpImageFullUrl(ResourceHelper.getImageUrlOnIndex(cardEntity.getHeadImg(), 0));
            if (ResourceHelper.isNotEmpty(this.mCurVcardNo)) {
                this.mTxvVcardSNo.setText(getString(R.string.frg_setting_account_vnumber)+ this.mCurVcardNo);
                String birthday = cardEntity.getBirthday();
                this.mLilSex.setVisibility(View.VISIBLE);
                this.mTxvCompany.setVisibility(View.GONE);
                this.mTxvAge.setText(ResourceHelper.getAgeFromBirthday(birthday));
                this.mTxvConstellation.setText(ResourceHelper.getAstro(birthday));
                this.mTxvProvince.setText(cardEntity.getNativeplace());
                this.mImvSex.setImageResource(ResourceHelper.getSexImageResource(cardEntity.getSex()));
                this.mRelHead.setBackgroundResource(R.mipmap.bg_user_head_small);
                // 设置用户等级
                int auth = cardEntity.getAuth();
                //会员等级状况
                if(auth == Constants.MemberGrade.SENIOR){
                    this.mImvVip.setVisibility(View.VISIBLE);
                    this.mImvGride.setVisibility(View.GONE);
                }else if(auth == Constants.MemberGrade.DIAMON){
                    this.mImvVip.setVisibility(View.VISIBLE);
                    this.mImvGride.setVisibility(View.VISIBLE);
                }else{
                    this.mImvVip.setVisibility(View.GONE);
                    this.mImvGride.setVisibility(View.GONE);
                }
                //            //验证情况
                int binWay = cardEntity.getBindway();
                this.mImvVerify.setVisibility(View.VISIBLE);
                if(Constants.BindWay.MOBILE == binWay || Constants.BindWay.ALL == binWay){
                    this.mImvVerify.setImageResource(R.mipmap.img_card_mobile_vertify);
                }else{
                    this.mImvVerify.setImageResource(R.mipmap.img_card_mobile_unvertify);
                }
            }else{
                this.mImvVip.setVisibility(View.GONE);
                this.mImvGride.setVisibility(View.GONE);
                this.mImvVerify.setVisibility(View.GONE);
                this.mLilSex.setVisibility(View.GONE);
                this.mRelHead.setBackgroundColor(Color.BLACK);
                this.mTxvCompany.setVisibility(View.VISIBLE);
                this.mTxvVcardSNo.setText(cardEntity.getJob());
                this.mTxvCompany.setText(cardEntity.getCompanyName());
            }
            this.mTxvName.setText(this.mCurDisplayName);
            if (Constants.DefaultUser.DEFAULT_USER_CARD_ID == mCurCardId) {
                this.mImvHead.setImageResource(R.mipmap.img_user_head_mytip);
            } else {
                this.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
                ResourceHelper.asyncImageViewFillPath(this.mImvHead, this.mCurHeadPath);
            }
            int business = cardEntity.getBusiness();
            if (0 == business) {
                this.mImvBusiness.setVisibility(View.GONE);
            } else {
                this.mImvBusiness.setVisibility(View.VISIBLE);
                this.mImvBusiness.setImageBitmap(ResourceHelper.getBusinessIconByCode(this, cardEntity.getBusiness()));
            }
            this.mCurPhoneList = ContactVCardDao.getInstance().getPhoneList(this.mCurContactKeyId);
            this.mCurMobile = cardEntity.getMobile();
            this.mCurEmail = cardEntity.getEmail();
        }
        this.mTxvCurcardPosition.setText((position + 1 ) + "/" + this.cardCount);
        this.mTxvCurCardBigPosition.setText((position + 1 ) + "/" + this.cardCount);
    }
    /**
     * 获取 viewpage的图片控件
     *
     * @return
     */
    private CardDetailsImageView getViewPageImg(ContactEntity contactEntity) {
        // "触摸板"数据引入
        String vcardNo = contactEntity.getVcardNo();
        CardTouchPadEntity cardTouchPadEntity = null;
        if (Constants.DefaultUser.DEFAULT_USER_VCARD_NO.equals(vcardNo)|| DEFAULT_USER_VCARD_NO_OLD.equals(vcardNo)) {
            cardTouchPadEntity = JsonHelper.fromJson(ResourceHelper.getStringFromRawFile(R.raw.mytip_touch_pad_data), CardTouchPadEntity.class);
        }
        CardDetailsImageView iv = new CardDetailsImageView(this, contactEntity, cardTouchPadEntity);
        return iv;
    }

    /**
     * 获取放大图片的viewpage对象
     *
     * @return
     */
    private CardDetailsMaxImageView getViewPageImgMax() {
        CardDetailsMaxImageView VwMax = new CardDetailsMaxImageView(this);
        return VwMax;
    }

    //region 子类
    private class CardcaseDetailImagePagerAdapter extends PagerAdapter {

        private ArrayList<ContactEntity> mItems = new ArrayList<ContactEntity>();

        private CardDetailsImageView.ImageViewLongPressListener mLongPressListener = new CardDetailsImageView.ImageViewLongPressListener() {

            @Override
            public void imageViewLongPress() {
                showOrHideMax();
            }
        };
        @Override
        public int getCount() {
            return this.mItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ContactEntity entity = this.mItems.get(position);
            CardDetailsImageView newView = getViewPageImg(entity);
            newView.show(entity);
//            newView.setImageViewLongPressListener(new CardDetailsImageView.ImageViewLongPressListener() {
//
//                @Override
//                public void imageViewLongPress() {
//                    showOrHideMax();
//                }
//            });
            newView.setImageViewLongPressListener(this.mLongPressListener);
            container.addView(newView);
            return newView;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            CardDetailsImageView cardDetailsImageView = (CardDetailsImageView) object;
            if (Helper.isNotNull(cardDetailsImageView)) {
                // AsyncImageView imv = cardDetailsImageView.getImgView();
                // String vcardNo = cardDetailsImageView.getVCardNo();
                // if(Helper.isNotNull(imv) && Helper.isNotNull(vcardNo) &&
                // !Constant.DefaultUser.DEFAULT_USER_VCARD_NO.equals(vcardNo)){
                LogHelper.i(TAG, "有进行回收！");
                // TODO 图片异步修改
                // AsyncImageManager.add4Recycle(imv.getDrawable());
                // }
            }
        }
        /**
         * 给ArrayList适配器添加新的ArrayList元素
         *
         * @param items
         *            ArrayList元素，他的对象为ContactEntity
         * @return 如果是true则表示添加成功，false表示添加失败
         */
        public boolean addItems(ArrayList<ContactEntity> items) {
            boolean result = false;
            if (Helper.isNotNull(items)) {
                result = this.mItems.addAll(items);
                this.notifyDataSetChanged();
            }
            return result;
        }
        /**
         * 移除ArrayList对象中的某个元素
         *
         * @param position
         *            该元素在ArrayList中的位置
         */
        public void removeItem(int position) {
            this.mItems.remove(position);
            this.notifyDataSetChanged();
        }
    }

    private class CardcaseBigVCardImagePagerAdapter extends PagerAdapter {

        private ArrayList<ContactEntity> mItems = new ArrayList<ContactEntity>();

        private CardDetailsMaxImageView.ImageViewLongPressListener mLongPressListener = new CardDetailsMaxImageView.ImageViewLongPressListener() {

            @Override
            public void imageViewLongPress() {
                showOrHideMax();
            }
        };
        @Override
        public int getCount() {
            return this.mItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ContactEntity entity = this.mItems.get(position);
            CardDetailsMaxImageView newView = getViewPageImgMax();
            newView.showMax(entity, true);
//            newView.setImageViewLongPressListener(new CardDetailsMaxImageView.ImageViewLongPressListener() {
//
//                @Override
//                public void imageViewLongPress() {
//                    showOrHideMax();
//                }
//            });
            newView.setImageViewLongPressListener(this.mLongPressListener);
            container.addView(newView);
            return newView;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            CardDetailsMaxImageView cardDetailsMaxView = (CardDetailsMaxImageView) object;
            if (Helper.isNotNull(cardDetailsMaxView)) {
                // AsyncImageView imv = cardDetailsMaxView.getImvMax();
                // String vcardNo = cardDetailsMaxView.getVCardNo();
                // if(Helper.isNotNull(imv) && Helper.isNotNull(vcardNo) &&
                // !Constant.DefaultUser.DEFAULT_USER_VCARD_NO.equals(vcardNo)){
                LogHelper.i(TAG, "有进行回收！");
                // AsyncImageManager.add4Recycle(imv.getDrawable());
                // }
            }
        }

        /**
         * 给ArrayList适配器添加新的ArrayList元素
         *
         * @param items
         *            ArrayList元素，他的对象为ContactEntity
         * @return 如果是true则表示添加成功，false表示添加失败
         */
        public boolean addItems(ArrayList<ContactEntity> items) {
            boolean result = false;
            if (Helper.isNotNull(items)) {
                result = this.mItems.addAll(items);
                this.notifyDataSetChanged();
            }
            return result;
        }

        /**
         * 移除ArrayList对象中的某个元素
         *
         * @param position
         *            该元素在ArrayList中的位置
         */
        public void removeItem(int position) {
            this.mItems.remove(position);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 小图名片滑动监听
     *
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-4-2
     *
     */
    private class VCardPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(final int position) {
            fillVCardData(position);
//            runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    switchContact(position);
//                }
//
//            });
            // switchContact(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

    /**
     * 大图名片滑动监听
     *
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-4-2
     *
     */
    private class BigVCardPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(final int position) {
            mVwpVCard.setCurrentItem(position);
//            runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    mVp.setCurrentItem(position);
//                }
//
//            });
            // mVp.setCurrentItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

    private void setTextData(TextView view,int ResId){
        setTextData(view, ResId, -1);
    }

    private void setTextData(TextView view,String text){
        setTextData(view, text, -1);
    }

    private void setTextData(TextView view, String text,int colorId){
        if(Helper.isNotNull(text)){
            view.setText(text);
            if(colorId != -1){
                view.setTextColor(getResources().getColor(colorId));
            }
        }else{
            //默认为白色
            view.setTextColor(getResources().getColor(R.color.color_ffffff));
        }
    }

    private void setTextData(TextView view, int ResId,int colorId){
        if(ResId != -1){
            view.setText(ResId);
            if(colorId !=- 1){
                view.setTextColor(getResources().getColor(colorId));
            }else{
                //默认为白色
                view.setTextColor(getResources().getColor(R.color.color_ffffff));
            }
        }
    }
    //endregion 子类

    /**
     * 选择号码拨号弹窗
     */
    private void showDialogFragmentCallPhone(ArrayList<String> phoneList){
        if(ResourceHelper.isNull(this.mDialogFragmentCallPhone)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(CardcaseDetailVCardActivity.this);
            this.mLsvCallAdapter = new CustomLsvAdapter(CardcaseDetailVCardActivity.this);
            mLsvItem.setAdapter(this.mLsvCallAdapter);
            this.mLsvCallAdapter.setShowRadio(false);
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String phone = mLsvCallAdapter.getItem(position);
                    mDialogFragmentCallPhone.getDialog().dismiss();
                    ResourceHelper.callPhone(CardcaseDetailVCardActivity.this, phone);
                }
            });
            this.mDialogFragmentCallPhone = DialogFragmentHelper.showCustomDialogFragment(this.mTitle, mLsvItem);
        }
        this.mDialogFragmentCallPhone.setTitle(this.mTitle);
        this.mLsvCallAdapter.addItems(phoneList);
        this.mDialogFragmentCallPhone.show(getSupportFragmentManager(), "mDialogFragmentCallPhone");
    }

    /**
     * 网络不给力弹窗
     */
    private void showDialogFragmentNetwork(){
        if(ResourceHelper.isNull(this.mDialogFragmentNetwork)){
            CustomDialogFragment.DialogFragmentInterface onClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, MessageConversationFragment.class.getName());
                        bundle.putLong(Constants.IntentSet.INTENT_KEY_ACCOUNT_ID, mCurAccountId);
                        bundle.putBoolean(Constants.IntentSet.INTENT_KEY_IS_FROM_NETWORK_FAIL, true);
                        intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                        switchTo(MessageMainActivity.class, intent);
                    }
                }
            };
            this.mDialogFragmentNetwork = DialogFragmentHelper.showCustomDialogFragment(R.string.warm_prompt ,R.string.send_without_network, R.string.frg_text_cancel, R.string.frg_text_ok, onClick);
        }
        this.mDialogFragmentNetwork.show(getSupportFragmentManager(), "mDialogFragmentNetwork");
    }

    /**
     * 拨打电话
     */
    private void callPhone(ArrayList<String> mCurPhoneList){
        if (Helper.isNotNull(mCurPhoneList)) {
            int size = mCurPhoneList.size();
            if (size == 1) {
                ResourceHelper.callPhone(CardcaseDetailVCardActivity.this, mCurPhoneList.get(0));
            } else if (size > 1) {
                showDialogFragmentCallPhone(mCurPhoneList);
            }
        }
    }

    /**
     * 发送消息
     */
    private void sendMess(){
        if(ResourceHelper.isNotEmpty(mCurVcardNo)){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            if (!NetworkHelper.isNetworkAvailable(CardcaseDetailVCardActivity.this)) {
                showDialogFragmentNetwork();
            } else {
                intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, MessageConversationFragment.class.getName());
                bundle.putLong(Constants.IntentSet.INTENT_KEY_ACCOUNT_ID, mCurAccountId);
                intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                switchTo(MessageMainActivity.class, intent);
            }
        }
     }

    /**
     * 发送邮件
     */
    private void sendEmail(){
        if(ResourceHelper.isNotEmpty(mCurVcardNo)){
            EmailSendEntity emailEntity = new EmailSendEntity(mCurEmail.split(","));
            ResourceHelper.sendEmail(emailEntity, CardcaseDetailVCardActivity.this);
        }
    }
}
