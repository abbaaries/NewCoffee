package com.topic.newcoffee;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.util.HashMap;
import java.util.Map;

public class VerifyPhone extends AppCompatActivity {
    String TAG = "Verifyphone",PHONE="PHONE";

    EditText etPhone,etVerifyCode;
    TextView tvShopAddress,tvTakingTime,tvLastPrice;
    ListView listView;
    Button btnGetCode,btnVerify;
    ImageButton btnSend;
    String userPhone,day,time,str,code;
    int count,userId,tYear,tMomth,tDay,tAm_Pm,tHour,tMinute,times=0,k,j,t,i=1;
    FirebaseDatabase db ;
    DatabaseReference userRef ;
    DB mDBHelper;
    SharedPreferences sp;
    boolean isVerify=false,isOpen,bo=false,b1,b2,b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        sp = getSharedPreferences("PREF",MODE_PRIVATE);
        isOpen=MainActivity.isOpen;
        mDBHelper = new DB(this).open();
        findView();
        setText();
        setListener();
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("users");
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOpenFun();
    }

    void isOpenFun(){
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference();
        if(!bo){
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    isOpen =(dataSnapshot.child("營業").getValue().toString().equals("true")?true:false);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    void findView(){
        etPhone = (EditText)findViewById(R.id.etPhone);
        etVerifyCode = (EditText)findViewById(R.id.et_verify_code);
        tvShopAddress = (TextView)findViewById(R.id.tvShopAddress);
        tvTakingTime = (TextView)findViewById(R.id.tvTakingTime);
        listView = (ListView)findViewById(R.id.listViewOfMeals);
        tvLastPrice = (TextView)findViewById(R.id.tv_lastPrice);
        btnGetCode = (Button)findViewById(R.id.btn_getVerifyCode);
        btnVerify = (Button)findViewById(R.id.btn_verifyCode);
        btnSend = (ImageButton)findViewById(R.id.imgBtnSend);
    }
    void setListener(){
        btnGetCode.setOnClickListener(listener);
        btnVerify.setOnClickListener(listener);
        btnSend.setOnClickListener(listener);
    }
    void setText(){
        times=0;
        etPhone.setText(sp.getString(PHONE,""));
        if(MainActivity.isSendOut){
            tvShopAddress.setText(SendOut.address);
            tYear = SendOut.tYear;
            tMomth = SendOut.tMomth;
            tDay = SendOut.tDay;
            tAm_Pm = SendOut.tAm_Pm;
            tHour = SendOut.tHour;
            tMinute = SendOut.tMinute;
            str="外送";
        }else {
            tvShopAddress.setText(TakeMeals.shopAddress);
            tYear = TakeMeals.tYear;
            tMomth = TakeMeals.tMomth;
            tDay = TakeMeals.tDay;
            tAm_Pm = TakeMeals.tAm_Pm;
            tHour = TakeMeals.tHour;
            tMinute = TakeMeals.tMinute;
            str="自取";
        }
        setTime();
        setAdapter();

        tvLastPrice.setText("共 "+mDBHelper.getAllPrice()+" 元");
    }
    void setTime(){
        String month =(tMomth>9?""+tMomth:"0"+tMomth);
        String d =(tDay>9?""+tDay:"0"+tDay);

        int tHour2 = tHour;
        int tMin2 = tMinute+9;
        if(tMin2>=60){
            tHour2+=1;
            tMin2-=60;
        }
        time =(tAm_Pm>0?"PM ":"AM ")+
                (tHour>12?(tHour-12)>=10?(tHour-12):"0"+(tHour-12):tHour>=10?tHour:"0"+tHour)+
                ":"+(tMinute>=10?tMinute:"0"+tMinute)+
                "~"+(tHour2>12?(tHour2-12)>=10?(tHour2-12):"0"+(tHour2-12):tHour2>=10?tHour2:"0"+tHour2)+
                ":"+(tMin2>=10?tMin2:"0"+tMin2);
        tvTakingTime.setText(tYear+"/"+month+"/"+d+" "+time);
        day = tYear+month+d;
    }
    void setAdapter(){
        Cursor c = mDBHelper.getAll();
        SimpleCursorAdapter scAdapter = new SimpleCursorAdapter(this,
                R.layout.simple_list_item_2,
                c,
                new String[]{"咖啡名稱","冷熱","大小","甜度","數量","總價"},
                new int[]{R.id.text_name2,R.id.text_type2,R.id.text_size2,R.id.text_sugar2,R.id.text_amount2,R.id.text_sum2},
                android.support.v4.widget.SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(scAdapter);
    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG,"listener:");
            isVerify=false;
            if(isOpen){
                userPhone=etPhone.getText().toString()+"";
                if(userPhone.length()==10&&userPhone.substring(0,2).equals("09")){
                    userRef = db.getReference("users");
//                    getVerify();
                   Runnable r1 = new Runnable() {
                       @Override
                       public void run() {
                           getVerify();
                       }
                   };
                   Thread t1 = new Thread(r1);
                    t1.start();
                    try {
                        t1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    switch (v.getId()) {
                        case R.id.btn_getVerifyCode:

                            getCode();
                            break;
                        case R.id.btn_verifyCode:
                            VerifingCode();
                            break;
                        case R.id.imgBtnSend:
                            sendOrder();
                            break;
                        }
                }else {
                    Toast.makeText(VerifyPhone.this,"請輸入正確手機號碼",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(VerifyPhone.this,"非營業時間",Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void getCode() {
        userRef = db.getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                getVerify();
                userRef = db.getReference("users");
                if(isVerify){
                    Toast.makeText(VerifyPhone.this,"手機號碼已完成驗證",Toast.LENGTH_SHORT).show();
                }else {
                    code = String.format("%04d",(int)(Math.random()*10000));
                    times++;
                    if(userId==0){
                        count++;
                        userRef.child(count+"").child("code").setValue(code);
                        userRef.child(count+"").child("msm").setValue(false);
                        userRef.child(count+"").child("isUpdate").setValue(false);
                        userRef.child(count+"").child("phone").setValue(userPhone);
                        userRef.child(count+"").child("sendCode").setValue(false);
                        Toast.makeText(VerifyPhone.this,"已送出手機驗證碼",Toast.LENGTH_SHORT).show();
                    }else {
                        if(times==1){
                            userRef.child(userId+"").child("code").setValue(code);
                            Toast.makeText(VerifyPhone.this,"已送出手機驗證碼",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(VerifyPhone.this,"請稍等片刻，收取簡訊",Toast.LENGTH_SHORT).show();
                            if(times==4){
                                times=0;
                            }
                        }

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void VerifingCode(){
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userRef = db.getReference("users");
//                getVerify();
                if(isVerify){
                    Toast.makeText(VerifyPhone.this, "手機號碼已完成驗證", Toast.LENGTH_SHORT).show();
                }else {
                    if(userId==0){
                        Toast.makeText(VerifyPhone.this, "請先取得驗證碼", Toast.LENGTH_SHORT).show();
                    }else {
                        if(etVerifyCode.length() == 4){
                            if(etVerifyCode.getText().toString().equals(code)){
                                userRef.child(userId+"").child("msm").setValue(true);
                                Toast.makeText(VerifyPhone.this, "驗證成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(VerifyPhone.this, "驗證碼不正確", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(VerifyPhone.this, "請填入正確的驗證碼", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getVerify(){
//        userRef = db.getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count=0;
                userId=0;
                code="0";
                for(DataSnapshot db1 : dataSnapshot.getChildren()){
                    count++;
                    if (db1.child("phone").getValue().toString().equals(userPhone)){
                        isVerify=db1.child("msm").getValue().toString().equals("true");
                        userId=count;
                        code=db1.child("code").getValue().toString();
                    }
                    Log.d(TAG,"phone:"+db1.child("phone").getValue().toString());
                }
                if(userId==0){times=0;}
                Log.d(TAG,"count:"+count);
                Log.d(TAG,"isVerify:"+isVerify);
                Log.d(TAG,"userId:"+userId);
                Log.d(TAG,"code:"+code);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void sendOrder() {

        userRef = db.getReference("order");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                count = 0;
                for (DataSnapshot db1:dataSnapshot.getChildren()){
                    if (db1.hasChild(time)){
                        for (DataSnapshot db2:db1.getChildren()){
                            count=(int)db2.getChildrenCount();
                        }
                    }
                }
                if(isVerify){
                    AlertDialog alertDialog = new AlertDialog.Builder(VerifyPhone.this)
                                .setTitle("是否送出訂單")
                                .setNeutralButton("取消",null)
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        count = 1;
                                        Cursor c = mDBHelper.getAll();
                                        c.moveToFirst();
                                        for (int t = 0; t < c.getCount(); t++) {
                                            count++;
                                            userRef.child(day).child(time).child(count+"").child("手機").setValue(userPhone);
                                            userRef.child(day).child(time).child(count+"").child("咖啡").setValue(c.getString(1));
                                            userRef.child(day).child(time).child(count+"").child("冷熱").setValue(c.getString(2));
                                            userRef.child(day).child(time).child(count+"").child("大小").setValue(c.getString(3));
                                            userRef.child(day).child(time).child(count+"").child("甜度").setValue(c.getString(4));
                                            userRef.child(day).child(time).child(count+"").child("數量").setValue(c.getString(5));
                                            userRef.child(day).child(time).child(count+"").child("單價").setValue(c.getString(6));
                                            userRef.child(day).child(time).child(count+"").child("總價").setValue(c.getString(7));
                                            userRef.child(day).child(time).child(count+"").child("完成").setValue(false);

//                                            userRef.child(userId+"").child(str).child(day+time).child(count +"").child("咖啡").setValue(c.getString(1));
//                                            userRef.child(userId+"").child(str).child(day+time).child(count +"").child("咖啡").setValue(c.getString(1));
//                                            userRef.child(userId+"").child(str).child(day+time).child(count +"").child("冷熱").setValue(c.getString(2));
//                                            userRef.child(userId+"").child(str).child(day+time).child(count +"").child("大小").setValue(c.getString(3));
//                                            userRef.child(userId+"").child(str).child(day+time).child(count +"").child("甜度").setValue(c.getString(4));
//                                            userRef.child(userId+"").child(str).child(day+time).child(count +"").child("數量").setValue(c.getString(5));
//                                            userRef.child(userId+"").child(str).child(day+time).child(count +"").child("單價").setValue(c.getString(6));
//                                            userRef.child(userId+"").child(str).child(day+time).child(count +"").child("總價").setValue(c.getString(7));
                                            c.moveToNext();

//                                            userRef.child(userId+"").child(str).child(day+time).child("總共").setValue(mDBHelper.getAllPrice());
//                                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                                    if(dataSnapshot.child(userId+"").child(str).child(day+time).child("總共").getValue().toString().equals(mDBHelper.getAllPrice()+"")){
//                                                        Toast.makeText(VerifyPhone.this,"已送出訂單",Toast.LENGTH_SHORT).show();
//                                                        sp.edit().putString(PHONE,userPhone).commit();           //驗證成功 記下手機號碼
//                                                    }else {
//                                                        Toast.makeText(VerifyPhone.this,"未送出訂單",Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(DatabaseError databaseError) {
//
//                                                }
//                                            });
                                        }
                                    }
                                }).show();
                }else {
                    Toast.makeText(VerifyPhone.this,"請先完成手機驗證",Toast.LENGTH_SHORT).show();
                }
//                getVerify();
//                if(dataSnapshot.hasChildren()){
//                    if(isVerify){
//                        Toast.makeText(VerifyPhone.this,"已完成手機驗證",Toast.LENGTH_SHORT).show();
//                        AlertDialog alertDialog = new AlertDialog.Builder(VerifyPhone.this)
//                                .setTitle("是否送出訂單")
//                                .setNeutralButton("取消",null)
//                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        count = 1;
//                                        Cursor c = mDBHelper.getAll();
//                                        c.moveToFirst();
//                                        for (int t = 0; t < c.getCount(); t++) {
//                                            userRef.child(k+"").child(str).child(day+time).child(count +"").child("咖啡").setValue(c.getString(1));
//                                            userRef.child(k+"").child(str).child(day+time).child(count +"").child("咖啡").setValue(c.getString(1));
//                                            userRef.child(k+"").child(str).child(day+time).child(count +"").child("冷熱").setValue(c.getString(2));
//                                            userRef.child(k+"").child(str).child(day+time).child(count +"").child("大小").setValue(c.getString(3));
//                                            userRef.child(k+"").child(str).child(day+time).child(count +"").child("甜度").setValue(c.getString(4));
//                                            userRef.child(k+"").child(str).child(day+time).child(count +"").child("數量").setValue(c.getString(5));
//                                            userRef.child(k+"").child(str).child(day+time).child(count +"").child("單價").setValue(c.getString(6));
//                                            userRef.child(k+"").child(str).child(day+time).child(count +"").child("總價").setValue(c.getString(7));
//                                            c.moveToNext();
//                                            count++;
//                                            userRef.child(k+"").child(str).child(day+time).child("總共").setValue(mDBHelper.getAllPrice());
//                                            Log.d(TAG,"ss221:"+mDBHelper.getAllPrice());
//                                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                                    if(dataSnapshot.child(k+"").child(str).child(day+time).child("總共").getValue().toString().equals(mDBHelper.getAllPrice()+"")){
//                                                        Toast.makeText(VerifyPhone.this,"已送出訂單",Toast.LENGTH_SHORT).show();
//                                                        sp.edit().putString(PHONE,userPhone).commit();           //驗證成功 記下手機號碼
//                                                    }else {
//                                                        Toast.makeText(VerifyPhone.this,"未送出訂單",Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(DatabaseError databaseError) {
//
//                                                }
//                                            });
//                                        }
//                                    }
//                                }).show();
//                    }else {
//                        Toast.makeText(VerifyPhone.this,"請先完成手機驗證",Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    Toast.makeText(VerifyPhone.this,"請先取得驗證碼，完成手機驗證",Toast.LENGTH_SHORT).show();
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
class firebaseThread extends Thread{

}
