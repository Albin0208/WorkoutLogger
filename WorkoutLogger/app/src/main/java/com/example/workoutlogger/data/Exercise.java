package com.example.workoutlogger.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Exercise implements Parcelable {
    private String id;
    private String name;
    private List<ExerciseSet> sets;
    private boolean userCreated;

    public Exercise() {
        this.sets = new ArrayList<>();
    }

    public Exercise(String id, String name) {
        this.id = id;
        this.name = name;
        this.sets = new ArrayList<>();
        this.userCreated = false;
    }

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

    public Exercise(Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.sets = new ArrayList<>();
        for (ExerciseSet set : exercise.getSets()) {
            this.sets.add(new ExerciseSet(set));
        }
    }

    public String getName() {
        return name;
    }

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
        parcel.writeByte((byte) (userCreated ? 1 : 0));
    }

    public boolean isUserCreated() {
        return userCreated;
    }
}
