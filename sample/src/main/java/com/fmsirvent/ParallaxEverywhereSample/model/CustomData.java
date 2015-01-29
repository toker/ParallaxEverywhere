package com.fmsirvent.ParallaxEverywhereSample.model;

import android.graphics.Bitmap;

/**
 * Created by toker-rg on 27.01.15.
 */
public class CustomData {
	public String text;
	public Bitmap bitmap;

	public CustomData(String text, Bitmap bitmap) {
		this.text = text;
		this.bitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}
}