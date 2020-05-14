package com.sofiadutta.lights;

import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, "Refreshing devices...", Snackbar.LENGTH_LONG);

                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
                snackbar.show();
            }
        });

        KasaInfo kasaInfo = (KasaInfo) getIntent().getSerializableExtra("object");

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