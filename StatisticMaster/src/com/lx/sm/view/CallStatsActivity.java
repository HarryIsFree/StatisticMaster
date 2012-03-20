package com.lx.sm.view;

import com.lvxiang.sm.util.CallUtil;
import com.lx.sm.model.Constants;
import com.lx.sm.util.adapters.MyListAdapter;

import android.app.Activity;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class CallStatsActivity extends MyActivity implements Callback{

	private ListView monthList;
	private ListView allList;
	private Button credit;
	private Button share;
	private Button settings;
	private Button diagram;
	private Button exit;
	private LinearLayout head_month;
	private LinearLayout head_all;
	private LinearLayout bottom_bar;
	private MyListAdapter monthAdp;
	private MyListAdapter allAdp;
	
	private static final int STATE_UP   = 0;
	private static final int STATE_DOWN = 1;
	private int state = STATE_UP;
	
	private Animation downAnimation;
	private Animation upAnimation;
	private int distance;
	private static final int bottom_height = 66;
	private static final int item_height    = 49;
	
	boolean monthExpanded = false;
	boolean allExpanded   = false;
	
	private Handler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_msg_layout);
	
		init();
	}

	private void init(){
		head_month = (LinearLayout) this.findViewById(R.id.call_msg_month);
		head_all   = (LinearLayout) this.findViewById(R.id.call_msg_all);
		
		credit = (Button) this.findViewById(R.id.call_msg_credit_button);
		share  = (Button) this.findViewById(R.id.call_msg_share_button);
		diagram = (Button) this.findViewById(R.id.call_msg_diagram_button);
		exit = (Button) this.findViewById(R.id.call_msg_exit_button);
		
		monthList = (ListView) this.findViewById(R.id.call_msg_list_month);
		monthList.setCacheColorHint(Color.TRANSPARENT);
		allList = (ListView) this.findViewById(R.id.call_msg_list_all);
		allList.setCacheColorHint(Color.TRANSPARENT);
		allAdp = new MyListAdapter(callUtil.getAllStats(this),this);
		monthAdp   = new MyListAdapter(callUtil.getMonthStats(this),this);
		monthList.setAdapter(monthAdp);
		allList.setAdapter(allAdp);
		
		bottom_bar = (LinearLayout) this.findViewById(R.id.call_msg_bottom);
		
		handler = new Handler(this);
		
		init_events();
		
	}
	
	private void initAnimation(){
		distance = bottom_bar.getTop() - head_all.getTop() - item_height;
		downAnimation = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,distance/2);
		downAnimation.setDuration(500);
		downAnimation.setFillAfter(true);
		upAnimation   = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,distance,Animation.ABSOLUTE,0);
		upAnimation.setFillAfter(true);
		upAnimation.setDuration(500);
	}

	private void init_events(){
		
		head_month.setOnClickListener(new MyMonthOnClickListener());
		
		head_all.setOnClickListener(new MyAllOnClickListener());
		
	}
	
	private class MyMonthOnClickListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(downAnimation == null||upAnimation == null){
				initAnimation();
			}
			// TODO Auto-generated method stub
			if(state == STATE_UP){
				allList.setLayoutParams(new LinearLayout.LayoutParams(screen_width,0));
				monthList.setLayoutParams(new LinearLayout.LayoutParams(screen_width,distance));
				state = STATE_DOWN;
			}
			else{
				monthList.setLayoutParams(new LinearLayout.LayoutParams(screen_width,0));
				state = STATE_UP; 
			}
		}
		
	}
	
	private class MyAllOnClickListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(downAnimation == null||upAnimation == null){
				initAnimation();
			}

			if(state == STATE_UP){
				if(!allExpanded){
					allList.setLayoutParams(new LinearLayout.LayoutParams(screen_width,distance));
					allExpanded = true;
				}
				else{
					allList.setLayoutParams(new LinearLayout.LayoutParams(screen_width,0));
					allExpanded = false;
				}
			}
			else{
				monthList.setLayoutParams(new LinearLayout.LayoutParams(screen_width,0));
				allList.setLayoutParams(new LinearLayout.LayoutParams(screen_width,distance));
				state = STATE_UP; 
			}
		}
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		callUtil.stop();
	}
	
	@Override
	public void onStop(){
		super.onStop();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		callUtil.setActHandler(handler);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		callUtil.stop();
	}

	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		
		return false;
	}

}
