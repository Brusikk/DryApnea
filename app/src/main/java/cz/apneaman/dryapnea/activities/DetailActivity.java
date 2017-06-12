package cz.apneaman.dryapnea.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cz.apneaman.dryapnea.R;
import cz.apneaman.dryapnea.db.dao.CycleDao;
import cz.apneaman.dryapnea.db.dao.TrainingDao;
import cz.apneaman.dryapnea.db.tables.Cycle;
import cz.apneaman.dryapnea.db.tables.Training;
import cz.apneaman.dryapnea.utils.DialogHelper;

public class DetailActivity extends AppCompatActivity {

    public static final String TRAINING_ID = "TRAINING_ID";

    private Training training;
    private RecyclerView recyclerView;
    private CycleAdapter cycleAdapter;

    private Button startButton;
    private Button settingsButton;
    private Button statisticsButton;
    private Button addCycleButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        /* Back-šipka na předešlou activitu */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Načtení komponent */
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        startButton = (Button) findViewById(R.id.startButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        statisticsButton = (Button) findViewById(R.id.statisticsButton);
        addCycleButton = (Button) findViewById(R.id.addCycleButton);

        /* Záporná defaultní hodnota ID, ošetření správného přenosu ID */
        int trainingId = getIntent().getIntExtra(TRAINING_ID, -1);

        if (trainingId > -1) {
            training = TrainingDao.selectTrainingById(trainingId);
            setTitle(training.getName());
        }

        setupAdapters();
        setupButtons();
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
    }

    /* Uktualizace sérií tréninku*/
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
            holder.textView2.setText(item.getHoldTime() + "s");
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

                textView1 = ((TextView) itemView.findViewById(R.id.textView1));
                textView2 = ((TextView) itemView.findViewById(R.id.textView2));

                itemView.setOnLongClickListener(this);
            }

            /* Dialog - editace série*/
            @Override
            public boolean onLongClick(View v) {
                DialogHelper.editCycleDialog(DetailActivity.this, getItem(getLayoutPosition()));
                return true;
            }
        }
    }
}
