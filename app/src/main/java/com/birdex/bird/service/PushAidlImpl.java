package com.birdex.bird.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.util.Log;

import com.birdex.bird.util.Constant;

/**
 * Created by huwei on 16/4/19.
 */
public class PushAidlImpl extends IPushAidlInterface.Stub{
    private SharedPreferences sp;

    private SharedPreferences.Editor editor;
    public PushAidlImpl(Context context){
        sp = context.getSharedPreferences(Constant.SP_NAME, Activity.MODE_PRIVATE);
        editor = sp.edit();
    }
    @Override
    public boolean getVoiceStatus() throws RemoteException {
        return sp.getBoolean(Constant.TONE_SETTING,true);
    }

    @Override
    public boolean getTimeVoiceStatus() throws RemoteException {
        return sp.getBoolean(Constant.TIME_SETTING,true);
    }

    @Override
    public void setVoiceStatus(boolean flag) throws RemoteException {
        editor.putBoolean(Constant.TONE_SETTING,flag);
        editor.commit();
    }

    @Override
    public void setTimeVoiceStatus(boolean flag) throws RemoteException {
        editor.putBoolean(Constant.TIME_SETTING,flag);
        editor.commit();
    }
}
