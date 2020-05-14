package com.sofiadutta.lights;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sofiadutta.SHACApplication;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private View contextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contextView = findViewById(R.id.mainActivityLayout);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, R.string.upload_data, Snackbar.LENGTH_LONG);

                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(contextView.getResources().getColor(
                        R.color.colorPrimary, getBaseContext().getTheme()));
                snackbar.show();
            }
        });

        KasaInfo kasaInfo = (KasaInfo) getIntent().getSerializableExtra(
                SHACApplication.getKasaInfoObject());
        if (Objects.equals(getIntent().getSerializableExtra(SHACApplication.getUserInfo()),
                SHACApplication.getAdultFamilyMember())) {
            //Show the information if the member is an adult
            showData(kasaInfo);
        } else if (Objects.equals(getIntent().getSerializableExtra(SHACApplication.getUserInfo()),
                SHACApplication.getChildFamilyMember())) {
            //Show read only view if the member is an child
            showReadOnlyData(kasaInfo);
        } else if (Objects.equals(getIntent().getSerializableExtra(SHACApplication.getUserInfo()),
                SHACApplication.getNonFamilyMember())) {
            //Show no data to strangers; kasaInfo will be null anyway
            showNoData();
        }

        /*
        InputStream is = getApplicationContext().getResources().openRawResource(
        R.raw.cloud_smart_device_privacy);

        Model schema = FileManager.get().loadModel(
        "https://ebiquity.umbc.edu/_file_directory_/papers/974.owl");
        Model data = FileManager.get().loadModel(
        "https://ebiquity.umbc.edu/_file_directory_/papers/974.owl");
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        InfModel infmodel = ModelFactory.createInfModel(reasoner, data);
        ValidityReport rep = infmodel.validate();
        Resource resource = infmodel.getResource(
        "https://prajitdas.com/assets/docs/ontologies/platys_access_control.owl#Person");
        infmodel.listStatements();
        Log.v("ontology", infmodel.listStatements().toString());
        SemanticManagement sm = new SemanticManagement(getApplicationContext());
        sm.getNamesInstances("Prajit");
        */
    }

    private void showData(KasaInfo kasaInfo) {
        RecyclerView mRecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        RecyclerView.Adapter mAdapter;
        if (kasaInfo != null) {
            mAdapter = new MyAdapter(kasaInfo);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void showReadOnlyData(KasaInfo kasaInfo) {
        RecyclerView mRecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        RecyclerView.Adapter mAdapter;
        if (kasaInfo != null) {
            mAdapter = new MyAdapter(kasaInfo);
            mRecyclerView.setAdapter(mAdapter);
        }

        Snackbar snackbar = Snackbar.make(contextView, R.string.read_only_access,
                Snackbar.LENGTH_LONG);

        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getApplicationContext().getResources().getColor(
                R.color.colorPrimary, getBaseContext().getTheme()));
        snackbar.show();
    }

    private void showNoData() {
        RecyclerView mRecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mRecyclerView.setAdapter(null);

        Snackbar snackbar = Snackbar.make(contextView, R.string.no_access, Snackbar.LENGTH_LONG);

        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getApplicationContext().getResources().getColor(
                R.color.colorPrimary, getBaseContext().getTheme()));
        snackbar.show();
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
        if (item.getItemId() == R.id.log_out) {
            Toast.makeText(this, R.string.logout, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}