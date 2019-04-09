package com.example.superiot;

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

import com.baidu.mapapi.SDKInitializer;

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

public class MainActivity extends Activity {

	private EditText usernametext,passwordtext;
	private Button loginbutton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		
		loginbutton = (Button) findViewById(R.id.loginbutton);
		usernametext = (EditText) findViewById(R.id.usernametext);
		passwordtext = (EditText) findViewById(R.id.passwordtext);
		
		loginbutton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(){  //�����������̷߳������磬����ŵ����߳�
					@Override  
					public void run()  
					{  
						connectPost("http://39.104.67.26:80/phpforandroid/clientlogin.php");    //���Ե�ַ//��������ʵĴ����������     
					}  
					}.start(); 
					Toast.makeText(MainActivity.this, "��¼", Toast.LENGTH_LONG).show();

			}
        });
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    private void connectPost(String url){
    	
  	  HttpClient httpClient = new DefaultHttpClient();    // �½�HttpClient����
  	  HttpPost httpPost = new HttpPost(url);    // �½�HttpPost����
  	  List<NameValuePair> params = new ArrayList<NameValuePair>();  //ʹ��NameValuePair������Ҫ���ݵ�Post����
  	  params.add(new BasicNameValuePair("username", usernametext.getText().toString()));    //���Ҫ���ݵĲ���
  	  params.add(new BasicNameValuePair("password", passwordtext.getText().toString()));   
  	  //params.add(new BasicNameValuePair("username", "USTB"));    //���Ҫ���ݵĲ���
  	  //params.add(new BasicNameValuePair("password", "iot1024"));   
			try {
				 HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);  // �����ַ���
	    	     httpPost.setEntity(entity);    // ���ò���ʵ��
	    	     HttpResponse httpResp = httpClient.execute(httpPost); // ��ȡHttpResponseʵ��
	    		if(httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  //��Ӧͨ��
	    			String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");   
	    			
	    			if( result.equals("tybndf") == true)
	    			{
		    			Intent httpclientIntent = new Intent();
		    			httpclientIntent.setClass(getApplicationContext(), functionActivity.class);
		    			startActivity(httpclientIntent);
	    			}
	    				
	    			
	    		}else{
	    			                                               //��Ӧδͨ��
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
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
