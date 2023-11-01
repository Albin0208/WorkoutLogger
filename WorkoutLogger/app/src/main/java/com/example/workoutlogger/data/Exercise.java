package com.example.workoutlogger.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.workoutlogger.data.ExerciseSet;

import java.util.ArrayList;
import java.util.List;

public class Exercise implements Parcelable {
    private String id;
    private String name;
    private List<ExerciseSet> sets = new ArrayList<>();

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
        return this.name.equals(exercise.getName()) && this.sets.equals(exercise.getSets());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addSet(ExerciseSet set) {
        if (this.sets == null) {
            this.sets = new ArrayList<>();
        }
        this.sets.add(set);
    }

    public void removeSet(int position) {
        sets.remove(position);
        for (int i = position; i < sets.size(); i++) {
            ExerciseSet exerciseSet = sets.get(i);
            exerciseSet.setSetNumber(i + 1);
        }
    }

    public List<ExerciseSet> getSets() {
        return this.sets;
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
