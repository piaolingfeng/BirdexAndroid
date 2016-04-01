package com.birdex.bird.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class ContentView extends RelativeLayout {

	private GestureDetector gestureDetector;

	public ContentView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ContentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (gestureDetector != null) {
			gestureDetector.onTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}
}
