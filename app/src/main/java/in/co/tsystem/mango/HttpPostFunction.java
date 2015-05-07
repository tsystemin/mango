package in.co.tsystem.mango;

/**
 * Created by diganta.paladhi on 05/04/15.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class HttpPostFunction {

    public String ck_name;
    public String ck_val;

    public  HttpResponse  processPost(String uri, JSONObject req, String cookie_name, String cookie_value)  throws UnsupportedEncodingException {
        HttpResponse response = null;

        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);


        try {
            Iterator x = req.keys();
            while (x.hasNext()) {
                String key = (String) x.next();
                nameValuePairs.add(new BasicNameValuePair(key, req.get(key).toString()));
            }
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(uri);

            if ((cookie_name.toString().length() != 0) && (cookie_value.toString().length() != 0)) {
                httppost.addHeader("Cookie ",cookie_name+"="+cookie_value+";");
            }

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            response = httpclient.execute(httppost);
            Header[] headers = response.getAllHeaders();
            HashMap result = convertHeadersToHashMap(headers);
            //List<String> cookies = headers.get()
            Log.d("Post Status","Code: " + result.toString());
            if (result.toString().length() != 0) {
                //SaveSharedPreference.setCookie(mContext, result.toString());
                String cookie_str = result.toString();
                String cookie_elements[];
                String cookie_val[];
                cookie_elements = cookie_str.split("=");
                Log.d("COOKIE name", "" + cookie_elements[1]);
                cookie_val = cookie_elements[2].split("\\}");
                Log.d("COOKIE val", "" + cookie_val[0]);
                ck_name = cookie_elements[1];
                ck_val = cookie_val[0];
            }

            return response;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return response;
    }

    private HashMap<String, String> convertHeadersToHashMap(Header[] headers) {
        HashMap<String, String> result = new HashMap<String, String>(headers.length);
        for (Header header : headers) {
            if (header.getName().equals("Set-Cookie")) {
                result.put(header.getName(), header.getValue().split(";", 2)[0]);
                break;
            }
        }
        return result;
    }

}