package in.co.tsystem.mango;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategoryActivity extends Activity {
    //private static final int[] IMAGES = { R.drawable.button, R.drawable.pause };
    //private int mPosition = 0;
    //private ImageSwitcher mImageSwitcher;

    //private DataHelper dbHelper = null;
    private GridViewAdapter ga;
    //myAsyncTask tsk;
    getCategories parent_tsk;
    int newCatDbVer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Calling onCreate", "");

        setContentView(R.layout.activity_category);
        String image_url = null;


        //dbHelper = new DataHelper(this);

        parent_tsk = new getCategories(this);
        parent_tsk.execute();

        /*
        mImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(MainActivity.this);
                return imageView;
            }
        });
        mImageSwitcher.setInAnimation(this, android.R.anim.slide_in_left);
        mImageSwitcher.setOutAnimation(this, android.R.anim.slide_out_right);

        onSwitch(null);

        mImageSwitcher.postDelayed(new Runnable() {
            int i = 0;
            public void run() {
                mImageSwitcher.setBackgroundResource(IMAGES[mPosition]);
                mPosition = (mPosition + 1) % IMAGES.length;
                mImageSwitcher.postDelayed(this, 3000);
            }
        }, 3000);*/
    }

    /*
    public void onSwitch(View view) {
        mImageSwitcher.setBackgroundResource(IMAGES[mPosition]);
        mPosition = (mPosition + 1) % IMAGES.length;
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                        view.animate().setDuration(2000).alpha(0)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        Integer idprod;
                                        int duration = Toast.LENGTH_SHORT;
                                        String txt;


                                        // AVI - looks like item is position on table
                                        idprod = ProdIdMap.get(item);
                                        txt = "item " + item + "id " + idprod;
                                        Toast.makeText(mContext, txt, duration).show();


                                        Intent intent = new Intent(mContext, ProductListActivity.class);
                                        intent.putExtra("category_id", idprod);
                                        startActivity(intent);

                                        //list.remove(item);
                                        //adapter.notifyDataSetChanged();
                                        //view.setAlpha(1);
                                    }
                                });

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

/*
            Integer db_ver_stored = 0;

            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                        openFileInput("CartDbVer")));
                String inputString;
                //StringBuffer stringBuffer = new StringBuffer();
                while ((inputString = inputReader.readLine()) != null) {
                    //stringBuffer.append(inputString + "\n");
                    db_ver_stored = Integer.parseInt(inputString
                    );
                }

            } catch (IOException e) {
                //e.printStackTrace();
                db_ver_stored = 0;
            }

            //Log.d("DB_VER_AND", dbHelper.DATABASE_VERSION + "" );
            Log.d("DB_VER_STORED is "+ db_ver_stored , "");
            //if (result != dbHelper.getVersionnew()) {
            if (result != db_ver_stored) {
                // download catalog db
                newCatDbVer = result;

                //write version to file
                try {
                    FileOutputStream fos = openFileOutput("CartDbVer", Context.MODE_PRIVATE);
                    fos.write(result.toString().getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //dbHelper.onUpgrade(dbHelper.getDb(),db_ver_stored,result);
                //tsk.execute();
            } else {
                // populate grid view from database

                GridView gv = (GridView)findViewById(R.id.gridview1);
                GridViewAdapter ga1 = new GridViewAdapter(mContext);
                gv.setAdapter(ga1);

                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        Toast.makeText(MainActivity.this, "" + position,
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
*/

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... arg0) {

            String url_new = null, ret = null;
            int version = 0;

            url_new = "http://192.168.43.214/opencart/?route=feed/rest_api/categories&key=1234";
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

