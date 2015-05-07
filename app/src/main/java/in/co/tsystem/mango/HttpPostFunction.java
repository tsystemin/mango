package in.co.tsystem.mango;

/**
 * Created by diganta.paladhi on 05/04/15.
 */
import android.util.Log;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class HttpPostFunction {

    public  HttpResponse  processPost(String uri, JSONObject req)  throws UnsupportedEncodingException {
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

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            response = httpclient.execute(httppost);
            Header[] headers = response.getAllHeaders();
            HashMap result = convertHeadersToHashMap(headers);
            //List<String> cookies = headers.get()
            Log.d("Post Status","Code: " + result.toString());
            if (result.toString().length() != 0) {
                //SaveSharedPreference.setCookie(mContext, result.toString());
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