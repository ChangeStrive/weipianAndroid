package com.maya.android.vcard.ui.act;

import android.annotation.SuppressLint;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.android.ocr.AbstractVcardScanActivity;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.AddVCardEntity;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.util.ResourceHelper;
import com.umeng.analytics.MobclickAgent;

public class AddVCardScanActivity extends AbstractVcardScanActivity {

    private static final String TAG = AddVCardScanActivity.class.getSimpleName();

    /** IntentKey：是否为双面模式 **/
    public static final String KEY_IS_DOUBLE_MODLE = "KEY_IS_DOUBLE_MODLE";
    /** HandlerWhat：设置View为enable **/
    private static final int WHAT_HANDLER_ENABLE = 1001;
    private static final int WHAT_HINT_GONE = 1002;
    /** 开启自动闪光灯 */
    private static final int CAMERA_FLASH_MODE_AUTO = 2000;
    /** 关闭闪光灯 */
    private static final int CAMERA_FLASH_MODE_OFF = 2001;
    /** 闪光灯长亮 */
    private static final int CAMERA_FLASH_MODE_TORCH = 2002;

    /** 是否判断过手机是否支持闪光灯 **/
    private static boolean sIsJudgeCameraFlash = false;
    /** 手机是否支持闪光灯 **/
    private static boolean sIsSupportCameraFlash = false;
    private ImageView mImvCancel, mImvTake, mImvModelSingle, mImvModelMulti,mImvModelPrompt;
    private ImageView mImvLightSingle, mImvLightDefault, mImvLightAuto, mImvLightClose, mImvExplain;
    private TextView mTxvBottom;
    /** 闪光灯选择栏 */
    private LinearLayout mLilLight;
    private SurfaceView mSurfaceView;
    private Camera mCamera;
    /** 正反面 */

    /** 是否为双面模式 **/
    private boolean isDoubleModel = false;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case WHAT_HANDLER_ENABLE:
                    View v = (View) msg.obj;
                    if(Helper.isNotNull(v)){
                        v.setEnabled(true);
                    }
                    break;
                case WHAT_HINT_GONE:
                    mImvModelPrompt.setVisibility(View.GONE);
                    break;
            }
        }

    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imv_act_card_camera_cancel:
                    // 取消拍照
//                    MobclickAgentHelper.onEvent(AddVCardCameraActivity.this, MobclickAgentConstant.CARDSCANACTIVITY_BACKPRESSED);
                    //TODO  好像有点东西没做
//				if(CurrentUser.getInstance().isResetCard()){
                    //重新拍摄来着
                    finish();
//				}else{
//					onBackPressed();
//				}
                    break;
                case R.id.imv_act_card_camera_take:
//                    MobclickAgentHelper.onEvent(AddVCardCameraActivity.this, MobclickAgentConstant.CARDSCANACTIVITY_TAKEPHOTOS);
                    v.setEnabled(false);
                    // 拍照
                    takePhoto();
                    Message msg = new Message();
                    msg.what = WHAT_HANDLER_ENABLE;
                    msg.obj = v;
                    mHandler.sendMessageDelayed(msg, 500);
                    break;
                case R.id.imv_act_card_camera_model_single:
//                    MobclickAgentHelper.onEvent(AddVCardCameraActivity.this, MobclickAgentConstant.CARDSCANACTIVITY_SINGLESIDEMODE);
                    // 单张模式
                    switchModel(false);
                    setTwoSideMode(false);
                    break;
                case R.id.imv_act_card_camera_model_multi:
//                    MobclickAgentHelper.onEvent(AddVCardCameraActivity.this, MobclickAgentConstant.CARDSCANACTIVITY_DOUBLESIDEMODE);
                    // 多张模式
                    switchModel(true);
                    setTwoSideMode(true);
                    break;
                case R.id.imv_act_card_camera_explain:
//                    MobclickAgentHelper.onEvent(AddVCardCameraActivity.this, MobclickAgentConstant.CARDSCANACTIVITY_SCANTIPS);
                    //showDlgExplain();
                    //TODO 拍照说明
//                    ActivityHelper.switchTo(AddVCardScanActivity.this, ExplainCameraActivity.class);
                    break;
                case R.id.imv_act_card_camera_light_single:
//                    MobclickAgentHelper.onEvent(AddVCardCameraActivity.this, MobclickAgentConstant.CARDSCANACTIVITY_FLASHLIGHTSINGLE);
                    // 闪光灯
                    v.setVisibility(View.GONE);
                    mLilLight.setVisibility(View.VISIBLE);
                    break;
                case R.id.imv_act_card_camera_light_auto:
                    // 闪光灯自动
//                    MobclickAgentHelper.onEvent(AddVCardCameraActivity.this, MobclickAgentConstant.CARDSCANACTIVITY_FLASHLIGHTAUTO);
                    setCameraFlashAndViewShow(CAMERA_FLASH_MODE_AUTO);
                    setCameraFlashMode(CAMERA_FLASH_MODE_AUTO);
                    break;
                case R.id.imv_act_card_camera_light_close:
                    // 关闭闪光灯
//                    MobclickAgentHelper.onEvent(AddVCardCameraActivity.this, MobclickAgentConstant.CARDSCANACTIVITY_FLASHLIGHTCLOSE);
                    setCameraFlashAndViewShow(CAMERA_FLASH_MODE_OFF);
                    setCameraFlashMode(CAMERA_FLASH_MODE_OFF);
                    break;
                case R.id.imv_act_card_camera_light_default:
                    // 闪光灯
//                    MobclickAgentHelper.onEvent(AddVCardCameraActivity.this, MobclickAgentConstant.CARDSCANACTIVITY_FLASHLIGHTDEFAULT);
                    setCameraFlashAndViewShow(CAMERA_FLASH_MODE_TORCH);
                    setCameraFlashMode(CAMERA_FLASH_MODE_TORCH);
                    break;
                default:
                    break;
            }

        }
    };

    private AddVCardEntity mAddVCardEntity = null;
    private CardEntity mVCard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vcard_scan);
        this.init();
        this.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        this.mCamera = super.getCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void startScanBack() {
        mTxvBottom.setText(R.string.please_scan_vcard_back);
    }

    @Override
    protected SurfaceView setSurfaceView() {
        return this.mSurfaceView;
    }

    @Override
    protected void getPhotoData(byte[] photoData, String photoPath) {
        this.mVCard.setCardImgA(photoPath);
    }

    @Override
    protected void getPhotoBackData(byte[] photoBackData, String photoPath) {
        this.mVCard.setCardImgB(photoPath);
    }

    @Override
    protected void switchToClass() {
        ActivityHelper.switchTo(this, AddVCardScanTrimActivity.class, true);
    }

    @Override
    protected void init() {

//        this.mIntentCode = getIntent().getIntExtra(Constant.IntentSet.INTENT_CODE_NAME, 0);
//        this.isDoubleModel = getIntent().getBooleanExtra(KEY_INTENT_IS_DOUBLE_MODLE, false);
        initUI();

        this.mCamera = super.getCamera();
        super.init();
        //是否有判断过支持闪光灯，无则判断，有则跳过
        if(!sIsJudgeCameraFlash){
            sIsJudgeCameraFlash = true;
            PackageManager pm = this.getPackageManager();
            FeatureInfo[] features = pm.getSystemAvailableFeatures();
            for(FeatureInfo feature : features){
                if(PackageManager.FEATURE_CAMERA_FLASH.equals(feature.name)){
                    sIsSupportCameraFlash = true;
                }
            }
        }
        //支持闪光灯
        if(sIsSupportCameraFlash){
            //TODO 是否需要记录
//			int cameraFlashMode = CurrentUser.getInstance().getCameraFlashMode();
//			if(cameraFlashMode == 0){
////				setCameraFlashMode(CAMERA_FLASH_MODE_AUTO);
//			}else{
//				setCameraFlashMode(cameraFlashMode);
//			}
        }else{
            //不支持闪光灯
            mImvLightSingle.setImageResource(R.mipmap.img_add_vcard_scan_light_close);
            this.mImvLightSingle.setEnabled(false);
        }
    }

    private void initUI(){
        this.mImvCancel = (ImageView) findViewById(R.id.imv_act_card_camera_cancel);
        this.mImvTake = (ImageView) findViewById(R.id.imv_act_card_camera_take);
        this.mImvModelSingle = (ImageView) findViewById(R.id.imv_act_card_camera_model_single);
        this.mImvModelMulti = (ImageView) findViewById(R.id.imv_act_card_camera_model_multi);
        this.mImvExplain = (ImageView) findViewById(R.id.imv_act_card_camera_explain);
        this.mImvLightSingle = (ImageView) findViewById(R.id.imv_act_card_camera_light_single);
        this.mLilLight = (LinearLayout) findViewById(R.id.lil_act_card_camera_light_much);
        this.mImvLightDefault  = (ImageView) findViewById(R.id.imv_act_card_camera_light_default);
        this.mImvLightAuto = (ImageView) findViewById(R.id.imv_act_card_camera_light_auto);
        this.mImvLightClose = (ImageView) findViewById(R.id.imv_act_card_camera_light_close);
        this.mImvModelPrompt = (ImageView) findViewById(R.id.imv_act_card_camera_model_prompt);
//		this.mImvCardFrame = (ImageView) findViewById(R.id.imv_act_card_camera_frame);
        this.mTxvBottom = (TextView) findViewById(R.id.txv_act_card_camera_bottom);
        switchModel(this.isDoubleModel);
        setTwoSideMode(this.isDoubleModel);

        this.mSurfaceView = (SurfaceView) findViewById(R.id.suv_act_card_camera_preview);

        this.mImvCancel.setOnClickListener(this.mOnClickListener);
        this.mImvModelSingle.setOnClickListener(this.mOnClickListener);
        this.mImvModelMulti.setOnClickListener(this.mOnClickListener);
        this.mImvLightSingle.setOnClickListener(this.mOnClickListener);
        this.mImvLightDefault.setOnClickListener(this.mOnClickListener);
        this.mImvLightAuto.setOnClickListener(this.mOnClickListener);
        this.mImvLightClose.setOnClickListener(this.mOnClickListener);
        this.mImvExplain.setOnClickListener(this.mOnClickListener);
        this.mImvTake.setOnClickListener(this.mOnClickListener);
    }

    private void initData(){
        CurrentUser currentUser = CurrentUser.getInstance();
        this.mAddVCardEntity = currentUser.getAddVCardEntity();
        if(Helper.isNull(this.mAddVCardEntity)){
            this.mAddVCardEntity = new AddVCardEntity();
            currentUser.setAddVCardEntity(this.mAddVCardEntity);
        }
        this.mVCard = this.mAddVCardEntity.getVcard();
        if(Helper.isNull(this.mVCard)){
            this.mVCard = new CardEntity();
            this.mAddVCardEntity.setVcard(this.mVCard);
        }
    }
    /**
     * 设置闪光灯
     * @param cameraFlashMode {CAMERA_FLASH_MODE_AUTO, CAMERA_FLASH_MODE_OFF, CAMERA_FLASH_MODE_TORCH}
     */
    private void setCameraFlashMode(int cameraFlashMode){
        if(Helper.isNotNull(this.mCamera) && sIsSupportCameraFlash){
            Camera.Parameters parameters = this.mCamera.getParameters();
            switch(cameraFlashMode){
                case CAMERA_FLASH_MODE_OFF:
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    break;
                case CAMERA_FLASH_MODE_AUTO:
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                    break;
                case CAMERA_FLASH_MODE_TORCH:
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    break;

                default:
                    break;
            }
            try{
                this.mCamera.setParameters(parameters);
            }catch(RuntimeException e){
                e.printStackTrace();
                LogHelper.e(TAG, "相机设置参数失败！！！！！！！！！！！！！！！！！");
            }
        }else{
            LogHelper.d(TAG, "哈哈，你的破手机不支持闪光灯，换了吧");
        }
    }
    /**
     *
     * @param cameraFlashMode
     */
    private void setCameraFlashAndViewShow(int cameraFlashMode){
        if(Helper.isNotNull(this.mLilLight) && Helper.isNotNull(this.mImvLightSingle)){
            this.mLilLight.setVisibility(View.GONE);
            this.mImvLightSingle.setVisibility(View.VISIBLE);
            switch(cameraFlashMode){
                case CAMERA_FLASH_MODE_OFF:
                    this.mImvLightSingle.setImageResource(R.mipmap.img_add_vcard_scan_light_close);
                    break;
                case CAMERA_FLASH_MODE_AUTO:
                    this.mImvLightSingle.setImageResource(R.mipmap.img_add_vcard_scan_light_auto);
                    break;
                case CAMERA_FLASH_MODE_TORCH:
                    this.mImvLightSingle.setImageResource(R.mipmap.img_add_vcard_scan_light_default);
                    break;

                default:
                    break;
            }
        }
    }
    /**
     * 切换模式
     * @param isMulti  是否多张模式
     * @return void
     */
    private void switchModel(boolean isMulti){

        int padBottom =  ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_16dp);
        mImvModelMulti.setBackgroundColor(Color.TRANSPARENT);
        mImvModelSingle.setBackgroundColor(Color.TRANSPARENT);
        mImvModelMulti.setImageResource(R.mipmap.img_add_vcard_scan_model_double_gray);
        mImvModelSingle.setImageResource(R.mipmap.img_add_vcard_scan_model_single_gray);
        mImvModelMulti.setPadding(0, 0, 0, padBottom);

        // 模式图标位置参数
        ViewGroup.MarginLayoutParams singleLp = (ViewGroup.MarginLayoutParams) mImvModelSingle.getLayoutParams();
        ViewGroup.MarginLayoutParams multiLp = (ViewGroup.MarginLayoutParams) mImvModelMulti.getLayoutParams();
        singleLp.topMargin = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_3dp);
        multiLp.topMargin =  ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_49dp);

        if(isMulti){
            mImvModelMulti.setBackgroundResource(R.mipmap.bg_add_vcard_scan_model);
            mImvModelMulti.setImageResource(R.mipmap.img_add_vcard_scan_model_double_colour);
            mImvModelPrompt.setImageResource(R.mipmap.img_add_vcard_scan_txt_double);
            mTxvBottom.setText(R.string.please_scan_vcard_front);
            multiLp.topMargin =  ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_51dp);
        }else{
            mImvModelSingle.setBackgroundResource(R.mipmap.bg_add_vcard_scan_model);
            mImvModelSingle.setImageResource(R.mipmap.img_add_vcard_scan_model_single_colour);
            mImvModelPrompt.setImageResource(R.mipmap.img_add_vcard_scan_txt_single);
            mImvModelSingle.setPadding(0, 0, 0, padBottom);
            mTxvBottom.setText(R.string.please_scan_vcard);
            singleLp.topMargin = 0;
        }
        mImvModelMulti.setLayoutParams(multiLp);
        mImvModelSingle.setLayoutParams(singleLp);

        mImvModelPrompt.setVisibility(View.VISIBLE);
        this.mHandler.sendEmptyMessageDelayed(WHAT_HINT_GONE, 2000);
    }

}
