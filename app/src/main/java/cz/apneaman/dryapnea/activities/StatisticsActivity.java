package cz.apneaman.dryapnea.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

import cz.apneaman.dryapnea.R;
import cz.apneaman.dryapnea.db.dao.CounterDao;
import cz.apneaman.dryapnea.db.dao.TrainingDao;
import cz.apneaman.dryapnea.db.tables.Counter;
import cz.apneaman.dryapnea.db.tables.Training;

import static cz.apneaman.dryapnea.activities.DetailActivity.TRAINING_ID;

public class StatisticsActivity extends AppCompatActivity {

    //private CounterAdapter counterAdapter;
    //private RecyclerView recyclerView;

    private List<Counter> counters;
    private Training training;
    private BarChart chart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setTitle("Počet sérií");
        int trainingId = getIntent().getIntExtra(TRAINING_ID, -1);
        String trainingName = TrainingDao.getTrainingName(trainingId);
        getSupportActionBar().setSubtitle(trainingName);



        if (trainingId > -1) {
            training = TrainingDao.selectTrainingById(trainingId);
        }
        counters = CounterDao.selectCountersByTraining(training);

        //recyclerView = ((RecyclerView) findViewById(R.id.recyclerView));
        //setupAdapters();

        /* Nastavení grafu */
        chart = (BarChart) findViewById(R.id.barChart);
        chart.setDrawGridBackground(false);
        chart.setPinchZoom(true);
        chart.getAxisLeft().setEnabled(true);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawLabels(true);
        chart.getLegend().setEnabled(true);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);


        setData();
    }

    /* Zadání dat */
    private void setData() {

        ArrayList<BarEntry> values = new ArrayList<BarEntry>();

        ArrayList<DayCycle> cycles = getDayCycles();

       /* Pokud neni odjetý žádný trénink */
        if(cycles.isEmpty()){
            Toast.makeText(this, "Zatím žádná zadržení dechu", Toast.LENGTH_SHORT).show();
            finish(); //nejsou žádné statistiky
            return;
        }

       /* Hodnoty dat do grafu - x,y */
        for (int i = 0; i < cycles.size(); i++) {
            values.add(new BarEntry(i, cycles.get(i).count));
        }
//        values.add(new BarEntry(1, 16));
//        values.add(new BarEntry(2,5));
//        values.add(new BarEntry(3, 12));
//        values.add(new BarEntry(4, 8));

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Počet sérií za den");

            set1.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            BarData data = new BarData(dataSets);

            /* Přidání dat do graf */
            chart.setData(data);
            /* Refresh grafu */
            chart.invalidate();
        }
    }

    /* Počet sérií z konkrétního dne */
    public ArrayList<DayCycle> getDayCycles() {
        ArrayList<DayCycle> dayCycles = new ArrayList<>();
        /* Série z jednoho dne vložíme do stejného objektu a budeme mu zvedat count */
        for (Counter counter : counters) {
            if (dayCycles.isEmpty()) {
                dayCycles.add(new DayCycle(1, counter.getTimeStamp()));

            } else if (Math.abs(dayCycles.get(dayCycles.size() - 1).time-counter.getTimeStamp()) < 86400000) { //shodují se dny
                dayCycles.get(dayCycles.size() - 1).count++;
            } else {
                dayCycles.add(new DayCycle(1, counter.getTimeStamp()));
            }
        }
        return dayCycles;
    }

    class DayCycle {

        private int count;
        private long time;

        public DayCycle(int count, long time) {
            this.count = count;
            this.time = time;
        }
    }
}
