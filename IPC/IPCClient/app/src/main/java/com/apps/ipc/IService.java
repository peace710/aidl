/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: G:\\simple_environment\\simple_demo\\IPC\\app\\src\\main\\aidl\\com\\apps\\ipc\\IService.aidl
 */
package com.apps.ipc;
public interface IService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.apps.ipc.IService
{
private static final String DESCRIPTOR = "com.apps.ipc.IService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.apps.ipc.IService interface,
 * generating a proxy if needed.
 */
public static com.apps.ipc.IService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.apps.ipc.IService))) {
return ((com.apps.ipc.IService)iin);
}
return new com.apps.ipc.IService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getAppDetail:
{
data.enforceInterface(DESCRIPTOR);
com.apps.request.GetAppDetailRequest _arg0;
if ((0!=data.readInt())) {
_arg0 = com.apps.request.GetAppDetailRequest.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
com.apps.ipc.ICallback _arg1;
_arg1 = com.apps.ipc.ICallback.Stub.asInterface(data.readStrongBinder());
int _result = this.getAppDetail(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.apps.ipc.IService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     *//**
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    */
@Override public int getAppDetail(com.apps.request.GetAppDetailRequest params, com.apps.ipc.ICallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((params!=null)) {
_data.writeInt(1);
params.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_getAppDetail, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getAppDetail = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     *//**
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    */
public int getAppDetail(com.apps.request.GetAppDetailRequest params, com.apps.ipc.ICallback callback) throws android.os.RemoteException;
}
