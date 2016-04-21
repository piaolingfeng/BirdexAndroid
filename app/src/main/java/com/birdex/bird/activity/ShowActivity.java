package com.birdex.bird.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.birdex.bird.R;

import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends Activity {

	private ArrayList<View> dots;
	private ViewPager mViewPager;
	private ViewPagerAdapter adapter;
	private View view1, view2, view3, view4;
	private ImageView entry;
	private int oldPosition = 0;// 记录上一次点的位置
	private int currentItem; // 当前页面
	private List<View> viewList = new ArrayList<View>();// 把需要滑动的页卡添加到这个list中

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show);

		// 添加当前Acitivity到ancivitylist里面去，为了方便统一退出
		// 显示的点
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.dot_1));
		dots.add(findViewById(R.id.dot_2));
		dots.add(findViewById(R.id.dot_3));
		dots.add(findViewById(R.id.dot_4));
		// 得到viewPager的布局
		LayoutInflater lf = LayoutInflater.from(ShowActivity.this);
		view1 = lf.inflate(R.layout.viewpager_item1, null);
		view2 = lf.inflate(R.layout.viewpager_item2, null);
		view3 = lf.inflate(R.layout.viewpager_item3, null);
		view4 = lf.inflate(R.layout.viewpager_item4, null);
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);
		// 找到点击进入那个按钮
		mViewPager = (ViewPager) findViewById(R.id.vp);

		adapter = new ViewPagerAdapter();
		mViewPager.setAdapter(adapter);
		dots.get(0).setBackgroundResource(R.drawable.dot_focused);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub

				dots.get(oldPosition).setBackgroundResource(
						R.drawable.dot_normal);
				dots.get(position)
						.setBackgroundResource(R.drawable.dot_focused);

				oldPosition = position;
				currentItem = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		entry = (ImageView) view4.findViewById(R.id.entry);
		entry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShowActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
	}

	private class ViewPagerAdapter extends PagerAdapter {

		public ViewPagerAdapter() {
			super();
			// TODO Auto-generated constructor stub
			// 得到viewpager切换的三个布局，并把它们加入到viewList里面,记得view用打气筒生成，否则容易出现空指针异常

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return viewList.size();
		}

		// 是否是同一张图片
		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			((ViewPager) view).removeView(viewList.get(position));

		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			((ViewPager) view).addView(viewList.get(position));
			return viewList.get(position);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}

}
