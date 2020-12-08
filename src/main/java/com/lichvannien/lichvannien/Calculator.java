package com.lichvannien.lichvannien;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

/**
 * Created by nguyenhuuthanh on 2/28/19.
 */

public class Calculator {


    Double PI = Math.PI;
    Double LOCAL_TIMEZONE = 7.0;


    public int[] Solar2Lunar(int D, int M, int Y) {
        int yy = Y;
        int[][] ly = LunarYear(Y); // Please cache the result of this computation for later use!!!
        int[] month11 = ly[ly.length - 1];
        double jdToday = LocalToJD(D, M, Y);
        double jdMonth11 = LocalToJD(month11[0], month11[1], month11[2]);
        if (jdToday >= jdMonth11) {
            ly = LunarYear(Y+1);
            yy = Y + 1;
        }
        int i = ly.length - 1;
        while (jdToday < LocalToJD(ly[i][0], ly[i][1], ly[i][2])) {
            i--;
        }
        int dd = (int)(jdToday - LocalToJD(ly[i][0], ly[i][1], ly[i][2])) + 1;
        int mm = ly[i][3];
        if (mm >= 11) {
            yy--;
        }
        return new int[] {dd, mm, yy, ly[i][4]};
    }

    public int[] Lunar2Solar(int D, int M, int Y) {
        int leap =0;
        int yy = Y;
        if (M >= 11) {
            yy = Y+1;
        }
        int[][] lunarYear = LunarYear(yy);
        int[] lunarMonth = null;
        for (int i = 0; i < lunarYear.length; i++) {
            int[] lm = lunarYear[i];
            if (lm[3] == M && lm[4] == leap) {
                lunarMonth = lm;
                break;
            }
        }
        if (lunarMonth != null) {
            double jd = LocalToJD(lunarMonth[0], lunarMonth[1], lunarMonth[2]);
            return LocalFromJD(jd + D - 1);
        } else {
            throw new RuntimeException("Incorrect input!");
        }
    }


    public static double UniversalToJD(int D, int M, int Y) {
        double JD;
        if (Y > 1582 || (Y == 1582 && M > 10) || (Y == 1582 && M == 10 && D > 14)) {
            JD = 367*Y - (int)(7*(Y+(int)((M+9)/12))/4) - (int)(3*((int)((Y+(M-9)/7)/100)+1)/4) + (int)(275*M/9)+D+1721028.5;
        } else {
            JD = 367*Y - (int)(7*(Y+5001+(int)((M-9)/7))/4) + (int)(275*M/9)+D+1729776.5;
        }
        return JD;
    }

    public int[] UniversalFromJD(double JD) {
        int Z, A, alpha, B, C, D, E, dd, mm, yyyy;
        double F;
        Z = (int)(JD+0.5);
        F = (JD+0.5)-Z;
        if (Z < 2299161) {
            A = Z;
        } else {
            alpha = (int)((Z-1867216.25)/36524.25);
            A = Z + 1 + alpha - (int)(alpha/4);
        }
        B = A + 1524;
        C = (int)( (B-122.1)/365.25);
        D = (int)( 365.25*C );
        E = (int)( (B-D)/30.6001 );
        dd = (int)(B - D - (int)(30.6001*E) + F);
        if (E < 14) {
            mm = E - 1;
        } else {
            mm = E - 13;
        }
        if (mm < 3) {
            yyyy = C - 4715;
        } else {
            yyyy = C - 4716;
        }
        return new int[]{dd, mm, yyyy};
    }

    public int[] LocalFromJD(double JD) {
        return UniversalFromJD(JD + LOCAL_TIMEZONE/24.0);
    }

    public double LocalToJD(int D, int M, int Y) {
        return UniversalToJD(D, M, Y) - LOCAL_TIMEZONE/24.0;
    }

    public int[] LunarMonth11(int Y) {
        double off = LocalToJD(31, 12, Y) - 2415021.076998695;
        int k = (int)(off / 29.530588853);
        double jd = NewMoon(k);
        int[] ret = LocalFromJD(jd);
        double sunLong = SunLongitude(LocalToJD(ret[0], ret[1], ret[2])); // sun longitude at local midnight
        if (sunLong > 3*PI/2) {
            jd = NewMoon(k-1);
        }
        return LocalFromJD(jd);
    }

    public double SunLongitude(double jdn) {
        double T = (jdn - 2451545.0 ) / 36525; // Time in Julian centuries from 2000-01-01 12:00:00 GMT
        double T2 = T*T;
        double dr = PI/180; // degree to radian
        double M = 357.52910 + 35999.05030*T - 0.0001559*T2 - 0.00000048*T*T2; // mean anomaly, degree
        double L0 = 280.46645 + 36000.76983*T + 0.0003032*T2; // mean longitude, degree
        double DL = (1.914600 - 0.004817*T - 0.000014*T2)*Math.sin(dr*M);
        DL = DL + (0.019993 - 0.000101*T)*Math.sin(dr*2*M) + 0.000290*Math.sin(dr*3*M);
        double L = L0 + DL; // true longitude, degree
        L = L*dr;
        L = L - PI*2*((int)(L/(PI*2))); // Normalize to (0, 2*PI)
        return L;
    }


    public double NewMoon(int k) {
        double T = k/1236.85; // Time in Julian centuries from 1900 January 0.5
        double T2 = T * T;
        double T3 = T2 * T;
        double dr = PI/180;
        double Jd1 = 2415020.75933 + 29.53058868*k + 0.0001178*T2 - 0.000000155*T3;
        Jd1 = Jd1 + 0.00033*Math.sin((166.56 + 132.87*T - 0.009173*T2)*dr); // Mean new moon
        double M = 359.2242 + 29.10535608*k - 0.0000333*T2 - 0.00000347*T3; // Sun's mean anomaly
        double Mpr = 306.0253 + 385.81691806*k + 0.0107306*T2 + 0.00001236*T3; // Moon's mean anomaly
        double F = 21.2964 + 390.67050646*k - 0.0016528*T2 - 0.00000239*T3; // Moon's argument of latitude
        double C1=(0.1734 - 0.000393*T)*Math.sin(M*dr) + 0.0021*Math.sin(2*dr*M);
        C1 = C1 - 0.4068*Math.sin(Mpr*dr) + 0.0161*Math.sin(dr*2*Mpr);
        C1 = C1 - 0.0004*Math.sin(dr*3*Mpr);
        C1 = C1 + 0.0104*Math.sin(dr*2*F) - 0.0051*Math.sin(dr*(M+Mpr));
        C1 = C1 - 0.0074*Math.sin(dr*(M-Mpr)) + 0.0004*Math.sin(dr*(2*F+M));
        C1 = C1 - 0.0004*Math.sin(dr*(2*F-M)) - 0.0006*Math.sin(dr*(2*F+Mpr));
        C1 = C1 + 0.0010*Math.sin(dr*(2*F-Mpr)) + 0.0005*Math.sin(dr*(2*Mpr+M));
        double deltat;
        if (T < -11) {
            deltat= 0.001 + 0.000839*T + 0.0002261*T2 - 0.00000845*T3 - 0.000000081*T*T3;
        } else {
            deltat= -0.000278 + 0.000265*T + 0.000262*T2;
        };
        double JdNew = Jd1 + C1 - deltat;
        return JdNew;
    }

    public final double[] SUNLONG_MAJOR = new double[] {
            0, PI/6, 2*PI/6, 3*PI/6, 4*PI/6, 5*PI/6, PI, 7*PI/6, 8*PI/6, 9*PI/6, 10*PI/6, 11*PI/6
    };

    public int[][] LunarYear(int Y) {
        int[][] ret = null;
        int[] month11A = LunarMonth11(Y-1);
        double jdMonth11A = LocalToJD(month11A[0], month11A[1], month11A[2]);
        int k = (int)Math.floor(0.5 + (jdMonth11A - 2415021.076998695) / 29.530588853);
        int[] month11B = LunarMonth11(Y);
        double off = LocalToJD(month11B[0], month11B[1], month11B[2]) - jdMonth11A;
        boolean leap = off > 365.0;
        if (!leap) {
            ret = new int[13][5];
        } else {
            ret = new int[14][5];
        }
        ret[0] = new int[]{month11A[0], month11A[1], month11A[2], 0, 0};
        ret[ret.length - 1] = new int[]{month11B[0], month11B[1], month11B[2], 0, 0};
        for (int i = 1; i < ret.length - 1; i++) {
            double nm = NewMoon(k+i);
            int[] a = LocalFromJD(nm);
            ret[i] = new int[]{a[0], a[1], a[2], 0, 0};
        }
        for (int i = 0; i < ret.length; i++) {
            ret[i][3] = MOD(i + 11, 12);
        }
        if (leap) {
            initLeapYear(ret);
        }
        return ret;
    }

    void initLeapYear(int[][] ret) {
        double[] sunLongitudes = new double[ret.length];
        for (int i = 0; i < ret.length; i++) {
            int[] a = ret[i];
            double jdAtMonthBegin = LocalToJD(a[0], a[1], a[2]);
            sunLongitudes[i] = SunLongitude(jdAtMonthBegin);
        }
        boolean found = false;
        for (int i = 0; i < ret.length-1; i++) {
            if (found) {
                ret[i][3] = MOD(i+10, 12);
                continue;
            }
            double sl1 = sunLongitudes[i];
            double sl2 = sunLongitudes[i+1];
            boolean hasMajorTerm = Math.floor(sl1/PI*6) != Math.floor(sl2/PI*6);
            if (!hasMajorTerm) {
                found = true;
                ret[i][4] = 1;
                ret[i][3] = MOD(i+10, 12);
            }
        }
    }

    public int INT(double d) {
        return (int)Math.floor(d);
    }

    public static int MOD(int x, int y) {
        int z = x - (int)(y * Math.floor(((double)x / y)));
        if (z == 0) {
            z = y;
        }
        return z;
    }

    public static String getTimeName(float h){
        if(h>1&&h<=3){
            return "Giờ Sửu";
        }else if(h>3&&h<=5){
            return "Giờ Dần";
        }else if(h>5&&h<=7){
            return "Giờ Mão";
        }else if(h>7&&h<=9){
            return "Giờ Thìn";
        }else if(h>9&&h<=11){
            return "Giờ Tị";
        }else if(h>11&&h<=13){
            return "Giờ Ngọ";
        }else if(h>13&&h<=15){
            return "Giờ Mùi";
        }else if(h>15&&h<=17){
            return "Giờ Thân";
        }else if(h>17&&h<=19){
            return "Giờ Dậu";
        }else if(19>3&&h<=21){
            return "Giờ Tuất";
        }else if(21>3&&h<=23){
            return "Giờ Hợi";
        }else{
            return "Giờ Tý";
        }

    }

    public String getDate_name(int d, int m , int y) {
        String can="";
        String chi="";
        int x=INT(UniversalToJD(d,m,y)+9.5) % 10;

        switch(x){
            case 0:
                can="Giáp";
                break;
            case 1:
                can="Ất";
                break;
            case 2:
                can="Bính";
                break;
            case 3:
                can="Đinh";
                break;
            case 4:
                can="Mậu";
                break;
            case 5:
                can="Kỷ";
                break;
            case 6:
                can="Canh";
                break;
            case 7:
                can="Tân";
                break;
            case 8:
                can="Nhâm";
                break;
            case 9:
                can="Quý";
                break;
            default:
                break;
        }

        int z =INT(UniversalToJD(d, m, y)+1.5) % 12;

        switch(z){
            case 0:
                chi="Tý";
                break;
            case 1:chi="Sửu";
                break;
            case 2:chi="Dần";
                break;
            case 3:chi="Mão";
                break;
            case 4:chi="Thìn";
                break;
            case 5:chi="Tỵ";
                break;
            case 6:chi="Ngọ";
                break;
            case 7:chi="Mùi";
                break;
            case 8:chi="Thân";
                break;
            case 9:chi="Dậu";
                break;
            case 10:chi="Tuất";
                break;
            case 11:chi="Hợi";
                break;
            default:
                break;
        }
        return can+" "+chi;
    }

    public String getMonth_name(int month, int year) {
        String can="";
        String chi="";
        int x=(year * 12+ month+ 3) % 10;
        switch(x){
            case 0:
                can="Giáp";
                break;
            case 1:
                can="Ất";
                break;
            case 2:
                can="Bính";
                break;
            case 3:
                can="Đinh";
                break;
            case 4:
                can="Mậu";
                break;
            case 5:
                can="Kỷ";
                break;
            case 6:
                can="Canh";
                break;
            case 7:
                can="Tân";
                break;
            case 8:
                can="Nhâm";
                break;
            case 9:
                can="Quý";
                break;
            default:
                break;
        }

        switch(month){
            case 11:
                chi="Tý";
                break;
            case 12:chi="Sửu";
                break;
            case 1:chi="Dần";
                break;
            case 2:chi="Mão";
                break;
            case 3:chi="Thìn";
                break;
            case 4:chi="Tỵ";
                break;
            case 5:chi="Ngọ";
                break;
            case 6:chi="Mùi";
                break;
            case 7:chi="Thân";
                break;
            case 8:chi="Dậu";
                break;
            case 9:chi="Tuất";
                break;
            case 10:chi="Hợi";
                break;
            default:
                break;
        }

        return can+" "+chi;
    }



    public String getYear_name(int year) {
        String can="";
        String chi="";
        int x= year%10;
        switch(x){
            case 4:
                can="Giáp";
                break;
            case 5:
                can="Ất";
                break;
            case 6:
                can="Bính";
                break;
            case 7:
                can="Đinh";
                break;
            case 8:
                can="Mậu";
                break;
            case 9:
                can="Kỷ";
                break;
            case 0:
                can="Canh";
                break;
            case 1:
                can="Tân";
                break;
            case 2:
                can="Nhâm";
                break;
            case 3:
                can="Quý";
                break;
            default:
                break;
        }

        int div3=0;
        int div4=0;

        div3=year % 3;
        div4=year % 4;

        if(div3==1&&div4==0){
            chi="Tý";
        }
        if(div3==1&&div4==1){
            chi="Dậu";
        }
        if(div3==1&&div4==2){
            chi="Ngọ";
        }
        if(div3==1&&div4==3){
            chi="Mão";
        }
        if(div3==2&&div4==0){
            chi="Thìn";
        }
        if(div3==2&&div4==1){
            chi="Sửu";
        }
        if(div3==2&&div4==2){
            chi="Tuất";
        }
        if(div3==2&&div4==3){
            chi="Mùi";
        }
        if(div3==0&&div4==0){
            chi="Thân";
        }
        if(div3==0&&div4==1){
            chi="Tỵ";
        }
        if(div3==0&&div4==2){
            chi="Dần";
        }
        if(div3==0&&div4==3){
            chi="Hợi";
        }

        return can+" "+chi;
    }

    public static float convertTo100(String input)
    {
        float time = Float.parseFloat(input.replace(":","."));
        String input_string = Float.toString(time);
        BigDecimal inputBD = new BigDecimal(input_string);
        String hhStr = input_string.split("\\.")[0];
        BigDecimal output = new BigDecimal(Float.toString(Integer.parseInt(hhStr)));
        output = output.add((inputBD.subtract(output).divide(BigDecimal.valueOf(60), 10, BigDecimal.ROUND_HALF_EVEN)).multiply(BigDecimal.valueOf(100)));
        return Float.parseFloat(output.toString());
    }

}
