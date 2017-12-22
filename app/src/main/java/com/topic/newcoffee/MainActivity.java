package com.topic.newcoffee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String TAG = "Mainactivity" ,userPhone;
    ImageButton btnSendOut,btnTakeMeal;
    public static boolean isSendOut=false,isOpen=false;
    Intent it;
    FirebaseDatabase db;
    DatabaseReference userRef;
    SharedPreferences sp;
    boolean isUpdate=false;
    int number,number2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /////////////////////
        db = FirebaseDatabase.getInstance();
        sp = getSharedPreferences("PREF",MODE_PRIVATE);
        userPhone = sp.getString("PHONE","");
        findView();
        setClickListener();
    }
        void findView(){
            btnSendOut = (ImageButton)findViewById(R.id.imgBtnSend1);
            btnTakeMeal = (ImageButton)findViewById(R.id.imgBtnTake1);
        }
        void setClickListener(){
            btnSendOut.setOnClickListener(listener);
            btnTakeMeal.setOnClickListener(listener);
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                it = new Intent();
                Runnable r1 = new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                };
                Thread t1 = new Thread(r1);
                t1.start();
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                switch (v.getId()){
                    case R.id.imgBtnSend1:
                        isSendOut = true;
                        it.setClass(MainActivity.this,MealList.class);
                        break;
                    case R.id.imgBtnTake1:
                        isSendOut =false;
                        it.setClass(MainActivity.this,MealList.class);
                        break;
                }
                startActivity(it);
            }
        };

    @Override
    protected void onResume() {
        super.onResume();
        isOpenF();
        getData();
    }
    public void isOpenF(){
        userRef = db.getReference();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isOpen = dataSnapshot.child("營業").getValue().toString().equals("true")?true:false;
                Log.d(TAG,"營業:"+dataSnapshot.child("營業").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getData(){

        Log.d(TAG,"getData");
        userRef = db.getReference("coffee");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number=0;
//                isOpen = dataSnapshot.child("營業").getValue().toString().equals("true")?true:false;
                for(DataSnapshot db : dataSnapshot.getChildren()){
                    sp.edit().putString("coffee"+number,db.child("name").getValue().toString()).commit();
                    sp.edit().putString("middle"+number,db.child("中杯").getValue().toString()).commit();
                    sp.edit().putString("bigIce"+number,db.child("大杯(冰)").getValue().toString()).commit();
                    sp.edit().putString("bigHot"+number,db.child("大杯(熱)").getValue().toString()).commit();
                    sp.edit().putString("middlePr"+number,db.child("中杯價").getValue().toString()).commit();
                    sp.edit().putString("bigIcePr"+number,db.child("大杯(冰)價").getValue().toString()).commit();
                    sp.edit().putString("bigHotPr"+number,db.child("大杯(熱)價").getValue().toString()).commit();
                    sp.edit().putString("bigHotPr"+number,db.child("大杯(熱)價").getValue().toString()).commit();
                    sp.edit().putString("store"+number,db.child("存貨").getValue().toString()).commit();
                    Log.d(TAG,"number:"+number);
                    Log.d(TAG,"name:"+db.child("name").getValue().toString());
                    Log.d(TAG,"中杯:"+db.child("中杯").getValue().toString());
                    Log.d(TAG,"中杯價:"+db.child("中杯價").getValue().toString());
                    Log.d(TAG,"大杯(冰):"+db.child("大杯(冰)").getValue().toString());
                    Log.d(TAG,"大杯(冰)價:"+db.child("大杯(冰)價").getValue().toString());
                    Log.d(TAG,"大杯(熱)"+db.child("大杯(熱)").getValue().toString());
                    Log.d(TAG,"大杯(熱)價:"+db.child("大杯(熱)價").getValue().toString());
                    Log.d(TAG,"存貨:"+db.child("存貨").getValue().toString());
                    number++;
                }
//                Log.d(TAG,"dataSnapshot:"+dataSnapshot.getValue().toString());
//                if(dataSnapshot.child("users").hasChildren()){
//                    if (!userPhone.equals("")){
//                        for (int i=1;i<=dataSnapshot.child("users").getChildrenCount();i++){
//                            if(dataSnapshot.child("users").child(i+"").child("phone").getValue().toString().equals(userPhone)){
//                                j=i;
//                                isUpdate = dataSnapshot.child("users").child(j+"").child("isUpdate").getValue().toString().equals("true")?true:false;
//                            }else {
//                                isUpdate = false;
//                            }
//                        }
//
//                    }
//                }
//
//                Log.d(TAG,"ss14:"+sp.getString("咖啡種類個數","").equals("")+"/"+sp.getString("咖啡種類個數","")+"/"+isUpdate);
//
//                if ((sp.getString("咖啡種類個數","").equals("")) ||isUpdate){
////                    取得firebase咖啡資料
//                    for (int i=0;i<dataSnapshot.child("coffee").getChildrenCount();i++){
//                        sp.edit().putString("coffee"+i,dataSnapshot.child("coffee").child((i+1)+"").child("name").getValue().toString()).commit();
//                        sp.edit().putString("middle"+i,dataSnapshot.child("coffee").child((i+1)+"").child("中杯").getValue().toString()).commit();
//                        sp.edit().putString("bigIce"+i,dataSnapshot.child("coffee").child((i+1)+"").child("大杯(冰)").getValue().toString()).commit();
//                        sp.edit().putString("bigHot"+i,dataSnapshot.child("coffee").child((i+1)+"").child("大杯(熱)").getValue().toString()).commit();
//                        sp.edit().putString("middlePr"+i,dataSnapshot.child("coffee").child((i+1)+"").child("中杯價").getValue().toString()).commit();
//                        sp.edit().putString("bigIcePr"+i,dataSnapshot.child("coffee").child((i+1)+"").child("大杯(冰)價").getValue().toString()).commit();
//                        sp.edit().putString("bigHotPr"+i,dataSnapshot.child("coffee").child((i+1)+"").child("大杯(熱)價").getValue().toString()).commit();
//
//                        Log.d(TAG,"ss14:"+dataSnapshot.child("coffee").child((i+1)+"").child("存貨").getValue().toString());
//                    }
//                    sp.edit().putString("咖啡種類個數",dataSnapshot.child("營業時間").getChildrenCount()+"").commit();
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef = db.getReference("店址");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number2=0;
                for (DataSnapshot db1 : dataSnapshot.getChildren()){
                    number2++;
                    sp.edit().putString("店數",number2+"").commit();
                    sp.edit().putString(number2+"town",db1.child("town").getValue().toString()).commit();
                    sp.edit().putString(number2+"店址",db1.child("住址").getValue().toString()).commit();
                    Log.d(TAG,"number2:"+number2);
                    Log.d(TAG,"town:"+db1.child("town").getValue().toString());
                    Log.d(TAG,"住址:"+db1.child("住址").getValue().toString());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //取得firebase店址資料
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(sp.getString("店數","").equals("")||isUpdate){
//                    Log.d(TAG,"sp1:"+dataSnapshot.child("店址").getChildrenCount());
//                    for(int i=0;i<dataSnapshot.child("店址").getChildrenCount();i++){
//                        Log.d(TAG,"sp2:"+dataSnapshot.child("店址").child((i+1)+"").child("住址").getValue().toString());
//                        sp.edit().putString("店數",dataSnapshot.child("店址").getChildrenCount()+"").commit();
//                        sp.edit().putString(i+"店址",dataSnapshot.child("店址").child((i+1)+"").child("住址").getValue().toString()).commit();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
/////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
