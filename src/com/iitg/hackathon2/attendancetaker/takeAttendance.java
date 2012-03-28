package com.iitg.hackathon2.attendancetaker;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class takeAttendance extends Activity implements OnClickListener {

    private String rollno;
	private Studentsdb myStudents;
	private Attendancedb myAttendance;
	private Button nextScan, exitScans;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_attendance);
		
		nextScan = (Button) findViewById(R.id.nextScan);
		exitScans = (Button) findViewById(R.id.exitScans);
		nextScan.setEnabled(true);
		exitScans.setEnabled(true);
		
	}
	
	private int studentEnrolled(String rollno) {
		int ret=0;
		myStudents=new Studentsdb(this);
		myStudents.open();
		Cursor results=myStudents.getRollno(Integer.parseInt(rollno));
		if(results.getCount()>0){
			ret=1;
		} else {
			ret=0;
		}
		myStudents.close();
		results.close();
		return ret;
	}

	@Override
	public void onClick(View clickedView) {
		int viewId=clickedView.getId();
		switch(viewId){
		case R.id.exitScans:
			finish();
			break;
		case R.id.nextScan:
			scanProduct();
			break;
		}
	}

	private boolean submitValues() {
		ContentValues submissionValues = new ContentValues();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); 
		Date date = new Date();		
		submissionValues.put("rollno", Integer.parseInt(rollno));
		submissionValues.put("date", Integer.parseInt(dateFormat.format(date)));
		myAttendance = new Attendancedb(this);
		myAttendance.open();
		if(myAttendance.insert(submissionValues)){
			myAttendance.close();
			return true;
		} else {
			Toast.makeText(this, "Attendance taken twice.", Toast.LENGTH_SHORT).show();
			myAttendance.close();
			return false;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 10) {
	        if (resultCode == RESULT_OK) {
	        	rollno = intent.getStringExtra("SCAN_RESULT");
	    		if(studentEnrolled(rollno)==1){
	    			if(submitValues()){
	    				myAttendance = new Attendancedb(this);
	    				myAttendance.open();
	    				Cursor entry = myAttendance.checkAttendance(Integer.parseInt(rollno));
	    				entry.moveToFirst();
	    				Integer attended = entry.getInt(0);
	    				entry.close();
	    				myAttendance.close();
	    				myAttendance = new Attendancedb(this);
	    				myAttendance.open();
	    				entry = myAttendance.checkAttendance(Integer.parseInt(rollno));
	    				entry.moveToFirst();
	    				Integer total = entry.getInt(0);
	    				entry.close();
	    				myAttendance.close();
						Toast.makeText(this, rollno+": "+attended+"/"+total, Toast.LENGTH_SHORT).show();	    				
	    			}
	    		} else {
					Toast.makeText(this, "Student "+rollno+" not registered.", Toast.LENGTH_SHORT).show();
				}
	    	} else if (resultCode == RESULT_CANCELED) {
	    		Toast.makeText(this, "Scan failed. Returning to previous screen.", Toast.LENGTH_LONG).show();
	    	}
	    }
	}
	

	private void scanProduct(){
			Intent scanproduct = new Intent("com.google.zxing.client.android.SCAN");
			scanproduct.setPackage("com.google.zxing.client.android");
			scanproduct.putExtra("SCAN_MODE","ONE_D_MODE");
			startActivityForResult(scanproduct, 10);
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    if (myStudents!=null){
	    	myStudents.close();
	    }
	    if(myAttendance!=null){
	    	myAttendance.close();
	    }
	}
}
