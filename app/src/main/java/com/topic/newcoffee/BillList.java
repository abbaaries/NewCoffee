package com.topic.newcoffee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class BillList extends AppCompatActivity {
    SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);

        simpleAdapter = new SimpleAdapter(this,MealList.billlist,R.layout.mybillitem,new String[]{"goods","number"},new int[]{R.id.tv_goods,R.id.tv_number});
    }
}
