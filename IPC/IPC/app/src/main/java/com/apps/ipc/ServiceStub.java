package com.apps.ipc;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;

import com.apps.data.App;
import com.apps.data.AppDetail;
import com.apps.data.Constant;
import com.apps.request.GetAppDetailRequest;
import com.apps.result.GetAppDetailResult;

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
