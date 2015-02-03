package com.fmsirvent.ParallaxEverywhereSample.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fmsirvent.ParallaxEverywhereSample.util.Logger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Staggered images grid view
 */
public class StaggeredImagesGridView extends FrameLayout /*implements View.OnClickListener*/ {

    private static final String TAG = Logger.getTag(StaggeredImagesGridView.class);

    private final int IMAGES_MAX_NUMBER = 10;

    private static final Float[][] IMAGES_1 = {{1f}};
    private static final Float[][] IMAGES_2 = {{1f}, {1f}};
    private static final Float[][] IMAGES_3 = {{1f}, {.5f, .5f}};
    private static final Float[][] IMAGES_4 = {{1f}, {.333f, .333f, .333f}};
    private static final Float[][] IMAGES_5 = {{.5f, .5f}, {.333f, .333f, .333f}};
    private static final Float[][] IMAGES_6 = {{.5f, .5f}, {1f}, {.333f, .333f, .333f}};
    private static final Float[][] IMAGES_7 = {{.5f, .5f}, {1f}, {.25f, .25f, .25f, .25f}};
    private static final Float[][] IMAGES_8 = {{.333f, .333f, .333f}, {.5f, .5f}, {.333f, .333f, .333f}};
    private static final Float[][] IMAGES_9 = {{.333f, .333f, .333f}, {.5f, .5f}, {.25f, .25f, .25f, .25f}};
    private static final Float[][] IMAGES_10 = {{.25f, .25f, .25f, .25f}, {1f}, {.2f, .2f, .2f, .2f, .2f}};

    private static final Map<Integer, Float[][]> DIMENSIONS = new HashMap<Integer, Float[][]>();

    //private OnImagesGridItemClickListener mOnImagesGridItemClickListener;

	private int mImagesActualNumber;

	private Random mRandom;

	private Context mContext;

    /**
     * Display options for image loader for images attachments
     */
    private DisplayImageOptions mImageAttachmentOptions;

	private List<String> mImagesUrls = new ArrayList<>();

    static {
        DIMENSIONS.put(1, IMAGES_1);
        DIMENSIONS.put(2, IMAGES_2);
        DIMENSIONS.put(3, IMAGES_3);
        DIMENSIONS.put(4, IMAGES_4);
        DIMENSIONS.put(5, IMAGES_5);
        DIMENSIONS.put(6, IMAGES_6);
        DIMENSIONS.put(7, IMAGES_7);
        DIMENSIONS.put(8, IMAGES_8);
        DIMENSIONS.put(9, IMAGES_9);
        DIMENSIONS.put(10, IMAGES_10);
    }

     private String imageUrl;
    //private List<Image> mImageList;
    private List<ImageView> mImageViewList;
    private int mVisibleImagesNumber;

    private Random random;


	public StaggeredImagesGridView(Context context) {
		this(context, null);
	}

    public StaggeredImagesGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

		mContext = context;

        mImageAttachmentOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.transparent)
                .cacheInMemory(true)
                .cacheOnDisc(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .resetViewBeforeLoading(false)
				.displayer(new FadeInBitmapDisplayer(200))
                .build();

		mImagesUrls = Arrays.asList(urls);
		mRandom = new Random();

        //mImageList = new ArrayList<Image>();
        mImageViewList = new ArrayList<ImageView>(IMAGES_MAX_NUMBER);

        random = new Random();
		mImagesActualNumber = random.nextInt(IMAGES_MAX_NUMBER);

        initImageViewList();
    }

    /*public void setOnImagesClickListener(OnImagesGridItemClickListener listener) {
        mOnImagesGridItemClickListener = listener;
    }*/

    private void initImageViewList() {
        for (int i = 0; i < IMAGES_MAX_NUMBER; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            addView(imageView);
            mImageViewList.add(imageView);
        }
    }

    /*private void setVisibility() {
        // guard
        if (mImageList.size() > IMAGES_MAX_NUMBER)
            mImagesActualNumber = IMAGES_MAX_NUMBER;
        else
            mImagesActualNumber = mImageList.size();

        // reduce visible ImageViews number
        if (mVisibleImagesNumber > mImagesActualNumber) {
            for (int i = mImagesActualNumber; i < mVisibleImagesNumber; i++) {
                mImageViewList.get(i).setVisibility(View.GONE);
            }
            // increase visible ImageViews number
        } else if (mVisibleImagesNumber < mImagesActualNumber) {
            for (int i = mVisibleImagesNumber; i < mImagesActualNumber; i++) {
                mImageViewList.get(i).setVisibility(View.VISIBLE);
            }
        }
        mVisibleImagesNumber = mImagesActualNumber;
    }*/
    private void setVisibility() {
        for (int i = 0; i < mImagesActualNumber; i++) {
            mImageViewList.get(i).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Set images for loading
     *
     * @param images
     * @param showThumbnails
     */
    /*public void setImages(List<Image> images, boolean showThumbnails) {
        mImageList = images;
        setVisibility();

        for (int i = 0; i < mImagesActualNumber; i++) {

            Image image = mImageList.get(i);
            ImageView imageView = mImageViewList.get(i);

            ImageLoader.getInstance().displayImage(image.getImageUrl(), imageView,
													mImageAttachmentOptions);
        }

        invalidate();
    }*/

    public void setImages() {
        /*mImageList = images;*/
        setVisibility();

        for (int i=0; i<mImagesActualNumber; i++) {

            /*Image image = mImageList.get(i);*/
            ImageView imageView = mImageViewList.get(i);

			imageUrl = mImagesUrls.get(mRandom.nextInt(mImagesUrls.size()));
            ImageLoader.getInstance().displayImage(imageUrl, imageView, mImageAttachmentOptions);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int margin = 5; // margin between images inside
        int width = right - left;
        int height = bottom - top;

        //Float[][] dimensions = DIMENSIONS.get(mImagesUrls.size());
        //Float[][] dimensions = DIMENSIONS.get(mImageList.size());
        Float[][] dimensions = DIMENSIONS.get(mImagesActualNumber);
        if (dimensions == null) {
            return;
        }

        int rowsCount = dimensions.length;
        int bigRowHeight;
        int smallRowHeight;

        if (rowsCount == 2) {
            bigRowHeight = (int) (height / 1.5);
            smallRowHeight = height - bigRowHeight;
        } else if (rowsCount == 3) {
            bigRowHeight = height / 2;
            smallRowHeight = (height - bigRowHeight) / 2;
        } else {
            bigRowHeight = smallRowHeight = height / rowsCount;
        }

        int index = 0;
        int currentLeft = 0;
        int currentTop = 0;

        for (int row = 0; row < rowsCount; row++) {

            boolean isBigRow = true;

            for (int tmpRow = 0; tmpRow < rowsCount; tmpRow++) {
                if (dimensions[tmpRow].length < dimensions[row].length) {
                    isBigRow = false;
                    break;
                }
            }

            int rowHeight = isBigRow ? bigRowHeight : smallRowHeight;

            for (int column = 0; column < dimensions[row].length; column++, index++) {
                int columnWidth = (int) (width * dimensions[row][column]);
                View childView = getChildAt(index);

                childView.measure(MeasureSpec.EXACTLY | columnWidth, MeasureSpec.EXACTLY | rowHeight);
                childView.layout(currentLeft, currentTop,
                        currentLeft + columnWidth, currentTop + rowHeight);

                currentLeft += columnWidth;
                currentLeft += margin;
            }

            currentTop += rowHeight;
            currentTop += margin;
            currentLeft = 0;
        }
    }


    /*@Override
    public void onClick(View view) {
        if (mOnImagesGridItemClickListener != null) {
            mOnImagesGridItemClickListener.onImageClick((Integer) view.getTag());
        }
    }*/

	private String [] urls = {
            "http://www.nat-geo.ru/images/upload/ph/photo/d71_3028_1300_1422531967_full.jpg",
			"http://farm7.staticflickr.com/6101/6853156632_6374976d38_c.jpg",
			"http://farm8.staticflickr.com/7232/6913504132_a0fce67a0e_c.jpg",
			"http://farm5.staticflickr.com/4133/5096108108_df62764fcc_b.jpg",
			"http://farm5.staticflickr.com/4074/4789681330_2e30dfcacb_b.jpg",
			"http://farm9.staticflickr.com/8208/8219397252_a04e2184b2.jpg",
			"http://farm9.staticflickr.com/8483/8218023445_02037c8fda.jpg",
			"http://farm9.staticflickr.com/8335/8144074340_38a4c622ab.jpg",
			"http://farm9.staticflickr.com/8060/8173387478_a117990661.jpg",
			"http://farm9.staticflickr.com/8056/8144042175_28c3564cd3.jpg",
			"http://farm9.staticflickr.com/8183/8088373701_c9281fc202.jpg",
			"http://farm9.staticflickr.com/8185/8081514424_270630b7a5.jpg",
			"http://farm9.staticflickr.com/8462/8005636463_0cb4ea6be2.jpg",
			"http://farm9.staticflickr.com/8306/7987149886_6535bf7055.jpg",
			"http://farm9.staticflickr.com/8444/7947923460_18ffdce3a5.jpg",
			"http://farm9.staticflickr.com/8182/7941954368_3c88ba4a28.jpg",
			"http://farm9.staticflickr.com/8304/7832284992_244762c43d.jpg",
			"http://farm9.staticflickr.com/8163/7709112696_3c7149a90a.jpg",
			"http://farm8.staticflickr.com/7127/7675112872_e92b1dbe35.jpg",
			"http://farm8.staticflickr.com/7111/7429651528_a23ebb0b8c.jpg",
			"http://farm9.staticflickr.com/8288/7525381378_aa2917fa0e.jpg",
			"http://farm6.staticflickr.com/5336/7384863678_5ef87814fe.jpg",
			"http://farm8.staticflickr.com/7102/7179457127_36e1cbaab7.jpg",
			"http://farm8.staticflickr.com/7086/7238812536_1334d78c05.jpg",
			"http://farm8.staticflickr.com/7243/7193236466_33a37765a4.jpg",
			"http://farm8.staticflickr.com/7251/7059629417_e0e96a4c46.jpg",
			"http://farm8.staticflickr.com/7084/6885444694_6272874cfc.jpg",

			"http://img.gazeta.ru/files3/529/6377529/iStock_000002344748_Small-pic510-510x340-14873.jpg",
			"http://img.gazeta.ru/files3/313/6382313/009-pic330-330x220-41940.jpg",
			"http://img.gazeta.ru/files3/853/6381853/AP538306833304-pic510-510x340-38495.jpg",
			"http://img.gazeta.ru/files3/813/6381813/aeroport-pic510-510x340-88648.jpg",
			"http://img.gazeta.ru/files3/73/6377073/upload-5-pic510-510x340-41696.jpg",
			"http://img.gazeta.ru/files3/57/6377057/upload-1-pic4-452x302-19517.jpg",
			"http://img.gazeta.ru/files3/61/6377061/upload-2-pic4-452x302-91813.jpg",
			"http://img.gazeta.ru/files3/65/6377065/upload-3-pic4_zoom-1000x1000-97621.jpg",
			"http://img.gazeta.ru/files3/69/6377069/upload-4-pic4-452x302-39723.jpg",
			"http://img.gazeta.ru/files3/69/6377069/upload-4-pic4_zoom-1000x1000-7852.jpg",
			"http://img.gazeta.ru/files3/241/6381241/upload-12-Monkeys-2511246-pic510-510x340-51798.jpg",
			"http://img.gazeta.ru/files3/233/6381233/upload-12-Monkeys-2511240-pic668-668x444-73526.jpg",
			"http://img.gazeta.ru/files3/237/6381237/upload-12-Monkeys-2511245-pic668-668x444-33738.jpg",
			"http://img.gazeta.ru/files3/229/6381229/upload-12-Monkeys-2511239-pic668-668x444-1182.jpg",
			"http://img.gazeta.ru/files3/713/6381713/upload-01-pic668-668x444-88193.jpg",
			"http://img.gazeta.ru/files3/717/6381717/upload-02-pic668-668x444-21150.jpg",
			"http://img.gazeta.ru/files3/721/6381721/upload-03-pic668-668x444-739.jpg",
			"http://img.gazeta.ru/files3/725/6381725/upload-04-pic4_zoom-1000x1000-98213.jpg",
			"http://img.gazeta.ru/files3/737/6381737/upload-07-pic4_zoom-1000x1000-88694.jpg",
			"http://pixabay.com/static/uploads/photo/2015/01/16/01/27/robonaut-600982_640.jpg",
			"http://pixabay.com/static/uploads/photo/2012/07/06/20/21/cat-51663_640.jpg",
			"http://pixabay.com/static/uploads/photo/2012/07/06/20/23/cat-51665_640.jpg",
			"http://image.shutterstock.com/display_pic_with_logo/61002/158212538/stock-photo-titanium-gears-and-parts-for-aerospace-industry-158212538.jpg",
			"http://pixabay.com/static/uploads/photo/2013/10/08/20/08/gear-192875_640.jpg",
			"http://pixabay.com/static/uploads/photo/2014/02/05/08/19/smoke-258786_640.jpg",
			"http://pixabay.com/static/uploads/photo/2014/06/04/16/58/cigar-362183_640.jpg",
			"http://pixabay.com/static/uploads/photo/2013/05/12/18/55/cigarette-110849_640.jpg",
			"http://pixabay.com/static/uploads/photo/2014/07/08/09/20/barbecue-386602_640.jpg",
			"http://pixabay.com/static/uploads/photo/2014/09/16/17/22/meat-448494_640.jpg"
	};
}