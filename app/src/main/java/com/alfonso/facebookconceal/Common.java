package com.alfonso.facebookconceal;


import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;
import java.io.File;


/**
 * Created by Praxis on 08/03/2016.
 */
public class Common {

    private Context context;
    private ContextWrapper contextWrapper;
    public static boolean IS_DEBUG_ACTIVE = true;


    public Common(Context context) {
        this.context = context;
        contextWrapper = new ContextWrapper(context);
    }


    /**
     *
     * @param folderName
     * @return
     */
    public String getAppDirectory(String folderName) {
        String path = null;

        if (IS_DEBUG_ACTIVE) {
            path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
                    + "/"
                    + folderName + "/";
        } else {
            path = contextWrapper.getFilesDir().getAbsolutePath() + "/" + folderName + "/";
        }

        buildDirectory(path);

        return path;
    }


    /**
     * @param Message
     * @param logLevel DEBUG = 3
     *                 ERROR = 6;
     *                 INFO = 4;
     *                 VERBOSE = 2;
     */
    public void logMessages( String Message, int logLevel) {

        String appName = context.getClass().getSimpleName();
        int stringId = context.getApplicationInfo().labelRes;
        appName = context.getString(stringId);

        switch (logLevel) {
            case Log.DEBUG:
                Log.d(appName, Message);
                break;
            case Log.INFO:
                Log.i(appName, Message);
                break;
            case Log.ERROR:
                Log.e(appName, Message);
                break;
            case Log.VERBOSE:
                Log.v(appName, Message);
                break;
        }
    }

    public boolean buildDirectory(String path) {
        File folder = new File(path);
        boolean success = false;

        if (!folder.exists()) {
            success = folder.mkdir();
        }
        return success;
    }

}
