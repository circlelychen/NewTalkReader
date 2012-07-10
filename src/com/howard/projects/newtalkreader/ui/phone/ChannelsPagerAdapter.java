package com.howard.projects.newtalkreader.ui.phone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.howard.projects.newtalkreader.ui.ItemListFragment;

public class ChannelsPagerAdapter extends FragmentPagerAdapter {
	
	private static final String[] CONTENT = new String[] { "美牛議題", "林益世風暴", "公廣集團", "核電危機", "文林苑爭議"};

	public ChannelsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return CONTENT.length;
	}

	@Override
	public Fragment getItem(int position) {
		return new ItemListFragment();
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position % CONTENT.length];
    }
}