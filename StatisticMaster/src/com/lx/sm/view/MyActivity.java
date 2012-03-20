package com.lx.sm.view;

import com.lvxiang.sm.util.BatteryUtil;
import com.lvxiang.sm.util.CallUtil;
import com.lvxiang.sm.util.CreditUtil;
import com.lvxiang.sm.util.MessageUtil;
import com.lvxiang.sm.util.SDCardUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

public abstract class MyActivity extends Activity{
	
	public static CallUtil callUtil;
	public static MessageUtil messageUtil;
	public static CreditUtil creditUtil;
	public static SDCardUtil sdcardUtil;
	public static BatteryUtil batteryUtil;
	
	public static DisplayMetrics dm;
	public static SharedPreferences sp;
	public static SharedPreferences.Editor editor;
	
	public static int screen_width;
	public static int screen_height;
	
}
