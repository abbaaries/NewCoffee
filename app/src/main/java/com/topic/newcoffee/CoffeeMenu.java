package com.topic.newcoffee;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CoffeeMenu extends AppCompatActivity {
    String[] name,midPr,hotPr,icePr,midSize,hotSize,iceSize;
    ImageView coffee_icon;
    TextView tvName,tvMidPr,tvHotPr,tvIcePr,tvMidSize,tvHotSize,tvIceSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_menu);
        setText();

    }
    void setText(){
        int type = getIntent().getIntExtra("type",1);
        Toast.makeText(CoffeeMenu.this,type+"",Toast.LENGTH_SHORT).show();
        name = getResources().getStringArray(R.array.coffee_list);
        midPr = getResources().getStringArray(R.array.coffee_middle_price);
        hotPr = getResources().getStringArray(R.array.coffee_big_hot_price);
        icePr = getResources().getStringArray(R.array.coffee_big_ice_price);
        midSize = getResources().getStringArray(R.array.coffee_middle);
        hotSize = getResources().getStringArray(R.array.coffee_big_hot);
        iceSize = getResources().getStringArray(R.array.coffee_big_ice);

        coffee_icon =(ImageView)findViewById(R.id.img_coffee);

        tvName = (TextView)findViewById(R.id.tv_name);
        tvMidPr = (TextView)findViewById(R.id.tv_mid_Price);
        tvHotPr = (TextView)findViewById(R.id.tv_hotPrice);
        tvIcePr = (TextView)findViewById(R.id.tv_coldPrice);
        tvMidSize = (TextView)findViewById(R.id.tv_midSize);
        tvHotSize = (TextView)findViewById(R.id.tv_hotSize);
        tvIceSize = (TextView)findViewById(R.id.tv_coldSize);

        tvName.setText(name[type]);
        tvMidPr.setText(midPr[type]);
        tvHotPr.setText(hotPr[type]);
        tvIcePr.setText(icePr[type]);
        tvMidSize.setText(midSize[type]);
        tvHotSize.setText(hotSize[type]);
        tvIceSize.setText(iceSize[type]);
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
                Toast.makeText(CoffeeMenu.this,"buy",Toast.LENGTH_SHORT).show();
                break;
            case R.id.home:
                Toast.makeText(CoffeeMenu.this,"回首頁",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void midSize(View view) {
        Toast.makeText(CoffeeMenu.this,"中杯",Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage()
    }

    public void hotSize(View view) {
        Toast.makeText(CoffeeMenu.this,"大杯熱",Toast.LENGTH_SHORT).show();
    }

    public void iceSize(View view) {
        Toast.makeText(CoffeeMenu.this,"大杯冰",Toast.LENGTH_SHORT).show();
    }

}
