package cz.apneaman.dryapnea.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import cz.apneaman.dryapnea.R;
import cz.apneaman.dryapnea.db.dao.CycleDao;
import cz.apneaman.dryapnea.db.dao.TrainingDao;
import cz.apneaman.dryapnea.db.tables.Cycle;
import cz.apneaman.dryapnea.db.tables.Training;
import cz.apneaman.dryapnea.utils.Constants;
import cz.apneaman.dryapnea.utils.DialogHelper;
import cz.apneaman.dryapnea.utils.TrainingType;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String TRAINING_ID = "TRAINING_ID";

    private Training training;
    private RecyclerView recyclerView;
    private CycleAdapter cycleAdapter;
    private ConstraintLayout defaultTableLayout;
    private TextView txtTargetAction;

    private Button startButton;
    private Button settingsButton;
    private Button statisticsButton;
    private Button addCycleButton;

    private Button btnO2Table;
    private Button btnCO2Table;
    private Button btnOneBreathTable;

    private EditText inputBreathing;
    private EditText inputBreathHold;
    private EditText inputShortage;
    private EditText inputNumberOfSeries;

     @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        /* Back-šipka na předešlou activitu */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Načtení komponent */
        recyclerView = findViewById(R.id.recyclerView);
        startButton = findViewById(R.id.startButton);
        settingsButton = findViewById(R.id.settingsButton);
        statisticsButton = findViewById(R.id.statisticsButton);
        addCycleButton = findViewById(R.id.addCycleButton);
        txtTargetAction = findViewById(R.id.txt_target_action);

        defaultTableLayout = findViewById(R.id.layout_default_table);
        btnO2Table = findViewById(R.id.btn_o2_table);
        btnCO2Table = findViewById(R.id.btn_co2_table);
        btnOneBreathTable = findViewById(R.id.btn_one_breath_table);

        /* Záporná defaultní hodnota ID, ošetření správného přenosu ID */
        int trainingId = getIntent().getIntExtra(TRAINING_ID, -1);

        if (trainingId > -1) {
            training = TrainingDao.selectTrainingById(trainingId);
            setTitle(training.getName());
        }

        setupAdapters();
        setupButtons();

        if (training.getType().equals(Constants.STATIC_APNEA)) {
            getSupportActionBar().setTitle(R.string.btn_static_apnea_title);
            txtTargetAction.setText(R.string.stop_breathing_title);
            setupDefaultTableLayout();
        } else {
            getSupportActionBar().setTitle(R.string.btn_apnea_walking_title);
            txtTargetAction.setText(R.string.number_of_steps_title);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    private void setupDefaultTableLayout() {
        if (cycleAdapter.getItemCount() > 0) {
            defaultTableLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            defaultTableLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /* Inicializace sérií tréninku*/
    private void setupAdapters() {
        List<Cycle> cyclesByTraining = CycleDao.selectCyclesByTraining(training);
        cycleAdapter = new CycleAdapter(cyclesByTraining);
        recyclerView.setAdapter(cycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /* Přechod do jiných aktivit */
    private void setupButtons() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, TrainingActivity.class);
                intent.putExtra(TRAINING_ID, training.getId());
                startActivity(intent);
            }
        });

        addCycleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.createCycleDialog(DetailActivity.this, training);
                defaultTableLayout.setVisibility(View.GONE);
            }
        });

        /* Settings activita */
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, SettingsActivity.class);
                intent.putExtra(TRAINING_ID, training.getId());
                startActivity(intent);
            }
        });

        /* Statistics activita*/
        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, StatisticsActivity.class);
                intent.putExtra(TRAINING_ID, training.getId());
                startActivity(intent);
            }
        });

        btnO2Table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateTrainingDialog(R.layout.dialog_o2_default, new TrainingType(TrainingType.O2));
            }
        });

        btnCO2Table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateTrainingDialog(R.layout.dialog_co2_default, new TrainingType(TrainingType.CO2));
            }
        });

        btnOneBreathTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateTrainingDialog(R.layout.dialog_one_breath_default, new TrainingType(TrainingType.ONE_BREATH));
            }
        });
    }

    private void showCreateTrainingDialog(int layoutId, final TrainingType type) {
        final View dialogView = LayoutInflater.from(this).inflate(layoutId, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                generateTrainingSeries(dialogView, type);
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void generateTrainingSeries(View dialogView, TrainingType type) {
        int shortage = 15;
        int breathing = 120;
        int breathHold = 120;
        int numberOfSeries = 8;

        inputBreathing = dialogView.findViewById(R.id.input_breathing);
        inputNumberOfSeries = dialogView.findViewById(R.id.input_series_number);

        if(type.getTrainingType().equals(TrainingType.ONE_BREATH)) {
            Log.e(TAG, "TrainingType.ONE_BREATH");
            inputBreathHold = dialogView.findViewById(R.id.input_breath_hold);
            breathing = 10;
            breathHold = 60;
        } else {
            Log.e(TAG, "TrainingType.CO2|O2");
            inputBreathHold = dialogView.findViewById(R.id.input_first_breath_hold);
            inputShortage = dialogView.findViewById(R.id.input_shortage);
            shortage = validateInput(inputShortage) ? Integer.parseInt(inputShortage.getText().toString()) : shortage;
        }

        numberOfSeries = validateInput(inputNumberOfSeries) ? Integer.parseInt(inputNumberOfSeries.getText().toString()) : numberOfSeries;
        breathing = validateInput(inputBreathing) ? Integer.parseInt(inputBreathing.getText().toString()) : breathing;
        breathHold = validateInput(inputBreathHold) ? Integer.parseInt(inputBreathHold.getText().toString()) : breathHold;

        Log.d(TAG, String.format("shortage: %d%nbreathing: %d%nbreath hold: %d%nseries: %d", shortage, breathing, breathHold, numberOfSeries));

        switch (type.getTrainingType()) {
            case TrainingType.O2:
                generateO2Series(numberOfSeries, breathing, breathHold, shortage);
                break;
            case TrainingType.CO2:
                generateCO2Series(numberOfSeries, breathing, breathHold, shortage);
                break;
            case TrainingType.ONE_BREATH:
                generateOneBreathSeries(numberOfSeries, breathing, breathHold);
                break;
            default:
                Log.e(TAG, "Some unknown training type.");
                break;
        }
        recyclerView.setVisibility(View.VISIBLE);
        defaultTableLayout.setVisibility(View.GONE);
        refreshList();
    }

    private void generateO2Series(int numberOfSeries, int breathing, int breathHold, int shortage) {
        for (int i = 0; i < numberOfSeries; i++) {
            Cycle cycle = new Cycle((long) breathing, (long) breathHold + i * shortage, training);
            CycleDao.createOrUpdate(cycle);
        }
    }

    private void generateCO2Series(int numberOfSeries, int breathing, int breathHold, int shortage) {
        for (int i = 0; i < numberOfSeries; i++) {
            Cycle cycle = new Cycle((long) breathing - i * shortage, (long) breathHold, training);
            CycleDao.createOrUpdate(cycle);
        }
    }

    private void generateOneBreathSeries(int numberOfSeries, int breathing, int breathHold) {
        for (int i = 0; i < numberOfSeries; i++) {
            Cycle cycle = new Cycle((long) breathing, (long) breathHold, training);
            CycleDao.createOrUpdate(cycle);
        }
    }

    private boolean validateInput(EditText input) {
        String value = input.getText().toString();
        try {
            Integer.parseInt(value);
            if (!value.isEmpty()) {
                return true;
            }
        } catch(NumberFormatException e) {
            input.setError("Hodnota není celé číslo...");
        }
        return false;
    }

    /* Aktualizace sérií tréninku*/
    public void refreshList() {
        cycleAdapter.updateList(CycleDao.selectCyclesByTraining(training));
    }

    /* Správa dat v recyclerView */
    class CycleAdapter extends RecyclerView.Adapter<CycleAdapter.ViewHolder> {
        private List<Cycle> cycles;

        public CycleAdapter(List<Cycle> cycles) {
            this.cycles = cycles;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.double_list_item, parent, false);
            return new ViewHolder(itemView);
        }

        /* Mapování listu*/
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Cycle item = getItem(position);
            holder.textView1.setText(item.getBreathTime() + "s");
            holder.textView2.setText(String.format("%d%s", item.getHoldTime(), training.getType().equals(Constants.STATIC_APNEA) ? "s" : ""));
        }

        /* Počet sérií v tréninku*/
        @Override
        public int getItemCount() {
            return cycles == null ? 0 : cycles.size();
        }

        public void updateList(List<Cycle> cycles) {
            this.cycles = cycles;
            notifyDataSetChanged();
        }

        public Cycle getItem(int position) {
            return cycles.get(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

            TextView textView1;
            TextView textView2;

            public ViewHolder(View itemView) {
                super(itemView);

                textView1 = itemView.findViewById(R.id.textView1);
                textView2 = itemView.findViewById(R.id.textView2);

                itemView.setOnLongClickListener(this);
            }

            /* Dialog - editace série*/
            @Override
            public boolean onLongClick(View v) {
                DialogHelper.editCycleDialog(DetailActivity.this, getItem(getLayoutPosition()), training);
                return true;
            }
        }
    }
}
