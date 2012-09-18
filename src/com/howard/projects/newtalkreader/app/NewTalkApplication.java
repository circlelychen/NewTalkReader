package com.howard.projects.newtalkreader.app;

import java.util.List;

import android.app.Application;

import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.app.ResourceFactory.ChannelInfo;
import com.howard.projects.newtalkreader.utils.DLog;

public class NewTalkApplication extends Application {
	
	private static final String TAG = NewTalkApplication.class.getSimpleName();
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		DLog.i(TAG, "onCreate()");
		ResourceFactory.getInstance().init(this);
		List<ChannelInfo> channels = ResourceFactory.getInstance().getChannelSources(ResourceFactory.TYPE_IMPORTANT);
		if(channels.isEmpty()){
			ResourceFactory.getInstance().initDatabase();
		}
	}

}
