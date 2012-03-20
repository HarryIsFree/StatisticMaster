package com.lvxiang.sm.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class OtherUtil {

	public static boolean goodNetwork(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info       = cm.getActiveNetworkInfo();
		if(info==null||!info.isAvailable()){
			return false;
		}
		return true;
	}
	
}
