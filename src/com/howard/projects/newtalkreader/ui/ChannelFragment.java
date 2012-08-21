package com.howard.projects.newtalkreader.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.feeds.FeedExtras;
import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.provider.RssContract.Items;
import com.howard.projects.newtalkreader.utils.DLog;


public class ChannelFragment extends SherlockFragment implements
		LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener,
		OnSharedPreferenceChangeListener {

	private static final String LOG_TAG = ChannelFragment.class.getSimpleName();
	
	private String DEFAULT_CHANNEL;
	private ListView mItemsList;
	private View mLoading;
	private View mError;
	private ChannelAdapter mAdapter;
	
	public ChannelFragment(String link){
		DEFAULT_CHANNEL = link;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		DLog.i(LOG_TAG, "onCreateView() with channel :"+ DEFAULT_CHANNEL);
		View root = inflater.inflate(R.layout.newtalk_items_list,container, false);
		mLoading = (View)root.findViewById(R.id.loading);
		mError = (View)root.findViewById(R.id.error);
		
		mItemsList = (ListView) root.findViewById(android.R.id.list);
		mAdapter = new ChannelAdapter(this.getActivity());
		mItemsList.setAdapter(mAdapter);
		mItemsList.setOnItemClickListener(this);
		this.getActivity().getSupportLoaderManager().initLoader(DEFAULT_CHANNEL.hashCode(), 
				Bundle.EMPTY, 
				this);
		
		return root;
		
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		Log.d(LOG_TAG,"create Loader on " + DEFAULT_CHANNEL);
		mLoading.setVisibility(mAdapter.isEmpty() ? View.VISIBLE : View.GONE);
   
		Context context = this.getActivity();
        Uri uri = Items.contentUri(DEFAULT_CHANNEL);
        return ChannelAdapter.createLoader(context, uri);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		Log.d(LOG_TAG,"Loader finished on " + DEFAULT_CHANNEL);
		mAdapter.swapCursor(data);
		mLoading.setVisibility(View.GONE);
		mError.setVisibility(mAdapter.isEmpty() && mAdapter.hasError() ? View.VISIBLE : View.GONE);
		
		mItemsList.setVisibility(View.VISIBLE);
		
        
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		mAdapter.swapCursor(null);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Toast.makeText(this.getActivity(), "click item", Toast.LENGTH_SHORT).show();
		
	}
}

class ChannelAdapter extends CursorAdapter{

	private static final String LOG_TAG = ChannelAdapter.class.getSimpleName();
	private Context mContext;
	
	private static final String[] PROJECTION = {
        Items._ID, Items.TITLE_PLAINTEXT, Items.DESCRIPTION, Items.LINK	
	};
	
	public ChannelAdapter(Context context) {
        super(context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        
        mContext = context;
    }
	
	 public static Loader<Cursor> createLoader(Context context, Uri uri) {
		 return new CursorLoader(context, uri, PROJECTION, null, null, null);
	 }

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		Log.d(LOG_TAG,"bindView ");
		String title = cursor.getString(cursor.getColumnIndex(Items.TITLE_PLAINTEXT));
        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        text1.setText(title);
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d(LOG_TAG,"newView ");
		LayoutInflater inflater = LayoutInflater.from(mContext);
        return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
	}
	
	public boolean hasError() {
        Cursor cursor = getCursor();
        return cursor != null && cursor.getExtras().containsKey(FeedExtras.EXTRA_ERROR);
    }
	
}
