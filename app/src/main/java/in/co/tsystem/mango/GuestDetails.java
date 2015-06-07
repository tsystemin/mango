package in.co.tsystem.mango;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class GuestDetails extends Activity {
    String  guest_name="", guest_email="", guest_address="", guest_pin="", guest_phone="";
    EditText gn, ge, ga, gp, gph;
    String payment_method="";
    mangoGlobals mg = mangoGlobals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_details);

        TextView total_price = (TextView) findViewById(R.id.textView1);
        total_price.setText(String.valueOf(mg.total_cart_price));

        Button login = (Button)findViewById(R.id.login_to_checkout);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentlogin = new Intent(GuestDetails.this, LoginActivity.class);
                intentlogin.putExtra("CallerActivity", "guestdetails");
                startActivity(intentlogin);
            }
        });



        Button chkout = (Button)findViewById(R.id.chkguest);
        chkout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                gn = (EditText)findViewById(R.id.guest_name);
                guest_name = gn.getText().toString();
                String name1 = guest_name.replaceAll(" ", "%20");
                guest_name = name1;

                ge = (EditText)findViewById(R.id.guest_email);
                guest_email = ge.getText().toString();


                ga = (EditText)findViewById(R.id.guest_addr);
                guest_address = ga.getText().toString();
                String addr1 = guest_address.replaceAll(" ", "%20");
                guest_address = addr1.replaceAll(",", "%40");
                addr1 = guest_address.replaceAll("-", "%30");
                guest_address = addr1;

                gp = (EditText)findViewById(R.id.guest_pin);
                guest_pin = gp.getText().toString();

                gph = (EditText)findViewById(R.id.guest_phone);
                guest_phone = gph.getText().toString();

                if(guest_name.isEmpty()) {
                    Toast.makeText(GuestDetails.this,
                            "Please enter Name", Toast.LENGTH_SHORT).show();
                    gn.setError("Name is required");
                    gn.requestFocus();
                    return;
                } else if (guest_email.isEmpty() && !isEmailValid(guest_email)) {
                    Toast.makeText(GuestDetails.this,
                            "Please enter Email", Toast.LENGTH_SHORT).show();
                    ge.setError("Email is required");
                    ge.requestFocus();
                    return;
                } else if (guest_address.isEmpty()) {
                    Toast.makeText(GuestDetails.this,
                            "Please enter Address", Toast.LENGTH_SHORT).show();
                    ga.setError("Address is required");
                    ga.requestFocus();
                    return;
                }

                RadioGroup radioGroupguest = (RadioGroup) findViewById
                        (R.id.radioGroupGuest1);
                int selected =
                        radioGroupguest.getCheckedRadioButtonId();
                RadioButton radioBtn1 = (RadioButton) findViewById(selected);
                //Toast.makeText(CheckoutActivity.this,
                //        radioBtn1.getText(), Toast.LENGTH_SHORT).show();
                if (radioBtn1.getText().equals("Cash on Delivery")) {
                    payment_method = "cod";

                } else {
                    Toast.makeText(GuestDetails.this,
                            "Only Cash on Delivery supported right now", Toast.LENGTH_SHORT).show();
                    return;
                }

                String checkoutUri;
                String postData;

                //AVI - implement guestcheckoutpost - set GUEST variables
                checkoutUri = "http://" + mg.server_ip + "/opencart/index.php?route=feed/rest_api/guestCheckoutpost";

                postData = "{'payment_method' : " + payment_method + ", 'payment_address_guest' : " + guest_address + ", 'shipping_method' : flat.flat" +
                        ", 'shipping_address_guest' : " + guest_address + ", 'name_guest' : " + guest_name + ", 'email_guest' : " + guest_email + ", 'phone_guest' : " +
                        guest_phone + ", 'pin_guest' : " + guest_pin + "}";
                guestcheckoutPost chk = new guestcheckoutPost(GuestDetails.this);
                chk.execute(checkoutUri, postData);
            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Put more checking logic
        if (email.contains("@") && email.contains(".")) {
            return true;
        }

        return false;
    }

    private class guestcheckoutPost extends AsyncTask<String, Void, HttpResponse> {
        private Context mContext;
        private HttpResponse response;

        public guestcheckoutPost(Context context) {
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

                    cartClearfrmchkoutguest cc = new cartClearfrmchkoutguest(mContext);
                    cc.execute();
                    JSONObject j = new JSONObject(myresult);
                    String or_id = j.getString("order_id");

                    Toast.makeText(getApplicationContext(), "Your order is placed successfully",
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(mContext, OrderDetails.class);
                    //intent.putExtra("order_id", 135);
                    intent.putExtra("order_id", Integer.parseInt(or_id));
                    startActivity(intent);


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private class cartClearfrmchkoutguest extends AsyncTask< Void, Void, JSONObject > {

        JSONObject jb;
        private Context mContext;
        BufferedReader br;

        public cartClearfrmchkoutguest(Context context) {
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
}
