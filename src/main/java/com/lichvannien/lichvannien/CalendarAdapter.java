
package com.lichvannien.lichvannien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarAdapter extends BaseAdapter {
	static final int FIRST_DAY_OF_WEEK =1;

	private Context mContext;
    private Calendar month;
    private Calendar selectedDate;
    Calculator c;
    public String[] days;

    public CalendarAdapter(Context c, Calendar monthCalendar) {
    	month = monthCalendar;
    	selectedDate = Calendar.getInstance();
    	mContext = c;
        month.set(Calendar.DAY_OF_MONTH, 2);
        refreshDays();
    }

    public int getCount() {
        return days.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent)  {
        View v = convertView;
    	final TextView dayView, dayView2;

        if (convertView == null) {
        	LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_item, null);

        }
        dayView = (TextView)v.findViewById(R.id.date);
        dayView2 = (TextView)v.findViewById(R.id.date2);

        if(days[position].equals("")) {
        	dayView.setClickable(false);
        	dayView.setFocusable(false);
        }
        else {
        	if(month.get(Calendar.YEAR)== selectedDate.get(Calendar.YEAR) && month.get(Calendar.MONTH)== selectedDate.get(Calendar.MONTH) && days[position].equals(""+selectedDate.get(Calendar.DAY_OF_MONTH))) {
                v.setBackground(mContext.getResources().getDrawable(R.drawable.cell_shape_selected));
        	}
        	else {
                v.setBackground(mContext.getResources().getDrawable(R.drawable.cell_shape));
        	}
            c = new Calculator();
            int[] b = c.Solar2Lunar(Integer.parseInt(days[position]), month.get(Calendar.MONTH)+1, month.get(Calendar.YEAR));
            dayView2.setText(b[0]+"");
        }
        dayView.setText(days[position]);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

                try {
                    LichNgay.calendar.setTime(df.parse(month.get(Calendar.YEAR)+ "/" + (month.get(Calendar.MONTH)+1) + "/" + days[position]));
                    LichNgay.setText(LichNgay.calendar);
                    MainActivity.tabLayout.getTabAt(1).select();
                }catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });


        return v;
    }
    
    public void refreshDays()
    {
    	int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = (int)month.get(Calendar.DAY_OF_WEEK);
        
        if(firstDay==1){
        	days = new String[lastDay+(FIRST_DAY_OF_WEEK*6)];
        }
        else {
        	days = new String[lastDay+firstDay-(FIRST_DAY_OF_WEEK+1)];
        }
        
        int j=FIRST_DAY_OF_WEEK;
        
        if(firstDay>1) {
	        for(j=0;j<firstDay-FIRST_DAY_OF_WEEK;j++) {
	        	days[j] = "";
	        }
        }
	    else {
	    	for(j=0;j<FIRST_DAY_OF_WEEK*6;j++) {
	        	days[j] = "";
	        }
	    	j=FIRST_DAY_OF_WEEK*6+1;
	    }

	    int dayNumber = 1;
        for(int i=j-1;i<days.length;i++) {
        	days[i] = ""+dayNumber;
        	dayNumber++;
        }
    }

}