package com.example.workoutlogger.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.ExerciseSet;
import com.example.workoutlogger.data.Record;
import com.example.workoutlogger.data.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    private List<Record> records = new ArrayList<>();

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = records.get(position);
        holder.bind(record);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void setRecords(List<Record> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        private TextView reps;
        private TextView weight;
        private TextView date;
        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            reps = itemView.findViewById(R.id.record_item_rm);
            weight = itemView.findViewById(R.id.record_item_weight);
            date = itemView.findViewById(R.id.record_item_date);
        }

        public void bind(Record record) {
            ExerciseSet set = record.getSet();
            reps.setText(set.getReps() + " RM");
            weight.setText(set.getWeight() + " kg");
            SimpleDateFormat formatter = new SimpleDateFormat(
                    itemView.getContext().getString(R.string.date_format), Locale.getDefault());
            date.setText(formatter.format(record.getTimestamp().toDate()));
        }
    }

}
