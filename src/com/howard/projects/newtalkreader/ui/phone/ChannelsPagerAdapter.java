package com.howard.projects.newtalkreader.ui.phone;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.howard.projects.newtalkreader.app.ResourceFactory;
import com.howard.projects.newtalkreader.app.ResourceFactory.ChannelInfo;
import com.howard.projects.newtalkreader.provider.RssContract.Items;
import com.howard.projects.newtalkreader.ui.ChannelFragment;
import com.howard.projects.newtalkreader.utils.DLog;

public class ChannelsPagerAdapter extends FragmentStatePagerAdapter {
	
	private static String TAG = ChannelsPagerAdapter.class.getSimpleName();
	
	private List<ChannelInfo> CONTENT;

	public ChannelsPagerAdapter(Context context ,FragmentManager fm) {
		super(fm);
		CONTENT = ResourceFactory.getInstance().getChannelSources(context, ResourceFactory.TYPE_IMPORTANT);
	}

	@Override
	public int getCount() {
		return CONTENT.size();
	}

	@Override
	public Fragment getItem(int position) {
		return new ChannelFragment(CONTENT.get(position).getLink());
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
        return CONTENT.get(position % CONTENT.size()).getName();
    }
}