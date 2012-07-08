package com.howard.projects.newtalkreader.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.howard.projects.newtalkreader.R;

public class NewTalkChannelActivity extends BaseActivity {

	private SpinnerAdapter categoryAdaptr;
	private boolean mDualPane;
			
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_talk_channel);
        		
        mDualPane = false;
        // Setup the Action Bar
 		ActionBar actionBar = getSupportActionBar();
 		actionBar.setDisplayShowTitleEnabled(false);
 		categoryAdaptr = new ArrayAdapter<String>(getApplication(),android.R.layout.simple_spinner_dropdown_item,this.getResources().getStringArray(R.array.rss_channel_category));
 		if (!mDualPane) {
 			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
 			actionBar.setListNavigationCallbacks(categoryAdaptr,
 					new ActionBar.OnNavigationListener() {
 						@Override
 						public boolean onNavigationItemSelected(
 								int itemPosition, long itemId) {
// 							onSourceSelected((int) itemId);
 							return true;
 						}
 					});
 		} else {
 			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 		}
    }
}