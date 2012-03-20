package com.lx.sm.util.adapters;

import java.util.ArrayList;

import com.lx.sm.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CreditListAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private ArrayList<View> views;
	
	public CreditListAdapter(Context c){
		inflater = LayoutInflater.from(c);
		views = new ArrayList<View>();
		
		for(int i =0;i<7;i++){
			View v = inflater.inflate(R.layout.credit_list_item	, null);
			ImageView iv = (ImageView) v.findViewById(R.id.credit_item_bg);
			if(i%2 == 0){
				iv.setBackgroundResource(R.drawable.credits_light_bg);
			}
			else{
				iv.setBackgroundResource(R.drawable.credits_dart_bg);
			}
			views.add(v);
		}
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return 7;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return views.get(arg0);
	}

}
