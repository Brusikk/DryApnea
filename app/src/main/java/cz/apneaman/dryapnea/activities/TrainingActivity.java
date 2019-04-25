package cz.apneaman.dryapnea.activities;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cz.apneaman.dryapnea.R;
import cz.apneaman.dryapnea.db.dao.CounterDao;
import cz.apneaman.dryapnea.db.dao.CycleDao;
import cz.apneaman.dryapnea.db.dao.SettingsDao;
import cz.apneaman.dryapnea.db.dao.TrainingDao;
import cz.apneaman.dryapnea.db.tables.Counter;
import cz.apneaman.dryapnea.db.tables.Cycle;
import cz.apneaman.dryapnea.db.tables.Settings;
import cz.apneaman.dryapnea.db.tables.Training;
import cz.apneaman.dryapnea.utils.Constants;
import cz.apneaman.dryapnea.utils.SoundHelper;

public class TrainingActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = TrainingActivity.class.getSimpleName();

    private static final int ONE_SECOND = 500; //millis - ne tisíc protože to nefunguje a poslední vteřina se skipne

    private Button startButton;
    private TextView infoTextView;
    private TextView breatheTimeTextView;
    private TextView holdTimeTextView;
    private TextView txtTargetAction;

    private Training training;
    private Settings settings;
    private List<Cycle> cycles;

    //    private Handler handler;
    /* Odpočet - 1s = 1000ms */
    private CountDownTimer countDownTimer;
    private CountDownTimer blackOutTimer;

    private boolean needAttetion;
    private boolean isHoldTime;
    private boolean timerCanceled;
    private boolean iAmOk;
    private boolean rip;
    private boolean isRunning;
    private boolean isRunning2;

    private long steps;
    private boolean walkingStarted;

    private SensorManager sManager;
    private Sensor stepSensor;
    private boolean isSensorPresent = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startButton = findViewById(R.id.startButton);
        infoTextView = findViewById(R.id.infoTextView);
        breatheTimeTextView = findViewById(R.id.breatheTimeTextView);
        holdTimeTextView = findViewById(R.id.holdTimeTextView);
        txtTargetAction = findViewById(R.id.txt_target_action);

        int trainingId = getIntent().getIntExtra(DetailActivity.TRAINING_ID, -1);

        if (trainingId == -1) {
            finish();
        }

        training = TrainingDao.selectTrainingById(trainingId);
        settings = SettingsDao.selectSettingByTrainingId(trainingId);

        if (training.getType().equals(Constants.STATIC_APNEA)) {
            txtTargetAction.setText(R.string.stop_breathing_title);
        } else {
            txtTargetAction.setText(R.string.number_of_steps_title);
        }
        /* Namapování sérií */
        cycles = CycleDao.selectCyclesByTraining(training);
        /* Zobrazení první série*/
        if (!cycles.isEmpty()) {
            breatheTimeTextView.setText(cycles.get(0).getBreathTime() + " sec");
            holdTimeTextView.setText(training.getType().equals(Constants.STATIC_APNEA) ? cycles.get(0).getHoldTime() + " sec" : cycles.get(0).getHoldTime()+"");
        }

        timerCanceled = false;
        isHoldTime = false;
        iAmOk = false;
        rip = false;
        isRunning = false;
        isRunning2 = false;
        /*
        if (walkingStarted) {
                txtSteps.setVisibility(View.VISIBLE);
                steps = 0;
                txtSteps.setText(String.valueOf(steps));
            } else {
                txtSteps.setVisibility(View.INVISIBLE);
            }
            walkingStarted = !walkingStarted;
         */

        sManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        if(sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isSensorPresent = true;
        } else {
            isSensorPresent = false;
        }

        Log.e(TAG, "Sensor exists? - "+ isSensorPresent);

        /* Start training button*/
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareToStart();
            }
        });
    }

    /* Odpočet do startu */
    private void prepareToStart() {
        infoTextView.setText("");
        infoTextView.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(4000, ONE_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                infoTextView.setText("" + millisUntilFinished / 1000);
            }

            @Override
            /* Konec odpočtu - nastaví se první série */
            public void onFinish() {
                infoTextView.setVisibility(View.GONE);
                if (training.getType().equals(Constants.STATIC_APNEA)) {
                    setupBreathTimer();
                } else {
                    walkingStarted = true;
                    steps = cycles.get(0).getHoldTime();
                    isRunning = true;
                }
            }
        };

        countDownTimer.start();
        startButton.setText("Stop");
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* DEJ MI SIGNÁL - stisknuto*/
                if (needAttetion) {
                    needAttetion = false;
                    blackOutTimer.cancel();
                    iAmOk = true;
                    startButton.setText("Stop");
                } else {
                    /* STOP - ručně zastaveno */
                    if (!timerCanceled) {
                        timerCanceled = true;
                        countDownTimer.cancel();
                        startButton.setText("Start");
                     /* START - ručně spuštěno */
                    } else {
                        timerCanceled = false;
                        countDownTimer.start();
                        startButton.setText("Stop");
                    }
                }
            }
        });
    }

    /* Nastavení zádrže dechu */
    /* Vykonané série z listu mažu */
    private void setupHoldTimer() {
        /* Nastavení hodnot z první pozice */
        breatheTimeTextView.setText(cycles.get(0).getBreathTime() + " sec");
        holdTimeTextView.setText(cycles.get(0).getHoldTime() + " sec");
        isHoldTime = true;
        countDownTimer = new CountDownTimer(cycles.get(0).getHoldTime() * 1000, ONE_SECOND) {
            /* Jednou za vteřinu pípne */
            @Override
            public void onTick(long millisUntilFinished) {
                isRunning = true;
                if ((millisUntilFinished / 500) % 2 == 1) {   // Každou vteřinu splněno
                    SoundHelper.shouldBeep(getApplicationContext(), millisUntilFinished, SoundHelper.AFTER_START, TrainingActivity.this, iAmOk);
                }
                holdTimeTextView.setText(millisUntilFinished / 1000 + " sec");
            }

            /* Upozornění na ztrátu vědomí, jednou ze 30 tréninků */
            @Override
            public void onFinish() {
                isRunning = false;
                CounterDao.createCounter(new Counter(cycles.get(0)));
//                if (new Random().nextInt(1) == 1) { //lze upravit
//                    setupIamOkayMessage();
//                } else {
                    holdTimeTextView.setText("Dýchej");
                    cycles.remove(0);
                    if (!cycles.isEmpty()) {
                        setupBreathTimer();
                    }
                    else {
                        setupEnd();
                    }
//                }
            }
        };
        countDownTimer.start();
    }

    /* Nastavení dechové fáze */
    private void setupBreathTimer() {
        if (!cycles.isEmpty()) {
            isHoldTime = false;
            countDownTimer = new CountDownTimer(cycles.get(0).getBreathTime() * 1000, ONE_SECOND) {
                /* Jednou za vteřinu pípne */
                @Override
                public void onTick(long millisUntilFinished) {
                    isRunning = true;
                    if ((millisUntilFinished / 500) % 2 == 1) {  // Každou vteřinu splněno
                        SoundHelper.shouldBeep(getApplicationContext(), millisUntilFinished, SoundHelper.TO_START, TrainingActivity.this, iAmOk);
                    }

                    breatheTimeTextView.setText(millisUntilFinished / 1000 + " sec");
                }

                /* Vykonané série z listu mažu */
                @Override
                public void onFinish() {
                    isRunning = false;
                    breatheTimeTextView.setText("Start");
                    //cycles.remove(0); //odstraníme první série
                    if (!cycles.isEmpty()) {
                        setupHoldTimer();
                    }
                }
            };
            countDownTimer.start();
        }
    }

    /* Kontrola vědomí*/
    public void setupIamOkayMessage() {
        needAttetion = true;
        startButton.setText("Jsem při vědomí");
        blackOutTimer = new CountDownTimer(5000, ONE_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                isRunning2 = true;
            }
            @Override
            public void onFinish() {
                isRunning2 = false;
                blackout();
            }
        };
        blackOutTimer.start();
    }

    public void blackout() {
        blackOutTimer = new CountDownTimer(10000, ONE_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                isRunning2 = true;
                if ((millisUntilFinished / 500) % 2 == 1) {  // Každou vteřinu splněno
                    SoundHelper.alarmBeep(getApplicationContext());
                }

            }
            @Override
            public void onFinish() {
                isRunning2 = false;
                rip = true;
                countDownTimer.cancel();
                setupEnd();
            }
        };
        blackOutTimer.start();
    }

    /* Konec tréninku */
    private void setupEnd() {
        infoTextView.setVisibility(View.VISIBLE);
        if (rip) {
            infoTextView.setText("A je to v pytli...");
        } else {
            infoTextView.setText("Konec tréninku");
        }

        SoundHelper.endBeep(getApplicationContext(), rip);

        infoTextView.setOnClickListener(new View.OnClickListener() {
            /* Konec aktivity */
            @Override
            public void onClick(View v) {
                TrainingActivity.this.finish();
            }
        });
    }

    /* Back button */
    /* Ukončení všech Timeru */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isRunning) {
                countDownTimer.cancel();
                isRunning = false;
            }

            if (isRunning2) {
                blackOutTimer.cancel();
                isRunning2 = false;
            }


            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setIAmOk(boolean iAmOk) {
        this.iAmOk = iAmOk;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isSensorPresent) {
            sManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isSensorPresent) {
            sManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        txtSteps.setText(String.valueOf(steps));
        holdTimeTextView.setText((String.valueOf(steps)));
        steps--;
        if (steps == 0) {
            cycles.remove(0);
//            setupEnd();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
