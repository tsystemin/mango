package in.co.tsystem.mango;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import org.apache.http.HttpResponse;
import org.json.JSONObject;


public class ViewCartActivity extends Activity {
    private JSONObject response;
    String server_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        mangoGlobals mg = mangoGlobals.getInstance();
        server_ip = mg.server_ip;

        myViewCartAsyncTask tsk = new myViewCartAsyncTask();
        tsk.execute();
    }

    public void checkOut (View view) {
        //Intent intent = new Intent(this, CheckOutActivity.class);
        //startActivity(intent);
    }

    // Async task to send login request in a separate thread
    private class myViewCartAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            Log.d("RESP", result.toString());
            /*
             Button mCheckOut = (Button) findViewById(R.id.check_out);
        mCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the check out function which invokes check out actvity
                //checkOut();
            }
        });
            if (response.optString("success") == "TRUE") {
                //Intent intent = new Intent(this, CategoryActivity.class);
                //startActivity(intent);
                Log.d("CATEGORY", "Inflated cart");
            }*/
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... arg0) {
            //String ip = getString(R.string.server_ip);
            String cartUrl = "http://"+ server_ip +"/opencart/?route=feed/rest_api/cart_products&key=1234";
            ServerComm.RestService re = new ServerComm.RestService();
            response = re.doGet(cartUrl);

            return response;
        }
    }

}
