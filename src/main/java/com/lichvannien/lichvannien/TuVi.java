package com.lichvannien.lichvannien;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nguyenhuuthanh on 4/8/19.
 */

public class TuVi  extends android.support.v4.app.Fragment{

    TextView textView, title;
    ImageButton ty, suu, dan, mao, thin, ti, ngo, mui, than, dau, tuat, hoi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.tu_vi, null);

        ty = view.findViewById(R.id.ty);
        suu = view.findViewById(R.id.suu);
        dan = view.findViewById(R.id.dan);
        mao = view.findViewById(R.id.mao);
        thin = view.findViewById(R.id.thin);
        ti = view.findViewById(R.id.ti);
        ngo = view.findViewById(R.id.ngo);
        mui = view.findViewById(R.id.mui);
        than = view.findViewById(R.id.than);
        dau = view.findViewById(R.id.dau);
        tuat = view.findViewById(R.id.tuat);
        hoi = view.findViewById(R.id.hoi);

        textView = view.findViewById(R.id.text);
        title= view.findViewById(R.id.title);

        textView.setText(getString(R.string.ty));
        title.setText("Tử vi tuổi Tí");

        ty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(getString(R.string.ty));
                title.setText("Tử vi tuổi Tí");
            }
        });

        suu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(getString(R.string.suu));
                title.setText("Tử vi tuổi Sửu");
            }
        });

        dan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(getString(R.string.dan));
                title.setText("Tử vi tuổi Dần");
            }
        });

        mao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(getString(R.string.mao));
                title.setText("Tử vi tuổi Mão");
            }
        });

        thin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(getString(R.string.thin));
                title.setText("Tử vi tuổi Thìn");
            }
        });

        ti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(getString(R.string.ti));
                title.setText("Tử vi tuổi Tị");
            }
        });

        ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(getString(R.string.ngo));
                title.setText("Tử vi tuổi Ngọ");
            }
        });

        mui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(getString(R.string.mui));
                title.setText("Tử vi tuổi Mùi");
            }
        });

        than.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(getString(R.string.than));
                title.setText("Tử vi tuổi Thân");
            }
        });

        dau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(getString(R.string.dau));
                title.setText("Tử vi tuổi Dậu");
            }
        });

        tuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(getString(R.string.tuat));
                title.setText("Tử vi tuổi Tuất");
            }
        });

        hoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(getString(R.string.hoi));
                title.setText("Tử vi tuổi Hợi");
            }
        });

        return view;
    }

}