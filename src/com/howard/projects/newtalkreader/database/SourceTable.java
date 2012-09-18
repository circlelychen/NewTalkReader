package com.howard.projects.newtalkreader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.howard.projects.newtalkreader.utils.DLog;

public class SourceTable {

	private static final String TAG = SourceTable.class.getSimpleName();
	
	public static final String KEY_TYPE = "_type";
	public static final String KEY_NAME = "_name";
	public static final String KEY_URL = "_urls";
	
	
	public static final String SOURCE_TABLE_NAME = "SourceTable";
	private static final String SOURCE_TABLE_CREATE =
            "CREATE TABLE " + SOURCE_TABLE_NAME + " (" +
            		KEY_TYPE + " INTEGER, " +
            		KEY_NAME + " TEXT, " +
            		KEY_URL + " TEXT);";

	
	public static void onCreate(Context context, SQLiteDatabase db) {
		// TODO Auto-generated method stub
		DLog.d(TAG, "onCreate() with sql statment: " + SOURCE_TABLE_CREATE);
		db.execSQL(SOURCE_TABLE_CREATE);

	}

	public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// Kills the table and existing data
        db.execSQL("DROP TABLE IF EXISTS "+ SOURCE_TABLE_NAME);

        // Recreates the database with a new version
        onCreate(context, db);

	}
}
