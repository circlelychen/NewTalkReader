package com.howard.projects.newtalkreader.utils;

import android.util.Log;

public class DLog {
	private static final String TAG = "NewTalkReader";
	
	public static int v(String tag, String msg) {
		return Log.v(TAG,format(tag,msg));
	}
	
	public static int d(String tag, String msg) {
		return Log.d(TAG,format(tag,msg));
	}
	
	public static int e(String tag, String msg) {
		return Log.e(TAG,format(tag,msg));
	}
	
	public static int i(String tag, String msg) {
		return Log.i(TAG,format(tag,msg));
	}
	
	public static int w(String tag, String msg) {
		return Log.w(TAG,format(tag,msg));
	}
	
	public static int e(String tag, String body, Exception e){
		return android.util.Log.e(tag,body,e);
	}

	private static String format(String tag, String msg){
		StringBuffer strbuf = new StringBuffer();
		strbuf.append(tag).append(" --  ").append(msg);
		return strbuf.toString();
	}

}
