package com.example.superiot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class BdSQLiteHelper extends SQLiteOpenHelper {
	
	public BdSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public void onCreate(SQLiteDatabase db){
		db.execSQL("create table bdnetdata(_id integer primary key autoincrement,device text,la text,lon text,ns text,ew text,tem text,hum text,time text)");
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int NewVersion){
		
		
	}

}
