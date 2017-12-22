package com.topic.newcoffee;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TakeMeals extends AppCompatActivity {
    GetFuction myS = new GetFuction();
    public static String shopAddress,TAG="TakeMeals";
    public static int tYear,tMomth,tDay,tAm_Pm=Calendar.AM,tHour=1,tMinute=1;
    private Context context;
    private TextView StoreLocation,tv_changeAddr,tv_showDate,warningInfo,orderInfo;
    String date,start,close,w1;
    boolean b=false;
    ArrayList<ArrayList<String>> arrayList1;
    ArrayList<String> arrayList2;
    String[] values4,values5,values6;
    int tY,tM,tD;
    NumberPicker np4,np5,np6;
    Calendar c;
    SharedPreferences sp;
    FirebaseDatabase db;
    DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_meals);
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("營業時間");
        sp = getSharedPreferences("PREF",MODE_PRIVATE);
        findView();
        setText();
        picker();
        setListener();
        getInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOpeningTime();
    }

    void findView(){
        StoreLocation = (TextView) findViewById(R.id.show_usual_store);
        tv_changeAddr = (TextView) findViewById(R.id.tv_changeAddr);
        tv_showDate = (TextView) findViewById(R.id.tv_showDate);
        warningInfo = (TextView) findViewById(R.id.warning_info2);
        orderInfo = (TextView) findViewById(R.id.order_info2);
        np4 = (NumberPicker)findViewById(R.id.numberPicker4);
        np5 = (NumberPicker)findViewById(R.id.numberPicker5);
        np6 = (NumberPicker)findViewById(R.id.numberPicker6);
    }


    void setText(){
        tv_changeAddr.setVisibility(View.INVISIBLE);
        String[] shops = new String[Integer.valueOf(sp.getString("店數",""))];
        for (int i=0;i<Integer.valueOf(sp.getString("店數",""));i++){
            shops[i] = sp.getString(i+"店址","");
        }
//        String[] shops = getResources().getStringArray(R.array.shop_location);
        shopAddress = shops[0];
        StoreLocation.setText(shopAddress);
        tYear = myS.getNow()[0];
        tMomth = myS.getNow()[1];
        tDay = myS.getNow()[2];
        tAm_Pm = myS.getNow()[3];
        tHour = myS.getNow()[4];
        tMinute = myS.getNow()[5];
        tv_showDate.setText(tYear+"/"+(tMomth>=10?tMomth:("0"+tMomth))+"/"+(tDay>=10?tDay:("0"+tDay)));
    }
    void dateCalendar(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                TakeMeals.this,datelistener,
                tYear,tMomth-1,tDay);
        datePickerDialog.setTitle("       選 擇 自 取 日 期");
        datePickerDialog.show();
    }
    DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            tYear = view.getYear();
            tMomth = view.getMonth()+1;
            tDay = view.getDayOfMonth();
            tv_showDate.setText(tYear+"/"+(tMomth>=10?tMomth:("0"+tMomth))+"/"+(tDay>=10?tDay:("0"+tDay)));
            Log.d(TAG,tYear+"/"+tMomth+"/"+(tDay));


        }
    };
    void setPickerText(){
        values4 = getResources().getStringArray(R.array.time_meridiem);
        Resources res = getResources();
        values5 = res.getStringArray(R.array.hours_12);
        values6 = getResources().getStringArray(R.array.time_interval);
        np4.setMinValue(0);
        np5.setMinValue(0);
        np6.setMinValue(0);
        np4.setMaxValue(values4.length-1);
        np5.setMaxValue(values5.length-1);
        np6.setMaxValue(values6.length-1);
        np4.setDisplayedValues(values4);
        np5.setDisplayedValues(values5);
        np6.setDisplayedValues(values6);
        np4.setWrapSelectorWheel(true);
        np5.setWrapSelectorWheel(false);
        np6.setWrapSelectorWheel(false);

        np4.setValue(myS.setPicker()[0]);
        np5.setValue(myS.setPicker()[1]);
        np6.setValue(myS.setPicker()[2]);
    }
    void picker (){
        setPickerText();
        np4.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tAm_Pm = picker.getValue();
                Log.d(TAG,"AM_PM:"+tAm_Pm);
            }
        });
        np5.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tHour = picker.getValue()+1;
                Log.d(TAG,"Hour:"+tHour);
            }
        });

        np6.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tMinute = picker.getValue()*10+1;
                Log.d(TAG,"Minute:"+tMinute);
            }
        });

    }

    void setListener(){
        tv_changeAddr.setOnClickListener(listener);
        tv_showDate.setOnClickListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_firstpage,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_changeAddr:
                    break;
                case R.id.tv_showDate:
                    dateCalendar();
                    break;
            }
        }
    };
    void getInfo() {
        warningInfo.setText(R.string.warning_info);
        orderInfo.setText(R.string.order_info);
    }
    void countTime(){
        tYear = tY;
        tMomth = tM;
        tDay = tD;

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
    }


    public void btnNext2(View v) {
        int[] s =myS.countTime(tYear,tMomth,tDay,tAm_Pm,tHour);
        Log.d(TAG,"s:"+s[0]+s[1]+s[2]+s[4]+tMinute);
        Log.d(TAG,"arr:"+arrayList1);
        myS.compareTime(s[0],s[1],s[2],s[4],tMinute,arrayList1);
        Log.d(TAG,"arr:"+myS.compareTime(s[0],s[1],s[2],s[4],tMinute,arrayList1));
        if(myS.compareTime(s[0],s[1],s[2],s[4],tMinute,arrayList1)){
            Intent it = new Intent();
            it.setClass(TakeMeals.this,VerifyPhone.class);
            startActivity(it);
        }else{
            Toast.makeText(this,"請選擇正確的時間",Toast.LENGTH_SHORT).show();
        }
    }
    public AbstractList<ArrayList<String>> getOpeningTime(){
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"數1:"+dataSnapshot.getChildrenCount()+"數2:"+dataSnapshot.child(1+"").getChildrenCount());
                arrayList1=new ArrayList<ArrayList<String>>();

                for (int j = 0 ;j<dataSnapshot.getChildrenCount();j++) {
                    arrayList2 = new ArrayList<String>();
                    arrayList2.add(0,dataSnapshot.child((j+1)+"").child("weeks").getValue().toString());
                    arrayList2.add(1,dataSnapshot.child((j+1)+"").child("是否營業").getValue().toString());
                    arrayList2.add(2,dataSnapshot.child((j+1)+"").child("開").getValue().toString());
                    arrayList2.add(3,dataSnapshot.child((j+1)+"").child("關").getValue().toString());
                    arrayList1.add(arrayList2);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return arrayList1;
    }



}

