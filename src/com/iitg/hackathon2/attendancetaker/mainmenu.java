package com.iitg.hackathon2.attendancetaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class mainmenu extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
    }

	@Override
	public void onClick(View clickedView) {
		// TODO Auto-generated method stub
		int viewId=clickedView.getId();
		switch(viewId){
			case R.id.scan:
				Intent scanproduct = new Intent(this, takeAttendance.class);
				startActivity(scanproduct);
				break;
			case R.id.view_att:
				Intent stocksheetint = new Intent(this, attendanceView.class);
				startActivity(stocksheetint);
				break;
			case R.id.edit_stud:
				Intent catalogint = new Intent(this, studentsView.class);
				startActivity(catalogint);
				break;
			case R.id.exit:
				finish();
				break;
		}
	}
}