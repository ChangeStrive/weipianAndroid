package com.maya.android.vcard.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.CardEntity;

import java.io.IOException;

/**
 * NFC帮助类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2015-7-10
 *
 */
public class NFCHelper {
//	"BEGIN:VCARD\r\nVERSION:3.0\r\nN:林触碰6;;;;\r\nFN:BUSINESS_CARD\r\n" +
//			"TITLE:董事长助理\r\nORG:触碰（厦门）智能科技有限公司\r\nWEB_SITE.URL:www.cupeng.com.cn\r\n" +
//			"ADR:;;福建省厦门软件园二期望海路55号之二906室;;;;\r\nTEL;TYPE=CELL,WORK:13023823112\r\n" +
//			"TEL;TYPE=VIDEO,WORK:0592-8264112\r\nTEL;TYPE=FAX,WORK:0592-8264113\r\nEND:VCARD";
//	private static final String NFC_DATA_FORMAT = "BEGIN:VCARD\r\nVERSION:3.0\r\n" +
//			"N:%1$s;\r\nFN:BUSINESS_CARD\r\n" +  //名字
//			"TITLE:%2$s;\r\nORG:%3$s\r\n;WEB_SITE.URL:%4$s\r\nADR;WORK:%5$s;\r\n" +  //职位  /公司 /网址 /地址
//			"TEL;CELL:%6$s;\r\nTEL;WORK:%7$s;\r\nEMAIL;WORK:%8$s;\r\nFAX,WORK:%9$s;\r\n" + //手机 /电话  /Email /传真
//			"NOTE:名片信息由[微片团队]提供;\r\nEND:VCARD";
	private static final String NFC_DATA_FORMAT = ActivityHelper.getGlobalApplicationContext().getString(R.string.nfc_data_format);
	//#region 常量

	//#endregion 常量

	//#region 静态成员变量
	private static boolean isInit = false;
	private static boolean isOpenNFC = false;
	private static boolean hasNFC = false;
	private static NFCHelper sInstance;
	//#endregion 静态成员变量

	//#region 成员变量
	private NfcAdapter mNFCAdapter;
	//#endregion 成员变量

	//#region Getter & Setter集
	private static NFCHelper getInstance(){
		if(Helper.isNull(sInstance)){
			sInstance = new NFCHelper();
		}
		return sInstance;
	}
	//#endregion Getter & Setter集

	//#region 构造方法
	private NFCHelper(){
		init(ActivityHelper.getGlobalApplicationContext());
	}
	//#endregion 构造方法

	//#region public 方法
	/**
	 * 更新NFC状态（最好在用到的Activity中onResume调用）
	 */
	public static void updateStatus(Context context){
		getInstance().init(context);
	}
	/**
	 * 设备是否有NFC功能
	 * @return
	 */
	public static boolean hasNFC(){
		if(!isInit){
			getInstance();
		}
		return hasNFC;
	}
	/**
	 * NFC是否开启
	 * @return
	 */
	public static boolean isOpenNFC(){
		if(!isInit){
			getInstance();
		}
		return isOpenNFC;
	}
	/**
	 * 获取NFCAdapter
	 * @return
	 */
	public static NfcAdapter getNfcAdapter(){
		return getInstance().mNFCAdapter;
	}
	/**
	 * 写入NFC
	 * @param entity
	 * @param displayName
	 */
	public static void writeInNFC(CardEntity entity, String displayName, Intent intent){
		getInstance().writeNFC(entity, displayName, intent);
	}
	//#endregion public 方法

	//#region private 方法
	@SuppressLint("NewApi")
	private void init(Context context){
		isInit = true;
		this.mNFCAdapter = NfcAdapter.getDefaultAdapter(context);
		hasNFC = Helper.isNotNull(this.mNFCAdapter);
		isOpenNFC = hasNFC && this.mNFCAdapter.isEnabled();
	}
	
	@SuppressLint("NewApi")
	private void writeNFC(CardEntity entity, String displayName, Intent intent){
		if(Helper.isNotNull(entity)){
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String name;
			if(Helper.isNotEmpty(displayName)){
				name = displayName;
			}else{
				name = entity.getCardName();
			}
			String data = String.format(NFC_DATA_FORMAT, name, name,
					entity.getJob(), entity.getCompanyName(), entity.getCompanyHome(), entity.getWorkAddress(),
					entity.getMobileTelphone(), entity.getLineTelphone(), entity.getEmail(), entity.getFax());
			byte[] dataBytes = data.getBytes();
			NdefRecord dataRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
					"text/vcard".getBytes(), new byte[] {}, dataBytes);
			NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] { dataRecord });
			this.writeTag(ndefMessage, tag);
			
		}else{
			ActivityHelper.showToast(R.string.nfc_no_data);
		}
	}
	
	@SuppressLint("NewApi")
	private boolean writeTag(NdefMessage message, Tag tag) {
		int size = message.toByteArray().length;
		try {
			Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				ndef.connect();

				if (!ndef.isWritable()) {
					ActivityHelper.showToast(R.string.nfc_tag_not_allow_writed);
					return false;
				}
				if (ndef.getMaxSize() < size) {
					ActivityHelper.showToast(R.string.nfc_tag_undercapacity);
					return false;
				}

				ndef.writeNdefMessage(message);
				ActivityHelper.showToast(R.string.nfc_write_success);
				
				return true;
			} else {
				NdefFormatable format = NdefFormatable.get(tag);
				if (format != null) {
					try {
						format.connect();
						format.format(message);
						ActivityHelper.showToast(R.string.nfc_write_success);
						return true;
					} catch (IOException e) {
						ActivityHelper.showToast(R.string.nfc_tag_not_allow_writed);
						return false;
					}
				} else {
					ActivityHelper.showToast(R.string.nfc_tag_not_allow_writed);
					return false;
				}
			}
		} catch (Exception e) {
			ActivityHelper.showToast(R.string.nfc_write_fail);
		}

		return false;
	}
	//#endregion private 方法
}
