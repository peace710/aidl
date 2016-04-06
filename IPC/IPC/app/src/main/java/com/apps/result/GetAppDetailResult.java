package com.apps.result;

import android.os.Parcel;
import android.os.Parcelable;

import com.apps.data.AppDetail;

public class GetAppDetailResult implements Parcelable{
    private AppDetail appDetail;

    public AppDetail getAppDetail() {
        return appDetail;
    }

    public void setAppDetail(AppDetail appDetail) {
        this.appDetail = appDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.appDetail, flags);
    }

    public GetAppDetailResult() {
    }

    protected GetAppDetailResult(Parcel in) {
        this.appDetail = in.readParcelable(AppDetail.class.getClassLoader());
    }

    public static final Creator<GetAppDetailResult> CREATOR = new Creator<GetAppDetailResult>() {
        @Override
        public GetAppDetailResult createFromParcel(Parcel source) {
            return new GetAppDetailResult(source);
        }

        @Override
        public GetAppDetailResult[] newArray(int size) {
            return new GetAppDetailResult[size];
        }
    };
}
