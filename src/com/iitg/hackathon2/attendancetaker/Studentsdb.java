package com.iitg.hackathon2.attendancetaker;


import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Studentsdb {

	private String DB_NAME="students_db";
	private String TABLE_NAME="students";
	private int DB_VERSION=1;
	private String ROLL_NO="rollno";
	private SQLiteDatabase mydb;
	private dbOpenHelper myDbOpenHelper;
	private Context dbContext;

	public Studentsdb(Context context) {
		dbContext = context;
	}

	public void open() {
		myDbOpenHelper = new dbOpenHelper(dbContext);
		mydb = myDbOpenHelper.getReadableDatabase();
	}
		
	public void close(){
		if(myDbOpenHelper != null)	myDbOpenHelper.close();
	}
		
	public Cursor getRollno(Integer rollno){
		String[] columns={ROLL_NO};
		String where_clause=ROLL_NO+"=\""+rollno+"\"";
		return mydb.query(TABLE_NAME, columns, where_clause, null, null, null, null);
	}

	public Cursor fetchall(){
		String[] columns={ROLL_NO};
		return mydb.query(TABLE_NAME, columns, null, null, null, null, "rollno ASC", null);
	}

	public boolean insert(ContentValues cv){
		if(mydb.insert(TABLE_NAME, null, cv)<0){
			return false;
		} else return true;
	}

	public boolean delete(Vector<Integer> ids){
		Attendancedb attend = new Attendancedb(dbContext);
		attend.open();
		attend.delete(ids);
		attend.close();
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
	
	private class dbOpenHelper extends SQLiteOpenHelper {

//		private Context myContext;
		private SQLiteDatabase privateDb;

		dbOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
//			myContext= context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.w("In onCreate()", "Reached onCreate");
			privateDb = db;
			Log.w("In onCreate",db.toString());
			privateDb.execSQL("CREATE TABLE "+TABLE_NAME+" ("+ROLL_NO+" integer primary key);");
		}
		
			@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}

	}
}
