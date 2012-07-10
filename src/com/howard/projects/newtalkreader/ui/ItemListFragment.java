package com.howard.projects.newtalkreader.ui;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockListFragment;
import com.howard.projects.newtalkreader.R;


public class ItemListFragment extends SherlockListFragment implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
		OnSharedPreferenceChangeListener {

	private static final String LOG_TAG = ItemListFragment.class
			.getSimpleName();
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
		View root = inflater.inflate(R.layout.newtalk_list,container, false);
		View loadingView = (View)root.findViewById(R.id.loading);
		loadingView.setVisibility(View.VISIBLE);
		
		return root;
		
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
}
