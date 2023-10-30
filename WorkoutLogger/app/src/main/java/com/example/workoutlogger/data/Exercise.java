package com.example.workoutlogger.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.workoutlogger.data.ExerciseSet;

import java.util.ArrayList;
import java.util.List;

public class Exercise implements Parcelable {
    private String id;
    private String name;
    private List<ExerciseSet> sets;

    public Exercise() {
    }

    public Exercise(String id, String name) {
        this.id = id;
        this.name = name;
        this.sets = new ArrayList<>();
    }

    protected Exercise(Parcel in) {
        id = in.readString();
        name = in.readString();
        sets = in.createTypedArrayList(ExerciseSet.CREATOR);
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    public Exercise(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Exercise exercise) {
        return this.name.equals(exercise.getName());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addSet() {
        ExerciseSet exerciseSet = new ExerciseSet();
        exerciseSet.setSetNumber(sets.size() + 1);
        sets.add(exerciseSet);
    }

    public void removeSet(int position) {
        sets.remove(position);
        for (int i = position; i < sets.size(); i++) {
            ExerciseSet exerciseSet = sets.get(i);
            exerciseSet.setSetNumber(i + 1);
        }
    }

    public List<ExerciseSet> getSets() {
        return sets;
    }

    public void setSets(List<ExerciseSet> sets) {
        this.sets = sets;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeTypedList(sets);
    }
}
