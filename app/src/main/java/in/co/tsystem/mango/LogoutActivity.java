package in.co.tsystem.mango;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.json.JSONObject;


public class LogoutActivity extends ActionBarActivity {

    mangoGlobals mg = mangoGlobals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        // Logout from the app
        logout();

        finish();
    }

    public void logout() {
        String logoutUri = "http://"+ mg.server_ip +"/opencart/index.php?route=feed/rest_api/customerLogout";
        String postData = "{'email' : dummy}";

        try {
            logOut tsk = new logOut();
            tsk.execute(logoutUri, postData);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        mg.cname = null;
        mg.cval = null;
    }

    private class logOut extends AsyncTask<String, Void, HttpResponse> {

        @Override
        protected void onPostExecute(HttpResponse result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), "You are being logged out!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HttpResponse doInBackground(String... arg0) {
            HttpResponse response = null;
            try {
                JSONObject obj = new JSONObject(arg0[1]);
                HttpPostFunction sChannel = new HttpPostFunction();
                response = sChannel.processPost(arg0[0], obj);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
    }
}
