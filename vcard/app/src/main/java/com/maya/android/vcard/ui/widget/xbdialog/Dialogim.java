package com.maya.android.vcard.ui.widget.xbdialog;

import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.widget.xbdialog.adapters.AbstractWheelTextAdapter;
import com.maya.android.vcard.ui.widget.xbdialog.wheelcity.AddressData;
import com.maya.android.vcard.ui.widget.xbdialog.wheelcity.OnWheelChangedListener;
import com.maya.android.vcard.ui.widget.xbdialog.wheelcity.WheelView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 城市选择用到的view
 * @author Administrator
 *
 */
public class Dialogim {
	private static String DIA_PROVINCES = "";// 记录选择的省份
	private static String DIA_CITIES = "";// 记录选择的市区
	private static String DIA_COUNTIES = "";// 记录选择的城市去区域
	private static Dialogim intance;

	private Dialogim() {

	}

	public static Dialogim getIntance() {
		if (intance == null) {
			intance = new Dialogim();
		}
		return intance;

	}

	/**
	 * 获取选择的省份
	 * 
	 * @return
	 */
	public String getProvinces() {
		return DIA_PROVINCES;

	}

	/**
	 * 获取选择的市级
	 * 
	 * @return
	 */
	public String getCities() {
		return DIA_CITIES;
	}
	
	/**
	 * 获取城市区域
	 * @return
	 */
	public String getCounties() {
		return DIA_COUNTIES;
	}

	/**
	 * 获取3级联动城市选择器的view
	 * 
	 * @param mContext
	 * @return
	 */
	@SuppressLint("InflateParams")
	public View getDialogim(final Context mContext) {
		View contentView = LayoutInflater.from(mContext).inflate(R.layout.wheelcity_cities_layout, null);
		final WheelView country = (WheelView) contentView.findViewById(R.id.wheelcity_country);
		country.setVisibleItems(3);
		country.setViewAdapter(new CountryAdapter(mContext));

		final String cities[][] = AddressData.CITIES;
		final String ccities[][][] = AddressData.COUNTIES;
		final WheelView city = (WheelView) contentView.findViewById(R.id.wheelcity_city);
		city.setVisibleItems(0);

		// 地区选择
		final WheelView ccity = (WheelView) contentView.findViewById(R.id.wheelcity_ccity);
		ccity.setVisibleItems(0);// 不限城市

		country.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateCities(mContext, city, cities, newValue);
				DIA_PROVINCES = AddressData.PROVINCES[country.getCurrentItem()];
				// + " | "
				// + AddressData.CITIES[country.getCurrentItem()][city
				// .getCurrentItem()]
				// + " | "
				// + AddressData.COUNTIES[country.getCurrentItem()][city
				// .getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		city.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updatecCities(mContext, ccity, ccities,
						country.getCurrentItem(), newValue);
				DIA_CITIES =
				// AddressData.PROVINCES[country.getCurrentItem()]
				// + " | "+
				AddressData.CITIES[country.getCurrentItem()][city
						.getCurrentItem()];
				// + " | "
				// + AddressData.COUNTIES[country.getCurrentItem()][city
				// .getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		ccity.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				DIA_COUNTIES =
				// AddressData.PROVINCES[country.getCurrentItem()]
				// + " | "
				// + AddressData.CITIES[country.getCurrentItem()][city
				// .getCurrentItem()]
				// + " | "+
				AddressData.COUNTIES[country.getCurrentItem()][city
						.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		country.setCurrentItem(1);// 设置北京
		city.setCurrentItem(1);
		ccity.setCurrentItem(1);
		return contentView;
	}

	/**
	 * Adapter for countries
	 */
	private class CountryAdapter extends AbstractWheelTextAdapter {
		// Countries names
		private String countries[] = AddressData.PROVINCES;

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context mContext) {
			super(mContext, R.layout.wheelcity_country_layout, NO_RESOURCE);
			setItemTextResource(R.id.wheelcity_country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return countries.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return countries[index];
		}
	}

	/**
	 * Updates the city wheel
	 */
	private static void updateCities(Context mContext, WheelView city,
			String cities[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				mContext, cities[index]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}

	/**
	 * Updates the ccity wheel
	 */
	private static void updatecCities(Context mContext, WheelView city,
			String ccities[][][], int index, int index2) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				mContext, ccities[index][index2]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}

}
