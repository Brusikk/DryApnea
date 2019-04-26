package cz.apneaman.dryapnea.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;

import cz.apneaman.dryapnea.R;
import cz.apneaman.dryapnea.db.dao.CycleDao;
import cz.apneaman.dryapnea.db.dao.TrainingDao;
import cz.apneaman.dryapnea.db.tables.Cycle;
import cz.apneaman.dryapnea.db.tables.Training;
import cz.apneaman.dryapnea.utils.Constants;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private Button btnStaticApnea;
    private Button btnApneaWalking;
    private Button btnHeartRate;

    /* Začátek každé aktivity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Načtení layoutu activity */
        setContentView(R.layout.activity_home);
        init();

        /* Když mejsou žádné tréninky, zadej testovací data */
        List<Training> staticTrainings = TrainingDao.selectAllTrainingsByType(Constants.STATIC_APNEA);
        if (staticTrainings.size()<= 0) {
            fillDBStatic();
        }

        List<Training> walkingTrainings = TrainingDao.selectAllTrainingsByType(Constants.APNEA_WALKING);
        if (walkingTrainings.size()<= 0) {
            fillDBWalking();
        }

    }


    /* Inicializace UI + tlacitek */
    private void init(){
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        btnStaticApnea = findViewById(R.id.btn_static);
        btnApneaWalking = findViewById(R.id.btn_walking);
        btnHeartRate = findViewById(R.id.btn_heartrate);

        btnStaticApnea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, StaticApneaActivity.class));
            }
        });

        btnApneaWalking.setOnClickListener(l -> {
            startActivity(new Intent(HomeActivity.this, ApneaWalkingActivity.class));
        });

        btnHeartRate.setOnClickListener(l -> {
            startActivity(new Intent(HomeActivity.this, HeartRateActivity.class));
        });
    }


    /* Naplnění DB */
    public void fillDBStatic() {
        Training co2 = new Training("CO2 table", Constants.STATIC_APNEA);
        TrainingDao.create(co2);
        generateCO2Series(8,120,120,15, co2);

        Training o2 = new Training("O2 table", Constants.STATIC_APNEA);
        TrainingDao.create(o2);
        generateO2Series(8,120,120,15, o2);

        Training oneBreath = new Training("One Breath table", Constants.STATIC_APNEA);
        TrainingDao.create(oneBreath);
        generateOneBreathSeries(8, 12, 60, oneBreath);
    }
    public void fillDBWalking(){
        Training co2Walking = new Training("CO2 Walking table", Constants.APNEA_WALKING);
        TrainingDao.create(co2Walking);
        generateOneBreathSeries(8, 120, 50, co2Walking);
    }


    private void generateCO2Series(int numberOfSeries, int breathing, int breathHold, int shortage, Training training) {
        for (int i = 0; i < numberOfSeries; i++) {
            Cycle cycle = new Cycle((long) breathing - i * shortage, (long) breathHold, training);
            CycleDao.createOrUpdate(cycle);
        }
    }
    private void generateO2Series(int numberOfSeries, int breathing, int breathHold, int shortage, Training training) {
        for (int i = 0; i < numberOfSeries; i++) {
            Cycle cycle = new Cycle((long) breathing - i * shortage, (long) breathHold, training);
            CycleDao.createOrUpdate(cycle);
        }
    }
    private void generateOneBreathSeries(int numberOfSeries, int breathing, int breathHold, Training training) {
        for (int i = 0; i < numberOfSeries; i++) {
            Cycle cycle = new Cycle((long) breathing, (long) breathHold, training);
            CycleDao.createOrUpdate(cycle);
        }
    }
    private void generateWalkingSeries(int numberOfSeries, int breathing, int steps, Training training) {
        for (int i = 0; i < numberOfSeries; i++) {
            Cycle cycle = new Cycle((long) breathing, (long) steps, training);
            CycleDao.createOrUpdate(cycle);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_personal_records:
                startActivity(new Intent(this, PersonalRecordsActivity.class));
                break;
            default:
                Log.d(TAG, "Unknown menu option.");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
