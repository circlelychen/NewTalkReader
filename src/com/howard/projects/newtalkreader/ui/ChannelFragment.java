package com.howard.projects.newtalkreader.ui;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.feeds.FeedExtras;
import com.google.android.imageloader.ImageLoader;
import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.provider.RssContract.Items;
import com.howard.projects.newtalkreader.utils.DLog;

public class ChannelFragment extends SherlockFragment implements
		LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener,
		OnSharedPreferenceChangeListener {

	private static final String LOG_TAG = ChannelFragment.class.getSimpleName();
	
	private Uri DEFAULT_CHANNEL;
	
	private ListView mItemsList;
	private ChannelAdapter mAdapter;
	private View mLoading;
	private View mError;
	
	
	/*
	 * All subclasses of Fragment must include a public empty constructor. The framework will often 
	 * re-instantiate a fragment class when needed, in particular during state restore, and needs 
	 * to be able to find this constructor to instantiate it. If the empty constructor is not available, 
	 * a runtime exception will occur in some cases during state restore.
	 * */
	public ChannelFragment(){
		
	}
	
	public static ChannelFragment newInstance(Uri link){
		//instantiate gragment and bind the URI and ImageLoader for DEFAUL_CHANNEL
		ChannelFragment fragment = new ChannelFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable("_url", link);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Bundle bundle = this.getArguments();
		DEFAULT_CHANNEL = bundle.getParcelable("_url");
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
        return ChannelAdapter.createLoader(context, DEFAULT_CHANNEL);
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
	ImageLoader mImageLoader;
	
	private static final String[] PROJECTION = {
        Items._ID, Items.TITLE_PLAINTEXT, Items.DESCRIPTION, Items.LINK, Items.PUBDATE	
	};
	
	public ChannelAdapter(Context context) {
        super(context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        
        mContext = context;
        mImageLoader = new ImageLoader();
    }
	
	 public static Loader<Cursor> createLoader(Context context, Uri uri) {
		 return new CursorLoader(context, uri, PROJECTION, null, null, null);
	 }

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		DLog.d(LOG_TAG,"bindView ");
		
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		
		//rss_title_plaintext
		String title_plaintext = cursor.getString(cursor.getColumnIndex(Items.TITLE_PLAINTEXT));
        viewHolder.tv_title_plaintext.setText(title_plaintext);
        
        String description = cursor.getString(cursor.getColumnIndex(Items.DESCRIPTION));
        Document content = Jsoup.parse(description);
        //set rss_thumbnai value
        Element imgEle = content.select("img").first();
        if(imgEle != null){
        	DLog.i(LOG_TAG, "thumbnail url: " + imgEle.attr("src"));
        	this.mImageLoader.bind(this, viewHolder.iv_thumbnail, imgEle.attr("src"));
        	imgEle.remove();
        }
        //set rss_description values
        viewHolder.tv_description.setText(content.text());
	}

	// viewholder to aptimize loading performance
	private static final class ViewHolder {
		ImageView iv_thumbnail;
		TextView tv_title_plaintext;
		TextView tv_description;
	}
	
	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup parent) {
		// TODO Auto-generated method stub
		DLog.d(LOG_TAG,"newView ");
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.newtalk_item_row, parent, false);
		
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.iv_thumbnail = (ImageView) view.findViewById(R.id.rss_item_thumbnail);
		viewHolder.tv_title_plaintext = (TextView) view.findViewById(R.id.rss_item_title_plainttext);
		viewHolder.tv_description = (TextView) view.findViewById(R.id.rss_item_description);
		view.setTag(viewHolder);
		
        return view;
	}
	
	public boolean hasError() {
        Cursor cursor = getCursor();
        return cursor != null && cursor.getExtras().containsKey(FeedExtras.EXTRA_ERROR);
    }
	
}
