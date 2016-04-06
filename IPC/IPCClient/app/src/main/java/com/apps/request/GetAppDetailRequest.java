package com.apps.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.apps.data.App;


public class GetAppDetailRequest implements Parcelable{
    private App app;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.app, flags);
    }

    public GetAppDetailRequest() {
    }

    protected GetAppDetailRequest(Parcel in) {
        this.app = in.readParcelable(App.class.getClassLoader());
    }

    public static final Creator<GetAppDetailRequest> CREATOR = new Creator<GetAppDetailRequest>() {
        @Override
        public GetAppDetailRequest createFromParcel(Parcel source) {
            return new GetAppDetailRequest(source);
        }

        @Override
        public GetAppDetailRequest[] newArray(int size) {
            return new GetAppDetailRequest[size];
        }
    };

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }
}
