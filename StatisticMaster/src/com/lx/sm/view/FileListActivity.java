package com.lx.sm.view;

import com.lx.sm.model.Constants;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class FileListActivity extends ListActivity{

	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.getIntent().getStringArrayExtra(Constants.DATA_FILE_ARRAY));
		
		this.setListAdapter(adapter);
	}
	
}
