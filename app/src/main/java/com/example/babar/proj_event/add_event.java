package com.example.babar.proj_event;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class add_event extends AppCompatActivity {
//refs https://notepad.pw/ow185vu3
    EditText et_event_name, et_desc;
    ImageButton b_set1, b_set2, b_set3, b_set4;
    static TextView tv_start_date;
    static TextView tv_end_date;
    static TextView tv_start_time;
    static TextView tv_end_time;
    static Calendar calendar;

    static String d_start_date;
    static String d_end_date;
    static String d_start_time, d_end_time;
    static String global_toast;

    static String hold1,hold2,hold3,hold4,hold5,hold6;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        et_event_name = (EditText) findViewById(R.id.et_event_name);
        et_desc = (EditText) findViewById(R.id.et_desc);
        b_set1 = (ImageButton) findViewById(R.id.b_set1);
        b_set2 = (ImageButton) findViewById(R.id.b_set2);
        b_set3 = (ImageButton) findViewById(R.id.b_set3);
        b_set4 = (ImageButton) findViewById(R.id.b_set4);
        tv_start_date = (TextView) findViewById(R.id.tv_start_date);
        tv_end_date = (TextView) findViewById(R.id.tv_end_date);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        calendar = Calendar.getInstance();
        d_start_date = d_end_date = "";
        d_start_time = d_end_time = "";
        global_toast="";
    }
    static public class start_date_dialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            View dialogView = inflater.inflate(R.layout.date_picker, null);
            final NumberPicker numberPicker1 = (NumberPicker) dialogView.findViewById(R.id.np_month);//NUMPICKER MONTHS
            final String[] mons = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            numberPicker1.setDisplayedValues(mons);
            numberPicker1.setMinValue(0);
            numberPicker1.setMaxValue(mons.length-1);
            numberPicker1.setValue(Integer.valueOf(calendar.get(Calendar.MONTH)));
            numberPicker1.setWrapSelectorWheel(false);
            numberPicker1.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            numberPicker1.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    // TODO Auto-generated method stub
                    return mons[value];
                }
            });
            final NumberPicker numberPicker2 = (NumberPicker) dialogView.findViewById(R.id.np_day);//NUMPICKER DAY
            numberPicker2.setMinValue(1);
            numberPicker2.setMaxValue(31);
            numberPicker2.setValue(calendar.get(Calendar.DAY_OF_MONTH));
            numberPicker2.setWrapSelectorWheel(false);
            numberPicker2.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            final NumberPicker numberPicker3 = (NumberPicker) dialogView.findViewById(R.id.np_year);//NUMPICKER YEAR
            numberPicker3.setMinValue(2017);
            numberPicker3.setMaxValue(2020);
            numberPicker3.setWrapSelectorWheel(false);
            numberPicker3.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            // Add action buttons
            builder.setView(dialogView);
            builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String err_str = "";
                    int day = numberPicker2.getValue();
                    int mons = numberPicker1.getValue() +1; //return index start=1
                    String year = String.valueOf(numberPicker3.getValue()); //return actual int
                    if(mons == 1 || mons == 3|| mons == 5||mons == 7||mons == 8||mons == 10||mons == 12){
                        //DO SOMETHING
                        d_start_date=mons+"/"+day+"/"+year;
                        if(check_date(d_start_date)){               //BEYOND THIS DATE?
                            tv_start_date.setText(d_start_date);
                        } else {
                            Toast.makeText(getActivity(), global_toast, Toast.LENGTH_LONG).show();
                        }
                    } else if (mons == 4|| mons == 6||mons == 9||mons == 11){
                        if(day<=30){ //CORRECT
                            //DO SOMETHING
                            d_start_date=mons+"/"+day+"/"+year;
                            if(check_date(d_start_date)){               //BEYOND THIS DATE?
                                tv_start_date.setText(d_start_date);
                            } else {
                                Toast.makeText(getActivity(), global_toast, Toast.LENGTH_LONG).show();
                            }
                        } else { //WRONG
                            err_str="Invalid DAY: no beyond 30";
                            Toast.makeText(getActivity(), err_str, Toast.LENGTH_LONG).show();
                        }
                    } else if(mons==2){ //ERROR HANDLING
                        if(day<=28){ //CORRECT
                            //DO SOMETHING
                            d_start_date=mons+"/"+day+"/"+year;
                            if(check_date(d_start_date)){               //BEYOND THIS DATE?
                                tv_start_date.setText(d_start_date);
                            } else {
                                Toast.makeText(getActivity(), global_toast, Toast.LENGTH_LONG).show();
                            }
                        } else { //WRONG
                            err_str="Invalid DAY: no beyond 28";
                            Toast.makeText(getActivity(), err_str, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            })
                    .setTitle("Set Date")
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            return builder.create();
        }
    }
    static public class end_date_dialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            View dialogView = inflater.inflate(R.layout.date_picker, null);

            final NumberPicker numberPicker1 = (NumberPicker) dialogView.findViewById(R.id.np_month);//NUMPICKER MONTHS
            final String[] mons = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            numberPicker1.setDisplayedValues(mons);
            numberPicker1.setMinValue(0);
            numberPicker1.setMaxValue(mons.length-1);
            numberPicker1.setValue(Integer.valueOf(calendar.get(Calendar.MONTH))-1);
            numberPicker1.setWrapSelectorWheel(false);
            numberPicker1.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            numberPicker1.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    // TODO Auto-generated method stub
                    return mons[value];
                }
            });
            final NumberPicker numberPicker2 = (NumberPicker) dialogView.findViewById(R.id.np_day);//NUMPICKER DAY
            numberPicker2.setMinValue(1);
            numberPicker2.setMaxValue(31);
            numberPicker2.setValue(calendar.get(Calendar.DAY_OF_MONTH));
            numberPicker2.setWrapSelectorWheel(false);
            numberPicker2.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            final NumberPicker numberPicker3 = (NumberPicker) dialogView.findViewById(R.id.np_year);//NUMPICKER YEAR
            numberPicker3.setMinValue(2017);
            numberPicker3.setMaxValue(2020);
            numberPicker3.setWrapSelectorWheel(false);
            numberPicker3.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            // Add action buttons
            builder.setView(dialogView);
            builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String err_str = "";
                    int day = numberPicker2.getValue();
                    int mons = 1+numberPicker1.getValue(); //return index start=1
                    String year = String.valueOf(numberPicker3.getValue()); //return actual int
                    if(mons == 1 || mons == 3|| mons == 5||mons == 7||mons == 8||mons == 10||mons == 12){
                        //DO SOMETHING
                        d_end_date=mons+"/"+day+"/"+year;
                        if(check_date(d_end_date)){               //BEYOND THIS DATE?
                            tv_end_date.setText(d_end_date);
                        } else {
                            Toast.makeText(getActivity(), global_toast, Toast.LENGTH_LONG).show();
                        }
                    } else if (mons == 4|| mons == 6||mons == 9||mons == 11){
                        if(day<=30){ //CORRECT
                            //DO SOMETHING
                            d_end_date=mons+"/"+day+"/"+year;
                            if(check_date(d_end_date)){               //BEYOND THIS DATE?
                                tv_end_date.setText(d_end_date);
                            } else {
                                Toast.makeText(getActivity(), global_toast, Toast.LENGTH_LONG).show();
                            }
                        } else { //WRONG
                            err_str="Invalid DAY: no beyond 30";
                            Toast.makeText(getActivity(), err_str, Toast.LENGTH_SHORT).show();
                        }
                    } else if(mons==2){ //ERROR HANDLING
                        if(day<=28){ //CORRECT
                            //DO SOMETHING
                            d_end_date=mons+"/"+day+"/"+year;
                            if(check_date(d_end_date)){               //BEYOND THIS DATE?
                                tv_end_date.setText(d_end_date);
                            } else {
                                Toast.makeText(getActivity(), global_toast, Toast.LENGTH_LONG).show();
                            }
                        } else { //WRONG
                            err_str="Invalid DAY: no beyond 28";
                            Toast.makeText(getActivity(), err_str, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            })
                    .setTitle("Set Date")
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            return builder.create();
        }
    }
    static public class start_time_dialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            View dialogView = inflater.inflate(R.layout.time_picker, null);

            final NumberPicker numberPicker1 = (NumberPicker) dialogView.findViewById(R.id.np_format);//NUMPICKER MONTHS
            final String[] mons = new String[]{"am", "pm"};
            numberPicker1.setDisplayedValues(mons);
            numberPicker1.setMinValue(0);
            numberPicker1.setMaxValue(mons.length-1);
            numberPicker1.setWrapSelectorWheel(false);
            numberPicker1.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            numberPicker1.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    // TODO Auto-generated method stub
                    return mons[value];
                }
            });
            final NumberPicker numberPicker2 = (NumberPicker) dialogView.findViewById(R.id.np_hour);//NUMPICKER DAY
            numberPicker2.setMinValue(1);
            numberPicker2.setMaxValue(12);
            numberPicker2.setWrapSelectorWheel(false);
            numberPicker2.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            final NumberPicker numberPicker3 = (NumberPicker) dialogView.findViewById(R.id.np_min);//NUMPICKER YEAR
            numberPicker3.setMinValue(0);
            numberPicker3.setMaxValue(59);
            numberPicker3.setWrapSelectorWheel(false);
            numberPicker3.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            // Add action buttons
            builder.setView(dialogView);
            builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String hour = String.valueOf(numberPicker2.getValue());
                    String form = String.valueOf(numberPicker1.getValue()); //return index start=1
                    String min = String.valueOf(numberPicker3.getValue()); //return actual int
                    System.out.println("MARK DENVER length: "+hour.length()+" "+min.length()+" "+form);
                    if(hour.length() == 1){
                        hour="0"+hour;
                    }
                    if(min.length() == 1){
                        min="0"+min;
                    }
                    if(form.equals("0")){
                        d_start_time = hour+":"+min+" am";
                    } else {
                        d_start_time = hour+":"+min+" pm";
                    }
                    tv_start_time.setText(d_start_time);
                }
            })
                    .setTitle("Set Time")
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            return builder.create();
        }
    }
    static public class end_time_dialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            View dialogView = inflater.inflate(R.layout.time_picker, null);

            final NumberPicker numberPicker1 = (NumberPicker) dialogView.findViewById(R.id.np_format);//NUMPICKER MONTHS
            final String[] mons = new String[]{"AM", "PM"};
            numberPicker1.setDisplayedValues(mons);
            numberPicker1.setMinValue(0);
            numberPicker1.setMaxValue(mons.length-1);
            numberPicker1.setWrapSelectorWheel(false);
            numberPicker1.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            numberPicker1.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    // TODO Auto-generated method stub
                    return mons[value];
                }
            });
            final NumberPicker numberPicker2 = (NumberPicker) dialogView.findViewById(R.id.np_hour);//NUMPICKER DAY
            numberPicker2.setMinValue(01);
            numberPicker2.setMaxValue(12);
            numberPicker2.setWrapSelectorWheel(false);
            numberPicker2.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            final NumberPicker numberPicker3 = (NumberPicker) dialogView.findViewById(R.id.np_min);//NUMPICKER YEAR
            numberPicker3.setMinValue(00);
            numberPicker3.setMaxValue(59);
            numberPicker3.setWrapSelectorWheel(false);
            numberPicker3.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            // Add action buttons
            builder.setView(dialogView);
            builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String hour = String.valueOf(numberPicker2.getValue());
                    String form = String.valueOf(numberPicker1.getValue()); //return index start=1
                    String min = String.valueOf(numberPicker3.getValue()); //return actual int
                    if(hour.length() == 1){
                        hour="0"+hour;
                    }
                    if(min.length() == 1){
                        min="0"+min;
                    }
                    if(form.equals("0")){
                        d_end_time = hour+":"+min+" am";
                    } else {
                        d_end_time = hour+":"+min+" pm";
                    }
                    tv_end_time.setText(d_end_time);
                }
            })
                    .setTitle("Set Time")
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            return builder.create();
        }
    }

    public void show_start_date_dialog(View v) {
        DialogFragment newFragment = new start_date_dialog();
        newFragment.show(getFragmentManager(), "date");
    }
    public void show_end_date_dialog(View v) {
        DialogFragment newFragment = new end_date_dialog();
        newFragment.show(getFragmentManager(), "date");
    }
    public void show_start_time_dialog(View v) {
        DialogFragment newFragment = new start_time_dialog();
        newFragment.show(getFragmentManager(), "date");
    }
    public void show_end_time_dialog(View v) {
        DialogFragment newFragment = new end_time_dialog();
        newFragment.show(getFragmentManager(), "date");
    }

    public static boolean check_date(String str1){
        try{
            Calendar calendar = Calendar.getInstance();
            int thisYear = calendar.get(Calendar.YEAR);
            int thisMonth = calendar.get(Calendar.MONTH);
            int thisDay = calendar.get(Calendar.DAY_OF_MONTH);

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String com_str = thisDay + "/" + thisMonth + "/" + thisYear;
            Date strDate1 = sdf.parse(str1);
            Date now = new Date();

            if (str1 != "" && now.getTime() < strDate1.getTime()) { // CORRECT
                return true;
            } else { // WRONG
                global_toast="Invalid DATE: Set DATE beyond today";
                return false;
            }
        } catch (ParseException e) {
            System.out.println("ERROR: 'check_date' PARSING INT TO DATE");
        }
        return false;
    }
    public static boolean check_final_date(String str1, String str2){
        try{
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date strDate1 = sdf.parse(str1);
            Date strDate2 = sdf.parse(str2);

            if (strDate1.getTime() <= strDate2.getTime()) { // CORRECT
                return true;
            } else { // WRONG
                global_toast="Invalid END DATE: Set END DATE\nbeyond or same as START DATE";
                return false;
            }
        } catch (ParseException e) {
            global_toast="ERROR: 'check_date' PARSING INT TO DATE";
        }
        return false;
    }


    public boolean check_time(String str1, String str2){
        boolean hold = false;
        try {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
            Date Date1 = format.parse(str1);
            Date Date2 = format.parse(str2);
            long mills = Date1.getTime() - Date2.getTime();
            int Hours = (int) (mills / (1000 * 60 * 60));
            int Mins = (int) (mills / (1000 * 60)) % 60;
            if(Hours >= 0 && Mins >0){
                return true;
            } else {
                return false;
            }
        } catch(Exception e){
            System.out.println("ERROR: 'check_time' method");
        }
        return false;
    }
    public boolean final_check_time(String str1, String str2){
        boolean hold = false;
        try {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
            Date Date1 = format.parse("8:00 pm");
            Date Date2 = format.parse("8:30 pm");
            long mills = Date1.getTime() - Date2.getTime();
            int Hours = (int) (mills / (1000 * 60 * 60));
            int Mins = (int) (mills / (1000 * 60)) % 60;
            if(Hours >= 0 && Mins >0){
                return true;
            } else {
                return false;
            }
        } catch(Exception e){
            System.out.println("ERROR: 'check_time' method");
        }
        return false;
    }

    public void submit(View v){
        boolean con1=false;
        boolean con2=false;
        boolean con3=false;
        boolean con22=false;
        String hold ="";
        hold="INVALID INPUT:";
        if(!et_event_name.getText().toString().isEmpty() && et_event_name.getText().toString().length() >=5){
            con1= false;
        } else {
            con1 = true;
            hold=hold+"\nEvent name: length must be greater than 4";
        }
        if(check_final_date(d_start_date, d_end_date)){
            con22= false;
        } else {
            con22 = true;
            hold=hold+"\nEvent date: last date must be greater to first day";
        }
        if(check_time(d_end_time, d_start_time)){
            con2= false;
        } else {
            con2 = true;
            hold=hold+"\nEvent time: dismissing time must be greater than starting time";
        }
        if(!et_desc.getText().toString().isEmpty() && et_desc.getText().toString().length() >=10){
            con3= false;
        } else {
            con3 = true;
            hold=hold+"\nEvent description: length must be greater than 9";
        }

        if(con1 || con2 || con3 || con22){
            Toast.makeText(this, hold, Toast.LENGTH_LONG).show();
        } else { //execute insertion
            Intent inteeent = getIntent();
            hold1=et_event_name.getText().toString();
            hold2=inteeent.getStringExtra("pass_org");
            hold3=tv_start_date.getText().toString();
            hold4=tv_end_date.getText().toString();
            hold5=tv_start_time.getText().toString()+"-"+tv_end_time.getText().toString();
            hold6=et_desc.getText().toString();
            Toast.makeText(this, hold1+"\n"+hold2+"\n"+hold3+"\n"+hold4+"\n"+hold5+"\n"+hold6, Toast.LENGTH_LONG).show();
            new MyTask2().execute();
            finish();
        }
    }


    class MyTask2 extends AsyncTask<Void, Void, Integer> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(add_event.this);
            dialog.setMessage("Loading..");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try{
                URL url = new URL("http://192.168.137.1/files/android/proj_add.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

                ContentValues cv = new ContentValues();
                cv.put("event_name", hold1);
                cv.put("org_name", hold2);
                cv.put("start_date", hold3);
                cv.put("end_date", hold4);
                cv.put("time", hold5);
                cv.put("event_desc", hold6);
                cv.put("stud_attend", "0");
                bw.write(createPostString(cv));
                bw.flush();
                bw.close();
                os.close();
                int rc = con.getResponseCode();
                con.disconnect();
                return rc;
            }
            catch (Exception e){
                System.err.println(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer strJSON) {
            dialog.dismiss();
            finish();
            Toast.makeText(getApplicationContext(), "Event Added", Toast.LENGTH_LONG).show();
        }
    }
    public String createPostString(ContentValues cv) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean flag = true;
        Set set = cv.valueSet();

        for(Map.Entry<String, Object> v: cv.valueSet()){
            if(flag){
                flag = false;
            } else {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(v.getKey(),"UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(v.getValue().toString(),"UTF-8"));
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
