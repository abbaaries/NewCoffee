package com.topic.newcoffee;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LastMeals extends AppCompatActivity implements AdapterView.OnItemClickListener{
    final String TAG = "ChangeMeals";
    TextView textView;
    ListView listView;
    private DB mDbHelper;
    public static long cRowId;
    public static int item,cAmount,cSum,cKind;
    public static String cName,cType,cSize,cSugar;
    protected static final int MENU_INSERT = Menu.FIRST;
    protected static final int MENU_DELETE = Menu.FIRST + 1;
    protected static final int MENU_UPDATE = Menu.FIRST + 2;
    protected static final int MENU_INSERT_WITH_SPECIFIC_ID = Menu.FIRST + 3;
    int[] img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_meals);
        Log.d(TAG,"LastMeals:onCreate");
        textView = (TextView)findViewById(R.id.tv_allPrice);
        listView = (ListView)findViewById(R.id.listViewOfLast);
        listView.setOnItemClickListener(this);
        setAdapter();
    }
    private void setAdapter() {
        Log.d(TAG,"LastMeals:setAdapter");
        mDbHelper = new DB(this).open();
        Log.d(TAG,"mBHelper"+mDbHelper.toString());
        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"LastMeals:onResume");
        MealList.isUpdate = false;
        fillData();
    }

    void fillData() {
        Log.d(TAG,"LastMeals:fillData");
        Cursor c = mDbHelper.getAll();
        startManagingCursor(c);
        SimpleCursorAdapter scAdapter = new SimpleCursorAdapter(
                this,
                R.layout.simple_list_item_1,
                c,
                new String[]{"咖啡名稱","冷熱","大小","甜度","數量","總價"},
                new int[]{R.id.text_name,R.id.text_type,R.id.text_size,R.id.text_sugar,R.id.text_amount,R.id.text_sum},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        Log.d(TAG,"fillData1");
        listView.setAdapter(scAdapter);
        Log.d(TAG,"fillData2");
        textView.setText(mDbHelper.getAllPrice()+"");

    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
        item=0;
        item = position;
        cRowId = mDbHelper.getrowId(item);
        cKind = mDbHelper.getKind(item);
        cName = mDbHelper.getName(item);
        cType = mDbHelper.getType(item);
        cSize = mDbHelper.getSize(item);
        cSugar = mDbHelper.getSugar(item);
        cAmount = mDbHelper.getAmount(item);
        cSum = mDbHelper.getSum(item);
        AlertDialog.Builder builder = new AlertDialog.Builder(LastMeals.this);
        builder.setTitle("是否修改或刪除" );
        builder.setIcon(android.R.drawable.ic_menu_edit);
        builder.setMessage(cName+"  "+cType+"  "+cSize+"  "+cSugar+"  "+cAmount+"杯  "+cSum+"元");
        builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MealList.isUpdate = true;
                Intent it = new Intent();
                it.setClass(LastMeals.this,CoffeeMenu.class);
                startActivity(it);
                fillData();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.setNeutralButton("刪除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDbHelper.delete(item);
                fillData();
                onResume();
            }
        });
        builder.show();

    }
    ///////  menu   ///////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG,"LastMeals:onCreateOptionsMenu");
        menu.add(0, MENU_INSERT, 0, "新增記事")
                .setIcon(android.R.drawable.ic_menu_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, MENU_DELETE, 0, "刪除記事")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_INSERT:
                MealList.isInsert =true;
                Intent it = new Intent();
                it.setClass(LastMeals.this,CoffeeMenu.class);
                startActivity(it);
                break;
            case MENU_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("確認刪除全部")
                        .setMessage("刪除確認")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDbHelper.delete();
                        fillData();
                    }
                }).setNegativeButton("取消",null).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void btnNext1(View view) {
        Intent it = new Intent();
        if(mDbHelper.getAllPrice()!=0){
            if (MainActivity.isSendOut==true){
                it.setClass(LastMeals.this,SendOut.class);
            }else {
                it.setClass(LastMeals.this,TakeMeals.class);
            }
            startActivity(it);
        }else {
            Toast.makeText(this,"請選擇欲購買的咖啡",Toast.LENGTH_SHORT).show();
        }

    }
}


