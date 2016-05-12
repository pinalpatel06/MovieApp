package com.example.android.movieshowapp1.BaseClasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Created by veeral on 05/05/2016.
 */
public class TrailerData implements Parcelable {

    @SerializedName("id") private String mId;
    @SerializedName("key") private String mKey;
    @SerializedName("name") private String mName;
    @SerializedName("site") private String mSite;
    @SerializedName("size") private String mSize;

    public TrailerData(){}


    public TrailerData(String mId, String mKey, String mName, String mSite, String mSize) {
        this.mId = mId;
        this.mKey = mKey;
        this.mName = mName;
        this.mSite = mSite;
        this.mSize = mSize;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSite() {
        return mSite;
    }

    public void setmSite(String mSite) {
        this.mSite = mSite;
    }

    public String getmSize() {
        return mSize;
    }

    public void setmSize(String mSize) {
        this.mSize = mSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mKey);
        dest.writeString(this.mName);
        dest.writeString(this.mSite);
        dest.writeString(this.mSize);
    }

    protected TrailerData(Parcel in) {
        this.mId = in.readString();
        this.mKey = in.readString();
        this.mName = in.readString();
        this.mSite = in.readString();
        this.mSize = in.readString();
    }

    public static final Creator<TrailerData> CREATOR = new Creator<TrailerData>() {
        @Override
        public TrailerData createFromParcel(Parcel source) {
            return new TrailerData(source);
        }

        @Override
        public TrailerData[] newArray(int size) {
            return new TrailerData[size];
        }
    };
}
