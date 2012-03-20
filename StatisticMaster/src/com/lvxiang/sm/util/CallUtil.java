package com.lvxiang.sm.util;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.lvxiang.sm.observers.MyCalldbObserver;
import com.lx.sm.model.Call;
import com.lx.sm.model.Constants;
import com.lx.sm.model.DataItem;
import com.lx.sm.view.R;
import com.mlr.sm.db.CallManager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.CallLog;

public class CallUtil implements UtilInterface,Callback{

	private static ContentResolver cr;
	private static ContentObserver   	   co;
	private static Handler self_handler;
	private static Handler act_handler = null;
	private static Context context;
	private static ArrayList<String> free_numbers;
	private static SharedPreferences sp;
	
	// Monthly data
	long id;
	String most_called_m;
	int longest_m = 0;
	int num_paid_m = 0;
	int num_free_m = 0;
	int paid_length_m = 0;
	int num_come_m = 0;
	int num_dial_m = 0;
	int num_miss_m = 0;
	int free_left = 0;
	int length_m = 0;
	
	//All data
	String most_called;
	int longest = 0;
	int num_paid = 0;
	int paid_length = 0;
	int num_come = 0;
	int num_dial = 0;
	int num_miss = 0;
	double avg_week = 0;
	double avg_month = 0;
	double avg_length = 0;
	int length = 0;
	int all    = 0;
	long duration = 0;
	
	public CallUtil(Context context){
		
		this.context = context;
		cr = context.getContentResolver();
		free_numbers = new ArrayList<String>();
		
		// Initialize Database manager
		CallManager.init(context);
		
		registerObservers();
	}
	
	private ArrayList<String> getFreeNumbers(){
		ArrayList<String> result = new ArrayList<String>();
		sp = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		if(!sp.contains(Constants.KEY_FREE_PHONE_NUMS)){
			editor.putString(Constants.KEY_FREE_PHONE_NUMS, "");
			editor.commit();
			return result;
		}
		else{
			String[] nums = sp.getString(Constants.KEY_FREE_PHONE_NUMS,"").split(";");
			for(String n: nums){
				result.add(n);
			}
			return result;
		}
	}
	
	public void registerObservers(){
		// Register ContentObserver
		self_handler = new Handler(this);
		co = new MyCalldbObserver(self_handler);
		cr.registerContentObserver(CallLog.Calls.CONTENT_URI, true, co);
	}
	
	/**
	 * UnresgiterContentObservers;
	 */
	public void unregisterObservers(){
		cr.unregisterContentObserver(co);
	}
	
	public void setActHandler(Handler handler){
		this.act_handler = handler;
	}
	
	public void finish(){
		unregisterObservers();
		CallManager.finish();
	}
	
	public void stop(){
		act_handler = null;
	}
	
	/**
	 * Called when this is the first time that the applications runs or when user click button to refresh all data.
	 */
	public void firstRun() {
		
		java.util.Date date = new java.util.Date();
		
		// TODO Auto-generated method stub
		// Get calllog data from local database
		ArrayList<Call> calls = CallManager.query();
		HashMap<String,Integer> name_num_m = new HashMap<String,Integer>();
		HashMap<String,Integer> name_num   = new HashMap<String,Integer>();
		
		for(int i = 0; i <calls.size() ; i++){
			Call c = calls.get(i);
			Date d = new Date(c.getDate());
			// Call beongs to current month
			if(date.getYear() == d.getYear()&&date.getMonth() == d.getMonth()){
				if(i == 0){
					this.id = c.getDate();
				}
				// The items are already ordered descendently by date
				if(!name_num_m.containsKey(c.getNumber())){
					name_num_m.put(c.getNumber(), 1);
					name_num.put(c.getNumber(), 1);
				}
				else{
					int num = name_num.get(c.getNumber());
					name_num_m.put(c.getNumber(), num+1);
					name_num.put(c.getNumber(), num+1);
				}
				
				if(longest_m<c.getDuration()){
					longest_m = c.getDuration();
				}
				length_m += c.getDuration();
				
				if(free_numbers.contains(c.getNumber())){
					num_free_m++;
				}
				else{
					paid_length_m += c.getDuration();
				}
				length_m += c.getDuration();
				
				switch(c.getType()){
				case CallLog.Calls.OUTGOING_TYPE:{
					num_dial_m++;
				}
				case CallLog.Calls.INCOMING_TYPE:{
					num_come_m++;
				}
				case CallLog.Calls.MISSED_TYPE:{
					num_miss_m++;
				}
				}
				
			}
			else // not current month
			{
				if(!name_num.containsKey(c.getNumber())){
					name_num.put(c.getNumber(), 1);
				}
				else{
					int num = name_num.get(c.getNumber());
					name_num.put(c.getNumber(), num+1);
				}
				if(longest<c.getDuration()){
					longest = c.getDuration();
				}
				length += c.getDuration();
				if(!free_numbers.contains(c.getNumber())){
					num_paid++;
					paid_length += c.getDuration();
				}
				switch(c.getType()){
				case CallLog.Calls.INCOMING_TYPE:{
					num_come++;
				}
				case CallLog.Calls.OUTGOING_TYPE:{
					num_dial++;
				}
				case CallLog.Calls.MISSED_TYPE:{
					num_miss++;
				}
				}
			}
			if(duration==0||duration>c.getDate()){
				duration = c.getDate();
			}
			
		}
		if(longest<longest_m) longest = longest_m;
		num_paid += num_paid_m;
		num_come += num_come_m;
		num_dial += num_dial_m;
		num_miss += num_miss_m;
		all      += num_paid;
		all      += num_dial;
		all      += num_miss;
		
		paid_length += paid_length_m;
		length      += length_m;
		
		duration = date.getTime() - duration;
		avg_week  = ((float)all)/((double)(duration)/Constants.TIME_WEEK_MILLI);
		avg_month = ((float)all)/((double)(duration)/Constants.TIME_MONTH_MILLI); 
		avg_length= ((float)length)/((double)all);
		
		most_called_m = getMostCalled(name_num_m);
		most_called   = getMostCalled(name_num);
		
		CallManager.initTableMonth(id, most_called, longest, num_paid, paid_length, num_come, num_dial, num_miss, free_left);
		CallManager.initTableAll(most_called, longest, num_paid, paid_length_m, num_come, num_dial, num_miss, avg_week, avg_month, avg_length);
	}
	
	public ArrayList<DataItem> getMonthStats(Context context){
		
		ArrayList<DataItem> result = new ArrayList<DataItem>();
		
		result.add(new DataItem(context.getString(R.string.paid_calls_m_cn),num_paid_m+"",""));
		result.add(new DataItem(context.getString(R.string.call_length_m_cn),length_m+"",Constants.unit_min_cn));
		result.add(new DataItem(context.getString(R.string.paid_length_m_cn),paid_length_m+"",Constants.unit_sec_cn));
		result.add(new DataItem(context.getString(R.string.call_dial_m_cn),num_dial_m+""+"",""));
		result.add(new DataItem(context.getString(R.string.call_recei_m_cn),num_come_m+"",""));
		result.add(new DataItem(context.getString(R.string.call_miss_m_cn),num_miss_m+"",""));
		result.add(new DataItem(context.getString(R.string.call_longest_m_cn),longest_m+"",""));
		result.add(new DataItem(context.getString(R.string.call_fee_m_cn),"34.2",Constants.unit_yuan_cn));
		result.add(new DataItem(context.getString(R.string.free_left_m_cn),free_left+"",""));
		
		return result;
	}
	
	public ArrayList<DataItem> getAllStats(Context context){
		ArrayList<DataItem> result = new ArrayList<DataItem>();
		
		result.add(new DataItem(context.getString(R.string.paid_calls_cn),num_paid+"",""));
		result.add(new DataItem(context.getString(R.string.paid_length_cn),paid_length+"",Constants.unit_sec_cn));
		result.add(new DataItem(context.getString(R.string.call_length_cn),length+"",Constants.unit_min_cn));
		result.add(new DataItem(context.getString(R.string.call_dial_cn),num_dial+""+"",""));
		result.add(new DataItem(context.getString(R.string.call_recei_cn),num_come+"",""));
		result.add(new DataItem(context.getString(R.string.call_miss_cn),num_miss+"",""));
		int  aw = (int) (avg_week*10);
		avg_week = ((float)aw)/(10.0f);
		
		int  am =(int)(avg_month*10);
		avg_month = ((float)am)/(10.0f);
		
		int  al =(int)(avg_length*10);
		avg_length = ((float)al)/(10.0f);
		
		result.add(new DataItem(context.getString(R.string.call_avg_week_cn),avg_week+"",""));
		result.add(new DataItem(context.getString(R.string.call_avg_month_cn),avg_month+"",""));
		result.add(new DataItem(context.getString(R.string.call_avg_length_cn),avg_length+"",""));
		
		return result;
		
	}

	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		
		// find what's changed
		
		// change UI
		if(act_handler!=null){
			
		}
		
		return true;
	}
	
	public long getID(){
		return this.id;
	}
	
	public String getMostCalled(HashMap<String,Integer> map){
		int num = 0;
		String name = null;
		Set<Entry<String,Integer>> set = map.entrySet();
		Iterator<Entry<String,Integer>> iterator = set.iterator();
		while(iterator.hasNext()){
			Entry<String,Integer> entry = iterator.next();
			if(entry.getValue()>num){
				num = entry.getValue();
				name = entry.getKey();
			}
		}
		return name;
	}
	
	public String getMostCalledMonth(){
		return this.most_called_m;
	}
	
	public String getMostCalledAll(){
		return this.most_called;
	}
	
	public int getNumPaidMonth(){
		return this.num_paid_m;
	}
	
	public int getNumPaidAll(){
		return this.num_paid;
	}
	
	public int getPaidLengthMonth(){
		return this.paid_length_m;
	}
	
	public int getPaidLengthAll(){
		return this.paid_length;
	}
	
	public int getLonestMonth(){
		return this.longest_m;
	}
	
	public int getLongestAll(){
		return this.longest;
	}
	
	public int getComeMonth(){
		return this.num_come_m;
	}
	
	public int getComeAll(){
		return this.num_come;
	}
	
	public int getDialMonth(){
		return this.num_dial_m;
	}
	
	public int getDialAll(){
		return this.num_dial;
	}
	
	public int getMissMonth(){
		return this.num_miss_m;
	}
	
	public int getMissAll(){
		return this.num_miss;
	}
	
	public int getFreeLeft(){
		return this.free_left;
	}
	
	public double getAvgWeek(){
		return this.avg_week;
	}
	
	public double getAvgMonth(){
		return this.avg_month;
	}
	
	public double getAvgLength(){
		return this.avg_length;
	}
	

}
