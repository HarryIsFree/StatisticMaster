package com.lvxiang.sm.observers;

import android.database.ContentObserver;
import android.os.Handler;

public class MyCalldbObserver extends ContentObserver{

	Handler mHandler;
	
	public MyCalldbObserver(Handler handler) {
		super(handler);
		this.mHandler = handler;
	}

	@Override
	public void onChange(boolean b){
		mHandler.sendEmptyMessage(0);
	}
	
}
