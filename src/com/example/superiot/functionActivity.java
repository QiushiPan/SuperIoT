package com.example.superiot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.HashMap;   
import java.util.Map; 
import android.database.Cursor;

public class functionActivity  extends Activity{
	
	private Button sensorbutton,beidoubutton;
	private MySQLiteHelper mySQLiteHelper;
	private BdSQLiteHelper bdSQLiteHelper;
	private SQLiteDatabase db;
	private SQLiteDatabase bddb;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.function);

		sensorbutton = (Button) findViewById(R.id.sensorbutton);
		beidoubutton = (Button) findViewById(R.id.beidoubutton);
		
		MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(this, "test.db",null,1);
		db = mySQLiteHelper.getReadableDatabase();
		SQLiteDatabase dbd=openOrCreateDatabase("test.db", 0, null);  
		Cursor c=dbd.rawQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='mynattiertable'", null); 
		if(c.getCount()==0){
			db.execSQL("create table mynattiertable(_id integer primary key autoincrement,node text,temp text,hum text,light text,air text,time text)");  
			ContentValues cv=new ContentValues();
				cv.put("node", "");
				cv.put("temp","");
				cv.put("hum","");
				cv.put("light","");
				cv.put("air","");  
				db.insert("mynattiertable", null, cv);
			}  
		c.close();
			
		BdSQLiteHelper bdSQLiteHelper = new BdSQLiteHelper(this, "test.db",null,1);
		bddb = bdSQLiteHelper.getReadableDatabase();
		SQLiteDatabase tdbd=openOrCreateDatabase("test.db", 0, null);  
		Cursor ct=tdbd.rawQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='mybdtable'", null); 
		if(ct.getCount()==0){
			bddb.execSQL("create table mybdtable(_id integer primary key autoincrement,device text,la text,lon text,ns text,ew text,tem text,hum text,time text)");  
			ContentValues cv=new ContentValues();
				cv.put("device", "");
				cv.put("la","");
				cv.put("lon","");
				cv.put("ns","");
				cv.put("ew",""); 
				cv.put("tem","");
				cv.put("hum","");   
				bddb.insert("mybdtable", null, cv);
			}  
		ct.close();
		
		sensorbutton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(){  //不允许在主线程访问网络，代码放到副线程
					@Override  
					public void run()  
					{  
						connectPostsensor("http://39.104.67.26:80/phpforandroid/sensorback.php");    //测试地址//把网络访问的代码放在这里     
					}  
					}.start(); 

			}
        });
		beidoubutton .setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(){  //不允许在主线程访问网络，代码放到副线程
					@Override  
					public void run()  
					{  
						connectPostbd("http://39.104.67.26:80/phpforandroid/bdback.php");    //测试地址//把网络访问的代码放在这里     
					}  
					}.start(); 

			}
        });
	}
	
	private void connectPostsensor(String url){
    	
	  	  HttpClient httpClient = new DefaultHttpClient();    // 新建HttpClient对象
	  	  HttpPost httpPost = new HttpPost(url);    // 新建HttpPost对象
	  	  List<NameValuePair> params = new ArrayList<NameValuePair>();  //使用NameValuePair来保存要传递的Post参数
	  	  params.add(new BasicNameValuePair("username", ""));    //添加要传递的参数
	  	  params.add(new BasicNameValuePair("password", ""));   
	  	  try {
	  		  HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);  // 设置字符集
	  		  httpPost.setEntity(entity);    // 设置参数实体
	  		  HttpResponse httpResp = httpClient.execute(httpPost); // 获取HttpResponse实例
	  		  if(httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  //响应通过
	  			  String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");   
	  			  if( result.charAt(0)>='0'&&result.charAt(0)<='9')
	  			  {
	  					db.delete("mynattiertable", null, null);
	  				  for(int i = 0;i < result.length()/12;i++)
	  				  {
	  					  SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日    HH:mm:ss     ");
	  					  Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  					  String    timestr    =    formatter.format(curDate);
	  					  ContentValues cv=new ContentValues();
		    				cv.put("node", result.substring(i*12+0, i*12+2));
		    				cv.put("temp",result.substring(i*12+2, i*12+4));
		    				cv.put("hum",result.substring(i*12+4, i*12+6));
		    				cv.put("light",result.substring(i*12+6, i*12+9));
		    				cv.put("air",result.substring(i*12+9, i*12+12));
		    				cv.put("time",timestr);
		    				db.insert("mynattiertable", null, cv);
		    				}
	  				  Intent httpclientIntent = new Intent();
	  				  httpclientIntent.setClass(this, detailActivity.class);
	  				  startActivity(httpclientIntent);
	  				  }else{
	  					  
	  				  }
	  			  }else{
   			                                               //响应未通过
	  				  }
	  		  } catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
	  }
	
	private void connectPostbd(String url){
    	
	  	  HttpClient httpClient = new DefaultHttpClient();    // 新建HttpClient对象
	  	  HttpPost httpPost = new HttpPost(url);    // 新建HttpPost对象
	  	  List<NameValuePair> params = new ArrayList<NameValuePair>();  //使用NameValuePair来保存要传递的Post参数
	  	  params.add(new BasicNameValuePair("username", ""));    //添加要传递的参数
	  	  params.add(new BasicNameValuePair("password", ""));   
				try {
					HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);  // 设置字符集
			  		  httpPost.setEntity(entity);    // 设置参数实体
			  		  HttpResponse httpResp = httpClient.execute(httpPost); // 获取HttpResponse实例
			  		  if(httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  //响应通过
			  			  String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");   
			  			  if( result.charAt(0)>='0'&&result.charAt(0)<='9')
			  			  {
			  					bddb.delete("mybdtable", null, null);
			  				  for(int i = 0;i < result.length()/30;i++)
			  				  {
			  					  SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日    HH:mm:ss     ");
			  					  Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
			  					  String    timestr    =    formatter.format(curDate);
			  					  ContentValues cvs=new ContentValues();
			  					  cvs.put("device",result.substring(i*30+0, i*30+7));
			  					  cvs.put("la",result.substring(i*30+7, i*30+15));
			  					  cvs.put("lon",result.substring(i*30+15, i*30+24));
			  					  cvs.put("ns",result.substring(i*30+24, i*30+25));
			  					  cvs.put("ew",result.substring(i*30+25, i*30+26)); 
			  					  cvs.put("tem",result.substring(i*30+26, i*30+28));
			  					  cvs.put("hum",result.substring(i*30+28, i*30+30));
			  					  cvs.put("time",timestr);
				    				bddb.insert("mybdtable", null, cvs);
				    				}
			  				  Intent httpclientIntent = new Intent();
			  				  httpclientIntent.setClass(this, bddetailActivity.class);
			  				  startActivity(httpclientIntent);
			  				  }else{
			  					  
			  				  }
			  			  }else{
		   			                                               //响应未通过
			  				  }
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
	  }

}
