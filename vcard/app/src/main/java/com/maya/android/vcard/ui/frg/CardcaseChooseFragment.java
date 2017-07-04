package com.maya.android.vcard.ui.frg;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.CircleDao;
import com.maya.android.vcard.dao.ContactVCardDao;
import com.maya.android.vcard.dao.MessageSessionDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.ContactListItemEntity;
import com.maya.android.vcard.entity.CustomLsvIconEntity;
import com.maya.android.vcard.entity.json.CardcaseChooseFrowardVCardJsonEntity;
import com.maya.android.vcard.entity.json.CircleGroupJsonEntity;
import com.maya.android.vcard.entity.json.CircleGroupMemberJsonEntity;
import com.maya.android.vcard.entity.json.CircleGroupMemberManageJsonEntity;
import com.maya.android.vcard.entity.result.CreateCircleGroupResultEntity;
import com.maya.android.vcard.entity.result.MessageResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.act.MessageMainActivity;
import com.maya.android.vcard.ui.adapter.CardcaseChooseAdapter;
import com.maya.android.vcard.ui.adapter.CustomLsvIconAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.SideBar;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.ConverChineseCharToEnHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.util.SideBarCharHintHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 名片夹：联系人选择
 * <p> 联系人选择 </p>
 * <p> 名片转发对象 </p>
 * <P> 添加成员 </P>
 */
public class CardcaseChooseFragment extends BaseFragment {
    public static final String TAG = CardcaseChooseFragment.class.getName();
    public static final String KEY_CARDCASE_CHOOSE = "key_cardcase_choose";
    /** 联系人选择(消息中心) **/
    public static final int CARDCASE_CHOOSE_SELECT_CONTACT_PERSON = 50001;
    /** 名片转发对象 **/
    public static final int CARDCASE_CHOOSE_SELECT_VCARD_FORWARD = 50002;
    /** 添加成员 **/
    public static final int CARDCASE_CHOOSE_SELECT_ADD_MEMBER = 50003;
    /** 会话转发 **/
    public static final int CARDCASE_CHOOSE_SELECT_SESSION_FORWARDING = 50004;
    /** 当前选择的页面 **/
    private int mIntentCode;
    /**是否全选**/
    private boolean isSelectAll = true;//默认是
    /** 名片Id **/
    private long carId = 0;
    /** 账户Id **/
    long accountId = 0;
    /**讨论组成员Id**/
    private long mCircleGroupId = 0;
    /** 会话键值对 Id**/
    private long mDefaultMsgSessionId = 0;
    /**是否隐藏选择项CheckBox**/
    private boolean isSelectHide = true;//默认隐藏
    /** true 单选  false 多选**/
    private boolean isSingleChoose = true;//默认单选
    /**记录右边按钮的值**/
    private String mPositive;
    /** 记录左边按钮的值**/
    private String mNegativie;
    private boolean isListSroll = false;
    /** 当个选中联系人用户Id **/
    private long contactAccountId;
    private CustomDialogFragment mDialogFragmentNetworkFail, mDialogFragmentVCardForward, mDialogFragmentWarmPrompt, mDialogFragmentBusiness;
    private Button mBtnSubmit, mBtnNagative;
    private View mViewDivider;
    private SideBar mSibSelect;
    private TextView mTxvBusiness, mTxvEmpty;
    private EditText mEdtSerach;
    private ListView mLsvCardcaseChoose;
    private CardcaseChooseAdapter vCardChooseAdapter;
    /** SideBar字母提示帮助类 **/
    private SideBarCharHintHelper charHintHelper;
    private URLResultEntity mUrlEntity = CurrentUser.getInstance().getURLEntity();

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txv_act_cardcase_bus:
                    showDialogFragmentBusiness();
                    break;
                case R.id.btn_submit:
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
                    //右边按钮(单个时也是此按钮)
                    requestType();
                    break;
                case R.id.btn_negative:
                    //TODO 左边按钮
                    ButtonHelper.setButtonEnableDelayed(mBtnNagative);
                    break;
                case R.id.txv_act_top_right:
                    if(isSelectAll){//全选
                        mTitleAction.setActivityTopRightTxv(R.string.frg_text_cancel,mOnClickListener);
                        vCardChooseAdapter.setSelectedAll();
                    }else{//取消全选
                        mTitleAction.setActivityTopRightTxv(R.string.select_all,mOnClickListener);
                        vCardChooseAdapter.setCancelSelectAll();
                    }
                    setTitleRightSelect();
                    break;
            }
        }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
         if(!super.onCommandCallback2(tag, commandResult, objects)){
             switch (tag){
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
                         if(mIntentCode == CARDCASE_CHOOSE_SELECT_SESSION_FORWARDING){
                             bundle.putLong(Constants.IntentSet.INTENT_KEY_MESSAGE_SESSION_ID, mDefaultMsgSessionId);
                         }
                         intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                         mActivitySwitchTo.switchTo(MessageMainActivity.class, intent);
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
            case Constants.CommandRequestTag.CMD_REQUEST_TRANSMIT_VCARD_ALLOW_REQUEST:
                //请求转发名片成功回调
                ActivityHelper.closeProgressDialog();
                showDialogFragmentVCardForward();
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_MEMBER_ADD:
                //请求添加群聊天组成员成功回调
                ArrayList<ContactListItemEntity> contactList = this.vCardChooseAdapter.getSelectedCards();
                if(ResourceHelper.isNotNull(contactList)){
                    CircleDao.getInstance().addMember(this.mCircleGroupId, contactList);
                    StringBuilder memberName = new StringBuilder();
                    for (int i = 0, size = contactList.size(); i < size; i++) {
                        memberName.append(contactList.get(i).getDisplayName()).append(",");
                    }
                    if(Helper.isNotEmpty(memberName)){
                        String body = getString(R.string.group_chat_member_add, memberName.subSequence(0, memberName.length() - 1));
                        MessageSessionDao.getInstance().add(mCircleGroupId, Constants.MessageContentType.SESSION_GROUP_TEXT, body, 0);
                    }
                 }
                Intent intent = new Intent();
                getActivity().setResult(getActivity().RESULT_OK, intent);
                super.mFragmentInteractionImpl.onActivityFinish();
                break;
        }
    }

    @Override
    protected void onResponseFail(int tag, int responseCode, String msgInfo) {
        super.onResponseFail(tag, responseCode, msgInfo);
        //失败的回调
        switch (tag){
            case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_CREATE:
                //请求创建群组聊天
                ActivityHelper.closeProgressDialog();
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_TRANSMIT_VCARD_ALLOW_REQUEST:
                //请求转发名片
                ActivityHelper.closeProgressDialog();
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_MEMBER_ADD:
                //请求添加群聊天组回调

                break;
        }
    }

    private void setTitleRightSelect(){
        this.isSelectAll = !this.isSelectAll;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cardcase_choose, container, false);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mSibSelect = (SideBar) view.findViewById(R.id.sib_cardcase_choose);
        this.mTxvBusiness = (TextView) view.findViewById(R.id.txv_act_cardcase_bus);
        this.mEdtSerach = (EditText) view.findViewById(R.id.edt_act_cardcase_search);
        this.mTxvEmpty = (TextView) view.findViewById(R.id.txv_lsv_empty);
        this.mLsvCardcaseChoose = (ListView) view.findViewById(R.id.lsv_list);
        this.mLsvCardcaseChoose.setBackgroundColor(Color.WHITE);
        this.mBtnNagative = (Button) view.findViewById(R.id.btn_negative);
        this.mViewDivider = (View) view.findViewById(R.id.view_divider);
        this.mLsvCardcaseChoose.setEmptyView(this.mTxvEmpty);
        this.mLsvCardcaseChoose.setOnItemClickListener(this.mOnItemClickListener);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        this.mTxvBusiness.setOnClickListener(this.mOnClickListener);
        this.mLsvCardcaseChoose.setOnScrollListener(this.mOnScrollListener);
        this.mSibSelect.setOnTouchingLetterChangedListener(this.mOnTouchingLetterChangedListener);
        this.mEdtSerach.addTextChangedListener(this.mTextWatcher);
        this.mEdtSerach.setOnKeyListener(this.mOnKeyListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButtonHelper.setButtonEnable(mBtnSubmit,R.color.color_787878 ,false);
        ButtonHelper.setButtonEnable(mBtnNagative, R.color.color_787878, false);
        this.mLsvCardcaseChoose.setDividerHeight(0);
        this.vCardChooseAdapter = new CardcaseChooseAdapter(getActivity());
        this.vCardChooseAdapter.setOnItemCardcaseChooseListener(this.mOnItemCardcaseChooseListener);
        this.mLsvCardcaseChoose.setAdapter(this.vCardChooseAdapter);
        this.charHintHelper = SideBarCharHintHelper.getIntance(getActivity(), this.mSibSelect);
        showFragmentType();
     }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(ResourceHelper.isNotNull(this.charHintHelper)){
            this.charHintHelper.clearCharHint();
        }
    }

    /**
     * Item单击事件监听
     */
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            vCardChooseAdapter.setSelect(position);
        }
    };

    /**
     * 选中项数量改变监听
     */
    private CardcaseChooseAdapter.OnItemCardcaseChooseListener mOnItemCardcaseChooseListener = new CardcaseChooseAdapter.OnItemCardcaseChooseListener() {

        @Override
        public void selectCount(int count, boolean isSend) {
            if(count > 0 && isSend){
                ButtonHelper.setButtonEnable(mBtnSubmit,R.color.color_399c2f ,true);
                ButtonHelper.setButtonEnable(mBtnNagative,R.color.color_399c2f ,true);
            }else{
                ButtonHelper.setButtonEnable(mBtnSubmit,R.color.color_787878 ,false);
                ButtonHelper.setButtonEnable(mBtnNagative,R.color.color_787878 ,false);
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
                mLsvCardcaseChoose.setSelection(vCardChooseAdapter.getPosition(s));
                charHintHelper.showCharHint(s);
            }
        }
    };

    /**
     * 键盘事件监听
     */
    private View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                EditText et = (EditText) v;
                if(et.getText().toString().length() > 0){
                    vCardChooseAdapter.getFilter().filter(et.getText().toString());
                }
            }
            return false;
        }
    };

    /**
     * 输入查询监听
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
            vCardChooseAdapter.getFilter().filter(s.toString());
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
                ContactListItemEntity item = vCardChooseAdapter.getItem(curPosition);
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
     * 当前页面类型
     */
    private void showFragmentType(){
        this.mIntentCode = CARDCASE_CHOOSE_SELECT_CONTACT_PERSON;//默认是选择联系人
        final Bundle arg = getArguments();
        if(ResourceHelper.isNotNull(arg)){
            this.mIntentCode = arg.getInt(KEY_CARDCASE_CHOOSE, CARDCASE_CHOOSE_SELECT_CONTACT_PERSON);
        }
        this.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        switch (this.mIntentCode){
            case CARDCASE_CHOOSE_SELECT_VCARD_FORWARD:
                // 名片转发对象
                super.mTitleAction.setActivityTitle(R.string.select_vcard_forward_object, false);
                super.mTitleAction.setActivityTopRightTxv(R.string.select_all, this.mOnClickListener);
                super.mTitleAction.getActivityTopRightTxv().setVisibility(View.VISIBLE);
                this.mPositive = getString(R.string.pop_vcard_forward);
                this.mNegativie = "";
                this.carId = arg.getLong(Constants.IntentSet.INTENT_KEY_VCARD_ID, 0);
                this.accountId = arg.getLong(Constants.IntentSet.INTENT_KEY_ACCOUNT_ID, 0);
                setSelectHideAndSelectSingleChoose(false, true);
                boolean isShowPrompt = PreferencesHelper.getInstance().getBoolean(Constants.Preferences.KEY_IS_TRANSMIT_VCARD_PROMPT_SHOW, true);
                if(isShowPrompt){
                     showDialogFragmentWarmPrompt();
                }
                break;
            case CARDCASE_CHOOSE_SELECT_ADD_MEMBER:
                //添加成员
                super.mTitleAction.setActivityTitle(R.string.add_member, false);
                super.mTitleAction.setActivityTopRightTxv(R.string.select_all, this.mOnClickListener);
                super.mTitleAction.getActivityTopRightTxv().setVisibility(View.VISIBLE);
                this.mPositive = getString(R.string.add_member);
                this.mNegativie = "";

                setSelectHideAndSelectSingleChoose(false, false);
            break;
            case CARDCASE_CHOOSE_SELECT_SESSION_FORWARDING:
                //会话转发
                super.mTitleAction.setActivityTitle(R.string.select_vcard_forward_object, false);
                super.mTitleAction.setActivityTopRightTxv(R.string.select_all, this.mOnClickListener);
                super.mTitleAction.getActivityTopRightTxv().setVisibility(View.VISIBLE);
                this.mPositive = getString(R.string.forward_conversation);
                this.mNegativie = "";
                setSelectHideAndSelectSingleChoose(false, false);
                this.mDefaultMsgSessionId = arg.getLong(Constants.IntentSet.INTENT_KEY_MESSAGE_SESSION_ID, 0L);
                break;
            case CARDCASE_CHOOSE_SELECT_CONTACT_PERSON:
                //联系人选择
                super.mTitleAction.setActivityTitle(R.string.select_contact_person, false);
                super.mTitleAction.setActivityTopRightTxv(R.string.select_all, this.mOnClickListener);
                this.mPositive = getString(R.string.initiate_chat);
                this.mNegativie = "";
                setSelectHideAndSelectSingleChoose(false, false);
                break;
        }

        if(ResourceHelper.isNotEmpty(this.mNegativie)){
            this.mBtnNagative.setVisibility(View.VISIBLE);
            this.mViewDivider.setVisibility(View.VISIBLE);
        }else{
            this.mBtnNagative.setVisibility(View.GONE);
            this.mViewDivider.setVisibility(View.GONE);
        }
        this.mBtnNagative.setText(this.mNegativie);
        this.mBtnSubmit.setText(this.mPositive);
        new AsyncTask<Void, Void, ArrayList<ContactListItemEntity>>() {

            @Override
            protected ArrayList<ContactListItemEntity> doInBackground(Void... params) {
                ArrayList<ContactListItemEntity> contactList = null;
                if(mIntentCode == CARDCASE_CHOOSE_SELECT_ADD_MEMBER){
                    mCircleGroupId = arg.getLong(Constants.IntentSet.INTENT_KEY_MESSAGE_TAG_ID, 0);
                    contactList = ContactVCardDao.getInstance().getListForCircle(mCircleGroupId);
                }else{
                    contactList = ContactVCardDao.getInstance().getListForExchange(carId);
                }
                return contactList;
            }
            protected void onPostExecute(ArrayList<ContactListItemEntity> result) {
                vCardChooseAdapter.addItems(result);
                vCardChooseAdapter.setIsSelectHide(isSelectHide);
                vCardChooseAdapter.setIsSingleChoose(isSingleChoose);
                mTxvEmpty.setText(R.string.frg_common_nothing_data);
            }
        }.execute();
    }

    /**
     * 设置适配器CheckBox状态
     * @param isHide 是否隐藏 CheckBox
     * @param isSingleChoose 是否是单选 CheckBox
     */
    private void setSelectHideAndSelectSingleChoose(boolean isHide, boolean isSingleChoose){
        this.isSelectHide = isHide;
        this.isSingleChoose = isSingleChoose;
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
                    vCardChooseAdapter.switchBusiness(position);
                    mDialogFragmentBusiness.getDialog().dismiss();
                }
            });
            this.mDialogFragmentBusiness = DialogFragmentHelper.showCustomDialogFragment(R.string.select_business, mLsvItem, true);
        }
        this.mDialogFragmentBusiness.show(getFragmentManager(), "mDialogFragmentBusiness");
    }


    /**
     * 温馨提示对话框
     */
    private void showDialogFragmentWarmPrompt(){
        if(ResourceHelper.isNull(this.mDialogFragmentWarmPrompt)){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_cardcase_choose_propmt, null);
            TextView tvxMsg = (TextView) view.findViewById(R.id.txv_msg);
            tvxMsg.setText(R.string.confirm_transmit_card);
            final CheckBox chbIsAgree = (CheckBox) view.findViewById(R.id.chb_is_agree);
            CustomDialogFragment.DialogFragmentInterface dlgOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        if(chbIsAgree.isChecked()){
                            PreferencesHelper.getInstance().putBoolean(Constants.Preferences.KEY_IS_TRANSMIT_VCARD_PROMPT_SHOW, false);
                        }else{
                            PreferencesHelper.getInstance().putBoolean(Constants.Preferences.KEY_IS_TRANSMIT_VCARD_PROMPT_SHOW, true);
                        }
                    }
                    mDialogFragmentWarmPrompt.getDialog().dismiss();
                }
            };
            this.mDialogFragmentWarmPrompt = DialogFragmentHelper.showCustomDialogFragment(R.string.warm_prompt, view ,R.string.frg_text_ok, dlgOnClick);
        }
        this.mDialogFragmentWarmPrompt.show(getFragmentManager(), "mDialogFragmentWarmPrompt");
    }

    /**
     * 请求
     */
    private void requestType(){
        switch (this.mIntentCode){
            case CARDCASE_CHOOSE_SELECT_VCARD_FORWARD:
                //网络验证
                if(!NetworkHelper.isNetworkAvailable(getActivity())){
                    ActivityHelper.showToast(R.string.no_network);
                    return;
                }
                // 名片转发对象请求
                requestForwardVCard(vCardChooseAdapter.getSelectedCards());
                break;
            case CARDCASE_CHOOSE_SELECT_ADD_MEMBER:
                //网络验证
                if(!NetworkHelper.isNetworkAvailable(getActivity())){
                    ActivityHelper.showToast(R.string.no_network);
                    return;
                }
                //添加讨论组成员请求
                requestAddCircleGroupMember(vCardChooseAdapter.getSelectedCards());
                break;
            case CARDCASE_CHOOSE_SELECT_SESSION_FORWARDING:
                //会话转发请求
            case CARDCASE_CHOOSE_SELECT_CONTACT_PERSON:
                //联系人选择请求
                requestSendMess(vCardChooseAdapter.getSelectedCards());
                break;
        }
    }

    /**
     * 请求转发名片
     * @param contactList
     */
    private void requestForwardVCard(ArrayList<ContactListItemEntity> contactList){
        if(ResourceHelper.isNotNull(contactList) && ResourceHelper.isNotNull(this.mUrlEntity)){
                ContactListItemEntity  contact = contactList.get(0);
                if(ResourceHelper.isNotNull(contact)){
                    ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
                    String url = ProjectHelper.fillRequestURL(this.mUrlEntity.getMsgVcardShare());
                    String token = CurrentUser.getInstance().getToken();
                    ArrayList<MessageResultEntity> msgParamList = new ArrayList<MessageResultEntity>();
                    String displayName = "";
                    UserInfoResultEntity userInfo = CurrentUser.getInstance().getUserInfoEntity();
                    if(ResourceHelper.isNotNull(userInfo)){
                        displayName = userInfo.getDisplayName();
                    }
                    String body = getString(R.string.notice_request_transmit_card, displayName, contact.getDisplayName());
                    MessageResultEntity msgParam = new MessageResultEntity();
                    msgParam.setToAccountId(this.accountId);
                    msgParam.setTagId(contact.getAccountId());
                    msgParam.setBody(body);
                    msgParam.setContentType(Constants.MessageContentType.NOTICE_TRANSMIT_ALLOW_REQUEST);
                    msgParam.setResult(-1);
                    msgParam.setCreateTime(ResourceHelper.getNowTime());
                    msgParam.setLoseTime(0);
                    long myCardId = 0;
                    CardEntity card = CurrentUser.getInstance().getCurrentVCardEntity();
                    if(ResourceHelper.isNotNull(card)){
                        myCardId = card.getId();
                    }
                    msgParam.setFromCardId(myCardId);
                    if(Helper.isNotNull(userInfo)){
                        msgParam.setFromAccountId(userInfo.getId());
                        msgParam.setFromHeadImg(userInfo.getHeadImg());
                        msgParam.setFromName(userInfo.getDisplayName());
                    }
                    msgParamList.add(msgParam);
                    CardcaseChooseFrowardVCardJsonEntity frowarVCard = new CardcaseChooseFrowardVCardJsonEntity();
                    frowarVCard.setMessageParamList(msgParamList);
                    String msgJson = JsonHelper.toJson(frowarVCard, CardcaseChooseFrowardVCardJsonEntity.class);
                    ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_TRANSMIT_VCARD_ALLOW_REQUEST, url, token, msgJson, this);
                }
        }
    }

    /**
     * 请求发送消息
     * @param contactList
     */
    private void requestSendMess(ArrayList<ContactListItemEntity> contactList){
        if(ResourceHelper.isNotNull(contactList)){
            int size = contactList.size();
            if(size == 1){
                ContactListItemEntity contact = contactList.get(0);
                if(ResourceHelper.isNotNull(contact)){
                    if(!NetworkHelper.isNetworkAvailable(getActivity())){
                        showDialogFragmentNetworkFail(contact);
                    }else{
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, MessageConversationFragment.class.getName());
                        bundle.putLong(Constants.IntentSet.INTENT_KEY_ACCOUNT_ID, contact.getAccountId());
                        bundle.putString(Constants.IntentSet.INTENT_KEY_TITLE_NAME, contact.getDisplayName());
                        if(mIntentCode == CARDCASE_CHOOSE_SELECT_SESSION_FORWARDING){
                            bundle.putLong(Constants.IntentSet.INTENT_KEY_MESSAGE_SESSION_ID, mDefaultMsgSessionId);
                        }
                        intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                        mActivitySwitchTo.switchTo(MessageMainActivity.class, intent);
                    }
                }
            }else{
                //网络验证
                if(!NetworkHelper.isNetworkAvailable(getActivity())){
                    ActivityHelper.showToast(R.string.no_network);
                }else{
                    requestCreateCircleGroup(contactList);
                }
            }
        }
    }

    /**
     * 请求创建聊天组
     * @param contactList
     */
    private void requestCreateCircleGroup(ArrayList<ContactListItemEntity> contactList) {
        if (ResourceHelper.isNotNull(contactList) && ResourceHelper.isNotNull(this.mUrlEntity)) {
            ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
            ArrayList<CircleGroupMemberJsonEntity> memberList = new ArrayList<CircleGroupMemberJsonEntity>();
            CircleGroupMemberJsonEntity member = new CircleGroupMemberJsonEntity();
            for (int i = 0, size = contactList.size(); i < size; i++) {
                ContactListItemEntity contact = contactList.get(i);
                if (ResourceHelper.isNotNull(contact)) {
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
            if (ResourceHelper.isNotNull(curCard)) {
                myCardId = curCard.getId();
            }
            // 添加自己到成员列表
            if (myCardId < 1) {
                myCardId = 0;
            }
            UserInfoResultEntity userInfo = CurrentUser.getInstance().getUserInfoEntity();
            long myAccountId = 0;
            if (Helper.isNotNull(userInfo)) {
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
     * 请求添加群聊天组成员
     * @param contactList
     */
    private void requestAddCircleGroupMember(ArrayList<ContactListItemEntity> contactList){
        if(ResourceHelper.isNotNull(contactList) && ResourceHelper.isNotNull(this.mUrlEntity)){
            String url = ProjectHelper.fillRequestURL(this.mUrlEntity.getMessageGroupChatAdd());
            String token = CurrentUser.getInstance().getToken();

            CircleGroupMemberManageJsonEntity memberJsonEntity = new CircleGroupMemberManageJsonEntity();
            memberJsonEntity.setGroupId(mCircleGroupId);
            ArrayList<CircleGroupMemberJsonEntity> memberList = new ArrayList<CircleGroupMemberJsonEntity>();
            CircleGroupMemberJsonEntity member = null;
            for (int i = 0, size = contactList.size(); i < size; i++) {
                ContactListItemEntity contact = contactList.get(i);
                if(ResourceHelper.isNotNull(contact)){
                    LogHelper.d(TAG, ""+ contact.getCardId() + "*" + contact.getAccountId());
                    member = new CircleGroupMemberJsonEntity();
                    member.setCardId(contact.getCardId());
                    member.setAccountId(contact.getAccountId());
                    member.setGroupId(mCircleGroupId);
                    memberList.add(member);
                }

            }
            memberJsonEntity.setMemberList(memberList);

            String memberJson = JsonHelper.toJson(memberJsonEntity, CircleGroupMemberManageJsonEntity.class);

            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CIRCLE_GROUP_MEMBER_ADD, url, token, memberJson, this);
        }
    }

    /**
     * 名片转发结果对话框
     */
    private void showDialogFragmentVCardForward(){
        if(ResourceHelper.isNull(this.mDialogFragmentVCardForward)){
            CustomDialogFragment.DialogFragmentInterface dlgOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        mFragmentInteractionImpl.onActivityBackPressed();
                    }
                    mDialogFragmentVCardForward.getDialog().dismiss();
                }
            };
            this.mDialogFragmentVCardForward = DialogFragmentHelper.showCustomDialogFragment(R.string.warm_prompt, R.string.confirm_transmit_card_ok,R.string.frg_text_ok, dlgOnClick);
        }
        this.mDialogFragmentVCardForward.show(getFragmentManager(), "mDialogFragmentVCardForward");
    }

    /**
     * 网络不给力对话框
     */
    private void showDialogFragmentNetworkFail(ContactListItemEntity contact){
        this.contactAccountId = contact.getAccountId();
        if(ResourceHelper.isNull(this.mDialogFragmentNetworkFail)){
            CustomDialogFragment.DialogFragmentInterface dlgOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, MessageConversationFragment.class.getName());
                        bundle.putLong(Constants.IntentSet.INTENT_KEY_ACCOUNT_ID, contactAccountId);
                        bundle.putBoolean(Constants.IntentSet.INTENT_KEY_IS_FROM_NETWORK_FAIL, true);
                        if(mIntentCode == CARDCASE_CHOOSE_SELECT_SESSION_FORWARDING){
                            bundle.putLong(Constants.IntentSet.INTENT_KEY_MESSAGE_SESSION_ID, mDefaultMsgSessionId);
                        }
                        intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                        mActivitySwitchTo.switchTo(MessageMainActivity.class, intent);
                    }
                    mDialogFragmentNetworkFail.getDialog().dismiss();
                }
            };
            this.mDialogFragmentNetworkFail = DialogFragmentHelper.showCustomDialogFragment(R.string.warm_prompt, R.string.message_chat_send_without_network, R.string.frg_text_cancel, R.string.frg_text_ok, dlgOnClick);
         }
        this.mDialogFragmentNetworkFail.show(getFragmentManager(), "mDialogFragmentNetworkFail");
    }

}
