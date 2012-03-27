package com.iitg.hackathon2.attendancetaker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StudentEntry extends LinearLayout{

	private LinearLayout baseLayout;
	
	public StudentEntry(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		baseLayout = (LinearLayout) inflater.inflate(R.layout.student_entry, null);
	}
	
	public void populate(Cursor entries){
		CheckBox checkbox = (CheckBox) baseLayout.findViewWithTag("id");
		checkbox.setId(entries.getInt(0)+10000);
		TextView location = (TextView) baseLayout.findViewWithTag("rollno");
		location.setText(((Integer)entries.getInt(0)).toString());
	}
	
	public LinearLayout getEntry(){
		return baseLayout;
	}
}