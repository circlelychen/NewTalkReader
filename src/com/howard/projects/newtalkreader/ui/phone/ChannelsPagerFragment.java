package com.howard.projects.newtalkreader.ui.phone;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.howard.projects.newtalkreader.R;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

public class ChannelsPagerFragment extends SherlockFragment {

	ViewPager mPager;
	TitlePageIndicator mIndicator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View root = (View)inflater.inflate(R.layout.newtalk_viewpager_layout, container, false);
		
		// find pager
		mPager = (ViewPager)root.findViewById(R.id.pager);
		ChannelsPagerAdapter mAdapter = new ChannelsPagerAdapter(getActivity().getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
		
		//find pager indicator, set theme, and associate pager  
        mIndicator = (TitlePageIndicator)root.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        mIndicator.setBackgroundColor(0x18FF0000);
        mIndicator.setFooterColor(0xFFAA2222);
        mIndicator.setFooterLineHeight(1 * density); //1dp
        mIndicator.setFooterIndicatorHeight(3 * density); //3dp
        mIndicator.setFooterIndicatorStyle(IndicatorStyle.Underline);
        mIndicator.setTextColor(0xAA000000);
        mIndicator.setSelectedColor(0xFF000000);
        mIndicator.setSelectedBold(true);
		
        return root;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

}