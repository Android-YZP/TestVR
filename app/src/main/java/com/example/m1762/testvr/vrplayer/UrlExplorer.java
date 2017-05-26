package com.example.m1762.testvr.vrplayer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.m1762.testvr.MainActivity;
import com.example.m1762.testvr.R;
import com.example.m1762.testvr.vrplayer.utils.SPUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class UrlExplorer {
	private static EditText m_edtInputURL			= null;
	private static View m_view					= null;
	private static ListView m_lvURL					= null;
	private static ArrayList<String> m_lstSelectURL		= null;
	private static Dialog m_dialog				= null;
	private static Context m_context 				= null;
	private static MainActivity m_activity				= null;
	private static String mUrl;
	private static final String TAG  = "UrlExplorer";
	
	 public static void showFileListWindow(Context context){
		 
		m_context = context;
		m_activity = (MainActivity) context;
		initLayout();
		
		readURL();
		
		createDialog();
		
	 }
	
	 private static TextWatcher watcher = new TextWatcher() {

	        @Override
	        public void afterTextChanged(Editable arg0) {
	            // TODO Auto-generated method stub
	        	mUrl = m_edtInputURL.getText().toString();
	        }

	        @Override
	        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
	            // TODO Auto-generated method stub
	        }

	        @Override
	        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
	            // TODO Auto-generated method stub
	        }
	    };
	
	private static void initLayout()
	{
		LayoutInflater inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_view = inflater.inflate(R.layout.url_explorer, null,false);
        m_edtInputURL = (EditText)m_view.findViewById(R.id.edtInputURL);
        m_lvURL = (ListView)m_view.findViewById(R.id.urllist);
        
		m_edtInputURL.addTextChangedListener(watcher);
		m_edtInputURL.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility") @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                m_edtInputURL.requestFocusFromTouch();
                return false;
            }
        });
		
		m_lvURL.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
            	String url=arg0.getItemAtPosition(arg2).toString();
                m_edtInputURL.setText(url);
                

            }
        }); 
	}
	
	private static void readURL() 
	{
	     m_lstSelectURL = null;
	     m_lstSelectURL = new ArrayList<String>();

        mUrl = (String) SPUtils.get( m_context, Definition.HISTORY_URL, "rtmp://live.feicui.rtmp.woniucloud.com/live/test");

        m_edtInputURL.setText(mUrl);

	     m_lstSelectURL.add("rtmp://live.feicui.rtmp.woniucloud.com/live/test");
         m_lstSelectURL.add("http://live.feicui.flv.woniucloud.com/live/test.flv");
         m_lstSelectURL.add("http://123.206.89.244/ty/10752.mp4");
         m_lstSelectURL.add("http://123.206.89.244/ty/10752.jpg");
         m_lstSelectURL.add("http://media.qicdn.detu.com/@/70955075-5571-986D-9DC4-450F13866573/2016-05-19/573d15dfa19f3-2048x1024.m3u8");
         m_lstSelectURL.add("http://9250.liveplay.myqcloud.com/live/9250_fe9c733029.m3u8");

	     // load from local file
//	     ReadUrlInfo(m_context,m_lstSelectURL);
//	     getLocalFiles(m_lstSelectURL, "/sdcard/SamplePlayer");
//	     getDownloadFiles(m_lstSelectURL, "/sdcard/SamplePlayer/Downloader");
	     ArrayAdapter<String> adapter = new ArrayAdapter<String>(m_context,
	             android.R.layout.simple_list_item_1, m_lstSelectURL);
	     m_lvURL.setAdapter(adapter);
	}
	
	private static void createDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
		
		builder.setView(m_view);
		
		builder.setNegativeButton("Cancel", null);
		/*************************************************************进入播放页面的*****************************************/
		builder.setPositiveButton("Start", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent i = new Intent(m_context,PlayActivity.class);
				i.putExtra(Definition.KEY_PLAY_URL, mUrl);
                SPUtils.put(m_context,Definition.HISTORY_URL,mUrl);
				m_context.startActivity(i);
                m_dialog.dismiss();
			}
			
		}
		);
		m_dialog = builder.create();
		m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		m_dialog.show();
        m_dialog.getWindow().setLayout(1500,1000);

    }
	
	public static void ReadUrlInfo(Context context, ArrayList<String> listUrl){
        String str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/url.txt";
        if (false == ReadUrlInfoToList(listUrl, str))
        	ReadAssetUrlInfoToList(context, listUrl);
        if (listUrl.isEmpty() == true)
        	Toast.makeText(context, "Could not find url ", Toast.LENGTH_LONG).show();
    }
	
	/* Retrieve list of media sources */
    public static boolean ReadUrlInfoToList(List<String> listUrl, String configureFile) {
        String sUrl,line = "";
        sUrl = configureFile;
        File UrlFile = new File(sUrl);
        
        if (!UrlFile.exists())
            return false;
        
        FileReader fileread;

        try {
            fileread = new FileReader(UrlFile);
            BufferedReader bfr = new BufferedReader(fileread);
            try {
                while (line != null) {
                    line = bfr.readLine();
                    if (line !=null)
                        listUrl.add(line);
                }
              
                fileread.close();
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return true;
    }
    
    /* Retrieve list of media sources */
    public static boolean ReadAssetUrlInfoToList(Context context, List<String> listUrl) {

    	String line = "";
        try {
        	InputStream in = context.getResources().getAssets().open("url.txt");
        	if (in == null)
        		return false;
            BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
            try {
                while (line != null) {
                    line = bfr.readLine();
                    if (line !=null)
                        listUrl.add(line);
                }
              
                in.close();
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return true;
    }
    
    public static void getLocalFiles(ArrayList<String> list, String url){
        File files = new File(url);
        File[] file = files.listFiles();
        if(file == null){
            return;
        }
        try {
            for (File f : file) {
            if (!f.isDirectory()) { 
               list.add(f.getPath());
                 }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void getDownloadFiles(ArrayList<String> list, String url){
        File files = new File(url);
        File[] file = files.listFiles();
        if(file == null){
            return;
        }
        try {
            for (File f : file) {
            if (f.isDirectory()) { 
               File[] videoFile = f.listFiles();
               int i = 0;
               for(File vf : videoFile){
                   if(vf.getPath().contains("Master.m3u8")){
                       list.add(vf.getPath());
                       i = 1;
                   }
               }
               if(i == 0){
                   for(File vf : videoFile){
                       if(vf.getPath().contains("Video.m3u8"))
                           list.add(vf.getPath());
                   }
               }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
