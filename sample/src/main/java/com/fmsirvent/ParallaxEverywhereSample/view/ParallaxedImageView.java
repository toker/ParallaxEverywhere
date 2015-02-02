package com.fmsirvent.ParallaxEverywhereSample.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.fmsirvent.ParallaxEverywhereSample.util.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A layout in which scrolling of StaggeredImagesGridView will happen
 */

public class ParallaxedImageView extends ScrollView {

	private static final String TAG = Logger.getTag(ParallaxedImageView.class);

	private ImageViewFixedSize mImageView;
	//private StaggeredImagesGridView mStaggeredImagesGridView;

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

    private int mId;


    public ParallaxedImageView(Context context) {
		this(context, null, 0);
	}

	public ParallaxedImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ParallaxedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		mImageView = new ImageViewFixedSize(context);
		addView(mImageView);

        /*mStaggeredImagesGridView = new StaggeredImagesGridView(context);
		addView(mStaggeredImagesGridView);*/

		//Log.i(TAG, "height=" + getHeight());
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
			@Override
			public void onScrollChanged() {
				//Log.i(TAG, "Scroll change detected");

				applyParallax();
			}
		};
		/*mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				viewHeight = (float) getHeight();
				viewWidth = (float) getWidth();

				Log.i(TAG, "onGlobalLayout called, view size: (" + viewWidth + "," +
						viewHeight + ")");

				applyParallax();
			}
		};*/

		ViewTreeObserver viewTreeObserver = getViewTreeObserver();
		viewTreeObserver.addOnScrollChangedListener(mOnScrollChangedListener);
		//viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener);

		//parallaxAnimation();

		init();
	}

	/**
	 * TODO
	 *  1. В зависимости от того, где (по высоте listview) находится scrollview-элемент,
	 * 	его контент должен быть прокручен на определенную величину от верхнего/нижнего края контента
	 *
	 * 	2. Какова бы ни была высота картинки нужно смасштабировать ее в нужный нам размер
	 * 	высота - размер скроллвью + scrollSpaceY
	 * 	ширина - считать по исходному aspect ratio
     *
     * 	3. Doing... Добиться, чтобы хорошо паралаксилась одна картина.
     * 	Т.е. ImageView внутри ScrollView
	 *
	 *	4. Впилить StaggeredImagesGridView
	 */

	@Override
	protected void onDetachedFromWindow() {
		ViewTreeObserver viewTreeObserver = getViewTreeObserver();
		viewTreeObserver.removeOnScrollChangedListener(mOnScrollChangedListener);

		super.onDetachedFromWindow();
	}

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        ViewGroup.LayoutParams params = mImageView.getLayoutParams();
        mImageView.setLayoutParams(params);

        Log.i(TAG, "setImageLayoutParams called: width=" + params.width + ", height=" + params.height);
    }

    /*public void setImageLayoutParams() {
        ViewGroup.LayoutParams params = mImageView.getLayoutParams();
        params.height = mHeightPx;
        mImageView.setLayoutParams(params);
        mImageView.requestLayout();

        Log.i(TAG, "setImageLayoutParams called: width=" + params.width + ", height=" + params.height);
    }*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.i(TAG, "onMeasure called");

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //TODO What's the diff between this call and getHeight?
        /*int height = getMeasuredHeight();
        int width = getMeasuredWidth();*/

        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();

        //Log.i(TAG, "view size: (" + viewWidth + "," + viewHeight + ")");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Log.i(TAG, "onDraw called");
        super.onDraw(canvas);

		/*if (mId == 1 || mId == 2 || mId == 3) {
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setStrokeWidth(20);

			int[] location = new int[2];
			getLocationOnScreen(location);
			canvas.drawPoint(location[0], location[1], paint);
		}*/
    }

	public void setImageBitmap(Bitmap bitmap) {
		mImageView.setImageBitmap(bitmap);
	}

	public void setImages() {
		//mStaggeredImagesGridView.setImages();
	}

    /*public void setInternalId() {
        if (mId == 0) {
            mId = mSeq.incrementAndGet();
            Log.i(TAG, "assigned internal id=" + mId);
        } else {
            Log.i(TAG, "already assigned internal id=" + mId);
        }
    }*/

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

		//Log.i(TAG, "screen size: (" + screenWidth + "," + screenHeight + ")");

		setScrollSpaceY(450);
		itemPathPx = convertDpToPixels(100f+190f+40f, getContext()) + screenHeight;
		deltaYPx = getScrollSpaceY() / itemPathPx;
	}

	/**
	 * Internal image scroll handling
	 */
	private void applyParallax() {
		int scrollValue = 0;

		//Log.i(TAG, "applyParallax() called");

		int[] location = new int[2];
		getLocationOnScreen(location);

		if (mScrollSpaceY != 0) {
			float locationY = (float) location[1];
			locationY = (int)(locationY + 2.0*viewHeight);
			//float newLocationY = locationY / (screenHeight+200) + deltaYPx;
            float newLocationY = locationY / (screenHeight) + deltaYPx;

			if (getTag() == "test_1" || getTag() == "test_3" ||
                    getTag() == "test_0" || getTag() == "test_2") {
				Log.i(TAG, "tag:" + getTag() + ", locationY=" + locationY);
                Log.i(TAG, "tag:" + getTag() + ", ImageView width=" + mImageView.getWidth());
                Log.i(TAG, "tag:" + getTag() + ", ImageView height=" + mImageView.getHeight());

				/*Log.i("PEWImageView", "internal_id:" + mId + ", itemPathPx=" + itemPathPx);
				Log.i("PEWImageView", "internal_id:" + mId + ", deltaYPx=" + deltaYPx);
				Log.i("PEWImageView", "internal_id:" + mId + ", newLocationY=" + newLocationY);*/
			}

            /*ImageView width=1026
            tag:test_3, ImageView height=1047*/

            /*tag:test_11, ImageView width=1026
            * tag:test_11, ImageView height=1920*/

			if (mIsReverseY) {
                //Log.i(TAG, "isReverseY");
				scrollValue = (int) (newLocationY * (-mScrollSpaceY));
				setMyScrollY(scrollValue);
			} else {
                //Log.i(TAG, "not isReverseY");
				scrollValue = (int) (newLocationY * mScrollSpaceY);
				setMyScrollY(scrollValue);
			}
		} else {
			Log.i(TAG, "mScrollSpaceY = 0");
		}
	}

	private void setMyScrollY(int value) {
        /*if (mId == 3 || mId == 12*/
        if (getTag() == "test_11") {
            Log.i(TAG, "internal_id:" + mId + ", scrollValue=" + value);
            Log.i(TAG, "===============================");
        }

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