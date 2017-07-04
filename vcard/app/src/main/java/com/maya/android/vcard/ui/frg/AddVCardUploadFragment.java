package com.maya.android.vcard.ui.frg;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 添加名片：本地名片上传
 */
public class AddVCardUploadFragment extends BaseFragment {

    private static final String TAG = AddVCardUploadFragment.class.getSimpleName();

    /** 需要上传的名片类型 **/
    public static final String ADD_UPLOAD_VCARD_FORM = "ADD_UPLOAD_VCARD_FORM";

    /** 正面名片旋转角度 */
    private int mCardFrontRotate = 0;
    /** 反面旋转角度 */
    private int mCardBackRotate = 0;
    /** 名片正面路径 **/
    private String mPhotoPathFront;
    /** 名片反面路径 **/
    private String mPhotoPathVerso;
    /** 名片请求图库的requestcode 正面**/
    private static final int REQ_CODE_NORMAL_FRONT = 11;
    /** 名片请求图库的requestcode 正面**/
    private static final int REQ_CODE_NORMAL_VERSO = 12;
    private int sDetailContentHeight;
    private RelativeLayout mRelFront, mRelVerso;
    private ImageView mImvShowVcardFront, mImvShowVcardVerso;
    private ImageButton mImbRotateFront, mImbRotateVerso;
    private Button mBtnSubmit;
    private TextView mTxvVCardFront, mTxvVCardVerso;
    private ImageView mImvFrontDivider, mImvVersoDivider;
    private CustomDialogFragment mDialogFragmentRecognitionCard;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rel_vcard_front:
                    //上传名片正面
                    Intent mImvFrontPicture = new Intent(Intent.ACTION_GET_CONTENT);
                    mImvFrontPicture.setType(Constants.TakePicture.SELECT_IMAGE_TYPE);
                    startActivityForResult(mImvFrontPicture, REQ_CODE_NORMAL_FRONT);
                    break;
                case R.id.rel_vcard_verso:
                    //上传名片反面
                    if(ResourceHelper.isEmpty(mPhotoPathFront)){

                    }else{
                        Intent mIntentPicture = new Intent(Intent.ACTION_GET_CONTENT);
                        mIntentPicture.setType(Constants.TakePicture.SELECT_IMAGE_TYPE);
                        startActivityForResult(mIntentPicture, REQ_CODE_NORMAL_VERSO);
                    }

                    break;
                case R.id.imb_rotate_picture_front:
                    //旋转名片正面
                    if(ResourceHelper.isNotEmpty(mPhotoPathFront)){
                        BitmapDrawable bmpDrw = (BitmapDrawable) mImvShowVcardFront.getBackground();
                        int rotate = 90;
                        mCardFrontRotate += rotate;
                        Drawable newDrw = drawableRotate(bmpDrw, rotate);
                        mImvShowVcardFront.setBackgroundDrawable(newDrw);
                    }
                    break;
                case R.id.imb_rotate_picture_verso:
                    //旋转名片方面
                    if(ResourceHelper.isNotEmpty(mPhotoPathVerso)){
                        BitmapDrawable bmpDrw = (BitmapDrawable) mImvShowVcardVerso.getBackground();
                        int rotate = 90;
                        mCardBackRotate += rotate;
                        Drawable newDrw = drawableRotate(bmpDrw, rotate);
                        mImvShowVcardVerso.setBackgroundDrawable(newDrw);
                    }
                    break;
                case R.id.btn_submit:
                    //名片识别
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
                    recognitionCard();
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Activity.RESULT_OK == resultCode){
            String selectPicPath = ResourceHelper.getChoosePhotoPath(data, getActivity());
            if(ResourceHelper.isEmpty(selectPicPath) ){
                return;
            }
            showPhoto(selectPicPath, requestCode);
            switch (requestCode){
                case REQ_CODE_NORMAL_FRONT:
                    mPhotoPathFront = selectPicPath;
                    break;
                case REQ_CODE_NORMAL_VERSO:
                    mPhotoPathVerso = selectPicPath;
                    break;
            }
            LogHelper.d(TAG, "本地图片地址：" + selectPicPath);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_vcard_upload, container, false);
        this.mRelFront = (RelativeLayout) view.findViewById(R.id.rel_vcard_front);
        this.mRelVerso = (RelativeLayout) view.findViewById(R.id.rel_vcard_verso);
        this.mImvShowVcardFront = (ImageView) view.findViewById(R.id.imv_show_vcard_front);
        this.mImvShowVcardVerso = (ImageView) view.findViewById(R.id.imv_show_vcard_verso);
        this.mImbRotateFront = (ImageButton) view.findViewById(R.id.imb_rotate_picture_front);
        this.mImbRotateVerso = (ImageButton) view.findViewById(R.id.imb_rotate_picture_verso);
        this.mTxvVCardFront = (TextView) view.findViewById(R.id.txv_upload_vcard_front);
        this.mTxvVCardVerso = (TextView) view.findViewById(R.id.txv_upload_vcard_verso);
        this.mImvFrontDivider = (ImageView) view.findViewById(R.id.view_front_divier);
        this.mImvVersoDivider = (ImageView) view.findViewById(R.id.view_verso_divider);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mRelFront.setOnClickListener(this.mOnClickListener);
        this.mRelVerso.setOnClickListener(this.mOnClickListener);
        this.mImbRotateFront.setOnClickListener(this.mOnClickListener);
        this.mImbRotateVerso.setOnClickListener(this.mOnClickListener);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTitle(R.string.uplod_vcard, false);
        this.sDetailContentHeight = ActivityHelper.getScreenHeight() - ResourceHelper.getDp2PxFromResouce( R.dimen.dimen_50dp) - ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_75dp);
//        selectUploadTypeVCard(Constants.Card.CARD_FORM_9054);//上传名片标准
//        selectUploadTypeVCard(Constants.Card.CARD_FORM_9054);//上传名片异形
//        selectUploadTypeVCard(Constants.Card.CARD_FORM_9094);//上传名片折叠
        int addUploadVCardForm = Constants.Card.CARD_FORM_9054;
        Bundle arg = getArguments();
        if(Helper.isNotNull(arg)){
            addUploadVCardForm = arg.getInt(ADD_UPLOAD_VCARD_FORM, Constants.Card.CARD_FORM_9054);
        }
        this.selectUploadTypeVCard(addUploadVCardForm);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.Card.CARD_FORM_9054:
                    //标准模板
                    mImvFrontDivider.setVisibility(View.GONE);
                    mImvVersoDivider.setVisibility(View.GONE);
                    setLayoutparam(R.dimen.dimen_41dp, R.dimen.dimen_15dp, R.dimen.dimen_3dp, R.dimen.dimen_30dp, Constants.ScaleButton.UPLOAD_VCARD_SCALE_STANDARD_TEMPLATE_HEIGHT_BUTTON, Constants.ScaleButton.UPLOAD_VCARD_SCALE_STANDARD_TEMPLATE_TOP_BUTTON);
                    break;
                case Constants.Card.CARD_FORM_9045:
                    //异形模板
                    mImvFrontDivider.setVisibility(View.GONE);
                    mImvVersoDivider.setVisibility(View.GONE);
                    setLayoutparam(R.dimen.dimen_42dp, R.dimen.dimen_27dp, R.dimen.dimen_15dp, R.dimen.dimen_30dp, Constants.ScaleButton.UPLOAD_VCARD_SCALE_HETEROMORPHISM_TEMPLATE_HEIGHT_BUTTON, Constants.ScaleButton.UPLOAD_VCARD_SCALE_HETEROMORPHISM_TEMPLATE_TOP_BUTTON);
                     break;
                case  Constants.Card.CARD_FORM_9094:
                    //折叠模板
                    mImvFrontDivider.setVisibility(View.VISIBLE);
                    mImvVersoDivider.setVisibility(View.VISIBLE);
                    setLayoutparam(R.dimen.dimen_79dp, R.dimen.dimen_5dp, R.dimen.dimen_2dp, R.dimen.dimen_9dp, Constants.ScaleButton.UPLOAD_VCARD_SCALE_FOLD_TEMPLATE_HEIGHT_BUTTON, Constants.ScaleButton.UPLOAD_VCARD_SCALE_FOLD_TEMPLATE_TOP_BUTTON);
                    break;
            }

        }
    };

    /**
     * 选择模板类型
     * @param what 模板型号
     */
    private void selectUploadTypeVCard(int what){
        Message msg = new Message();
        msg.what = what;
        mHandler.sendMessage(msg);
    }

    /**
     *动态设置控件属性
     * @param leftandrightMargin 左右间距
     * @param topMarginRel rel上间距
     * @param topMarginTxv txv上间距
     * @param bottomMarginTxv txv下间距
     * @param mImgHeightSize 图片长度比
     * @param topRelSize 上间距离高度比
     */
    private void setLayoutparam(int leftandrightMargin, int topMarginRel, int topMarginTxv, int bottomMarginTxv, float mImgHeightSize, float topRelSize){
        int mHeight = (int)(ActivityHelper.getScreenHeight() * mImgHeightSize);
        int top = (this.sDetailContentHeight - (int)(ActivityHelper.getScreenHeight() * topRelSize))/2;
        RelativeLayout.LayoutParams lpRelFront = (RelativeLayout.LayoutParams) this.mRelFront.getLayoutParams();
        lpRelFront.topMargin = top;
        lpRelFront.leftMargin = ResourceHelper.getDp2PxFromResouce(leftandrightMargin);
        lpRelFront.rightMargin = ResourceHelper.getDp2PxFromResouce(leftandrightMargin);
        this.mRelFront.setLayoutParams(lpRelFront);

        RelativeLayout.LayoutParams lpShowFront = (RelativeLayout.LayoutParams) this.mImvShowVcardFront.getLayoutParams();
        lpShowFront.height = mHeight;
        this.mImvShowVcardFront.setLayoutParams(lpShowFront);

        RelativeLayout.LayoutParams lpRelVerso = (RelativeLayout.LayoutParams) this.mRelVerso.getLayoutParams();
        lpRelVerso.topMargin = ResourceHelper.getDp2PxFromResouce(topMarginRel);
        lpRelVerso.leftMargin = ResourceHelper.getDp2PxFromResouce(leftandrightMargin);
        lpRelVerso.rightMargin = ResourceHelper.getDp2PxFromResouce(leftandrightMargin);
        this.mRelVerso.setLayoutParams(lpRelVerso);


        RelativeLayout.LayoutParams lpShowVerso = (RelativeLayout.LayoutParams) this.mImvShowVcardVerso.getLayoutParams();
        lpShowVerso.height = mHeight;
        this.mImvShowVcardVerso.setLayoutParams(lpShowVerso);

        RelativeLayout.LayoutParams lpTxvVcardFront = (RelativeLayout.LayoutParams) this.mTxvVCardFront.getLayoutParams();
        lpTxvVcardFront.topMargin = ResourceHelper.getDp2PxFromResouce(topMarginTxv);
        this.mTxvVCardFront.setLayoutParams(lpTxvVcardFront);

        RelativeLayout.LayoutParams lpTxvVcardVerso = (RelativeLayout.LayoutParams) this.mTxvVCardVerso.getLayoutParams();
        lpTxvVcardVerso.bottomMargin = ResourceHelper.getDp2PxFromResouce(bottomMarginTxv);
        this.mTxvVCardVerso.setLayoutParams(lpTxvVcardVerso);
    }

    /**
     * 显示选择的图片
     * @param selectPicPath
     * @param requestCode
     * @return void
     */
    public void showPhoto(String selectPicPath, int requestCode){
//			Bitmap bmp = DesignData.bitmapZoomHorizontal(selectPicPath);
        //取到放缩后的bitmap
        Bitmap bmp = bitmapZoomPath(selectPicPath);
        //对bitmap进行旋转90度
        if(Helper.isNotNull(bmp)){
            if(bmp.getWidth() < bmp.getHeight()){
                int rotate = 90;
                bmp = bitmapRotate(bmp, rotate);
                Drawable selectDraw = new BitmapDrawable(getActivity().getResources(), bmp);
                switch (requestCode) {
                    case REQ_CODE_NORMAL_FRONT:
                        mCardFrontRotate += rotate;
                        this.mImvShowVcardFront.setBackgroundDrawable(selectDraw);
                        break;
                    case REQ_CODE_NORMAL_VERSO:
                        mCardBackRotate += rotate;
                        this.mImvShowVcardVerso.setBackgroundDrawable(selectDraw);
                        break;
                }
            }
        }

    }
    /**
     * 缩放图片
     *
     * @param btimapPath
     * @return bitmap
     */
    private Bitmap bitmapZoomPath(String btimapPath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(btimapPath, options);
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        int scaleWidth = outWidth/ActivityHelper.getScreenWidth();
        int scaleHeight = outHeight/ActivityHelper.getScreenHeight();
        int scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        try {
            bm = BitmapFactory.decodeFile(btimapPath, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            options.inSampleSize *= 2;
            try {
                bm = BitmapFactory.decodeFile(btimapPath, options);
            } catch (OutOfMemoryError e2) {
                e2.printStackTrace();
            }
        }
        return bm;
    }
    /**
     * 对图片进行旋转的角度
     * @param bm
     * @param rotate
     * @return
     */
    public Bitmap bitmapRotate(Bitmap bm, int rotate){
        Bitmap result = null;
        if(Helper.isNotNull(bm)){
            Matrix matrix = new Matrix();
            matrix.reset();
            matrix.postRotate(rotate);
            result = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            if(Helper.isNotNull(result) && result != bm){
                bm.recycle();
                bm = null;
            }
        }
        return result;
    }

    /**
     * 对Drawable进行旋转
     * @param drawable
     * @param rotate
     * @return
     */
    public Drawable drawableRotate(Drawable drawable, int rotate){
        Drawable result = null;
        if(Helper.isNotNull(drawable)){
            if(0 != rotate){
                result = new BitmapDrawable(getResources(), bitmapRotate(((BitmapDrawable)drawable).getBitmap(), rotate));
            }else{
                result = drawable;
            }
        }
        return result;
    }

    /**
     * 识别名片
     */
    private void recognitionCard(){
        if(ResourceHelper.isEmpty(this.mPhotoPathFront)){
            return;
        }

        if(ResourceHelper.isEmpty(this.mPhotoPathVerso)){
            showDialogFragmentRecognitionCard();
        }

        //TODO 识别正反面名片
    }

    /**
     * 只上传名片正面对话框
     */
    private void showDialogFragmentRecognitionCard(){
        if(ResourceHelper.isNull(this.mDialogFragmentRecognitionCard)){
            CustomDialogFragment.DialogFragmentInterface onClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        //TODO 识别正面名片
                    }
                }
            };

            this.mDialogFragmentRecognitionCard = DialogFragmentHelper.showCustomDialogFragment(R.string.uplod_vcard, R.string.confirm_save_only_card_front, R.string.frg_text_cancel, R.string.frg_text_ok, onClick);
        }
        this.mDialogFragmentRecognitionCard.show(getFragmentManager(),"mDialogFragmentRecognitionCard");
    }

}
