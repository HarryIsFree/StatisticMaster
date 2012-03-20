package com.lx.sm.util.adapters;

import com.lx.sm.model.Constants;
import com.lx.sm.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SDListAdapter extends BaseAdapter{

	private int [] nums = new int[Constants.NUM_FORMATS];
	private LayoutInflater inflater;
	private Context c;
	
	public SDListAdapter(int[] nums,Context context){
		this.nums = nums;
		inflater = LayoutInflater.from(context);
		c = context;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return nums.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return nums[arg0];
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(arg1 == null){
			arg1 = inflater.inflate(R.layout.sd_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.bg = (ImageView) arg1.findViewById(R.id.sd_bg);
			viewHolder.im = (ImageView) arg1.findViewById(R.id.sd_item_img);
			viewHolder.title = (TextView) arg1.findViewById(R.id.sd_item_title);
			viewHolder.num = (TextView) arg1.findViewById(R.id.sd_item_num);
			arg1.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder) arg1.getTag();
		}
		if(arg0%2==0){
			viewHolder.bg.setBackgroundResource(R.drawable.credits_light_bg);
		}
		else{
			viewHolder.bg.setBackgroundResource(R.drawable.credits_dart_bg);
		}
		switch(arg0){
		case Constants.INDEX_MP3:{
			viewHolder.im.setImageResource(R.drawable.mp3);
			viewHolder.title.setText("MP3"+c.getResources().getString(R.string.audio));
			viewHolder.num.setText(nums[Constants.INDEX_MP3]+"");
			break;
		}
		case Constants.INDEX_MP4:{
			viewHolder.im.setImageResource(R.drawable.mpg);
			viewHolder.title.setText("MP4"+c.getResources().getString(R.string.video));
			viewHolder.num.setText(nums[Constants.INDEX_MP4]+"");
			break;
		}
		case Constants.INDEX_TXT:{
			viewHolder.im.setImageResource(R.drawable.txt);
			viewHolder.title.setText("TXT"+c.getResources().getString(R.string.document));
			viewHolder.num.setText(nums[Constants.INDEX_TXT]+"");
			break;
		}
		case Constants.INDEX_PDF:{
			viewHolder.im.setImageResource(R.drawable.pdf);
			viewHolder.title.setText("PDF"+c.getResources().getString(R.string.e_book));
			viewHolder.num.setText(nums[Constants.INDEX_PDF]+"");
			break;
		}
		case Constants.INDEX_AVI:{
			viewHolder.im.setImageResource(R.drawable.avi);
			viewHolder.title.setText("AVI"+c.getResources().getString(R.string.video));
			viewHolder.num.setText(nums[Constants.INDEX_AVI]+"");
			break;
		}
		case Constants.INDEX_DOC:{
			viewHolder.im.setImageResource(R.drawable.doc);
			viewHolder.title.setText("DOC"+c.getResources().getString(R.string.document));
			viewHolder.num.setText(nums[Constants.INDEX_DOC]+"");
			break;
		}
		case Constants.INDEX_MPG:{
			viewHolder.im.setImageResource(R.drawable.mpg);
			viewHolder.title.setText("MPG"+c.getResources().getString(R.string.video));
			viewHolder.num.setText(nums[Constants.INDEX_MPG]+"");
			break;
		}
		case Constants.INDEX_PPT:{
			viewHolder.im.setImageResource(R.drawable.ppt);
			viewHolder.title.setText("PPT"+c.getResources().getString(R.string.document));
			viewHolder.num.setText(nums[Constants.INDEX_PPT]+"");
			break;
		}
		case Constants.INDEX_RMVB:{
			viewHolder.im.setImageResource(R.drawable.rmvb);
			viewHolder.title.setText("RMVB"+c.getResources().getString(R.string.video));
			viewHolder.num.setText(nums[Constants.INDEX_RMVB]+"");
			break;
		}
		case Constants.INDEX_JPG:{
			viewHolder.im.setImageResource(R.drawable.jpg);
			viewHolder.title.setText("JPG"+c.getResources().getString(R.string.pictrue));
			viewHolder.num.setText(nums[Constants.INDEX_JPG]+"");
			break;
		}
		case Constants.INDEX_OTHER:{
			viewHolder.im.setImageResource(R.drawable.avi);
			viewHolder.title.setText(c.getResources().getString(R.string.other));
			viewHolder.num.setText(nums[Constants.INDEX_OTHER]+"");
			break;
		}
		
		}

		return arg1;
	}
	
	private class ViewHolder{
		public ImageView bg;
		public ImageView im;
		public TextView  title;
		public TextView  num;
	}

}
