package in.co.tsystem.mango;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;


public class CheckoutActivity extends Activity {

    String payment_method;


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

                checkOutFromStore tsk = new checkOutFromStore(CheckoutActivity.this);
                tsk.execute(payment_method);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
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
    }
}
