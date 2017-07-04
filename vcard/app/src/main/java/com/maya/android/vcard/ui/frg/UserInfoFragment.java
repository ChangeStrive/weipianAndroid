package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.ContactVCardDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.ContactEntity;
import com.maya.android.vcard.entity.ContactListItemEntity;
import com.maya.android.vcard.entity.TitleMoreMenuLsvIconEntity;
import com.maya.android.vcard.entity.json.UserInfoContactJsonEntity;
import com.maya.android.vcard.entity.json.UserInfoOtherJsonEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UserInfoListResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.RatingbarView;
import com.maya.android.vcard.ui.widget.TitleMoreMenuPopView;
import com.maya.android.vcard.ui.widget.UserHeadMaxDialogFragment;
import com.maya.android.vcard.ui.widget.UserInfoMoreItemView;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.util.TitleMoreMenuPopHelper;

import org.json.JSONObject;

/**
 * 用户：详细资料页
 * */
public class UserInfoFragment extends BaseFragment {
    private static final String TAG = UserInfoFragment.class.getSimpleName();
    /** 传递参数的密钥 **/
    public static final String KEY_SHOW_USER = "key_show_user";
    /** 微片号 Key **/
    public static final String KEY_VCARDSNO = "key_vcardsno";
    /** 名片ID key **/
    public static final String KEY_CARD_ID = "key_card_id";
    /** 用户个人资料 **/
    public static final int CODE_SHOW_CURRENT_USER_INFO = 10001;
    /** 别人的资料 **/
    public static final int CODE_SHOW_OTHER_USER_INFO  = 10002;
    /** 联系人资料 **/
    public static final int CODE_SHOW_CONTACT_USER_INFO = 1003;
    /** 当前显示的用户资料类型 **/
    private int infoCode;
    private URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
    private boolean isShowSelfIntroduction = true;
    private boolean isShowMore = true;
    private UserHeadMaxDialogFragment mDialogFragmentHeadMax;
    //对以下进行赋值
    private int scoialRelation;//社交关系
    private String social = "";
    private boolean isUserSelf = false;//默认不是用户本身(看别人资料)
    private boolean isVerify = false;//默认未验证
    private String mWorkExperience;//工作经验
    private String mEducationalInformation;//教育信息
    /** 显示第一张图像 **/
    private String headImg;
    //以下 头部
    private AsyncImageView mImvImgHead;
    private ImageView mImvVip, mImvGrade, mImvSex, mImvVerify, mImvBus;
    private TextView mTxvName, mTxvVcardSno, mTxvAge, mTxvConstellation, mTxvProvince, mTxvCity;
    //以下 vip等级、时间、距离
    private RatingbarView mRabVipClass;
    private TextView mTxvLoaction, mTxvTime;
    //用户资料
    private TextView mTxvInfoName, mTxvInfoSex, mTxvInfoConstellation, mTxvInfoAge, mTxvInfoBirthday,mTxvEducation, mTxvInfoHometown, mTxvGraduateInstitutions, mTxvMore;
    private LinearLayout mLilWorkExperience, mLilEducationalInformation;
    //以下  行业相关
    private TextView mTxvBusiness, mTxvJob, mTxvCompany;
    //以下自我介绍
    private TextView mTxvSelfIntroduction;
    //以下社交关系（手机验证）、注册时间
    private TextView mTxvshowSelfOrSocial,mTxvMobileOrSocial, mTxvRegistrationDate, mTxvVerify;
    //以下底部菜单栏
    private RelativeLayout mRelSocialRelation;
    private LinearLayout mLilSocialRelation;
    private TextView mTxvChat, mTxvPrivateLetter, mTxvAttention, mTxvMutualAttention, mTxvReport, mTxvSwap;
    private TitleMoreMenuPopView mTitleMoreMenuPop;
    private View rootView;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txv_more:
                    //更多
                    if(isShowMore){
                        mTxvMore.setText(R.string.pack_up);
                        if(Helper.isNotEmpty(mWorkExperience)){
                            mLilWorkExperience.setVisibility(View.VISIBLE);
                        }
                        if(Helper.isNotEmpty(mEducationalInformation)){
                            mLilEducationalInformation.setVisibility(View.VISIBLE);
                        }

                    }else{
                        mTxvMore.setText(R.string.more);
                        mLilWorkExperience.setVisibility(View.GONE);
                        mLilEducationalInformation.setVisibility(View.GONE);
                    }
                    isShowMore();
                    break;
                case R.id.txv_self_introduction:
                    //自我介绍
                    if(isShowSelfIntroduction){
                        mTxvSelfIntroduction.setMaxLines(100);
                    }else{
                        mTxvSelfIntroduction.setMaxLines(2);
                    }
                    isShowSelfIntroduction();
                    break;
                case R.id.txv_chat:
                    //TODO 聊天
                    break;
                case R.id.txv_private_letter:
                    //TODO 私信
                    break;
                case R.id.txv_attention:
                    //TODO 关注
                    break;
                case R.id.txv_mutual_attention:
                    //TODO 相互关注
                    break;
                case R.id.txv_report:
                    //TODO 拉黑/举报
                    break;
                case R.id.txv_swap_card:
                    //TODO 名片交换
                    break;
                case R.id.txv_phone_have_verify:
                    //TODO 手机验证
                    break;
                case R.id.txv_act_top_right:
                    //编辑
                    mFragmentInteractionImpl.onFragmentInteraction(UserInfoEditFragment.class.getName(), null);
                    break;
                case R.id.imv_user_head:
                    //显示头像大图
                    showDialogFragmentHeadMax(headImg);
                    break;
                case R.id.imv_act_top_right:
                    //右上角弹窗
                    showTitleMoreMenuPop();
                    break;
            }
        }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_CARD_PERSON_TO_ININET_GET:
                    //其他人详细信息回调
                    UserInfoListResultEntity userInfoList = JsonHelper.fromJson(commandResult,UserInfoListResultEntity.class);
                    if(ResourceHelper.isNotNull(userInfoList)){
                        this.scoialRelation = userInfoList.getSocialRelation();
                        fillUserInfo(userInfoList.getAccountEntity());
                        fillVCardInfo(userInfoList.getCardEntity());
                        showSelfOrOtherView(false);
                    }
                    break;
                case Constants.CommandRequestTag.CMD_REQUEST_CARD_PERSON:
                    //联系人详细信息回调
                    ContactEntity contact = JsonHelper.fromJson(commandResult, ContactEntity.class);
                    if(ResourceHelper.isNotNull(contact)){
                        this.scoialRelation = contact.getSocialRelation();
                        fillUserInfo(contact.getAccountEntity());
                        fillVCardInfo(contact.getCardEntity());
                        showSelfOrOtherView(false);
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onResponseSuccess(int tag, String msgInfo) {
        super.onResponseSuccess(tag, msgInfo);
        switch (tag){
            case Constants.CommandRequestTag.CMD_REQUEST_CARD_PERSON_TO_ININET_GET:
            case Constants.CommandRequestTag.CMD_REQUEST_CARD_PERSON:
                super.mFragmentInteractionImpl.onActivityBackPressed();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        this.mImvImgHead = (AsyncImageView) view.findViewById(R.id.imv_user_head);
        this.mImvVip = (ImageView) view.findViewById(R.id.imv_user_head_vip);
        this.mImvGrade = (ImageView) view.findViewById(R.id.imv_user_head_grade);
        this.mImvSex = (ImageView) view.findViewById(R.id.imv_user_sex);
        this.mImvVerify = (ImageView) view.findViewById(R.id.imv_user_head_verify);
        this.mImvBus = (ImageView) view.findViewById(R.id.imv_user_head_bus);
        this.mTxvName = (TextView) view.findViewById(R.id.txv_user_name);
        this.mTxvVcardSno = (TextView) view.findViewById(R.id.txv_user_vcardsno_enter);
        this.mTxvAge = (TextView) view.findViewById(R.id.txv_user_age);
        this.mTxvConstellation = (TextView) view.findViewById(R.id.txv_user_constellation);
        this.mTxvProvince = (TextView) view.findViewById(R.id.txv_user_province);
        this.mTxvCity = (TextView) view.findViewById(R.id.txv_user_city);

        this.mRabVipClass = (RatingbarView) view.findViewById(R.id.rab_vip_class);
        this.mTxvLoaction = (TextView) view.findViewById(R.id.txv_userinfo_location);
        this.mTxvTime = (TextView) view.findViewById(R.id.txv_userinfo_time);
        this.mTxvInfoName = (TextView) view.findViewById(R.id.txv_username);
        this.mTxvInfoSex = (TextView) view.findViewById(R.id.txv_sex);
        this.mTxvInfoConstellation = (TextView) view.findViewById(R.id.txv_constellation);
        this.mTxvInfoAge = (TextView) view.findViewById(R.id.txv_age);
        this.mTxvInfoBirthday = (TextView) view.findViewById(R.id.txv_birthday);
        this.mTxvEducation = (TextView) view.findViewById(R.id.txv_education);
        this.mTxvInfoHometown = (TextView) view.findViewById(R.id.txv_hometown);
        this.mTxvGraduateInstitutions = (TextView) view.findViewById(R.id.txv_graduate_institutions);
        this.mTxvMore = (TextView) view.findViewById(R.id.txv_more);
        this.mLilWorkExperience = (LinearLayout) view.findViewById(R.id.lil_work_experience);
        this.mLilEducationalInformation = (LinearLayout) view.findViewById(R.id.lil_educational_information);

        this.mTxvBusiness = (TextView) view.findViewById(R.id.txv_business);
        this.mTxvJob = (TextView) view.findViewById(R.id.txv_job);
        this.mTxvCompany = (TextView) view.findViewById(R.id.txv_company);
        this.mTxvSelfIntroduction = (TextView) view.findViewById(R.id.txv_self_introduction);
        this.mTxvMobileOrSocial = (TextView) view.findViewById(R.id.txv_social_relation);
        this.mTxvshowSelfOrSocial = (TextView) view.findViewById(R.id.txv_show_self_or_social);
        this.mTxvRegistrationDate = (TextView) view.findViewById(R.id.txv_registration_date);
        this.mTxvVerify = (TextView) view.findViewById(R.id.txv_phone_have_verify);

        this.mTxvChat = (TextView) view.findViewById(R.id.txv_chat);
        this.mTxvPrivateLetter = (TextView) view.findViewById(R.id.txv_private_letter);
        this.mTxvAttention = (TextView) view.findViewById(R.id.txv_attention);
        this.mTxvMutualAttention = (TextView) view.findViewById(R.id.txv_mutual_attention);
        this.mTxvReport = (TextView) view.findViewById(R.id.txv_report);
        this.mTxvSwap = (TextView) view.findViewById(R.id.txv_swap_card);

        this.mRelSocialRelation = (RelativeLayout) view.findViewById(R.id.rel_social_relation);
        this.mLilSocialRelation = (LinearLayout) view.findViewById(R.id.lil_social_relation);

        this.mTxvMore.setOnClickListener(this.mOnClickListener);
        this.mTxvSelfIntroduction.setOnClickListener(this.mOnClickListener);
        this.mTxvChat.setOnClickListener(this.mOnClickListener);
        this.mTxvPrivateLetter.setOnClickListener(this.mOnClickListener);
        this.mTxvAttention.setOnClickListener(this.mOnClickListener);
        this.mTxvMutualAttention.setOnClickListener(this.mOnClickListener);
        this.mTxvReport.setOnClickListener(this.mOnClickListener);
        this.mTxvSwap.setOnClickListener(this.mOnClickListener);
        this.mImvImgHead.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rootView = view;
        this.infoCode = CODE_SHOW_CURRENT_USER_INFO;
        Bundle arg = this.getArguments();
        if(ResourceHelper.isNotNull(arg)){
            this.infoCode = arg.getInt(KEY_SHOW_USER, CODE_SHOW_CURRENT_USER_INFO);
        }
        switch (this.infoCode){
            case CODE_SHOW_CURRENT_USER_INFO:
                //我的资料
                super.mTitleAction.setActivityTitle(R.string.user_info, true);
                super.mTitleAction.setActivityTopRightTxvVisibility(View.VISIBLE);
                super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
                this.mTitleAction.setActivityTopRightTxv(R.string.edit, this.mOnClickListener);
                UserInfoResultEntity userInfo = CurrentUser.getInstance().getUserInfoEntity();
                CardEntity cardInfo = CurrentUser.getInstance().getCurrentVCardEntity();
                fillUserInfo(userInfo);
                fillVCardInfo(cardInfo);
                showSelfOrOtherView(true, userInfo);
                break;
            case CODE_SHOW_OTHER_USER_INFO:
                //别人详细资料
                super.mTitleAction.setActivityTitle(R.string.detail_info, true);
                super.mTitleAction.setActivityTopRightImv(R.mipmap.img_top_more, this.mOnClickListener);
                super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
                super.mTitleAction.setActivityTopRightImvVisibility(View.VISIBLE);
                String curVcardNo = arg.getString(KEY_VCARDSNO, "");
                ContactListItemEntity contactLsv = ContactVCardDao.getInstance().getEntityForListItem(curVcardNo);
                if(ResourceHelper.isNull(contactLsv)){
                    //网络验证
                    if(!NetworkHelper.isNetworkAvailable(getActivity())){
                        ActivityHelper.showToast(R.string.no_network);
                        super.mFragmentInteractionImpl.onActivityBackPressed();
                    }else{
                        //详细资料
                        requestOtherInfo(curVcardNo);
                    }
                }else{
                    ContactEntity contact = ContactVCardDao.getInstance().getEntity(contactLsv.getCardId());
                    if(ResourceHelper.isNotNull(contact)){
                        this.scoialRelation = Constants.ScoialRelAtion.SCOIAL_MUTUAL_ATTENTION;
                        fillUserInfo(contact.getAccountEntity());
                        fillVCardInfo(contact.getCardEntity());
                        this.social = getString(R.string.good_friend);
                        showSelfOrOtherView(false);
                    }
                }
                break;
            case CODE_SHOW_CONTACT_USER_INFO:
                //联系人详细资料
                super.mTitleAction.setActivityTitle(R.string.detail_info, true);
                super.mTitleAction.setActivityTopRightImv(R.mipmap.img_top_more, this.mOnClickListener);
                super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
                super.mTitleAction.setActivityTopRightImvVisibility(View.VISIBLE);
                long cardId = arg.getLong(KEY_CARD_ID, 0);
                ContactEntity contact = ContactVCardDao.getInstance().getEntity(cardId);
                if(ResourceHelper.isNull(contact)){
                    if(!NetworkHelper.isNetworkAvailable(getActivity())){
                        ActivityHelper.showToast(R.string.no_network);
                        super.mFragmentInteractionImpl.onActivityBackPressed();
                    }else{
                        //请求联系人资料
                        requestContactInfo(cardId);
                    }
                }else{
                    this.scoialRelation = contact.getSocialRelation();
                    fillUserInfo(contact.getAccountEntity());
                    fillVCardInfo(contact.getCardEntity());
                    showSelfOrOtherView(false);
                }
                break;
        }
      }

    @Override
    protected void onBackPressed() {
        super.onBackPressed();
        if(this.infoCode == CODE_SHOW_CURRENT_USER_INFO){
            //TODO 回传值有误，需要解决
            getActivity().setResult(getActivity().RESULT_OK);
        }
    }

    /**
     * 填充用户资料数据
     */
    private void fillUserInfo(UserInfoResultEntity userInfo){
        if(ResourceHelper.isNotNull(userInfo)){
            this.mImvImgHead.setDefaultImageResId(R.mipmap.img_upload_head);
            this.headImg = ResourceHelper.getImageUrlOnIndex(userInfo.getHeadImg(), 0);
            ResourceHelper.asyncImageViewFillUrl(this.mImvImgHead,  this.headImg);
            this.mTxvName.setText(userInfo.getDisplayName());
            this.mTxvVcardSno.setText(userInfo.getVcardNo());
            this.mTxvProvince.setText(userInfo.getProvince());
            this.mTxvCity.setText(userInfo.getCity());
            //会员等级状况
            int grade = userInfo.getAuth();
            if(grade == Constants.MemberGrade.SENIOR){
                this.mImvVip.setVisibility(View.VISIBLE);
                this.mImvGrade.setVisibility(View.GONE);
            }else if(grade == Constants.MemberGrade.DIAMON){
                this.mImvVip.setVisibility(View.VISIBLE);
                this.mImvGrade.setVisibility(View.VISIBLE);
            }else{
                this.mImvVip.setVisibility(View.GONE);
                this.mImvGrade.setVisibility(View.GONE);
            }
            this.mImvSex.setImageResource(ResourceHelper.getSexImageResource(userInfo.getSex()));
            String birthday = userInfo.getBirthday();
            this.mTxvAge.setText(ResourceHelper.getAgeFromBirthday(birthday));
            this.mTxvConstellation.setText(ResourceHelper.getAstro(birthday));
            //验证情况
            int binWay = userInfo.getBindWay();
            if(Constants.BindWay.MOBILE == binWay || Constants.BindWay.ALL == binWay){
                this.mImvVerify.setImageResource(R.mipmap.img_card_mobile_vertify);
                setVerify(true);
            }else{
                this.mImvVerify.setImageResource(R.mipmap.img_card_mobile_unvertify);
                setVerify(false);
            }
            this.mTxvInfoName.setText(userInfo.getDisplayName());
            String sex = userInfo.getSex() == 1 ? "男" : "女";
            this.mTxvInfoSex.setText(sex);
            this.mTxvInfoConstellation.setText(ResourceHelper.getAstro(birthday));
            this.mTxvInfoAge.setText(ResourceHelper.getAgeFromBirthday(birthday));
            this.mTxvInfoBirthday.setText(birthday);
            this.mTxvEducation.setText(userInfo.getEduBackground());
            this.mTxvInfoHometown.setText(userInfo.getNativeplace());
            this.mTxvGraduateInstitutions.setText(userInfo.getSchool());
            memberGride(userInfo.getAuth(), userInfo.getIntegral());
            //工作经验
            this.mWorkExperience =  userInfo.getWorkHistory();
             if(ResourceHelper.isNotNull(this.mWorkExperience) ){
                this.mTxvMore.setVisibility(View.VISIBLE);
                addItemViewContent(this.mLilWorkExperience, this.mWorkExperience);
            }
            //工作教育经验
            this.mEducationalInformation = userInfo.getEduInfo();
            if(ResourceHelper.isNotNull(this.mEducationalInformation)){
                this.mTxvMore.setVisibility(View.VISIBLE);
                addItemViewContent(this.mLilEducationalInformation, this.mEducationalInformation);
            }
            this.mTxvSelfIntroduction.setText(userInfo.getIntro());
            this.mTxvRegistrationDate.setText(ResourceHelper.getDateTimeFormat(userInfo.getCreateTime(), "yyyy.MM.dd"));
        }
     }

    /**
     * 填充用户名片资料
     */
    private void fillVCardInfo(CardEntity cardInfo ){
        if(ResourceHelper.isNotNull(cardInfo)){
            this.mImvBus.setImageBitmap(ResourceHelper.getBusinessIconByCode(getActivity(), cardInfo.getBusiness()));
            this.mTxvBusiness.setText(ResourceHelper.getBusinessNameByCode(getActivity(), cardInfo.getBusiness()));
            this.mTxvJob.setText(cardInfo.getJob());
            this.mTxvCompany.setText(cardInfo.getCompanyName());
        }
    }

    /**
     * 手机验证状态
     * @param isVerify
     */
    private void setVerify(boolean isVerify){
        this.isVerify = isVerify;
    }

    /**
     *根据社交关系显示底部菜单
     * @param mSocialRelation 社交关系
     */
    private void showSocialRelation(int mSocialRelation){
        this.mRelSocialRelation.setVisibility(View.VISIBLE);
        switch (mSocialRelation){
            case Constants.ScoialRelAtion.SCOIAL_ATTENTION://已关注
                this.mTxvPrivateLetter.setVisibility(View.VISIBLE);
                this.mTxvAttention.setVisibility(View.VISIBLE);
                this.mTxvSwap.setVisibility(View.VISIBLE);
                this.mTxvSwap.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.img_userinfo_swap_card_gay, 0, 0);
                this.mTxvSwap.setTextColor(getActivity().getResources().getColor(R.color.color_b7babc));
                this.social = getString(R.string.store_up_already);
                break;
            case Constants.ScoialRelAtion.SCOIAL_MUTUAL_ATTENTION://相互关注
                this.mTxvChat.setVisibility(View.VISIBLE);
                this.mTxvMutualAttention.setVisibility(View.VISIBLE);
                this.mTxvSwap.setVisibility(View.VISIBLE);
                if(ResourceHelper.isEmpty(social)){
                    this.social = getString(R.string.mutual_attention);
                }
                 break;
            case Constants.ScoialRelAtion.SCOIAL_STRANGER://陌生人
            case Constants.ScoialRelAtion.SCOIAL_IS_ATTENTION:
            default:
                //被关注
                this.mTxvPrivateLetter.setVisibility(View.VISIBLE);
                this.mTxvAttention.setVisibility(View.VISIBLE);
                this.mTxvReport.setVisibility(View.VISIBLE);
                this.social = getString(R.string.stranger);
                break;
        }
        this.mTxvMobileOrSocial.setText(this.social);
     }

    /**
     * 每一次变化取相反值(自我介绍)
     */
    private void isShowSelfIntroduction(){
        this.isShowSelfIntroduction = !this.isShowSelfIntroduction;
    }
    /**
     * 每一次变化取相反值(更多)
     */
    private void isShowMore(){
        this.isShowMore = !this.isShowMore;
    }

    /**
     *
     * @param isUserSelf
     */
    public void showSelfOrOtherView(boolean isUserSelf){
        showSelfOrOtherView(false, null);
    }

    /**
     *
     * @param isUserSelf 判断是用户资料还是别人资料
     */
    private void showSelfOrOtherView(boolean isUserSelf, UserInfoResultEntity userInfo){
        if(isUserSelf){//用户资料
            this.mTxvshowSelfOrSocial.setText(R.string.mobile_verify);
            this.mTxvVerify.setVisibility(View.VISIBLE);
            //根据手机验证情况设定显示字体内容和颜色
            if(this.isVerify){
                setTextColor(this.mTxvVerify, R.string.have_verify, R.color.color_399c2f);
            }else{
                setTextColor(this.mTxvVerify, R.string.not_verify, R.color.color_fe6030);
            }
            if(ResourceHelper.isNotNull(userInfo)){
                this.mTxvMobileOrSocial.setText(userInfo.getMobileTel());
            }
            this.mRelSocialRelation.setVisibility(View.GONE);
        }else{//别人资料
            if(infoCode == CODE_SHOW_CONTACT_USER_INFO){
                this.mRelSocialRelation.setVisibility(View.GONE);
                this.mTxvMobileOrSocial.setText(R.string.good_friend);
            }else{
                showSocialRelation(this.scoialRelation);
            }
            this.mTxvshowSelfOrSocial.setText(R.string.social_relation);
            this.mTxvVerify.setVisibility(View.GONE);
            if(ResourceHelper.isNotNull(userInfo)){

            }
        }
    }

    /**
     * 修改字体内容和颜色
     * @param mTxvVerify
     * @param resId
     * @param colorId
     */
    private void setTextColor(TextView mTxvVerify, int resId, int colorId){
        mTxvVerify.setTextColor(getActivity().getResources().getColor(colorId));
        mTxvVerify.setText(resId);
    }

    /**
     * 默认是普通会员
     * @param mGride  会员等级
     * @param integral 会员积分
     */
    private void memberGride(int mGride, int integral){
        if(mGride == Constants.MemberGrade.SENIOR){//高级会员
            this.mRabVipClass.setRatingbarDrawable(R.mipmap.bg_senior_member_gay, R.mipmap.bg_senior_member);
        }else if(mGride == Constants.MemberGrade.DIAMON){//钻石会员
            this.mRabVipClass.setRatingbarDrawable(R.mipmap.bg_diamond_member_gay, R.mipmap.bg_diamond_member);
        }
        double interFloat = ((double)integral) / 200;
        this.mRabVipClass.setRating(interFloat);
    }

    /**
     * 添加单个赋值框(默认显示分割线)
     * @param mLilItemView
     * @param text
     */
    private void addItemView(LinearLayout mLilItemView, String text){
        addItemView(mLilItemView, text, View.VISIBLE);
    }
    /**
     * 添加单个赋值框
     *
     * @param mLilItemView
     * @param text
     */
    private void addItemView(LinearLayout mLilItemView, String text, int visibility) {
        if (Helper.isNotNull(mLilItemView)) {
            UserInfoMoreItemView itemView = new UserInfoMoreItemView(getActivity());
            if (Helper.isNotNull(text)) {
                itemView.setTxvContent(text);
            }
            itemView.setmViewline(visibility);
            mLilItemView.addView(itemView);
        }
    }
    /**
     * 动态添加赋值框
     * @param mLilItemView
     * @param mContent
     */
    private void addItemViewContent(LinearLayout mLilItemView, String mContent) {
        if (Helper.isNotEmpty(mContent)) {
            String[] emailArr = mContent.trim().split(Constants.Other.CONTENT_ARRAY_SPLIT);
            int emailArrLength = emailArr.length;
            if (emailArrLength > 0) {
                for (int i = 0; i < emailArrLength; i++) {
                    String content = emailArr[i];
                    if (Helper.isNotEmpty(content)) {
                        if(i == 0){
                            addItemView(mLilItemView, content, View.GONE);
                        }else{
                            addItemView(mLilItemView, content);
                        }
                    }
                }
            }
        }
    }

    /**
     * 显示大图
     */
    private void showDialogFragmentHeadMax(String headImg){
        if(ResourceHelper.isNull(this.mDialogFragmentHeadMax)){
            this.mDialogFragmentHeadMax = DialogFragmentHelper.showUserHeadMaxDialogFragment(headImg);
            this.mDialogFragmentHeadMax.setTargetFragment(this, Constants.TakePicture.REQUEST_CODE_CHOOSE_HEAD);
        }
        this.mDialogFragmentHeadMax.setHeadImg(headImg);
        this.mDialogFragmentHeadMax.show(getFragmentManager(),"mDialogFragmentHeadMax");
    }

    /**
     * 右上角弹窗
     */
    private void showTitleMoreMenuPop(){
        if(ResourceHelper.isNull(this.mTitleMoreMenuPop)){
            this.mTitleMoreMenuPop = TitleMoreMenuPopHelper.getUserInfoPop(getActivity(), this.scoialRelation);
            this.mTitleMoreMenuPop.setItemClickListener(new TitleMoreMenuPopView.MoreMenuItemClickListener() {
                @Override
                public void onItemClick(TitleMoreMenuLsvIconEntity menu) {
                    switch (menu.getBusinessName()) {
                        case R.string.cancel_attention:
                            //TODO 取消关注
                            break;
                        case R.string.friend_delete:
                            //TODO 删除好友
                            break;
                        case R.string.pop_report:
                            //TODO 拉黑举报
                            break;
                        case R.string.remarks:
                            //TODO 备注
                            break;
                    }
                }
            });
        }
        this.mTitleMoreMenuPop.showAtLocation(this.rootView, Gravity.FILL, 0, 0);
    }

    /**
     * 请求别人详细信息
     */
    private void requestOtherInfo(String vcardNo){
        if(Helper.isNotNull(this.urlEntity)){
            String token = CurrentUser.getInstance().getToken();
            UserInfoOtherJsonEntity cardInfo = new UserInfoOtherJsonEntity();
            cardInfo.setVcardNo(vcardNo);
            String json = JsonHelper.toJson(cardInfo, UserInfoOtherJsonEntity.class);
            String url =  ProjectHelper.fillRequestURL(this.urlEntity.getPersonMessage());
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CARD_PERSON_TO_ININET_GET, url, token, json, this);
        }
    }

    /**
     * 请求联系人详细信息
     */
    private void requestContactInfo(long cardId){
        if(Helper.isNotNull(this.urlEntity)){
            String token = CurrentUser.getInstance().getToken();
            UserInfoContactJsonEntity contactInfo = new UserInfoContactJsonEntity();
            contactInfo.setCardId(cardId);
            String json = JsonHelper.toJson(contactInfo, UserInfoContactJsonEntity.class);
            String url =  ProjectHelper.fillRequestURL(this.urlEntity.getCardPerson());
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CARD_PERSON, url, token, json, this);
        }
    }
}
