package com.fmsirvent.ParallaxEverywhereSample.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.fmsirvent.ParallaxEverywhereSample.R;
import com.fmsirvent.ParallaxEverywhereSample.util.Logger;

/**
 * Created by toker-rg on 02.02.15.
 */

/**
 * TODO
 * 1. done! Запилить инфлейтингом готовой вьюхи из xml
 * 2. done! На каждый апроуч завести отдельную активити
 * 3. doing... Задать вопрос на stackoverflow по поводу параллакса одной картинки.
 *      Цель - порефакторить, избавиться от лишнего LinearLyout
 * 4. Почитать про сложные вьюхи
 * 5. done! Завести параллакс на гриде фоток
 * 6. Перенести в песочницу ленты.
 * 7. Прикинуть задачи на неделю (4 дня).
 */

public class ParallaxedImageView2 extends ScrollView {

    private static final String TAG = Logger.getTag(ParallaxedImageView.class);

    private ImageView mImageView;
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = null;

    private float viewHeight;
    private float viewWidth;

    private int screenWidth;
    private int screenHeight;

    private boolean mIsReverseY = false;

    /**
     * Figured out experimentally.
     * Depends on height of scrollview container and
     *  height of parallaxed imageview
     */
    private float mScrollSpaceY = 480;

    private float mDeltaYPx = 0;
    private float mItemScrollPathPx = 0.0f;


    public ParallaxedImageView2(Context context) {
        this(context, null, 0);
    }

    public ParallaxedImageView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxedImageView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(getContext(), R.layout.view_parallaxed_image, this);
        mImageView = (ImageView)findViewById(R.id.parall_content_iv);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                applyParallax();
            }
        };

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnScrollChangedListener(mOnScrollChangedListener);

        init();
    }


    @Override
    protected void onDetachedFromWindow() {
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.removeOnScrollChangedListener(mOnScrollChangedListener);

        super.onDetachedFromWindow();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        ViewGroup.LayoutParams params = getLayoutParams();
        //mImageView.setLayoutParams(params);

        Log.i(TAG, "LayoutParams size: (" + params.width + "," + params.height + ")");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.i(TAG, "onMeasure called");

        Log.i(TAG, "measure size: (" + widthMeasureSpec + "," + heightMeasureSpec + ")");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();

        Log.i(TAG, "view size: (" + viewWidth + "," + viewHeight + ")");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Log.i(TAG, "onDraw called");
        super.onDraw(canvas);
    }

    public void setImageBitmap(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
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

        //Log.i(TAG, "screen size: (" + screenWidth + "," + screenHeight + ")");

        mItemScrollPathPx = dpToPx(100f + 190f + 40f, getContext()) + screenHeight;
        mDeltaYPx = getScrollSpaceY() / mItemScrollPathPx;
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
            locationY = (int)(locationY + 0.2*viewHeight);
            float newLocationY = locationY / (screenHeight) + mDeltaYPx;

            if (getTag() == "test_1" || getTag() == "test_3" ||
                    getTag() == "test_0" || getTag() == "test_2") {
                Log.i(TAG, "tag:" + getTag() + ", locationY=" + locationY);
                Log.i(TAG, "tag:" + getTag() + ", ImageView width=" + mImageView.getWidth());
                Log.i(TAG, "tag:" + getTag() + ", ImageView height=" + mImageView.getHeight());
            }

            if (mIsReverseY) {
                scrollValue = (int) (newLocationY * (-mScrollSpaceY));
                setMyScrollY(scrollValue);
            } else {
                scrollValue = (int) (newLocationY * mScrollSpaceY);
                setMyScrollY(scrollValue);
            }
        } else {
            Log.i(TAG, "mScrollSpaceY = 0");
        }
    }

    private void setMyScrollY(int value) {
        if (getTag() == "test_11") {
            Log.i(TAG, "tag:" + getTag() + ", scrollValue=" + value);
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

    public static float dpToPx(float dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return (dp * density);
    }
}