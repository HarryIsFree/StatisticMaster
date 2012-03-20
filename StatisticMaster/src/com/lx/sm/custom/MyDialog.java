package com.lx.sm.custom;

import com.lx.sm.view.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class MyDialog extends Dialog{

	private Context context;
	
	public MyDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//this.setContentView(R.layout.my_dialog);
	}

}
