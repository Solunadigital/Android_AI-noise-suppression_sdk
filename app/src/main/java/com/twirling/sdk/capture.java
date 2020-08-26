package com.twirling.sdk;

import android.util.Log;


public class capture{
    public static String TAG = "CAPTURE";

    static {
        try{
            System.loadLibrary("TwirlingCapture");
        }catch(Exception e)
        {
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
        }
        Log.d(TAG,"LoadLibrary ok");
    }

    public void CaptureAuthInit(String appid, String app_passwd)
    {
        Log.d(TAG,"capture auth init");

        captureAuthInit(appid,app_passwd);
    }

    public void CaptureAuthRelease()
    {
        Log.d(TAG,"capture auth release");
        captureAuthRelease();
    }

    public long DnnoiseInit()
    {
        Log.d(TAG,"capture init");
        return dnnoiseInit();
    }

    public void DnnoiseProcess(long obj,float[] data)
    {
        Log.d(TAG,"capture process");
        dnnoiseProcess(obj,data);
    }

    public void DnnoiseSet(long obj)
    {
        Log.d(TAG,"capture set");
        dnnoiseSet(obj);
    }

    public void DnnoiseRelease(long obj)
    {
        Log.d(TAG,"capture release");
        dnnoiseRelease(obj);
    }

    private static native void captureAuthInit(String appid,String app_passwd);

    private static native void captureAuthRelease();

    private static native long dnnoiseInit();

    private static native void dnnoiseProcess(long obj, float[] data);

    private static native void dnnoiseSet(long obj);

    private static native void dnnoiseRelease(long obj);
}