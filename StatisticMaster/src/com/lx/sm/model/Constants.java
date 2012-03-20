package com.lx.sm.model;

public class Constants {
	
	public static final int BUTTON_MONTH = 0x0000001;
	public static final int BUTTON_ALL   = 0x0000002;
	
	public static final double TIME_WEEK_MILLI  = 604800000.0;
	public static final double TIME_MONTH_MILLI = 2592000000.0;
	
	//Message
	public static final String MSG_TYPE        = "MSG_TYPE";
	public static final int MSG_EMPTY_MSG      = 0x00000000;
	public static final int MSG_INBOX_CHANGED  = 0x00000001;
	public static final int MSG_OUTBOX_CHANGED = 0x00000002;
	public static final int MSG_PROGRESS_FULL  = 0x00010000;
	public static final int MSG_BATTERY_CHANGE = 0x00000003;
	
	//Data
	public static final String DATA_FILE_ARRAY = "file_array";
	
	
	// FORMATS
	public static final String FORMAT_DOC_L = "doc";
	public static final String FORMAT_DOC_U = "DOC";
	public static final String FORMAT_DOCX_L = "docx";
	public static final String FORMAT_DOCX_U = "DOCX";
	public static final String FORMAT_TXT_L = "txt";
	public static final String FORMAT_TXT_U = "TXT";
	public static final String FORMAT_PPT_L = "ppt";
	public static final String FORMAT_PPT_U = "PPT";
	public static final String FORMAT_PPTX_L = "pptx";
	public static final String FORMAT_PPTX_U = "PPTX";
	public static final String FORMAT_PDF_L = "pdf";
	public static final String FORMAT_MP3_L = "mp3";
	public static final String FORMAT_MP3_U = "MP3";
	public static final String FORMAT_MPG_L = "mpg";
	public static final String FORMAT_MPG_U = "MPG";
	public static final String FORMAT_MPEG4_L = "mp4";
	public static final String FORMAT_MPEG4_U = "MP4";
	public static final String FORMAT_RMVB_L = "rmvb";
	public static final String FORMAT_RMVB_U = "RMVB";
	public static final String FORMAT_RM_L  = "rm";
	public static final String FORMAT_RM_U  = "RM";
	public static final String FORMAT_JPG_L = "jpg";
	public static final String FORMAT_JPG_U = "JPG";
	public static final String FORMAT_AVI_L = "avi";
	public static final String FORMAT_AVI_U = "AVI";
	public static final int    NUM_FORMATS  = 11;
	public static final int    INDEX_MP3    = 0;
	public static final int    INDEX_JPG	= 1;
	public static final int    INDEX_MP4    = 2;
	public static final int    INDEX_PDF    = 3;
	public static final int    INDEX_TXT    = 4;
	public static final int    INDEX_PPT    = 5;
	public static final int    INDEX_DOC    = 6;
	public static final int    INDEX_MPG    = 7;
	public static final int    INDEX_RMVB	= 8;
	public static final int    INDEX_AVI    = 9;
	public static final int    INDEX_OTHER  = 10;    
	
	//uri
	public static final String MSG_SEND_URI = "content://sms/sent";
	public static final String MSG_IN_URI   = "content://sms/inbox";
	
	// Units China
	public static final String unit_ge_cn   = "个";
	public static final String unit_yuan_cn = "￥";
	public static final String unit_sec_cn  = "秒";
	public static final String unit_min_cn  = "分钟";
	public static final String unit_hour_cn = "小时";
	// Units EN
	public static final String unit_yuan_en = "$";
	public static final String unit_sec_en  = "sec";
	public static final String unit_min_en  = "min";
	public static final String unit_hour_en = "h";
	// UNIT LENGTH
	public static final long   unit_length_KB = 1024;
	public static final long   unit_length_MB = 1024*1024;
	public static final long   unit_length_GB = 1024*1024*1024;
	
	public static final String DB_NAME = "SMDB";
	
	// SharedPreferences
	public static final String PREFERENCE_NAME     = "SM";
	public static final String KEY_FIRST_RUN       = "KFR";
	public static final String KEY_FREE_PHONE_NUMS = "FPN";
	public static final String KEY_SD_PREF_SET     = "KSPS";
	
	// Call flags month
	public static final int KEY_PAID_CALLS_M  = 0x0000000;
	public static final int KEY_CALL_LENGTH_M = 0x0000001;
	public static final int KEY_PAID_LENGTH_M = 0x0000002;
	public static final int KEY_CALL_DIAL_M   = 0x0000003;
	public static final int KEY_CALL_RECEIV_M = 0x0000004;
	public static final int KEY_CALL_LONGEST_M= 0x0000005;
	public static final int KEY_CALL_FEE_M    = 0x0000006;
	public static final int KEY_CALL_MISS_M   = 0x0000007;
	public static final int KEY_FREE_LEFT_M   = 0x0000008;
	public static final int KEY_CALL_AVERAG_M = 0x0000009;
	public static final int KEY_MOST_CALLED_M = 0x000000a;
	public static final int KEY_DAYS_LEFT_M   = 0x000000b;
	
	// Call flags all
	public static final int KEY_PAID_CALLS_Y   = 0x0000000;
	public static final int KEY_CALL_LENGTH_Y  = 0x0000001;
	public static final int KEY_PAID_LENGTH_Y  = 0x0000002;
	public static final int KEY_CALL_DIAL_Y    = 0x0000003;
	public static final int KEY_CALL_RECEIV_Y  = 0x0000004;
	public static final int KEY_CALL_LONGEST_Y = 0x0000005;
	public static final int KEY_CALL_FEE_Y     = 0x0000006;
	public static final int KEY_CALL_MISS_Y    = 0x0000007;
	public static final int KEY_CALL_AVERAG_Y  = 0x0000008;
	public static final int KEY_MOST_CALLED_Y  = 0x0000009;
	public static final int KEY_AVG_TIME_CYCLE = 0x000000a;
	public static final int KEY_AVG_TIME_WEEK  = 0x000000b;
	public static final int KEY_AVG_TIME_PER_Y = 0x000000c;
	
	// Call Observer flags
	public static final int FLAG_CALL_NEW_COME = 0x00000000;
	public static final int FLAG_CALL_NEW_DIAL = 0x00000001;
	public static final int FLAG_CALL_DELETE   = 0x00000002;
	public static final int FLAG_CALL_MISS     = 0x00000003;
	public static final int FLAG_CALL_FAIL     = 0x00000004;

}
