package com.howard.projects.newtalkreader.ui;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.imageloader.ImageLoader;
import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.utils.DLog;

public class ItemDetailActivity extends SherlockFragmentActivity implements
		LoaderManager.LoaderCallbacks<NewsEntity>, View.OnClickListener{

	private static String TAG = ItemDetailActivity.class.getSimpleName();
	private Uri mItemUri; 
	
	private TextView mTitle;
	private TextView mDescription;
	private ImageView mImage;
	private View mLoading;
	private View mError;
	private ImageLoader mImageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DLog.d(TAG, "onCreate()");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		mItemUri = bundle.getParcelable("_link");
		
		setContentView(R.layout.newtalk_item_detail);
		mTitle = (TextView) this.findViewById(R.id.title);
		mDescription = (TextView) this.findViewById(R.id.description);
		mImage = (ImageView) this.findViewById(R.id.image);
		mLoading = (View) this.findViewById(R.id.loading);
		mError = (View) this.findViewById(R.id.error);
		mError.findViewById(R.id.retry).setOnClickListener(this);
		
		mImageLoader = new ImageLoader();
		this.getSupportLoaderManager().initLoader(mItemUri.hashCode(), 
				Bundle.EMPTY, 
				this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
	private void reload() {
        getSupportLoaderManager().restartLoader(mItemUri.hashCode(), Bundle.EMPTY, this);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
        case R.id.retry:
            reload();
            break;
		}
		
	}
	
	@Override
	public Loader<NewsEntity> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		DLog.d(TAG,"create Loader on " + mItemUri);
		
		mLoading.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
        
		return new NewsLoader(this, mItemUri);
	}

	@Override
	public void onLoadFinished(Loader<NewsEntity> loader, NewsEntity data) {
		// TODO Auto-generated method stub
		DLog.d(TAG,"Loader finished on " + mItemUri);  
		
		Bundle bundle = this.getIntent().getExtras();
		mTitle.setText(bundle.getString("_title"));
		
		
		mDescription.setText(Html.fromHtml(data.content));
		if(data.imgUrl == Uri.EMPTY){
			mImage.setImageBitmap(null);
			mImage.setVisibility(View.GONE);
		}else{
			mImageLoader.bind(mImage, data.imgUrl.toString(), null);
			mImage.setVisibility(View.VISIBLE);
		}
		
		mLoading.setVisibility(View.GONE);
		mTitle.setVisibility( data.isEmpty()? View.GONE : View.VISIBLE);
		mDescription.setVisibility( data.isEmpty()? View.GONE : View.VISIBLE);
		mError.setVisibility( data.isEmpty()? View.VISIBLE : View.GONE);
	}

	@Override
	public void onLoaderReset(Loader<NewsEntity> arg0) {
		// TODO Auto-generated method stub
		DLog.d(TAG,"Loader reseted on " + arg0.toString());  
		mImage.setImageBitmap(null);
		mDescription.setText("Empty");
	}

}

class NewsEntity {
	public Uri imgUrl; 
	public String content;
	
	public NewsEntity(){
		reset();
	}
	
	public void reset(){
		//reference to resource assets in term of Uri form
		this.imgUrl = Uri.EMPTY;
		this.content = "";
	}
	
	public boolean isEmpty(){
		return content.equals("")? true : false;
	}
	public String toString(){
		return "NewsEntity is [ imgUrl: " + this.imgUrl + " ] and [ content: " + this.content + " ]";
	}
}

class NewsLoader extends AsyncTaskLoader<NewsEntity>{

	private static String TAG = NewsLoader.class.getSimpleName();
	private Uri mUrl;
	
	public NewsLoader(Context context, Uri url){
		super(context);
		mUrl = url;
	}
	
	public NewsLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public NewsEntity loadInBackground() {
		// TODO Auto-generated method stub
		DLog.d(TAG,"loadInBackground()");
		NewsEntity entity = new NewsEntity();
		
		Document doc;
		try {
			doc = Jsoup.connect(mUrl.toString()).get();
		}catch (IOException e) {
			DLog.i(TAG, "IOException Exception (Message):" + e.getMessage());
			DLog.i(TAG, "IOException Exception (Cause):" + e.getCause());
			// TODO Auto-generated catch block
			e.printStackTrace();
			return entity;
		}
			
		//parse and fetch thumbnail url
		try{
			Element imageElement = doc.select("div.news_ctxt_area_pic a img").first();
			DLog.d(TAG, "select div.news_ctxt_area_pic a img :" + imageElement.attr("src"));
			entity.imgUrl = Uri.parse(imageElement.attr("src"));
		}catch(NullPointerException e){
			DLog.i(TAG, "NullPointer Exception : parse and fetch thumbnail url");
			e.printStackTrace();
		}
		
		//parse and fetch text coontent
		try{
			Element textElement = doc.select("div.news_ctxt_area_word").first();
			DLog.d(TAG, "select div.news_ctxt_area_word : " + textElement.text());
			entity.content = new String(textElement.html());
		}catch(NullPointerException e){
			DLog.i(TAG, "NullPointer Exception : parse and fetch text coontent");
			e.printStackTrace();
		}
		
		return entity;
	}
}