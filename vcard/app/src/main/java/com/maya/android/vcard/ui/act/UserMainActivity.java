package com.maya.android.vcard.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.ui.base.BaseActivity;
import com.maya.android.vcard.ui.frg.UserInfoFragment;
import com.maya.android.vcard.ui.impl.OnNewIntentImpl;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * Activity:User
 */
public class UserMainActivity extends BaseActivity {
    private OnNewIntentImpl onNewIntentImplistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        this.initUI();
    }

    @Override
    public void switchTo(Class<?> cls, Intent intent) {
        if(Helper.isNull(intent)){
            intent = new Intent();
        }
        ActivityHelper.switchTo(this, cls, intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(ResourceHelper.isNotNull(this.onNewIntentImplistener)){
            this.onNewIntentImplistener.onNewIntent(intent);

        }
    }

    /**
     * onNewIntent接口
     * @param onNewIntentImplistener
     */
    public void setOnNewIntentImpl(OnNewIntentImpl onNewIntentImplistener){
        this.onNewIntentImplistener = onNewIntentImplistener;
    }

    private void initUI(){
        super.initTop();
        super.setTopLeftVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String frgName = intent.getStringExtra(Constants.IntentSet.KEY_FRG_NAME);
        Bundle bundle = intent.getBundleExtra(Constants.IntentSet.KEY_FRG_BUNDLE);
        if(Helper.isNull(frgName) || "".equals(frgName)){
            frgName = UserInfoFragment.class.getName();
        }
        this.changeFragment(frgName, bundle, false);
    }

}
