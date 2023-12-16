package com.example.workoutlogger.ui.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.ExerciseSet;

public class SetDetailAdapter extends ListAdapter<ExerciseSet, SetDetailAdapter.SetViewHolder> {

    public SetDetailAdapter(@NonNull DiffUtil.ItemCallback<ExerciseSet> diffCallback) {
        super(diffCallback);;
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_detail_item, parent, false);
        return new SetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        ExerciseSet exerciseSet = getItem(position);
        holder.bind(exerciseSet);
    }

    public static class SetViewHolder extends RecyclerView.ViewHolder {
        private final TextView setNumber;
        private final EditText reps;
        private final EditText weight;

        private ExerciseSet exerciseSet;

        public SetViewHolder(@NonNull View itemView) {
            super(itemView);
            setNumber = itemView.findViewById(R.id.setNumberTextView);
            reps = itemView.findViewById(R.id.repsEditText);
            weight = itemView.findViewById(R.id.weightEditText);
        }


        public void bind(ExerciseSet exerciseSet) {
            this.exerciseSet = exerciseSet;
            setNumber.setText(String.valueOf(exerciseSet.getSetNumber()));
            reps.setText(String.valueOf(exerciseSet.getReps()));
            weight.setText(String.valueOf(exerciseSet.getWeight()));

            setNumber.setSelected(this.exerciseSet.isCompleted());
        }
    }
}
