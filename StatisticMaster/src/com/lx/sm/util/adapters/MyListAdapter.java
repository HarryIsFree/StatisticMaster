package com.lx.sm.util.adapters;

import java.util.ArrayList;

import com.lx.sm.model.DataItem;
import com.lx.sm.view.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter{
	
	private ArrayList<DataItem> key_values;
	private Context context;
	private LayoutInflater inflater;
	
	private static final int w = 391;
	private static final int h = 111;
	
	private static Paint key_paint;
	private static Paint value_paint;
	private static Paint unit_paint;
	
	public MyListAdapter(ArrayList<DataItem> map,Context context){
		this.key_values = map;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		
		key_paint = new Paint();
		key_paint.setTextSize(25);
		key_paint.setColor(Color.BLACK);
		
		value_paint = new Paint();
		value_paint.setTextSize(40);
		value_paint.setColor(Color.rgb(252, 38, 41));
		
		unit_paint = new Paint();
		unit_paint.setTextSize(13);
		unit_paint.setColor(Color.BLACK);
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		if(key_values!=null){
			return key_values.size();
		}
		
		return 0;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return key_values.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		if(convertView == null){
			if(position%2==0)
				convertView = inflater.inflate(R.layout.item_light, null);
			else 
				convertView = inflater.inflate(R.layout.item_dark, null);
			holder = new ViewHolder();
			holder.key   = (TextView)convertView.findViewById(R.id.item_key);
			holder.value = (TextView)convertView.findViewById(R.id.item_value);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//holder.iv.setImageBitmap(getBitmap(key_values.get(position)));
		holder.key.setText(key_values.get(position).key);
		holder.value.setText(key_values.get(position).value+key_values.get(position).unit);
		
		return convertView;
	}
	
	private class ViewHolder{
		public TextView key;
		public TextView value;
	}
	
	/*
	private Bitmap getBitmap(DataItem item){
		Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c  = new Canvas(bm);
		
		c.drawText(item.key, 20, 30, key_paint);
		int x = 180;
		int y = 35;
		for(int i = 0;i<item.value.length();i++){
			char ch = item.value.charAt(i);
			if(ch>='0'&&ch<='9'){
				c.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.num_0+ch-'0'), x, y, null);
				x += 41;
			}
			else if(ch=='.'){
				c.drawText(".", x, y+40, value_paint);
				x+=25;
			}
			else
			{
				c.drawText(ch+"", x, y, value_paint);
				x += 25;
			}
		}
		if(item.unit!=null){
			c.drawText(item.unit, x, y, unit_paint);
		}
		
		return bm;
	}
	*/
}
