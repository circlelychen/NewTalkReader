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
	
	private Context mContext;
	private List<ChannelInfo> CONTENT;

	public ChannelsPagerAdapter(Context context ,FragmentManager fm) {
		super(fm);
		mContext = context;
		setSource(ResourceFactory.TYPE_IMPORTANT);
		
	}

	@Override
	public int getCount() {
		return CONTENT.size();
	}

	@Override
	public Fragment getItem(int position) {
		DLog.i(TAG,"getItem("+position+")");
		Uri channel_uri = Items.contentUri(CONTENT.get(position).getLink());
		return ChannelFragment.newInstance( channel_uri );
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
        return CONTENT.get(position % CONTENT.size()).getName();
    }
	
	/*
	 * Override this method : return POSITION_NONE. view pager will remove all views and reload them all.
	 * reference: http://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view
	 * */
	@Override
	public int getItemPosition(Object object) {
	    return POSITION_NONE;
	}
	
	public void setSource(int source){
		CONTENT = ResourceFactory.getInstance().getSelectedSources(source);
		this.notifyDataSetChanged();
	}
	
}