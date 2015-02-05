package com.fmsirvent.ParallaxEverywhereSample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.fmsirvent.ParallaxEverywhereSample.R;
import com.fmsirvent.ParallaxEverywhereSample.adapters.ImagesGridAdapter;

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