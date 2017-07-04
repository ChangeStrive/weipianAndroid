package com.maya.android.vcard.ui.frg;


import android.app.Activity;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.ContactBookDao;
import com.maya.android.vcard.entity.ContactBookEntity;
import com.maya.android.vcard.entity.ContactGroupEntity;
import com.maya.android.vcard.ui.adapter.UserChooseLocalContactAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.SideBar;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.ConverChineseCharToEnHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.util.SideBarCharHintHelper;

import java.util.ArrayList;

/**
 * 选择本地联系人
 */
public class UserChooseLocalContactFragment extends BaseFragment {
    private static final String TAG = UserChooseLocalContactFragment.class.getSimpleName();
    public static final String KEY_CHOOSE_LOCAL_CONTACT = "choose_local_contact_key";
    /** Activity之间的回调 **/
    public static final int FROM_ACTIVITY_RESULT = 70001;
    /** 碎片之间的回调 **/
    public static final int FROM_FRAGMENT_RESULT = 70002;
    private int fromCode;

    private ListView mLsvChooseLocalContact;
    private TextView mTxvEmpty;
    private EditText mEdtSearch;
    private UserChooseLocalContactAdapter mLocalContactAdapter;
    private Button mBtnNegative, mBtnPositive;
    private SideBar mSibSearch;
    /** SideBar字母提示帮助类 **/
    private SideBarCharHintHelper charHintHelper;
    /** 是否滑动 **/
    private boolean isListSroll = false;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_negative:
                    //取消选择
                    mLocalContactAdapter.cancelSelectedAll();
                    ButtonHelper.setButtonEnable(mBtnNegative, R.color.color_787878, false);
                    ButtonHelper.setButtonEnable(mBtnPositive, R.color.color_787878, false);
                    break;
                case R.id.btn_positive:
                    //确定
                    ButtonHelper.setButtonEnableDelayed(mBtnPositive);
                    String mobile = mLocalContactAdapter.getMobile();
                    LogHelper.d(TAG, "mobile:" + mobile);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.IntentSet.INTENT_KEY_MOBILE, mobile);
                    switch (fromCode){
                        case FROM_ACTIVITY_RESULT:
                            //Activity之间的回调
                            Intent intent = new Intent();
                            intent.putExtra("data", bundle);
                            getActivity().setResult(Activity.RESULT_OK, intent);
                            mFragmentInteractionImpl.onActivityFinish();
                            break;
                        case FROM_FRAGMENT_RESULT:
                            //碎片之间的回调
                            break;
                    }
                    mFragmentInteractionImpl.onActivityBackPressed();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_choose_local_contact, container, false);
        this.mLsvChooseLocalContact = (ListView) view.findViewById(R.id.lsv_list);
        this.mTxvEmpty = (TextView) view.findViewById(R.id.txv_lsv_empty);
        this.mLsvChooseLocalContact.setEmptyView(this.mTxvEmpty);
        this.mLsvChooseLocalContact.setDividerHeight(0);
        this.mBtnNegative = (Button) view.findViewById(R.id.btn_negative);
        this.mBtnPositive = (Button) view.findViewById(R.id.btn_positive);
        this.mSibSearch = (SideBar) view.findViewById(R.id.sib_contact_choose);
        this.mEdtSearch = (EditText) view.findViewById(R.id.edt_enter_search);
        this.mBtnNegative.setOnClickListener(this.mOnClickListener);
        this.mBtnPositive.setOnClickListener(this.mOnClickListener);
        this.mSibSearch.setOnTouchingLetterChangedListener(this.mOnTouchingLetterChangedListener);
        this.mLsvChooseLocalContact.setOnItemClickListener(this.mOnItemClickListener);
        this.mLsvChooseLocalContact.setOnScrollListener(this.mOnScrollListener);
        this.mEdtSearch.addTextChangedListener(this.mTextWatcher);
        this.mEdtSearch.setOnKeyListener(this.mOnKeyListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTitle(R.string.add_from_contact, false);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        this.mLocalContactAdapter = new UserChooseLocalContactAdapter(getActivity());
        this.mLsvChooseLocalContact.setAdapter(this.mLocalContactAdapter);

        this.mLocalContactAdapter.addItems(getContacts());
        this.mLocalContactAdapter.setIsSingleChoose(true);
        this.charHintHelper = SideBarCharHintHelper.getIntance(getActivity(), this.mSibSearch);
        ButtonHelper.setButtonEnable(mBtnNegative, R.color.color_787878, false);
        ButtonHelper.setButtonEnable(mBtnPositive, R.color.color_787878 , false);
        this.fromCode = FROM_ACTIVITY_RESULT;
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
            fromCode = bundle.getInt(KEY_CHOOSE_LOCAL_CONTACT, FROM_ACTIVITY_RESULT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(ResourceHelper.isNotNull(this.charHintHelper)){
            this.charHintHelper.clearCharHint();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ResourceHelper.isNotNull(this.charHintHelper)){
            this.charHintHelper.clearCharHint();
        }
    }

    /**
     * item点击事件监听
     */
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mLocalContactAdapter.setSelected(position);
            if(mLocalContactAdapter.getSelectedCount() > 0){
                ButtonHelper.setButtonEnable(mBtnNegative, R.color.color_399c2f , true);
                ButtonHelper.setButtonEnable(mBtnPositive, R.color.color_399c2f , true);
            }else{
                ButtonHelper.setButtonEnable(mBtnNegative, R.color.color_787878 , false);
                ButtonHelper.setButtonEnable(mBtnPositive, R.color.color_787878 , false);
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
                mSibSearch.setBackgroundResource(R.drawable.bg_sidebar);
                mLsvChooseLocalContact.setSelection(mLocalContactAdapter.getPosition(s));
                charHintHelper.showCharHint(s);
            }
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
                ContactBookEntity item = mLocalContactAdapter.getItem(curPosition);
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
            mLocalContactAdapter.getFilter().filter(s.toString());
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
                    mLocalContactAdapter.getFilter().filter(et.getText().toString());
                }
            }
            return false;
        }
    };

    /**
     * 获取通讯录数据
     * @return
     */
    private ArrayList<ContactBookEntity> getContacts(){
        return ContactBookDao.getInstance().getListForShow(ContactGroupEntity.GROUP_ALL_ID);
    }
}
