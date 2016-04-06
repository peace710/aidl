// ICallback.aidl
package com.apps.ipc;

// Declare any non-default types here with import statements
import android.os.Bundle;

interface ICallback {
    void onResult(in Bundle result);

    void onError(String code,String desc);
}
