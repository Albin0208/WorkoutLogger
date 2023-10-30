package com.example.workoutlogger.ui.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.data.ExerciseSet;

import java.util.ArrayList;
import java.util.List;

public class SetAdapter extends ListAdapter<ExerciseSet, SetAdapter.SetViewHolder> {
    public SetAdapter(@NonNull DiffUtil.ItemCallback<ExerciseSet> diffCallback) {
        super(diffCallback);
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
        holder.bind(exerciseSet, position);

        PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.menuIcon);
        popupMenu.inflate(R.menu.menu_exercise);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_delete) {
                holder.removeItem(position);
                return true;
            }
            return false;
        });

        holder.menuIcon.setOnClickListener(v -> popupMenu.show());
    }

    public class SetViewHolder extends RecyclerView.ViewHolder {
        private final TextView setNumber;
        private final EditText reps;
        private final EditText weight;

        private final ImageView menuIcon;

        public SetViewHolder(@NonNull View itemView) {
            super(itemView);
            setNumber = itemView.findViewById(R.id.setNumberTextView);
            reps = itemView.findViewById(R.id.repsEditText);
            weight = itemView.findViewById(R.id.weightEditText);
            menuIcon = itemView.findViewById(R.id.menu_icon);

            setUpHintBehavior(reps);
            setUpHintBehavior(weight);
        }

        /**
         * Sets up the hint behavior for the EditTexts
         *
         * @param editText The EditText to set up
         */
        private void setUpHintBehavior(EditText editText) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    editText.setHint(charSequence.length() > 0 ? null : "0");
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });
        }

        public void bind(ExerciseSet exerciseSet, int position) {
            setNumber.setText(String.valueOf(exerciseSet.getSetNumber()));
            reps.setText(String.valueOf(exerciseSet.getReps()));
            weight.setText(String.valueOf(exerciseSet.getWeight()));
        }

        private void removeItem(int position) {
            List<ExerciseSet> currentList = new ArrayList<>(getCurrentList());
            if (position >= 0 && position < currentList.size()) {
                currentList.remove(position);

                // Update set numbers for the remaining items
                for (int i = position; i < currentList.size(); i++) {
                    ExerciseSet exerciseSet = currentList.get(i);
                    exerciseSet.setSetNumber(i + 1);
                    exerciseSet.setId(i + 1);
                }

                // Notify the adapter of the item range change
                int itemCount = getItemCount() - position;
                if (itemCount > 0) {
                    notifyItemRangeChanged(position, itemCount);
                }

                submitList(currentList);
            }
        }
    }
}
