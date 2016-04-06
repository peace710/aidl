package com.apps.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
