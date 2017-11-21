package com.topic.newcoffee;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

public class MealList extends AppCompatActivity {
    GridView grid ;
    String[] fun = {"招牌拿鐵","栗子拿鐵","招牌咖啡","美式咖啡","摩卡咖啡","焦糖瑪奇朵"};
    int[] icons = {R.drawable.coffee1, R.drawable.coffee2, R.drawable.coffee3,
            R.drawable.coffee4, R.drawable.coffee5, R.drawable.coffee6,R.drawable.home};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);

        grid = (GridView)findViewById(R.id.grid_view_list);
        grid.setAdapter(new ImageAdapter(this));
        setListener();
    }
    class ImageAdapter extends BaseAdapter {

        Context magcontext;
        ImageAdapter(Context context){
            magcontext = context;
        }
        @Override
        public int getCount() {
            return fun.length;
        }

        @Override
        public Object getItem(int position) {

            return fun[position];
        }

        @Override
        public long getItemId(int position) {
            return icons[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            DisplayMetrics dm =new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int w = dm.widthPixels;
            int h = dm.heightPixels;
            ImageView imageView = new ImageView(magcontext);
            imageView.setLayoutParams(new GridView.LayoutParams(w/2,h*53/200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
            imageView.setImageResource(icons[position]);
            TextView textView = new TextView(magcontext);
            textView.setGravity(1);
            textView.setText(fun[position]);
            LinearLayout linearLayout = new LinearLayout(magcontext);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            return linearLayout;
        }
    }
    void setListener(){
        grid.setOnItemClickListener(itemClickListener);
    }
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            boolean isSendOut;
            Toast.makeText(MealList.this,parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
            Intent it = new Intent();
            it.setClass(MealList.this,CoffeeMenu.class);
            it.putExtra("type",position);
            isSendOut=getIntent().getBooleanExtra("isSendOut",false);
            it.putExtra("isSendOut",isSendOut);
            startActivity(it);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.buy_items:
                Toast.makeText(MealList.this,"buy",Toast.LENGTH_SHORT).show();
                break;
            case R.id.home:
                Toast.makeText(MealList.this,"回首頁",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
