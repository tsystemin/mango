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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ViewCartActivity extends Activity {
    private JSONObject response;
    mangoGlobals mg = mangoGlobals.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myViewCartAsyncTask tsk;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        mangoGlobals mg = mangoGlobals.getInstance();
        tsk = new myViewCartAsyncTask(this);
        tsk.execute();
    }

    public void checkOut (View view) {
        //Intent intent = new Intent(this, CheckOutActivity.class);
        //startActivity(intent);
    }
    private class myViewCartAsyncTask extends AsyncTask<Void, Void, JSONObject> {
        JSONObject jb;
        private Context mContext;
        BufferedReader br;
        HashMap<String, Integer> ProdIdMap = new HashMap<String, Integer>();

        private class StableArrayAdapter extends ArrayAdapter<String> {

            HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

            public StableArrayAdapter(Context context, int textViewResourceId,
                                      List<String> objects) {
                super(context, textViewResourceId, objects);
                for (int i = 0; i < objects.size(); ++i) {
                    mIdMap.put(objects.get(i), i);
                }
            }

            @Override
            public long getItemId(int position) {
                String item = getItem(position);
                return mIdMap.get(item);
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

        }

        public myViewCartAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            JSONArray cart_items;
            JSONObject item;
            String prod_name;
            String quantity;
            String price;

            Integer prod_id;
            final ListView listview1 = (ListView) findViewById(R.id.listView1);
            //sankar final ArrayList<String> list_prod = new ArrayList<String>();
            ArrayList<HashMap<String, String>> list_prod = new ArrayList<HashMap<String, String>>();

            try {
                cart_items = result.getJSONArray("product");
                for (int i = 0; i < cart_items.length(); i++) {

                    item = cart_items.getJSONObject(i);
                    prod_name  = item.getString("name");
                    prod_id = item.getInt("product_id");
                    quantity = item.getString("quantity");
                    price = item.getString("price");
                    /*
                    list_prod.add(prod_name);
                    ProdIdMap.put(prod_name, prod_id);
                    */

                    HashMap<String,String> temp=new HashMap<String, String>();
                    temp.put(mg.FIRST_COLUMN, prod_name);
                    temp.put(mg.SECOND_COLUMN, prod_id.toString());
                    temp.put(mg.THIRD_COLUMN, quantity);
                    temp.put(mg.FOURTH_COLUMN, price);


                    list_prod.add(temp);

                }

                //final StableArrayAdapter adapter_prod = new StableArrayAdapter(mContext, android.R.layout.simple_list_item_1, list_prod);
                //listview1.setAdapter(adapter_prod);

                ListViewAdapter adapter=new ListViewAdapter(ViewCartActivity.this, list_prod);
                listview1.setAdapter(adapter);

                Button b = (Button)findViewById(R.id.check_out);
                b.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {
                        //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                        checkOutFromStore chkout_tsk = new checkOutFromStore(mContext);
                        chkout_tsk.execute();

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... arg0) {
            String cartUrl = "http://"+ mg.server_ip +"/opencart/?route=feed/rest_api/cart_products&key=1234";
            ServerComm.RestService re = new ServerComm.RestService();
            String name = mg.cname;
            String val = mg.cval;

            return re.doGet(cartUrl, name, val);
        }
    }


    private class checkOutFromStore extends AsyncTask< Void, Void, JSONObject > {

        JSONObject jb;
        private Context mContext;
        BufferedReader br;

        public checkOutFromStore(Context context) {
            mContext = context;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... arg) {

            String url_new = null, ver = null;
            int version = 0;

            mangoGlobals mg = mangoGlobals.getInstance();
            String server_ip = mg.server_ip;
            url_new = "http://"+ server_ip +"/opencart/?route=feed/rest_api/checkout&payment_method=cod&shipping_method=flat.flat&key=1234"; // add count

            //Log.i("PRODDET prod_id is", arg[0] + "");
            ServerComm.RestService re = new ServerComm.RestService();

            // get cookie info
            //SharedPreferences settings = mContext.getSharedPreferences("PREFS_NAME", 0);
            //String name = settings.getString("cookie_name", "");
            //String val = settings.getString("cookie_val", "");

            //SaveSharedPreference p = SaveSharedPreference.getSharedPreferences(mContext);
            //String name = SaveSharedPreference.getCookieName(mContext);
            //String val = SaveSharedPreference.getCookieVal(mContext);


            String name = mg.cname;
            String val = mg.cval;

            Log.d(" CART ADD cookie name" , name + "");
            Log.d(" CART ADD cookie val", val + "" );

            jb = re.doGet(url_new, name, val);
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
