package com.iitg.hackathon2.attendancetaker;

import java.util.Vector;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class studentsView extends Activity implements OnClickListener {

	private Studentsdb myStudents;
	private Cursor entries = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.studentsview);
		myStudents = new Studentsdb(this);
		myStudents.open();
		entries=myStudents.fetchall();
		entries.moveToFirst();
		loadResults(entries);
	}

	private void loadResults(Cursor results) {
		if(results == null){
			Toast.makeText(this, "Query not called", Toast.LENGTH_SHORT).show();
			return;
		}
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.studentsLayout);
		for(int ij=0;;ij++){
			if(results.isAfterLast()){
				results.deactivate();
				results.close();
				TextView j = new TextView(this);
				j.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				j.setText("End of student list");
				j.setGravity(Gravity.CENTER);
				mainLayout.addView(j);
				return;
			} else {
				StudentEntry entry = new StudentEntry(this);
				entry.populate(results);
				mainLayout.addView(entry.getEntry());
				results.moveToNext();
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		LinearLayout mainLayout = null;
		int viewid=arg0.getId();
		switch(viewid){
		case R.id.back:
			if(entries != null)	entries.close();
			if(myStudents != null)	myStudents.close();
			finish();
			break;
		case R.id.deleteEntry:
			if(entries != null)	entries.close();

			Vector<Integer> checked = new Vector<Integer>();			
			mainLayout = (LinearLayout) findViewById(R.id.studentsLayout);
			int childCount = mainLayout.getChildCount();
			for(int i=0;i<childCount-1;i++){
				LinearLayout singleEntry = (LinearLayout) mainLayout.getChildAt(i);
				CheckBox cb = (CheckBox) singleEntry.getChildAt(0);
				TextView tv = (TextView) singleEntry.getChildAt(1);
				if(cb.isChecked()){
					checked.add(Integer.parseInt(tv.getText().toString()));
				}
			}
			myStudents = new Studentsdb(this);
			myStudents.open();
			myStudents.delete(checked);
			mainLayout.removeAllViews();
			entries=myStudents.fetchall();
			entries.moveToFirst();
			loadResults(entries);
            myStudents.close();
			break;
		case R.id.addRollno:
			if(entries != null)	entries.close();
			ContentValues cv = new ContentValues();
			EditText rollno = (EditText) findViewById(R.id.number);
			cv.put("rollno", Integer.parseInt(rollno.getText().toString()));
			myStudents = new Studentsdb(this);
			myStudents.open();
			if(myStudents.insert(cv)){
				mainLayout = (LinearLayout) findViewById(R.id.studentsLayout);
				mainLayout.removeAllViews();
				entries=myStudents.fetchall();
				entries.moveToFirst();
				loadResults(entries);
				entries.close();
				myStudents.close();
			}
			break;
		}
	}
}
