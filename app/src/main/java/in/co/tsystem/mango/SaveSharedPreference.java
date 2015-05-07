package in.co.tsystem.mango;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import org.apache.http.cookie.Cookie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diganta.paladhi on 02/05/15.
 */
public class SaveSharedPreference {
    static final String PREF_USER_NAME = "u";
    static final String PREF_PASS = "p";
    static final String PREF_COOKIE = "c";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static void setPassword(Context ctx, String password)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_PASS, password);
        editor.commit();
    }

    public static void setCookie(Context ctx, String cookie)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_COOKIE, cookie);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getPass(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_PASS, "");
    }

    public static String getCookie(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_COOKIE, "");
    }

    public static void clearCred(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }

    /*public static void saveSharedPreferencesCookies(Context context, List<Cookie> cookies) {
        SerializableCookie[] serializableCookies = new SerializableCookie[cookies.size()];
        for (int i=0;i<cookies.size();i++){
            SerializableCookie serializableCookie = new SerializableCookie(cookies.get(i));
            serializableCookies[i] = serializableCookie;
        }
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        ObjectOutputStream objectOutput;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            objectOutput = new ObjectOutputStream(arrayOutputStream);


            objectOutput.writeObject(serializableCookies);
            byte[] data = arrayOutputStream.toByteArray();
            objectOutput.close();
            arrayOutputStream.close();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Base64OutputStream b64 = new Base64OutputStream(out, Base64.DEFAULT);
            b64.write(data);
            b64.close();
            out.close();

            editor.putString("cookies", new String(out.toByteArray()));
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Cookie> loadSharedPreferencesCookie(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        byte[] bytes = preferences.getString("cookies", "{}").getBytes();
        if (bytes.length == 0 || bytes.length==2)
            return null;
        ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
        Base64InputStream base64InputStream = new Base64InputStream(byteArray, Base64.DEFAULT);
        ObjectInputStream in;
        List<Cookie> cookies = new ArrayList<Cookie>();
        SerializableCookie[] serializableCookies;
        try {
            in = new ObjectInputStream(base64InputStream);
            serializableCookies = (SerializableCookie[]) in.readObject();
            for (int i=0;i<serializableCookies.length; i++){
                Cookie cookie = serializableCookies[i].getCookie();
                cookies.add(cookie);
            }
            return cookies;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }*/

}
