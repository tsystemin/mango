package in.co.tsystem.mango;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class CheckoutActivity extends Activity {

    String payment_method;
    EditText billing_addr, delivery_addr;
    CheckBox use_same_addr, use_reg_addr;
    TextView total_price;
    mangoGlobals mg = mangoGlobals.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);



        RadioGroup radioGroupWebsite = (RadioGroup) findViewById
                (R.id.radioGroup1);

        /*
        RadioButton cod = (RadioButton)findViewById(R.id.codradiobutton);
        cod.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                RadioButton cod = (RadioButton)findViewById(R.id.codradiobutton);
                //int selected =
                        //cod.get();
                RadioButton pol = (RadioButton)findViewById(R.id.onlineradiobutton);
                pol.setChecked(false);
            }
        });

        RadioButton pol = (RadioButton)findViewById(R.id.onlineradiobutton);
        cod.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                RadioButton cod = (RadioButton)findViewById(R.id.codradiobutton);
                cod.setChecked(false);
            }
        });
        */

        total_price = (TextView) findViewById(R.id.textView1);
        total_price.setText(String.valueOf(mg.total_cart_price));

        billing_addr = (EditText) findViewById(R.id.bill_addr);
        delivery_addr = (EditText) findViewById(R.id.deli_addr);
        use_same_addr = (CheckBox) findViewById(R.id.del_same_as_bill);

        use_same_addr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    delivery_addr.setText(billing_addr.getText());
                }

            }
        });

        use_reg_addr = (CheckBox) findViewById(R.id.use_reg_addr);
        use_reg_addr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    billing_addr.setEnabled(false);
                    delivery_addr.setEnabled(false);
                } else {
                    billing_addr.setEnabled(true);
                    delivery_addr.setEnabled(true);
                }
            }
        });




        Button chkout = (Button)findViewById(R.id.chkoutbutton);
        chkout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                RadioGroup radioGroupWebsite = (RadioGroup) findViewById
                        (R.id.radioGroup1);
                int selected =
                        radioGroupWebsite.getCheckedRadioButtonId();
                RadioButton radioBtn1 = (RadioButton) findViewById(selected);
                //Toast.makeText(CheckoutActivity.this,
                //        radioBtn1.getText(), Toast.LENGTH_SHORT).show();
                if (radioBtn1.getText().equals("Cash on Delivery")) {
                    payment_method = "cod";

                } else {
                    Toast.makeText(CheckoutActivity.this,
                            "Only Cash on Delivery supported right now", Toast.LENGTH_SHORT).show();


                    return;
                    //finish();
                }

                String bill = billing_addr.getText().toString();
                String delivery = delivery_addr.getText().toString();

                String checkoutUri;
                String postData;

                if (bill.isEmpty() && delivery.isEmpty()) {
                    checkoutUri = "http://" + mg.server_ip + "/opencart/index.php?route=feed/rest_api/checkoutpost";
                    postData = "{'payment_method' : " + payment_method + ", 'shipping_method' : flat.flat" + "}";

                } else {
                    String bill1 = bill.replaceAll(",", "%40");
                    bill = bill1.replaceAll(" ", "%20");
                    bill1 = bill.replaceAll("-", "%30");


                    String delivery1 = delivery.replaceAll(",", "%40");
                    delivery = delivery1.replaceAll(" ", "%20");
                    delivery1 = delivery.replaceAll("-", "%30");

                    Toast.makeText(getApplicationContext(), bill1 + " and " + delivery1,
                            Toast.LENGTH_LONG).show();

                    //String registerUri = "http://"+ mg.server_ip +"?route=feed/rest_api/checkout&payment_method="+arg[0]+"&shipping_method=flat.flat&key=1234";
                    checkoutUri = "http://" + mg.server_ip + "/opencart/index.php?route=feed/rest_api/checkoutpost";

                    postData = "{'payment_method' : " + payment_method + ", 'payment_address' : " + bill1 + ", 'shipping_method' : flat.flat" +
                            ", 'shipping_address' : " + delivery1 + "}";
                }

                checkoutPost tsk = new checkoutPost(CheckoutActivity.this);
                //tsk.execute(payment_method);
                tsk.execute(checkoutUri, postData);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private class checkoutPost extends AsyncTask<String, Void, HttpResponse> {
        private Context mContext;
        private HttpResponse response;

        public checkoutPost(Context context) {
            mContext = context;
        }

        @Override
        protected void onPostExecute(HttpResponse result) {
            super.onPostExecute(result);
            String ret;
            try {
                HttpEntity entity = result.getEntity();
                InputStream inputStream = null;
                String myresult = null;

                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                myresult = sb.toString();
                //JSONObject json_result = new JSONObject(myresult);
                //String aJsonString = json_result.getString("success");

                if (myresult.contains("PASS")) {

                    cartClearfrmchkout cc = new cartClearfrmchkout(mContext);
                    cc.execute();

                    Toast.makeText(getApplicationContext(), "Your order is placed successfully",
                            Toast.LENGTH_LONG).show();
                } else {
                    Log.d("CHECKOUT ERROR", "code" + myresult);
                    Toast.makeText(getApplicationContext(), myresult,
                            Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected HttpResponse doInBackground(String... arg0) {
            JSONObject obj;

            try {
                obj = new JSONObject(arg0[1]);
                HttpPostFunction sChannel = new HttpPostFunction();
                response = sChannel.processPost(arg0[0], obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private class cartClearfrmchkout extends AsyncTask< Void, Void, JSONObject > {

        JSONObject jb;
        private Context mContext;
        BufferedReader br;

        public cartClearfrmchkout(Context context) {
            mContext = context;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            //clear the cart on APP
            mg.local_cart.clear();

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

    /*
    private class checkOutFromStore extends AsyncTask< String, Void, JSONObject > {

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
                    //cartClear cleartsk = new cartClear(mContext);
                    //cleartsk.execute();

                    Toast.makeText(getApplicationContext(), "Your order is placed successfully",
                            Toast.LENGTH_LONG).show();
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
        protected JSONObject doInBackground(String... arg) {

            String url_new = null, ver = null;
            int version = 0;

            mangoGlobals mg = mangoGlobals.getInstance();
            String server_ip = mg.server_ip;
            //url_new = "http://"+ server_ip +"/opencart/?route=feed/rest_api/checkout&payment_method=cod&shipping_method=flat.flat&key=1234"; // add count
            url_new = "http://"+ server_ip +"/opencart/?route=feed/rest_api/checkout&payment_method="+arg[0]+"&shipping_method=flat.flat&key=1234"; // add count

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
    }*/
}
