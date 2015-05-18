package in.co.tsystem.mango;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
        //myViewCartAsyncTask tsk;
        addToCartFromLocal addtsk;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        mangoGlobals mg = mangoGlobals.getInstance();

        addtsk = new addToCartFromLocal(this);
        addtsk.execute();

        //tsk = new myViewCartAsyncTask(this);
        //tsk.execute();
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
                    temp.put(mg.SECOND_COLUMN, quantity);
                    temp.put(mg.THIRD_COLUMN, price);


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
                        //checkOutFromStore chkout_tsk = new checkOutFromStore(mContext);
                        //chkout_tsk.execute();

                        Intent intent = new Intent(ViewCartActivity.this, CheckoutActivity.class);
                        startActivity(intent);

                    }
                });

                Button b1 = (Button)findViewById(R.id.clear_cart);
                b1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {
                        //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                        cartClear c = new cartClear(mContext);
                        c.execute();

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

            return re.doGet(cartUrl);
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
            String ret;

            // we can optimize by clearing cart in server after checkout
            try {
                ret = result.getString("success");
                if (ret.contains("PASS")) {
                    cartClear cleartsk = new cartClear(mContext);
                    cleartsk.execute();
                } else {
                    Log.d("CHECKOUT ERROR", "code" + ret);
                    Toast.makeText(getApplicationContext(), ret,
                            Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

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

    private class cartClear extends AsyncTask< Void, Void, JSONObject > {

        JSONObject jb;
        private Context mContext;
        BufferedReader br;

        public cartClear(Context context) {
            mContext = context;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            //clear the cart on APP

            final ListView listview1 = (ListView) findViewById(R.id.listView1);
            //sankar final ArrayList<String> list_prod = new ArrayList<String>();
            ArrayList<HashMap<String, String>> list_prod = new ArrayList<HashMap<String, String>>();

            try {
                ListViewAdapter adapter = new ListViewAdapter(ViewCartActivity.this, list_prod);
                listview1.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }


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
            url_new = "http://"+ server_ip +"/opencart/?route=feed/rest_api/cart_clear&key=1234"; // add count

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

    private class addToCartFromLocal extends AsyncTask<Void, Void, JSONObject > {

        JSONObject jb;
        private Context mContext;
        BufferedReader br;
        myViewCartAsyncTask tsk;

        public addToCartFromLocal(Context context) {
            mContext = context;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            tsk = new myViewCartAsyncTask(mContext);
            tsk.execute();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... arg) {

            String url_new = null;

            mangoGlobals mg = mangoGlobals.getInstance();
            String server_ip = mg.server_ip;


            for (cart_item item : mg.local_cart.values()) {

                if (!item.added_to_cart || item.item_changed) {
                    //url_new = "http://" + server_ip + "/opencart/?route=feed/rest_api/cart_add&product_id=" + arg[0] + "&quantity=" + arg[1] + "&key=1234"; // add count
                    url_new = "http://" + server_ip + "/opencart/?route=feed/rest_api/cart_add&product_id=" + item.prod_id + "&quantity=" + item.quantity + "&key=1234";

                    Log.i("PRODDET prod_id is", item.prod_id + "");
                    ServerComm.RestService re = new ServerComm.RestService();

                    jb = re.doGet(url_new);
                    try {
                        //ver = jb.getString("db_ver");
                        //version = Integer.parseInt(ver);
                        Log.i("PRODDET", jb.toString() + "");
                        item.added_to_cart = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return jb;
        }
    }
}
