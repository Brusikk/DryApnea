package cz.apneaman.dryapnea.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import cz.apneaman.dryapnea.R;
import cz.apneaman.dryapnea.preferences.PrefManager;

import static cz.apneaman.dryapnea.preferences.PrefManager.AFTER_START_0_SECSONDS;
import static cz.apneaman.dryapnea.preferences.PrefManager.AFTER_START_10_SECSONDS;
import static cz.apneaman.dryapnea.preferences.PrefManager.AFTER_START_1_MINUTE;
import static cz.apneaman.dryapnea.preferences.PrefManager.AFTER_START_2_MINUTES;
import static cz.apneaman.dryapnea.preferences.PrefManager.AFTER_START_30_SECSONDS;
import static cz.apneaman.dryapnea.preferences.PrefManager.AFTER_START_5_SECSONDS;
import static cz.apneaman.dryapnea.preferences.PrefManager.TO_START_0_SECSONDS;
import static cz.apneaman.dryapnea.preferences.PrefManager.TO_START_10_SECSONDS;
import static cz.apneaman.dryapnea.preferences.PrefManager.TO_START_1_MINUTE;
import static cz.apneaman.dryapnea.preferences.PrefManager.TO_START_2_MINUTES;
import static cz.apneaman.dryapnea.preferences.PrefManager.TO_START_30_SECSONDS;
import static cz.apneaman.dryapnea.preferences.PrefManager.TO_START_5_SECSONDS;

public class SettingsActivity extends AppCompatActivity {

    private SeekBar seekBarVolume;
    private CheckBox vibrationCheckBox;

    private CheckBox toStart2minutesCheckBox;
    private CheckBox toStart1minuteCheckBox;
    private CheckBox toStart30secondsCheckBox;
    private CheckBox toStart10secondsCheckBox;
    private CheckBox toStart5secondsCheckBox;
    private CheckBox toStartNowCheckBox;

    private CheckBox afterStart2minutesCheckBox;
    private CheckBox afterStart1minuteCheckBox;
    private CheckBox afterStart30secondsCheckBox;
    private CheckBox afterStart10secondsCheckBox;
    private CheckBox afterStart5secondsCheckBox;
    private CheckBox afterStartNowCheckBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Načtení komponent */
        seekBarVolume = (SeekBar) findViewById(R.id.seekBarVolume);
        vibrationCheckBox = (CheckBox) findViewById(R.id.vibrationCheckBox);

        toStart2minutesCheckBox = (CheckBox) findViewById(R.id.toStart2minutesCheckBox);
        toStart1minuteCheckBox = (CheckBox) findViewById(R.id.toStart1minuteCheckBox);
        toStart30secondsCheckBox = (CheckBox) findViewById(R.id.toStart30secondsCheckBox);
        toStart10secondsCheckBox = (CheckBox) findViewById(R.id.toStart10secondsCheckBox);
        toStart5secondsCheckBox = (CheckBox) findViewById(R.id.toStart5secondsCheckBox);
        toStartNowCheckBox = (CheckBox) findViewById(R.id.toStartNowCheckBox);

        afterStart2minutesCheckBox = (CheckBox) findViewById(R.id.afterStart2minutesCheckBox);
        afterStart1minuteCheckBox = (CheckBox) findViewById(R.id.afterStart1minuteCheckBox);
        afterStart30secondsCheckBox = (CheckBox) findViewById(R.id.afterStart30secondsCheckBox);
        afterStart10secondsCheckBox = (CheckBox) findViewById(R.id.afterStart10secondsCheckBox);
        afterStart5secondsCheckBox = (CheckBox) findViewById(R.id.afterStart5secondsCheckBox);
        afterStartNowCheckBox = (CheckBox) findViewById(R.id.afterStartNowCheckBox);

        seekBarVolume.setMax(10);

        /* Načte se globální nastavení z db */
        seekBarVolume.setProgress(PrefManager.getVolume());

        /* Listener pro nové nastavení */
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                PrefManager.setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        vibrationCheckBox.setChecked(PrefManager.isVibration());
        vibrationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefManager.setVibration(isChecked);
            }
        });

        toStart2minutesCheckBox.setChecked(PrefManager.isToStart(TO_START_2_MINUTES));
        toStart2minutesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefManager.setToStart(TO_START_2_MINUTES, isChecked);
            }
        });
        toStart1minuteCheckBox.setChecked(PrefManager.isToStart(TO_START_1_MINUTE));
        toStart1minuteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefManager.setToStart(TO_START_1_MINUTE, isChecked);
            }
        });
        toStart30secondsCheckBox.setChecked(PrefManager.isToStart(TO_START_30_SECSONDS));
        toStart30secondsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefManager.setToStart(TO_START_30_SECSONDS, isChecked);
            }
        });
        toStart10secondsCheckBox.setChecked(PrefManager.isToStart(TO_START_10_SECSONDS));
        toStart10secondsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefManager.setToStart(TO_START_10_SECSONDS, isChecked);
            }
        });
        toStart5secondsCheckBox.setChecked(PrefManager.isToStart(TO_START_5_SECSONDS));
        toStart5secondsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefManager.setToStart(TO_START_5_SECSONDS, isChecked);
            }
        });
        toStartNowCheckBox.setChecked(PrefManager.isToStart(TO_START_0_SECSONDS));
        toStartNowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefManager.setToStart(TO_START_0_SECSONDS, isChecked);
            }
        });

        afterStart2minutesCheckBox.setChecked(PrefManager.isAfterStart(AFTER_START_2_MINUTES));
        afterStart2minutesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefManager.setAfterStart(AFTER_START_2_MINUTES, isChecked);
            }
        });
        afterStart1minuteCheckBox.setChecked(PrefManager.isAfterStart(AFTER_START_1_MINUTE));
        afterStart1minuteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefManager.setAfterStart(AFTER_START_1_MINUTE, isChecked);
            }
        });
        afterStart30secondsCheckBox.setChecked(PrefManager.isAfterStart(AFTER_START_30_SECSONDS));
        afterStart30secondsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefManager.setAfterStart(AFTER_START_30_SECSONDS, isChecked);
            }
        });
        afterStart10secondsCheckBox.setChecked(PrefManager.isAfterStart(AFTER_START_10_SECSONDS));
        afterStart10secondsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefManager.setAfterStart(AFTER_START_10_SECSONDS, isChecked);
            }
        });
        afterStart5secondsCheckBox.setChecked(PrefManager.isAfterStart(AFTER_START_5_SECSONDS));
        afterStart5secondsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefManager.setAfterStart(AFTER_START_5_SECSONDS, isChecked);
            }
        });
        afterStartNowCheckBox.setChecked(PrefManager.isAfterStart(AFTER_START_0_SECSONDS));
        afterStartNowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefManager.setAfterStart(AFTER_START_0_SECSONDS, isChecked);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
