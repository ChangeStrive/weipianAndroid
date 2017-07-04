package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.RatingbarView;

/**
 * 会员等级：主页
 * */
public class UserLevelFragment extends BaseFragment {

    private TextView  mTxvCurrentIntergal, mTxvCurrentGride;
    private RatingbarView mRabvCurrentGride, mRabvCurrentIntergal;
    private ImageView mImvVerify;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 点击事件
            switch (v.getId()){
                case R.id.txv_understand_the_membership_grade:
                    //了解会员等级
                    mFragmentInteractionImpl.onFragmentInteraction(UserLevelIntroduceFragment.class.getName(), null);
                    break;
                case R.id.txv_how_to_promote_membership_grade:
                    //如何提升会员等级
                    mFragmentInteractionImpl.onFragmentInteraction(UserLevelUpFragment.class.getName(), null);
                    break;
                case R.id.txv_how_to_get_a_card_and_integral:
                    //如何获得名片和积分
                    mFragmentInteractionImpl.onFragmentInteraction(UserLevelGetPointFragment.class.getName(), null);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_level, container, false);
        this.mImvVerify = (ImageView) view.findViewById(R.id.imv_verify);
        this.mTxvCurrentIntergal = (TextView) view.findViewById(R.id.txv_show_current_integral);
        this.mTxvCurrentGride = (TextView) view.findViewById(R.id.txv_show_current_gride);
        this.mRabvCurrentGride = (RatingbarView) view.findViewById(R.id.rabv_class_grade);
        this.mRabvCurrentIntergal = (RatingbarView) view.findViewById(R.id.rabv_class_integral);
        TextView mTxvUnderstandLevelGrade = (TextView) view.findViewById(R.id.txv_understand_the_membership_grade);
        TextView mTxvPromoteLevelGrade = (TextView) view.findViewById(R.id.txv_how_to_promote_membership_grade);
        TextView mTxvGetCardAndIntegral = (TextView) view.findViewById(R.id.txv_how_to_get_a_card_and_integral);
        mTxvUnderstandLevelGrade.setOnClickListener(this.mOnClickListener);
        mTxvPromoteLevelGrade.setOnClickListener(this.mOnClickListener);
        mTxvGetCardAndIntegral.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoResultEntity userInfo = CurrentUser.getInstance().getUserInfoEntity();
        this.mTitleAction.setActivityTitle(R.string.user_vip, true);
        //验证情况
        int binWay = userInfo.getBindWay();
        if(Constants.BindWay.MOBILE == binWay || Constants.BindWay.ALL == binWay){
            this.mImvVerify.setImageResource(R.mipmap.img_card_mobile_vertify);
        }else{
            this.mImvVerify.setImageResource(R.mipmap.img_card_mobile_unvertify);
        }
        memberGride(userInfo.getAuth(), userInfo.getIntegral());
    }

    /**
     * 默认是普通会员
     * @param mGride  会员等级
     * @param integral 会员积分
     */
    private void memberGride(int mGride, int integral){
        this.mTxvCurrentIntergal.setText(getString(R.string.current_integral) + integral);
        this.mRabvCurrentIntergal.setRatingbarDrawable(R.mipmap.bg_member_points_gay,R.mipmap.bg_member_points);
        if(mGride == Constants.MemberGrade.SENIOR){//高级会员
            this.mRabvCurrentGride.setRatingbarDrawable(R.mipmap.bg_senior_member_gay, R.mipmap.bg_senior_member);
            this.mTxvCurrentGride.setText(R.string.senior_member);
        }else if(mGride == Constants.MemberGrade.DIAMON){//钻石会员
            this.mRabvCurrentGride.setRatingbarDrawable(R.mipmap.bg_diamond_member_gay, R.mipmap.bg_diamond_member);
            this.mTxvCurrentGride.setText(R.string.diamond_member);
        }
        double interFloat = ((double)integral) / 200;
        this.mRabvCurrentGride.setRating(interFloat);
        this.mRabvCurrentIntergal.setRating(interFloat);
    }
}
