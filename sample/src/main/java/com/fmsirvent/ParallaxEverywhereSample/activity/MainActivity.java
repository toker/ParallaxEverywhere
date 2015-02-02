package com.fmsirvent.ParallaxEverywhereSample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fmsirvent.ParallaxEverywhereSample.R;

/**
 * Created by toker-rg on 02.02.15.
 */
public class MainActivity extends Activity {

    Button mOriginParallaxBtn;
    Button mListImagesBtn;
    Button mListGridImagesBtn;
    Button mAboutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mOriginParallaxBtn = (Button)findViewById(R.id.origin_parallax);
        mListImagesBtn = (Button)findViewById(R.id.list_images_parallax);
        mListGridImagesBtn = (Button)findViewById(R.id.list_grid_images_parallax);
        mAboutBtn = (Button)findViewById(R.id.about);

        mOriginParallaxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ParallaxOriginalActivity.class);
                startActivity(i);
            }
        });
        mListImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FeedImagesActivity.class);
                startActivity(i);
            }
        });
        mListGridImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FeedImagesGridActivity.class);
                startActivity(i);
            }
        });
        mAboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(i);
            }
        });
    }
}