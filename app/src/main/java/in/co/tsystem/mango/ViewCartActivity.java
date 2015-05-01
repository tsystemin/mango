package in.co.tsystem.mango;

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

import org.json.JSONObject;


public class ViewCartActivity extends ActionBarActivity {
    private JSONObject response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        //Show the cart
        showCart();

        Button mCheckOut = (Button) findViewById(R.id.check_out);
        mCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the check out function which invokes check out actvity
                //checkOut();
            }
        });
    }

    public void showCart () {
        String cartUrl = "http://10.0.0.139/opencart/?route=feed/rest_api/cart_products";
        try {
            myViewCartAsyncTask tsk = new myViewCartAsyncTask();
            tsk.execute(cartUrl);
            Log.d("RESP", response.toString());
            if (response.optString("success") == "TRUE") {
                Intent intent = new Intent(this, CategoryActivity.class);
                startActivity(intent);
                Log.d("CATEGORY", "Inflated category");
            }
        } catch (Exception e) {

        }
    }

    public void checkOut (View view) {
        //Intent intent = new Intent(this, CheckOutActivity.class);
        //startActivity(intent);
    }

    // Async task to send login request in a separate thread
    private class myViewCartAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... arg0) {

            ServerComm.RestService re = new ServerComm.RestService();
            response = re.doGet(arg0[0]);

            return null;
        }
    }

}
