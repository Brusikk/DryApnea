package cz.apneaman.dryapnea.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import cz.apneaman.dryapnea.R;

public class PersonalRecordsActivity extends AppCompatActivity {

    private static final String TAG = PersonalRecordsActivity.class.getSimpleName();

    TextView txtTimeRecord;
    TextView txtStepsRecord;

    int timeRecord = 76;
    int stepsRecord = 123;

    /* Začátek každé aktivity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Načtení layoutu activity */
        setContentView(R.layout.activity_personal_records);
        init();
    }

    public void setTxtTimeRecord(TextView txtTimeRecord) {
        this.txtTimeRecord = txtTimeRecord;
    }

    /* Inicializace UI + tlacitek */
    private void init(){
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.personal_records_title));



        txtTimeRecord = findViewById(R.id.txt_time_record);
        txtStepsRecord = findViewById(R.id.txt_steps_record);

        txtTimeRecord.setText(DateUtils.formatElapsedTime(timeRecord));
        txtStepsRecord.setText(stepsRecord+"");
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
