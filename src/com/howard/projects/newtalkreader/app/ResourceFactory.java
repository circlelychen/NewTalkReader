package com.howard.projects.newtalkreader.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.howard.projects.newtalkreader.R;

public class ResourceFactory {

	public static class ChannelInfo {
		
		private String _name;
		private String _link;
		
		public ChannelInfo(String name, String link){
			_name = name;
			_link = link;
		}
		
		public String getName(){return _name;}
		public String getLink(){return _link;};
	}
	
	public static final int TYPE_IMPORTANT = 0;
	public static final int TYPE_TOPIC = 1;
	
	private static ResourceFactory mSingleton = null;
	private ResourceFactory(){}
	
	private List<ChannelInfo> mImportantChannelInfo = null;
	private List<ChannelInfo> mTopicChannelInfo = null;
	public static ResourceFactory getInstance(){
		if(mSingleton == null)
			mSingleton = new ResourceFactory();
		return mSingleton;
	}
	
	public int getDefaultChannelIndex(Context context){
		return  0;
	}
	
	public List<ChannelInfo> getChannelSources(Context context, int type){
		if(mImportantChannelInfo == null)
			mImportantChannelInfo = loadImportantChannelInfo(context);
		return mImportantChannelInfo;
	}
	
	private List<ChannelInfo> loadImportantChannelInfo(Context context){
		String [] channelNames = context.getResources().getStringArray(R.array.channel_category);
		String [] channelLinks = context.getResources().getStringArray(R.array.channel_link);
		if(channelNames.length != channelLinks.length)
			return null;
		
		int size = channelNames.length;
		List<ChannelInfo> channelResources = new ArrayList<ChannelInfo>();
		for (int i = 0 ; i < size ; i++){
			channelResources.add(new ChannelInfo(channelNames[i], channelLinks[i]));
		}
		return channelResources;
	}
}
