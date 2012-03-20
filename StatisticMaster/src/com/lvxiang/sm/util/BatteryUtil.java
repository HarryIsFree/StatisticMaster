package com.lvxiang.sm.util;

import com.lx.sm.model.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BatteryUtil {

	private Handler handler;
	
	private BroadcastReceiver batteryReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
				
				int status = intent.getIntExtra("status", 0);
                int health = intent.getIntExtra("health", 1);
                boolean present = intent.getBooleanExtra("present", false);
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 0);  
                int plugged = intent.getIntExtra("plugged", 0);  
                int voltage = intent.getIntExtra("voltage", 0);  
                int temperature = intent.getIntExtra("temperature", 0);  
                String technology = intent.getStringExtra("technology"); 
                
                if(handler!=null){
                	Message msg = new Message();
                	Bundle bundle = new Bundle();
                	bundle.putInt(Constants.MSG_TYPE, Constants.MSG_BATTERY_CHANGE); 
                	bundle.putInt(BatteryManager.EXTRA_LEVEL, level);
                	bundle.putInt(BatteryManager.EXTRA_STATUS, status);
                	bundle.putInt(BatteryManager.EXTRA_PLUGGED, plugged);
                	bundle.putInt(BatteryManager.EXTRA_HEALTH, health);
                	bundle.putInt(BatteryManager.EXTRA_TEMPERATURE, temperature);
                	bundle.putInt(BatteryManager.EXTRA_VOLTAGE, voltage);
                	bundle.putString(BatteryManager.EXTRA_TECHNOLOGY, technology);
                	bundle.putInt(BatteryManager.EXTRA_SCALE, scale);
                	msg.setData(bundle);
                	
                	
                	handler.sendMessage(msg);
                }
                
			}
		}
		
	};
	
	public BatteryUtil(){
		
	}
	
	public void setHandler(Handler handler){
		this.handler = handler;
	}
	
	public void registerReceiver(Context c){
		c.registerReceiver(this.batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}
	
	public void unregisterReceiver(Context c){
		c.unregisterReceiver(batteryReceiver);
	}
	
}
