package com.lx.sm.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

public class MyImageView extends ImageView{

	int width;
	int height;
	
	int current_amount;
	
	public MyImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas); 
		
		Paint p = new Paint();
		//p.setColor(color)
	}
	
	public void setAmount(int num){
		
		this.current_amount = num;
		
	}
	
	public void startAnimate(){
		
	}
	
	public void stopAnimate(){
		
	}
	
}
