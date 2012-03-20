package com.mlr.sm.db;

import java.util.ArrayList;

import com.lx.sm.model.Call;
import com.lx.sm.model.Constants;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.CallLog;

public class CallManager {

	private static SQLiteDatabase db;
	private static ContentResolver cr;
	
	private static final String TABLE_NAME_MONTH   = "call_table_m";
	private static final String TABLE_NAME_ALL     = "call_table_a";
	private static final String CALL_ID     = "_id";   
	private static final String CALL_LONGEST= "_longest";
	private static final String CALL_PAID   = "num_paid";
	private static final String PAID_LENGTH = "paid_length";
	private static final String CALL_COME   = "num_come";
	private static final String CALL_DIAL   = "num_dial";
	private static final String CALL_FEE    = "num_fee";
	private static final String CALL_MISS   = "num_miss";
	private static final String FREE_LEFT   = "num_left";
	private static final String MOST_CALLED = "most_called";
	
	private static final String AVG_WEEK    = "avg_week";
	private static final String AVG_MONTH   = "avg_month";
	private static final String AVG_LENGTH  = "avg_length";
	
	
	private static final String[] fields = {
		CallLog.Calls._ID,
		CallLog.Calls.CACHED_NAME,// String
		CallLog.Calls.DATE,// long
		CallLog.Calls.NUMBER,// String
		CallLog.Calls.DURATION,// long (in seconds)
		CallLog.Calls.CACHED_NUMBER_TYPE,// String
		CallLog.Calls.TYPE
	};
	
	private static final String CREATE_TABLE_MONTH = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_MONTH
											  +" ( "+ CALL_ID +" LONG , " +
											  		  MOST_CALLED +" TEXT ," +
											  		  CALL_LONGEST + " INTEGER , " +
											  		  CALL_PAID + " INTEGER , " +
											  		  PAID_LENGTH + " INTEGER , " + // in seconds
											  		  CALL_FEE + " FLOAT , " +
											  		  CALL_COME + " INTEGER , "  +
											  		  CALL_DIAL + " INTEGER , " +
											  		  CALL_MISS + " INTEGER , " +
											  		  FREE_LEFT + " INTEGER "
											  + " ) ";
	
	private static final String CREATE_TABLE_ALL  = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_ALL
														+" ( "+ MOST_CALLED +" TEXT ," +
																CALL_LONGEST + " INTEGER , " +
																CALL_PAID + " INTEGER , " +
																PAID_LENGTH + " INTEGER , " +
																CALL_FEE + " FLOAT , " +
																CALL_COME + " INTEGER , "  +
																CALL_DIAL + " INTEGER , " +
																CALL_MISS + " INTEGER , " +
																AVG_WEEK + " DOUBLE , " +
																AVG_MONTH + " DOUBLE , " +
																AVG_LENGTH + " DOUBLE "
														+ " ) ";
	
										  		
	public static void init(Context c){
		cr = c.getContentResolver();
		db = c.openOrCreateDatabase(Constants.DB_NAME, Context.MODE_PRIVATE, null);
		db.execSQL(CREATE_TABLE_MONTH);
		db.execSQL(CREATE_TABLE_ALL);
	}
	
	public static ArrayList<Call> query(){
		
		Cursor cursor = cr.query(android.provider.CallLog.Calls.CONTENT_URI, fields, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
		
		ArrayList<Call> calls = new ArrayList<Call>();
		int count = cursor.getCount();
		for(int i = 0; i < count ; i++){
			cursor.moveToPosition(i);
			
			Call call = new Call();
			call.setID(cursor.getLong(0));
			call.setName(cursor.getString(1));
			call.setDate(cursor.getLong(2));
			call.setNumber(cursor.getString(3));
			call.setDuration(cursor.getInt(4));
			call.setType(cursor.getInt(5));
			
			calls.add(call);
		}
		cursor.close();
		return calls;
	}
	
	public static void initTableMonth(long id,String most_called,int longest, int num_paid, int paid_length, int num_come
										,int num_dial, int num_miss, int free_left){
		ContentValues cv = new ContentValues();
		cv.put(CALL_ID, id);
		cv.put(MOST_CALLED, most_called);
		cv.put(CALL_LONGEST, longest);
		cv.put(CALL_PAID, num_paid);
		cv.put(PAID_LENGTH, paid_length);
		cv.put(CALL_COME, num_come);
		cv.put(CALL_DIAL, num_dial);
		cv.put(CALL_MISS, num_miss);
		cv.put(FREE_LEFT, free_left);
		db.insert(TABLE_NAME_MONTH, null, cv);
	}
	
	public static void initTableAll(String most_called, int longest, int num_paid, int paid_length, int num_come, int num_dial
										,int num_miss,double avg_week,double avg_month,double avg_length){
		ContentValues cv = new ContentValues();
		cv.put(MOST_CALLED, most_called);
		cv.put(CALL_LONGEST, longest);
		cv.put(CALL_PAID, num_paid);
		cv.put(PAID_LENGTH, paid_length);
		cv.put(CALL_COME, num_come);
		cv.put(CALL_DIAL, num_dial);
		cv.put(CALL_MISS, num_miss);
		cv.put(AVG_WEEK, avg_week);
		cv.put(AVG_MONTH, avg_month);
		cv.put(AVG_LENGTH, avg_length);
		db.insert(TABLE_NAME_ALL, null, cv);
	}
	
	public static long getCurrentID(){
		Cursor c = db.rawQuery("SELECT " + CALL_ID +" FROM " + TABLE_NAME_MONTH, null);
		if(c.getCount()>0){
			return c.getLong(0);
		}
		else return 0;
	}
	
	public static void finish(){
		db.close();
	}
											  
}
