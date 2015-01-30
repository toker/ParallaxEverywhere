package com.fmsirvent.ParallaxEverywhereSample.util;

import android.util.Log;

/**
 * Logging helper class
 */
public final class Logger {

    private static final String TAG = "SMOOTHFEED/%s";

    private static final boolean LOGGING_ENABLED = true;

    public static String getTag(Class clazz) {
        return String.format(TAG, clazz.getSimpleName());
    }

    private Logger() {
    }

    /**
     * Informational message
     *
     * @param tag     Tag
     * @param message Message
     */
    public static void i(String tag, String message) {
        if (LOGGING_ENABLED) {
            Log.i(tag, message);
        }
    }

    /**
     * Error message
     *
     * @param tag     Tag
     * @param message Message
     */
    public static void e(String tag, String message) {
        if (LOGGING_ENABLED) {
            Log.e(tag, message);
        }
    }


}
