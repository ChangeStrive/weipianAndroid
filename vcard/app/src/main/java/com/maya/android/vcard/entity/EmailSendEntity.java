package com.maya.android.vcard.entity;

public class EmailSendEntity {
	/** 设置邮件类型为纯文本，即为所有可见字符与一些简单的控制符的组合 **/
	private String type = "plain/text";
	/** 收件人地址   **/
	private String[] arrReceiver;
	/** 抄送地址 **/
	private String[] arrCc;
	/** 密送地址  */
	private String[] arrBcc;
	/** 标题 **/
	private String subject;
	/** 内容 **/
	private String body;
	/** 指定要添加的附件的完整路径  **/
	private String attachPath;
	
	public EmailSendEntity(){
		
	}
	public EmailSendEntity(String[] receiverEmailArr){
		this.arrReceiver = receiverEmailArr;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String[] getArrReceiver() {
		return arrReceiver;
	}
	public void setArrReceiver(String[] arrReceiver) {
		this.arrReceiver = arrReceiver;
	}
	public String[] getArrCc() {
		return arrCc;
	}
	public void setArrCc(String[] arrCc) {
		this.arrCc = arrCc;
	}
	public String[] getArrBcc() {
		return arrBcc;
	}
	public void setArrBcc(String[] arrBcc) {
		this.arrBcc = arrBcc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAttachPath() {
		return attachPath;
	}
	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}
}
