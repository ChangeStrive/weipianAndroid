package com.maya.android.vcard.constant;

import android.content.Context;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.util.ResourceHelper;

public class DatabaseConstant {

	/**
	 * 键值id 自增值
	 */
	public static final String _ID = "_id";
	public static final String _COUNT = "_count";

	/**
	 * 数据库表名集合
	 * 
	 * @author ZuoZiJi-Y.J
	 * @version v1.0
	 * @since 2014-3-23
	 * 
	 */
	public static class DBTableName {
		/** 名片模板表名 */
		public static final String TABLE_CARD_TEMPLATES = "card_templates";
		/** 企业表 */
		public static final String TABLE_ENTERPRISES = "enterprises";
		/** 企业成员表 */
		public static final String TABLE_ENTERPRISE_MEMBERS = "enterprise_members";
		/** 群聊组 */
		public static final String TABLE_CIRCLE_GROUPS = "circle_groups";
		/** 群聊成员表 */
		public static final String TABLE_CIRCLE_GROUP_MEMBERS = "circle_group_members";
		/** 消息会话表 */
		public static final String TABLE_MESSAGES = "messages";
		/** 通知表 */
		public static final String TABLE_NOTICES = "notices";
		/** 名片夹联系人表 */
		public static final String TABLE_CONTACTS = "contacts";
		/** 名片夹分组 */
		public static final String TABLE_CONTACT_GROUPS = "contact_groups";
		/** 备份恢复日志表 */
		public static final String TABLE_BACKUP_LOGS = "backup_logs";
		/** 帐户切换登录表 */
		public static final String TABLE_MY_ACCOUNT = "my_account";
	}

	//region 表字段 类
	
	/**
	 * 联系人 Contact表字段名
	 * 
	 * @Version: 1.0
	 * @author: zheng_cz
	 * @since: 2013-7-16 下午6:11:56
	 */
	public static class ContactColumns {
		/** 联系人交换id */
		public static final String CONTACT_ID = "contact_id";
		/** 显示名称 */
		public static final String DISPLAY_NAME = "display_name";
		/** 帐户id */
		public static final String ACCOUNT_ID = "account_id";
		/** 注册用户名 */
		public static final String ACCOUNT_NAME = "account_name";
		/** 微片号 */
		public static final String VCARD_NO = "vcard_no";
		/** 分组id */
		public static final String GROUP_ID = "group_id";
		/** 分组名称 */
		public static final String GROUP_NAME = "group_name";

		/** 头像路径 */
		public static final String HEAD_IMG = "head_img";
		/** 联系人备注三 */
		public static final String REMARK = "remark";
		/** 第三方标识 */
		public static final String PARTNER_ID = "partner_id";
		/** 省 */
		public static final String PROVINCE = "province";
		/** 市 */
		public static final String CITY = "city";
		/** 国家 */
		public static final String COUNTRY = "country";
		/** 英文名称 */
		public static final String EN_NAME = "en_name";
		/** 性别 */
		public static final String SEX = "sex";
		/** 个性签名 */
		public static final String SELF_SIGN = "self_sign";
		/** 籍贯 */
		public static final String NATIVEPLACE = "nativeplace";
		/** 出生日期 */
		public static final String BIRTHDAY = "birthday";
		/** 账户等级 */
		public static final String ACCOUNT_GRADE = "account_grade";
		/** 认证加v */
		public static final String AUTH = "auth";
		/** 学历 */
		public static final String DEGREE = "degree";
		/** 注册时间 */
		public static final String REGISTER_TIME = "register_time";
		/** 名片id */
		public static final String CARD_ID = "card_id";
		/** 名片名称 */
		public static final String CARD_NAME = "card_name";

		/** 行业 */
		public static final String BUSINESS = "business";
		/** 名片正面 */
		public static final String CARD_IMG_A = "card_img_a";
		/** 名片背面 */
		public static final String CARD_IMG_B = "card_img_b";
		/** 公司简介 */
		public static final String COMPANY_ABOUT = "company_about";
		/** 公司主页 */
		public static final String COMPANY_HOME = "company_home";
		/** 公司名称 */
		public static final String COMPANY_NAME = "company_name";
		/** 公司国籍 */
		public static final String COMPANY_COUNTRY = "company_country";
		/** 公司所在省 */
		public static final String COMPANY_PROVINCE = "company_province";
		public static final String EMAIL = "email";
		public static final String FAX = "fax";
		/** 职位 */
		public static final String JOB = "job";
		public static final String MOBILE = "mobile";
		/**
		 * 固话
		 */
		public static final String TELEPHONE = "telephone";

		/** 工作地址 */
		public static final String ADDRESS = "address";
		/** 邮编 */
		public static final String POSTCODE = "postcode";
		/** 名片备注 */
		public static final String CARD_REMARK = "card_remark";
		/** 企业认证状态 */
		public static final String COMPANY_APPROVAL = "company_approval";
		/**
		 * 排序值
		 */
		public static final String ORDER_CODE = "order_code";
		/**
		 * 收藏标志 0-不收藏 1-收藏
		 */
		public static final String STARRED = "starred";
		/**
		 * 通话次数
		 */
		public static final String TIMES_CONTACTED = "times_contacted";
		/**
		 * 最后联系时间
		 */
		public static final String LAST_CONTACTED_TIME = "last_contacted_time";
		/**
		 * 就读学校
		 */
		public static final String SCHOOL = "school";
		/**
		 * 个人简介
		 */
		public static final String INTRODUCTION = "introduction";
		/**
		 * 联系人添加时间
		 */
		public static final String CREATE_TIME = "create_time";
		/** 及时通讯 json串 */
		public static final String INSTANT_MESSENGER = "instant_messenger";

		/** 单位名称列表 json串 **/
		public static final String COMPANY_NAME_LIST = "company_name_list";

		/** 英文单位 **/
		public static final String COMPANY_EN_NAME = "company_en_name";

		/** 绑定方式 **/
		public static final String BIND_WAY = "bind_way";

		/** 新增名片属性 **/
		public static final String CARD_A_TYPE = "card_a_type";
		public static final String CARD_B_TYPE = "card_b_type";
		public static final String CARD_A_FORM = "card_a_form";
		public static final String CARD_B_FORM = "card_b_form";
		public static final String CARD_A_ORIENTATION = "card_a_orientation";
		public static final String CARD_B_ORIENTATION = "card_b_orientation";

		/** 社交信息 qq空间链接地址 */
		public static final String TENCENT_QQ_URL = "tencent_qq_url";

		/** 社交信息 新浪微博链接地址 */
		public static final String SINA_BLOG_URL = "sina_blog_url";

		/** 社交信息 腾讯微博链接地址 */
		public static final String TENCENT_BLOG_URL = "tencent_blog_url";
		/** 企业logo */
		public static final String COMPANY_LOGO = "company_logo";
	}

	/**
	 * 联系人分组 表 ContactGroup 字段
	 * 
	 * @Version: 1.0
	 * @author: zheng_cz
	 * @since: 2013-7-16 下午6:13:00
	 */
	public static class ContactGroupColumns {

		/**
		 * 分组名称
		 */
		public static final String NAME = "name";
		/**
		 * 排序值 升序
		 */
		public static final String ORDER_CODE = "order_code";
		/**
		 * 是否不可编辑
		 */
		public static final String IS_ENABLE = "is_enable";
		/**
		 * 描述
		 */
		public static final String DESCRIPTION = "description";
		/**
		 * 分组图标
		 */
		public static final String ICON = "icon";
		/**
		 * 最后更改时间
		 */
		public static final String LAST_UPDATE_TIME = "last_update_time";
	}

	/**
	 * 名片模板表字段常量
	 * 
	 * @author zheng_cz
	 * @since 2014-1-14 上午11:43:42
	 */
	public static class CardTemplateColumns {
		/** 模板id */
		public static final String MODE_ID = "mode_id";
		/** 模板名称 */
		public static final String MODE_NAME = "mode_name";
		/** 模板类型(0-免费/ 1-vip /2-积分/ 3-微币) */
		public static final String MODE_TYPE = "mode_type";
		/** 模板行业分类 */
		public static final String CLASS_ID = "class_id";
		/** 模板 纵向标示 */
		public static final String IS_VERTICAL = "is_vertical";
		/** 模板正面 */
		public static final String SHOW_IMG = "show_img";
		/** 模板反面 */
		public static final String SHOW_IMG_BACK = "show_img_back";
		/** 模板 0-本地 1-在线 */
		public static final String IS_ONLINE = "is_online";
		/** 模板 CardModeEntity 对象 json */
		public static final String CARD_MODE_ENTITY = "card_mode_entity";
		/** 是否有企业logo */
		public static final String HAS_LOGO = "has_logo";
		/** 是否 有头像 */
		public static final String HAS_HEAD_IMG = "has_head_img";
	}

	/**
	 * 消息 表 字段
	 * 
	 * @author zheng_cz
	 * @since 2013-9-11 下午4:25:12
	 */
	public static class MessageColumns {
		/**
		 * 发起聊天时的名片id
		 */
		public static final String CARD_ID = "card_id";
		/**
		 * 对方 帐户id
		 */
		public static final String OTHER_ACCOUNT_ID = "other_account_id";
		/**
		 * 群组id
		 */
		public static final String TAG_ID = "tag_id";
		/**
		 * 当前选择的名片id
		 */
		public static final String OTHER_CARD_ID = "other_card_id";
		/**
		 * 对方名称
		 */
		public static final String OTHER_DISPLAY_NAME = "other_display_name";
		/**
		 * 对方头像
		 */
		public static final String OTHER_HEAD_IMG = "other_head_img";
		/**
		 * 发送接收标志 0-发送 1-接收
		 */
		public static final String SEND_TYPE = "send_type";
		/**
		 * 消息内容
		 */
		public static final String BODY = "body";
		/**
		 * 已读未读标志 0-未读 1-已读
		 */
		public static final String READ = "read";
		/**
		 * 消息内容类型 11-文本会话：tagId为空 12-文件消息：tagId为空 13-群聊文本消息：tagId为群聊组Id
		 * 14-群聊文件消息：tagId为群聊组Id 20-好友通知（添加处理结果）消息:tagId为空
		 * 21-附近的人好友添加请求通知：tagId为空 22-附近的人好友请求反馈：tagId为空 23-通讯录联系人加好友请求：tagId为空
		 * 24-通讯录联系人加好友请求反馈：tagId为空 31-圈子成员加入请求：tagId为圈子ID
		 * 32-圈子(加人\踢人)消息：tagId为圈子ID
		 * ，fromCardId为空，fromAccountId为空，fromHeadImg为圈子头像，fromName为圈子名字
		 * 33-群聊成员离开的消息通知类型
		 * :fromCardId、fromAccountId、fromName、fromHeadImg为空，tagId为群组ID
		 * 41-转发名片允许通知：tagId为转发给谁的帐户ID 42-转发名片的允许反馈：tagId为空 43-转发名片确认通知：tagId为空
		 * 44-转发名片确认反馈：tagId为空
		 */
		public static final String CONTENT_TYPE = "content_type";
		/**
		 * 发送 失败 标志
		 */
		public static final String IS_SEND_FAIL = "is_send_fail";
		/**
		 * 发送时间
		 */
		public static final String CREATE_TIME = "create_time";
		/**
		 * 失效时长
		 */
		public static final String LOST_TIME = "lost_time";
		/**
		 * 文件本地路径
		 */
		public static final String FILE_PATH = "file_path";
		/**
		 * 是否黑名单消息
		 */
		public static final String IS_BLACK = "is_black";
		/** 是否可操作项 0-可以  1-不可以 **/
		public static final String ENABLE = "enable";
	}

	/**
	 * 通知字段
	 * 
	 * @author zheng_cz
	 * @since 2013-9-26 上午9:30:20
	 */
	public static class NoticeColumns {
		/** 发起聊天时的名片id */
		public static final String FROM_CARD_ID = "from_card_id";
		/** 头像 */
		public static final String FROM_HEAD_IMG = "from_head_img";
		/** 当前 的名片/ 企业id */
		public static final String FROM_ACCOUNT_ID = "from_account_id";
		/** 对方名称 */
		public static final String FROM_NAME = "from_name";
		/** data 字符串中的 BODY 内容 */
		public static final String BODY = "body";
		/** 消息内容类型 */
		public static final String CONTENT_TYPE = "content_type";
		/** 发送时间 */
		public static final String CREATE_TIME = "create_time";
		/** 失效时长 */
		public static final String LOST_TIME = "lose_time";
		/** 通知处理类型 */
		public static final String DEALED = "dealed";
		/** 根据类型变动的id */
		public static final String TAG_ID = "tag_id";
		/** 我的名片id */
		public static final String CARD_ID = "card_id";
		/** 是否已读 0未读 1已读 */
		public static final String READ = "read";
		/** 显示标题  **/
		public static final String TITLE = "title";
	}

	/**
	 * 群聊群组及成员表
	 * 
	 * @author zheng_cz
	 * @since 2013-10-8 上午10:36:20
	 */
	public static class CircleGroupColumns {
		/** 创建的群组id  */
		public static final String GROUP_ID = "group_id";
		/**  圈子 或 组名称  */
		public static final String GROUP_NAME = "group_name";
		/** 圈子 或 群组 头像  */
		public static final String HEAD_IMG = "head_img";
		/** 群组创建时间  */
		public static final String CREATE_TIME = "create_time";
		/** 创建者 帐户id  */
		public static final String ACCOUNT_ID = "account_id";
		/** 创建者 名片id **/
		public static final String CARD_ID = "card_id";
		/**  备注  */
		public static final String REMARK = "remark";
		/**
		 * 类型 1-群组 2-自定义圈子 3-企业圈
		 */
		public static final String TYPE = "type";
		/**
		 * 成员人数
		 */
		public static final String MEMBER_COUNT = "member_count";
		
		/** 请求过成员列表 次数 加1 **/
		public static final String HAD_REQUEST_MEMBER = "had_request_member";
		/** 创建者姓名 **/
		public static final String DISPLAY_NAME = "display_name";
	}

	/**
	 * 群聊群组及成员表
	 * 
	 * @author zheng_cz
	 * @since 2013-10-8 上午10:36:20
	 */
	public static class CircleGroupMemberColumns {

		/** 创建的群组id  */
		public static final String GROUP_ID = "group_id";
		/** 成员名片id  */
		public static final String CARD_ID = "card_id";
		/** 成员帐户id  */
		public static final String ACCOUNT_ID = "account_id";
		/** 成员名称  */
		public static final String DISPLAY_NAME = "display_name";
		/** 成员 头像  */
		public static final String HEAD_IMG = "head_img";
		/**  身份权限标识 2-创建人 1-管理员 0-普通成员  */
		public static final String ROLE = "role";
		/** 成员id **/
		public static final String MEMBER_ID = "member_id";
		/** 昵称 **/
		public static final String NICK_NAME = "nick_name";
	}

	/**
	 * 备份恢复日志表字段
	 * 
	 * @Version: 1.0
	 * @author: zheng_cz
	 * @since: 2013-8-22 上午9:57:11
	 */
	public static class BackupLogColumns {
		/**
		 * 恢复名称
		 */
		public static final String NAME = "name";
		/**
		 * 备份恢复标志 1-备份 2-恢复
		 */
		public static final String FLAG = "flag";
		/**
		 * 最后备份恢复时间
		 */
		public static final String LAST_TIME = "last_time";
		/**
		 * 备份恢复数量
		 */
		public static final String NUMBER = "number";
		/**
		 * 级别 1-一级分类（名片） 2-二级类别 （名片下的 收藏夹）
		 */
		public static final String LEVEL = "level";
		/**
		 * 备份时的文件大小
		 */
		public static final String SIZE = "size";
		/**
		 * 组类型 1-名片夹分组 2-通讯录分组
		 */
		public static final String TYPE = "type";
	}

	/**
	 * 企业信息Key
	 * 
	 * @author hzq
	 * 
	 *         2013-8-10 上午9:25:50
	 */
	public static class EnterpriseColumns {
		/**
		 * 企业ID
		 */
		public static final String ENTERPRISE_ID = "enterprise_id";
		/**
		 * 企业头象
		 */
		public static final String HEAD_IMG = "head_img";
		/**
		 * 企业地址
		 */
		public static final String ADDRESS = "address";
		/**
		 * 企业等级
		 */
		public static final String GRADE = "grade";
		/**
		 * 企业公告
		 */
		public static final String ANNOUNCEMENT = "announcement";
		/**
		 * 标签
		 */
		public static final String CLASS_LABEL = "class_label";
		/**
		 * 企业名称
		 */
		public static final String ENTERPRISE_NAME = "enterprise_name";
		/**
		 * 企业简介
		 */
		public static final String ENTERPRISE_ABOUNT = "enterprise_about";
		/**
		 * 创建时间
		 */
		public static final String CREATE_TIME = "create_time";
		/**
		 * 成员数量
		 */
		public static final String MEMBER_COUNT = "member_count";
		/**
		 * 创始人
		 */
		public static final String CREATER = "creater";
		/**
		 * 圈子类型,是否公开
		 */
		public static final String IS_OPEN = "is_open";
		/**
		 * 是否认证 -1：未认证；0：已申请认证；1：已认证
		 */
		public static final String APPROVAL = "approval";

		/**
		 * 单位群聊id
		 */
		public static final String MESSAGE_GROUP_ID = "message_group_id";
		/** 请求过成员列表 次数 加1 **/
		public static final String HAD_REQUEST_MEMBER = "had_request_member";
		/** 单位网址 **/
		public static final String ENTERPRISE_WEB = "enterprise_web";
	}

	/**
	 * 单位成员信息Key
	 * 
	 * @author hzq
	 * 
	 *         2013-8-10 上午9:25:50
	 */
	public static class EnterpriseMemberColumns {
		/** 企业ID */
		public static final String ENTERPRISE_ID = "enterprise_id";
		/** 成员id */
		public static final String MEMBER_ID = "member_id";
		/** 角色 */
		public static final String ROLE = "role";
		/** 账户id */
		public static final String ACCOUNT_ID = "account_id";
		/** 姓名 */
		public static final String DISPLAY_NAME = "display_name";

		/** 昵称 */
		public static final String NICK_NAME = "nick_name";
		/** 名片id */
		public static final String CARD_ID = "card_id";
		/** 备注 */
		public static final String DESPRITION = "desprition";
		/** 头像 */
		public static final String HEAD_IMG = "head_img";
		/** 职位 */
		public static final String JOB = "job";
		/** 微片号 */
		public static final String VCARD_NO = "vcard_no";

	}

	/**
	 * 多个账户登录记录表字段
	 * 
	 * @author zheng_cz
	 * @since 2013-11-18 下午4:22:14
	 */
	public static class MyAccountColumns {
		public static final String DISPLAY_NAME = "display_name";
		public static final String HEAD_IMG = "head_img";
		public static final String PASSWORD = "pwd";
		public static final String VCARD_NO = "vcard_no";
	}

	/**
	 * 系统短信插入字段
	 * 
	 * @author zheng_cz
	 * @since 2013-9-26 上午9:30:55
	 */
	public static class SMSColumns {
		/**
		 * 发送内容
		 */
		public static final String BODY = "body";

		/**
		 * 发送时间
		 */
		public static final String DATE = "date";

		/**
		 * 阅读状态 0-未读 1-已读
		 */
		public static final String READ = "read";

		/**
		 * 1-收 2-发送
		 */
		public static final String TYPE = "type";
		/**
		 * 接收方号码
		 */
		public static final String ADDRESS = "address";
	}

	// endregion 表字段类

	// region 字段枚举值
	/**
	 * 角色权限 值
	 * 
	 * @author zheng_cz
	 * @since 2013-10-8 上午10:52:02
	 */
	public static class Role {
		/**
		 * 创建者
		 */
		public static final int CREATER = 2;
		/**
		 * 管理员
		 */
		public static final int ADMIN = 1;
		/**
		 * 普通成员
		 */
		public static final int MEMBER = 0;
		
		/** 移除 **/
		public static final int REMOVE = -1;
	}

	/**
	 * 单位认证状态
	 * 
	 * @author zheng_cz
	 * 
	 */
	public static class EnterpriseApproval {
		/**
		 * 未认证
		 */
		public static int UNAPPROVAL = -1;
		/**
		 * 申请中
		 */
		public static int APPROVALING = 0;
		/**
		 * 已认证
		 */
		public static int APPROVALED = 1;
	}

	/**
	 * 消息 发送接收标志
	 * 
	 * @author zheng_cz
	 * @since 2013-9-11 下午7:05:00
	 */
	public static class MessageSendType {
		/**
		 * 发送
		 */
		public static final int SEND = 0;
		/**
		 * 接收
		 */
		public static final int RECIVER = 1;
		/** 组消息  **/
		public static final int OTHER = 2;

	}

	/**
	 * 消息 已读 未读 标志
	 * 
	 * @author zheng_cz
	 * @since 2013-9-11 下午7:05:47
	 */
	public static class MessageReadFlag {
		/**
		 * 已读
		 */
		public static final int READED = 1;
		/**
		 * 未读
		 */
		public static final int UNREAD = 0;
	}

	/**
	 * 圈子 群组类型
	 * 
	 * @author zheng_cz
	 * @since 2013-10-8 上午10:49:42
	 */
	public static class CircleGroupType {
		/**
		 * 讨论组
		 */
		public static final int GROUP = 1;
		/**
		 * 自定义圈子
		 */
		public static final int CIRCLE_CUSTOM = 2;
		/**
		 * 企业圈子
		 */
		public static final int CIRCLE_COMPANY = 3;

	}

	/**
	 * 通知处理状态
	 * 
	 * @author zxf
	 * @since 2013-12-1
	 */
	public static class NoticeDealStatus {
		/**
		 * 未处理
		 */
		public static final int UNDEAL = -1;

		/** 已同意 */
		public static final int DEAL_AGREE = 0;

		/** 拒绝 */
		public static final int DEAL_REJECT = 1;
		/** 忽略 **/
		public static final int DEAL_NEGLECT = 2;
		/**
		 * 已批量处理
		 */
		public static final int DEAL_BATCH = 3;

	}

	/**
	 * 密码类型
	 * 
	 * @author zheng_cz
	 * @since 2013-12-10 上午10:59:21
	 */
	public static class PasswordType {
		/**
		 * 登录密码
		 */
		public static final int LOGIN_PWD = 1;
		/**
		 * 收藏夹加密密码
		 */
		public static final int ENCODE_PWD = 2;
	}

	/**
	 * 及时通讯类型 vcard 标签
	 * 
	 * @author zheng_cz
	 * @since 2014-1-10 上午11:02:53
	 */
	public static class InstantMessengerLabel {
		public static final String QQ = "X-QQ";
		public static final String MSN = "X-MSN";
		public static final String ICQ = "X-ICQ";
		/** 阿里旺旺 */
		public static final String AIM = "X-AIM";
		/** 腾讯微博 */
		public static final String MICROBLOG_TX = "X-TX-MICROBLOG";
		/** 新浪微博 */
		public static final String MICROBLOG_SINA = "X-SINA-MICROBLOG";
		/** 微信 */
		public static final String WEI_XIN = "X-WEIXIN";
	}

	/**
	 * 手机通讯录 及时通讯类型常量
	 * 
	 * @author zheng_cz
	 * @since 2014-1-10 下午3:12:12
	 */
	public static class InstantMessengerType {
		public static final int QQ = 4;
		public static final int MSN = 1;
		public static final int ICQ = 6;
		/** 阿里旺旺 */
		public static final int AIM = 0;
		/** 谷歌帐户 */
		public static final int GOOGLE_TALK = 5;
		/**
		 * 自定义
		 */
		public static final int CUSTOM = -1;

	}

	/**
	 * 模板类型
	 * 
	 * @author ZuoZiJi-Y.J
	 * @version v1.0
	 * @since 2014-3-24
	 * 
	 */
	public static class VCardTemplateType {
		/** 免费模板 */
		public static final int FREE = 0;
		/** vip模板 */
		public static final int VIP = 1;
		/** 积分模板 */
		public static final int INTEGRAL = 2;
		/** 微币模板 */
		public static final int SMALL_COINS = 3;
	}

	/**
	 * 管理成员权限设置
	 * 
	 * @author zheng_cz
	 * 
	 */
	public static class MemberRoleSet {

		/**
		 * 设置为管理员
		 */
		public static int MANAGER = 1;

		/**
		 * 设置为普通成员
		 */
		public static int COMMON = 0;

		/**
		 * 踢出单位
		 */
		public static int OUT = -1;
	}

	//endregion 字段枚举值

	/**
	 * 数据库初始化语句
	 */
	public static class initDatabaseSQL {
		/**
		 * 创建联系人表 contacts
		 */
		public static final String CREATE_TABLE_CONTACTS = new StringBuilder().append("create table ").append(DBTableName.TABLE_CONTACTS)
				.append("( ").append(_ID).append(" integer not null primary key autoincrement, ").append(ContactColumns.VCARD_NO)
				.append(" TEXT, ").append(ContactColumns.CONTACT_ID).append(" INTEGER NOT NULL DEFAULT 0, ").append(ContactColumns.STARRED)
				.append(" INTEGER NOT NULL DEFAULT 0, ").append(ContactColumns.GROUP_ID).append(" INTEGER NOT NULL DEFAULT 0, ")
				.append(ContactColumns.GROUP_NAME).append(" TEXT, ").append(ContactColumns.ACCOUNT_ID).append(" INTEGER NOT NULL DEFAULT 0, ")
				.append(ContactColumns.ACCOUNT_NAME).append(" TEXT, ").append(ContactColumns.ACCOUNT_GRADE).append(" INTEGER, ")
				.append(ContactColumns.DISPLAY_NAME).append(" TEXT, ").append(ContactColumns.HEAD_IMG).append(" TEXT, ")
				.append(ContactColumns.PARTNER_ID).append(" INTEGER NOT NULL DEFAULT 0, ").append(ContactColumns.EN_NAME).append(" TEXT, ")
				.append(ContactColumns.AUTH).append(" INTEGER, ").append(ContactColumns.BIND_WAY).append(" INTEGER, ").append(ContactColumns.SEX)
				.append(" INTEGER NOT NULL DEFAULT 0, ").append(ContactColumns.BIRTHDAY).append(" TEXT, ").append(ContactColumns.DEGREE)
				.append(" TEXT, ").append(ContactColumns.NATIVEPLACE).append(" TEXT, ").append(ContactColumns.REGISTER_TIME).append(" TEXT, ")
				.append(ContactColumns.INTRODUCTION).append(" TEXT, ").append(ContactColumns.SCHOOL).append(" TEXT, ")
				.append(ContactColumns.SELF_SIGN).append(" TEXT, ").append(ContactColumns.TENCENT_BLOG_URL).append(" TEXT, ")
				.append(ContactColumns.TENCENT_QQ_URL).append(" TEXT, ").append(ContactColumns.SINA_BLOG_URL).append(" TEXT, ")
				.append(ContactColumns.REMARK).append(" TEXT, ").append(ContactColumns.COUNTRY).append(" TEXT, ").append(ContactColumns.PROVINCE)
				.append(" TEXT, ").append(ContactColumns.CITY).append(" TEXT, ").append(ContactColumns.CARD_ID)
				.append(" INTEGER not null default 0, ").append(ContactColumns.CARD_NAME).append(" TEXT, ").append(ContactColumns.CARD_IMG_A)
				.append(" TEXT, ").append(ContactColumns.CARD_IMG_B).append(" TEXT, ").append(ContactColumns.CARD_A_FORM).append(" INTEGER, ")
				.append(ContactColumns.CARD_A_ORIENTATION).append(" INTEGER, ").append(ContactColumns.CARD_A_TYPE).append(" INTEGER, ")
				.append(ContactColumns.CARD_B_FORM).append(" INTEGER, ").append(ContactColumns.CARD_B_ORIENTATION).append(" INTEGER, ")
				.append(ContactColumns.CARD_B_TYPE).append(" INTEGER, ").append(ContactColumns.COMPANY_NAME).append(" TEXT, ")
				.append(ContactColumns.COMPANY_APPROVAL).append(" INTEGER, ").append(ContactColumns.COMPANY_EN_NAME).append(" TEXT, ")
				.append(ContactColumns.COMPANY_NAME_LIST).append(" TEXT, ").append(ContactColumns.COMPANY_LOGO).append(" TEXT, ")
				.append(ContactColumns.COMPANY_ABOUT).append(" TEXT, ").append(ContactColumns.MOBILE).append(" TEXT, ")
				.append(ContactColumns.TELEPHONE).append(" TEXT, ").append(ContactColumns.FAX).append(" TEXT, ").append(ContactColumns.EMAIL)
				.append(" TEXT, ").append(ContactColumns.ADDRESS).append(" TEXT, ").append(ContactColumns.POSTCODE).append(" TEXT, ")
				.append(ContactColumns.INSTANT_MESSENGER).append(" TEXT, ").append(ContactColumns.BUSINESS).append(" INTEGER, ")
				.append(ContactColumns.JOB).append(" TEXT, ").append(ContactColumns.COMPANY_HOME).append(" TEXT, ")
				.append(ContactColumns.CARD_REMARK).append(" TEXT, ").append(ContactColumns.COMPANY_COUNTRY).append(" TEXT, ")
				.append(ContactColumns.COMPANY_PROVINCE).append(" TEXT, ").append(ContactColumns.ORDER_CODE).append(" INTEGER, ")
				.append(ContactColumns.CREATE_TIME).append(" TEXT, ").append(ContactColumns.LAST_CONTACTED_TIME).append(" TEXT, ")
				.append(ContactColumns.TIMES_CONTACTED).append(" INTEGER); ").toString();

		/** 创建联系人分组表 */
		public static final String CREATE_TABLE_CONTACT_GROUPS = new StringBuilder().append("create table ").append(DBTableName.TABLE_CONTACT_GROUPS)
				.append("( ").append(_ID).append(" integer not null primary key autoincrement, ").append(ContactGroupColumns.NAME)
				.append(" TEXT not null, ").append(ContactGroupColumns.IS_ENABLE).append(" INTEGER NOT NULL DEFAULT 0, ")
				.append(ContactGroupColumns.ICON).append(" TEXT, ").append(ContactGroupColumns.ORDER_CODE).append(" INTEGER NOT NULL DEFAULT 1, ")
				.append(ContactGroupColumns.DESCRIPTION).append(" TEXT, ").append(ContactGroupColumns.LAST_UPDATE_TIME).append(" TEXT); ").toString();

		/** 创建 单位表 enterprises */
		public static final String CREATE_TABLE_ENTERPRISES = new StringBuilder().append("create table ").append(DBTableName.TABLE_ENTERPRISES)
				.append("( ").append(_ID).append(" integer not null primary key autoincrement, ").append(EnterpriseColumns.ENTERPRISE_ID)
				.append(" INTEGER not null default 0, ").append(EnterpriseColumns.ENTERPRISE_NAME).append(" TEXT, ")
				.append(EnterpriseColumns.APPROVAL).append(" INTEGER not null default -1, ").append(EnterpriseColumns.ADDRESS).append(" TEXT, ")
				.append(EnterpriseColumns.ANNOUNCEMENT).append(" TEXT, ").append(EnterpriseColumns.GRADE).append(" INTEGER not null default 0, ")
				.append(EnterpriseColumns.ENTERPRISE_ABOUNT).append(" TEXT, ").append(EnterpriseColumns.HEAD_IMG).append(" TEXT, ")
				.append(EnterpriseColumns.CLASS_LABEL).append(" INTEGER, ").append(EnterpriseColumns.CREATER).append(" TEXT, ")
				.append(EnterpriseColumns.CREATE_TIME).append(" TEXT, ").append(EnterpriseColumns.MEMBER_COUNT)
				.append(" INTEGER not null default 0, ").append(EnterpriseColumns.MESSAGE_GROUP_ID).append(" INTEGER not null default 0, ")
				.append(EnterpriseColumns.HAD_REQUEST_MEMBER).append(" INTEGER not null default 0, ")
				.append(EnterpriseColumns.ENTERPRISE_WEB).append(" TEXT, ")
				.append(EnterpriseColumns.IS_OPEN).append(" INTEGER not null default 0); ").toString();

		/** 创建 单位成员表 enterprise_members */
		public static final String CREATE_TABLE_ENTERPRISE_MEMBERS = new StringBuilder().append("create table ")
				.append(DBTableName.TABLE_ENTERPRISE_MEMBERS).append("( ").append(_ID).append(" integer not null primary key autoincrement, ")
				.append(EnterpriseMemberColumns.MEMBER_ID).append(" INTEGER not null default 0, ").append(EnterpriseMemberColumns.ENTERPRISE_ID)
				.append(" INTEGER not null default 0, ").append(EnterpriseMemberColumns.ACCOUNT_ID).append(" INTEGER not null default 0, ")
				.append(EnterpriseMemberColumns.CARD_ID).append(" INTEGER not null default 0, ").append(EnterpriseMemberColumns.HEAD_IMG)
				.append(" TEXT, ").append(EnterpriseMemberColumns.DISPLAY_NAME).append(" TEXT, ").append(EnterpriseMemberColumns.NICK_NAME)
				.append(" TEXT, ").append(EnterpriseMemberColumns.DESPRITION).append(" TEXT, ").append(EnterpriseMemberColumns.JOB)
				.append(" TEXTS, ").append(EnterpriseMemberColumns.ROLE).append(" INTEGER not null default 0, ")
				.append(EnterpriseMemberColumns.VCARD_NO).append(" TEXT);").toString();

		/** 创建 圈子讨论组 circle_groups */
		public static final String CREATE_TABLE_CIRCLE_GROUPS = new StringBuilder().append("create table ").append(DBTableName.TABLE_CIRCLE_GROUPS)
				.append("( ").append(_ID).append(" integer not null primary key autoincrement, ")
				.append(CircleGroupColumns.GROUP_ID).append(" INTEGER not null default 0, ")
				.append(CircleGroupColumns.GROUP_NAME).append(" TEXT, ")
				.append(CircleGroupColumns.MEMBER_COUNT).append(" INTEGER not null default 0, ")
				.append(CircleGroupColumns.CREATE_TIME).append(" TEXT, ")
				.append(CircleGroupColumns.ACCOUNT_ID).append(" INTEGER not null default 0, ")
				.append(CircleGroupColumns.CARD_ID).append(" INTEGER not null default 0, ")
				.append(CircleGroupColumns.DISPLAY_NAME).append(" TEXT, ")
				.append(CircleGroupColumns.HEAD_IMG).append(" TEXT, ")
				.append(CircleGroupColumns.TYPE).append(" INTEGER not null default 0, ")
				.append(EnterpriseColumns.HAD_REQUEST_MEMBER).append(" INTEGER not null default 0, ")
				.append(CircleGroupColumns.REMARK).append(" TEXT); ")
				.toString();

		/** 创建 讨论组 成员表 circle_members */
		public static final String CREATE_TABLE_CIRCLE_GROUP_MEMBERS = new StringBuilder().append("create table ")
				.append(DBTableName.TABLE_CIRCLE_GROUP_MEMBERS).append("( ").append(_ID).append(" integer not null primary key autoincrement, ")
				.append(CircleGroupMemberColumns.MEMBER_ID).append(" INTEGER not null default 0, ")
				.append(CircleGroupMemberColumns.GROUP_ID).append(" INTEGER not null default 0, ")
				.append(CircleGroupMemberColumns.ACCOUNT_ID).append(" INTEGER not null default 0, ")
				.append(CircleGroupMemberColumns.CARD_ID).append(" INTEGER not null default 0, ")
				.append(CircleGroupMemberColumns.HEAD_IMG).append(" TEXT, ")
				.append(CircleGroupMemberColumns.DISPLAY_NAME).append(" TEXT, ")
				.append(CircleGroupMemberColumns.ROLE).append(" INTEGER not null default 0, ")
				.append(CircleGroupMemberColumns.NICK_NAME).append(" TEXT);").toString();

		/** 创建 会话表 messages */
		public static final String CREATE_TABLE_MESSAGES = new StringBuilder().append("create table ").append(DBTableName.TABLE_MESSAGES)
				.append("( ").append(_ID).append(" integer not null primary key autoincrement, ")
				.append(MessageColumns.TAG_ID).append(" INTEGER not null default 0, ")
				.append(MessageColumns.CARD_ID).append(" INTEGER not null default 0, ")
				.append(MessageColumns.OTHER_ACCOUNT_ID).append(" INTEGER not null default 0, ")
				.append(MessageColumns.OTHER_CARD_ID).append(" INTEGER not null default 0, ")
				.append(MessageColumns.OTHER_DISPLAY_NAME).append(" TEXT, ")
				.append(MessageColumns.OTHER_HEAD_IMG).append(" TEXT, ")
				.append(MessageColumns.BODY).append(" TEXT, ")
				.append(MessageColumns.CONTENT_TYPE).append(" INTEGER not null default 0, ")
				.append(MessageColumns.FILE_PATH).append(" TEXT, ")
				.append(MessageColumns.SEND_TYPE).append(" INTEGER not null default 0, ")
				.append(MessageColumns.READ).append(" INTEGER not null default 0, ")
				.append(MessageColumns.CREATE_TIME).append(" TEXT, ")
				.append(MessageColumns.LOST_TIME).append(" INTEGER not null default 0, ")
				.append(MessageColumns.IS_SEND_FAIL).append(" INTEGER not null default 0, ")
				.append(MessageColumns.ENABLE).append(" INTEGER not null default 0, ")
				.append(MessageColumns.IS_BLACK).append(" INTEGER not null default 0);")
				.toString();

		/** 创建 通知表 notices */
		public static final String CREATE_TABLE_NOTICES = new StringBuilder().append("create table ").append(DBTableName.TABLE_NOTICES).append("( ")
				.append(_ID).append(" integer not null primary key autoincrement, ")
				.append(NoticeColumns.TITLE).append(" TEXT, ")
				.append(NoticeColumns.CARD_ID).append(" INTEGER not null default 0, ")
				.append(NoticeColumns.FROM_ACCOUNT_ID).append(" INTEGER not null default 0, ")
				.append(NoticeColumns.FROM_CARD_ID).append(" INTEGER not null default 0, ").append(NoticeColumns.FROM_HEAD_IMG).append(" TEXT, ")
				.append(NoticeColumns.FROM_NAME).append(" TEXT, ").append(NoticeColumns.BODY).append(" TEXT, ").append(NoticeColumns.DEALED)
				.append(" INTEGER not null default 0, ").append(NoticeColumns.READ).append(" INTEGER not null default 0, ")
				.append(NoticeColumns.CREATE_TIME).append(" TEXT, ").append(NoticeColumns.LOST_TIME).append(" INTEGER not null default 0, ")
				.append(NoticeColumns.TAG_ID).append(" INTEGER not null default 0, ").append(NoticeColumns.CONTENT_TYPE)
				.append(" INTEGER not null default 0); ").toString();

		/** 创建 备份恢复日志表  backup_log */
		public static final String CREATE_TABLE_BACKUP_LOGS = new StringBuilder().append("create table ").append(DBTableName.TABLE_BACKUP_LOGS)
				.append("( ").append(_ID).append(" integer not null primary key autoincrement, ").append(BackupLogColumns.NAME).append(" TEXT, ")
				.append(BackupLogColumns.FLAG).append(" INTEGER not null default 1, ").append(BackupLogColumns.LEVEL)
				.append(" INTEGER not null default 1, ").append(BackupLogColumns.NUMBER).append(" INTEGER not null default 0, ")
				.append(BackupLogColumns.SIZE).append(" TEXT, ").append(BackupLogColumns.TYPE).append(" INTEGER not null default 1, ")
				.append(BackupLogColumns.LAST_TIME).append(" TEXT); ").toString();

		/** 创建 名片模板 表 card_templates */
		public static final String CREATE_TABLE_CARD_TEMPLATES = new StringBuilder().append("create table ").append(DBTableName.TABLE_CARD_TEMPLATES)
				.append("( ").append(_ID).append(" integer not null primary key autoincrement, ").append(CardTemplateColumns.MODE_ID)
				.append(" INTEGER not null default 0, ").append(CardTemplateColumns.MODE_NAME).append(" TEXT, ")
				.append(CardTemplateColumns.MODE_TYPE).append(" TEXT, ").append(CardTemplateColumns.CLASS_ID).append(" INTEGER not null default 0, ")
				.append(CardTemplateColumns.CARD_MODE_ENTITY).append(" TEXT, ").append(CardTemplateColumns.SHOW_IMG).append(" TEXT, ")
				.append(CardTemplateColumns.SHOW_IMG_BACK).append(" TEXT, ").append(CardTemplateColumns.HAS_HEAD_IMG)
				.append(" INTEGER not null default 0, ").append(CardTemplateColumns.HAS_LOGO).append(" INTEGER not null default 0, ")
				.append(CardTemplateColumns.IS_VERTICAL).append(" INTEGER not null default 0, ").append(CardTemplateColumns.IS_ONLINE)
				.append(" INTEGER not null default 0); ").toString();

		/** 创建 帐户登录日志 表 my_account */
		public static final String CREATE_TABLE_MY_ACCOUNT = new StringBuilder().append("create table ").append(DBTableName.TABLE_MY_ACCOUNT)
				.append("( ").append(_ID).append(" integer not null primary key autoincrement, ").append(MyAccountColumns.VCARD_NO).append(" TEXT, ")
				.append(MyAccountColumns.PASSWORD).append(" TEXT, ").append(MyAccountColumns.HEAD_IMG).append(" TEXT, ")
				.append(MyAccountColumns.DISPLAY_NAME).append(" TEXT);").toString();

//		/** 插入 默认分组 我的vip */
//		public static final String INSERT_CONTACT_GROUP1 = new StringBuilder().append("insert into ").append(DBTableName.TABLE_CONTACT_GROUPS)
//				.append(" ( ").append(ContactGroupColumns.NAME).append(",").append(ContactGroupColumns.ICON).append(",")
//				.append(ContactGroupColumns.ORDER_CODE).append(") values('我的VIP', 'img_groups_vip', 1 );").toString();
//
//		/** 插入 默认分组 家人 */
//		public static final String INSERT_CONTACT_GROUP2 = new StringBuilder().append("insert into ").append(DBTableName.TABLE_CONTACT_GROUPS)
//				.append(" ( ").append(ContactGroupColumns.NAME).append(",").append(ContactGroupColumns.ICON).append(",")
//				.append(ContactGroupColumns.ORDER_CODE).append(") values('家人','img_groups_home',2 );").toString();
//
//		/** 插入 默认分组 朋友 */
//		public static final String INSERT_CONTACT_GROUP3 = new StringBuilder().append("insert into ").append(DBTableName.TABLE_CONTACT_GROUPS)
//				.append(" ( ").append(ContactGroupColumns.NAME).append(",").append(ContactGroupColumns.ICON).append(",")
//				.append(ContactGroupColumns.ORDER_CODE).append(") values('朋友','img_groups_friend',3);").toString();

//		/** 插入 默认分组 同事 */
//		public static final String INSERT_CONTACT_GROUP4 = new StringBuilder().append("insert into ").append(DBTableName.TABLE_CONTACT_GROUPS)
//				.append(" ( ").append(ContactGroupColumns.NAME).append(",").append(ContactGroupColumns.ICON).append(",")
//				.append(ContactGroupColumns.ORDER_CODE).append(") values('同事','img_groups_schoolmate',4);").toString();
		
		/** 插入 默认分组 我的vip */
		public static final String INSERT_CONTACT_GROUP1 = new StringBuilder().append("insert into ").append(DBTableName.TABLE_CONTACT_GROUPS)
				.append(" ( ").append(ContactGroupColumns.NAME).append(",").append(ContactGroupColumns.ICON).append(",")
				.append(ContactGroupColumns.ORDER_CODE).append(") values('我的VIP', 'img_groups_vip', 1 );").toString();

		/** 插入 默认分组 家人 */
		public static final String INSERT_CONTACT_GROUP2 = new StringBuilder().append("insert into ").append(DBTableName.TABLE_CONTACT_GROUPS)
				.append(" ( ").append(ContactGroupColumns.NAME).append(",").append(ContactGroupColumns.ICON).append(",")
				.append(ContactGroupColumns.ORDER_CODE).append(") values('家人','img_groups_family',2 );").toString();

		/** 插入 默认分组 朋友 */
		public static final String INSERT_CONTACT_GROUP3 = new StringBuilder().append("insert into ").append(DBTableName.TABLE_CONTACT_GROUPS)
				.append(" ( ").append(ContactGroupColumns.NAME).append(",").append(ContactGroupColumns.ICON).append(",")
				.append(ContactGroupColumns.ORDER_CODE).append(") values('朋友','img_groups_friend',3);").toString();
		/** 插入 默认分组 同学 */
		public static final String INSERT_CONTACT_GROUP4 = new StringBuilder().append("insert into ").append(DBTableName.TABLE_CONTACT_GROUPS)
				.append(" ( ").append(ContactGroupColumns.NAME).append(",").append(ContactGroupColumns.ICON).append(",")
				.append(ContactGroupColumns.ORDER_CODE).append(") values('同学','img_groups_schoolmate',4);").toString();
		
		/** 联系人 表 插入 微片小秘书 */
		public static final String INSERT_CONTACT_MYTIP = new StringBuilder()
				.append("insert into ")
				.append(DBTableName.TABLE_CONTACTS).append("( ")
				.append(ContactColumns.CONTACT_ID).append(",").append(ContactColumns.VCARD_NO).append(",")
				.append(ContactColumns.ACCOUNT_ID).append(",").append(ContactColumns.ACCOUNT_NAME).append(",")
				.append(ContactColumns.DISPLAY_NAME).append(",").append(ContactColumns.AUTH).append(",")
				.append(ContactColumns.BIND_WAY).append(",").append(ContactColumns.BIRTHDAY).append(",")
				.append(ContactColumns.SEX).append(",").append(ContactColumns.SELF_SIGN).append(",")
				.append(ContactColumns.SCHOOL).append(",").append(ContactColumns.DEGREE).append(",")
				.append(ContactColumns.INTRODUCTION).append(",").append(ContactColumns.NATIVEPLACE).append(",")
				.append(ContactColumns.COUNTRY).append(",").append(ContactColumns.PROVINCE).append(",")
				.append(ContactColumns.CITY).append(",").append(ContactColumns.CARD_ID).append(",")
				.append(ContactColumns.COMPANY_NAME).append(",").append(ContactColumns.COMPANY_EN_NAME).append(",")
				.append(ContactColumns.COMPANY_APPROVAL).append(",").append(ContactColumns.COMPANY_HOME).append(",")
				.append(ContactColumns.COMPANY_COUNTRY).append(",").append(ContactColumns.COMPANY_PROVINCE).append(",")
				.append(ContactColumns.ADDRESS).append(",").append(ContactColumns.POSTCODE).append(",")
				.append(ContactColumns.BUSINESS).append(",").append(ContactColumns.JOB).append(",")
				.append(ContactColumns.MOBILE).append(",").append(ContactColumns.TELEPHONE).append(",")
				.append(ContactColumns.FAX).append(",").append(ContactColumns.EMAIL).append(",")
				.append(ContactColumns.INSTANT_MESSENGER).append(",").append(ContactColumns.SINA_BLOG_URL).append(",")
				.append(ContactColumns.TENCENT_QQ_URL).append(",").append(ContactColumns.TENCENT_BLOG_URL).append(",")
				.append(ContactColumns.REGISTER_TIME).append(",").append(ContactColumns.GROUP_ID).append(",")
				.append(ContactColumns.GROUP_NAME).append(",").append(ContactColumns.COMPANY_ABOUT).append(",")
				.append(ContactColumns.ORDER_CODE).append(") values (")
				.append(Constants.DefaultUser.DEFAULT_USER_CONTACT_KEY_ID).append(",").append("'").append(Constants.DefaultUser.DEFAULT_USER_VCARD_NO).append("',")
				.append(Constants.DefaultUser.DEFAULT_USER_ID)
				.append(",")
				.append("'")
				.append(Constants.DefaultUser.DEFAULT_USER_NAME)
				.append("',")
				.append("'")
				.append(Constants.DefaultUser.DEFAULT_USER_DISPLAY_NAME)
				.append("',")
				.append("1")
				.append(",")
				.append("3")
				.append(",")
				.append("'2012-12-21'")
				.append(",")
				.append("0")
				.append(",")
				.append("'您好，有任何问题随时咨询我哦！'")
				.append(",")
				.append("'微片大学客户服务学院'")
				.append(",")
				.append("'本科'")
				.append(",")
				.append("'        我是微片小秘书，如果您在使用软件过程中有遇到任何问题，或者想详细了解微片的使用方法和最新版本信息，欢迎随时咨询我哦！'")
				.append(",")
				.append("'福建 福州'")
				.append(",")
				.append("'中国'")
				.append(",")
				.append("'福建省'")
				.append(",")
				.append("'福州市'")
				.append(",")
				.append(Constants.DefaultUser.DEFAULT_USER_CARD_ID)
				.append(",")
				.append("'福建玛雅软件科技有限公司'")
				.append(",")
				.append("'FuJian Maya Software Technology Co.,Ltd'")
				.append(",")
				.append("1")
				.append(",")
				.append("'www.mayasoft.com.cn'")
				.append(",")
				.append("'中国'")
				.append(",")
				.append("'福建省'")
				.append(",")
				.append("'福州市工业路611号省高新技术科技创业园\n厦门市软件园二期望海路55号之二906室'")
				.append(",")
				.append("'350001'")
				.append(",")
				.append("1")
				.append(",")
				.append("'客户服务'")
				.append(",")
				.append("''")
				.append(",")
				.append("'0592-8264112'")
				.append(",")
				.append("'0592-8264113'")
				.append(",")
				.append("'vcard@mayasoft.com.cn'")
				.append(",")
				.append("'[{\"label\":\"X-QQ\",\"name\":\"Q Q\",\"value\":\"11777086\",\"protocol\":4},{\"label\":\"X-SINA-MICROBLOG\",\"name\":\"新浪微博\",\"value\":\"@玛雅微片\",\"protocol\":-1},{\"label\":\"X-TX-MICROBLOG\",\"name\":\"腾讯微博\",\"value\":\"@微片\",\"protocol\":-1},{\"label\":\"X-WEIXIN\",\"name\":\"微信\",\"value\":\"vCard2012\",\"protocol\":-1}]'")
				.append(",")
				.append("'http://weibo.com/u/3199207983'")
				.append(",")
				.append("'http://user.qzone.qq.com/1662983798'")
				.append(",")
				.append("'http://1.t.qq.com/vcard1'")
				.append(",")
				.append("'2012-12-21'")
				.append(",")
				.append("0")
				.append(",")
				.append("'未分组'")
				.append(",")
				.append("'        福建玛雅软件科技有限公司，简称玛雅软件，成立于2012年12月21日玛雅预言中的“世界末日”。这天是玛雅历法中第四太阳纪的末日，也是第五太阳纪的开始，蕴含世间万物 “毁灭与重生”之轮回大道。\n        玛雅软件以“畅想科技开发中心”技术力量为主体，主要致力于移动互联网领域的传统商务社交“无纸化”应用工具的开发和运营，也是全球领先的移动互联网电子名片应用服务商和企业电子画册应用服务商。 \n        志存高远，锐意进取。我们秉承“客户服务价值优先”的经营理念，坚守“简单、专注、激情、共享”的企业精神，引进现代企业管理机制，追求完善的客户体验服务并致力于帮助他人成功，树立一种社会效益和经济效益双赢的企业价值观，借以成为一个真正的人本化先进团队。\n        一张名片的交换，一棵树木倒下；一本画册的问世，一片森林倒下。为了我们的家园，请节约纸张，绿色商务，树会感谢你！“少砍一棵树，少用一张纸，守护绿色地球”是我们玛雅人最崇高的愿景和毕生追求!'")
				.append(",")
				.append("'WEIPIANXIAOMISHU'")
				.append(");").toString();
		/** 联系人表 更新 微片小秘书 **/
		public static final String UPDATE_CONTACT_MYTIP = "update contacts set order_code = 'WEIPIANXIAOMISHU' where vcard_no = 10000000";
		private static final Context mContext = ActivityHelper.getGlobalApplicationContext();
		/** 联系人表 更新 微片小秘书   公司资料 **/
		public static final String UPDATE_CONTACT_MYTIP_INFO = "update contacts set "+ 
				ContactColumns.COMPANY_NAME + " = '" + mContext.getString(R.string.mytip_info_company_name) +"', " +
				ContactColumns.COMPANY_EN_NAME + " = '" + mContext.getString(R.string.mytip_info_company_about) + "', " +
				ContactColumns.TELEPHONE + " = '" + mContext.getString(R.string.mytip_info_company_telephone) + "', " +
				ContactColumns.FAX + " = '" + mContext.getString(R.string.mytip_info_company_fax) + "', " +
				ContactColumns.COMPANY_ABOUT + " = '" + mContext.getString(R.string.mytip_info_company_about) + "', " +
				ContactColumns.ADDRESS + " = '" + mContext.getString(R.string.mytip_info_company_address) + "' " +
				" where " + ContactColumns.VCARD_NO + " = 10000000";
		/** 插入微片小秘书消息1 */
		public static final String INSERT_MESSAGE_MYTIP1 = new StringBuilder().append("INSERT INTO ").append(DBTableName.TABLE_MESSAGES).append("(")
				.append(MessageColumns.OTHER_ACCOUNT_ID).append(",")
				.append(MessageColumns.OTHER_CARD_ID).append(",")
				.append(MessageColumns.OTHER_DISPLAY_NAME).append(",")
				.append(MessageColumns.CONTENT_TYPE).append(",")
				.append(MessageColumns.BODY).append(",")
				.append(MessageColumns.SEND_TYPE).append(",")
				.append(MessageColumns.ENABLE).append(",")
				.append(MessageColumns.CREATE_TIME)
				.append(") values (")
				.append(Constants.DefaultUser.DEFAULT_USER_ID).append(",")
				.append(Constants.DefaultUser.DEFAULT_USER_CARD_ID).append(",")
				.append("'").append(Constants.DefaultUser.DEFAULT_USER_DISPLAY_NAME).append("',")
				.append("11").append(",")
				.append("'您好!我是微片小秘书,有什么可以帮您的?'").append(",")
				.append(MessageSendType.RECIVER).append(",")
				.append("1,")
				.append("'").append(ResourceHelper.getNowTime()).append("'")
				.append(");")
				.toString();
		/**
		 * /**插入微片小秘书消息 2
		 */
		public static final String INSERT_MESSAGE_MYTIP2 = new StringBuilder().append("INSERT INTO ").append(DBTableName.TABLE_MESSAGES).append("(")
				.append(MessageColumns.OTHER_ACCOUNT_ID).append(",")
				.append(MessageColumns.OTHER_CARD_ID).append(",")
				.append(MessageColumns.OTHER_DISPLAY_NAME).append(",")
				.append(MessageColumns.CONTENT_TYPE).append(",")
				.append(MessageColumns.BODY).append(",")
				.append(MessageColumns.SEND_TYPE).append(",")
				.append(MessageColumns.ENABLE).append(",")
				.append(MessageColumns.CREATE_TIME).append(") values (")
				.append(Constants.DefaultUser.DEFAULT_USER_ID).append(",")
				.append(Constants.DefaultUser.DEFAULT_USER_CARD_ID).append(",").append("'")
				.append(Constants.DefaultUser.DEFAULT_USER_DISPLAY_NAME).append("',")
				.append("11,")
				.append("'您好，我们当前发布的是微片公测版本，可能有一些功能还不够完善，如果您在使用过程中发现bug，请将bug反馈给我们。由此造成的不便，我们深感歉意！我们将持续改进产品功能，为您提供更好的用户体验！'").append(",")
				.append(MessageSendType.RECIVER).append(",")
				.append("1,")
				.append("'").append(ResourceHelper.getNowTime()).append("'").append(");").toString();
	}

}
