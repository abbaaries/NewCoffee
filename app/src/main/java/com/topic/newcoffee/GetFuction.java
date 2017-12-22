package com.topic.newcoffee;


import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Nick on 2017/12/11.
 */

public class GetFuction extends AppCompatActivity {
    String TAG = "getfuction",dayofWeek;
    boolean isTime=false;
    Calendar c = Calendar.getInstance();
    String[] str1;
    int[] int1,int2,time;
    int k1,k2;
    public  int[] getNow(){
        int h = c.get(Calendar.HOUR_OF_DAY);
        int1 = new int[7];
        int m = c.get(Calendar.MINUTE)+10;
        int1[0] = c.get(Calendar.YEAR);
        int1[1] = c.get(Calendar.MONTH)+1;
        int1[2] = c.get(Calendar.DAY_OF_MONTH);
        int1[3] = c.get(Calendar.AM_PM);
        int1[4] = h>12?h-12:h;
        if (m>=60){
            int1[4]+=1;
            m-=60;
        }
        int1[5] = m>50?51:m>40?41:m>30?31:m>20?21:m>10?11:1;
        return int1;
    }
    public  int[] setPicker(){
        int2 = new int[3];
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE)+10;
        int2[0] = (c.get(Calendar.AM_PM)>0?1:0);
        int2[1] = h>12?h-13:h-1;
        if (m>=60){
            int2[1]+=1;
            m-=60;
        }
        int2[2] = m>50?5:m>40?4:m>30?3:m>20?2:m>10?1:0;
        return int2;
    }
    SharedPreferences sp;

    public int[] countTime(int tYear, int tMomth ,int tDay,int tAMPM, int tHour){
        if(tAMPM==1){
            tAMPM = 0;
            tHour+=12;
        }

        int2 = new int[5];
        if(tHour == 24) {
            if (tYear % 4 == 0) {
                if (tDay == 30 && tMomth == 2) {
                    tDay = 0;
                    tMomth += 1;
                }
                if (tYear % 100 == 0) {
                    if (tDay == 29 && tMomth == 2) {
                        tDay = 0;
                        tMomth += 1;
                    }
                    if (tYear % 400 == 0) {
                        if (tDay == 30 && tMomth == 2) {
                            tDay = 0;
                            tMomth += 1;
                        }
                        if (tYear % 4000 == 0) {
                            if (tDay == 29 && tMomth == 2) {
                                tDay = 0;
                                tMomth += 1;
                            }
                        }
                    }
                }
            }
            tHour = 0;
            tDay += 1;
            if ((tDay == 31 && (tMomth == 4 || tMomth == 6 || tMomth == 9 || tMomth == 11))||(tDay ==29 && tMomth==2)) {
                tDay = 1;
                tMomth += 1;
            }
            if (tDay == 32 && (tMomth == 1 || tMomth == 3 || tMomth == 5 || tMomth == 7 || tMomth == 8 || tMomth == 10 || tMomth == 12)) {
                if (tMomth == 12) {
                    tDay = 1;
                    tMomth = 1;
                    tYear += 1;
                    Log.d("TAGT", "1月1日");
                } else {
                    tDay = 1;
                    tMomth += 1;
                }
            }
        }

        int2[0] = tYear;
        int2[1] = tMomth;
        int2[2] = tDay;
        int2[3] = tAMPM;
        int2[4] = tHour;
        return int2;
    }
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference userRef = db.getReference("營業時間");
    ArrayList<ArrayList<String>> arrayList1;
    ArrayList<String> arrayList2;

    public boolean compareTime(int tYear, int tMomth ,int tDay, int tHour,int tMin,ArrayList<ArrayList<String>> lists){
        String tWeek = getWeek(tYear,tMomth,tDay);
        Log.d(TAG,"tweek:"+tWeek);
        Log.d(TAG,"arraylist:"+lists.get(0).get(0));
        for (int i=0;i<lists.size();i++){
            if (lists.get(i).get(1).equals("false")) {
                if (lists.get(i).get(0).equals(tWeek)){
                    Log.d(TAG,"losts.get:"+lists.get(i).get(0));
                }
                k1=i;//week(false)沒營業的星期幾
            }
            if(lists.get(i).get(0).equals(tWeek)){
                k2=i;//挑選的星期幾
            }
        }
        Log.d(TAG,"k:"+lists.get(k1).get(0));
        Log.d(TAG,"k:"+lists.get(k1).get(0).equals(tWeek));
        if(lists.get(k1).get(0).equals(tWeek)){
            isTime = false;
        }else {
            int hour = getNow()[3]==1?getNow()[4]+12:getNow()[4];
            Log.d(TAG,"hour:"+hour+"/"+getNow()[4]);
            Log.d(TAG,"營業時間內限制:"+(tHour>=Integer.valueOf(lists.get(k2).get(2))&&tHour<Integer.valueOf(lists.get(k2).get(3)))+"");
            if(tHour>=Integer.valueOf(lists.get(k2).get(2))&&tHour<Integer.valueOf(lists.get(k2).get(3))){
                Log.d(TAG,"預定時間限制:"+(tHour>=Integer.valueOf(lists.get(k2).get(2))&&tHour<Integer.valueOf(lists.get(k2).get(3)))+"");
                if((tYear>getNow()[0])||(tYear==getNow()[0]&&tMomth>getNow()[1])||
                        (tYear==getNow()[0]&&tMomth==getNow()[1]&&tDay>getNow()[2])||
                        (tYear==getNow()[0]&&tMomth==getNow()[1]&&tDay==getNow()[2]&&tHour>hour)||
                        ((tYear==getNow()[0]&&tMomth==getNow()[1]&&tDay==getNow()[2]&&tHour==hour&&tMin>=getNow()[5]))){
                    isTime = true;
                }else {
                    isTime = false;
                }
            }else {
                isTime = false;
            }
        }
        Log.d(TAG,"bollean isTime:"+isTime);
        return isTime;
    }
    public String getWeek(int year, int month, int day){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE");
        Date datetest = new Date(year,month,day-4);
        dayofWeek = simpleDateFormat.format(datetest);
        Log.d(TAG,"tyear:"+year+",tmonth:"+month+",tday:"+day+",week:"+dayofWeek);
        return dayofWeek;
    }

}
