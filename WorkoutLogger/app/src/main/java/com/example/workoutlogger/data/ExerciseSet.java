package com.example.workoutlogger.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a set of an exercise.
 */
public class ExerciseSet implements Parcelable {
    private int reps;
    private int weight;
    private int setNumber;
    private boolean isCompleted;

    /**
     * Creates an exercise set with the given reps, weight, and set number.
     *
     * @param reps The number of reps in the set.
     * @param weight The weight of the set.
     * @param setNumber The set number.
     */
    public ExerciseSet(int reps, int weight, int setNumber) {
        this.reps = reps;
        this.weight = weight;
        this.setNumber = setNumber;
        this.isCompleted = false;
    }


    /**
     * Empty constructor for Firestore.
     */
    public ExerciseSet() {}

    protected ExerciseSet(Parcel in) {
        reps = in.readInt();
        weight = in.readInt();
        setNumber = in.readInt();
        isCompleted = in.readBoolean();
    }

    public static final Creator<ExerciseSet> CREATOR = new Creator<>() {
        @Override
        public ExerciseSet createFromParcel(Parcel in) {
            return new ExerciseSet(in);
        }

        @Override
        public ExerciseSet[] newArray(int size) {
            return new ExerciseSet[size];
        }
    };

    /**
     * Creates a copy of the given exercise set.
     *
     * @param set The exercise set to copy.
     */
    public ExerciseSet(ExerciseSet set) {
        this.reps = set.getReps();
        this.weight = set.getWeight();
        this.setNumber = set.getSetNumber();
        this.isCompleted = set.isCompleted();
    }

    /**
     * Returns the number of reps in the set.
     * @return The number of reps in the set.
     */
    public int getReps() {
        return reps;
    }

    /**
     * Sets the number of reps in the set.
     * @param reps The number of reps in the set.
     */
    public void setReps(int reps) {
        this.reps = reps;
    }

    /**
     * Returns the weight of the set.
     * @return The weight of the set.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the weight of the set.
     * @param weight The weight of the set.
     */
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

    /**
     * Returns the set number.
     * @return The set number.
     */
    public int getSetNumber() {
        return setNumber;
    }

    /**
     * Sets the set number.
     * @param setNumber The set number.
     */
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

    /**
     * Returns whether or not the set is completed.
     * @return Whether or not the set is completed.
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Sets whether or not the set is completed.
     * @param completed Whether or not the set is completed.
     */
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
