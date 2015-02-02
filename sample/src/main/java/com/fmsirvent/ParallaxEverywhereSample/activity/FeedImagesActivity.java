package com.fmsirvent.ParallaxEverywhereSample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.fmsirvent.ParallaxEverywhereSample.R;
import com.fmsirvent.ParallaxEverywhereSample.adapters.ImagesAdapter;
import com.fmsirvent.ParallaxEverywhereSample.adapters.ImagesGridAdapter;

/**
 * Created by toker-rg on 01.02.15.
 */
public class FeedImagesActivity extends Activity {

    private ListView mListView;
    private ImagesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parallax_listview_activity);

        mListView = (ListView) findViewById(R.id.parallax_lv);

        mAdapter = new ImagesAdapter(FeedImagesActivity.this);
        mListView.setAdapter(mAdapter);
    }
}