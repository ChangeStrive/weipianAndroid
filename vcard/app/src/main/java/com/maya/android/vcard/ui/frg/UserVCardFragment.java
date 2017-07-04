package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.ui.adapter.UserVCardAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.NoScrolListView;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 我的：名片
 */
public class UserVCardFragment extends BaseFragment {
    public static final String USER_VCARD_KEY_BUNDLE = "user_vcard_key_bundle";
    private TextView mTxvNum;
    private NoScrolListView mLsvVcard;
    private TextView mTxvEmpyt;
    private UserVCardAdapter mVCardAdapter;
    private CurrentUser mCurrentUser = CurrentUser.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_vcard, container, false);
        this.mLsvVcard = (NoScrolListView) view.findViewById(R.id.lsv_noscrol_list);
        this.mTxvEmpyt = (TextView) view.findViewById(R.id.txv_lsv_empty);
        this.mLsvVcard.setEmptyView(this.mTxvEmpyt);
        this.mTxvNum = (TextView) view.findViewById(R.id.txv_num);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.setActivityTitle(R.string.my_card, true);
        this.mVCardAdapter = new UserVCardAdapter(getActivity());
        this.mVCardAdapter.setUserVCardRecommendListener(this.mUserVCardRecommendListener);
        this.mLsvVcard.setAdapter(this.mVCardAdapter);
        this.mVCardAdapter.addItems(this.mCurrentUser.getVCardEntityList());
        this.mVCardAdapter.setCurrentPosition(this.mCurrentUser.getCurrentVCardPosition());
        int cardNum = 0;
        ArrayList<CardEntity> cardEntityList = CurrentUser.getInstance().getVCardEntityList();
        if(ResourceHelper.isNotNull(cardEntityList)){
            for(CardEntity vcard : cardEntityList){
                cardNum += vcard.getCardCount();
            }
        }
        this.mTxvNum.setText("" +cardNum);
    }

    private UserVCardAdapter.UserVCardRecommendListener mUserVCardRecommendListener = new UserVCardAdapter.UserVCardRecommendListener() {
        @Override
        public void recommend(int position) {
            if(CurrentUser.getInstance().isLogin()){
                Bundle bundle = new Bundle();
                bundle.putInt(USER_VCARD_KEY_BUNDLE, position);
                mFragmentInteractionImpl.onFragmentInteraction(UserSmsRecommendFriendFragment.class.getName(), bundle);
            }

        }
    };
}
