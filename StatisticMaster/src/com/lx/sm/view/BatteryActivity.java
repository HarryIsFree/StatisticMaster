package com.lx.sm.view;

import com.lx.sm.model.Constants;

import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class BatteryActivity extends MyActivity implements Callback{

	private ImageView imageView;
	private TextView  tv1;
	private TextView  tv2;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.battery);
		
		initUI();
		batteryUtil.registerReceiver(this);
		batteryUtil.setHandler(new Handler(this));
	}
	
	private void initUI(){
		
		tv1 = (TextView) this.findViewById(R.id.battery_percent);
		tv2 = (TextView) this.findViewById(R.id.battery_status);
		
	}
	
	
	private void setBatteryHeight(){
		
		FrameLayout.LayoutParams lp = (LayoutParams) imageView.getLayoutParams();
		
		imageView.requestLayout();
	}


	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		Bundle bundle = msg.getData();
		if(bundle.getInt(Constants.MSG_TYPE)==Constants.MSG_BATTERY_CHANGE){
			int status  = bundle.getInt(BatteryManager.EXTRA_STATUS);
			int level   = bundle.getInt(BatteryManager.EXTRA_LEVEL);
			int plugged = bundle.getInt(BatteryManager.EXTRA_PLUGGED);
			int health  = bundle.getInt(BatteryManager.EXTRA_HEALTH);
			int voltage = bundle.getInt(BatteryManager.EXTRA_VOLTAGE);
			String technology = bundle.getString(BatteryManager.EXTRA_TECHNOLOGY);
			
			tv1.setText("电池电量："+level+"%");
			
			String statusStr = "电池状态：";
			switch(status){
			case BatteryManager.BATTERY_STATUS_CHARGING:{
				switch(plugged){
				case BatteryManager.BATTERY_PLUGGED_AC:{
					statusStr += "正在使用电源充电";
				}
				case BatteryManager.BATTERY_PLUGGED_USB:{
					statusStr += "正在使用USB充电";
				}
				}
				break;
			}
			case BatteryManager.BATTERY_STATUS_DISCHARGING:{
				statusStr += "电池正在放电";
				break;
			}
			case BatteryManager.BATTERY_STATUS_FULL:{
				statusStr += "电量已满";
				break;
			}
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:{
				statusStr += "非充电状态";
				break;
			}
			case BatteryManager.BATTERY_STATUS_UNKNOWN:{
				statusStr += "未知";
				break;
			}
			}
			
			tv2.setText(statusStr);
			
			return true;
		}
		return false;
	}
	
	public void onPause(){
		super.onPause();
		batteryUtil.unregisterReceiver(this);
	}
	
	public void onResume(){
		super.onResume();
		batteryUtil.registerReceiver(this);
	}
	
	
}
