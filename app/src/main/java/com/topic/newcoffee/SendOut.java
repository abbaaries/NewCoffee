package com.topic.newcoffee;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;

public class SendOut extends AppCompatActivity {
    GetFuction myS = new GetFuction();
    ArrayList<ArrayList<String>> arrayList1;
    ArrayList<String> arrayList2;
    Button btnAddAddr;
    private TextView tvAddr,tvChangeAddr,warningInfo2,orderInfo2,tvShowDate2,tvOpenTime2;
    public static int tYear,tMomth,tDay,tAm_Pm= Calendar.AM,tHour=1,tMinute=1;
    public static String address;
    String PREF_ADDRESS,date,TAG="SendOut";
    NumberPicker np7,np8,np9;
    Calendar c;
    String[] values4,values5,values6;
    int tY,tM,tD;
    FirebaseDatabase db;
    DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_out);
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("營業時間");
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
        SharedPreferences settings = getSharedPreferences(PREF_ADDRESS,MODE_PRIVATE);
        address=settings.getString("MYADDRESS","");
        if (address.length()>0){
            btnAddAddr.setVisibility(View.INVISIBLE);
            tvAddr.setText(address);
        }else {
            tvChangeAddr.setVisibility(View.INVISIBLE);
            tvAddr.setVisibility(View.INVISIBLE);
        }

    }

    void findView(){
        btnAddAddr = (Button)findViewById(R.id.btn_addaddr2);
        tvChangeAddr = (TextView)findViewById(R.id.tv_change_address);
        tvAddr = (TextView)findViewById(R.id.tv_address);
        tvShowDate2 = (TextView)findViewById(R.id.tv_showDate2);
        np7 = (NumberPicker)findViewById(R.id.ampmPicker2);
        np8 = (NumberPicker)findViewById(R.id.hourPicker2);
        np9 = (NumberPicker)findViewById(R.id.secPicker2);
        warningInfo2 = (TextView)findViewById(R.id.warning_info32);
        orderInfo2 = (TextView)findViewById(R.id.order_info32);
        tvOpenTime2 = (TextView)findViewById(R.id.tv_openTime2);
//        imageBtn3 = (ImageButton)findViewById(R.id.imgBtn3);
    }
    void setListener(){
        btnAddAddr.setOnClickListener(listener);
        tvChangeAddr.setOnClickListener(listener);
        tvShowDate2.setOnClickListener(listener);
    }

    void setText(){
        orderInfo2.setText("警示");
        orderInfo2.setText("訂購須知");
        tYear = myS.getNow()[0];
        tMomth = myS.getNow()[1];
        tDay = myS.getNow()[2];
        tAm_Pm = myS.getNow()[3];
        tHour = myS.getNow()[4];
        tMinute = myS.getNow()[5];
        tvShowDate2.setText(tYear+"/"+(tMomth>=10?tMomth:("0"+tMomth))+"/"+(tDay>=10?tDay:("0"+tDay)));
//        c = Calendar.getInstance();
//        cNow= Calendar.getInstance();
//        tY = cNow.get(Calendar.YEAR);
//        tM = cNow.get(Calendar.MONTH)+1;
//        tD = cNow.get(Calendar.DAY_OF_MONTH);
//        tvShowDate2.setText(tY+"/"+(tM>=10?tM:("0"+tM))+"/"+(tD>=10?tD:("0"+tD)));
    }
    void dateCalendar(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SendOut.this,datelistener,
                calendar.get(calendar.YEAR),
                calendar.get(calendar.MONTH),
                calendar.get(calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("       選 擇 自 取 日 期");
        datePickerDialog.show();
    }
    DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            tYear = view.getYear();
            tMomth = view.getMonth()+1 ;
            tDay = view.getDayOfMonth();
            tvShowDate2.setText(tYear+"/"+(tMomth>=10?tMomth:("0"+tMomth))+"/"+(tDay>=10?tDay:("0"+tDay)));
//            tY = view.getYear();
//            tM = view.getMonth()+1 ;
//            tD = view.getDayOfMonth();
//            date =year+"/"+(tM>=10?tM:("0"+tM))+"/"+(dayOfMonth<10?"0"+dayOfMonth:dayOfMonth) ;
//            Toast.makeText(SendOut.this,date,Toast.LENGTH_SHORT).show();
//            tvShowDate2.setText(date);
        }
    };

    void picker (){
        values4 = getResources().getStringArray(R.array.time_meridiem);
        Resources res = getResources();
        values5 = res.getStringArray(R.array.hours_12);
        values6 = getResources().getStringArray(R.array.time_interval);
        np7.setMinValue(0);
        np8.setMinValue(0);
        np9.setMinValue(0);
        np7.setMaxValue(values4.length-1);
        np8.setMaxValue(values5.length-1);
        np9.setMaxValue(values6.length-1);
        np7.setDisplayedValues(values4);
        np8.setDisplayedValues(values5);
        np9.setDisplayedValues(values6);
        np7.setWrapSelectorWheel(true);
        np8.setWrapSelectorWheel(false);
        np9.setWrapSelectorWheel(false);
        
        np7.setValue(myS.setPicker()[0]);
        np8.setValue(myS.setPicker()[1]);
        np9.setValue(myS.setPicker()[2]);


        np7.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tAm_Pm = picker.getValue();
                    Log.d("TAG","AM_PM"+tAm_Pm);
            }
        });
        np8.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tHour = picker.getValue()+1;
                Log.d("TAG","Hour"+tHour);
            }
        });

        np9.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tMinute = picker.getValue()*10+1;
                Log.d("TAG","Minute"+tMinute);
            }
        });
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
            Intent it = new Intent();
            switch (v.getId()){
                case R.id.btn_addaddr2:
                case R.id.tv_change_address:
                    it.setClass(SendOut.this,SendOutAddress.class);
                    startActivity(it);
                    break;
                case R.id.tv_showDate2:
                    dateCalendar();
                    break;
            }
        }
    };
    void getInfo() {
        warningInfo2.setText(R.string.warning_info);
        orderInfo2.setText(R.string.order_info);
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
    public void btnNext3(View v) {
        int[] s =myS.countTime(tYear,tMomth,tDay,tAm_Pm,tHour);
        Log.d(TAG,"s:"+s[0]+s[1]+s[2]+s[4]+tMinute);
        Log.d(TAG,"arr:"+arrayList1);
        myS.compareTime(s[0],s[1],s[2],s[4],tMinute,arrayList1);
        Log.d(TAG,"arr:"+myS.compareTime(s[0],s[1],s[2],s[4],tMinute,arrayList1));
        if(myS.compareTime(s[0],s[1],s[2],s[4],tMinute,arrayList1)){
            Intent it = new Intent();
            it.setClass(SendOut.this,VerifyPhone.class);
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
