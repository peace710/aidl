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
