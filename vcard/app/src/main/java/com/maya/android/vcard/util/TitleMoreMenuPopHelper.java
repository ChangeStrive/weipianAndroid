package com.maya.android.vcard.util;

import android.content.Context;

import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.TitleMoreMenuLsvIconEntity;
import com.maya.android.vcard.ui.widget.TitleMoreMenuPopView;

import java.util.ArrayList;

/**
 * 标题栏右侧更多菜单帮助类
 * Created by Administrator on 2015/9/14.
 */
public class TitleMoreMenuPopHelper {


    /**
     * 我的名片主页弹窗
     * @param mContext
     * @return
     */
    public static TitleMoreMenuPopView getVcardMainPop(Context mContext){
        return getTitleMoreMenuPopView(mContext, getCardMainPopLsv());
    }

    /**
     * 名片夹主页弹窗内容
     * @return
     */
    public static  ArrayList<TitleMoreMenuLsvIconEntity> getCardMainPopLsv(){
        ArrayList<TitleMoreMenuLsvIconEntity> menus = new ArrayList<TitleMoreMenuLsvIconEntity>();
        TitleMoreMenuLsvIconEntity menu = new TitleMoreMenuLsvIconEntity();
        menus.add(getMoreMenuItem(menu, R.string.pop_add_card, R.mipmap.img_pop_add_case_card));
        menus.add(getMoreMenuItem(menu, R.string.pop_group_management, R.mipmap.img_pop_case_grouping));
        menus.add(getMoreMenuItem(menu, R.string.pop_batch_operation, R.mipmap.img_pop_case_piliang));
        menus.add(getMoreMenuItem(menu, R.string.pop_backup_restore, R.mipmap.img_pop_case_recover));
        menus.add(getMoreMenuItem(menu, R.string.pop_cloud_search, R.mipmap.img_pop_cloud_find));
//        menus.add(getMoreMenuItem(menu, R.string.pop_nearby_friends, R.mipmap.img_pop_case_nearby));
        return menus;
    }

    /**
     * 我的名片详情页弹窗
     * @param mContext
     * @return
     */
    public static TitleMoreMenuPopView getVcardDetailPop(Context mContext){
        return getTitleMoreMenuPopView(mContext, getCardDetailPopLsv());
    }


    /**
     * 名片详情页弹窗内容
     * @return
     */
    public static  ArrayList<TitleMoreMenuLsvIconEntity> getCardDetailPopLsv(){
        ArrayList<TitleMoreMenuLsvIconEntity> menus = new ArrayList<TitleMoreMenuLsvIconEntity>();
        TitleMoreMenuLsvIconEntity menu = new TitleMoreMenuLsvIconEntity();
        menus.add(getMoreMenuItem(menu, R.string.pop_telephone, R.mipmap.img_pop_telephone, R.mipmap.img_pop_telephone_gay));
        menus.add(getMoreMenuItem(menu, R.string.pop_send_msg, R.mipmap.img_pop_send_msg, R.mipmap.img_pop_send_msg_gay));
        menus.add(getMoreMenuItem(menu, R.string.pop_send_email, R.mipmap.img_pop_send_email, R.mipmap.img_pop_send_email_gay));
        menus.add(getMoreMenuItem(menu, R.string.pop_navigation, R.mipmap.img_pop_navigation, R.mipmap.img_pop_navigation_gay));
        menus.add(getMoreMenuItem(menu, R.string.pop_vcard_forward, R.mipmap.img_pop_vcard_forward, R.mipmap.img_pop_vcard_forward_gay));
        menus.add(getMoreMenuItem(menu, R.string.pop_vcard_del, R.mipmap.img_pop_vcard_del, R.mipmap.img_pop_vcard_del_gay));
        menus.add(getMoreMenuItem(menu, R.string.pop_report, R.mipmap.img_pop_report, R.mipmap.img_pop_report_gay));
        return menus;
    }

    /**
     *名片资料编辑弹窗
     * @param mContext
     * @return
     */
    public static TitleMoreMenuPopView getCardInfoEditPop(Context mContext){
        return getTitleMoreMenuPopView(mContext, getCardInfoEditPopLsv());
    }

    /**
     * 名片资料编辑弹窗内容
     * @return
     */
    public static  ArrayList<TitleMoreMenuLsvIconEntity> getCardInfoEditPopLsv(){
        ArrayList<TitleMoreMenuLsvIconEntity> menus = new ArrayList<TitleMoreMenuLsvIconEntity>();
        TitleMoreMenuLsvIconEntity menu = new TitleMoreMenuLsvIconEntity();
        menus.add(getMoreMenuItem(menu, R.string.re_shooting_recognition, R.mipmap.img_pop_add_case_card, R.mipmap.img_pop_add_case_card_gay));
        menus.add(getMoreMenuItem(menu, R.string.upload_electronic_business_card, R.mipmap.img_pop_update_card, R.mipmap.img_pop_update_card_gay));
        menus.add(getMoreMenuItem(menu, R.string.change_business_card_template, R.mipmap.img_pop_chance_card, R.mipmap.img_pop_chance_card_gay));
        menus.add(getMoreMenuItem(menu, R.string.pop_vcard_del, R.mipmap.img_pop_vcard_del, R.mipmap.img_pop_vcard_del_gay));
        return menus;
    }

    /**
     * 单位认证弹窗
     * @param mContext
     * @return
     */
    public static TitleMoreMenuPopView getUnitDetailPop(Context mContext){
        return getTitleMoreMenuPopView(mContext, getUnitDetailPopLsv());
    }

    /**
     * 单位认证弹窗内容
     * @return
     */
    public static  ArrayList<TitleMoreMenuLsvIconEntity> getUnitDetailPopLsv(){
        ArrayList<TitleMoreMenuLsvIconEntity> menus = new ArrayList<TitleMoreMenuLsvIconEntity>();
        TitleMoreMenuLsvIconEntity menu = new TitleMoreMenuLsvIconEntity();
        menus.add(getMoreMenuItem(menu, R.string.unit_auth, R.mipmap.img_pop_v, R.mipmap.img_pop_v_gay));
        menus.add(getMoreMenuItem(menu, R.string.edt_unit_Introduction, R.mipmap.img_pop_company_edit, R.mipmap.img_pop_company_edit_gay));
        menus.add(getMoreMenuItem(menu, R.string.release_notice, R.mipmap.img_pop_company_announce, R.mipmap.img_pop_company_announce_gay));
        menus.add(getMoreMenuItem(menu, R.string.permission_set, R.mipmap.img_pop_auth, R.mipmap.img_pop_auth_gay));
        menus.add(getMoreMenuItem(menu, R.string.administration_member, R.mipmap.img_pop_member, R.mipmap.img_pop_member_gay));
        return menus;
    }

    /**
     * 用户资料弹窗
     * @param mContext
     * @param mSocialRelation
     * @return
     */
    public static TitleMoreMenuPopView getUserInfoPop(Context mContext, int mSocialRelation){
        return getTitleMoreMenuPopView(mContext, getUserInfoPopLsv(mSocialRelation));
    }

    /**
     * 用户资料弹窗内容
     * @param mSocialRelation 社交关系
     * @return
     */
    public static ArrayList<TitleMoreMenuLsvIconEntity> getUserInfoPopLsv(int mSocialRelation){
        ArrayList<TitleMoreMenuLsvIconEntity> menus = new ArrayList<TitleMoreMenuLsvIconEntity>();
        TitleMoreMenuLsvIconEntity menu = new TitleMoreMenuLsvIconEntity();
        if(mSocialRelation == Constants.ScoialRelAtion.SCOIAL_ATTENTION || mSocialRelation == Constants.ScoialRelAtion.SCOIAL_MUTUAL_ATTENTION){
            //已关注
            menus.add(getMoreMenuItem(menu, R.string.cancel_attention, R.mipmap.img_pop_vcard_del, R.mipmap.img_pop_vcard_del_gay));
        }else{
            //好友
            menus.add(getMoreMenuItem(menu, R.string.friend_delete, R.mipmap.img_pop_vcard_del, R.mipmap.img_pop_vcard_del_gay));
        }
        menus.add(getMoreMenuItem(menu, R.string.pop_report, R.mipmap.img_pop_report, R.mipmap.img_pop_report_gay));
        menus.add(getMoreMenuItem(menu, R.string.remarks, R.mipmap.img_pop_remarks, R.mipmap.img_pop_remarks_gay));
        return menus;
    }

    /**
     *  微片通讯录弹窗
     * @param mContext
     * @return
     */
    public static TitleMoreMenuPopView getCardBookPop(Context mContext){
        return getTitleMoreMenuPopView(mContext, getCardBookPopLsv());
    }

    /**
     * 微片通讯录弹窗内容
     * @return
     */
    public static ArrayList<TitleMoreMenuLsvIconEntity> getCardBookPopLsv(){
        ArrayList<TitleMoreMenuLsvIconEntity> menus = new ArrayList<TitleMoreMenuLsvIconEntity>();
        TitleMoreMenuLsvIconEntity menu = new TitleMoreMenuLsvIconEntity();
        menus.add(getMoreMenuItem(menu, R.string.add_contact, R.mipmap.img_pop_add_contact, R.mipmap.img_pop_add_contact_gay));
        menus.add(getMoreMenuItem(menu, R.string.pop_group_management, R.mipmap.img_pop_case_grouping, R.mipmap.img_pop_case_grouping_gay));
        menus.add(getMoreMenuItem(menu, R.string.pop_batch_operation, R.mipmap.img_pop_case_piliang, R.mipmap.img_pop_case_piliang_gay));
        menus.add(getMoreMenuItem(menu, R.string.add_contact, R.mipmap.img_pop_report, R.mipmap.img_pop_report_gay));
        menus.add(getMoreMenuItem(menu, R.string.pop_backup_restore, R.mipmap.img_pop_case_recover, R.mipmap.img_pop_case_recover_gay));
        menus.add(getMoreMenuItem(menu, R.string.cloud_swap_card, R.mipmap.img_pop_swap_card, R.mipmap.img_pop_swap_card_gay));
        return menus;
    }
    /**
     * 二维码扫一扫弹窗
     * @return
     */
    public static TitleMoreMenuPopView getQrcodeScanPop(Context mContext){
        return getTitleMoreMenuPopView(mContext, getQrcodeScanPopLsv());
    }

    /**
     * 获取二维码扫一扫内容
     * @return
     */
    public static ArrayList<TitleMoreMenuLsvIconEntity> getQrcodeScanPopLsv(){
        ArrayList<TitleMoreMenuLsvIconEntity> menus = new ArrayList<TitleMoreMenuLsvIconEntity>();
        TitleMoreMenuLsvIconEntity menu = new TitleMoreMenuLsvIconEntity();
        menus.add(getMoreMenuItem(menu, R.string.select_qr_code_from_picture, 0, 0));
        menus.add(getMoreMenuItem(menu, R.string.my_vcard_qrcode, 0, 0));
        return menus;
    }

     /**
     *  获取popWind弹窗
     * @param mContext
     * @param menus
     * @return
     */
    public static TitleMoreMenuPopView getTitleMoreMenuPopView(Context mContext,  ArrayList<TitleMoreMenuLsvIconEntity> menus){
        TitleMoreMenuPopView mTitleMoreMenuPop = new TitleMoreMenuPopView(mContext);
        mTitleMoreMenuPop.setItemLsv(menus);
        return mTitleMoreMenuPop;
    }

    /**
     * 克隆实体类
     * @param item
     * @param resId
     * @param iconId
     * @return
     */
    private static TitleMoreMenuLsvIconEntity getMoreMenuItem(TitleMoreMenuLsvIconEntity item, int resId, int iconId){
        return getMoreMenuItem(item, resId, iconId, 0);
    }

    /**
     * 克隆实体类
     * @param item
     * @param resId
     * @param iconId
     * @param iconGayId
     * @return
     */
    private static TitleMoreMenuLsvIconEntity getMoreMenuItem(TitleMoreMenuLsvIconEntity item, int resId, int iconId, int iconGayId){
        TitleMoreMenuLsvIconEntity mItem = null;
        try {
            mItem = (TitleMoreMenuLsvIconEntity) item.clone();
            mItem.setBusinessName(resId);
            mItem.setIconId(iconId);
            mItem.setIconGayId(iconGayId);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return mItem;
    }
}
