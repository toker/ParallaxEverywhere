package com.fmsirvent.ParallaxEverywhereSample.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by toker-rg on 01.02.15.
 */
public class ImageViewFixedSize extends ImageView {

    private final float targetWidthDp = 342; // TODO should be set to parent width
    private final float targetHeightDp = 228; //scale = 3/2

    private float targetWidthPx;
    private float targetHeightPx;

    public ImageViewFixedSize(Context context) {
        this(context, null, 0);
    }

    public ImageViewFixedSize(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewFixedSize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        targetWidthPx = dpToPx(targetWidthDp, context);
        targetHeightPx = dpToPx(targetHeightDp, context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int bitmapNewWidth = 0;
        int bitmapNewHeight = 0;

        Log.v("Pictures", "heightMeasureSpec = " + heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Drawable drawable = getDrawable();
        if (drawable == null)
            return;

        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / targetWidthPx;
            width = (int)targetWidthPx;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / targetHeightPx;
            height = (int)targetHeightPx;
            width = (int) (width / ratio);
        } else {
            // square
            height = (int)targetHeightPx;
            width = (int)targetWidthPx;
        }

        //Log.v("Pictures", "after scaling width and height are " + width + "--" + height);

        /*Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        setImageDrawable(result);*/


        /*float drawableHeight = drawable.getIntrinsicHeight();
        float drawableWidth = drawable.getIntrinsicWidth();*/

        float ratio;
        /*if (targetHeightPx / bitmapHeight < targetWidthPx / bitmapWidth) {
            ratio = targetHeightPx / bitmapHeight;
            bitmapNewHeight = (int)targetHeightPx;
            bitmapNewWidth = (int)(bitmapHeight * ratio);
        } else {
            ratio = targetWidthPx / bitmapWidth;
            bitmapNewWidth = (int)targetWidthPx;
            bitmapNewHeight = (int)(bitmapHeight * ratio);
        }*/

        /*ratio = targetWidthPx / (float)bitmapWidth;
        bitmapNewWidth = (int)targetWidthPx;
        bitmapNewHeight = (int)(bitmapWidth * ratio);


        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmapNewWidth, bitmapNewHeight,
                true);
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        setImageDrawable(result);*/

        //TODO do this in onLayout(...)?
        // Now change ImageView's dimensions to match the scaled image
        /*
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);*/
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = (int)targetHeightPx;
        setLayoutParams(params);
        setScaleType(ScaleType.CENTER_CROP);
    }

    public static float dpToPx(float dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return (dp * density);
    }
}
