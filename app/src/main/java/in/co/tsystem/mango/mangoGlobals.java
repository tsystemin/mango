package in.co.tsystem.mango;

import android.app.Application;

import java.util.HashMap;

/**
 * Created by diganta.paladhi on 06/05/15.
 */
public class mangoGlobals extends Application {
    public static String server_ip = "192.168.1.6";
    public static String cname;
    public static String cval;
    public static Integer cookie_set = 0;
    public static Double total_cart_price = 0.0;
    private static mangoGlobals singleInstance = null;
    public static mangoGlobals getInstance() {
        return singleInstance;
    }
    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    public static final String THIRD_COLUMN="Third";
    public static final String FOURTH_COLUMN="Fourth";
    public static String user = "";
    public static String passwd = "";

    HashMap<Integer, cart_item> local_cart = new HashMap<Integer, cart_item>();

    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
    }
}
