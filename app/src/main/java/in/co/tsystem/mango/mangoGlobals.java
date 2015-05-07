package in.co.tsystem.mango;

import android.app.Application;

/**
 * Created by diganta.paladhi on 06/05/15.
 */
public class mangoGlobals extends Application {
    public static String server_ip = "192.168.43.214";
    private static mangoGlobals singleInstance = null;
    public static mangoGlobals getInstance() {
        return singleInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
    }
}
