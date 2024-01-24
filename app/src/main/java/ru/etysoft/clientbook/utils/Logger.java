package ru.etysoft.clientbook.utils;

import android.util.Log;

public class Logger {

    public static void logDebug(String tag, String log) {
        //if (BuildConfig.DEBUG) {
            Log.d(tag, log);
        //}
    }
}
