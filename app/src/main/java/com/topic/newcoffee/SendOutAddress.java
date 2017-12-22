package com.topic.newcoffee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SendOutAddress extends AppCompatActivity {
    private Context context;
    ArrayAdapter<CharSequence> adapter;
    Spinner sprCity,sprTown,sprRoad;
    EditText etroadnum,etlanenum,etnum,etnum2,etfloornum,etfloornum2,etroomnum;
    String ADDRESS,loc,city,town,road,PREF_ADDRESS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_out_address);
        findView();
        ArrayAdapter();
        setListener();
        context = this;
    }
    void findView(){
        sprCity = (Spinner) findViewById(R.id.spinner_city);
        sprTown = (Spinner) findViewById(R.id.spinner_town);
        sprRoad = (Spinner) findViewById(R.id.spinner_road);
        etroadnum = (EditText) findViewById(R.id.et_road_num);
        etlanenum = (EditText) findViewById(R.id.et_lane_num);
        etnum = (EditText) findViewById(R.id.et_num);
        etnum2 = (EditText) findViewById(R.id.et_num2);
        etfloornum = (EditText) findViewById(R.id.et_floor_num);
        etfloornum2 = (EditText) findViewById(R.id.et_floor_num2);
        etroomnum = (EditText) findViewById(R.id.et_room_num);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_firstpage,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemSelectedListener itemSelectedListener1 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getSelectedItemPosition()) {
                case 1:
                    adapter = ArrayAdapter.createFromResource(context, R.array.town_name_array, android.R.layout.simple_dropdown_item_1line);
                    sprTown.setAdapter(adapter);
                    break;
                case 2:
                    break;
            }
            city = sprCity.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    AdapterView.OnItemSelectedListener itemSelectedListener2 = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (sprTown.getSelectedItemPosition() != 0) {
                switch (sprTown.getSelectedItemPosition()) {
                    case 1:
                        adapter = ArrayAdapter.createFromResource(context, R.array.houli_road_name_array, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case 2:
                        adapter = ArrayAdapter.createFromResource(context, R.array.fengyuan_road_name_array, android.R.layout.simple_dropdown_item_1line);
                        break;
                }
                sprRoad.setAdapter(adapter);
                town = sprTown.getItemAtPosition(position).toString();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    AdapterView.OnItemSelectedListener itemSelectedListener3 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            road = sprRoad.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    void setListener() {
        sprCity.setOnItemSelectedListener(itemSelectedListener1);
        sprTown.setOnItemSelectedListener(itemSelectedListener2);
        sprRoad.setOnItemSelectedListener(itemSelectedListener3);
    }
    void ArrayAdapter() {
        adapter = ArrayAdapter.createFromResource(this, R.array.city_name_array, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprCity.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.choseTown, android.R.layout.simple_dropdown_item_1line);
        sprTown.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.choseRoad, android.R.layout.simple_dropdown_item_1line);
        sprRoad.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.choseRoad, android.R.layout.simple_dropdown_item_1line);
        sprRoad.setAdapter(adapter);
    }
    void setEtRoadNum(){
        String r1="",r2="",n1="",n2="",f1="",f2="",m="";
        if(etroadnum.length()!=0){
            r1 = etroadnum.getText().toString()+"巷 ";
        }
        if(etlanenum.length()!=0){
            r2 = etlanenum.getText().toString()+"弄 ";
        }
        if(etnum.length()!=0) {
            n1 = etnum.getText().toString()+"號 ";
            if(etnum2.length()!=0) {
                n2 = "之" + etnum2.getText().toString() + " ";
            }
        }
        if(etfloornum.length()!=0){
            f1 = etfloornum.getText().toString()+"樓";
            if(etfloornum2.length()!=0) {
                f2 = "之" + etfloornum2.getText().toString() + " ";
            }
        }else {
            if(etfloornum2.length()!=0)
                Toast.makeText(SendOutAddress.this,"請正確輸入欄位",Toast.LENGTH_SHORT).show();
        }
        if(etroomnum.length()!=0){
            m = etroomnum.getText().toString()+"室";
        }
        loc=r1+r2+n1+n2+f1+f2+m;
    }

    public void btnNext4(View view) {
        if(sprCity.getSelectedItemPosition()!=0&&sprTown.getSelectedItemPosition()!=0&&sprRoad.getSelectedItemPosition()!=0&&etnum.length()!=0){
            setEtRoadNum();
            ADDRESS = city+town+road+loc;
            Toast.makeText(SendOutAddress.this,"已新增完成",Toast.LENGTH_LONG).show();
            SharedPreferences settings = getSharedPreferences(PREF_ADDRESS,MODE_PRIVATE);
            settings.edit().putString("MYADDRESS",ADDRESS).commit();
            Intent it = new Intent();
            it.setClass(SendOutAddress.this,SendOut.class);
            startActivity(it);
            finish();
        }else {
            Toast.makeText(SendOutAddress.this,"請正確輸入欄位",Toast.LENGTH_SHORT).show();
        }
    }
}
