package com.maya.android.vcard.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.ui.base.BaseActivity;

public class WebActivity extends BaseActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initUI();
        this.initData();
    }

    @Override
    public void switchTo(Class<?> cls, Intent intent) {

    }

    private void initUI(){
        setContentView(R.layout.activity_web);
        super.initTop();
        super.setTopLeftVisibility(View.VISIBLE);
        this.mWebView = findView(R.id.wbv_act_web);
    }

    private void initData(){
        int intentCode = getIntent().getIntExtra(Constants.IntentSet.KEY_INTENT_CODE, 0);
        switch (intentCode){
            //隐私条款
            case Constants.IntentSet.INTENT_CODE_STATEMENT:
                super.setTopTitle(R.string.agreement, false);
                this.mWebView.loadUrl("file:///android_asset/statement.html");
                break;
            //使用帮助
            case Constants.IntentSet.INTENT_CODE_SET_HELP:
                super.setTopTitle(R.string.using_help, false);
                this.mWebView.setWebChromeClient(new WebChromeClient());
                this.mWebView.getSettings().setJavaScriptEnabled(true);
                this.mWebView.loadUrl("file:///android_asset/help.html");
                break;
            //鸣谢
            case Constants.IntentSet.INTENT_CODE_SET_THANKS:
                super.setTopTitle(R.string.thanks, false);
                this.mWebView.loadUrl("file:///android_asset/thanks.html");
                break;
            //功能介绍
            case Constants.IntentSet.INTENT_CODE_FUNCTION_INTRODUCE:
                super.setTopTitle(R.string.fuction_introduce, false);
                this.mWebView.loadUrl("file:///android_asset/introduce.html");
                break;
            //添加名片技巧
            case Constants.IntentSet.INTENT_CODE_ADD_CARD_EXPLAIN:
                super.setTopTitle(R.string.add_card_explain, false);
                this.mWebView.loadUrl("file:///android_asset/add_card_explain.html");
                break;
            //绿色公益
            case Constants.IntentSet.INTENT_CODE_GREEN_PUBLIC:
                super.setTopTitle(R.string.green_public, false);
                this.mWebView.loadUrl("file:///android_asset/green_public.html");
                break;
        }
    }
}
