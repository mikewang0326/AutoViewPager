package com.imran.autoviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

interface OnThreadPageChangeListener {
	public void pageChange(int item);
}

public class AutoViewPager extends ViewPager {

	private static final int DURATION = 5000;
	private int duration = DURATION;
	static int cItem = 0;
	AdThread aThread;
	boolean mThreadActive = true;
	static OnThreadPageChangeListener mThreadPageChange;

	public AutoViewPager(Context context) {
		super(context);
		mThreadPageChange = new OnThreadPageChangeListener() {

			@Override
			public void pageChange(int item) {
				setCurrentItem(item);
			}
		};
	}

	public AutoViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mThreadPageChange = new OnThreadPageChangeListener() {

			@Override
			public void pageChange(int item) {
				setCurrentItem(item);
			}
		};
	}

	/**
	 * Set the duration for changing the page.
	 * 
	 * @param duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void getNextItem() {
		int i = getCurrentItem();
		if (i < getAdapter().getCount()) {
			setCurrentItem(i + 1);
		}
	}

	public void getPreviousItem() {
		int i = getCurrentItem();
		if (i > 0) {
			setCurrentItem(i - 1);
		}
	}

	@Override
	protected void onAttachedToWindow() {
		startThread();
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		stopThread();
		super.onDetachedFromWindow();
	}

	private void startThread() {
		mThreadActive = true;
		aThread = new AdThread();
		aThread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		aThread.start();
	}

	public void stopThread() {
		mThreadActive = false;
		synchronized (aThread) {
			if (aThread != null) {
				aThread.interrupt();
				aThread = null;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			stopThread();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			startThread();
		}

		return super.onTouchEvent(event);
	}

	/**
	 * Handler executes the code in the UI thread when called from the
	 * background thread.
	 */
	@SuppressLint("HandlerLeak")
	private static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			cItem = msg.what;
			mThreadPageChange.pageChange(cItem + 1);
		};
	};

	/**
	 * Thread class changes the page after the duration limit is reached in the
	 * background process.
	 * 
	 * @author imranmohammed
	 * 
	 */
	class AdThread extends Thread {

		public void run() {
			while (mThreadActive) {
				try {
					sleep(duration);
					int i = getCurrentItem();
					if (i == getAdapter().getCount() - 1) {
						i = -1;
					}
					handler.sendEmptyMessage(i);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}

			}
		}
	}

}
