package com.apps.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AppDetail implements Parcelable {
    private App app;
    private String name;
    private String desc;
    private long downloadTimes;

    public static final Creator<AppDetail> CREATOR = new Creator<AppDetail>() {

        @Override
        public AppDetail createFromParcel(Parcel source) {
            return new AppDetail(source);
        }

        @Override
        public AppDetail[] newArray(int size) {
            return new AppDetail[size];
        }

    };
    public AppDetail(Parcel source) {
        app = source.readParcelable(App.class.getClassLoader());
        name = source.readString();
        desc = source.readString();
        downloadTimes = source.readLong();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(app, flags);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeLong(downloadTimes);
    }

    public AppDetail(){

    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getDownloadTimes() {
        return downloadTimes;
    }

    public void setDownloadTimes(long downloadTimes) {
        this.downloadTimes = downloadTimes;
    }
}
