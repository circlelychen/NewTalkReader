package com.howard.projects.newtalkreader.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.database.DatabaseOpenHelper;
import com.howard.projects.newtalkreader.database.SourceTable;

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
	private ResourceFactory(){
	}
	
	private SQLiteOpenHelper mDbHelper;
	private Context mContext;
	
	public void init(Context context){
		mContext = context;
		mDbHelper = new DatabaseOpenHelper(mContext);
	}
	
	public static ResourceFactory getInstance(){
		if(mSingleton == null)
			mSingleton = new ResourceFactory();
		return mSingleton;
	}
	
	public int getDefaultChannelIndex(){
		return  TYPE_IMPORTANT;
	}
	
	public void initDatabase(){
		String [] channelNames  = mContext.getResources().getStringArray(R.array.channel_category);
		String [] channelLinks = mContext.getResources().getStringArray(R.array.channel_link);
		
		
		SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
		int size = channelNames.length;
		for (int i = 0 ; i < size ; i++){
			ContentValues cv = new ContentValues();
			cv.put(SourceTable.KEY_TYPE, TYPE_IMPORTANT);
			cv.put(SourceTable.KEY_NAME, channelNames[i]);
			cv.put(SourceTable.KEY_URL, channelLinks[i]);
			mDb.insert(SourceTable.SOURCE_TABLE_NAME, null, cv);
		}
	}
	
	public List<ChannelInfo> getChannelSources(int source){
		
		SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
		
		String [] colums = {SourceTable.KEY_NAME,SourceTable.KEY_URL};
		String slection = SourceTable.KEY_TYPE + "=?";
		String [] selectionArgs = {""+source};
		Cursor cursor = mDb.query(SourceTable.SOURCE_TABLE_NAME, colums, slection, selectionArgs, null, null, null);
		
		int size =  cursor.getCount();
		List<ChannelInfo> channels = new ArrayList<ChannelInfo>();
		if(size > 0){
			cursor.moveToFirst();
			do{
				ChannelInfo chinfo = curcorToChannelInfo(cursor);
				channels.add(chinfo);
			}while(cursor.moveToNext());
			
		}
		cursor.close();
		mDb.close();
		return channels;
	}
	
	private ChannelInfo curcorToChannelInfo(Cursor cursor){
		
		String name =  cursor.getString(cursor.getColumnIndex(SourceTable.KEY_NAME));
		String url =  cursor.getString(cursor.getColumnIndex(SourceTable.KEY_URL));
		ChannelInfo chinfo = new ChannelInfo(name, url);
		return chinfo;
	}
}
