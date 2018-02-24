package com.example.babar.proj_event;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class attend_event extends AppCompatActivity {
    static public ArrayList<String> member_nfc_id;
    JSONArray jsonArray;
    JSONObject jsonObject;

    String id;
    String event_name;
    String org_name;
    String start_date;
    String end_date;
    String time;
    String event_desc;
    String stud_attend;

    String mem_nfc_id;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_event);
        admin_main am = new admin_main();
        Intent intent = getIntent();
        pos = intent.getIntExtra("Position", -1);
        id = am.id.get(pos);
        event_name = am.event_name.get(pos);
        org_name = am.org_name.get(pos);
        start_date = am.start_date.get(pos);
        end_date = am.end_date.get(pos);
        event_desc = am.event_desc.get(pos);
        stud_attend = am.stud_attend.get(pos);


    }

    //NFC
    // list of NFC technologies detected:
    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            /*((TextView)findViewById(R.id.text)).setText(
                    "NFC Tag\n" +
                            ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID))); DO NOT ERASE*/
            mem_nfc_id = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            new MyTask().execute();
        }
    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    public void parseJSON (String strJSON){
        try {
            member_nfc_id = new ArrayList<>();
            jsonObject = new JSONObject(strJSON);

            jsonArray = jsonObject.getJSONArray("items");
            int i =0;

            while(jsonArray.length()>i){
                jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("nfc_id");
                if(mem_nfc_id == name){
                    store st = new store();
                    st.store_nfc.add(name);
                }
                i++;
            }
            /*BaseAdapter mAdapter;
            mAdapter = new custom_adapter(this, id_list, items, price);
            lv.setAdapter(mAdapter);*/
        } catch (Exception e) {
            System.err.println("ERROR - PARSEJSON");

        }
    }

    class MyTask extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(attend_event.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                //URL url = new URL("http://"+intToIp(Integer.valueOf(getWifiApIpAddress(LoginActivity.this)))+"/files/android/proj_fetch.php");
                URL url = new URL("http://192.168.137.1/files/android/proj_fetch.php");
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
}
