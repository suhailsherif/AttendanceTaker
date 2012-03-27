package com.iitg.hackathon2.attendancetaker;

import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Attendancedb {

	private Context pContext;
	private dbOpenHelper myDbOpenHelper;
	private String DB_NAME="attendance_db";
	private int DB_VERSION=1;
	private SQLiteDatabase mydb;
	private String TABLE_NAME="attendance";
	private String ROLL_NO="rollno";
	private String DATE="date";
	
	Attendancedb(Context ctx){
		pContext=ctx;
	}
	
	public void close(){
		if(myDbOpenHelper != null)	myDbOpenHelper.close();
	}

	public void open(){
		myDbOpenHelper = new dbOpenHelper(pContext);
		mydb=myDbOpenHelper.getWritableDatabase();
	}
	
	public void clearAll(){
		mydb.execSQL("DELETE FROM "+TABLE_NAME+";");
	}
	
	public boolean insert(ContentValues cv){
		if(mydb.insert(TABLE_NAME, null, cv)<0){
			return false;
		} else return true;
	}
	
	public boolean delete(Vector<Integer> ids){
		int count = ids.size();
		Log.w("1", ids.toString());
		String where_clause="rollno IN (";
		if(count==0)	return true;
		for(int i=0; i<count; i++){
			where_clause=where_clause+"\""+ids.get(i)+"\"";
			if(i!=count-1)	where_clause=where_clause+", ";
			else			where_clause=where_clause+")";
		}
		if(mydb.delete(TABLE_NAME, where_clause, null)==1) return true;
		else return false;	
	}
	
	public Cursor checkAttendance(Integer rollno){
		String[] columns={"COUNT(*)"};
		String where_clause=ROLL_NO+"="+rollno;
		return mydb.query(TABLE_NAME, columns, where_clause, null, null, null, null, null);
	}

	
	public Cursor fetchall(){
		String[] columns={ROLL_NO, "COUNT(*)"};
		return mydb.query(TABLE_NAME, columns, null, null, ROLL_NO, null, null, null);
	}

	public Cursor totalclass(){
		String[] columns={"count(distinct(date))"};
		return mydb.query(TABLE_NAME, columns, null, null, null, null, null, null);
	}

	
	private class dbOpenHelper extends SQLiteOpenHelper {

		private SQLiteDatabase stocksheet;
//		private Context helperContext;
		
		dbOpenHelper(Context ctx) {
			super(ctx, DB_NAME, null, DB_VERSION);
//			helperContext=ctx;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			stocksheet = db;
			stocksheet.execSQL("CREATE TABLE "+TABLE_NAME+" ("+ROLL_NO+" INTEGER, "+DATE+" INTEGER, UNIQUE("+ROLL_NO+","+DATE+"));" );
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}

}