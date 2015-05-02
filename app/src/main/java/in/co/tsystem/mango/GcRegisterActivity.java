package in.co.tsystem.mango;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Register user
 */
public class GcRegisterActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    // UI references.
    private AutoCompleteTextView mEmailView, mNameView, mPhoneView, mAddrView;
    private EditText mPasswordView, mCPasswordView;
    private View mProgressView, mRegisterFormView;
    //  HttpResponse response;
    HttpResponse http_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gc_register);

        // Set up the register form.
        mNameView = (AutoCompleteTextView) findViewById(R.id.editText1);
        populateAutoComplete();

        mEmailView = (AutoCompleteTextView) findViewById(R.id.editText2);
        populateAutoComplete();

        mPhoneView = (AutoCompleteTextView) findViewById(R.id.editText3);
        populateAutoComplete();

        mAddrView = (AutoCompleteTextView) findViewById(R.id.editText4);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.editText5);
        mCPasswordView = (EditText) findViewById(R.id.editText6);
        mCPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        Button mSignUpButton = (Button) findViewById(R.id.gc_register_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                attemptRegister();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptRegister() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String cpassword = mCPasswordView.getText().toString();
        String phone = mPhoneView.getText().toString();
        String address = mAddrView.getText().toString();
        String firstname = "F";
        String lastname = "L";

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) | TextUtils.isEmpty(cpassword)) {
           // mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid name
        if (TextUtils.isEmpty(name)) {
          //  mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        } /*else if (!isNameValid(name)) {
            mNameView.setError(getString(R.string.error_invalid_email));
            focusView = mNameView;
            cancel = true;
        }*/ else {
            String[] parseName = name.split("\\s");
            firstname = parseName[0];
            lastname = parseName[1];
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
         //   mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
         //   mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid phone number
        if (TextUtils.isEmpty(phone)) {
         //   mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        } /*else if (!isPhoneValid(phone)) {
            mPhoneView.setError(getString(R.string.error_incorrect_phone));
            focusView = mPhoneView;
            cancel = true;
        }*/

        // Check for valid address
        if (TextUtils.isEmpty(address)) {
         //   mAddrView.setError(getString((R.string.error_field_required)));
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            String ip = getString(R.string.server_ip);
            String registerUri = "http://"+ ip +"/opencart/index.php?route=feed/rest_api/addNewCustomer";
            String postData = "{'firstname' : " + firstname + ", 'lastname' : " + lastname +
                    ", 'email' : " + email + ", 'telephone' : " + phone +
                    ", 'password' : " + password + ", 'address_1' : " + address + "}";
            try {
                myRegAsyncTask tsk = new myRegAsyncTask(this);
                tsk.execute(registerUri, postData);
            } catch (Throwable t) {
                Log.e("My App", "Could not : " + t);
            }

            //Log.d("JSON", response.optString(2));
        }
    }

    private boolean isNameValid(String name) {
        //TODO: Put more checking logic
        if (name.contains("[a-zA-Z]")) {
            return true;
        }

        return false;
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Put more checking logic
        if (phone.contains("[0-9]")) {
            return true;
        }

        return false;
    }

    private boolean isEmailValid(String email) {
        //TODO: Put more checking logic
        if (email.contains("@") && email.contains(".")) {
            return true;
        }

        return false;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Put more checking logic
        if ((password.length() > 4)) {
            return true;
        }
        return false;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(GcRegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    // Async task to send register request in a separate thread
    private class myRegAsyncTask extends AsyncTask<String, Void, HttpResponse> {



        private Context mContext;
        public myRegAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPostExecute(HttpResponse result) {
            super.onPostExecute(result);
            showProgress(false);
            mRegisterFormView.setVisibility(View.GONE);
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
                JSONObject json_result = new JSONObject(myresult);
                String aJsonString = json_result.getString("success");
                Log.d("ASYNC_CATCH", aJsonString);

                if (aJsonString.equals("TRUE")) {
                    Intent intent = new Intent(mContext, CategoryActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("ASYNC_CATCH wrong username / password", aJsonString);
                }
            } catch (Exception e) {
                // Oops
                Log.d("ASYNC_CATCH", "out....");
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HttpResponse doInBackground(String... arg0) {
            JSONObject obj;
            try {
                obj = new JSONObject(arg0[1]);
                HttpPostFunction sChannel = new HttpPostFunction();
                http_response = sChannel.processPost(arg0[0], obj);
                Thread.sleep(2000);
            } catch (Exception e) {
                Log.d("ASYNC CATCH", "exception");
            }

            return http_response;
        }
    }
}
