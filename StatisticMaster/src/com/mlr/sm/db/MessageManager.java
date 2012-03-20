package com.mlr.sm.db;

import java.util.ArrayList;

import com.lx.sm.model.Constants;
import com.lx.sm.model.Message;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MessageManager {
	private static ContentResolver cr;
	
	private static SQLiteDatabase db;
	
	private static final String DB_NAME = "SMDB";
	
	private static final String TABLE_Month = "Message_Month";
	private static final String SEND_DATE="send_date";
	private static final String RECEIVE_DATE="receive_date";
	private static final String SEND="send";
	private static final String RECEIVE="receive";
	private static final String REMAINDER="remainder";
	private static final String CONSUME="consume";
	private static final String LONGEST="longest";
	private static final String EVE_SEND="eve_send";
	private static final String EVE_RECEIVE="eve_receive";
	
	private static final String CREATE_TABLE1 = "CREATE TABLE IF NOT EXISTS " + TABLE_Month 
			  +" ( "+SEND_DATE+" LONG,"
			        +RECEIVE_DATE+" LONG,"
			        +SEND+" INTEGER,"
			        +RECEIVE + " INTEGER,"
			        +REMAINDER + " INTEGER,"
			        +CONSUME + " FLOAT,"
			        +LONGEST + " INTEGER,"
			        +EVE_SEND + " FLOAT,"
			        +EVE_RECEIVE + " FLOAT"
			  +" )";
	
	public static void init(Context context){
		cr=context.getContentResolver();
		
		db = context.openOrCreateDatabase(DB_NAME, 0, null);
		db.execSQL(CREATE_TABLE1);
	}
	
	//
	public static ArrayList<Message> getSendData(){
		ArrayList<Message> messages=new ArrayList<Message>();
		Uri send_uri = Uri.parse(Constants.MSG_SEND_URI);
		Cursor cur = cr.query(send_uri, null,null,null,null);
		if(cur!=null&&cur.getCount()>0){
			for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
				Message m=new Message();
				m.id=cur.getInt(cur.getColumnIndexOrThrow("_id"));
				m.send=true;
				m.address=cur.getString(cur.getColumnIndexOrThrow("address"));
				m.date=cur.getLong(cur.getColumnIndexOrThrow("date"));
				String body=cur.getString(cur.getColumnIndexOrThrow("body"));
				m.length=body.length();
				messages.add(m);
			}
		}
		if(cur!=null){
			cur.close();
		}
		return messages;
	}
	
	//
	public static ArrayList<Message> getReceiveData(){
		ArrayList<Message> messages=new ArrayList<Message>();
		Uri receive_uri = Uri.parse(Constants.MSG_IN_URI);
		Cursor cur = cr.query(receive_uri, null,null,null,null);
		if(cur!=null&&cur.getCount()>0){
			for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
				Message m=new Message();
				m.id=cur.getInt(cur.getColumnIndexOrThrow("_id"));
				m.send=false;
				m.address=cur.getString(cur.getColumnIndexOrThrow("address"));
				m.date=cur.getLong(cur.getColumnIndexOrThrow("date"));
				String body=cur.getString(cur.getColumnIndexOrThrow("body"));
				m.length=body.length();
				messages.add(m);
			}
		}
		if(cur!=null){
			cur.close();
		}
		return messages;
	}
	
	/**
	 * 获取发件箱新增的message
	 * @return
	 */
	public static ArrayList<Message> getSendChange(){
		long send_date=0;
		String sql = "select send_date from Message_Month";
		Cursor c = db.rawQuery(sql, null);
		if(c!=null&&c.getCount()>0){
			c.moveToFirst();
			send_date = c.getLong(0);
		}
		if(c!=null){
			c.close();
		}
		
		ArrayList<Message> messages=new ArrayList<Message>();
		Uri send_uri = Uri.parse("content://sms/sent");
		Cursor cur = cr.query(send_uri, null,null,null,"date DESC");
		if(cur!=null&&cur.getCount()>0){
			for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
				long date=cur.getLong(cur.getColumnIndexOrThrow("date"));
				if(date>send_date){
					Message m=new Message();
					m.id=cur.getInt(cur.getColumnIndexOrThrow("_id"));
					m.send=true;
					m.address=cur.getString(cur.getColumnIndexOrThrow("address"));
					m.date=date;
					String body=cur.getString(cur.getColumnIndexOrThrow("body"));
					m.length=body.length();
					messages.add(m);
				}
				else{
					break;
				}
			}
		}
		if(cur!=null){
			cur.close();
		}
		return messages;
	}
	
	/**
	 * 获取收件箱新增的message
	 * @return
	 */
	public static ArrayList<Message> getReceiveChange(){
		long receive_date=0;
		String sql = "select receive_date from Message_Month";
		Cursor c = db.rawQuery(sql, null);
		if(c!=null&&c.getCount()>0){
			c.moveToFirst();
			receive_date = c.getLong(0);
		}
		if(c!=null){
			c.close();
		}
		
		ArrayList<Message> messages=new ArrayList<Message>();
		Uri receive_uri = Uri.parse("content://sms/inbox");
		Cursor cur = cr.query(receive_uri, null,null,null,"date DESC");
		if(cur!=null&&cur.getCount()>0){
			for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
				long date=cur.getLong(cur.getColumnIndexOrThrow("date"));
				if(date>receive_date){
					Message m=new Message();
					m.id=cur.getInt(cur.getColumnIndexOrThrow("_id"));
					m.send=false;
					m.address=cur.getString(cur.getColumnIndexOrThrow("address"));
					m.date=date;
					String body=cur.getString(cur.getColumnIndexOrThrow("body"));
					m.length=body.length();
					messages.add(m);
				}
				else{
					break;
				}
			}
		}
		if(cur!=null){
			cur.close();
		}
		return messages;
	}
	
	//
	public static void insert(long send_date,long receive_date,int send,int receive,int remainder,
			                  float consume,int longest,float eve_send,float eve_receive){
		ContentValues cv=new ContentValues();
		cv.put(SEND_DATE, send_date);
		cv.put(RECEIVE_DATE, receive_date);
		cv.put(SEND, send);
		cv.put(RECEIVE, receive);
		cv.put(REMAINDER, remainder);
		cv.put(CONSUME, consume);
		cv.put(LONGEST, longest);
		cv.put(EVE_SEND, eve_send);
		cv.put(EVE_RECEIVE, eve_receive);
		db.insert(TABLE_Month, null, cv);
	}
	
	//////////////////////write////////////////////////
	
	//
	public static void writeSend_date(long send_date){
		String sql="update Message_Month set send_date=?";
		db.execSQL(sql, new String[] { send_date+""});
	}
	
	//
	public static void writeReceive_id(long receive_date){
		String sql="update Message_Month set received_date=?";
		db.execSQL(sql, new String[] { receive_date+""});
	}
	
	//
	public static void writeSend(int send){
		String sql="update Message_Month set send=?";
		db.execSQL(sql, new String[] { send+""});
	}
	
	//
	public static void writeReceive(int receive){
		String sql="update Message_Month set receive=?";
		db.execSQL(sql, new String[] { receive+""});
	}
	
	//
	public static void writeRemainder(int remainder){
		String sql="update Message_Month set remainder=?";
		db.execSQL(sql, new String[] { remainder+""});
	}
	
	//
	public static void writeConsume(float consume){
		String sql="update Message_Month set consume=?";
		db.execSQL(sql, new String[] { consume+""});
	}
	
	//
	public static void writeLongest(int longest){
		String sql="update Message_Month set longest=?";
		db.execSQL(sql, new String[] { longest+""});
	}
	
	//
	public static void writeEve_send(int eve_send){
		String sql="update Message_Month set eve_send=?";
		db.execSQL(sql, new String[] { eve_send+""});
	}
	
	//
	public static void writeEve_receive(int eve_receive){
		String sql="update Message_Month set eve_receive=?";
		db.execSQL(sql, new String[] { eve_receive+""});
	}
	
	///////////////////read///////////////////
	
	//
	public static long readSend_date(){
		long send_date=0;
		String sql = "select send_date from Message_Month";
		Cursor cur = db.rawQuery(sql, null);
		if(cur!=null&&cur.getCount()>0){
			cur.moveToFirst();
			send_date = cur.getLong(0);
		}
		if(cur!=null){
			cur.close();
		}
		return send_date;
	}
	
	//
	public static long readReceive_date(){
		long receive_date=0;
		String sql = "select receive_date from Message_Month";
		Cursor cur = db.rawQuery(sql, null);
		if(cur!=null&&cur.getCount()>0){
			cur.moveToFirst();
			receive_date = cur.getLong(0);
		}
		if(cur!=null){
			cur.close();
		}
		return receive_date;
	}	
	
	//
	public static int readSend(){
		int send=0;
		String sql = "select send from Message_Month";
		Cursor cur = db.rawQuery(sql, null);
		if(cur!=null&&cur.getCount()>0){
			cur.moveToFirst();
			send = cur.getInt(0);
		}
		if(cur!=null){
			cur.close();
		}
		return send;
	}
	
	//
	public static int readReceive(){
		int receive=0;
		String sql = "select receive from Message_Month";
		Cursor cur = db.rawQuery(sql, null);
		if(cur!=null&&cur.getCount()>0){
			cur.moveToFirst();
			receive = cur.getInt(0);
		}
		if(cur!=null){
			cur.close();
		}
		return receive;
	}
	
	//
	public static int readRemainder(){
		int remainder=0;
		String sql = "select remainder from Message_Month";
		Cursor cur = db.rawQuery(sql, null);
		if(cur!=null&&cur.getCount()>0){
			cur.moveToFirst();
			remainder = cur.getInt(0);
		}
		if(cur!=null){
			cur.close();
		}
		return remainder;
	}	
	
	//
	public static float readConsume(){
		float consume=0;
		String sql = "select consume from Message_Month";
		Cursor cur = db.rawQuery(sql, null);
		if(cur!=null&&cur.getCount()>0){
			cur.moveToFirst();
			consume = cur.getFloat(0);
		}
		if(cur!=null){
			cur.close();
		}
		return consume;
	}
	
	//
	public static int readLongest(){
		int longest=0;
		String sql = "select longest from Message_Month";
		Cursor cur = db.rawQuery(sql, null);
		if(cur!=null&&cur.getCount()>0){
			cur.moveToFirst();
			longest = cur.getInt(0);
		}
		if(cur!=null){
			cur.close();
		}
		return longest;
	}
	
	//
	public static float readEve_send(){
		float eve_send=0;
		String sql = "select eve_send from Message_Month";
		Cursor cur = db.rawQuery(sql, null);
		if(cur!=null&&cur.getCount()>0){
			cur.moveToFirst();
			eve_send = cur.getFloat(0);
		}
		if(cur!=null){
			cur.close();
		}
		return eve_send;
	}

	//
	public static float readEve_receive(){
		float eve_receive=0;
		String sql = "select eve_receive from Message_Month";
		Cursor cur = db.rawQuery(sql, null);
		if(cur!=null&&cur.getCount()>0){
			cur.moveToFirst();
			eve_receive = cur.getFloat(0);
		}
		if(cur!=null){
			cur.close();
		}
		return eve_receive;
	}
	
	
	////////////////////////////////////////////	
	public static void finish(){
		db.close();
	}
}
