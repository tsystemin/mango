package in.co.tsystem.mango;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategoryActivity extends ActionBarActivity implements View.OnClickListener {
    private GridViewAdapter ga;
    //myAsyncTask tsk;
    getCategories parent_tsk;
    int newCatDbVer = 0;
    ImageView cart;

    Boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);
        String image_url = null;

        parent_tsk = new getCategories(this);
        parent_tsk.execute();

        // Add cart icon to the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar_custom_view, null);

        actionBar.setCustomView(v);

        cart = (ImageView) findViewById(R.id.imageView4);
        cart.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView4:
                view_cart(view);
                break;
            default:
                break;
        }
    }

    /**
     * Back button listener.
     * Will close the application if the back button pressed twice.
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            //super.onBackPressed();
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


    public class GridViewAdapter extends BaseAdapter {
        private Context context;

        public GridViewAdapter(Context context) {
            this.context = context;
        }

        private int[] icons = {
                android.R.drawable.btn_star_big_off,
                android.R.drawable.btn_star_big_on,
                android.R.drawable.alert_light_frame,
                android.R.drawable.alert_dark_frame,
                android.R.drawable.arrow_down_float,
                android.R.drawable.gallery_thumb,
                android.R.drawable.ic_dialog_map,
                android.R.drawable.ic_popup_disk_full,
                android.R.drawable.star_big_on,
                android.R.drawable.star_big_off,
                android.R.drawable.star_big_on
        };

        @Override
        public int getCount() {
            // need to traverse database and get length
            // may be populate an array
            return icons.length;
            //return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(600,400));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(10, 10, 10, 10);
            } else {
                imageView = (ImageView) convertView;
            }
            //imageView.setImageResource(icons[position]);
            //imageView.setImageBitmap(dbHelper.getBitmap(position + 1));
            return imageView;
        }


    }

    private class getCategories extends AsyncTask< Void, Void, JSONObject > {

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

            TextView tv;

            //tv = (TextView)findViewById(R.id.textView1);
            final ListView listview = (ListView) findViewById(R.id.listView1);
            //arr = jb.getJSONArray('categories');
            final ArrayList<String> list = new ArrayList<String>();



            try {
                categories = jb.getJSONArray("categories");
                for (int i = 0; i < categories.length(); i++) {
                    item = categories.getJSONObject(i);
                    cat_name  = item.getString("name");
                    cat_id = item.getInt("category_id");
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

            String url_new = null, ret = null;
            int version = 0;

            String ip = getString(R.string.server_ip);
            url_new = "http://"+ ip +"/opencart/?route=feed/rest_api/categories&key=1234";
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

    public void view_cart(View view) {
        Intent intent = new Intent(this, ViewCartActivity.class);
        startActivity(intent);
    }

}

