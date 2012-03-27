package com.iitg.hackathon2.attendancetaker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AttendanceEntry extends LinearLayout{

	private LinearLayout baseLayout;
	
	public AttendanceEntry(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		baseLayout = (LinearLayout) inflater.inflate(R.layout.attendance_entry, null);
	}
	
	public void populate(Cursor entries, Integer total_classes){
		TextView rollno = (TextView) baseLayout.findViewWithTag("rollno");
		rollno.setText(((Integer)entries.getInt(0)).toString());
		TextView no_classes = (TextView) baseLayout.findViewWithTag("no_classes");
		no_classes.setText(((Integer)entries.getInt(1)).toString()+"/"+total_classes);
		TextView percentage = (TextView) baseLayout.findViewWithTag("percentage");
		Integer perc= (entries.getInt(1)/total_classes ) *100;
		percentage.setText(perc.toString());
	}
	
	public LinearLayout getEntry(){
		return baseLayout;
	}
}