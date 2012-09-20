package com.howard.projects.newtalkreader.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.app.ResourceFactory;
import com.howard.projects.newtalkreader.app.ResourceFactory.ChannelInfo;
import com.howard.projects.newtalkreader.utils.DLog;

public class ChannelSelectionActivity extends SherlockFragmentActivity {

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
        setContentView(R.layout.newtalk_channel_selection_dialog);
        
        /*Configure List view within Dialog with "layout"*/
		tagListView = (ListView) findViewById(R.id.channel_list);
		tagListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		List<String> selectedNames = new ArrayList<String>();
		List<ChannelInfo> selectedChannels = ResourceFactory.getInstance().getSelectedSources(source);
		for(int i=0 ; i < selectedChannels.size(); i++){
			selectedNames.add(selectedChannels.get(i).getName());
		}
		
		mapPosChannel = new LinkedHashMap<Integer,ChannelInfo>();
		displayNames = new ArrayList<String>();
		List<ChannelInfo> originChannels = ResourceFactory.getInstance().getOriginSources(source);
		for(int i=0 ; i < originChannels.size(); i++){
			displayNames.add(originChannels.get(i).getName());
			mapPosChannel.put(i, originChannels.get(i));
		}
		
		/*Configure List view within Dialog with "adapter" and bind data*/
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, displayNames);
		tagListView.setAdapter(adapter);
		for(int i = 0 ; i < mapPosChannel.size(); i++){
			ChannelInfo chinfo = mapPosChannel.get(i);
			if(selectedNames.contains(chinfo.getName())){
				tagListView.setItemChecked(i, true);
			}
		}
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
	
	
}
