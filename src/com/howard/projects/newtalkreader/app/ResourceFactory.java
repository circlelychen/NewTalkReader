package com.howard.projects.newtalkreader.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.database.DatabaseOpenHelper;
import com.howard.projects.newtalkreader.database.SourceTable;
import com.howard.projects.newtalkreader.utils.DLog;

public class ResourceFactory {

	private static String TAG = ResourceFactory.class.getSimpleName();
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
		List<ChannelInfo> channels = getOriginSources(TYPE_IMPORTANT);
		insertDatabase(TYPE_IMPORTANT, channels);
	}
	
	public void cleanDatabase(int source){
		
		SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
		String slection = SourceTable.KEY_TYPE + "=?";
		String [] selectionArgs = {""+source};
		mDb.delete(SourceTable.SOURCE_TABLE_NAME, slection, selectionArgs);
	}
	
	public void insertDatabase(int source,List<ChannelInfo> list){
		
		SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
		int size = list.size();
		for (int i = 0 ; i < size ; i++){
			ContentValues cv = new ContentValues();
			cv.put(SourceTable.KEY_TYPE, source);
			cv.put(SourceTable.KEY_NAME, list.get(i).getName());
			cv.put(SourceTable.KEY_URL, list.get(i).getLink());
			mDb.insert(SourceTable.SOURCE_TABLE_NAME, null, cv);
		}
	}
	
	public List<ChannelInfo> getSelectedSources(int source){
		
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
	
	public List<ChannelInfo> getOriginSources(int source){
		
		List<ChannelInfo> channels = new ArrayList<ChannelInfo>();
		if(source == TYPE_IMPORTANT){
			/*
			 * preload TYPE_IMPORTANT channels links in static resource 
			 * */
			String [] channelNames  = mContext.getResources().getStringArray(R.array.channel_category);
			String [] channelLinks = mContext.getResources().getStringArray(R.array.channel_link);
			int size = channelNames.length;
			for (int i = 0 ; i < size ; i++){
				channels.add(new ChannelInfo(channelNames[i],channelLinks[i]));
			}
		}else if(source == TYPE_TOPIC){
			/*
			 * Real-time fetching TYPE_TOPIC channels links in from remote website 
			 * */
			Document doc;
			try {
				doc = Jsoup.connect(mContext.getString(R.string.newtalk_rss_url)).get();
			}catch (IOException e) {
				DLog.i(TAG, "IOException Exception (Message):" + e.getMessage());
				DLog.i(TAG, "IOException Exception (Cause):" + e.getCause());
				// TODO Auto-generated catch block
				e.printStackTrace();
				return channels;
			}
			
			//parse and fetch RSS fees urls
			try{
				Elements rss_elements = doc.select("div.cont_citizen_lt ul li a");
				DLog.d(TAG, "== select 'div.cont_citizen_lt ul li a' ==");
				for(int i = 0 ; i < rss_elements.size(); i++){
					String name = rss_elements.get(i).text().trim();
					String link = rss_elements.get(i).attr("href");
					if(link.contains("rss_discussions")){
						//topic type
						DLog.d(TAG, " topic type =>  href:" + rss_elements.get(i).attr("href") + ", value: " + rss_elements.get(i).html());
						channels.add(new ChannelInfo(name, mContext.getString(R.string.newtalk_entry_url) + link));
					}else if(link.contains("rss_news")){
						//importance type
						DLog.d(TAG, " importance type => href:" + rss_elements.get(i).attr("href") + ", value: " + rss_elements.get(i).html());
					}
				}
			}catch(NullPointerException e){
				DLog.i(TAG, "NullPointer Exception : parse and fetch RSS feed urls");
				e.printStackTrace();
			}
		}
		return channels;
	}
	
	
	
	private ChannelInfo curcorToChannelInfo(Cursor cursor){
		
		String name =  cursor.getString(cursor.getColumnIndex(SourceTable.KEY_NAME));
		String url =  cursor.getString(cursor.getColumnIndex(SourceTable.KEY_URL));
		ChannelInfo chinfo = new ChannelInfo(name, url);
		return chinfo;
	}
}
