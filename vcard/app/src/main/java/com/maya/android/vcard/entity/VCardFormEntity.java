package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;
/**
 * 我的名片格式参数Entity
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-11-28
 *
 */
public class VCardFormEntity {
	@SerializedName("45x90")
	private CardFormEntity form4590;
	@SerializedName("54x90")
	private CardFormEntity form5490;
	@SerializedName("94x90")
	private CardFormEntity form9490;
	
	public CardFormEntity getForm4590() {
		return form4590;
	}

	public void setForm4590(CardFormEntity form4590) {
		this.form4590 = form4590;
	}

	public CardFormEntity getForm5490() {
		return form5490;
	}

	public void setForm5490(CardFormEntity form5490) {
		this.form5490 = form5490;
	}

	public CardFormEntity getForm9490() {
		return form9490;
	}

	public void setForm9490(CardFormEntity form9490) {
		this.form9490 = form9490;
	}
	/**
	 * 我的名片类型参数Entity
	 * @author ZuoZiJi-Y.J
	 * @version v1.0
	 * @since 2013-11-28
	 *
	 */
	public class CardFormEntity{
		@SerializedName("cardTop")
		private int cardTop;
		@SerializedName("cardLeft")
		private int cardLeft;
		@SerializedName("cardBottom")
		private int cardBottom;
		@SerializedName("markRight")
		private int markRight;
		@SerializedName("markBottom")
		private int markBottom;
		@SerializedName("scanWidth")
		private int scanWidth;
		@SerializedName("scanHeight")
		private int scanHeight;
		@SerializedName("sideWidth")
		private int sideWidth;
		@SerializedName("sideHeight")
		private int sideHeight;
		@SerializedName("sideBottom")
		private int sideBottom;
		@SerializedName("sidItemHeight")
		private int sidItemHeight;
		public int getCardTop() {
			return cardTop;
		}
		public void setCardTop(int cardTop) {
			this.cardTop = cardTop;
		}
		public int getCardLeft() {
			return cardLeft;
		}
		public void setCardLeft(int cardLeft) {
			this.cardLeft = cardLeft;
		}
		public int getCardBottom() {
			return cardBottom;
		}
		public void setCardBottom(int cardBottom) {
			this.cardBottom = cardBottom;
		}
		public int getMarkRight() {
			return markRight;
		}
		public void setMarkRight(int markRight) {
			this.markRight = markRight;
		}
		public int getMarkBottom() {
			return markBottom;
		}
		public void setMarkBottom(int markBottom) {
			this.markBottom = markBottom;
		}
		public int getScanWidth() {
			return scanWidth;
		}
		public void setScanWidth(int scanWidth) {
			this.scanWidth = scanWidth;
		}
		public int getScanHeight() {
			return scanHeight;
		}
		public void setScanHeight(int scanHeight) {
			this.scanHeight = scanHeight;
		}
		public int getSideWidth() {
			return sideWidth;
		}
		public void setSideWidth(int sideWidth) {
			this.sideWidth = sideWidth;
		}
		public int getSideHeight() {
			return sideHeight;
		}
		public void setSideHeight(int sideHeight) {
			this.sideHeight = sideHeight;
		}
		public int getSideBottom() {
			return sideBottom;
		}
		public void setSideBottom(int sideBottom) {
			this.sideBottom = sideBottom;
		}
		public int getSidItemHeight() {
			return sidItemHeight;
		}
		public void setSidItemHeight(int sidItemHeight) {
			this.sidItemHeight = sidItemHeight;
		}
		
	}
}
