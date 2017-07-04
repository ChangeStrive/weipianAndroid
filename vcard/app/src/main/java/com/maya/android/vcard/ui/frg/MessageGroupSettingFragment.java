package com.maya.android.vcard.ui.frg;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.dao.CircleDao;
import com.maya.android.vcard.dao.MessageSessionDao;
import com.maya.android.vcard.dao.UnitDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CustomLsvIntegerEntity;
import com.maya.android.vcard.entity.SettingEntity;
import com.maya.android.vcard.entity.json.CircleGroupIdJsonEntity;
import com.maya.android.vcard.entity.json.CircleGroupMemberJsonEntity;
import com.maya.android.vcard.entity.json.CircleGroupMemberManageJsonEntity;
import com.maya.android.vcard.entity.json.CircleGroupUpdateJsonEntity;
import com.maya.android.vcard.entity.json.UnitMemberManageJsonEntity;
import com.maya.android.vcard.entity.result.CircleGroupMemberListResultEntity;
import com.maya.android.vcard.entity.result.CircleGroupMemberResultEntity;
import com.maya.android.vcard.entity.result.CircleGroupResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.ui.act.CardcaseMainActivity;
import com.maya.android.vcard.ui.adapter.CustomLsvIntegerAdapter;
import com.maya.android.vcard.ui.adapter.GroupMemberAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.NameLengthFilter;
import com.maya.android.vcard.ui.widget.SlipButton;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.CustomLsvContentHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 聊天：群设置
 */
public class MessageGroupSettingFragment extends BaseFragment {
    private static final String TAG = MessageGroupSettingFragment.class.getName();
    public static final int GROUP_ADD_MEMBER = 22222;
    private static final int GROUP_NAME_LENGTH_MAX = 5;
    /** 推送群聊组的TagId **/
    private long groupId;
    /** 群聊组类型 **/
    private int mGroupType;
    /** 当前用户权限 **/
    private int curRole;
    /** 修改后的组名称 **/
    private String mGroupNameNew;
    /**是否有修改群信息**/
    private boolean isChangeNews = false;
    /** 当前选中的项 **/
    private int curPosition;
    /** 单位信息 **/
    private CircleGroupResultEntity mGroupEntity;
    private SettingEntity mSetting = CurrentUser.getInstance().getSetting();
    private TextView mTxvName, mTxvChatLog;
    private SlipButton mSlbNewsNotify;
    private GridView mGrvGroupMember;
    private GroupMemberAdapter mGroupMemberAdapter;
    private Button mBtnSubmit;
    private  EditText mEdtAddGroup;
    private CustomDialogFragment mDialogFragmentGroupNameDialog, mDialogFragmentClearChatLog, mDialogFragmentCircleGroupQuit, mDialogFragmentCircleGroupDissolution,
                                    mDialogFragmentCircleGroupDel, mDialogFragmentCircleGroupSetMember, mDialogFragmentCircleGroupSetAdmin, mDialogFragmentCreateSelect;
    private URLResultEntity mUrlEntity = CurrentUser.getInstance().getURLEntity();
    private CustomLsvIntegerAdapter mLsvAdapter;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txv_clear_chat_log:
                    //清空聊天记录
                    showClearAllChatLog();
                    break;
                case R.id.txv_name:
                    if(curRole != DatabaseConstant.Role.CREATER){
                        ActivityHelper.showToast(R.string.you_are_no_create);
                        return;
                    }
                    //网络验证
                    if(!NetworkHelper.isNetworkAvailable(getActivity())){
                        ActivityHelper.showToast(R.string.no_network);
                        return;
                    }
                    //修改群名称
                    showAddGroupDialog();
                    break;
                case R.id.btn_submit:
                    //网络验证
                    if(!NetworkHelper.isNetworkAvailable(getActivity())){
                        ActivityHelper.showToast(R.string.no_network);
                        return;
                    }
                    if(curRole == DatabaseConstant.Role.CREATER){
                        //解散聊天组
                        showDialogFragmentCircleGroupDissolution();
                    }else{
                        //退出聊天组
                        showDialogFragmentCircleGroupQuit();
                    }
                    break;
            }
        }
    };

    /**
     * 消息提示音按钮监听
     */
    private SlipButton.OnSlipChangedListener mOnSlipChangedListener = new SlipButton.OnSlipChangedListener() {
        @Override
        public void OnChanged(View v, String strName, boolean checkState) {
            switch (v.getId()){
                case R.id.sbtn_frg_notice_chatting_records:
                    mSetting.setAlertNewMessageForGroup(checkState);
                    break;
            }
        }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(!  super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_MEMBER_LIST:
                    //获取讨论组成员回调
                    CircleGroupMemberListResultEntity memberListResultEntity = JsonHelper.fromJson(commandResult, CircleGroupMemberListResultEntity.class);
                    if(ResourceHelper.isNotNull(memberListResultEntity)){
                        CircleDao.getInstance().addMember(memberListResultEntity.getMemberList());
                        this.curRole = CircleDao.getInstance().getMyRole(this.groupId);
                        this.mGroupMemberAdapter.addItems(getMemberLis(this.groupId));
                        this.mGroupEntity = CircleDao.getInstance().getEntity(this.groupId);
                        refreshUI(this.mGroupEntity, this.curRole);
                        sendBroadcastMessage();
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
            case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_NAME_UPDATE:
                //修改群名称
                CircleDao.getInstance().updateName(this.groupId, this.mGroupNameNew);
                this.mGroupEntity.setGroupName(this.mGroupNameNew);
                this.mTxvName.setText(getString(R.string.group_title, this.mGroupEntity.getGroupName(), this.mGroupEntity.getMemberCount()));
                this.isChangeNews = true;
                ActivityHelper.closeProgressDialog();
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_QUIT:
                //退出群聊天
            case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_DELETE:
                //解散群聊天回调
                CircleDao.getInstance().delete(this.groupId);
                MessageSessionDao.getInstance().deleteByGroup(this.groupId, true);
                sendBroadcastMessage();
                ActivityHelper.closeProgressDialog();
                mFragmentInteractionImpl.onActivityFinish();
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_MEMBER_DELETE:
                //请求移除成员回调
                CircleGroupMemberResultEntity member = mGroupMemberAdapter.getItem(curPosition);
                CircleDao.getInstance().deleteMember(groupId, member.getAccountId());
                String body = getString(R.string.group_chat_member_remove , member.getDisplayName());
                MessageSessionDao.getInstance().add(this.groupId, Constants.MessageContentType.SESSION_GROUP_TEXT, body, 0);
                this.isChangeNews = true;
                this.mGroupMemberAdapter.removeItem(member);
                ActivityHelper.closeProgressDialog();
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_SET_MANAGE:
                //设置管理员
                CircleGroupMemberResultEntity member1 = mGroupMemberAdapter.getItem(this.curPosition);
                this.mGroupMemberAdapter.setAdministrator(this.curPosition, DatabaseConstant.Role.ADMIN);
                ActivityHelper.closeProgressDialog();
                if(ResourceHelper.isNotNull(member1)){
                    UnitDao.getInstance().updateMemberRole(this.groupId, member1.getMemberId(), DatabaseConstant.Role.ADMIN);
                }
               break;
            case Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_DELETE_MANAGE:
                //撤销管理员
                this.mGroupMemberAdapter.setAdministrator(this.curPosition, DatabaseConstant.Role.MEMBER);
                ActivityHelper.closeProgressDialog();
                CircleGroupMemberResultEntity member2 = mGroupMemberAdapter.getItem(this.curPosition);
                if(ResourceHelper.isNotNull(member2)){
                    UnitDao.getInstance().updateMemberRole(this.groupId, member2.getMemberId(), DatabaseConstant.Role.MEMBER);
                }
                break;
        }
    }

    @Override
    protected void onResponseFail(int tag, int responseCode, String msgInfo) {
        super.onResponseFail(tag, responseCode, msgInfo);
        //TODO 失败的回调
        switch (tag){
            case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_MEMBER_LIST:
                //获取讨论组成员回调
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_NAME_UPDATE:
                //修改群名称
                ActivityHelper.closeProgressDialog();
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_QUIT:
                //退出群聊天
                ActivityHelper.closeProgressDialog();
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_DELETE:
                //解散群聊天回调
                ActivityHelper.closeProgressDialog();
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_MEMBER_DELETE:
                //请求移除成员类
                ActivityHelper.closeProgressDialog();
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_SET_MANAGE:
                //设置管理员
            case Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_DELETE_MANAGE:
                //撤销管理员
                ActivityHelper.closeProgressDialog();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GROUP_ADD_MEMBER:
                if(resultCode == getActivity().RESULT_OK){
                    boolean hasMember = CircleDao.getInstance().isHasMember(this.groupId);
                    if(hasMember){
                        CircleGroupResultEntity mGroupEntity = CircleDao.getInstance().getEntity(this.groupId);
                        this.curRole = CircleDao.getInstance().getMyRole(this.groupId);
                        this.mGroupMemberAdapter.addItems(getMemberLis(this.groupId));
                        refreshUI(mGroupEntity, this.curRole);
                    }else{
                        requestCircleGroupMember(this.groupId);
                    }
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_group_setting, container, false);
        this.mTxvName = (TextView) view.findViewById(R.id.txv_name);
        this.mTxvChatLog = (TextView) view.findViewById(R.id.txv_clear_chat_log);
        this.mSlbNewsNotify = (SlipButton) view.findViewById(R.id.sbtn_frg_notice_chatting_records);
        this.mGrvGroupMember = (GridView) view.findViewById(R.id.grv_group_member);
        this.mGrvGroupMember.setBackgroundColor(Color.WHITE);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mTxvChatLog.setOnClickListener(this.mOnClickListener);
        this.mTxvName.setOnClickListener(this.mOnClickListener);
        this.mSlbNewsNotify.setOnChangedListener("", this.mOnSlipChangedListener);
        this.mGrvGroupMember.setOnItemClickListener(this.mOnItemClickListener);
        this.mGrvGroupMember.setOnItemLongClickListener(this.mOnItemLongClickListener);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTitle(R.string.circle_group_chat_msg, false);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        this.mGroupMemberAdapter = new GroupMemberAdapter(getActivity());
        this.mGrvGroupMember.setAdapter(this.mGroupMemberAdapter);
        this.mSlbNewsNotify.setChecked(this.mSetting.isAlertNewMessageForGroup());
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
            this.groupId = bundle.getLong(Constants.IntentSet.INTENT_KEY_MESSAGE_TAG_ID, 0);
            this.mGroupType = bundle.getInt(Constants.IntentSet.INTENT_KEY_CIRCLE_GROUP_TYPE, 0);
        }
        boolean hasMember = CircleDao.getInstance().isHasMember(this.groupId);
        if(hasMember){
            this.mGroupEntity = CircleDao.getInstance().getEntity(this.groupId);
            this.curRole = CircleDao.getInstance().getMyRole(this.groupId);
            this.mGroupMemberAdapter.addItems(getMemberLis(this.groupId));
            refreshUI(mGroupEntity, this.curRole);
        }else{
            requestCircleGroupMember(this.groupId);
        }

    }

    @Override
    protected void onBackPressed() {
        super.onBackPressed();
        if(this.isChangeNews){
            sendBroadcastMessage();
        }
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(curRole > DatabaseConstant.Role.MEMBER){
                CircleGroupMemberResultEntity member = mGroupMemberAdapter.getItem(position);
                if(ResourceHelper.isNotNull(member)){
                    Intent intent = new Intent(getActivity(), CardcaseMainActivity.class);
                    Bundle bundle = new Bundle();
                    if(member.getAccountId() == R.string.common_add){
                        intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, CardcaseChooseFragment.class.getName());
                        bundle.putLong(Constants.IntentSet.INTENT_KEY_MESSAGE_TAG_ID, groupId);
                        bundle.putInt(CardcaseChooseFragment.KEY_CARDCASE_CHOOSE, CardcaseChooseFragment.CARDCASE_CHOOSE_SELECT_ADD_MEMBER);
                        intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                        startActivityForResult(intent, GROUP_ADD_MEMBER);
                    }
                }
            }
        }
    };

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(curRole > DatabaseConstant.Role.MEMBER){
                curPosition = position;
                CircleGroupMemberResultEntity member = mGroupMemberAdapter.getItem(position);
                if(ResourceHelper.isNotNull(member)){
                    int role = member.getRole();
                    if(curRole > role){
                        if(curRole == DatabaseConstant.Role.CREATER && DatabaseConstant.CircleGroupType.CIRCLE_COMPANY == mGroupType){//创建者
                            showDialogFragmentCreateSelect(role);
                        }else{
                            //管理员
                            showDialogFragmentCircleGroupDel(member.getDisplayName());
                        }
                    }else{
                        ActivityHelper.showToast(R.string.insufficient_authority);
                    }
                }
            }else{
                ActivityHelper.showToast(R.string.you_are_no_admin);
            }
            return true;
        }
    };

    /**
     * 刷新UI
     * @param mGroupEntity
     * @param curRole
     */
    private void refreshUI( CircleGroupResultEntity mGroupEntity, int curRole){
        if(ResourceHelper.isNotNull(mGroupEntity)){
            this.mTxvName.setText(mGroupEntity.getSessionTitle());
        }
        if(DatabaseConstant.CircleGroupType.CIRCLE_COMPANY == mGroupType){
            ButtonHelper.setButtonEnable(this.mBtnSubmit, R.color.color_787878, false);
        }else{
            if(curRole == DatabaseConstant.Role.CREATER){
                this.mBtnSubmit.setText(R.string.group_chat_dismiss_group);
            }else{
                this.mBtnSubmit.setText(R.string.group_chat_quit_group);
            }
            ButtonHelper.setButtonEnable(this.mBtnSubmit, R.color.color_399c2f, true);
        }
    }

    /**
     * 获取重组的所有成员对象
     * @param groupId
     * @return
     */
    private ArrayList<CircleGroupMemberResultEntity> getMemberLis(long groupId){
        ArrayList<CircleGroupMemberResultEntity> mMemberList = new ArrayList<CircleGroupMemberResultEntity>();
        //获取群聊普通成员
        ArrayList<CircleGroupMemberResultEntity> commonList = CircleDao.getInstance().getMemberList(groupId, DatabaseConstant.Role.MEMBER);
        if(Helper.isNotNull(commonList)){
            mMemberList.addAll(commonList);
        }
        //获取群聊管理员
        ArrayList<CircleGroupMemberResultEntity> mAdminList = CircleDao.getInstance().getMemberList(groupId, DatabaseConstant.Role.ADMIN);
        if(ResourceHelper.isNotNull(mAdminList)){
            mMemberList.addAll(0, mAdminList);
        }
        //获取群聊创建者
        CircleGroupMemberResultEntity mCreater = CircleDao.getInstance().getMemberCreater(groupId);
        if(ResourceHelper.isNotNull(mCreater)){
            mMemberList.add(0, mCreater);
        }
        return mMemberList;
    }

   /**
     * 发送广播
     */
    private void sendBroadcastMessage(){
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        Intent refreshIntent = new Intent();
        if(Helper.isNotEmpty(this.mGroupNameNew)){
            refreshIntent.putExtra(Constants.IntentSet.INTENT_KEY_IS_UPDATE_CIRCLE_GROUP_NAME, true);
        }
        refreshIntent.setAction(Constants.ActionIntent.ACTION_INTENT_SESSION_CHAT);
        broadcastManager.sendBroadcast(refreshIntent);
        refreshIntent.setAction(Constants.ActionIntent.ACTION_INTENT_MESSAGE_MAIN);
        broadcastManager.sendBroadcast(refreshIntent);
    }

    /**
     * 修改群聊天名称对话框
     */
    private void showAddGroupDialog(){
        if(ResourceHelper.isNull(this.mDialogFragmentGroupNameDialog)){
            View mView = LayoutInflater.from(getActivity()).inflate(R.layout.item_add_group_edt, null);
            this.mEdtAddGroup = (EditText) mView.findViewById(R.id.edt_add_group);
            InputFilter[] filters = {new NameLengthFilter(GROUP_NAME_LENGTH_MAX, GROUP_NAME_LENGTH_MAX *2)};  //设置编辑框字符长度
            this.mEdtAddGroup.setFilters(filters);
            int margin = ResourceHelper.getDp2PxFromResouce( R.dimen.dimen_15dp);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mEdtAddGroup.getLayoutParams();
            params.width = DialogFragmentHelper.getCustomDialogFragmentWidth() - (margin * 2);
            this.mEdtAddGroup.setLayoutParams(params);
            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        String newGroupName = mEdtAddGroup.getText().toString();
                        if(ResourceHelper.isNotEmpty(newGroupName)){
                            mGroupNameNew = newGroupName;
                            requestCircleGroupName(newGroupName);
                            mDialogFragmentGroupNameDialog.getDialog().dismiss();
                        }else{
                            ActivityHelper.showToast(R.string.enter_groups_name_please);
                        }
                    }else{
                        mDialogFragmentGroupNameDialog.getDialog().dismiss();
                    }
                }
            };
            this.mDialogFragmentGroupNameDialog = DialogFragmentHelper.showCustomDialogFragment(R.string.edit_group_chat_name, mView, R.string.frg_text_cancel, R.string.frg_text_ok, mOnClick);
            this.mDialogFragmentGroupNameDialog.setManualDismiss(true);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(Message.obtain());
            }
        }).start();
        this.mDialogFragmentGroupNameDialog.show(getFragmentManager(), "mDialogFragmentGroupNameDialog");
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(ResourceHelper.isNotNull(mGroupEntity)){
                mGroupNameNew = mGroupEntity.getGroupName();
                mEdtAddGroup.setText(mGroupNameNew);
            }
        }
    };

    /**
     * 清空聊天记录
     */
    private void showClearAllChatLog(){
        if(Helper.isNull(this.mDialogFragmentClearChatLog)){

            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface(){
                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        int deleteMessageLog = MessageSessionDao.getInstance().deleteByGroup(groupId, false);
                        if(deleteMessageLog > 0){
                            isChangeNews = true;
                            ActivityHelper.showToast(R.string.common_delete_ok);
                        }else{
                            ActivityHelper.showToast(R.string.common_delete_fail);
                        }
                    }
                }
            };
            this.mDialogFragmentClearChatLog = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_setting_notice_clear_chat_log_all, R.string.frg_setting_notice_clear_chat_friend_log_all_sure, R.string.frg_text_cancel, R.string.frg_text_ok, dialogOnclick);
        }
        this.mDialogFragmentClearChatLog.show(getFragmentManager(),"mDialogFragmentClearChatLog" );
    }

    /**
     * 退出群对话框
     */
    private void showDialogFragmentCircleGroupQuit(){
        if(ResourceHelper.isNull(this.mDialogFragmentCircleGroupQuit)){
            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface(){
                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        requestCircleGroupQuit();
                    }
                }
            };
            this.mDialogFragmentCircleGroupQuit = DialogFragmentHelper.showCustomDialogFragment( R.string.group_chat_quit_group,R.string.confirm_quit_group,R.string.frg_text_cancel ,R.string.frg_text_ok,dialogOnclick);
            this.mDialogFragmentCircleGroupQuit.show(getFragmentManager(), "mDialogFragmentCircleGroupQuit");
        }
    }

    /**
     * 解散群对话框
     */
    private void showDialogFragmentCircleGroupDissolution(){
        if(ResourceHelper.isNull(this.mDialogFragmentCircleGroupDissolution)){
            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface(){
                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        requestCircleGroupDissolution();
                    }
                }
            };
            this.mDialogFragmentCircleGroupDissolution = DialogFragmentHelper.showCustomDialogFragment( R.string.group_chat_dismiss_group,R.string.confirm_dismiss_group,R.string.frg_text_cancel ,R.string.frg_text_ok,dialogOnclick);
            this.mDialogFragmentCircleGroupDissolution.show(getFragmentManager(), "mDialogFragmentCircleGroupDissolution");
        }
    }

    /**
     * 创建者选项对话框
     * @param role 对应成员权限
     */
    private void showDialogFragmentCreateSelect(int role){
        if(ResourceHelper.isNull(this.mDialogFragmentCreateSelect)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            this.mLsvAdapter = new CustomLsvIntegerAdapter(getActivity());
            mLsvItem.setAdapter(this.mLsvAdapter);
            this.mLsvAdapter.setShowRadio(false);
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CustomLsvIntegerEntity item = mLsvAdapter.getItem(position);
                    CircleGroupMemberResultEntity member = mGroupMemberAdapter.getItem(curPosition);
                    if (ResourceHelper.isNotNull(member)) {
                        switch (item.getContentId()) {
                            case R.string.set_administrator:
                                //设置管理员
                                showDialogFragmentCircleGroupSetAdmin(member.getDisplayName());
                                break;
                            case R.string.cancel_administrator:
                                //撤销管理员
                                showDialogFragmentCircleGroupSetMember(member.getDisplayName());
                                break;
                            case R.string.remove_member:
                                //移除成员
                                showDialogFragmentCircleGroupDel(member.getDisplayName());
                                 break;
                        }
                    }
                    mDialogFragmentCreateSelect.getDialog().dismiss();
                }
            });
            this.mDialogFragmentCreateSelect = DialogFragmentHelper.showCustomDialogFragment(R.string.group_create, mLsvItem);
        }
        this.mLsvAdapter.addItems(CustomLsvContentHelper.getCircleGroupCreateSelectItems(role));
        this.mDialogFragmentCreateSelect.show(getFragmentManager(), "mDialogFragmentCreateSelect");
    }

    /**
     * 设置管理员身份对话框
     * @param displayName
     */
    private void showDialogFragmentCircleGroupSetAdmin(String displayName){
        String msg = getString(R.string.set_group_member_administrator, displayName);
        if(ResourceHelper.isNull(this.mDialogFragmentCircleGroupSetAdmin)){
            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface(){
                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        //设置管理员
                        requestUnitMemberManager(DatabaseConstant.Role.ADMIN, mGroupMemberAdapter.getItem(curPosition), Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_SET_MANAGE);
                    }
                }
            };
            this.mDialogFragmentCircleGroupSetAdmin = DialogFragmentHelper.showCustomDialogFragment(R.string.set_administrator, msg, R.string.frg_text_cancel ,R.string.frg_text_ok, dialogOnclick);
        }
        this.mDialogFragmentCircleGroupSetAdmin.setMessage(msg);
        this.mDialogFragmentCircleGroupSetAdmin.show(getFragmentManager(), "mDialogFragmentCircleGroupSetAdmin");
    }

    /**
     * 撤销管理员身份对话框
     * @param displayName
     */
    private void showDialogFragmentCircleGroupSetMember(String displayName){
        String msg = getString(R.string.remove_group_member_administrator, displayName);
        if(ResourceHelper.isNull(this.mDialogFragmentCircleGroupSetMember)){
            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface(){
                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        //撤销管理员
                       requestUnitMemberManager(DatabaseConstant.Role.MEMBER, mGroupMemberAdapter.getItem(curPosition), Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEMBER_DELETE_MANAGE);
                    }
                }
            };
            this.mDialogFragmentCircleGroupSetMember = DialogFragmentHelper.showCustomDialogFragment(R.string.cancel_administrator, msg, R.string.frg_text_cancel ,R.string.frg_text_ok, dialogOnclick);
        }
        this.mDialogFragmentCircleGroupSetMember.setMessage(msg);
        this.mDialogFragmentCircleGroupSetMember.show(getFragmentManager(), "mDialogFragmentCircleGroupSetMember");
    }

    /**
     * 删除聊天组成员对话框
     * @param displayName
     */
    private void showDialogFragmentCircleGroupDel(String displayName){
        String msg = getString(R.string.confirm_quit_group_member_del, displayName);
        if(ResourceHelper.isNull(this.mDialogFragmentCircleGroupDel)){
            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface(){
                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        CircleGroupMemberResultEntity member = mGroupMemberAdapter.getItem(curPosition);
                        requestCircleGroupRemoveMember(member);
                    }
                }
            };
            this.mDialogFragmentCircleGroupDel = DialogFragmentHelper.showCustomDialogFragment(R.string.remove_member, msg, R.string.frg_text_cancel ,R.string.frg_text_ok, dialogOnclick);
        }
        this.mDialogFragmentCircleGroupDel.setMessage(msg);
        this.mDialogFragmentCircleGroupDel.show(getFragmentManager() , "this.mDialogFragmentCircleGroupDel");
    }

    /**
     * 请求获取讨论组成员
     * @param groupId
     */
    private void requestCircleGroupMember(long groupId){
        if(ResourceHelper.isNotNull(this.mUrlEntity)){
            String url = ProjectHelper.fillRequestURL(this.mUrlEntity.getMessageGroupMemberSelect());
            String token = CurrentUser.getInstance().getToken();
            CircleGroupIdJsonEntity group = new CircleGroupIdJsonEntity();
            group.setGroupId(groupId);
            String json = JsonHelper.toJson(group, CircleGroupIdJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_MEMBER_LIST, url, token, json, this, groupId);
        }

    }

    /**
     * 请求修改讨论组名称
     */
    private void requestCircleGroupName(String groupName){
        if(ResourceHelper.isNotNull(this.mUrlEntity)){
            ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
            String url = ProjectHelper.fillRequestURL(this.mUrlEntity.getMessageGroupUpdate());
            String token = CurrentUser.getInstance().getToken();
            CircleGroupUpdateJsonEntity groupUpdateEntity = new CircleGroupUpdateJsonEntity();
            groupUpdateEntity.setGroupName(groupName);
            groupUpdateEntity.setGroupId(this.groupId);
            groupUpdateEntity.setSendPushMsg(true);
            String json = JsonHelper.toJson(groupUpdateEntity, CircleGroupUpdateJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_NAME_UPDATE, url, token, json, this);
        }
     }

    /**
     * 退出群聊天
     */
    private void requestCircleGroupQuit(){
        if(ResourceHelper.isNotNull(this.mUrlEntity)){
            ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
            String token = CurrentUser.getInstance().getToken();
            CircleGroupIdJsonEntity group = new CircleGroupIdJsonEntity();
            group.setGroupId(groupId);
            String json = JsonHelper.toJson(group, CircleGroupIdJsonEntity.class);
            String url = ProjectHelper.fillRequestURL(mUrlEntity.getMessageGroupQuit());
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_QUIT, url, token, json, this);
        }
    }

    /**
     * 解散群聊天
     */
    private void requestCircleGroupDissolution(){
        if(ResourceHelper.isNotNull(this.mUrlEntity)){
            ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
            String token = CurrentUser.getInstance().getToken();
            String url = ProjectHelper.fillRequestURL(mUrlEntity.getMessageGroupDel());
            CircleGroupIdJsonEntity group = new CircleGroupIdJsonEntity();
            group.setGroupId(groupId);
            String json = JsonHelper.toJson(group, CircleGroupIdJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_DELETE, url, token, json, this);
        }
    }

    /**
     * 请求移除成员
     */
    private void requestCircleGroupRemoveMember(CircleGroupMemberResultEntity member){
        if(ResourceHelper.isNotNull(member) && ResourceHelper.isNotNull(this.mUrlEntity)){
            ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
            CircleGroupMemberJsonEntity groupMember = new CircleGroupMemberJsonEntity();
            groupMember.setAccountId(member.getAccountId());
            groupMember.setCardId(member.getCardId());
            groupMember.setGroupId(this.groupId);
            groupMember.setMemberId(member.getMemberId());
            ArrayList<CircleGroupMemberJsonEntity> memberList = new ArrayList<CircleGroupMemberJsonEntity>();
            memberList.add(groupMember);
            CircleGroupMemberManageJsonEntity memberManage = new CircleGroupMemberManageJsonEntity();
            memberManage.setGroupId(this.groupId);
            memberManage.setMemberList(memberList);
            String json = JsonHelper.toJson(memberManage);
            String token = CurrentUser.getInstance().getToken();
            String requestUrl = ProjectHelper.fillRequestURL(this.mUrlEntity.getMessageGroupChatDelete());
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_MEMBER_DELETE, requestUrl, token, json, this);
        }
    }

    /**
     * 权限管理、移除
     * @param role
     * @param tag
     */
    private void requestUnitMemberManager(int role, CircleGroupMemberResultEntity member, int tag){
        if(ResourceHelper.isNotNull(member) && ResourceHelper.isNotNull(this.mUrlEntity)){
            ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
            String requestUrl = ProjectHelper.fillRequestURL(this.mUrlEntity.getEnterpriseMemberRoleSave());
            String mAccessToken = CurrentUser.getInstance().getToken();
            ArrayList<Long> mMemberIdList = new ArrayList<Long>();
            mMemberIdList.add(member.getMemberId());
            UnitMemberManageJsonEntity unitMemberManage = new UnitMemberManageJsonEntity();
            unitMemberManage.setEnterpriseId(this.groupId);
            unitMemberManage.setMemberIdList(mMemberIdList);
            unitMemberManage.setRole(role);
            String json = JsonHelper.toJson(unitMemberManage, UnitMemberManageJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(tag, requestUrl, mAccessToken, json, this);
        }
    }
}
