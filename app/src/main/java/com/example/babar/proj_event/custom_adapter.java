package com.example.babar.proj_event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by babar on 6/19/2017.
 */

public class custom_adapter extends BaseAdapter {
    ArrayList<String> hold_id = new ArrayList<>();
    ArrayList<String> hold_event_name = new ArrayList<>();
    ArrayList<String> hold_org_name = new ArrayList<>();
    ArrayList<String> hold_start_date = new ArrayList<>();
    ArrayList<String> hold_end_date = new ArrayList<>();
    /*ArrayList<String> hold_time = new ArrayList<>();
    ArrayList<String> hold_event_desc = new ArrayList<>();
    ArrayList<String> hold_stud_attend = new ArrayList<>();*/
    private static LayoutInflater inflater = null;
    private Context contextm;
    public custom_adapter(@NonNull Context context, ArrayList<String> id, ArrayList<String> name, ArrayList<String> org_name,
                          ArrayList<String> start_date, ArrayList<String> end_date) {
        contextm = context;
        hold_id = id;
        hold_event_name = name;
        hold_org_name = org_name;
        hold_start_date = start_date;
        hold_end_date = end_date;
        /*hold_time = time;
        hold_event_desc = event_desc;
        hold_stud_attend = stud_attend;*/
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    TextView tv_eventname;
    TextView tv_date;
    TextView tv_orgname;
    ImageView iv;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View rowView;
        rowView = inflater.inflate(R.layout.custom_grid, null);
        tv_eventname=(TextView) rowView.findViewById(R.id.tv_eventname);
        tv_date=(TextView) rowView.findViewById(R.id.tv_date);
        tv_orgname=(TextView) rowView.findViewById(R.id.tv_orgname);
        iv = (ImageView) rowView.findViewById(R.id.iv);
        tv_orgname.setText(hold_org_name.get(position));
        if(hold_event_name.get(position).length() >= 17){
            tv_eventname.setText(hold_event_name.get(position).substring(0, 13)+"...");
        } else {
            tv_eventname.setText(hold_event_name.get(position));
        }
        tv_date.setText(trans(hold_start_date.get(position)+"-"+hold_end_date.get(position)));

        return rowView;
    }
    public String trans(String str){
        String[] hold_str = str.split("-");
        String[] hold_str_split1 = hold_str[0].split("/");
        String[] hold_str_split2 = hold_str[1].split("/");

        switch(Integer.valueOf(hold_str_split1[0])){
            case 1:
                hold_str_split1[0] = "Jan";
                break;
            case 2:
                hold_str_split1[0] = "Feb";
                break;
            case 3:
                hold_str_split1[0] = "Mar";
                break;
            case 4:
                hold_str_split1[0] = "Apr";
                break;
            case 5:
                hold_str_split1[0] = "May";
                break;
            case 6:
                hold_str_split1[0] = "Jun";
                break;
            case 7:
                hold_str_split1[0] = "Jul";
                break;
            case 8:
                hold_str_split1[0] = "Aug";
                break;
            case 9:
                hold_str_split1[0] = "Sep";
                break;
            case 10:
                hold_str_split1[0] = "Oct";
                break;
            case 11:
                hold_str_split1[0] = "Nov";
                break;
            case 12:
                hold_str_split1[0] = "Dec";
                break;
        }
        switch(Integer.valueOf(hold_str_split2[0])){
            case 1:
                hold_str_split2[0] = "Jan";
                break;
            case 2:
                hold_str_split2[0] = "Feb";
                break;
            case 3:
                hold_str_split2[0] = "Mar";
                break;
            case 4:
                hold_str_split2[0] = "Apr";
                break;
            case 5:
                hold_str_split2[0] = "May";
                break;
            case 6:
                hold_str_split2[0] = "Jun";
                break;
            case 7:
                hold_str_split2[0] = "Jul";
                break;
            case 8:
                hold_str_split2[0] = "Aug";
                break;
            case 9:
                hold_str_split2[0] = "Sep";
                break;
            case 10:
                hold_str_split2[0] = "Oct";
                break;
            case 11:
                hold_str_split2[0] = "Nov";
                break;
            case 12:
                hold_str_split2[0] = "Dec";
                break;
        }

        return hold_str_split1[0]+" "+hold_str_split1[1]+", "+hold_str_split1[2]+" to \n"
                +hold_str_split2[0]+" "+hold_str_split2[1]+", "+hold_str_split2[2];
    }
    @Override
    public int getCount() {
        return hold_id.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
