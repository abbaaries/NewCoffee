package com.topic.newcoffee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    GridView grid ;
    ArrayList<Map<String,Object>> mylist,billlist;
    SimpleAdapter simpleAdapter;
    String[] fun;
    Intent it;
    HashMap<String,Object> m1,m2,m3,m4,m5,m6,m7;
    public static int kind,amount0,amount1,amount2,amount3,amount4,amount5,amount6,pr0,pr1,pr2,pr3,pr4,pr5,pr6,num,money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mylist = new ArrayList<>();
        setContentView(R.layout.activity_meal_list);
        simpleAdapter = new SimpleAdapter(this,mylist,R.layout.myitem,
                new String[]{"name","total","img"}, new int[]{R.id.tv_name,R.id.tv_total,R.id.img_coffee});
        m1 = new HashMap<>();
        m2 = new HashMap<>();
        m3 = new HashMap<>();
        m4 = new HashMap<>();
        m5 = new HashMap<>();
        m6 = new HashMap<>();
        m7 = new HashMap<>();
        it = new Intent();
        Log.d("test","onCreate");
    }
    void setGrid() {
        fun = getResources().getStringArray(R.array.coffee_list);
        mylist.clear();
        m1.put("name", fun[0]);
        m1.put("total", "x" + amount0);
        m1.put("img", R.drawable.coffee1);
        m2.put("name", fun[1]);
        m2.put("total", "x" + amount1);
        m2.put("img", R.drawable.coffee2);
        m3.put("name", fun[2]);
        m3.put("total", "x" + amount2);
        m3.put("img", R.drawable.coffee3);
        m4.put("name", fun[3]);
        m4.put("total", "x" + amount3);
        m4.put("img", R.drawable.coffee4);
        m5.put("name", fun[4]);
        m5.put("total", "x" + amount4);
        m5.put("img", R.drawable.coffee5);
        m6.put("name", fun[5]);
        m6.put("total", "x" + amount5);
        m6.put("img", R.drawable.coffee6);
        m7.put("name", fun[6]);
        m7.put("total", "x" + amount6);
        m7.put("img", R.drawable.coffee7);
        mylist.add(m1);
        mylist.add(m2);
        mylist.add(m3);
        mylist.add(m4);
        mylist.add(m5);
        mylist.add(m6);
        mylist.add(m7);
        grid = (GridView)findViewById(R.id.grid_view_list);
        grid.setAdapter(simpleAdapter);
        grid.setOnItemClickListener(itemClickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            kind = position;
            it.setClass(MealList.this,CoffeeMenu.class);
            it.putExtra("kind",kind);
            Log.d("test11","kind="+kind);
            startActivity(it);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("test55","onResume");
        Log.d("test11",kind+"num="+num);
        switch (kind){
            case 0:
                amount0+=num;
                pr0+=money;
                Log.d("test12","amount0="+pr0);
                break;
            case 1:
                amount1+=num;
                pr1+=money;
                Log.d("test12","amount1="+pr1);
                break;
            case 2:
                amount2+=num;
                pr2+=money;
                Log.d("test12","amount2="+pr2);
                break;
            case 3:
                amount3+=num;
                pr3+=money;
                Log.d("test12","amount3="+pr3);
                break;
            case 4:
                amount4+=num;
                pr4+=money;
                Log.d("test12","amount4="+pr4);
                break;
            case 5:
                amount5+=num;
                pr5+=money;
                Log.d("test12","amount5="+pr5);
                break;
            case 6:
                amount6+=num;
                pr6+=money;
                Log.d("test12","amount6="+pr6);
                break;
        }
        num=0;
        money=0;
        setGrid();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.buy_items:
                if(pr0==0&&pr1==0&&pr2==0&&pr3==0&&pr4==0&&pr5==0&&pr6==0) {
                    Toast.makeText(MealList.this,"請選擇欲購買的咖啡",Toast.LENGTH_SHORT).show();

                }else {
                    it.setClass(this, BillList.class);
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
}
