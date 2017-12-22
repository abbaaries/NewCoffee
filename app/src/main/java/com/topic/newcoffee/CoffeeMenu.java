package com.topic.newcoffee;



import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;


public class CoffeeMenu extends AppCompatActivity {
    final String TAG ="CoffeeMenu";
    private DB mDbHelper;
    private boolean isInsert=false,isUpdate=false;
    String[] values1,values2,values3,name,middle,bigHot,bigIce;
    NumberPicker nameNp,np1,np2,np3;
    ImageView imgCoffee;
    TextView tvName,tv1,tv2,tv3,tvPrice,tvNum;
    Button btnReduce,btnAdd,btnPlus;
    int amount,kind,newVal3,sum,item;
    String unit,type,size,sugar;
    TypedArray img;
    long rowId;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_menu);
        sp = getSharedPreferences("PREF",MODE_PRIVATE);
        kind = MealList.kind;
        img = getResources().obtainTypedArray(R.array.coffee_images);
        isInsert = MealList.isInsert;
        isUpdate = MealList.isUpdate;
        findView();
        picker();
        setText();
        btClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDbHelper==null)
        mDbHelper= new DB(this).open();
    }
    void findView(){
        tvName = (TextView)findViewById(R.id.tv_coffeeName1);
        tv1 = (TextView)findViewById(R.id.tView1);
        tv2 = (TextView)findViewById(R.id.tView2);
        tv3 = (TextView)findViewById(R.id.tView3);
        tvPrice = (TextView)findViewById(R.id.tv_totalPrice1);
        imgCoffee = (ImageView)findViewById(R.id.img_coffee1);
        nameNp = (NumberPicker)findViewById(R.id.name_Picker1);
        np1 = (NumberPicker)findViewById(R.id.typePicker1);
        np2 = (NumberPicker)findViewById(R.id.sizePicker1);
        np3 = (NumberPicker)findViewById(R.id.sugarPicker1);
        btnReduce = (Button)findViewById(R.id.btn_reduce);
        btnAdd = (Button)findViewById(R.id.btn_add);
        tvNum = (TextView)findViewById(R.id.tv_number);
        btnPlus = (Button)findViewById(R.id.btn);
    }
    void setText(){
        amount=1;
        Log.d(TAG,""+sp.getString("咖啡種類個數",""));
        name = new String[Integer.valueOf(sp.getString("咖啡種類個數",""))];
        middle = new String[Integer.valueOf(sp.getString("咖啡種類個數",""))];
        bigHot = new String[Integer.valueOf(sp.getString("咖啡種類個數",""))];
        bigIce = new String[Integer.valueOf(sp.getString("咖啡種類個數",""))];
        for (int i=0;i<Integer.valueOf(sp.getString("咖啡種類個數",""));i++){
            Log.d(TAG,"sp1"+sp.getString("coffee"+i,""));
            name[i]=sp.getString("coffee"+i,"");
            middle[i]="中杯 : "+sp.getString("middle"+i,"");
            bigHot[i]="大杯(熱) : "+sp.getString("bigHot"+i,"");
            bigIce[i]="大杯(冰) : "+sp.getString("bigIce"+i,"");
        }
        if(isUpdate){
            updateSetText();
        }else {
            if(isInsert){
                insertSetText();
            }else {
                btnPlus.setText("確認");
                nameNp.setVisibility(View.INVISIBLE);
                tvName.setText(name[kind]);
            }
        }
        changedSetText();
    }
    void changedSetText(){
        changeNumber();
        imgCoffee.setImageResource(img.getResourceId(kind,-1));

        tv1.setText(middle[kind]);
        tv2.setText(bigHot[kind]);
        tv3.setText(bigIce[kind]);

    }
    void changeNumber(){
        String s;
        if (type.equals("熱")&&size.equals("大杯")){
            s = sp.getString("bigHotPr"+kind,"")+"元";
            Log.d(TAG,"s1:"+s);
            unit = s.substring(0,s.length()-1);
        }else if(type.equals("冷")&&size.equals("大杯")){
            s = sp.getString("bigIcePr"+kind,"")+"元";
            Log.d(TAG,"s2:"+s);
            unit = s.substring(0,s.length()-1);
        }else {
            s = sp.getString("middlePr"+kind,"")+"元";
            Log.d(TAG,"s3:"+s);
            unit =s.substring(0,s.length()-1);
        }
        tvNum.setText(amount+"");
        Log.d(TAG,"unit:"+unit+",amount"+amount);
        sum = Integer.valueOf(unit)*amount;
        tvPrice.setText(sum+"元");
    }
    void insertSetText(){
        tvName.setVisibility(View.INVISIBLE);
        btnPlus.setText("新增");
        namePicker();
    }
    void updateSetText(){
        nameNp.setVisibility(View.INVISIBLE);
        btnPlus.setText("修改");
        namePicker();
        item=LastMeals.item;
        Log.d(TAG,"item:"+item);
        rowId = LastMeals.cRowId;
        kind = LastMeals.cKind;
        type = LastMeals.cType;
        size = LastMeals.cSize;
        sugar = LastMeals.cSugar;
        amount = LastMeals.cAmount;
        sum = LastMeals.cSum;
        tvName.setText(LastMeals.cName);
        np1.setValue(type.equals("熱")?0:1);
        np2.setValue(size.equals("中杯")?0:1);
        np3.setValue(sugar.equals("無糖")?0:sugar.equals("少糖")?1:2);
        tvNum.setText(amount+"");
        tvPrice.setText(sum+"元");
    }
    void namePicker(){
        nameNp.setMaxValue(0);
        nameNp.setMaxValue(name.length-1);
        nameNp.setDisplayedValues(name);
        nameNp.setWrapSelectorWheel(false);
        nameNp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                kind = picker.getValue();
                changedSetText();
            }
        });
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
                switch (picker.getValue()){
                    case 0: type="熱";break;
                    case 1: type="冷";break;
                }
                changeNumber();
            }
        });
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                switch (picker.getValue()){
                    case 0:size="中杯";break;
                    case 1:size="大杯";break;
                }
                changeNumber();
            }
        });

        np3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                newVal3 = picker.getValue();
                sugar=values3[newVal3];
                Log.d(TAG,"sugar="+sugar);
            }
        });
    }
    void btClickListener(){
        btnReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount>1)
                amount-=1;
                changeNumber();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                amount+=1;
                changeNumber();
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] price = new int[name.length];
                for (int i=0;i<name.length;i++){
                    price[i] = mDbHelper.getCoffee(name[i]);
                    Log.d("TAG55",price[i]+"i"+i);
                }
                write();
                finish();
                amount=1;
            }
        });
    }
    
    void write(){
        Calendar c =Calendar.getInstance();
        String onTime = c.getTime().toString();
        if (isUpdate){
            mDbHelper.update(rowId,type,size,sugar,amount+"",unit,sum+"");
        }else {
            mDbHelper.create(name[kind],type,size,sugar,amount+"",unit,sum+"",onTime,kind+"");
            Toast.makeText(CoffeeMenu.this,"新增成功",Toast.LENGTH_SHORT).show();
        }
    }

}
