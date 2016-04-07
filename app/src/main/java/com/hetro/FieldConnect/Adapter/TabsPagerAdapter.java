package com.hetro.FieldConnect.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hetro.FieldConnect.Activity.Channel1Fragment;
import com.hetro.FieldConnect.Activity.Channel2Fragment;
import com.hetro.FieldConnect.Activity.Channel3Fragment;
import com.hetro.FieldConnect.Activity.Channel4Fragment;
import com.hetro.FieldConnect.Activity.Channel5Fragment;
import com.hetro.FieldConnect.Activity.Channel6Fragment;
import com.hetro.FieldConnect.Activity.Channel7Fragment;


public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new Channel1Fragment();
		case 1:
			// Games fragment activity
			return new Channel2Fragment();
		case 2:
			// Movies fragment activity
			return new Channel3Fragment();

		case 3:
				// Games fragment activity
		return new Channel4Fragment();

		case 4:
				// Movies fragment activity
		return new Channel5Fragment();

		case 5:
				// Games fragment activity
		return new Channel6Fragment();
		case 6:
				// Movies fragment activity
		return new Channel7Fragment();


		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 7;
	}

}
