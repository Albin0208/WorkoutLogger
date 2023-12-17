package com.example.workoutlogger.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an exercise that can be performed.
 */
public class Exercise implements Parcelable {
    private String id;
    private String name;
    private List<ExerciseSet> sets;
    private boolean userCreated;

    /**
     * Creates an empty exercise.
     */
    public Exercise() {
        this.sets = new ArrayList<>();
    }

    /**
     * Creates an exercise with the given name and id.
     *
     * @param id The sets of the exercise.
     * @param name The name of the exercise.
     */
    public Exercise(String id, String name) {
        this.id = id;
        this.name = name;
        this.sets = new ArrayList<>();
        this.userCreated = false;
    }

    /**
     * Creates an exercise with the given name.
     *
     * @param name The name of the exercise.
     */
    public Exercise(String name) {
        this.name = name;
        this.sets = new ArrayList<>();
        this.userCreated = true;
    }

    protected Exercise(Parcel in) {
        id = in.readString();
        name = in.readString();
        sets = in.createTypedArrayList(ExerciseSet.CREATOR);
        userCreated = in.readByte() != 0;
    }

    public static final Creator<Exercise> CREATOR = new Creator<>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    /**
     * Creates a copy of the given exercise.
     *
     * @param exercise The exercise to copy.
     */
    public Exercise(@NonNull Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.sets = new ArrayList<>();
        this.userCreated = exercise.isUserCreated();
        for (ExerciseSet set : exercise.getSets()) {
            this.sets.add(new ExerciseSet(set));
        }
    }

    /**
     * Gets the name of the exercise.
     * @return The name of the exercise.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the exercise.
     * @param name The name of the exercise.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Exercise other = (Exercise) obj;

        // Compare based on exercise name and sets
        return Objects.equals(name, other.name) && Objects.equals(sets, other.sets) && userCreated == other.userCreated;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sets);
    }

    /**
     * Gets the id of the exercise.
     * @return The id of the exercise.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the exercise.
     * @param id The id of the exercise.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Adds a set to the exercise.
     * @param set The set to add.
     */
    public void addSet(ExerciseSet set) {
        if (this.sets == null) {
            this.sets = new ArrayList<>();
        }
        this.sets.add(set);
    }

    /**
     * Removes a set from the exercise.
     * @param position The position of the set to remove.
     */
    public void removeSet(int position) {
        sets.remove(position);
        for (int i = position; i < sets.size(); i++) {
            ExerciseSet exerciseSet = sets.get(i);
            exerciseSet.setSetNumber(i + 1);
        }
    }

    /**
     * Gets the sets of the exercise.
     * @return The sets of the exercise.
     */
    public List<ExerciseSet> getSets() {
        return this.sets;
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
        parcel.writeByte((byte) (userCreated ? 1 : 0));
    }

    /**
     * Gets whether the exercise was created by the user.
     * @return Whether the exercise was created by the user.
     */
    public boolean isUserCreated() {
        return userCreated;
    }
}
