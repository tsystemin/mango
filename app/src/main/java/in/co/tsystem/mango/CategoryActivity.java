package in.co.tsystem.mango;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static in.co.tsystem.mango.R.drawable.ic_drawer;


public class CategoryActivity extends ActionBarActivity implements View.OnClickListener {
    getCategories parent_tsk;
    TextView tv;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private ListView mDrawerListView;

    Boolean doubleBackToExitPressedOnce = false;
    mangoGlobals mg = mangoGlobals.getInstance();

    String[] drawer_array;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_navigation);
        String image_url = null;

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.actionbar_custom_view, null);
        actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        //updateHotCount();

        tv = (TextView) findViewById(R.id.hotlist_hot);
        tv.setOnClickListener(this);

        ImageView callIcon = (ImageView) findViewById(R.id.callImage);
        callIcon.setOnClickListener(this);

        Resources resource = getResources();
        drawer_array = resource.getStringArray(R.array.nav_drawer_array);

        parent_tsk = new getCategories(this);
        parent_tsk.execute();

        // navigation drawer new
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerListView = (ListView) findViewById(R.id.NavList1);
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
                View container = findViewById(R.id.listView1);
                container.setTranslationX(offset * drawerView.getWidth());
            }

        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBarDrawerToggle.syncState();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hotlist_hot:
                view_cart(view);
                break;
            case R.id.callImage:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9986380357"));
                startActivity(intent);
            default:
                break;
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
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
                    Intent intentgd = new Intent(this, GuestDetails.class);
                    startActivity(intentgd);
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

    /**
     * Back button listener.
     * Will close the application if the back button pressed twice.
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            // Logout from the app
            logout();

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);

            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



    private class getCategories extends AsyncTask< Void, Void, JSONObject > {
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

        public getCategories(Context context) {
            mContext = context;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            JSONArray categories;
            JSONObject item;
            String cat_name;
            Integer cat_id;
            final ListView listview = (ListView) findViewById(R.id.listView1);
            final ArrayList<String> list = new ArrayList<String>();

            try {
                categories = result.getJSONArray("categories");
                for (int i = 0; i < categories.length(); i++) {
                    item = categories.getJSONObject(i);
                    cat_name  = item.getString("name");
                    cat_id = item.getInt("category_id");
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

                        Intent intent = new Intent(mContext, ProductListActivity.class);
                        intent.putExtra("category_id", idprod);
                        startActivity(intent);

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
            String url_new = "http://"+ mg.server_ip +"/opencart/?route=feed/rest_api/categories&key=1234";

            ServerComm.RestService re = new ServerComm.RestService();
            return re.doGet(url_new);
        }
    }

    public void view_cart(View view) {
        Intent intent = new Intent(this, ViewCartActivity.class);
        startActivity(intent);
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

