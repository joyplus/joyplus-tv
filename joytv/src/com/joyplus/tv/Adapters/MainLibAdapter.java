package com.joyplus.tv.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joyplus.tv.R;

public class MainLibAdapter extends BaseAdapter {

	private int[] data;
	private Context c;
	private android.widget.Gallery.LayoutParams layoutParam;
	private int displayWith;
	
	public MainLibAdapter(Context c, int[] data) {
		// TODO Auto-generated constructor stub
		this.c = c;
		this.data = data;
		displayWith = ((Activity)c).getWindowManager().getDefaultDisplay().getWidth();
		layoutParam = new android.widget.Gallery.LayoutParams((displayWith-40)/6,2*displayWith/9);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(c).inflate(R.layout.item_layout_gallery, null);
			holder = new ViewHolder();
			holder.firstTitle = (TextView) convertView.findViewById(R.id.first_title);
			holder.secondTitle = (TextView) convertView.findViewById(R.id.second_title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.score = (TextView) convertView.findViewById(R.id.score);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.definition = (ImageView) convertView.findViewById(R.id.definition);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.bottom_frame);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.firstTitle.setVisibility(View.GONE);
		holder.layout.setVisibility(View.GONE);
		holder.definition.setVisibility(View.GONE);
		holder.image.setImageResource(data[position]);
		convertView.setPadding(15, 10, 15, 10);
		convertView.setLayoutParams(layoutParam);
		return convertView;
	}
	
	class ViewHolder{
		TextView firstTitle;
		TextView secondTitle;
		TextView content;
		TextView score;
		ImageView image;
		ImageView definition;
		LinearLayout layout;
	}

}
