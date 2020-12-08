package com.lichvannien.lichvannien;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

/**
 * Created by nguyenhuuthanh on 4/8/19.
 */

public class LichThang extends Fragment {

    Calendar month;
    CalendarAdapter adapter;
    ArrayList<String> items;
    TextView title;
    GridView gridview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar, null);

        month = Calendar.getInstance();

        items = new ArrayList<String>();
        adapter = new CalendarAdapter(getContext(),month);

        gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        title = (TextView) view.findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM / yyyy", month));

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button previous  = (Button) view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (month.get(Calendar.MONTH) == 0){
                    month.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),1);
                }else{
                    month.set(Calendar.MONTH,month.get(Calendar.MONTH)-1);
                }
                adapter = new CalendarAdapter(getContext(),month);
                gridview.setAdapter(adapter);
                title.setText(android.text.format.DateFormat.format("MMMM / yyyy", month));

            }
        });

        Button next  = (Button) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(month.get(Calendar.MONTH)== 11) {
                    month.set((month.get(Calendar.YEAR)+1),month.getActualMinimum(Calendar.MONTH),1);
                } else {
                    month.set(Calendar.MONTH,month.get(Calendar.MONTH)+1);
                }
                adapter = new CalendarAdapter(getContext(),month);
                gridview.setAdapter(adapter);
                title.setText(android.text.format.DateFormat.format("MMMM / yyyy", month));
            }
        });

        return view;
    }

}
