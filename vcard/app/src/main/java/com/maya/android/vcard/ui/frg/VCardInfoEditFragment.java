package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.InstantMessengerEntity;
import com.maya.android.vcard.entity.TitleMoreMenuLsvIconEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.adapter.CustomLsvAdapter;
import com.maya.android.vcard.ui.adapter.CustomLsvIconAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.TitleMoreMenuPopView;
import com.maya.android.vcard.ui.widget.VCardInfoMoreEdtItemView;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.util.TitleMoreMenuPopHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 名片信息编辑页
 */
public class VCardInfoEditFragment extends BaseFragment {
    /** 类别Key **/
    public static final String KEY_VCARD_TYPE = "key_vcard_type";
    /** 名片编辑 **/
    public static final int INTENT_CODE_VCARD_EDIT = 6000001;
    /** 名片扫描 **/
    public static final int INTENT_CODE_VCARD_BY_SCAN = 6000002;
    /** 名片上传 **/
    public static final int INTENT_CODE_VCARD_BY_UPLOAD = 6000003;
    /** 名片管理 **/
    public static final int INTENT_CODE_VCARD_CARDCASE = 6000004;
    /** 名片模板 **/
    public static final int INTENT_CODE_VCARD_TEMPLATE =  6000005;
    /** 当前名片类型 **/
    private int intentType;
    /** 当前用户资料 **/
    private UserInfoResultEntity userInfo = CurrentUser.getInstance().getUserInfoEntity();
    /** 是否应许修改名字 **/
    private boolean isAllowModifyName = true;
    /** 记录当前的名片信息 **/
    private String mFamilyName, mName, mJob, mCompanyName, mCompanyAddress, mCompanyUrl, mMobilePhone, mTelePhone, mFax, mEmail, mPostCode, mOtherMessage, mJson;
    /** 行业类别编号 **/
    private int mBusinessPosition;

    private EditText mEdtFamilyName, mEdtName, mEdtJob, mEdtCompany, mEdtAddress, mEdtUrl, mEdtMobile, mEdtTelePhone, mEdtFax, mEdtEmail, mEdtOtherInformation;
    private TextView mTxvBusiness;
    private LinearLayout mLilCompany, mLilAddress, mLilMobile, mLilTelePhone, mLilFax, mLilEmail;
    private LinearLayout mLilQq, mLilSinaWeibo, mLilTencentWeibo, mLilWeChat, mLilMSN, mLilPostcode;
    private Button mBtnPositive;
    private ArrayList<Integer> mUnEnablePositionList;
    private CustomDialogFragment mDialogFragmentAddMoreEdt, mDialogFragmentBusiness, mDialogFragmentChangeUserName;
    private CustomLsvAdapter mLsvAdapter;
    private TitleMoreMenuPopView mTitleMoreMenuPop;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.imb_company:
                    //公司(更多输入框)
                    addItemView(mLilCompany);
                    break;
                case R.id.imb_address:
                    //地址(更多输入框)
                    addItemView(mLilAddress);
                    break;
                case R.id.imb_mobile:
                    //手机(更多输入框)
                    addItemView(mLilMobile);
                    break;
                case R.id.imb_telephone:
                    //电话(更多输入框)
                    addItemView(mLilTelePhone);
                    break;
                case R.id.imb_fax:
                    //传真(更多输入框)
                    addItemView(mLilFax);
                    break;
                case R.id.imb_email:
                    //邮箱(更多输入框)
                    addItemView(mLilEmail);
                    break;
                case R.id.txv_add_more_information:
                    //添加更多信息
                    showDialogFragmentAddMoreEdt();
                    break;
                case R.id.btn_negative:
                    //取消
                    mFragmentInteractionImpl.onActivityBackPressed();
                    break;
                case R.id.btn_positive:
                    //TODO  保存
                    ButtonHelper.setButtonEnableDelayed(mBtnPositive);
                    if(isValidAllData()){
                        if(isAllowModifyName){
                            showDialogFragmentChangeUserName();
                        }else{
                            //TODO 保存名片资料
                        }
                    }
                    break;
                case R.id.txv_business:
                    //选中行业类别
                    showDialogFragmentBusiness();
                    break;
                case R.id.imv_act_top_right:
                    //右上角弹窗
                    showTitleMoreMenuPop(v);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_vcard_info_edit, container, false);
        this.mEdtFamilyName = (EditText) rootView.findViewById(R.id.edt_family_name);
        this.mEdtName = (EditText) rootView.findViewById(R.id.edt_name);
        this.mEdtJob = (EditText) rootView.findViewById(R.id.edt_job);
        this.mEdtCompany = (EditText) rootView.findViewById(R.id.edt_company);
        this.mTxvBusiness = (TextView) rootView.findViewById(R.id.txv_business);
        this.mEdtAddress = (EditText) rootView.findViewById(R.id.edt_address);
        this.mEdtUrl = (EditText) rootView.findViewById(R.id.edt_url);
        this.mEdtMobile = (EditText) rootView.findViewById(R.id.edt_mobile);
        this.mEdtTelePhone = (EditText) rootView.findViewById(R.id.edt_telephone);
        this.mEdtFax = (EditText) rootView.findViewById(R.id.edt_fax);
        this.mEdtEmail = (EditText) rootView.findViewById(R.id.edt_email);
        this.mEdtOtherInformation = (EditText) rootView.findViewById(R.id.edt_other_information);
        ImageButton mImbCompany = (ImageButton) rootView.findViewById(R.id.imb_company);
        ImageButton mImbAddress = (ImageButton) rootView.findViewById(R.id.imb_address);
        ImageButton mImbMobile = (ImageButton) rootView.findViewById(R.id.imb_mobile);
        ImageButton mImbTelePhone = (ImageButton) rootView.findViewById(R.id.imb_telephone);
        ImageButton mImbFax = (ImageButton) rootView.findViewById(R.id.imb_fax);
        ImageButton mImbEmail = (ImageButton) rootView.findViewById(R.id.imb_email);
        TextView mTxvAddMoreInformation = (TextView) rootView.findViewById(R.id.txv_add_more_information);
        Button mBtnNegative = (Button) rootView.findViewById(R.id.btn_negative);
        this.mBtnPositive = (Button) rootView.findViewById(R.id.btn_positive);
        mImbCompany.setOnClickListener(this.mOnClickListener);
        mImbAddress.setOnClickListener(this.mOnClickListener);
        mImbMobile.setOnClickListener(this.mOnClickListener);
        mImbTelePhone.setOnClickListener(this.mOnClickListener);
        mImbFax.setOnClickListener(this.mOnClickListener);
        mImbEmail.setOnClickListener(this.mOnClickListener);
        mTxvAddMoreInformation.setOnClickListener(this.mOnClickListener);
        mBtnNegative.setOnClickListener(this.mOnClickListener);
        this.mBtnPositive.setOnClickListener(this.mOnClickListener);
        this.mTxvBusiness.setOnClickListener(this.mOnClickListener);
        this.mLilCompany = (LinearLayout) rootView.findViewById(R.id.lil_company);
        this.mLilAddress = (LinearLayout) rootView.findViewById(R.id.lil_address);
        this.mLilMobile = (LinearLayout) rootView.findViewById(R.id.lil_mobile);
        this.mLilTelePhone = (LinearLayout) rootView.findViewById(R.id.lil_telephone);
        this.mLilFax = (LinearLayout) rootView.findViewById(R.id.lil_fax);
        this.mLilEmail = (LinearLayout) rootView.findViewById(R.id.lil_email);
        this.mLilQq = (LinearLayout) rootView.findViewById(R.id.lil_qq);
        this.mLilSinaWeibo = (LinearLayout) rootView.findViewById(R.id.lil_sina_weibo);
        this.mLilTencentWeibo = (LinearLayout) rootView.findViewById(R.id.lil_tencent_weibo);
        this.mLilWeChat = (LinearLayout) rootView.findViewById(R.id.lil_wechat);
        this.mLilMSN = (LinearLayout) rootView.findViewById(R.id.lil_msn);
        this.mLilPostcode = (LinearLayout) rootView.findViewById(R.id.lil_postcode);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.card_detail_edt, false);
        super.mTitleAction.setActivityTopRightImv(R.mipmap.img_top_more, this.mOnClickListener);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.VISIBLE);
        this.mUnEnablePositionList = new ArrayList<Integer>();
        this.intentType = INTENT_CODE_VCARD_EDIT;
        CardEntity mVCardInfo = null;
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
            this.intentType = bundle.getInt(KEY_VCARD_TYPE, INTENT_CODE_VCARD_EDIT);
        }
        switch (this.intentType){
            case INTENT_CODE_VCARD_EDIT:
                //名片编辑
                mVCardInfo = CurrentUser.getInstance().getCurrentVCardEntity();
                break;
            case INTENT_CODE_VCARD_BY_SCAN:
                //名片扫描
            case INTENT_CODE_VCARD_BY_UPLOAD:
                //名片上传
            case INTENT_CODE_VCARD_CARDCASE:
                //名片管理
            case INTENT_CODE_VCARD_TEMPLATE:
                //名片模板
                break;
        }
        fillUserName(this.userInfo);
        fillVCardInfo(mVCardInfo);
    }

    /**
     * 填充当前名片用户名
     * @param userInfo
     */
    private void fillUserName(UserInfoResultEntity userInfo){
        if(ResourceHelper.isNotNull(userInfo)){
            this.mEdtFamilyName.setText(userInfo.getSurname());
            this.mEdtName.setText(userInfo.getFirstName());
            this.isAllowModifyName = CurrentUser.getInstance().getUserNameEdt();
            if(this.isAllowModifyName){
                ResourceHelper.setTextViewEnable(this.mEdtFamilyName, true, R.color.color_292929);
                ResourceHelper.setTextViewEnable(this.mEdtName, true, R.color.color_292929);
            }else {
                ResourceHelper.setTextViewEnable(this.mEdtFamilyName, false, R.color.color_787878);
                ResourceHelper.setTextViewEnable(this.mEdtName, false, R.color.color_787878);
            }
        }
    }

    /**
     * 填充当前名片信息
     */
    private void fillVCardInfo(CardEntity mVCardInfo){
        if(ResourceHelper.isNotNull(mVCardInfo)){
            this.mEdtJob.setText(mVCardInfo.getJob());
            addItemViewContent(this.mLilCompany, this.mEdtCompany, mVCardInfo.getCompanyName());
            addItemViewContent(this.mLilAddress, this.mEdtAddress, mVCardInfo.getWorkAddress());
            this.mEdtUrl.setText(mVCardInfo.getCompanyHome());
            addItemViewContent(this.mLilMobile, this.mEdtMobile, mVCardInfo.getMobileTelphone());
            addItemViewContent(this.mLilTelePhone, this.mEdtTelePhone, mVCardInfo.getLineTelphone());
            addItemViewContent(this.mLilFax, this.mEdtFax, mVCardInfo.getFax());
            addItemViewContent(this.mLilEmail, this.mEdtEmail, mVCardInfo.getEmail());
            //行业
            this.mBusinessPosition = mVCardInfo.getBusiness();
            this.mTxvBusiness.setText(ResourceHelper.getBusinessNameByCode(getActivity(), this.mBusinessPosition));
            //其他信息
            this.mEdtOtherInformation.setText(mVCardInfo.getRemark());
            //及时通讯
            if(ResourceHelper.isNotNull(mVCardInfo.getImJson())){
                Type typeOfIm = new TypeToken<ArrayList<InstantMessengerEntity>>(){}.getType();
                ArrayList<InstantMessengerEntity> imList = JsonHelper.fromJson(mVCardInfo.getImJson(), typeOfIm);
                for (int i = 0, size = imList.size(); i < size; i++) {
                    InstantMessengerEntity imItem = imList.get(i);
                    String content = imItem.getValue();
                    if(ResourceHelper.isEmpty(content)){
                        continue;
                    }
                    if(imItem.getLabel().contains(DatabaseConstant.InstantMessengerLabel.QQ)){
                        //腾讯QQ
                        addItemView(this.mLilQq, R.string.qq, content, 0);
                    } else if(imItem.getLabel().contains(DatabaseConstant.InstantMessengerLabel.MICROBLOG_SINA)){
                        //新浪微博
                        addItemView(this.mLilSinaWeibo, R.string.sina_weibo, content, 1);
                    } else if(imItem.getLabel().contains(DatabaseConstant.InstantMessengerLabel.MICROBLOG_TX)){
                        //腾讯微博
                        addItemView(this.mLilTencentWeibo, R.string.tencent_weibo, content, 2);
                    } else if(imItem.getLabel().contains(DatabaseConstant.InstantMessengerLabel.WEI_XIN)){
                        //微信
                        addItemView(this.mLilWeChat, R.string.we_chat, ResourceHelper.getImageUrlOnIndex(content,0), 3);
                    }else if(imItem.getLabel().contains(DatabaseConstant.InstantMessengerLabel.MSN)){
                        //MSN
                        addItemView(this.mLilMSN, R.string.msn, content, 4);
                    }
                }
            }
            //邮编
            String postcode = mVCardInfo.getPostcode();
            if(ResourceHelper.isNotEmpty(postcode)){
                addItemView(this.mLilPostcode, R.string.msn, postcode, 5);
            }
        }
    }

   /**
     * 获取某个LinearLayout中所有子view编辑框中的内容
     * @param mLilItemView
     * @param mEditContent
     * @return 返回一个list
     */
    private String getAllItemContent(LinearLayout mLilItemView,EditText mEditContent){
        StringBuffer mAllContent = new StringBuffer();
        mAllContent.append(mEditContent.getText().toString());
        int count = mLilItemView.getChildCount();
        for (int i = 1; i < count; i++) {
            String content = getItemContent(mLilItemView, i);
            if(ResourceHelper.isNotEmpty(content)){
                mAllContent.append(Constants.Other.CONTENT_ARRAY_SPLIT).append(content);
            }
        }
        return mAllContent.toString();
    }

    /**
     * 获取单个Item中编辑框内容
     * @param mLilItemView
     * @param index
     * @return
     */
    private String getItemContent(LinearLayout mLilItemView, int index){
        VCardInfoMoreEdtItemView itemView = (VCardInfoMoreEdtItemView) mLilItemView.getChildAt(index);
        if(Helper.isNotNull(itemView)){
            return itemView.getContent();
        }else{
            return null;
        }
    }

    /**
     * 添加输入框(标签、内容为空)
     * @param mLilItemView
     */
    public void addItemView(LinearLayout mLilItemView){
        addItemView(mLilItemView, -1, null);
    }

    /**
     * 动态添加输入控件及相应内容
     * @param mLilItemView 添加到指定LinearLayout
     * @param mEdt 该LinearLayout首个子RelativeLayout中的EditText
     * @param mContent 需填充的String[]型的字符串
     */
    private void addItemViewContent(LinearLayout mLilItemView, EditText mEdt, String mContent){
        if(Helper.isNotEmpty(mContent)){
            String[] emailArr = mContent.trim().split(Constants.Other.CONTENT_ARRAY_SPLIT);
            mEdt.setText(emailArr[0]);
            int emailArrLength = emailArr.length;
            if(emailArrLength > 1){
                for(int i = 1; i < emailArrLength; i++){
                    String content = emailArr[i];
                    if(Helper.isNotEmpty(content)){
                        addItemView(mLilItemView, -1, content);
                    }
                }
            }
        }
    }

     /**
     * 添加输入框 并赋值
     * @param mLilItemView 指定LinearLayout
     * @param label 赋值标签
     * @param mContent  赋值内容
     * @return
     */
    private VCardInfoMoreEdtItemView addItemView(final LinearLayout mLilItemView, int label,  String mContent) {
        final VCardInfoMoreEdtItemView itemView = new VCardInfoMoreEdtItemView(getActivity());
        itemView.getImbDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLilItemView.removeView(itemView);
            }
        });
        if(label > 0 ){
            itemView.setViewLabel(label);
        }else{
            itemView.setLableViewColor(R.color.color_00000000);
        }
        if(Helper.isNotEmpty(mContent)){
            itemView.setContent(mContent);
        }
        mLilItemView.addView(itemView);
        return itemView;
     }

    /**
     * 添加输入框 (及时通讯内容为空)
     * @param mLilItemView
     * @param label
     * @param position
     */
    private void addItemView(final LinearLayout mLilItemView, int label, final int position) {
        addItemView(mLilItemView, label, null, position);
    }

    /**
     * 添加输入框 (及时通讯)
     * @param mLilItemView 指定LinearLayout
     * @param label 赋值标签
     * @param mContent 赋值内容
     * @param position 触发弹框编号
     * @return
     */
    private VCardInfoMoreEdtItemView addItemView(final LinearLayout mLilItemView, int label, String mContent, final int position) {
        mLilItemView.setVisibility(View.VISIBLE);
        final VCardInfoMoreEdtItemView itemView = new VCardInfoMoreEdtItemView(getActivity());
        itemView.getImbDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLilItemView.removeView(itemView);
                mLilItemView.setVisibility(View.GONE);
                mUnEnablePositionList.remove((Integer) position);
            }
        });
        this.mUnEnablePositionList.add(position);
        itemView.setViewLabel(label);
        if(Helper.isNotEmpty(mContent)){
            itemView.setContent(mContent);
        }
        mLilItemView.addView(itemView);
        return itemView;
    }

    /**
     * 添加更多信息对话框
     */
    private void showDialogFragmentAddMoreEdt(){
        if(Helper.isNull(this.mDialogFragmentAddMoreEdt)){
            ListView mListItem = DialogFragmentHelper.getCustomContentView(getActivity());
            mListItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            //腾讯QQ
                            addItemView(mLilQq, R.string.qq, position);
                            break;
                        case 1:
                            //新浪微博
                            addItemView(mLilSinaWeibo, R.string.sina_weibo, position);
                            break;
                        case 2:
                            //腾讯微博
                            addItemView(mLilTencentWeibo, R.string.tencent_weibo, position);
                            break;
                        case 3:
                            //微信
                            addItemView(mLilWeChat, R.string.we_chat, position);
                            break;
                        case 4:
                            //MSN
                            addItemView(mLilMSN, R.string.msn, position);
                            break;
                        case 5:
                            //邮编
                            addItemView(mLilPostcode, R.string.postcode, position);
                            break;
                    }
                    mDialogFragmentAddMoreEdt.getDialog().dismiss();
                }
            });
            this.mLsvAdapter = new CustomLsvAdapter(getActivity());
            mListItem.setAdapter(mLsvAdapter);
            this.mLsvAdapter.addItems(DialogFragmentHelper.getListFromResArray(getActivity(), R.array.card_detail_more_info));
            this.mLsvAdapter.setShowRadio(false);
            this.mDialogFragmentAddMoreEdt = DialogFragmentHelper.showCustomDialogFragment(R.string.tile_more_info_choose, mListItem);
        }
        this.mLsvAdapter.setUnEnableItems(this.mUnEnablePositionList);
        this.mDialogFragmentAddMoreEdt.show(getFragmentManager(), "mDialogFragmentAddMoreEdt");
    }

    /**
     * 右上角弹窗
     */
    private void showTitleMoreMenuPop(View v){
        if(ResourceHelper.isNull(this.mTitleMoreMenuPop)){
            this.mTitleMoreMenuPop = TitleMoreMenuPopHelper.getCardInfoEditPop(getActivity());
            this.mTitleMoreMenuPop.setItemClickListener(new TitleMoreMenuPopView.MoreMenuItemClickListener() {
                @Override
                public void onItemClick(TitleMoreMenuLsvIconEntity menu) {
                    switch (menu.getBusinessName()) {
                        case R.string.re_shooting_recognition:
                            //重新拍摄识别
                            break;
                        case R.string.upload_electronic_business_card:
                            //上传电子名片
                            break;
                        case R.string.change_business_card_template:
                            //更换名片模板
                            break;
                        case R.string.pop_vcard_del:
                            //删除名片
                            break;
                    }
                }
            });
        }
        this.mTitleMoreMenuPop.showAtLocation(v, Gravity.FILL, 0, 0);
    }

    /**
     * 行业类别对话框
     */
    private void showDialogFragmentBusiness(){
        if(ResourceHelper.isNull(this.mDialogFragmentBusiness)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvIconAdapter customIconAdapter = new CustomLsvIconAdapter(getActivity());
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mBusinessPosition = position;
                    mTxvBusiness.setText(customIconAdapter.getItem(position).getBusinessName());
                    mDialogFragmentBusiness.getDialog().dismiss();
                }
            });
            mLsvItem.setAdapter(customIconAdapter);
            customIconAdapter.addItems(ResourceHelper.getArrayListBusiness());
            this.mDialogFragmentBusiness = DialogFragmentHelper.showCustomDialogFragment(R.string.select_business, mLsvItem, true);
        }
        this.mDialogFragmentBusiness.show(getFragmentManager(), "mDialogFragmentBusiness");
    }

    /**
     * 修改名字提示框
     */
    private void showDialogFragmentChangeUserName(){
        String disPlayName = this.mFamilyName + this.mName;
        String msg = getString(R.string.detail_edit_info_prompt_save_name, disPlayName);
        if(ResourceHelper.isNull(this.mDialogFragmentChangeUserName)){
            CustomDialogFragment.DialogFragmentInterface onClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        //TODO 保存名片
                    }
                }
            };
            this.mDialogFragmentChangeUserName = DialogFragmentHelper.showCustomDialogFragment(R.string.warm_prompt ,msg, R.string.frg_text_cancel, R.string.frg_text_ok, onClick);
        }
        this.mDialogFragmentChangeUserName.setMessage(msg);
        this.mDialogFragmentChangeUserName.show(getFragmentManager(), "mDialogFragmentChangeUserName");
    }

    /**
     * 请求修改名片资料
     */
    private void requestUploadVCardInfo(){

    }

    /**
     * 输入验证、判空
     * @return
     */
    private boolean isValidAllData(){
        //姓名
        if(this.isAllowModifyName){
            this.mFamilyName = this.mEdtFamilyName.getText().toString();
            this.mName = this.mEdtName.getText().toString();
            if(Helper.isEmpty(this.mFamilyName) || Helper.isEmpty(this.mName)){
                ActivityHelper.showToast(R.string.please_enter_name);
                return false;
            }
        }
         //职业
        this.mJob = this.mEdtJob.getText().toString();
        if(Helper.isEmpty(this.mJob)){
            ActivityHelper.showToast(R.string.please_input_job);
            return false;
        }
        //公司名称
        this.mCompanyName = getAllItemContent(this.mLilCompany, this.mEdtCompany);
        if(ResourceHelper.isEmpty(this.mCompanyName)){
            ActivityHelper.showToast(R.string.please_input_company_name);
            return false;
        }
        //公司地址
        this.mCompanyAddress = getAllItemContent(this.mLilAddress, this.mEdtAddress);
        if(ResourceHelper.isEmpty(this.mCompanyAddress)){
            ActivityHelper.showToast(R.string.please_input_company_address);
            return false;
        }
        //手机号码
        this.mMobilePhone = getAllItemContent(this.mLilMobile, this.mEdtMobile);
        if(!ResourceHelper.isValidArrayMobile(this.mMobilePhone) ){
            ActivityHelper.showToast(R.string.please_input_right_mobile);
        }
        //公司网址
        this.mCompanyUrl =  this.mEdtUrl.getText().toString();
        //电话号码
        this.mTelePhone = getAllItemContent(this.mLilTelePhone, this.mEdtTelePhone);
        //传真
        this.mFax = getAllItemContent(this.mLilFax, this.mEdtFax);
        //邮箱
        this.mEmail = getAllItemContent(this.mLilEmail, this.mEdtEmail);
        //邮编
        this.mPostCode = getItemContent(this.mLilPostcode, 1);
        //其他信息
        this.mOtherMessage = this.mEdtOtherInformation.getText().toString();
        //及时通讯
        this.mJson = getImJson();
        return true;
    }

    /**
     * 获取及时通讯中输入的相关信息
     * @return 返回json型数据
     */
    private String getImJson(){
        ArrayList<InstantMessengerEntity> imList = new ArrayList<InstantMessengerEntity>();
        InstantMessengerEntity imEntity = new InstantMessengerEntity();
        String qq = getItemContent(this.mLilQq, 1);
        String sinaWebo = getItemContent(this.mLilSinaWeibo, 1);
        String tencentWebo = getItemContent(this.mLilTencentWeibo, 1);
        String msn = getItemContent(this.mLilMSN, 1);
        String wenxin = getItemContent(this.mLilWeChat, 1);

        if(ResourceHelper.isNotEmpty(qq)){
            imList.add(getImEmpty(imEntity, DatabaseConstant.InstantMessengerLabel.QQ, qq));
        }
        if(ResourceHelper.isNotEmpty(sinaWebo)){
            imList.add(getImEmpty(imEntity, DatabaseConstant.InstantMessengerLabel.MICROBLOG_SINA, sinaWebo));
        }
        if(ResourceHelper.isNotEmpty(tencentWebo)){
            imList.add(getImEmpty(imEntity, DatabaseConstant.InstantMessengerLabel.MICROBLOG_TX, tencentWebo));
        }
        if(ResourceHelper.isNotEmpty(msn)){
            imList.add(getImEmpty(imEntity, DatabaseConstant.InstantMessengerLabel.MSN, msn));
        }

        if(ResourceHelper.isNotEmpty(wenxin)){
            imList.add(getImEmpty(imEntity, DatabaseConstant.InstantMessengerLabel.WEI_XIN, wenxin));
        }
        return JsonHelper.toJson(imList);
    }

    /**
     * 用克隆的方式保存单个类数据
     * @param imEntity
     * @param label
     * @param value
     * @return
     */
    private InstantMessengerEntity getImEmpty(InstantMessengerEntity imEntity,String label,String value){
        InstantMessengerEntity item = null;
        try {
            item = (InstantMessengerEntity) imEntity.clone();
            item.setLabel(label);
            item.setValue(value);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return item;

    }

    /**
     * 手机验证
     * @param mContent
     * @return
     */
    private boolean isValliMobile(String mContent){
        if(Helper.isNotEmpty(mContent)){
            String[] emailArr = mContent.trim().split(Constants.Other.CONTENT_ARRAY_SPLIT);
            for(int i = 0, size = emailArr.length; i < size; i++){
                if(ResourceHelper.isValidMobile(emailArr[i])){
                    return true;
                }
            }
        }
        return false;
    }

}
