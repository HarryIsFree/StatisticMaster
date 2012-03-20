package com.lx.sm.view;

import com.lx.sm.custom.MyDialog;
import com.lx.sm.model.Constants;
import com.lx.sm.util.adapters.SDListAdapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SDCardActivity extends MyActivity implements Callback{
	
	private ListView list;
	private TextView tv_percent;
	private TextView tv_info1;
	private TextView tv_info2;
	private LayoutInflater inflater;
	private MyDialog    dialog;
	
	private LinearLayout info;
	private LinearLayout progress;
	private ProgressBar  pb;
	private Button       clear;
	private Button       rescan;
	private Button       start_scan;
	
	private Button positive;
	private Button negative;
	private TextView dialog_info;
	
	private Handler handler;
	
	private float total_space;
	private float free_space;
	private float used_space;
	private long  total_num;
	private int   space_rate;
	
	private String total_unit;
	private String free_unit;
	private String used_unit;
	
	private SDListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sd_layout);
		
		initUI();
		initRes();
	}
	
	private void initUI(){
		
		inflater = LayoutInflater.from(this);
		
		list = (ListView) this.findViewById(R.id.sd_list);
		list.setCacheColorHint(Color.TRANSPARENT);
		list.setOnItemClickListener(new MyOnItemListener());
		
		tv_percent = (TextView) this.findViewById(R.id.sd_percentage);
		tv_info1   = (TextView) this.findViewById(R.id.sd_text1);
		tv_info2   = (TextView) this.findViewById(R.id.sd_text2);
		
		info   = (LinearLayout) this.findViewById(R.id.sd_info);
		clear  = (Button) this.findViewById(R.id.sd_button_clear);
		rescan = (Button) this.findViewById(R.id.sd_button_scan);
		rescan.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				info.setVisibility(View.INVISIBLE);
				progress.setVisibility(View.VISIBLE);
				pb.setProgress(0);
				new SDInitThread().start();
			}
			
		});
		clear.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				clear_fragments();
			}
			
		});
		//info.setVisibility(View.INVISIBLE);
		
		progress   = (LinearLayout) this.findViewById(R.id.sd_progress);
		start_scan = (Button) this.findViewById(R.id.sd_start_scan);
		start_scan.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				progress.setVisibility(View.INVISIBLE);
				info.setVisibility(View.VISIBLE);
			}
			
		});
		
		pb = (ProgressBar) this.findViewById(R.id.sd_progressbar);
	}
	
	private void initRes(){
		
		handler = new Handler(this);
		sdcardUtil.setActHandler(handler);
		
		getUsedRate();
		getTotalSpace();
		getFreeSpace();
		total_num = sdcardUtil.getFileNum();
		
		tv_percent.setText(space_rate+"%");
		tv_info1.setText(this.getResources().getString(R.string.total_space)+
				" "+
				total_space+
				total_unit+
				" \n"+
				this.getResources().getString(R.string.free_space)+
				free_space+
				free_unit);
		tv_info2.setText(this.getResources().getString(R.string.file_num)+total_num);
		
	}
	
	private void getUsedRate(){
		space_rate = (int) (sdcardUtil.getUsedSpace()*100/sdcardUtil.getTotalSpace());
	}
	
	private void getTotalSpace(){
		if(sdcardUtil.getTotalSpace()>Constants.unit_length_GB){
			total_space = (int) (sdcardUtil.getTotalSpace()*100/Constants.unit_length_GB);
			total_space = (float)total_space/(100.0f);
			total_unit = this.getResources().getString(R.string.unit_gb);
		}
		else if(sdcardUtil.getTotalSpace()>Constants.unit_length_MB){
			total_space = (int) (sdcardUtil.getTotalSpace()*100/Constants.unit_length_MB);
			total_space = (float)total_space/(100.0f);
			total_unit = this.getResources().getString(R.string.unit_mb);
		}
		else{
			total_space = (int) (sdcardUtil.getTotalSpace()*100/Constants.unit_length_KB);
			total_space = (float)total_space/(100.0f);
			total_unit = this.getResources().getString(R.string.unit_kb);
		}
	}
	
	private void getFreeSpace(){
		if(sdcardUtil.getFreeSpace()>Constants.unit_length_GB){
			free_space = (int) (sdcardUtil.getFreeSpace()*100/Constants.unit_length_GB);
			free_space = (float)free_space/(100.0f);
			free_unit = this.getResources().getString(R.string.unit_gb);
		}
		else if(sdcardUtil.getFreeSpace()>Constants.unit_length_MB){
			free_space = (int) (sdcardUtil.getFreeSpace()*100/Constants.unit_length_MB);
			free_space = (float)free_space/(100.0f);
			free_unit = this.getResources().getString(R.string.unit_mb);
		}
		else{
			free_space = (int) (sdcardUtil.getFreeSpace()*100/Constants.unit_length_KB);
			free_space = (float)free_space/(100.0f);
			free_unit = this.getResources().getString(R.string.unit_kb);
		}
	}
	
	private void clear_fragments(){
		dialog = new MyDialog(this,R.style.MyDialog);
		View v = inflater.inflate(R.layout.my_dialog, null);
		dialog.setContentView(v);
		
		positive = (Button) v.findViewById(R.id.dialog_positive);
		positive.setText(R.string.auto_clear);
		negative = (Button) v.findViewById(R.id.dialog_negative);
		negative.setText(R.string.manual_clear);
		
		dialog_info = (TextView) v.findViewById(R.id.dialog_text);
		dialog_info.setText(R.string.auto_clear_hint);
		
		positive.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(!sp.contains(Constants.KEY_SD_PREF_SET)){
					
					dialog = new MyDialog(SDCardActivity.this,R.style.MyDialog);
					View view = inflater.inflate(R.layout.my_dialog, null);
					positive = (Button) view.findViewById(R.id.dialog_positive);
					positive.setText(R.string.OK);
					negative = (Button) view.findViewById(R.id.dialog_negative);
					negative.setText(R.string.CANCEL);
					dialog_info = (TextView) view.findViewById(R.id.dialog_text);
					dialog_info.setText(R.string.auto_clear_error);
					
					positive.setOnClickListener(new OnClickListener(){

						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
						
					});
					
					negative.setOnClickListener(new OnClickListener(){

						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
						
					});
					
					dialog.setContentView(view);
					dialog.show();
					
				}
				else{
					
				}
			}
			
		});
		
		negative.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();
			}
			
		});
		
		dialog.show();
	}
	
	private void getUsedSpace(){
		
	}
	

	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		int progress = msg.what;
		if(progress<=100){
			pb.setProgress(progress);
		}
		else if(progress ==Constants.MSG_PROGRESS_FULL){
			pb.setProgress(100);
			start_scan.setVisibility(View.VISIBLE);
			total_num = sdcardUtil.getFileNum();
			tv_info2.setText(this.getResources().getString(R.string.file_num)+total_num);
			
			adapter = new SDListAdapter(sdcardUtil.getTypeNum(),SDCardActivity.this);
			list.setAdapter(adapter);
		}
		return true;
	}
	
	private class SDInitThread extends Thread{
		
		public void run(){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sdcardUtil.init_scan();
		}
		
	}
	
	private class MyOnItemListener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Bundle bundle = new Bundle();
			
			switch(arg2){
			case Constants.INDEX_MP3:{
				bundle.putStringArray(Constants.DATA_FILE_ARRAY, sdcardUtil.getFileString(sdcardUtil.mp3List));
				break;
			}
			case Constants.INDEX_MP4:{
				//bundle.putStringArray(Constants.DATA_FILE_ARRAY, sdcardUtil.getFileString(sdcardUtil.mp4List));
				break;
			}
			case Constants.INDEX_TXT:{
				bundle.putStringArray(Constants.DATA_FILE_ARRAY, sdcardUtil.getFileString(sdcardUtil.txtList));
				break;
			}
			case Constants.INDEX_PDF:{
				bundle.putStringArray(Constants.DATA_FILE_ARRAY, sdcardUtil.getFileString(sdcardUtil.pdfList));
				break;
			}
			case Constants.INDEX_AVI:{
				bundle.putStringArray(Constants.DATA_FILE_ARRAY, sdcardUtil.getFileString(sdcardUtil.aviList));
				break;
			}
			case Constants.INDEX_DOC:{
				bundle.putStringArray(Constants.DATA_FILE_ARRAY, sdcardUtil.getFileString(sdcardUtil.docList));
				break;
			}
			case Constants.INDEX_MPG:{
				bundle.putStringArray(Constants.DATA_FILE_ARRAY, sdcardUtil.getFileString(sdcardUtil.mpgList));
				break;
			}
			case Constants.INDEX_PPT:{
				bundle.putStringArray(Constants.DATA_FILE_ARRAY, sdcardUtil.getFileString(sdcardUtil.pptList));
				break;
			}
			case Constants.INDEX_RMVB:{
				bundle.putStringArray(Constants.DATA_FILE_ARRAY, sdcardUtil.getFileString(sdcardUtil.rmvbList));
				break;
			}
			case Constants.INDEX_JPG:{
				bundle.putStringArray(Constants.DATA_FILE_ARRAY, sdcardUtil.getFileString(sdcardUtil.jpgList));
				break;
			}
			case Constants.INDEX_OTHER:{
				break;
			}
			}
			
			Intent intent = new Intent(SDCardActivity.this,FileListActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
	}
	
}
