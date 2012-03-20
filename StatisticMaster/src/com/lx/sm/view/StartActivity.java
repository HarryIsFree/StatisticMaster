package com.lx.sm.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public class StartActivity extends Activity implements Callback{
	
	Handler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		
		handler = new Handler(this);
		
		Runnable t = new Runnable(){
			public void run(){
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.dispatchMessage(new Message());
			}
		};
		
		new Thread(t).start();
	}

	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(StartActivity.this,MainActivity.class);
		startActivity(intent);
		StartActivity.this.finish();
		return false;
	}
	
}
