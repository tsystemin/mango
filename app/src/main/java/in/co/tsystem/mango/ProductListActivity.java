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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ProductListActivity extends Activity {

    getProductList tsk;
    int cat_id = 0;
    String server_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        mangoGlobals mg = mangoGlobals.getInstance();
        server_ip = mg.server_ip;

        Intent myIntent = getIntent();
        cat_id = myIntent.getIntExtra("category_id", 0);

        tsk = new getProductList(this);
        tsk.execute();
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private class getProductList extends AsyncTask< Void, Void, JSONObject > {

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

        public getProductList(Context context) {
            mContext = context;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            JSONArray categories;
            JSONObject item;
            String cat_name;
            Integer cat_id;

            TextView tv;

            //tv = (TextView)findViewById(R.id.textView1);
            final ListView listview = (ListView) findViewById(R.id.prodlistView);
            //arr = jb.getJSONArray('categories');
            final ArrayList<String> list = new ArrayList<String>();



            try {
                categories = jb.getJSONArray("products");
                for (int i = 0; i < categories.length(); i++) {
                    item = categories.getJSONObject(i);
                    cat_name  = item.getString("name");
                    cat_id = item.getInt("id");
                    list.add(cat_name);
                    ProdIdMap.put(cat_name, cat_id);

                    //Log.d("Type", shop.getString(i););
                    //tv.setText(url_new);
                    //tv.append(cat_name + "\n");
                    Log.d("JSONPARSE cat %s", cat_name + "");
                }
                final StableArrayAdapter adapter = new StableArrayAdapter(mContext, android.R.layout.simple_list_item_1, list);
                listview.setAdapter(adapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {
                        final String item = (String) parent.getItemAtPosition(position);

                        Integer idprod = ProdIdMap.get(item);
                        Intent intent = new Intent(mContext, ProductDetailActivity.class);
                        intent.putExtra("prod_id", idprod);
                        startActivity(intent);
                        /*
                        view.animate().setDuration(2000).alpha(0)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        Integer idprod;
                                        int duration = Toast.LENGTH_SHORT;
                                        String txt;

                                        //list.remove(item);
                                        // AVI - looks like item is position on table
                                        idprod = ProdIdMap.get(item);
                                        //txt = "item " + item + "id " + idprod;

                                        Intent intent = new Intent(mContext, ProductDetailActivity.class);
                                        intent.putExtra("prod_id", idprod);
                                        startActivity(intent);

                                        //Toast.makeText(mContext, txt, duration).show();
                                        //adapter.notifyDataSetChanged();
                                        //view.setAlpha(1);
                                    }
                                });*/

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

            String url_new = null, ret = null;
            int version = 0;

            //url_new = "http://10.0.0.103/opencart/?route=feed/rest_api/products&category=59&key=1234";
            //String ip = getString(R.string.server_ip);
            url_new = "http://"+ server_ip +"/opencart/?route=feed/rest_api/products&category="+cat_id+"&key=1234";
            ServerComm.RestService re = new ServerComm.RestService();
            jb = re.doGet(url_new);
            try {
                ret = jb.getString("success");

                Log.i("JSONPARSE return is %s", ret + "");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return jb;
        }
    }
}
