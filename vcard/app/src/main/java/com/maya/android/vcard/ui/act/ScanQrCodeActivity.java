package com.maya.android.vcard.ui.act;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.Result;
import com.maya.android.extra.zxing.scan.AbstractScanActivity;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.TitleMoreMenuLsvIconEntity;
import com.maya.android.vcard.ui.widget.TitleMoreMenuPopView;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.util.TitleMoreMenuPopHelper;

import java.util.ArrayList;

public class ScanQrCodeActivity extends AbstractScanActivity {
    /** 显示二维码名片是否需要重新跳转到二维码名片页面 **/
    public static final String IS_NEED_SWITCH_TO_SHOW_QR_CODE_FRG = "IS_NEED_SWITCH_TO_SHOW_QR_CODE_FRG";
    private TextView mTxvTopTitleLeft, mTxvQrcodeScan;
    private ImageView mImvTopLeft, mImvTopRight;
    private Button mBtnQrcode;
    private int mImvWidth;
    private int mImvScanTop = 125;
    private boolean isNeedSwithchToShowQrCodeFrg = false;
    private TitleMoreMenuPopView mTitleMoreMenuPop;
    private ArrayList<Integer> unEnablePositionList = new ArrayList<Integer>();
    private View.OnClickListener mOnCkiOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.imv_act_top_left:
                case R.id.txv_act_top_title_left:
                    onBackPressed();
                    break;
                case R.id.btn_act_qrcode_choose:
                    //TODO 我的二维码名片
                    break;
                case R.id.imv_act_top_right:
                    showTitleMoreMenuPop(v);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setDisplayParams();
        super.onCreate(savedInstanceState);
        this.initData();
    }

    @Override
    protected void doAfterScan(Result obj, Bitmap barcode) {

    }

    @Override
    protected void doBeforeScan() {

    }

    @Override
    protected View doAddNewViews() {
        View view = getLayoutInflater().inflate(R.layout.activity_scan_qr_code, null);
        this.mTxvTopTitleLeft = (TextView) view.findViewById(R.id.txv_act_top_title_left);
        this.mImvTopLeft = (ImageView) view.findViewById(R.id.imv_act_top_left);
        this.mImvTopRight = (ImageView) view.findViewById(R.id.imv_act_top_right);
        this.mBtnQrcode = (Button) view.findViewById(R.id.btn_act_qrcode_choose);
        this.mTxvQrcodeScan = (TextView) view.findViewById(R.id.txv_act_qrcode_scan);
        this.mTxvTopTitleLeft.setOnClickListener(this.mOnCkiOnClickListener);
        this.mImvTopLeft.setOnClickListener(this.mOnCkiOnClickListener);
        this.mBtnQrcode.setOnClickListener(this.mOnCkiOnClickListener);
        this.mTxvTopTitleLeft.setVisibility(View.VISIBLE);
        this.mImvTopLeft.setVisibility(View.VISIBLE);
        this.mTxvTopTitleLeft.setText(R.string.common_scan);
        this.mImvTopRight.setImageResource(R.mipmap.img_top_more);
        this.mImvTopRight.setVisibility(View.VISIBLE);
        this.mImvTopRight.setOnClickListener(this.mOnCkiOnClickListener);
        return view;
    }

    /**
     * 设置摄像框参数
     */
    private void setDisplayParams(){
        this.mImvWidth = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_200dp);
        // 扫描内容块垂直居中
        this.mImvScanTop = (ActivityHelper.getScreenHeight() - ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_106dp))/2 - mImvWidth/2 ;

        //设置图像摄取框的位置
        setDisplayFrame(this.mImvScanTop, this.mImvWidth, this.mImvWidth);
        //取消摄取框中间的红线
        setShowRedLineInMiddle(false);

        //设置摄像头竖屏,该功能在scan2下需要注释掉但是在scan下不用注释，因为scan2默认是竖屏的
//		setCameraOrientation(false);
        //设置不展示扫描结果图片到的二维码取景框
        setShowScanImage(false);
        //设置不展示扫描结果到界面
        setShowScanTextResult(false);
    }

    private void initData(){
        this.isNeedSwithchToShowQrCodeFrg = getIntent().getBooleanExtra(IS_NEED_SWITCH_TO_SHOW_QR_CODE_FRG, false);
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) this.mTxvQrcodeScan.getLayoutParams();
        param.topMargin = this.mImvScanTop + this.mImvWidth + ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_25dp) - ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_48dp);
        this.mTxvQrcodeScan.setLayoutParams(param);
    }

    /**
     * 右上角弹窗
     */
    private void showTitleMoreMenuPop(View v){
        if(ResourceHelper.isNull(this.mTitleMoreMenuPop)){
            this.mTitleMoreMenuPop = TitleMoreMenuPopHelper.getQrcodeScanPop(this);
            this.mTitleMoreMenuPop.setItemClickListener(new TitleMoreMenuPopView.MoreMenuItemClickListener() {
                @Override
                public void onItemClick(TitleMoreMenuLsvIconEntity menu) {
                    switch (menu.getBusinessName()) {
                        case R.string.select_qr_code_from_picture:
                            //TODO 从相册选择二维码
                            break;
                        case R.string.my_vcard_qrcode:
                            //TODO 我的二维码名片
                            break;
                    }
                }
            });
        }
        this.unEnablePositionList.add(0);
        this.mTitleMoreMenuPop.setItemUnEnable(this.unEnablePositionList);
        this.mTitleMoreMenuPop.showAtLocation(v, Gravity.FILL, 0, 0);
    }
}
