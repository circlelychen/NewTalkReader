package com.howard.projects.newtalkreader.ui.phone;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.ui.ChannelFragment;

public class ChannelsPagerAdapter extends FragmentPagerAdapter {
	
	private Context mContext;
	private String[] CONTENT ;

	public ChannelsPagerAdapter(Context context ,FragmentManager fm) {
		super(fm);
		mContext = context;
		CONTENT = mContext.getResources().getStringArray(R.array.channel_category);
	}

	@Override
	public int getCount() {
		return CONTENT.length;
	}

	@Override
	public Fragment getItem(int position) {
		return new ChannelFragment(mContext, position);
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position % CONTENT.length];
    }
}