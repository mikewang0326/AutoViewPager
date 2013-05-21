package com.imran.autoviewpager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	class CustomAd {
		int drawable;
		String text;

		public CustomAd(int drawable, String text) {
			this.drawable = drawable;
			this.text = text;
		}
	}

	ArrayList<CustomAd> mCustomAds;
	AutoViewPager mAdViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mCustomAds = new ArrayList<MainActivity.CustomAd>();
		mCustomAds.add(new CustomAd(R.drawable.image1, "Ad Text 1"));
		mCustomAds.add(new CustomAd(R.drawable.image2, "Ad Text 2"));
		mCustomAds.add(new CustomAd(R.drawable.image3, "Ad Text 3"));
		mCustomAds.add(new CustomAd(R.drawable.image4, "Ad Text 4"));
		mCustomAds.add(new CustomAd(R.drawable.image5, "Ad Text 5"));

		mAdViewPager = (AutoViewPager) findViewById(R.id.adViewPager1);
		mAdViewPager.setAdapter(new AdPagerAdapter());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.buttonNext:
			mAdViewPager.getNextItem();
			break;
		case R.id.buttonPrevious:
			mAdViewPager.getPreviousItem();
			break;

		default:
			break;
		}
	}

	class AdPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mCustomAds.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (View) object;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			super.setPrimaryItem(container, position, object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.custom_ad, null);
			Holder holder = new Holder();
			holder.mImageViewAd = (ImageView) view
					.findViewById(R.id.imageViewAd);
			holder.mTextViewAd = (TextView) view.findViewById(R.id.textViewAd);

			holder.mImageViewAd
					.setImageResource(mCustomAds.get(position).drawable);
			holder.mTextViewAd.setText(mCustomAds.get(position).text);
			container.addView(view);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	static class Holder {
		ImageView mImageViewAd;
		TextView mTextViewAd;
	}

}
