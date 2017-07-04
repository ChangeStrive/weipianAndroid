package com.maya.android.vcard.ui.widget;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 雷达搜索点击单个成员展示
 * Created by Administrator on 2015/9/17.
 */
public class SwapRadarMemberDialogFragment extends DialogFragment {
    private boolean isCanceled = true;//点击弹框外外围屏幕，弹框默认消失
    private AsyncImageView mImvHead;
    private TextView mTxvName, mTxvJob, mTxvCompany;
    private Button mBtnSubmit;

    private String name;
    private String job;
    private String company;
    private String headImg;
    private String btnName;
    private int btnBackgroundColorId = -1;
    private int btnTextColorId = -1;
    public OnClickInterface mListener;
    /**按钮是否可点击**/
    private boolean isEnable = true;//默认可点击

    /**
     * SwapRadarMemberDialogFragment
     * @return
     */
    public static SwapRadarMemberDialogFragment newInstance(){
        SwapRadarMemberDialogFragment dlgFragment = new SwapRadarMemberDialogFragment();
        return dlgFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.theme_swap_radar_member);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //去掉原始标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_fragment_swap_radar_member, container, false);
        this.mImvHead = (AsyncImageView) view.findViewById(R.id.imv_radar_search_detail_head);
        //头像圆角
        this.mImvHead.setRoundCorner(-2);
        this.mTxvName = (TextView) view.findViewById(R.id.txv_radar_search_detail_name);
        this.mTxvJob = (TextView) view.findViewById(R.id.txv_radar_search_detail_job);
        this.mTxvCompany = (TextView) view.findViewById(R.id.txv_radar_search_detail_company);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ResourceHelper.isNotNull(mListener)) {
                    mListener.onClick();
                    getDialog().dismiss();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ResourceHelper.isNotNull(getDialog()) ) {
            //设置对话框背景透明
            Window window = getDialog().getWindow();
            if(ResourceHelper.isNotNull(window)){
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            //设置点击屏幕，对话框消失
            getDialog().setCanceledOnTouchOutside(this.isCanceled);
//            setCancelable(this.isCanceled);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mImvHead.setDefaultImageResId(R.mipmap.img_head);
        ResourceHelper.asyncImageViewFillUrl(this.mImvHead, headImg);
        this.mTxvName.setText(this.name);
        this.mTxvJob.setText(this.job);
        this.mTxvCompany.setText(this.company);
        this.mBtnSubmit.setText(this.btnName);
        if(this.btnTextColorId != -1){
            this.mBtnSubmit.setTextColor(this.btnTextColorId);
        }
        if(this.btnBackgroundColorId != -1){
            this.mBtnSubmit.setBackgroundResource(this.btnBackgroundColorId);
        }
        this.mBtnSubmit.setEnabled(this.isEnable);

    }

    /**
     * 姓名
     * @param name
     */
    public SwapRadarMemberDialogFragment setName(String name){
        this.name = name;
        return this;
    }
    /**
     * 职业
     * @param job
     */
    public SwapRadarMemberDialogFragment setJob(String job){
        this.job = job;
        return this;
    }

    /**
     * 头像
     * @param headImg
     */
    public SwapRadarMemberDialogFragment setHeadImg(String headImg){
        this.headImg = headImg;
        return this;
    }

    /**
     * 公司
     * @param company
     */
    public SwapRadarMemberDialogFragment setCompany(String company){
        this.company = company;
        return this;
    }

    /**
     * 按钮名称
     * @param btnName
     * @param backgroundId 按钮背景图片Id
     */
    public SwapRadarMemberDialogFragment setBtnName(String btnName, int backgroundId){
        this.btnName = btnName;
        this.btnBackgroundColorId = backgroundId;
        return this;
    }

    /**
     * 按钮名称
     * @param resId
     * @param backgroundId 按钮背景图片Id
     */
    public SwapRadarMemberDialogFragment setBtnName(int resId, int backgroundId, int textColorId){
        this.btnName = ActivityHelper.getGlobalApplicationContext().getString(resId);
        this.btnBackgroundColorId = backgroundId;
        this.btnTextColorId = ActivityHelper.getGlobalApplicationContext().getResources().getColor(textColorId);
        return this;
    }

    /**
     * 设置按钮是否可点击
     * @param isEnable
     * @return
     */
    public SwapRadarMemberDialogFragment setIsEnable(boolean isEnable){
        this.isEnable = isEnable;
        return this;
    }

    /**
     * 按钮点击监听
     * @param mListener
     */
    public void setOnClickInterface(OnClickInterface mListener){
        this.mListener = mListener;
    }

    public interface OnClickInterface{
        void onClick();
    }
}
