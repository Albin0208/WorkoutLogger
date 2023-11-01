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

public class SetAdapter extends ListAdapter<ExerciseSet, SetAdapter.SetViewHolder> {
    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    private final SetListener setListener;
    private int adapterPosition;

    public SetAdapter(@NonNull DiffUtil.ItemCallback<ExerciseSet> diffCallback, SetListener listener) {
        super(diffCallback);
        this.setListener = listener;
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item_layout, parent, false);
        return new SetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        ExerciseSet exerciseSet = getItem(position);
        holder.bind(exerciseSet);

        holder.setNumber.setOnClickListener(v -> setListener.onSetToggleCompletion(exerciseSet, position, this));

        PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.menuIcon);
        popupMenu.inflate(R.menu.menu_exercise);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_delete) {
                setListener.onSetRemoved(exerciseSet, adapterPosition, this);

                return true;
            }
            return false;
        });

        holder.menuIcon.setOnClickListener(v -> popupMenu.show());
    }

    public static class SetViewHolder extends RecyclerView.ViewHolder {
        private final TextView setNumber;
        private final EditText reps;
        private final EditText weight;

        private final ImageView menuIcon;
        private ExerciseSet exerciseSet;

        public SetViewHolder(@NonNull View itemView) {
            super(itemView);
            setNumber = itemView.findViewById(R.id.setNumberTextView);
            reps = itemView.findViewById(R.id.repsEditText);
            weight = itemView.findViewById(R.id.weightEditText);
            menuIcon = itemView.findViewById(R.id.menu_icon);

            setUpHintBehavior(reps, true);
            setUpHintBehavior(weight, false);
        }

        /**
         * Sets up the hint behavior for the EditTexts
         *
         * @param editText The EditText to set up
         */
        private void setUpHintBehavior(EditText editText, final boolean isReps) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    editText.setHint(charSequence.length() > 0 ? null : "0");

                    if (exerciseSet != null) {
                        int value = charSequence.length() == 0 ? 0 : Integer.parseInt(charSequence.toString());
                        if (isReps) {
                            exerciseSet.setReps(value);
                        } else {
                            exerciseSet.setWeight(value);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });
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
