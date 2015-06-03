package in.co.tsystem.mango;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import static in.co.tsystem.mango.R.drawable.ic_drawer;


public class ProductListActivity extends ActionBarActivity implements View.OnClickListener{

    getProductList tsk;
    int cat_id = 0;
    String server_ip;
    TextView tv;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private ListView mDrawerListView;
    String[] drawer_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        mangoGlobals mg = mangoGlobals.getInstance();
        server_ip = mg.server_ip;

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.actionbar_custom_view, null);
        actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        //updateHotCount();

        Intent myIntent = getIntent();
        cat_id = myIntent.getIntExtra("category_id", 0);

        tsk = new getProductList(this);
        tsk.execute();

        // navigation drawer new
        Resources resource = getResources();
        drawer_array = resource.getStringArray(R.array.nav_drawer_array);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout3);

        mDrawerListView = (ListView) findViewById(R.id.NavList3);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onNavigationDrawerItemSelected(position);
            }
        });
        mDrawerListView.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                new String[]{
                        drawer_array[0],
                        drawer_array[1],
                        drawer_array[2],
                        drawer_array[3],
                        drawer_array[4],
                        drawer_array[5],
                        drawer_array[6],
                        drawer_array[7],
                        drawer_array[8]
                }));

        //android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        {
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerSlide(View drawerView, float offset)
            {
                View container = findViewById(R.id.Relative3);
                container.setTranslationX(offset * drawerView.getWidth());
            }

        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBarDrawerToggle.syncState();
    }


    public void onNavigationDrawerItemSelected(int position) {
        try {
            switch (position) {
                case 0:
                    break;
                case 1:
                    //intent = new Intent(MainActivity.this, Vedanta.class);
                    //startActivity(intent);

                case 2:
                    //intent = new Intent(MainActivity.this, Festival.class);
                    //startActivity(intent);
                    break;
                case 3:
                    //intent = new Intent(MainActivity.this, Ideology.class);
                    //startActivity(intent);
                    view_cart(null);
                    break;
                case 4:
                    //intent = new Intent(MainActivity.this, SriRamakrishna.class);
                    //startActivity(intent);
                    break;
                case 5:
                    //intent = new Intent(MainActivity.this, SaradaDevi.class);
                    //startActivity(intent);
                    break;
                case 6:
                    //intent = new Intent(MainActivity.this, SwamiVivekananda.class);
                    //startActivity(intent);
                    break;
                case 7:
                    //intent = new Intent(MainActivity.this, Emblem.class);
                    //startActivity(intent);
                    break;
                case 8:
                    //intent = new Intent(MainActivity.this, BranchesActivity.class);
                    //startActivity(intent);
                    Intent intent = new Intent(this, CheckoutActivity.class);
                    startActivity(intent);
                    break;
                case 9:
                    //intent = new Intent(MainActivity.this, DonationPage.class);
                    //startActivity(intent);
                    break;
                case 10:
                    //intent = new Intent(MainActivity.this, ContactUs.class);
                    //startActivity(intent);
                    break;
                default:
                    break;
            }
        }
        catch (Exception e) {
            super.onStop();
        }

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hotlist_hot:
                view_cart(view);
                break;
            default:
                break;
        }
    }

    public void view_cart(View view) {
        Intent intent = new Intent(this, ViewCartActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

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

            url_new = "http://"+ server_ip +"/opencart/?route=feed/rest_api/products&category="+cat_id;
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
