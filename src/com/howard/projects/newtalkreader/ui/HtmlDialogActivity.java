package com.howard.projects.newtalkreader.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.os.Bundle;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.utils.DLog;

public class HtmlDialogActivity extends SherlockFragmentActivity{

	private static final String TAG = HtmlDialogActivity.class.getSimpleName();
	private WebView mWebView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	DLog.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.newtalk_html_dialog);
        setTitle(getIntent().getExtras().getInt("_titleId"));
        mWebView = (WebView) findViewById(R.id.wv);
        
        try {
            InputStream input = getResources().openRawResource(
            		this.getIntent().getExtras().getInt("_resId"));
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            String content = new String(buffer);
            mWebView.loadDataWithBaseURL("file:///android_res/raw/", content, "text/html", "utf-8", null);
            input.close();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
	}
}
