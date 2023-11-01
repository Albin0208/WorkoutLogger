package com.example.workoutlogger.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ExerciseSet implements Parcelable {
    private int reps;
    private int weight;
    private int setNumber;
    private boolean isCompleted;

    public ExerciseSet(int reps, int weight, int setNumber) {
        this.reps = reps;
        this.weight = weight;
        this.setNumber = setNumber;
        this.isCompleted = false;
    }

    public ExerciseSet() {
    }

    protected ExerciseSet(Parcel in) {
        reps = in.readInt();
        weight = in.readInt();
        setNumber = in.readInt();
        isCompleted = in.readBoolean();
    }

    public static final Creator<ExerciseSet> CREATOR = new Creator<ExerciseSet>() {
        @Override
        public ExerciseSet createFromParcel(Parcel in) {
            return new ExerciseSet(in);
        }

        @Override
        public ExerciseSet[] newArray(int size) {
            return new ExerciseSet[size];
        }
    };

    public ExerciseSet(ExerciseSet set) {
        this.reps = set.getReps();
        this.weight = set.getWeight();
        this.setNumber = set.getSetNumber();
        this.isCompleted = set.isCompleted();
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ExerciseSet exerciseSet = (ExerciseSet) obj;

        return reps == exerciseSet.getReps() && weight == exerciseSet.getWeight() &&
                setNumber == exerciseSet.getSetNumber() && isCompleted == exerciseSet.isCompleted();
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(reps);
        parcel.writeInt(weight);
        parcel.writeInt(setNumber);
        parcel.writeBoolean(isCompleted);
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
