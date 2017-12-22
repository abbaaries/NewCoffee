package com.topic.newcoffee;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

public class DB {
    final String TAG = "DB";
    private static final String DATABASE_NAME = "mydb.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "mydb";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + "(`_id` INTEGER PRIMARY KEY, `咖啡名稱` TEXT ," +
            "`冷熱` TEXT ,`大小` TEXT ,`甜度` TEXT ,`數量` TEXT ," +
            "`單價` TEXT ,`總價` TEXT ,`取餐時間` TEXT ,`kind` TEXT ) ;";

    private Context mCtx;
    private DataBaseOpenHelper helper;
    private SQLiteDatabase db;

    public DB(Context context) {
        mCtx = context;
    }

    public DB open() {
        helper = new DataBaseOpenHelper(mCtx);
        db = helper.getReadableDatabase();
        return this;
    }
    public void  close(){
        db.close();
    }

    private class DataBaseOpenHelper extends SQLiteOpenHelper {

        public DataBaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    private static final String KEY_rowId = "_id";
    private static final String KEY_NAME = "咖啡名稱";
    private static final String KEY_TYPE = "冷熱";
    private static final String KEY_SIZE = "大小";
    private static final String KEY_SUGAR = "甜度";
    private static final String KEY_AMOUNT = "數量";
    private static final String KEY_UNIT = "單價";
    private static final String KEY_SUM = "總價";
    private static final String KEY_OnTime = "取餐時間";
    private static final String KEY_KIND = "kind";



    String[] strCols = {KEY_rowId, KEY_NAME, KEY_TYPE,KEY_SIZE,KEY_SUGAR,KEY_AMOUNT,KEY_UNIT,KEY_SUM,KEY_OnTime,KEY_KIND};
    String str = "SELECT "+ KEY_NAME + "," +KEY_TYPE + "," + KEY_SIZE +
            "," + KEY_SUGAR + "," + KEY_AMOUNT + "," + KEY_SUM + " FROM "+TABLE_NAME;
    public Cursor getAll() {
        return db.query(TABLE_NAME, strCols, null, null, null, null, null);
    }
    public Cursor getList(){
        return db.rawQuery(str,null);
    }

    public long getrowId(int position){
        Cursor c = db.rawQuery("SELECT "+KEY_rowId+" FROM "+TABLE_NAME,null);
        long id =0;
        if(c!=null){
            c.moveToFirst();
            String[] idList = new String[c.getCount()];
            for (int i =0 ;i<idList.length;i++){
                idList[i] = c.getString(0);
                c.moveToNext();
            }
            id = Integer.valueOf(idList[position]);
        }
        Log.d(TAG,"getrowID:"+id);
        return id;
    }


    public int getAllPrice(){
        Cursor c = db.rawQuery("SELECT "+KEY_SUM+" FROM "+TABLE_NAME,null);
        int sumprice =0;
        if(c!=null){
            c.moveToFirst();
            String[] list = new String[c.getCount()];
            Log.d("TAG","c.count = " +c.getCount());
            for(int i = 0; i<list.length; i++){
                Log.d("TAG",""+KEY_SUM + c.getString(0));
               sumprice += Integer.valueOf(c.getString(0));
                c.moveToNext();
                Log.d(TAG,"sumPR:"+sumprice);
            }
            c.close();
        }
        return sumprice;
    }
    public int getKind (int position){
        long rowId = getrowId(position);
        int kind = Integer.valueOf(get(rowId).getString(9));
        return kind;
    }
    public int getCoffee(String name){
        int sumAmount=0;
        Cursor c = db.rawQuery("SELECT * FROM `"+TABLE_NAME+"` WHERE `"+ KEY_NAME+"`='"+name+"'",null);
        if(c!=null){
            c.moveToFirst();
            String[] coffee = new String[c.getCount()];
            for(int i = 0;i<coffee.length;i++){
                sumAmount += Integer.valueOf(c.getString(5));
                Log.d(TAG,"t:"+c.getString(5));
                c.moveToNext();
            }
            Log.d(TAG,"sumAmount:"+sumAmount);
        }
        return sumAmount;
    }
    public String getName (int position){
        long rowId = getrowId(position);
        String name = get(rowId).getString(1);
        return name;
    }
    public String getType (int position){
        long rowId = getrowId(position);
        String type = get(rowId).getString(2);
        return type;
    }
    public String getSize (int position){
        long rowId = getrowId(position);
        String size = get(rowId).getString(3);
        return size;
    }
    public String getSugar (int position){
        long rowId = getrowId(position);
        String sugar = get(rowId).getString(4);
        return sugar;
    }
    public int getAmount (int position){
        long rowId = getrowId(position);
        int amount = Integer.valueOf(get(rowId).getString(5));
        return amount;
    }
    public int getUnit (int position){
        long rowId = getrowId(position);
        int unit = Integer.valueOf(get(rowId).getString(6));
        return unit;
    }
    public int getSum (int position){
        long rowId = getrowId(position);
        int sum = Integer.valueOf(get(rowId).getString(7));
        return sum;
    }
    public Cursor get(long rowId) throws SQLException {
        Cursor mCursor = db.query(true,
                TABLE_NAME,
                strCols,
                KEY_rowId + "=" + rowId,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long create(String noteName,String noteType, String noteSize,String noteSugar,String noteAmount,String noteUnit,String noteSum,String noteOnTime,String noteKind) {
        Date now = new Date();
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, noteName);
        args.put(KEY_TYPE, noteType);
        args.put(KEY_SIZE, noteSize);
        args.put(KEY_SUGAR, noteSugar);
        args.put(KEY_AMOUNT, noteAmount);
        args.put(KEY_UNIT, noteUnit);
        args.put(KEY_SUM, noteSum);
        args.put(KEY_OnTime, noteOnTime);
        args.put(KEY_KIND,  noteKind);
        Log.d("TAG","create"+args.getAsString(KEY_NAME));
        return db.insert(TABLE_NAME, null, args);
    }

    public boolean delete(int position) {
        long rowId = getrowId(position);
        return db.delete(TABLE_NAME, KEY_rowId + "=" + rowId, null) > 0;
    }
    public boolean delete(){
        return db.delete(TABLE_NAME, null, null) > 0;
    }

    public boolean update(long rowId, String noteType, String noteSize , String noteSugar, String noteAmount,String noteUnit, String noteSum) {
        ContentValues args = new ContentValues();
        args.put(KEY_TYPE, noteType);
        args.put(KEY_SIZE, noteSize);
        args.put(KEY_SUGAR, noteSugar);
        args.put(KEY_AMOUNT, noteAmount);
        args.put(KEY_UNIT, noteUnit);
        args.put(KEY_SUM, noteSum);
        return db.update(TABLE_NAME, args, KEY_rowId + "=" + rowId, null) > 0;
    }


}

