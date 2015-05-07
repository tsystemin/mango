package in.co.tsystem.mango;

/**
 * Created by diganta.paladhi on 05/04/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;


public class MainActivity extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;

    //Variable to keep track of button clicked
    private boolean FB_CLICKED = false;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private View main_view;

    CallbackManager callbackManager;
    LoginButton fbLoginButton;
    SignInButton gLoginButton;
    Button gcSignInButton;
    Button gcSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String USER = SaveSharedPreference.getUserName(MainActivity.this);
        final String PASS = SaveSharedPreference.getPass(MainActivity.this);

        if(USER.length() == 0 || PASS.length() == 0)
        {
            // call Login Activity
            //Initialize Facebook SDK
            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();

            //Initialize Google SDK
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_PROFILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            setContentView(R.layout.activity_main);

            //FB Login
            fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
            fbLoginButton.setReadPermissions("user_friends");
            fbLoginButton.setOnClickListener(this);

            //Google+ Login
            gLoginButton = (SignInButton) findViewById(R.id.g_login_button);
            setGooglePlusButtonText(gLoginButton, "Sign In with Google");
            gLoginButton.setOnClickListener(this);
            gLoginButton.setSize(SignInButton.SIZE_WIDE);

            //GameChanger Login
            gcSignInButton = (Button) findViewById(R.id.gc_sign_in_button);
            gcSignInButton.setOnClickListener(this);

            //GameChanger Register
            gcSignUpButton = (Button) findViewById(R.id.gc_sign_up_button);
            gcSignUpButton.setOnClickListener(this);

            main_view = findViewById(R.id.main_view);
        }
        else
        {
            // Login to the server
            GcLoginActivity gcLoginActivity = new GcLoginActivity();
            gcLoginActivity.silentLogin(USER, PASS);
            // Call Next Activity
            Intent intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);
        }


    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb_login_button:
                main_view.setVisibility(true ? View.GONE : View.VISIBLE);
                // Callback registration
                FB_CLICKED = true;
                fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d("SUCCESS"," Facebook Login Successful");
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
                break;
            case R.id.g_login_button:
                main_view.setVisibility(true ? View.GONE : View.VISIBLE);
                // Google+ Client
                mSignInClicked = true;
                if (!mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                } else if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                }
                break;
            case R.id.gc_sign_in_button:
                //myAsyncTask tsk = new myAsyncTask();
                //tsk.execute();
                main_view.setVisibility(true ? View.GONE : View.VISIBLE);
                gc_login(view);
                break;
            case R.id.gc_sign_up_button:
                main_view.setVisibility(true ? View.GONE : View.VISIBLE);
                gc_register(view);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if(!mResolvingError) {
        //mGoogleApiClient.connect();
        //}
    }

    /*@Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("SUCCESS"," Google Login Successful");
        getProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, ConnectionResult.RESOLUTION_REQUIRED);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        }
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView mTextView = (TextView) v;
                mTextView.setText(buttonText);
                return;
            }
        }
    }

    public void gc_login(View view) {
        Intent intent = new Intent(this, GcLoginActivity.class);
        startActivity(intent);

    }

    public void gc_register(View view) {
        Intent intent = new Intent(this, GcRegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e("TAG", "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                //txtName.setText(personName);
                //txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                //new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
