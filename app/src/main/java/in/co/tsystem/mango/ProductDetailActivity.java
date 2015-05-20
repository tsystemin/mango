package in.co.tsystem.mango;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static in.co.tsystem.mango.R.drawable.ic_drawer;


public class ProductDetailActivity extends ActionBarActivity implements View.OnClickListener {

    Integer prod_id;
    dnldProductDetails tsk;
    getImageNPopulateDate tsk2;
    TextView tv;
    mangoGlobals mg = mangoGlobals.getInstance();
    ActionBarDrawerToggle actionBarDrawerToggle;
    private ListView mDrawerListView;
    String[] drawer_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.actionbar_custom_view, null);
        actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        //updateHotCount();


        tv = (TextView) findViewById(R.id.hotlist_hot);
        tv.setOnClickListener(this);

        Intent myIntent = getIntent();
        prod_id = myIntent.getIntExtra("prod_id", 0);

        TextView q = (TextView)findViewById(R.id.quantity);
        cart_item tmp = new cart_item();
        tmp = mg.local_cart.get(prod_id);
        if (tmp != null) {
            q.setText(tmp.quantity.toString());
        } else {
            q.setText("0");
        }

        tsk = new dnldProductDetails(this);
        tsk.execute(prod_id);

        // navigation drawer new
        Resources resource = getResources();
        drawer_array = resource.getStringArray(R.array.nav_drawer_array);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);

        mDrawerListView = (ListView) findViewById(R.id.NavList2);
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
                View container = findViewById(R.id.Relative2);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class getImageNPopulateDate extends AsyncTask< JSONObject, Void, Bitmap > {

        JSONObject jb;
        private Context mContext;
        public getImageNPopulateDate (Context context){
            mContext = context;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            JSONObject prod;
            String price, desc, clear_desc;
            super.onPostExecute(result);
            ImageView im = (ImageView)findViewById(R.id.PimageView);
            im.setImageBitmap(result);

            try {
                prod = jb.getJSONObject("product");
                try {
                    price = prod.getString("price");
                    TextView tv = (TextView)findViewById(R.id.PtextView2);
                    tv.setText(price);
                } catch (Exception e) {
                }

                try {
                    desc = prod.getString("description");
                    SanitizeString se = new SanitizeString(desc);
                    clear_desc = se.mstring;
                    TextView tvd = (TextView)findViewById(R.id.PtextView);
                    tvd.setText(clear_desc);
                } catch (Exception e) {
                }

                /*
                TextView q = (TextView)findViewById(R.id.quantity);
                cart_item tmp = new cart_item();
                tmp = mg.local_cart.get(prod_id);
                if (tmp != null) {
                    q.setText(tmp.quantity.toString());
                } else {
                    q.setText("0");
                }*/

            } catch (Exception e) {

            }



            Button incb = (Button)findViewById(R.id.incbutton);
            incb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                    TextView tv = (TextView)findViewById(R.id.quantity);
                    CharSequence cnt = tv.getText();
                    Integer c = Integer.parseInt(cnt.toString());
                    c++;
                    tv.setText(c.toString());
                }
            });

            Button decb = (Button)findViewById(R.id.decbutton);
            decb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                    TextView tv = (TextView)findViewById(R.id.quantity);
                    CharSequence cnt = tv.getText();
                    Integer c = Integer.parseInt(cnt.toString());
                    if (c > 0) {
                        c--;
                        tv.setText(c.toString());
                    }

                }
            });

            Button b = (Button)findViewById(R.id.Pbutton);
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    TextView tv = (TextView)findViewById(R.id.quantity);
                    CharSequence cnt = tv.getText();
                    Integer c = Integer.parseInt(cnt.toString());
                    //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}

                    //addToCart cart_tsk = new addToCart(mContext);
                    //cart_tsk.execute(prod_id, c);

                    // ADD to local_cart
                    cart_item ci = new cart_item();
                    cart_item tmp = new cart_item();

                    ci.quantity = c;
                    ci.prod_id = prod_id;

                    tmp = mg.local_cart.get(prod_id);
                    if (tmp != null) {
                        tmp.quantity = c;
                    } else {
                        mg.local_cart.put(prod_id, ci);
                    }

                }
            });

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(JSONObject... arg) {

            Bitmap bitmap=null;
            String url_new = null;
            JSONArray categories;
            JSONObject item;
            JSONObject prod;
            String img = "data/demo/default.png";
            ArrayList<String> urls = new ArrayList<String>();
            jb = arg[0];

            try {
                prod = jb.getJSONObject("product");
                try {
                    img = prod.getString("image");
                } catch (Exception e) {
                }
            } catch (Exception e) {

            }
            mangoGlobals mg = mangoGlobals.getInstance();
            String server_ip = mg.server_ip;
            //url_new = "http://"+ ip +"/opencart/image/data/demo/chicken_numchow.jpeg";
            url_new = "http://"+ server_ip +"/opencart/image/" + img;
            urls.add(url_new);

            for (String url_to_open : urls) {
                try {
                    // Download the image
                    URL url = new URL(url_to_open);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    // Decode image to get smaller image to save memory
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = 4;
                    bitmap = BitmapFactory.decodeStream(is, null, options);
                    is.close();
                 } catch (IOException e) {
                    return null;
                }
            }
            return bitmap;
        }
    }

    private class dnldProductDetails extends AsyncTask< Integer, Void, JSONObject > {

        JSONObject jb;
        private Context mContext;
        BufferedReader br;

        public dnldProductDetails(Context context) {
            mContext = context;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            tsk2 = new getImageNPopulateDate(mContext);
            tsk2.execute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Integer... arg) {

            String url_new = null, ver = null;
            int version = 0;

            mangoGlobals mg = mangoGlobals.getInstance();
            String server_ip = mg.server_ip;
            url_new = "http://"+ server_ip +"/opencart/?route=feed/rest_api/product&id="+ arg[0] +"&key=1234";

            Log.i("PRODDET prod_id is", arg[0] + "");
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

    // Righ tnow not using addCart to add individual items directly to server. We update the local_cart and then
    // once ViewCartActivity is called, we update cart on server.

    private class addToCart extends AsyncTask< Integer, Void, JSONObject > {

        JSONObject jb;
        private Context mContext;
        BufferedReader br;

        public addToCart(Context context) {
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
        protected JSONObject doInBackground(Integer... arg) {

            String url_new = null, ver = null;
            int version = 0;

            mangoGlobals mg = mangoGlobals.getInstance();
            String server_ip = mg.server_ip;
            url_new = "http://"+ server_ip +"/opencart/?route=feed/rest_api/cart_add&product_id="+ arg[0] +"&quantity="+ arg[1] + "&key=1234"; // add count

            Log.i("PRODDET prod_id is", arg[0] + "");
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



