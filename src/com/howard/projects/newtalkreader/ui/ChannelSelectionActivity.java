package com.howard.projects.newtalkreader.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.app.ResourceFactory;
import com.howard.projects.newtalkreader.app.ResourceFactory.ChannelInfo;
import com.howard.projects.newtalkreader.utils.DLog;

public class ChannelSelectionActivity extends SherlockFragmentActivity implements 
		LoaderManager.LoaderCallbacks<SourceCollection>, View.OnClickListener{

	private static String TAG = ChannelSelectionActivity.class.getSimpleName();
	/**
	 * Interface description goes here.
	 * 
	 * TagSelectionListener is a interface for others to implement. 
	 * The callback "TagSelectionUpdate" will be called when tags be updated. 
	 * @author chenhao-yuan
	 */
	public static interface ChannelSelectionListener{
		void onChannelSelectionChanged(List<String> selectedChannel);
	}
	
	private View mLoading;
	private View mError;
	private ListView tagListView;
	private List<String> displayNames;
	private Map<Integer, ChannelInfo> mapPosChannel;
	private ArrayAdapter<String> adapter;
	private int source;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	DLog.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        
        source = this.getIntent().getExtras().getInt("_source");
        setContentView(R.layout.newtalk_items_list);
        
        /*Configure List view within Dialog with "layout"*/
		tagListView = (ListView) findViewById(android.R.id.list);
		tagListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		mLoading = (View) this.findViewById(R.id.loading);
		mError = (View) this.findViewById(R.id.error);
		mError.findViewById(R.id.retry).setOnClickListener(this);
		
		this.getSupportLoaderManager().initLoader(source, 
				Bundle.EMPTY, 
				this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		List<ChannelInfo> result = getChannelSelectionResult();
		
		ResourceFactory.getInstance().cleanDatabase(source);
		ResourceFactory.getInstance().insertDatabase(source, result);
	}

	/**
	 * 
	 * @return List<String> a list of selected tags, "None" tag must be added by default.
	 */

	private List<ChannelInfo> getChannelSelectionResult(){
		
		SparseBooleanArray checked = tagListView.getCheckedItemPositions();
		List<ChannelInfo> checkedTags = new ArrayList<ChannelInfo>();
		
		for(int i=0; i < checked.size(); i++){
			if(checked.valueAt(i) == true){
				checkedTags.add(mapPosChannel.get(checked.keyAt(i)));
			}
		}
		return checkedTags;
	}


	@Override
	public void onLoadFinished(Loader<SourceCollection> arg0, SourceCollection sc) {
		// TODO Auto-generated method stub
		DLog.d(TAG,"Loader finished on " + source);
		
		mLoading.setVisibility(View.GONE);
		mError.setVisibility( sc.isEmpty()? View.VISIBLE : View.GONE);
		
		List<String> selectedNames = new ArrayList<String>();
		for(int i=0 ; i < sc.getSelectedChannels().size(); i++){
			selectedNames.add(sc.getSelectedChannels().get(i).getName());
		}
		
		mapPosChannel = new LinkedHashMap<Integer,ChannelInfo>();
		displayNames = new ArrayList<String>();
		for(int i=0 ; i < sc.getOriginChannels().size(); i++){
			displayNames.add(sc.getOriginChannels().get(i).getName());
			mapPosChannel.put(i, sc.getOriginChannels().get(i));
		}
		
		/*Configure List view within Dialog with "adapter" and bind data*/
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			adapter = new ArrayAdapter<String>(this, R.layout.abs__list_menu_item_checkbox, displayNames);
		}else{
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, displayNames);
		}
		tagListView.setAdapter(adapter);
		for(int i = 0 ; i < mapPosChannel.size(); i++){
			ChannelInfo chinfo = mapPosChannel.get(i);
			if(selectedNames.contains(chinfo.getName())){
				tagListView.setItemChecked(i, true);
			}
		}
		
	}

	@Override
	public void onLoaderReset(Loader<SourceCollection> arg0) {
		// TODO Auto-generated method stub
		DLog.d(TAG,"Loader reseted on " + arg0.toString());  
		tagListView.setAdapter(null);	
	}

	@Override
	public Loader<SourceCollection>  onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		DLog.d(TAG,"create Loader on " + source);
		
		mLoading.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
        
		return new ChannelInfoLoader(this, source);
	}

	private void reload() {
        getSupportLoaderManager().restartLoader(source, Bundle.EMPTY, this);
    }
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
        case R.id.retry:
            reload();
            break;
		}
		
	}
}

class SourceCollection {
	private List<ChannelInfo> selectedChannels;
	private List<ChannelInfo> originChannels;
	
	public List<ChannelInfo> getSelectedChannels(){ return selectedChannels;}
	public List<ChannelInfo> getOriginChannels(){ return originChannels;}
	
	public void setSelectedChannels(List<ChannelInfo> input){  selectedChannels = input;}
	public void setOriginChannels(List<ChannelInfo> input){  originChannels = input;}
	
	public boolean isEmpty(){
		return originChannels.isEmpty();
	}
	
}

class ChannelInfoLoader extends AsyncTaskLoader<SourceCollection>{

	private int mSource;
	public ChannelInfoLoader(Context context, int source) {
		super(context);
		mSource = source;
		// TODO Auto-generated constructor stub
	}
	
	public ChannelInfoLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * Must override this callback function to make loader forceLoad instead of directly call forceLoad in Activity/Fragment*/
	@Override
	protected void onStartLoading() {
		// this is tentitive solution -- need to be refined
		forceLoad();
	}
	
	@Override
	public SourceCollection loadInBackground() {
		// TODO Auto-generated method stub
		
		SourceCollection sc = new SourceCollection();
		sc.setSelectedChannels(
				ResourceFactory.getInstance().getSelectedSources(mSource));
		sc.setOriginChannels(
				ResourceFactory.getInstance().getOriginSources(mSource));
		return sc;
	}
	
}
