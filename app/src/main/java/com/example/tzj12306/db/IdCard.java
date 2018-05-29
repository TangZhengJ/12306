package com.example.tzj12306.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by 正军 on 2018/5/29.
 */

public class IdCard extends DataSupport implements Parcelable {
    private int id;
    private String Name;
    private String CardId;

    public IdCard() {
    }

    public static final Creator<IdCard> CREATOR = new Creator<IdCard>() {
        @Override
        public IdCard createFromParcel(Parcel in) {
            IdCard idCard = new IdCard();
            idCard.Name = in.readString();
            idCard.CardId = in.readString();
            return idCard;
        }

        @Override
        public IdCard[] newArray(int size) {
            return new IdCard[size];
        }
    };

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

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String cardId) {
        CardId = cardId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(CardId);
    }
}
