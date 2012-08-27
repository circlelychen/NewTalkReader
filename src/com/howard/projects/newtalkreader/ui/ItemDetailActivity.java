package com.howard.projects.newtalkreader.ui;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.imageloader.ImageLoader;
import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.utils.DLog;

public class ItemDetailActivity extends SherlockFragmentActivity implements
		LoaderManager.LoaderCallbacks<NewsEntity>{

	private static String TAG = ItemDetailActivity.class.getSimpleName();
	private Uri mItemUri; 
	
	private TextView mTitle;
	private TextView mDescription;
	private ImageView mImage;
	private View mLoading;
	private View mError;
	private ImageLoader mImageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		mItemUri = bundle.getParcelable("_link");
		
		setContentView(R.layout.newtalk_item_detail);
		mTitle = (TextView) this.findViewById(R.id.title);
		mDescription = (TextView) this.findViewById(R.id.description);
		mImage = (ImageView) this.findViewById(R.id.image);
		mLoading = (View) this.findViewById(R.id.loading);
		mError = (View) this.findViewById(R.id.error);
		
		mImageLoader = new ImageLoader();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		this.getSupportLoaderManager().initLoader(mItemUri.hashCode(), 
				Bundle.EMPTY, 
				this).forceLoad();
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		DLog.d(TAG,"create Loader on " + mItemUri);
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		DLog.d(TAG,"Loader finished on " + mItemUri);  
		
		mLoading.setVisibility(View.GONE);
		//mError.setVisibility(mAdapter.isEmpty() && mAdapter.hasError() ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		//mAdapter.swapCursor(null);
	}
}
