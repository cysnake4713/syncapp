package com.cysnake.syncapp.act;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class MainViewPager extends Activity {

	private ViewPager mainViewPager;
	private MainViewAdapter mainViewAdapter;
	private ArrayList<View> viewList;

	private LayoutInflater inflater;
	private View accountLayout;
	private View contactLayout;
	private View favoriteLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewList = new ArrayList<View>();
		setContentView(R.layout.main);
		mainViewPager = (ViewPager) findViewById(R.id.main_viewpager);
		mainViewAdapter = new MainViewAdapter();
		mainViewPager.setAdapter(mainViewAdapter);
		inflater = getLayoutInflater();
		accountLayout = inflater.inflate(R.layout.activity_account, null);
		contactLayout = inflater.inflate(R.layout.activity_contact, null);
		viewList.add(accountLayout);
		viewList.add(contactLayout);
		mainViewPager.setCurrentItem(0);
	}

	private class MainViewAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(viewList.get(position), 0);
			return viewList.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(viewList.get(position));
		}

	}

}
