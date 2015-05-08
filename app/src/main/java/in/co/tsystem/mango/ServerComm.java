package in.co.tsystem.mango;

/**
 * Created by diganta.paladhi on 20/04/15.
 */

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerComm {
    public static JSONObject json = null;

    public static class RestService {

        public JSONObject doGet(String url) {

            HttpClient httpclient = new DefaultHttpClient();
            // Prepare a request object
            HttpGet httpget = new HttpGet(url);

            // Accept JSON
            httpget.addHeader("accept", "application/json");

            // Use the cookie if available
            if ((mangoGlobals.cname != null) && (mangoGlobals.cval != null)) {
                httpget.addHeader("Cookie", mangoGlobals.cname + "=" + mangoGlobals.cval + ";");
            }
            // Execute the request
            HttpResponse response;

            try {
                response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();

                // If response entity is not null
                if (entity != null) {
                    // get entity contents and convert it to string
                    InputStream instream = entity.getContent();
                    String result= convertStreamToString(instream);

                    json=new JSONObject(result);
                    // Closing the input stream will trigger connection release
                    instream.close();
                }
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // Return the json
            return json;
        }

        private String convertStreamToString(InputStream is) {

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();
        }
    }
}
