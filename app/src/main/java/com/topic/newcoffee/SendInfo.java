package com.topic.newcoffee;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

import android.provider.Telephony;
import android.support.annotation.NonNull;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SendInfo extends AppCompatActivity {
    EditText et_verify,et_phoneNumber;
    String phone ;
//    public static final int READ_SMS=0;
    String READ_SMS="",SEND_SMS="";
    int REQUEST_SMS=1;
    private SMSContentObserver smsContentObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_info);
        et_phoneNumber = (EditText)findViewById(R.id.et_phoneNumber);

    }

    public void verify(View v) {
        phone = et_phoneNumber.getText().toString();
        if(phone.length()==10 && phone.substring(0,2).equals("09")){
            LayoutInflater inflater = this.getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialog_v = inflater.inflate(R.layout.dialog_verify, null);
            et_verify = (EditText) dialog_v.findViewById(R.id.et_verify);
            builder.setTitle("請輸入驗證碼").setView(dialog_v).setMessage("").setPositiveButton("確認", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String verifyCode = et_verify.getText().toString();
                    getPermission();
                    Log.d("AlertDialog", verifyCode);
                }
            }).setNegativeButton("取消", null).show();

        }else {
            Toast.makeText(SendInfo.this,"請正確輸入手機號碼",Toast.LENGTH_SHORT).show();
        }
    }
    //////////////////取得權限
    void getPermission(){
        int permission = ActivityCompat.checkSelfPermission(this,SEND_SMS);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{READ_SMS,SEND_SMS,},REQUEST_SMS);
        }else{
            sendSMS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_SMS){
            if(grantResults.length>0
                    && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                sendSMS();
            }
        }
    }
//////////////////////////傳送簡訊
    private void sendSMS(){
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SendInfo.this,0,new Intent(),0);
        smsManager.sendTextMessage(phone,null,"123456",pendingIntent,null);
        registerContentObservers();
    }
//////////////////////// 準備ContentObservers
    private void registerContentObservers(){
        Uri uri = Telephony.Sms.CONTENT_URI;
        smsContentObserver = new SMSContentObserver(this, handler);
        getContentResolver().registerContentObserver(uri,true,smsContentObserver);
    }
    private void unregisterContentObservers(){
        getContentResolver().unregisterContentObserver(smsContentObserver);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String code = String.valueOf(msg.what);
            et_verify.setText(code);
            smsVerify(code);
        }
    };
    private void smsVerify(String code){
        if("123456".equals(code)){
            unregisterContentObservers();
            Toast.makeText(this,"簡訊驗證成功",Toast.LENGTH_SHORT).show();
        }
    }
}
///////////收取簡訊驗證
    class SMSContentObserver extends ContentObserver{
        private  static final String TAG = "SMS";
        private Context mcontext;
        private Handler mhandler;

        public SMSContentObserver(Handler handler) {
            super(handler);
            mhandler = handler;
        }

        public SMSContentObserver(Handler handler, Context mcontext, Handler mhandler) {
            super(handler);
            this.mcontext = mcontext;
            this.mhandler = mhandler;
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
//            super.onChange(selfChange, uri);
            if(uri.toString().equals("content://sms/raw")){
                return;
            }
            String[] projection = new String[]{
                    Telephony.Sms.Inbox._ID,
                    Telephony.Sms.Inbox.ADDRESS,
                    Telephony.Sms.Inbox.BODY
            };
            Cursor cursor = mcontext.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI,
                    projection,null,null,Telephony.Sms.Inbox.DATE+" DESC limit 1");
            if(cursor.moveToFirst()){
                String id = cursor.getString(cursor.getColumnIndex(Telephony.Sms.Inbox._ID));
                String address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.Inbox.ADDRESS));
                String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.Inbox.BODY));

                if (address.equals("+886912345678")){
                    Message msg = new Message();
                    msg.what = Integer.parseInt(body);
                    mhandler.sendMessage(msg);
                }
            }
            cursor.close();
        }

    }


