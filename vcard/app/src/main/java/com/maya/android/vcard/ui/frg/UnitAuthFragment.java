package com.maya.android.vcard.ui.frg;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.dao.UnitDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.json.UnitAuthJsonEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UnitMemberResultEntity;
import com.maya.android.vcard.entity.result.UnitResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.adapter.CustomLsvAdapter;
import com.maya.android.vcard.ui.adapter.CustomLsvIconAdapter;
import com.maya.android.vcard.ui.adapter.UnitAuthGridAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.NoScrolGridView;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * 单位：认证
 */
public class UnitAuthFragment extends BaseFragment {
    /** 单位Id key **/
    public static final String KEY_UNIT_AUTH_UNIT_ID = "key_unit_auth_unit_id";
    public static final String KEY_FOUNDER_ADD = "key_founder_add";
    private static final int FILE_MAX_SIZE = 150;
    /** 创始人存储管理员编号 **/
    private static final int KEY_FOUNDER = 10000001;
    /** 需要添加的管理员编号 **/
    private int mPositionKey = -1;
    /** 单位Id **/
    private long unitId;
    /** 单位信息 **/
    private UnitResultEntity unit;
    /** 当前名片Id **/
    private long curCardId;
    /** 记录管理成员 **/
    private SparseArray<UnitMemberResultEntity> memberAdministrator = new SparseArray<UnitMemberResultEntity>();
    /** 用户资料 **/
    private UserInfoResultEntity userInfo;
    /** 名片信息 **/
    private CardEntity card;
    /** 上传链接 **/
    private URLResultEntity mUrlEntity;
    private String mUnitName, mUnitFounder, mUnitPhone, mUnitEmail, mUnitAddress;
    private ArrayList<UnitMemberResultEntity> founders;
    private ArrayList<UnitMemberResultEntity> members;
    private TextView mTxvIndustryCategory, mTxvUnitNature, mTxvSuperAdministrator;
    private TextView mEdtUnitName, mEdtLegalRepressentative, mEdtContactPhone, mEdtEmail, mEdtUnitAddress;
    private AsyncImageView  mImvCopy, mImvCertificate;
    private Button mBtnSubmit;
    private int mBusinessPosition;
    private int mPropertyPostion = -1;
    private int mAdministratorPosition;//记录当前选中项
    private CustomDialogFragment mDialogFragmentBusiness, mDialogFragmentProperty, mDialogFragmentFileSizeMax, mDialogFragmentDelAdministrator;
    private String mCopyFilePath, mCertificateFilePath, mCopyUrl, mCertificateUrl;
    private int mRequestCode;
    private NoScrolGridView mGridAdministrator;
    private UnitAuthGridAdapter mUnitAuthGridAdapter;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_submit:
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
                    if(!NetworkHelper.isNetworkAvailable(getActivity())){
                        uploadUnitAuthInfo();
                    }else{
                        ActivityHelper.showToast(R.string.no_network);
                    }
                    break;
                case R.id.txv_unit_nature:
                    //单位性质
                    showDialogFragmentProperty();
                    break;
                case R.id.txv_industry_category:
                    //行业
                    showDialogFragmentBusiness();
                    break;
                case R.id.imv_approve_license_copy:
                    //营业执照副本
                    toChoosePicture(Constants.RequestCode.REQUEST_CODE_ADD_LICENSE_COPY);
                    break;
                case R.id.imv_approve_organization_certificate:
                    //组织机构代码证或税务证
                    toChoosePicture(Constants.RequestCode.REQUEST_CODE_ADD_TAX_CERTIFICATE);
                    break;
            }
        }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_ENTERPRISE_LICENSE_COPY:
                    //上传营业执照副本回调
                    try {
                        JSONArray arrCopyUrl = commandResult.getJSONArray("fileUrl");
                        this.mCopyUrl = arrCopyUrl.getString(0);
                        // 上传组织机构
                        uploadPicture(Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_ENTERPRISE_TAX_CERTIFICATE, this.mCertificateFilePath);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_ENTERPRISE_TAX_CERTIFICATE:
                    // 提交认证
                    try {
                        JSONArray arrCertUrl = commandResult.getJSONArray("fileUrl");
                        this.mCertificateUrl = arrCertUrl.getString(0);
                        uploadUnitInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
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
    }

    @Override
    protected void onResponseSuccess(int tag, String msgInfo) {
        super.onResponseSuccess(tag, msgInfo);
        ActivityHelper.closeProgressDialog();
        if(tag == Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_APPROVE){
            ActivityHelper.showToast(R.string.auth_certify_waiting);
            //将数据库设置为申请认证
            UnitDao.getInstance().updateApproval(this.unitId, DatabaseConstant.EnterpriseApproval.APPROVALING);
            //TODO 已申请认证回调
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constants.RequestCode.REQUEST_CODE_ADD_LICENSE_COPY:
                if(Activity.RESULT_OK == resultCode){
                    this.mCopyFilePath = ResourceHelper.getChoosePhotoPath(data, getActivity());
                    if(Helper.isNotEmpty(mCopyFilePath)){
                        //判断图片大小
                        File imgFile = new File(mCopyFilePath);
                        long fileLen = imgFile.length() / 1024;
                        if(fileLen > FILE_MAX_SIZE){
                            showDialogFragmentFileSizeMax(Constants.RequestCode.REQUEST_CODE_ADD_LICENSE_COPY);
                        }else{
                            this.mImvCopy.autoFillFromUrl(this.mCopyFilePath);
                            this.mImvCopy.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                    }else{
                        ActivityHelper.showToast(R.string.company_certify_load_img_error);
                    }
                }
                break;
            case Constants.RequestCode.REQUEST_CODE_ADD_TAX_CERTIFICATE:
                if(Activity.RESULT_OK == resultCode){
                    this.mCertificateFilePath = ResourceHelper.getChoosePhotoPath(data, getActivity());
                    if(ResourceHelper.isNotEmpty(this.mCertificateFilePath)){
                        //判断图片大小
                        File imgFile = new File(this.mCertificateFilePath);
                        long fileLen = imgFile.length() / 1024;
                        if(fileLen > FILE_MAX_SIZE){
                            showDialogFragmentFileSizeMax(Constants.RequestCode.REQUEST_CODE_ADD_LICENSE_COPY);
                        }else{
                            this.mImvCertificate.autoFillFromUrl(this.mCertificateFilePath);
                            this.mImvCertificate.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                    }else{
                        ActivityHelper.showToast(R.string.company_certify_load_img_error);
                    }
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unit_auth, container, false);
        this.mTxvIndustryCategory = (TextView) view.findViewById(R.id.txv_industry_category);
        this.mTxvUnitNature = (TextView) view.findViewById(R.id.txv_unit_nature);
        this.mEdtUnitName = (EditText) view.findViewById(R.id.edt_unit_name);
        this.mEdtLegalRepressentative = (EditText) view.findViewById(R.id.edt_legal_representative);
        this.mEdtContactPhone = (EditText) view.findViewById(R.id.edt_contact_phone);
        this.mEdtEmail = (EditText) view.findViewById(R.id.edt_email_english);
        this.mEdtUnitAddress = (EditText) view.findViewById(R.id.edt_unit_address);
        this.mTxvSuperAdministrator = (TextView) view.findViewById(R.id.txv_super_administrator);
        this.mImvCopy = (AsyncImageView) view.findViewById(R.id.imv_approve_license_copy);
        this.mImvCertificate = (AsyncImageView) view.findViewById(R.id.imv_approve_organization_certificate);
        this.mGridAdministrator = (NoScrolGridView) view.findViewById(R.id.grv_administrator_head);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        this.mTxvUnitNature.setOnClickListener(this.mOnClickListener);
        this.mTxvIndustryCategory.setOnClickListener(this.mOnClickListener);
        this.mImvCopy.setOnClickListener(this.mOnClickListener);
        this.mImvCertificate.setOnClickListener(this.mOnClickListener);
        this.mEdtLegalRepressentative.addTextChangedListener(this.mLegalRepresentative);
        this.mGridAdministrator.setOnItemLongClickListener(this.mOnItemLongClickListener);
        this.mGridAdministrator.setOnItemClickListener(this.mOnItemClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.setActivityTitle(R.string.unit_auth, false);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
            this.unitId = bundle.getLong(KEY_UNIT_AUTH_UNIT_ID, 0);
            this.mPositionKey = bundle.getInt(KEY_FOUNDER_ADD, -1);
        }
        this.mUnitAuthGridAdapter = new UnitAuthGridAdapter(getActivity());
        this.mGridAdministrator.setAdapter(this.mUnitAuthGridAdapter);
        if(ResourceHelper.isNull(this.userInfo)){
            this.userInfo =  CurrentUser.getInstance().getUserInfoEntity();
        }
        if(ResourceHelper.isNull(this.unit)){
            this.unit = UnitDao.getInstance().getEntity(this.unitId);
        }
        if(ResourceHelper.isNull(this.card)){
            this.card =  CurrentUser.getInstance().getCurrentVCardEntity();
        }
        if(ResourceHelper.isNotNull(this.card)){
            this.curCardId = this.card.getId();
        }
        fillData(this.userInfo, this.unit);
        this.mUnitAuthGridAdapter.addItems(getArrayListAdministrator(this.userInfo));
    }

    private void fillData(UserInfoResultEntity userInfo, UnitResultEntity unit){
        if(ResourceHelper.isNotNull(unit)){
            //单位名称
            this.mEdtUnitName.setText(unit.getEnterpriseName());
            //单位地址
            this.mEdtUnitAddress.setText(unit.getAddress());
            //行业类别
            this.mBusinessPosition = unit.getClassLabel();
            this.mTxvIndustryCategory.setText(ResourceHelper.getBusinessNameByCode(getActivity(), this.mBusinessPosition));
        }
        if(ResourceHelper.isNotNull(userInfo)){
            //法人代表
            this.mEdtLegalRepressentative.setText(userInfo.getDisplayName());
            this.mTxvSuperAdministrator.setText(userInfo.getDisplayName());
        }
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (ResourceHelper.isEmpty(mUnitAuthGridAdapter.getItem(position).getHeadImg())) {
                Bundle bundle = new Bundle();
                bundle.putLong(UnitRememberFragment.KEY_UNIT_REMEMBER_UNIT_ID, unitId);
                bundle.putInt(UnitRememberFragment.KEY_UNIT_REMEMBER, Constants.UNIT.ERTERPRISE_APPROVAL);
                mFragmentInteractionImpl.onFragmentInteraction(UnitRememberFragment.class.getName() ,bundle);
            }
        }
    };

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (ResourceHelper.isNotEmpty(mUnitAuthGridAdapter.getItem(position).getHeadImg())) {
                showDialogFragmentDelAdministrator(position);
            }
            return true;
        }
    };

    /**
     * 法人代表输入监听
     */
    private TextWatcher mLegalRepresentative = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mTxvSuperAdministrator.setText(s.toString());
        }
    };

    /**
     * 获取管理员
     * @return
     */
    private ArrayList<UnitMemberResultEntity> getArrayListAdministrator(UserInfoResultEntity userInfo){
        if(ResourceHelper.isNull(founders)){
            this.founders = new ArrayList<UnitMemberResultEntity>();
        }
        this.founders.clear();
        UnitMemberResultEntity curFounder = new UnitMemberResultEntity();
        if(ResourceHelper.isNull(this.memberAdministrator.get(KEY_FOUNDER, null))){
            if(ResourceHelper.isNotNull(userInfo)){
                curFounder.setAccountId(userInfo.getId());
                curFounder.setSurname(userInfo.getSurname());
                curFounder.setFirstName(userInfo.getFirstName());
                curFounder.setHeadImg(userInfo.getHeadImg());
                curFounder.setNickName(userInfo.getNickName());
                curFounder.setRole(DatabaseConstant.Role.ADMIN);
                curFounder.setCardId(this.curCardId);
            }
            curFounder.setEnterpriseId(this.unitId);
            this.memberAdministrator.put(KEY_FOUNDER, curFounder);
        }
        if(this.mPositionKey >= 0){
            if(ResourceHelper.isNull(this.members)){
                this.members = UnitDao.getInstance().getMemberList(this.unitId, this.curCardId);
            }
            if(ResourceHelper.isNull(this.memberAdministrator.get(this.mPositionKey, null))){
                this.memberAdministrator.put(this.mPositionKey, this.members.get(this.mPositionKey));
            }
        }
        int count = this.memberAdministrator.size();
        int key;
        UnitMemberResultEntity founder;
        for(int i = 0; i < count; i++){
            key = this.memberAdministrator.keyAt(i);
            founder = this.memberAdministrator.get(key, null);
            if(ResourceHelper.isNotNull(founder)){
                founders.add(founder);
            }
        }
        return founders;
    }

    /**
     * 图库选择图片
     * @param requestCode
     */
    private void toChoosePicture(int requestCode){
        Intent mIntentPicture = new Intent(Intent.ACTION_GET_CONTENT);
        mIntentPicture.setType("image/*");
        startActivityForResult(mIntentPicture, requestCode);
    }

    /**
     * 行业类别对话框
     */
    private void showDialogFragmentBusiness(){
        if(ResourceHelper.isNull(this.mDialogFragmentBusiness)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvIconAdapter customIconAdapter = new CustomLsvIconAdapter(getActivity());
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mTxvIndustryCategory.setText(customIconAdapter.getItem(position).getBusinessName());
                    mBusinessPosition = position;
                    mDialogFragmentBusiness.getDialog().dismiss();
                }
            });
            mLsvItem.setAdapter(customIconAdapter);
            customIconAdapter.addItems(ResourceHelper.getArrayListBusiness());
            this.mDialogFragmentBusiness = DialogFragmentHelper.showCustomDialogFragment(R.string.select_business, mLsvItem, true);
        }
        this.mDialogFragmentBusiness.show(getFragmentManager(), "mDialogFragmentBusiness");
    }

    /**
     * 单位性质对话框
     */
    private void showDialogFragmentProperty(){
        if(ResourceHelper.isNull(this.mDialogFragmentProperty)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mTxvUnitNature.setText(mLsvAdapter.getItem(position));
                    mPropertyPostion = position;
                    mDialogFragmentProperty.getDialog().dismiss();
                }
            });
            mLsvItem.setAdapter(mLsvAdapter);
            mLsvAdapter.addItems(DialogFragmentHelper.getListFromResArray(getActivity(), R.array.company_type));
            mLsvAdapter.setShowRadio(false);
            this.mDialogFragmentProperty = DialogFragmentHelper.showCustomDialogFragment(R.string.select_property, mLsvItem);
        }
        this.mDialogFragmentProperty.show(getFragmentManager(), "mDialogFragmentProperty");
    }

    /**
     * 上传文件失败对话框
     * @param requestCode
     */
    private void showDialogFragmentFileSizeMax(int requestCode){
        this.mRequestCode = requestCode;
        if(ResourceHelper.isNull(this.mDialogFragmentFileSizeMax)){
            CustomDialogFragment.DialogFragmentInterface onClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        toChoosePicture(mRequestCode);
                    }
                }
            };
            this.mDialogFragmentFileSizeMax = DialogFragmentHelper.showCustomDialogFragment(R.string.upload_fail_file, R.string.file_min_150kb, R.string.frg_text_cancel, R.string.frg_text_ok, onClick);
        }
        this.mDialogFragmentFileSizeMax.show(getFragmentManager(), "mDialogFragmentFileSizeMax");
    }

    /**
     * 删除管理员对话框
     * @param position
     */
    private void showDialogFragmentDelAdministrator(int position){
        this.mAdministratorPosition = position;
        if(ResourceHelper.isNull(this.mDialogFragmentDelAdministrator)){
            CustomDialogFragment.DialogFragmentInterface onClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        mUnitAuthGridAdapter.delItem(mAdministratorPosition);
                    }
                }
            };
            this.mDialogFragmentDelAdministrator = DialogFragmentHelper.showCustomDialogFragment(R.string.remove_administrator, R.string.confirm_remove_admin, R.string.frg_text_cancel, R.string.frg_text_ok, onClick);
        }

        this.mDialogFragmentDelAdministrator.show(getFragmentManager(), "mDialogFragmentDelAdministrator");
    }

    /**
     * 输入验证
     * @return
     */
    private boolean  isValidAllData(){
        this.mUnitName = this.mEdtUnitName.getText().toString();
        if(ResourceHelper.isEmpty(this.mUnitName)){
            return false;
        }
        if(this.mBusinessPosition < 0){
            return false;
        }
        if(this.mPropertyPostion < 0){
            return false;
        }
        this.mUnitFounder = this.mEdtLegalRepressentative.getText().toString();
        if(ResourceHelper.isEmpty(this.mUnitFounder)){
            return false;
        }
        this.mUnitPhone = this.mEdtContactPhone.getText().toString();
        if(ResourceHelper.isEmpty(this.mUnitPhone) ){
            return false;
        }
        this.mUnitEmail = this.mEdtEmail.getText().toString();
        if(ResourceHelper.isEmpty(this.mUnitEmail) || ResourceHelper.isValidEmail(this.mUnitEmail)){
            return false;
        }
        this.mUnitAddress = this.mEdtUnitAddress.getText().toString();
        if(ResourceHelper.isEmpty(this.mUnitAddress)){
            return false;
        }
        return true;
    }

    /**
     * 单位证件图片上传
     * @param tag
     * @param filePath
     */
    private void uploadPicture(int tag, String filePath){
        if(ResourceHelper.isNull(this.mUrlEntity)){
            this.mUrlEntity = CurrentUser.getInstance().getURLEntity();
        }
        String urlUpload = this.mUrlEntity.getImgBaseService().concat(this.mUrlEntity.getCommonUpload());
        ArrayList<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("category", "company"));
        String newFileName = "";
        ReqAndRespCenter.getInstance().upload(tag, urlUpload, filePath, newFileName, new JSONObject(), paramsList, this);
    }

    /**
     * 上传单位信息
     */
    private void uploadUnitInfo(){
        UnitAuthJsonEntity unitInfo = new UnitAuthJsonEntity();
        if(this.unitId > 0){
            unitInfo.setEnterpriseId(this.unitId);
        }
        unitInfo.setEnterpriseName(this.mUnitName);
        unitInfo.setApplyCardId(this.curCardId);
        unitInfo.setAddress(this.mUnitAddress);
        unitInfo.setEmail(this.mUnitEmail);
        unitInfo.setTelephone(this.mUnitPhone);
        unitInfo.setLegalPerson(this.mUnitFounder);
        unitInfo.setClassLabel(this.mBusinessPosition);
        unitInfo.setProperty(this.mPropertyPostion);
        unitInfo.setLicenseCopy(this.mCopyUrl);
        unitInfo.setTaxCertificate(this.mCertificateUrl);
        int count = this.memberAdministrator.size();
        Long[] mAdminCardIds = new Long[count];
        int key;
        UnitMemberResultEntity founder;
        for(int i = 0; i< count; i++){
            key = this.memberAdministrator.keyAt(i);
            founder = this.memberAdministrator.get(key, null);
            if(ResourceHelper.isNotNull(founder)){
                mAdminCardIds[i] = founder.getCardId();
            }
        }
        unitInfo.setAdminCardIds(mAdminCardIds);
        if(ResourceHelper.isNull(this.mUrlEntity)){
            this.mUrlEntity = CurrentUser.getInstance().getURLEntity();
        }
        String url = ProjectHelper.fillRequestURL(this.mUrlEntity.getEnterPriseApprove());
        String jsonStr = JsonHelper.toJson(unitInfo, UnitAuthJsonEntity.class);
        String mToken = CurrentUser.getInstance().getToken();
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_APPROVE, url, mToken, jsonStr, this);
    }

    /**
     * 上传单位认证信息
     */
    private void uploadUnitAuthInfo(){
        if(isValidAllData()){
            uploadPicture(Constants.CommandRequestTag.CMD_REQUEST_UPLOAD_ENTERPRISE_LICENSE_COPY, this.mCopyFilePath);
        }
    }
}
