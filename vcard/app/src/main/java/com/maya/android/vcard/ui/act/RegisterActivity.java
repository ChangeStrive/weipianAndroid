package com.maya.android.vcard.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.ui.base.BaseActivity;
import com.maya.android.vcard.ui.frg.RegisterEmailFragment;
import com.maya.android.vcard.ui.frg.RegisterMobileFragment;

/**
 * Activity:注册
 * 包含如下frg：
 * <ol>
 *     <li>{@link RegisterMobileFragment}</li>
 *     <li>{@link RegisterEmailFragment}</li>
 * </ol>
 */
public class RegisterActivity extends BaseActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.initUI();
        this.initData();
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
        this.mFrgManager = getSupportFragmentManager();
        Intent intent = getIntent();
        String frgName = intent.getStringExtra(Constants.IntentSet.KEY_FRG_NAME);
        Bundle bundle = intent.getBundleExtra(Constants.IntentSet.KEY_FRG_BUNDLE);
        if(Helper.isEmpty(frgName)){
            frgName = RegisterMobileFragment.class.getName();
        }
        this.changeFragment(frgName, bundle, false);
    }

    private void initData(){

    }

}
