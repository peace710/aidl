package com.apps.data;

import android.os.Parcel;
import android.os.Parcelable;

public class App implements Parcelable {

    private String id;
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static final Creator<App> CREATOR = new Creator<App>() {

        @Override
        //从序列化后的对象中创建原始对象
        public App createFromParcel(Parcel source) {
            return new App(source);
        }

        @Override
        //创建指定长度的原始对象数组
        public App[] newArray(int size) {
            return new App[size];
        }

    };

    public App(String id, String version) {
        this.id = id;
        this.version = version;
    }
    //从序列化后的对象中创建原始对象
    public App(Parcel source) {
        id = source.readString();
        version = source.readString();
    }

    @Override
    //当前对象内容描述符，有文件描述符返回1
    //大多数情况都是返回0
    public int describeContents() {
        return 0;
    }

    @Override
    //将当前对象写入序列化结构中，flag有0与1两种值
    //当flag为1时，标识当前对象需要作为返回值返回，不能立即释放资源
    //大多数情况都为0
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(version);
    }
}
