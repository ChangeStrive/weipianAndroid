package com.maya.android.vcard.ui.act;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.ocr.AbstractVcardTrimAndRecognitionActivity;
import com.maya.android.ocr.entity.OcrRecogResultEntitiy;
import com.maya.android.ocr.util.OcrImageHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.AddVCardEntity;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.umeng.analytics.MobclickAgent;

import eu.janmuller.android.cropimage.CropImageView;

public class AddVCardScanTrimActivity extends AbstractVcardTrimAndRecognitionActivity {

    private static final String TAG = AddVCardScanTrimActivity.class.getSimpleName();

    private static final int TIME_MASK_TRANS = 3000;
    private static final int WHAT_HANDLER_SWITCH_TO_COUNT = 1000;
    private static final int WHAT_HANDLER_SWITCH_TO = 1001;

    private ImageView mImvCard,mImvMask;
    private TextView mTxvTitle, mCutTxvTitle;
    private RelativeLayout mRelFrame;
    private String mImgPathA,mImgPathB;
    private boolean isRecognitionFrontSuccess = true;
    private boolean isRecognitionBackSuccess = true;
    private boolean isRecognitionFinish = false;
    private CropImageView mCropImageView;
    private RelativeLayout mRelCropBitmap, mRelRecogBitmap;
    private int mSaveBitmapCount = 1;
    /** 单面模式或双面正面的扫描结果 */
    private OcrRecogResultEntitiy mOcrItems;
    /** 双面模式背面的扫描结果 */
    private OcrRecogResultEntitiy mOcrBackItems;
    private boolean isSwitchTo = false;
    private Bitmap mBitmapA, mBitmapB;
    /** 名片正面被旋转几度 **/
    private int mCardFrontRotate = 0;
    /** 名片反面被旋转几度  **/
    private int mCardBackRotate = 0;
    /** 名片是否需要裁剪 */
    private boolean isCardNeedCrop = true;
    /** 识别失败的对话框显示 */
    private boolean isDialogShowing;
    /**
     * 1：代表单面模式</br>
     * 2：代表双面模式</br>
     * (don't touch me)</br>
     */
    private int mImageMaskFinishCount = 0;
    private int mRelFrameWidth = 0;
    private int mRelFrameHeight = 0;
    private int mSwitchToCount = 0;
    /** 名片方向 **/
    private int mCardAOrientation = Constants.Card.CARD_ORIENTATION_LANDSCAPE;
    /** 识别失败时 选择的操作值  **/
    private int mRecognizeFailOptPosition = 0;
    /** 名片类型 */
    private int mCardForm;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case WHAT_HANDLER_SWITCH_TO_COUNT:
                    mSwitchToCount ++;
                    if(getTwoSideMode()){
                        if(mSwitchToCount % 4 == 0){
//						mHandler.sendEmptyMessage(WHAT_HANDLER_SWITCH_TO);
                            mSwitchToHandler.sendEmptyMessage(WHAT_HANDLER_SWITCH_TO);
                        }
                    }else{
                        if(mSwitchToCount % 2 == 0){
                            mSwitchToHandler.sendEmptyMessage(WHAT_HANDLER_SWITCH_TO);
                        }
                    }
                    break;
            }
        }

    };
    @SuppressLint("HandlerLeak")
    private Handler mSwitchToHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case WHAT_HANDLER_SWITCH_TO:
                    if(!isSwitchTo){
                        switchToClass();
                        isSwitchTo = true;
                    }
                    break;
            }
        }

    };
    /** 点击事件监听 **/
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_act_card_cut_next:
                    nextStep();
                    break;
                case R.id.btn_act_card_cut_scan:
                    ActivityHelper.switchTo(AddVCardScanTrimActivity.this, AddVCardScanActivity.class, true);
//                    Intent intent = getIntent();
//                    if(Helper.isNull(intent)){
//                        intent = new Intent();
//                        intent.putExtra(Constants.IntentSet.INTENT_CODE_NAME, mIntentCode);
//                    }
//                    ActivityHelper.switchTo(AddVCardTrimActivity.this, AddVCardCameraActivity.class, intent, true);
                    break;
            }
        }
    };
    private AddVCardEntity mAddVCardEntity = CurrentUser.getInstance().getAddVCardEntity();
    private CardEntity mVCard = this.mAddVCardEntity.getVcard();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vcard_scan_trim);
        this.initUI();
        this.initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.mSaveBitmapCount = 1;
        this.mOcrItems = null;
        this.mOcrBackItems = null;
        this.isSwitchTo = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected CropImageView setCropImageView() {
        return this.mCropImageView;
    }

    @Override
    protected void vcardRecognitionSuccess(OcrRecogResultEntitiy entity) {
        if(this.mImageMaskFinishCount < 2){
            this.mOcrItems = entity;
            this.mImageMaskFinishCount += 2;
        }else{
            this.mOcrBackItems = entity;
        }
        this.isRecognitionFinish = true;
        this.mHandler.sendEmptyMessage(WHAT_HANDLER_SWITCH_TO_COUNT);
    }

    @Override
    protected void vcardRecognitionFail() {
        if(this.isRecognitionFrontSuccess && Helper.isNull(this.mOcrItems)){
            this.isRecognitionFrontSuccess = false;
        }else if(getTwoSideMode()){
            this.isRecognitionBackSuccess = false;
        }
        this.isRecognitionFinish = true;
        this.mHandler.sendEmptyMessage(WHAT_HANDLER_SWITCH_TO_COUNT);
    }

    @Override
    protected void startRecognitionBack() {
        this.mRelCropBitmap.setVisibility(View.GONE);
        this.mRelRecogBitmap.setVisibility(View.VISIBLE);
        //展示图片框
//		this.mRelFrame.setBackgroundResource(R.drawable.bg_act_card_trim_frame);
        this.mTxvTitle.setText(R.string.vcard_trim_reading_black);
        this.isRecognitionFinish = false;
        this.mImvMask.setVisibility(View.GONE);
        this.startBitmapRecognition(this.mBitmapB, this.mCardBackRotate);
        this.mSwitchToHandler.sendEmptyMessageDelayed(WHAT_HANDLER_SWITCH_TO, 5000);
    }

    protected void switchToClass() {
        if(!this.isRecognitionFinish){
            return;
        }
        //判断失败
        if((!getTwoSideMode() && !isRecognitionFrontSuccess) || (!isRecognitionFrontSuccess && !isRecognitionBackSuccess)){
            if(!isDialogShowing){
                isDialogShowing = true;
                // 识别失败，弹出对话框
                showDlgRecognizeFail();
            }
            return;
        }
        Intent toEditIntent = getIntent();
        Class<?> toActivity = null;
//        if(mIntentCode == Constants.IntentSet.INTENT_CODE_CARDCASE){
////			toActivity = CardcaseLocalAddCardActivity.class;
//            toActivity = NewCardInfomationEditActivity.class;
//        }else{
////			toActivity = MyCardDetailEditActivity.class;
//            toActivity = NewCardInfomationEditActivity.class;
//        }
//        toEditIntent.putExtra(Constants.Card.INTENT_KEY_CARD_FORM, this.mCardForm);
//        toEditIntent.putExtra(Constants.IntentSet.INTENT_CODE_NAME, mIntentCode);
//        toEditIntent.putExtra(Constants.Card.INTENT_KEY_CARD_ORIENTATION, mCardAOrientation);
//        toEditIntent.putExtra(Constants.IntentSet.INTENT_KEY_CARD_SCAN_PATH_FRONT, mImgPathA);
//        toEditIntent.putExtra(Constants.IntentSet.INTENT_KEY_CARD_SCAN_PATH_BACK, mImgPathB);
//        toEditIntent.putExtra(Constants.IntentSet.INTENT_KEY_VCARD_OCR_RESULT, mOcrItems);
//        if(this.getTwoSideMode()){
//            toEditIntent.putExtra(Constants.IntentSet.INTENT_KEY_VCARD_OCR_RESULT_BACK, this.mOcrBackItems);
//        }
//        ActivityHelper.switchTo(this, toActivity, toEditIntent, true);
    }

    private void initUI(){
        this.mImvCard = (ImageView) findViewById(R.id.imv_act_card_trim_card);
        this.mImvMask = (ImageView) findViewById(R.id.imv_act_card_trim_mask);
        this.mRelFrame = (RelativeLayout) findViewById(R.id.rel_act_card_trim_cut);

        this.mTxvTitle = (TextView) findViewById(R.id.txv_act_card_trim_title);
        this.mCutTxvTitle = (TextView) findViewById(R.id.txv_act_card_cut_title);

        this.mCropImageView = (CropImageView) findViewById(R.id.act_crop_image_view);
        this.mRelCropBitmap = (RelativeLayout) findViewById(R.id.rel_act_card_trim_crop);
        this.mRelRecogBitmap = (RelativeLayout) findViewById(R.id.rel_act_card_trim_recog);
        Button btnNext = (Button) findViewById(R.id.btn_act_card_cut_next);
        Button btnReScan = (Button) findViewById(R.id.btn_act_card_cut_scan);
        btnNext.setOnClickListener(this.mOnClickListener);
        btnReScan.setOnClickListener(this.mOnClickListener);
    }
    /**
     * 初始化数据
     */
    private void initData(){
//        Intent intent = getIntent();
//        mImgPathA = intent.getStringExtra(Constants.IntentSet.INTENT_KEY_CARD_SCAN_PATH_FRONT);
//        mImgPathB = intent.getStringExtra(Constants.IntentSet.INTENT_KEY_CARD_SCAN_PATH_BACK);

//        mImgPicture = intent.getByteArrayExtra(Constants.IntentSet.INTENT_KEY_CARD_SCAN_PATH_FRONT_BYTE);
//        mImgBackPicture = intent.getByteArrayExtra(Constants.IntentSet.INTENT_KEY_CARD_SCAN_PATH_BACK_BYTE);
//
//        this.mCardFrontRotate = intent.getIntExtra(Constants.IntentSet.INTENT_KEY_CARD_SCAN_FRONT_ROTATE, 0);
//        this.mCardBackRotate = intent.getIntExtra(Constants.IntentSet.INTENT_KEY_CARD_SCAN_BACK_ROTATE, 0);
//        this.isCardNeedCrop = getIntent().getBooleanExtra(Constants.IntentSet.INTENT_KEY_IS_CARD_NEED_CROP, true);
//        this.mIntentCode = intent.getIntExtra(Constants.IntentSet.INTENT_CODE_NAME, Constants.IntentSet.INTENT_CODE_ADD_MY_VCARD_BY_SCAN);
        this.mImgPathA = this.mVCard.getCardImgA();
        this.mImgPathB = this.mVCard.getCardImgB();
        this.mCardFrontRotate = this.mAddVCardEntity.getVcardARotate();
        this.mCardBackRotate = this.mAddVCardEntity.getVcardBRotate();
        this.isCardNeedCrop = Constants.Card.CARD_TYPE_UPLOAD != this.mVCard.getCardAType();
        if(Helper.isNull(mImgPathA)){
            mImgPathA = "";
        }else{
            if(Helper.isNull(mImgPathB)){
                mImgPathB = "";
                setTwoSideMode(false);
            }else{
                setTwoSideMode(true);
            }
        }
        if(ResourceHelper.isNotEmpty(this.mImgPathA)){
            setPhotoPath(this.mImgPathA);
        }
        if(ResourceHelper.isNotEmpty(this.mImgPathB)){
            setPhotoBackPath(this.mImgPathB);
        }
        if(getTwoSideMode()){
            this.mTxvTitle.setText(R.string.vcard_trim_reading_front);
            this.mCutTxvTitle.setText(R.string.vcard_cut_double_title_front);
        }
        if(ResourceHelper.isNotEmpty(getPhotoPath())){
            //名牌需要裁剪
            if(this.isCardNeedCrop){
                init(getPhotoPath());
            }else{
                //名片不需要裁剪
                this.mBitmapA = OcrImageHelper.getValidOcrBitmap(getPhotoPath());
                if(getTwoSideMode() && Helper.isNotEmpty(getPhotoBackPath())){
                    this.mBitmapB = OcrImageHelper.getValidOcrBitmap(getPhotoBackPath());
                }
                if(Helper.isNotNull(this.mBitmapA)){
                    this.startBitmapRecognition(this.mBitmapA, this.mCardFrontRotate);
                }
            }
        }
    }

    /**
     * 开始扫描条滑动
     */
    private void startImageMask(){
        this.mImvMask.setVisibility(View.VISIBLE);
        TranslateAnimation transAnim = new TranslateAnimation(0, mRelFrameWidth, 0, 0);
        transAnim.setFillAfter(true);
        transAnim.setRepeatCount(Animation.ABSOLUTE);
        transAnim.setRepeatMode(Animation.REVERSE);
        transAnim.setDuration(TIME_MASK_TRANS);
        //扫描滚动条Listener
        transAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mImageMaskFinishCount++;
                mHandler.sendEmptyMessage(WHAT_HANDLER_SWITCH_TO_COUNT);
                mImvMask.setVisibility(View.GONE);
            }
        });
        this.mImvMask.setAnimation(transAnim);
        transAnim.start();
    }

    private void startBitmapRecognition(Bitmap bitmap, int rotate){

        //计算展示控件的宽高
        if(Helper.isNotNull(bitmap)){
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if(width < height){
                //设置名片为竖向
                this.mCardAOrientation = Constants.Card.CARD_ORIENTATION_PORTRAIT;
            }
        }
        mRelFrameHeight = (int) (ActivityHelper.getScreenWidth() * 0.75);
        if(Constants.Card.CARD_FORM_9094 == this.mCardForm){
            mRelFrameWidth = (int) (mRelFrameHeight * Constants.Card.CARD_RATIO_9094);
        }else if(Constants.Card.CARD_FORM_9045 == this.mCardForm){
            mRelFrameHeight = (int) (ActivityHelper.getScreenWidth() * 0.68);
            mRelFrameWidth = (int) (mRelFrameHeight * Constants.Card.CARD_RATIO_9045);
        }else{
            mRelFrameWidth = (int) ( mRelFrameHeight * Constants.Card.CARD_RATIO_9054);
        }
        ViewGroup.LayoutParams relFrmLp = mRelFrame.getLayoutParams();
        relFrmLp.width = mRelFrameWidth;
        relFrmLp.height = mRelFrameHeight;
        mRelFrame.setLayoutParams(relFrmLp);

        this.mRelCropBitmap.setVisibility(View.GONE);
        this.mRelRecogBitmap.setVisibility(View.VISIBLE);
        super.startRecognition(bitmap);
        startImageMask();
        if(rotate != 0){
            bitmap = bitmapRoat(bitmap, rotate);
        }
        this.mImvCard.setImageBitmap(bitmap);
//		this.mHandler.sendEmptyMessageDelayed(WHAT_HANDLER_SWITCH_TO, 3000);
    }
    /**
     * 弹出识别失败框
     */
    private void showDlgRecognizeFail(){
        //TODO 识别失败
        LogHelper.d(TAG, "识别失败");
//        int dlgWidth = (int)(ActivityHelper.getScreenWidth() * 0.82) ;
//        View viewContent = getLayoutInflater().inflate(R.layout.dlg_recognize_fail, null);
//        RadioButton rabAgain = (RadioButton) viewContent.findViewById(R.id.rab_dlg_recognize_fail_scan_again);
//        if(mIntentCode == Constant.IntentSet.INTENT_CODE_ADD_MY_VCARD_BY_SCAN){
//            rabAgain.setText(R.string.dlg_recognize_fail_scan_again);
//        }else if(mIntentCode == Constant.IntentSet.INTENT_CODE_ADD_MY_VCARD_BY_UPLOAD){
//            rabAgain.setText(R.string.dlg_recognize_fail_upload_again);
//        }
//        TextView txvPrompt = (TextView) viewContent.findViewById(R.id.txv_dlg_recognize_prompt);
//        txvPrompt.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // 提示链接
//                ActivityHelper.switchTo(AddVCardTrimActivity.this, ExplainCameraActivity.class);
//            }
//        });
//        RadioGroup ragSelect = (RadioGroup) viewContent.findViewById(R.id.rag_dlg_recognize_fail_way);
//        ragSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                switch(group.getCheckedRadioButtonId()){
//                    case R.id.rab_dlg_recognize_fail_hand_input:
//                        mRecognizeFailOptPosition = 0;
//                        break;
//                    case R.id.rab_dlg_recognize_fail_scan_again:
//                        mRecognizeFailOptPosition = 1;
//                        break;
//                }
//            }
//        });
//        DialogInterface.OnClickListener dlgOnclick = new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case DialogInterface.BUTTON_POSITIVE:
//                        if(mRecognizeFailOptPosition == 0){
//                            Intent intent = getIntent();
//                            intent.putExtra(Constant.IntentSet.INTENT_CODE_NAME, mIntentCode);
//                            intent.putExtra(Constant.Card.INTENT_KEY_CARD_ORIENTATION, mCardAOrientation);
//                            intent.putExtra(Constant.IntentSet.INTENT_KEY_CARD_SCAN_PATH_FRONT, mImgPathA);
//                            intent.putExtra(Constant.IntentSet.INTENT_KEY_CARD_SCAN_PATH_BACK, mImgPathB);
//                            if(mIntentCode == Constant.IntentSet.INTENT_CODE_CARDCASE){
////							ActivityHelper.switchTo(AddVCardTrimActivity.this, CardcaseLocalAddCardActivity.class, intent, true);
//                                ActivityHelper.switchTo(AddVCardTrimActivity.this, NewCardInfomationEditActivity.class, intent, true);
//                            }else{
////							ActivityHelper.switchTo(AddVCardTrimActivity.this, MyCardDetailEditActivity.class, intent, true);
//                                ActivityHelper.switchTo(AddVCardTrimActivity.this, NewCardInfomationEditActivity.class, intent, true);
//                            }
//
//                            dialog.dismiss();
//                        }else if(mRecognizeFailOptPosition == 1){
//                            Intent intent;
//                            if(Helper.isNotNull(getIntent())){
//                                intent = getIntent();
//                            }else{
//                                intent = new Intent();
//                            }
//                            if(mIntentCode == Constant.IntentSet.INTENT_CODE_ADD_MY_VCARD_BY_SCAN){
//                                intent.putExtra(Constant.IntentSet.INTENT_CODE_NAME, mIntentCode);
////							intent.putExtra(Constant.IntentSet.INTENT_CODE_TWO, mIntentCodeTwo);
//                                ActivityHelper.switchTo(AddVCardTrimActivity.this, AddVCardCameraActivity.class, intent, true);
//                            }else if(mIntentCode == Constant.IntentSet.INTENT_CODE_ADD_MY_VCARD_BY_UPLOAD){
//                                intent.putExtra(Constant.IntentSet.INTENT_CODE_NAME, Constant.IntentSet.INTENT_CODE_NAME);
//                                ActivityHelper.switchTo(AddVCardTrimActivity.this, AddVCardFormatChooseActivity.class, intent, true);
//                            }
//                        }
//                        break;
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        dialog.dismiss();
//                        finish();
////					if(mIntentCodeTwo == Constant.IntentKey.CODE_CARDCASE){
////
////					}else{
////						ActivityHelper.switchTo(CardTrimActivity.this, MainActivity.class,true);
////					}
//                        break;
//                }
//            }
//        };
//        CustomDialog.Builder build = new CustomDialog.Builder(this,true);
//        build.setTitle(R.string.dlg_recognize_fail_title)
//                .setIcon(R.drawable.new_img_dlg_popup)
//                .setContentView(viewContent)
//                .setNegativeButton(R.string.common_cancel, dlgOnclick)
//                .setPositiveButton(R.string.common_ok, dlgOnclick);
//        DialogHelper.showCustomDialog(build, dlgWidth, false).setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                finish();
//            }
//        });
    }

    /**
     * 对图片进行旋转的角度
     * @param bitmap
     * @param rotate
     * @return
     */
    private Bitmap bitmapRoat(Bitmap bitmap, int rotate){
        Bitmap result = null;
        if(Helper.isNotNull(bitmap)){
            Matrix matrix = new Matrix();
            matrix.reset();
            matrix.postRotate(rotate);
            result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            //TODO 是否需要回收？？
        }
        return result;
    }
    /**
     * 下一步
     */
    private void nextStep(){
        if(this.mSaveBitmapCount == 1){

            this.mImgPathA = saveCropImage();
            this.mBitmapA = getCroppedBitmap();
            this.mSaveBitmapCount ++;
//            if(this.mIntentCode == Constants.IntentSet.INTENT_CODE_ADD_MY_VCARD_BY_SCAN){
//                this.mCardForm = ProjectHelper.getVCardForm(mBitmapA);
//            }else{
//                this.mCardForm = getIntent().getIntExtra(Constants.Card.INTENT_KEY_CARD_FORM, Constants.Card.CARD_FORM_9054);
//            }
            if(Constants.Card.CARD_TYPE_SCAN == this.mVCard.getCardAType()){
                this.mCardForm = ProjectHelper.getVCardForm(mBitmapA);
                this.mVCard.setCardAForm(this.mCardForm);
                this.mVCard.setCardBForm(this.mCardForm);
            }
            this.mVCard.setCardImgA(this.mImgPathA);
            //有双面模式，则进行背面裁切；无则进行扫描识别
            if(getTwoSideMode()){
                super.init(mImgPathB);
                this.mCutTxvTitle.setText(R.string.vcard_cut_double_title_back);
            }else{
                startBitmapRecognition(this.mBitmapA, this.mCardFrontRotate);
            }
        }else if(getTwoSideMode() && (this.mSaveBitmapCount == 2)){
            this.mImgPathB = saveCropImage();
            this.mBitmapB = getCroppedBitmap();
            this.mSaveBitmapCount ++;
            this.mVCard.setCardImgB(this.mImgPathB);
            startBitmapRecognition(this.mBitmapA, this.mCardFrontRotate);
        }
    }
}
