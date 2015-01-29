package com.fmsirvent.ParallaxEverywhere;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.fmsirvent.ParallaxEverywhere.Utils.InterpolatorSelector;

import java.lang.Override;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fmsirvent on 03/11/14.
 */

public class PEWImageView  extends ImageView {

    private boolean mIsReverseX = false;
    private boolean mIsReverseY = false;
    private float scrollSpaceX = 0;
    private float scrollSpaceY = 0;

    private int screenWidth;
    private int screenHeight;

    private float viewHeight;
    private float viewWidth;

    private boolean mIsBlockedParallaxX = false;
    private boolean mIsBlockedParallaxY = false;

    Interpolator interpolator = null;

    ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = null;
    ViewTreeObserver.OnGlobalLayoutListener  mOnGlobalLayoutListener = null;

	private int mId;

	private static AtomicInteger mSeq;
	static {
		Log.i("PEWImageView", "static block called");
		mSeq = new AtomicInteger(0);
	}

	private int framePathPx = 350; // imageview "frame" path
	private float deltaYPx = 0.0f;
	private float itemPathPx = 0.0f;

    public PEWImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            checkAttributes(attrs);
        }

		mId = mSeq.incrementAndGet();
    }

    public PEWImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            checkAttributes(attrs);
        }

		mId = mSeq.incrementAndGet();
    }

	private void checkAttributes(AttributeSet attrs) {
		TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.PEWAttrs);
		int reverse = arr.getInt(R.styleable.PEWAttrs_reverse, 1);

		mIsBlockedParallaxX = arr.getBoolean(R.styleable.PEWAttrs_block_parallax_x, false);
		mIsBlockedParallaxY = arr.getBoolean(R.styleable.PEWAttrs_block_parallax_y, false);

		mIsReverseX = false;
		mIsReverseY = false;
		switch (reverse) {
			case AttrConstants.REVERSE_NONE:
				break;
			case AttrConstants.REVERSE_X:
				mIsReverseX = true;
				break;
			case AttrConstants.REVERSE_Y:
				mIsReverseY = true;
				break;
			case AttrConstants.REVERSE_BOTH:
				mIsReverseX = true;
				mIsReverseY = true;
				break;
		}

		switch (getScaleType()) {
			case CENTER:
			case CENTER_CROP:
			case CENTER_INSIDE:
				break;
			case FIT_CENTER:
				Log.d("ParallaxEverywhere", "Scale type firCenter unsupported");
				break;
			case FIT_END:
				Log.d("ParallaxEverywhere", "Scale type fitEnd unsupported");
				break;
			case FIT_START:
				Log.d("ParallaxEverywhere", "Scale type fitStart unsupported");
				break;
			case FIT_XY:
				Log.d("ParallaxEverywhere", "Scale type fitXY unsupported");
				break;
			case MATRIX:
				Log.d("ParallaxEverywhere", "Scale type matrix unsupported");
				break;
		}

		int interpolationId = arr.getInt(R.styleable.PEWAttrs_interpolation, 0);
		interpolator = InterpolatorSelector.interpolatorId(interpolationId);

		arr.recycle();
	}

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                applyParallax2();
            }
        };

        mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewHeight = (float) getHeight();
                viewWidth = (float) getWidth();

				Log.i("PEWImageView", "onGlobalLayout, imageview size: (" + viewWidth + "," +
						viewHeight + ")");

                applyParallax2();
            }
        };

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnScrollChangedListener(mOnScrollChangedListener);
        viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener);

        parallaxAnimation();
    }

	@Override
	protected void onDraw(Canvas canvas) {
		Log.i("PEWImageView", "onDraw called");
		super.onDraw(canvas);

		/*if (mId == 3) {
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setStrokeWidth(200);

			int[] location = new int[2];
			getLocationOnScreen(location);
			canvas.drawPoint(location[0], location[1], paint); //-1800

			Log.i("PEWImageView", "internal_id:" + mId + ", view location: " + location[0] + "," +
					location[1]);

		}*/
	}

    @Override
    protected void onDetachedFromWindow() {
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.removeOnScrollChangedListener(mOnScrollChangedListener);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        } else {
            viewTreeObserver.removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
        }
        super.onDetachedFromWindow();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.i("PEWImageView", "onMeasure called");

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getDrawable() != null) {
            int drawableHeight = getDrawable().getIntrinsicHeight();
            int drawableWidth = getDrawable().getIntrinsicWidth();
            int height = getMeasuredHeight();
            int width = getMeasuredWidth();

			Log.i("PEWImageViewSize", "drawableWidth=" + drawableWidth);
			Log.i("PEWImageViewSize", "drawableHeight=" + drawableHeight);

			Log.i("PEWImageViewSize", "width=" + width);
			Log.i("PEWImageViewSize", "height=" + height);

            float scale;

			float drawableNewWidth = 0;
			float drawableNewHeight = 0;

            switch (getScaleType()) {
                case CENTER_CROP:
                case CENTER:
                case CENTER_INSIDE:
                    if (drawableWidth * height > width * drawableHeight) {
                        scale = (float) height / (float) drawableHeight;
                        drawableNewWidth = drawableWidth * scale;
                        drawableNewHeight = height;
                    } else {
                        scale = (float) width / (float) drawableWidth;
                        drawableNewWidth = width;
                        drawableNewHeight = drawableHeight * scale;
                    }
                    break;
                case FIT_CENTER:
                case FIT_END:
                case FIT_START:
                case FIT_XY:
                case MATRIX:
                    break;
            }

            scrollSpaceY = (drawableNewHeight > height) ? (drawableNewHeight - height) : 0;
            scrollSpaceX = (drawableNewWidth > width) ? (drawableNewWidth - width) : 0;

			Log.i("PEWImageView", "scrollSpaceX: " + scrollSpaceX);
			Log.i("PEWImageView", "scrollSpaceY: " + scrollSpaceY);
        }
    }

    private void parallaxAnimation() {
        initSizeScreen();
        //applyParallax();
    }

    private void initSizeScreen() {
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

		Log.i("PEWImageView", "initSizeScreen, screen size: (" + screenHeight + "," +
				screenHeight + ")");

		itemPathPx = convertDpToPixels(100f+200f+40f, getContext());
		itemPathPx = itemPathPx + screenHeight;
		deltaYPx = framePathPx / itemPathPx;
    }

	/*
	TODO Как сделано сейчас
	1. Вычисляем scrollSpaceY - расстояние, которое должно пройти "окошко" по картинке при
	при пролистывании пункта списка.
	Берем высоту картинки из нее вычитаем высоту "окошка", т.е. ImageView, содержащего картинку.

	2. Вычисляем scrollDelta шаг параллакс-прокрутки
	Берем текущую Y-координату, прибавляем к ней половину высоты данного ImageView.
	Делим эту сумму на высоту экрана.

	3. Вычисляем scrollValue величину сдвига "окошка"
	scrollDelta умножаем на scrollSpaceY

	4. Сдвигаем окошко на scrollValue
	* */

	/*
	TODO Как нужно:
	1. При листании вверх "окошко" движется по картинке вверх
	2. Скорость движения "окошка" должна быть постоянной и не зависеть от высоты картинки
	3. Скорость движения "окошка" должна быть меньше скорости основного скролла
	4.
	* */

	/*
	TODO Алгоритм
	1.
	 */

	private void applyParallax() {
		Log.i("PEWImageView", "applyParallax() called");

		int[] location = new int[2];
        getLocationOnScreen(location);

        if (scrollSpaceY != 0) {
			float locationY = (float) location[1];
			//float locationUsableY = locationY + viewHeight / 2;
			float scrollDeltaY = (locationY - 580) / (screenHeight);
			scrollDeltaY -= scrollDeltaY*0.1;
			float interpolatedScrollDeltaY = interpolator.getInterpolation(scrollDeltaY);

			/*float locationY = (float) location[1];
			float locationUsableY = locationY + viewHeight / 2;
			float scrollDeltaY = locationUsableY / (screenHeight);
			float interpolatedScrollDeltaY = interpolator.getInterpolation(scrollDeltaY);*/

			if (mId == 3) {
				Log.i("PEWImageView", "internal_id:" + mId + ", scrollSpaceY=" + scrollSpaceY);
				Log.i("PEWImageView", "internal_id:" + mId + ", locationY=" + locationY);
				//Log.i("PEWImageView", "internal_id:" + mId + ", locationUsableY=" + locationUsableY);
				Log.i("PEWImageView", "internal_id:" + mId + ", scrollDeltaY=" + scrollDeltaY);
				Log.i("PEWImageView", "internal_id:" + mId + ", interpolatedScrollDeltaY=" + interpolatedScrollDeltaY);
			}

			int scrollValue = 0;
			if (mIsReverseY) {
				//Log.i("PEWImageView", "mIsReverseY");
				/*scrollValue = (int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaY), -0.5f),
						0.5f) * -scrollSpaceY);*/
				scrollValue = (int) (interpolatedScrollDeltaY * (-scrollSpaceY) / 2);
				setMyScrollY(scrollValue);
			} else {
				/*scrollValue = (int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaY), -0.5f),
						0.5f) * scrollSpaceY);*/
				scrollValue = (int) (interpolatedScrollDeltaY * scrollSpaceY / 2);
				setMyScrollY(scrollValue);
			}
        }

        if (scrollSpaceX != 0) {
            float locationX = (float) location[0];
            float locationUsableX = locationX + viewWidth / 2;
            float scrollDeltaX = locationUsableX / screenWidth;

            float interpolatedScrollDeltaX = interpolator.getInterpolation(scrollDeltaX);

            if (mIsReverseX) {
                setMyScrollX((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaX), -0.5f), 0.5f) * -scrollSpaceX));
            } else {
				setMyScrollX((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaX), -0.5f), 0.5f) * scrollSpaceX));
            }
        }
    }

	/**
	 * Internal image scroll handling
	 */
	private void applyParallax2() {
		int scrollValue = 0;

		Log.i("PEWImageView", "applyParallax2() called");

		int[] location = new int[2];
		getLocationOnScreen(location);

		//framePathPx = 0;
		if (framePathPx != 0) {
			float locationY = (float) location[1];
			locationY = (int)(locationY - 1.4* viewHeight);
			float newLocationY = (locationY / screenHeight) + deltaYPx;

			if (mId == 3) {
				Log.i("PEWImageView", "internal_id:" + mId + ", locationY=" + locationY);
				Log.i("PEWImageView", "internal_id:" + mId + ", framePathPx=" + framePathPx);
				Log.i("PEWImageView", "internal_id:" + mId + ", itemPathPx=" + itemPathPx);
				Log.i("PEWImageView", "internal_id:" + mId + ", deltaYPx=" + deltaYPx);
				Log.i("PEWImageView", "internal_id:" + mId + ", newLocationY=" + newLocationY);
			}

			if (mIsReverseY) {
				scrollValue = (int) (newLocationY * (-framePathPx));
				setMyScrollY(scrollValue);
			} else {
				scrollValue = (int) (newLocationY * framePathPx);
				setMyScrollY(scrollValue);
			}
		} else {
			Log.i("PEWImageView", "framePathPx = 0");
		}
	}

    private void setMyScrollX(int value) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setScrollX(value);
        } else {
            scrollTo(value, getScrollY());
        }
    }

    private void setMyScrollY(int value) {
		Log.i("PEWImageView", "internal_id:" + mId + ", setMyScrollY(), value=" + value);
		Log.i("PEWImageView", "===============================");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setScrollY(value);
        } else {
            scrollTo(getScrollX(),value);
        }
    }

    public float getScrollSpaceX() {
        return scrollSpaceX;
    }

    public void setScrollSpaceX(float scrollSpaceX) {
        this.scrollSpaceX = scrollSpaceX;
    }

    public float getScrollSpaceY() {
        return scrollSpaceY;
    }

    public void setScrollSpaceY(float scrollSpaceY) {
        this.scrollSpaceY = scrollSpaceY;
    }


	public static float convertPixelsToDp(float px, Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	public static float convertDpToPixels(float dp, Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}
}