package com.lvxiang.sm.util;

import android.os.Handler;


public interface UtilInterface {

	public void registerObservers();
	public void unregisterObservers();
	public void finish();
	public void firstRun();
	/**
	 * called when the application is suspended
	 */
	public void stop();
	public void setActHandler(Handler handler);
	
}
