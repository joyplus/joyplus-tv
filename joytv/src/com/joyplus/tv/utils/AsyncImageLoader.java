package com.joyplus.tv.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.joyplus.tv.Constant;

public class AsyncImageLoader {
	
	private static Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	private static final int nThreadPoolSize = 20;
	private static ExecutorService mExecutorService = Executors.newFixedThreadPool(nThreadPoolSize);
	private OnImageLoadListener mImageLoadListener;
	private Handler mHandler;
	
	public AsyncImageLoader(OnImageLoadListener l){
		mImageLoadListener = l;
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 0:
					String imageUrl = msg.getData().getString("imageUrl");
					int position = msg.getData().getInt("position");
					mImageLoadListener.ImageLoadFinished((Bitmap)msg.obj, imageUrl, position);
					break;
					
				default:
					break;
				}
			}
		};
	}

	public void loadBitmap(String imageUrl, final int position){
		final String imgUrl = imageUrl;

		mExecutorService.execute(new Runnable(){
			@Override
			public void run() {
				Bitmap bitmap = loadBitmapFromCache(imgUrl);
				if(bitmap == null){
					bitmap = loadBitmapFromLocal(imgUrl);
					if(bitmap == null){
						bitmap = loadBitmapFromUrl(imgUrl);
						if(bitmap == null){
							imageCache.put(imgUrl, null);
							return;
						}
					}
				}
				
				Message msg = mHandler.obtainMessage(0, bitmap);
				Bundle bundle = new Bundle();
				bundle.putString("imageUrl", imgUrl);
				bundle.putInt("position", position);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
				return;
			}
		});
	}
	
	private Bitmap loadBitmapFromUrl(String imageUrl){
		URL m;
		InputStream i = null;
		try {
			m = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) m.openConnection();
			connection.setDoInput(true);
			connection.connect();
			i =  connection.getInputStream();
		
//		Bitmap bm = BitmapFactory.decodeStream(i);
		
			 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		     byte[] b = new byte[1024];
		     int len = 0;
		     while ((len = i.read(b, 0, 1024)) != -1){
			   baos.write(b, 0, len);
			   baos.flush();
		     }
		     byte[] bytes = baos.toByteArray();
		     Bitmap bm=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
			
			if(bm != null){
				saveBitmap(bm, getFilenameFromUrl(imageUrl));
				if(!imageCache.containsKey(imageUrl)){
					imageCache.put(imageUrl, new SoftReference<Bitmap>(bm));
				}
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return loadBitmapFromCache(imageUrl);
	}
	
	private Bitmap loadBitmapFromLocal(String imageUrl){

		String filename = getFilenameFromUrl(imageUrl);
		File dir = new File(Constant.PATH_BIG_IMAGE);
		if(dir.exists()){
			File filepath = new File(dir, filename);
			Bitmap bm = BitmapFactory.decodeFile(filepath.getAbsolutePath());
			if(bm != null){
				if(!imageCache.containsKey(imageUrl)){
					imageCache.put(imageUrl, new SoftReference<Bitmap>(bm));
				}
			}
		}
		return loadBitmapFromCache(imageUrl);
	}
	
	public Bitmap loadBitmapFromCache(String imageUrl){
		Bitmap bm = null;
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			if(softReference != null){
				bm = softReference.get();
				if(bm == null){
					imageCache.remove(imageUrl);
				}
			}
		}

		return bm;
	}
	
	
	
	public static String getFilenameFromUrl(String imageUrl){
		String [] strs = imageUrl.split("/");
		String filename = strs[strs.length - 1];
		if(filename.contains(".")){
			filename = filename.substring(0, filename.lastIndexOf("."));
		}
		return filename;
	}
	
	public static void saveBitmap(Bitmap bitmap, String filename){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File dir = new File(Constant.PATH_BIG_IMAGE);
			if(!dir.exists()){
				dir.mkdirs();
			}
			
			File file = new File(dir, filename);
			FileOutputStream fos;
			if(!file.exists()){
				try {
					file.createNewFile();
					fos = new FileOutputStream(file);
					if(bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos)){
						fos.flush();
                    }
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
