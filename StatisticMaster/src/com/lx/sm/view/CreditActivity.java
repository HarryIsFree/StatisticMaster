package com.lx.sm.view;

import com.lx.sm.util.adapters.CreditListAdapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

public class CreditActivity extends Activity{

	private ListView credits;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credits_layout);
		
		credits = (ListView) this.findViewById(R.id.credit_list);
		credits.setAdapter(new CreditListAdapter(this));
		credits.setCacheColorHint(Color.TRANSPARENT);
	}
	
}
