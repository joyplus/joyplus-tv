package com.joyplus.tv.utils;

import android.graphics.Bitmap;

public interface OnImageLoadListener{
	public void ImageLoadFinished(Bitmap bitmap, String imageUrl, int position);
}