package com.example.tzj12306.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 正军 on 2018/5/18.
 */

public class User extends DataSupport implements Parcelable{
    private int id;
    private String UserName;
    private String Password;
    private List<IdCard> idCards;
    private String PhoneNum;
    private String email;
    private boolean loginFlag;

    public User() {
    }


    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            User user = new User();
            user.UserName = in.readString();
            user.Password = in.readString();
            user.PhoneNum = in.readString();
            user.email = in.readString();
            user.loginFlag = ((in.readInt()==1)?true:false);
            user.idCards = in.readArrayList(IdCard.class.getClassLoader());
            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UserName);
        dest.writeString(Password);
        dest.writeString(PhoneNum);
        dest.writeString(email);
        dest.writeInt(loginFlag?1:0);
        dest.writeList(idCards);
    }
    public List<IdCard> getIdCards() {
        return idCards;
    }

    public void setIdCards(List<IdCard> idCards) {
        this.idCards = idCards;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", UserName='" + UserName + '\'' +
                ", Password='" + Password + '\'' +
                ", idCards=" + idCards +
                ", PhoneNum='" + PhoneNum + '\'' +
                ", email='" + email + '\'' +
                ", loginFlag=" + loginFlag +
                '}';
    }

    public void setId(int id) {
        this.id = id;
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
