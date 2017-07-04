package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.ContactVCardDao;
import com.maya.android.vcard.entity.ContactGroupEntity;
import com.maya.android.vcard.ui.adapter.CardcaseGroupDragAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CardcaseGroupDragListView;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.NameLengthFilter;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 名片夹：分组管理
 */
public class CardcaseGroupManagerFragment extends BaseFragment {
    private static final int GROUP_NAME_LENGTH_MAX = 5;
    private CardcaseGroupDragListView mLsvGroupDrag;
    private CardcaseGroupDragAdapter mGroupDragAdapter;
    private ImageView mImvLock;
    /** 添加、编辑分组 **/
    private EditText mEdtAddGroup;
    /** 加密、解密标题 **/
    private TextView mTxvPwdTitle;
    /** 加密、解密输入框 **/
    private EditText mEdtPwd, mEdtPwdValid;
    private CustomDialogFragment mDialogFragmentAddGroup, mDialogFragmentDelGroup, mDialogFragmentDelContact, mDialogFragmentEncodePwd;
    /** 当前加密状态 默认未加密 **/
    private boolean isLocked = false;
    /** 当前选中的分组 **/
    private int curPosition;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imv_lock:
                    //加/解锁
                    showEncodePwdDialog();
                    break;
                case R.id.imv_act_top_right:
                    //添加分组
                    showAddGroupDialog(-1);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cardcase_group_manager, container, false);
        this.mImvLock = (ImageView) view.findViewById(R.id.imv_lock);
        this.mLsvGroupDrag = (CardcaseGroupDragListView) view.findViewById(R.id.lsv_group_drag);
        this.mImvLock.setOnClickListener(this.mOnClickListener);
        this.mLsvGroupDrag.setCardcaseGroupDragListListenerAndDragImvId(R.id.imv_drag, this.mCardcaseGroupDragListListener);
        this.mLsvGroupDrag.setOnItemLongClickListener(this.mOnItemLongClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.pop_group_management, false);
        super.mTitleAction.setActivityTopRightImv(R.mipmap.img_top_add, this.mOnClickListener);
        this.mGroupDragAdapter = new CardcaseGroupDragAdapter(getActivity());
        this.mLsvGroupDrag.setAdapter(this.mGroupDragAdapter);
        this.mGroupDragAdapter.setContactGroupItemOperateListener(this.mContactGroupItemOperateListener);
        this.mGroupDragAdapter.addItems(ContactVCardDao.getInstance().getGroupList());
        String mEncodePwd = PreferencesHelper.getInstance().getString(Constants.Preferences.KEY_CARDCASE_COLLECT_LOCKED_PWD, "");
        if(ResourceHelper.isEmpty(mEncodePwd)){
            setIsLocked(false);
            this.mImvLock.setImageResource(R.mipmap.img_no_lock);
        }else{
            setIsLocked(true);
            this.mImvLock.setImageResource(R.mipmap.img_lock);
        }
    }

    private void setIsLocked(boolean isLocked){
        this.isLocked = isLocked;
    }

    /**
     * 长按监听
     */
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(mGroupDragAdapter.getItem(position).isEnable()){
                showAddGroupDialog(position);
            }
            return true;
        }
    };

    /**
     * 拖拽监听
     */
    private CardcaseGroupDragListView.CardcaseGroupDragListListener mCardcaseGroupDragListListener = new CardcaseGroupDragListView.CardcaseGroupDragListListener() {
        @Override
        public void exchange(int mDragSrcPosition, int mDragPosition) {
            if(mDragSrcPosition < mGroupDragAdapter.getCount()){
                mGroupDragAdapter.exchange(mDragSrcPosition, mDragPosition);
            }
        }
    };

    /**
     * 适配器监听
     */
    private CardcaseGroupDragAdapter.ContactGroupItemOperateListener mContactGroupItemOperateListener = new CardcaseGroupDragAdapter.ContactGroupItemOperateListener() {
        @Override
        public void onDeleteClick(int position) {
            showDelGroupDialog(position);
        }
    };

    /**
     * 删除分组对话框
     */
    public void showDelGroupDialog(int position){
        this.curPosition = position;
        if(ResourceHelper.isNull(this.mDialogFragmentDelGroup)){
            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        showDelContact();
                    }
                }
            };
            this.mDialogFragmentDelGroup = DialogFragmentHelper.showCustomDialogFragment(R.string.del_group ,R.string.confirm_delete_group,R.string.frg_text_cancel,R.string.frg_text_ok,mOnClick);
        }
        this.mDialogFragmentDelGroup.show(getFragmentManager(), "mDialogFragmentDelGroup");
    }

    /**
     * 删除分组下联系人确认对话框
     */
    public void showDelContact(){
        if(ResourceHelper.isNull(this.mDialogFragmentDelContact)){
            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    boolean isDelGroup;
                    ContactGroupEntity mSelectGroup = mGroupDragAdapter.getItem(curPosition);
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        isDelGroup = ContactVCardDao.getInstance().deleteGroup(mSelectGroup, true);
                    }else{
                        isDelGroup = ContactVCardDao.getInstance().deleteGroup(mSelectGroup, false);
                    }

                    if(isDelGroup){
                        mGroupDragAdapter.removeItem(mSelectGroup);
                        //TODO 删除通讯录的分组
//                        ContactBookDao.getInstance().deleteGroup(mSelectGroup.getName(), false);
                    }
                }
            };
            this.mDialogFragmentDelContact = DialogFragmentHelper.showCustomDialogFragment(R.string.del_group ,R.string.confirm_delete_group_contacts,R.string.frg_text_cancel,R.string.frg_text_ok,mOnClick);
        }
        this.mDialogFragmentDelContact.show(getFragmentManager(), "mDialogFragmentDelContact");
    }

    /**
     * 添加分组/修改分组名称对话框
     */
    private void showAddGroupDialog(int position){
        this.curPosition = position;
        if(ResourceHelper.isNull(this.mDialogFragmentAddGroup)){
            View mView = LayoutInflater.from(getActivity()).inflate(R.layout.item_add_group_edt, null);
            this.mEdtAddGroup = (EditText) mView.findViewById(R.id.edt_add_group);
            InputFilter[] filters = {new NameLengthFilter(GROUP_NAME_LENGTH_MAX, GROUP_NAME_LENGTH_MAX *2)};  //设置编辑框字符长度
            mEdtAddGroup.setFilters(filters);
            int margin = ResourceHelper.getDp2PxFromResouce( R.dimen.dimen_15dp);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mEdtAddGroup.getLayoutParams();
            params.width = DialogFragmentHelper.getCustomDialogFragmentWidth() - (margin * 2);
            mEdtAddGroup.setLayoutParams(params);
            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        String newGroupName = mEdtAddGroup.getText().toString();
                        if(ResourceHelper.isNotEmpty(newGroupName)){
                            if(curPosition < 0){
                                mGroupDragAdapter.addItem(newGroupName);
                            } else {
                                String groupName = mGroupDragAdapter.getItem(curPosition).getName();
                                if(!newGroupName.equals(groupName)){
                                    mGroupDragAdapter.updateItem(curPosition, newGroupName);
                                }
                            }
                        }else{
                            ActivityHelper.showToast(R.string.enter_groups_name_please);
                        }
                    }
                }
            };
            this.mDialogFragmentAddGroup = DialogFragmentHelper.showCustomDialogFragment(R.string.add_group, mView, R.string.frg_text_cancel, R.string.frg_text_ok, mOnClick);
        }
        setRefreshView();
        if(this.curPosition < 0){
            this.mDialogFragmentAddGroup.setTitle(R.string.add_group);
        }else{
            this.mDialogFragmentAddGroup.setTitle(R.string.edit_group);
        }
        this.mDialogFragmentAddGroup.show(getFragmentManager(), "mDialogFragmentAddGroup");
    }

    /**
     * 刷新UI控件
     */
    private void setRefreshView(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                if(curPosition < 0){
                    bundle.putString("groupName", "");
                }else{
                    String groupName = mGroupDragAdapter.getItem(curPosition).getName();
                    bundle.putString("groupName", groupName);
                }
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String groupName = msg.getData().getString("groupName", "");
            mEdtAddGroup.setText(groupName);
            mEdtAddGroup.setSelection(groupName.length());

        }
    };

    /**
     * 加密/解密对话框
     */
    private void showEncodePwdDialog(){
        if(ResourceHelper.isNull(this.mDialogFragmentEncodePwd)){
            View mView = LayoutInflater.from(getActivity()).inflate(R.layout.item_groups_encode, null);
            this.mTxvPwdTitle = (TextView) mView.findViewById(R.id.txv_groups_encode);
            this.mEdtPwd = (EditText) mView.findViewById(R.id.edt_groups_encode_pwd);
            this.mEdtPwdValid = (EditText)mView.findViewById(R.id.edt_groups_encode_pwd_valid);
            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    String pwd = mEdtPwd.getText().toString();
                    if (CustomDialogFragment.BUTTON_POSITIVE == which) {
                        if(ResourceHelper.isNotEmpty(pwd)){
                            if(isLocked){
                                String mEncodePwd = PreferencesHelper.getInstance().getString(Constants.Preferences.KEY_CARDCASE_COLLECT_LOCKED_PWD, "");
                                if(!pwd.equals(mEncodePwd)){
                                    ActivityHelper.showToast(R.string.locked_pwd_wrong);
                                    return;
                                }
                                PreferencesHelper.getInstance().putString(Constants.Preferences.KEY_CARDCASE_COLLECT_LOCKED_PWD, "");
                                mImvLock.setImageResource(R.mipmap.img_no_lock);
                                ActivityHelper.showToast(R.string.decode_success);
                            }else{
                                String pwdValid = mEdtPwdValid.getText().toString();
                                if(!pwd.equals(pwdValid)){
                                    ActivityHelper.showToast(R.string.valid_pwd_different);
                                    return;
                                }
                                PreferencesHelper.getInstance().putString(Constants.Preferences.KEY_CARDCASE_COLLECT_LOCKED_PWD, pwd);
                                ActivityHelper.showToast(R.string.locked_success);
                                mImvLock.setImageResource(R.mipmap.img_lock);
                                mEdtPwdValid.setText("");
                            }
                            mEdtPwd.setText("");
                            isLocked = !isLocked;
                        }
                    }
                    mDialogFragmentEncodePwd.getDialog().dismiss();
                }
            };
            this.mDialogFragmentEncodePwd = DialogFragmentHelper.showCustomDialogFragment(R.string.add_group, mView, R.string.frg_text_cancel, R.string.frg_text_ok, mOnClick);
            this.mDialogFragmentEncodePwd.setManualDismiss(true);
        }
        if(this.isLocked){
            this.mTxvPwdTitle.setText(R.string.folder_had_pwd);
            this.mDialogFragmentEncodePwd.setTitle(R.string.decrypt);
            this.mEdtPwdValid.setVisibility(View.GONE);
        }else{
            this.mTxvPwdTitle.setText(R.string.set_password);
            this.mDialogFragmentEncodePwd.setTitle(R.string.encryption);
            this.mEdtPwdValid.setVisibility(View.VISIBLE);
        }
        this.mDialogFragmentEncodePwd.show(getFragmentManager(), "this.mDialogFragmentEncodePwd");
    }


}
