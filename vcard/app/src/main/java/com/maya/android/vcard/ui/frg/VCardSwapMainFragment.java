package com.maya.android.vcard.ui.frg;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.json.VCardSwapJsonEntity;
import com.maya.android.vcard.entity.result.SwapCardResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.ui.act.VCardSwapMainActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.AudioRecordHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 名片交换
 */
public class VCardSwapMainFragment extends BaseFragment {
    private static final String TAG = VCardSwapMainFragment.class.getSimpleName();
    /** 倒数计时 **/
    private static final int SWAP_COUNT_DOWN_NUM = 10;
    /**开始名片交换**/
    private static final int WHAT_HANDLER_START_SWAP = 10001;
    /**图片变换**/
    private static final int WHAT_HANDLER_CHANGE_IMAGE = 10002;
    /** 偏角 **/
    private float mDeflection;
    /** 是否正在交换 **/
    private boolean isDoingSwapCard = false;
    /** 是否交换成功 **/
    private boolean isSuccessSwap = false;
    /** 是否强制取消 **/
    private boolean isForceCancle = false;
    /** Sensor传感管理器  */
    private SensorManager mSensorManager;
    private TextView mTxvCountTime;
    private ImageView mImvFace;
    private ImageButton mImbClose;
    private int  mFacePosition = 0;//记录当前脸部编号
    private long mVCardId = 0;
    private int mSwapWay = Constants.SwapCard.SWAP_CARD_WAY_TO_PADDLE;
    private SwapCountDownTimer mSwapCountDownTimer;
    private String mSwapVCardListStr = null;
    /** 微片脸部Icon集合 */
    private int[] imvFace = { R.mipmap.img_vcard_face_sign, R.mipmap.img_vcard_face_sign_01, R.mipmap.img_vcard_face_sign_02, R.mipmap.img_vcard_face_sign_03, R.mipmap.img_vcard_face_sign_04,
            R.mipmap.img_vcard_face_sign_05, R.mipmap.img_vcard_face_sign_06, R.mipmap.img_vcard_face_sign_07, R.mipmap.img_vcard_face_sign_08, R.mipmap.img_vcard_face_sign_09, R.mipmap.img_vcard_face_sign_10,
            R.mipmap.img_vcard_face_sign_11, R.mipmap.img_vcard_face_sign_12,R.mipmap.img_vcard_face_sign_13, R.mipmap.img_vcard_face_sign_14, R.mipmap.img_vcard_face_sign_15, R.mipmap.img_vcard_face_sign_16,
            R.mipmap.img_vcard_face_sign_17 };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imb_swap_close:
                    // 关闭名片交换
                    //getActivity().setResult(Activity.RESULT_CANCELED);
                    mFragmentInteractionImpl.onActivityFinish();
                    break;
            }
        }
    };
    /**
     * 传感监听
     */
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            int sensorType = event.sensor.getType();
            float[] values = event.values;
            if(values.length > 0 && Sensor.TYPE_ORIENTATION == sensorType){
                mDeflection = (values[0] * 1.0f + 720 )%360;
//			     Log.i(TAG, "偏角:" + mDeflection);
                if(!isDoingSwapCard){
                    isDoingSwapCard = true;
                    //sendSwapRequest();
                }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WHAT_HANDLER_START_SWAP:
                    //开始交换名片
                    if(!isForceCancle){
                        mSwapCountDownTimer = new SwapCountDownTimer(SWAP_COUNT_DOWN_NUM * 1000, 1000);
                        mSwapCountDownTimer.start();
                        sendSwapRequest();
                        try{
                            // 设置声音
                            int soundId = ResourceHelper.getSwapCardResourceId(CurrentUser.getInstance().getSetting().getSwapCardSoundResourceId());
                            LogHelper.d(TAG, "soundId:" + soundId);
                            if(soundId != 0){
                                AudioRecordHelper.setRescource2MediaPlay(soundId);
                                AudioRecordHelper.play();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
                case WHAT_HANDLER_CHANGE_IMAGE:
                    //图片换换
                    try {
                        if(mFacePosition >= imvFace.length){
                            mFacePosition = 0;
                        }
                        int resId = imvFace[mFacePosition];
                        mImvFace.setImageDrawable(getResources().getDrawable(resId));
                        mFacePosition ++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessageDelayed(WHAT_HANDLER_CHANGE_IMAGE, 500);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleAction.setActivityTopVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vcard_swap_main, container, false);
        this.mTxvCountTime = (TextView) view.findViewById(R.id.txv_swap_card_count_timer);
        this.mImvFace = (ImageView) view.findViewById(R.id.imv_swap_face);
        this.mImbClose = (ImageButton) view.findViewById(R.id.imb_swap_close);
        this.mImbClose.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        this.mTxvCountTime.setText(String.valueOf(SWAP_COUNT_DOWN_NUM));
        this.mHandler.sendEmptyMessageDelayed(WHAT_HANDLER_START_SWAP, 1000);
        this.mHandler.sendEmptyMessageDelayed(WHAT_HANDLER_CHANGE_IMAGE, 500);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mSensorManager.unregisterListener(this.mSensorEventListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.isForceCancle = true;
        if(Helper.isNotNull(this.mSwapCountDownTimer)){
            this.mSwapCountDownTimer.cancel();
        }
        //停掉音乐
        AudioRecordHelper.stopMediaPlay();
    }

    @Override
    protected void onBackPressed() {
        super.onBackPressed();
        this.isForceCancle = true;
        if(Helper.isNotNull(this.mSwapCountDownTimer)){
            this.mSwapCountDownTimer.cancel();
        }
        //停掉音乐
        AudioRecordHelper.stopMediaPlay();
        getActivity().setResult(Activity.RESULT_CANCELED);
    }

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(tag == Constants.CommandRequestTag.CMD_REQUEST_SWAP_CARD){
            if(!super.onCommandCallback2(tag, commandResult, objects)){
                HashMap<Long, SwapCardResultEntity> swapCardMap = JsonHelper.fromJson(commandResult, new TypeToken<HashMap<Long, SwapCardResultEntity>>(){}.getType());
                if(Helper.isNotNull(swapCardMap)){
                    LogHelper.d(TAG, "交换结果：" + swapCardMap);
                    if(Helper.isNotNull(swapCardMap)){
                        swapCardMap.remove(this.mVCardId);
                        if(swapCardMap.size() > 0){
                            mSwapVCardListStr = JsonHelper.toJson(swapCardMap);
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 发送名片交换请求
     */
    private void sendSwapRequest(){
        //发送名片交换请求
        Bundle args = getArguments();
        if(Helper.isNotNull(args)){
            this.mVCardId = args.getLong(Constants.IntentSet.INTENT_KEY_VCARD_ID, 0);
            this.mSwapWay = args.getInt(Constants.IntentSet.INTENT_KEY_VCARD_SWAP_WAY, Constants.SwapCard.SWAP_CARD_WAY_TO_PADDLE);
        }
        LogHelper.d(TAG, "cardId:" + this.mVCardId + " swapWay:" + this.mSwapWay);
        VCardSwapJsonEntity jsonEntity = new VCardSwapJsonEntity(this.mVCardId, this.mSwapWay, this.mDeflection);
        URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
        if(Helper.isNotNull(urlEntity)){
            String urlCardSwap = ProjectHelper.fillSwapRequestURL(urlEntity.getCardSwap());
            String json = JsonHelper.toJson(jsonEntity, VCardSwapJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_SWAP_CARD, urlCardSwap, json, this);
        }
    }

    private void switchToClass(){
        // 结束名片交换 跳转
        if(ResourceHelper.isNotEmpty(this.mSwapVCardListStr)){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, VCardSwapResultMultiFragment.class.getName());
            intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
            bundle.putLong(Constants.IntentSet.INTENT_KEY_VCARD_ID, this.mVCardId);
            bundle.putInt(Constants.IntentSet.INTENT_KEY_VCARD_SWAP_WAY, this.mSwapWay);
            bundle.putString(Constants.IntentSet.INTENT_KEY_VCARD_SWAP_CARD_LIST, this.mSwapVCardListStr);
            mActivitySwitchTo.switchTo(VCardSwapMainActivity.class, intent);
        }else{
            getActivity().setResult(Activity.RESULT_FIRST_USER);
        }
        mFragmentInteractionImpl.onActivityFinish();
    }

    private class SwapCountDownTimer extends CountDownTimer{
        long interval;
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive {@link #onTick(long)} callbacks.
         */
        public SwapCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.interval = countDownInterval;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTxvCountTime.setText(String.valueOf((int) (millisUntilFinished / this.interval)));
        }

        @Override
        public void onFinish() {
            mTxvCountTime.setText("0");
            mHandler.removeMessages(WHAT_HANDLER_CHANGE_IMAGE);
            // 进行跳转
            switchToClass();
        }
    }
}
