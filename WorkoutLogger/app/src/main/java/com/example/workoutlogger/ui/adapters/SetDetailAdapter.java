package com.example.workoutlogger.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.ExerciseSet;

import java.util.ArrayList;
import java.util.List;

public class SetDetailAdapter extends RecyclerView.Adapter<SetDetailAdapter.SetViewHolder> {

    private List<ExerciseSet> sets;
    public SetDetailAdapter() {
        this.sets = new ArrayList<>();
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_detail_item, parent, false);
        return new SetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        ExerciseSet exerciseSet = sets.get(position);
        holder.bind(exerciseSet);
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public void setSets(List<ExerciseSet> sets) {
        this.sets = sets;
        notifyDataSetChanged();
    }

    public static class SetViewHolder extends RecyclerView.ViewHolder {
        private final TextView setNumber;
        private final EditText reps;
        private final EditText weight;

        public SetViewHolder(@NonNull View itemView) {
            super(itemView);
            setNumber = itemView.findViewById(R.id.setNumberTextView);
            reps = itemView.findViewById(R.id.repsEditText);
            weight = itemView.findViewById(R.id.weightEditText);
        }


        public void bind(ExerciseSet exerciseSet) {
            setNumber.setText(String.valueOf(exerciseSet.getSetNumber()));
            reps.setText(String.valueOf(exerciseSet.getReps()));
            weight.setText(String.valueOf(exerciseSet.getWeight()));

            setNumber.setSelected(exerciseSet.isCompleted());
        }
    }
}
