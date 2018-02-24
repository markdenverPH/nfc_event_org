package com.example.babar.proj_event;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by babar on 6/28/2017.
 */

public class member_main extends AppCompatActivity {
    static public ArrayList<String> id;
    static public ArrayList<String> event_name;
    static public ArrayList<String> org_name;
    static public ArrayList<String> start_date;
    static public ArrayList<String> end_date;
    static public ArrayList<String> time;
    static public ArrayList<String> event_desc;
    static public ArrayList<String> stud_attend;
    GridView gv;
    JSONArray jsonArray;
    JSONObject jsonObject;

    public static String USER_LOGGED;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        final Intent intent = new Intent(this, view_event.class);
        USER_LOGGED = intent.getStringExtra("Position");
        gv = (GridView) findViewById(R.id.gv);
        new MyTask().execute();
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                intent.putExtra("Position", position);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //re-run mytask to refresh listview
    }
    public void parseJSON (String strJSON){
        try {
            id = new ArrayList<>();
            event_name = new ArrayList<>();
            org_name = new ArrayList<>();
            start_date = new ArrayList<>();
            end_date = new ArrayList<>();
            time = new ArrayList<>();
            event_desc = new ArrayList<>();
            stud_attend = new ArrayList<>();
            jsonObject = new JSONObject(strJSON);

            jsonArray = jsonObject.getJSONArray("items");
            int i =0;

            while(jsonArray.length()>i){
                jsonObject = jsonArray.getJSONObject(i);
                String str1 = jsonObject.getString("id");
                String str2 = jsonObject.getString("event_name");
                String str3 = jsonObject.getString("org_name");
                String str4 = jsonObject.getString("start_date");
                String str5 = jsonObject.getString("end_date");
                String str6 = jsonObject.getString("time");
                String str7 = jsonObject.getString("event_desc");
                String str8 = jsonObject.getString("stud_attend");

                id.add(str1);
                event_name.add(str2);
                org_name.add(str3);
                start_date.add(str4);
                end_date.add(str5);
                time.add(str6);
                event_desc.add(str7);
                stud_attend.add(str8);
                i++;
            }
            BaseAdapter mAdapter;
            mAdapter = new custom_adapter(this, id, event_name, org_name, start_date, end_date);
            gv.setAdapter(mAdapter);
        } catch (Exception e) {
            Log.i("Check",String.valueOf(e));
        }
    }
    class MyTask extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(member_main.this);
            dialog.setMessage("Fetching some data..");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                URL url = new URL("http://192.168.137.1/files/android/proj_main_fetch.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String str = "";
                while((str = bufferedReader.readLine()) != null){
                    sb.append(str);
                }
                return sb.toString();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strJSON) {
            dialog.dismiss();
            parseJSON(strJSON);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mem_options_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch(item.getItemId()){
            case R.id.logout:
                Intent intent3 = new Intent(this, LoginActivity.class);
                startActivity(intent3);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

