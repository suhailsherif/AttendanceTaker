package com.iitg.hackathon2.attendancetaker;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class attendanceView extends Activity implements OnClickListener {

	private Attendancedb myAttendance;
	private Cursor entries = null;
	private Integer total_classes = 0;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.attendanceview);
		myAttendance = new Attendancedb(this);
		myAttendance.open();
		entries=myAttendance.totalclass();
		if(entries.getCount()<1){
			Toast.makeText(this, "Empty table.", Toast.LENGTH_SHORT);
			entries.close();
			myAttendance.close();
			finish();
		}
		entries.moveToFirst();
		total_classes=entries.getInt(0);
		entries.close();
		myAttendance.close();
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.uglyhack);
		TextView j = new TextView(this);
		j.setText("Total number of classes: "+total_classes);
		j.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mainLayout.addView(j, 0);
		myAttendance = new Attendancedb(this);
		myAttendance.open();
		entries=myAttendance.fetchall();
		entries.moveToFirst();
		loadResults(entries);
	}

	private void loadResults(Cursor results) {
		if(results == null){
			Toast.makeText(this, "Query not called", Toast.LENGTH_SHORT).show();
			return;
		}
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.attendanceLayout);
		for(int ij=0;;ij++){
			if(results.isAfterLast()){
				results.deactivate();
				results.close();
				TextView j = new TextView(this);
				j.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				j.setText("End of attendance list");
				j.setGravity(Gravity.CENTER);
				mainLayout.addView(j);
				return;
			} else {
				AttendanceEntry entry = new AttendanceEntry(this);
				entry.populate(results, total_classes);
				mainLayout.addView(entry.getEntry());
				results.moveToNext();
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int viewid=arg0.getId();
		switch(viewid){
		case R.id.back:
			if(entries != null)	entries.close();
			if(myAttendance != null)	myAttendance.close();
			finish();
			break;
		}
	}

}
