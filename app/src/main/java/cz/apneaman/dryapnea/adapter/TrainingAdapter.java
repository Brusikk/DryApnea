package cz.apneaman.dryapnea.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.apneaman.dryapnea.R;
import cz.apneaman.dryapnea.activities.ApneaWalkingActivity;
import cz.apneaman.dryapnea.activities.DetailActivity;
import cz.apneaman.dryapnea.db.tables.Training;
import cz.apneaman.dryapnea.utils.Constants;
import cz.apneaman.dryapnea.utils.DialogHelper;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder>{
    private List<Training> trainingList;
    private Activity activity;

    public TrainingAdapter(List<Training> trainingList, Activity activity) {
        this.trainingList = trainingList;
        this.activity = activity;
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
        return trainingList == null ? 0 : trainingList.size();
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

            textView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        /* Přechod na detail tréninku */
        @Override
        public void onClick(View v) {
            String detailActivityType = Constants.STATIC_APNEA;
            if (activity instanceof ApneaWalkingActivity) {
                detailActivityType = Constants.APNEA_WALKING;
            }
            Intent intent = new Intent(activity, DetailActivity.class);
            intent.putExtra(DetailActivity.TRAINING_ID, getItem(getLayoutPosition()).getId());
            intent.putExtra("DetailActivityType", detailActivityType);
            activity.startActivity(intent);
        }

        /* Dialog - edit tréninku */
        @Override
        public boolean onLongClick(View v) {
            DialogHelper.editTrainingDialog(activity, trainingList.get(getLayoutPosition()));
            return true;
        }
    }
}
