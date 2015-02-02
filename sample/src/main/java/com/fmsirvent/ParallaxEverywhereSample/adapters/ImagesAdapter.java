package com.fmsirvent.ParallaxEverywhereSample.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmsirvent.ParallaxEverywhereSample.R;
import com.fmsirvent.ParallaxEverywhereSample.model.CustomData;
import com.fmsirvent.ParallaxEverywhereSample.view.ParallaxStaggeredImagesGridLayout;
import com.fmsirvent.ParallaxEverywhereSample.view.ParallaxedImageView;
import com.fmsirvent.ParallaxEverywhereSample.view.ParallaxedImageView2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toker-rg on 03.02.15.
 * Adapter for feed with images
 */

public class ImagesAdapter extends BaseAdapter {

    List<CustomData> mDataList;
    LayoutInflater mInflater;
    Context mContext;

    public ImagesAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mDataList = new ArrayList<>();
        mContext = context;

        setData(createDumbData());
    }

    public void setData(List<CustomData> data) {
        mDataList.addAll(data);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem3, null);

            viewHolder = new ViewHolder();
            viewHolder.parallaxedImage = (ParallaxedImageView2)convertView.
                    findViewById(R.id.image_piv2);
            viewHolder.text = (TextView)convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CustomData dataEntry = mDataList.get(position);
        viewHolder.text.setText(dataEntry.text);
		viewHolder.parallaxedImage.setImageBitmap(dataEntry.getBitmap());
        viewHolder.parallaxedImage.setTag(dataEntry.text);

        return convertView;
    }

    public static class ViewHolder {
        ParallaxedImageView2 parallaxedImage;
        TextView text;
    }


    private ArrayList<CustomData> createDumbData() {
        ArrayList<CustomData> data = new ArrayList<>();

        Resources resources = mContext.getResources();
        Bitmap lbitmap = BitmapFactory.decodeResource(resources, R.drawable.alicante_luceros);
        Bitmap lbitmap1 = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
        Bitmap lbitmap2 = BitmapFactory.decodeResource(resources, R.drawable.img_2);
        Bitmap lbitmap3 = BitmapFactory.decodeResource(resources, R.drawable.img_3);
        Bitmap lbitmap4 = BitmapFactory.decodeResource(resources, R.drawable.img_4);
        Bitmap lbitmap5 = BitmapFactory.decodeResource(resources, R.drawable.img_6);
        Bitmap lbitmap6 = BitmapFactory.decodeResource(resources, R.drawable.img_0);
        Bitmap lbitmap7 = BitmapFactory.decodeResource(resources, R.drawable.img_saxon);
		/*
		Bitmap lbitmap6 = BitmapFactory.decodeResource(getResources(), R.drawable.img_6);
		Bitmap lbitmap7 = BitmapFactory.decodeResource(getResources(), R.drawable.img_7);
		Bitmap lbitmap8 = BitmapFactory.decodeResource(getResources(), R.drawable.img_8);
		Bitmap lbitmap9 = BitmapFactory.decodeResource(getResources(), R.drawable.img_9);*/

        data.add(new CustomData("test_0", lbitmap3));
        data.add(new CustomData("test_1", lbitmap));
        data.add(new CustomData("test_2", lbitmap1));
        data.add(new CustomData("test_3", lbitmap7));
        data.add(new CustomData("test_4", lbitmap6));
        data.add(new CustomData("test_5", lbitmap));
        data.add(new CustomData("test_6", lbitmap5));
        data.add(new CustomData("test_7", lbitmap));
        data.add(new CustomData("test_8", lbitmap4));
        data.add(new CustomData("test_9", lbitmap));
        data.add(new CustomData("test_10", lbitmap1));
        data.add(new CustomData("test_11", lbitmap4));
        data.add(new CustomData("test_12", lbitmap));
        data.add(new CustomData("test_13", lbitmap2));
        data.add(new CustomData("test_14", lbitmap6));
        data.add(new CustomData("test_15", lbitmap5));
        data.add(new CustomData("test_16", lbitmap));
        data.add(new CustomData("test_17", lbitmap5));
        data.add(new CustomData("test_18", lbitmap3));
        data.add(new CustomData("test_19", lbitmap1));

        return data;
    }
}