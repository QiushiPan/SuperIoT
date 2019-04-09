package com.example.superiot;

import android.app.Activity;
import android.os.Bundle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.Cursor;
import android.widget.Button;
import java.text.SimpleDateFormat;   
import java.sql.Date;   

public class detailActivity extends Activity{
	
	private MySQLiteHelper mySQLiteHelper;
	private SQLiteDatabase db;
	private ListView       lv;
	private TextView       tv;
	private List<Map<String,Object>> list;
	private Cursor cursor;
	private int mywhich;
	private int myid;
	private String title;
	private String content;
	private EditText et01;
	private EditText et02;
	private Button ClearTable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.detail);
		 
		 tv=(TextView) findViewById(R.id.TextView01);
		 lv=(ListView) findViewById(R.id.ListView01);

		
		mySQLiteHelper=new MySQLiteHelper(this, "test.db", null, 1);
//		//Êý¾Ý¿â
		 db=mySQLiteHelper.getReadableDatabase();
		 cursor=db.query("mynattiertable", new String[]{"_id","node","temp","hum","light","air","time"}, null, null, null, null, null);
		if(cursor.getCount()>0){
		tv.setVisibility(View.GONE);
		}
    	SimpleCursorAdapter sca=new SimpleCursorAdapter(this, R.layout.item, cursor, new String[]{"node","temp","hum","light","air","time"}, new int[]{R.id.TextView02,R.id.TextView03,R.id.TextView04,R.id.TextView05,R.id.TextView06,R.id.TextView07});
		lv.setAdapter(sca);
		
		
	}
}
