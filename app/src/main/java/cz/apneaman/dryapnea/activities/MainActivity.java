package cz.apneaman.dryapnea.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.apneaman.dryapnea.R;
import cz.apneaman.dryapnea.db.dao.TrainingDao;
import cz.apneaman.dryapnea.db.tables.Training;
import cz.apneaman.dryapnea.utils.DialogHelper;

public class MainActivity extends AppCompatActivity {

    TrainingAdapter trainingAdapter;
    RecyclerView recyclerView;

    /* test gitu */

    /* Začátek každé aktivity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Načtení layoutu activity */
        setContentView(R.layout.activity_main);
        /* Vždy přetypovávat - findVieWByID defaultně vrací typ View */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Seznam tréninků */
        recyclerView = ((RecyclerView) findViewById(R.id.recyclerView));
        setupAdapters();

        /* FAB - vytvoření tréninku */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /* Použití AlerDialog pro PopUp okno */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.createTrainingDialog(MainActivity.this);
            }
        });
    }


    /* Inicializace tréninků*/
    private void setupAdapters(){
        /* Naplnění seznamu tréninků a předání do recycleru */
        List<Training> trainings = TrainingDao.selectAllTrainings();
        trainingAdapter = new TrainingAdapter(trainings);
        recyclerView.setAdapter(trainingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    /* Aktualizace seznamu tréninků */
    public void refreshList() {
        trainingAdapter.updateList(TrainingDao.selectAllTrainings());
    }


    /* Správa dat v recyclerView */
    class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder>{
        private List<Training> trainingList;

        public TrainingAdapter(List<Training> trainingList) {
            this.trainingList = trainingList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item, parent, false);
            return new ViewHolder(itemView);
        }

        /* Mapování listu */
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(trainingList.get(position).getName());
        }

        /*Počet tréninků v listu*/
        @Override
        public int getItemCount() {
            return trainingList == null? 0 : trainingList.size();
        }

        public void updateList(List<Training> trainingList){
            this.trainingList = trainingList;
            notifyDataSetChanged();
        }

        public Training getItem(int position){
            return trainingList.get(position);
        }


        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);

                textView = ((TextView) itemView.findViewById(R.id.textView));

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            /* Přechod na detail tréninku */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.TRAINING_ID, getItem(getLayoutPosition()).getId());
                startActivity(intent);
            }

            /* Dialog - edit tréninku */
            @Override
            public boolean onLongClick(View v) {
                DialogHelper.editTrainingDialog(MainActivity.this, trainingList.get(getLayoutPosition()));
                return true;
            }
        }
    }
}
