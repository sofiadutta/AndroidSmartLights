package com.sofiadutta.lights;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private View mProgressView;

    private static final String ADULT_FAMILY_MEMBER = "Adult/Family_Member";
    private static final String CHILD_FAMILY_MEMBER = "Child/Family_Member";
    private static final String NON_FAMILY_MEMBER = "Stranger";

    private View contextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        contextView = findViewById(R.id.mainLinearLayout);
        mProgressView = findViewById(R.id.login_progress);

        Button mButtonAdultFamilyMember = findViewById(R.id.button_adult);
        mButtonAdultFamilyMember.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(LoginActivity.ADULT_FAMILY_MEMBER);
            }
        });

        Button mButtonChildFamilyMember = findViewById(R.id.button_child);
        mButtonChildFamilyMember.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(LoginActivity.CHILD_FAMILY_MEMBER);
            }
        });

        Button mButtonNonFamilyMember = findViewById(R.id.button_stranger);
        mButtonNonFamilyMember.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(LoginActivity.NON_FAMILY_MEMBER);
            }
        });

        /*
        try {
            // Create key
            final KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            final KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder("first",
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build();
            keyGenerator.init(keyGenParameterSpec);
            final SecretKey secretKey = keyGenerator.generateKey();


            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] iv = cipher.getIV();
            byte[] encryption = cipher.doFinal("im hungry".getBytes(StandardCharsets.UTF_8));

            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            final KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore
                    .getEntry("first", null);
            final SecretKey secretKey1 = secretKeyEntry.getSecretKey();
            final Cipher cipher1 = Cipher.getInstance("AES/GCM/NoPadding");
            final GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey1, spec);
            final byte[] decodedData = cipher.doFinal();
            final String unencryptedString = new String(decodedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.e("LoginActivity", e.toString());
        } */
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(String userInfo) {
        if (mAuthTask != null) {
            return;
        }

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        mAuthTask = new UserLoginTask();
        mAuthTask.execute(
                userInfo,
                getApplication().getResources().getString(R.string.email),
                getApplication().getResources().getString(R.string.password));
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @SuppressLint("StaticFieldLeak")
    public class UserLoginTask extends AsyncTask<String, Void, Boolean> {
        private KasaInfo kasaInfo;
        private String userInfo;

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                userInfo = params[0];
                kasaInfo = new KasaInfo(params[1], params[2]);
                kasaInfo.refresh();
                return kasaInfo.isValid();
            } catch (Exception e) {
                Log.e("LoginActivity", e.toString());
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("object", kasaInfo);
                intent.putExtra("userInfo", userInfo);

                startActivity(intent);
            } else {
                Snackbar snackbar = Snackbar.make(contextView, "The email or password provided is incorrect", Snackbar.LENGTH_LONG);

                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimary, getBaseContext().getTheme()));
                snackbar.show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
