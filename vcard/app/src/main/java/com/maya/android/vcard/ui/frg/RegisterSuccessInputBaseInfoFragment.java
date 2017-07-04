package com.maya.android.vcard.ui.frg;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.maya.android.vcard.ui.act.VCardMainActivity;
import com.maya.android.vcard.ui.adapter.CustomLsvAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.xbdialog.DialogDate;
import com.maya.android.vcard.ui.widget.xbdialog.Dialogim;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONObject;

/**
 * 注册成功：填写基本资料
 */
public class RegisterSuccessInputBaseInfoFragment extends BaseFragment {

//    private String mFamilyName, mName, mSex, mConstellation, mAge, mBirthday, mEducation, mHometown, mProvince, mGraduateInstitutions;
    private String mFamilyName, mName,  mBirthday, mEducation, mHometown, mProvince;
//    private int mDialogFragmentSexPosition, mDialogFragmentEducationPosition;
    private int mDialogFragmentSexPosition;
//    private CustomDialogFragment mDialogFragmentSex, mDialogFragmentEducation, mDialogFragmentHometown;
    private CustomDialogFragment   mDialogFragmentHometown;
    //    private EditText mEdtFamilyName, mEdtName, mEdtGraduateInstitutions;
//    private EditText mEdtFamilyName, mEdtGraduateInstitutions;
    private EditText mEdtFamilyName;
    //    private TextView mTxvSex, mTxvConstellation, mTxvAge, mTxvBirthday, mTxvEducation, mTxvHometown;
    private TextView mTxvBirthday, mTxvHometown;
    private Button mBtnSubmit;
    private CurrentUser mCurrentUser = CurrentUser.getInstance();
    private CustomDialogFragment mDialogFragmentDate;
    private RadioGroup sexRadioGroup;//性别选择
    private RadioButton male, female;//男、女

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO d点击事件
            switch (v.getId()) {
                case R.id.btn_submit:
                    //确定
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
                    if (isValidAllData()) {
                        requestSaveUserInfo();
                    }
                    break;
//                case R.id.txv_sex:
//                    //性别
//                    showDialogFragmentSex();
//                    break;
//                case R.id.txv_constellation:
//                case R.id.txv_age:
                case R.id.txv_birthday:
                    //选择出生年月
                    showDialogFragmentDate();
                    break;
//                case R.id.txv_education:
//                    //学历
//                    showDialogFragmentEducation();
//                    break;
                case R.id.txv_hometown:
                    //家乡
                    showDialogFragmentHometown();
                    break;

            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_success_input_base_info, container, false);
        this.mEdtFamilyName = (EditText) view.findViewById(R.id.edt_family_name);//姓名
        // 名字 this.mEdtName = (EditText) view.findViewById(R.id.edt_name);   名字
//        this.mEdtGraduateInstitutions = (EditText) view.findViewById(R.id.edt_graduate_institutions);毕业院校
//        this.mTxvSex = (TextView) view.findViewById(R.id.txv_sex);
        //性别选择按钮
        sexRadioGroup = (RadioGroup) view.findViewById(R.id.rg_sex);
        male = (RadioButton) view.findViewById(R.id.male);
        female = (RadioButton) view.findViewById(R.id.female);
        mDialogFragmentSexPosition = 1;
        sexRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (male.getId() == checkedId) {//男
                    mDialogFragmentSexPosition = 1;
                }
                if (female.getId() == checkedId) {//女
                    mDialogFragmentSexPosition = 0;
                }
            }
        });

        // this.mTxvConstellation = (TextView) view.findViewById(R.id.txv_constellation); 星座
//        this.mTxvAge = (TextView) view.findViewById(R.id.txv_age); 年龄
        this.mTxvBirthday = (TextView) view.findViewById(R.id.txv_birthday);
//        this.mTxvEducation = (TextView) view.findViewById(R.id.txv_education);学历
        this.mTxvHometown = (TextView) view.findViewById(R.id.txv_hometown);
        TextView txvVCandNo = findView(view, R.id.txv_vcard_number);//微号
        txvVCandNo.setText(CurrentUser.getInstance().getVCardNo());//设置微号
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
//        this.mTxvSex.setOnClickListener(this.mOnClickListener);
//        this.mTxvConstellation.setOnClickListener(this.mOnClickListener);
        this.mTxvBirthday.setOnClickListener(this.mOnClickListener);
//        this.mTxvAge.setOnClickListener(this.mOnClickListener);
        this.mTxvHometown.setOnClickListener(this.mOnClickListener);
//        this.mTxvEducation.setOnClickListener(this.mOnClickListener);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        addTextChange();
        return view;
    }

    //添家监听
    private void addTextChange() {
        mTxvHometown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s)){
                    mBtnSubmit.setTextColor(getResources().getColor(R.color.color_a1a1aa));
                }else {
                    mBtnSubmit.setTextColor(getResources().getColor(R.color.color_2d659f));
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTitle(R.string.base_info_input, true);
        super.mTitleAction.setActivityTopLeftVisibility(View.GONE);
        //设置Activity不能回退
        super.mFragmentInteractionImpl.onActivitySetBackPressed(false);
    }

    @Override
    protected void onBackPressed() {
        super.onBackPressed();
        ActivityHelper.showToast(R.string.please_input_base_info);
    }

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if (!super.onCommandCallback2(tag, commandResult, objects)) {
            //设置不需要再输入基本信息     我写的
//            this.mCurrentUser.setNeedCompleteInfo(false);
            switch (tag) {
                case Constants.CommandRequestTag.CMD_REQUEST_MY_INFO_SAVE:
                    ActivityHelper.closeProgressDialog();
                    this.mCurrentUser.setNeedCompleteInfo(false);
                    //跳转到名片主页
                    this.mActivitySwitchTo.switchTo(VCardMainActivity.class, null);
                    break;
            }
        }
        return true;
    }

    /**
     * 输入判空验证
     *
     * @return
     */
    private boolean isValidAllData() {
//       姓名
        this.mFamilyName = this.mEdtFamilyName.getText().toString();
//        this.mName = this.mEdtName.getText().toString();
//        if(ResourceHelper.isEmpty(this.mFamilyName) || ResourceHelper.isEmpty(this.mName)){
//            ActivityHelper.showToast(R.string.please_enter_name);
//            return false;
//        }
        if (ResourceHelper.isEmpty(this.mFamilyName)) {
            ActivityHelper.showToast(R.string.please_enter_name);
            return false;
        }
////        性别
//        this.mSex = this.mTxvSex.getText().toString();
//        if (ResourceHelper.isEmpty(this.mSex)) {
//            ActivityHelper.showToast(R.string.please_selected_sex);
//            return false;
//        }
//        出生年月、年龄、星座
        this.mBirthday = this.mTxvBirthday.getText().toString();
//        this.mAge = this.mTxvAge.getText().toString();
//        this.mConstellation = this.mTxvConstellation.getText().toString();
//        if (ResourceHelper.isEmpty(this.mBirthday) || ResourceHelper.isEmpty(this.mAge) || ResourceHelper.isEmpty(this.mConstellation)) {
//            ActivityHelper.showToast(R.string.please_selected_birthday);
//            return false;
//        }
        if (ResourceHelper.isEmpty(this.mBirthday)) {
            ActivityHelper.showToast(R.string.please_selected_birthday);
            return false;
        }
//        学历
//        this.mEducation = this.mTxvEducation.getText().toString();
//        if (ResourceHelper.isEmpty(this.mEducation)) {
//            ActivityHelper.showToast(R.string.please_selected_education);
//            return false;
//        }
//        家乡
//        this.mHometown = this.mTxvHometown.getText().toString();
        if (ResourceHelper.isEmpty(this.mHometown)) {
            ActivityHelper.showToast(R.string.please_selected_hometown);
            return false;
        }
        return true;
    }

    /**
     * 选择性别对方框
     */
//    private void showDialogFragmentSex() {
//        if (Helper.isNull(this.mDialogFragmentSex)) {
//            ListView mLsvItems = DialogFragmentHelper.getCustomContentView(getActivity());
//            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
//            mLsvItems.setAdapter(mLsvAdapter);
//            mLsvAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.sex));
//            mLsvAdapter.setPosition(this.mDialogFragmentSexPosition);
//            mLsvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    mDialogFragmentSexPosition = position;
//                    mLsvAdapter.setPosition(position);
//                }
//            });
//            CustomDialogFragment.DialogFragmentInterface mDlgOnClick = new CustomDialogFragment.DialogFragmentInterface() {
//                @Override
//                public void onDialogClick(int which) {
//                    if (which == CustomDialogFragment.BUTTON_POSITIVE) {
//                        mTxvSex.setText(mLsvAdapter.getItem(mDialogFragmentSexPosition));
//                    }
//                }
//            };
//            this.mDialogFragmentSex = DialogFragmentHelper.showCustomDialogFragment(R.string.selected_sex, mLsvItems, R.string.frg_text_cancel, R.string.frg_text_ok, mDlgOnClick);
//            this.mDialogFragmentSex.show(getFragmentManager(), "mDialogFragmentSex");
//        } else {
//            this.mDialogFragmentSex.show(getFragmentManager(), "mDialogFragmentSex");
//        }
//    }

    /**
     * 选择学历对话框
     */
//    private void showDialogFragmentEducation() {
//        if (Helper.isNull(this.mDialogFragmentEducation)) {
//            ListView mLsvItems = DialogFragmentHelper.getCustomContentView(getActivity());
//            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
//            mLsvItems.setAdapter(mLsvAdapter);
//            mLsvAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.education));
//            mLsvAdapter.setPosition(this.mDialogFragmentEducationPosition);
//            mLsvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    mDialogFragmentEducationPosition = position;
//                    mLsvAdapter.setPosition(position);
//                }
//            });
//            CustomDialogFragment.DialogFragmentInterface mDlgOnClick = new CustomDialogFragment.DialogFragmentInterface() {
//                @Override
//                public void onDialogClick(int which) {
//                    if (which == CustomDialogFragment.BUTTON_POSITIVE) {
//                        mTxvEducation.setText(mLsvAdapter.getItem(mDialogFragmentEducationPosition));
//                    }
//                }
//            };
//            this.mDialogFragmentEducation = DialogFragmentHelper.showCustomDialogFragment(R.string.selected_education, mLsvItems, R.string.frg_text_cancel, R.string.frg_text_ok, mDlgOnClick);
//            this.mDialogFragmentEducation.show(getFragmentManager(), "mDialogFragmentEducation");
//        } else {
//            this.mDialogFragmentEducation.show(getFragmentManager(), "mDialogFragmentEducation");
//        }
//    }

    /**
     * 选择出生日期对话框
     */
    private void showDialogFragmentDate() {
        if (ResourceHelper.isNull(this.mDialogFragmentDate)) {
            View view = DialogDate.getIntance().getDialogDate(getActivity(), 1900, 2100);
            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if (CustomDialogFragment.BUTTON_POSITIVE == which) {
                        String strDate = DialogDate.getIntance().getDates();
                        String age = ResourceHelper.getAgeFromBirthday(strDate);
                        if (Helper.isNotNull(age)) {
                            mTxvBirthday.setText(strDate);
//                            mTxvConstellation.setText(ResourceHelper.getAstro(strDate));
//                            mTxvAge.setText(age);
                        }
                    }
                }
            };
            this.mDialogFragmentDate = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_text_cancel, view, R.string.frg_text_cancel, R.string.frg_text_ok, mOnClick);

        }
        this.mDialogFragmentDate.show(getFragmentManager(), "mDialogFragmentDate");
    }

    /**
     * 请求保存资料
     */
    private void requestSaveUserInfo() {
        UserInfoSaveJsonEntity saveUserInfo = new UserInfoSaveJsonEntity();
        String name = mFamilyName.substring(1, mFamilyName.length());//姓名
        saveUserInfo.setSurname(name);
//        saveUserInfo.setFirstName(this.mName);
         mName = mFamilyName.substring(0,1);//姓氏
        saveUserInfo.setFirstName(this.mName);
        saveUserInfo.setBirthday(this.mBirthday);
//        saveUserInfo.setEduBackground(this.mEducation);//学历
        this.mEducation = "小学";
        saveUserInfo.setEduBackground(this.mEducation);//学历
        saveUserInfo.setNativeplace(this.mHometown);//籍贯
        saveUserInfo.setProvince(this.mProvince);//省
        saveUserInfo.setSex(this.mDialogFragmentSexPosition);//性别0 、1表示
//        this.mGraduateInstitutions = this.mEdtGraduateInstitutions.getText().toString();
//        saveUserInfo.setSchool(this.mGraduateInstitutions);
        String json = JsonHelper.toJson(saveUserInfo, UserInfoSaveJsonEntity.class);
        URLResultEntity mUrlEntity = CurrentUser.getInstance().getURLEntity();
        String requestUrl = ProjectHelper.fillRequestURL(mUrlEntity.getMyInfoSave());
        String token = CurrentUser.getInstance().getToken();
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_MY_INFO_SAVE, requestUrl, token, json, this);
    }

    /**
     * 选择城市对话框
     */
    private void showDialogFragmentHometown() {
        if (ResourceHelper.isNull(this.mDialogFragmentHometown)) {
            View view = Dialogim.getIntance().getDialogim(getActivity());
            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if (CustomDialogFragment.BUTTON_POSITIVE == which) {
//                        mTxvHometown.setText(Dialogim.getIntance().getCities());
                        //省份
                        String myHome = Dialogim.getIntance().getProvinces() + "省-" + Dialogim.getIntance().getCities() + "市-" + Dialogim.getIntance().getCounties() + "区";
                        mTxvHometown.setText(myHome);
                        mHometown = Dialogim.getIntance().getProvinces();
                        mProvince = Dialogim.getIntance().getProvinces();
                    }
                }
            };
            this.mDialogFragmentHometown = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_text_cancel, view, R.string.frg_text_cancel, R.string.frg_text_ok, mOnClick);
        }
        this.mDialogFragmentHometown.show(getFragmentManager(), "mDialogFragmentHometown");
    }
}
