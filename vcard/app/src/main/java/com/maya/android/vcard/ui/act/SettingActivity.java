package com.maya.android.vcard.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.ui.base.BaseActivity;
import com.maya.android.vcard.ui.frg.SettingMainFragment;

/**
 * Activity：设置
 */
public class SettingActivity extends BaseActivity {

    private static final String TAG = SettingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.initUI();
        this.initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ReqAndRespCenter.getInstance().requestSaveSetting(CurrentUser.getInstance().getSetting());
    }

    @Override
    public void switchTo(Class<?> cls, Intent intent) {
        if(Helper.isNull(intent)){
            intent = new Intent();
        }
        ActivityHelper.switchTo(this, cls, intent);
    }

    private void initUI(){
        super.initTop();
        super.setTopLeftVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String frgName = intent.getStringExtra(Constants.IntentSet.KEY_FRG_NAME);
        Bundle bundle = intent.getBundleExtra(Constants.IntentSet.KEY_FRG_BUNDLE);
        if(Helper.isNull(frgName) || "".equals(frgName)){
            frgName = SettingMainFragment.class.getName();
        }
        this.changeFragment(frgName, bundle, false);
    }

    private void initData(){

    }



}
