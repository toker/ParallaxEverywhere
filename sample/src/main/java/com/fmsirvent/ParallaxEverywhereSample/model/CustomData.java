package com.fmsirvent.ParallaxEverywhereSample.model;

import android.graphics.Bitmap;

import java.util.Random;

/**
 * Created by toker-rg on 27.01.15.
 */
public class CustomData {
	public String text;
	public Bitmap bitmap;
	public int type;

	private final int NUMBER_OF_TYPES = 2;
	private Random random = new Random();

	public CustomData(String text, Bitmap bitmap) {
		this.text = text;
		this.bitmap = bitmap;
		this.type = random.nextInt(NUMBER_OF_TYPES);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public int getType() {
		return type;
	}
}