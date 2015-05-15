package in.co.tsystem.mango;

import android.app.Application;

/**
 * Created by diganta.paladhi on 06/05/15.
 */
public class mangoGlobals extends Application {
    public static String server_ip = "192.168.43.214";
    public static String cname;
    public static String cval;
    private static mangoGlobals singleInstance = null;
    public static mangoGlobals getInstance() {
        return singleInstance;
    }
    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    public static final String THIRD_COLUMN="Third";
    public static final String FOURTH_COLUMN="Fourth";

    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
    }
}
