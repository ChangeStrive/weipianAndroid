package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ButtonHelper;

/**
 *会员等级：提升
 */
public class UserLevelUpFragment extends BaseFragment {
    private TextView mTxvNum, mTxvUserLevel, mTxvCurGrade;
    private Button mBtnSubmit;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_submit:
                    //TODO 发送邀请
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_level_up, container, false);
        this.mTxvNum = (TextView) view.findViewById(R.id.txv_num);
        this.mTxvUserLevel = (TextView) view.findViewById(R.id.txv_will_user_level);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        this.mTxvCurGrade = (TextView) view.findViewById(R.id.txv_cur_grade);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTitle(R.string.how_to_promote_membership_grade, false);
        UserInfoResultEntity userInfo = CurrentUser.getInstance().getUserInfoEntity();
        showUserLevelContent(userInfo.getRecommend());
        memberGride(userInfo.getAuth());
    }

    /**
     * 填充会员内容
     * @param mRecommend
     */
    private void showUserLevelContent(int mRecommend){
        if(mRecommend >= 50){//钻石会员
            this.mTxvUserLevel.setText(R.string.promote_bottom_content_show_have_diamond);
        }else if(mRecommend >= 10){//高级会员
            this.mTxvUserLevel.setText(R.string.promote_bottom_content_show_diamond);
        }
        mTxvNum.setText("" + mRecommend);
    }

    /**
     * 当前会员等级
     * @param mGride
     */
    private void memberGride(int mGride){
        if(mGride == Constants.MemberGrade.SENIOR){//高级会员
            this.mTxvCurGrade.setText(getString(R.string.current_user_level) + getString(R.string.senior_member));
        }else if(mGride == Constants.MemberGrade.DIAMON){//钻石会员
            this.mTxvCurGrade.setText(getString(R.string.current_user_level) + getString(R.string.diamond_member));
        }else{//普通会员
            this.mTxvCurGrade.setText(getString(R.string.current_user_level) + getString(R.string.regular_members));
        }
    }
}
