package com.lx.sm.view;

import com.lvxiang.sm.util.BatteryUtil;
import com.lvxiang.sm.util.CallUtil;
import com.lvxiang.sm.util.CreditUtil;
import com.lvxiang.sm.util.MessageUtil;
import com.lvxiang.sm.util.OtherUtil;
import com.lvxiang.sm.util.SDCardUtil;
import com.lx.sm.model.Constants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends MyActivity implements Callback{

	private ListView activities;
	private ImageView mainBoard;
	private TextView  noAct;
	private LinearLayout head_msg;
	private LinearLayout ani_board;
	private ImageView switchBar;
	private Button credits;
	private TextView scoreText;
	
	private boolean animation_complete;
	private Handler animation_handler;
	private Handler init_handler;
	
	// Msg Board viarables
	private float msg_down_x;
	private float msg_down_y;
	private float msg_up_x;
	private float msg_up_y;
	private static final int state_left   = 0x000;
	private static final int state_middle = 0x001;
	private static final int state_right  = 0x002;
	private static final float anim_duration = 0.5f;
	private int state = state_middle;
	private int msg_width;
	private int frame_rate;
	private Animation leftTranslate;
	private Animation rightTranslate;
	private Animation leftTranslate_1;
	private Animation rightTranslate_1;
	private LinearLayout.LayoutParams lp;
	
	///Main board state flags
	private static final int OUT        = 0x001;
	private static final int FLOW       = 0x002;
	private static final int CALLS      = 0x003;
	private static final int SMS        = 0x004;
	private static final int BATTERY    = 0x005;
	private static final int SDCARD   = 0x006;
	private static final int APPS = 0x007;
	private static final int CONTACT  = 0x008;
	private static final int HARDWARE = 0x009;
	private int location;
	private int down_x;
	private int down_y;
	private int up_x;
	private int up_y;
	private int left; // the main board left coordinate
	private int top;  // the main board top  coordinate
	
	// Message Flags
	private static final int REFRESH_PROGRESS_BAR = 0x000;
	private static final int DATA_INIT_COMPLETE   = 0x001;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		initRes();
		initManagers();
		if(firstRun()){
			
		}
		initUI();
		checkForUpdate();
		
	}
	
	private void initRes(){
        animation_handler = new Handler(this);
        init_handler      = new Handler(this);
        
        dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        msg_width = dm.widthPixels;
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;
        
        sp = this.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        
        leftTranslate  = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,-msg_width,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0);
        leftTranslate.setDuration(500);
        leftTranslate.setFillAfter(true);
        
        leftTranslate_1  = new TranslateAnimation(Animation.ABSOLUTE,-msg_width,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0);
        leftTranslate_1.setDuration(500);
        leftTranslate_1.setFillAfter(true);
        
        rightTranslate = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,msg_width,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0);
        rightTranslate.setDuration(500);
        rightTranslate.setFillAfter(true);
        rightTranslate.setAnimationListener(new RightAnimationListener());
        
        rightTranslate_1 = new TranslateAnimation(Animation.ABSOLUTE,msg_width,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0);
        rightTranslate_1.setDuration(500);
        rightTranslate_1.setFillAfter(true);
	}
	
	private void initUI(){
		
		head_msg = (LinearLayout) this.findViewById(R.id.main_head_msg);
		head_msg.setOnTouchListener(new MsgTouchListener());
		
		ani_board = (LinearLayout) this.findViewById(R.id.main_ani_board);
		
		credits = (Button) this.findViewById(R.id.main_enter_credit);
		credits.setOnClickListener(new CreditButtonListener());
		
		mainBoard = (ImageView) this.findViewById(R.id.main_mainboard);
		mainBoard.setOnTouchListener(new BoardTouchListener());
		left += mainBoard.getLeft();
		top  += mainBoard.getTop();
		
		scoreText = (TextView) this.findViewById(R.id.main_msg_middle_score);
		scoreText.setText(100+"");
		
		switchBar = (ImageView) this.findViewById(R.id.main_msg_switchbar);
		
		activities = (ListView) this.findViewById(R.id.main_activities);
		
		if(creditUtil.getCredits().size()==0){
			noAct = (TextView) this.findViewById(R.id.main_no_activity);
			noAct.setVisibility(View.VISIBLE);
		}
		
	}
	
    /**
     * Initialize Database managers
     */
    private void initManagers(){
    	callUtil = new CallUtil(this);
    	callUtil.firstRun();
    	
    	messageUtil = new MessageUtil(this);
    	messageUtil.firstRun();
    	
    	creditUtil =new CreditUtil(this);
    	creditUtil.firstRun();
    	
    	sdcardUtil = new SDCardUtil(this);
    	sdcardUtil.init();
    	// Many other managers to come
    	
    	batteryUtil = new BatteryUtil();
    }
    
    /**
     * Called when this main activity is finished.
     */
    private void finishManagers(){
    	callUtil.finish();
    	messageUtil.finish();
    }
	
    /**
     * Test if this is the first time the program runs
     * @return
     */
    private boolean firstRun(){
    	
    	if(!sp.contains(Constants.KEY_FIRST_RUN)){
    		editor.putInt(Constants.KEY_FIRST_RUN, 1);
    		editor.commit();
    		return true;
    	}
    	return false;
    }
	
    /**
     * Check for available updates
     */
    private void checkForUpdate(){
    	if(OtherUtil.goodNetwork(this))// Check network status
    	{
    		// do something
    	}
    }
	
    private class MsgTouchListener implements OnTouchListener{

		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				msg_down_x = event.getX();
				msg_down_y = event.getY();
				return true;
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				msg_up_x = event.getX();
				msg_up_y = event.getY();
				
				if(msg_up_x - msg_down_x > 10){
					if(state  == state_middle){
						//lp = (LayoutParams) credits.getLayoutParams();
						ani_board.startAnimation(rightTranslate);
						state = state_left;
						switchBar.setImageResource(R.drawable.switch_left);
						
					}
					else if(state == state_right){
						ani_board.startAnimation(leftTranslate_1);
						state = state_middle;
						switchBar.setImageResource(R.drawable.switch_middle);
					}
				}
				else if(msg_up_x - msg_down_x < -10){
					if(state  == state_middle){
						ani_board.startAnimation(leftTranslate);
						state = state_right;
						switchBar.setImageResource(R.drawable.switch_right);
					}
					else if(state == state_left){
						ani_board.startAnimation(rightTranslate_1);
						state = state_middle;
						switchBar.setImageResource(R.drawable.switch_middle);
					}
				}
				else{
					// If this is a click, test if it falls in the button scope
					if(state == state_left){
						if(msg_down_x>=credits.getLeft()&&msg_down_x<=credits.getRight()&&msg_down_y>=credits.getTop()&&msg_down_y<=credits.getBottom()){
							startActivity(new Intent(MainActivity.this,CreditActivity.class));
						}
					}
				}
				//Toast.makeText(MainActivity.this, msg_width+"", Toast.LENGTH_LONG).show();
				return true;
			}
			return false;
		}
    	
    }
    
    private class RightAnimationListener implements AnimationListener{

		public void onAnimationEnd(Animation arg0) {
			// TODO Auto-generated method stub
			/*
			lp.setMargins(lp.leftMargin+screen_width, 
						  lp.topMargin, 
						  lp.rightMargin+screen_width, 
						  lp.bottomMargin);
			credits.requestLayout();
			*/
		}

		public void onAnimationRepeat(Animation arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onAnimationStart(Animation arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
    private class LeftAnimationListener implements AnimationListener{

		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
    private class CreditButtonListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(new Intent(MainActivity.this,CreditActivity.class));
		}
    	
    }
    
    
	private class BoardTouchListener implements OnTouchListener{

		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				
				down_x = (int) (event.getX() - left);
				down_y = (int) (event.getY() - top);
				//Toast.makeText(StatisticMasterActivity.this, "("+down_x+","+down_y+")", Toast.LENGTH_SHORT).show();
				
				location=detectLocation();
				
				// 返回值代表事件是否被使用
				return true;
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				
				up_x = (int) (event.getX() - left);
				up_y = (int) (event.getY() - top);
				//Toast.makeText(StatisticMasterActivity.this, "("+up_x+","+up_y+")", Toast.LENGTH_SHORT).show();
				
				// now that we get a down-up pare, we resolve the
				// duration and distances between them to see if it's
				// a click operation
				if(isClick()){
					switch(location){
					case FLOW:{
						break;
					}
					case CALLS:{
						startActivity(new Intent(MainActivity.this,CallStatsActivity.class));
						break;
					}
					case SMS:{
						break;
						
					}
					case BATTERY:{
						startActivity(new Intent(MainActivity.this,BatteryActivity.class));
						break;
					}
					case SDCARD:{
						if(sdcardUtil.sdMount())
							startActivity(new Intent(MainActivity.this,SDCardActivity.class));
						else
							Toast.makeText(MainActivity.this, "无法检测到sd卡", Toast.LENGTH_SHORT).show();
						break;
					}
					case APPS:{
						break;
					}
					case HARDWARE:{
						break;
					}
					case CONTACT:{
						break;
					}
					
					
				}
				
			}
			return true;
			}
			return false;
		}
	}
	
    /**
     * Detect which part of the main board user is clicking on<br>
     * use line equations to find out whether the point is above<br>
     * or below the given line.
     * @return Flags that represent location of the point
     */
    private int detectLocation(){
    	int x = down_x/120;
    	int y = down_y/64;
    	if(x==0&&y==0){
    		return FLOW;
    	}
    	else if(x==1&&y==0){
    		return CALLS;
    	}
    	else if(x==2&&y==0){
    		return SMS;
    	}
    	else if(x==3&&y==0){
    		return BATTERY;
    	}
    	else if(x==0&&y==1){
    		return SDCARD;
    	}
    	else if(x==1&&y==1){
    		return APPS;
    	}
    	else if(x==2&&y==1){
    		return CONTACT;
    	}
    	else if(x==3&&y==1){
    		return HARDWARE;
    	}
    	return OUT;
    }
	
    /**
     * Test if the current action is a click. It is if distance between<br>
     * the down point and up point is less than 10 pixels.
     * @return <code>true</code> if is a click<br>
     * 		   <code>false</code> if not
     */
    private boolean isClick(){
    	float dX = up_x - down_x;
    	float dY = up_y - down_y;
    	double d = Math.pow(dX*dX+dY*dY, 0.5d);
    	if(d>10){
    		return false;
    	}
    	return true;
    }
    
    private class DataInitThread extends Thread{
    	
    	public DataInitThread(int doWhat){
    		
    	}
    	
    	@Override
    	public void run(){
    		Looper.prepare();
    		
    		//Check calls
    		
    		
    		//Check messages
    		
    		//Check SD card
    		
    		//Check Internet Usage
    		
    		//Check Hardware
    		
    		//Check Battery
    		
    		//Check Applications
    		
    		//Check it out
    	}
    }
    
    private class ProgressBarAnimation extends Thread{
    	
    	private int from;
    	private int to;
    	
    	public ProgressBarAnimation(int from, int to){
    		this.from = from;
    		this.to   = to;
    	}
    	
    	@Override
    	public void run(){
    		Looper.prepare();
    		animation_complete = false;
    		for(int i=from+1;i<=to;i++){
    			
    			try {
    				animation_handler.sendEmptyMessage(REFRESH_PROGRESS_BAR);
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		animation_complete = true;
    	}
    }
    
    public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		switch(arg0.what){
		case REFRESH_PROGRESS_BAR:{
			//FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(bar.getWidth()+1,120);
			//bar.setLayoutParams(lp);
			break;
		}
		case DATA_INIT_COMPLETE:{
			break;
		}
		}
		return false;
	}
    
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	@Override
	public void onStop(){
		super.onStop();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		finishManagers();
	}

}
