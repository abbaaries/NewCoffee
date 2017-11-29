package com.topic.newcoffee;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Register extends AppCompatActivity {
    private  static final String HTTP_URL = "https://coffee-20171127.firebaseio.com/";
    EditText et_account,et_password,et_name,et_birthday,et_phone,et_eamil,et_address;
    RadioButton rbtn_male;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findView();

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

    private String createJsonString(){
        String name =et_name.getText().toString();
        Person p;
        if(!name.equals("")){
            String account =et_account.getText().toString();
            String password =et_password.getText().toString();
            String birthday =et_birthday.getText().toString();
            boolean isMale =rbtn_male.isChecked();
            String phone =et_phone.getText().toString();
            String email =et_eamil.getText().toString();
            String address =et_address.getText().toString();
            p= new Person(account,password,name,birthday,
            isMale,phone,email,address);
        }else{
            p = new Person();
        }
        return new Gson().toJson(p);
    }
    public void verification_code(View view) {
    }

    public void register(View view) {
        String jsonStr = createJsonString();

    }

    public void cancel(View view) {
        finish();
    }
    class MyAsyncTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }
    String uploadData(String jsonString){
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is =null;
        try {
            URL url = new URL(HTTP_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder().appendQueryParameter("JSON",jsonString);
            String query = builder.build().getEncodedQuery();
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(query.getBytes());
            os.flush();

            is = new BufferedInputStream(conn.getInputStream());
            byte[] buf = new byte[1024];
            is.read(buf);
            String result = new String(buf);
            return result.trim();
        } catch (IOException e) {
            e.printStackTrace();
            return "Send Failed";
        }finally {
            try {
                if(os !=null)
                    os.close();
                if(is !=null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(conn != null)
                conn.disconnect();

        }
    }
}
