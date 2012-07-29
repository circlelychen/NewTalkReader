package com.howard.projects.newtalkreader.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.ui.phone.ChannelsPagerFragment;
import com.howard.projects.newtalkreader.utils.DLog;

public class NewTalkChannelActivity extends SherlockFragmentActivity {

	private static final String DLog_TAG = NewTalkChannelActivity.class.getSimpleName();
	private SpinnerAdapter categoryAdaptr;
	private boolean mDualPane;
			
	private static final String SAVED_SELECTED_CATEGORY = "_s_s";
	private static final String SAVED_SELECTED_CHANNEL = "_s_c";
	
	private int mSelectedCategory;
	private String mSelectedChannel;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtalk_singlepane);
        		
        mDualPane = false;
        // Setup the Action Bar
 		ActionBar actionBar = getSupportActionBar();
 		actionBar.setDisplayShowTitleEnabled(false);
 		categoryAdaptr = new ArrayAdapter<String>(getApplication(),
 				android.R.layout.simple_spinner_dropdown_item,
 				getResources().getStringArray(R.array.rss_category));
 		if (!mDualPane) {
 			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
 			actionBar.setListNavigationCallbacks(categoryAdaptr,
 					new ActionBar.OnNavigationListener() {
 						@Override
 						public boolean onNavigationItemSelected(
 								int itemPosition, long itemId) {
 							onCategorySelected(itemPosition);
 							return true;
 						}
 					});
 		} else {
 			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 		}
 		
 		// restore state parameter
 		restoreSelection(savedInstanceState);
 		
 		
    }

	protected void onResume(){
    	super.onResume();
    	getSupportActionBar().setSelectedNavigationItem(mSelectedCategory);
    }
    
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		DLog.i(DLog_TAG, "onRestoreInstanceState()");
		super.onRestoreInstanceState(savedInstanceState);
 		 
		//restore state parameter		
		restoreSelection(savedInstanceState);	
	}
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		DLog.i(DLog_TAG, "onSaveInstanceState()");
		super.onSaveInstanceState(outState);
		outState.putInt(SAVED_SELECTED_CATEGORY, mSelectedCategory);
		outState.putString(SAVED_SELECTED_CHANNEL, mSelectedChannel);
	}
    
	private void restoreSelection(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mSelectedCategory = savedInstanceState.getInt(SAVED_SELECTED_CATEGORY);
			mSelectedChannel = savedInstanceState.getString(SAVED_SELECTED_CHANNEL);
		}else{
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			mSelectedCategory = prefs.getInt(SAVED_SELECTED_CATEGORY,0);
			mSelectedChannel = prefs.getString(SAVED_SELECTED_CHANNEL, 
					ResourceFactory.getInstance().getDefaultChannel(this.getApplication()));
		}
		this.onCategorySelected(mSelectedCategory);
		this.onChannelSelected(mSelectedChannel);
	}
	
	private void onCategorySelected(int category) {
		DLog.i(DLog_TAG,"Select Category on item position: " + category);
		mSelectedCategory = category;

		// Store the CategorySelected parameters into SharedPreference
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(SAVED_SELECTED_CATEGORY, mSelectedCategory);
		editor.commit();
		
		
		if (!mDualPane) {
			ChannelsPagerFragment frag = (ChannelsPagerFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_channels_items);
//			frag.loadChannels(mSelectedCategory);
//			onChannelSelected(mSelectedChannel);
		} else {
//			ChannelsListFragment frag = (ChannelsListFragment) getSupportFragmentManager()
//					.findFragmentById(R.id.fragment_channels);
//			frag.loadChannels(source);
//			onChannelSelected(mSelectedChannel);
		}
	}
	
	private void onChannelSelected(String channel){
		DLog.i(DLog_TAG,"Select Channel on: " + channel);
		mSelectedChannel = channel;
		
		// Store the ChannelSelected parameters into SharedPreference
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(SAVED_SELECTED_CHANNEL, mSelectedChannel);
		editor.commit();
		
		if (!mDualPane) {
			ChannelsPagerFragment fragment = (ChannelsPagerFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_channels_items);
			fragment.loadChannel(mSelectedChannel);
		} else {
//			ChannelsListFragment frag = (ChannelsListFragment) getSupportFragmentManager()
//					.findFragmentById(R.id.fragment_channels);
//			onChannelSelected(mSelectedChannel);
		}
		
	}
}

