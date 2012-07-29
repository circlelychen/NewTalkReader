package com.howard.projects.newtalkreader.ui.phone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.howard.projects.newtalkreader.ui.ChannelFragment;

public class ChannelsPagerAdapter extends FragmentPagerAdapter {
	
	private static final String[] CONTENT = new String[] { "要聞", "國際", "政治", "財經", "司法", "生活"
		, "媒體", "中國", "科技", "環保", "娛樂", "藝文", "地方"};

	public ChannelsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return CONTENT.length;
	}

	@Override
	public Fragment getItem(int position) {
		return new ChannelFragment(position);
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position % CONTENT.length];
    }
}