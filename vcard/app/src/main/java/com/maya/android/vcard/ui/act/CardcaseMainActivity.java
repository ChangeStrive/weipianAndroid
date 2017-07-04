package com.maya.android.vcard.ui.act;

import android.content.Intent;
import android.os.Bundle;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.ui.base.BaseActivity;
import com.maya.android.vcard.ui.frg.VCardSwapRadarFragment;

/**
 * 名片夹
 */
public class CardcaseMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardcase_main);
        this.initUI();
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
        Intent intent = getIntent();
        String frgName = intent.getStringExtra(Constants.IntentSet.KEY_FRG_NAME);
        Bundle bundle = intent.getBundleExtra(Constants.IntentSet.KEY_FRG_BUNDLE);
        if(Helper.isNull(frgName) || "".equals(frgName)){
            frgName = VCardSwapRadarFragment.class.getName();
        }
        this.changeFragment(frgName, bundle, false);
    }
}
