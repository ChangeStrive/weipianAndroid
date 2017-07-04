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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.json.UserInfoSaveJsonEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UpdateNameTimeResultEntity;
import com.maya.android.vcard.entity.result.UploadImageResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.adapter.CustomLsvAdapter;
import com.maya.android.vcard.ui.adapter.ImageHeadAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.UserHeadMaxDialogFragment;
import com.maya.android.vcard.ui.widget.UserInfoMoreEdtItemView;
import com.maya.android.vcard.ui.widget.xbdialog.DialogDate;
import com.maya.android.vcard.ui.widget.xbdialog.Dialogim;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 用户：详细资料编辑页
 */
public class UserInfoEditFragment extends BaseFragment {
    private static final int HEAD_WIDTH = 200;
    /** 显示更多 **/
    private boolean isShowMore = true;
//    private String mFamilyName, mName, mGraduateInstitutions, mSex, mConstellation, mAge, mBirthday, mEducation, mHometown, mProvince;
    private String mFamilyName, mName, mGraduateInstitutions, mConstellation, mAge, mBirthday, mEducation, mHometown, mProvince;
    private int mDialogFragmentSexPosition, mDialogFragmentEducationPosition;
    /** 放大图像 **/
    private UserHeadMaxDialogFragment mDialogFragmentHeadMax;
//    private CustomDialogFragment mDialogFragmentSex, mDialogFragmentEducation, mDialogFragmentTakePicture, mDialogFragmentDelPicture, mDialogFragmentDate, mDialogFragmentChangeUserName, mDialogFragmentHometown;
    private CustomDialogFragment  mDialogFragmentEducation, mDialogFragmentTakePicture, mDialogFragmentDelPicture, mDialogFragmentDate, mDialogFragmentChangeUserName, mDialogFragmentHometown;
//    private EditText mEdtFamilyName, mEdtName, mEdtGraduateInstitutions, mEdtIntro;
    private EditText mEdtFamilyName,mEdtName, mEdtGraduateInstitutions, mEdtIntro;
//    private TextView mTxvSex, mTxvConstellation, mTxvAge, mTxvBirthday, mTxvEducation, mTxvHometown;
    private TextView  mTxvConstellation, mTxvAge, mTxvBirthday, mTxvEducation, mTxvHometown;
    private TextView mTxvCompleteness;
    private TextView mTxvMoreEdt;
    private LinearLayout mLilWorkExperience, mLilEducationalInformation;
    private ImageButton mImbWorkExperience, mImbEducationalInformation;
    private EditText mEdtWorkExperience,mEdtEducationalInformation;
    private GridView mGrvHead;
    private ImageHeadAdapter mHeadAdapter;
    private ArrayList<String> mHeadUrlList = new ArrayList<String>();
    private ArrayList<String> mHeadDelList, mHeadAddlist;
    private String mCutHeadPath, mCurrHeadUrl;
    /**记录当前选中图片位置**/
    private int mPictureSelectedPoistion = -1;
    /** 本地新上传图片在列表中的位置 **/
    private HashMap<String, Integer> mHeadUrlPositions = new HashMap<String, Integer>();
    /**当前放大图像的路径**/
    private String mShowMaxHeadUrl;
    /** 拍照临时文件 **/
    private File mCameraTempFile;
    private static String mCameraTempFilePath;
    private boolean isFromTopHead = false;
    /** 是否修改用户名 **/
    private boolean isChangeUserName = false;
    private URLResultEntity mUrlEntity = CurrentUser.getInstance().getURLEntity();
    /** 用户资料 **/
    private UserInfoResultEntity userInfo =  CurrentUser.getInstance().getUserInfoEntity();


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
//                case R.id.txv_sex:
//                    //性别
//                    showDialogFragmentSex();
//                    break;
                case R.id.txv_constellation:
                case R.id.txv_age:
                case R.id.txv_birthday:
                    //选择出生年月
                    showDialogFragmentDate();
                    break;
                case R.id.txv_education:
                    //学历
                    showDialogFragmentEducation();
                    break;
                case R.id.txv_hometown:
                    //家乡
                    showDialogFragmentHometown();
                    break;
                case R.id.txv_more:
                    //更多
                    if(isShowMore){
                        mTxvMoreEdt.setText(R.string.pack_up);
                        mLilWorkExperience.setVisibility(View.VISIBLE);
                        mLilEducationalInformation.setVisibility(View.VISIBLE);
                    }else{
                        mTxvMoreEdt.setText(R.string.more);
                        mLilWorkExperience.setVisibility(View.GONE);
                        mLilEducationalInformation.setVisibility(View.GONE);
                    }
                    isShowMore();
                    break;
                case R.id.imb_work_experience_add:
                    //添加更多工作经历
                    addItemView(mLilWorkExperience);
                    break;
                case R.id.imb_educational_information:
                    //添加更多教育信息
                    addItemView(mLilEducationalInformation);
                    break;
                case R.id.txv_act_top_right:
                    if(isValidAllData()){
                        if(isChangeUserName){
                            showDialogFragmentChangeUserName();
                        }else{
                            saveUserInfo();
                        }
                    }
                    break;
         }
        }
    };
    private RadioGroup rgSex;
    private RadioButton rdMale;
    private RadioButton rdFemale;

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_HEAD:
                    //上传图片回调
                    int position = Integer.valueOf(objects[0].toString());
                    UploadImageResultEntity uploadResult = JsonHelper.fromJson(commandResult, UploadImageResultEntity.class);
                    if(Helper.isNotNull(uploadResult) && Helper.isNotNull(uploadResult.getFileUrl())){
                        this.mCurrHeadUrl = uploadResult.getFileUrl().get(0);
                        String headPath = this.mHeadAddlist.get(position);
                        for (int i = 0, size = this.mHeadUrlList.size(); i < size; i++) {
                            if(headPath.equals(this.mHeadUrlList.get(i))){
                                this.mHeadUrlList.set(i, this.mCurrHeadUrl);
                            }
                        }
                        if(position < this.mHeadAddlist.size() - 1){
                            uploadHead(position + 1);
                        }else{
                            uploadUserInfo();
                        }
                    }else{
                        ActivityHelper.closeProgressDialog();
                    }
                    break;
                case Constants.CommandRequestTag.CMD_REQUEST_MY_INFO_SAVE:
                    //上传用户信息回调
                    UpdateNameTimeResultEntity resultEntity = JsonHelper.fromJson(commandResult, UpdateNameTimeResultEntity.class);
                    if(Helper.isNotNull(resultEntity)){
                        this.userInfo.setUpdNameTime(resultEntity.getUpdNameTime());
                    }
                    String name = mFamilyName.substring(1,mFamilyName.length());
//                    this.userInfo.setSurname(this.mFamilyName);
                    this.userInfo.setSurname(name);
                    this.mName = this.mFamilyName.substring(0,1);
                    this.userInfo.setFirstName(this.mName);//取姓
                    this.userInfo.setIntro(this.mEdtIntro.getText().toString());
                    this.userInfo.setSchool(this.mGraduateInstitutions);
                    this.userInfo.setBirthday(this.mBirthday);
                    this.userInfo.setEduBackground(this.mEducation);
                    this.userInfo.setNativeplace(this.mHometown);
                    this.userInfo.setWorkHistory(getWorkExperience());
                    this.userInfo.setEduInfo(getEducationalInformation());
                    this.userInfo.setProvince(this.mProvince);
                    this.userInfo.setSex(this.mDialogFragmentSexPosition);
                    this.userInfo.setHeadImg(ResourceHelper.getStringFromList(this.mHeadUrlList, Constants.Other.CONTENT_ARRAY_SPLIT));
                    CurrentUser.getInstance().saveUserInfoEntity(JsonHelper.toJson(this.userInfo, UserInfoResultEntity.class), null);
                    super.mFragmentInteractionImpl.onActivityBackPressed();
                    ActivityHelper.closeProgressDialog();
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onResponseFail(int tag, int responseCode, String msgInfo) {
        super.onResponseFail(tag, responseCode, msgInfo);
        ActivityHelper.closeProgressDialog();
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
            case Constants.TakePicture.REQUEST_CODE_CHOOSE_HEAD:
                //放大图像回调
                if(resultCode == Activity.RESULT_OK){
                    this.mHeadUrlList.remove(mShowMaxHeadUrl);
                    this.mHeadUrlList.add(0, mShowMaxHeadUrl);
                    this.mHeadAdapter.selectItem(mShowMaxHeadUrl);
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info_edit, container, false);
        this.mGrvHead = (GridView) view.findViewById(R.id.grv_img_head);
        this.mTxvCompleteness = (TextView) view.findViewById(R.id.txv_improve_the_degree_of_information_num);

        this.mEdtFamilyName = (EditText) view.findViewById(R.id.edt_family_name);//姓名
        this.mEdtName = (EditText) view.findViewById(R.id.edt_name);//名字
        this.mEdtGraduateInstitutions = (EditText) view.findViewById(R.id.edt_graduate_institutions);
        this.mEdtIntro = (EditText) view.findViewById(R.id.edt_detail_user_introduce);//个性签名
//        this.mTxvSex = (TextView) view.findViewById(R.id.txv_sex);    性别选择
        this.mTxvConstellation = (TextView) view.findViewById(R.id.txv_constellation);
        this.mTxvAge = (TextView) view.findViewById(R.id.txv_age);
        this.mTxvBirthday = (TextView) view.findViewById(R.id.txv_birthday);
        this.mTxvEducation = (TextView) view.findViewById(R.id.txv_education);
        this.mTxvHometown = (TextView) view.findViewById(R.id.txv_hometown);
        this.mTxvMoreEdt = (TextView) view.findViewById(R.id.txv_more);


        rgSex = (RadioGroup) view.findViewById(R.id.rg_sex);
        rdMale = (RadioButton) view.findViewById(R.id.male);
        rdFemale = (RadioButton) view.findViewById(R.id.female);
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rdMale.getId()==checkedId){//选择男
                    mDialogFragmentSexPosition = 1;
                }
                if(rdFemale.getId()==checkedId){//选择女
                    mDialogFragmentSexPosition = 0;
                }
            }
        });

        this.mLilWorkExperience = (LinearLayout) view.findViewById(R.id.lil_work_experience);
        this.mLilEducationalInformation = (LinearLayout) view.findViewById(R.id.lil_educational_information);
        this.mImbWorkExperience = (ImageButton) view.findViewById(R.id.imb_work_experience_add);
        this.mImbEducationalInformation = (ImageButton) view.findViewById(R.id.imb_educational_information);
        this.mEdtWorkExperience = (EditText) view.findViewById(R.id.edt_work_experience);
        this.mEdtEducationalInformation = (EditText) view.findViewById(R.id.edt_educational_information);

//        this.mTxvSex.setOnClickListener(this.mOnClickListener);
        this.mTxvConstellation.setOnClickListener(this.mOnClickListener);
        this.mTxvBirthday.setOnClickListener(this.mOnClickListener);
        this.mTxvAge.setOnClickListener(this.mOnClickListener);
        this.mTxvHometown.setOnClickListener(this.mOnClickListener);
        this.mTxvEducation.setOnClickListener(this.mOnClickListener);
        this.mTxvMoreEdt.setOnClickListener(this.mOnClickListener);
        this.mImbWorkExperience.setOnClickListener(this.mOnClickListener);
        this.mImbEducationalInformation.setOnClickListener(this.mOnClickListener);
         return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTitle(R.string.user_edit_info,true);
        super.mTitleAction.setActivityTopRightTxv(R.string.frg_text_ok, this.mOnClickListener);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        showImageHead(this.userInfo);
        fillData(this.userInfo);
        //单击
        this.mGrvHead.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != mHeadUrlList.size()) {
                    showDialogFragmentHeadMax(mHeadAdapter.getItem(position));
                } else {
                    showDialogFragmentTakePicture(false);
                }
            }
        });
        //长按
        this.mGrvHead.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPictureSelectedPoistion = position;
                if(mPictureSelectedPoistion == 0){
                    ActivityHelper.showToast(R.string.detail_is_current_head);
                }else if(mPictureSelectedPoistion != mHeadUrlList.size() && mPictureSelectedPoistion != -1){
                    showDialogFragmentDelPicture();
                }
                return true;
            }
        });
    }

    /**
     * 填充用户资料
     */
    private void fillData(UserInfoResultEntity userInfo){
        if(ResourceHelper.isNotNull(userInfo)){
            this.mEdtFamilyName.setText(userInfo.getFirstName()+userInfo.getSurname());//设置姓名：姓氏+名片
            this.mEdtName.setText(userInfo.getFirstName());
            if(CurrentUser.getInstance().getUserNameEdt()){
                ResourceHelper.setTextViewEnable(this.mEdtFamilyName, true, R.color.color_292929);
                ResourceHelper.setTextViewEnable(this.mEdtName, true, R.color.color_292929);
                setChangeUserName(true);
            }else {
                ResourceHelper.setTextViewEnable(this.mEdtFamilyName, false, R.color.color_787878);
                ResourceHelper.setTextViewEnable(this.mEdtName, false, R.color.color_787878);
                setChangeUserName(false);
            }
//            String sex = userInfo.getSex() == 1 ? "男" : "女";
            String birthday = userInfo.getBirthday();
//            this.mTxvSex.setText(sex);    设置性别
            int sex1 = userInfo.getSex();//根据int值设置按钮选择状态
            if(sex1==0){
                rdFemale.setChecked(true);
                rdMale.setChecked(false);
            }else {
                rdMale.setChecked(true);
                rdFemale.setChecked(false);
            }
            this.mTxvConstellation.setText(ResourceHelper.getAstro(birthday));
            this.mTxvAge.setText(ResourceHelper.getAgeFromBirthday(birthday));
            this.mTxvBirthday.setText(birthday);
            this.mTxvEducation.setText(userInfo.getEduBackground());
//            String home = userInfo.getProvince()+"省-"+userInfo.getCity()+"市-"+userInfo.getCountry()+"区";
            this.mTxvHometown.setText(userInfo.getNativeplace());
//            this.mTxvHometown.setText(home);
            this.mEdtGraduateInstitutions.setText(userInfo.getSchool());
            this.mEdtIntro.setText(userInfo.getIntro());
            //工作经验
            addItemViewContent(this.mLilWorkExperience, this.mEdtWorkExperience, userInfo.getWorkHistory());
            //教育经验
            addItemViewContent(this.mLilEducationalInformation, this.mEdtEducationalInformation, userInfo.getEduInfo());
            this.mTxvCompleteness.setText(getCalculationCompletion(userInfo) + "%");
            this.mProvince = userInfo.getProvince();
        }
    }

    /**
     * 设置用户名是否可以修改
     * @param isChangeUserName
     */
    private void setChangeUserName(boolean isChangeUserName){
        this.isChangeUserName = isChangeUserName;
    }

    /**
     * 计算完个人资料成度
     * @param userInfo
     */
    private int getCalculationCompletion(UserInfoResultEntity userInfo){
        int completeness = 0;//记录资料完善度
        if(ResourceHelper.isNotNull(userInfo)){
            if(ResourceHelper.isNotEmpty(userInfo.getBirthday())){//出生日期
                completeness = completeness + 30;
            }
            if(ResourceHelper.isNotNull(userInfo.getSex())) {//性别
                completeness=completeness+10;
            }

            if(ResourceHelper.isNotEmpty(userInfo.getDisplayName())){//姓名
                completeness = completeness + 10;
            }

            if(ResourceHelper.isNotEmpty(userInfo.getHeadImg())){//头像
                completeness = completeness + 10;
            }

            if(ResourceHelper.isNotEmpty(userInfo.getNativeplace())){//住址
                completeness =  completeness + 10;
            }

            if(ResourceHelper.isNotEmpty(userInfo.getSchool())){//学校
                completeness = completeness + 10;
            }

            if(ResourceHelper.isNotEmpty(userInfo.getEduBackground())){//学历
                completeness = completeness + 10;
            }

            if(ResourceHelper.isNotEmpty(userInfo.getIntro())){//我介绍
                completeness = completeness + 10;
            }
        }

        return completeness;
    }

    /**
     *  显示照片墙
     */
    private void showImageHead(UserInfoResultEntity userInfo){
        this.mHeadAdapter = new ImageHeadAdapter(getActivity());
        this.mGrvHead.setAdapter(this.mHeadAdapter);
        this.mHeadAdapter.setEditState(true);
        if(ResourceHelper.isNotNull(userInfo)){
            this.mHeadUrlList = ResourceHelper.getListFromString(userInfo.getHeadImg(), Constants.Other.CONTENT_ARRAY_SPLIT);
        }
         if(ResourceHelper.isNull(this.mHeadUrlList)){
            this.mHeadUrlList = new ArrayList<String>();
        }
        this.mHeadAdapter.addItems(this.mHeadUrlList);
        if(Helper.isNotNull(this.mHeadUrlList) && this.mHeadUrlList.size() > 0){
            this.mCurrHeadUrl = this.mHeadUrlList.get(0);
        }
    }

    /**
     * 每一次变化取相反值(更多)
     */
    private void isShowMore(){
        this.isShowMore = !this.isShowMore;
    }

    /**
     * 添加输入框
     * @param mLilItem
     */
    private void addItemView(LinearLayout mLilItem){
        addItemView(mLilItem, null);
    }

    /**
     * 添加输入框
     * @param mLilItem
     * @param text 赋值
     */
    private void addItemView(final LinearLayout mLilItem, String text){
        if(ResourceHelper.isNotNull(mLilItem)){
            final UserInfoMoreEdtItemView itemView = new UserInfoMoreEdtItemView(getActivity());
            itemView.getBtnDelete().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLilItem.removeView(itemView);
                }
            });

            if(Helper.isNotNull(text)){
                itemView.setEdtContent(text);
            }
            mLilItem.addView(itemView);
        }
    }

    /**
     * 动态添加输入控件及相应内容
     * @param mLilItemView 添加到指定LinearLayout
     * @param mEdt 该LinearLayout首个子RelativeLayout中的EditText
     * @param mContent 需填充的String[]型的字符串
     */
    private void addItemViewContent(LinearLayout mLilItemView, EditText mEdt, String mContent){
        if(Helper.isNotEmpty(mContent)){
            String[] emailArr = mContent.trim().split(Constants.Other.CONTENT_ARRAY_SPLIT);
            mEdt.setText(emailArr[0]);
            int emailArrLength = emailArr.length;
            if(emailArrLength > 1){
                for(int i = 1; i < emailArrLength; i++){
                    String content = emailArr[i];
                    if(Helper.isNotEmpty(content)){
                        addItemView(mLilItemView, content);
                    }
                }
            }
        }
    }

    /**
     * 获取当前输入的工作经历(所有)
     * @return
     */
    private String getWorkExperience(){
        return getAllItemContent(this.mLilWorkExperience, this.mEdtWorkExperience);
    }

    /**
     * 获取当前输入的教育信息(所有)
     * @return
     */
    private String getEducationalInformation(){
        return getAllItemContent(this.mLilEducationalInformation, this.mEdtEducationalInformation);
    }

   /**
     * 获取某个LinearLayout中所有子view编辑框中的内容
     * @param mLilItemView
     * @param mEdit
     * @return 返回一个list
     */
    private String getAllItemContent(LinearLayout mLilItemView, EditText mEdit){
        StringBuffer mAllContent = new StringBuffer();
        mAllContent.append(mEdit.getText().toString());
        int count = mLilItemView.getChildCount();
        //动态添加输入框从第三个开始算
        for (int i = 3; i < count; i++) {
            mAllContent.append("#").append(getItemContent(mLilItemView, i));
        }
        return mAllContent.toString();
    }

    /**
     * 获取单个Item中编辑框内容
     * @param mLilItemView
     * @param index 子项编号
     * @return
     */
    private String getItemContent(LinearLayout mLilItemView, int index){
        UserInfoMoreEdtItemView itemView = (UserInfoMoreEdtItemView) mLilItemView.getChildAt(index);
        return itemView.getEdtContent();
    }

    /**
     * 输入判空验证
     * @return
     */
    private boolean isValidAllData(){
//       姓名
        this.mFamilyName = this.mEdtFamilyName.getText().toString();
//        this.mName = this.mEdtName.getText().toString();
//        if(Helper.isEmpty(this.mFamilyName) || Helper.isEmpty(this.mName)){
//            ActivityHelper.showToast(R.string.please_enter_name);
//            return false;
//        }
        if (Helper.isEmpty(this.mFamilyName)) {
            ActivityHelper.showToast(R.string.please_enter_name);
            return false;
        }
//        性别
//        this.mSex = this.mTxvSex.getText().toString();
//        if(Helper.isEmpty(this.mSex)){
//            ActivityHelper.showToast(R.string.please_selected_sex);
//            return false;
//        }
//        出生年月、年龄、星座
        this.mBirthday = this.mTxvBirthday.getText().toString();
        this.mAge = this.mTxvAge.getText().toString();
        this.mConstellation = this.mTxvConstellation.getText().toString();
        if(Helper.isEmpty(this.mBirthday) || Helper.isEmpty(this.mAge) || Helper.isEmpty(this.mConstellation)){
            ActivityHelper.showToast(R.string.please_selected_birthday);
            return false;
        }
//        学历
        this.mEducation = this.mTxvEducation.getText().toString();
        if(Helper.isEmpty(this.mEducation)){
            ActivityHelper.showToast(R.string.please_selected_education);
            return false;
        }
//        家乡
        this.mHometown = this.mTxvHometown.getText().toString();
        if(Helper.isEmpty(this.mHometown)){
            ActivityHelper.showToast(R.string.please_selected_hometown);
            return false;
        }
        return true;
    }

    /**
     * 选择性别对方框
     */
//    private void showDialogFragmentSex(){
//        if(Helper.isNull(this.mDialogFragmentSex)){
//            ListView mLsvItems = DialogFragmentHelper.getCustomContentView(getActivity());
//            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
//            mLsvItems.setAdapter(mLsvAdapter);
//            mLsvAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.sex));
//            mLsvAdapter.setPosition(this.mDialogFragmentSexPosition);
//            mLsvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    mDialogFragmentSexPosition = position;
//                    mLsvAdapter .setPosition(position);
//                }
//            });
//            CustomDialogFragment.DialogFragmentInterface mDlgOnClick = new CustomDialogFragment.DialogFragmentInterface() {
//                @Override
//                public void onDialogClick(int which) {
//                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
//                        mTxvSex.setText(mLsvAdapter.getItem(mDialogFragmentSexPosition));
//                    }
//                }
//            };
//            this.mDialogFragmentSex = DialogFragmentHelper.showCustomDialogFragment(R.string.selected_sex, mLsvItems, R.string.frg_text_cancel, R.string.frg_text_ok, mDlgOnClick);
//            this.mDialogFragmentSex.show(getFragmentManager(),"mDialogFragmentSex");
//        }else{
//            this.mDialogFragmentSex.show(getFragmentManager(),"mDialogFragmentSex");
//        }
//    }

    /**
     * 选择学历对话框
     */
    private void showDialogFragmentEducation(){
        if(Helper.isNull(this.mDialogFragmentEducation)){
            ListView mLsvItems = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
            mLsvItems.setAdapter(mLsvAdapter);
            mLsvAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.education));
            mLsvAdapter.setPosition(this.mDialogFragmentEducationPosition);
            mLsvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mDialogFragmentEducationPosition = position;
                    mLsvAdapter.setPosition(position);
                }
            });
            CustomDialogFragment.DialogFragmentInterface mDlgOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        mTxvEducation.setText(mLsvAdapter.getItem(mDialogFragmentEducationPosition));
                    }
                }
            };
            this.mDialogFragmentEducation = DialogFragmentHelper.showCustomDialogFragment(R.string.selected_education, mLsvItems, R.string.frg_text_cancel, R.string.frg_text_ok, mDlgOnClick);
            this.mDialogFragmentEducation.show(getFragmentManager(),"mDialogFragmentEducation");
        }else{
            this.mDialogFragmentEducation.show(getFragmentManager(),"mDialogFragmentEducation");
        }
    }

    /**
     * 选择拍照方式
     */
    private void showDialogFragmentTakePicture(boolean isFromTop){
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
     * 图库选择图片
     */
    private void intent2Picture(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.TakePicture.REQUEST_CODE_ADD_IMAGE);
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
     * 选择出生日期对话框
     */
    private void showDialogFragmentDate(){
        if(ResourceHelper.isNull(this.mDialogFragmentDate)){
            View view = DialogDate.getIntance().getDialogDate(getActivity(), 1900, 2100);
            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        String strDate = DialogDate.getIntance().getDates();
                        String age = ResourceHelper.getAgeFromBirthday(strDate);
                        if(Helper.isNotNull(age)){
                            mTxvBirthday.setText(strDate);
                            mTxvConstellation.setText(ResourceHelper.getAstro(strDate));
                            mTxvAge.setText(age);
                        }
                    }
                }
            };
            this.mDialogFragmentDate = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_text_cancel, view, R.string.frg_text_cancel, R.string.frg_text_ok, mOnClick);

        }
        this.mDialogFragmentDate.show(getFragmentManager(), "mDialogFragmentDate");
    }

    /**
     * 选择城市对话框
     */
    private void showDialogFragmentHometown(){
        if(ResourceHelper.isNull(this.mDialogFragmentHometown)){
            View view = Dialogim.getIntance().getDialogim(getActivity());
            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
//                        mTxvHometown.setText(Dialogim.getIntance().getCities());
                        String home = Dialogim.getIntance().getProvinces()+"省-"+Dialogim.getIntance().getCities()+"市-"+Dialogim.getIntance().getCounties();
                        mTxvHometown.setText(home);
                        //省份
                        mProvince = Dialogim.getIntance().getProvinces();
                    }
                }
            };
            this.mDialogFragmentHometown = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_text_cancel, view, R.string.frg_text_cancel, R.string.frg_text_ok, mOnClick);
        }
        this.mDialogFragmentHometown.show(getFragmentManager(), "mDialogFragmentHometown");
    }

    /**
     * 修改名字提示框
     */
    private void showDialogFragmentChangeUserName(){
//        String disPlayName = this.mFamilyName + this.mName;
        String disPlayName = this.mFamilyName;
        String msg = getString(R.string.detail_edit_info_prompt_save_name, disPlayName);
        if(ResourceHelper.isNull(this.mDialogFragmentChangeUserName)){
            CustomDialogFragment.DialogFragmentInterface onClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        saveUserInfo();
                    }
                }
            };
            this.mDialogFragmentChangeUserName = DialogFragmentHelper.showCustomDialogFragment(R.string.warm_prompt ,msg, R.string.frg_text_cancel, R.string.frg_text_ok, onClick);
        }
        this.mDialogFragmentChangeUserName.setMessage(msg);
        this.mDialogFragmentChangeUserName.show(getFragmentManager(), "mDialogFragmentChangeUserName");
    }

    /**
     * 显示大图
     */
    private void showDialogFragmentHeadMax(String headImg){
        this.mShowMaxHeadUrl = headImg;
        if(ResourceHelper.isNull(this.mDialogFragmentHeadMax)){
            this.mDialogFragmentHeadMax = DialogFragmentHelper.showUserHeadMaxDialogFragment(headImg,R.string.frg_text_cancel ,R.string.frg_text_ok);
            this.mDialogFragmentHeadMax.setTargetFragment(this, Constants.TakePicture.REQUEST_CODE_CHOOSE_HEAD);
        }
        this.mDialogFragmentHeadMax.setHeadImg(headImg);
        this.mDialogFragmentHeadMax.show(getFragmentManager(),"mDialogFragmentHeadMax");
    }

    /**
     * 上传头像
     * @param position
     */
    private void uploadHead(int position){
        String headPath = this.mHeadAddlist.get(position);
        String uploadUrl = this.mUrlEntity.getImgBaseService() + this.mUrlEntity.getCommonUpload();
        uploadUrl += "?"+ "category" +"=head";
        String newFileName = "";
        JSONObject json = new JSONObject();
        ReqAndRespCenter.getInstance().upload(Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_HEAD, uploadUrl, headPath, newFileName, json, null, this, position);
    }

    /**
     * 上传用户资料
     */
    private void uploadUserInfo(){
        UserInfoSaveJsonEntity saveUserInfo = new UserInfoSaveJsonEntity();
        String name = mFamilyName.substring(1,mFamilyName.length());
//        saveUserInfo.setSurname(this.mFamilyName);
        saveUserInfo.setSurname(name);
        this.mName = this.mFamilyName.substring(0,1);
        saveUserInfo.setFirstName(this.mName);//设置用户资料
        String text = this.mEdtIntro.getText().toString();
        saveUserInfo.setIntro(text);//个性签名
        this.mGraduateInstitutions =  this.mEdtGraduateInstitutions.getText().toString();
        saveUserInfo.setSchool(this.mGraduateInstitutions);
        saveUserInfo.setBirthday(this.mBirthday);
        saveUserInfo.setEduBackground(this.mEducation);
        saveUserInfo.setNativeplace(this.mHometown);
        saveUserInfo.setWorkHistory(getWorkExperience());
        saveUserInfo.setEduInfo(getEducationalInformation());
        saveUserInfo.setProvince(this.mProvince);
        saveUserInfo.setSex(this.mDialogFragmentSexPosition);//设置性别
        saveUserInfo.setHeadImg(ResourceHelper.getStringFromList(this.mHeadUrlList, Constants.Other.CONTENT_ARRAY_SPLIT));
        String json = JsonHelper.toJson(saveUserInfo, UserInfoSaveJsonEntity.class);
        String requestUrl = ProjectHelper.fillRequestURL(this.mUrlEntity.getMyInfoSave());
        String token =  CurrentUser.getInstance().getToken();
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_MY_INFO_SAVE, requestUrl, token, json, this);
    }

    /**
     * 保存资料
     */
    private void saveUserInfo(){
        ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
        if(Helper.isNotNull(this.mHeadAddlist)){
            uploadHead(0);
        }else{
            uploadUserInfo();
        }
    }

}
