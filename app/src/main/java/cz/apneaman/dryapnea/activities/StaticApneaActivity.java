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

public class StaticApneaActivity extends AppCompatActivity {

    private static final String TAG = StaticApneaActivity.class.getSimpleName();

    TrainingAdapter trainingAdapter;
    RecyclerView recyclerView;

    /* Začátek každé aktivity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Načtení layoutu activity */
        setContentView(R.layout.activity_static_apnea);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.btn_static_apnea_title));


        /* Seznam tréninků */
        // Vždy přetypovávat - findVieWByID defaultně vrací typ View
        recyclerView = findViewById(R.id.recyclerView);

        setupAdapters();

        /* FAB - vytvoření tréninku */
        FloatingActionButton fab = findViewById(R.id.fab);
        /* Použití AlerDialog pro PopUp okno */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.createTrainingDialog(StaticApneaActivity.this);
            }
        });
    }


    /* Inicializace tréninků*/
    private void setupAdapters(){
        /* Naplnění seznamu tréninků a předání do recycleru */
        List<Training> trainings = TrainingDao.selectAllTrainingsByType(Constants.STATIC_APNEA);
        trainingAdapter = new TrainingAdapter(trainings, this);
        recyclerView.setAdapter(trainingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    /* Aktualizace seznamu tréninků */
    public void refreshList() {
        trainingAdapter.updateList(TrainingDao.selectAllTrainingsByType(Constants.STATIC_APNEA));
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
