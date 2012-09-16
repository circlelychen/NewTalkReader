package com.howard.projects.newtalkreader.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.app.ResourceFactory;
import com.howard.projects.newtalkreader.app.ResourceFactory.ChannelInfo;

public class ChannelSelectionDialogFragment extends SherlockDialogFragment {

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
	private List<String> channels;
	private ArrayAdapter<String> adapter;
	
	private ChannelSelectionDialogFragment(){
	}
	
	public static ChannelSelectionDialogFragment newInstance(Context _context, int source){
		ChannelSelectionDialogFragment fg = new ChannelSelectionDialogFragment();
		
		List<ChannelInfo> channels = ResourceFactory.getInstance().getChannelSources(_context, source);
		
		List<String> channelNames = new ArrayList<String>();
		for(int i=0 ; i < channels.size(); i++){
			channelNames.add(channels.get(i).getName());
		}
		fg.channels = channelNames;
		
		return fg;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View root = (View) inflater.inflate(R.layout.newtalk_channel_selection_dialog, container);
		
		/*Configure List view within Dialog with "layout"*/
		tagListView = (ListView) root.findViewById(R.id.channel_list);
		tagListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		return root;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.getDialog().setTitle("Select Tags");
		/*Configure List view within Dialog with "adapter" and bind data*/
		adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_multiple_choice,channels);
		tagListView.setAdapter(adapter);
		for(int i = 0 ; i < tagListView.getAdapter().getCount() ; i++){
    		tagListView.setItemChecked(i, true);
    	}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		List<String> result = getChannelSelectionResult();
/*		
		ChannelSelectionListener listener = (ChannelSelectionListener)this.getActivity();
		listener.onChannelSelectionChanged(result);
*/
	}
	
	/**
	 * 
	 * @return List<String> a list of selected tags, "None" tag must be added by default.
	 */
	private List<String> getChannelSelectionResult(){
		String message = "";
		SparseBooleanArray checked = tagListView.getCheckedItemPositions();
		List<String> checkedTags = new ArrayList<String>();
		
		for(int i=0; i < checked.size(); i++){
			if(checked.valueAt(i) == true){
				message += channels.get(tagListView.getCheckedItemPositions().keyAt(i)) + " , ";
				checkedTags.add(channels.get(tagListView.getCheckedItemPositions().keyAt(i)));
			}
		}
		return checkedTags;
	}
	
	
}
