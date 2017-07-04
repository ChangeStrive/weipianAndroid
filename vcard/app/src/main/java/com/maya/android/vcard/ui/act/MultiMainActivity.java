package com.maya.android.vcard.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.TitleMoreMenuLsvIconEntity;
import com.maya.android.vcard.ui.adapter.MultiMainFragmentAdapter;
import com.maya.android.vcard.ui.base.BaseActivity;
import com.maya.android.vcard.ui.frg.BackupAndRecoveryMainFragment;
import com.maya.android.vcard.ui.frg.CardcaseBatchOperationFragment;
import com.maya.android.vcard.ui.frg.CardcaseChooseFragment;
import com.maya.android.vcard.ui.frg.CardcaseCloudSearchFragment;
import com.maya.android.vcard.ui.frg.CardcaseGroupManagerFragment;
import com.maya.android.vcard.ui.frg.CardcaseMainFragment;
import com.maya.android.vcard.ui.frg.MessageMainFragment;
import com.maya.android.vcard.ui.frg.UserMainFragment;
import com.maya.android.vcard.ui.impl.CardcaseBatchOprerationImpl;
import com.maya.android.vcard.ui.impl.CardcaseCardNumImpl;
import com.maya.android.vcard.ui.impl.TwoFragmentParameterImpl;
import com.maya.android.vcard.ui.widget.TitleMoreMenuPopView;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.util.TitleMoreMenuPopHelper;

import java.util.ArrayList;

/**
 * 多功能页
 */
public class MultiMainActivity extends BaseActivity implements TwoFragmentParameterImpl {

    private static final String TAG = MultiMainActivity.class.getSimpleName();
    public static final String FRG_INDEX = "FRG_INDEX";
    public static final int FRG_INDEX_CARDCASE = 0;
    public static final int FRG_INDEX_MSG = 1;
    public static final int FRG_INDEX_MINE = 2;
    public static final int WHAT_ACTIVITY_TOP_LEFT_VIEW_CLICK = 10001;
    public static final int WHAT_ACTIVITY_TOP_RIGHT_VIEW_CLICK = 10002;

    private Handler mCardcaseHandler = null;
    private Handler mMessageHandler = null;
    private MultiMainFragmentAdapter mFragmentAdapter;
    private ViewPager mViewPager;
    private int mCurrentFrgIndex = FRG_INDEX_CARDCASE;
    private boolean isOpneFrgCardcaseGroup = false;
    /** 是否处于批量操作状态 默认false **/
    private boolean isBatchOperation = false;
    private TitleMoreMenuPopView mTitleMoreMenuPop;
    private CardcaseBatchOperationFragment mDialogFragmentBatchOperation;
    private CardcaseBatchOprerationImpl mCardcaseBatchOprerationListener;
    private CardcaseCardNumImpl mCardcaseCardNumListener;
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrentFrgIndex = position;
            setNavStatus(position);
            setActivityTop(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private TextView mTxvNavVCard, mTxvNavCardcase, mTxvNavMsg, mTxvNavMine;

    private View.OnClickListener mOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //Top 特殊开始
                case R.id.imv_act_top_left:
                    if(FRG_INDEX_CARDCASE == mCurrentFrgIndex){
                        int resId;
                        isOpneFrgCardcaseGroup = !isOpneFrgCardcaseGroup;
                        if(isOpneFrgCardcaseGroup){
                            resId = R.mipmap.img_cardcase_group_up;
                        }else{
                            resId = R.mipmap.img_cardcase_group_down;
                        }
                        getActivityTopLeftImv().setImageResource(resId);
                        if(Helper.isNotNull(mCardcaseHandler)){
                            mCardcaseHandler.sendEmptyMessage(WHAT_ACTIVITY_TOP_LEFT_VIEW_CLICK);
                        }
                    }
                    break;
                case R.id.imv_act_top_right:
                    if(FRG_INDEX_CARDCASE == mCurrentFrgIndex){
                        if(!isBatchOperation){
                            showTitleMoreMenuPop(v);
                        }else{
                            closeBatchOperation();
                            isBatchOperation = !isBatchOperation;
                            setActivityTopRightImv(R.mipmap.img_top_more, mOnClickListener);
                        }

                    }else if(FRG_INDEX_MSG == mCurrentFrgIndex){
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, CardcaseChooseFragment.class.getName());
                        bundle.putInt(CardcaseChooseFragment.KEY_CARDCASE_CHOOSE, CardcaseChooseFragment.CARDCASE_CHOOSE_SELECT_CONTACT_PERSON);
                        intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                        switchTo(CardcaseMainActivity.class, intent);
                    }
                    break;
                //Top 特殊结束
                case R.id.txv_nav_vcard:
                    onBackPressed();
                    break;
                case R.id.txv_nav_cardcase:
                    mViewPager.setCurrentItem(FRG_INDEX_CARDCASE);
                    break;
                case R.id.txv_nav_msg:
                    mViewPager.setCurrentItem(FRG_INDEX_MSG);
                    break;
                case R.id.txv_nav_mine:
                    mViewPager.setCurrentItem(FRG_INDEX_MINE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_main);
        this.initUI();
        this.initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogHelper.d(TAG, "RESULT:" + resultCode);
    }

    @Override
    public void switchTo(Class<?> cls, Intent intent) {
        if(Helper.isNull(intent)){
            intent = new Intent();
        }
        ActivityHelper.switchTo(this, cls, intent);
    }

    public void setCardcaseHandler(Handler handler){
        this.mCardcaseHandler = handler;
    }

    public void setMessageHandler(Handler handler){
        this.mMessageHandler = handler;
    }

    /**
     * 批量操作弹窗按钮监听
     * @param mCardcaseBatchOprerationListener
     */
    public void setCardcaseBatchOprerationListener(CardcaseBatchOprerationImpl mCardcaseBatchOprerationListener){
        this.mCardcaseBatchOprerationListener = mCardcaseBatchOprerationListener;
    }

    /**
     * 批量操作UI监听
     * @param mCardcaseCardNumListener
     */
    public void setCardcaseCardNumListener(CardcaseCardNumImpl mCardcaseCardNumListener){
        this.mCardcaseCardNumListener = mCardcaseCardNumListener;
    }

    private void initUI(){
        super.initTop();
        this.mViewPager = findView(R.id.vwp_multi);
        this.mFragmentAdapter = new MultiMainFragmentAdapter(this, getSupportFragmentManager());
        this.mViewPager.setAdapter(this.mFragmentAdapter);
        this.mViewPager.setOnPageChangeListener(this.mOnPageChangeListener);
        this.mTxvNavVCard = findView(R.id.txv_nav_vcard);
        this.mTxvNavCardcase = findView(R.id.txv_nav_cardcase);
        this.mTxvNavMsg = findView(R.id.txv_nav_msg);
        this.mTxvNavMine = findView(R.id.txv_nav_mine);

        this.mTxvNavVCard.setOnClickListener(this.mOnClickListener);
        this.mTxvNavCardcase.setOnClickListener(this.mOnClickListener);
        this.mTxvNavMsg.setOnClickListener(this.mOnClickListener);
        this.mTxvNavMine.setOnClickListener(this.mOnClickListener);

    }

    private void initData(){
        ArrayList<String> items = new ArrayList<>();
        items.add(CardcaseMainFragment.class.getName());
        items.add(MessageMainFragment.class.getName());
        items.add(UserMainFragment.class.getName());
        this.mFragmentAdapter.addItems(items);

        int frgIndex = getIntent().getIntExtra(FRG_INDEX, FRG_INDEX_CARDCASE);
        this.mCurrentFrgIndex = frgIndex;
        this.mViewPager.setCurrentItem(frgIndex);
        this.setActivityTop(frgIndex);
    }

    private void setNavStatus(int frgIndex){
        switch (frgIndex){
            case FRG_INDEX_CARDCASE:
                mTxvNavCardcase.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.img_cardcase_blue, 0, 0);
                mTxvNavMsg.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.img_msg_gray, 0, 0);
                mTxvNavMine.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.img_mine_gray, 0, 0);
                break;
            case FRG_INDEX_MSG:
                mTxvNavCardcase.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.img_cardcase_gray, 0, 0);
                mTxvNavMsg.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.img_msg_blue, 0, 0);
                mTxvNavMine.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.img_mine_gray, 0, 0);
                break;
            case FRG_INDEX_MINE:
                mTxvNavCardcase.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.img_cardcase_gray, 0, 0);
                mTxvNavMsg.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.img_msg_gray, 0, 0);
                mTxvNavMine.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.img_mine_blue, 0, 0);
                break;
        }
    }

    private void setActivityTop(int frgIndex){
        switch (frgIndex){
            case FRG_INDEX_CARDCASE:
                this.setActivityTitle(R.string.cardcase, true);
                this.setActivityTopLeftVisibility(View.VISIBLE);
                this.setActivityTopRightImvVisibility(View.VISIBLE);
                int resId;
                if(isOpneFrgCardcaseGroup){
                    resId = R.mipmap.img_cardcase_group_up;
                }else{
                    resId = R.mipmap.img_cardcase_group_down;
                }
                this.setActivityTopLeftImv(resId, this.mOnClickListener);
                this.setActivityTopRightImv(R.mipmap.img_top_more, this.mOnClickListener);
                break;
            case FRG_INDEX_MSG:
                this.setActivityTitle(R.string.message, true);
                this.setActivityTopLeftVisibility(View.GONE);
                this.setActivityTopRightImvVisibility(View.VISIBLE);
                this.setActivityTopRightImv(R.mipmap.img_top_add, this.mOnClickListener);
                break;
            case FRG_INDEX_MINE:
                this.setActivityTitle(R.string.my, true);
                this.setActivityTopLeftVisibility(View.GONE);
                this.setActivityTopRightImvVisibility(View.GONE);
                break;
        }
    }

    /**
     * 右上角弹窗
     */
    private void showTitleMoreMenuPop(View v){
        if(ResourceHelper.isNull(this.mTitleMoreMenuPop)){
            this.mTitleMoreMenuPop = TitleMoreMenuPopHelper.getVcardMainPop(this);
            this.mTitleMoreMenuPop.setItemClickListener(new TitleMoreMenuPopView.MoreMenuItemClickListener() {
                @Override
                public void onItemClick(TitleMoreMenuLsvIconEntity menu) {
                    //TODO 跳转

                    Intent intent = new Intent();
                    switch (menu.getBusinessName()) {
                        case R.string.pop_add_card:
                            //新增名片
                            break;
                        case R.string.pop_group_management:
                            //分组管理
                            intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, CardcaseGroupManagerFragment.class.getName());
                            switchTo(CardcaseMainActivity.class, intent);
                            break;
                        case R.string.pop_batch_operation:
                            //批量操作
                            dialogFragmentBatchOperation();

                            setActivityTopRightImv(R.mipmap.img_title_close, mOnClickListener);
                            isBatchOperation = !isBatchOperation;
                            break;
                        case R.string.pop_backup_restore:
                            //备份恢复
                            intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, BackupAndRecoveryMainFragment.class.getName());
                            switchTo(CardcaseMainActivity.class, intent);
                            break;
                        case R.string.pop_cloud_search:
                            //云端查找
                            intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, CardcaseCloudSearchFragment.class.getName());
                            switchTo(CardcaseMainActivity.class, intent);
                            break;
//                        case R.string.pop_nearby_friends:
//                            //附近的朋友
//                            break;
                    }
                }
            });
        }
        this.mTitleMoreMenuPop.showAtLocation(v, Gravity.FILL, 0, 0);
    }

    /**
     * 批量操作控件弹窗
     */
    private void dialogFragmentBatchOperation(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(ResourceHelper.isNull(this.mDialogFragmentBatchOperation)){
            this.mDialogFragmentBatchOperation = CardcaseBatchOperationFragment.newInstance();
            transaction.add(R.id.frg_multi_main_contend, this.mDialogFragmentBatchOperation);
        }else{
            transaction.show(this.mDialogFragmentBatchOperation);
        }
        transaction.commit();
        if (ResourceHelper.isNotNull(mCardcaseBatchOprerationListener)) {
            mCardcaseBatchOprerationListener.showSelect();
        }
    }

    /**
     * 关闭批量操作
     */
    private void closeBatchOperation(){
        if(ResourceHelper.isNotNull(this.mDialogFragmentBatchOperation)){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(this.mDialogFragmentBatchOperation);
            transaction.commit();
            if (ResourceHelper.isNotNull(mCardcaseBatchOprerationListener)) {
                mCardcaseBatchOprerationListener.hideSelect();
            }
        }
    }

    @Override
    public void twoFragmentTransfer(String frgName, Bundle bundle) {
        if(frgName.equals(CardcaseMainFragment.class.getName())){
            if(ResourceHelper.isNotNull(this.mCardcaseBatchOprerationListener)){
                this.mCardcaseBatchOprerationListener.userMethodType(bundle);
            }
        }else if(frgName.equals(CardcaseBatchOperationFragment.class.getName())){
            if(ResourceHelper.isNotNull(this.mCardcaseCardNumListener)){
                this.mCardcaseCardNumListener.refreshCardcaseBatchUI(bundle);
            }
        }
    }


}
