package com.maya.android.vcard.ui.frg;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.CircleDao;
import com.maya.android.vcard.dao.ContactVCardDao;
import com.maya.android.vcard.dao.UnitDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.CardcaseVCardEntity;
import com.maya.android.vcard.entity.ContactEntity;
import com.maya.android.vcard.entity.ContactGroupEntity;
import com.maya.android.vcard.entity.ContactListItemEntity;
import com.maya.android.vcard.entity.CustomLsvIconEntity;
import com.maya.android.vcard.entity.EmailSendEntity;
import com.maya.android.vcard.entity.json.CircleGroupJsonEntity;
import com.maya.android.vcard.entity.json.CircleGroupMemberJsonEntity;
import com.maya.android.vcard.entity.json.ContactDeleteJsonEntity;
import com.maya.android.vcard.entity.result.ContactsListResultEntity;
import com.maya.android.vcard.entity.result.CreateCircleGroupResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UnitListResultEntity;
import com.maya.android.vcard.entity.result.UnitResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.act.CardcaseDetailVCardActivity;
import com.maya.android.vcard.ui.act.CardcaseMainActivity;
import com.maya.android.vcard.ui.act.MessageMainActivity;
import com.maya.android.vcard.ui.act.MultiMainActivity;
import com.maya.android.vcard.ui.act.UnitMainActivity;
import com.maya.android.vcard.ui.adapter.CardcaseGroupsAdapter;
import com.maya.android.vcard.ui.adapter.CardcaseVCardAdapter;
import com.maya.android.vcard.ui.adapter.CustomGroupLsvAdapter;
import com.maya.android.vcard.ui.adapter.CustomLsvAdapter;
import com.maya.android.vcard.ui.adapter.CustomLsvIconAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.impl.CardcaseBatchOprerationImpl;
import com.maya.android.vcard.ui.impl.TwoFragmentParameterImpl;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.NameLengthFilter;
import com.maya.android.vcard.ui.widget.SideBar;
import com.maya.android.vcard.util.ConverChineseCharToEnHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.util.SideBarCharHintHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 名片夹主页（放后面做）
 */
public class CardcaseMainFragment extends BaseFragment implements CardcaseBatchOprerationImpl {
    private static final String TAG = CardcaseMainFragment.class.getSimpleName();
    private static final int GROUP_NAME_LENGTH_MAX = 5;
    /** 弹窗 **/
    private CustomDialogFragment mDialogFragmentItemClick, mDialogFragmentBusiness, mDialogFragmentCallPhone,mDialogFragmentNetwork,
            mDialogFragmentAddGroupDialog, mDialogFragmentMoveGroup, mDialogFragmentDeleteContact;
    /** 拨号适配器 **/
    private CustomLsvAdapter mLsvCallAdapter;
    /** 移动至分组适配器 **/
    private CustomGroupLsvAdapter mMoveGroupAdapter;
    private LinearLayout mLilLeft;
    private ListView lsvVCard;
    /**当前选中的名片**/
    private ContactListItemEntity curVcard;
    /** 当前选中的item位置 **/
    private int curPosition;
    /** 是否重新加载联系人 **/
    private boolean isReloadContacts;
    /** 是批量操作 **/
    private boolean isBatchOpreratrion = true;
    private TextView mTxvGroupAll, mTxvBusiness, mTxvLsvEmpty;
    private EditText mEdtSearch;
    private SideBar mSibSelect;
    /** SideBar 字母提示帮助类 **/
    private SideBarCharHintHelper charHintHelper;
    /**左边选项适配器**/
    private CardcaseGroupsAdapter mAdapterGroups;
    /**名片夹列表适配器**/
    private CardcaseVCardAdapter mAdapterVCard;
    private boolean isListSroll = false;
    private boolean isOpenGroups;
    private int mCurrentGroupId = 100;
    private URLResultEntity mUrlEntity = CurrentUser.getInstance().getURLEntity();
    /** 分组列表 **/
    private ArrayList<ContactGroupEntity> mGroupList;
    /** 单位列表 **/
    private ArrayList<UnitResultEntity> mUnitList;
    protected TwoFragmentParameterImpl towFragmentParameterListener;
    /** 父Activity **/
    protected MultiMainActivity multiMainActivity;
    /** 记录所有被选中的项 **/
    private ArrayList<ContactListItemEntity> contacts;
    /** 弹窗适配器 **/
    private CustomLsvAdapter mLsvAdapter;
    private VCardTask mVCardTask;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MultiMainActivity.WHAT_ACTIVITY_TOP_LEFT_VIEW_CLICK:
                    showOrCloseGroups();
                    break;
                case MultiMainActivity.WHAT_ACTIVITY_TOP_RIGHT_VIEW_CLICK:
                    break;
            }
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txv_cardcase_group_all:
                    //全部
                    setGrounpAllSelected(true);
                    break;
                case R.id.txv_act_cardcase_bus:
                    //行业类别
                    showDialogFragmentBusiness();
                    break;
            }
        }
    };

    @Override
    protected void onResponseSuccess(int tag, String msgInfo) {
        super.onResponseSuccess(tag, msgInfo);
        //请求成功的回调
        switch (tag){
            case Constants.CommandRequestTag.CMD_REQUEST_MY_CONTACT_DELETE:
                //删除联系人
                break;
        }
    }

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, final Object... objects) {
        //请求回调
        if(!super.onCommandCallback2(tag, commandResult, objects)) {
            switch (tag) {
                case Constants.CommandRequestTag.CMD_CONTACT_LIST:
                    ContactsListResultEntity resultEntity = JsonHelper.fromJson(commandResult, ContactsListResultEntity.class);
                    if (Helper.isNotNull(resultEntity)) {
                        ArrayList<ContactEntity> contactList = resultEntity.getContactEntityList();
                        if (Helper.isNotNull(contactList)) {
                            LogHelper.d(TAG, "ContactList.size():" + contactList.size());
                            // 数据库进行操作
                            // 空指针，得解决（造成原因： 用户经常却换账号登录）
                            ContactVCardDao.getInstance().add(contactList);
                            //删除 不存在的联系人
                            ContactVCardDao.getInstance().deleteContactFromService(contactList);
                            // 反馈到页面
                            if (this.mCurrentGroupId == ContactGroupEntity.GROUP_ALL_ID) {
//                                fillVCardData(ContactGroupEntity.GROUP_ALL_ID);
                                this.mAdapterVCard.addVCardItems(ContactVCardDao.getInstance().getList(ContactGroupEntity.GROUP_ALL_ID));
//                                this.mAdapterVCard.addVCardItems(contactList);
                            }
                        }
                    }
                    break;
                case Constants.CommandRequestTag.CMD_UNIT_LIST:
                    //单位信息
                    UnitListResultEntity result = JsonHelper.fromJson(commandResult, UnitListResultEntity.class);
                    if (Helper.isNotNull(result)) {
                        ArrayList<UnitResultEntity> unitList = result.getUnitList();
                        if (Helper.isNotNull(unitList) && unitList.size() > 0) {
                            this.mUnitList = unitList;
                            UnitDao unitDao = UnitDao.getInstance();
                            unitDao.deleteAll();
                            unitDao.add(unitList);
                        }
                    }
                    break;
                case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_CREATE:
                    //创建聊天组
                    ActivityHelper.closeProgressDialog();
                    CreateCircleGroupResultEntity createCircleGroup = JsonHelper.fromJson(commandResult, CreateCircleGroupResultEntity.class);
                    if(ResourceHelper.isNotNull(createCircleGroup)){
                        long groupId = createCircleGroup.getMessageGroupId();
                        CircleGroupJsonEntity groupJsonEntity = (CircleGroupJsonEntity) objects[0];
                        CircleDao.getInstance().add(groupId, groupJsonEntity);
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, MessageConversationFragment.class.getName());
                        bundle.putLong(Constants.IntentSet.INTENT_KEY_MESSAGE_TAG_ID, groupId);
                        intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                        mActivitySwitchTo.switchTo(MessageMainActivity.class, intent);

                    }
                    break;
            }
        }
        return true;
    }

     @Override
    protected void onResponseFail(int tag, int responseCode, String msgInfo) {
        super.onResponseFail(tag, responseCode, msgInfo);
        //TODO 请求失败的回调
        switch (tag){
            case Constants.CommandRequestTag.CMD_REQUEST_MY_CONTACT_DELETE:
                //删除联系人
                break;
            case Constants.CommandRequestTag.CMD_CONTACT_LIST:
                //获取联系人
                break;
            case Constants.CommandRequestTag.CMD_UNIT_LIST:
                //获取单位信息
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_CREATE:
                //创建群组聊天
                ActivityHelper.closeProgressDialog();
                break;
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            multiMainActivity = (MultiMainActivity) activity ;
            multiMainActivity.setCardcaseHandler(this.mHandler);
            multiMainActivity.setCardcaseBatchOprerationListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MultiMainActivity");
        }

        try {
            towFragmentParameterListener = (TwoFragmentParameterImpl) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TwoFragmentParameterImpl");
        }
    }

    @Override
    public void userMethodType(Bundle bundle) {
        if(ResourceHelper.isNotNull(bundle)){
            int methodType = bundle.getInt(CardcaseBatchOperationFragment.KEY_BUTTON_CODE, 0);
            if(ResourceHelper.isNull(mAdapterVCard)){
                return;
            }
            switch (methodType){
                case CardcaseBatchOperationFragment.REMOVE_GROUP:
                    //移除分组
                    showDialogFragmentMoveGroup(mAdapterVCard.getSelecedeALl(), true);
                     break;
                case CardcaseBatchOperationFragment.SEND_MESS:
                    //发送消息
                    ArrayList<ContactListItemEntity> contacts = mAdapterVCard.getSelecedeALl();
                    if(ResourceHelper.isNull(contacts)){
                        return;
                    }
                    int size = contacts.size();
                    if(!NetworkHelper.isNetworkAvailable(getActivity())){
                        if(size == 1){
                            curVcard = contacts.get(0);
                            if(ResourceHelper.isNotNull(curVcard)){
                                showDialogFragmentNetwork();
                            }
                        }else{
                            ActivityHelper.showToast(R.string.no_network);
                            return;
                        }
                    }else{
                        requestCreateCircleGroup(contacts);
                    }
                    break;
                case CardcaseBatchOperationFragment.DELETE_CARD:
                    //删除名片
                    showDialogFragmentDeleteContact(mAdapterVCard.getSelecedeALl(), true);
                    break;
                case CardcaseBatchOperationFragment.SELECT_ALL:
                    //选择所有
                    mAdapterVCard.setSelectedAll();
                    break;
                case CardcaseBatchOperationFragment.CANCEL_ALL:
                    //全部取消
                    mAdapterVCard.setCancelSelectAll();
                    break;
            }
        }
    }

    @Override
    public void showSelect() {
        if(ResourceHelper.isNotNull(mAdapterVCard)){
            mAdapterVCard.setHideSelect(false);
        }
    }

    @Override
    public void hideSelect() {
        if(ResourceHelper.isNotNull(mAdapterVCard)){
            mAdapterVCard.setHideSelect(true);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardcase_main, container, false);
        this.mLilLeft = findView(view, R.id.lil_cardcase_left);
        this.mTxvBusiness = (TextView) view.findViewById(R.id.txv_act_cardcase_bus);
        this.mTxvBusiness.setOnClickListener(this.mOnClickListener);
        this.mEdtSearch = (EditText) view.findViewById(R.id.edt_act_cardcase_search);
        this.mEdtSearch.setOnKeyListener(this.mOnKeyListener);
        this.mEdtSearch.addTextChangedListener(this.mTextWatcher);
        this.mSibSelect = (SideBar) view.findViewById(R.id.cust_act_cardcase_side_bar);
        this.mSibSelect.setOnTouchingLetterChangedListener(this.mOnTouchingLetterChangedListener);
        ListView lsvGroups = findView(view, R.id.lsv_cardcase_groups);
        this.mAdapterGroups = new CardcaseGroupsAdapter(getActivity());
        lsvGroups.setAdapter(this.mAdapterGroups);
        lsvGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == mAdapterGroups.getCount() - 1) {
                    // 弹窗具体功能
                    showAddGroupDialog();
                } else {
                    mAdapterGroups.setSelectedPosition(position);
//					mTxvBus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_arrow_g_s_down, 0);
//                    mTxvBus.setText(R.string.common_business_class);
                    fillVCardData(mAdapterGroups.getAllItems().get(position).getId());
//                    mEdtSearch.setText("");
                    //设置全部为透明
                    setGrounpAllSelected(false);
                }
            }
        });
        this.mTxvGroupAll = findView(view, R.id.txv_cardcase_group_all);
        this.mTxvGroupAll.setOnClickListener(this.mOnClickListener);
        this.lsvVCard = findView(view, R.id.lsv_cardcase_vcard);
        this.lsvVCard.setDivider(null);
        this.lsvVCard.setDividerHeight(0);
        this.lsvVCard.setOnScrollListener(this.mOnScrollListener);
        //单击事件
        this.lsvVCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CardcaseVCardEntity Item = mAdapterVCard.getItem(position);
                Intent intent = new Intent();
                if (Item.getSendType() == CardcaseVCardAdapter.VIEW_TYPE_UNIT) {
                    UnitResultEntity curUnit = Item.getUnitDetailEntity();
                    Bundle bundle = new Bundle();
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UnitDetailFragment.class.getName());
                    bundle.putLong(UnitDetailFragment.KEY_UNIT_ID, curUnit.getEnterpriseId());
                    intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                    mActivitySwitchTo.switchTo(UnitMainActivity.class, intent);
                } else {
                    intent.putExtra(CardcaseDetailVCardActivity.GROUP_ID, mCurrentGroupId);
                    if(mAdapterVCard.getIsShowUnit()){
                        intent.putExtra(CardcaseDetailVCardActivity.GROUP_POSITON, position - mAdapterVCard.getUnitCount());
                        LogHelper.d(TAG, "发送:groupId:" + mCurrentGroupId + " position:" + (position - mAdapterVCard.getUnitCount()));
                    }else{
                        intent.putExtra(CardcaseDetailVCardActivity.GROUP_POSITON, position);
                        LogHelper.d(TAG, "发送:groupId:" + mCurrentGroupId + " position:" + (position));
                    }
                    mActivitySwitchTo.switchTo(CardcaseDetailVCardActivity.class, intent);
                }

            }
        });
        //长按事件
        this.lsvVCard.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CardcaseVCardEntity Item = mAdapterVCard.getItem(position);
                if (Item.getSendType() != CardcaseVCardAdapter.VIEW_TYPE_UNIT) {
                    ContactListItemEntity card = Item.getVcardEntity();

                    if(ResourceHelper.isNotNull(card) && ResourceHelper.isNotEmpty(card.getVcardNo())){
                        showDialogFragmentItemClick(position, false);
                    }else{
                        showDialogFragmentItemClick(position, true);
                    }

                    return true;
                }
                return false;
            }
        });
        if(Helper.isNull(this.mAdapterVCard)) {
            this.mAdapterVCard = new CardcaseVCardAdapter(getActivity());
        }
        this.lsvVCard.setAdapter(this.mAdapterVCard);
        this.mAdapterVCard.setCardcaseVCardListener(this.mCardcaseVCardListener);
        this.mTxvLsvEmpty = findView(view, R.id.txv_cardcase_vcard_empty);
        this.lsvVCard.setEmptyView(this.mTxvLsvEmpty);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(this.isOpenGroups){
            this.mLilLeft.setVisibility(View.VISIBLE);
        }
        if(Helper.isNull(this.mGroupList)) {
            this.mGroupList = ContactVCardDao.getInstance().getGroupListForShow(true);
        }
        if(Helper.isNull(this.mUnitList)){
            this.mUnitList = UnitDao.getInstance().getAllUnit();
        }
        if(this.mCurrentGroupId == 100) {
//            this.fillVCardData(ContactGroupEntity.GROUP_ALL_ID);
            mVCardTask = new VCardTask();
            mVCardTask.execute(ContactGroupEntity.GROUP_ALL_ID);
        }
        this.mAdapterGroups.addItems(mGroupList);
        this.requestAllVCard();
        this.requestAllUnit();
        this.mTxvLsvEmpty.setText(R.string.common_loading_data);
        this.charHintHelper = SideBarCharHintHelper.getIntance(getActivity(), this.mSibSelect);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(ResourceHelper.isNotNull(this.charHintHelper)){
            this.charHintHelper.clearCharHint();
        }
        if(ResourceHelper.isNotNull(this.mVCardTask) && this.mVCardTask.getStatus() == AsyncTask.Status.RUNNING){
            this.mVCardTask.cancel(true);
        }
    }



    /**
     * item选中事件监听
     */
    private CardcaseVCardAdapter.CardcaseVCardListener mCardcaseVCardListener = new CardcaseVCardAdapter.CardcaseVCardListener() {

        @Override
        public void selectNum(int num, boolean isCanSendMess) {
            Bundle bundle = new Bundle();
            if(num > 0){
                if(isCanSendMess){
                    bundle.putInt(CardcaseBatchOperationFragment.KEY_BUTTON_CODE,  CardcaseBatchOperationFragment.COUNT_AND_IS_CAN_SEND_MESS);
                    //TODO 此处可做本地名片提示信息
                }else{
                    bundle.putInt(CardcaseBatchOperationFragment.KEY_BUTTON_CODE,  CardcaseBatchOperationFragment.COUNT_AND_IS_NOT_SEND_MESS);
                }
            }else{
                bundle.putInt(CardcaseBatchOperationFragment.KEY_BUTTON_CODE,  CardcaseBatchOperationFragment.COUNT_IS_NOT);
            }
            if(ResourceHelper.isNotNull(towFragmentParameterListener)){
                towFragmentParameterListener.twoFragmentTransfer(CardcaseBatchOperationFragment.class.getName(), bundle);
            }

        }
    };

    /**
     * 键盘输入监听
     */
    private View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                EditText et = (EditText) v;
                if(et.getText().toString().length() > 0){
                    mAdapterVCard.getFilter().filter(et.getText().toString());
                }
            }
            return false;
        }
    };

    /**
     * 输入框输入监听
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(count < 1){
                return;
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mAdapterVCard.getFilter().filter(s.toString());
        }
    };

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
                int curPosition = firstVisibleItem + (visibleItemCount >> 1);
                ContactListItemEntity item = mAdapterVCard.getItem(curPosition).getVcardEntity();
                if(Helper.isNotNull(item)){
                    String curAlpha = ConverChineseCharToEnHelper.getFirstLetter(item.getDisplayName()).toUpperCase();
                    if(!charHintHelper.getCharDelay()){
                        charHintHelper.showCharHint(curAlpha);
                    }
                }
            }
        }
    };

    /**
     * 右侧字母选择事件监听
     */
    private SideBar.OnTouchingLetterChangedListener mOnTouchingLetterChangedListener = new SideBar.OnTouchingLetterChangedListener() {

        @Override
        public void onTouchingLetterChanged(String s) {
            if(!charHintHelper.getCharDelay()){
                mSibSelect.setBackgroundResource(R.drawable.bg_sidebar);
                lsvVCard.setSelection(mAdapterVCard.getPosition(s));
                charHintHelper.showCharHint(s);
            }
        }
    };

    private void showOrCloseGroups(){
        if(this.isOpenGroups){
            this.mLilLeft.setVisibility(View.GONE);
        }else{
            this.mLilLeft.setVisibility(View.VISIBLE);
        }
        this.mAdapterVCard.setLeftHide(this.isOpenGroups);
        this.isOpenGroups = !this.isOpenGroups;
    }

    /**
     * 设置全部是否选中
     * @param selected
     */
     private void setGrounpAllSelected(boolean selected){
        if(selected){
            this.mAdapterGroups.setSelectedPosition(-1);
            this.fillVCardData(ContactGroupEntity.GROUP_ALL_ID);
            this.mTxvGroupAll.setBackgroundResource(R.drawable.bg_cardcase_group_select);
            this.mTxvGroupAll.setTextColor(getResources().getColor(R.color.color_ffffff));
        }else{
            this.mTxvGroupAll.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
            this.mTxvGroupAll.setTextColor(getResources().getColor(R.color.color_35466c));
        }
    }

    private void fillVCardData(int groupId){
        if(this.mCurrentGroupId != groupId) {
            this.mCurrentGroupId = groupId;
            ArrayList<ContactListItemEntity> result = ContactVCardDao.getInstance().getList(groupId);
            LogHelper.d(TAG, "VCard Size()=" + result.size());
            if(this.mCurrentGroupId == ContactGroupEntity.GROUP_ALL_ID){
                this.mTxvGroupAll.setText(this.getString(R.string.all) +"\n(" + result.size() + ")");
                //添加单位
                this.mAdapterVCard.setShowUnit(true);
                this.mAdapterVCard.addUnitItems(this.mUnitList);
            }else{
                this.mAdapterVCard.setShowUnit(false);
            }
            this.mAdapterVCard.addVCardItems(result);
        }
        this.mTxvLsvEmpty.setText(R.string.no_contact);
    }


    /**
     * 异步加载本地名片数据
     */
    class VCardTask extends AsyncTask<Integer, Void , ArrayList<ContactListItemEntity>>{


        @Override
        protected ArrayList<ContactListItemEntity> doInBackground(Integer... params) {
            if(isCancelled()){
                return null;
            }
            mCurrentGroupId = params[0];
            return ContactVCardDao.getInstance().getList(mCurrentGroupId);
        }

        @Override
        protected void onPostExecute(ArrayList<ContactListItemEntity> contactListItemEntities) {
            super.onPostExecute(contactListItemEntities);
            if(mCurrentGroupId == ContactGroupEntity.GROUP_ALL_ID){
                if(ResourceHelper.isNotNull(contactListItemEntities)){
                    mTxvGroupAll.setText(getString(R.string.all) +"\n(" + contactListItemEntities.size() + ")");
                }
                 //添加单位
                mAdapterVCard.setShowUnit(true);
                mAdapterVCard.addUnitItems(mUnitList);
            }else{
                mAdapterVCard.setShowUnit(false);
            }
            mAdapterVCard.addVCardItems(contactListItemEntities);
            mTxvLsvEmpty.setText(R.string.no_contact);
        }
    }

    /**
     * 请求所有联系人
     */
    private void requestAllVCard(){
        if(Helper.isNotNull(this.mUrlEntity)) {
            String url = ProjectHelper.fillRequestURL(this.mUrlEntity.getMyContact());
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_CONTACT_LIST, url, new JSONObject(), this);
        }
    }

    /**
     * 请求所有我的单位
     */
    private void requestAllUnit(){
        if(Helper.isNotNull(this.mUrlEntity)){
            String url = ProjectHelper.fillRequestURL(this.mUrlEntity.getEnterpriseList());
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_UNIT_LIST, url, new JSONObject(), this);
        }
    }

    /**
     * 名片夹列表Item点对话框
     * @param position
     */
    private void showDialogFragmentItemClick(int position, boolean isNotEnable){
        this.curPosition = position;
        this.curVcard = this.mAdapterVCard.getItem(position).getVcardEntity();
        if(ResourceHelper.isNull(this.mDialogFragmentItemClick)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            this.mLsvAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mLsvAdapter);
            this.mLsvAdapter.setShowRadio(false);
            this.mLsvAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.cardcase_contact_info));
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    switch (position) {
                        case 0:
                            // 查看详情
                            intent.putExtra(CardcaseDetailVCardActivity.GROUP_ID, mCurrentGroupId);
                            if (mAdapterVCard.getIsShowUnit()) {
                                intent.putExtra(CardcaseDetailVCardActivity.GROUP_POSITON, curPosition - mAdapterVCard.getUnitCount());
                                LogHelper.d(TAG, "发送:groupId:" + mCurrentGroupId + " position:" + (curPosition - mAdapterVCard.getUnitCount()));
                            } else {
                                intent.putExtra(CardcaseDetailVCardActivity.GROUP_POSITON, curPosition);
                                LogHelper.d(TAG, "发送:groupId:" + mCurrentGroupId + " position:" + curPosition);
                            }
                            mActivitySwitchTo.switchTo(CardcaseDetailVCardActivity.class, intent);
                            break;
                        case 1:
                            // 呼叫
                            ArrayList<String> phoneList = ContactVCardDao.getInstance().getPhoneList(curVcard.getId());
                            if (Helper.isNotNull(phoneList)) {
                                int size = phoneList.size();
                                if (size == 1) {
                                    ResourceHelper.callPhone(getActivity(), phoneList.get(0));
                                } else if (size > 1) {
                                    showDialogFragmentCallPhone(phoneList, curVcard.getDisplayName());
                                }
                            }
                            break;
                        case 2:
                            // 发送消息
                            if (!NetworkHelper.isNetworkAvailable(getActivity())) {
                                showDialogFragmentNetwork();
                            }else{
                                intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, MessageConversationFragment.class.getName());
                                bundle.putLong(Constants.IntentSet.INTENT_KEY_ACCOUNT_ID, curVcard.getAccountId());
                                intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                                mActivitySwitchTo.switchTo(MessageMainActivity.class, intent);
                            }
                            break;
                        case 3:
                            // 发送邮件
                            ContactEntity curContact = ContactVCardDao.getInstance().getEntity(curVcard.getId());
                            EmailSendEntity emailEntity = new EmailSendEntity(curContact.getEmail().split(","));
                            ResourceHelper.sendEmail(emailEntity, getActivity());
                            break;
                        case 4:
                            // 转发名片
                            intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, CardcaseChooseFragment.class.getName());
                            bundle.putLong(Constants.IntentSet.INTENT_KEY_VCARD_ID, curVcard.getCardId());
                            bundle.putLong(Constants.IntentSet.INTENT_KEY_ACCOUNT_ID, curVcard.getAccountId());
                            bundle.putInt(CardcaseChooseFragment.KEY_CARDCASE_CHOOSE, CardcaseChooseFragment.CARDCASE_CHOOSE_SELECT_VCARD_FORWARD);
                            intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                            mActivitySwitchTo.switchTo(CardcaseMainActivity.class, intent);
                            break;
                        case 5:
                            // 移动至分组
                            ArrayList<ContactListItemEntity> selectContactList = new ArrayList<ContactListItemEntity>();
                            selectContactList.add(curVcard);
                            showDialogFragmentMoveGroup(selectContactList, false);
                            break;
                        case 6:
                            //加入黑名单
                            ContactVCardDao.getInstance().joinBlacklist(curVcard.getId() + "");
                            refreshData(true);
                            break;
                        case 7:
                            //删除名片
                            ArrayList<ContactListItemEntity> items = new ArrayList<ContactListItemEntity>();
                            items.add(curVcard);
                            showDialogFragmentDeleteContact(items, false);
                            break;
                    }
                    mDialogFragmentItemClick.getDialog().dismiss();
                }
            });
            this.mDialogFragmentItemClick = DialogFragmentHelper.showCustomDialogFragment(this.curVcard.getDisplayName(), mLsvItem);
        }
        ArrayList<Integer> unEnablePositionList = new ArrayList<Integer>();
        if(isNotEnable){
            unEnablePositionList.add(1);
            unEnablePositionList.add(2);
            unEnablePositionList.add(3);
            this.mLsvAdapter.setUnEnableItems(unEnablePositionList);
        }else{
            this.mLsvAdapter.setUnEnableItems(unEnablePositionList);
        }
        this.mDialogFragmentItemClick.setTitle(this.curVcard.getDisplayName());
        this.mDialogFragmentItemClick.show(getFragmentManager(), "mDialogFragmentItemClick");
    }

    /**
     * 刷新数据 (是否重新加载联系人)
     * @param isReload
     */
    public void refreshData(boolean isReload){
        this.isReloadContacts = isReload;
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                mGroupList = ContactVCardDao.getInstance().getGroupListForShow(true);
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                mAdapterGroups.addItems(mGroupList);
                if(isReloadContacts){
                    fillVCardData(mCurrentGroupId);
                }
            }

        }.execute();
    }

    /**
     * 请求创建聊天组
     * @param contactList
     */
    private void requestCreateCircleGroup(ArrayList<ContactListItemEntity> contactList){
        if(ResourceHelper.isNotNull(contactList) && ResourceHelper.isNotNull(this.mUrlEntity)){
            ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
            ArrayList<CircleGroupMemberJsonEntity> memberList = new ArrayList<CircleGroupMemberJsonEntity>();
            CircleGroupMemberJsonEntity member = new CircleGroupMemberJsonEntity();
            for (int i = 0, size = contactList.size(); i < size; i++) {
                ContactListItemEntity contact = contactList.get(i);
                if(ResourceHelper.isNotNull(contact)){
                    try {
                        member = (CircleGroupMemberJsonEntity) member.clone();
                        member.setCardId(contact.getCardId());
                        member.setAccountId(contact.getAccountId());
                        memberList.add(member);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
            long myCardId = 0;
            CardEntity curCard = CurrentUser.getInstance().getCurrentVCardEntity();
            if(ResourceHelper.isNotNull(curCard)){
                myCardId = curCard.getId();
            }
            // 添加自己到成员列表
            if(myCardId < 1){
                myCardId = 0;
            }
            UserInfoResultEntity userInfo =  CurrentUser.getInstance().getUserInfoEntity();
            long myAccountId = 0;
            if(Helper.isNotNull(userInfo)){
                myAccountId = userInfo.getId();
                memberList.add(0, new CircleGroupMemberJsonEntity(myCardId, myAccountId));
            }
            try {
                member = (CircleGroupMemberJsonEntity) member.clone();
                member.setCardId(myCardId);
                member.setAccountId(myAccountId);
                memberList.add(0, member);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            CircleGroupJsonEntity groupEntity = new CircleGroupJsonEntity(myCardId, memberList);
            String mAccessToken = CurrentUser.getInstance().getToken();
            String msgUrl = ProjectHelper.fillRequestURL(this.mUrlEntity.getMessageGroupChatCreate());
            String toJson = JsonHelper.toJson(groupEntity);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_CREATE, msgUrl, mAccessToken, toJson, this, groupEntity);
        }
    }

    /**
     * 请求删除联系人
     * @param selectContactList
     */
    private void requestDeleteMyContact(ArrayList<ContactListItemEntity> selectContactList){
        if(ResourceHelper.isNotNull(selectContactList) && ResourceHelper.isNotNull(this.mUrlEntity)){
            String mAccessToken = CurrentUser.getInstance().getToken();
            String msgUrl = ProjectHelper.fillRequestURL(this.mUrlEntity.getMyContactDelete());
            ArrayList<Long> cardIdList = new ArrayList<Long>();
            ArrayList<Long> contactPersonIdList = new ArrayList<Long>();
            for (int i = 0, size = selectContactList.size(); i < size; i++) {
                ContactListItemEntity contact = selectContactList.get(i);
                if(contact.getCardId() > 0){
                    cardIdList.add(contact.getCardId());
                }else{
                    if(contact.getContactId() > 0){
                        contactPersonIdList.add(contact.getContactId());
                    }
                }
            }
            ContactDeleteJsonEntity jsonEntity = new ContactDeleteJsonEntity();
            jsonEntity.setCardIdList(cardIdList);
            jsonEntity.setContactPersonIdList(contactPersonIdList);
            String toJson = JsonHelper.toJson(jsonEntity, ContactDeleteJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_MY_CONTACT_DELETE, msgUrl, mAccessToken, toJson, this);
        }
    }

    /**
     * 行业类别对话框
     */
    private void showDialogFragmentBusiness(){
        if(ResourceHelper.isNull(this.mDialogFragmentBusiness)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvIconAdapter customIconAdapter = new CustomLsvIconAdapter(getActivity());
            mLsvItem.setAdapter(customIconAdapter);
            ArrayList<CustomLsvIconEntity> customIcons = new ArrayList<CustomLsvIconEntity>();
            CustomLsvIconEntity customLsvIcon = new CustomLsvIconEntity();
            customLsvIcon.setBusinessName(getString(R.string.all_business));
            customIcons.add(customLsvIcon);
            customIcons.addAll(ResourceHelper.getArrayListBusiness());
            customIconAdapter.addItems(customIcons);
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CustomLsvIconEntity lsvIcon = customIconAdapter.getItem(position);
                    mTxvBusiness.setCompoundDrawablesWithIntrinsicBounds(lsvIcon.getIconId(), 0, 0, 0);
                    mTxvBusiness.setText(R.string.business_class);
                    mAdapterVCard.switchBusiness(position);
                    mDialogFragmentBusiness.getDialog().dismiss();
                }
            });
            this.mDialogFragmentBusiness = DialogFragmentHelper.showCustomDialogFragment(R.string.select_business, mLsvItem, true);
        }
        this.mDialogFragmentBusiness.show(getFragmentManager(), "mDialogFragmentBusiness");
    }

    /**
     * 添加分组对话框
     */
    private void showAddGroupDialog(){
        if(ResourceHelper.isNull(this.mDialogFragmentAddGroupDialog)){
            View mView = LayoutInflater.from(getActivity()).inflate(R.layout.item_add_group_edt, null);
            final EditText mEdtAddGroup = (EditText) mView.findViewById(R.id.edt_add_group);
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
                            ContactGroupEntity newGroup = ContactVCardDao.getInstance().addGroup(newGroupName, 0);
                            mAdapterGroups.addItem(newGroup);
                            mGroupList.add(mGroupList.size() - 2 ,newGroup);
                            mEdtAddGroup.setText("");
                        }else{
                            ActivityHelper.showToast(R.string.enter_groups_name_please);
                        }
                    }
                }
            };

            this.mDialogFragmentAddGroupDialog = DialogFragmentHelper.showCustomDialogFragment(R.string.add_group, mView, R.string.frg_text_cancel, R.string.frg_text_ok, mOnClick);
        }
        this.mDialogFragmentAddGroupDialog.show(getFragmentManager(), "mDialogFragmentAddGroupDialog");
    }

    /**
     * 选择号码拨号弹窗
     */
    private void showDialogFragmentCallPhone(ArrayList<String> phoneList, String displayName){
        if(ResourceHelper.isNull(this.mDialogFragmentCallPhone)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            this.mLsvCallAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(this.mLsvCallAdapter);
            this.mLsvCallAdapter.setShowRadio(false);
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String phone = mLsvCallAdapter.getItem(position);
                    mDialogFragmentCallPhone.getDialog().dismiss();
                    ResourceHelper.callPhone(getActivity(), phone);
                }
            });
            this.mDialogFragmentCallPhone = DialogFragmentHelper.showCustomDialogFragment(displayName, mLsvItem);
        }
        this.mDialogFragmentCallPhone.setTitle(displayName);
        this.mLsvCallAdapter.addItems(phoneList);
        this.mDialogFragmentCallPhone.show(getFragmentManager(), "mDialogFragmentCallPhone");
    }

    /**
     * 移动至分组弹出框
     */
    private void showDialogFragmentMoveGroup(ArrayList<ContactListItemEntity> selectContactList, boolean isBatch){
        this.contacts = selectContactList;
        this.isBatchOpreratrion = isBatch;
        if(ResourceHelper.isNull(this.mDialogFragmentMoveGroup)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            this.mMoveGroupAdapter = new CustomGroupLsvAdapter(getActivity());
            mLsvItem.setAdapter(this.mMoveGroupAdapter);
            this.mMoveGroupAdapter.setShowRadio(false);
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    ContactVCardDao.getInstance().moveContact(contacts, mMoveGroupAdapter.getItem(position));
                    refreshData(true);
                    if(isBatchOpreratrion){
                        mAdapterVCard.removeVCardAllSelected();
                    }else{
                        mAdapterVCard.removeVCardItem(mAdapterVCard.getItem(curPosition));
                    }
                    mDialogFragmentMoveGroup.getDialog().dismiss();
                }
            });
            this.mDialogFragmentMoveGroup = DialogFragmentHelper.showCustomDialogFragment(R.string.move_to_group, mLsvItem);
        }
        this.mMoveGroupAdapter.addItems(this.mGroupList);
        this.mDialogFragmentMoveGroup.show(getFragmentManager(), "mDialogFragmentMoveGroup");
    }

    /**
     * 删除联系人对话框
     */
    private void showDialogFragmentDeleteContact(ArrayList<ContactListItemEntity> selectContactList, boolean isBatch){
        this.contacts = selectContactList;
        this.isBatchOpreratrion = isBatch;
        String msg = "";
        if(ResourceHelper.isNull(this.mDialogFragmentDeleteContact)){
            CustomDialogFragment.DialogFragmentInterface onClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                        if(which == CustomDialogFragment.BUTTON_POSITIVE){
                            //删除本地数据库
                            ContactVCardDao.getInstance().deleteContact(contacts);
                            refreshData(true);
                            if(isBatchOpreratrion){
                                mAdapterVCard.removeVCardAllSelected();
                            }else{
                                mAdapterVCard.removeVCardItem(mAdapterVCard.getItem(curPosition));
                            }
                            //请求删除数据
                            requestDeleteMyContact(contacts);

                         }
                }
            };
            this.mDialogFragmentDeleteContact = DialogFragmentHelper.showCustomDialogFragment(R.string.friend_delete ,msg, R.string.frg_text_cancel, R.string.frg_text_ok, onClick);
        }
        if(isBatchOpreratrion){
            msg = getString(R.string.is_delete_contact);
        }else{
            msg = getString(R.string.is_delete_contact, this.curVcard.getDisplayName());
        }
        this.mDialogFragmentDeleteContact.setMessage(msg);
        this.mDialogFragmentDeleteContact.show(getFragmentManager(), "mDialogFragmentDeleteContact");
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
                        bundle.putLong(Constants.IntentSet.INTENT_KEY_ACCOUNT_ID, curVcard.getAccountId());
                        bundle.putBoolean(Constants.IntentSet.INTENT_KEY_IS_FROM_NETWORK_FAIL, true);
                        intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                        mActivitySwitchTo.switchTo(MessageMainActivity.class, intent);
                    }
                }
            };
            this.mDialogFragmentNetwork = DialogFragmentHelper.showCustomDialogFragment(R.string.warm_prompt ,R.string.send_without_network, R.string.frg_text_cancel, R.string.frg_text_ok, onClick);
        }
        this.mDialogFragmentNetwork.show(getFragmentManager(), "mDialogFragmentNetwork");
    }



}
