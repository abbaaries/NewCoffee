package com.topic.newcoffee;

/**
 * Created by Nick on 2017/11/27.
 */

public class Data {
    public Data() {
        this("台中市后里區","0912345678");
    }

    public Data(String address, String phone) {

        this.address = address;
        this.phone = phone;
    }

    String address;
    String phone;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
