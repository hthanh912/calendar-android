package com.lichvannien.lichvannien;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextClock;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class LichNgay extends Fragment {

    ConstraintLayout layout;
    static TextView duongThangNam, duongNgay, duongThu, amNgay, amThang, amNam, tenGio, tenNgay, tenThang, tenNam, tvChamNgon;
    static TextClock textClock;
    static Calendar calendar;
    static String strthu;
    static int thu;
    Button btnpreDay, btnChonNgay, btnnextDay;
    static int am[];
    static String[] chamNgon;
    static Random rand;
    Context context;
    static Calculator c;
    static float floatGio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lich_ngay, null);


        layout = view.findViewById(R.id.layout);
        duongThangNam = view.findViewById(R.id.duongThangNam);
        duongNgay = view.findViewById(R.id.duongNgay);
        duongThu = view.findViewById(R.id.duongThu);
        amNgay = view.findViewById(R.id.amNgay);
        amThang = view.findViewById(R.id.amThang);
        amNam = view.findViewById(R.id.amNam);
        tenGio = view.findViewById(R.id.tenGio);
        tenNgay = view.findViewById(R.id.tenNgay);
        tenThang = view.findViewById(R.id.tenThang);
        tenNam = view.findViewById(R.id.tenNam);
        textClock = view.findViewById(R.id.textClock);
        tvChamNgon = view.findViewById(R.id.tvChamNgon);

        btnChonNgay = view.findViewById(R.id.btnChonNgay);
        btnpreDay = view.findViewById(R.id.btnpreDay);
        btnnextDay = view.findViewById(R.id.btnnextDay);

        layout.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                calendar.add(Calendar.DATE, -1);
                setText(calendar);            }
            public void onSwipeLeft() {
                calendar.add(Calendar.DATE, 1);
                setText(calendar);            }

        });



        chamNgon = getString(R.string.cham_ngon).split("--");

        calendar = Calendar.getInstance();
        SimpleDateFormat gio = new SimpleDateFormat("HH:mm");
        String gioHienTai = gio.format(calendar.getTime());
        textClock.setFormat12Hour("kk:mm");
        textClock.setText(gioHienTai);

        final int yy = calendar.get(Calendar.YEAR);
        final int mm = calendar.get(Calendar.MONTH);
        final int dd = calendar.get(Calendar.DAY_OF_MONTH);
        setText(calendar);

        final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    calendar.setTime(df.parse(i+"/" + (i1+1) +"/" +i2));
                    setText(calendar);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        btnChonNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),onDateSetListener, yy, mm, dd);
                datePickerDialog.show();
            }
        });

        btnnextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, 1);
                setText(calendar);
            }
        });

        btnpreDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, -1);
                setText(calendar);
            }
        });

        return view;
    }

    public static void setText(Calendar day){
        SimpleDateFormat ngay, thang, nam ;
        int intNgayDuong, intThangDuong, intNamDuong;

        c = new Calculator();

        ngay = new SimpleDateFormat("dd");
        thang = new SimpleDateFormat("MM");
        nam = new SimpleDateFormat("yyyy");

        intNgayDuong = Integer.parseInt(ngay.format(day.getTime()));
        intThangDuong = Integer.parseInt(thang.format(day.getTime()));
        intNamDuong = Integer.parseInt(nam.format(day.getTime()));

        thu = day.get(Calendar.DAY_OF_WEEK);
        strthu = "";
        switch (thu){
            case Calendar.SUNDAY: strthu = "Chủ nhật";
                break;
            case Calendar.MONDAY: strthu = "Thứ hai";
                break;
            case Calendar.TUESDAY: strthu = "Thứ ba";
                break;
            case Calendar.WEDNESDAY: strthu = "Thứ tư";
                break;
            case Calendar.THURSDAY: strthu = "Thứ năm";
                break;
            case Calendar.FRIDAY: strthu = "Thứ sáu";
                break;
            case Calendar.SATURDAY: strthu = "Thứ bảy";
                break;
        }


        duongThu.setText(strthu);


        duongNgay.setText(intNgayDuong+"");
        duongThangNam.setText("THÁNG " + intThangDuong + ", NĂM " + intNamDuong );


        am = c.Solar2Lunar(intNgayDuong, intThangDuong, intNamDuong);

        amNgay.setText(am[0]+"");
        amThang.setText(am[1]+"");
        amNam.setText(am[2]+"");
        floatGio = c.convertTo100(textClock.getText().toString());
        tenGio.setText(c.getTimeName(floatGio));

        tenNgay.setText(c.getDate_name(intNgayDuong, intThangDuong, intNamDuong));

        tenThang.setText(c.getMonth_name(am[1], am[2]));

        tenNam.setText(c.getYear_name(am[2]));

        rand = new Random();
        tvChamNgon.setText(chamNgon[rand.nextInt(chamNgon.length)]);
    }


}
