package com.apps.ipcclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.apps.data.App;
import com.apps.data.AppDetail;
import com.apps.data.Constant;
import com.apps.ipc.ICallback;
import com.apps.ipc.IService;
import com.apps.request.GetAppDetailRequest;
import com.apps.result.GetAppDetailResult;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout appIdWrapper;
    private TextInputLayout appVersionWrapper;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appIdWrapper = (TextInputLayout)findViewById(R.id.app_id_wrapper);
        appVersionWrapper = (TextInputLayout)findViewById(R.id.app_version_wrapper);
        result = (TextView)findViewById(R.id.result);

        appIdWrapper.setHint(getString(R.string.app_id));
        appVersionWrapper.setHint(getString(R.string.app_version));

        bindService();
    }

    public void post(View v){
        String id = appIdWrapper.getEditText().getText().toString().trim();
        String version = appVersionWrapper.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(id)){
            appIdWrapper.setError("id can not be empty");
            return;
        }

        if (TextUtils.isEmpty(version)){
            appIdWrapper.setError("version can not be empty");
            return;
        }

        getAppDetail(id,version);
    }

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

    private void bindService(){
        String action = "com.apps.ipc.IPCService";
        String packageName = "com.apps.ipc";
        Intent intent = new Intent(action);
        intent.setPackage(packageName);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }


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

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }
}
