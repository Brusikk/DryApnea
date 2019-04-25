package cz.apneaman.dryapnea.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import cz.apneaman.dryapnea.R;

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
