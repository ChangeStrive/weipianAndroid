package com.maya.android.vcard.ui.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.AnimationHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.SettingEntity;
import com.maya.android.vcard.entity.ShakeValueEntity;
import com.maya.android.vcard.entity.VCardFormEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.adapter.VCardNameAdapter;
import com.maya.android.vcard.ui.base.BaseActivity;
import com.maya.android.vcard.ui.frg.VCardSwapMainFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.VCardImageView;
import com.maya.android.vcard.util.AudioRecordHelper;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 名片主页：我的名片
 */
public class VCardMainActivity extends BaseActivity {

    //region 变量
    /** 名片最多为3张 **/
    private static final int VCARD_NUM_MAX = 3;

    /** ForResult:请求交换 **/
    private static final int REQUEST_CODE_VCARD_SWAP = 10000;
    private static final int WHAT_CLOSE_RIGHT_VIEW = 10001;
    private static final int WHAT_DELAY_SHAKE = 10002;
    /** Delay Time：延迟关闭右侧边栏 **/
    private static final int TIME_DELAY_CLOSE_RIGHT_VIEW = 3000;
    /** 摇一摇间隔 */
    private static final int TIME_GAP_SHAKE = 2000;
    /** Value：摇晃强度 **/
    private static final int VALUE_SERSOR_STRENGTH = 100;
    /** 扫一扫 闪烁的间隔时间 */
    private static final int DURATION_SHOW_SCAN = 800;
    /** 扫一扫 显示的持续时间 **/
    private static final long TIME_FLICKER_SCAN_SHOW = 4000;

    private TextView mTxvTitle;
    private ImageView mImvScan;
    /** 未验证标签 */
    private ImageView mImvUnverified;
    private Button mBtnScan;
    /** 我的名片图片 */
    private VCardImageView mImvVCard;
    /** 加载失败界面 */
    private LinearLayout mLilLoadFail;
    private TextView mTxvFail;
    private TextView mTxvAddCard;
    /** Sensor传感管理器 */
    private SensorManager mSensorManager;
    /** 震动 */
    private Vibrator mVibrator;
    private ShakeValueEntity mShakeValueX;
    private ShakeValueEntity mShakeValueY;
    private ShakeValueEntity mShakeValueZ;
    /** 是否正在进行摇一摇 **/
    private boolean isDoingShake = false;
    /** 摇动传感器出错提醒时间 **/
    private long mLastSensorFailHintTime = 0;
    /** 扫一扫：闪烁开始时间 **/
    private long mScanButtonFlickerStartTime;
    /** 扫描手势 **/
    private GestureDetector mScanGestureDetector;
    /** 当前选中的名片位置 */
    private int mCurrentVCardPosition = 0 ;
    /** 当前用户名片 **/
    private ArrayList<CardEntity> mVCardList;
    /** 顶部View **/
    private LinearLayout mLilVCardName;
    /** 右边栏View **/
    private LinearLayout mLilVCardRight;

    private VCardNameAdapter mAdapterVCardName;
    private CustomDialogFragment mDialogHintAddCard;
    private CustomDialogFragment mDialogSwapFail;
    private SwapFailCountDownTimer mSwapFailCountDownTimer;

    private CurrentUser mCurrentUser = CurrentUser.getInstance();
    /** 图片加载监听 **/
    private VCardImageView.ImageLoadListener mImageLoadListener = new VCardImageView.ImageLoadListener() {

        @Override
        public void imageLoading() {
//            showVcardLoading();
        }

        @Override
        public void imageLoadFinish() {
//            dismissVcardLoading();
//            showVcardLoadFail(false, false);
        }

        @Override
        public void imageLoadFail() {
//            dismissVcardLoading();
//            showVcardLoadFail(true, false);
        }
    };
    /** 滑动监听 **/
    private VCardImageView.OnEventListener mOnEventListener = new VCardImageView.OnEventListener() {

        @Override
        public void onScrollUpComplete() {
            closeVCardTopName();
            closeVCardRight();
//            MobclickAgentHelper.onEvent(MyCardActivity.this, MobclickAgentConstant.MYCARDACTIVITY_UPGLIDE);
            switchToSwapVCard(Constants.SwapCard.SWAP_CARD_WAY_TO_PADDLE);
        }

        @Override
        public void onScrollDownComplete() {
            closeVCardTopName();
            closeVCardRight();
            ActivityHelper.switchTo(VCardMainActivity.this, UnitMainActivity.class);
//            MobclickAgentHelper.onEvent(MyCardActivity.this, MobclickAgentConstant.MYCARDACTIVITY_DOWNGLIDE);
//            if(CurrentUser.getInstance().isLogin()){
//                //已登录
//                ActivityHelper.switchTo(MyCardActivity.this, NewMyCompanyActivity.class);
//            }else{
//                //未登录
//                ActivityHelper.showToast(R.string.toast_please_login_before_swap);
//                Intent intent = new Intent();
//                ActivityHelper.switchTo(MyCardActivity.this, LoginActivity.class, intent);
//            }
        }

        @Override
        public void onClick() {
            closeVCardTopName();
            showOrCloseVCardRight();
        }

        @Override
        public void onAnimRoteImage() {
            // do nothing

        }

        @Override
        public void onTopTitleClick() {
            closeVCardRight();
            showOrCloseVCardTopName();
        }

        @Override
        public void onLongClick() {
            // do nothing

        }

        @Override
        public void onScrollRightComplete() {
            closeVCardTopName();
            closeVCardRight();
            ActivityHelper.switchTo(VCardMainActivity.this, MultiMainActivity.class);
        }

        @Override
        public void onScrollLeftComplete() {
            closeVCardTopName();
            closeVCardRight();
            ActivityHelper.switchTo(VCardMainActivity.this, MultiMainActivity.class);
        }
    };
    /** 传感监听 */
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @SuppressWarnings("deprecation")
        @Override
        public void onSensorChanged(SensorEvent event) {
            CardEntity cardEntity = CurrentUser.getInstance().getCurrentVCardEntity();
            if(Helper.isNotNull(cardEntity)){
                if(Helper.isNotNull(event) && Helper.isNotNull(event.values) && event.values.length > 2){
                    float sensorValueX = event.values[SensorManager.DATA_X];
                    float sensorValueY = event.values[SensorManager.DATA_Y];
                    float sensorValueZ = event.values[SensorManager.DATA_Z];
                    mShakeValueX.add(sensorValueX);
                    mShakeValueY.add(sensorValueY);
                    mShakeValueZ.add(sensorValueZ);
                    float valueX = sensorValueX - mShakeValueX.getMedian();
                    float valueY = sensorValueX - mShakeValueX.getMedian();
                    float valueZ = sensorValueX - mShakeValueX.getMedian();
                    float valueAll = Math.abs(valueX) + Math.abs(valueY) + Math.abs(valueZ);
                    //		     Log.i("TAG", "valueX:" + valueX + " valueY:" + valueY + " valueZ:" + valueZ + " valueAll:" + valueAll);
                    SettingEntity settingEntity = CurrentUser.getInstance().getSetting();
                    int valueSersor;
                    if(Helper.isNotNull(settingEntity)){
                        valueSersor = (int)(VALUE_SERSOR_STRENGTH - settingEntity.getRockRate());
                    }else{
                        valueSersor = 70;
                    }
                    if(valueAll > valueSersor && !isDoingShake){
                        isDoingShake = true;
                        //设置摇一摇声音 TODO 为什么声音播放不出来？？？
                        AudioRecordHelper.setRescource2MediaPlay(R.raw.shake_sound);
                        //播放摇一摇声音
                        AudioRecordHelper.play();
                        if(Helper.isNotNull(mVibrator)){
                            mVibrator.vibrate(300);
                        }
                        if(NetworkHelper.isNetworkAvailable(VCardMainActivity.this)){
                            // 摇一摇进行交换
                            switchToSwapVCard(Constants.SwapCard.SWAP_CARD_WAY_TO_SHAKE);
                        }else{
                            ActivityHelper.showToast(R.string.no_network_please_open_network);
                        }
                        //延迟2秒
                        mHandler.sendEmptyMessageDelayed(WHAT_DELAY_SHAKE, TIME_GAP_SHAKE);
                    }
                }else{
                    if(System.currentTimeMillis() - mLastSensorFailHintTime > 60000){
                        ActivityHelper.showToast(R.string.sernor_fail_please_open_sernor);
                        mLastSensorFailHintTime = System.currentTimeMillis();
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                //TopView
                case R.id.imv_vcard_top_view_add:
                    //添加名片
                    judgeAddVCard();
                    closeVCardTopName();
                    break;
                case R.id.imv_vcard_top_view_edit:
                    //编辑名片
                    closeVCardTopName();
                    break;
                //RightView
                case R.id.txv_vcard_right_view_cardcase:
                    //跳转到名片夹
                    ActivityHelper.switchTo(VCardMainActivity.this, MultiMainActivity.class);
                    closeVCardRight();
                    break;
                case R.id.txv_vcard_right_view_msg:
                    //跳转到消息
                    intent = new Intent();
                    intent.putExtra(MultiMainActivity.FRG_INDEX, MultiMainActivity.FRG_INDEX_MSG);
                    ActivityHelper.switchTo(VCardMainActivity.this, MultiMainActivity.class, intent);
                    closeVCardRight();
                    break;
                case R.id.txv_vcard_right_view_mine:
                    //跳转到我的
                    intent = new Intent();
                    intent.putExtra(MultiMainActivity.FRG_INDEX, MultiMainActivity.FRG_INDEX_MINE);
                    ActivityHelper.switchTo(VCardMainActivity.this, MultiMainActivity.class, intent);
                    closeVCardRight();
                    break;
            }
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WHAT_CLOSE_RIGHT_VIEW:
                    closeVCardRight();
                    break;
                case WHAT_DELAY_SHAKE:
                    isDoingShake = false;
                    break;
            }
        }
    };
    //endregion 变量

    //region Override 方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcard_main);
        this.initUI();
        this.initData();
        this.initVCardTopName();
        this.initVCardRight();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.resetVCardAnimation();
        if(Helper.isNotNull(mSensorManager)){
            //注册传感器
            mSensorManager.registerListener(mSensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Helper.isNotNull(mSensorManager)){
            // 注销传感器
            mSensorManager.unregisterListener(mSensorEventListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //交换
        if(REQUEST_CODE_VCARD_SWAP == requestCode){
            if(RESULT_OK == resultCode){
                //交换成功
                initVCardTopName();
            }else if(RESULT_FIRST_USER == resultCode){
                // 交换失败
                this.showSwapFail();
            }else{
                //交换取消
                //do nothing
            }
        }
    }

    @Override
    protected boolean isMainActivity() {
        return true;
    }

    @Override
    public void switchTo(Class<?> cls, Intent intent) {

    }
    //endregion Override 方法

    //region private 方法
    private void initUI(){
        this.mTxvTitle = findView(R.id.txv_vcard_title);
        this.mImvScan = findView(R.id.imv_act_mycard_scan_code);
        this.mImvUnverified = findView(R.id.imv_act_mycard_unverified_label);
        this.mBtnScan = findView(R.id.btn_act_mycard_scan_code);
        this.mImvVCard = findView(R.id.imv_vcard_main);
        this.mLilLoadFail = findView(R.id.lil_vcard_load_fail);
        this.mTxvFail = findView(R.id.txv_act_mycard_load_fail_warn);
        this.mTxvAddCard = findView(R.id.txv_vcard_add);
        this.mLilVCardName = findView(R.id.lil_vcard_name);
        this.mLilVCardRight = findView(R.id.lil_vcard_right);

        //TopView
        ImageView imvTopViewAddVCard = findView(R.id.imv_vcard_top_view_add);
        ImageView imvTopViewVCardEdit = findView(R.id.imv_vcard_top_view_edit);
        imvTopViewAddVCard.setOnClickListener(this.mOnClickListener);
        imvTopViewVCardEdit.setOnClickListener(this.mOnClickListener);

        ListView lsvTopViewVCardName = findView(R.id.lsv_vcard_top_view_names);
        this.mAdapterVCardName = new VCardNameAdapter(this);
        lsvTopViewVCardName.setAdapter(this.mAdapterVCardName);
        lsvTopViewVCardName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                judjeShowOrAddVCard(position);
                closeVCardTopName();
            }
        });

        //RightView
        TextView txvRightViewCardcase = findView(R.id.txv_vcard_right_view_cardcase);
        TextView txvRightViewMsg = findView(R.id.txv_vcard_right_view_msg);
        TextView txvRightViewMine = findView(R.id.txv_vcard_right_view_mine);
        txvRightViewCardcase.setOnClickListener(this.mOnClickListener);
        txvRightViewMsg.setOnClickListener(this.mOnClickListener);
        txvRightViewMine.setOnClickListener(this.mOnClickListener);

        this.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        this.mTxvTitle.setOnClickListener(this.mOnClickListener);
        this.mLilLoadFail.setOnClickListener(this.mOnClickListener);
        this.mTxvAddCard.setOnClickListener(this.mOnClickListener);
        this.mTxvFail.setOnClickListener(this.mOnClickListener);

        this.mImvVCard.setImageLoadListener(this.mImageLoadListener);
        this.mImvVCard.setOnEventListener(this.mOnEventListener);

        this.mScanGestureDetector = new GestureDetector(this, new ButtonScanGestureListener());
        this.mBtnScan.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mScanGestureDetector.onTouchEvent(event);
            }
        });
    }

    private void initData(){
        //初始化摇动数据
        this.mShakeValueX = new ShakeValueEntity();
        this.mShakeValueY = new ShakeValueEntity();
        this.mShakeValueZ = new ShakeValueEntity();

        this.mCurrentVCardPosition = CurrentUser.getInstance().getCurrentVCardPosition();
        this.mVCardList = CurrentUser.getInstance().getVCardEntityList();
        int vcardListSize = this.mVCardList.size();
        if(vcardListSize == 0){
            //无名片
            // 提示添加名片
            this.showHintAddCard();
        }else{
            //有名片
            if(vcardListSize <= this.mCurrentVCardPosition){
                this.mCurrentVCardPosition = vcardListSize - 1;
            }
            this.showVCard(this.mVCardList.get(this.mCurrentVCardPosition));
        }
    }

    private void initVCardTopName(){
        this.mAdapterVCardName.addItems(this.mVCardList);
        int vcardListSize = this.mVCardList.size();
        if(vcardListSize <= this.mCurrentVCardPosition){
            this.mCurrentVCardPosition = vcardListSize - 1;
        }
        this.mAdapterVCardName.setPosition(this.mCurrentVCardPosition);
    }

    private void initVCardRight(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mLilVCardRight.getLayoutParams();
        params.bottomMargin = this.mImvVCard.getCardFormEntity().getSideBottom();
        this.mLilVCardRight.setLayoutParams(params);
    }

    private void judjeShowOrAddVCard(int vcardPosition){
        int vcardListSize = this.mVCardList.size();
        if(vcardPosition < vcardListSize){
            this.mCurrentVCardPosition = vcardPosition;
            this.mCurrentUser.setCurrentVCardPosition(this.mCurrentVCardPosition);
            //更新TopView位置
            this.mAdapterVCardName.setPosition(this.mCurrentVCardPosition);
            showVCard(this.mVCardList.get(this.mCurrentVCardPosition));
        }else{
            this.judgeAddVCard();
        }
    }

    private void showVCard(CardEntity entity){
        this.mImvVCard.showCard(entity);
        this.showMarkSign();
    }
    /**
     * 显示验证 或 高级会员标签
     */
    private void showMarkSign(){
        UserInfoResultEntity curUserInfo = this.mCurrentUser.getUserInfoEntity();
        if(Helper.isNotNull(curUserInfo) && this.mVCardList.size() != 0){
            if(Constants.MemberGrade.DIAMON == curUserInfo.getAuth()){
                //钻石会员
                this.mImvUnverified.setImageResource(R.mipmap.img_user_grade_diamon);
            }else if(Constants.MemberGrade.SENIOR == curUserInfo.getAuth()){
                //高级会员
                this.mImvUnverified.setImageResource(R.mipmap.img_user_grade_senior);
            }else if(Constants.MemberGrade.COMMON == curUserInfo.getAuth()){
                //普通会员
                if(Constants.BindWay.ALL == curUserInfo.getBindWay() || Constants.BindWay.MOBILE == curUserInfo.getBindWay()){
                    //全部绑定或手机绑定
                    this.mImvUnverified.setImageResource(R.mipmap.img_user_grade_common_verified);
                }else{
                    //邮箱绑定
                    this.mImvUnverified.setImageResource(R.mipmap.img_user_grade_common_unverified);
                }
            }
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mImvUnverified.getLayoutParams();
            VCardFormEntity.CardFormEntity cardFormEntity = this.mImvVCard.getCardFormEntity();
            params.bottomMargin = cardFormEntity.getMarkBottom();
            params.leftMargin = cardFormEntity.getCardLeft() - cardFormEntity.getMarkRight();
            this.mImvUnverified.setLayoutParams(params);
            this.mImvUnverified.setVisibility(View.VISIBLE);
        }else{
            this.mImvUnverified.setVisibility(View.GONE);
        }

    }

    private void showOrCloseVCardTopName(){
        if(this.mLilVCardName.getVisibility() == View.GONE){
            this.mLilVCardName.setVisibility(View.VISIBLE);
        }else{
            this.mLilVCardName.setVisibility(View.GONE);
        }
    }

    private void closeVCardTopName(){
        if(this.mLilVCardName.getVisibility() == View.VISIBLE) {
            this.mLilVCardName.setVisibility(View.GONE);
        }
    }

    private void showOrCloseVCardRight(){
        if(this.mLilVCardRight.getVisibility() == View.GONE){
            this.mLilVCardRight.setVisibility(View.VISIBLE);
            AnimationHelper.play(this.mLilVCardRight, R.anim.in_right2left, View.VISIBLE);
            this.mHandler.removeMessages(WHAT_CLOSE_RIGHT_VIEW);
            this.mHandler.sendEmptyMessageDelayed(WHAT_CLOSE_RIGHT_VIEW, TIME_DELAY_CLOSE_RIGHT_VIEW);
        }else{
            AnimationHelper.play(this.mLilVCardRight, R.anim.out_left2right, View.GONE);
        }
    }

    private void closeVCardRight(){
        if(this.mLilVCardRight.getVisibility() == View.VISIBLE) {
            AnimationHelper.play(this.mLilVCardRight, R.anim.out_left2right, View.GONE);
        }
    }
    /**
     * 进入交换
     */
    private void switchToSwapVCard(int swapWay){
        //网络判断
        if(!NetworkHelper.isNetworkAvailable(this)){
            ActivityHelper.showToast(R.string.no_network_please_open_network);
            this.resetVCardAnimation();
            return;
        }
        //判断名片
        CardEntity cardEntity = this.mCurrentUser.getCurrentVCardEntity();
        if(Helper.isNull(cardEntity)){
            this.showHintAddCard();
            this.resetVCardAnimation();
            return;
        }
        //判断名片是否已经上传
        if(Helper.isNull(cardEntity.getId()) || cardEntity.getId() < 0){
            ActivityHelper.showToast(R.string.vcard_is_uploading_please_waiting);
            this.resetVCardAnimation();
            return;
        }
        //判断当前名片数量
        int cardCount = cardEntity.getCardCount();
        if(cardCount < 0){
            ActivityHelper.showToast(R.string.vcard_has_no_count);
            this.resetVCardAnimation();
            return;
        }
        //进行交换
        Intent intent = new Intent(this, FullScreenActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.IntentSet.INTENT_KEY_VCARD_ID, cardEntity.getId());
        bundle.putInt(Constants.IntentSet.INTENT_KEY_VCARD_SWAP_WAY, swapWay);
        intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, VCardSwapMainFragment.class.getName());
        intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
        startActivityForResult(intent, REQUEST_CODE_VCARD_SWAP);
    }

    private void judgeAddVCard(){
        if(this.mCurrentUser.isLogin() && Helper.isNotNull(this.mVCardList)){
            int vcardNum = this.mVCardList.size();
            if(vcardNum > 0){
                UserInfoResultEntity userInfoEntity = this.mCurrentUser.getUserInfoEntity();
                if(Helper.isNotNull(userInfoEntity)){
                    if(Constants.MemberGrade.SENIOR == userInfoEntity.getAuth() || Constants.MemberGrade.DIAMON == userInfoEntity.getAuth()){
                        //高级会员、钻石会员，可添加3张
                        if(vcardNum < VCARD_NUM_MAX){
                            this.switchToAddVCard();
                        }else{
                            ActivityHelper.showToast(R.string.vcard_num_max_is_three);
                        }
                    }else{
                        //普通会员，不好意思，只能添加1张
                        ActivityHelper.showToast(R.string.general_user_only_add_vcard_one);
                    }
                }
            }else{
                // 提示添加名片
                this.showHintAddCard();
            }
        }
    }

    private void switchToAddVCard(){
        closeVCardTopName();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
        bundle.putBoolean(Constants.IntentSet.KEY_ADD_VCARD_FRG_IS_SHOW_BACK, true);
        ActivityHelper.switchTo(this, AddVCardActivity.class, intent);
    }
    /**
     * 展示提示添加名片
     */
    private void showHintAddCard(){
        // 提示添加名片
        if(Helper.isNull(this.mDialogHintAddCard)){
            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    switchToAddVCard();
                }
            };
            this.mDialogHintAddCard = DialogFragmentHelper.
                    showCustomDialogFragment(R.string.add_my_vcard, R.string.add_my_vcard_message, R.string.frg_text_ok, mOnClick);
            //设置返回监听
            this.mDialogHintAddCard.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
            //设置外围不可点击
            this.mDialogHintAddCard.setCanceledOutSize(false);
        }
        if(!this.mDialogHintAddCard.isAdded()) {
            this.mDialogHintAddCard.show(getSupportFragmentManager(), "mDialogHintAddCard");
        }
    }
    /**
     * 重置名片状态
     */
    private void resetVCardAnimation(){
        this.mImvVCard.reloadCardAnim();
        this.flickerScanButtonAnimation();
    }
    /**
     * 扫一扫：闪烁
     */
    private void flickerScanButtonAnimation(){
        if(Helper.isNotNull(this.mImvScan)){
            Animation animation = new AlphaAnimation(1, 0);
            animation.setDuration(DURATION_SHOW_SCAN); // duration - half a second
            //animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatCount(Animation.INFINITE);
            animation.setRepeatMode(Animation.REVERSE);
            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    mScanButtonFlickerStartTime = System.currentTimeMillis();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    long diffTime = System.currentTimeMillis() - mScanButtonFlickerStartTime;
                    if(diffTime > 2 * TIME_FLICKER_SCAN_SHOW){
                        mImvScan.clearAnimation();
                        mImvScan.setImageResource(R.mipmap.img_vcard_scan_char);
                        mImvScan.setVisibility(View.GONE);
                    }else if(diffTime > TIME_FLICKER_SCAN_SHOW && diffTime < 2 * TIME_FLICKER_SCAN_SHOW){
                        mBtnScan.setBackgroundResource(R.mipmap.btn_vcard_scan_radar);
                        mImvScan.setImageResource(R.mipmap.img_vcard_scan_radar_char);
                    }else{
                        mBtnScan.setBackgroundResource(R.mipmap.btn_vcard_scan_code);
                        mImvScan.setImageResource(R.mipmap.img_vcard_scan_char);
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mBtnScan.setBackgroundResource(R.mipmap.btn_vcard_scan_code);
                }
            });
            this.mImvScan.startAnimation(animation);
        }
    }
    /**
     * 展示名片交换失败
     */
    private void showSwapFail(){
        // 名片交换失败
        if(ResourceHelper.isNull(this.mDialogSwapFail)){
            mSwapFailCountDownTimer = new SwapFailCountDownTimer(5 * 1000, 1000);
            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
//                    mDialogSwapFail.dismiss();
                    mSwapFailCountDownTimer.cancel();
                }
            };
            this.mDialogSwapFail = DialogFragmentHelper.showCustomDialogFragment(R.string.add_my_vcard, R.string.add_my_vcard_message, R.string.frg_text_ok, mOnClick);
            //设置返回监听
            this.mDialogSwapFail.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
            //设置外围不可点击
            this.mDialogSwapFail.setCanceledOutSize(false);
        }
        String okTime = getString(R.string.frg_text_ok_time, 5);
        this.mDialogSwapFail.setPositiveButton(okTime);
        this.mSwapFailCountDownTimer.start();
        this.mDialogSwapFail.show(getSupportFragmentManager(), "mDialogSwapFail");
    }
    //endregion private 方法

    //region 子类
    /**
     * 扫描长按手势监听
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-3-14
     *
     */
    public class ButtonScanGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        //短按，触摸屏按下后片刻后抬起，会触发这个手势，如果迅速抬起则不会
        @Override
        public void onShowPress(MotionEvent e) {
//            isBtnScanLongClick = false;
        }

        // 抬起，手指离开触摸屏时触发(长按、滚动、滑动时，不会触发这个手势)
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            isBtnScanLongClick = false;
            return false;
        }

        //触摸屏按下后既不抬起也不移动，过一段时间后触发
        @Override
        public void onLongPress(MotionEvent e) {
//            isBtnScanLongClick = true;
            ButtonHelper.setButtonEnableDelayed(mBtnScan);
            ActivityHelper.switchTo(VCardMainActivity.this, FullScreenActivity.class);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
//            isBtnScanLongClick = false;
            return false;
        }
        /**
         * 单击
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
//            isBtnScanLongClick = false;
//            MobclickAgentHelper.onEvent(MyCardActivity.this, MobclickAgentConstant.QRCODESCANACTIVITY_MYQRCODEONE);
            ButtonHelper.setButtonEnableDelayed(mBtnScan);
            ActivityHelper.switchTo(VCardMainActivity.this, ScanQrCodeActivity.class);
//            Intent intent = new Intent();
//            intent.putExtra(Constant.IntentSet.INTENT_CODE_NAME, Constant.IntentSet.INTENT_CODE_MY_CARD);
//            ActivityHelper.switchTo(MyCardActivity.this, QrcodeScanActivity.class, intent);
            return true;
        }
        /**
         *  双击时产生一次
         */
        public boolean onDoubleTap(MotionEvent e) {

            return true;
        }
    }
    /**
     * 扫描失败 对话框关闭倒计时
     * @author zheng_cz
     * @since 2014年5月17日 上午10:16:25
     */
    private class SwapFailCountDownTimer extends CountDownTimer {

        public SwapFailCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mDialogSwapFail.dismiss();
        }

        @Override
        public void onTick(final long millisUntilFinished) {
            //TODO 动态修改确定button
            mDialogSwapFail.setIsAddedPositiveButton(getString(R.string.frg_text_ok_time, millisUntilFinished / 1000));
        }
    }
    //endregion 子类

}
