// IPushAidlInterface.aidl
package com.birdex.bird.service;

// Declare any non-default types here with import statements

interface IPushAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    //获取声音设置
    boolean getVoiceStatus();
    boolean getTimeVoiceStatus();
    void setVoiceStatus(boolean flag);
    void setTimeVoiceStatus(boolean flag);
    //停止推送服务
    void stopPushService();
}
