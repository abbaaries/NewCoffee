package com.topic.newcoffee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class MealList extends AppCompatActivity {
    final String TAG ="MealList";
    public static ArrayList<Map<String,Object>> mylist;
    public static boolean isInsert,isUpdate;
    public static int kind,num,money;
    public static TypedArray img;
    private DB mDbHelper;
    String[] name;
    int[] amount,pr;
    HashMap<String,Object> m;
    GridView grid ;
    SimpleAdapter simpleAdapter;
    Intent it;
    SharedPreferences sp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);
        mDbHelper= new DB(this).open();
        pr = new int[6];
        mylist = new ArrayList<>();
        simpleAdapter = new SimpleAdapter(this,mylist,R.layout.myitem, new String[]{"name","total","img"}
                , new int[]{R.id.tv_name,R.id.tv_total,R.id.img_coffee});
        it = new Intent();
        Log.d(TAG,"MealList:onCreate");
        sp = getSharedPreferences("PREF",MODE_PRIVATE);
    }
    void setGrid() {
        Log.d(TAG,"setGrid :");
        mylist.clear();
        name = new String[Integer.valueOf(sp.getString("咖啡種類個數",""))];
        img = getResources().obtainTypedArray(R.array.coffee_images);
        //從shared 抓取名字陣列
        for (int i=0;i<name.length;i++){
            name[i] = sp.getString(("coffee"+i),"");
            Log.d(TAG,"咖啡種類:"+i+sp.getString(("coffee"+i),""));
        }

//        name= getResources().getStringArray(R.array.coffee_list);
        // 從SQLite 抓取杯數資料
        amount = new int[name.length];
        for (int i=0;i<name.length;i++){
            amount[i] = mDbHelper.getCoffee(name[i]);
            Log.d(TAG,"杯數:"+amount[i]);
        }
        //建立 gridView
        for (int i=0;i<name.length;i++) {
            m = new HashMap<>();
            m.put("img", img.getResourceId(i,-1));
            m.put("name", name[i]);
            m.put("total", "x" + amount[i]);
            mylist.add(m);
        }
        grid = (GridView)findViewById(R.id.grid_view_list);
        grid.setAdapter(simpleAdapter);
        grid.setOnItemClickListener(itemClickListener);
    }
        //gridView 設定監聽器
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            kind = position;
            it.setClass(MealList.this,CoffeeMenu.class);
            Log.d(TAG,"MealList kind:"+kind);
            startActivity(it);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        isInsert = false;
        isUpdate = false;
        Log.d(TAG,"onResume kind:"+kind+"num:"+num);
        num=0;
        money=0;
        setGrid();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
        //OptionsMenu的監聽器
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.buy_items:
                if(amount[0]==0&&amount[1]==0&&amount[2]==0&&amount[3]==0&&amount[4]==0&&amount[5]==0&&amount[6]==0) {
                    Toast.makeText(MealList.this,"請選擇欲購買的咖啡",Toast.LENGTH_SHORT).show();
                }else {
                    it.setClass(this, LastMeals.class);
                    startActivity(it);
                }
                break;
            case R.id.home:
                Toast.makeText(MealList.this,"回首頁",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        it.setClass(this,MainActivity.class);
        startActivity(it);
    }

}
