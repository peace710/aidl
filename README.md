# aidl
####Android AIDL
AIDL的中文名是Android接口定义语言，用来实现进程之间的数据的共享传递。
 
####AIDL支持的数据类型
基本数据类型（int、long、char、boolean、double等）；
String和CharSequence；
List：只支持ArrayList，里面的每个元素都必须能够被AIDL支持；
Map：只支持HashMap，里面的每个元素都必须能够被AIDL支持；
Parcelable：所有实现了Parcelable接口的对象；
AIDL：所有的AIDL接口本身也可以在AIDL文件中使用。
除此之外，自定义的Parcelable对象和AIDL对象必须要显式import进来

####AIDL接口的创建
需要创建一个包含.aidl后缀的文件，在其中声明一些接口

```
// IService.aidl
package com.apps.ipc;

// Declare any non-default types here with import statements
import com.apps.request.GetAppDetailRequest;
import com.apps.ipc.ICallback;

interface IService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    /**
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    */

    int getAppDetail(in GetAppDetailRequest params,ICallback callback);

}
```

```
// ICallback.aidl
package com.apps.ipc;

// Declare any non-default types here with import statements
import android.os.Bundle;

interface ICallback {
    void onResult(in Bundle result);

    void onError(String code,String desc);
}
```

如果AIDL文件中引用到了自定义的Parcelable对象，必须要建一个与其同名的AIDL文件，并在其中声明为Parcelable类型
```
// IGetAppDetailRequest.aidl
package com.apps.request;

// Declare any non-default types here with import statements

parcelable GetAppDetailRequest;
```

####注：
AIDL中除了基本数据类型，其他类型的参数必须标上方向：in、out或inout
in表示输入型参数
out表示输出型参数
inout表示输入输出参数

####远程服务端Service的实现
首先需要对服务端的AIDL接口进行实现，然后在Service的onBind方法将其返回
```
public class IPCService extends Service {

    private ServiceStub serviceStub;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceStub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceStub = new ServiceStub(this);
    }
}
```
```
public class ServiceStub extends IService.Stub {
    private Context context;

    public ServiceStub(Context context){
        this.context = context;
    }

    public ServiceStub() {
        super();
    }

    @Override
    public int getAppDetail(GetAppDetailRequest params, ICallback callback) throws RemoteException {
        App app = params.getApp();
        if (null == app || null == callback){
            return  Constant.AIDL_PARAMS_ERROR;
        }

        String id = app.getId();
        String version = app.getVersion();
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(version)){
            return Constant.AIDL_PARAMS_ERROR;
        }
        getAppDetailFromNetWork(id,version,callback);
        return Constant.AIDL_SUCCESS;
    }

    //模拟从网络获取数据
    private void getAppDetailFromNetWork(String id,String version,ICallback callback) throws RemoteException{
        //假设id为48，version为01.00.01能获得正确数据
        String app_id = "48";
        String app_version = "01.00.01";
        if (id.equals(app_id) && version.equals(app_version)){
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constant.CALLBACK_KEY,newAppDetailResult(id,version));
            callback.onResult(bundle);
        }else{
            callback.onError(Constant.CALLBACK_ERROR_CODE,Constant.CALLBACK_ERROR_DESC);
        }
    }

    private GetAppDetailResult newAppDetailResult(String id, String version){
        AppDetail appDetail = new AppDetail();
        App app = new App(id,version);
        appDetail.setApp(app);
        appDetail.setDesc("a apk about aidl");
        appDetail.setDownloadTimes(48);
        appDetail.setName("ipc");
        GetAppDetailResult getAppDetailResult = new GetAppDetailResult();
        getAppDetailResult.setAppDetail(appDetail);
        return  getAppDetailResult;
    }

}
```
####注：
由于AIDL方法是在服务端的Binder线程池执行，因此当多个客户端同时访问时，会存在多线程操作，所以需要在AIDL方法处理线程同步。

在AndroidManifest.xml文件中需要对Service进行配置
```
<service android:name=".IPCService"
         android:enabled="true">
         <intent-filter>
              <action android:name="com.apps.ipc.IPCService"></action>
         </intent-filter>
</service>
```

####客户端的实现
服务端在编译后，会在app/build/generated/source/debug下生成对应AIDL接口的java文件，需要将其以及使用到的相关的Java类一并拷贝到客户端工程，必须确保服务端与客户端这些类的包名，否则服务端客户端无法正确反序列化对方传送的数据，造成程序无法正常运行。
![img one](https://github.com/peace710/aidl/blob/master/IPC/img_1.png)
![img two](https://github.com/peace710/aidl/blob/master/IPC/img_2.png)


####绑定远程服务端
通过Context的bindService方法对远程服务端进行绑定，intent设置包名是为了解决Android5.0之后需要显示调用服务的问题。
```
private void bindService(){
    String action = "com.apps.ipc.IPCService";
    String packageName = "com.apps.ipc";
    Intent intent = new Intent(action);
    intent.setPackage(packageName);
    bindService(intent,connection, Context.BIND_AUTO_CREATE);
}
```

ServiceConnection用于处理客户端与服务端的连接、断开处理。当绑定服务成功后会回调执行onServiceConnected方法，表示已经连接上服务端，然后将服务端返回的Binder对象转换成AIDL接口，之后就可以通过这个接口去调用服务端的方法。当服务端因异常或者主动断开后，会执行onServiceDisConnected方法，表示已经断开连接。
```
private IService service;

private ServiceConnection connection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MainActivity.this.service = IService.Stub.asInterface(service);
        result.setText("Service is connected");
    }

    @Override
	public void onServiceDisconnected(ComponentName name) {
        MainActivity.this.service = null;
        result.setText("Service is disconnected");
    }
 };

private ICallback.Stub callback = new ICallback.Stub(){
    @Override
    public IBinder asBinder() {
        return super.asBinder();
    }

    @Override
    public void onResult(Bundle bundle) throws RemoteException {
        bundle.setClassLoader(GetAppDetailResult.class.getClassLoader());
        GetAppDetailResult getAppDetailResult = bundle.getParcelable(Constant.CALLBACK_KEY);
        AppDetail appDetail = getAppDetailResult.getAppDetail();
        App app = appDetail.getApp();
        //定义了一个TextView用于显示结果
        result.setText("id:" + app.getId() + ",version:" + app.getVersion() + ",name:" + appDetail.getName()
                    + ",desc:" + appDetail.getDesc() + ",downlaodTimes:" + appDetail.getDownloadTimes());

    }

    @Override
    public void onError(String code, String desc) throws RemoteException {
        result.setText("error:" + code + ",message:" + desc);
    }
};

private void getAppDetail(String id,String version){
    if (null != service){
        GetAppDetailRequest getAppDetailRequest = new GetAppDetailRequest();
        getAppDetailRequest.setApp(new App(id,version));
        try {
            int ret = service.getAppDetail(getAppDetailRequest,callback);
            if (ret == Constant.AIDL_PARAMS_ERROR){
                result.setText("request params error");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
```
####运行结果
![screen one](https://github.com/peace710/aidl/blob/master/IPC/image1.png =360x640)
![screen two](https://github.com/peace710/aidl/blob/master/IPC/image2.png =360x640)
