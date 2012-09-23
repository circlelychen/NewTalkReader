/*-
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.howard.projects.newtalkreader.content;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.feeds.FeedLoader;
import com.google.android.feeds.FeedProvider;
import com.howard.projects.newtalkreader.provider.RssContract.Items;

/*
 * This is file is modified to fit project purpose
 * */
public class RssProvider extends ContentProvider {

	private static final String TAG = RssProvider.class.getSimpleName();
	
    private static final int CHANNEL_ITEMS = 1;
    private static final int CHANNEL_ITEM = 2;

    private UriMatcher mUriMatcher;

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(info.authority, "channels/*/items", CHANNEL_ITEMS);
        mUriMatcher.addURI(info.authority, "channels/*/items/*", CHANNEL_ITEM);
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case CHANNEL_ITEMS:
                return Items.CONTENT_TYPE;
            case CHANNEL_ITEM:
                return Items.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        int type = mUriMatcher.match(uri);
        
        MatrixCursor output = new MatrixCursor(projection);
        Bundle extras = new Bundle();
        try {
            RssContentHandler handler = new RssContentHandler(output, extras);
            switch (type) {
                case CHANNEL_ITEM:
                    String guid = Items.getItemGuid(uri);
                    handler.setFilter(guid);
                    break;
            }

            String feedUrl = Items.getChannelUrl(uri);
            Uri feedUri = Uri.parse(feedUrl);
            FeedLoader.loadFeed(handler, feedUri);
            
            /*
             * This segments sort order by pubDate
             * */
            if("pubDate DESC".equals(sortOrder) && output.getCount() > 0){
            	//sorted by pubDate in desc order
            	
            	List<SortElement> list = new ArrayList<SortElement>();
            	output.moveToFirst();
            	do{
            		SortElement el = new SortElement();
            		el._ID = output.getString(output.getColumnIndex(Items._ID));
            		el.TITLE_PLAINTEXT = output.getString(output.getColumnIndex(Items.TITLE_PLAINTEXT));
            		el.DESCRIPTION = output.getString(output.getColumnIndex(Items.DESCRIPTION));
            		el.PUBDATE = output.getString(output.getColumnIndex(Items.PUBDATE));
            		el.LINK = output.getString(output.getColumnIndex(Items.LINK));
            		list.add(el);
            	}while(output.moveToNext());
            	
            	Collections.sort(list, Collections.reverseOrder());
            	
            	MatrixCursor sorted = new MatrixCursor(projection);
            	for(int i = 0 ; i < list.size() ; i++){
            		// to be determined
            		SortElement el = list.get(i);
            		sorted.addRow(new Object[] {el._ID, el.TITLE_PLAINTEXT, el.DESCRIPTION, el.LINK, el.PUBDATE});
            	}
            	output = sorted;
            }
            
            return FeedProvider.feedCursor(output, extras);
            
        } catch (Throwable t) {
            return FeedProvider.errorCursor(output, extras, t, null);
        }
    }
    
    static class SortElement implements Comparable<SortElement>{

    	public String _ID;
    	public String TITLE_PLAINTEXT;
    	public String DESCRIPTION;
    	public String LINK; 
    	public String PUBDATE;
		@Override
		public int compareTo(SortElement another) {
			// TODO Auto-generated method stub
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	try {
				Date thisDate = format.parse(this.PUBDATE);
				Date argumentDate = format.parse(another.PUBDATE);
				
				return thisDate.compareTo(argumentDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		}
    	
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
