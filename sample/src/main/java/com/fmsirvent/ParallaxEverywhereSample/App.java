package com.fmsirvent.ParallaxEverywhereSample;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.fmsirvent.ParallaxEverywhereSample.util.Logger;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;


public class App extends Application {

	private static final String TAG = Logger.getTag(App.class);

	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 100; //100 MB

	@Override
	public void onCreate() {
		super.onCreate();

		initUniversalImageLoader(getApplicationContext());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}


	private void initUniversalImageLoader(Context context) {

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(android.R.color.transparent)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.ARGB_8888)
				.resetViewBeforeLoading(true)
				.displayer(new RoundedBitmapDisplayer(100))
				.build();

		File cacheDir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.memoryCacheExtraOptions(480, 800) // width, height
				.discCacheExtraOptions(480, 800, null)
				.threadPoolSize(4)
				.threadPriority(Thread.MIN_PRIORITY + 4)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCacheSizePercentage(20)
				.diskCache(new UnlimitedDiscCache(cacheDir)) // default
				.diskCacheSize(DISK_CACHE_SIZE)
				.diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 8 * 1000)) // connection and read timeout
				.defaultDisplayImageOptions(defaultOptions)
				.writeDebugLogs()
				.build();

		ImageLoader.getInstance().init(config);
	}
}