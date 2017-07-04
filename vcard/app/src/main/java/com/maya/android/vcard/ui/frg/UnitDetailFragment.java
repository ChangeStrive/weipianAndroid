package com.maya.android.vcard.ui.frg;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.dao.CircleDao;
import com.maya.android.vcard.dao.UnitDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.TitleMoreMenuLsvIconEntity;
import com.maya.android.vcard.entity.WeNoticeEntity;
import com.maya.android.vcard.entity.json.CircleGroupJsonEntity;
import com.maya.android.vcard.entity.json.UnitNoticeJsonEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UnitMemberResultEntity;
import com.maya.android.vcard.entity.result.UnitResultEntity;
import com.maya.android.vcard.ui.act.MessageMainActivity;
import com.maya.android.vcard.ui.adapter.UnitWeNoticeAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.TitleMoreMenuPopView;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.util.TitleMoreMenuPopHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 单位：详细页
 */
public class UnitDetailFragment extends BaseFragment {
    /** 企业id **/
    public static final String KEY_UNIT_ID = "key_unit_id";
    /** 单位Id **/
    private long unitId;
    /** 判断是否是管理员 **/
    private boolean isAdmin = false;
    /** menu不可点击项 **/
    private ArrayList<Integer> mMenuUnEnableList = new ArrayList<Integer>();
    /** 当前用户权限 **/
    private int curRole = -1;
    private ImageView mImvUnitIcon;
    private TextView mTxvUnitName, mTxvIndustryCategory, mTxvAdministrator;
    private ImageView mImvVerify, mImvBuness;
    private TextView mTxvUnitDetail, mTxvWeWebSite, mTxvPictureAlbum, mTxvWeNotice;
    private Button mBtnInternalChat, mBtnUnitMember;
    private ListView mLsvWeNotice;
    private TextView mTxvEmpty;
    private UnitWeNoticeAdapter mUnitWeNoticeAdapter;
    private TitleMoreMenuPopView mTitleMoreMenuPop;
    private UnitResultEntity unit;
    private URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
    private CardEntity card;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Bundle bundle = new Bundle();
             switch (v.getId()){
                 case R.id.txv_unit_detail:
                     //单位简介
                     bundle.putLong(UnitDetailIntroFragment.KEY_DETAIL_INTRO_UNIT_ID, unitId);
                     mFragmentInteractionImpl.onFragmentInteraction(UnitDetailIntroFragment.class.getName(), bundle);
                     break;
                 case R.id.txv_we_web_site:
                     //TODO 微网站
                     break;
                 case R.id.txv_picture_album:
                     //TODO 微画册
                     break;
                 case R.id.txv_we_notice:
                     //微公告
                     bundle.putLong(UnitDetailNotifyFragment.KEY_NOTIFY_PUBLISH_UNITID, unitId);
                     mFragmentInteractionImpl.onFragmentInteraction(UnitDetailNotifyFragment.class.getName() ,bundle);
                     break;
                 case R.id.btn_internal_chat:
                     //内部聊天
                     // 内部聊天
                     if(unit.getMessageGroupId() > 0){
                       long groupId = unit.getMessageGroupId();
                         toIntentCircleGroup(bundle, groupId);
                     }else{
                         //请求群讨论
                         requestCircleGroup();
                     }
                     break;
                 case R.id.btn_unit_member:
                     //单位成员
                     bundle.putLong(UnitRememberFragment.KEY_UNIT_REMEMBER_UNIT_ID, unitId);
                     bundle.putInt(UnitRememberFragment.KEY_UNIT_REMEMBER, Constants.UNIT.ENTERPRISE_MEMBER);
                     mFragmentInteractionImpl.onFragmentInteraction(UnitRememberFragment.class.getName() ,bundle);
                     break;
                 case R.id.imv_act_top_right:
                     //右上角弹窗
                     showTitleMoreMenuPop(v);
                     break;
             }
         }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEUNIT_ANNOUNCEMENT_LIST:
                    //获取企业公告
                    this.mTxvEmpty.setText(R.string.frg_common_nothing_data);
                    String memberResult = commandResult.optString("noticeList");
                    if(ResourceHelper.isNotEmpty(memberResult)){
                        Type memberType = new TypeToken<ArrayList<WeNoticeEntity>>(){}.getType();
                        ArrayList<WeNoticeEntity> memberList = JsonHelper.fromJson(memberResult, memberType);
                        mUnitWeNoticeAdapter.addItems(memberList);
                    }
                    break;
                case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_CREATE:
                    //发起群聊
                    long groupId = commandResult.optLong("messageGroupId");
                    CircleGroupJsonEntity groupJsonEntity = (CircleGroupJsonEntity) objects[0];
                    CircleDao.getInstance().add(groupId, groupJsonEntity);
                    Bundle bundle = new Bundle();
                    toIntentCircleGroup(bundle, groupId);
                    ActivityHelper.closeProgressDialog();
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_unit_detail, container, false);
        this.mImvUnitIcon = (ImageView) rootView.findViewById(R.id.imv_unit_icon);
        this.mTxvUnitName = (TextView) rootView.findViewById(R.id.txv_unit_name);
        this.mTxvIndustryCategory = (TextView) rootView.findViewById(R.id.txv_industry_category);
        this.mTxvAdministrator = (TextView) rootView.findViewById(R.id.txv_administrator);
        this.mImvVerify = (ImageView) rootView.findViewById(R.id.imv_user_head_verify);
        this.mImvBuness = (ImageView) rootView.findViewById(R.id.imv_user_head_bus);
        this.mTxvUnitDetail = (TextView) rootView.findViewById(R.id.txv_unit_detail);
        this.mTxvWeWebSite = (TextView) rootView.findViewById(R.id.txv_we_web_site);
        this.mTxvPictureAlbum = (TextView) rootView.findViewById(R.id.txv_picture_album);
        this.mTxvWeNotice = (TextView) rootView.findViewById(R.id.txv_we_notice);
        this.mBtnInternalChat = (Button) rootView.findViewById(R.id.btn_internal_chat);
        this.mBtnUnitMember = (Button) rootView.findViewById(R.id.btn_unit_member);
        this.mLsvWeNotice = (ListView) rootView.findViewById(R.id.lsv_list);
        this.mTxvEmpty = (TextView) rootView.findViewById(R.id.txv_lsv_empty);
        this.mLsvWeNotice.setEmptyView(this.mTxvEmpty);
        this.mTxvUnitDetail.setOnClickListener(this.mOnClickListener);
        this.mTxvWeWebSite.setOnClickListener(this.mOnClickListener);
        this.mTxvPictureAlbum.setOnClickListener(this.mOnClickListener);
        this.mTxvWeNotice.setOnClickListener(this.mOnClickListener);
        this.mBtnInternalChat.setOnClickListener(this.mOnClickListener);
        this.mBtnUnitMember.setOnClickListener(this.mOnClickListener);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.my_unit, false);
        super.mTitleAction.setActivityTopRightImvVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightImv(R.mipmap.img_top_more, this.mOnClickListener);
        this.mLsvWeNotice.setBackgroundColor(getActivity().getResources().getColor(R.color.color_ffffff));
        this.mUnitWeNoticeAdapter = new UnitWeNoticeAdapter(getActivity());
        this.mLsvWeNotice.setAdapter(this.mUnitWeNoticeAdapter);
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
            this.unitId = bundle.getLong(KEY_UNIT_ID, 0);
        }
        if(ResourceHelper.isNull(this.unit)){
            this.unit = UnitDao.getInstance().getEntity(this.unitId);
        }
        this.curRole = getCurUnitMemberRole();
        if(ResourceHelper.isNotNull(this.unit)){
            this.mTxvUnitName.setText(this.unit.getEnterpriseName());
            this.mTxvAdministrator.setText(UnitDao.getInstance().getAdminNames(this.unitId, "/"));
            this.mTxvIndustryCategory.setText(ResourceHelper.getBusinessNameByCode(getActivity(), this.unit.getClassLabel()));
            if(this.unit.getApproval() == DatabaseConstant.EnterpriseApproval.APPROVALED){
                this.mImvVerify.setImageResource(R.mipmap.img_card_mobile_vertify);
            }else{
                this.mImvVerify.setImageResource(R.mipmap.img_card_mobile_unvertify);
            }
            refreshMenu();
        }
        requestMyUnitNotice(this.unitId);
    }

    /**
     * 切换到内容聊天
     */
    private void toIntentCircleGroup(Bundle bundle, long groupId){
        Intent intent = new Intent();
        intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, MessageConversationFragment.class.getName());
        bundle.putInt(Constants.IntentSet.INTENT_KEY_CIRCLE_GROUP_TYPE, DatabaseConstant.CircleGroupType.CIRCLE_COMPANY);
        bundle.putLong(Constants.IntentSet.INTENT_KEY_MESSAGE_TAG_ID, groupId);
        intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
        mActivitySwitchTo.switchTo(MessageMainActivity.class, intent);
    }

    /**
     * 请求群讨论
     */
    private void requestCircleGroup(){
        ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
        CardEntity card = CurrentUser.getInstance().getCurrentVCardEntity();
        long cardId = 0;
        if(ResourceHelper.isNotNull(card)){
            cardId = card.getId();
        }
        CircleGroupJsonEntity groupEntity = new CircleGroupJsonEntity(cardId, unitId);
        String json = JsonHelper.toJson(groupEntity, CircleGroupJsonEntity.class);
        String mAccessToken = CurrentUser.getInstance().getToken();
        String msgUrl = ProjectHelper.fillRequestURL(urlEntity.getMessageGroupChatCreate());
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_CREATE, msgUrl, mAccessToken, json, this);
    }

    /**
     * 请求企业公告
     * @param unitId
     */
    private void requestMyUnitNotice(long unitId){
        if (NetworkHelper.isNetworkAvailable(getActivity())) {
            this.urlEntity = CurrentUser.getInstance().getURLEntity();
            if(ResourceHelper.isNotNull(this.urlEntity)){
                String mAccessToken = CurrentUser.getInstance().getToken();
                String url = ProjectHelper.fillRequestURL(this.urlEntity.getEnterpriseGetNotice());
                UnitNoticeJsonEntity jsonUnit = new UnitNoticeJsonEntity();
                jsonUnit.setEnterpriseId(unitId);
                String json = JsonHelper.toJson(jsonUnit,UnitNoticeJsonEntity.class);
                ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEUNIT_ANNOUNCEMENT_LIST, url, mAccessToken, json, this);
            }
        }
     }

    /**
     * 刷新右上角弹窗状态
     */
    private void refreshMenu(){
        if(ResourceHelper.isNotNull(this.unitId)){
            this.isAdmin = UnitDao.getInstance().isAdminForMe(this.unitId);
            this.mMenuUnEnableList.clear();
            if(this.unit.getApproval() != DatabaseConstant.EnterpriseApproval.UNAPPROVAL){
                this.mMenuUnEnableList.add(0);
//                if(this.curRole !=  DatabaseConstant.Role.CREATER){
//                    this.mMenuUnEnableList.add(1);
//                    this.mMenuUnEnableList.add(3);
//                }
            }
            if(this.unit.getApproval() == DatabaseConstant.EnterpriseApproval.UNAPPROVAL || !this.isAdmin){
                this.mMenuUnEnableList.add(1);
                this.mMenuUnEnableList.add(2);
                this.mMenuUnEnableList.add(3);
                this.mMenuUnEnableList.add(4);
            }
        }
    }

    /**
     * 右上角标题弹窗更多
     */
    private void showTitleMoreMenuPop(View v){
        if(ResourceHelper.isNull(this.mTitleMoreMenuPop)){
            this.mTitleMoreMenuPop = TitleMoreMenuPopHelper.getUnitDetailPop(getActivity());
            this.mTitleMoreMenuPop.setItemClickListener(new TitleMoreMenuPopView.MoreMenuItemClickListener() {
                @Override
                public void onItemClick(TitleMoreMenuLsvIconEntity menu) {
                    Bundle bundle = new Bundle();
                    switch (menu.getBusinessName()) {
                        case R.string.unit_auth:
                            //单位认证
                            bundle.putLong(UnitAuthFragment.KEY_UNIT_AUTH_UNIT_ID, unitId);
                            mFragmentInteractionImpl.onFragmentInteraction(UnitAuthFragment.class.getName(), bundle);
                            break;
                        case R.string.edt_unit_Introduction:
                            //单位简介
                            if (curRole  == DatabaseConstant.Role.CREATER) {//只有创始人才能修改单位简介
                                bundle.putLong(UnitDetailIntroFragment.KEY_DETAIL_INTRO_UNIT_ID, unitId);
                                mFragmentInteractionImpl.onFragmentInteraction(UnitDetailIntroFragment.class.getName(), bundle);
                            } else {
                                ActivityHelper.showToast(R.string.no_operation_permissions);
                            }
                            break;
                        case R.string.release_notice:
                            //发布公告
                            if (unit.getApproval() == DatabaseConstant.EnterpriseApproval.APPROVALED && curRole > DatabaseConstant.Role.MEMBER) {
                                bundle.putLong(UnitDetailNotifyPublishFragment.KEY_NOTIFY_PUBLISH_UNITID, unitId);
                                mFragmentInteractionImpl.onFragmentInteraction(UnitDetailNotifyPublishFragment.class.getName(), bundle);
                            } else {
                                //提示信息
                                ActivityHelper.showToast(R.string.no_operation_permissions);
                            }
                            break;
                        case R.string.permission_set:
                            //权限设置
                            if (curRole == DatabaseConstant.Role.CREATER) {//只有创始人才有权限设置
                                bundle.putLong(UnitRememberFragment.KEY_UNIT_REMEMBER_UNIT_ID, unitId);
                                bundle.putInt(UnitRememberFragment.KEY_UNIT_REMEMBER, Constants.UNIT.ENTERPRISE_MEMBER_ROLE);
                                mFragmentInteractionImpl.onFragmentInteraction(UnitRememberFragment.class.getName(), bundle);
                            } else {
                                ActivityHelper.showToast(R.string.no_operation_permissions);
                            }
                            break;
                        case R.string.administration_member:
                            //成员管理
                            if (curRole > DatabaseConstant.Role.MEMBER) {//只有管理员以上级别才能对成员进行管理
                                bundle.putLong(UnitRememberFragment.KEY_UNIT_REMEMBER_UNIT_ID, unitId);
                                bundle.putInt(UnitRememberFragment.KEY_UNIT_REMEMBER, Constants.UNIT.ENTERPRISE_MEMBER_MANAGE);
                                mFragmentInteractionImpl.onFragmentInteraction(UnitRememberFragment.class.getName(), bundle);
                            } else {
                                ActivityHelper.showToast(R.string.no_operation_permissions);
                            }
                            break;
                    }
                }
            });
        }
        this.mTitleMoreMenuPop.setItemUnEnable(this.mMenuUnEnableList);
        this.mTitleMoreMenuPop.showAtLocation(v, Gravity.FILL, 0, 0);
    }

    /**
     * 获取当前名片在当前单位中的权限
     * @return
     */
    private int getCurUnitMemberRole(){
       if(ResourceHelper.isNull(this.card)){
            this.card =  CurrentUser.getInstance().getCurrentVCardEntity();
        }
        if(ResourceHelper.isNotNull(this.card)){
            UnitMemberResultEntity curUnit = UnitDao.getInstance().getMemberEntity(this.unitId, card.getId());
            if(ResourceHelper.isNotNull(curUnit)){
                return curUnit.getRole();
            }
        }
        return -1;
    }
 }
