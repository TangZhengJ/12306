package com.example.tzj12306.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 正军 on 2018/5/18.
 */

public class User extends DataSupport{
    private int id;
    private String UserName;
    private String Password;
    private String Name;
    private String CardId;
    private String PhoneNum;
    private String email;
    private boolean loginFlag;

    public User() {
    }

    public User(String userName, String password, String name, String id, String phoneNum, String email) {
        UserName = userName;
        Password = password;
        Name = name;
        CardId = id;
        PhoneNum = phoneNum;
        this.email = email;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String id) {
        CardId = id;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(boolean loginFlag) {
        this.loginFlag = loginFlag;
    }
}
