package com.cysnake.syncapp.act;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainViewPager extends FragmentActivity {

	private ViewPager mainViewPager;
	private MainViewAdapter mainViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mainViewPager = (ViewPager) findViewById(R.id.main_viewpager);
		mainViewAdapter = new MainViewAdapter(getSupportFragmentManager());
		mainViewPager.setAdapter(mainViewAdapter);

	}

	private class MainViewAdapter extends FragmentPagerAdapter {

		public MainViewAdapter(FragmentManager fm) {
			super(fm);

		}

		@Override
		public Fragment getItem(int index) {
			switch (index) {
			case 0:
				return new ContactFragment();

			default:
				return new AccountFragment();
			}

		}

		@Override
		public int getCount() {
			return 2;
		}

	}

}
