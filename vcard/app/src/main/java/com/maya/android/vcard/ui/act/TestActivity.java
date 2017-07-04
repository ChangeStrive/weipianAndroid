package com.maya.android.vcard.ui.act;

//import android.app.Fragment;
//import android.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.base.BaseActivity;
import com.maya.android.vcard.ui.frg.UserShareFragment;

/**
 * 测试Activity
 */
public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //名片夹
        Fragment fragment = null;
        //设置首页
//      fragment = SettingMainFragment.newInstance("","");
        //账户设置
//      fragment = SettingAccountFragment.newInstance("", "");
        //消息提醒
//        fragment = SettingNoticeFragment.newInstance("", "");
        //其他设置
//      fragment = SettingOtherFragment.newInstance("", "");
        //隐私设置
//      fragment = SettingPrivacyFragment.newInstance("", "");
        //修改密码
//      fragment = SettingAccountChangePasswordFragment.newInstance("", "");
        //备份设置
//      fragment = SettingOtherBackupFragment.newInstance("", "");
        //登录页面
//        fragment = new LoginFragment();
        //交换模式
//      fragment = SettingPrivacySwapModelFragment.newInstance("", "");
        //备份日志
//      fragment = SettingOtherBackupLogFragment.newInstance("", "");
        //账户管理
//      fragment = SettingAccountManageFragment.newInstance("", "");
        //修改密码
//        fragment = new LoginFindPasswordFragment();
        //手机注册
//        fragment = new RegisterMobileFragment();
        //邮箱注册
//        fragment = new RegisterEmailFragment();
        //关于微片
//        fragment = new SettingAboutFragment();
        //基本信息录入
//        fragment = new RegisterSuccessInputBaseInfoFragment();
        //注册成功后改变密码
//        fragment = new RegisterSuccessChangePasswordFragment();
        //已经反馈
//        fragment = new SettingFeedbackFragment();
//        消息主页
//        fragment = new MessageMainFragment();
//        我的主页
//        fragment = new UserMainFragment();
        //用户资料
//        fragment = new UserInfoFragment();
        //用户资料输入
//        fragment = new UserInfoEditFragment();
        //名片资料
//        fragment = new VCardInfoFragment();
        //名片资料输入
//        fragment = new VCardInfoEditFragment();
        //添加名片
//        fragment = new AddVCardMainFragment();
        //选择上传名片
//        fragment = new AddVCardChooseUploadFormFragment();
        //本地上传名片
//        fragment = new AddVCardUploadFragment();
        //会员等级主页
//        fragment = new UserLevelFragment();
        //了解积分规则
//        fragment = new UserLevelGetPointFragment();
        //如何提升会员等级
//        fragment = new UserLevelUpFragment();
        //了解会员等级
//        fragment = new UserLevelIntroduceFragment()
//        应用中心;
//        fragment = new UserAppCenterFragment();
//        粉丝/关注
//        fragment = new UserAttAndFansFragment();
        //财富中心
//        fragment = new UserWealthCenterFragment();
//        我的名片
//        fragment = new UserVCardFragment();
        //微集市
//        fragment = new UserMainFragment();
        //微官网
//        fragment = new UnitWebsiteFragment();
        //云画册
//        fragment = new UnitPictureFragment();
        //创建微官网
//        fragment = new UnitWebsiteCreateFragment();
        //加入微官网链接
//        fragment = new UnitWebsiteJoinFragment();
        //单位详细
//        fragment = new UnitDetailFragment();
//        单位微公告
//        fragment = new UnitDetailNotifyFragment();
//        单位简介
//        fragment = new UnitDetailIntroFragment();
        //发布微公告
//        fragment = new UnitDetailNotifyPublishFragment();
        //单位认证
//        fragment = new UnitAuthFragment();
        //成员管理
//        fragment = new UnitRememberFragment();
        //系统通知
//        fragment = new MessageNotifyFragment();
        //好友推荐
//        fragment = new MessageInviteFragment();
        //聊天会话
//        fragment = new MessageConversationFragment();
        //名片交换
//        fragment = new VCardSwapMainFragment();
        //名片交换结果多人
//        fragment = new VCardSwapResultMultiFragment();
        //名片交换结果单人
//        fragment = new VCardSwapResultSingleFragment();
        //雷达搜索
//        fragment = new VCardSwapRadarFragment();
        //分享名片
        fragment = new UserShareFragment();
        transaction.add(R.id.frg_contend, fragment);
        transaction.commit();
    }


    @Override
    public void switchTo(Class<?> cls, Intent intent) {

    }
}
