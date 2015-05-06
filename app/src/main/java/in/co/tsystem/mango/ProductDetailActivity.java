package in.co.tsystem.mango;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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


public class ProductDetailActivity extends Activity {

    Integer prod_id;
    dnldProductDetails tsk;
    getImageNPopulateDate tsk2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Intent myIntent = getIntent();
        prod_id = myIntent.getIntExtra("prod_id", 0);

        tsk = new dnldProductDetails(this);

        tsk.execute(prod_id);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_detail, menu);
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

    private class getImageNPopulateDate extends AsyncTask< JSONObject, Void, Bitmap > {

        JSONObject jb;
        private Context mContext;
        public getImageNPopulateDate (Context context){
            mContext = context;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            JSONObject prod;
            String price, desc;
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
                    TextView tvd = (TextView)findViewById(R.id.PtextView);
                    tvd.setText(desc);
                } catch (Exception e) {
                }
            } catch (Exception e) {

            }

            Button b = (Button)findViewById(R.id.Pbutton);
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                    addToCart cart_tsk = new addToCart(mContext);
                    cart_tsk.execute(prod_id);

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




            String ip = getString(R.string.server_ip);
            //url_new = "http://"+ ip +"/opencart/image/data/demo/chicken_numchow.jpeg";
            url_new = "http://"+ ip +"/opencart/image/" + img;
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

            String ip = getString(R.string.server_ip);
            url_new = "http://"+ ip +"/opencart/?route=feed/rest_api/product&id="+ arg[0] +"&key=1234";

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

            String ip = getString(R.string.server_ip);
            url_new = "http://"+ ip +"/opencart/?route=feed/rest_api/cart_add&product_id="+ arg[0] +"&key=1234"; // add count

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



