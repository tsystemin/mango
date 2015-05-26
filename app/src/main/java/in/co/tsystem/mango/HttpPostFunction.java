package in.co.tsystem.mango;

/**
 * Created by diganta.paladhi on 05/04/15.
 */

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

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
            if (req.keys() != null) {
                Iterator x = req.keys();
                while (x.hasNext()) {
                    String key = (String) x.next();
                    nameValuePairs.add(new BasicNameValuePair(key, req.get(key).toString()));
                }
            }
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(uri);

            if ((mangoGlobals.cname != null) && (mangoGlobals.cval != null)) {
                httppost.addHeader("Cookie", mangoGlobals.cname + "=" + mangoGlobals.cval + ";");
            }

            if (req.keys() != null) {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }

            // Execute HTTP Post Request
            response = httpclient.execute(httppost);

            Header[] headers = response.getAllHeaders();
            HashMap result = convertHeadersToHashMap(headers);

            if (result.toString().length() != 0) {
                String cookie_str = result.toString();
                String cookie_elements[];
                String cookie_val[];
                cookie_elements = cookie_str.split("=");
                cookie_val = cookie_elements[2].split("\\}");
                if (mangoGlobals.cookie_set == 0) {
                    mangoGlobals.cname = cookie_elements[1];
                    mangoGlobals.cval = cookie_val[0];
                    mangoGlobals.cookie_set = 1;
                }
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