package com.fmsirvent.ParallaxEverywhereSample.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.fmsirvent.ParallaxEverywhereSample.util.Logger;

/**
 * A layout it which scrolling of StaggeredImagesGridView will happen
 */

public class ParallaxedImageView extends ScrollView {

	private static final String TAG = Logger.getTag(ParallaxedImageView.class);

	//private ImageView mImageView;
	private StaggeredImagesGridView mStaggeredImagesGridView;

	//private int mScrollSpaceY = 400; //in px

	ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = null;
	ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = null;

	private float viewHeight;
	private float viewWidth;

	private int screenWidth;
	private int screenHeight;

	private boolean mIsReverseY = false;
	private float mScrollSpaceY = 0;

	private float deltaYPx = 0;

	private float itemPathPx = 0.0f;


	public ParallaxedImageView(Context context) {
		this(context, null, 0);
	}

	public ParallaxedImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ParallaxedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		/*mImageView = new ImageView(context);
		mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		addView(mImageView);*/

		mStaggeredImagesGridView = new StaggeredImagesGridView(context);
		addView(mStaggeredImagesGridView);

		Log.i(TAG, "height=" + getHeight());
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
			@Override
			public void onScrollChanged() {
				Log.i(TAG, "Scroll change detected");

				applyParallax();
			}
		};
		mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				viewHeight = (float) getHeight();
				viewWidth = (float) getWidth();

				Log.i("PEWImageView", "onGlobalLayout called, view size: (" + viewWidth + "," +
						viewHeight + ")");

				applyParallax();
			}
		};

		ViewTreeObserver viewTreeObserver = getViewTreeObserver();
		viewTreeObserver.addOnScrollChangedListener(mOnScrollChangedListener);
		viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener);

		//parallaxAnimation();

		init();
	}

	/**
	 * TODO
	 *  1. В зависимости от того, где (по высоте listview) находится scrollview-элемент,
	 * 	его контент должен быть прокручен на определенную величину
	 *
	 * 	2. Какова бы ни была высота картинки нужно смасштабировать ее в нужный нам размер
	 * 	высота - размер скроллвью + scrollSpaceY
	 * 	ширина - считать по исходному aspect ratio
	 *
	 *	3. Doing... Впилить StaggeredImagesGridView
	 */

	@Override
	protected void onDetachedFromWindow() {
		ViewTreeObserver viewTreeObserver = getViewTreeObserver();
		viewTreeObserver.removeOnScrollChangedListener(mOnScrollChangedListener);

		super.onDetachedFromWindow();
	}

	public void setImageBitmap(Bitmap bitmap) {
		//mImageView.setImageBitmap(bitmap);
	}

	public void setImages() {
		mStaggeredImagesGridView.setImages();
	}

	private void init() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			display.getSize(size);
			screenHeight = size.y;
			screenWidth = size.x;
		} else {
			screenHeight = display.getHeight();
			screenWidth = display.getWidth();
		}

		Log.i(TAG, "initSizeScreen, screen size: (" + screenHeight + "," + screenHeight + ")");

		setScrollSpaceY(450);
		itemPathPx = convertDpToPixels(100f+200f+40f, getContext());
		itemPathPx = itemPathPx + screenHeight;
		deltaYPx = getScrollSpaceY() / itemPathPx;
	}

	/**
	 * Internal image scroll handling
	 */
	private void applyParallax() {
		int scrollValue = 0;

		Log.i("PEWImageView", "applyParallax() called");

		int[] location = new int[2];
		getLocationOnScreen(location);

		if (mScrollSpaceY != 0) {
			float locationY = (float) location[1];
			//locationY = (int)(locationY - 0. * viewHeight);
			//locationY = (int)(locationY + viewHeight);
			float newLocationY = locationY / (screenHeight+200) + deltaYPx;

			/*if (mId == 3) {
				Log.i("PEWImageView", "internal_id:" + mId + ", locationY=" + locationY);
				Log.i("PEWImageView", "internal_id:" + mId + ", framePathPx=" + framePathPx);
				Log.i("PEWImageView", "internal_id:" + mId + ", itemPathPx=" + itemPathPx);
				Log.i("PEWImageView", "internal_id:" + mId + ", deltaYPx=" + deltaYPx);
				Log.i("PEWImageView", "internal_id:" + mId + ", newLocationY=" + newLocationY);
			}*/

			if (mIsReverseY) {
				scrollValue = (int) (newLocationY * (-mScrollSpaceY));
				setMyScrollY(scrollValue);
			} else {
				scrollValue = (int) (newLocationY * mScrollSpaceY);
				setMyScrollY(scrollValue);
			}
		} else {
			Log.i("PEWImageView", "framePathPx = 0");
		}
	}

	private void setMyScrollY(int value) {
		Log.i(TAG, "===============================");

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			setScrollY(value);
		} else {
			scrollTo(getScrollX(),value);
		}
	}

	public float getScrollSpaceY() {
		return mScrollSpaceY;
	}

	public void setScrollSpaceY(float scrollSpaceY) {
		mScrollSpaceY = scrollSpaceY;
	}

	public static float convertDpToPixels(float dp, Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}
}