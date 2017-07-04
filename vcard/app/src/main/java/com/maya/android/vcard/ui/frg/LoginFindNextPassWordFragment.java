package com.maya.android.vcard.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.base.BaseFragment;

/**
 * Created by Administrator on 2017/6/28.
 */
public class LoginFindNextPassWordFragment extends BaseFragment {

    private EditText etPassword;
    private EditText etRePassword;
    private Button btnSub;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_login_next_find_password, null);
        //密码
        etPassword = (EditText) view.findViewById(R.id.edt_password_new);
        //第二次密码
        etRePassword = (EditText) view.findViewById(R.id.edt_password_new_repetition);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.chb_is_show_password);//判断显示密码
        //提交并且登录
        btnSub = (Button) view.findViewById(R.id.btn_submit_login);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //是 显示密码
//                    etPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
//                    etRePassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etRePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
//                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT );
//                    etRePassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                    // 否 隐藏密码
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etRePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                String rePassword = etRePassword.getText().toString();
                if(!password.equals(rePassword)){
                    //Toast.makeText(getActivity(),"俩次密码不一样!",Toast.LENGTH_LONG).show();
                    ActivityHelper.showToast(R.string.entered_passwords_differ);
                }else {
                    ActivityHelper.showToast(R.string.login_submint);
                }
            }
        });
        addTextChange();
        return view;
    }
    //添加监听
    private void addTextChange() {
        etRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = etPassword.getText().toString();
                if(password.equals(s)){
                    btnSub.setTextColor(getResources().getColor(R.color.color_2d659f));
                }else {
                    btnSub.setTextColor(getResources().getColor(R.color.color_a1a1aa));
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTitle(R.string.find_password, true);
        this.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
    }
}
