package com.maya.android.vcard.entity;

/**
 * Description:行业常量枚举类
 * User: oujie
 * Update: oujie(2013-05-10 09:17)
 */
public enum BusinessEnum {
    DEFAULT(0,""),
    IT(1,"计算机/互联网/通信"),MANUFACTURE(2,"生产/工艺/制造业"),MINING_INDUSTRY(3,"能源/化工/冶金/矿业"),
    TRADE(4,"商业/贸易/采购/零售/个体"),SERVICE(5,"酒店/餐饮/旅游/服务业"),FINANCIAL_INDUSTRY(6,"金融/银行/保险/投资/财税"),
    MEDIA(7,"文化/广告/公关/传媒/新闻"),ART(8,"影视/娱乐/艺术/表演"),TENEMENT(9,"建筑/房地产/装修/物业"),
    TRAFFIC(10,"交通/运输/物流"),MEDICINE(11,"医疗/护理/制药/美容/保健"),AGRICULTURE(12,"农业/林业/畜牧/养殖"),
    COUNSEL(13,"律师/法务/咨询"),EDUCATION(14,"学校/教育/培训"),INSTITUTION(15,"政府/公务员/事业单位/协会"),
    UNEMPLOYED(16,"自由职业者/待业/失业/退休"),STUDENT(17,"在校学生"),OTHER(18,"其他");

    private final static String[] iconNameArr = new String[]{"","it","manufacture","mining_industry","trade","service","finance","media","art","tenement"
    		,"traffic","medicine","agriculture","counsel","edu","institution","unemployed","student","other"};
    
    private final static String ICON_PREFIX = "img_choose_business_"; //图标前缀
    private final static String ICON_SUFFIX = ""; //图标后缀
    public final static int length = 18;
    private int code ; //行业代码
    private String name; //行业名称
    BusinessEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据给定编码，获取行业信息
     * 如果给定code未定义，则返回 DEFAULT(0,"")
     * @param code
     * @return
     */
    public static BusinessEnum getBusinessByCode(int code){
        switch (code){
            case 1:return IT;
            case 2:return MANUFACTURE;
            case 3:return MINING_INDUSTRY;
            case 4:return TRADE;
            case 5:return SERVICE;
            case 6:return FINANCIAL_INDUSTRY;
            case 7:return MEDIA;
            case 8:return ART;
            case 9:return TENEMENT;
            case 10:return TRAFFIC;
            case 11:return MEDICINE;
            case 12:return AGRICULTURE;
            case 13:return COUNSEL;
            case 14:return EDUCATION;
            case 15:return INSTITUTION;
            case 16:return UNEMPLOYED;
            case 17:return STUDENT;
            case 18:return OTHER;
            default: return DEFAULT;
        }
    }
    
   

    /**
     * 根据给定编码，获取行业名称信息
     * @param code
     * @return
     */
    public static String getBusinessNameByCode(int code){
        BusinessEnum business = getBusinessByCode(code);
        return business.getName();
    }

    	
    /**
     * 根据给定编码，获取行业图标信息
     * 图片文件名,格式为：ICON_PREFIX+name+ICON_SUFFIX
     * @param code
     * @return
     */
    public static String getBusinessIconNameByCode(int code){
        String iconImgName = null;
        if(!"".equals(iconNameArr[code]) )
        	iconImgName = ICON_PREFIX + iconNameArr[code] + ICON_SUFFIX;
        return iconImgName;
    }
}
