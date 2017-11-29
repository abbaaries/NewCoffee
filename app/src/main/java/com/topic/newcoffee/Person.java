package com.topic.newcoffee;

import java.util.ArrayList;

/**
 * Created by Nick on 2017/11/27.
 */

public class Person {
    String account;
    String password;
    String name ;
    String birthday;
    boolean isMale;
    String phone;
    String email;
    String address;
    Data data;

    public Person() {
        this("aa","1234","王小明","1984-01-01",true,"0988123456",
                "abcd@yahoo.com.tw","台中市后里區文化路");
    }

    public Person(String account,String password,String name,String birthday,
                  boolean isMale,String phone,String email,String address) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.birthday = birthday;
        this.isMale = isMale;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
