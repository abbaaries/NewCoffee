package com.topic.newcoffee;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CoffeeMenu extends AppCompatActivity {
    String[] values1,values2,values3;
    NumberPicker np1,np2,np3;
    ImageView imgCoffee;
    TextView tvName,tv1,tv2,tv3,tvPrice,tvNum;
    Button btnReduce,btnAdd,btnPlus;
    int count=1,kind=0,newVal1=0,newVal2,newVal3=0,values;
    String unit="0",type,size,sugar;
    int[] img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_menu);
        SharedPreferences pref = getSharedPreferences("bill",MODE_PRIVATE);

        findView();
        kind = MealList.kind;
        img = MealList.img;
        setText();
        picker();
        btClickListener();

    }
    void findView(){
        tvName = (TextView)findViewById(R.id.tv_coffeeName);
        tv1 = (TextView)findViewById(R.id.textView1);
        tv2 = (TextView)findViewById(R.id.textView2);
        tv3 = (TextView)findViewById(R.id.textView3);
        tvPrice = (TextView)findViewById(R.id.tv_totalPrice);
        imgCoffee = (ImageView)findViewById(R.id.img_coffee);

        np1 = (NumberPicker)findViewById(R.id.numberPicker1);
        np2 = (NumberPicker)findViewById(R.id.numberPicker2);
        np3 = (NumberPicker)findViewById(R.id.numberPicker3);
        btnReduce = (Button)findViewById(R.id.btn_reduce);
        btnAdd = (Button)findViewById(R.id.btn_add);
        btnPlus = (Button)findViewById(R.id.btn_plus);
        tvNum = (TextView)findViewById(R.id.tv_number);
    }
    void setText(){
        imgCoffee.setImageResource(img[kind]);
        tv1.setText(R.string.midsize);
        tv2.setText(R.string.hotbigsize);
        tv3.setText(R.string.icebigSize);
        if(kind==2|kind==3){
            tv3.setText(R.string.icebigSize2);
        }else if(kind==5){
            tv2.setText(R.string.hotbigsize2);
            tv3.setText(R.string.icebigSize2);
        }
        tvNum.setText(count+"");
        String name[] = getResources().getStringArray(R.array.coffee_list);
        String price[] = getResources().getStringArray(R.array.coffee_middle_price);
        tvName.setText(name[kind]);
        unit =price[kind];
        tvPrice.setText(unit);
    }
    void picker (){
        values1 = getResources().getStringArray(R.array.hot_cold_size);
        values2 = getResources().getStringArray(R.array.size1);
        values3 = getResources().getStringArray(R.array.meal_sugar);
        type=values1[0];
        size=values2[0];
        sugar=values3[0];
        np1.setMinValue(0);
        np2.setMinValue(0);
        np3.setMinValue(0);
        np1.setMaxValue(values1.length-1);
        np2.setMaxValue(values2.length-1);
        np3.setMaxValue(values3.length-1);
        np1.setDisplayedValues(values1);
        np2.setDisplayedValues(values2);
        np3.setDisplayedValues(values3);
        np1.setWrapSelectorWheel(true);
        np2.setWrapSelectorWheel(true);
        np3.setWrapSelectorWheel(true);
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                int kind = getIntent().getIntExtra("kind",0);
                newVal1=newVal;
                if( 1==newVal2 && 0==newVal1 )
                    unit = getResources().getStringArray(R.array.coffee_big_hot_price)[kind];
                else if( 1==newVal2  && 1==newVal1 )
                    unit = getResources().getStringArray(R.array.coffee_big_ice_price)[kind];
                else
                    unit = getResources().getStringArray(R.array.coffee_middle_price)[kind];
                type = values1[newVal1];
                tvPrice.setText(unit);
                tvPrice.setText(String.valueOf(count*Integer.valueOf(unit.substring(0,unit.length()-1)))+"元");
                Log.d("test1","type="+type+",unit="+unit+",newVal1="+newVal1+",newVal2="+newVal2);
                Log.d("test","種類="+kind+",type="+type+",size="+size+",sugar="+sugar+",數量="+count);
            }
        });
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                int kind = getIntent().getIntExtra("kind", 0);
                newVal2=newVal;
                if(newVal1 == 0 && newVal2 == 1 )
                    unit = getResources().getStringArray(R.array.coffee_big_hot_price)[kind];
                else if(newVal1 == 1 && newVal2 == 1 )
                    unit = getResources().getStringArray(R.array.coffee_big_ice_price)[kind];
                else
                    unit = getResources().getStringArray(R.array.coffee_middle_price)[kind];
                size = values2[newVal2];
                tvPrice.setText(unit);
                tvPrice.setText(String.valueOf(count*Integer.valueOf(unit.substring(0,unit.length()-1)))+"元");

                Log.d("test1","size="+size+",unit="+unit+",newVal1="+newVal1+",newVal2="+newVal2);
                Log.d("test","種類="+kind+",type="+type+",size="+size+",sugar="+sugar+",數量="+count);
            }
        });

        np3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sugar=values3[newVal3];
                newVal3=newVal;
                Log.d("test1","sugar="+sugar);
            }
        });
    }
    void btClickListener(){
        btnReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>1)
                count-=1;
                tvNum.setText(count+"");
                tvPrice.setText(String.valueOf(count*Integer.valueOf(unit.substring(0,unit.length()-1)))+"元");

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                count+=1;
                tvNum.setText(count+"");
                tvPrice.setText(String.valueOf(count*Integer.valueOf(unit.substring(0,unit.length()-1)))+"元");
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","種類="+kind+",type="+type+",size="+size+",sugar="+sugar+",數量="+count);

                MealList.money = count*Integer.valueOf(unit.substring(0,unit.length()-1));
                MealList.num = count;
                finish();
                count=0;
            }
        });
    }

}
