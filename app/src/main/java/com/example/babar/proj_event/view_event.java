package com.example.babar.proj_event;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class view_event extends AppCompatActivity {
    TextView tv_event_name, tv_org_name, tv_start_date, tv_end_date, tv_time, tv_stud_attend, tv_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        admin_main am = new admin_main();
        Intent intent = getIntent();
        int hold_id = 0;
        hold_id= intent.getIntExtra("Position", -1);

        tv_event_name = (TextView) findViewById(R.id.tv_event_name);
        tv_org_name = (TextView) findViewById(R.id.tv_org_name);
        tv_start_date = (TextView) findViewById(R.id.tv_start_date);
        tv_end_date = (TextView) findViewById(R.id.tv_end_date);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_stud_attend = (TextView) findViewById(R.id.tv_stud_attend);
        tv_desc = (TextView) findViewById(R.id.tv_desc);

        tv_event_name.setText("Event name: "+am.event_name.get(hold_id));
        tv_org_name.setText("Organized by: "+am.org_name.get(hold_id));
        tv_start_date.setText("Start Date: "+am.start_date.get(hold_id));
        tv_end_date.setText("End Date: "+am.end_date.get(hold_id));
        tv_time.setText("Time: "+am.time.get(hold_id));
        tv_stud_attend.setText("Attendance: "+am.stud_attend.get(hold_id));
        tv_desc.setText(am.event_desc.get(hold_id));

    }
}