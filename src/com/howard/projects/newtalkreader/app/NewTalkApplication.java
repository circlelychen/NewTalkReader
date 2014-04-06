package com.howard.projects.newtalkreader.app;

import java.util.List;
import java.util.Random;

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
		List<ChannelInfo> channels = ResourceFactory.getInstance().getSelectedSources(ResourceFactory.TYPE_IMPORTANT);
		if(channels.isEmpty()){
			ResourceFactory.getInstance().initDatabase();
		}
		
		// set http user-agent as null
		String [] userAgents = this.getResources().getStringArray(R.array.user_agent);
		System.setProperty("http.agent", userAgents[new Random().nextInt(userAgents.length)]);
	}

}
