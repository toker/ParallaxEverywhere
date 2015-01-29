package com.fmsirvent.ParallaxEverywhereSample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fmsirvent.ParallaxEverywhereSample.model.CustomData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toker-rg on 27.01.15.
 */

public class ParallaxListViewActivity extends Activity {

	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parallax_listview_activity);

		mListView = (ListView) findViewById(R.id.parallax_lv);

		ListAdapter adapter = new ListAdapter(ParallaxListViewActivity.this);
		adapter.setData(createDumbData());
		mListView.setAdapter(adapter);
	}

	/**
	 * Adapter
	 */
	public class ListAdapter extends BaseAdapter {

		List<CustomData> mDataList;
		LayoutInflater mInflater;

		public ListAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
			mDataList = new ArrayList<>();
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
				convertView = mInflater.inflate(R.layout.listitem, null);

				viewHolder = new ViewHolder();
				viewHolder.image = (ImageView)convertView.findViewById(R.id.image);
				viewHolder.text = (TextView)convertView.findViewById(R.id.text);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			CustomData dataEntry = mDataList.get(position);
			viewHolder.text.setText(dataEntry.text);
			viewHolder.image.setImageBitmap(dataEntry.getBitmap());

			return convertView;
		}
	}

	public static class ViewHolder {
		ImageView image;
		TextView text;
	}

	private ArrayList<CustomData> createDumbData() {
		ArrayList<CustomData> data = new ArrayList<>();

		Bitmap lbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.alicante_luceros);
		Bitmap lbitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.img_0);
		Bitmap lbitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.img_2);
		Bitmap lbitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.img_3);
		Bitmap lbitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.img_4);
		/*Bitmap lbitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.img_5);
		Bitmap lbitmap6 = BitmapFactory.decodeResource(getResources(), R.drawable.img_6);
		Bitmap lbitmap7 = BitmapFactory.decodeResource(getResources(), R.drawable.img_7);
		Bitmap lbitmap8 = BitmapFactory.decodeResource(getResources(), R.drawable.img_8);
		Bitmap lbitmap9 = BitmapFactory.decodeResource(getResources(), R.drawable.img_9);*/

		data.add(new CustomData("test_0", lbitmap3));
		data.add(new CustomData("test_1", lbitmap));
		data.add(new CustomData("test_2", lbitmap));
		data.add(new CustomData("test_3", lbitmap1));
		data.add(new CustomData("test_4", lbitmap));
		data.add(new CustomData("test_5", lbitmap));
		data.add(new CustomData("test_6", lbitmap));
		data.add(new CustomData("test_7", lbitmap));
		data.add(new CustomData("test_8", lbitmap));
		data.add(new CustomData("test_9", lbitmap));
		data.add(new CustomData("test_10", lbitmap1));
		data.add(new CustomData("test_11", lbitmap3));
		data.add(new CustomData("test_12", lbitmap));
		data.add(new CustomData("test_13", lbitmap2));
		data.add(new CustomData("test_14", lbitmap));
		data.add(new CustomData("test_15", lbitmap));
		data.add(new CustomData("test_16", lbitmap));
		data.add(new CustomData("test_17", lbitmap));
		data.add(new CustomData("test_18", lbitmap));

		return data;
	}
}
