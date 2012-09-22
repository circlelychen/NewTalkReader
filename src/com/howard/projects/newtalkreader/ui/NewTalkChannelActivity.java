package com.howard.projects.newtalkreader.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.app.ResourceFactory;
import com.howard.projects.newtalkreader.ui.phone.ChannelsPagerFragment;
import com.howard.projects.newtalkreader.ui.phone.ChannelsPagerFragment.OnChannelSelectedListener;
import com.howard.projects.newtalkreader.utils.DLog;

public class NewTalkChannelActivity extends SherlockFragmentActivity implements OnChannelSelectedListener{

	private static final String DLog_TAG = NewTalkChannelActivity.class.getSimpleName();
	private SpinnerAdapter categoryAdaptr;
	
	private boolean mDualPane;
			
	private static final String SAVED_SELECTED_CATEGORY = "_s_s";
	private static final String SAVED_SELECTED_CHANNEL = "_s_c";
	
	private int mSelectedCategory;
	private int mSelectedChannel;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	DLog.i(DLog_TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtalk_singlepane);
        		
        mDualPane = false;
        // Setup the Action Bar
 		ActionBar actionBar = getSupportActionBar();
 		actionBar.setDisplayShowTitleEnabled(false);
 		
 		// detect the version code to load the suitable layout for spinner_dropdown_item
 		int actionbar_sipinner_dropdown_item_layout;
 		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
 		     // only for android older than gingerbread
 			actionbar_sipinner_dropdown_item_layout = R.layout.sherlock_spinner_dropdown_item;
 		}else{
 			actionbar_sipinner_dropdown_item_layout = android.R.layout.simple_spinner_dropdown_item;
 		}
 		
 		categoryAdaptr = new ArrayAdapter<String>(actionBar.getThemedContext(),
 				actionbar_sipinner_dropdown_item_layout,
 				//R.layout.sherlock_spinner_dropdown_item,
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
 			
 			ChannelsPagerFragment fragment = (ChannelsPagerFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_channels_items);
			fragment.setOnChannelSelectedListener(this);
 		} else {
// 			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 		}
 		
 		// restore state parameter
 		restoreSelection(savedInstanceState);
 		
    }

	protected void onResume(){
		DLog.i(DLog_TAG, "onResume()");
    	super.onResume();
    	getSupportActionBar().setSelectedNavigationItem(mSelectedCategory);
    	
    	if (!mDualPane) {
			ChannelsPagerFragment frag = (ChannelsPagerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_channels_items);
			frag.loadSource(mSelectedCategory);
			frag.loadChannel(mSelectedChannel);
		}
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
		outState.putInt(SAVED_SELECTED_CHANNEL, mSelectedChannel);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_new_talk_channel, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		switch(id){
		case R.id.menu_author:
			showAutherDialog();
			break;
		case R.id.menu_version:
			showVersionDialog();
			break;
		case R.id.menu_customized:
			showCustomizedDialog();
			break;
		}
		return super.onOptionsItemSelected(item);
		
	}

	private void showVersionDialog() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(this, HtmlDialogActivity.class);
		intent.putExtra("_resId", R.raw.version);
		intent.putExtra("_titleId", R.string.menu_version);
		this.startActivity(intent);
	}

	private void showAutherDialog() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(this, HtmlDialogActivity.class);
		intent.putExtra("_resId", R.raw.about);
		intent.putExtra("_titleId", R.string.menu_author);
		this.startActivity(intent);
	}
	
	private void showCustomizedDialog(){
		Intent intent = new Intent();
		intent.setClass(this, ChannelSelectionActivity.class);
		intent.putExtra("_source", mSelectedCategory);
		this.startActivity(intent);
	}

	private void restoreSelection(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mSelectedCategory = savedInstanceState.getInt(SAVED_SELECTED_CATEGORY);
			mSelectedChannel = savedInstanceState.getInt(SAVED_SELECTED_CHANNEL);
		}else{
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			mSelectedCategory = prefs.getInt(SAVED_SELECTED_CATEGORY, 
					ResourceFactory.TYPE_IMPORTANT);
			mSelectedChannel = prefs.getInt(SAVED_SELECTED_CHANNEL,
					ResourceFactory.getInstance().getDefaultChannelIndex());
		}
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
			frag.loadSource(mSelectedCategory);
		} else {
//			ChannelsListFragment frag = (ChannelsListFragment) getSupportFragmentManager()
//					.findFragmentById(R.id.fragment_channels);
//			frag.loadChannels(source);
//			onChannelSelected(mSelectedChannel);
		}
	}
	
	public void onChannelSelected(int channel){
		DLog.i(DLog_TAG,"onChannelSelected on: " + channel);
		mSelectedChannel = channel;
		
		// Store the ChannelSelected parameters into SharedPreference
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(SAVED_SELECTED_CHANNEL, mSelectedChannel);
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

