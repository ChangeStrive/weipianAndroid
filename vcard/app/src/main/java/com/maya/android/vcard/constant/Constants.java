package com.maya.android.vcard.constant;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.util.ProjectHelper;

import java.io.File;

/**
 * Created by YongJi on 2015/8/17.
 */
public class Constants {

    /**
     * URL 集合
     */
    public static final class URL{
        /** 服务器基地址 **/
        public static final String URL_VCARD_SERVICE = "http://42.96.190.67:8080/vcard_service";// 云1
//		public static final String URL_VCARD_SERVICE = "http://42.96.190.67:8081/vcard_service";// 云新
//		public static final String URL_VCARD_SERVICE = "http://192.168.10.55:8081/vcard_service";

        /** 交换等服务基地址 **/
        public static final String URL_VCARD_SWAP_BASE_SERVICE = "http://42.96.190.67:804/vcard_send_service";
        //发布Umeng控制
        /** 服务器地址列表链接 */
        public static final String URL_VCARD_ADDRESS_LIST = ProjectHelper.fillRequestURL("/common/apiUrl/list");
        //TODO 打包时，需关掉
        /** 是否为URL调试模式 **/
        public static final boolean URL_DEBUG_MODE = true;
//		public static final boolean URL_DEBUG_MODE = false;
    }
    /**
     * Intent 相关集合
     */
    public static final class IntentSet{
        /** KEY:fragment 名称 **/
        public static final String KEY_FRG_NAME = "KEY_FRG_NAME";
        /** KEY:fragment 参数 **/
        public static final String KEY_FRG_BUNDLE = "KEY_FRG_BUNDLE";
        /** IntentCode 名称 **/
        public static final String KEY_INTENT_CODE = "KEY_INTENT_CODE";
        /** 添加名片技巧 */
        public static final int INTENT_CODE_ADD_CARD_EXPLAIN = 2003;
        /** 隐私条款 */
        public static final int INTENT_CODE_STATEMENT = 2007;
        /** 使用帮助 */
        public static final int INTENT_CODE_SET_HELP = 2008;
        /** 鸣谢  */
        public static final int INTENT_CODE_SET_THANKS = 2009;
        /** 功能介绍 */
        public static final int INTENT_CODE_FUNCTION_INTRODUCE = 2010;
        /** 绿色公益 **/
        public static final int INTENT_CODE_GREEN_PUBLIC = 107;
        /** 名片管理 */
        public static final int INTENT_CODE_CARDCASE = 2013;
        /** 会话转发 **/
        public static final int INTENT_CODE_MESSAGE_CHAT_TRANSMIT = 2006;

        /** KEY:Login Frg 是否展示回退按钮 **/
        public static final String KEY_LOGIN_FRG_IS_SHOW_BACK = "KEY_LOGIN_FRG_IS_SHOW_BACK";
        /** KEY:Login Frg 是否自动登录 **/
        public static final String KEY_LOGIN_FRG_IS_AUTO_LOGIN = "KEY_LOGIN_FRG_IS_AUTO_LOGIN";
        /** KEY:Login Frg 自动登录微片号 **/
        public static final String KEY_LOGIN_FRG_IS_AUTO_LOGIN_VCARD_NO = "KEY_LOGIN_FRG_IS_AUTO_LOGIN_VCARD_NO";
        /** KEY:Login Frg 自动登录密码 **/
        public static final String KEY_LOGIN_FRG_IS_AUTO_LOGIN_PASSWORD = "KEY_LOGIN_FRG_IS_AUTO_LOGIN_PASSWORD";
        /** KEY:Login Frg 是否记住登录密码 **/
        public static final String KEY_LOGIN_FRG_IS_AUTO_LOGIN_REMEMBER_PASSWORD = "KEY_LOGIN_FRG_IS_AUTO_LOGIN_REMEMBER_PASSWORD";
        /** KEY:AddVCard Frg 是否展示登录 **/
        public static final String KEY_ADD_VCARD_FRG_IS_SHOW_LOGIN = "KEY_ADD_VCARD_FRG_IS_SHOW_LOGIN";
        /** KEY:AddVCard Frg 是否展示回退按钮 **/
        public static final String KEY_ADD_VCARD_FRG_IS_SHOW_BACK = "KEY_ADD_VCARD_FRG_IS_SHOW_BACK";

        /** 从主页面 **/
        public static final int INTENT_CODE_IS_FROM_ADD_VCARD_ACTIVITY = 2001;
        /** 来自扫一扫 **/
        public static final int INTENT_CODE_QRCODE_SCAN = 101;
        /** 来自雷达扫描结果 **/
        public static final int INTENT_CODE_FROM_RADAR_RESULT = 102;
        /** 来自雷达扫描交换名片 **/
        public static final int INTENT_CODE_FROM_RADAR_SWAP = 103;
        /** 来自雷达扫描交换名片结果 **/
        public static final int INTENT_CODE_FROM_RADAR_SWAP_RESULT = 104;
        /** 联系人ContactEntity **/
        public static final String INTENT_KEY_CONTACT_ENTITY = "INTENT_KEY_CONTACT_ENTITY";

        /** 名片id **/
        public static final String INTENT_KEY_VCARD_ID = "INTENT_KEY_VCARD_ID";
        /** 名片交换方式 **/
        public static final String INTENT_KEY_VCARD_SWAP_WAY = "INTENT_KEY_VCARD_SWAP_WAY";
        /** 名片交换列表 **/
        public static final String INTENT_KEY_VCARD_SWAP_CARD_LIST = "INTENT_KEY_VCARD_SWAP_CARD_LIST";
        /** 账户id **/
        public static final String INTENT_KEY_ACCOUNT_ID = "INTENT_KEY_ACCOUNT_ID";
        /** 推送消息中 的  tagId **/
        public static final String INTENT_KEY_MESSAGE_TAG_ID = "INTENT_KEY_MESSAGE_TAG_ID";
        /** 会话键值 id **/
        public static final String INTENT_KEY_MESSAGE_SESSION_ID = "INTENT_KEY_MESSAGE_SESSION_ID";
        /** 手机号码 **/
        public static final String INTENT_KEY_MOBILE = "INTENT_KEY_MOBILE";
        /** 群聊组类型 **/
        public static final String INTENT_KEY_CIRCLE_GROUP_TYPE = "INTENT_KEY_CIRCLE_GROUP_TYPE";
        /** 标题栏传值得键值名 */
        public static final String INTENT_KEY_TITLE_NAME = "INTENT_KEY_TITLE_NAME";
        /** 是否 刷新 通知  **/
        public static final String INTENT_KEY_IS_NOTICE = "INTENT_KEY_IS_NOTICE";
        /** 是否更改讨论组名称 **/
        public static final String INTENT_KEY_IS_UPDATE_CIRCLE_GROUP_NAME = "INTENT_KEY_IS_UPDATE_CIRCLE_GROUP_NAME";
        /** 是否来自无网络提示对话框 **/
        public static final String INTENT_KEY_IS_FROM_NETWORK_FAIL = "INTENT_KEY_IS_FROM_NETWORK_FAIL";
        /** 名片夹联系人键值id**/
        public static final String INTENT_KEY_CONTACT_ID = "INTENT_KEY_CONTACT_ID";
        /** 名片姓名 **/
        public static final String INTENT_KEY_VCARD_NAME = "INTENT_KEY_VCARD_NAME";
        /** 微片号**/
        public static final String INTENT_KEY_USER_VCARD_NUMBER = "INTENT_KEY_USER_VCARD_NUMBER";
        /** 密码 **/
        public static final String INTENT_KEY_USER_PASSWORD = "INTENT_KEY_USER_PASSWORD";


    }
    /**
     * Action 广播
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-1-2
     *
     */
    public static class ActionIntent {
        /** Intent Action： 退出程序 **/
        public static final String ACTION_INTENT_EXIT_PROCEDURE = "ACTION_INTENT_EXIT_PROCEDURE";
        /** 短信发送 action  */
        public static final String ACTION_INTENT_SMS_SEND = "ACTION_INTENT_SMS_SEND";
        /** 短信接收action */
        public static final String ACTION_INTENT_SMS_DELIVERED = "ACTION_INTENT_SMS_DELIVERED";
        /** 雷达扫描 **/
        public static final String ACTION_INTENT_RADAR_SCAN = "ACTION_INTENT_RADAR_SCAN";
        /** 系统通知 接收刷新 **/
        public static final String ACTION_INTENT_NOTICE_LIST = "ACTION_INTENT_NOTICE_LIST";
        /** 消息 的会话列表 刷新数据   **/
        public static final String ACTION_INTENT_MESSAGE_MAIN = "ACTION_INTENT_MESSAGE_MAIN";
        /** 我的名片刷新 **/
        public static final String ACTION_INTENT_MY_CARD = "ACTION_INTENT_MY_CARD";
        /** 导航栏 消息数量刷新 **/
        public static final String ACTION_INTENT_NAV_BOTTOM_MESSAGE_COUNT = "ACTION_INTENT_NAV_BOTTOM_MESSAGE_COUNT";
        /** 聊天窗口 接收消息 **/
        public static final String ACTION_INTENT_SESSION_CHAT = "ACTION_INTENT_SESSION_CHAT";
    }
    /**
     * 路径  集合
     */
    public static class PATH{
        /** PATH：微片SDCard基本路径 **/
        public static final String PATH_BASE_VCARD = ActivityHelper.getExternalStoragePath() + "VCARD" + File.separator;
        /** PATH：微片配置文件 **/
        public static final String PATH_VCARD_CONFIG = PATH_BASE_VCARD + "config.ini";
        /** PATH：Format：路径 **/
        public static final String PATH_FORMAT = PATH_BASE_VCARD + "%1$s" + File.separator + "%2$s" + File.separator;
        /** PATH：临时文件存放  **/
        public static final String PATH_TEMP_FILE = PATH_BASE_VCARD + "temp/";
        /** PATH：本地名片模板存储路径 **/
        public static final String PATH_LOCAL_VCARD_TEMPLATE = ActivityHelper.getBaseFilePath() + "Vcard/modes/";
    }
    /**
     * 登录方式
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-3-11
     *
     */
    public static class LoginWay{
        /** LoginWay：腾讯微博 **/
        public static final int LOGIN_WAY_BY_WEIBO_QQ =1;
        /** LoginWay：新浪微博 **/
        public static final int LOGIN_WAY_BY_WEIBO_SINA=2;
        /** LoginWay：腾讯QQ **/
        public static final int LOGIN_WAY_BY_QQ = 3;
        /** LoginWay：微信 **/
        public static final int LOGIN_WAY_BY_WEIXIN = 4;
    }
    /**
     * 获取短信标识SMS Flag 集合</br>
     * 1-注册验证码，2-手机绑定验证码，3-重设密码验证码
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-3-26
     *
     */
    public static class GetSMSFlag{
        /** 1-注册验证码 **/
        public static final int SMS_FLAG_REGISTER = 1;
        /** 2-手机绑定验证码 **/
        public static final int SMS_FLAG_BIND = 2;
        /** 3-重设密码验证码 **/
        public static final int SMS_FLAG_RESET_PASSWORD = 3;

    }
    /**
     * Preferences KEY & NAME
     */
    public static class Preferences {
//        /** 软件信息 **/
//        public static final String KEY_NAME_SOFTWARE = "KEY_NAME_SOFTWARE";
//        /** 用户信息 **/
//        public static final String KEY_NAME_USER_INFO = "KEY_NAME_USER_INFO";
        /** 是否进行强制关闭 **/
        public static final String KEY_IS_FORCE_FINISH = "KEY_IS_FORCE_FINISH";
        /** 保持地址列表 **/
        public static final String KEY_SAVE_URL_ADDRESS_LIST = "KEY_SAVE_URL_ADDRESS_LIST";
        /** 保存Old Version **/
        public static final String KEY_SAVE_OLD_VERSION = "KEY_SAVE_OLD_VERSION";
        /** 经度 **/
        public static final String KEY_LONGITUDE = "KEY_LONGITUDE";
        /** 纬度 **/
        public static final String KEY_LATITUDE = "KEY_LATITUDE";
        /** 个信：ClientId **/
        public static final String KEY_GE_XIN_CLIENT_ID = "KEY_GE_XIN_CLIENT_ID";
        /** 当前用户Token **/
        public static final String KEY_CURRENT_USER_TOKEN = "KEY_CURRENT_USER_TOKEN";
        /** 当前用户微片号 **/
        public static final String KEY_CURRENT_USER_VCARD_NO = "KEY_CURRENT_USER_VCARD_NO";
        /** 当前用户资料 **/
        public static final String KEY_CURRENT_USER_INFO = "KEY_CURRENT_USER_INFO";
        /** 当前用户名片列表 **/
        public static final String KEY_CURRENT_USER_VCARD_LIST = "KEY_CURRENT_USER_VCARD_LIST";
        /** 当前名片位置  */
        public static final String KEY_CURRENT_VCARD_POSITION = "KEY_CURRENT_VCARD_POSITION";
        /** 设置数据 **/
        public static final String KEY_SETTING_DATA = "KEY_SETTING_DATA";
        /** 是否登录  */
        public static final String KEY_IS_LOGIN = "KEY_IS_LOGIN";
        /** 是否被迫下线 **/
        public static final String KEY_IS_FORCED_LOGOFF = "KEY_IS_FORCED_LOGOFF";
        /** 被迫下线信息 **/
        public static final String KEY_IS_FORCED_LOGOFF_MESSAGE = "KEY_IS_FORCED_LOGOFF_MESSAGE";
        /** 是否需要重置密码 **/
        public static final String KEY_IS_NEED_RESET_PASSWORD = "KEY_IS_NEED_RESET_PASSWORD";
        /** 是否需要完善资料 **/
        public static final String KEY_IS_NEED_COMPLETE_INFO = "KEY_IS_NEED_COMPLETE_INFO";
        /** 收藏夹密码 **/
        public static final String KEY_CARDCASE_COLLECT_LOCKED_PWD = "KEY_CARDCASE_COLLECT_LOCKED_PWD";
        /** 好友推荐 注册的 微片账户id **/
        public static final String KEY_RECOMMEND_FRIEND_VCARD_USER_ID = "KEY_RECOMMEND_FRIEND_VCARD_USER_ID";
        /** 好友推荐 本机联系人的 contact_id **/
        public static final String KEY_RECOMMEND_FRIEND_BOOK_CONTACT_ID = "KEY_RECOMMEND_FRIEND_BOOK_CONTACT_ID";
        /** 转发名片温馨提示对话框显示 **/
        public static final String KEY_IS_TRANSMIT_VCARD_PROMPT_SHOW = "KEY_IS_TRANSMIT_VCARD_PROMPT_SHOW";
        /** 云端当前日志 **/
        public static final String KEY_SAVE_CURRENT_USER_BACKUP_LOG = "KEY_SAVE_CURRENT_USER_BACKUP_LOG";
        /** 附近的朋友温馨提示对话框显示  **/
        public static final String KEY_IS_NEARBY_PERSON_PROMPT_SHOW = "KEY_IS_NEARBY_PERSON_PROMPT_SHOW";
        /** 好友推荐 成员列表 **/
        public static final String KEY_RECOMMEND_FRIEND_VCARD_USER_LIST = "KEY_RECOMMEND_FRIEND_VCARD_USER_LIST";
        /** 联系人是否需要刷新 **/
        public static final String KEY_IS_CONTACT_HAS_REFRESH = "KEY_IS_CONTACT_HAS_REFRESH";
        /** 好友推荐的 更新图标是否展示 （默认展示） **/
        public static final String KEY_IS_RECOMMEN_NEW_SIGN_SHOW = "KEY_IS_RECOMMEN_NEW_SIGN_SHOW";
        /** 保存登陆简单信息列表 **/
        public static final String KEY_SAVE_LOGIN_SIMPLE_INFO_LIST = "KEY_SAVE_LOGIN_SIMPLE_INFO_LIST";

    }
    /**
     * TAG：请求命令标识
     */
    public static class CommandRequestTag{
        /** 地址列表 */
        public static final int CMD_LIST = 3001;
        /** 正常登录 */
        public static final int CMD_LOGIN_NORMAL = 3002;
        /** 第三方登录 */
        public static final int CMD_LOGIN_THIRD = 3003;
        /** 我的信息 */
        public static final int CMD_MY_INFO = 3005;
        /** 我的名片 */
        public static final int CMD_MY_CARD = 3004;
        /** 修改密码 */
        public static final int CMD_CHANGE_PASSWORD = 3059;
        /**查询我的设置**/
        public static final int CMD_SETTING = 3073;
        /** 保存我的设置 **/
        public static final int CMD_SETTING_SAVE = 3074;
        /** 手机注册 */
        public static final int CMD_REGISTER_MOBILE = 3011;
        /** 获取我的联系人 */
        public static final int CMD_CONTACT_LIST = 3016;
        /** 获取我的单位列表 **/
        public static final int CMD_UNIT_LIST = 3041;
        /** 上传消息会话语音文件 **/
        public static final int CMD_REQUEST_UPLOAD_MESSAGE_SESSION_AUDIO = 3020;
        /** 上传消息会话视频文件 **/
        public static final int CMD_REQUEST_UPLOAD_MESSAGE_SESSION_VIDEO = 3021;
        /** 上传消息会话图片 **/
        public static final int CMD_REQUEST_UPLOAD_MESSAGE_SESSION_IMAGE = 3022;
        /** 上传消息会话名片 **/
        public static final int CMD_REQUEST_UPLOAD_MESSAGE_SESSION_VCARD = 3023;
        /** 上传会话  文件 **/
        public static final int CMD_REQUEST_UPLOAD_MESSAGE_SESSION_FILE = 3024;
        /** 获取单个讨论组消息 **/
        public static final int CMD_REQUEST_CIRCLE_GROUP_SELECT = 3034;
        /** 上传图片 */
        public static final int CMD_UPLOAD_IMAGE = 3025;
        /** 上传头像 **/
        public static final int CMD_REQUEST_UPLOAD_HEAD = 3007;
        /** 短信验证码 */
        public static final int CMD_AUTH_CODE = 3010;
        /** 获取企业公告**/
        public static final int CMD_REQUEST_ENTERPRISE_MEUNIT_ANNOUNCEMENT_LIST = 3079;
        /** 单位信息微公告 **/
        public static final int CMD_REQUEST_ENTERPRISE_EDIT = 3045;
        /** 设置 管理员 **/
        public static final int CMD_REQUEST_ENTERPRISE_MEMBER_SET_MANAGE = 3046;
        /** 撤销管理员 **/
        public static final int CMD_REQUEST_ENTERPRISE_MEMBER_DELETE_MANAGE = 3047;
        /** 移除成员  **/
        public static final int CMD_REQUEST_ENTERPRISE_MEMBER_REMOVE = 3048;
        /** 创建讨论组 **/
        public static final int CMD_REQUEST_CIRCLE_GROUP_CREATE = 3028;
        /** 保存我的信息 */
        public static final int CMD_REQUEST_MY_INFO_SAVE = 3006;
        /** 上传营业执照副本 **/
        public static final int CMD_REQUEST_UPLOAD_ENTERPRISE_LICENSE_COPY = 3042;
        /** 上传组织机构代码证 **/
        public static final int CMD_REQUEST_UPLOAD_ENTERPRISE_TAX_CERTIFICATE = 3043;
        /** 单位认证 **/
        public static final int CMD_REQUEST_ENTERPRISE_APPROVE = 3044;
        /** 备份恢复日志 **/
        public static final int CMD_REQUEST_CLOUD_BACKUP_LOG = 3050;
        /** 云端备份 **/
        public static final int CMD_REQUEST_CLOUD_BACKUP = 3051;
        /** 云端恢复 **/
        public static final int CMD_REQUEST_CLOUD_RECOVER = 3052;
        /** 邮箱注册 */
        public static final int CMD_REQUEST_REGISTER_EMAIL = 3012;
        /** 绑定邮箱 */
        public static final int CMD_REQUEST_BIND_EMAIL = 3015;
        /** 附近的朋友 **/
        public static final int CMD_REQUEST_NEARBY_PEOPLE_LIST = 3056;
        /** 根据通讯录 手机号获取 微片注册用户  **/
        public static final int CMD_REQUEST_VCARD_USERS_FROM_MOBILE = 3057;
        /** 关注TA **/
        public static final int CMD_REQUEST_ATTENTIONS_ADD = 3076;
        /** 获取粉丝/关注列表 **/
        public static final int CMD_REQUEST_FANS_OR_ATTENTIONS_LIST = 3075;
        /** 获取名片人详情 */
        public static final int CMD_REQUEST_CARD_PERSON_TO_ININET_GET = 3079;
        /** 查看名片个人详情 */
        public static final int CMD_REQUEST_CARD_PERSON = 3072;
        /** 推荐短信URL */
        public static final int CMD_REQUEST_RECOMMOND_SMS_URL = 3040;
        /** 名片交换请求 */
        public static final int CMD_REQUEST_SWAP_CARD = 3018;
        /** 添加成员  **/
        public static final int CMD_REQUEST_ENTERPRISE_MEMBER_ADD = 3062;
        /** 推送通知消息  **/
        public static final int CMD_REQUEST_MESSAGE_NOTICE_SEND = 3037;
        /** 转发名片请求 同意结果 **/
        public static final int CMD_REQUEST_TRANSMIT_VCARD_ALLOW_RESULT = 3055;
        /** 交换确认 */
        public static final int CMD_REQUEST_SWAP_CARD_COMMIT = 3019;
        /** 转发名片请求同意 **/
        public static final int CMD_REQUEST_TRANSMIT_VCARD_ALLOW_REQUEST = 3053;
        /** 获取我的联系人 */
        public static final int CMD_REQUEST_MY_CONTACT = 3016;
        /** 推送会话消息 **/
        public static final int CMD_REQUEST_MESSAGE_SESSION_SEND = 3017;
        /** 删除我的联系人 **/
        public static final int CMD_REQUEST_MY_CONTACT_DELETE = 3039;
        /** 通过手机号码查询个人详情 */
        public static final int CMD_SEARCH_DETAIL_FOR_PHONE = 3058;
        /** 获取聊天组成员列表 **/
        public static final int CMD_REQUEST_CIRCLE_GROUP_MEMBER_LIST = 3035;
        /** 更改讨论组名称 **/
        public static final int CMD_REQUEST_CIRCLE_GROUP_NAME_UPDATE = 3029;
        /** 退出讨论组 **/
        public static final int CMD_REQUEST_CIRCLE_GROUP_QUIT = 3030;
        /** 解散删除讨论组  **/
        public static final int CMD_REQUEST_CIRCLE_GROUP_DELETE = 3031;
        /** 添加讨论组成员 **/
        public static final int CMD_REQUEST_CIRCLE_GROUP_MEMBER_ADD = 3033;
        /** 删除讨论组成员 **/
        public static final int CMD_REQUEST_CIRCLE_GROUP_MEMBER_DELETE = 3032;
        /** 雷达扫描 **/
        public static final int CMD_REQUEST_RADAR_SCAN = 3067;
        /**断开雷达扫描**/
        public static final int CMD_REQUEST_RADAR_REMOVE = 3068;
    }
    /**
     * 友盟在线参数
     */
    public static class UmengOnlineKeyAndValue{
        /** 友盟在线参数KEY：分享我的名片URL **/
        public static final String KEY_UMENG_URL_SHARE_MY_CARD_WEIXIN = "KEY_UMENG_URL_SHARE_MY_CARD_WEIXIN";
        /** 友盟在线参数KEY：分享给朋友URL **/
        public static final String KEY_UMENG_URL_SHARE_TO_FRIEND_WEIXIN = "KEY_UMENG_URL_SHARE_TO_FRIEND_WEIXIN";
        /** 友盟在线参数VALUE:分享我的名片URL **/
        public static String VALUE_UMENG_URL_SHARE_MY_CARD_WEIXIN = "http://m.vcard2012.com/m/share/friend";
        /** 友盟在线参数VALUE：分享给朋友URL **/
        public static String VALUE_UMENG_URL_SHARE_TO_FRIEND_WEIXIN = "http://m.vcard2012.com/m/download";
        /** 友盟在线参数KEY：分享我的名片URL **/
        public static final String KEY_UMENG_URL_SHARE_MY_CARD = "URL_SHARE_MY_CARD";
        /** 友盟在线参数KEY：分享给朋友URL **/
        public static final String KEY_UMENG_URL_SHARE_TO_FRIEND = "URL_SHARE_TO_FRIEND";
        /** 友盟在线参数VALUE:分享我的名片URL **/
        public static String VALUE_UMENG_URL_SHARE_MY_CARD = "http://m.vcard2012.com/mobile?p=32";
        /** 友盟在线参数VALUE：分享给朋友URL **/
        public static String VALUE_UMENG_URL_SHARE_TO_FRIEND = "http://m.vcard2012.com/mobile?p=32";
        /** 友盟在线参数KEY：当前版本CODE **/
        public static final String KEY_UMENG_CURRENT_VERSION_CODE = "CURRENT_VERSION_CODE";
        /** 友盟在线参数VALUE：当前版本CODE **/
        public static String VALUE_UMENG_CURRENT_VERSION_CODE = "";
        /** 友盟在线参数KEY：是否强制更新 **/
        public static final String KEY_UMENG_FORCE_UPDATE = "FORCE_UPDATE";
        /** 友盟在线参数KEY：强制更新version以下的版本 **/
        public static final String KEY_UMENG_FORCE_UPDATE_VERSION = "FORCE_UPDATE_VERSION";
        /** 友盟在线参数KEY：是否启用名片线上模版 **/
        public static final String KEY_UMENG_IS_OPEN_CARD_TEMPLATE = "IS_OPEN_CARD_TEMPLATE";
        /** 友盟在线参数VALUE：是否启用名片线上模版 **/
        public static boolean VALUE_UMENG_IS_OPEN_CARD_TEMPLATE = true;
        /** 友盟在线参数KEY：当前服务地址 **/
        public static final String KEY_UMENG_URL_CURRENT_SERVICE = "URL_CURRENT_SERVICE";
        /** 友盟在线参数VALUE：当前服务地址 **/
        public static String VALUE_UMENG_URL_CURRENT_SERVICE = null;
    }
    /**
     * 第三方参数 相关集合
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-2-12
     */
    public static class ThirdPartySet{
        /** Key：新浪微博 **/
        public static final String KEY_SINA_WEIBO = "2270477653";
        /** 回调URL：新浪微博 **/
        public static final String URL_CALLBACK_SINA = "http://www.mayasoft.com.cn";
        /** Key：腾讯微博 **/
        public static final String KEY_QQ_WEIBO = "801332634";
        /** Key-SECRET：腾讯微博 **/
        public static final String KEY_QQ_WEIBO_SECRET = "7816dd8aff081049d3f8a2e386886be9";
        /** KEY：腾讯QQ **/
        public static final String KEY_QQ_APP_SSO = "100397760";
        /** 微信ID */
        public static final String WX_APP_ID = "wxa6ffd90a854a366e";
        /** 微信Secret */
        public static final String WX_APP_SECRET = "c47631270941fa604d0294a6b17acefe";

    }
    /**
     * 联系人显示名称的类型
     * @author zheng_cz
     * @since 2014年4月2日 下午5:07:57
     */
    public static class ContactShowNameType{
        /**显示姓名**/
        public static final int DISPLAY_NAME = 0;
        /** 显示用户名 **/
        public static final int ACCOUNT_NAME = 1;
        /** 显示微片号 **/
        public static final int VCARD_NO = 2;
    }
    /**
     * 会员等级
     */
    public static class MemberGrade{
        /** 普通用户 **/
        public static final int COMMON = 0;
        /** 高级会员 **/
        public static final int SENIOR = 1;
        /** 钻石会员 **/
        public static final int DIAMON = 2;
    }
    /**
     * 服务器返回的错误代号
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-3-6
     *
     */
    public static class ResponseErrorCode{
//        /** 操作成功 **/
//        public static final int SUCCESSFUL = 0;
//        /** 操作失败 **/
//        public static final int AUTH_ERR = 1;
//        /** 服务端异常 **/
//        public static final int UNKNOWN_ERR = -1;
//        /** 帐号已经登出 **/
//        public static final int SESSION_OUT = -10;
        /** 被迫离线 **/
        public static final int FORCED_OFFLINE = -11;
//        /** 无效Token,帐号不存在 **/
//        public static final int TOKEN_ERROR = -100;
//        /** 101表示接口请求时缺少必要参数accessToken或者头部Terminal-Agent **/
//        public static final int PARAMS_ERROR = 101;
//        /** 102表示登录时缺少消息推送的客户端SDK序列这个必要参数 **/
//        public static final int PUSH_SERVICE_ERROR = 102;
//        /** -100表示不存在该微片用户 **/
//        public static final int VNUMBER_NO_FOUND = -100;
//        /** 名片数量不足 */
//        public static final int CARD_COUNT_NO_ENOUGH = 103;
//        /** 名片未上传 */
//        public static final int CARD_NO_HAVE = 104;
    }
    /**
     * 社交关系
     */
    public static class ScoialRelAtion{
        /** 陌生人 **/
        public static final int SCOIAL_STRANGER = 0;
        /** 已关注 **/
        public static final int SCOIAL_ATTENTION = 1;
        /** 相互关注 **/
        public static final int SCOIAL_MUTUAL_ATTENTION = 2;
        /** 被关注 **/
        public static final int SCOIAL_IS_ATTENTION = 3;

    }
    /**
     * 名片交换相关常量
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-2-19
     *
     */
    public static final class SwapCard{
        /** 名片交换方式1:划一划 **/
        public static final int SWAP_CARD_WAY_TO_PADDLE = 1;
        /** 名片交换方式2:摇一摇 **/
        public static final int SWAP_CARD_WAY_TO_SHAKE = 2 ;
        /** 名片交换方式3:扫一扫 **/
        public static final int SWAP_CARD_WAY_TO_SCAN = 3;
        /** 名片交换方式4:云端交换 **/
        public static final int SWAP_CARD_WAY_TO_CLOUND = 4;
        /** 名片交换方式5:转发名片(被人转发) **/
        public static final int SWAP_CARD_WAY_TO_TRANSLATE = 5;
        /** 名片交换方式6: 雷达扫描交换**/
        public static final int SWAP_CARD_WAY_TO_RADA = 6;
    }
    /**
     * 图片地址标识
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-3-24
     */
    public static class ImagePathSign{
        /** 服务器的图片标识-上传 */
        public static final String VCARD_IMAGE_PATH_SIGN_UPLOAD = "/upload/";
        /** 服务器的图片标识-网络 */
        public static final String VCARD_IMAGE_PATH_SIGN_HTTP = "http://";
    }
    /**
     * 名片模板数据更新标识
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-4-2
     *
     */
    public static class CardMode{
        public static final String CARD_MODE_ADDRESS = "address";
        public static final String CARD_MODE_TEL = "telephone";
        public static final String CARD_MODE_MOBILE = "mobile";
        public static final String CARD_MODE_FAX = "fax";
        public static final String CARD_MODE_QQ = "QQ";
        public static final String CARD_MODE_COMPANY_NAME = "companyName";
        public static final String CARD_MODE_COMPANY_EN_NAME = "companyNameEn";
        public static final String CARD_MODE_EMAIL = "Email";
        public static final String CARD_MODE_HOME_PAGE = "homePage";
        public static final String CARD_MODE_NAME = "name";
        public static final String CARD_MODE_JOB = "job";
        public static final String CARD_MODE_INTRO_TITLE = "introTitle";
        public static final String CARD_MODE_INTRO_CONTENT = "introContent";
        public static final String CARD_MODE_HEAD_IMAGE = "headImg";
        public static final String CARD_MODE_LOGO = "logo";
        /**
         * 新增英文
         */
        public static final String CARD_MODE_EN_NAME = "enName";
        public static final String CARD_MODE_EN_COMPANY_NAME = "enCompanyName";
    }
    /**
     * 图片来源类别
     */
    public static class TakePicture{
        /** 拍照 */
        public static final int REQUEST_CODE_CAMERA_HEAD = 10001;
        /** 图库选择图片 */
        public static final int REQUEST_CODE_ADD_IMAGE = 10002;
        /** 裁剪图片 */
        public static final int REQUEST_CODE_IMAGE_CUT = 10003;
        /** 个人头像 选择 **/
        public static final int REQUEST_CODE_CHOOSE_HEAD = 1004;
        /** 媒体Type：图片Type **/
        public static final String SELECT_IMAGE_TYPE = "image/*";
        /** 名片分组是否更改 **/
        public static final String INTENT_KEY_IS_DATA_EDIT = "INTENT_KEY_IS_CONTACT_GROUP_EDIT";
    }

    /**
     * 其他特殊类别
     */
    public static class Other{

        public static final String SMS_DOWNLOAD_SPLIT = "下载";
        /** 信息编辑框最多字数  **/
        public static final int MESSAGE_CHAT_EDITTEXT_COUNT = 200;
        /** 内容多个拼接分隔符 */
        public static final String CONTENT_ARRAY_SPLIT = "#";
        /** 分享默认连接 **/
        public static final String SMS_DOWNLOAD_URL = "http://m.vcard2012.com/s/xxxxxx.jspx";
        /** 密码最小长度 **/
        public static final int LENGTH_PASSWORD_LEAST = 6;
        /** 密码最大长度 **/
        public static final int LENGTH_PASSWORD_MORE = 12;
        /** 一般用户 本地模板 最大数量 **/
        public static final int LOCAL_TEMPLATE_COUNT_NORMAL_MAX = 18;
        /** vip 用户 本地模板 最大数量 **/
        public static final int LOCAL_TEMPLATE_COUNT_VIP_MAX = 18;

    }

    /**
     * 请求的消息处理 结果
     * @author zheng_cz
     * @since 2014年4月3日 上午11:30:07
     */
    public static class MessageResultType{
        /** 成功 /默认  **/
        public static final int SUCCESS = 0;
        /** 拒绝  **/
        public static final int REJUECT = 1;
    }

    /**
     * 指定控件动态比例
     */
    public static class ScaleButton{
        /** 添加名片 Button:Width比例 **/
        public static final float ADD_VCARD_SCALE_WIDTH_BUTTON = 0.55f;
        /** 添加名片 Button:Height比例 **/
        public static final float ADD_VCARD_SCALE_HEIGHT_BUTTON = 0.15f;
        /** 添加名片 选择上传名片 STANDARD Button:Height比例 **/
        public static final float ADD_VCARD_CHOOSE_UPLOAD_WIDTH_BUTTON = 0.48f;
        /** 添加名片 选择上传名片 标准名片 Button:Height比例 **/
        public static final float ADD_VCARD_CHOOSE_UPLOAD_STANDARD_HEIGHT_BUTTON = 0.18f;
        /** 添加名片 选择上传名片 异形名片_ Button:Height比例 **/
        public static final float ADD_VCARD_CHOOSE_UPLOAD_HETEROMORPHISM_HEIGHT_BUTTON = 0.15f;
        /** 添加名片 选择上传名片 折叠名片 Button:Height比例 **/
        public static final float ADD_VCARD_CHOOSE_UPLOAD_FOLD_HEIGHT_BUTTON = 0.31f;
        /** 名片上传 标准名片  Button:Height比例 **/
        public static final float UPLOAD_VCARD_SCALE_STANDARD_TEMPLATE_HEIGHT_BUTTON = 0.28f;
        /** 名片上传 异形名片 Button:Height比例 **/
        public static final float UPLOAD_VCARD_SCALE_HETEROMORPHISM_TEMPLATE_HEIGHT_BUTTON = 0.24f;
        /** 名片上传 折叠名片   Button:Height比例 **/
        public static final float UPLOAD_VCARD_SCALE_FOLD_TEMPLATE_HEIGHT_BUTTON = 0.32f;
        /** 名片上传 标准名片  Button:Height比例 **/
        public static final float UPLOAD_VCARD_SCALE_STANDARD_TEMPLATE_TOP_BUTTON = 0.72f;
        /** 名片上传 异形名片 Button:Height比例 **/
        public static final float UPLOAD_VCARD_SCALE_HETEROMORPHISM_TEMPLATE_TOP_BUTTON = 0.69f;
        /** 名片上传 折叠名片   Button:Height比例 **/
        public static final float UPLOAD_VCARD_SCALE_FOLD_TEMPLATE_TOP_BUTTON = 0.785f;

    }
    /**
     * 小秘书相关常量
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-2-21
     *
     */
    public static class DefaultUser{
        /** 微片小秘书的 cardid */
        public static final long DEFAULT_USER_CARD_ID = 1;
        /** 微片小秘书的账户id */
        public static final long DEFAULT_USER_ID = 1;
        /** 微片小秘书 的用户名和 键值id */
        public static final String DEFAULT_USER_NAME = "10000000";
        /** 联系人 中微片小秘书 的键值id */
        public static final int DEFAULT_USER_CONTACT_KEY_ID = 1;
        /** 微片小秘书 微片号 */
        public static final String DEFAULT_USER_VCARD_NO = "10000000";
        public static final String DEFAULT_USER_DISPLAY_NAME = "微片小秘书";
        /** 默认密码 **/
        public static final String DEFAULT_PASSWORD = "123456";
    }

    /**
     * 名片相关
     */
    public static class Card{
        /** 名片类别（0预留、1为扫描、2为模板、3为本地上传） **/
        public static final int CARD_TYPE_SCAN = 1;
        /** 名片类别（0预留、1为扫描、2为模板、3为本地上传） **/
        public static final int CARD_TYPE_TEMPLATE = 2;
        /** 名片类别（0预留、1为扫描、2为模板、3为本地上传） **/
        public static final int CARD_TYPE_UPLOAD = 3;
        /** 名片类型（0为不确定类型、1为90*54、2为90*45、3为90*94 ） **/
        public static final int CARD_FORM_OTHER = 0;
        /** 名片类型(标准)（0为不确定类型、1为90*54、2为90*45、3为90*94 ） **/
        public static final int CARD_FORM_9054 = 1;
        /** 名片类型(异形)（0为不确定类型、1为90*54、2为90*45、3为90*94 ） **/
        public static final int CARD_FORM_9045 = 2;
        /** 名片类型(折叠)（0为不确定类型、1为90*54、2为90*45、3为90*94 ） **/
        public static final int CARD_FORM_9094 = 3;
        /** 名片方向（0为横向、1为纵向，默认为0） **/
        public static final int CARD_ORIENTATION_LANDSCAPE = 0;
        /** 名片方向（0为横向、1为纵向，默认为0） **/
        public static final int CARD_ORIENTATION_PORTRAIT = 1;
        /** 名片比例：9054 **/
        public static final float CARD_RATIO_9054 = 1.66667f;
        /** 名片比例：9045 **/
        public static final float CARD_RATIO_9045 = 2f;
        /** 名片比例：9094 **/
        public static final float CARD_RATIO_9094 = 0.95744f;
        /** index viewpage:本地模版 **/
        public static final int CARD_LOCAL_TEMPLATE = 0;
        /** index viewpage:线上模版 **/
        public static final int CARD_ONLINE_TEMPLATE = 1;
    }

    /**
     * 回调相关
     */
    public static class RequestCode{
        /** 选择营业执照副本 **/
        public static final int REQUEST_CODE_ADD_LICENSE_COPY = 2000;
        /** 选择组织机构代码证 **/
        public static final int REQUEST_CODE_ADD_TAX_CERTIFICATE = 2001;
        /** 语音文件 **/
        public static final int REQUEST_CODE_ADD_FINISH_CIRCLEGROUPDETAIL = 4308;
        /** 语音文件 **/
        public static final int REQUEST_CODE_ADD_VIDEO = 4302;
        /** 图片**/
        public static final int REQUEST_CODE_ADD_IMAGE = 4301;
        /** 文件 **/
        public static final int REQUEST_CODE_ADD_FILE = 4303;
        /** 快捷短信 **/
        public static final int REQUEST_CODE_ADD_QUICK_MESSAGE = 4304;
        /** 祝福短信 **/
        public static final int REQUEST_CODE_ADD_BLESS_MESSAGE = 4305;
        /** 选择联系人 **/
        public static final int REQUEST_CODE_CHOOSE_CONTACT = 60001;
        /** 分享  **/
        public static final int REQUEST_CODE_SHARE = 1005;
        /** 拨打电话 **/
        public static final int REQUEST_CODE_CALL = 4306;
    }

    /**
     * 单位相关
     */
    public static class UNIT{
        /** 可设置单位管理员的数量 **/
        public static final int SETTING_ADMINISTRATOR_COUNT = 4;

        /** 单位成员 **/
        public static final int ENTERPRISE_MEMBER = 8000;
        /** 管理成员 **/
        public static final int ENTERPRISE_MEMBER_MANAGE = 8001;
        /** 单位成员权限管理 **/
        public static final int ENTERPRISE_MEMBER_ROLE = 8002;
        /**  单位认证时 **/
        public static final int ERTERPRISE_APPROVAL = 8003;
    }

    /**
     * 消息类型
     */
    public static class MessageContentType{
        /**文本消息  **/
        public static final int SESSION_TEXT = 11;
        /** 文件内容  **/
        public static final int SESSION_FILE = 12;
        /** 群聊文本  **/
        public static final int SESSION_GROUP_TEXT = 13;
        /** 群聊文件  **/
        public static final int SESSION_GROUP_FILE = 14;

        /** 云端交换名片请求通知：tagId为空, data(来自附近的朋友/来自手机联系人/来自云端查找/来自好友推荐（转发）) **/
        public static final int NOTICE_CARD_SWAP_REQUEST = 21;
        /** 云端交换名片请求反馈：tagId为空 （成功：系统无通知显示，会话窗显示开始聊天。 失败：系统通知显示） **/
        public static final int NOTICE_CARD_SWAP_RESULT = 22;
        /**雷达扫描 交换确认反馈 **/
        public static final int NOTICE_RADA_SWAP_RESULT = 23;
        /** 二维码扫描交换名片时通知 **/
        public static final int NOTICE_QRCODE_SCAN_REQUEST = 27;

        /** 单位成员加入请求： **/
        public static final int NOTICE_ENTERPRISE_ADD_REQUEST = 31;
        /** 单位(加人\踢人)消息通知 **/
        public static final int NOTICE_ENTERPRISE_OPERATE_RESULT = 32;
        /** 单位是否通过认证的通知 **/
        public static final int NOTICE_ENTERPRISE_APPROVAL_RESULT = 34;
        /** 群聊成员加入，退出的消息通知类型 **/
        public static final int NOTICE_CIRCLE_MEMBER_RESULT = 33;
        /** 群聊组名称更改的通知 **/
        public static final int NOTICE_CIRCLE_GROUP_UPDATE_NAME = 35;
        /** 群聊组创建 **/
        public static final int NOTICE_CIRCLE_GROUP_CREATE = 36;
        /** 群聊组解散 **/
        public static final int NOTICE_CIRCLE_GROUP_DELETE = 37;

        /** 转发名片 请求 允许通知  */
        public static final int NOTICE_TRANSMIT_ALLOW_REQUEST = 41;
        /** 转发名片 请求允许的反馈 */
        public static final int NOTICE_TRANSMIT_ALLOW_RESULT = 42;
        /** 转发名片确认 通知(同意, 被转发者请求与接收者交换名片）) */
        public static final int NOTICE_TRANSMIT_SWAP_REQUEST = 43;
        /** 转发名片确认反馈给介绍人 */
        public static final int NOTICE_TRANSMIT_SWAP_RESULT = 44;
        /** 转发名片确认反馈给 发起转发请求的中间人  */
        public static final int NOTICE_TRANSMIT_SWAP_RESULT_TO_REQUESTER = 45;

        /** 名片数量同步 **/
        public static final int NOTICE_CARD_COUNT_SYNC = 62;
        /** 短信推荐 云端交换好友请求 **/
        public static final int NOTICE_SMS_RECOMMEND_SWAP_REQUEST = 63;
        /** 短信推荐 云端交换好友请求 反馈 **/
        public static final int NOTICE_SMS_RECOMMEND_SWAP_RESULT = 64;
        /** 邮件激活 结果 **/
        public static final int NOTICE_REGISTER_EMAIL_ACTIVITY = 65;
        /** 雷达扫描结果  **/
        public static final int NOTICE_RADAR_SCAN_RESULT = 66;
    }

    /**
     * 消息会话  文件 相关 类
     */
    public static class MessageFiles{
        /** 消息聊天路径 **/
        public static final String MESSAGE_PATH = ActivityHelper.getExternalStoragePath() + "VCARD" + File.separator + "%1$s" + File.separator + "MESSAGE" + File.separator + "%2$s" + File.separator;

        /** 视频文件  */
        public static final String FOLDER_VIDEO = "video";
        /**  语音文件  */
        public static final String FOLDER_AUDIO = "audio";
        /** 图片文件  */
        public static final String FOLDER_IMAGE = "image";
        /** 名片文件  */
        public static final String FOLDER_VCARD = "vcard";
        /**  其他文件  */
        public static final String FOLDER_OTHER = "other";

        /** 语音文件扩展名 **/
        public static final String FILE_EXT_AUDIO = ".amr";
        /** 视频文件扩展名 **/
        public static final String FILE_EXT_VIDEO = ".3gp";
    }

    /**
     * 名片交换音效
     */
    public static class SwapSound{
        /** 名片交换播放的音效：京剧脸谱 **/
        public static final int SWAP_CARD_RESOURCE_ID_BEIJING_OPERA = 0;
        /** 名片交换播放的音效：Sunset Journey **/
        public static final int SWAP_CARD_RESOURCE_ID_SUNSET = 1;
        /** 名片交换播放的音效：静音 **/
        public static final int SWAP_CARD_RESOURCE_ID_SOUND_OFF = 2;
    }

    /**
     * 绑定方式
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-3-24
     *
     */
    public static final class BindWay{
        /** 未绑定 */
        public static int NONE = 0;
        /** 手机绑定 */
        public static int MOBILE = 1;
        /** 邮箱绑定 */
        public static int EMAIL = 2;
        /** 手机 邮箱 都绑定 */
        public static int ALL = 3;
    }

    /**
     * AES加密Key
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-4-15
     *
     */
    public static final class AESKey{
        /** 用户密码 **/
        public static final String KEY_USER_PASSWORD = "p]&*M181l`. 89hI";
    }
}
