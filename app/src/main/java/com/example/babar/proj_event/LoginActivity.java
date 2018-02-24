package com.example.babar.proj_event;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    static SharedPreferences sp;
    static SharedPreferences.Editor spedit;
    static public ArrayList<String> nfc_id;
    static public ArrayList<String> user_type;
    static public ArrayList<String> id;
    JSONArray jsonArray;
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = getPreferences(MODE_PRIVATE);
        spedit = sp.edit();

        new MyTask().execute();

    }
    @Override
    protected void onStart() {
        super.onStart();
        new MyTask().execute();
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
            boolean hey = true;
            String hold_nfc = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            /*Intent intent5 = new Intent(this, member_main.class);
            startActivity(intent5);*/
            for(int x=0;x<nfc_id.size();x++) {
                System.out.println("NFC ID RECORDED: "+nfc_id.get(x)+"\nUSER TYPE: "+user_type.get(x));
                if(hold_nfc.equals(nfc_id.get(x)) && user_type.get(x).equals("admin")) {
                    hey = false;
                    Toast.makeText(getApplicationContext(), "Success Login", Toast.LENGTH_LONG).show();
                    /*spedit.putString("logged", String.valueOf(x));
                    spedit.commit();*/
                    Intent intent2 = new Intent(this, admin_main.class);
                    intent2.putExtra("pass_org", id.get(x));
                    startActivity(intent2);
                    finish();
                } else if (hold_nfc.equals(nfc_id.get(x)) &&  user_type.get(x).equals("student")){
                    hey = false;
                    Toast.makeText(getApplicationContext(), "Success Login", Toast.LENGTH_LONG).show();
                    /*spedit.putString("logged", String.valueOf(x));
                    spedit.commit();*/
                    Intent inten= new Intent(this, member_main.class);
                    inten.putExtra("Position", x);
                    startActivity(inten);
                    finish();
                }
            }
            if(hey) {
                Toast.makeText(getApplicationContext(), "Unknown ID", Toast.LENGTH_LONG).show();
            }
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

    public String getWifiApIpAddress(Context context) {
        WifiManager wifii = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String d="";
        d=String.valueOf(wifii.getDhcpInfo().gateway);
        return d;
    }
    public String intToIp(int addr) {
        return  ((addr & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF));
    }

    public void parseJSON (String strJSON){
        try {
            nfc_id = new ArrayList<>();
            user_type = new ArrayList<>();
            id = new ArrayList<>();
            jsonObject = new JSONObject(strJSON);

            jsonArray = jsonObject.getJSONArray("items");
            int i =0;

            while(jsonArray.length()>i){
                jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("nfc_id");
                String user_type2 = jsonObject.getString("user_type");
                String id2 = jsonObject.getString("org_name");

                nfc_id.add(name);
                user_type.add(user_type2);
                id.add(id2);
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
            dialog = new ProgressDialog(LoginActivity.this);
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

