package com.sofiadutta.lights;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;
import com.sofiadutta.semanticweb.SemanticManagement;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class MainActivity extends AppCompatActivity {

    private static final String ADULT_FAMILY_MEMBER = "Adult/Family_Member";
    private static final String CHILD_FAMILY_MEMBER = "Child/Family_Member";
    private static final String NON_FAMILY_MEMBER = "Stranger";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mProgressView;
    private Button mAdultFamilyMember;
    private Button mChildFamilyMember;
    private Button mNonFamilyMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressView = findViewById(R.id.login_progress);

        mAdultFamilyMember = findViewById(R.id.button_parent);
        mChildFamilyMember = findViewById(R.id.button_child);
        mNonFamilyMember = findViewById(R.id.button_stranger);

        mAdultFamilyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(MainActivity.ADULT_FAMILY_MEMBER);
            }
        });

        mChildFamilyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(MainActivity.CHILD_FAMILY_MEMBER);
            }
        });

        mNonFamilyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(MainActivity.NON_FAMILY_MEMBER);
            }
        });

        createKey();
    }

    private void doRestOfUILoadingIfSuccessful(KasaInfo kasaInfo) {
//        KasaInfo kasaInfo = (KasaInfo) getIntent().getSerializableExtra("object");

        mRecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(kasaInfo);
        mRecyclerView.setAdapter(mAdapter);

//        InputStream is = getApplicationContext().getResources().openRawResource(R.raw.cloud_smart_device_privacy);

//        Model schema = FileManager.get().loadModel("https://ebiquity.umbc.edu/_file_directory_/papers/974.owl");
//        Model data = FileManager.get().loadModel("https://ebiquity.umbc.edu/_file_directory_/papers/974.owl");
//        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
//        reasoner = reasoner.bindSchema(schema);
//        InfModel infmodel = ModelFactory.createInfModel(reasoner, data);
//        ValidityReport rep = infmodel.validate();
//        Resource resource = infmodel.getResource("https://prajitdas.com/assets/docs/ontologies/platys_access_control.owl#Person");
//        infmodel.listStatements();
//        Log.v("ontology", infmodel.listStatements().toString());
//        SemanticManagement sm = new SemanticManagement(getApplicationContext());
//        sm.getNamesInstances("Prajit");
    }

    private void createKey() {
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

        }
    }

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private MainActivity.UserLoginTask mAuthTask = null;

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(String whoLoggedIn) {
        if (mAuthTask != null) {
            return;
        }

//        // Reset errors.
//        til1.setError(null);
//        til2.setError(null);

        // Store values at the time of the login attempt.
        String email = getApplication().getResources().getString(R.string.email);
        String password = getApplication().getResources().getString(R.string.password);

//        boolean cancel = false;
//        View focusView = null;

        /*
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {

           // til2.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
           // til2.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
         //   til1.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
          //  til1.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
*/
//        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
//        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
        showProgress(true);
        mAuthTask = new UserLoginTask(email, password);
        mAuthTask.execute();
//        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

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
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private KasaInfo kasaInfo;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            kasaInfo = new KasaInfo(mPassword, mEmail);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            kasaInfo.refresh();

            return kasaInfo.isValid();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("object", kasaInfo);

                startActivity(intent);
            } else {
//                Snackbar snackbar = Snackbar.make(mainLayout, "The email or password provided is incorrect", Snackbar.LENGTH_LONG);
//
//                View snackBarView = snackbar.getView();
//                snackBarView.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
//                snackbar.show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.log_out:
                Toast.makeText(this, "Logout called...", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
