package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.UserSmsRecommendFriendEntity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.AddPhoneNumberView;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 通过短信分享给好友
 */
public class UserSmsRecommendFriendFragment extends BaseFragment {
    private Button mBtnSubmit;
    private LinearLayout mLilAddPhone;
    /** 刷新UI **/
    private static final int REFRESH_UI_ALL = 200001;
    /** 添加一个号码输入框 **/
    private static final int REFRESH_UI_ADD = 200002;
    /** 要推荐的名片 **/
    private CardEntity mCardEntity;
    /** 当前选择Item的position **/
    private int curPosition = -1;
    /** 记录的推荐Item **/
    private SparseArray<UserSmsRecommendFriendEntity> phoneNumbers = new SparseArray<UserSmsRecommendFriendEntity>();
    private UserSmsRecommendFriendEntity mItem = new UserSmsRecommendFriendEntity();
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_send:
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
                    //TODO 发送短信
                    break;
                case R.id.imv_act_top_right:
                    //添加更多输入框
                    addPhoneViewManual(getUserSmsRecommendFriendEntity());
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_sms_recommend_friend, container, false);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_send);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        this.mLilAddPhone = (LinearLayout) view.findViewById(R.id.lil_add_phone);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.sms_recommend_friend, false);
        super.mTitleAction.setActivityTopRightImvVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImv(R.mipmap.img_top_add, this.mOnClickListener);
        refreshData();
        //获取当前正在使用的微片编号
        int curPosition = CurrentUser.getInstance().getCurrentVCardPosition();
        int mCurCardId = 0;
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
             mCurCardId = bundle.getInt(UserVCardFragment.USER_VCARD_KEY_BUNDLE, curPosition);
        }
        if(mCurCardId < 1){
            mCurCardId = curPosition;
        }
        this.mCardEntity = CurrentUser.getInstance().getVCardEntityList().get(mCurCardId);
    }

    /**
     * 手动添加AddViewItem
     * @param phoneNumber
     */
    private void addPhoneViewManual(UserSmsRecommendFriendEntity phoneNumber){
        final AddPhoneNumberView addPhones = new AddPhoneNumberView(getActivity());
        addPhones.getImvBook().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curPosition = addPhones.getCurPosition();
                savePhoneNumbers();
                ActivityHelper.showLongToast(addPhones.getEdtContent());
                mFragmentInteractionImpl.onFragmentInteraction(UserChooseLocalContactFragment.class.getName(), null);
            }
        });
        addPhones.setEdtContent(phoneNumber.getPhoneNumber());
        addPhones.setChecked(phoneNumber.getIsChecked());
        //新添加view在LinearLayout中的位置
        int position = this.mLilAddPhone.getChildCount();
        addPhones.setCurPosition(position);
        this.mLilAddPhone.addView(addPhones);
        this.phoneNumbers.put(addPhones.getCurPosition(), phoneNumber);
    }

    /**
     * 刷新数据和UI
     */
    private void refreshData(){
        this.mLilAddPhone.removeAllViews();
        toAddPhoneView();
    }

    /**
     * 在线程中生成一个新的View
     */
    private void toAddPhoneView(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //刷新所有UI
            int count = phoneNumbers.size();
                if(count <= 0){
                    addPhoneViewManual(getUserSmsRecommendFriendEntity());
                }else{
                    int key;
                    UserSmsRecommendFriendEntity item;
                    for(int i = 0; i < count; i++){
                        key = phoneNumbers.keyAt(i);
                        item = phoneNumbers.get(key, null);
                        if(ResourceHelper.isNotNull(item)){
                            String phoneNumber = item.getPhoneNumber();
                            boolean isChecked = item.getIsChecked();
                            addPhoneView(phoneNumber, isChecked, key);
                        }
                    }
                }
            }

    };

    /**
     * 动态添加AddViewItem
     */
    private void addPhoneView(String phoneNumber, boolean isChecked, int key){

        final AddPhoneNumberView addPhones = new AddPhoneNumberView(getActivity());

        addPhones.getImvBook().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curPosition = addPhones.getCurPosition();
                savePhoneNumbers();
                Bundle bundle = new Bundle();
                bundle.putInt(UserChooseLocalContactFragment.KEY_CHOOSE_LOCAL_CONTACT, UserChooseLocalContactFragment.FROM_FRAGMENT_RESULT);
                mFragmentInteractionImpl.onFragmentInteraction(UserChooseLocalContactFragment.class.getName(), bundle);
            }
        });
        addPhones.setEdtContent(phoneNumber);
        addPhones.setChecked(isChecked);
        addPhones.setCurPosition(key);
        this.mLilAddPhone.addView(addPhones);
     }

    /**
     * 克隆实体类
     * @return
     */
    private UserSmsRecommendFriendEntity getUserSmsRecommendFriendEntity(){
        UserSmsRecommendFriendEntity item = null;
        try {
            item =(UserSmsRecommendFriendEntity) this.mItem.clone();
            item.setIsChecked(true);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
       return item;
    }

    /**
     * 保存输入的手机号码
     */
    private void savePhoneNumbers(){
        int count = this.mLilAddPhone.getChildCount();
        UserSmsRecommendFriendEntity phoneNumber;
        AddPhoneNumberView addPhones;
        for(int i = 0; i < count; i++){
            addPhones = (AddPhoneNumberView) this.mLilAddPhone.getChildAt(i);
            phoneNumber = this.phoneNumbers.get(addPhones.getCurPosition(), null);
            if(ResourceHelper.isNull(phoneNumber)){
                phoneNumber = getUserSmsRecommendFriendEntity();
                this.phoneNumbers.put(addPhones.getCurPosition(), phoneNumber);
            }
            phoneNumber.setPhoneNumber(addPhones.getEdtContent());
            phoneNumber.setIsChecked(addPhones.getChecked());
        }
    }

    /**
     * 获取所有选中且有效的电话号码的号码
     * @return
     */
    private String[] getSelectArray(){
        savePhoneNumbers();
        ArrayList<String> selectPhoneNumbers = new ArrayList<String>();
        int count = this.phoneNumbers.size();
        int key;
        for(int i = 0; i < count; i++){
            key = this.phoneNumbers.keyAt(i);
            UserSmsRecommendFriendEntity phoneNumber = this.phoneNumbers.get(key, null);
            if(ResourceHelper.isNotNull(phoneNumber) && phoneNumber.getIsChecked()){
                String phone = phoneNumber.getPhoneNumber();
                if(ResourceHelper.isValidMobile(phone)){
                    selectPhoneNumbers.add(phone);
                }
            }
        }
        int size = selectPhoneNumbers.size();
        if(size > 0){
            String[] mPhoneArr = new String[size];
            selectPhoneNumbers.toArray(mPhoneArr);
            return mPhoneArr;
        }
        return null;
    }
}
