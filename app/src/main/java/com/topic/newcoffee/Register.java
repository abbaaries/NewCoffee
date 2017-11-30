package com.topic.newcoffee;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    EditText et_account,et_password,et_name,et_birthday,et_phone,et_eamil,et_address;
    RadioButton rbtn_male;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference userRef = db.getReference("member");
    Map<String,Object> data;
    String account,password,name,birthday,phone,email,address;
    boolean isMale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findView();
//        userRef.child("abba").child("phone").setValue("1234567");
//        userRef.child("abba").child("name").setValue("Sam");




    }

    void findView(){
        et_account = (EditText)findViewById(R.id.et_account);
        et_password = (EditText)findViewById(R.id.et_password);
        et_name = (EditText)findViewById(R.id.et_name);
        et_birthday = (EditText)findViewById(R.id.et_birthday);
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_eamil = (EditText)findViewById(R.id.et_email);
        et_address = (EditText)findViewById(R.id.et_address);
        rbtn_male = (RadioButton)findViewById(R.id.rbtn_male);
    }


    void register(){

        int i = 1;
        data = new HashMap<>();
        data.put("id",i);
        data.put("帳號",account);
        data.put("密碼",password);
        data.put("姓名",name);
        data.put("性別",(isMale)?"男":"女");
        data.put("生日",birthday);
        data.put("電話",phone);
        data.put("E-mail",email);
        data.put("住址",address);
        userRef.setValue(data);
        String usersId = userRef.getKey();
    }
    void update(){
        userRef.updateChildren(data, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError!=null){
                    Toast.makeText(Register.this,"更新完成",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Register.this,"更新失敗",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onRegister(View view) {

        account= et_account.getText().toString();
        password= et_password.getText().toString();
        name= et_name.getText().toString();
        isMale = rbtn_male.isChecked();
        birthday= et_birthday.getText().toString();
        phone= et_phone.getText().toString();
        email= et_eamil.getText().toString();
        address= et_address.getText().toString();

       register();
    }

    public void cancel(View view) {
        finish();
    }
//    private String createJsonString(){
//        String name =et_name.getText().toString();
//        Person p;
//        if(!name.equals("")){
//            String account =et_account.getText().toString();
//            String password =et_password.getText().toString();
//            String birthday =et_birthday.getText().toString();
//            boolean isMale =rbtn_male.isChecked();
//            String phone =et_phone.getText().toString();
//            String email =et_eamil.getText().toString();
//            String address =et_address.getText().toString();
//            p= new Person(account,password,name,birthday,
//            isMale,phone,email,address);
//        }else{
//            p = new Person();
//        }
//        return new Gson().toJson(p);
//    }
//    public void verification_code(View view) {
//    }
//

//    class MyAsyncTask extends AsyncTask<String,Integer,String>{
//        @Override
//        protected String doInBackground(String... params) {
//            return null;
//        }
//    }
//    String uploadData(String jsonString){
//        HttpURLConnection conn = null;
//        OutputStream os = null;
//        InputStream is =null;
//        try {
//            URL url = new URL(HTTP_URL);
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//
//            Uri.Builder builder = new Uri.Builder().appendQueryParameter("JSON",jsonString);
//            String query = builder.build().getEncodedQuery();
//            os = new BufferedOutputStream(conn.getOutputStream());
//            os.write(query.getBytes());
//            os.flush();
//
//            is = new BufferedInputStream(conn.getInputStream());
//            byte[] buf = new byte[1024];
//            is.read(buf);
//            String result = new String(buf);
//            return result.trim();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Send Failed";
//        }finally {
//            try {
//                if(os !=null)
//                    os.close();
//                if(is !=null)
//                    is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if(conn != null)
//                conn.disconnect();
//
//        }
//    }
}
