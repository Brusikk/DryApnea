package cz.apneaman.dryapnea.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import cz.apneaman.dryapnea.R;
import cz.apneaman.dryapnea.adapter.TrainingAdapter;
import cz.apneaman.dryapnea.db.dao.TrainingDao;
import cz.apneaman.dryapnea.db.tables.Training;
import cz.apneaman.dryapnea.utils.Constants;
import cz.apneaman.dryapnea.utils.DialogHelper;

public class ApneaWalkingActivity extends AppCompatActivity {

    private static final String TAG = ApneaWalkingActivity.class.getSimpleName();

    TrainingAdapter trainingAdapter;
    RecyclerView recyclerView;

    /* Začátek každé aktivity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Načtení layoutu activity */
        setContentView(R.layout.activity_static_apnea);
        /* Vždy přetypovávat - findVieWByID defaultně vrací typ View */
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.btn_apnea_walking_title));


        /* Seznam tréninků */
        recyclerView = findViewById(R.id.recyclerView);
        setupAdapters();

        /* FAB - vytvoření tréninku */
        FloatingActionButton fab = findViewById(R.id.fab);
        /* Použití AlerDialog pro PopUp okno */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.createTrainingDialog(ApneaWalkingActivity.this);
            }
        });
    }


    /* Inicializace tréninků*/
    private void setupAdapters(){
        /* Naplnění seznamu tréninků a předání do recycleru */
        List<Training> trainings = TrainingDao.selectAllTrainingsByType(Constants.APNEA_WALKING);
        trainingAdapter = new TrainingAdapter(trainings, this);
        recyclerView.setAdapter(trainingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    /* Aktualizace seznamu tréninků */
    public void refreshList() {
        trainingAdapter.updateList(TrainingDao.selectAllTrainingsByType(Constants.APNEA_WALKING));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
            default:
                Log.d(TAG, "Unknown menu option.");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
