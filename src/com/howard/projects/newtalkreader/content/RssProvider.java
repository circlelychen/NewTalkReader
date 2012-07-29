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
            return FeedProvider.feedCursor(output, extras);
        } catch (Throwable t) {
            return FeedProvider.errorCursor(output, extras, t, null);
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
