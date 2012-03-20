package com.lvxiang.sm.util;

import com.lvxiang.sm.observers.MyInboxObserver;
import com.lvxiang.sm.observers.MyOutBoxObserver;
import com.lx.sm.model.Constants;
import com.mlr.sm.db.MessageManager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public class MessageUtil implements UtilInterface,Callback{

	private Context context;
	private Handler self_handler;
	private Handler act_handler;
	
	private ContentResolver cr;
	private ContentObserver co_i; // in box obserber
	private ContentObserver co_o; // out box observer
	
	public MessageUtil(Context context){
		this.context = context;
		
		self_handler = new Handler(this);
		
		cr = context.getContentResolver();
		registerObservers();
	}
	
	public void registerObservers() {
		// TODO Auto-generated method stub
		co_i = new MyInboxObserver(self_handler);
		co_o = new MyOutBoxObserver(self_handler);
		cr.registerContentObserver(Uri.parse(Constants.MSG_IN_URI), true, co_i);
		cr.registerContentObserver(Uri.parse(Constants.MSG_SEND_URI), true, co_o);
	}

	public void unregisterObservers() {
		// TODO Auto-generated method stub
		cr.unregisterContentObserver(co_i);
		cr.unregisterContentObserver(co_o);
	}

	public void finish() {
		// TODO Auto-generated method stub
		MessageManager.finish();
	}

	public void firstRun() {
		// TODO Auto-generated method stub
		MessageManager.init(context);
	}

	public void stop() {
		// TODO Auto-generated method stub
		this.act_handler = null;
	}

	public void setActHandler(Handler handler) {
		// TODO Auto-generated method stub
		this.act_handler = handler;
	}

	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case Constants.MSG_INBOX_CHANGED:{
			
		}
		case Constants.MSG_OUTBOX_CHANGED:{
			
		}
		}
		return false;
	}

}
