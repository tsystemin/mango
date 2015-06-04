package in.co.tsystem.mango;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;


public class OrderDetails extends Activity {

    mangoGlobals mg = mangoGlobals.getInstance();
    TextView o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        o = (TextView)findViewById(R.id.order_id_view);

        Intent mIntent = getIntent();
        Integer order = mIntent.getIntExtra("order_id", 0);
        getOrderId go = new getOrderId(this);
        go.execute(order);
    }


    private class getOrderId extends AsyncTask< Integer, Void, JSONObject > {

        JSONObject jb;
        private Context mContext;
        BufferedReader br;

        public getOrderId(Context context) {
            mContext = context;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            //clear the cart on APP

            try {
                JSONObject or = result.getJSONObject("order");
                try {
                    String or_id = or.getString("order_id");
                    o.setText("Order " + or_id + " successfully placed");
                } catch (Exception e) {

                }
            } catch (Exception e) {

            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Integer... arg) {

            String url_new = null, ver = null;
            int version = 0;
            Integer oid = arg[0];

            String server_ip = mg.server_ip;
            url_new = "http://"+ server_ip +"/opencart/?route=feed/rest_api/order&order_id=" + oid; // add count

            //Log.i("PRODDET prod_id is", arg[0] + "");
            ServerComm.RestService re = new ServerComm.RestService();
            jb = re.doGet(url_new);
            try {
                //ver = jb.getString("db_ver");
                //version = Integer.parseInt(ver);
                Log.i("PRODDET", jb.toString() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jb;
        }
    }
}
