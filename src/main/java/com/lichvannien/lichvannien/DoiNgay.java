package com.lichvannien.lichvannien;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;


public class DoiNgay extends Fragment {

    private int mYear, mMonth, mDay;
    Button btnXemNgay;
    NumberPicker pkNgayAm, pkThangAm, pkNamAm;
    DatePicker pkNgayDuong;
    Calculator cal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doi_ngay, null);

        pkNgayDuong = view.findViewById(R.id.pkNgayDuong);
        pkNgayAm = view.findViewById(R.id.pkNgayAm);
        pkThangAm = view.findViewById(R.id.pkThangAm);
        pkNamAm = view.findViewById(R.id.pkNamAm);
        btnXemNgay = view.findViewById(R.id.btnXemNgay);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        pkNgayAm.setMinValue(1);
        pkNgayAm.setMaxValue(30);
        pkNgayAm.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                if (i < 10) return "0" + i;
                else return i+"";
            }
        });

        pkThangAm.setMinValue(1);
        pkThangAm.setMaxValue(12);
        pkThangAm.setDisplayedValues(new String[]{"thg 1", "thg 2", "thg 3", "thg 4", "thg 5", "thg 6", "thg 7", "thg 8", "thg 9", "thg 10","thg 11","thg 12"});

        pkNamAm.setMinValue(1900);
        pkNamAm.setMaxValue(2100);

        cal = new Calculator();
        updateNgayAm(cal.Solar2Lunar(mDay, mMonth+1, mYear));

        pkNgayDuong.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                int[] result = cal.Solar2Lunar(i2, i1 +1, i);
                updateNgayAm(result);
            }
        });

        pkNgayAm.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int[] result = cal.Lunar2Solar(i1, pkThangAm.getValue(), pkNamAm.getValue());
                updateNgayDuong(result);
            }
        });
        pkThangAm.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int[] result = cal.Lunar2Solar(pkNgayAm.getValue(), i1, pkNamAm.getValue());
                updateNgayDuong(result);
            }
        });
        pkNamAm.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int[] result = cal.Lunar2Solar(pkNgayAm.getValue(), pkThangAm.getValue(), i1);
                updateNgayDuong(result);
            }
        });

        btnXemNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.tabLayout.getTabAt(1).select();
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

                try {
                    LichNgay.calendar.setTime(df.parse(pkNgayDuong.getYear() + "/" + (pkNgayDuong.getMonth()+1) + "/" + pkNgayDuong.getDayOfMonth()));
                    LichNgay.setText(LichNgay.calendar);
                }catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void updateNgayAm(int[] result){
        pkNgayAm.setValue(result[0]);
        pkThangAm.setValue(result[1]);
        pkNamAm.setValue(result[2]);
    }


    private void updateNgayDuong(int[] result){
        pkNgayDuong.updateDate(result[2], result[1]-1, result[0]);
    }


}