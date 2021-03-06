package com.howard.projects.newtalkreader.ui.phone;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.howard.projects.newtalkreader.R;
import com.howard.projects.newtalkreader.utils.DLog;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

public class ChannelsPagerFragment extends SherlockFragment implements ViewPager.OnPageChangeListener{

	public static interface OnChannelSelectedListener {
		public void onChannelSelected(int channel);
	}
	
	private static final String TAG = ChannelsPagerFragment.class.getSimpleName();  
	
	ViewPager mPager;
	ChannelsPagerAdapter mAdapter;
	TitlePageIndicator mIndicator;
	OnChannelSelectedListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		DLog.i(TAG, "onCreateView()");
		super.onCreateView(inflater, container, savedInstanceState);
		View root = (View)inflater.inflate(R.layout.newtalk_viewpager_layout, container, false);
		
		// find pager
		mPager = (ViewPager)root.findViewById(R.id.pager);
		mAdapter = new ChannelsPagerAdapter( getSherlockActivity().getApplication(), 
				getSherlockActivity().getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
		
		//find pager indicator, set theme, and associate pager  
        mIndicator = (TitlePageIndicator)root.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        
        final float density = getResources().getDisplayMetrics().density;
        mIndicator.setBackgroundColor(this.getActivity().getResources().getColor(R.color.TitleIndicatorBackground));
        mIndicator.setFooterColor(0x33F18F00);
        mIndicator.setFooterLineHeight(1 * density); //1dp
        mIndicator.setFooterIndicatorHeight(3 * density); //3dp
        mIndicator.setFooterIndicatorStyle(IndicatorStyle.Underline);
        mIndicator.setTextColor(this.getActivity().getResources().getColor(R.color.TitleIndicatorText));
        mIndicator.setSelectedColor(Color.WHITE);
		
        return root;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		DLog.i(TAG, "onResume()");
		super.onResume();
		mIndicator.setOnPageChangeListener(this);
		
	}
	
	public void setOnChannelSelectedListener(OnChannelSelectedListener listener){
		mListener = listener;
	}
	
	public int loadSource(int source){
		mAdapter.setSource(source);
		mIndicator.notifyDataSetChanged();
		return mAdapter.getCount();
	}
	
	public void loadChannel(int channel){
		mPager.setCurrentItem(channel);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageSelected(int channel) {
		// TODO Auto-generated method stub
		if(mListener != null)
			mListener.onChannelSelected(channel);
	}

}


