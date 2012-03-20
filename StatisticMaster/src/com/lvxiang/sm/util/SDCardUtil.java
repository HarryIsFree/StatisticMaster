package com.lvxiang.sm.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lx.sm.model.Constants;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SDCardUtil implements UtilInterface{
	
	private File sdDir;
	private long total_space;
	private long free_space;
	private long used_space;
	private long total_num;
	private long byte_count;
	private int  progress;
	
	private int doc;
	private int pdf;
	private int txt;
	private int ppt;
	private int jpg;
	private int avi;
	private int rmvb;
	private int mpg;
	private int mp3;
	private int other;
	private int num_array[];
	
	private Context context;
	
	private Handler actHandler;
	
	public List<File> txtList = new ArrayList<File>();
	public List<File> docList = new ArrayList<File>();
	public List<File> pdfList = new ArrayList<File>();
	public List<File> pptList = new ArrayList<File>();
	public List<File> jpgList = new ArrayList<File>();
	public List<File> aviList = new ArrayList<File>();
	public List<File> rmvbList = new ArrayList<File>();
	public List<File> mpgList = new ArrayList<File>();
	public List<File> mp3List = new ArrayList<File>();
	
	public SDCardUtil(Context context){
		this.context = context;
	}
	
	
	public void init(){
		if(sdMount()){
			sdDir = Environment.getExternalStorageDirectory();
			Toast.makeText(context, sdDir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
			total_space = sdDir.getTotalSpace();
			free_space  = sdDir.getFreeSpace();
			Log.d("free space", free_space+"");
			used_space  = total_space - free_space;
		}
		else{
			Toast.makeText(context, "media unmouted", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void init_scan(){
		//FileNode node = new FileNode(null,sdDir);
		clear();
		scan(sdDir);
		num_array = new int[Constants.NUM_FORMATS];
		num_array[Constants.INDEX_AVI] = getAVI();
		num_array[Constants.INDEX_DOC] = getDOC();
		num_array[Constants.INDEX_JPG] = getJPG();
		num_array[Constants.INDEX_MP3] = getMP3();
		num_array[Constants.INDEX_MP4] = getMPG();
		num_array[Constants.INDEX_MPG] = getMPG();
		num_array[Constants.INDEX_PDF] = getPDF();
		num_array[Constants.INDEX_PPT] = getPPT();
		num_array[Constants.INDEX_RMVB] = getRMVB();
		num_array[Constants.INDEX_TXT] = getTXT();
		num_array[Constants.INDEX_OTHER] = getOthers();
		
		if(progress<100){
			progress = 100;
		}
		actHandler.sendEmptyMessage(Constants.MSG_PROGRESS_FULL);
	}
	
	private void clear(){
		progress  = 0;
		total_num = 0;
		doc = 0;
		pdf = 0;
		txt = 0;
		ppt = 0;
		jpg = 0;
		avi = 0;
		rmvb = 0;
		mpg = 0;
		mp3 = 0;
		other = 0;
	}
	
	/**
	 * Very strange
	 * @param f
	 */
	public void scan(File f){
		
		if(f.isDirectory()){
			
			File[] files = f.listFiles();
			if(files!=null){
				for(int i = 0; i<files.length; i++){
				
					scan(files[i]);
				
				}
			}
		}
		
		else{
			byte_count += f.length();
			progress   =  (int) (((float)byte_count/(float)used_space)*100);
			getFileType(f);
			if(actHandler!=null)
				actHandler.sendEmptyMessage(progress);
		}
		total_num++;
	}
	
	public void scan(FileNode node){
		
		if(node!=null){
		if(node.self.isDirectory()){
			total_num++;
			File [] files = node.self.listFiles();
			if(files!=null&&node.index<=files.length-1){
				if(files[node.index].isDirectory()){
					FileNode fn = new FileNode(node,files[node.index]);
					files = null;
					node.index++;
					scan(fn);
				}
				else{
					byte_count += files[node.index].length();
					getFileType(files[node.index]);
					files = null;
					progress   =  (int) (((float)byte_count/(float)used_space)*100);
					
					if(actHandler!=null)
						actHandler.sendEmptyMessage(progress);
					
					node.index++;
					scan(node);
				}
			}
			else{
				if(node.parent!=null){
					scan(node.parent);
					node = null;
				}
				else{
					return;
				}
			}
		}
		else{
			total_num ++ ;
			byte_count += node.self.length();
			progress   =  (int) (((float)byte_count/(float)used_space)*100);
			getFileType(node.self);
			
			if(actHandler!=null)
				actHandler.sendEmptyMessage(progress);
			
			if(node.parent!=null){
				scan(node.parent);
			}
			else
				return;
		}
		}
		else{
			
			return;
		}
	}
	
	private class FileNode{
		
		public FileNode(FileNode parent,File self){
			this.parent = parent;
			this.self = self;
		}
		
		public FileNode parent;
		public File self;
		public int  index = 0;
	}
	
	public long getTotalSpace(){
		return total_space;
	}
	
	public long getFreeSpace(){
		return free_space;
	}
	
	public long getUsedSpace(){
		return used_space;
	}
	
	public File getSDDir(){
		return sdDir;
	}
	
	public long getFileNum(){
		
		return total_num;
	}
	
	private void getFileType(File f){
		String name = f.getName();
		if(name.endsWith(Constants.FORMAT_PDF_L)){
			pdf++;
			pdfList.add(f);
		}
		else if(name.endsWith(Constants.FORMAT_JPG_L)||name.endsWith(Constants.FORMAT_JPG_U)){
			jpg++;
			jpgList.add(f);
		}
		else if(name.endsWith(Constants.FORMAT_MP3_L)||name.endsWith(Constants.FORMAT_MP3_U)){
			mp3++;
			mp3List.add(f);
		}
		else if(name.endsWith(Constants.FORMAT_PPT_L)||name.endsWith(Constants.FORMAT_PPT_U)
				||name.endsWith(Constants.FORMAT_PPTX_L)||name.endsWith(Constants.FORMAT_PPTX_U)){
			ppt++;
			pptList.add(f);
		}
		else if(name.endsWith(Constants.FORMAT_DOC_L)||name.endsWith(Constants.FORMAT_DOC_U)||
				name.endsWith(Constants.FORMAT_DOCX_L)||name.endsWith(Constants.FORMAT_DOCX_U)){
			doc++;
			docList.add(f);
		}
		else if(name.endsWith(Constants.FORMAT_RM_L)||name.endsWith(Constants.FORMAT_RM_U)||
				name.endsWith(Constants.FORMAT_RMVB_L)||name.endsWith(Constants.FORMAT_RMVB_U)){
			rmvb++;
			rmvbList.add(f);
		}
		else if(name.endsWith(Constants.FORMAT_TXT_L)||name.endsWith(Constants.FORMAT_TXT_U)){
			txt++;
			txtList.add(f);
		}
		else if(name.endsWith(Constants.FORMAT_MPG_L)||name.endsWith(Constants.FORMAT_MPG_U)||
				name.endsWith(Constants.FORMAT_MPEG4_L)||name.endsWith(Constants.FORMAT_MPEG4_U)){
			mpg++;
			mpgList.add(f);
		}
		else if(name.endsWith(Constants.FORMAT_AVI_L)||name.endsWith(Constants.FORMAT_AVI_U)){
			avi++;
			aviList.add(f);
		}
		else{
			other++;
		}
		
	}
	
	public int getDOC(){
		return doc;
	}
	
	public int getJPG(){
		return jpg;
	}
	public int getMP3(){
		return mp3;
	}
	public int getPPT(){
		return ppt;
	}
	public int getRMVB(){
		return rmvb;
	}
	public int getTXT(){
		return txt;
	}
	public int getMPG(){
		return mpg;
	}
	public int getAVI(){
		return avi;
	}
	public int getPDF(){
		return pdf;
	}
	public int getOthers(){
		return other;
	}
	
	public int[] getTypeNum(){
		return num_array;
	}
	
	/**
	 * shit! String compare must use equals() function
	 * @return
	 */
	public boolean sdMount(){
		
		return (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
		
	}

	//将文件复制到指定目录
	public void copyFile(String type,String dir){
		
	}
	
	//将文件移动到指定目录
	public void moveFile(String type,String dir){
		
	}
	
	//添加指定类型的文件到统计类型
	public void addFileType(String type){
		
	}
	
	//搜索指定的文件
	public void searchFile(String fileName){
		
	}

	public void registerObservers() {
		// TODO Auto-generated method stub
		
	}


	public void unregisterObservers() {
		// TODO Auto-generated method stub
		
	}


	public void finish() {
		// TODO Auto-generated method stub
		
	}


	public void firstRun() {
		// TODO Auto-generated method stub
		
	}


	public void stop() {
		// TODO Auto-generated method stub
		this.actHandler = null;
	}


	public void setActHandler(Handler handler) {
		// TODO Auto-generated method stub
		this.actHandler = handler;
	}
	
	public String[] getFileString(List<File> files){
		
		if(files!=null){
			String[] result = new String[files.size()];
			for(int i = 0;i<files.size();i++){
				result[i] = files.get(i).getName();
			}
			return result;
		}
		
		return null;
	}
	
}
