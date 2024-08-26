package ru.etysoft.clientbook;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import ru.etysoft.clientbook.notification.Notifier;
import ru.etysoft.clientbook.gloable_observe.GlobalAppointmentObserver;

public class CBApplication extends Application implements LifecycleObserver {

    private static boolean isBackgrounded = true;

    public static boolean isIsBackgrounded() {
        return isBackgrounded;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        GlobalAppointmentObserver.Companion.getInstance().release();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onAppBackgrounded() {
        isBackgrounded = true;
        Log.d("CBApplication", "App in background");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onAppForegrounded() {
        isBackgrounded = false;
        try {
            Notifier.cancelAllNotifications(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("CBApplication", "App in foreground");
    }
}
