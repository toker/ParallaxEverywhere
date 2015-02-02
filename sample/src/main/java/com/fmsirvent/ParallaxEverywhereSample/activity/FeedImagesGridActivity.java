package com.fmsirvent.ParallaxEverywhereSample.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fmsirvent.ParallaxEverywhereSample.R;
import com.fmsirvent.ParallaxEverywhereSample.adapters.ImagesGridAdapter;
import com.fmsirvent.ParallaxEverywhereSample.model.CustomData;
import com.fmsirvent.ParallaxEverywhereSample.view.ParallaxStaggeredImagesGridLayout;
import com.fmsirvent.ParallaxEverywhereSample.view.ParallaxedImageView;
import com.fmsirvent.ParallaxEverywhereSample.view.ParallaxedImageView2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toker-rg on 27.01.15.
 */

public class FeedImagesGridActivity extends Activity {

	private ListView mListView;
    private ImagesGridAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parallax_listview_activity);

		mListView = (ListView) findViewById(R.id.parallax_lv);

        mAdapter = new ImagesGridAdapter(FeedImagesGridActivity.this);
		mListView.setAdapter(mAdapter);
	}

}