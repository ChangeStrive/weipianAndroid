package com.maya.android.utils.base;
/**
 * FormFile
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-7-3
 *
 */
public class FormFile {
	private byte[] data;
	private String fileName;
	private String formName;
	private String contentType = "text/plain";

	public FormFile(String fileName, byte[] data, String formName,
			String contentType) {
		this.data = data;
		this.fileName = fileName;
		this.formName = formName;
		if (contentType != null)
			this.contentType = contentType;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * @return the filName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param filName
	 *            the filName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the formName
	 */
	public String getFormName() {
		return formName;
	}

	/**
	 * @param formName
	 *            the formName to set
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}