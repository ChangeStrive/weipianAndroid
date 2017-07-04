package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.dao.UnitDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.json.UnitMemberManageJsonEntity;
import com.maya.android.vcard.entity.json.UnitNoticeJsonEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UnitMemberListResultEntity;
import com.maya.android.vcard.entity.result.UnitMemberResultEntity;
import com.maya.android.vcard.entity.result.UnitResultEntity;
import com.maya.android.vcard.ui.adapter.UnitMembersAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.SideBar;
import com.maya.android.vcard.util.ConverChineseCharToEnHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.util.SideBarCharHintHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 单位：成员
 * <p>1、成员管理</p>
 * <p>2、权限管理</p>
 * <p>3、单位认证添加管理员</p>
 */
public class UnitRememberFragment extends BaseFragment {
    /** 类别 key **/
    public static final String KEY_UNIT_REMEMBER = "unit_remember_key";
    /** 单位Id key **/
    public static final String KEY_UNIT_REMEMBER_UNIT_ID = "key_unit_remember_unit_id";
    /** 记录当前页功能 **/
    private int mIntentCode = Constants.UNIT.ENTERPRISE_MEMBER;
    /** 记录当前页选中Item项 **/
    private int mMemberManagePosition = -1;
    /** 是否管理员 **/
    private boolean isDelManager;
    /** 是否处于滑动状态 **/
    private boolean isListSroll = false;
    /** 单位Id **/
    private long unitId;
    /** 当前单位信息 **/
    private UnitResultEntity unit;
    /** 当前名片Id **/
    private long curCardId;
    /** 当前用户名片 **/
    private CardEntity card;
    /** 当前用户权限 **/
    private int curRole = -1;
    private URLResultEntity urlEntity;
    private ArrayList<UnitMemberResultEntity> members;
    private EditText mEdtEnterSearch;
    private ListView mLsvMembers;
    private TextView mTxvEmpty;
    private SideBar mSibMember;
    private UnitMembersAdapter mUnitMemberAdapter;
    private CustomDialogFragment mDialogFragmentSetAdministrator, mDialogFragmentDelMember;
    private SideBarCharHintHelper mCharHintHelper;
    private ArrayList<Long> mMemberIdList = new ArrayList<Long>();

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           switch (mIntentCode){
                case Constants.UNIT.ENTERPRISE_MEMBER:
                    //单位成员
                    break;
                case Constants.UNIT.ENTERPRISE_MEMBER_MANAGE:
                    //管理成员
                    UnitMemberResultEntity member = mUnitMemberAdapter.getItem(position);
                    if(curRole > member.getRole()){
                        showDialogFragmentDelMember(position);
                    }else{
                        ActivityHelper.showToast(R.string.no_operation_permissions);
                    }
                    break;
                case Constants.UNIT.ENTERPRISE_MEMBER_ROLE:
                    //权限管理
                    showDialogFragmentSetAdministrator(position);
                    break;
                case Constants.UNIT.ERTERPRISE_APPROVAL:
                    //单位认证添加管理员
                    Bundle bundle = new Bundle();
                    bundle.putInt(UnitAuthFragment.KEY_FOUNDER_ADD, position);
                    bundle.putLong(UnitAuthFragment.KEY_UNIT_AUTH_UNIT_ID, unitId);

                    break;
            }
        }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(!super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEUNIT_ANNOUNCEMENT_LIST:
                    UnitMemberListResultEntity unitMemberList = JsonHelper.fromJson(commandResult,UnitMemberListResultEntity.class);
                    if(ResourceHelper.isNotNull(unitMemberList)){
                        this.members = unitMemberList.getMemberList();
                        UnitDao.getInstance().addMember(this.unitId, this.members);
                        this.mUnitMemberAdapter.addItems(this.members);

                        this.mTxvEmpty.setText(R.string.frg_common_nothing_data);
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onResponseFail(int tag, int responseCode, String msgInfo) {
        super.onResponseFail(tag, responseCode, msgInfo);
        ActivityHelper.closeProgressDialog();
    }

    @Override
    protected void onResponseSuccess(int tag, String msgInfo) {
        super.onResponseSuccess(tag, msgInfo);
        ActivityHelper.closeProgressDialog();
        switch (tag){
            case Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_REMOVE:
                //移除成员
                this.mUnitMemberAdapter.removeItem(this.mMemberManagePosition);
                UnitDao.getInstance().deleteMember(this.unitId, mMemberIdList);
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_SET_MANAGE:
                //设置管理员
                this.mUnitMemberAdapter.setAdministrator(this.mMemberManagePosition, DatabaseConstant.Role.ADMIN);
                UnitDao.getInstance().updateMemberRole(this.unitId, mMemberIdList.get(0), DatabaseConstant.Role.ADMIN);
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_DELETE_MANAGE:
                //撤销管理员
                this.mUnitMemberAdapter.setAdministrator(this.mMemberManagePosition, DatabaseConstant.Role.MEMBER);
                UnitDao.getInstance().updateMemberRole(this.unitId, mMemberIdList.get(0), DatabaseConstant.Role.MEMBER);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unit_remember, container, false);
        this.mEdtEnterSearch = (EditText) view.findViewById(R.id.edt_enter_search_unit_remember);
        this.mLsvMembers = (ListView) view.findViewById(R.id.lsv_list);
        this.mTxvEmpty = (TextView) view.findViewById(R.id.txv_lsv_empty);
        this.mLsvMembers.setEmptyView(this.mTxvEmpty);
        this.mSibMember = (SideBar) view.findViewById(R.id.sib_unit_member);
        this.mLsvMembers.setDivider(null);
        this.mLsvMembers.setDividerHeight(0);
        this.mLsvMembers.setOnItemClickListener(this.mOnItemClickListener);
        this.mLsvMembers.setOnScrollListener(this.mOnScrollListener);
        this.mSibMember.setOnTouchingLetterChangedListener(this.mOnTouchingLetterChangedListener);
        this.mEdtEnterSearch.addTextChangedListener(this.mEdtSearchTextWatcher);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
            this.mIntentCode = bundle.getInt(KEY_UNIT_REMEMBER, Constants.UNIT.ENTERPRISE_MEMBER);
            this.unitId = bundle.getLong(KEY_UNIT_REMEMBER_UNIT_ID, 0);
        }
        this.unit = UnitDao.getInstance().getEntity(this.unitId);
        this.curRole =  getCurUnitMemberRole();
        this.mUnitMemberAdapter = new UnitMembersAdapter(getActivity());
        this.mLsvMembers.setAdapter(this.mUnitMemberAdapter);
        this.mCharHintHelper = SideBarCharHintHelper.getIntance(getActivity(), this.mSibMember);
        switch (mIntentCode){
            case Constants.UNIT.ENTERPRISE_MEMBER:
                //单位成员
                super.mTitleAction.setActivityTitle(R.string.unit_member,false);
                this.members = UnitDao.getInstance().getMemberList(this.unitId, true);
                break;
            case Constants.UNIT.ENTERPRISE_MEMBER_MANAGE:
                //管理成员
                super.mTitleAction.setActivityTitle(R.string.administration_member,false);
                this.members = UnitDao.getInstance().getMemberList(this.unitId, true);
                break;
            case Constants.UNIT.ENTERPRISE_MEMBER_ROLE:
                //权限管理
                super.mTitleAction.setActivityTitle(R.string.permission_set,false);
                this.members = UnitDao.getInstance().getMemberList(this.unitId, true);
                break;
            case Constants.UNIT.ERTERPRISE_APPROVAL:
                //单位认证添加管理员
                super.mTitleAction.setActivityTitle(R.string.add_dministration,false);
                CardEntity card = CurrentUser.getInstance().getCurrentVCardEntity();
                if(ResourceHelper.isNotNull(card)){
                    this.curCardId = card.getId();
                }
                this.members = UnitDao.getInstance().getMemberList(this.unitId, this.curCardId);
                break;
        }
        if(ResourceHelper.isNull(this.members) || this.members.size() == 0){
            requestUnitMembers();
        }else{
            this.mUnitMemberAdapter.addItems(this.members);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(ResourceHelper.isNotNull(this.mCharHintHelper)) {
            this.mCharHintHelper.clearCharHint();
        }
    }

    /**
     * ListView滑动监听
     */
    private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            isListSroll = scrollState == SCROLL_STATE_FLING || scrollState == SCROLL_STATE_TOUCH_SCROLL;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(isListSroll){
                int curPosition = firstVisibleItem+ (visibleItemCount >> 1);
                UnitMemberResultEntity item = mUnitMemberAdapter.getItem(curPosition);
                if(Helper.isNotNull(item)){
                    String curAlpha = ConverChineseCharToEnHelper.getFirstLetter(item.getDisplayName()).toUpperCase();
                    if(!mCharHintHelper.getCharDelay()){
                        mCharHintHelper.showCharHint(curAlpha);
                    }
                }
            }
        }
    };

    /**
     * 右侧字母选择事件
     */
    private SideBar.OnTouchingLetterChangedListener mOnTouchingLetterChangedListener = new SideBar.OnTouchingLetterChangedListener() {

        @Override
        public void onTouchingLetterChanged(String s) {
            if(!mCharHintHelper.getCharDelay()){
                mSibMember.setBackgroundResource(R.drawable.bg_sidebar);
                mCharHintHelper.showCharHint(s);
            }
        }
    };

    /**
     * 输入框收索监听
     */
    private TextWatcher mEdtSearchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(ResourceHelper.isNotNull(s)){
                mUnitMemberAdapter.getFilter().filter(s);
            }
        }
    };

    /**
     * 设置成员权限对话框
     */
    private void showDialogFragmentSetAdministrator(int position){
        this.mMemberManagePosition = position;
        UnitMemberResultEntity member = this.mUnitMemberAdapter.getItem(position);
        this.isDelManager = member .getRole() == DatabaseConstant.Role.ADMIN;
        int titleId;
        String msg = "";
        if(this.isDelManager){
            titleId = R.string.cancel_administrator;
            msg = getString(R.string.remove_member_administrator, member.getDisplayName());
        }else{
            titleId = R.string.set_administrator;
            msg = getString(R.string.set_member_administrator, member.getDisplayName());
        }
        if(ResourceHelper.isNull(this.mDialogFragmentSetAdministrator)){
            CustomDialogFragment.DialogFragmentInterface onClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        long memberId = mUnitMemberAdapter.getItem(mMemberManagePosition).getId();
                        mMemberIdList.clear();
                        mMemberIdList.add(memberId);
                        if(isDelManager){
                            //移除管理员
                            requestUnitMemberManager(DatabaseConstant.Role.MEMBER, Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_DELETE_MANAGE);
                        }else{
                            //设置为管理员
                            requestUnitMemberManager(DatabaseConstant.Role.ADMIN, Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_SET_MANAGE);
                        }
                    }
                }
            };
            this.mDialogFragmentSetAdministrator = DialogFragmentHelper.showCustomDialogFragment(titleId, msg, R.string.frg_text_cancel, R.string.frg_text_ok, onClick);
        }
        this.mDialogFragmentSetAdministrator.setTitle(titleId).setMessage(msg);
        this.mDialogFragmentSetAdministrator.show(getFragmentManager(), "mDialogFragmentSetAdministrator");
    }

    /**
     * 移除成员对话框
     */
    private void showDialogFragmentDelMember(int position){
        this.mMemberManagePosition = position;
        String msg = getString(R.string.sure_remove_member, this.mUnitMemberAdapter.getItem(position).getDisplayName());
        if(ResourceHelper.isNull(this.mDialogFragmentDelMember)){
            CustomDialogFragment.DialogFragmentInterface onClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        long memberId = mUnitMemberAdapter.getItem(mMemberManagePosition).getId();
                        mMemberIdList.clear();
                        mMemberIdList.add(memberId);
                        requestUnitMemberManager(DatabaseConstant.Role.REMOVE, Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_REMOVE);
                    }
                }
            };
            this.mDialogFragmentDelMember = DialogFragmentHelper.showCustomDialogFragment(R.string.remove_member, msg, R.string.frg_text_cancel, R.string.frg_text_ok, onClick);
        }
        this.mDialogFragmentDelMember.show(getFragmentManager(), "mDialogFragmentDelMember");
    }

    /**
     * 权限管理、移除
     * @param role
     * @param tag
     */
    private void requestUnitMemberManager(int role, int tag){
        if(ResourceHelper.isNull(this.urlEntity)){
            this.urlEntity = CurrentUser.getInstance().getURLEntity();
        }
        ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
        String requestUrl = ProjectHelper.fillRequestURL(this.urlEntity.getEnterpriseMemberRoleSave());
        String mAccessToken = CurrentUser.getInstance().getToken();
        UnitMemberManageJsonEntity unitMemberManage = new UnitMemberManageJsonEntity();
        unitMemberManage.setEnterpriseId(this.unitId);
        unitMemberManage.setMemberIdList(this.mMemberIdList);
        unitMemberManage.setRole(role);
        String json = JsonHelper.toJson(unitMemberManage, UnitMemberManageJsonEntity.class);
        ReqAndRespCenter.getInstance().postForResult(tag, requestUrl, mAccessToken, json, this);
    }

    /**
     * 请求单位成员列表
     */
    private void requestUnitMembers(){
        if(ResourceHelper.isNull(this.urlEntity)){
            this.urlEntity = CurrentUser.getInstance().getURLEntity();
        }
        String mAccessToken = CurrentUser.getInstance().getToken();
        String requestUrl = ProjectHelper.fillRequestURL(this.urlEntity.getEnterPriseMemberList());
        UnitNoticeJsonEntity unitMembers = new UnitNoticeJsonEntity();
        unitMembers.setEnterpriseId(this.unitId);
        String json = JsonHelper.toJson(unitMembers, UnitNoticeJsonEntity.class);
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEUNIT_ANNOUNCEMENT_LIST, requestUrl, mAccessToken, json, this);
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
