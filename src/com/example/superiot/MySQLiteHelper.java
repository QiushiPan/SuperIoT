package com.example.superiot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class MySQLiteHelper extends SQLiteOpenHelper {
	
	public MySQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public void onCreate(SQLiteDatabase db){
		db.execSQL("create table nattiernetdata(_id integer primary key autoincrement,node text,temp text,hum text,light text,air text,time text)");
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int NewVersion){
		
		
	}

}

