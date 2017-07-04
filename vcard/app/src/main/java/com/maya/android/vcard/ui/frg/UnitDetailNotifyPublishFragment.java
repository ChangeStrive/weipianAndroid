package com.maya.android.vcard.ui.frg;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.json.UnitNotifyPublishJsonEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UploadImageResultEntity;
import com.maya.android.vcard.ui.adapter.CustomLsvAdapter;
import com.maya.android.vcard.ui.adapter.ImageHeadAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.UserHeadMaxDialogFragment;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 单位：详细页：微公告：发布微公告
 */
public class UnitDetailNotifyPublishFragment extends BaseFragment {
    private static final int HEAD_WIDTH = 200;
    public static final String KEY_NOTIFY_PUBLISH_UNITID = "key_notify_publish_unitid";
    private EditText mEdtNotifyPublish;
    private Button mBtnSubmit;
    private CustomDialogFragment  mDialogFragmentTakePicture, mDialogFragmentDelPicture;
    /** 放大图像 **/
    private UserHeadMaxDialogFragment mDialogFragmentHeadMax;
    private GridView mGrvHead;
    private ImageHeadAdapter mHeadAdapter;
    private ArrayList<String> mHeadUrlList = new ArrayList<String>();
    private ArrayList<String> mHeadDelList, mHeadAddlist;
    private String mCutHeadPath, mCurrHeadUrl;
    /**记录当前选中图片位置**/
    private int mPictureSelectedPoistion = -1;
    /** 本地新上传图片在列表中的位置 **/
    private HashMap<String, Integer> mHeadUrlPositions = new HashMap<String, Integer>();
    /** 拍照临时文件 **/
    private File mCameraTempFile;
    private static String mCameraTempFilePath;
    private boolean isFromTopHead = false;
    /** 地址列表 **/
    private URLResultEntity mUrlEntity = CurrentUser.getInstance().getURLEntity();
    /** 单位Id **/
    private long unitId;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_submit:
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
                    publishNotify();
                    break;
            }
        }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if( !super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag) {
                case Constants.CommandRequestTag.CMD_UPLOAD_IMAGE:
                    int position = Integer.valueOf(objects[0].toString());
                    UploadImageResultEntity uploadResult = JsonHelper.fromJson(commandResult, UploadImageResultEntity.class);
                    String mCurrHeadUrl = "";
                    if(ResourceHelper.isNotNull(uploadResult) && ResourceHelper.isNotNull(uploadResult.getFileUrl())){
                        mCurrHeadUrl = uploadResult.getFileUrl().get(0);
                        //本地文件替换为网路路径
                        String headPath = this.mHeadAddlist.get(position);
                        for (int i = 0, size = this.mHeadUrlList.size(); i < size; i++) {
                            if(headPath.equals(this.mHeadUrlList.get(i))){
                                this.mHeadUrlList.set(i, mCurrHeadUrl);
                            }
                        }
                        if(position < this.mHeadAddlist.size() - 1){
                            uploadPicture(position + 1);
                        }else{
                            uploadNotifyContent();
                        }
                    }else{
                        ActivityHelper.closeProgressDialog();
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onResponseFail(int tag, int responseCode, String msgInfo) {
        super.onResponseFail(tag, responseCode, msgInfo);
        ActivityHelper.closeProgressDialog();
        mEdtNotifyPublish.setEnabled(true);
    }

    @Override
    protected void onResponseSuccess(int tag, String msgInfo) {
        super.onResponseSuccess(tag, msgInfo);
        if(tag == Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_EDIT){
            ActivityHelper.closeProgressDialog();
            super.mFragmentInteractionImpl.onActivityBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.TakePicture.REQUEST_CODE_CAMERA_HEAD:
                // 拍照头像
                if( Activity.RESULT_OK == resultCode){
                    if(Helper.isNull(mCameraTempFile)){
                        this.mCameraTempFile = new File(mCameraTempFilePath);
                    }
                    doCropImage(Uri.fromFile(mCameraTempFile));
                }
                break;
            case Constants.TakePicture.REQUEST_CODE_ADD_IMAGE:
                // 图库选择头像
                if( Activity.RESULT_OK == resultCode){
                    if(Helper.isNotNull(data)){
                        Uri uri = data.getData();
                        try {
                            doCropImage(uri);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case Constants.TakePicture.REQUEST_CODE_IMAGE_CUT:
                // 图片裁切
                if( Activity.RESULT_OK == resultCode){

                    if(Helper.isNotEmpty(this.mCameraTempFile)){
                        this.mCameraTempFile.deleteOnExit();
                        mCameraTempFilePath = "";
                    }

                    Bundle extras = data.getExtras();
                    if (Helper.isNotNull(extras)) {
                        final Bitmap photo = extras.getParcelable("data");
                        if(Helper.isNull(photo)){
                            ActivityHelper.showToast(R.string.save_cut_image_fail);
                            return;
                        }

                        this.mCutHeadPath = ResourceHelper.saveBitmap2SDCard(photo, Constants.PATH.PATH_TEMP_FILE, null);
                        if(Helper.isNull(this.mHeadAddlist)){
                            this.mHeadAddlist = new ArrayList<String>();
                        }
                        this.mHeadAddlist.add(0, this.mCutHeadPath);
                        if(this.isFromTopHead){
                            this.mHeadAdapter.addItem(0, this.mCutHeadPath);
                            this.mHeadUrlList.add(0, this.mCutHeadPath);
                            this.mHeadUrlPositions.put(this.mCutHeadPath, 0);
                        }else{
                            int hadSize = this.mHeadUrlList.size();
                            this.mHeadAdapter.addItem(hadSize, mCutHeadPath);
                            this.mHeadUrlPositions.put(this.mCutHeadPath, hadSize);
                            this.mHeadUrlList.add(this.mCutHeadPath);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unit_detail_notify_public, container, false);
        this.mEdtNotifyPublish = (EditText) view.findViewById(R.id.edt_notify_publish);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mGrvHead = (GridView) view.findViewById(R.id.grv_img_head);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.we_notice, false);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        showImageHead();
        this.mGrvHead.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != mHeadUrlList.size()) {
                    showDialogFragmentHeadMax(mHeadAdapter.getItem(position));
                } else {
                    showmDialogFragmentTakePicture(false);
                }
            }
        });

        this.mGrvHead.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPictureSelectedPoistion = position;
                if (mPictureSelectedPoistion != mHeadUrlList.size() && mPictureSelectedPoistion != -1) {
                    showDialogFragmentDelPicture();
                }
                return false;
            }
        });
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
            this.unitId = bundle.getLong(KEY_NOTIFY_PUBLISH_UNITID, 0);

        }
    }

    /**
     * 显示照片墙
     */
    private void showImageHead(){
        this.mHeadAdapter = new ImageHeadAdapter(getActivity());
        this.mGrvHead.setAdapter(this.mHeadAdapter);
        this.mHeadAdapter.setEditState(true);
        this.mHeadUrlList = ResourceHelper.getListFromString("", "#");
        if(ResourceHelper.isNull(this.mHeadUrlList)){
            this.mHeadUrlList = new ArrayList<String>();
        }
        this.mHeadAdapter.addItems(this.mHeadUrlList);
        if(ResourceHelper.isNotNull(this.mHeadUrlList) && this.mHeadUrlList.size() > 0){
            this.mCurrHeadUrl = this.mHeadUrlList.get(0);
        }
    }

    /**
     * 选择拍照方式
     */
    private void showmDialogFragmentTakePicture(boolean isFromTop){
        this.isFromTopHead = isFromTop;
        if(Helper.isNull(this.mDialogFragmentTakePicture)){
            ListView mLsvItems = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
            mLsvItems.setAdapter(mLsvAdapter);
            mLsvAdapter.setShowRadio(false);
            mLsvAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.image_choose_type));
            mLsvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0){
                        // 指定调用相机拍照后的照片存储的路径
                        mCameraTempFilePath = ResourceHelper.creatEmptyFile(null);
                        mCameraTempFile = new File(mCameraTempFilePath);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraTempFile));
                        startActivityForResult(intent, Constants.TakePicture.REQUEST_CODE_CAMERA_HEAD);
                    }else{
                        intent2Picture();
                    }
                    mDialogFragmentTakePicture.getDialog().dismiss();
                }
            });

            this.mDialogFragmentTakePicture = DialogFragmentHelper.showCustomDialogFragment(R.string.selected_get_picture_way, mLsvItems);
            this.mDialogFragmentTakePicture.show(getFragmentManager(),"mDialogFragmentTakePicture");
        }else{
            this.mDialogFragmentTakePicture.show(getFragmentManager(),"mDialogFragmentTakePicture");
        }
    }

    /**
     * 图库选择图片
     */
    private void intent2Picture(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.TakePicture.REQUEST_CODE_ADD_IMAGE);
    }

    /**
     * 裁切图像
     */
    private void doCropImage(Uri uri){
        if(Helper.isNull(uri)){
            return;
        }
        Intent cropIntent = ResourceHelper.getCropImageIntent(uri, HEAD_WIDTH);
        startActivityForResult(cropIntent,Constants.TakePicture.REQUEST_CODE_IMAGE_CUT);
    }

    /**
     * 删除图片对话框
     */
    private void showDialogFragmentDelPicture(){
        if(Helper.isNull(this.mDialogFragmentDelPicture)){
            CustomDialogFragment.DialogFragmentInterface onClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        if(Helper.isNull(mHeadDelList)){
                            mHeadDelList = new ArrayList<String>();
                        }
                        String delHeadPath = mHeadAdapter.getItem(mPictureSelectedPoistion);
                        if(!mHeadDelList.contains(delHeadPath)){
                            mHeadDelList.add(delHeadPath);
                        }
                        mHeadUrlList.remove(delHeadPath);
                        mHeadAdapter.removeItem(delHeadPath);
                    }
                }
            };
            this.mDialogFragmentDelPicture = DialogFragmentHelper.showCustomDialogFragment(R.string.del_head, R.string.confirm_delete_head, R.string.frg_text_cancel, R.string.frg_text_ok, onClick);
            this.mDialogFragmentDelPicture.show(getFragmentManager(),"mDialogFragmentDelPicture");
        }else{
            this.mDialogFragmentDelPicture.show(getFragmentManager(),"mDialogFragmentDelPicture");
        }
    }

    /**
     * 显示大图
     */
    private void showDialogFragmentHeadMax(String headImg){
        if(ResourceHelper.isNull(this.mDialogFragmentHeadMax)){
            this.mDialogFragmentHeadMax = DialogFragmentHelper.showUserHeadMaxDialogFragment(headImg);
            this.mDialogFragmentHeadMax.setTargetFragment(this, Constants.TakePicture.REQUEST_CODE_CHOOSE_HEAD);
        }
        this.mDialogFragmentHeadMax.setHeadImg(headImg);
        this.mDialogFragmentHeadMax.show(getFragmentManager(),"mDialogFragmentHeadMax");
    }

    /**
     * 单张图片上传
     * @param position
     */
    private void uploadPicture(int position){
        String headPath = this.mHeadAddlist.get(position);
        String uploadUrl = this.mUrlEntity.getImgBaseService() + this.mUrlEntity.getCommonUpload();
        uploadUrl += "?"+ "category" + "=notice";
        String newFileName = "";
        JSONObject json = new JSONObject();
        ReqAndRespCenter.getInstance().upload(Constants.CommandRequestTag.CMD_UPLOAD_IMAGE, uploadUrl, headPath, newFileName, json, null, this, position);
    }

    /**
     * 上传微公告文本信息
     */
    private void uploadNotifyContent(){
        String requestUrl = ProjectHelper.fillRequestURL(this.mUrlEntity.getEnterpriseNotice());
        String token = CurrentUser.getInstance().getToken();
        String notifyContent = this.mEdtNotifyPublish.getText().toString();
        long cardId = CurrentUser.getInstance().getCurrentVCardEntity().getId();
        UnitNotifyPublishJsonEntity unitNotifyPublish = new UnitNotifyPublishJsonEntity();
        unitNotifyPublish.setEnterpriseId(this.unitId);
        unitNotifyPublish.setContent(notifyContent);
        unitNotifyPublish.setCardId(cardId);
        unitNotifyPublish.setImage(ResourceHelper.getStringFromList(this.mHeadUrlList, Constants.Other.CONTENT_ARRAY_SPLIT));
        String json = JsonHelper.toJson(unitNotifyPublish, UnitNotifyPublishJsonEntity.class);
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_EDIT, requestUrl,token, json, this);
    }

    /**
     * 发布微公告
     */
    private void publishNotify(){
        this.mEdtNotifyPublish.setEnabled(false);
        ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
        if(ResourceHelper.isNotNull(this.mHeadAddlist)){
            uploadPicture(0);
        }else{
            uploadNotifyContent();
        }
    }
}
