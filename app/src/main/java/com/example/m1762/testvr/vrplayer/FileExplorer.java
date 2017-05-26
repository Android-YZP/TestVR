package com.example.m1762.testvr.vrplayer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup.LayoutParams;

import com.example.m1762.testvr.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者: 姚中平  时间:2017年5月24日17:31:03
 * 描述: 提供文件相关操作的静态方法
 */
public class FileExplorer {
	private static ListView m_lvFile = null;
	private static ArrayList<String> m_listUrl = null;
	private static File m_current = null;
	private static AlertDialog m_dialog = null;
	private static Context m_context = null;
	private static MainActivity m_activity = null;

	public static void showFileListWindow(Context context) {
		m_context = context;
		m_activity = (MainActivity) context;
		m_listUrl = new ArrayList<String>();

		initLayout();

		if (m_current == null)
			m_current = new File(Environment.getExternalStorageDirectory()
					.getPath());
		if (!m_current.exists()) {
			m_current = new File("/");
		}

		updateFileList();

		AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
		builder.setView(m_lvFile);
		builder.setNegativeButton("Cancel", null);
		m_dialog = builder.create();
		m_dialog.setTitle(m_current.getAbsolutePath());
		m_dialog.show();
	}

	private static void updateFileList() {
		String urls[] = m_current.list();
 		m_listUrl.clear();

		String current = m_current.getAbsolutePath();
		if (!current.equals("/"))
			m_listUrl.add("..");
	    
		if (urls == null) 
	        	return;
		
		for (int i = 0; i < urls.length; i++) {
			m_listUrl.add(urls[i]);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(m_context,
				android.R.layout.simple_list_item_1, m_listUrl);
		m_lvFile.setAdapter(adapter);
	}

	private static void initLayout() {
		m_lvFile = new ListView(m_context);
		m_lvFile.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		m_lvFile.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
				String url = arg0.getItemAtPosition(arg2).toString();

				String fullPath = m_current.getAbsolutePath() + "/" + url;
				File select = new File(fullPath);
				if (url.equals("..")) {
					String parent = m_current.getParent();
					if (parent.equals("/storage/emulated"))
						return;
					m_dialog.setTitle(parent);
					m_current = new File(parent);
				} else {
					if (!select.exists() || !select.canRead())
						return;
					String path = select.getAbsolutePath();
					m_dialog.setTitle(path);
					if (!select.isDirectory()) {
						boolean isMediaFile = checkFileExt(path);
						if (isMediaFile) {
							// 
							Intent i = new Intent(m_context,PlayActivity.class);
							i.putExtra(Definition.KEY_PLAY_URL, path);
							m_context.startActivity(i);
							m_dialog.dismiss();
						}
						return;
					}

					m_current = select;
				}

				if (m_current.isDirectory()) {
					updateFileList();
				}
			}
		});

	}

	@SuppressLint("DefaultLocale")
	public static boolean checkFileExt(String str) {
		Pattern audioPattern = Pattern
				.compile(
						".+\\.(mp3|amr|aac|wma|m4a|wav|ec3|ac3|mp2|ogg|ra|isma|flac|evrc|qcelp|pcm|adpcm|au|awb)$",
						Pattern.CASE_INSENSITIVE);
		Pattern videoPattern = Pattern
				.compile(
						".+\\.(avi|asf|rm|rmvb|mp4|m4v|3gp|3g2|wmv|3g2|mpg|mpeg|qt|mkv|flv|mov|asx|m3u8|m3u|manifest|mpd|ts|webm|ismv|ismc|k3g|sdp|265|h265|264|h264|m2ts|dts|dtshd)$",
						Pattern.CASE_INSENSITIVE);
		Pattern imagePattern = Pattern.compile(".+\\.(jpg|jpeg|png)$",
				Pattern.CASE_INSENSITIVE);

		Matcher matcher = audioPattern.matcher(str);
		if (matcher.find()) {
			return true;
		}

		matcher = videoPattern.matcher(str);
		if (matcher.matches()) {
			return true;
		}

		matcher = imagePattern.matcher(str);
		if (matcher.matches()) {
			return true;
		}

		String strTest = str.toLowerCase();

		if (strTest.startsWith("mtv://") == true)
			return true;

		if (strTest.endsWith("/manifest") == true)
			return true;

		if (strTest.contains("/manifest?") == true)
			return true;

		if (strTest.contains("m3u8") == true)
			return true;

		if (strTest.contains("m3u?") == true)
			return true;

		int index = strTest.lastIndexOf("_");
		if (index != -1) {
			strTest = strTest.substring(index);
			if (strTest.length() == 5) {

				if (isNumeric(strTest.substring(1))) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
}
